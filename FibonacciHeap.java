/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
    // fields:
    protected HeapNode min;
    protected HeapNode sentinel; //the left most root in the roots list - a sentinel.
    protected int size;       //number of keys in the heap

    protected int treesNum; //number of the trees in the heap
    protected int markedNum;      //number of marked nodes in the heap

    public static int linksNum = 0; //total number of link operations made during the run-time of the program
    public static int cutsNum = 0; //total number of cut operations made during the run-time of the program


    // constructors:
    public FibonacciHeap(){             //constructor for an empty tree
        this.min = null;
        this.sentinel = new HeapNode();
        this.size = 0;
        this.treesNum = 0;
        this.markedNum = 0;
    }

    public FibonacciHeap(int key){      //constructor of a one item heap with a given key
        this.sentinel = new HeapNode();
        this.min = new HeapNode(key); //creating a root list with a single node
        this.sentinel.next = this.min;
        this.sentinel.prev = this.min;
        this.min.prev = this.sentinel;
        this.min.next = this.sentinel;
        this.size = 1;
        this.treesNum = 1;
        this.markedNum = 0;
    }

    //-------------------------------------- getSentinel ------------------------------------
    public HeapNode getSentinel(){
        return this.sentinel;
    }
    //--------------------------------------- getTail ------------------------------------
    public HeapNode getTail(){
        return this.sentinel.prev;
    }
    //--------------------------------------- getTreesNum ------------------------------------
    public int getTreesNum(){
        return this.treesNum;
    }
    //--------------------------------------- getMarkedNum ------------------------------------
    public int getMarkedNum(){
        return this.markedNum;
    }
    //--------------------------------------- customLog --------------------

    private static double customLog(double base, double logNumber) {
        return (double)Math.log(logNumber) / Math.log(base);
    }

    //---------------------------------- changeTo ------------------ needs modifications (!) ----------------------
    /**
     * private void changeTo(FibonacciHeap heap2)
     *
     * changing the heap into heap2 by:
     * assigning the fields of heap2 to the fields of the heap
     */
    private void changeTo(FibonacciHeap heap2){
        this.min = heap2.findMin();
        this.sentinel = heap2.getSentinel();
        this.size = heap2.size();
        this.treesNum = heap2.getTreesNum();
        this.markedNum = heap2.getMarkedNum();
    }

    //--------------------------------------- isEmpty -------------------------------------
    /**
     * public boolean isEmpty()
     *
     * precondition: none
     *
     * The method returns true if and only if the heap is empty.
     */
    public boolean isEmpty() {
        return this.min == null;
    }

    //----------------------------- inset --------------------------------------------------------------
    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * Returns the new node created.
     */
    public HeapNode insert(int key) {
        //creating new heap with one node
        HeapNode NodeToInsert = new HeapNode(key);
        this.sentinel.InsertAfter(NodeToInsert);

        if (this.min != null) {
            if (key < this.min.getKey()) {
                this.min = NodeToInsert;
            }
        }
        else { //inserting the first node
            this.min = NodeToInsert;
        }
        this.size += 1;
        this.treesNum += 1;

        return NodeToInsert;
    }

    //---------------------------  deleteMin ------------------------------------------------------------
    /**
     * public void deleteMin()
     *
     * Delete the node containing the minimum key.
     */
    public void deleteMin() {
        if (this.isEmpty()){
            return;
        }
        HeapNode z = this.min;
        if (z != null) { //in case the heap is not empty we delete the minimum
            if (z.child != null) {
                HeapNode y0 = z.child;
                HeapNode y = y0;
                while (y != y0) {
                    z.InsertAfter(y);
                    y.parent = null;
                    y = y.next;
                }
            }
        }
        z.prev.next = z.next; //we skip over the minimum in the roots list.
        z.next.prev = z.prev;

        if (z == z.next) { //in case we deleted the only node in the tree
            this.min = null;
        } else {
            this.min = z.next;
            Consolidate();
        }
        this.size -= 1; //updating the size field

    }
    //------------------------ numOfTrees ------------------------------

    public int numOfTrees() {
        if(this.isEmpty()){
            return 0;
        }
        int res = 1;
        HeapNode start = this.sentinel; //looping through the root list
        HeapNode runner = start.next;
        while (runner != start) {
            res += 1;
            runner = runner.next;
        }
        return res;
    }

    //------------------------- Consolidate / Successive Linking ----------------------------------------
    //makes the fib-Heap to nonLazy-Heap

    public void Consolidate() {
        int maxRank = (int)Math.floor(customLog(1.618,this.size));
        HeapNode[] buckets = new HeapNode[maxRank + 1 ]; //initialize the buckets list
        for (int i = 0; i < buckets.length ; i++) {
            buckets[i] = null;                          //they are allready nulls
        }

        HeapNode start = this.sentinel; //looping through the root list, starting sentinel
        HeapNode runner = start.next;
        while (runner != start) {
            HeapNode x = runner;
            int d = x.rank;
            while (buckets[d] != null) { //there are trees in the bucket
                HeapNode y = buckets[d];
                if (x.getKey() > y.getKey()) { //making sure that x is will be the root in the linking action
                    HeapNode tmp = x;
                    x = y;
                    y = tmp;
                }
                Link(y, x);
                buckets[d] = null;
                d += 1;
            }
            buckets[d] = x; //no trees in the bucket
            runner = runner.next;
        }
        this.treesNum = 0;
        this.min = null; //unpacking the buckets
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                if (this.min == null) {
                    this.sentinel.next = buckets[i];
                    this.sentinel.prev = buckets[i];
                    this.min = buckets[i];
                } else {
                    this.sentinel.InsertAfter(buckets[i]); //inserting the tree in bucket i to the root list
                    if (buckets[i].getKey() < this.min.getKey()) { //updating the new minimum node
                        this.min = buckets[i];
                    }
                }
                this.treesNum += 1;
            }
        }
    }

    //-------------------------- Link -----------------------------------------------

    public void Link(HeapNode y, HeapNode x) {
        //removing y from the root list
        y.prev.next = y.next;
        y.next.prev = y.prev;

        //making y a child of x
        if (x.child != null){ //x already have other children
            y.InsertAfter(x.child);
        }
        x.child = y; //adding y as the biggest child of x and connecting his other children to it
        y.parent = x;
        //y.marked = false;             why?
        //this.markedNum -= 1;
        x.rank += 1;
        linksNum += 1;
    }

    //-------------------------- findMin --------------------------------------------------------
    /**
     * public HeapNode findMin()
     *
     * Return the node of the heap whose key is minimal.
     */
    public HeapNode findMin() {
        return this.min;
    }

    //------------------------ meld  -----------------------------------------------------------
    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Meld the heap with heap2  - by concatenating heap2 roots list to the END of this heap roots list
     * updating the fields of the heap
     */

    public void meld (FibonacciHeap heap2) {
        // Edge cases:
        if (heap2.isEmpty()){  // heap2 is empty
            return;
        }
        if (this.isEmpty()){    // the heap is empty and heap2 is not empty
            this.changeTo(heap2);
        }
        // saving pointers:
        HeapNode thisTail = this.getTail();
        HeapNode heap2Tail = heap2.getTail();
        HeapNode heap2Sentinel = heap2.getSentinel();

        // changing the "next"s:
        thisTail.next = heap2Sentinel.next;
        heap2Tail.next = this.sentinel;

        // changing the "prev"s:
        heap2Sentinel.next.prev = thisTail;
        this.sentinel.prev = heap2Tail;

        //Updating fields:
        this.size += heap2.size();
        this.treesNum += heap2.getTreesNum();
        this.markedNum += heap2.getMarkedNum();
        if (heap2.findMin().key < this.min.key){
            this.min = heap2.findMin();
        }
    }

    //---------------------------------  size ----------------------------------------------
    /**
     * public int size()
     *
     * Return the number of elements in the heap
     */
    public int size() {
        return this.size;
    }

    //---------------------------------  countersRep ----------------------------------------------
    /**
     * public int[] countersRep()
     *
     * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap.
     */
    public int[] countersRep() {
        int[] arr = new int[42];
        return arr; //	 to be replaced by student code
    }

    //---------------------------------  delete ----------------------------------------------
    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     *
     */
    public void delete(HeapNode x) {
        return; // should be replaced by student code
    }

    //---------------------------------  decreaseKey ----------------------------------------------
    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) {
        x.key -= delta;
        if (x.parent == null){ // x is a root
            if (x.key < this.min.key){
                this.min = x;
            }
        }
        else if (x.key < x.parent.key){  // x is not a root and new key violate the heap role
            cascadingCut(x);
        }
        return;
    }
    //------------------------ cut -----------------------------------------------------------
    /*
    * private void cut(HeapNode x)
    * cut away from its original tree the subtree that x is its root.
    * fixes the original tree pointers and fields of its nodes. does not deal with the subtree that was cut.
    * add one to the counter of cuts that happened.
    */
    private void cut(HeapNode x){
        HeapNode parent = x.parent;
        x.parent = null;
        x.marked = false;
        this.markedNum -= 1;
        parent.rank -= 1;
        if (x.next == x){ // x was an only child
            parent.child = null;
        }
        else {
            if (parent.child == x){  // cutting the leftmost son
                parent.child = x.next;
            }
            x.prev.next = x.next;
            x.next.prev = x.prev;
        }
        cutsNum += 1;
    }
    //------------------------ cascading cut ----------------------------------------------------

    /*
    * private void cascadingCut(HeapNode x)
    * cut away from its original tree the subtree that x is its root and keep on cutting its ancestors subtrees if needed.
    * append the cut subtrees to the root list.
    */
    private void cascadingCut(HeapNode x){
        HeapNode parent = x.parent;
        cut(x);

        // adding the tree that x is its root to the beginning of the roots list:
        this.sentinel.InsertAfter(x);
        this.treesNum += 1;
        if (x.key < this.min.key){
            this.min = x;
        }

        // moving up the tree (in recursion):
        if (parent.parent != null){  // parent is not the root
            if (!parent.marked){
                parent.marked = true;
                this.markedNum += 1;
            }
            else {
                cascadingCut(parent);
            }
        }
    }

    //---------------------------------  potential ----------------------------------------------
    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap.
     */
    public int potential() {
        return this.treesNum + (2 * this.markedNum);
    }

    //---------------------------------  totalLinks ----------------------------------------------
    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the run-time of the program.
     * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of
     * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value
     * in its root.
     */
    public static int totalLinks() {
        return linksNum; // should be replaced by student code
    }

    //---------------------------------  totalCuts ----------------------------------------------
    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the run-time of the program.
     * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() {
        return cutsNum;
    }

    //---------------------------------  Kmin ----------------------------------------------
    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k minimal elements in a binomial tree H.
     * The function should run in O(k*deg(H)).
     * You are not allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k) {
        int[] arr = new int[42];
        return arr; // should be replaced by student code
    }

    //------------------------------- HeapNode --------------------------------------

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in
     * another file
     */
    public class HeapNode{

        protected int key;
        protected int rank;          // number of children
        protected boolean marked;
        protected HeapNode child;    // first child (left most child)
        protected HeapNode next;
        protected HeapNode prev;
        protected HeapNode parent;

        public HeapNode(){ //constructor of sentinel
            this.key = 0;
            this.child = null;
            this.next = this; //pointing on itself
            this.prev = this; //pointing on itself
            this.parent = null;
            this.marked = false;
            this.rank = -1;
        }

        public HeapNode(int key) {      //constructor for a node with a given key
            this.key = key;
            this.rank = 0;
            this.marked = false;
            this.child = null;
            this.next = null;
            this.prev = null;
            this.parent = null;
        }

        public int getKey() {
            return this.key;
        }

        //------------------------ InsertAfter -----------------------------------------------------------
        /**
         * public void InsertAfter(HeapNode B)
         * insert node B after the node "this"
         */
        public void InsertAfter(HeapNode B){
            HeapNode A = this;
            B.prev = A;
            B.next = A.next;
            A.next = B;
            B.next.prev = B;
        }

        //-------------------------------------------------------------------------------------------

    }
}
