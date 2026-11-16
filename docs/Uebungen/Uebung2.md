# Übung 2 – Skills & Subagents erstellen

**Nicht fertig mit Ü1?** Fallback aus der `Uebungen-Artefakte.zip` kopieren: `Uebung-1/README.md`

**Ihr lernt:** Wiederkehrendes Wissen in einem Skill zu sammeln, der *beim richtigen Anlass* greift; eine Teilaufgabe an
einen Subagenten mit eigenem Kontext, eigenem Modell und
eigenen Rechten zu delegieren.

**Rahmen:** Sonnet-Klasse, Effort medium, für den Subagenten reicht Haiku

**Ergebnis:** Skills:`.agents/skills/annahmen-audit/`, `.agents/skills/vor-der-umsetzung/` und Agent:
`.agents/agents/umsetzer.md`

### Vorgehen

**2.0 Skills sichtbar machen** (einmalig, nur für claude Anwender, die in der `.agents` arbeiten wollen): `.agents/skills` existiert, `.claude/skills` verweist darauf.

```bash
mkdir -p .claude
ln -s ../.agents/skills .claude/skills  # erzeugt symlink in .claude/skills  
```

**2.1 annahmen-audit Skill erstellen** Erstellt aus dem Audit-Prompt aus Ü1.4 den Skill
`.agents/skills/annahmen-audit/SKILL.md`:

Für zukünftige Umsetzungen wird dieses Vorgehen in einen Skill geschrieben. Der Skill muss in dieser Schulung nicht weiter angewendet werden.

Prompt:
```text
Erstelle folgenden Skill
---
name: annahmen-audit
description: <WANN wird er gebraucht? Den Anlass beschreiben, nicht was er tut>
---

<Der Audit-Prompt aus Übung 1, als Anweisung formuliert>
```

✓ **Check:** Aktualisiert die Skills mit `/reload-skills`. Fragt nach einer kleinen Code-Änderung durch den Agenten „Was hast du dabei angenommen?“. Erwartung: Der Skill wird verwendet, ohne dass ihr ihn aufruft. Wenn nicht, bearbeitet die Description des Skills.

**2.2 vor-der-umsetzung Skill erstellen (unabhängig von 2.1)** Aus den Kategorien eurer Annahmen-Tabelle wird ein Ablauf, der **vor** jeder fachlichen Umsetzung greift.
Für `<Tabelle einfügen>` kopiert ihr eure ausgefüllte Annahmen-Tabelle aus Ü1 (Fallback:
`Uebung-1/annahmen-tabelle.md` aus der `Uebungen-Artefakte.zip`).

Prompt:

```text
Hier ist unsere Annahmen-Tabelle: <Tabelle einfügen>
Entwirf daraus den Skill .agents/skills/vor-der-umsetzung/SKILL.md, der greift, BEVOR eine neue
fachliche Anforderung umgesetzt wird.
- description: beschreibt den Anlass, nicht diesen Fall
- Body: Ablauf als Regeln, je Annahme-Kategorie ein Schritt (Begriff, Randfall, Ort im Code, Verhalten, Test). Offenes wird gefragt, nicht geraten.
  Erst ein fehlschlagender Test, dann die Umsetzung. Befehle stehen in der AGENTS.md.
- Keine Projektbegriffe und keine festen Werte im SKILL.md (also keine Fachwörter, Grenzwerte, Datei- oder Feldnamen aus diesem Projekt). Projektwissen aus der Tabelle kommt nach references/projekt.md; im SKILL.md nur ein Verweis "lies das, wenn …".
```

✓ **Check:** `grep -iE 'film|fsk|kino' .agents/skills/vor-der-umsetzung/SKILL.md` ist leer. Der Skill enthält keine featurespezifischen Informationen. Der Agent hat den Skills bereits generalisiert. Ggf. müsst ihr nachbessern (lassen). Wir wollen einen Skill der mindestens allgemein genug ist, um auf beliebige Filter-Umsetzungen (2.5) angewendet werden.

**2.3 Subagenten erstellen.** In `.claude/agents/umsetzer.md` oder
`.agents/agents/umsetzer.md`:

Prompt:

```text
Erstelle folgenden Subagenten für die Umsetzung von Features
---
name: umsetzer
description: <wann soll delegiert werden?>
tools: <welche Werkzeuge braucht er wirklich?>
model: haiku
---

<Rolle, Arbeitsweise, was er zurückmeldet: Änderungen, Testergebnis>
```

**Frage**: Welches Modell und welche Tools nutzt dein Agent und warum?

✓ **Check:** Prompten ob ein `umsetzer` Sub-Agent erstellt werden kann (/agents deprecated).

**2.4 Erst committen, dann Worktree erstellen.**

Beispiel mit Claude:

```bash
git add .agents .claude/skills .claude/agents && git commit -m "Übung 2: Skills und Subagent"
git worktree add ../kino-uebung2 -b uebung2 && cd ../kino-uebung2 && (cd frontend && npm ci)
```

✓ **Check:** `ls .agents/skills/` bzw. `ls .agents/agents/` oder `ls .claude/skills/` bzw. `ls .agents/agents/` zeigt im Worktree eure Skills und Agents.

**2.5 Neues Feature mit Subagenten entwickeln:**

Prompt:
```text
Im Programm soll man nach Sprache filtern können. Nutze den Subagenten umsetzer zur Umsetzung. 
```

✓ **Check:** Wurde der vor-der-umsetzung Skill im Hauptagenten geladen? Wird der Subagent in der UI sichtbar?

**2.6 Abschließen:** Selbstcheck (s.u.) beantworten, committen, mergen, Worktree aufräumen (wie Ü1).

### Selbstcheck

Nach der Übung kurz im Plenum besprechen:

- Hat die `description` ausgelöst – automatisch, nur über den Subagenten oder gar nicht?
- Welche festen Werte oder fallspezifischen Annahmen aus dem ersten Fall haben gestört?
- Hat der Subagent bei Unsicherheit gefragt oder geraten? Was fehlte ihm an Kontext?
- Wo musste ein Mensch fachlich urteilen?
- Hat Haiku gereicht? Wo nicht?
- Was ändert ihr am Skill, am Subagenten oder an der AGENTS.md?

### Bonus

- Commit/Änderungen löschen, denselben Lauf mit `model: sonnet` – lohnt sich der Unterschied?