package com.fhc.alcatrazclientspring.entities;

/**
 * Repräsentiert einen Zug im Spiel.
 * Wird als JSON über REST verschickt.
 */
public class MoveDTO {
    private int turn;
    private int playerId;
    private int prisonerId;
    private int rowOrCol;
    private int row;
    private int col;

    public MoveDTO() {}

    public MoveDTO(int turn, int playerId, int prisonerId, int rowOrCol, int row, int col) {
        this.turn = turn;
        this.playerId = playerId;
        this.prisonerId = prisonerId;
        this.rowOrCol = rowOrCol;
        this.row = row;
        this.col = col;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPrisonerId() {
        return prisonerId;
    }

    public void setPrisonerId(int prisonerId) {
        this.prisonerId = prisonerId;
    }

    public int getRowOrCol() {
        return rowOrCol;
    }

    public void setRowOrCol(int rowOrCol) {
        this.rowOrCol = rowOrCol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    // getters / setters
}
