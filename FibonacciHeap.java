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


   /**
    * public boolean isEmpty()
    * O(1)
    *
    * Returns true if and only if the Heap is empty.
    *
    */
    public boolean isEmpty()
    {
        return (minNode == null);
    }

   /**
    * public HeapNode insert(int key)
    * O(1)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the Heap.
    * The added key is assumed not to already belong to the Heap.
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
    * O(logn)
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
        // minnode doesn't have children
        if (minNode.child == null || minNode.child == minNode){
            if (minNode.next == minNode && minNode.prev == minNode){
                minNode = null;
            }else {
                minNode.prev.setNext(minNode.next);
                minNode.next.setPrev(minNode.prev);
                if(minNode == leftNode){
                    leftNode = leftNode.next;}
                consolidation();
                minNode.setKey(Integer.MAX_VALUE);
                minNode = findMin();
                return;
            }
        }else{  //minnode has children
            HeapNode childNode = minNode.child;
            if (leftNode==minNode){
                if (leftNode.next==leftNode){ //this is the only root in the heap
                    do {
                        if(childNode.marked ==  true){
                            childNode.marked = false;
                            totalMarked--;
                        }
                        childNode.parent = null;
                        childNode = childNode.prev;
                    }while (childNode != minNode.child);
                    minNode.setChild(null);
                    //minNode = childNode;
                    leftNode = childNode;
                    consolidation();
                    minNode.setKey(Integer.MAX_VALUE);
                    minNode = findMin();
                    return;
                }
                else { //the heap has several roots
                    do {
                        if(childNode.marked ==  true){
                            childNode.marked = false;
                            totalMarked--;
                        }
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
                    minNode.setKey(Integer.MAX_VALUE);
                    minNode = findMin();
                    return;
                }
            }
            do {
                if(childNode.marked ==  true){
                    childNode.marked = false;
                    totalMarked--;
                }
                childNode.parent = null;
                childNode = childNode.prev;
            }while (childNode != minNode.child);
            HeapNode prv = minNode.prev;
            HeapNode nxt = minNode.next;
            childNode.prev.setNext(nxt);
            nxt.setPrev(childNode.prev);
            childNode.setPrev(prv);
            prv.setNext(childNode);
            minNode.setChild(null);
            consolidation();
            minNode.setKey(Integer.MAX_VALUE);
            minNode = findMin();
            return;
        }
    }

    /**
     * private void consolidation() {
     * O(n)
     *
     *  consolidates the trees in a Fibonacci heap by repeatedly linking trees with the same
     *  degree until there are no more trees with the same degree.
     *
     */
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
     *  private HeapNode link(HeapNode a, HeapNode b)
     *  O(1)
     *
     * method links two trees of the same degree in a Fibonacci heap by adding one tree as the child of the other,
     * with the tree having the smaller key becoming the child.
     *
     * The method takes two HeapNode objects as parameters and returns the HeapNode object that becomes the parent of the other node.
     *
     */
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
    /**
     * private int fix_array (HeapNode[] array,HeapNode node)
     *O(n)
     *
     * helps with the consolidation process by adding a node to an array of nodes
     * and then repeatedly linking the node with other nodes in the array that have the same degree,
     * until a slot in the array becomes available.
     *
     * The method takes an array of HeapNode objects and a HeapNode object as parameters,
     * and returns an integer indicating the number of times the node was linked with other nodes in the array.
     *
     */
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
    /**
     *  private HeapNode[] createarray(int cnt)
     *O(n)
     *
     * Creates an array of HeapNode objects representing the trees in a Fibonacci heap.
     * The method takes an integer as a parameter and returns an array of HeapNode objects.
     *
     */
    private HeapNode[] createarray(int cnt){
        HeapNode[] array = new HeapNode[cnt];
        HeapNode node = leftNode;
        for (int i =0;i<array.length;i++){
            array[i] = node;
            node = node.next;
        }
        return array;
    }
    /**
     * private HeapNode[] create_main_array(int cnt)
     *O(n)
     *
     * creates an array of HeapNode objects that will be used during the consolidation process.
     *
     * The method takes an integer as a parameter and returns an array of HeapNode objects
     *
     */
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
    /**
     * private void sort_heap(HeapNode[] array)
     *O(n)
     *
     * Reorders the nodes in a Fibonacci heap so that they form a circular linked list, with the minimum node at the front.
     *
     * The method takes an array of HeapNode objects as a parameter.
     *
     */
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
    /**
     * public int numberOftrees()
     * O(t) t is the number of trees in the heap
     *
     * Counts the number of trees in a Fibonacci heap.
     *
     */
    public int numberOftrees(){
        if(isEmpty()){
            return 0;
        }
        HeapNode node = leftNode.next;
        int cnt = 1;
        while (node != leftNode){
            cnt +=1;
            node = node.next;
        }
        return cnt;
    }



   /**
    * public HeapNode findMin()
    * O(t) the number of roots
    *
    * Returns the node of the Heap whose key is minimal, or null if the Heap is empty.
    *
    */
    public HeapNode findMin()
    {   if(isEmpty()){return null;}
        if(minNode.key != Integer.MAX_VALUE){
            return minNode;
        }else{
            minNode = null;
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
    * O(1)
    *
    * Melds heap2 with the current Heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
        FibonacciHeap heap1 = this;
        if(heap1.isEmpty()){
            heap1.minNode = heap2.minNode;
            heap1.leftNode = heap2.leftNode;
            heap1.size = heap2.size;
            heap1.totalMarked = heap2.totalMarked;
            return;
        }
        if(heap2.isEmpty()){
            return;
        }
        HeapNode temp = heap2.leftNode.next;
        heap2.leftNode.setNext(heap1.leftNode.next);
        heap1.leftNode.next.setPrev(heap2.leftNode);
        heap1.leftNode.setNext(temp);
        temp.setPrev(heap1.leftNode);
        if(heap1.minNode.getKey() > heap2.minNode.getKey()){ heap1.minNode = heap2.minNode;}
        heap1.size += heap2.size;
        heap1.totalMarked += heap2.totalMarked;
    }

   /**
    * public int size()
    * O(1)
    *
    * Returns the number of elements in the Heap.
    *
    */
    public int size()
    {
    	return size; // should be replaced by student code
    }

    /**
    * public int[] countersRep()
     * O(n)
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the Heap.
    * (Note: The size of the array depends on the maximum order of a tree.)
    *
    */
    public int[] countersRep()
    {
        if(isEmpty()){
            int[] empty = new int[0];
            return empty;
        }
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
    * O(n)
    *
    * Deletes the node x from the Heap.
	* It is assumed that x indeed belongs to the Heap.
    *
    */
    public void delete(HeapNode x)
    {   if (x.marked){
            x.marked =false;
            totalMarked--;
    }
        if(minNode.getKey() == Integer.MIN_VALUE){
            // decrease the key of x to the minimum key
            minNode.setKey(Integer.MAX_VALUE);
            int delta = Integer.MIN_VALUE;
            decreaseKey(x, delta);
            // now x is the minNode so we just use the deleteMin() method
            deleteMin();
            minNode.setKey(Integer.MIN_VALUE);

        }else {
            // decrease the key of x to the minimum key
            int delta = Integer.MIN_VALUE;
            decreaseKey(x, delta);
            // now x is the minNode so we just use the deleteMin() method
            deleteMin();
        }
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    * O(logn)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the Heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {
    	if(x.getKey() - delta > x.getKey() ){
            // the new key should be smaller than the current key.
            return;
        }
        int newKey =(x.getKey() - delta);
        x.setKey(newKey);
        HeapNode parentX = x.parent;
        if (parentX == null || parentX.getKey() <= newKey){
            if(newKey < minNode.getKey()){minNode = x;}
            return;
        }else{
            cascadingCut(x,parentX);}
        if(newKey < minNode.getKey()){minNode = x;}//update minNode
    }

    /**
     * private void cut(HeapNode x, HeapNode parentX)
     * O(1)
     *
     * removes a node from its parent and adds it back into the root list of the Fibonacci heap.
     */
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
        // add x as a root in the Heap
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
    /**
     * private void cascadingCut(HeapNode x, HeapNode parentX)
     * O(h) h is the height of the tree.
     *
     *  cuts up the tree by repeatedly calling the cut() method on a node and its parent,
     *  until a node is encountered that is not marked or until the root of the tree is reached.
     *  This method is used to maintain the heap's min-heap property when a node's key is decreased
     *  or when a node is removed from the heap.
     */

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
    * O(1)
    *
    * This function returns the current number of non-marked items in the Heap
    */
    public int nonMarked()
    {
        return size -totalMarked; // should be replaced by student code
    }

   /**
    * public int potential()
    * O(t) the number of trees
    *
    * This function returns the current potential of the Heap, which is:
    * Potential = #trees + 2*#marked
    *
    * In words: The potential equals to the number of trees in the Heap
    * plus twice the number of marked nodes in the Heap.
    */
    public int potential()
    {
        int treeNum =numberOftrees();
        int potential = treeNum + 2 * totalMarked;
        return potential;
    }

   /**
    * public static int totalLinks()
    * O(1)
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
    * O(1)
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
     * private HeapNode insertforkMin(int key,HeapNode cnode)
     * O(1)
     *
     * method adds a new node with the specified key to a Fibonacci heap for kmin() use.
     *
     */
     private HeapNode insertforkMin(int key,HeapNode cnode)
     {
         HeapNode node =new HeapNode(key);
         node.setConnected(cnode);
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
     * public static int[] kMin(FibonacciHeap H, int k)
     * O(k * deg(H))
     *
     * This static function returns the k smallest elements in a Fibonacci Heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        if (k == 0){
            int[] arr = new int[0];
            return arr;
        }
        if (k==1){
            int[] arr = new int[k];
            arr[0]= H.minNode.getKey();
            return arr;

        }
        FibonacciHeap fibonacciHeap2 = new FibonacciHeap();
        int[] arr = new int[k];
        for (int i =0;i<k;i++){
            if (i==0){
                HeapNode firstmin = H.findMin();
                arr[0] = firstmin.getKey();
                HeapNode childNode = firstmin.child;
                do {
                    fibonacciHeap2.insertforkMin(childNode.getKey(),childNode);
                    childNode = childNode.next;
                }while (childNode != firstmin.child);
            }
            else {
                HeapNode min = fibonacciHeap2.findMin();
                arr[i] = min.getKey();
                if (min.connected.child !=null){
                    HeapNode childNode = min.connected.child;
                    do {
                        fibonacciHeap2.insertforkMin(childNode.getKey(),childNode);
                        childNode = childNode.next;
                    }while (childNode != min.connected.child);
                }
                fibonacciHeap2.deleteMin();}
        }
        return arr;
    }

    /**
     * public HeapNode getFirst()
     * O(1)
     *
     * returns the leftNode of the heap.
     *
     */
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
        public Integer key;
        public HeapNode parent;
        public HeapNode child;
        public HeapNode prev;
        public HeapNode next;
        public int rank;
        public boolean marked;
        public HeapNode connected;

        public HeapNode(int key) {
            this.key = key;
            this.next = this;
            this.prev = this;
            this.marked = false;
            this.rank = 0;
            this.connected = null;
        }

        public Integer getKey() {
            return this.key;
        }
        public void setKey(Integer newKey){this.key = newKey;}

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
        public void setConnected(HeapNode node) {
        this.connected = node;
    }

    }
}