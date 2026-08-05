package de.wps.ddd.kino.kartenverkauf.adapters.secondary.persistence.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "verkaufsvorgaenge", schema = "kartenverkauf")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VerkaufsvorgangEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID auftragsnummer;

    private UUID vorstellungId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "verkaufsvorgang_plaetze",
            schema = "kartenverkauf",
            joinColumns = @JoinColumn(name = "auftragsnummer")
    )
    private List<PlatzEmbeddable> plaetze = new ArrayList<>();

    private int gesamtpreisInCent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "popcorn_portionen",
            schema = "kartenverkauf",
            joinColumns = @JoinColumn(name = "auftragsnummer")
    )
    private List<PopcornPortionEmbeddable> popcornPortionen = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "status", column = @Column(name = "zahlungsstatus"))
    private ZahlungsvorgangEmbeddable zahlungsvorgang;

    private int anlaeufe;

    private String status;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlatzEmbeddable {
        private int reihe;
        private int platz;
    }

    /**
     * Eine persistierte Popcorn-Portion (Collection-Tabelle {@code kartenverkauf.popcorn_portionen}).
     * Größe und Geschmack werden als Enum-Namen gespeichert.
     */
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PopcornPortionEmbeddable {
        private String groesse;
        private String geschmack;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZahlungsvorgangEmbeddable {
        private UUID zahlungsvorgangId;
        private Integer anlauf;
        private Integer betragInCent;
        private String status;
    }
}
