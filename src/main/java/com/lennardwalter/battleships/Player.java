package com.lennardwalter.battleships;

public class Player {
    private Game game;
    private String name;
    private Area area;

    public Player(Game game, String name) {
        this.game = game;
        this.name = name;
        this.area = new Area();
    }

    public void placeShip(Ship ship, int x, int y, Direction direction) throws GameException {
        if (this.game.getPhase() != GamePhase.SETUP) {
            throw new GameException(GameException.Code.WRONG_PHASE);
        }

        this.area.placeShip(ship, x, y, direction);

        this.game.notifyShipPlaced();
    }

    public void fireShot(Player at, int x, int y) throws GameException {
        if (this.game.getPhase() != GamePhase.IN_PROGRESS) {
            throw new GameException(GameException.Code.WRONG_PHASE);
        }

        if (this.game.getCurrentPlayer() != this) {
            throw new GameException(GameException.Code.NOT_CURRENT_PLAYER);
        }

        FireshotResult result = at.getArea().fireShot(x, y);

        this.game.notifyShotFired(result, x, y);
    }

    public Game getGame() {
        return this.game;
    }

    public String getName() {
        return this.name;
    }

    public Area getArea() {
        return this.area;
    }

}
