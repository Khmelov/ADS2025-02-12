package by.it.group451001.drzhevetskiy.lesson12;
import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {


    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(int key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    private boolean isRed(Node n) {
        return n != null && n.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.left = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.right = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = RED;
        if (h.left != null) h.left.color = BLACK;
        if (h.right != null) h.right.color = BLACK;
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        root = insert(root, key, value, null);
        root.color = BLACK;
        if (old == null) size++;
        return old;
    }

    private Node insert(Node h, int key, String value, Node parent) {
        if (h == null) return new Node(key, value, RED, parent);

        if (key < h.key) h.left = insert(h.left, key, value, h);
        else if (key > h.key) h.right = insert(h.right, key, value, h);
        else {
            h.value = value;
            return h;
        }

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }


    @Override
    public String get(Object key) {
        Node n = root;
        int k = (Integer) key;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public String remove(Object key) {
        String old = get(key);
        if (old != null) {
            root = delete(root, (Integer) key);
            size--;
        }
        return old;
    }

    private Node delete(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) h.left = delete(h.left, key);
        else if (key > h.key) h.right = delete(h.right, key);
        else {
            if (h.left == null) return h.right;
            if (h.right == null) return h.left;
            Node min = h.right;
            while (min.left != null) min = min.left;
            h.key = min.key;
            h.value = min.value;
            h.right = delete(h.right, min.key);
        }
        return h;
    }


    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }


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


    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, map, toKey);
        return map;
    }

    private void headMap(Node n, MyRbMap map, int toKey) {
        if (n == null) return;
        if (n.key < toKey) {
            map.put(n.key, n.value);
            headMap(n.left, map, toKey);
            headMap(n.right, map, toKey);
        } else {
            headMap(n.left, map, toKey);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, map, fromKey);
        return map;
    }

    private void tailMap(Node n, MyRbMap map, int fromKey) {
        if (n == null) return;
        if (n.key >= fromKey) {
            map.put(n.key, n.value);
            tailMap(n.left, map, fromKey);
            tailMap(n.right, map, fromKey);
        } else {
            tailMap(n.right, map, fromKey);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }


    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}
