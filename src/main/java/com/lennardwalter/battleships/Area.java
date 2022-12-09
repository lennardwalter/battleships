package com.lennardwalter.battleships;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Area {
    private List<List<Cell>> cells;
    private List<Ship> ships;

    public Area() {
        this.cells = IntStream.range(0, 10)
                .mapToObj(y -> IntStream.range(0, 10)
                        .mapToObj(x -> new Cell(this, x, y))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        this.ships = new ArrayList<Ship>();

    }

    public List<Cell> getNeighbours(Cell cell) {
        return this.cells.stream()
                .flatMap(List::stream)
                .filter(c -> Math.abs(c.getX() - cell.getX()) <= 1 && Math.abs(c.getY() - cell.getY()) <= 1)
                .collect(Collectors.toList());

    }

    public void placeShip(Ship ship, int x, int y, Direction direction) throws GameException {
        long shipCount = this.ships.stream().filter(s -> s.getLength() == ship.getLength()).count();
        if (shipCount >= Ship.getQuantity(ship.getLength())) {
            throw new GameException(GameException.Code.CANT_PLACE_SHIP_OF_LENGTH);
        }

        if (x < 0 || x > 10 || y < 0 || y > 10) {
            throw new GameException(GameException.Code.COORDINATE_OUT_OF_BOUNDS);
        }

        if (direction == Direction.HORIZONTAL && x + ship.getLength() > 10) {
            throw new GameException(GameException.Code.COORDINATE_OUT_OF_BOUNDS);
        }

        if (direction == Direction.VERTICAL && y + ship.getLength() > 10) {
            throw new GameException(GameException.Code.COORDINATE_OUT_OF_BOUNDS);
        }

        List<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < ship.getLength(); i++) {
            Cell cell = direction == Direction.HORIZONTAL ? this.cells.get(y).get(x + i) : this.cells.get(y + i).get(x);
            if (cell.getShip() != null) {
                throw new GameException(GameException.Code.CELL_ALREADY_OCCUPIED);
            }

            List<Cell> neighbours = this.getNeighbours(cell);
            if (neighbours.stream().anyMatch(c -> c.getShip() != null)) {
                throw new GameException(GameException.Code.SHIP_TOO_CLOSE);
            }

            cells.add(cell);
        }

        ship.setCells(cells);
        for (Cell cell : cells) {
            cell.setShip(ship);
        }

        this.ships.add(ship);
    }

    public FireshotResult fireShot(int x, int y) throws GameException {
        if (x < 0 || x > 10 || y < 0 || y > 10) {
            throw new GameException(GameException.Code.COORDINATE_OUT_OF_BOUNDS);
        }

        Cell cell = this.cells.get(y).get(x);
        if (cell.isHit()) {
            throw new GameException(GameException.Code.CELL_ALREADY_HIT);
        }

        cell.setHit(true);

        if (cell.getShip() != null) {
            if (cell.getShip().getCells().stream().allMatch(Cell::isHit)) {
                cell.getShip().setSunk(true);
                return FireshotResult.SUNK;
            }
            return FireshotResult.HIT;
        }

        return FireshotResult.MISS;
    }

    public String pretty() {
        return "  " + IntStream.rangeClosed('A', 'J')
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining(" ")) + "\n" +

                cells.stream()
                        .map(row -> Integer.toString(row.get(0).getY()) + " " + row.stream()
                                .map(cell -> cell.isHit()
                                        ? (cell.getShip() != null ? "O" : "X")
                                        : (cell.getShip() != null ? "S" : " "))
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining("\n"));

    }

    public List<Ship> getShips() {
        return this.ships;
    }

    public List<List<Cell>> getCells() {
        return this.cells;
    }

}
