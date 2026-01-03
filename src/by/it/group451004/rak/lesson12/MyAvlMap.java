package by.it.group451004.rak.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private Node root = null;
    private int size = 0;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        if (old == null) size++;
        root = AvlTree.insert(root, key, value);
        return old;
    }


    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer key)) return null;
        String old = get(key);
        if (old == null) return null;
        size--;
        root = AvlTree.remove(root, key);
        return old;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer key)) return null;
        Node cur = root;
        while (cur != null) {
            if (key.equals(cur.key)) return cur.value;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        return get(keyObj) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //ЗАГЛУШКИ

    @Override
    public boolean containsValue(Object value) { return false; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Set<Integer> keySet() { return Set.of(); }

    @Override
    public Collection<String> values() { return List.of(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { return Set.of(); }
}

class Node {
    Integer key;
    String value;
    Node left = null, right = null;
    int height = 1;

    Node(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}

class AvlTree{
    static int height(Node node){ //высота null равна 0
        return node == null ? 0 : node.height;
    }
    static int bFactor(Node node){ //для прроверки разбалансировки
        return height(node.right)-height(node.left);
    }

    static void fixHeight(Node node){
        int left = height(node.left);
        int right = height(node.right);
        node.height = 1 + Math.max(left, right);
    }
    static Node rotateRight(Node b){
        Node a = b.left;
        b.left = a.right;
        a.right = b;
        fixHeight(b);
        fixHeight(a);
        return a;
    }

    static Node rotateLeft(Node a){
        Node b = a.right;
        a.right = b.left;
        b.left = a;
        fixHeight(a);
        fixHeight(b);
        return b;
    }

    static Node balance(Node p) {
        fixHeight(p);
        if (bFactor(p) == 2){
            if (bFactor(p.right) < 0)
                p.right = rotateRight(p.right);
            return rotateLeft(p);
        }
        if (bFactor(p) == -2){
            if (bFactor(p.left) > 0)
                p.left = rotateLeft(p.left);
            return rotateRight(p);
        }
        return p;
    }

    static Node insert(Node p, Integer key, String value){
        if (p == null) return new Node(key, value);

        if (key.equals(p.key)) {
            p.value = value;
            return p;
        } else if (key < p.key) {
            p.left = insert(p.left, key, value);
        } else {
            p.right = insert(p.right, key, value);
        }

        return balance(p);
    }

    static Node remove(Node p, Integer key) {
        if (p == null) return null;

        if (key < p.key) p.left = remove(p.left, key);
        else if (key > p.key) p.right = remove(p.right, key);
        else {
            Node L = p.left, R = p.right;
            if (R == null) return L;
            Node min = findMin(R);
            min.right = removeMin(R);
            min.left = L;
            return balance(min);
        }

        return balance(p);
    }

    static Node findMin(Node p) {
        return p.left == null ? p : findMin(p.left);
    }

    static Node removeMin(Node p) {
        if (p.left == null) return p.right;
        p.left = removeMin(p.left);
        return balance(p);
    }
}

