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
    private static int totalCuts;
    private static int totalLinks;

    public HeapNode link(HeapNode a, HeapNode b){
        totalLinks++;
        this.size -=1;
        if (a.rank == 0){
            if (b.key<a.key){
                b.prev = a.prev;
                a.prev.setNext(b);
                b.child = a;
                a.setNext(a);
                a.prev = a;
                a.parent = b;
                b.rank +=1;
                return b;
            }
            else {
                b.next.prev = a;
                a.setNext(b.next);
                a.child = b;
                b.setNext(b);
                b.prev = b;
                b.parent = a;
                a.rank +=1;
                return a;
            }
        }
        if (b.key<a.key){
            b.prev = a.prev;
            a.prev.setNext(b);
            a.prev = b.child.prev;
            b.child.prev.setNext(a);
            b.child.prev = a;
            a.setNext(b.child);
            b.child = a;
            b.rank += 1;
            a.parent = b;
            return b;
        }
        else {
            b.next.prev = a;
            a.setNext(b.next);
            b.prev =a.child.prev;
            a.child.prev.setNext(b);
            a.child.prev = b;
            b.setNext(a.child);
            a.child = b;
            a.rank +=1;
            b.parent = a;
            return a;
        }
    }

    public void consolidation(){
        HeapNode node = leftNode;
        int max = 0;
        for (int m = 0;m<this.size;m++){
            if (node.rank>max){
                max = node.rank;
            }
            node = node.next;
        }
        HeapNode[] array = new HeapNode[this.size+1+max];
        for (int i =0 ;i<this.size;i++){
            int rank = node.rank;
            if (array[rank] != null){
                HeapNode s= node.next;
                HeapNode it_node =link(node,array[rank]);
                array[rank]=null;
                while (rank+1<array.length && array[rank+1]!=null){
                    it_node = link(it_node,array[rank+1]);
                    array[rank+1] = null;
                    rank+=1;
                }
                if (rank+1<array.length && array[rank+1]==null){
                    array[rank+1] = it_node;
                }
                node = s;
            }
            else {
                array[rank]= node;
                node = node.next;
            }
        }
        int j = 0;
        while (array[j]==null){
            j+=1;
        }
        HeapNode n = array[j];
        this.leftNode = n;
        for (int m=0;m<array.length;m++){
            if (array[m] != n && array[m]!=null){
                n.next = array[m];
                array[m].prev = n;
                n=array[m];
                if (m==array.length){
                    n.next=this.leftNode;
                    this.leftNode.prev = n;
                }
            }
        }
    }
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
            node.setPrev(leftNode.prev);
            node.setNext(leftNode);
            leftNode.prev.setNext(node);
            leftNode.setPrev(node);
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
    {   size--;
        if(minNode == null){
            return;
        }

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
            childNode.prev.setNext(leftNode);
            childNode.setPrev(leftNode.prev);
            leftNode.prev.setNext(childNode);
            leftNode.setPrev(childNode.prev);
            leftNode = childNode;
            minNode = findMin();
            this.consolidation();
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
    * (Note: The size of the array depends on the maximum order of a tree.)
    * 
    */
    public int[] countersRep()
    {
        int maxRank = 0;
        HeapNode node1 = leftNode;
        do {
            node1 = node1.next;
            if (node1.rank > maxRank){ maxRank = node1.rank;}
        }while (node1 != leftNode);{}

        HeapNode node2 = leftNode;
        int[] counterArray = new int[maxRank + 1];
        do {
            node2 = node2.next;
            counterArray[node2.rank]++;
        }while (node2 != leftNode);{}

        return counterArray;
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
    	int minKey = minNode.getKey();
        // decrease the key of x to the minimum key
        int delta = x.getKey() - (minKey);
        decreaseKey(x, delta);
        // now x is the minNode so we just use the deleteMin() method
        deleteMin();

    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	if(x.getKey() - delta > x.getKey() ){
            // the new key should be smaller than the current key.
            return;
        }
        int newKey = x.getKey() - delta;
        x.setKey(newKey);
        HeapNode parentX = x.parent;
        if (parentX == null || parentX.getKey() <= newKey){
            if(newKey < minNode.getKey()){minNode = x;}
            return;
        }
        cut(x,parentX);
        // if parentX was marked we recursively perform the same steps on it
        if(parentX.marked){cascadingCut(parentX);}
        //update minNode
        if(newKey < minNode.getKey()){minNode = x;}
    }

    private void cut(HeapNode x, HeapNode parentX){
        totalCuts++;
        if(x.next == x){parentX.child = null;} // the node is the only child of its parent(point to himself) so we just cut.
        else {
            x.prev.setNext(x.next);
            x.next.setPrev(x.prev);
            if(parentX.child == x){
                // x is the left child
                parentX.child = x.next;
            }
        }
        parentX.rank--;
        // add x as a root in the heap
        x.setPrev(leftNode.prev);
        x.setNext(leftNode);
        leftNode.prev.setNext(x);
        leftNode.setPrev(x);
        //unmark the node and mark its parent as a parent who lost a child
        x.marked = false;
        parentX.marked = true;
    }

    private void cascadingCut(HeapNode x) {
        HeapNode parentX = x.parent;
        if(parentX != null) {
            if (x.marked == false) {
                // mark the node
                x.marked = true;
                totalMarked++;
            } else {
                // cut the node and perform the same steps on its parent
                cut(x, parentX);
                cascadingCut(parentX);
            }
        }
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
        int treeNum = 0;
        HeapNode node = leftNode;
        do {
            node = node.next;
            treeNum++;
        }while (node != leftNode);{}

        int potential = treeNum + 2 * totalMarked;
        return potential;
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
    	return totalLinks;
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
    	return totalCuts;
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
        public void setKey(int newKey){this.key = newKey;}

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