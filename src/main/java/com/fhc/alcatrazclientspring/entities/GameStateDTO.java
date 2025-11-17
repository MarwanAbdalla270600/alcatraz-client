package com.fhc.alcatrazclientspring.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Dient für den State-Sync (GET /state?fromTurn=x).
 * Enthält eine Liste von Zügen, die ab einem Turn nachgezogen werden müssen.
 */
public class GameStateDTO {

    private List<MoveDTO> moves = new ArrayList<>();

    public GameStateDTO() {
    }

    public GameStateDTO(List<MoveDTO> moves) {
        this.moves = moves;
    }

    public List<MoveDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveDTO> moves) {
        this.moves = moves;
    }
}