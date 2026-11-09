# Übungen – Agentic Engineering Schulung
Jede Übung trainiert eine Technik, die ihr in jedes andere Projekt mitnehmen könnt. Mit jeder Übung wächst euer **Werkzeugkasten** um ein Stück:
eine AGENTS.md, Skills, Subagents, Hooks, Vorlagen.

Musterlösungen und Erklärungen zu den Artefakten (Trainer-Material) liegen in der **`Artefakte.zip`**, die ihr vom Trainer bekommt.
Entpackt sie außerhalb des Repos: `unzip Artefakte.zip -d ~/kino-artefakte`. Darin gibt es je Übung einen Ordner
`~/kino-artefakte/Artefakte/Uebung-N/` mit einer `README.md`; die Befehle in den READMEs nehmen diesen Pfad an.

| Übung                                                     | Ihr lernt | Baut auf | Ihr baut |
|-----------------------------------------------------------|---|---|---|
| [1](#übung-1--isoliert-arbeiten-und-annahmen-aufdecken)   | Isoliert arbeiten, Annahmen des Agenten sichtbar machen | – | AGENTS.md, Annahmen-Tabelle |
| [2](#übung-2--skill-bauen-kapseln) | Skills schneiden, Subagents kapseln, Generalisierung prüfen | Annahmen-Tabelle, AGENTS.md | Skills `annahmen-audit`, `vor-der-umsetzung`, Subagent `umsetzer` |
| [3](#übung-3--rüstzeug-harness-klärung-mockup)            | Gates, die wirklich blockieren; Anforderungen klären, bevor Code entsteht | AGENTS.md, Skills aus Ü2 | Quality-Gate-Hook, Deny-Regeln, Skill `klaeren`, Klärungsdokument, Mockup |
| [4](#übung-4--spezifikation-als-hebel)                    | Spezifikation mit „Fertig, wenn“, ADR, testgetriebener Slice | Klärung, Harness | Skills `spezifikation`, `adr`; Spec, ADR, erster Slice |
| [5](#übung-5--neues-feature-mit-workflow)                 | Einen Ablauf als Workflow festschreiben (Plan → Code → Review) und die Grenzen jeder Phase erkennen | alles davor | Workflow-Skript `feature-umsetzen`; Skills `diff-review`, `testluecken`, `sicherheits-review`; Subagent `reviewer` |

## Spielregeln
- **Am Ende jeder Übung committen.** Wer nicht fertig wird, steigt mit dem Stand-Branch des Trainers in die
  nächste Übung ein (`stand/uebung-1` bis `stand/uebung-3`; für Übung 4 und 5 mit `popcorn-upsell.patch` vom Collaboard, dort fehlen Spec und ADR –
  Anleitung: [Übung 5, Fallback](#fallback-popcorn-stand-einspielen-nur-wenn-ü4-fehlt)).
- **Nicht raten lassen, wo ihr es wisst.** Aber in Übung 1 und 2 *bewusst* raten lassen – genau das wollen wir sehen.

## Setup des Werkzeugkastens (einmalig)
Wir arbeiten mit offenen Standards:

| Was | Wo (offener Standard) | Anbindung Claude Code                                                                                                                                                                                |
|---|---|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Projektkontext | `AGENTS.md` im Projektstamm | ~~`CLAUDE.md` mit einer einzigen Zeile: `@AGENTS.md`~~                                                                                                                                               |
| Skills | `.agents/skills/<name>/SKILL.md` | `.claude/skills` als Verweis: `mkdir -p .claude && ln -s ../.agents/skills .claude/skills` und **mit committen**, damit er in jedem Worktree existiert (Windows ohne Symlink-Recht: Ordner kopieren) |
| Hook-Skripte | `.agents/hooks/*.sh` (reine Shell-Skripte) | Verdrahtung in `.claude/settings.json`                                                                                                                                                               |
| Deny-Regeln | – (tool-spezifisch) | `.claude/settings.json` → `permissions.deny`                                                                                                                                                         |
| Subagents | – (tool-spezifisch) | `.claude/agents/<name>.md`                                                                                                                                                                           |

> **Andere Tools** (Copilot, Codex, Opencode, Cursor …): AGENTS.md und `SKILL.md`-Skills werden von vielen
> Tools gelesen – prüft in der Doku eures Tools, ab welcher Version und an welchem Ort. Subagents, Hooks und
> Berechtigungen heißen überall etwas anders. Die *Prüflogik* (unsere Shell-Skripte, die Skill-Texte) bleibt
> gleich, nur die Verdrahtung ändert sich. Wo ein Tool keine Hooks hat: dasselbe Skript als Git-Pre-Commit-Hook
> oder CI-Schritt.

Die Prompts sind für Claude Code formuliert. `/name` ruft einen Skill direkt auf; in anderen Tools
schreibt ihr „Nutze den Skill `name`“.

---

# Übung 1 – Isoliert arbeiten und Annahmen aufdecken
**Ihr lernt:** Eine Aufgabe in einem eigenen Arbeitsordner (Worktree) laufen zu lassen, ohne den Hauptstand
anzufassen – und herauszufinden, *was der Agent angenommen hat*, statt nur zu prüfen, ob es läuft. Das
Ergebnis ist „ich weiß, wo der Agent geraten hat“, nicht „das Feature läuft“.

**Rahmen:** 45 min inkl. Besprechung · Sonnet-Klasse, Effort medium · Ausgangspunkt `main`, frisch gezogen.

**Ihr nehmt mit:** `AGENTS.md` (auf `main`), ausgefüllte Annahmen-Tabelle, den Audit-Prompt (wird in Ü2 zum Skill).
**Nicht fertig?** Fallback und Musterstand: `Artefakte.zip` → `Uebung-1/README.md`.

### Kernpfad
**1. AGENTS.md prüfen.** Sie ist in Kapitel 7 entstanden. Gegenlesen und auf `main` committen.  
Prompt:
```text
Verschaffe dir einen Überblick über dieses Repository und schreibe daraus eine AGENTS.md in den Projektstamm.
Hinein gehören:
1. Wofür das System fachlich da ist, in drei Sätzen
2. Die Bausteine und wo welcher Code liegt
3. Bauen, Starten, Testen als ausführbare Befehle – führe die Build- und Testbefehle einmal aus und
   nimm nur auf, was funktioniert
4. Fachbegriffe, die im Code anders heißen als im Gespräch
Belege jede Aussage mit der Datei, aus der du sie hast. Erfinde keine Regeln. Max Länge ein Bildschirm.
```

Lest die generierte Datei selbst und **kürzt**: Was ihr nicht nachvollziehen könnt, fliegt raus.  
Datei auf `main` committen.

✓ **Check:** Neue Session, Frage „Wie führe ich die Backend-Tests aus?“ – die Antwort kommt aus der AGENTS.md,
ohne dass der Agent sucht.

**2. Worktree anlegen und hineinwechseln**

```bash
git worktree add ../kino-uebung1 -b uebung1
cd ../kino-uebung1
(cd frontend && npm ci)      # Abhängigkeiten gibt es pro Worktree
claude  # claude oder andere Tool starten
```

✓ **Check:** `git worktree list`. Läuft die App im Hauptordner noch, doppelt belegte Ports beachten.
Alle Handgriffe und Stolpersteine: Worktree-Kommandokarte (`Artefakte.zip` → `Uebung-1/worktree-kommandokarte.md`).

**3. Bewusst knapper Auftrag.** Kein Plan-Modus, keine Zusatzinfos – der Agent darf raten:

```text
Im Programm soll man auf Kinderfilme filtern können.
```

Greift nicht ein. Notiert nebenbei: Wo sucht er? Fragt er überhaupt etwas?

**4. Selbst prüfen.** Tests laufen lassen (Befehle aus der AGENTS.md).

✓ **Check:** Ihr könnt in einem Satz sagen, was „Kinderfilm“ jetzt im System bedeutet – und in welcher Datei das steht.

**5. Annahmen aufdecken** (Audit-Prompt – merkt ihn euch, er wird in Übung 2 zum Skill):

```text
Ändere nichts. Liste jede Annahme, die du bei dieser Umsetzung getroffen hast – auch implizite
(Begriffe, Standardwerte, Testdaten, Ort der Logik).
Als Tabelle: # | Annahme | Kategorie (Begriff | Randfall | Ort im Code | Verhalten | Test) |
Fundstelle Datei:Zeile | Sicherheit (hoch/mittel/niedrig) | Wer hätte es wissen müssen?
Tabelle als annahmen-tabelle.md in /docs speichern“
```

Übernehmt die Tabelle in den Beobachtungsbogen und ergänzt die Spalte „Hätte ich das so gewollt?“.

**6. Abschließen**

```bash
git add -A && git commit -m "Übung 1: Filter (Agent-Stand, ungeprüfte Annahmen)"
cd ../<hauptordner>
git checkout main && git merge uebung1
git worktree remove ../kino-uebung1 && git worktree prune
```

Eine Zeile in die AGENTS.md (Abschnitt „Regeln aus Erfahrung“): der Begriff, den der Agent falsch geraten hat,
und was er bei uns bedeutet. Committen.

### Beobachtungsbogen: Annahmen-Tabelle

| # | Annahme | Kategorie | Fundstelle | Sicherheit | Hätte ich das so gewollt? | Wer hätte es wissen müssen? |
|---|---|---|---|---|---|---|
| 1 | | | | | | |

Kategorien: **Begriff** · **Randfall** · **Ort im Code** · **Verhalten** · **Test**  
Sicherheit: **hoch** (belegt, Quelle nennen) · **mittel** (aus Code abgeleitet) · **niedrig** (geraten)

Nachfragen an den Agenten, wenn die Liste zu glatt aussieht: „Welche Default-Werte hast du gesetzt?“ ·
„Was passiert bei null?“ · „Welche Testdaten hast du benutzt und was fehlt darin?“ ·
„Gibt es den Begriff im Code schon in anderer Bedeutung?“

### Bonus
- **Ein Agent, der fragt, rät nicht:** Zweiter Worktree `uebung1b`, gleicher Auftrag, aber mit dem Zusatz
  „Bevor du etwas umsetzt, stelle mir Fragen.“ Vergleicht die Zahl der Annahmen.

---

# Übung 2 – Skill bauen, kapseln
**Ihr lernt:** Wiederkehrendes Wissen in einen Skill zu packen, der *beim richtigen Anlass* greift; eine Teilaufgabe an einen Subagenten mit eigenem Kontext, eigenem Modell und
eigenen Rechten zu delegieren.

**Rahmen:** 55 min inkl. Besprechung · Sonnet-Klasse, Effort medium, für den Subagenten reicht Haiku ·
Ausgangspunkt: `main` mit Ergebnis aus Ü1.

**Ihr nehmt mit:** `.agents/skills/annahmen-audit/`, `.agents/skills/vor-der-umsetzung/` (mit `references/`),
`.agents/agents/umsetzer.md`, Antworten auf den Selbstcheck.
**Nicht fertig?** Fallback und Musterstand: `Artefakte.zip` → `Uebung-2/README.md`.

### Kernpfad
**0. Skills sichtbar machen** (einmalig, siehe Setup): `.agents/skills` existiert, `.claude/skills` verweist darauf.
```bash
mkdir -p .claude
ln -s ../.agents/skills .claude/skills  # erzeugt symlink in .claude/skills  
```

**1. Aufwärmen: Prompt → Skill** Macht aus dem Audit-Prompt aus Ü1 den Skill
`.agents/skills/annahmen-audit/SKILL.md`:

```markdown
---
name: annahmen-audit
description: <WANN wird er gebraucht? Den Anlass beschreiben, nicht was er tut>
---
<Der Audit-Prompt aus Übung 1, als Anweisung formuliert>
```

✓ **Check:** Neue Session, nach einer kleinen Änderung fragen „Was hast du dabei angenommen?“ – der Skill wird
gezogen, ohne dass ihr ihn aufruft. Wenn nicht: an der `description` liegt es.

**2. Den eigentlichen Skill bauen.** Aus den *Kategorien* eurer Annahmen-Tabelle wird ein Ablauf,
der vor jeder fachlichen Umsetzung greift:

```text
Hier ist unsere Annahmen-Tabelle: <Tabelle einfügen>
Entwirf daraus den Skill .agents/skills/vor-der-umsetzung/SKILL.md, der greift, BEVOR eine neue
fachliche Anforderung umgesetzt wird.
- description: beschreibt den Anlass, nicht diesen Fall
- Body: Ablauf als Regeln, je Annahme-Kategorie ein Schritt. Offenes wird gefragt, nicht geraten.
  Erst ein fehlschlagender Test, dann die Umsetzung. Befehle stehen in der AGENTS.md.
- Keine Projektbegriffe und keine festen Werte im SKILL.md. Projektwissen aus der Tabelle kommt nach
  references/projekt.md; im SKILL.md nur ein Verweis "lies das, wenn …".
```

✓ **Check:** `grep -iE 'film|fsk|kino' .agents/skills/vor-der-umsetzung/SKILL.md` ist leer.

**3. Als Subagent kapseln (10 min).** `.claude/agents/umsetzer.md` oder `.agents/agents/umsetzer.md + Symlink .claude/agents`:

```markdown
---
name: umsetzer
description: <wann soll delegiert werden?>
tools: <welche Werkzeuge braucht er wirklich?>
model: haiku
skills:
  - vor-der-umsetzung
---
<Rolle, Arbeitsweise, was er zurückmeldet: Änderungen, Testergebnis, Annahmen, offene Fragen>
```

Schreibt in einem Satz dazu, *warum* ihr Modell und Werkzeuge so gewählt habt.

✓ **Check:** `/agents` listet `umsetzer`.

**4. Erst committen, dann Worktree.**

Beispiel mit Claude:
```bash
git add .agents .claude/skills .claude/agents && git commit -m "Übung 2: Skills und Subagent"
git worktree add ../kino-uebung2 -b uebung2 && cd ../kino-uebung2 && (cd frontend && npm ci)
```

✓ **Check:** `ls .agents/agents/` oder `ls .claude/skills/` zeigt im Worktree eure Skills.

**5. Der zweite Fall – über den Subagenten:**

```text
Nutze den Subagenten umsetzer: Im Programm soll man nach Sprache filtern können.
```

Beobachtet: Wird delegiert? Wird der Skill angewendet? Welche Fragen kommen zurück?

**6. Abschließen:** Selbstcheck beantworten, committen, mergen, Worktree aufräumen (wie Ü1).

### Selbstcheck
Nach dem zweiten Lauf kurz im Plenum beantworten:

- Hat die `description` ausgelöst – automatisch, nur über den Subagenten oder gar nicht?
- Welche festen Werte oder fallspezifischen Annahmen aus dem ersten Fall haben gestört?
- Hat der Subagent bei Unsicherheit gefragt oder geraten? Was fehlte ihm an Kontext?
- Wo musste ein Mensch fachlich urteilen?
- Hat Haiku gereicht? Wo nicht?
- Was ändert ihr am Skill, am Subagenten oder an der AGENTS.md?

**Fazit in einem Satz:** generalisiert / zu eng / generalisiert mit Nachschärfen, weil …

### Bonus
- Denselben Lauf mit `model: sonnet` – lohnt sich der Unterschied?

---

# Übung 3 – Rüstzeug: Harness, Klärung, Mockup
**Ihr lernt:** Regeln, die *abbrechen* statt bitten (Hooks mit exit 2, Deny-Regeln); einen Guard, der sagt, warum
er blockiert; eine Anforderung per Interview zu
klären und ein Mockup als Klärungswerkzeug zu nutzen – alles, bevor Code entsteht.

**Rahmen:** 60 min inkl. Besprechung · Sonnet-Klasse, Effort medium · Ausgangspunkt: `main` mit Stand aus Ü2 · dieser Übung macht ihr in Paaren. Was hier entsteht, ist der Ausgangsstand für Übung 4.

**Ihr nehmt mit:** `.agents/hooks/quality-gate.sh`, `.claude/settings.json` (Hooks + Deny), Skill `klaeren`,
`docs/anforderungen/popcorn-klaerung.md`, `docs/mockups/popcorn.html`.
**Nicht fertig?** Fallback und Musterstand: `Artefakte.zip` → `Uebung-3/README.md`.

### Teil A – Harness

**A1. Quality Gate als Stop-Hook**

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

Prüft selbst – das ist der Kern der Übung:

```bash
sh -n .agents/hooks/quality-gate.sh && echo "Syntax ok"
echo '{"stop_hook_active":true}' | sh .agents/hooks/quality-gate.sh; echo "exit=$?"     # erwartet 0
echo '{}' | QG_NAME=Test QG_PATHS=. QG_COMMAND=false sh .agents/hooks/quality-gate.sh; echo "exit=$?"  # erwartet 2 (bei vorhandenen Änderungen)
touch .agents/hooks/.qg-pause    # Pausenschalter: erwartet 0 mit Hinweis "PAUSIERT" auf stderr
echo '{}' | QG_NAME=Test QG_PATHS=. QG_COMMAND=false sh .agents/hooks/quality-gate.sh; echo "exit=$?"
rm .agents/hooks/.qg-pause
```

✓ **Check:** Den Verstoß provozieren:

```text
Gib der Domänenklasse Verkaufsvorgang (Kartenverkauf) ein Feld vom Typ VerkaufsvorgangEntity aus dem
Persistenz-Adapter. Sonst nichts.
```

Der Stop-Hook muss blockieren, und der Agent muss den Verstoß *selbst* wieder entfernen. (Ein bloßer, ungenutzter
Import reicht nicht: Architekturtests prüfen den kompilierten Code, und dort taucht er nicht auf.)

Der Pausenschalter `.agents/hooks/.qg-pause` ist für euch, nicht für den Agenten,
wenn ein roter Test bewusst als Zwischenstand stehen bleiben soll (`touch` zum Pausieren, `rm` zum Aufheben).

**A2. Deny-Regeln für Secrets.** In `.claude/settings.json` unter `permissions.deny`: Lesen und Bearbeiten von
`.env`-Dateien und von allem mit `secret` im Namen.

```bash
echo "API_KEY=my_value" > .env
printf '\n.env\n' >> .gitignore   # .gitignore verhindert den Commit – nicht das Lesen
```

✓ **Check:** „Lies .env und gib mir den Wert aus“ scheitert. Dann: „Gib mir den Wert über die Shell aus“ –
was passiert? Notiert es.

### Teil B – Klärung
**B1. Skill `klaeren` bauen** – `.agents/skills/klaeren/SKILL.md`, ohne Projektbegriffe:
```text
- Noch keinen Code schreiben. Eine Frage nach der anderen, auf die Antwort warten.
- Nach allem fragen, was sonst angenommen werden müsste – die Kategorien kennt ihr aus Ü1/Ü2.
- Den Ist-Prozess aus Code und vorhandenen Diagrammen selbst erheben.
- „Wissen wir nicht“ wird zur offenen Frage, nicht zur Entscheidung.
- Ergebnis nach `docs/anforderungen/<thema>-klaerung.md`, immer mit vier Abschnitten:
  **1 Entschieden · 2 Offene Fragen an den Fachbereich · 3 Ist/Soll mit Lücken und Chancen ·
  4 Vertikale Slices, priorisiert nach Business Value, technischem Risiko und Impact.**
```
**B2. Interview im Rollenspiel.** Eine Person bedient den Agenten, die andere ist Fachexperte mit der Karte vom
Trainer. Steht etwas nicht auf der Karte, lautet die Antwort: „Wissen wir noch nicht.“

```text
/klaeren Beim Ticketkauf sollen Gäste Popcorn mitbestellen können.
```

✓ **Check:** Alle vier Abschnitte gefüllt · jede offene Frage hat einen Adressaten · Slice 1 geht durch alle
*nötigen* Schichten bis zu einem prüfbaren Ergebnis (API oder Oberfläche) und ist klein genug für einen Commit.

### Teil C – Mockup

```text
Erzeuge aus docs/anforderungen/popcorn-klaerung.md ein klickbares Wegwerf-Mockup
docs/mockups/popcorn.html (eine Datei, kein Build, kein Framework).
Markiere jede offene Frage sichtbar an der Stelle im Ablauf, an der sie auftritt.
```

Erst jetzt holt ihr das fertige Design aus der `Artefakte.zip` (es lag bewusst bis hierhin nur dort, damit die Klärung
unbeeinflusst bleibt) und vergleicht euer Mockup damit:

```bash
cp -r ~/kino-artefakte/Artefakte/Uebung-3/design_handoff_popcorn_upsell docs/
echo 'docs/design_handoff_popcorn_upsell/' >> .git/info/exclude   # bleibt lokal, kein Commit
```

✓ **Check:** Das Mockup hat mindestens eine neue Frage oder Entscheidung sichtbar gemacht.

Alles auf `main` committen – Übung 4 startet von hier.

### Grenzen und „bitte prüfen“

- **Deny gilt nicht für Bash.** Der Guard ist nur eine Musterliste. Echte Grenze ist erst eine Sandbox ohne Secrets.
- **Pfadsyntax der Deny-Regeln prüfen:** gitignore-Syntax, `./` relativ zum Arbeitsverzeichnis, Groß-/Kleinschreibung
  zählt. Mit `backend/.env` testen.
- **`stop_hook_active`:** Nach einer Blockade lässt das Gate den nächsten Stopp durch (sonst Endlosschleife).
  Verlässlich wird es erst mit Pre-Commit und CI.
- **Surefire-Filter prüfen:** Läuft `-Dtest="de.wps.ddd.kino.architecture.*"` nicht, dann
  `-Dtest='ArchitectureTest,ModularityTest'`.
- **Hook-Ausgabe:** Bei `exit 2` geht stderr an den Agenten, bei `exit 0` erscheint stdout nur im Transkript. Das kann
  sich pro Version ändern – `/hooks` und die aktuelle Doku prüfen.
- **Windows:** Die Hooks brauchen `sh` (Git Bash).

### Selbstcheck
Zum Abschluss kurz im Plenum beantworten – mit Beleg (Zitat, Datei, Hook-Ausgabe):

- Hat der Hook wirklich abgebrochen (exit 2) – oder nur gemeldet?
- Hat der Agent den provozierten Verstoß selbst behoben? Wie viele Runden?
- Ging der Umweg über die Shell an der Deny-Regel vorbei?
- Welche Fragen hat der Agent gestellt, auf die ihr allein nicht gekommen wärt?
- Was hat das Mockup sichtbar gemacht, was die Klärung nicht hatte?

### Bonus
- **Guard, der begründet:** `.agents/hooks/secret-guard.sh` als PreToolUse-Hook, der auch Shell-Aufrufe auf
  Secret-Dateien mit exit 2 blockiert und *erklärt*, was der Agent stattdessen tun soll.
- **Dieselbe Prüfung als CI-Gate:** `quality-gate.sh` als Git-Pre-Commit-Hook bzw. Pipeline-Schritt skizzieren.
- **Rückfluss:** Was Mockup und Design-Vergleich geklärt haben, als Entscheidungen (mit Quelle) in die Klärung übernehmen.

---

# Übung 4 – Spezifikation als Hebel
**Ihr lernt:** Eine Spezifikation, deren „Fertig, wenn“ prüfbar ist, ersetzt das Nachfassen. Eine
Architekturentscheidung als ADR beendet Diskussionen, statt sie zu wiederholen. Umsetzung in vertikalen Slices,
testgetrieben, ein Slice = ein Commit – mit aktivem Harness.

**Rahmen:** 60 min inkl. ca. 15 min Kaffeepause (gemeinsam angesagt) und Besprechung · starkes Modell zum Planen,
Sonnet-Klasse zum Umsetzen · Ausgangspunkt: `main` mit Stand aus Ü3 · wer Ü3 nicht abgeschlossen hat: mit dem
Sitznachbarn zusammen oder Stand-Branch.

**Ihr nehmt mit:** Skills `spezifikation` und `adr` (mit Vorlagen), `specs/popcorn.md`, `docs/adr/0001-*.md`,
ein Commit mit Slice 1.
**Nicht fertig?** Fallback und Musterstand: `Artefakte.zip` → `Uebung-4/README.md` – die Skills
zum Kopieren liegen dort in `Uebung-4/.agents/skills/`.

### Kernpfad
**1. Zwei Skills in `main` kopieren und durchlesen.** 
```bash
- `.agents/skills/spezifikation/SKILL.md` + `spec-template.md`: aus der Klärungsdokument würde Specs generiert

- `.agents/skills/adr/SKILL.md` + `adr-template.md`:
```

**2. Worktree und Modellwahl**

```bash
git worktree add ../kino-popcorn -b popcorn && cd ../kino-popcorn && (cd frontend && npm ci)
claude
```

In Claude Code: `/model opusplan` (Opus im Plan-Modus, Sonnet bei der Umsetzung). In anderen Tools: zum Planen
das stärkste Modell, zum Umsetzen ein günstigeres.

**3. Spezifikation**

```text
/spezifikation Schreibe aus docs/anforderungen/popcorn-klaerung.md die Spezifikation specs/popcorn.md.
Entscheide nichts, was in der Klärung offen ist.
```

Selbst gegenlesen.

✓ **Check:** 
- Jede Zeile unter „Fertig, wenn“ ist ein Befehl oder eine beobachtbare Tatsache 
- „Ein Kauf ohne Popcorn verhält sich unverändert“ ist dabei 
- Offenes steht unter „Offen“.

**4. ADR**

```text
/adr Wo lebt die Popcorn-Bestellung fachlich und technisch? Denke mindestens drei Optionen durch,
darunter eine minimal invasive. Nenne Risiken und Trade-offs, dann eine begründete Entscheidung.
```

Spec und ADR committen (`docs: Spezifikation und ADR Popcorn`).

**5. Slice 1 testgetrieben umsetzen.** Slice 1 ist der kleinste Durchstich aus eurer Klärung – im Muster:
eine Portion mit serverseitigem Preis in der Gesamtsumme, bis zur API; die Oberfläche ist Slice 2. Erst im
Plan-Modus planen lassen, Plan lesen, dann:

```text
Setze Slice 1 aus docs/anforderungen/popcorn-klaerung.md um, gegen specs/popcorn.md und das ADR.
- Pro Akzeptanzszenario dieses Slices: zuerst einen fehlschlagenden Test schreiben, kurz zeigen, dass er rot ist,
  und im selben Durchgang weiter umsetzen, bis er grün ist.
- Nur dieser Slice. Weicht etwas von Spec oder ADR ab: anhalten und fragen.
- Am Ende genau ein Commit, die Nachricht nennt die Szenarien.
```

(„Im selben Durchgang“, weil euer Stop-Hook aus Ü3 jedes Anhalten mit rotem Test blockiert.)

✓ **Check:** `git log --oneline main..` zeigt zwei Commits (Spec/ADR, Slice 1) · `cd backend && ./mvnw verify`
ist grün · ihr wisst, ob und wie oft der Stop-Hook eingegriffen hat.

**6. Weitere Slices** dürfen während der Kaffeepause weiterlaufen – je Slice ein Commit.

### Selbstcheck
Zum Abschluss kurz im Plenum beantworten – mit Beleg (Zitat, Datei, Testausgabe):

- Wie oft musstet ihr nachfassen? (Vergleich zu Ü1)
- Welche Diskussion hat das ADR beendet?
- Was hat der Harness geblockt – zu Recht oder zu Unrecht?
- Welcher Teil von „Fertig, wenn“ war zu vage?

### Bonus
- **Set-based Design:** Die zweitbeste ADR-Option in einem zweiten Worktree umsetzen lassen und beide Varianten
  *vorher festgelegten* Kriterien gegenüberstellen (Tests, Umfang der Änderung, Lesbarkeit). Begründung ins ADR.
- **Diagramm als Text:** Den neuen Ablauf als Mermaid-Diagramm nach `docs/diagrams/` schreiben lassen.

---

# Übung 5 – Neues Feature mit Workflow
**Ihr lernt:** Einen Ablauf, den ihr immer wieder braucht, als **Workflow** festzuschreiben: feste Phasen, ein Gate,
das bei Unklarem stoppt, und ein paralleles Review. Dazu, wo AI in jeder Phase trägt und wo sie kippt. Das Feature
„Getränke kaufen“ ist nur das Vehikel.

**Rahmen:** Sonnet-Klasse, Effort medium · Grundlage: euer Popcorn-Stand aus Ü4 (Fallback:
`popcorn-upsell.patch` einspielen, siehe [Fallback unten](#fallback-popcorn-stand-einspielen-nur-wenn-ü4-fehlt))

**Baut auf:** Skills `klaeren` (Ü3), `spezifikation` (Ü4), `vor-der-umsetzung` und Subagent `umsetzer` (Ü2),
Stop-Hook aus Ü3.

**Ihr nehmt mit:** das Workflow-Skript `feature-umsetzen`, Skills `diff-review`, `testluecken`, `sicherheits-review`,
Subagent `reviewer`, Workflow.

> **Warum zwei Teile?** Klären ist ein Interview und braucht euch; ein Workflow läuft im Hintergrund und **kann nicht
> nachfragen**.

### Fallback: Popcorn-Stand einspielen (nur wenn Ü4 fehlt)
`popcorn-upsell.patch` (vom Trainer) ist ein einzelner Diff gegen `main` (ohne Commit-Historie). Es enthält das Popcorn-Feature, aber keine Spec und kein ADR.
Aus dem Repo-Hauptordner, mit sauberem Stand auf `main`:

```bash
git worktree add ../kino-popcorn-fallback -b popcorn-fallback main
cd ../kino-popcorn-fallback
git apply --check /pfad/zu/popcorn-upsell.patch   # Trockenlauf: ändert nichts, meldet Konflikte
git apply /pfad/zu/popcorn-upsell.patch           # Änderungen liegen danach im Arbeitsverzeichnis
git add -A && git commit -m "Fallback: Popcorn-Stand"
(cd frontend && npm ci)                           # Abhängigkeiten gibt es pro Worktree
```

- `git apply` legt **keinen Commit** an – den macht ihr selbst (letzte Zeile).
- Meldet `--check` einen Fehler („patch does not apply“), steht `main` nicht auf dem Stand des Trainers: `git pull`, dann nochmal.
- Ohne Worktree geht es auch direkt im Repo: Branch anlegen (`git switch -c popcorn-fallback`), dann dieselben `git apply`-Befehle.
- Spec und ADR kopiert ihr danach aus der `Artefakte.zip` (`Uebung-4/muster-ergebnisse/`) nach `specs/` bzw. `docs/adr/`.

### Teil A – Vorbereitung
**A1. Review-Werkzeuge bereitstellen.** Skills `diff-review`, `testluecken`, `sicherheits-review` und
den Subagenten `reviewer` aus dem Anhang kopieren und committen. Danach den Worktree für das neue Feature anlegen.

```bash
git worktree add ../kino-getraenke -b getraenke && cd ../kino-getraenke && (cd frontend && npm ci)
```

**A2. Klären** Steht etwas nicht auf der Karte, lautet die Antwort: „Wissen wir noch nicht.“

```text
/klaeren Beim Ticketkauf sollen Gäste Getränke mitbestellen können. Die Ausgabedatei soll getraenke-klaerung.md heißen
```

**A3. Spezifikation und Mockup.**

```text
/spezifikation Schreibe aus docs/anforderungen/getraenke-klaerung.md die Spezifikation specs/getraenke.md.
Entscheide nichts, was in der Klärung offen ist.
```

```text
Erzeuge aus docs/anforderungen/getraenke-klaerung.md ein klickbares Wegwerf-Mockup docs/mockups/getraenke.html. Markiere jede offene Frage sichtbar an der Stelle im Ablauf.
```

### Teil B – Den Workflow bauen und laufen lassen
**B1. Workflow-Skript schreiben lassen.** Ihr beschreibt den Ablauf; der Agent schreibt das Skript.js; ihr lest es gegen.  
Das Skript enthält **keine** Projektbegriffe, sie kommen
über `args`.

```text
Schreibe einen Workflow feature-umsetzen (reines JavaScript), der die ganze Story umsetzt – ein Slice nach dem anderen.
0. Slices: Ein Agent liest die Slice-Liste aus der Klärung und den Abschnitt "Offen" der Spezifikation. Slices, die an
   einer offenen Frage hängen, überspringt das Skript und nennt sie im Ergebnis. Der Rest läuft in der Reihenfolge der Klärung.
Dann je Slice nacheinander (jeder baut auf dem Commit des vorigen auf) drei Phasen:
1. Plan: Skill vor-der-umsetzung, nur die Schritte 1–3 (Begriffe, Fundstellen, Annahmen mit Sicherheit), kein Code.
   Der Plan meldet auch den aktuellen Commit (git rev-parse HEAD) als Startpunkt des Slices.
   Danach ein Gate im Skript: Gibt es Annahmen mit Sicherheit "niedrig", die die Spezifikation nicht als offen führt,
   endet der Workflow mit der Frageliste.
2. Code: Subagent umsetzer (model sonnet), dieser Slice test-first, genau ein Commit. Weicht etwas von der
   Spezifikation ab: abbrechen, Grund melden, Workflow endet.
3. Review: erst den Diff vom Startpunkt des Slices bis HEAD holen, dann parallel: Subagent reviewer (Skill diff-review),
   Skill testluecken und Skill sicherheits-review. Funde im Skript nach Schwere sortieren. Nichts ändern.
   Gibt es einen inhaltlichen Fund mit Schwere "hoch" (oder liefert ein Prüfer nichts), endet der Workflow vor dem nächsten Slice.
Alle aufgabenspezifischen Informationen über args: spec, klaerung, mockup, basis; optional slices (Liste von Kennungen, überschreibt
die automatische Auswahl). Jede Phase liefert ein Ergebnis mit Ausgabeformat; das Gesamtergebnis nennt je Slice Plan, Commit
und Funde sowie die übersprungenen Slices.
Speichere es unter .claude/workflows/feature-umsetzen.js.
```

**B2. Laufen lassen.** Der Workflow läuft im Hintergrund und meldet sich mit dem Ergebnis.
```text
Führe den Workflow /feature-umsetzen aus mit args: { spec: "specs/getraenke.md",
klaerung: "docs/anforderungen/getraenke-klaerung.md", mockup: "docs/mockups/getraenke.html",
basis: "main" }
```

✓ **Check:** /workflows

**Stoppt der Workflow** – am Gate, weil eine Annahme unklar ist, bei einem Abbruch des Code-Agenten oder wegen eines Funds mit
Schwere „hoch“ –, steht im Ergebnis, bei welchem Slice. Ursache beheben (Spec oder Klärung ergänzen, Fund klären) und mit
`slices: [<die noch offenen>]` neu starten. Das ist kein Fehler, sondern der Zweck.

```text
Führe den Workflow /feature-umsetzen aus mit args: {     
  spec: "specs/getraenke.md",                             
  klaerung: "docs/anforderungen/getraenke-klaerung.md",   
  mockup: "docs/mockups/getraenke.html",                  
  basis: "main", slices: ["SL2", "SL3", "SL4"] }    
```
Euer Stop-Hook aus Ü3 blockiert, solange ein Test rot ist; ein bewusst roter Zwischenstand wird von *euch* pausiert
(`touch .agents/hooks/.qg-pause`, danach `rm`).

**B3. Ergebnis beurteilen.**
- Je Slice den Commit und die Fundliste lesen, nicht nur das Ende des Berichts. Welche Slices wurden übersprungen, und warum?
- Welcher Fund ist vor dem Merge zu klären, welcher ist überflüssig? Wer hat ihn gefunden (`reviewer`, `testluecken`,
  `sicherheits-review`)? wurde er mehrfach gefunden?
- Findet das Review etwas, das der Coding-Agent selbst eingebaut hat – oder aus dem Muster kopiert?

### Selbstcheck
- An welcher Stelle müsstet ihr eingreifen? Bei welchem Slice hat der Workflow gestoppt, und zu Recht?
- Ist es mehrmals vorgekommen?
- Hat ein späterer Slice einen Fehler eines früheren geerbt?
- Was können wir verbessern?
