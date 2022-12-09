package com.lennardwalter.battleships;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
    public enum Type {
        GAME_PHASE_CHANGED,
        PLAYER_CHANGED,
        SHOT_FIRED,
        PLAYER_JOINED,
        SHIP_PLACED,
        ERROR
    }

    public static class GamePhaseChange {
        @JsonProperty("phase")
        private GamePhase phase;

        @JsonProperty("extra")
        private Object extra;

        public static class GameOver {
            public static enum Reason {
                PLAYER_LEFT,
                PLAYER_WON,
            }

            @JsonProperty("reason")
            private Reason reason;

            @JsonProperty("winner")
            private String winner;

            public GameOver(Reason reason, String winner) {
                this.reason = reason;
                this.winner = winner;
            }

            public Reason getReason() {
                return reason;
            }

            public String getWinner() {
                return winner;
            }
        }

        public static class Setup {
            @JsonProperty("enemy")
            private String enemy;

            public Setup(String enemy) {
                this.enemy = enemy;
            }

            public String getEnemy() {
                return enemy;
            }
        }

        public GamePhaseChange(GamePhase phase, Object extra) {
            this.phase = phase;
            this.extra = extra;
        }

        public GamePhaseChange(GamePhase phase) {
            this.phase = phase;
            this.extra = null;
        }

        public GamePhase getPhase() {
            return phase;
        }

        public Object getExtra() {
            return extra;
        }
    }

    public static class PlayerChanged {
        @JsonProperty("name")
        private String name;

        public PlayerChanged(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class ShotFired {
        @JsonProperty("result")
        private FireshotResult result;

        @JsonProperty("x")
        private int x;

        @JsonProperty("y")
        private int y;

        @JsonProperty("player")
        private String player;

        @JsonProperty("shipCoordinates")
        private List<int[]> shipCoordinates;

        public ShotFired(FireshotResult result, int x, int y, String player, List<int[]> shipCoordinates) {
            this.result = result;
            this.x = x;
            this.y = y;
            this.player = player;
            this.shipCoordinates = shipCoordinates;
        }

        public FireshotResult getResult() {
            return result;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getPlayer() {
            return player;
        }

        public List<int[]> getShipCoordinates() {
            return shipCoordinates;
        }
    }

    public static class ShipPlaced {
        @JsonProperty("x")
        private int x;

        @JsonProperty("y")
        private int y;

        @JsonProperty("direction")
        private Direction direction;

        @JsonProperty("length")
        private int length;

        public ShipPlaced(int x, int y, Direction direction, int length) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.length = length;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getLength() {
            return length;
        }

    }

    public static class Error {
        @JsonProperty("code")
        private GameException.Code code;

        public Error(GameException.Code code) {
            this.code = code;
        }

        public GameException.Code getCode() {
            return code;
        }
    }

    @JsonProperty
    private Object data;

    @JsonProperty
    private Type type;

    public Event(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public Type getType() {
        return type;
    }
}
