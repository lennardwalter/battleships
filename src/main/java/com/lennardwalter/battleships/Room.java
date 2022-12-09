package com.lennardwalter.battleships;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Room {
    private String id;
    private Map<WebSocket, Player> players;
    private Game game;

    public Room(String roomId) {
        this.id = roomId;
        this.players = new HashMap<WebSocket, Player>();
        this.game = new Game(this);
    }

    public void onMessage(WebSocket conn, String message) throws GameException {
        if (!this.players.containsKey(conn)) {
            throw new GameException(GameException.Code.INVALID_REQUEST);
        }

        Player player = this.players.get(conn);

        ObjectMapper mapper = new ObjectMapper();

        try {
            Request request = mapper.readValue(message, Request.class);
            switch (request.getType()) {
                case "PLACE_SHIP":
                    Request.PlaceShip placeRequest = mapper.convertValue(request.getData(), Request.PlaceShip.class);
                    this.onPlace(placeRequest, player);
                    break;
                case "FIRE_SHOT":
                    Request.FireShot shootRequest = mapper.convertValue(request.getData(), Request.FireShot.class);
                    this.onShoot(shootRequest, player);
                    break;
                default:
                    throw new GameException(GameException.Code.INVALID_REQUEST);
            }
        } catch (Exception e) {
            if (e instanceof GameException) {
                throw (GameException) e;
            }
            throw new GameException(GameException.Code.INVALID_REQUEST);
        }
    }

    // broadcast
    public void onGameEvent(Event event) {

        if (event.getType() == Event.Type.GAME_PHASE_CHANGED
                && ((Event.GamePhaseChange) event.getData()).getPhase() == GamePhase.SETUP) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(event);
            for (WebSocket conn : this.players.keySet()) {
                conn.send(json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // only send to specific player
    public void onGameEvent(Event event, Player player) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(event);
            for (WebSocket conn : this.players.keySet()) {
                if (this.players.get(conn) == player) {
                    conn.send(json);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void onPlace(Request.PlaceShip request, Player player) throws GameException {
        player.placeShip(new Ship(request.getLength()), request.getX(), request.getY(), request.getDirection());
        this.onGameEvent(new Event(Event.Type.SHIP_PLACED,
                new Event.ShipPlaced(request.getX(), request.getY(), request.getDirection(), request.getLength())),
                player);
    }

    public void onShoot(Request.FireShot request, Player player) throws GameException {
        Player enemy = this.game.getPlayers().stream().filter(p -> p != player).findFirst().get();
        player.fireShot(enemy, request.getX(), request.getY());
    }

    public void addPlayer(WebSocket conn, String name) throws GameException {
        this.players.put(conn, null);
        try {
            Player player = this.game.registerPlayer(name);
            this.players.put(conn, player);
            if (this.game.getPhase() == GamePhase.WAITING_FOR_PLAYERS) {
                // send initial game phase if waiting for players
                // if this is the second player IN_PROGRESS will be sent
                this.onGameEvent(
                        new Event(Event.Type.GAME_PHASE_CHANGED, new Event.GamePhaseChange(this.game.getPhase())));

            } else if (this.game.getPhase() == GamePhase.SETUP) {
                List<Player> players = this.game.getPlayers();
                this.onGameEvent(new Event(Event.Type.GAME_PHASE_CHANGED, new Event.GamePhaseChange(GamePhase.SETUP,
                        new Event.GamePhaseChange.Setup(players.get(1).getName()))), players.get(0));
                this.onGameEvent(new Event(Event.Type.GAME_PHASE_CHANGED, new Event.GamePhaseChange(GamePhase.SETUP,
                        new Event.GamePhaseChange.Setup(players.get(0).getName()))), players.get(1));

            }
        } catch (GameException e) {
            this.players.remove(conn);
            throw e;
        }
    }

    public void removePlayer(WebSocket conn) {
        Player player = this.players.get(conn);
        this.players.remove(conn);
        this.game.removePlayer(player);
    }

    public boolean isFull() {
        return this.players.size() == 2;
    }

    public boolean isEmpty() {
        return this.players.size() == 0;
    }

    public String getId() {
        return this.id;
    }
}
