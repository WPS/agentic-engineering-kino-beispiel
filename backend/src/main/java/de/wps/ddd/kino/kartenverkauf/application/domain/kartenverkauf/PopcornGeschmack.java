package de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Geschmacksrichtung einer Popcorn-Portion. Hat keinen Einfluss auf den Preis.
 */
@ValueObject
public enum PopcornGeschmack {
    SALZIG,
    SUESS,
    GEMISCHT
}
