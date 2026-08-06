package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

/**
 * Eine einzelne Popcorn-Portion (Menge ist implizit 1 – mehr Popcorn = weitere Portion).
 * Der Preis ergibt sich allein aus der {@link PopcornGroesse}.
 */
@ValueObject
public record PopcornPortion(PopcornGroesse groesse, PopcornGeschmack geschmack) {

    public PopcornPortion {
        Assert.notNull(groesse, "Größe darf nicht null sein.");
        Assert.notNull(geschmack, "Geschmack darf nicht null sein.");
    }

    public Geldbetrag preis() {
        return groesse.preis();
    }
}
