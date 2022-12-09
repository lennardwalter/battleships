package com.lennardwalter.battleships;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        BattleshipsServer s = new BattleshipsServer(new InetSocketAddress("localhost", 8080));
        s.setReuseAddr(true);
        s.start();

        // Game game = new Game();

        // Player player1 = game.registerPlayer("Player 1");
        // Player player2 = game.registerPlayer("Player 2");

        // player1.placeShip(new Ship(5), 0, 0, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(4), 0, 2, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(4), 0, 4, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(3), 0, 6, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(3), 0, 8, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(3), 7, 0, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(2), 7, 2, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(2), 7, 4, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(2), 7, 6, Direction.HORIZONTAL);
        // player1.placeShip(new Ship(2), 7, 8, Direction.HORIZONTAL);

        // player2.placeShip(new Ship(5), 0, 0, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(4), 0, 2, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(4), 0, 4, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(3), 0, 6, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(3), 0, 8, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(3), 7, 0, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(2), 7, 2, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(2), 7, 4, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(2), 7, 6, Direction.HORIZONTAL);
        // player2.placeShip(new Ship(2), 7, 8, Direction.HORIZONTAL);

        // System.out.println(player1.getArea().pretty());

        // for (List<Cell> row : game.getCurrentPlayer().getArea().getCells()) {
        // for (Cell cell : row) {
        // if (cell.getShip() != null) {
        // game.getCurrentPlayer().fireShot(cell.getX(), cell.getY());
        // }
        // }
        // }

        // System.out.println(game.getCurrentPlayer().getArea().pretty());
        // System.out.println(game.getPhase());

        // // System.out.println(player1.getArea().pretty());

    }
}
