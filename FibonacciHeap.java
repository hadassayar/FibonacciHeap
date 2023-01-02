/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap{
    private HeapNode minNode;
    private HeapNode leftNode;
    private int size;
    public int totalMarked;
    private static int totalCuts;
    private static int totalLinks;

    private HeapNode link(HeapNode a, HeapNode b){
        totalLinks++;
        if (a.rank == 0){
            if (b.key<a.key){
                b.setChild(a);
                a.setParent(b);
                a.setNext(a);
                a.setPrev(a);
                b.rank ++;
                return b;
            }
            else {
                a.setChild(b);
                b.setParent(a);
                b.setNext(b);
                b.setPrev(b);
                a.rank ++;
                return a;
            }
        }
        if (b.key<a.key){
            a.setPrev(b.child.prev);
            b.child.prev.setNext(a);
            b.child.setPrev(a);
            a.setNext(b.child);
            b.setChild(a);
            a.setParent(b);
            b.rank ++;
            return b;
        }
        else {
            b.setPrev(a.child.prev);
            a.child.prev.setNext(b);
            a.child.setPrev(b);
            b.setNext(a.child);
            a.setChild(b);
            b.setParent(a);
            a.rank ++;
            return a;
        }
    }
    private int fix_array (HeapNode[] array,HeapNode node){
        int ranked = node.rank;
        if (array[node.rank] == null){
            array[node.rank]=node;
            return 0;
        }
        HeapNode new_node = link(node,array[node.rank]);
        array[ranked] = null;
        return fix_array(array, new_node);
    }

    private HeapNode[] createarray(int cnt){
        HeapNode[] array = new HeapNode[cnt];
        HeapNode node = leftNode;
        for (int i =0;i<array.length;i++){
            array[i] = node;
            node = node.next;
        }
        return array;
    }

    private HeapNode[] create_main_array(int cnt){
        int max = 0;
        HeapNode node = leftNode;
        for (int i =0;i<cnt;i++){
            if (node.rank>max){
                max = node.rank;
            }
            node=node.next;
        }
        HeapNode[] array = new HeapNode[max+3 + (int)(Math.log(cnt)/Math.log(2))];
        return array;
    }

    private void sort_heap(HeapNode[] array){
        if (size == 0){
            return;
        }
        HeapNode node1 = null;
        HeapNode node2 = null;
        boolean bool = false;
        for (int i =0;i<array.length;i++){
            if (array[i]!=null){
                if (!bool){
                    leftNode = array[i];
                    node1 = array[i];
                    bool = true;
                }
                node1.setNext(array[i]);
                array[i].setPrev(node1);
                node1 = array[i];
            }
            if (i == array.length-1){
                node1.setNext(leftNode);
                leftNode.setPrev(node1);
            }
        }
    }

    public int numberOftrees(){
        HeapNode node = leftNode.next;
        int cnt = 1;
        while (node != leftNode){
            cnt +=1;
            node = node.next;
        }
        return cnt;
    }


    private void consolidation() {
        int trees_number = numberOftrees();
        HeapNode[] itarray = createarray(trees_number);
        HeapNode[] mainarray = create_main_array(trees_number);
        for (HeapNode node : itarray) {
            fix_array(mainarray, node);
        }
        sort_heap(mainarray);
    }
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
        return (minNode == null);
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
    public void deleteMin() {
        size --;
        if(minNode == null){
            return;
        }
        if (size == 0){
            minNode = null;
            leftNode = null;
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
                consolidation();
                minNode = null;
                minNode = findMin();
                return;
            }
        }else{
            HeapNode childNode = minNode.child;
            if (leftNode==minNode){
                if (leftNode.next==leftNode){
                    do {
                        childNode.parent = null;
                        childNode = childNode.prev;
                    }while (childNode != minNode.child);
                    minNode.setChild(null);
                    minNode = childNode;
                    leftNode = childNode;
                    consolidation();
                    minNode = null;
                    minNode = findMin();
                    return;
                }
                else {
                    do {
                        childNode.parent = null;
                        childNode = childNode.prev;
                    }while (childNode != minNode.child);
                    HeapNode childNode1 = minNode.child;
                    childNode1.prev.setNext(minNode.next);
                    minNode.next.setPrev(childNode1.prev);
                    childNode1.setPrev(minNode.prev);
                    minNode.prev.setNext(childNode1);
                    minNode.setChild(null);
                    leftNode = childNode1;
                    consolidation();
                    minNode = null;
                    minNode = findMin();
                    return;
                }
            }
            do {
                childNode.parent = null;
                childNode = childNode.prev;
            }while (childNode != minNode.child);
            minNode.prev.setNext(minNode.next);
            minNode.next.setPrev(minNode.prev);
            childNode.prev.setNext(leftNode);
            childNode.setPrev(leftNode.prev);
            leftNode.prev.setNext(childNode);
            leftNode.setPrev(childNode.prev);
            leftNode = childNode;
            minNode.setChild(null);
            consolidation();
            minNode = null;
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
    {   if (x.marked){totalMarked--;}
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
        }else{
            cascadingCut(x,parentX);}
        if(newKey < minNode.getKey()){minNode = x;}//update minNode


    }

    private void cut(HeapNode x, HeapNode parentX){
        totalCuts++;
        if(x.next == x){// the node is the only child of its parent(point to himself) so we just cut.
            parentX.child = null;
        }
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
        x.parent = null;
        leftNode.prev.setNext(x);
        leftNode.setPrev(x);
        if(x.marked){
            x.marked = false;
            totalMarked--;
        }
        leftNode = x;
    }

    private void cascadingCut(HeapNode x, HeapNode parentX) {
        cut(x,parentX);
        if(parentX.parent != null) {
            if (parentX.marked == false) {// mark the node
                parentX.marked = true;
                totalMarked++;
            } else {
                // cut the node and perform the same steps on its parent
                cascadingCut(parentX,parentX.parent );
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
        int treeNum =numberOftrees();
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
    public HeapNode getFirst(){
        return leftNode;
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

        public void setChild(HeapNode node) {
        this.child = node;
    }
        public HeapNode getNext(){
            return next;
        }
        public HeapNode getPrev(){
            return prev;
        }
        public HeapNode getChild(){
            return child;
        }

        public HeapNode getParent(){
            return parent;
        }

        public int getRank(){
            return rank;
        }

        public boolean getMarked(){
            return marked;
        }

    }
}