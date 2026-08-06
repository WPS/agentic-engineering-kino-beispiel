package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Wählbare Popcorn-Größen. Der Preis ist Teil der Domäne (3 / 5 / 7 €) und wird
 * serverseitig autoritativ verwendet – das Frontend zeigt ihn nur an.
 */
@ValueObject
public enum PopcornGroesse {
    KLEIN(Geldbetrag.euro(3, 0)),
    MITTEL(Geldbetrag.euro(5, 0)),
    GROSS(Geldbetrag.euro(7, 0));

    private final Geldbetrag preis;

    PopcornGroesse(Geldbetrag preis) {
        this.preis = preis;
    }

    public Geldbetrag preis() {
        return preis;
    }
}
