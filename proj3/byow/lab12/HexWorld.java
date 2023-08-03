package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {


    private enum Direction {
        TOP,
        BOTTOM,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }


    private static final int WIDTH = 50;

    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        int xCor = 0;
        int yCor = 0;
        addHexagon(3, world, xCor, yCor);
        xCor = changeStartX(xCor, Direction.TOP, 3);
        yCor = changeStartY(yCor, Direction.TOP, 3);
        addHexagon(3, world, xCor, yCor);
        xCor = changeStartX(xCor, Direction.TOP_RIGHT, 3);
        yCor = changeStartY(yCor, Direction.TOP_RIGHT, 3);
        addHexagon(3, world, xCor, yCor);
        xCor = changeStartX(xCor, Direction.BOTTOM, 3);
        yCor = changeStartY(yCor, Direction.BOTTOM, 3);
        addHexagon(3, world, xCor, yCor);

        renderer.renderFrame(world);
    }

    private static int changeStartX(int x, Direction direction, int sideLength) {
        int incrementedSpace = 2 * sideLength - 1;
        switch (direction) {

            case TOP_LEFT:
            case BOTTOM_LEFT:
                x = x - incrementedSpace;
                break;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                x = x + incrementedSpace;
                break;
        }
        return x;
    }
    private static int changeStartY(int y, Direction direction, int sideLength) {
        int incrementedSpace;
        switch (direction) {
            case TOP:
                incrementedSpace = 2 * sideLength;
                y = y + incrementedSpace;
                break;
            case TOP_LEFT:
            case TOP_RIGHT:
                incrementedSpace = sideLength;
                y = y + incrementedSpace;
                break;
            case BOTTOM:
                incrementedSpace = 2 * sideLength;
                y = y - incrementedSpace;
                break;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                incrementedSpace = sideLength;
                y = y - incrementedSpace;
                break;
        }
        return y;
    }

    private static void addHexagon(int sideLength, TETile[][] world, int xCor, int yCor) {
        int space = sideLength - 1;
        /** add top half */
        for (int i = 0; i < sideLength; i++) {
            int startX = xCor + space - i;
            int startY = yCor + i;
            int wallNum = sideLength + 2 * i;
            addLine(startX, startY, wallNum, world);
        }
        /** add bottom half */
        for (int i = 0; i < sideLength; i++) {
            int startX = xCor + space - i;
            int startY = yCor + sideLength * 2 - i - 1;
            int wallNum = sideLength + 2 * i;
            addLine(startX, startY, wallNum, world);
        }

    }

    private static void addLine(int xCor, int yCor, int num, TETile[][] world) {
        for (int i = 0; i < num; i++) {
            world[xCor + i][yCor] = Tileset.WALL;
        }
    }

}
