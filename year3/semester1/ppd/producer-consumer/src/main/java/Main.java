public class Main {
    public static void main(String[] args) {
        int n = 5;
        int g = 10000;
        int nrMaxMon = 100;
        int p1 = Integer.parseInt(args[0]);
        int p2 = Integer.parseInt(args[1]);
//        UtilsIO.createFiles(n, g, nrMaxMon);

        try {
            AddPolynomials.parallelVariant(p1, p2, n);
//            AddPolynomials.sequentialVariant(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(UtilsIO.equalFilesValues("data/result.txt", "data/result2.txt"));
    }
}
