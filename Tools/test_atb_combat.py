#!/usr/bin/env python3
"""Compile and run the production ATB rules without Unity scene/player state."""
import json
import os
import subprocess
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
EDITOR = Path(os.environ.get('UNITY_EDITOR_CONTENTS', '/Applications/Unity/Hub/Editor/6000.3.0f1/Unity.app/Contents'))
MONO_HOME = EDITOR / 'Resources/Scripting/MonoBleedingEdge'
MONO = MONO_HOME / 'bin/mono'
CSC = MONO_HOME / 'lib/mono/4.5/csc.exe'


def main():
    if not MONO.exists() or not CSC.exists():
        raise SystemExit('Unity bundled Mono compiler unavailable; set UNITY_EDITOR_CONTENTS.')
    with tempfile.TemporaryDirectory(prefix='remake-atb-') as tmp:
        folder = Path(tmp)
        harness = folder / 'Harness.cs'
        harness.write_text('''using System;
using System.IO;
using System.Web.Script.Serialization;
using MonsterRemake2D.Combat;
namespace UnityEditor { public class MenuItem : Attribute { public MenuItem(string label) {} } }
namespace UnityEngine {
 public class TextAsset { public string text; }
 public static class Resources { public static T Load<T>(string path) { throw new NotSupportedException(); } }
 public static class JsonUtility { public static T FromJson<T>(string text) { return new JavaScriptSerializer().Deserialize<T>(text); } }
 public static class Debug { public static void Log(object text) { Console.WriteLine(text); } }
}
public class Program {
 public static int Main(string[] args) {
  try { var b = new JavaScriptSerializer().Deserialize<CombatBook>(File.ReadAllText(args[0]));
    Console.WriteLine("PASS: " + AtbCombatValidation.Validate(b) + " ATB rule checks"); return 0;
  } catch (Exception e) { Console.Error.WriteLine(e); return 1; }
 }
}
''')
        exe = folder / 'AtbTests.exe'
        sources = list((ROOT / 'Unity/Assets/Remake/Scripts/Combat').glob('*.cs'))
        sources.append(ROOT / 'Unity/Assets/Remake/Editor/AtbCombatValidation.cs')
        compile_cmd = [str(MONO), str(CSC), '-nologo', '-langversion:latest', '-r:System.Web.Extensions.dll', '-out:' + str(exe)]
        subprocess.run(compile_cmd + [str(harness)] + [str(p) for p in sources], check=True, cwd=ROOT)
        result = subprocess.run([str(MONO), str(exe), str(ROOT / 'Unity/Assets/Remake/Resources/Data/atb-combat.json')], check=True, text=True, capture_output=True, cwd=ROOT)
        print(result.stdout.strip())
        out = ROOT / 'Production/Combat/atb-rules-validation.json'
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text(json.dumps({'result': 'pass', 'scope': 'Production pure C# ATB rules, not visual approval',
            'suite': result.stdout.strip(), 'command': 'python3 Tools/test_atb_combat.py'}, ensure_ascii=False, indent=2) + '\n')


if __name__ == '__main__':
    main()
