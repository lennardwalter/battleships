package com.lennardwalter.battleships;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Room room;
    private List<Player> players;
    private GamePhase phase;
    private Player currentPlayer;

    public Game(Room room) {
        this.players = new ArrayList<Player>();
        this.phase = GamePhase.WAITING_FOR_PLAYERS;
        this.room = room;
    }

    public Player registerPlayer(String name) throws GameException {
        if (this.phase != GamePhase.WAITING_FOR_PLAYERS) {
            throw new GameException(GameException.Code.WRONG_PHASE);
        }

        if (this.players.stream().anyMatch(p -> p.getName().equals(name))) {
            throw new GameException(GameException.Code.PLAYER_ALREADY_EXISTS);
        }

        Player player = new Player(this, name);

        this.players.add(player);
        if (this.players.size() == 2) {
            this.setPhase(GamePhase.SETUP);
        }

        return player;
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        if (this.players.size() == 1) {
            this.setPhase(GamePhase.GAME_OVER,
                    new Event.GamePhaseChange.GameOver(Event.GamePhaseChange.GameOver.Reason.PLAYER_LEFT,
                            this.players.get(0).getName()));
        }

    }

    protected void notifyShipPlaced() {
        if (this.players.stream().allMatch(p -> p.getArea().getShips().size() == Ship.totalShips)) {
            this.setGameInProgress();
        }
    }

    protected void notifyShotFired(FireshotResult result, int x, int y) {
        List<int[]> shipCoords = null;
        if (result == FireshotResult.SUNK) {
            Ship ship = this.currentPlayer.getArea().getCells().get(y).get(x).getShip();
            shipCoords = ship.getCells().stream().map(c -> new int[] { c.getX(), c.getY() }).toList();
        }

        this.room.onGameEvent(
                new Event(Event.Type.SHOT_FIRED,
                        new Event.ShotFired(result, x, y, this.currentPlayer.getName(), shipCoords)));

        if (result == FireshotResult.MISS) {
            this.switchPlayer();
        } else if (result == FireshotResult.SUNK) {
            if (this.currentPlayer.getArea().getShips().stream().allMatch(s -> s.isSunk())) {
                this.setPhase(GamePhase.GAME_OVER, new Event.GamePhaseChange.GameOver(
                        Event.GamePhaseChange.GameOver.Reason.PLAYER_WON, this.currentPlayer.getName()));
            }
        }
    }

    private void setGameInProgress() {
        this.setPhase(GamePhase.IN_PROGRESS);
        this.setCurrentPlayer(this.players.get(ThreadLocalRandom.current().nextInt(0, 2)));
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public GamePhase getPhase() {
        return this.phase;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    private void switchPlayer() {
        if (this.currentPlayer == this.players.get(0)) {
            this.setCurrentPlayer(this.players.get(1));
        } else {
            this.setCurrentPlayer(this.players.get(0));
        }
    }

    private void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        this.room.onGameEvent(new Event(Event.Type.PLAYER_CHANGED, new Event.PlayerChanged(currentPlayer.getName())));
    }

    private void setPhase(GamePhase phase) {
        this.setPhase(phase, null);
    }

    private void setPhase(GamePhase phase, Object extra) {
        this.phase = phase;
        this.room.onGameEvent(new Event(Event.Type.GAME_PHASE_CHANGED, new Event.GamePhaseChange(phase, extra)));
    }

    public Room getRoom() {
        return this.room;
    }
}
