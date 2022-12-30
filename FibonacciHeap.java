/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap{
    private HeapNode minNode;
    private HeapNode leftNode;
    private int size;
    private int totalMarked;
    private int totalCuts;
    private int totalLinks;

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
        return (minNode == null );
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {
        HeapNode node =new HeapNode(key);
        if(minNode == null){
            minNode = node;
            leftNode = node;
        }else{
            node.setNext(leftNode.next);
            node.setPrev(leftNode);
            leftNode.next.setPrev(node);
            leftNode.setNext(node);
            leftNode = node;
            if(node.getKey() < minNode.getKey()){minNode = node;}
        }
        size ++;
        return node ;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
        if(minNode == null){
            return;
        }
        size--;
        if (minNode.child == null || minNode.child == minNode){
            if (minNode.next == minNode && minNode.prev == minNode){
                minNode = null;
            }else {
                minNode.prev.setNext(minNode.next);
                minNode.next.setPrev(minNode.prev);
                if(minNode == leftNode){
                    leftNode = leftNode.prev;}
                minNode = null;
                minNode = findMin();
                return;
            }
        }else{ //minNode has a child
            HeapNode childNode = minNode.child;
            do {
                childNode.parent = null;
                childNode = childNode.prev;
            }while (childNode != minNode.child);
            minNode.child.setNext(minNode.next);
            childNode.setPrev(minNode.prev);
            minNode = findMin();
            return;
        }
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
        if(minNode != null){
            return minNode;
        }else{
            HeapNode min = leftNode;
            HeapNode tempNode = leftNode.next;
            while (tempNode.getKey() != leftNode.getKey()) {
                if (tempNode.getKey() < min.getKey()) {
                    min = tempNode;
                }
                tempNode = tempNode.next;
            }
            return min;
        }

    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
        FibonacciHeap heap1 = this;
        HeapNode temp = heap2.leftNode.next;
        heap2.leftNode.setNext(heap1.leftNode.next);
        heap1.leftNode.next.setPrev(heap2.leftNode);
        heap1.leftNode.setNext(temp);
        temp.setPrev(heap1.leftNode);
        if(heap1.minNode.getKey() > heap2.minNode.getKey()){ heap1.minNode = heap2.minNode;}
        heap1.size += heap2.size;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return size; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return size -totalMarked; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
        return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return totalLinks(); // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts(); // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }


/**
 * public class HeapNode
 * <p>
 * If you wish to implement classes other than FibonacciHeap
 * (for example HeapNode), do it in this file, not in another file.
 */

    public static class HeapNode {
        public int key;
        public HeapNode parent;
        public HeapNode child;
        public HeapNode prev;
        public HeapNode next;
        public int rank;
        public boolean marked;

        public HeapNode(int key) {
            this.key = key;
            this.next = this;
            this.prev = this;
            this.marked = false;
            this.rank = 0;
        }

        public int getKey() {
            return this.key;
        }

        public void setNext(HeapNode node) {
            this.next = node;
        }

        public void setPrev(HeapNode node) {
            this.prev = node;
        }

        public void setParent(HeapNode node) {
            this.parent = node;
        }


    }
}