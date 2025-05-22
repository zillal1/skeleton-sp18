package hw2;

import edu.princeton.cs.introcs.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PercolationStats {
    private final double[] thresholds;
    private final int N;
    private final int T;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        thresholds = new double[T];
        this.N = N;
        this.T = T;
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be greater than 0");
        }
        for (int t = 0; t < T; t++) {
            Percolation p = pf.make(N);
            /*while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                p.open(row, col);
            }*/
            List<int []> positions = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    positions.add(new int[]{i, j});
                }
            }
            Collections.shuffle(positions);
            for (int[] position : positions) {
                int row = position[0];
                int col = position[1];
                p.open(row, col);
                if (p.percolates()) {
                    break;
                }
            }
            thresholds[t] = (double) p.numberOfOpenSites() / (N * N);
        }
    }  // perform T independent experiments on an N-by-N grid
    public double mean() {
        double sum = 0;
        for (double threshold : thresholds) {
            sum += threshold;
        }
        return sum / thresholds.length;
    }
    public double stddev() {
        double mean = mean();
        double sum = 0;
        for (double threshold : thresholds) {
            sum += Math.pow(threshold - mean, 2);
        }
        return Math.sqrt(sum / (thresholds.length - 1));
    }
    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        return mean - (1.96 * stddev / Math.sqrt(T));
    }
    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        return mean + (1.96 * stddev / Math.sqrt(T));
    }

}
