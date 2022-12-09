package com.lennardwalter.battleships;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    public static class FireShot {
        private int x;
        private int y;

        @JsonCreator
        public FireShot(@JsonProperty("x") int x, @JsonProperty("y") int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static class PlaceShip {
        private int length;
        private int x;
        private int y;
        private Direction direction;

        @JsonCreator
        public PlaceShip(@JsonProperty("length") int length, @JsonProperty("x") int x, @JsonProperty("y") int y,
                @JsonProperty("direction") Direction direction) throws GameException {
            this.length = length;
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public int getLength() {
            return length;
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

    }

    private String type;
    private Object data;

    @JsonCreator
    public Request(@JsonProperty("type") String type, @JsonProperty("data") Object data) throws GameException {
        if (type == null || data == null) {
            throw new GameException(GameException.Code.INVALID_REQUEST);
        }
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

}
