"""Regression tests for provenance, branch context and source completeness."""
import json
import unittest
from pathlib import Path
from content_inventory import ROOT, build, digest, render
from story_analysis import analyze


class ContentInventoryTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.inventory = build()

    def test_original_bytes_unchanged(self):
        expected = json.loads((ROOT / 'Reference/original-input-sha256.json').read_text())
        for path, value in expected.items():
            self.assertEqual(digest(ROOT / path), value, path)

    def test_imports_and_placeholders_do_not_claim_gameplay(self):
        inv = self.inventory
        story = {s['file']: s for s in inv['scripts']}
        self.assertEqual(story['1044.bin']['importedDialogueLines'], 28)
        self.assertFalse(story['1044.bin']['importedDataExecuted'])
        self.assertEqual(story['1044.bin']['status'], 'partial_adaptation')
        self.assertEqual(sum(s['status'] == 'restore_required' for s in inv['skills']), 95)
        self.assertFalse(any(s['runtimeImplementedByOriginalId'] for s in inv['skills']))
        self.assertEqual({x['originalId'] for x in inv['characters'] if x['status'] == 'placeholder'}, {50})
        self.assertEqual(sum(m['role'] == 'monster' for m in inv['characters']), 50)
        map78 = next(m for m in inv['maps'] if m['originalId'] == 78)
        self.assertEqual([n['name'] for n in map78['nameEvidence']], ['土之秘境3层', '自家小院'])
        self.assertTrue(all(n['conditional'] for n in map78['nameEvidence']))

    def test_nested_branch_context_and_boundary(self):
        def row(offset, opcode, depth, args):
            return {'offset': offset, 'opcode': opcode, 'depth': depth, 'args': args}
        data = analyze([{'file': 'fixture.bin', 'instructions': [
            row(0, 59, 0, [9999, 2, 27, 0]),
            row(18, 59, 1, [1, 1, 3, 0]),
            row(36, 8, 2, [9999, 2, 28, 0]),
            row(54, 60, 2, [0, 0, 0, 0]),
            row(72, 29, 1, [1044, 0, 0, 0]),
            row(90, 60, 1, [0, 0, 0, 0]),
            row(108, 8, 0, [9999, 2, 30, 0]),
        ]}])
        self.assertEqual(data['warnings'], [])
        outer, inner = data['branches']
        self.assertEqual(inner['parentBranches'], [outer['id']])
        self.assertEqual(inner['endOffset'], 54)
        self.assertNotIn('script:fixture.bin@108', outer['instructionIds'])
        self.assertEqual(data['contextualCalls'][0]['parents'], [outer['id']])
        variable = next(v for v in data['variables'] if v['originalId'] == 9999)
        self.assertEqual(variable['writes'][0]['parents'], [outer['id'], inner['id']])
        self.assertEqual(variable['writes'][1]['parents'], [])

    def test_original_conditional_dialogue_and_incomplete_branch(self):
        _, report = render(self.inventory)
        outputs, _ = render(self.inventory)
        events = json.loads(outputs[ROOT / 'Production/Content/original-event-candidates.json'])
        parents = [b['id'] for b in events['branches'] if 'script:3032.bin@6464' in b['instructionIds']]
        self.assertEqual(parents, ['script:3032.bin@4346'])
        branch = next(b for b in events['branches'] if b['id'] == parents[0])
        self.assertEqual(branch['condition']['rawArgs'], [9999, 2, 27, 0])
        self.assertEqual(len(events['branches']), 748)
        self.assertEqual(events['warnings'][0]['instruction'], 'script:2001.bin@15799')
        self.assertEqual(report['counts']['instructions'], 6714)

    def test_rebuild_is_deterministic_and_external_cwd_independent(self):
        self.assertEqual(render(self.inventory)[0], render(build())[0])
        outputs, _ = render(self.inventory)
        for path in outputs:
            self.assertTrue(path.is_relative_to(ROOT / 'Docs/Content') or path.is_relative_to(ROOT / 'Production/Content'))


if __name__ == '__main__':
    unittest.main()
