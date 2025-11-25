package com.fhc.alcatrazclientspring.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class LocalGameState {

    private final List<MoveDTO> moves = new ArrayList<>();

    public synchronized void apply(MoveDTO m) {
        moves.add(m);
        moves.sort(Comparator.comparingInt(MoveDTO::getTurn));
    }

    public synchronized List<MoveDTO> getMissing(int fromTurn) {
        return moves.stream()
                .filter(m -> m.getTurn() > fromTurn)
                .toList();
    }

    public synchronized int lastTurn() {
        return moves.isEmpty() ? 0 : moves.getLast().getTurn();
    }
}
