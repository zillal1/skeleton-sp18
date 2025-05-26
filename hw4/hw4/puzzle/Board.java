package hw4.puzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class Board implements WorldState{

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    private final int[][] tiles;
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Tiles cannot be null");
        }
        int N = tiles.length;
        for (int i = 0; i < N; i++) {
            if (tiles[i].length != N) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
        }
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, N);
        }
    }
    public int tileAt(int i, int j) {
        if (i < 0 || i >= size() || j < 0 || j >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i + ", " + j);
        }
        return tiles[i][j];
    }
    public int size() {
        return tiles.length;
    }
    public Iterable<WorldState> neighbors() {
        int N = size();
        int zeroRow = -1, zeroCol = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
            if (zeroRow != -1) break;
        }

        // Directions: up, down, left, right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayList<WorldState> neighbors = new ArrayList<>();

        for (int[] dir : directions) {
            int newRow = zeroRow + dir[0];
            int newCol = zeroCol + dir[1];
            if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < N) {
                int[][] newTiles = new int[N][N];
                for (int i = 0; i < N; i++) {
                    System.arraycopy(tiles[i], 0, newTiles[i], 0, N);
                }
                // Swap the zero tile with the adjacent tile
                newTiles[zeroRow][zeroCol] = newTiles[newRow][newCol];
                newTiles[newRow][newCol] = 0;
                neighbors.add(new Board(newTiles));
            }
        }
        return neighbors;
    }
    public int hamming() {
        int count = 0;
        int N = size();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tile = tileAt(i, j);
                if (tile != 0 && tile != i * N + j + 1) {
                    count++;
                }
            }
        }
        return count;
    }
    public int manhattan() {
        int distance = 0;
        int N = size();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tile = tileAt(i, j);
                if (tile != 0) {
                    int targetRow = (tile - 1) / N;
                    int targetCol = (tile - 1) % N;
                    distance += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }
        return distance;
    }
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
