import static java.lang.Math.abs;
import static java.lang.Math.min;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }
    private void helper(double[][] M) {
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                if (j == 0) {
                    M[i][j] = energy(i, j); // Arbitrary value for the first pixel
                } else if (i == 0) {
                    M[i][j] = M[i][j - 1] + energy(i, j);
                } else {
                    M[i][j] = min(M[i - 1][j], min(M[i][j - 1], M[i - 1][j - 1])) + energy(i, j);
                }
            }
        }
    }
    public Picture picture() {
        return picture;
    }                      // current picture
    public     int width() {
        return picture.width();
    }                        // width of current picture
    public     int height() {
        return picture.height();
    }                       // height of current picture
    public double energy(int x, int y) {
        /* Energy of pixel at column x and row y */
        // Calculate the energy based on the color differences in the surrounding pixels
        // Return the energy value
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException("Pixel out of bounds");
        }
        if (width() == 1 && height() == 1) {
            return 0.0; // Special case for a single pixel
        }
        int leftColor = picture().get(x == 0 ? width() - 1 : x - 1, y).getRGB();
        int rightColor = picture().get(x == width() - 1 ? 0 : x + 1, y).getRGB();
        int topColor = picture().get(x, y == 0 ? height() - 1 : y - 1).getRGB();
        int bottomColor = picture().get(x, y == height() - 1 ? 0 : y + 1).getRGB();
        int rx = ((leftColor >> 16) & 0xFF) - ((rightColor >> 16) & 0xFF);
        int gx = ((leftColor >> 8) & 0xFF) - ((rightColor >> 8) & 0xFF);
        int bx = (leftColor & 0xFF) - (rightColor & 0xFF);
        int ry = ((topColor >> 16) & 0xFF) - ((bottomColor >> 16) & 0xFF);
        int gy = ((topColor >> 8) & 0xFF) - ((bottomColor >> 8) & 0xFF);
        int by = (topColor & 0xFF) - (bottomColor & 0xFF);
        return rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;

    }
    private void transpose() {
        Picture transposed = new Picture(height(), width());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transposed.set(j, i, picture.get(i, j));
            }
        }
        picture = transposed;
    } // transpose the current picture
    public   int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }           // sequence of indices for horizontal seam
    public   int[] findVerticalSeam() {
        double[][] M = new double[width()][height()];
        helper(M);
        int[] seam = new int[height()];
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int i = 0; i < width(); i++) {
            if (M[i][height() - 1] < minEnergy) {
                minEnergy = M[i][height() - 1];
                minIndex = i;
            }
        }
        seam[height() - 1] = minIndex;
        double epslon = 1e-6; // Small value to avoid floating-point precision issues
        for (int i = height() - 2; i >= 0; i--) {
            int currentIndex = seam[i + 1];
            double currentEnergy = energy(currentIndex, i + 1);
            if (abs(M[currentIndex][i] + currentEnergy
                    - M[currentIndex][i + 1]) < epslon) {
                seam[i] = currentIndex;
            } else if (currentIndex > 0
                    && abs(M[currentIndex - 1][i] + currentEnergy
                    - M[currentIndex][i + 1]) < epslon) {
                seam[i] = currentIndex - 1;
            } else if (currentIndex < width() - 1
                    && abs(M[currentIndex + 1][i] + currentEnergy
                    - M[currentIndex][i + 1]) < epslon) {
                seam[i] = currentIndex + 1;
            }
        }
        return seam;
    }             // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam) {
        if (seam.length != width()) {
            throw new IllegalArgumentException("Seam length does not match picture width");
        }
        for (int i = 1; i < width(); i++) {
            if (abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid seam: adjacent pixels differ by more than one");
            }
        }
        Picture newPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < seam[x]) {
                    newPicture.set(x, y, picture.get(x, y));
                } else if (y > seam[x]) {
                    newPicture.set(x, y - 1, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
    }  // remove horizontal seam from picture
    public    void removeVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new IllegalArgumentException("Seam length does not match picture height");
        }
        for (int i = 1; i < height(); i++) {
            if (abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid seam: adjacent pixels differ by more than one");
            }
        }
        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x < seam[y]) {
                    newPicture.set(x, y, picture.get(x, y));
                } else if (x > seam[y]) {
                    newPicture.set(x - 1, y, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
    }    // remove vertical seam from picture
}
