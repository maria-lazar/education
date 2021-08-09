import domain.*;
import util.DecoderUtils;
import util.EncoderUtils;
import util.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            double[][] q = {
                    {6, 4, 4, 6, 10, 16, 20, 24},
                    {5, 5, 6, 8, 10, 23, 24, 22},
                    {6, 5, 6, 10, 16, 23, 28, 22},
                    {6, 7, 9, 12, 20, 35, 32, 25},
                    {7, 9, 15, 22, 27, 44, 41, 31},
                    {10, 14, 22, 26, 32, 42, 45, 37},
                    {20, 26, 31, 35, 41, 48, 48, 40},
                    {29, 37, 38, 39, 45, 40, 41, 40}};

            // ENCODER

            P3Image image = IOUtils.readP3File("nt-P3.ppm");
//            P3Image image = IOUtils.readP3File("p.ppm");

            List<Block> yBlocks = image.getYUVBlocks("Y");
            List<Block> uBlocks = image.getYUVBlocks("U");
            List<Block> vBlocks = image.getYUVBlocks("V");

            EncoderUtils.dctAndQuantization(yBlocks, uBlocks, vBlocks, q);

            List<Integer> arrayEntropy = EncoderUtils.entropyEncode(yBlocks, uBlocks, vBlocks);

//             DECODER

//            yBlocks.clear();
//            uBlocks.clear();
//            vBlocks.clear();
            List<Block> yBlocks2 = new ArrayList<>();
            List<Block> uBlocks2 = new ArrayList<>();
            List<Block> vBlocks2 = new ArrayList<>();

            DecoderUtils.entropyDecode(arrayEntropy, yBlocks2, uBlocks2, vBlocks2, image.getHeight(), image.getWidth());

//            System.out.println(IOUtils.equalListsOfBlocks(yBlocks, yBlocks2));
//            System.out.println(IOUtils.equalListsOfBlocks(uBlocks, uBlocks2));
//            System.out.println(IOUtils.equalListsOfBlocks(vBlocks, vBlocks2));

            DecoderUtils.dequantizationAndInverseDCT(yBlocks2, uBlocks2, vBlocks2, q);

            double[][] y = DecoderUtils.getYUVMatrix(yBlocks2, image.getHeight(), image.getWidth());
            double[][] u = DecoderUtils.getYUVMatrix(uBlocks2, image.getHeight(), image.getWidth());
            double[][] v = DecoderUtils.getYUVMatrix(vBlocks2, image.getHeight(), image.getWidth());

            double[][] r = new double[image.getHeight()][image.getWidth()];
            double[][] g = new double[image.getHeight()][image.getWidth()];
            double[][] b = new double[image.getHeight()][image.getWidth()];
            DecoderUtils.fromYUVToRGB(r, g, b, y, u, v, image.getMaxRange());

            IOUtils.writeImageToFile("out.ppm", r, g, b, image.getMaxRange());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
