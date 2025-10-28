package lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    int size = 0;
    private Node root;
    private class Node {
        public int key;
        public int diff = 0;
        public String value;
        public Node left;
        public Node right;
        public Node parent;
        public Node(int key, String value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            this.parent = null;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if(root != null)
            traverse(root, sb);
        else
            sb.append("}");
        return sb.toString();
    }
    private void traverse(Node node, StringBuilder sb) {
        if (node.left != null)
            traverse(node.left, sb);
        sb.append(node.key).append('=').append(node.value).append(", ");
        if (node.right != null)
            traverse(node.right, sb);
        if(node == root)
            sb.replace(sb.length() - 2, sb.length(), "}");
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        Node pointer = root;
        String ret = null;
        while (pointer != null && pointer.key != (Integer)key) {
            if(pointer.key > (Integer)key)
                pointer = pointer.left;
            else
                pointer = pointer.right;
        }
        if(pointer != null)
            ret = pointer.value;
        return ret;
    }

    private void rLeft(Node node){
        Node b = node.right;
        if(node.parent == null)
            root = b;
        b.parent = node.parent;
        node.right = b.left;
        node.right.parent = b;
        b.left = node;
        node.parent = b;
        if(b.diff==-1){
            node.diff = 0;
            b.diff = 0;
        }
        else{
            node.diff = -1;
            b.diff = 1;
        }
    }

    private void rRight(Node node){
        Node b = node.left;
        if(node.parent == null)
            root = b;
        b.parent = node.parent;
        node.left = b.right;
        node.left.parent = b;
        b.right = node;
        node.parent = b;
        if(b.diff==1){
            node.diff = 0;
            b.diff = 0;
        }
        else{
            node.diff = 1;
            b.diff = -1;
        }
    }
    private void rLeftBig(Node node){
        rRight(node.right);
        rLeft(node);
    }
    private void rRightBig(Node node){
        rLeft(node.left);
        rRight(node);
    }
    private void rotate(Node node){
        if(node.diff == -2){
            if(node.right.diff == -1 || node.right.diff == 0)
                rLeft(node);
            else
                rLeftBig(node);
        }
        else{
            if(node.left.diff == 1 || node.left.diff == 0)
                rRight(node);
            else
                rRightBig(node);
        }
    }
    private void rebalanceAdd(Node node){
        Node pointer;
        while(node.parent!=null && node.parent.diff!=0){
            pointer = node.parent;
            if(pointer.left == node)
                pointer.diff++;
            else
                pointer.diff--;
            if(pointer.diff==2 || pointer.diff==-2){
                rotate(pointer);
            }
        }
    }
    private void rebalanceRemove(Node node){
        Node pointer;
        while(node.parent!=null && node.parent.diff!=1 && node.parent.diff!=-1){
            pointer = node.parent;
            if(pointer.left == node)
                pointer.diff--;
            else
                pointer.diff++;
            if(pointer.diff==2 || pointer.diff==-2){
                rotate(pointer);
            }
        }
    }
    @Override
    public String put(Integer key, String value) {
        String ret = null;
        Node newNode = new Node(key, value);
        if (root == null) {
            root = newNode;
            size++;
            return null;
        }
        Node current = root;
        while (true) {
            if (current.key == key) {
                ret = current.value;
                current.value = value;
                return ret;
            }
            else if(current.key > key) {
                if (current.left == null) {
                    current.left = newNode;
                    newNode.parent = current;
                    size++;
                    rebalanceAdd(newNode);
                    return null;
                }
                current = current.left;
            }
            else {
                if (current.right == null) {
                    current.right = newNode;
                    newNode.parent = current;
                    size++;
                    rebalanceAdd(newNode);
                    return null;
                }
                current = current.right;
            }
        }
    }

    @Override
    public String remove(Object key) {

        Node current = root;
        String ret  = null;
        while((current != null) && (current.key != (Integer)key)){
            if(current.key < (Integer)key)
                current = current.right;
            else current = current.left;
        }
        if(current == null)
            return null;
        ret = current.value;
        size--;
        if(current.left == null && current.right == null) {
            if(current == root)
                root = null;
            else if(current.parent.left == current)
                current.parent.left = null;
            else
                current.parent.right = null;
        }
        else if(current.left == null) {
            if(current == root) {
                root = current.right;
                current.parent = null;
            }
            else if(current.parent.left == current) {
                current.parent.left = current.right;
                current.right.parent = current.parent;
            }
            else {
                current.parent.right = current.right;
                current.right.parent = current.parent;
            }
        }
        else if(current.right == null) {
            if(current == root) {
                root = current.left;
                current.parent = null;
            }
            else if(current.parent.left == current) {
                current.parent.left = current.left;
                current.left.parent = current.parent;
            }
            else {
                current.parent.right = current.left;
                current.left.parent = current.parent;
            }
        }
        else{
            Node pointer = current.right;
            while(pointer.left != null){
                pointer = pointer.left;
            }
            current.key = pointer.key;
            current.value = pointer.value;
            if(pointer.parent.right == pointer)
                pointer.parent.right = null;
            else
                pointer.parent.left = null;
            current = pointer;
        }
        if(current.parent != null)
            rebalanceRemove(current.parent);
        return ret;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
