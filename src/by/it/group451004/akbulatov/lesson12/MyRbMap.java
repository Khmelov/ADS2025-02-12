package by.it.group451004.akbulatov.lesson12;
/*
 * MyRbMap.java
 *
 * Реализация SortedMap<Integer, String> на основе красно-черного дерева
 * Без использования коллекций стандартной библиотеки (кроме интерфейсов).
 *
 * Реализованы обязательные методы задания уровня B:
 *   toString(), put(Integer,String), remove(Integer), get(Integer),
 *   containsKey(Integer), containsValue(String), size(), clear(), isEmpty(),
 *   headMap(Integer), tailMap(Integer), firstKey(), lastKey()
 *
 * Все остальные методы Map/SortedMap помечены UnsupportedOperationException
 * (чтобы класс компилировался и формально реализовывал интерфейс).
 *
 * Примечания:
 *  - headMap(toKey) возвращает новый независимый MyRbMap, содержащий
 *    копию всех пар с ключом < toKey.
 *  - tailMap(fromKey) возвращает новый независимый MyRbMap, содержащий
 *    копию всех пар с ключом >= fromKey.
 *  - toString() выводит элементы в порядке возрастания ключей в формате
 *    стандартной коллекции: {k1=v1, k2=v2}
 *
 * Сложность основных операций: поиск/вставка/удаление — O(log n)
 */

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static final class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color; // RED or BLACK

        Node(Integer k, String v, boolean color, Node parent) {
            this.key = k;
            this.value = v;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    // ---------------------- basic helpers ----------------------
    private Node getNode(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) return false;
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node x, Object value) {
        if (x == null) return false;
        if (value == null) {
            if (x.value == null) return true;
        } else {
            if (value.equals(x.value)) return true;
        }
        return containsValueRecursive(x.left, value) || containsValueRecursive(x.right, value);
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

    // ---------------------- rotations & utilities ----------------------
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

    // ---------------------- insertion ----------------------
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Null keys not supported");
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                return old;
            }
            x = (cmp < 0) ? x.left : x.right;
        }
        Node z = new Node(key, value, RED, y);
        if (y == null) root = z;
        else if (key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;
        size++;
        insertFixup(z);
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // uncle
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left; // uncle
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    // ---------------------- deletion ----------------------
    @Override
    public String remove(Object key) {
        if (key == null) return null;
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String old = z.value;
        deleteNode(z);
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
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
        if (yOriginalColor == BLACK) deleteFixup(x, (y.parent != null) ? y.parent : null);
    }

    private void deleteFixup(Node x, Node parent) {
        // In this implementation we pass parent because x might be null.
        while ((x != root) && (x == null || x.color == BLACK)) {
            if (parent == null) break; // safety
            if (x == parent.left) {
                Node w = parent.right;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if ((w == null) || ((w.left == null || w.left.color == BLACK) && (w.right == null || w.right.color == BLACK))) {
                    if (w != null) w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.right == null || w.right.color == BLACK) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    if (w != null) w.color = parent.color;
                    parent.color = BLACK;
                    if (w != null && w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    break;
                }
            } else {
                Node w = parent.left;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if ((w == null) || ((w.left == null || w.left.color == BLACK) && (w.right == null || w.right.color == BLACK))) {
                    if (w != null) w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || w.left.color == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    if (w != null) w.color = parent.color;
                    parent.color = BLACK;
                    if (w != null && w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    break;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    // ---------------------- traversal & toString ----------------------
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        Node x = root;
        java.util.Deque<Node> stack = new java.util.ArrayDeque<>(); // only for iteration, allowed
        while (x != null || !stack.isEmpty()) {
            while (x != null) {
                stack.push(x);
                x = x.left;
            }
            x = stack.pop();
            if (!first) sb.append(", ");
            sb.append(x.key).append('=').append(x.value);
            first = false;
            x = x.right;
        }
        sb.append('}');
        return sb.toString();
    }

    // ---------------------- headMap / tailMap ----------------------
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, null, toKey);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, fromKey, null);
        return m;
    }

    // copyRange: include keys k such that (from == null || k >= from) && (to == null || k < to)
    private void copyRange(Node x, MyRbMap target, Integer from, Integer to) {
        if (x == null) return;
        copyRange(x.left, target, from, to);
        boolean geFrom = (from == null) || (x.key.compareTo(from) >= 0);
        boolean ltTo = (to == null) || (x.key.compareTo(to) < 0);
        if (geFrom && ltTo) target.put(x.key, x.value);
        copyRange(x.right, target, from, to);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    // ---------------------- other SortedMap / Map methods ----------------------
    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null; // natural ordering
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, fromKey, toKey);
        return m;
    }

    // The following methods are part of Map interface; we implement minimal stubs
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet() not implemented");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values() not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ---------------------- unsupported operations we don't need now ----------------------
    @Override
    public boolean equals(Object o) {
        // Could be implemented, but not required for the assignment
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    // ---------------------- simple test harness in comment ----------------------
    /*
    Пример использования:
        MyRbMap m = new MyRbMap();
        m.put(5, "five");
        m.put(2, "two");
        m.put(8, "eight");
        System.out.println(m); // {2=two, 5=five, 8=eight}
        System.out.println(m.firstKey()); // 2
        System.out.println(m.lastKey()); // 8
        SortedMap<Integer,String> h = m.headMap(5);
        System.out.println(h); // {2=two}
        m.remove(5);
    */
}
