package de.wps.ddd.kino.kartenverkauf.adapters.primary.web.model;

import java.util.List;
import java.util.UUID;

/**
 * Bestellung zum Start eines Verkaufsvorgangs: Vorstellung, gewählte Plätze und die
 * optionale Popcorn-Bestellung. {@code popcorn} darf leer oder {@code null} sein.
 */
public record KartenbestellungDto(UUID vorstellungId, ZusammenhaengendePlaetzeDto plaetze,
                                  List<PopcornPortionDto> popcorn) {
}
