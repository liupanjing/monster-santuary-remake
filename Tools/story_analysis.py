"""Recover conditional event candidates and original variable dependencies.

This is a static development tool, not an interpreter or an invented plot order.
Opcode semantics cite Reference/OriginalGame/decompiled/t.java.
"""
import collections

COMPARISONS = {0: '>', 1: '<', 2: '=='}
WRITES = {0: '+=', 1: '-=', 2: '='}


def analyze(scripts):
    branches = []
    variable_uses = collections.defaultdict(lambda: {'reads': [], 'writes': []})
    warnings = []
    annotated_calls = []
    for script in scripts:
        name = script['file']
        rows = script['instructions']
        active = []
        for ins in rows:
            depth = ins['depth']; op = ins['opcode']; args = ins['args']
            active = [b for b in active if b['bodyDepth'] <= depth]
            uid = f'script:{name}@{ins["offset"]}'
            if op == 60:
                closing = [b for b in active if b['bodyDepth'] == depth]
                if closing:
                    closing[-1]['endOffset'] = ins['offset']
                else:
                    warnings.append({'instruction': uid, 'reason': 'No matching static branch at closing depth'})
                active = [b for b in active if b['bodyDepth'] < depth]
                continue
            context = [b['id'] for b in active]
            for b in active:
                b['instructionIds'].append(uid)
            if op == 59:
                # t.a(f,int): negative selectors have handler-specific semantics.
                selector = f'flag:{args[0]}' if args[0] >= 0 else {-1: 'w.e', -2: 'actorIndex', -3: 'w.d_with_possible_side_effect'}.get(args[0], 'unknown_negative_selector')
                b = {'id': uid, 'script': name, 'offset': ins['offset'], 'depth': depth,
                     'bodyDepth': depth + 1, 'parentBranches': context,
                     'condition': {'selector': selector, 'rawArgs': args,
                                   'comparison': COMPARISONS.get(args[1], 'unknown'), 'value': args[2]},
                     'instructionIds': [], 'endOffset': None,
                     'implementationStatus': 'restore_required', 'source': 't.java opcode59 handler and comparator'}
                active.append(b); branches.append(b)
                if args[0] >= 0:
                    variable_uses[args[0]]['reads'].append({'instruction': uid, 'operation': COMPARISONS.get(args[1]), 'value': args[2], 'parents': context})
            if op == 8 and args[0] >= 0:
                variable_uses[args[0]]['writes'].append({'instruction': uid, 'operation': WRITES.get(args[1], 'unknown'), 'value': args[2], 'parents': context})
            if op == 29 and args[0] >= 0:
                annotated_calls.append({'instruction': uid, 'target': f'{args[0]}.bin', 'parents': context, 'kind': 'opcode29_unconditional_call_within_context'})
        for b in active:
            if b['endOffset'] is None:
                warnings.append({'instruction': b['id'], 'reason': 'Branch has no explicit close before source file ends'})
    transitions = []
    for key, uses in sorted(variable_uses.items()):
        for write in uses['writes']:
            if write['operation'] != '=':
                continue
            matches = [r for r in uses['reads'] if r['operation'] == '==' and r['value'] == write['value']]
            if matches:
                transitions.append({'variable': key, 'value': write['value'], 'write': write['instruction'],
                                    'candidateReaders': [r['instruction'] for r in matches],
                                    'meaning': 'shared variable value; NOT proof of chronological reachability'})
    return {'scope': 'Static branch context and variable dependencies; gameplay conditions and full chronology require review',
            'branches': branches,
            'variables': [{'originalId': k, **v, 'semanticName': None} for k, v in sorted(variable_uses.items())],
            'candidateStateTransitions': transitions, 'contextualCalls': annotated_calls,
            'warnings': warnings}
