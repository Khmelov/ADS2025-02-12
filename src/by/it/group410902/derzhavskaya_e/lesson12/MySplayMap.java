package by.it.group410902.derzhavskaya_e.lesson12;


import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        Node(Integer k, String v) {
            key = k;
            value = v;
        }
    }

    private Node root;
    private int size = 0;


    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
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
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node x = root;
        Node last = null;
        while (x != null) {
            last = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                splay(x);
                return x;
            } else if (cmp < 0) x = x.left;
            else x = x.right;
        }
        if (last != null) splay(last);
        return null;
    }

    private Node subtreeMin(Node n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node subtreeMax(Node n) {
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }


    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        Node x = root;
        Node parent = null;
        while (x != null) {
            parent = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                splay(x);
                return old;
            } else if (cmp < 0) x = x.left;
            else x = x.right;
        }

        Node n = new Node(key, value);
        n.parent = parent;
        if (key < parent.key) parent.left = n;
        else parent.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return (n == null) ? null : n.value;
    }

    @Override
    public String remove(Object key) {
        Node n = findNode((Integer) key);
        if (n == null) return null;

        splay(n);
        String val = n.value;

        if (n.left == null) {
            root = n.right;
            if (root != null) root.parent = null;
        } else {
            Node maxLeft = subtreeMax(n.left);
            splay(maxLeft);
            maxLeft.right = n.right;
            if (n.right != null) n.right.parent = maxLeft;
            root = maxLeft;
            root.parent = null;
        }

        size--;
        return val;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValueRecursive(root, (String) value);
    }

    private boolean containsValueRecursive(Node n, String value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValueRecursive(n.left, value) || containsValueRecursive(n.right, value);
    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }


    @Override
    public Integer firstKey() {
        Node n = subtreeMin(root);
        return (n == null) ? null : n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = subtreeMax(root);
        return (n == null) ? null : n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (key > x.key) {
                res = x.key;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (key >= x.key) {
                res = x.key;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (key <= x.key) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (key < x.key) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }


    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap m = new MySplayMap();
        fillSubMap(root, m, Integer.MIN_VALUE, toKey, false);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        fillSubMap(root, m, fromKey, Integer.MAX_VALUE, true);
        return m;
    }

    private void fillSubMap(Node n, MySplayMap m, int from, int to, boolean isTail) {
        if (n == null) return;
        fillSubMap(n.left, m, from, to, isTail);
        if ((!isTail && n.key < to) || (isTail && n.key >= from))
            m.put(n.key, n.value);
        fillSubMap(n.right, m, from, to, isTail);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node n, StringBuilder sb) {
        if (n == null) return;
        buildString(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        buildString(n.right, sb);
    }


    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

}
