package byow.Core;

import java.util.Random;

public class Position {
    enum Direction {
        TOP,
        BOTTOM,
        RIGHT,
        LEFT,

    }

    private int xCor;
    private int yCor;

    private Direction direction = null;

    Position(int x, int y, Random random) {
        xCor = x;
        yCor = y;
        direction = changeRandomDirection(random, direction);
    }
    Position(int x, int y, Direction direction) {
        xCor = x;
        yCor = y;
        this.direction = direction;
    }

    Position(int x, int y) {
        xCor = x;
        yCor = y;
    }

    private static final int NUM_DIR = 4;
    private Direction changeRandomDirection(Random random, Direction direction) {
        int i = random.nextInt(NUM_DIR);
        Direction newDir = randomDirection(i);
        while (newDir == direction) {
            i = random.nextInt(NUM_DIR);
            newDir = randomDirection(i);
        }
        return newDir;
    }
    private Direction randomDirection(int i) {
        switch (i) {
            case 1:
                return Direction.TOP;
            case 2:
                return Direction.BOTTOM;
            case 3:
                return Direction.LEFT;
            case 4:
                return Direction.RIGHT;
        }
        return null;
    }
    public int getxCor() {
        return xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public void moveX(int x) {
        xCor += x;
    }
    public void moveY(int y) {
        yCor += y;
    }
    public Direction getDirection() {
        return direction;
    }

    public void movePosition() {
        switch (direction) {
            case TOP:
                moveY(1);
                break;
            case BOTTOM:
                moveY(-1);
                break;
            case LEFT:
                moveX(-1);
                break;
            case RIGHT:
                moveX(1);
                break;
        }
    }
}
