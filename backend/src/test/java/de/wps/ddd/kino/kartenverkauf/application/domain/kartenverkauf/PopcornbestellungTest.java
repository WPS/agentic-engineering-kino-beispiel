package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PopcornbestellungTest {

    private PopcornPortion portion(PopcornGroesse groesse) {
        return new PopcornPortion(groesse, PopcornGeschmack.GEMISCHT);
    }

    @Test
    void leer_istLeerUndKostetNichts() {
        // act
        var bestellung = Popcornbestellung.leer();

        // assert
        assertThat(bestellung.istLeer()).isTrue();
        assertThat(bestellung.gesamtpreis()).isEqualTo(Geldbetrag.euroInCent(0));
    }

    @Test
    void gesamtpreis_summiertAllePortionen() {
        // arrange
        var bestellung = new Popcornbestellung(List.of(
                portion(PopcornGroesse.MITTEL),
                portion(PopcornGroesse.GROSS)
        ));

        // act / assert
        assertThat(bestellung.istLeer()).isFalse();
        assertThat(bestellung.gesamtpreis()).isEqualTo(Geldbetrag.euro(12, 0));
    }

    @Test
    void portionen_sindUnveraenderlich() {
        // arrange
        var quelle = new java.util.ArrayList<>(List.of(portion(PopcornGroesse.KLEIN)));
        var bestellung = new Popcornbestellung(quelle);

        // act
        quelle.add(portion(PopcornGroesse.GROSS));

        // assert – nachträgliche Änderung der Quelle wirkt sich nicht aus
        assertThat(bestellung.portionen()).hasSize(1);
        assertThat(bestellung.gesamtpreis()).isEqualTo(Geldbetrag.euro(3, 0));
    }
}
