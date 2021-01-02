public class fibHeapTester {

    //---------------------------------------------- printers -------------------------------------------------------

    private static void p(String str) {
        System.out.println(str);
    }

    private static void sepetatorPrint(String heapKeys, String lastAction) {
        p("");
        p("");
        p("-------- Keys in tree: " + heapKeys + " ------------- Last action: " + lastAction + " ------------ :");
    }

    //-------------------------------error Printers: ----------------------------------
    //boolean:
    private static void errorPrinter(String testName, boolean wantedRes , boolean output ) {
        if (output != wantedRes) {
            p("error in: " + testName + ", suppose to be: " + wantedRes + ", output: " + output);
        }
    }
    //String:
    private static void errorPrinter(String testName, String wantedRes , String output ) {
        if (output != wantedRes) {
            p("error in: " + testName + ", suppose to be: " + wantedRes + ", output: " + output);
        }
    }
    //int:
    private static void errorPrinter(String testName, int wantedRes , int output ) {
        if (output != wantedRes) {
            p("error in: " + testName + ", suppose to be: " + wantedRes + ", output: " + output);
        }
    }
    //int[]:
    private static void errorPrinter(String testName, int[] wantedRes , int[] output ) {
        if (output != wantedRes) {
            p("error in: " + testName + ", suppose to be: " + wantedRes + ", output: " + output);
        }
    }
    //String[]:
    private static void errorPrinter(String testName, String[] wantedRes , String[] output ) {
        if (output != wantedRes) {
            p("error in: " + testName + ", suppose to be: " + wantedRes + ", output: " + output);
        }
    }

    //----------------------------------------------- tests ------------------------------------------------------------

    //tree testers:

    private static void testEmpty(FibonacciHeap h, boolean wantedRes) {
        errorPrinter("empty()", wantedRes, h.isEmpty());
    }

    private static void testMin(FibonacciHeap h, int wantedRes) {
        errorPrinter("min()", wantedRes, h.findMin().key);
    }

    private static void testSize(FibonacciHeap h, int wantedRes) {
        errorPrinter("size()", wantedRes, h.size());
    }

    private static void testTreesNum(FibonacciHeap h, int wantedRes) {
        errorPrinter("treeNum()", wantedRes, h.getNum_of_trees());
    }

    private static void testMarkedNum(FibonacciHeap h, int wantedRes) {
        errorPrinter("MarkedNum()", wantedRes, h.getMarkedNum());
    }

    //--------------------------------  heap testers ---------------------------------------------------------------------------------

    private static void heapTest(FibonacciHeap h, boolean empty, int min, int size, int treesNum, int markedNum){
        testEmpty(h, empty);
        testMin(h, min);
        testSize(h, size);
        testTreesNum(h, treesNum);
        testMarkedNum(h, markedNum);
    }

    private static void emptyHeapTest(FibonacciHeap h) {
        if (h.findMin() != null ) {
            p("bug in min in case of empty tree");
        }
        testEmpty(h, true);
        testSize(h, 0);
        testTreesNum(h, 0);
        testMarkedNum(h,0);
    }

    //------------------------------------------------- main ------------------------------------------------------

    public static void main(String[] args) {

        FibonacciHeap h = new FibonacciHeap();
        sepetatorPrint("no keys", "new AVLTree()");
        emptyHeapTest(h);

        //------------------------------------------------------------------------

        sepetatorPrint("5", "insert(5)");
        h.insert(5);
        heapTest(h, false, 5, 1, 1, 0);

        //----------------------------------------------------------------------------------------

        sepetatorPrint("5,4", "insert(4)");
        h.insert(4);
        heapTest(h, false, 4, 2, 2, 0);

        //----------------------------------------------------------------------------------------

        sepetatorPrint("5,4,6", "insert(6)");
        h.insert(6);
        heapTest(h, false, 4, 3, 3, 0);

        //------------------------- heap h2 with keys 7,1,9,3 -----------------------------------------

        sepetatorPrint("7,1,9,3 ", "new heap h2 with keys: 7,1,9,3 ");
        FibonacciHeap h2 = new FibonacciHeap();
        h2.insert(7);
        h2.insert(1);
        h2.insert(9);
        h2.insert(3);
        heapTest(h2, false, 1, 4, 4, 0);

        //------------------------  meld h with h2 ---------------------------------------------------

        sepetatorPrint("5,4,6,7,1,9,3", "meld with h2 (keys:7,1,9,3)");
        h.meld(h2);
        heapTest(h, false, 1, 7, 7,0);

        //-----------------------  heap h3 with keys 14, 0, -21, -2  -----------------------------------------

        sepetatorPrint("14, 0, -21, -2", "new heap h3 with keys: 14, 0, -21, -2");
        FibonacciHeap h3 = new FibonacciHeap();
        h3.insert(14);
        h3.insert(0);
        h3.insert(-21);
        h3.insert(-2);
        heapTest(h3, false, -21, 4, 4, 0);

        //-------------------- meld h with h3 ---------------------------------------------------

        sepetatorPrint("5,4,6,7,1,9,3 , 14, 0, -21, -2", "meld h with h3 ");
        h.meld(h3);
        heapTest(h, false, -21, 11, 11,0);

        //----------  new heap h4 with keys: -87, 55 build with the "one node constructor" -------------------------------------

        sepetatorPrint("-87, 55", "new heap h4 with keys: -87, 55");
        FibonacciHeap h4 = new FibonacciHeap(-87);
        heapTest(h4, false, -87, 1, 1,0);
        h4.insert(55);
        heapTest(h4, false, -87, 2, 2,0);

        //-------------------- meldAtFirst h with h4 ---------------------------------------------------

        sepetatorPrint("-87, 55, 5,4,6,7,1,9,3,14,0,-21,-2", "meldAtFirst h with h4 ");
        h.meldAtFirst(h4);
        heapTest(h, false, -87, 13, 13,0);

        //


        //------------------------------------------------------------------------------------------------
        p("\nDone!");
    }
}
