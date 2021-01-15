public class fibHeapTester {

    //---------------------------------------------- printers -------------------------------------------------------

    private static void printHeap(FibonacciHeap h){
        if(h.isEmpty()){
            System.out.println("empty heap");
            return;
        }
        printHeapRec(h.sentinel, 0);
    }

    private static void printHeapRec(FibonacciHeap.HeapNode firstBro, int level){
        if (firstBro != null){
            FibonacciHeap.HeapNode currBro = firstBro;
            do{
                for (int i = 0 ; i < level; i++){
                    System.out.print("\t");
                }
                if (currBro.rank != -1){
                    System.out.println(currBro.key);
                }
                printHeapRec(currBro.child, level+1);
                currBro = currBro.next;
            } while (currBro != firstBro);
        }
    }



    private static void p(String str) {
        System.out.println(str);
    }

    private static void sepetatorPrint( String lastAction) {
        p("");
        p("");
        p("----------------- Last action: " + lastAction + " ---------------------- :");
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
        errorPrinter("treeNum()", wantedRes, h.getTreesNum());
    }

    private static void testMarkedNum(FibonacciHeap h, int wantedRes) {
        errorPrinter("MarkedNum()", wantedRes, h.getMarkedNum());
    }

    //---------------

    private static void testLinksNum( int wantedRes) {
        errorPrinter("LinksdNum()", wantedRes, FibonacciHeap.linksNum);
    }

    private static void testCutsNum(int wantedRes) {
        errorPrinter("cutsNum()", wantedRes, FibonacciHeap.cutsNum);
    }

    //--------------------------------  heap testers ---------------------------------------------------------------------------------

    private static void heapTest(FibonacciHeap h, int min, int size, int treesNum, int markedNum){
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
        sepetatorPrint( "new AVLTree()");
        emptyHeapTest(h);

        //------------------------------------ inserts -----------------------------------------------

        sepetatorPrint( "insert 5, 4, 6");
        h.insert(5);
        h.insert(4);
        h.insert(6);
        heapTest(h, 4, 3, 3, 0);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, 5, 2, 1, 0);
        testLinksNum(1);
        printHeap(h);

        //---------------------------------- insert -9 -------------------------------------------

        sepetatorPrint("inset -9, 4, 10");
        h.insert(-9);
        h.insert(4);
        h.insert(10);
        heapTest(h,-9, 5, 4, 0);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, 4, 4, 1, 0);
        testLinksNum(3);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, 5, 3, 2, 0);
        testLinksNum(3);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, 6, 2, 1, 0);
        testLinksNum(4);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, 10, 1, 1, 0);
        testLinksNum(4);
        printHeap(h);

        //----------------------------------- delete min - to get an empty heap--------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        emptyHeapTest(h);
        testLinksNum(4);
        printHeap(h);

        //--------------------------------- new heap h2 with keys: 1, 7, 9, -4, -6  ---------------------

        sepetatorPrint("new heap h2 with keys: 1, 7, 9, -4, -6");
        FibonacciHeap h2 = new FibonacciHeap(-4);
        heapTest(h2, -4, 1, 1, 0);
        FibonacciHeap.HeapNode node9 = h2.insert(9);
        h2.insert(-6);
        h2.insert(7);
        FibonacciHeap.HeapNode node1 = h2.insert(1);
        heapTest(h2,-6, 5,5,0);
        printHeap(h2);

        //----------------------------- meld h2 to h ----------------------------------------------------

        sepetatorPrint("meld h2 to h (which is empty)");
        h.meld(h2);
        heapTest(h, -6, 5, 5, 0);
        printHeap(h);

        //----------------------------------- insert -12 -------------------------------------------
        sepetatorPrint("inset -12");
        h.insert(-12);
         heapTest(h, -12, 6, 6, 0);
         printHeap(h);
        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, -6, 5, 2, 0);
        testLinksNum(7);
        printHeap(h);

        //----------------------------------- new empty heap h3, meld it to h ----------------------------

        sepetatorPrint("new empty heap h3, meld it to h");
        FibonacciHeap h3 = new FibonacciHeap();
        h.meld(h3);
        heapTest(h, -6, 5, 2, 0);
        printHeap(h);

        //------------------------------ new heap h4 with 5, 0, 2, 11, -3, -20 ---------------------------

        sepetatorPrint("new heap h4 with 5, 0, 2, 11, -3, -20, -30");
        FibonacciHeap h4 = new FibonacciHeap();
        h4.insert(-30);
        h4.insert(-20);
        h4.insert(2);
        FibonacciHeap.HeapNode node5 = h4.insert(5);
        FibonacciHeap.HeapNode node0 =h4.insert(0);
        FibonacciHeap.HeapNode node11 = h4.insert(11);
        h4.insert(-3);
        heapTest(h4, -30,7, 7, 0);

        //------------------------------ delete min of h4 ---------------------------------------------------

        sepetatorPrint("delete min of h4");
        h4.deleteMin();
        heapTest(h4, -20, 6, 2,0);
        testLinksNum(11);
        printHeap(h4);

        //-------------------------------- meld h4 to h -------------------------------------------------

        sepetatorPrint("meld h4 to h");
        h.meld(h4);
        heapTest(h, -20, 11, 4, 0);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, -6, 10, 2, 0);
        testLinksNum(13);
        printHeap(h);

        //---------------------------------- h.decreaseKey(node11, 16) : to -5" ------------------------------

        sepetatorPrint("h.decreaseKey(node11, 16) : to -5");
        h.decreaseKey(node11, 16);
        heapTest(h, -6,10, 3, 1);
        testCutsNum(1);
        printHeap(h);

        //--------------------------------- h.decreaseKey(node5, 23) : to -18"-----------------------------

        sepetatorPrint("h.decreaseKey(node5, 23)");
        h.decreaseKey(node5, 23);
        heapTest(h, -18, 10, 4,2);
        testCutsNum(2);
        printHeap(h);

        //--------------------------------- h.decreaseKey(node0, 4) : to -4"-----------------------------

        sepetatorPrint("h.decreaseKey(node0, 4)");
        h.decreaseKey(node0, 8);
        heapTest(h, -18, 10, 6,0);
        testCutsNum(4);
        printHeap(h);

        //----------------------------------- delete min --------------------------------------------

        sepetatorPrint("delete min");
        h.deleteMin();
        heapTest(h, -8, 9, 2, 0);
        testLinksNum(16);
        printHeap(h);

        //--------------------------------- delete node1 ---------------------------------------------

        sepetatorPrint("h.delete(node1)");
        h.delete(node1);
        heapTest(h, -8, 8, 2, 1);
        testLinksNum(17);
        testCutsNum(5);
        printHeap(h);

        //--------------------------------- delete node9 ---------------------------------------------

        sepetatorPrint("h.delete(node9)");
        h.delete(node9);
        heapTest(h, -8, 7, 3, 0);
        testLinksNum(17);
        testCutsNum(7);
        printHeap(h);

        //------------------------------------------------------------------------------------------------
        p("\nDone!");
    }

}
