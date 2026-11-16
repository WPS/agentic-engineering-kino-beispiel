# Übung 5 – Neues Feature mit Workflow

**Nicht fertig mit Ü4?** Fallback aus `Uebungen-Artefakte.zip` kopieren `adr`, `spezifikation` und git patch integrieren `popcorn-upsell.patch`

```bash
git worktree add ../kino-popcorn-fallback -b popcorn-fallback main
cd ../kino-popcorn-fallback
git apply --check /pfad/zu/popcorn-upsell.patch
git apply /pfad/zu/popcorn-upsell.patch           # Änderungen liegen danach im Arbeitsverzeichnis
git add -A && git commit -m "Fallback: Popcorn-Stand"
(cd frontend && npm ci)                           # Abhängigkeiten gibt es pro Worktree
```

**Ihr lernt:** Einen Ablauf mit definierter Reihenfolge als **Workflow** zu erstellen. Zudem lernt ihr die Grenzen und Möglichkeiten kennen.

**Rahmen:** Sonnet-Klasse, Effort medium

**Ergebnis:** das Workflow-Skript `feature-umsetzen`, Skills `diff-review`, `testluecken`, `sicherheits-review`,
Subagent `reviewer`

> **Warum zwei Teile?** Klären ist ein Interview und braucht euch. Ein Workflow läuft im Hintergrund und kann nicht
> nachfragen.


### Teil A – Vorbereitung

**A1. Review-Werkzeuge bereitstellen.** Skills `diff-review`, `testluecken`, `sicherheits-review` und
den Subagenten `reviewer` aus `Uebungen-Artefakte.zip` kopieren und committen. Danach den Worktree für das neue Feature anlegen.

```bash
git worktree add ../kino-getraenke -b getraenke && cd ../kino-getraenke && (cd frontend && npm ci)
```

**A2. Klären** Steht etwas nicht auf der Karte, lautet die Antwort: „Wissen wir noch nicht.“

Prompt:
```text
/klaeren Beim Ticketkauf sollen Gäste Getränke mitbestellen können. Die Ausgabedatei soll getraenke-klaerung.md heißen
```

**A3. Spezifikation und Mockup.**

Prompt:
```text
/spezifikation Schreibe aus docs/anforderungen/getraenke-klaerung.md die Spezifikation specs/getraenke.md.
Entscheide nichts, was in der Klärung offen ist.
```

Prompt:

```text
Erzeuge aus docs/anforderungen/getraenke-klaerung.md ein klickbares Wegwerf-Mockup docs/mockups/getraenke.html. Markiere jede offene Frage sichtbar an der Stelle im Ablauf.
```

### Teil B – Den Workflow bauen und laufen lassen

**B1. Workflow-Skript schreiben lassen.** Ihr beschreibt den Ablauf und der Agent schreibt das Skript. Das Skript soll **keine** Projektbegriffe enthalten und diese nur über `args` übergeben.

Prompt
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

Prompt
```text
Führe den Workflow /feature-umsetzen aus mit args: { spec: "specs/getraenke.md",
klaerung: "docs/anforderungen/getraenke-klaerung.md", mockup: "docs/mockups/getraenke.html",
basis: "main" }
```

✓ **Check:** /workflows

**Stoppt der Workflow** – am Gate, weil eine Annahme unklar ist, bei einem Abbruch des Code-Agenten oder wegen eines
Funds mit
Schwere „hoch“ –, steht im Ergebnis, bei welchem Slice. Ursache beheben (Spec oder Klärung ergänzen, Fund klären) und
mit
`slices: [<die noch offenen>]` neu starten. Das ist kein Fehler, sondern der Zweck.

```text
Führe den Workflow /feature-umsetzen aus mit args: {     
  spec: "specs/getraenke.md",                             
  klaerung: "docs/anforderungen/getraenke-klaerung.md",   
  mockup: "docs/mockups/getraenke.html",                  
  basis: "main", slices: ["SL2", "SL3", "SL4"] }    
```

**B3. Ergebnis beurteilen.**

- Je Slice den Commit und die Fundliste lesen, nicht nur das Ende des Berichts. Welche Slices wurden übersprungen, und
  warum?
- Welcher Fund ist vor dem Merge zu klären, welcher ist überflüssig? Wer hat ihn gefunden (`reviewer`, `testluecken`,
  `sicherheits-review`)? wurde er mehrfach gefunden?
- Findet das Review etwas, das der Coding-Agent selbst eingebaut hat – oder aus dem Muster kopiert?

### Selbstcheck

- An welcher Stelle müsstet ihr eingreifen? Bei welchem Slice hat der Workflow gestoppt, und zu Recht?
- Ist es mehrmals vorgekommen?
- Hat ein späterer Slice einen Fehler eines früheren geerbt?
- Was können wir verbessern?