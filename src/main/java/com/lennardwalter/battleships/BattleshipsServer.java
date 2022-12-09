package com.lennardwalter.battleships;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.server.WebSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class BattleshipsServer extends WebSocketServer {
    private Map<String, Room> roomIdToRoom;
    private Map<WebSocket, Room> connectionToRoom;

    public BattleshipsServer(InetSocketAddress address) {
        super(address);
        this.roomIdToRoom = new HashMap<String, Room>();
        this.connectionToRoom = new HashMap<WebSocket, Room>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Map<String, String> query = Util.parseQuery(handshake.getResourceDescriptor().split("\\?")[1]);

        if (!query.containsKey("room") || !query.containsKey("name")) {
            this.sendGameException(conn, new GameException(GameException.Code.INVALID_HANDSHAKE));
            conn.close();
            return;
        }

        String roomId = query.get("room");
        String playerName = query.get("name");

        try {
            Room room;

            if (this.roomIdToRoom.containsKey(roomId)) {
                room = this.roomIdToRoom.get(roomId);
                if (room.isFull()) {
                    this.sendGameException(conn, new GameException(GameException.Code.ROOM_IS_FULL));
                    conn.close();
                    return;
                }
                room.addPlayer(conn, playerName);
            } else {
                room = new Room(roomId);
                room.addPlayer(conn, playerName);
                this.roomIdToRoom.put(roomId, room);
            }
            this.connectionToRoom.put(conn, room);

        } catch (GameException e) {
            this.sendGameException(conn, e);
            conn.close();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (!this.connectionToRoom.containsKey(conn)) {
            return;
        }

        Room room = this.connectionToRoom.get(conn);
        room.removePlayer(conn);
        this.connectionToRoom.remove(conn);
        System.out.println("Player left room " + room.getId());
        if (room.isEmpty()) {
            this.roomIdToRoom.remove(room.getId());
            System.out.println("Room " + room.getId() + " is empty and has been removed");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (!this.connectionToRoom.containsKey(conn)) {
            conn.close();
            return;
        }

        Room room = this.connectionToRoom.get(conn);
        try {
            room.onMessage(conn, message);
        } catch (GameException e) {
            this.sendGameException(conn, e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific
            // websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    private void sendGameException(WebSocket conn, GameException gameException) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper
                    .writeValueAsString(new Event(Event.Type.ERROR, new Event.Error(gameException.getCode())));
            conn.send(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
