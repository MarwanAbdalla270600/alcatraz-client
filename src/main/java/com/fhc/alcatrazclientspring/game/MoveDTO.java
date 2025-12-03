package com.fhc.alcatrazclientspring.game;

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

    public int getTurn() { return turn; }
    public int getPlayerId() { return playerId; }
    public int getPrisonerId() { return prisonerId; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getRowOrCol() { return rowOrCol; }

    public void setTurn(int t) { turn = t; }
    public void setPlayerId(int id) { playerId = id; }
    public void setPrisonerId(int id) { prisonerId = id; }
    public void setRow(int r) { row = r; }
    public void setCol(int c) { col = c; }
    public void setRowOrCol(int rc) { rowOrCol = rc; }
}
