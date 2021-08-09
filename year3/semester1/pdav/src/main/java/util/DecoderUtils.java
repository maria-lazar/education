package util;

import domain.Block;

import java.util.ArrayList;
import java.util.List;

public class DecoderUtils {

    public static double[][] getYUVMatrix(List<Block> blocks, int height, int width) {
        double[][] m = new double[height][width];
        for (Block block : blocks) {
            populateMatrixWithBlockValues(block, m, block.getValues());
        }
        return m;
    }

    public static double[][] from4x4To8x8(double[][] values) {
        double[][] result = new double[8][8];
        for (int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 8; j += 2) {
                result[i][j] = values[i / 2][j / 2];
                result[i][j + 1] = values[i / 2][j / 2];
                result[i + 1][j] = values[i / 2][j / 2];
                result[i + 1][j + 1] = values[i / 2][j / 2];
            }
        }
        return result;
    }


    private static void populateMatrixWithBlockValues(Block block, double[][] m, double[][] values) {
        int i1 = 0;
        int j1 = 0;
        for (int i = block.getI(); i < block.getI() + 8; i++) {
            for (int j = block.getJ(); j < block.getJ() + 8; j++) {
                m[i][j] = values[i1][j1];
                j1++;
            }
            i1++;
            j1 = 0;
        }
    }

    public static void fromYUVToRGB(double[][] r, double[][] g, double[][] b, double[][] y, double[][] u, double[][] v, double maxRange) {
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                r[i][j] = DecoderUtils.transformR(y[i][j], u[i][j], v[i][j], maxRange);
                g[i][j] = DecoderUtils.transformG(y[i][j], u[i][j], v[i][j], maxRange);
                b[i][j] = DecoderUtils.transformB(y[i][j], u[i][j], v[i][j], maxRange);
            }
        }
    }

    private static double transformR(double y, double u, double v, double maxRange) {
        double r = y + 1.402 * (v - 128);
        return EncoderUtils.verifyRange(r, maxRange);
    }

    private static double transformG(double y, double u, double v, double maxRange) {
        double g = y - 0.344 * (u - 128) - (v - 128) * 0.714;
        return EncoderUtils.verifyRange(g, maxRange);
    }

    private static double transformB(double y, double u, double v, double maxRange) {
        double b = y + 1.772 * (u - 128);
        return EncoderUtils.verifyRange(b, maxRange);
    }

    public static void dequantizationAndInverseDCT(List<Block> yBlocks, List<Block> uBlocks, List<Block> vBlocks, double[][] q) {
        applyDequantization(yBlocks, q);
        applyDequantization(uBlocks, q);
        applyDequantization(vBlocks, q);

        applyInverseDCT(yBlocks);
        applyInverseDCT(uBlocks);
        applyInverseDCT(vBlocks);
    }

    private static void applyInverseDCT(List<Block> blocks) {
        for (Block block : blocks) {
            double[][] dctValues = block.getValues();
            double[][] values = new double[8][8];
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    values[j][k] = getInverseDCTValue(j, k, dctValues);
                }
            }
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    values[j][k] += 128;
                }
            }
            block.setValues(values);
        }
    }

    private static double getInverseDCTValue(int x, int y, double[][] F) {
        double alfaU, alfaV;

        double result = 0;
        for (int u = 0; u < 8; u++) {
            alfaU = 1;
            if (u == 0) {
                alfaU = 1 / Math.sqrt(2);
            }
            for (int v = 0; v < 8; v++) {
                alfaV = 1;
                if (v == 0) {
                    alfaV = 1 / Math.sqrt(2);
                }
                result += alfaU * alfaV * F[u][v] * Math.cos(((2 * x + 1) * u * Math.PI) / 16) * Math.cos(((2 * y + 1) * v * Math.PI) / 16);
            }
        }
        result = ((float) 1 / 4) * result;
        return result;
    }

    private static void applyDequantization(List<Block> blocks, double[][] q) {
        for (Block block : blocks) {
            double[][] values = block.getValues();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    values[i][j] = values[i][j] * q[i][j];
                }
            }
            block.setValues(values);
        }
    }

    public static void entropyDecode(List<Integer> entropyElements, List<Block> yBlocks, List<Block> uBlocks, List<Block> vBlocks, int height, int width) {
        // get list of bytes for each block
        List<List<Integer>> blocks = splitValuesPerBlocks(entropyElements);
        int l = 0;
        int c = 0;
        for (int i = 0; i < blocks.size(); i++) {
            double[][] values = entropyDecodeBlock(blocks.get(i));

            Block block = new Block(l, c, values);
            if (i % 3 == 0) {
                block.setType("Y");
                yBlocks.add(block);
            } else if (i % 3 == 1) {
                block.setType("U");
                uBlocks.add(block);
            } else {
                block.setType("V");
                vBlocks.add(block);
                if (c == width - 8) {
                    l += 8;
                    c = 0;
                } else {
                    c += 8;
                }
            }
        }
    }

    public static List<List<Integer>> splitValuesPerBlocks(List<Integer> entropyElements) {
        List<List<Integer>> blocks = new ArrayList<>();
        int k = 0;
        while (k < entropyElements.size()) {
            // list of bytes for next block
            List<Integer> blockBytes = new ArrayList<>();
            blockBytes.add(entropyElements.get(k++)); // DC size
            blockBytes.add(entropyElements.get(k++)); // DC amplitude
            int count = 1; // number of block elements added
            while (count < 64) {
                if (entropyElements.get(k) == 0 && entropyElements.get(k + 1) == 0) {
                    blockBytes.add(0);
                    blockBytes.add(0);
                    k = k + 2;
                    break;
                } else {
                    int runLength = entropyElements.get(k++);
                    int size = entropyElements.get(k++);
                    int amplitude = entropyElements.get(k++);
                    blockBytes.add(runLength);
                    blockBytes.add(size);
                    blockBytes.add(amplitude);
                    count += runLength + 1;
                }
            }
            blocks.add(blockBytes);
        }
        return blocks;
    }

    private static double[][] entropyDecodeBlock(List<Integer> blockBytes) {
        List<Integer> beforeRunLengthEncoding = beforeRunLengthEncoding(blockBytes); // get the list of 64 elements of block
        return zigzagParse(beforeRunLengthEncoding);
    }

    private static List<Integer> beforeRunLengthEncoding(List<Integer> blockBytes) {
        List<Integer> beforeRunLengthEncoding = new ArrayList<>();
        beforeRunLengthEncoding.add(blockBytes.get(1)); // DC coefficient
        // AC coefficients
        for (int i = 2; i < blockBytes.size(); i += 3) {
            Integer runLength = blockBytes.get(i);
            Integer size = blockBytes.get(i + 1);
            if (size == 0) {
                // 2 consecutive zeros => end of values
                break;
            }
            Integer amplitude = blockBytes.get(i + 2);
            for (int j = 0; j < runLength; j++) {
                beforeRunLengthEncoding.add(0); // add 0 elements(which were not encoded)
            }
            beforeRunLengthEncoding.add(amplitude);
        }
        int currentSize = beforeRunLengthEncoding.size();
        // add consecutive zeros from end
        for (int j = 0; j < 64 - currentSize; j++) {
            beforeRunLengthEncoding.add(0);
        }
        return beforeRunLengthEncoding;
    }

    private static double[][] zigzagParse(List<Integer> elements) {
        double[][] values = new double[8][8];
        int p = 0; // index of current element from elements
        for (int i = 1; i < 9; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < i; j++) {
                    values[j][i - j - 1] = elements.get(p++);
                }
            } else {
                for (int j = i - 1; j >= 0; j--) {
                    values[j][i - j - 1] = elements.get(p++);
                }
            }
        }
        for (int i = 1; i < 8; i++) {
            if (i % 2 == 0) {
                for (int j = i; j < 8; j++) {
                    values[j][8 - j + i - 1] = elements.get(p++);
                }
            } else {
                for (int j = 7; j >= i; j--) {
                    values[j][8 - j + i - 1] = elements.get(p++);
                }
            }
        }
        return values;
    }
}
