# Übung 4 – Spezifikation als Hebel

**Nicht fertig mit Ü3?** Kein Problem. Eine fertige popcorn-klaerung.md findet ihr unter `Uebungen-Artefakte\Uebung-3\muster-ergebnisse` in der`Uebungen-Artefakte.zip`.

**Ihr lernt:** Eine gute Spezifikation definiert die Akzeptanzkritieren und vermeidet mehrere ungewollte Schleifen und
eine Architekturentscheidung als ADR verhindert eine erneute Diskussion.

**Rahmen:** starkes Modell zum Planen, Sonnet-Klasse zum Umsetzen

**Ergebnis:** `/spezifikation`, `/adr`, `specs/popcorn.md`, `docs/adr/0001-*.md`

### Vorgehen

**4.1. Skills** `/spezifikation` mit `spec-template.md` und `/adr` mit `adr-template.md` aus `Uebungen-Artefakte.zip` → `Uebung-4` kopieren.

**4.2 Worktree anlegen und Modellwahl**

```bash
git worktree add ../kino-popcorn -b popcorn && cd ../kino-popcorn && (cd frontend && npm ci)
claude
```

In Claude Code: `/model opusplan` (Opus im Plan-Modus).
In anderen Tools: zum Planen das stärkere Modell, zum Umsetzen das schwächere.

**4.3 Spezifikation Skill anwenden**

Prompt:
```text
/spezifikation Schreibe aus docs/anforderungen/popcorn-klaerung.md die Spezifikation specs/popcorn.md.
Entscheide nichts, was in der Klärung offen ist.
```
`popcorn-klaerung.md` durchlesen.

✓ **Check:**

- Bei jeder Zeile unter „Fertig, wenn“ ist ein Befehl oder eine beobachtbare Tatsache
- Etwas wie „Ein Kauf ohne Popcorn verhält sich unverändert“ ist dabei
- Offenes steht unter „Offen“.

**4.4 ADR Skill anwenden**

```text
/adr Wo lebt die Popcorn-Bestellung fachlich und technisch? Denke mindestens drei Optionen durch,
darunter eine minimal invasive. Nenne Risiken und Trade-offs, dann eine begründete Entscheidung.
```
- Ergebnis `0001-popcorn-im-verkaufsvorgang.md` durchlesen
- Skills und Artefakten committen (Commit-Message: `docs: Spezifikation und ADR Popcorn`).

**4.5 Slice 1 testgetrieben umsetzen.**
Beispiel-Musterplan: eine Portion mit Preis, Berechnung der Gesamtsumme, bis zur API als Slice 1;
die Oberfläche wäre Slice 2. Erst im Plan-Modus loslegen und dann den Plan überprüfen.

Prompt:
```text
Setze Slice 1 aus docs/anforderungen/popcorn-klaerung.md um, gegen specs/popcorn.md und das ADR.
- Pro Akzeptanzszenario dieses Slices: zuerst einen fehlschlagenden Test schreiben, kurz zeigen, dass er rot ist,
  und im selben Durchgang weiter umsetzen, bis er grün ist.
- Falls der Slice von Spec oder ADR abweicht: anhalten und fragen.
- Am Ende genau ein Commit, die Nachricht nennt die Szenarien.
```

_(„Im selben Durchgang“, weil euer Stop-Hook aus Ü3 jedes Anhalten mit rotem Test blockiert.)_

✓ **Check:** `
- git log --oneline main..` zeigt zwei Commits (Spec/ADR, Slice 1)
- `cd backend && ./mvnw verify` ist grün
- ihr wisst, ob und wie oft der Stop-Hook eingegriffen hat

**4.6. Weitere Slices** dürfen während der Kaffeepause weiterlaufen.

### Selbstcheck
- Wie oft musstet ihr nachfassen? (Vergleich zu Ü1)
- Welche Diskussion hat das ADR beendet?
- Was hat der Harness geblockt? zu Recht oder zu Unrecht?
- Die „Fertig, wenn“ in der Specs waren zu vage oder genau richtig?

### Bonus

- **Set-based Design:** Den zweitbeste Umsetzungsplan aus den ADR-Optionen in einem zweiten Worktree umsetzen lassen und beide Varianten gegenüberstellen (Tests, Umfang der Änderung, Lesbarkeit). Gibt es weitere Take-aways die ins ADR sollen?
- **Diagramm als Text:** Der neue Bestellungsprozess als Mermaid-Diagramm nach `docs/diagrams/` zeichnen lassen
