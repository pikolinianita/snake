package pl.lcc.talisman.engine;

public enum DIRECTION {

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
