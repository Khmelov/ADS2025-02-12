package by.it.group451001.volynets.lesson12;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static final class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color = RED;
        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private Node grandparent(Node n) {
        return n != null && n.parent != null ? n.parent.parent : null;
    }

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

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            Node g = z.parent.parent;
            if (z.parent == g.left) {
                Node u = g.right;
                if (u != null && u.color == RED) {
                    z.parent.color = BLACK;
                    u.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    g.color = RED;
                    rotateRight(g);
                }
            } else {
                Node u = g.left;
                if (u != null && u.color == RED) {
                    z.parent.color = BLACK;
                    u.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    g.color = RED;
                    rotateLeft(g);
                }
            }
        }
        root.color = BLACK;
    }

    private static final class Holder<T> { T value; }

    private Node bstPut(int key, String value, Holder<String> oldVal) {
        if (root == null) {
            root = new Node(key, value);
            root.color = BLACK;
            size = 1;
            return root;
        }
        Node cur = root, parent = null;
        int dir = 0;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
                dir = -1;
            } else if (key > cur.key) {
                cur = cur.right;
                dir = 1;
            } else {
                oldVal.value = cur.value;
                cur.value = value;
                return cur;
            }
        }
        Node z = new Node(key, value);
        z.parent = parent;
        if (dir < 0) parent.left = z; else parent.right = z;
        size++;
        insertFixup(z);
        return z;
    }

    private Node treeMin(Node x) {
        while (x != null && x.left != null) x = x.left;
        return x;
    }

    private Node treeMax(Node x) {
        while (x != null && x.right != null) x = x.right;
        return x;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private void deleteFixup(Node x, Node xParent) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == (xParent != null ? xParent.left : null)) {
                Node w = xParent.right;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateLeft(xParent);
                    w = xParent.right;
                }
                boolean wLeftBlack = (w == null || w.left == null || w.left.color == BLACK);
                boolean wRightBlack = (w == null || w.right == null || w.right.color == BLACK);
                if (wLeftBlack && wRightBlack) {
                    if (w != null) w.color = RED;
                    x = xParent;
                    xParent = xParent != null ? xParent.parent : null;
                } else {
                    if (w == null) {
                        x = xParent;
                        xParent = xParent != null ? xParent.parent : null;
                    } else {
                        if (w.right == null || w.right.color == BLACK) {
                            if (w.left != null) w.left.color = BLACK;
                            w.color = RED;
                            rotateRight(w);
                            w = xParent.right;
                        }
                        if (w != null) w.color = xParent.color;
                        xParent.color = BLACK;
                        if (w != null && w.right != null) w.right.color = BLACK;
                        rotateLeft(xParent);
                        x = root;
                        xParent = null;
                    }
                }
            } else {
                Node w = xParent.left;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateRight(xParent);
                    w = xParent.left;
                }
                boolean wLeftBlack = (w == null || w.left == null || w.left.color == BLACK);
                boolean wRightBlack = (w == null || w.right == null || w.right.color == BLACK);
                if (wLeftBlack && wRightBlack) {
                    if (w != null) w.color = RED;
                    x = xParent;
                    xParent = xParent != null ? xParent.parent : null;
                } else {
                    if (w == null) {
                        x = xParent;
                        xParent = xParent != null ? xParent.parent : null;
                    } else {
                        if (w.left == null || w.left.color == BLACK) {
                            if (w.right != null) w.right.color = BLACK;
                            w.color = RED;
                            rotateLeft(w);
                            w = xParent.left;
                        }
                        if (w != null) w.color = xParent.color;
                        xParent.color = BLACK;
                        if (w != null && w.left != null) w.left.color = BLACK;
                        rotateRight(xParent);
                        x = root;
                        xParent = null;
                    }
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = new boolean[]{true};
        inOrderAppend(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inOrderAppend(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        inOrderAppend(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inOrderAppend(n.right, sb, first);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null key");
        Holder<String> oldVal = new Holder<>();
        bstPut(key, value, oldVal);
        return oldVal.value;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node z = findNode(k);
        if (z == null) return null;
        String old = z.value;

        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;
        Node xParent;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = treeMin(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            y.color = z.color;
        }

        size--;
        if (yOriginalColor == BLACK) {
            deleteFixup(x, xParent);
        }
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return dfsContainsValue(root, value);
    }

    private boolean dfsContainsValue(Node n, Object v) {
        if (n == null) return false;
        if (v == null ? n.value == null : v.equals(n.value)) return true;
        return dfsContainsValue(n.left, v) || dfsContainsValue(n.right, v);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) return;
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException("null key");
        MyRbMap m = new MyRbMap();
        inOrderCopy(root, m, Integer.MIN_VALUE, toKey, true, false);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException("null key");
        MyRbMap m = new MyRbMap();
        inOrderCopy(root, m, fromKey, Integer.MAX_VALUE, false, true);
        return m;
    }

    private void inOrderCopy(Node n, MyRbMap dst, int from, int to, boolean toExclusive, boolean fromInclusive) {
        if (n == null) return;
        inOrderCopy(n.left, dst, from, to, toExclusive, fromInclusive);
        boolean geFrom = fromInclusive ? n.key >= from : n.key > from;
        boolean ltTo = toExclusive ? n.key < to : n.key <= to;
        if (geFrom && ltTo) dst.put(n.key, n.value);
        inOrderCopy(n.right, dst, from, to, toExclusive, fromInclusive);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return treeMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return treeMax(root).key;
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
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
}