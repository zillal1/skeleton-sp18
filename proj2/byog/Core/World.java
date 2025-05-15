package byog.Core;

import java.util.List;
import java.util.Random;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class World implements Serializable {
    private Random r = new Random();

    private int width;
    private int height;
    private long seed;
    private transient TETile[][] teTiles;
    private RoomGenerator generator;
    private Player player;
    private Outdoor outdoor;
    private List<Room> roomsAndTunnels;


    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;

        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }

        outdoor = new Outdoor(seed);

        generator = new RoomGenerator(width, height, seed);
        roomsAndTunnels = generator.generateRooms(teTiles);

        player = new Player(teTiles, seed);

        outdoor.generateRandomOutdoor(teTiles);
        //Room.toDrawOn(teTiles, roomsAndTunnels);

    }
    public TETile[][] getTeTiles() {
        return teTiles;
    }

    public Player getPlayer() {
        return player;
    }

    public void reset() {
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[outdoor.getX()][outdoor.getY()] = Tileset.UNLOCKED_DOOR;
    }


    public void resetLoad() {
        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[outdoor.getX()][outdoor.getY()] = Tileset.UNLOCKED_DOOR;
    }
    public void move(Toward direction) {
        player.move(this, direction);
    }
}
