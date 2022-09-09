
import java.util.NoSuchElementException;


public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private Node root = null;
    private int ZERO = 0;
    private int ONE = 1;
    private int TWO = 2;
    private int THREE = 3;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
    	if (root == null) {
    		throw new NoSuchElementException("empty tree has no root");
    	}
        return root;
    }
	
    public Node search(int k) {
        Node output = root;
        boolean found = false;
        while (output != null & !found){
            int currKey = output.key;
            if (currKey == k){
                found = true;
            } else if (currKey > k){
                output = output.left;
            } else {
                output = output.right;
            }
        }
    	return output;
    }

    public void insert(Node node) {
        if (node == null) //**********
            throw new IllegalArgumentException("can not input null node");
        if (!redoStack.isEmpty()){ //clear retrack stack if insert is not called from retrack.
            if ((int)redoStack.pop() != 2){
                redoStack.clear();
            }
        }
        node.parent = null;
        node.right = null;
        node.left = null;
        if (root == null){ //insert as node
            this.root = node;
        } else {
            Node temp = root;
            boolean found = false;
            while (!found){
                if (temp.key > node.key){  //find correct node to input new node as child
                    if (temp.left == null){
                        found = true;
                        temp.left = node;
                        node.parent = temp;
                    } else {
                        temp = temp.left;
                    }
                } else {
                    if (temp.right == null){
                        found = true;
                        temp.right = node;
                        node.parent = temp;
                    } else {
                        temp = temp.right;
                    }
                }
            }
        }
        stack.push(node);
        stack.push(ZERO); //sign for insert operation
    }

    public void delete(Node node) { //check case if node has same key and different name
        if (!redoStack.isEmpty()){ //clear retrack stack if delete is not called from retrack.
            if ((int)redoStack.pop() != 2){
                redoStack.clear();
            }
        }
        if (node != null && search(node.getKey()) != null ){
            if (node.left == null & node.right==null){
                if (node.parent == null){
                    root = null;
                } else {
                    if (node.parent.getKey() > node.getKey()){
                        node.parent.left = null;
                    } else {
                        node.parent.right = null;
                    }
                }
                stack.push(node);
                stack.push(ONE); //signing for leaf deletion.

            } else if (node.left != null & node.right != null){  //delete node with 2 children
                Node suc = successor(node);
                delete(suc);
                stack.push(suc.parent);
                stack.push(suc.right);
                stack.push(suc.left);
                stack.push(suc);
                suc.left = node.left; //gives suc the pointers that node had.
                suc.right = node.right;
                suc.parent = node.parent;
                if (suc.left != null){
                    suc.left.parent = suc;
                }
                if (suc.right != null){
                    suc.right.parent = suc;
                }
                if(suc.parent==null){  //in case 2 child node was the root.
                    root=suc;
                }
                else { //in case 2 child node was not the root, need to change nodes parent to point at successor(new node).
                    if (suc.key > suc.parent.key)
                        suc.parent.right = suc;
                    else
                        suc.parent.left = suc;
                }
                stack.push(node);
                stack.push(THREE); //signing for 2 child node deletion

            } else { //delete node with 1 son.
                Node temp = node.right; //assigns nodes right/left son to temp.
                if(temp==null) {
                    temp = node.left;
                }
                temp.parent = node.parent; //assign temp parent field as nodes parent
                if (node.parent == null){ // in case node 1 son was root
                    root = temp;
                } else {   //delete 1 son not root
                    if (node.getKey() > node.parent.key){
                        node.parent.right = temp;
                    } else {
                        node.parent.left = temp;
                    }
                }
                stack.push(node);
                stack.push(TWO); //sign for 1 son node deletion
            }
            stack.push(ONE); //signing for deletion operation
        }
        else
            throw new IllegalArgumentException("node not found");
    }

    public Node minimum() {
        Node output = getRoot();
        if (output == null){
            throw new IllegalArgumentException("tree is empty - no minimum available.");
        }
        while (output.left != null){
            output = output.left;
        }
    	return output;
    }

    public Node maximum() {
        Node output = getRoot();
        if (output == null){
            throw new IllegalArgumentException("tree is empty - no maximum available.");
        }
        while (output.right != null){
            output = output.right;
        }
        return output;
    }

    public Node successor(Node node) {
        if (node == null)
            throw new IllegalArgumentException("can not find successor for null node");
        Node output = node.right;
        if (output == null){
            throw new IllegalArgumentException("no successor for input node");
        }
        while (output.left != null){
            output = output.left;
        }
    	return output;
    }

    public Node predecessor(Node node) {
        if (node == null)
            throw new IllegalArgumentException("can not find successor for null node");
        Node output = node.left;
        if (output == null){
            throw new IllegalArgumentException("no predecessor for input node");
        }
        while (output.right != null){
            output = output.right;
        }
        return output;
    }

    @Override
    public void backtrack() {
        if (!stack.isEmpty()){
            int condition = (int)stack.pop();
            if(condition == 0){   //backtrack insert operation
                Node temp = (Node)stack.pop();
                redoStack.push(temp);
                if (temp.parent == null){
                    this.root = null;
                } else {
                    if (temp.parent.getKey() > temp.getKey()){
                        temp.parent.left = null;
                    } else {
                        temp.parent.right = null;
                    }
                }
                redoStack.push(ZERO);

            } else { //backtrack delete operation
                int ident = (int)stack.pop();
                Node temp = (Node)stack.pop();
                redoStack.push(temp);
                if (ident == 1){ //backtrack deleted leaf
                    if (temp.parent == null){ // return leaf that was root
                        root = temp;
                    } else {
                        if (temp.parent.getKey() > temp.getKey()){
                            temp.parent.left = temp;
                        } else {
                            temp.parent.right = temp;
                        }
                    }

                } else if (ident == 2){    //backtrack deleted 1 son node
                    if (temp.right==null){
                        temp.left.parent = temp;
                    } else {
                        temp.right.parent = temp;
                    }
                    if (temp.parent != null){
                        if (temp.getKey() > temp.parent.getKey()){
                            temp.parent.right = temp;
                        } else {
                            temp.parent.left = temp;
                        }
                    } else {
                        root = temp;
                    }
                } else {
                    Node successor = (Node)stack.pop();
                    successor.left=(Node)stack.pop();
                    successor.right=(Node)stack.pop();
                    successor.parent=(Node)stack.pop();
                    backtrack();
                    redoStack.pop();
                    redoStack.pop();
                    temp.left.parent=temp;
                    temp.right.parent=temp;
                    if(temp.parent==null)
                        root=temp;
                    else{
                        if(temp.parent.key>temp.key)
                            temp.parent.left=temp;
                        else
                            temp.parent.right=temp;
                    }
                }
                redoStack.push(ONE);
            }
        }
    }

    @Override
    public void retrack() {
        if (!redoStack.isEmpty()){
            int condition = (int)redoStack.pop();
            Node temp = (Node)redoStack.pop();
            redoStack.push(TWO);
            if (condition == 0){
               insert(temp);
            } else {
                delete(temp);
            }
        }

    }

    public void printPreOrder(){
        String output = "";
        output = printer(root, output);
        if (output.length() > 1){
            output = output.substring(0, output.length()-1);
        }
        System.out.println(output);
    }

    private String printer (Node node, String output){
        if (node != null){
            output = output + node.key + " ";
            output = printer(node.left, output);
            output = printer(node.right, output);
        }
        return output;
    }

    @Override
    public void print() {
    	printPreOrder();
    }

    public static class Node {
    	// These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public Node left;
        public Node right;
        
        private Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
        
    }

}
