
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class FibHeapMeasurements {

    //----------------------------------------  Sequence1  -------------------------------------

    public static long Sequence1(int m) {

        FibonacciHeap h = new FibonacciHeap();
        FibonacciHeap.HeapNode[] nodesArr = new FibonacciHeap.HeapNode[m+1];

        long startTime = System.nanoTime();

        for (int j = m; j >= 0; j--) {
            FibonacciHeap.HeapNode x = h.insert(j);
            nodesArr[j] = x;
        }

        h.deleteMin();

        double delta = m + 1;
        for (int i = 0; i < Math.log(m) - 1; i++) {
            double sum = 0;
            for (int k = 1; k <= i; k++) {
                sum += Math.pow(0.5, k);
            }
            double valForDecreaseKey = (m * (sum)) + 2;
            h.decreaseKey(nodesArr[(int) valForDecreaseKey], (int) delta);
        }
        h.decreaseKey(nodesArr[m - 1], (int) delta);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        //System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

        //System.out.println("total links: " + FibonacciHeap.linksNum);
        //System.out.println("total cuts: " + FibonacciHeap.cutsNum );
        //System.out.println("potential: " + h.potential() + "\n");

        return (timeElapsed / 1000000); //Execution time in milliseconds
    }

    //----------------------------------------  Sequence1  -------------------------------------

    public static long Sequence2(int M) {

        FibonacciHeap h = new FibonacciHeap();

        long startTime = System.nanoTime();

        for (int j = M; j > 0; j--) {
            FibonacciHeap.HeapNode x = h.insert(j);
        }

        for (int i = 0; i < M/2; i++) {
            h.deleteMin();
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        //System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

        //System.out.println("total links: " + FibonacciHeap.linksNum);
        //System.out.println("total cuts: " + FibonacciHeap.cutsNum );
        //System.out.println("potential: " + h.potential() + "\n");

        FibonacciHeap.linksNum = 0;
        FibonacciHeap.cutsNum = 0;

        return (timeElapsed / 1000000); //Execution time in milliseconds

    }

    //--------------------------------------------  main  -------------------------------------------

    public static void main(String[] args) {

        System.out.println("------------------");
        System.out.println("Sequence 1");
        System.out.println("------------------");
        System.out.println();

        for (int exp = 8; exp < 13; exp++){
            int m = (int) Math.pow(2, exp);
            long[] timeArr1 = new long[100000];
            for (int i = 0; i < 100000; i++){
                timeArr1[i] = Sequence1(m);
            }
            double sumTime1 = 0;// Arrays.stream(timeArr1).sum();
            for (int j = 0; j<100000; j++){
                sumTime1 += timeArr1[j];
            }
            double meanTime1 = sumTime1/100000;

            if (exp > 9){
                System.out.println("m is: " + m);
                System.out.println("Execution time in milliseconds : " + meanTime1);
            }
        }

        System.out.println();
        System.out.println("------------------");
        System.out.println("Sequence 2");
        System.out.println("------------------");
        System.out.println();

        for (int fac = 5; fac > 0; fac--){
            int M = fac*1000;
            long[] timeArr2 = new long[10000];
            for (int i = 0; i < 10000; i++){
                timeArr2[i] = Sequence2(M);
            }
            double sumTime2 = 0;// Arrays.stream(timeArr1).sum();
            for (int j = 0; j<10000; j++){
                sumTime2 += timeArr2[j];
            }
            double meanTime2 = sumTime2/10000;

            if (fac < 4){
                System.out.println("M is: " + M);
                System.out.println("Execution time in milliseconds : " + meanTime2);
            }
        }





    }
}

