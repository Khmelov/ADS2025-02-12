package by.it.group451004.maximenko.lesson12;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color = RED;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    // ------------ UTILS ------------

    private void checkKey(Object key) {
        if (key == null) throw new NullPointerException("Null key");
        if (!(key instanceof Integer))
            throw new ClassCastException("Only Integer keys supported");
    }

    // ------------ SEARCH ------------

    private Node findNode(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        checkKey(key);
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if ((n.value == null && value == null) ||
                (n.value != null && n.value.equals(value)))
            return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String get(Object key) {
        checkKey(key);
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    // ------------ ROTATIONS ------------

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

    // ------------ INSERTION ------------

    @Override
    public String put(Integer key, String value) {
        checkKey(key);
        Node parent = null;
        Node x = root;

        while (x != null) {
            parent = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                return old;
            }
            x = (cmp < 0) ? x.left : x.right;
        }

        Node n = new Node(key, value, RED, parent);
        if (parent == null) {
            root = n;
        } else if (key < parent.key) {
            parent.left = n;
        } else {
            parent.right = n;
        }

        fixInsert(n);
        size++;
        return null;
    }

    private void fixInsert(Node z) {
        while (z != root && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // дядя
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
                Node y = z.parent.parent.left; // дядя
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

    // ------------ DELETE ------------

    @Override
    public String remove(Object keyObj) {
        checkKey(keyObj);
        Integer key = (Integer) keyObj;

        Node z = findNode(key);
        if (z == null) return null;

        String old = z.value;

        deleteNode(z);
        size--;
        return old;
    }

    private Node minimum(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;

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
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            fixDelete(x, z.parent);
        }
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private void fixDelete(Node x, Node parent) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == parent.left) {
                Node w = parent.right;
                if (w == null) break;

                if (w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }

                if ((w.left == null || w.left.color == BLACK) &&
                        (w.right == null || w.right.color == BLACK)) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.right == null || w.right.color == BLACK) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    break;
                }
            } else { // зеркальный случай
                Node w = parent.left;
                if (w == null) break;

                if (w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }

                if ((w.left == null || w.left.color == BLACK) &&
                        (w.right == null || w.right.color == BLACK)) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || w.left.color == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    break;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    // ------------ SIZE / CLEAR ------------

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    // ------------ ORDERED METHODS ------------

    @Override
    public Integer firstKey() {
        if (root == null) throw new IllegalStateException("map is empty");
        return minimum(root).key;
    }

    private Node maximum(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new IllegalStateException("map is empty");
        return maximum(root).key;
    }

    // headMap — map с ключами < toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap res = new MyRbMap();
        fillHead(root, toKey, res);
        return res;
    }

    private void fillHead(Node n, Integer toKey, MyRbMap out) {
        if (n == null) return;
        if (n.key < toKey) {
            out.put(n.key, n.value);
            fillHead(n.left, toKey, out);
            fillHead(n.right, toKey, out);
        } else {
            fillHead(n.left, toKey, out);
        }
    }

    // tailMap — map с ключами >= fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap res = new MyRbMap();
        fillTail(root, fromKey, res);
        return res;
    }

    private void fillTail(Node n, Integer fromKey, MyRbMap out) {
        if (n == null) return;
        if (n.key >= fromKey) {
            out.put(n.key, n.value);
            fillTail(n.left, fromKey, out);
            fillTail(n.right, fromKey, out);
        } else {
            fillTail(n.right, fromKey, out);
        }
    }

    // ------------ toString() ------------

    @Override
    public String toString() {
        firstPrinted = false;              // ← ОБЯЗАТЕЛЬНО
        StringBuilder sb = new StringBuilder("{");
        printInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }


    private boolean firstPrinted = false;

    private void printInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        printInOrder(n.left, sb);
        if (firstPrinted) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        firstPrinted = true;
        printInOrder(n.right, sb);
    }

    // ------------ Methods required by SortedMap but unused ------------

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap res = new MyRbMap();
        fillSub(root, fromKey, toKey, res);
        return res;
    }

    private void fillSub(Node n, Integer fromKey, Integer toKey, MyRbMap out) {
        if (n == null) return;
        if (n.key >= fromKey && n.key < toKey) {
            out.put(n.key, n.value);
        }
        if (n.key > fromKey) fillSub(n.left, fromKey, toKey, out);
        if (n.key < toKey) fillSub(n.right, fromKey, toKey, out);
    }

    // Необязательные наборы коллекций — оставляем unimplemented
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet(){ throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet(){ throw new UnsupportedOperationException(); }
    @Override public Collection<String> values(){ throw new UnsupportedOperationException(); }

    @Override public void putAll(Map<? extends Integer, ? extends String> m){
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
}

