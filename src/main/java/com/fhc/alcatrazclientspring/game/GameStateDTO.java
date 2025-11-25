package com.fhc.alcatrazclientspring.game;

import com.fhc.alcatrazclientspring.game.MoveDTO;

import java.util.List;

public class GameStateDTO {
    private List<MoveDTO> moves;

    public GameStateDTO() {}
    public GameStateDTO(List<MoveDTO> moves) { this.moves = moves; }

    public List<MoveDTO> getMoves() { return moves; }
    public void setMoves(List<MoveDTO> moves) { this.moves = moves; }
}
