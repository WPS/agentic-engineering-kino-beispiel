# Datenbank

Das Backend nutzt [H2](https://www.h2database.com/), dateibasiert unter `./data/kinosoft.mv.db`
relativ zum Arbeitsverzeichnis, in dem der Backend-Prozess gestartet wird. Startet man das Backend
aus dem Projekt-Root (z.B. über eine IDE-Run-Konfiguration), liegt die Datei unter
`<Projektpfad>/data/kinosoft.mv.db`; bei `cd backend && ./mvnw spring-boot:run` entsprechend unter
`backend/data/`. Konfiguriert in
[`application.properties`](../backend/src/main/resources/application.properties). Der Ordner
`data/` ist per `.gitignore` ausgeschlossen.

Bei jedem Start wird das Schema neu angelegt (`spring.jpa.hibernate.ddl-auto=create`) und zur
Laufzeit aus der Wochenplanung befüllt: Die `WochenplanFixture` erzeugt den Wochenplan der
**aktuellen ISO-Kalenderwoche**, Filmauswahl und Kartenverkauf übernehmen daraus ihre Sichten.
Ein Neustart des Backends genügt also, um wieder einen sauberen Datenstand zu bekommen — und
nach dem Stoppen bleibt die Datei erhalten und lässt sich in Ruhe inspizieren. (Die Tests
laufen unabhängig davon auf einer In-Memory-H2, siehe `backend/src/test/resources`.)

## Verbindung in IntelliJ

1. Backend einmal starten, damit die Datenbankdatei existiert.
2. **Database**-Tool-Window öffnen ▸ `+` ▸ **Data Source** ▸ **H2**.
3. Angaben:
   - **Connection type:** `URL only`
   - **URL:** `jdbc:h2:file:<Projektpfad>/data/kinosoft;AUTO_SERVER=TRUE`
   - **User:** `sa`, **Password:** leer
4. Fehlenden JDBC-Treiber über **Download driver files** installieren, dann **Test Connection** ▸ **OK**.

Danach sind die Schemas `filmauswahl` und `kartenverkauf` sichtbar. Dank `AUTO_SERVER=TRUE` in der
JDBC-URL darf die Verbindung auch **parallel zum laufenden Backend** geöffnet sein (H2 startet
automatisch einen kleinen Server-Prozess für die Datei) — wichtig ist nur, das `;AUTO_SERVER=TRUE`
in der URL nicht zu vergessen.

Die In-Memory-Repositories (z.B. Zahlung, Wochenplanung) tauchen hier bewusst nicht auf — sie
persistieren nicht in die H2.

## Alternative: H2-Konsole

Bei laufendem Backend erreichbar unter `http://localhost:8080/h2-console`, mit derselben JDBC-URL
wie in `application.properties`, Nutzer `sa` und leerem Passwort.
