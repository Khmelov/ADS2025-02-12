package by.it.group410902.plekhova.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/*
 Создайте class MyRbMap, который реализует интерфейс SortedMap<Integer, String>
    и работает на основе красно-черного дерева
 */
public class MyRbMap implements SortedMap<Integer, String> {

    private final Node NIL = new Node();
    private Node root;
    private int size = 0;

    public MyRbMap() {

        NIL.color = BLACK;
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }

    // цвета
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // узел дерева
    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;


        Node() {
            this.key = null;
            this.value = null;
            this.left = this.right = this.parent = null;
            this.color = BLACK;
        }

        // для обычных узлов
        Node(Integer k, String v) {
            this.key = k;
            this.value = v;
            this.left = NIL;
            this.right = NIL;
            this.parent = NIL;
            this.color = RED;
        }
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private Node getNode(Integer key) {
        Node cur = root;
        while (cur != NIL) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return NIL;
    }

    private Node minimum(Node x) {
        while (x.left != NIL) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        while (x.right != NIL) x = x.right;
        return x;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null keys not supported");

        Node y = NIL;
        Node x = root;
        while (x != NIL) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                return old;
            } else if (cmp < 0) x = x.left;
            else x = x.right;
        }

        Node z = new Node(key, value);
        z.parent = y;
        if (y == NIL) root = z;
        else if (key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;

        insertFixup(z);
        size++;
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }



    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node z = getNode(key);
        if (z == NIL) return null;

        String old = z.value;
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
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

        if (yOriginalColor == BLACK) {
            deleteFixup(x);
        }

        size--;
        return old;
    }

    private void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    /* ------------------ доступ + проверки ------------------ */

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Node n = getNode((Integer) keyObj);
        return (n == NIL) ? null : n.value;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return getNode((Integer) keyObj) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == NIL) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }



    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = new boolean[] { true }; // mutable wrapper
        inorderToString(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inorderToString(Node n, StringBuilder sb, boolean[] first) {
        if (n == NIL) return;
        inorderToString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inorderToString(n.right, sb, first);
    }



    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException("Map is empty");
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException("Map is empty");
        return maximum(root).key;
    }

    //  возвращает все элементы карты Map, где ключ <= fromKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap res = new MyRbMap();
        headRec(root, res, toKey);
        return res;
    }

    private void headRec(Node n, MyRbMap res, Integer toKey) {
        if (n == NIL) return;
        headRec(n.left, res, toKey);
        if (n.key.compareTo(toKey) < 0) res.put(n.key, n.value);
        headRec(n.right, res, toKey);
    }


    //  возвращает все элементы карты Map, где ключ ≥ fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap res = new MyRbMap();
        tailRec(root, res, fromKey);
        return res;
    }

    private void tailRec(Node n, MyRbMap res, Integer fromKey) {
        if (n == NIL) return;
        tailRec(n.left, res, fromKey);
        if (n.key.compareTo(fromKey) >= 0) res.put(n.key, n.value);
        tailRec(n.right, res, fromKey);
    }



    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
}

