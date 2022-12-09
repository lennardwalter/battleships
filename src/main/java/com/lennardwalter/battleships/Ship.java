package com.lennardwalter.battleships;

import java.util.List;

public class Ship {
    private int length;
    private List<Cell> cells;
    private boolean sunk;

    public static int totalShips = 10;

    public Ship(int length) {
        this.length = length;
    }

    public static int getQuantity(int length) {
        switch (length) {
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 1;
            default:
                return 0;
        }
    }

    public int getLength() {
        return this.length;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public boolean isSunk() {
        return this.sunk;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }
}
