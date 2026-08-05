package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PopcornPortionTest {

    @ParameterizedTest
    @CsvSource({
            "KLEIN, 300",
            "MITTEL, 500",
            "GROSS, 700",
    })
    void preis_richtetSichNachGroesse(PopcornGroesse groesse, int erwarteteCent) {
        // arrange
        var portion = new PopcornPortion(groesse, PopcornGeschmack.GEMISCHT);

        // act / assert
        assertThat(portion.preis()).isEqualTo(Geldbetrag.euroInCent(erwarteteCent));
    }

    @Test
    void konstruktor_ohneGroesse_wirftException() {
        // act / assert
        assertThatThrownBy(() -> new PopcornPortion(null, PopcornGeschmack.SALZIG))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void konstruktor_ohneGeschmack_wirftException() {
        // act / assert
        assertThatThrownBy(() -> new PopcornPortion(PopcornGroesse.MITTEL, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
