package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Die zu einem Verkaufsvorgang gehörenden Popcorn-Portionen. Kann leer sein (kein Popcorn).
 * Kapselt die Preisaggregation, sodass Karten- und Popcorn-Preis sauber getrennt bleiben.
 */
@ValueObject
public record Popcornbestellung(List<PopcornPortion> portionen) {

    public Popcornbestellung {
        Assert.notNull(portionen, "Portionen dürfen nicht null sein.");
        portionen = List.copyOf(portionen);
    }

    public static Popcornbestellung leer() {
        return new Popcornbestellung(List.of());
    }

    public boolean istLeer() {
        return portionen.isEmpty();
    }

    public Geldbetrag gesamtpreis() {
        return portionen.stream()
                .map(PopcornPortion::preis)
                .reduce(Geldbetrag.euroInCent(0), Geldbetrag::plus);
    }
}
