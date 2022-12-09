package com.lennardwalter.battleships;

public class GameException extends Exception {

    public enum Code {
        COORDINATE_OUT_OF_BOUNDS,
        WRONG_PHASE,
        PLAYER_ALREADY_EXISTS,
        CANT_PLACE_SHIP_OF_LENGTH,
        CELL_ALREADY_OCCUPIED,
        SHIP_TOO_CLOSE,
        CELL_ALREADY_HIT,
        NOT_CURRENT_PLAYER,
        INVALID_REQUEST,
        INVALID_HANDSHAKE,
        ROOM_IS_FULL,
    }

    private Code code;

    public GameException(Code code) {
        super(code.name());
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

}
