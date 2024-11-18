package pl.lcc.talisman.engine;

import javafx.scene.input.KeyCode;
import pl.lcc.talisman.controller.TheMap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;


public class Engine {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static Engine instance;

    DIRECTION direction;

    int timeDelay = 300;
    private List<List<String>> internalMap;
    int mapWidth;
    int mapHeight;
    private Runnable updateMapCallback;
    private TheMap displayMap;
    private List<Point> body;

    private void runGameLoop() {
        Thread.ofVirtual().start(() -> {
            while (true) {
                doTick();
                try {
                    Thread.sleep(timeDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setMapPoint(Point head, DIRECTION direction) {
        internalMap.get(head.y() * mapWidth + head.x()).set(0, direction.getCode());
    }

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }

    public void processKey(KeyCode key) {
        switch (key) {
            case A -> direction = DIRECTION.WEST;
            case W -> direction = DIRECTION.NORTH;
            case S -> direction = DIRECTION.SOUTH;
            case D -> direction = DIRECTION.EAST;
            default -> System.out.println("Unknown key");
        }
    }

    private void doTick() {
        if (updateMapCallback != null) {
            var newHead = move(body.getFirst(), direction.getDirection());
            var last = body.removeLast();
            detectCollision(newHead, internalMap);
            body.addFirst(newHead);
            internalMap.get(last.y() * mapWidth + last.x()).set(0, "empty");
            internalMap.get(newHead.y() * mapWidth + newHead.x()).set(0, direction.getCode());
            displayMap.updateAllTiles(internalMap);
            updateMapCallback.run();
        }
    }

    private void detectCollision(Point newHead, List<List<String>> visibleMap) {
        var value = visibleMap.get(newHead.y() * mapWidth + newHead.x()).getFirst();
        if (value.equals("food")) {
            increaseLength();
            addNewFood();
        } else if (!value.equals("empty")) {
            gameOver();
        }
    }

    private void addNewFood() {
        var free_tiles = internalMap.size() - body.size();
        internalMap.stream()
                .filter(list -> list.getFirst().equals("empty"))
                .skip(new java.util.Random().nextInt(free_tiles))
                .findFirst()
                .ifPresent(list -> list.set(0, "food"));

    }

    private void gameOver() {
        System.out.println(ANSI_RED + "Game over" + ANSI_RESET);
    }

    private void increaseLength() {
        body.add(body.getLast());
        body.add(body.getLast());
    }

    public TheMap initializeAndCreateMap(int mapWidth, int mapHeight, int tileWidth, int tileHeight, Runnable updateMap) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        displayMap = new TheMap(mapWidth, mapHeight, tileWidth, tileHeight);
        setUpGame();
        displayMap.updateAllTiles(internalMap);
        setUpdateCallBack(updateMap);
        return displayMap;
    }

    public void setUpGame() {
        internalMap = IntStream.range(0, mapHeight * mapWidth)
                .mapToObj(_ -> Arrays.asList("empty"))
                .toList();
        direction = DIRECTION.NORTH;
        body = new LinkedList<>(List.of(new Point(5, 5), new Point(5, 6), new Point(5, 7)));
        body.forEach(point -> internalMap.get(point.y() * mapWidth + point.x()).set(0, "up"));
        addNewFood();
        runGameLoop();
    }

    public void setUpdateCallBack(Runnable updateMap) {
        updateMapCallback = updateMap;
    }

    public Point move(Point p1, Point p2) {
        var newX = p1.x() + p2.x() < 0 ? mapWidth - 1 : (p1.x() + p2.x() > mapWidth - 1 ? 0 : p1.x() + p2.x());
        var newY = p1.y() + p2.y() < 0 ? mapHeight - 1 : (p1.y() + p2.y() > mapHeight - 1 ? 0 : p1.y() + p2.y());
        return new Point(newX, newY);
    }

}

