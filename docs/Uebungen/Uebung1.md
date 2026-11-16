# Übungen – Agentic Engineering Schulung

Jede Übung trainiert eine Technik, die ihr in jedes andere Projekt mitnehmen könnt. Mit jeder Übung wächst euer *
*Werkzeugkasten** um ein Stück:
eine AGENTS.md, Skills, Subagents, Hooks, Vorlagen.

Musterlösungen und Erklärungen zu den Artefakten (Trainer-Material) liegen in der **`Uebungen-Artefakte.zip`**, die ihr vom
Trainer bekommt.

## Spielregeln

- Am Ende jeder Übung committen
- Wer nicht fertig wird, kann die Artefakte der Übung aus der `Uebungen-Artefakte.zip` kopieren
- Die in den Übungen gezeigten Befehle sind für `bash` ausgelegt
  - Für Windows Nutzer:innen: `wsl` oder `git bash` nutzen

# Übung 1 – Isoliert arbeiten und Annahmen aufdecken

**Ihr lernt:** Einen Agenten in einem eigenen Arbeitsordner (Worktree) laufen zu lassen, ohne dabei das Hauptrepository (main worktree)
anzufassen. Ihr findet heraus, *was der Agent angenommen hat*, statt nur zu prüfen, ob das Programm läuft.

**Rahmen:** Sonnet-Klasse, Effort medium

**Ergebnis:** `AGENTS.md`, ausgefüllte Annahmen-Tabelle, Audit-Prompt

### Vorgehen

**1.1 AGENTS.md erstellen.**

Prompt:

```text
Verschaffe dir einen Überblick über dieses Repository und schreibe daraus eine AGENTS.md im Root-Verzeichnis.
Hinein gehören:
1. Wofür das System fachlich da ist, in drei Sätzen
2. Die Bausteine und wo welcher Code liegt
3. Bauen, Starten, Testen als ausführbare Befehle – führe die Build- und Testbefehle einmal aus und
   nimm nur auf, was funktioniert
4. Fachbegriffe, die im Code anders heißen als im Gespräch.
Belege jede Aussage mit der Datei, aus der du sie hast. Erfinde keine Regeln. Maximal 30 Zeilen.
```

Lest die generierte Datei selbst und **kürzt**: Was ihr nicht nachvollziehen könnt, fliegt raus.  
Datei auf `main` committen.

✓ **Check:** Neue Session, Frage „Wie führe ich die Backend-Tests aus?“ – die Antwort kommt aus der AGENTS.md,
ohne dass der Agent sucht.

**1.2 Worktree anlegen**

```bash
git worktree add ../kino-uebung1 -b uebung1
cd ../kino-uebung1
(cd frontend && npm ci)      # Abhängigkeiten gibt es pro Worktree
claude  # claude oder ein anderes Tool starten
```

✓ **Check:** `git worktree list`. Läuft die App noch im Hauptrepository, kann es zu Portkonflikten kommen.

**1.3 Kinofilm-Filter erstellen.** Der Agent soll bewusst Annahmen treffen. Gebt ihm keine Zusatzinfos und verwendet keinen Plan-Modus.

Prompt:
```text
Im Programm soll man auf Kinderfilme filtern können.
```

✓ **Check:** Führt die Tests aus und startet die Anwendung neu. Probiert das neue Feature aus.

**1.4 Annahmen aufdecken**:

Prompt:

```text
Ändere nichts. Liste jede Annahme auf, die du bei dieser Umsetzung getroffen hast. Beachte auch implizite Annahmen, wie 
Begriffe, Standardwerte, Testdaten, Ort der Logik.
Erstelle eine Tabelle mit den folgenden Spalten: # | Annahme | Kategorie (Begriff | Randfall | Ort im Code | Verhalten | Test) |
Fundstelle Datei:Zeile | Sicherheit (hoch/mittel/niedrig) | Wer hätte es wissen müssen?
Tabelle als annahmen-tabelle.md in /docs speichern“
```

✓ **Check:** Schaut euch die Tabelle im Team an. Hättet ihr auch diese Annahmen getroffen? Was fällt euch auf? (Lasst den Agenten die Tabelle noch einmal bearbeiten, wenn ihr nicht zufrieden mit dem Ergebnis seid)

**1.5 Abschließen: Änderungen comitten, worktree mergen**

```bash
git add -A && git commit -m "Übung 1: Filter (Agent-Stand, ungeprüfte Annahmen)"
cd ../<hauptordner>
git checkout main && git merge uebung1
git worktree remove ../kino-uebung1 && git worktree prune
```

### Bonus

- **Ein Agent, der fragt, rät nicht:** Commit/Änderungen löschen und zweiten Worktree `uebung1b` erstellen. Darin gleichen Auftrag abarbeiten lassen, aber mit dem Zusatz
  „Bevor du etwas umsetzt, stelle mir Fragen.“ Vergleicht die Zahl der Annahmen.
