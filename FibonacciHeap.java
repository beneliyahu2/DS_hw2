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
    public FibonacciHeap(){             //constructor for an empty heap (a sentinel that points to itself)
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

    //---------------------------------- changeTo ----------------------------------------
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

        FibonacciHeap insertHeap = new FibonacciHeap(key); // new heap with one tree
        HeapNode insertNode = insertHeap.findMin();         // new node

        //meld at first (takes care of updating the fields):
        insertHeap.meld(this);
        this.changeTo(insertHeap);

        return insertNode;
    }

    //---------------------------  deleteMin ------------------------------------------------------------
    /**
     * public void deleteMin()
     *
     * Delete the node containing the minimum key.
     */

    public void deleteMin(){
        if (this.isEmpty()){
            return;
        }

        if (this.min.child == null){                 //min is childless -  only bypassing min
            this.min.prev.next = this.min.next;
            this.min.next.prev = this.min.prev;
            if (this.size == 1){                    //deletion of the only node, returning an empty heap
                this.min = null;
                this.size = 0;
                this.treesNum = 0;
                this.markedNum = 0;
                return;
            }
        }
        else{                       // insert mins sons to the trees list, and unmarked them
            HeapNode child = this.min.child;

            HeapNode currSon = child;  // looping over the sons of min - making their parent a null and unmarked them.
            do {
                currSon.parent = null;
                if (currSon.marked){
                    currSon.marked = false;
                    markedNum -= 1;
                }
                currSon = currSon.next;
            } while (currSon != child);

            HeapNode lastSon = child.prev;

            this.min.prev.next = child;
            child.prev = this.min.prev;
            lastSon.next = this.min.next;
            this.min.next.prev = lastSon;
        }

        consolidate();      //finds the new min and update the min field. update the treesNum field.

        this.size -= 1;

    }

    //------------------------- Consolidate / Successive Linking ----------------------------------------
    /**
     * protected void consolidate()
     *
     * makes the heap with only one tree from each rank.
     * update the treesNum field and the min field accordingly.
     */

    protected void consolidate() {
        fromBuckets(toBuckets());
    }

    /**
     * protected HeapNode[] toBuckets()
     *
     * loop over the the trees of the heap and link every pair of trees with the same rank.
     * return a buckets array with the linked trees.
     */
    protected HeapNode[] toBuckets(){
        int maxRank = (int)Math.floor(Math.log(this.size) / Math.log(1.618));
        HeapNode[] bucketsArr = new HeapNode[maxRank + 1 ]; //initialize the buckets list

        HeapNode currTree = this.sentinel.next;
        while (currTree.rank != -1){  //looping through the root list
            HeapNode nextTree = currTree.next;
            while (bucketsArr[currTree.rank] != null){
                currTree = link(bucketsArr[currTree.rank],currTree);
                bucketsArr[currTree.rank - 1] = null;
            }
            bucketsArr[currTree.rank] = currTree;
            currTree = nextTree;
        }
        return bucketsArr;
    }

    /**
     * protected void fromBuckets(HeapNode[] bucketsArr)
     *
     * loop over the the buckets and add all linked trees to the heap.
     * update the treesNum field and the min field accordingly.
     */
    protected void fromBuckets(HeapNode[] bucketsArr){
        this.treesNum = 0;
        this.min = this.sentinel;
        HeapNode prevTree = this.sentinel;
        for (int i = 0; i < bucketsArr.length; i++) {
            if (bucketsArr[i] != null) {
                HeapNode currTree = bucketsArr[i];

                prevTree.next = currTree;
                currTree.prev = prevTree;
                currTree.next = this.sentinel;

                //updating the number of trees and the min
                this.treesNum += 1;
                if (currTree.key < this.min.key) {
                    this.min = currTree;
                }

                prevTree = currTree;
            }
        }
        this.sentinel.prev = prevTree;
    }

    //-------------------------- Link -----------------------------------------------
    /**
     * public HeapNode Link(HeapNode x, HeapNode y)
     *
     * link the node with the larger key as a son to the other one.
     * add one to the parent rank and to the static field linksNum.
     * return the parent node;
     */

    public HeapNode link(HeapNode x, HeapNode y){
        if (y.key < x.key){  //defining x to be the smaller, hens the parent.
            HeapNode temp = x;
            x = y;
            y = temp;
        }
        y.parent = x;

        if (x.child == null){ //linking two childless nodes
            y.next = y;
            y.prev = y;
        }
        else {  //insert y at the beginning of x sons list
            y.next = x.child;
            y.prev = x.child.prev;
            x.child.prev.next = y;
            x.child.prev = y;
        }
        x.child = y;
        x.rank += 1;
        linksNum += 1;

        return x;
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
            return;
        }
        // saving pointers:
        HeapNode thisTail = this.getTail();
        HeapNode heap2Tail = heap2.getTail();
        HeapNode heap2Sentinel = heap2.getSentinel();

        // changing the "next"s:
        thisTail.next = heap2Sentinel.next;
        heap2Tail.next = this.sentinel;

        // changing the "prev"s:
        heap2Sentinel.next.prev = thisTail;  //skipping heap2 Sentinel
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
        this.decreaseKey(x, x.key - Integer.MIN_VALUE);
        this.deleteMin();
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
    }

    //------------------------ cut -----------------------------------------------------------
    /**
    * private void cut(HeapNode x)
    * cut away from its original tree the subtree that x is its root.
    * fixes the original tree pointers and fields of its nodes. does not deal with the subtree that was cut.
    * add one to the counter of cuts that happened.
    */
    private void cut(HeapNode x){
        HeapNode parent = x.parent;
        parent.rank -= 1;
        x.parent = null;
        if (x.marked){
            x.marked = false;
            this.markedNum -= 1;
        }
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

    /**
    * private void cascadingCut(HeapNode x)
    * cut away from its original tree the subtree that x is its root and keep on cutting its ancestors subtrees if needed.
    * append the cut subtrees to the root list.
    */
    private void cascadingCut(HeapNode x){
        HeapNode parent = x.parent;
        cut(x);

        // adding the tree that x is its root to the beginning of the roots list:
        x.prev = this.sentinel;
        x.next = this.sentinel.next;
        this.sentinel.next = x;
        x.next.prev = x;

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
            this.key = Integer.MAX_VALUE;
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

        //-------------------------------------------------------------------------------------------

    }
}