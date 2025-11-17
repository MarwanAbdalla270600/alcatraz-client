package com.fhc.alcatrazclientspring.service;


import com.fhc.alcatrazclientspring.entities.MoveDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hält den lokalen Spielzustand (in diesem simplen Modell nur als Liste von Moves).
 * Thread-sicher, weil mehrere HTTP-Requests parallel kommen können.
 */
@Component
public class LocalGameState {

    private final List<MoveDTO> moves = new ArrayList<>();

    public synchronized void applyMove(MoveDTO move) {
        moves.add(move);
        // Sicherheitshalber nach Turn sortieren
        moves.sort(Comparator.comparingInt(MoveDTO::getTurn));
    }

    public synchronized List<MoveDTO> getMissing(int fromTurn) {
        List<MoveDTO> result = new ArrayList<>();
        for (MoveDTO move : moves) {
            if (move.getTurn() > fromTurn) {
                result.add(move);
            }
        }
        return result;
    }

    public synchronized int getLastTurn() {
        if (moves.isEmpty()) {
            return 0;
        }
        return moves.getLast().getTurn();
    }

    public synchronized List<MoveDTO> getAllMoves() {
        return new ArrayList<>(moves);
    }
}