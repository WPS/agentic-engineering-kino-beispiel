# Übung 3 – Rüstzeug: Harness, Klärung, Mockup

**Nicht fertig mit Ü2?** Fallback aus `Uebungen-Artefakte.zip` kopieren:`Uebung-2/.agents/skills/annahmen-audit/`, `Uebung-2/.agents/skills/vor-der-umsetzung/`,
`Uebung-2/.agents/agents/umsetzer.md`

**Ihr lernt:** Regeln, die *abbrechen* statt bitten (Hooks mit exit 2, Deny-Regeln); einen Guard, der sagt, warum er blockiert; eine Anforderung per Interview zu
klären und ein Mockup als Klärungswerkzeug zu nutzen.

**Rahmen:** Sonnet-Klasse, Effort medium

**Ergebnis:** `.agents/hooks/quality-gate.sh`, `.claude/settings.json` (Hooks + Deny), Skill `klaeren`,
`docs/anforderungen/popcorn-klaerung.md`, `docs/mockups/popcorn.html`.


**3.1 Quality Gate als Stop-Hook**

Prompt:

```text
Schreibe ein POSIX-Shell-Skript .agents/hooks/quality-gate.sh für einen Stop-Hook:
- liest das Hook-JSON von stdin; ist "stop_hook_active" true → sofort exit 0 (keine Endlosschleife)
- läuft nur, wenn es unter $QG_PATHS Änderungen gibt (git status --porcelain)
- führt $QG_COMMAND aus; schlägt er fehl: exit 2 und auf stderr: welcher Check ($QG_NAME), die letzten
  Zeilen der Ausgabe und "Behebe die Ursache. Ändere weder Test noch Hook."
- existiert die Datei .agents/hooks/.qg-pause, gibt es nur einen Hinweis und exit 0 (Pausenschalter für Menschen)
- keine Projektbegriffe im Skript, alles kommt über Variablen
Verdrahte es in .claude/settings.json zweimal als Stop-Hook: einmal für die Architekturtests,
einmal für die Unit-Tests des Backends (Befehle aus der AGENTS.md). Setze ein ausreichendes timeout.
```

✓ **Check:** Führt das Shell-Skript für den Hook aus – das ist der Kern der Unteraufgabe:

```bash
sh -n .agents/hooks/quality-gate.sh && echo "Syntax ok"
echo '{"stop_hook_active":true}' | sh .agents/hooks/quality-gate.sh; echo "exit=$?"     # erwartet 0
echo '{}' | QG_NAME=Test QG_PATHS=. QG_COMMAND=false sh .agents/hooks/quality-gate.sh; echo "exit=$?"  # erwartet 2 (bei vorhandenen Änderungen)
touch .agents/hooks/.qg-pause    # Pausenschalter: erwartet 0 mit Hinweis "PAUSIERT" auf stderr
echo '{}' | QG_NAME=Test QG_PATHS=. QG_COMMAND=false sh .agents/hooks/quality-gate.sh; echo "exit=$?" # erwartet 0
rm .agents/hooks/.qg-pause
```

Macht eine Änderung am Code, welche die Tests umfallen lässt. Euer Hook sollte nun greifen.

Prompt:

```text
Gib der Domänenklasse Verkaufsvorgang (Kartenverkauf) ein Feld vom Typ VerkaufsvorgangEntityMapper aus dem Persistenz-Adapter. Sonst nichts. Ändere keine Tests
```

Der Pausenschalter `.agents/hooks/.qg-pause` ist für euch, nicht für den Agenten,
wenn ein roter Test bewusst als Zwischenstand stehen bleiben soll (`touch` zum Pausieren, `rm` zum Aufheben).

**3.2 Deny-Regeln für Secrets.** In `.claude/settings.json` unter `permissions.deny`: Lesen und Bearbeiten von
`.env`-Dateien und von Dateien mit `secret` im Namen.

Beispiel `.env` Datei anlegen:
```bash
echo "API_KEY=my_value" > .env
printf '\n.env\n' >> .gitignore   # .gitignore verhindert den Commit – nicht das Lesen
```

Eine Datei wie "secrets.db" oder "secrets.txt" anlegen.

✓ **Check:**
- Das Lesen der `.env` und der "secrets"-Dateien klappt nicht.
- **Deny gilt nicht für Shell-Zugriffe.** Der Guard ist nur eine Musterliste. Echte Grenze ist erst eine Sandbox ohne Secrets.
- **Pfadsyntax der Deny-Regeln prüfen:** gitignore-Syntax, `./` relativ zum Arbeitsverzeichnis, Groß-/Kleinschreibung
  zählt. Mit `backend/.env` testen.
- **`stop_hook_active`:** Nach einer Blockade lässt das Gate den nächsten Stopp durch (sonst Endlosschleife).
  Verlässlich wird es erst mit Pre-Commit und CI.
- **Windows:** Die Hooks brauchen `sh` (Git Bash).

**3.3 Skill `klaeren` bauen lassen** – `.agents/skills/klaeren/SKILL.md`:

Prompt:
```text
- Noch keinen Code schreiben. Eine Frage nach der anderen (max 10 Fragen), auf die Antwort warten.
- Nach allem fragen, was sonst angenommen werden müsste – die Kategorien kommen aus der @annahmen-tabelle.md
- Den Ist-Prozess aus Code und vorhandenen Diagrammen selbst erheben.
- „Wissen wir nicht“ wird zur offenen Frage, nicht zur Entscheidung.
- Ergebnis nach `docs/anforderungen/<thema>-klaerung.md`, immer mit vier Abschnitten:
  **1 Entschieden · 2 Offene Fragen an den Fachbereich · 3 Ist/Soll mit Lücken und Chancen ·
  4 Vertikale Slices, priorisiert nach Abhängigkeiten.**
```

**3.4 Interview** `/klaeren` Skill aufrufen für eine neue Anforderung: Gäste sollen Popcorn mitbestellen können.
Ihr dürft in manche Fälle „Wissen wir noch nicht.“ beantworten.

Prompt:
```text
/klaeren Beim Ticketkauf sollen Gäste Popcorn mitbestellen können.
```

✓ **Check:** Die Slices sind definiert und jede Slice ist klein genug für einen Commit.


**3.5 Mockup**

Prompt:
```text
Erzeuge aus docs/anforderungen/popcorn-klaerung.md ein klickbares Wegwerf-Mockup
docs/mockups/popcorn.html (eine Datei, kein Build, kein Framework).
Markiere jede offene Frage sichtbar an der Stelle im Ablauf, an der sie auftritt.
```

**Backup**: Ihr dürft das fertige Design aus der `Uebungen-Artefakte.zip` kopieren.

```bash
cp -r ~/kino-artefakte/Uebungen-Artefakte/Uebung-3/design_handoff_popcorn_upsell docs/
echo 'docs/design_handoff_popcorn_upsell/' >> .git/info/exclude   # bleibt lokal, kein Commit
```

✓ **Check:** Das Mockup hat geholfen mindestens weitere Fragen oder Entscheidungen offen zu legen.

Alles auf `main` committen.

### Selbstcheck
- Hat der Hook wirklich abgebrochen (exit 2), oder nur gemeldet?
- Hat der Agent den provozierten Verstoß selbst behoben? Wie viele Runden?
- Ging der Umweg über die Shell an der Deny-Regel vorbei?
- Welche Fragen hat der Agent gestellt, auf die ihr allein nicht gekommen wärt?
- Was hat das Mockup sichtbar gemacht, was die Klärung nicht hatte?

### Bonus
- **Guard, der begründet:** `.agents/hooks/secret-guard.sh` als PreToolUse-Hook, der auch Shell-Aufrufe auf
  Secret-Dateien mit exit 2 blockiert und *erklärt*, was der Agent stattdessen tun soll.
- **Dieselbe Prüfung als CI-Gate:** `quality-gate.sh` als Git-Pre-Commit-Hook bzw. Pipeline-Schritt skizzieren.
- **Rückfluss:** Was Mockup und Design-Vergleich geklärt haben, als Entscheidungen (mit Quelle) in die Klärung
  übernehmen.
