package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static byow.Core.Position.Direction;




public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        ter.initialize(WIDTH, HEIGHT);
        StringInputDevice inputDevice = new StringInputDevice(input);
        Random random = new Random(inputDevice.getSeed());

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        generateWorld(random, finalWorldFrame);

        return finalWorldFrame;
    }

    public void generateWorld(Random random, TETile[][] world) {
        Position position = new Position(30, 20, random);
        int width = 3 + random.nextInt(MAX_ROOM_SIZE);
        int height = 3 + random.nextInt(MAX_ROOM_SIZE);
        RoomDirection roomDirection = parseDirection(position.getDirection(), random);
        checkRoomOccupied(world, position, width, height, position.getDirection(), roomDirection);
        position = generateRectangleRoom(width, height, random, world, position, roomDirection);

    }

    private static final int MAX_ROOM_SIZE = 10;
    private static final int MAX_WALL_NUM = 4 * MAX_ROOM_SIZE;


    enum RoomDirection {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    private RoomDirection parseDirection(Direction direction, Random random) {
        int i = random.nextInt(2);
        if (direction == Direction.TOP) {
            if (i == 0) {
                return RoomDirection.TOP_LEFT;
            } else {
                return RoomDirection.TOP_RIGHT;
            }
        } else if (direction == Direction.BOTTOM) {
            if (i == 0) {
                return RoomDirection.BOTTOM_LEFT;
            } else {
                return RoomDirection.BOTTOM_LEFT;
            }
        } else if (direction == Direction.LEFT) {
            if (i == 0) {
                return RoomDirection.TOP_LEFT;
            } else {
                return RoomDirection.BOTTOM_LEFT;
            }
        } else if (direction == Direction.RIGHT) {
            if (i == 0) {
                return RoomDirection.TOP_RIGHT;
            } else {
                return RoomDirection.BOTTOM_RIGHT;
            }
        }
        return null;
    }


    private int changeSignX(int x, RoomDirection roomDirection) {
        if (roomDirection == RoomDirection.TOP_LEFT || roomDirection == RoomDirection.BOTTOM_LEFT) {
            return -x;
        }
        return x;
    }

    private int changeSignY(int y, RoomDirection roomDirection) {
        if (roomDirection == RoomDirection.TOP_LEFT || roomDirection == RoomDirection.TOP_RIGHT) {
            return y;
        }
        return -y;
    }
    private int generateRectangleRoomHelp (int innitialX, int innitialY, int width, int height, TETile[][] world, Position[] positions,
                                            int coorX, int coorY, RoomDirection roomDirection, Direction direction1,
                                            Direction direction2, Direction direction3, Direction direction4) {
        int wallNum = 0;
        for (int x = innitialX; x < width; x++) {
            if (x == innitialX || x == width - 1) {
                for (int y = innitialY; y < height; y++) {
                    int newX = coorX + changeSignX(x, roomDirection);
                    int newY = coorY + changeSignY(y, roomDirection);
                    /** invariant: newX is x_coor newY is y_coor */
                    if (!(x == 0 && y == 0)) {
                        world[newX][newY] = Tileset.WALL;
                        if (x == innitialX) {
                            positions[wallNum] = new Position(newX, newY, direction1);
                        } else if (x == width - 1) {
                            positions[wallNum] = new Position(newX, newY, direction2);
                        }
                        wallNum++;
                    }

                }
            } else {
                int newX = coorX + changeSignX(x, roomDirection);
                int newY1 = coorY + changeSignY(-1, roomDirection);
                int newY2 = coorY + changeSignY(height - 1, roomDirection);
                world[newX][newY1] = Tileset.WALL;
                world[newX][newY2] = Tileset.WALL;
                positions[wallNum] = new Position(newX, newY1, direction3);
                wallNum++;
                positions[wallNum] = new Position(newX, newY2, direction4);
                wallNum++;
            }
        }
        return wallNum;
    }

    public int roomHelp (Direction direction, RoomDirection roomDirection, int width, int height, TETile[][] world, Position[] positions,
                          Position position) {
        if (direction == Direction.TOP || direction == Direction.BOTTOM) {
            switch (roomDirection) {
                case TOP_LEFT:
                    return generateRectangleRoomHelp(-1, 0, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.RIGHT, Direction.LEFT, Direction.BOTTOM, Direction.TOP);
                case TOP_RIGHT:
                    return generateRectangleRoomHelp(-1, 0, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.LEFT, Direction.RIGHT, Direction.BOTTOM, Direction.TOP);
                case BOTTOM_LEFT:
                    return generateRectangleRoomHelp(-1, 0, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.RIGHT, Direction.LEFT, Direction.TOP, Direction.BOTTOM);
                case BOTTOM_RIGHT:
                    return generateRectangleRoomHelp(-1, 0, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.LEFT, Direction.RIGHT, Direction.TOP, Direction.BOTTOM);
            }

        } else {
            switch (roomDirection) {
                case TOP_LEFT:
                    return generateRectangleRoomHelp(0, -1, width, height, world, positions, position.getyCor(), position.getxCor(),
                            roomDirection, Direction.RIGHT, Direction.LEFT, Direction.BOTTOM, Direction.TOP);
                case TOP_RIGHT:
                    return generateRectangleRoomHelp(0, -1, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.LEFT, Direction.RIGHT, Direction.BOTTOM, Direction.TOP);
                case BOTTOM_LEFT:
                    return generateRectangleRoomHelp(0, -1, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.RIGHT, Direction.LEFT, Direction.TOP, Direction.BOTTOM);
                case BOTTOM_RIGHT:
                    return generateRectangleRoomHelp(0, -1, width, height, world, positions, position.getxCor(), position.getyCor(),
                            roomDirection, Direction.LEFT, Direction.RIGHT, Direction.TOP, Direction.BOTTOM);
            }
        }
        return -1;
    }



    private Position generateRectangleRoom(int width, int height, Random random, TETile[][] world, Position position, RoomDirection roomDirection) {

        Position[] positions = new Position[MAX_WALL_NUM];

        Direction currentDirection = position.getDirection();

        int wallNum = roomHelp(currentDirection, roomDirection, width, height, world, positions, position);
        wallNum--;

        /** set rectangle to occupied */
        setRectangleOccupied(world, position, width, height);

        /** get newPosition */
        Position newPosition = getRandomPosition(positions, random, wallNum);
        world[newPosition.getxCor()][newPosition.getyCor()] = Tileset.NOTHING;
        newPosition.movePosition();

        return newPosition;

    }

    private Position getRandomPosition(Position[] positions, Random random, int wallNum) {
        int randomWall = random.nextInt(wallNum);
        return positions[randomWall];
    }


    private static final int MAX_HALLWAY_LENGTH = 10;
    private void generateHall(Random random, TETile[][] world, Position position) {
        int width = 1 + random.nextInt(2);
        int length = 1 + random.nextInt(MAX_HALLWAY_LENGTH);


    }

    private void drawWalls() {

    }

    private boolean checkOccupied(TETile[][] world,  Position position) {
        /** check position out of bound
         * then check whether the position is occupied*/
        if (position.getxCor() >= WIDTH || position.getxCor() < 0 || position.getyCor() >= HEIGHT || position.getyCor() < 0) {
            return true;
        } else if (world[position.getxCor()][position.getyCor()].isOccupied()) {
            return true;
        }
        return false;
    }

    private boolean checkRoomOccupied(TETile[][] world, Position position, int width, int height,
                                      Direction direction, RoomDirection roomDirection) {
        if (direction == Direction.TOP || direction == Direction.BOTTOM) {
            return checkRoomOccupiedHelp(-1, 0, world, position, width, height, roomDirection);
        } else {
            return checkRoomOccupiedHelp(0, -1, world, position, width, height, roomDirection);
        }

    }

    private boolean checkRoomOccupiedHelp(int innitialX, int innitialY, TETile[][] world, Position position, int width, int height,
                                          RoomDirection roomD) {

        /** check four corners */
        Position position1 = new Position(position.getxCor() + changeSignX(innitialX, roomD), position.getyCor() + changeSignY(innitialY, roomD));
        if (checkOccupied(world, position1)) {
            return true;
        }
        Position position2 = new Position(position.getxCor() + changeSignX(width, roomD), position.getyCor());
        if (checkOccupied(world, position2)) {
            return true;
        }
        Position position3 = new Position(position.getxCor() + changeSignX(innitialX, roomD), position.getyCor() + changeSignY(height, roomD));
        if (checkOccupied(world, position3)) {
            return true;
        }
        Position position4 = new Position(position.getxCor() + changeSignX(width, roomD), position.getyCor() + changeSignY(height, roomD));
        if (checkOccupied(world, position4)) {
            return true;
        }
        return false;
    }



    private void setRectangleOccupied(TETile[][] world, Position position, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[position.getxCor() + x][position.getyCor() + y].SetOccupied();
            }
        }
    }
    public void renderFrame(TETile[][] world) {
        ter.renderFrame(world);
    }


}
