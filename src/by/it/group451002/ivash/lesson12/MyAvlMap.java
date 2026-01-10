package by.it.group451002.ivash.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;

        Node left;
        Node right;
        int height;

        Node(Integer k, String v) {
            key = k;
            value = v;
            height = 1;
        }
    }

    private Node root;
    private int size = 0;

    // -----------------------------------------------------------
    //                 ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // -----------------------------------------------------------

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        if (bf > 1) {  // левый перегруз
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }

        if (bf < -1) { // правый перегруз
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }

        return n;
    }

    // -----------------------------------------------------------
    //                      PUT
    // -----------------------------------------------------------

    @Override
    public String put(Integer key, String value) {
        String[] prev = new String[1]; // сохраняем старое значение
        root = insert(root, key, value, prev);
        if (prev[0] == null) size++;
        return prev[0];
    }

    private Node insert(Node n, Integer key, String value, String[] prev) {
        if (n == null) return new Node(key, value);

        int cmp = key.compareTo(n.key);

        if (cmp < 0) {
            n.left = insert(n.left, key, value, prev);
        } else if (cmp > 0) {
            n.right = insert(n.right, key, value, prev);
        } else {
            prev[0] = n.value;
            n.value = value;
            return n;
        }

        return balance(n);
    }

    // -----------------------------------------------------------
    //                     GET + containsKey
    // -----------------------------------------------------------

    @Override
    public String get(Object key) {
        Node n = root;
        Integer k = (Integer) key;

        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // -----------------------------------------------------------
    //                       REMOVE
    // -----------------------------------------------------------

    @Override
    public String remove(Object key) {
        String[] removed = new String[1];
        root = removeNode(root, (Integer) key, removed);
        if (removed[0] != null) size--;
        return removed[0];
    }

    private Node removeNode(Node n, Integer key, String[] removed) {
        if (n == null) return null;

        int cmp = key.compareTo(n.key);

        if (cmp < 0) {
            n.left = removeNode(n.left, key, removed);
        } else if (cmp > 0) {
            n.right = removeNode(n.right, key, removed);
        } else {
            removed[0] = n.value;

            // один ребёнок или нет детей
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;

            // два ребёнка
            Node min = findMin(n.right);
            n.key = min.key;
            n.value = min.value;
            n.right = removeNode(n.right, min.key, new String[1]);
        }

        return balance(n);
    }

    private Node findMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    // -----------------------------------------------------------
    //                     size, clear, isEmpty
    // -----------------------------------------------------------

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

    // -----------------------------------------------------------
    //                       toString()
    // -----------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    // -----------------------------------------------------------
    //  НЕОБЯЗАТЕЛЬНЫЕ методы Map (можно оставить пустыми)
    // -----------------------------------------------------------

    @Override public boolean containsValue(Object v) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
}
