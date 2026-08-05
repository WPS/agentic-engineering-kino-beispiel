package de.wps.ddd.kino.kartenverkauf.application.services;

import de.wps.ddd.kino.kartenverkauf.application.domain.sitzplatzvergabe.Platzanzahl;
import de.wps.ddd.kino.kartenverkauf.application.domain.sitzplatzvergabe.ZusammenhaengendePlaetze;
import de.wps.ddd.kino.kartenverkauf.application.domain.vorstellungen.VorstellungId;
import de.wps.ddd.kino.kartenverkauf.application.ports.secondary.SaalplanStapel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class SucheZusammenhaengendePlaetze implements de.wps.ddd.kino.kartenverkauf.application.ports.primary.SucheZusammenhaengendePlaetze {

    private final SaalplanStapel saalplanStapel;

    @Override
    public ZusammenhaengendePlaetze fuer(VorstellungId vorstellungId, Platzanzahl platzanzahl) {
        log.info("Suche {} zusammenhängende Plätze für Vorstellung {}", platzanzahl.value(), vorstellungId);
        var saalplan = saalplanStapel.holeSaalplan(vorstellungId);
        var ergebnis = saalplan.sucheZusammenhaengendePlaetze(platzanzahl);
        log.info("Gefundene zusammenhängende Plätze für Vorstellung {}: {}", vorstellungId, ergebnis.plaetze());
        return ergebnis;
    }
}
