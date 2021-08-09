package domain;

import util.EncoderUtils;

import java.util.Arrays;

public class Block {
    protected String type;
    protected int i;
    protected int j;
    protected double[][] values;

    public double[][] getValues() {
        return values;
    }

    public Block(int i, int j, double[][] values) {
        this.i = i;
        this.j = j;
        this.values = values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    public Block() {
    }

    public Block(String type, int i, int j, double[][] m) {
        this.type = type;
        this.i = i;
        this.j = j;
        if (type.equals("Y")) {
            values = EncoderUtils.populate8x8BlockValues(i, j, m);
        } else {
            double[][] values8 = EncoderUtils.populate8x8BlockValues(i, j, m);
            values = EncoderUtils.subsampling(values8);
        }
    }

    @Override
    public String toString() {
        return "Block{" +
                "type='" + type + '\'' +
                ", i=" + i +
                ", j=" + j +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
