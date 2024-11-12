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
    private List<List<String>> visibleMap;
    static int mapWidth = 13;
    static int mapHeight = 11;
    private Runnable updateMapCallback;
    private TheMap theMap;
    private List<Point> body;

    Engine() {
        visibleMap = IntStream.range(0, mapHeight*mapWidth)
                .mapToObj(_ -> Arrays.asList("empty"))
                .toList();
        direction = DIRECTION.NORTH;
        body = new LinkedList<>(List.of(new Point(5, 5), new Point(5, 6), new Point(5, 7)));
        body.forEach(point -> visibleMap.get(point.y()*mapWidth+point.x()).set(0, "up"));
        addNewFood();
        createGameLoop();
    }

    private void createGameLoop() {
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

    private void setMap(Point head, DIRECTION direction) {
        visibleMap.get(head.y()*mapWidth+head.x()).set(0, direction.getCode());
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
        System.out.println(direction);
    }

    private void doTick(){
        if (updateMapCallback!=null) {
            var newHead = body.getFirst().move(direction.getDirection());
            var last = body.removeLast();
            detectCollision(newHead,visibleMap);
            body.addFirst(newHead);
            visibleMap.get(last.y()*mapWidth+last.x()).set(0, "empty");
            visibleMap.get(newHead.y()*mapWidth+newHead.x()).set(0, direction.getCode());
            theMap.setAllTiles(visibleMap);
            updateMapCallback.run();
        }
    }

    private void detectCollision(Point newHead, List<List<String>> visibleMap) {
        var value = visibleMap.get(newHead.y()*mapWidth+newHead.x()).get(0);
        if (value.equals("food")) {
            increaseLength();
            addNewFood();
        } else if (!value.equals("empty")) {
            gameOver();
        }
    }

    private void addNewFood() {
        var free_tiles = visibleMap.size() - body.size();
        visibleMap.stream()
                .filter(list -> list.get(0).equals("empty"))
                .skip(new java.util.Random().nextInt(free_tiles))
                .findFirst()
                .ifPresent(list -> list.set(0, "food"));

    }

    private void gameOver() {
        System.out.println(ANSI_RED + "Game over"+ ANSI_RESET);
    }

    private void increaseLength() {
        body.add(body.getLast());
        body.add(body.getLast());
    }


    public TheMap createMap() {
       theMap = new TheMap(mapWidth,mapHeight, 50, 30);
       theMap.setAllTiles(visibleMap);
       return theMap;
    }

    public void setUpdateCallBack(Runnable updateMap) {
        updateMapCallback = updateMap;
    }
}

enum DIRECTION {

    NORTH("up", new Point(0, -1)),
    EAST("right", new Point(1, 0)),
    SOUTH("down", new Point(0, 1)),
    WEST("left", new Point(-1, 0));

   private final String code;
    private final Point direction;

    DIRECTION(String code, Point direction) {
        this.code = code;
        this.direction = direction;
    }

    public String getCode() {
        return code;
    }

    public Point getDirection() {
        return direction;
    }
}

record Point(int x, int y) {

    Point move(Point direction) {
        var newX = x + direction.x<0 ? Engine.mapWidth-1 : (x + direction.x>Engine.mapWidth-1 ? 0 : x + direction.x);
        var newY = y + direction.y<0 ? Engine.mapHeight-1 : (y + direction.y>Engine.mapHeight-1 ? 0 : y + direction.y);
        return new Point(newX, newY);
    }

}
