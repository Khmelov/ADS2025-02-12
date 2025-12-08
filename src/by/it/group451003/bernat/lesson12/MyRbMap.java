package by.it.group451003.bernat.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color; // true = red, false = black

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private static class NilNode extends Node {
        NilNode() {
            super(null, null, false, null);
        }
    }

    private Node root;
    private int size;

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    private void fixInsert(Node k) {
        while (k != root && k.parent.color) {
            if (k.parent == k.parent.parent.left) {
                Node u = k.parent.parent.right;
                if (u != null && u.color) {
                    k.parent.color = false;
                    u.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rotateRight(k.parent.parent);
                }
            } else {
                Node u = k.parent.parent.left;
                if (u != null && u.color) {
                    k.parent.color = false;
                    u.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rotateRight(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rotateLeft(k.parent.parent);
                }
            }
        }
        root.color = false;
    }

    @Override
    public String put(Integer key, String value) {
        Node node = root;
        Node parent = null;
        String old = null;
        while (node != null) {
            parent = node;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else {
                old = node.value;
                node.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value, true, parent);
        if (parent == null) root = newNode;
        else if (key.compareTo(parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        size++;
        fixInsert(newNode);
        return old;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private void fixDelete(Node x) {
        while (x != root && !x.color) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if ((w.left == null || !w.left.color) && (w.right == null || !w.right.color)) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (w.right == null || !w.right.color) {
                        if (w.left != null) w.left.color = false;
                        w.color = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    if (w.right != null) w.right.color = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if ((w.right == null || !w.right.color) && (w.left == null || !w.left.color)) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (w.left == null || !w.left.color) {
                        if (w.right != null) w.right.color = false;
                        w.color = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    if (w.left != null) w.left.color = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yColor = y.color;
        Node x = null;
        Node originalYParent = y.parent;
        boolean originalYIsLeft = false;
        if (originalYParent != null) {
            originalYIsLeft = y == originalYParent.left;
        }
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = findMin(z.right);
            yColor = y.color;
            x = y.right;
            originalYParent = y.parent;
            originalYIsLeft = false;
            if (originalYParent != null) {
                originalYIsLeft = y == originalYParent.left;
            }
            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (!yColor) {
            if (x != null) {
                fixDelete(x);
            } else {
                if (originalYParent == null) {
                    // do nothing, tree empty
                } else {
                    NilNode temp = new NilNode();
                    Node nilParent = (originalYParent == z) ? y : originalYParent;
                    boolean nilIsLeft = originalYIsLeft;
                    temp.parent = nilParent;
                    temp.color = false;
                    if (nilIsLeft) {
                        nilParent.left = temp;
                    } else {
                        nilParent.right = temp;
                    }
                    fixDelete(temp);
                    if (temp == temp.parent.left) {
                        temp.parent.left = null;
                    } else {
                        temp.parent.right = null;
                    }
                }
            }
        }
    }

    @Override
    public String remove(Object o) {
        if (!(o instanceof Integer)) return null;
        Integer key = (Integer) o;
        Node node = getNode(key);
        if (node == null) return null;
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    private Node getNode(Integer key) {
        Node node = root;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    @Override
    public String get(Object o) {
        if (!(o instanceof Integer)) return null;
        Node node = getNode((Integer) o);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object o) {
        if (!(o instanceof Integer)) return false;
        return getNode((Integer) o) != null;
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (node.value.equals(value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object o) {
        return containsValue(root, o);
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
    public void clear() {
        root = null;
        size = 0;
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void addToHeadMap(Node node, MyRbMap sub, Integer toKey) {
        if (node == null) return;
        addToHeadMap(node.left, sub, toKey);
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) sub.put(node.key, node.value);
        if (cmp < 0) addToHeadMap(node.right, sub, toKey);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        addToHeadMap(root, sub, toKey);
        return sub;
    }

    private void addToTailMap(Node node, MyRbMap sub, Integer fromKey) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) addToTailMap(node.left, sub, fromKey);
        if (cmp >= 0) sub.put(node.key, node.value);
        addToTailMap(node.right, sub, fromKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        addToTailMap(root, sub, fromKey);
        return sub;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    private boolean checkEntries(Node node, Map<?, ?> m) {
        if (node == null) return true;
        if (!checkEntries(node.left, m)) return false;
        Object val = m.get(node.key);
        if (node.value == null ? val != null : !node.value.equals(val)) return false;
        return checkEntries(node.right, m);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) return false;
        return checkEntries(root, m);
    }

    @Override
    public int hashCode() {
        return calculateHash(root);
    }

    private int calculateHash(Node node) {
        if (node == null) return 0;
        int h = (node.key == null ? 0 : node.key.hashCode()) ^ (node.value == null ? 0 : node.value.hashCode());
        return calculateHash(node.left) + h + calculateHash(node.right) * 31;
    }

    // Unsupported
    @Override
    public Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}