package de.wps.ddd.kino.kartenverkauf.adapters.primary.web.model;

/**
 * Eine Popcorn-Portion in der Kartenbestellung. {@code groesse} und {@code geschmack}
 * tragen die Enum-Namen der Domäne (z.&nbsp;B. {@code MITTEL} / {@code GEMISCHT}).
 */
public record PopcornPortionDto(String groesse, String geschmack) {
}
