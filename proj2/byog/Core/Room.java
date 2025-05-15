package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
    int x;
    int y;
    int width;
    int height;
    transient TETile[][] world;

    public Room(int x, int y, int width, int height, TETile[][] inputWorld) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = inputWorld;
    }

    public void roomOf(TETile[][] inputWorld) {
        this.world = inputWorld;
    }

    public boolean ifValid(TETile[][] inputWord) {
        int numXTiles = inputWord.length;
        int numYTiles = inputWord[0].length;
        return (x + width <= numXTiles && y + height <= numYTiles);
    }
    public boolean ifInside(Room room) {
        return (x < room.x + room.width && x + width > room.x
            && y < room.y + room.height && y + height > room.y);
    }

    private void drawWallOn(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        for (int i = x; i < x + width && i < numXTiles; i++) {
            for (int j = y; j < y + height && j < numYTiles; j++) {
                if (inputWorld[i][j] != Tileset.FLOOR) {
                    inputWorld[i][j] = Tileset.WALL;
                }
            }
        }
    }

    private void drawFloorOn(TETile[][] inputWorld) {
        for (int i = x + 1; i < x + width - 1 && i < inputWorld.length; i++) {
            for (int j = y + 1; j < y + height - 1 && j < inputWorld[0].length; j++) {
                inputWorld[i][j] = Tileset.FLOOR;
            }
        }
    }

    public void toDrawOn(TETile[][] inputWorld) {
        drawWallOn(inputWorld);
        drawFloorOn(inputWorld);
    }
    public static void toDrawOn(TETile[][] inputWorld, List<Room> rooms) {
        for (Room room : rooms) {
            room.toDrawOn(inputWorld);
        }
    }
}
