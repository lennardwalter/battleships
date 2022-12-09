package com.lennardwalter.battleships;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        BattleshipsServer s = new BattleshipsServer(new InetSocketAddress("localhost", 8080));
        s.setReuseAddr(true);
        s.start();
    }
}
