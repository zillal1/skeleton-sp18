package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int N;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final int topVirtual;
    private final int bottomVirtual;
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("N must be greater than 0");
        this.N = N;
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufFull = new WeightedQuickUnionUF(N * N + 1);
        topVirtual = N * N;
        bottomVirtual = N * N + 1;
    }               // create N-by-N grid, with all sites initially blocked
    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("row or column index out of bounds");
        }
        if (!grid[row][col]) {
            grid[row][col] = true;
            int index = row * N + col;
            if (row == 0) {
                uf.union(index, topVirtual);
                ufFull.union(index, topVirtual);
            }
            if (row == N - 1) uf.union(index, bottomVirtual);
            connectAdjacent(row, col);
        }
    }      // open the site (row, col) if it is not open already
    public void connectAdjacent(int row, int col) {
        int index = row * N + col;
        if (row > 0 && grid[row - 1][col]) {
            uf.union(index, (row - 1) * N + col);
            ufFull.union(index, (row - 1) * N + col);
        }
        if (row < N - 1 && grid[row + 1][col]) {
            uf.union(index, (row + 1) * N + col);
            ufFull.union(index, (row + 1) * N + col);
        }
        if (col > 0 && grid[row][col - 1]) {
            uf.union(index, row * N + col - 1);
            ufFull.union(index, row * N + col - 1);
        }
        if (col < N - 1 && grid[row][col + 1]) {
            uf.union(index, row * N + col + 1);
            ufFull.union(index, row * N + col + 1);
        }
    }
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("row or column index out of bounds");
        }
        return grid[row][col];
    } // is the site (row, col) open?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("row or column index out of bounds");
        }
        return ufFull.connected(topVirtual, row * N + col);
    } // is the site (row, col) full?
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }          // number of open sites
    public boolean percolates() {
        return uf.connected(topVirtual, bottomVirtual);
    }             // does the system percolate?
    public static void main(String[] args) {

    }  // use for unit testing (not required)
}
