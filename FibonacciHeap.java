/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
    // fields:
    private HeapNode min;
    private HeapNode head;  //the left most root in the roots list
    private int size;       //number of keys in the heap


    // constructors:
    public FibonacciHeap(){             //constructor for an empty tree
        this.min = null;
        this.size = 0;
        this.head = null;
    }

    public FibonacciHeap(int key){      //constructor of a one item heap with a given key
        HeapNode keyNode = new HeapNode(key);
        this.min = keyNode;
        this.size = 1;
        this.head = keyNode;
        keyNode.next = keyNode;
        keyNode.prev = keyNode;

    }

    //--------------------------------------- getHead ------------------------------------

    private HeapNode getHead(){
        return this.head;
    }

    //--------------------------------------- getTail ------------------------------------

    private HeapNode getTail(){
        return this.head.prev;
    }

    //--------------------------------------- isEmpty -------------------------------------
    /**
     * public boolean isEmpty()
     *
     * precondition: none
     *
     * The method returns true if and only if the heap
     * is empty.
     *
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    //----------------------------- inset --------------------------------------------------------------
    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     *
     * Returns the new node created.
     */
    public HeapNode insert(int key) {
        FibonacciHeap insertHeap = new FibonacciHeap(key);
        HeapNode insertNode = insertHeap.min;
        this.meld(insertHeap);                  //takes care of updating the size field
        return insertNode;
    }

    //---------------------------  deleteMin ------------------------------------------------------------
    /**
     * public void deleteMin()
     *
     * Delete the node containing the minimum key.
     *
     */
    public void deleteMin() {
        return; // should be replaced by student code

    }

    //-------------------------- findMin --------------------------------------------------------
    /**
     * public HeapNode findMin()
     * Return the node of the heap whose key is minimal.
     */
    public HeapNode findMin() {
        return this.min;
    }


    //------------------------ meld  -----------------------------------------------------------
    /**
     * public void meld (FibonacciHeap heap2)
     * Meld the heap with heap2  - by concatenating the two roots lists
     */
    public void meld (FibonacciHeap heap2) {
        HeapNode thisTail = this.getTail();
        HeapNode heap2Tail = heap2.getTail();
        HeapNode heap2Head = heap2.getHead();

        thisTail.next = heap2Head;
        heap2Tail.next = this.head;

        heap2Head.prev = thisTail;
        this.head.prev = heap2Tail;

        this.size += heap2.size();
    }

    //---------------------------------  size ----------------------------------------------
    /**
     * public int size()
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
     *
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
        return; // should be replaced by student code
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
        return 0; // should be replaced by student code
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
     *
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


    }
}
