/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
    // fields:
    private HeapNode min;
    private HeapNode head;  //the left most root in the roots list //no need because it is a doubly linked list
    private int size;       //number of keys in the heap
    private int number_of_trees; //number of the trees in the heap
    private int markedNum;      //number of marked nodes in the heap

    private static int linkNum; //total number of link operations made during the run-time of the program
    private static int cutNum; //total number of cut operations made during the run-time of the program


    // constructors:
    public FibonacciHeap(){             //constructor for an empty tree
        this.min = null;
        this.head = null;
        this.size = 0;
        this.number_of_trees = 0;
        this.markedNum = 0;
    }

    public FibonacciHeap(int key){      //constructor of a one item heap with a given key
        HeapNode keyNode = new HeapNode(key);
        this.min = keyNode;
        this.head = keyNode;
        this.size = 1;
        keyNode.next = keyNode;
        keyNode.prev = keyNode;
        this.number_of_trees = 1;
        this.markedNum = 0;
    }

    //--------------------------------------- getHead ------------------------------------

    private HeapNode getHead(){
        return this.head;
    }

    //--------------------------------------- getTail ------------------------------------

    private HeapNode getTail(){
        return this.head.prev;
    }

    //--------------------------------------- getNum_of_trees ------------------------------------

    public int getNum_of_trees (){
        return this.number_of_trees;
    }

    //--------------------------------------- getMarkedNum ------------------------------------

    public int getMarkedNum(){
        return this.markedNum;
    }

    //-------------------------------------- changeTo ----------------------------------------
    /**
     * private void changeTo(FibonacciHeap heap2)
     *
     * changing the heap into heap2 by:
     * assigning the fields of heap2 to the fields of the heap
     */
    private void changeTo(FibonacciHeap heap2){
        this.min = heap2.findMin();
        this.head = heap2.getHead();
        this.size = heap2.size();
        this.number_of_trees = heap2.getNum_of_trees();
        this.markedNum = heap2.getMarkedNum();
    }
    //------------------------------ treeSize ----------------------------- maybe unnecessary (!)

    private int treeSize(HeapNode x){
        if (x.child == null){  // x is a leaf
            return 1;
        }
        int childrenSize = 0;
        HeapNode child = x.child;
        do{
            childrenSize += treeSize(child);
            child = child.next;
        } while (child != x.child);
        return childrenSize;
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
        return this.size == 0;
    }

    //----------------------------- inset --------------------------------------------------------------
    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * Updating the fields of the heap (size, number_of_trees, head)
     * Returns the new node created.
     */
    public HeapNode insert(int key) {

        FibonacciHeap insertHeap = new FibonacciHeap(key); // new heap with one tree
        HeapNode insertNode = insertHeap.findMin();         // new node

        this.meldAtFirst(insertHeap);                  //meld at first (takes care of updating the fields)

        return insertNode;
    }

    private void insertNode(HeapNode basket) {  //need to split the insert for the delete
    }

    //---------------------------  deleteMin ------------------------------------------------------------
    /**
     * public void deleteMin()
     *
     * Delete the node containing the minimum key.
     */
    public void deleteMin() {
        return; // should be replaced by student code

    }

    //------------------------- Consolidating / Successive Linking ----------------------------------------
    //makes the fib-Heap to nonLazy-Heap

    private void consolidating(){

     //making the baskets
    double possible_ranks = Math.log10(this.size) / Math.log10(2);
    HeapNode[] baskets = new HeapNode[(int) possible_ranks];

    //sorting each tree to the basket of its rank
    for(HeapNode y=this.min ; y != null ; y=y.next)  //need to change - head
    {
      if (baskets[y.rank] != null) {    //basket has a tree
          HeapNode merged = baskets[y.rank].link(y);
          baskets[y.rank+1] = merged;
      }
      else{                              //the basket is empty
          baskets[y.rank] = y;
      }
    }
    //adding the non-Lazy heap the the original heap
    FibonacciHeap UpdatedHeap = new FibonacciHeap();
    for (int i=0;i<baskets.length;i++){
        if (baskets[i]!=null){
            this.insertNode(baskets[i]);
        }
    }

}

//needs to add update new min

//needs to add delete on a binomial heap

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
        HeapNode heap2Head = heap2.getHead();

        // changing the "next"s:
        thisTail.next = heap2Head;
        heap2Tail.next = this.head;

        // changing the "prev"s:
        heap2Head.prev = thisTail;
        this.head.prev = heap2Tail;

        //Updating fields:
        this.size += heap2.size();
        this.number_of_trees += heap2.getNum_of_trees();
        this.markedNum += heap2.getMarkedNum();
        if (heap2.findMin().key < this.min.key){
            this.min = heap2.findMin();
        }
    }
    //------------------------ meld at first -----------------------------------------------
    /**
     * public void meldAtFirst (FibonacciHeap heap2)
     *
     * Meld the heap with heap2  - by concatenating heap2 roots list to the BEGINNING of this heap roots list
     * updating the fields: size, min and number_of_trees
     */

    public void meldAtFirst (FibonacciHeap heap2) {
        heap2.meld(this);
        this.changeTo(heap2);
    }

    //---------------------------------  size ----------------------------------------------
    /**
     * public int size()
     *
     * Return the number of elements in the heap
     */
    public int size() {
        return this.size; // should be replaced by student code
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
        x.mark = false;
        parent.rank -=1;
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
        cutNum += 1;
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

        // adding the tree that x is its root to the roots list:
        x.next = this.head;
        x.prev = this.getTail();
        this.head.prev = x;
        this.getTail().next = x;
        this.head = x;
        this.number_of_trees += 1;
        if (x.key < this.min.key){
            this.min = x;
        }

        // moving up the tree (in recursion):
        if (parent.parent != null){  // parent is not the root
            if (!parent.mark){
                parent.mark = true;
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
        return 0; // should be replaced by student code
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
        return 0; // should be replaced by student code
    }

    //---------------------------------  totalCuts ----------------------------------------------
    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the run-time of the program.
     * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() {
        return cutNum;
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

        public int key;
        public int rank;          // number of children
        public boolean mark;
        public HeapNode child;    // first child (left most child)
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;

        public HeapNode(int key) {      //constructor for a node with a given key
            this.key = key;
            this.rank = 0;
            this.mark = false;
            this.child = null;
            this.next = null;
            this.prev = null;
            this.parent = null;
        }

        public int getKey() {
            return this.key;
        }

        //------------------------ link -----------------------------------------------------------
        /**
         * public void link (FibonacciHeap heap2)
         * link the node to another node, considering their keys.
         * the nodes are roots of two trees
         */
        public HeapNode link(HeapNode y){
            return null;
        }


        //-------------------------------------------------------------------------------------------

    }
}
