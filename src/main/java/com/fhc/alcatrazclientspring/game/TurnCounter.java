package com.fhc.alcatrazclientspring.game;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TurnCounter {
    private final AtomicInteger counter = new AtomicInteger(0);

    public int nextTurn() { return counter.incrementAndGet(); }
    public void setTo(int turn) { counter.set(turn); }
    public int current() { return counter.get(); }
}
