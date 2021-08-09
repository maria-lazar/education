package domain;

import util.EncoderUtils;

import java.util.ArrayList;
import java.util.List;

public class P3Image {
    private int height;
    private int width;
    private double maxRange;
    private double[][] r;
    private double[][] g;
    private double[][] b;
    private double[][] y;
    private double[][] u;
    private double[][] v;

    public P3Image(int height, int width, double maxRange, double[][] r, double[][] g, double[][] b) {
        this.height = height;
        this.width = width;
        this.maxRange = maxRange;
        this.r = r;
        this.g = g;
        this.b = b;
        y = new double[height][width];
        u = new double[height][width];
        v = new double[height][width];
        EncoderUtils.fromRGBToYUV(r, g, b, y, u, v, maxRange);
    }

    public double[][] getR() {
        return r;
    }

    public void setR(double[][] r) {
        this.r = r;
    }

    public double[][] getG() {
        return g;
    }

    public void setG(double[][] g) {
        this.g = g;
    }

    public double[][] getB() {
        return b;
    }

    public void setB(double[][] b) {
        this.b = b;
    }

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        this.y = y;
    }

    public double[][] getU() {
        return u;
    }

    public void setU(double[][] u) {
        this.u = u;
    }

    public double[][] getV() {
        return v;
    }

    public void setV(double[][] v) {
        this.v = v;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public List<Block> getYUVBlocks(String type) {
        double[][] matrix;
        if (type.equals("Y")) {
            matrix = y;
        } else if (type.equals("U")) {
            matrix = u;
        } else {
            matrix = v;
        }
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < height; i += 8) {
            for (int j = 0; j < width; j += 8) {
                Block block = new Block(type, i, j, matrix);
                blocks.add(block);
            }
        }
        return blocks;
    }
}
