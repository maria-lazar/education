package util;

import domain.Block;

import java.util.ArrayList;
import java.util.List;

public class EncoderUtils {

    public static void fromRGBToYUV(double[][] r, double[][] g, double[][] b, double[][] y, double[][] u, double[][] v, double maxRange) {
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                y[i][j] = EncoderUtils.transformY(r[i][j], g[i][j], b[i][j], maxRange);
                u[i][j] = EncoderUtils.transformU(r[i][j], g[i][j], b[i][j], maxRange);
                v[i][j] = EncoderUtils.transformV(r[i][j], g[i][j], b[i][j], maxRange);
            }
        }
    }

    private static double transformY(double r, double g, double b, double maxRange) {
        double y = 0.299 * r + 0.587 * g + 0.114 * b;
        return EncoderUtils.verifyRange(y, maxRange);
    }

    private static double transformU(double r, double g, double b, double maxRange) {
        double u = 128 - 0.169 * r - 0.331 * g + 0.499 * b;
        return EncoderUtils.verifyRange(u, maxRange);
    }

    private static double transformV(double r, double g, double b, double maxRange) {
        double v = 128 + 0.499 * r - 0.418 * g - 0.0813 * b;
        return EncoderUtils.verifyRange(v, maxRange);
    }

    public static double verifyRange(double nr, double maxRange) {
        if (nr > maxRange) {
            return maxRange;
        }
        if (nr < 0) {
            return 0;
        }
        return nr;
    }


    public static double[][] populate8x8BlockValues(int i, int j, double[][] m) {
        double[][] values8 = new double[8][8];
        int i1 = 0;
        int j1 = 0;
        for (int k = i; k < i + 8; k++) {
            for (int p = j; p < j + 8; p++) {
                values8[i1][j1++] = m[k][p];
            }
            i1++;
            j1 = 0;
        }
        return values8;
    }

    public static double[][] subsampling(double[][] values8) {
        double[][] values = new double[4][4];
        for (int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 8; j += 2) {
                values[i / 2][j / 2] = EncoderUtils.getAverage(i, j, values8);
            }
        }
        return values;
    }

    private static Double getAverage(int i, int j, double[][] values8) {
        return (values8[i][j] + values8[i][j + 1] + values8[i + 1][j] + values8[i + 1][j + 1]) / 4;
    }

    public static void convertUVBlockValuesTo8x8(List<Block> blocks) {
        for (Block block : blocks) {
            block.setValues(DecoderUtils.from4x4To8x8(block.getValues()));
        }
    }

    public static void applyForwardDCT(List<Block> blocks) {
        for (Block block : blocks) {
            double[][] values = block.getValues();
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    values[j][k] -= 128;
                }
            }
            double[][] dctValues = new double[8][8];
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    dctValues[j][k] = getForwardDCTValue(j, k, values);
                }
            }
            block.setValues(dctValues);
        }
    }

    private static double getForwardDCTValue(int u, int v, double[][] g) {
        double result = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                result += g[x][y] * Math.cos(((2 * x + 1) * u * Math.PI) / 16) * Math.cos(((2 * y + 1) * v * Math.PI) / 16);
            }
        }
        double alfaU = 1;
        if (u == 0) {
            alfaU = 1 / Math.sqrt(2);
        }
        double alfaV = 1;
        if (v == 0) {
            alfaV = 1 / Math.sqrt(2);
        }
        result = ((float) 1 / 4) * alfaU * alfaV * result;
        return result;
    }

    public static void applyQuantization(List<Block> blocks, double[][] q) {
        for (Block block : blocks) {
            double[][] values = block.getValues();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    values[i][j] = Math.round(values[i][j] / q[i][j]);
                }
            }
            block.setValues(values);
        }
    }

    public static void dctAndQuantization(List<Block> yBlocks, List<Block> uBlocks, List<Block> vBlocks, double[][] q) {
        EncoderUtils.convertUVBlockValuesTo8x8(uBlocks);
        EncoderUtils.convertUVBlockValuesTo8x8(vBlocks);

        EncoderUtils.applyForwardDCT(yBlocks);
        EncoderUtils.applyForwardDCT(uBlocks);
        EncoderUtils.applyForwardDCT(vBlocks);

        EncoderUtils.applyQuantization(yBlocks, q);
        EncoderUtils.applyQuantization(uBlocks, q);
        EncoderUtils.applyQuantization(vBlocks, q);
    }

    public static double[] zigzagParse(double[][] values) {
        double[] result = new double[64];
        int p = 0;
        for (int i = 1; i < 9; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < i; j++) {
                    result[p++] = values[j][i - j - 1];
                }
            } else {
                for (int j = i - 1; j >= 0; j--) {
                    result[p++] = values[j][i - j - 1];
                }
            }
        }
        for (int i = 1; i < 8; i++) {
            if (i % 2 == 0) {
                for (int j = i; j < 8; j++) {
                    result[p++] = values[j][8 + i - j - 1];
                }
            } else {
                for (int j = 7; j >= i; j--) {
                    result[p++] = values[j][8 + i - j - 1];
                }
            }
        }
        return result;
    }


    public static List<Integer> entropyEncodeBlock(Block b) {
        double[] result = zigzagParse(b.getValues());

        List<Integer> entropyElements = new ArrayList<>();
        // DC coefficient
        int sizeDC = findInterval(result[0]);
        entropyElements.add(sizeDC); // size
        entropyElements.add((int) result[0]); // amplitude
        // AC coefficients
        int zeroCount = 0;
        for (int i = 1; i < 64; i++) {
            if (result[i] == 0) {
                // not encoded
                zeroCount++;
            } else {
                int size = findInterval(result[i]);
                entropyElements.add(zeroCount); // runlength
                entropyElements.add(size); // size
                entropyElements.add((int) result[i]); // amplitude
                zeroCount = 0;
            }
        }
        if (zeroCount > 0) {
            entropyElements.add(0);
            entropyElements.add(0);
        }
        return entropyElements;
    }


    public static int findInterval(double x) {
        int size = 0;
        int value;
        if (x > 0) {
            value = 1;
            while (x >= value) {
                value = value * 2;
                size++;
            }
        } else {
            value = -1;
            while (x <= value) {
                value = value * 2;
                size++;
            }
        }
        return size;
    }


    public static List<Integer> entropyEncode(List<Block> yBlocks, List<Block> uBlocks, List<Block> vBlocks) {
        List<Integer> entropyElements = new ArrayList<>();
        for (int i = 0; i < yBlocks.size(); i++) {
            List<Integer> blockList = entropyEncodeBlock(yBlocks.get(i));
            entropyElements.addAll(blockList);
            blockList = entropyEncodeBlock(uBlocks.get(i));
            entropyElements.addAll(blockList);
            blockList = entropyEncodeBlock(vBlocks.get(i));
            entropyElements.addAll(blockList);
        }
        return entropyElements;
    }
}
