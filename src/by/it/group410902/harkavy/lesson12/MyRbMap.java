package by.it.group410902.harkavy.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Set;

public class MyRbMap implements SortedMap<Integer, String> {

    // узел красно-черного дерева
    private static final class Node {
        int key;
        String val;
        Node left, right, parent;
        boolean red; // true = RED, false = BLACK
        Node(int key, String val, boolean red) { this.key = key; this.val = val; this.red = red; }
    }

    // общий NIL-страж
    private final Node NIL = new Node(0, null, false);
    private Node root = NIL;
    private int size = 0;

    // --- базовые операции дерева ---
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

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != NIL) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == NIL) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    private Node search(int key) {
        Node x = root;
        while (x != NIL) {
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else return x;
        }
        return NIL;
    }

    private Node minimum(Node x) { while (x.left != NIL) x = x.left; return x; }
    private Node maximum(Node x) { while (x.right != NIL) x = x.right; return x; }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    // --- фиксация после вставки ---
    private void insertFixup(Node z) {
        while (z.parent.red) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // дядя
                if (y.red) { // случай 1
                    z.parent.red = false; y.red = false; z.parent.parent.red = true; z = z.parent.parent;
                } else {
                    if (z == z.parent.right) { z = z.parent; leftRotate(z); } // случай 2
                    z.parent.red = false; z.parent.parent.red = true; rightRotate(z.parent.parent); // случай 3
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.red) {
                    z.parent.red = false; y.red = false; z.parent.parent.red = true; z = z.parent.parent;
                } else {
                    if (z == z.parent.left) { z = z.parent; rightRotate(z); }
                    z.parent.red = false; z.parent.parent.red = true; leftRotate(z.parent.parent);
                }
            }
        }
        root.red = false;
    }

    // --- фиксация после удаления ---
    private void deleteFixup(Node x) {
        while (x != root && !x.red) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.red) { w.red = false; x.parent.red = true; leftRotate(x.parent); w = x.parent.right; }
                if (!w.left.red && !w.right.red) { w.red = true; x = x.parent; }
                else {
                    if (!w.right.red) { w.left.red = false; w.red = true; rightRotate(w); w = x.parent.right; }
                    w.red = x.parent.red; x.parent.red = false; w.right.red = false; leftRotate(x.parent); x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.red) { w.red = false; x.parent.red = true; rightRotate(x.parent); w = x.parent.left; }
                if (!w.right.red && !w.left.red) { w.red = true; x = x.parent; }
                else {
                    if (!w.left.red) { w.right.red = false; w.red = true; leftRotate(w); w = x.parent.left; }
                    w.red = x.parent.red; x.parent.red = false; w.left.red = false; rightRotate(x.parent); x = root;
                }
            }
        }
        x.red = false;
    }

    // ================== обязательные методы ==================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(sb, root);
        return sb.append("}").toString();
    }

    private void appendInOrder(StringBuilder sb, Node x) {
        if (x == NIL) return;
        appendInOrder(sb, x.left);
        if (sb.length() > 1) sb.append(", ");
        sb.append(x.key).append("=").append(x.val);
        appendInOrder(sb, x.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node y = NIL, x = root;
        while (x != NIL) {
            y = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else { String old = x.val; x.val = value; return old; } // обновление
        }
        Node z = new Node(key, value, true); // новый — красный
        z.left = z.right = z.parent = NIL;
        z.parent = y;
        if (y == NIL) root = z;
        else if (key < y.key) y.left = z; else y.right = z;
        insertFixup(z);
        size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        Node z = search(key);
        if (z == NIL) return null;

        String old = z.val;
        Node y = z;
        boolean yRed = y.red;
        Node x;

        if (z.left == NIL) { x = z.right; transplant(z, z.right); }
        else if (z.right == NIL) { x = z.left; transplant(z, z.left); }
        else {
            y = minimum(z.right);
            yRed = y.red;
            x = y.right;
            if (y.parent == z) { x.parent = y; }
            else {
                transplant(y, y.right);
                y.right = z.right; y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left; y.left.parent = y;
            y.red = z.red;
        }
        if (!yRed) deleteFixup(x);
        size--;
        return old;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Node n = search((Integer) keyObj);
        return n == NIL ? null : n.val;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return search((Integer) keyObj) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node x, Object v) {
        if (x == NIL) return false;
        if (v == null ? x.val == null : v.equals(x.val)) return true;
        return containsValueRec(x.left, v) || containsValueRec(x.right, v);
    }

    @Override public int size() { return size; }

    @Override
    public void clear() { root = NIL; size = 0; }

    @Override public boolean isEmpty() { return size == 0; }

    // --- вьюхи (возвращаем новые карты с нужным диапазоном) ---

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        // строго меньше toKey
        MyRbMap m = new MyRbMap();
        copyIf(root, m, Integer.MIN_VALUE, toKey, false, /*includeHi*/ false);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        // больше либо равно fromKey
        MyRbMap m = new MyRbMap();
        copyIf(root, m, fromKey, Integer.MAX_VALUE, /*includeLo*/ true, false);
        return m;
    }

    private void copyIf(Node x, MyRbMap dst,
                        int lo, int hi, boolean includeLo, boolean includeHi) {
        if (x == NIL) return;
        if (x.key > lo || (includeLo && x.key == lo)) copyIf(x.left, dst, lo, hi, includeLo, includeHi);
        boolean geLo = includeLo ? x.key >= lo : x.key > lo;
        boolean leHi = includeHi ? x.key <= hi : x.key < hi;
        if (geLo && leHi) dst.put(x.key, x.val);     // копируем подходящие ключи
        if (x.key < hi || (includeHi && x.key == hi)) copyIf(x.right, dst, lo, hi, includeLo, includeHi);
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
    }

    // --- остальное не требуется заданием ---
    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { unsupported(); return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { unsupported(); return null; }
    @Override public Set<Integer> keySet() { unsupported(); return null; }
    @Override public Collection<String> values() { unsupported(); return null; }
    @Override public void putAll(java.util.Map<? extends Integer, ? extends String> m) { unsupported(); }
}
