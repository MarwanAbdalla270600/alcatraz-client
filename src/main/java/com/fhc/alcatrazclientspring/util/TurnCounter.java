package com.fhc.alcatrazclientspring.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Erzeugt fortlaufende Zugnummern.
 * Kann später auch mit Server/Token synchronisiert werden,
 * für jetzt reicht eine lokale Zählung.
 */
@Component
public class TurnCounter {

    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Nächsten Turn-Wert liefern (1, 2, 3, ...).
     */
    public int nextTurn() {
        return counter.incrementAndGet();
    }

    /**
     * Manuelles Setzen z.B. nach Resync,
     * falls du dich am globalen letzten Turn ausrichten willst.
     */
    public void setTo(int turn) {
        counter.set(turn);
    }

    public int current() {
        return counter.get();
    }
}