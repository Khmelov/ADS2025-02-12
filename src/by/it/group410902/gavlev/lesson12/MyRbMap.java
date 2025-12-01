package by.it.group410902.gavlev.lesson12;

import java.util.SortedMap;
import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        boolean red;
        Node left, right, parent;

        Node(Integer key, String value, boolean red, Node parent) {
            this.key = key;
            this.value = value;
            this.red = red;
            this.parent = parent;
        }
    }

    private Node root = null;
    private int size = 0;

    private int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }

    private Node minimum(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maximum(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    private void leftTurn(Node x) {
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

    private void rightTurn(Node y) {
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

    @Override
    public String put(Integer key, String value) {
        Node parent = null;
        Node cur = root;
        while (cur != null) {
            parent = cur;
            int cmp = compare(key, cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                return old;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        Node newNode = new Node(key, value, true, parent);
        if (parent == null) root = newNode;
        else if (compare(key, parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        fixInsert(newNode);
        size++;
        return null;
    }

    private void fixInsert(Node z) {
        while (z.parent != null && z.parent.red) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftTurn(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rightTurn(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightTurn(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    leftTurn(z.parent.parent);
                }
            }
        }
        root.red = false;
    }

    @Override
    public String get(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = compare((Integer) key, cur.key);
            if (cmp == 0) return cur.value;
            else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (java.util.Objects.equals(node.value, value)) return true;
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }


    @Override
    public String remove(Object key) {
        Node node = root;
        while (node != null) {
            int cmp = compare((Integer) key, node.key);
            if (cmp == 0) break;
            else if (cmp < 0) node = node.left;
            else node = node.right;
        }
        if (node == null) return null;
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yRed = y.red;
        Node x;
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yRed = y.red;
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
            y.red = z.red;
        }
        if (!yRed && x != null) fixDelete(x);
    }

    private void fixDelete(Node x) {
        while (x != root && !x.red) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    leftTurn(x.parent);
                    w = x.parent.right;
                }
                if ((w.left == null || !w.left.red) &&
                        (w.right == null || !w.right.red)) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (w.right == null || !w.right.red) {
                        if (w.left != null) w.left.red = false;
                        w.red = true;
                        rightTurn(w);
                        w = x.parent.right;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    if (w.right != null) w.right.red = false;
                    leftTurn(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.red) {
                    w.red = false;
                    x.parent.red = true;
                    rightTurn(x.parent);
                    w = x.parent.left;
                }
                if ((w.right == null || !w.right.red) &&
                        (w.left == null || !w.left.red)) {
                    w.red = true;
                    x = x.parent;
                } else {
                    if (w.left == null || !w.left.red) {
                        if (w.right != null) w.right.red = false;
                        w.red = true;
                        leftTurn(w);
                        w = x.parent.left;
                    }
                    w.red = x.parent.red;
                    x.parent.red = false;
                    if (w.left != null) w.left.red = false;
                    rightTurn(x.parent);
                    x = root;
                }
            }
        }
        x.red = false;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        return maximum(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        headRec(root, sub, toKey);
        return sub;
    }

    private void headRec(Node node, MyRbMap sub, Integer toKey) {
        if (node == null) return;
        if (compare(node.key, toKey) < 0) {
            sub.put(node.key, node.value);
            headRec(node.left, sub, toKey);
            headRec(node.right, sub, toKey);
        } else {
            headRec(node.left, sub, toKey);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        tailRec(root, sub, fromKey);
        return sub;
    }

    private void tailRec(Node node, MyRbMap sub, Integer fromKey) {
        if (node == null) return;
        if (compare(node.key, fromKey) >= 0) {
            sub.put(node.key, node.value);
            tailRec(node.left, sub, fromKey);
            tailRec(node.right, sub, fromKey);
        } else {
            tailRec(node.right, sub, fromKey);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRec(root, sb);
        // удалить последнюю запятую и пробел
        if (sb.length() >= 2) sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    private void toStringRec(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringRec(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toStringRec(node.right, sb);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap sub = new MyRbMap();
        subMapRec(root, sub, fromKey, toKey);
        return sub;
    }

    private void subMapRec(Node node, MyRbMap sub, Integer fromKey, Integer toKey) {
        if (node == null) return;
        if (compare(node.key, fromKey) >= 0 && compare(node.key, toKey) < 0) {
            sub.put(node.key, node.value);
        }
        if (compare(node.key, fromKey) > 0) subMapRec(node.left, sub, fromKey, toKey);
        if (compare(node.key, toKey) < 0) subMapRec(node.right, sub, fromKey, toKey);
    }

    @Override
    public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}
