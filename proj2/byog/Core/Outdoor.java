package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Outdoor implements Serializable {
    private int x;
    private int y;
    private Random random;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Outdoor(long seed) {
        random = new Random(seed);
    }

    public void generateRandomOutdoor(TETile[][] world) {
        x = random.nextInt(world.length);
        y = random.nextInt(world[0].length);
        while (world[x][y] != Tileset.WALL) {
            if (random.nextBoolean()) {
                x = (x + random.nextInt(world.length)) % world.length;
            } else {
                y = (y + random.nextInt(world[0].length)) % world[0].length;
            }
        }
        if (world[x][y] == Tileset.WALL) {
            world[x][y] = Tileset.UNLOCKED_DOOR;
        }
    }
}
