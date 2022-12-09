package com.lennardwalter.battleships;

public class Cell {
    private boolean isHit;
    private Ship ship;
    private int x;
    private int y;

    public Cell(Area area, int x, int y) {
        this.isHit = false;
        this.ship = null;
        this.x = x;
        this.y = y;
    }

    public boolean isHit() {
        return this.isHit;
    }

    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }

    public Ship getShip() {
        return this.ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
