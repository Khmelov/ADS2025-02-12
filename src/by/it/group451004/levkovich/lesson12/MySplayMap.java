package by.it.group451004.levkovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.Comparator;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        int key;
        String val;
        Node left, right;
        Node(int k, String v) { key = k; val = v; }
    }

    private Node root;
    private int size;

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node splay(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) {
            if (h.left == null) return h;
            if (key < h.left.key) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (key > h.left.key) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) h.left = rotateLeft(h.left);
            }
            return h.left == null ? h : rotateRight(h);
        } else if (key > h.key) {
            if (h.right == null) return h;
            if (key > h.right.key) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            } else if (key < h.right.key) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) h.right = rotateRight(h.right);
            }
            return h.right == null ? h : rotateLeft(h);
        } else {
            return h;
        }
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = ((Integer) key).intValue();
        root = splay(root, k); // поднимаем ближайший узел к ключу
        if (root == null || root.key != k) return null;
        return root.val;
    }

    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        int k = key.intValue();
        if (root == null) {
            root = new Node(k, value);
            size++;
            return null;
        }
        root = splay(root, k);
        if (k == root.key) {
            String old = root.val;
            root.val = value;
            return old;
        }
        Node n = new Node(k, value);
        if (k < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }
        size++;
        return null;
    }

    private Node minNode(Node x) {
        if (x == null) return null;
        while (x.left != null) x = x.left;
        return x;
    }
    private Node maxNode(Node x) {
        if (x == null) return null;
        while (x.right != null) x = x.right;
        return x;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null) return null;
        if (root == null) return null;
        int k = key.intValue();
        root = splay(root, k);
        if (root.key != k) return null;
        String old = root.val;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSub = root.right;
            Node leftSub = root.left;
            leftSub = splay(leftSub, maxNode(leftSub).key);
            leftSub.right = rightSub;
            root = leftSub;
        }
        size--;
        return old;
    }

    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }
    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.val == null : value.equals(n.val)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    public int size() { return size; }
    public void clear() { root = null; size = 0; }
    public boolean isEmpty() { return size == 0; }

    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRec(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }
    private void toStringRec(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        toStringRec(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append("=").append(n.val);
        first[0] = false;
        toStringRec(n.right, sb, first);
    }

    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap m = new MySplayMap();
        collectHead(root, toKey.intValue(), m);
        return m;
    }

    private void collectHead(Node n, int toKey, MySplayMap m) {
        if (n == null) return;
        if (n.key < toKey) {
            collectHead(n.left, toKey, m);
            m.put(n.key, n.val);
            collectHead(n.right, toKey, m);
        } else {
            collectHead(n.left, toKey, m);
        }
    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap m = new MySplayMap();
        collectTail(root, fromKey.intValue(), m);
        return m;
    }

    private void collectTail(Node n, int fromKey, MySplayMap m) {
        if (n == null) return;
        if (n.key >= fromKey) {
            collectTail(n.left, fromKey, m);
            m.put(n.key, n.val);
            collectTail(n.right, fromKey, m);
        } else {
            collectTail(n.right, fromKey, m);
        }
    }

    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = minNode(root);
        return x.key;
    }
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = maxNode(root);
        return x.key;
    }

    public Integer lowerKey(Integer k) {
        if (k == null) throw new NullPointerException();
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key < k) {
                res = x.key;
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return res;
    }

    public Integer floorKey(Integer k) {
        if (k == null) throw new NullPointerException();
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key <= k) {
                res = x.key;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    public Integer ceilingKey(Integer k) {
        if (k == null) throw new NullPointerException();
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key >= k) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    public Integer higherKey(Integer k) {
        if (k == null) throw new NullPointerException();
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key > k) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    public Comparator<? super Integer> comparator() { return null; }
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        if (fromKey.compareTo(toKey) > 0) throw new IllegalArgumentException();
        MySplayMap m = new MySplayMap();
        collectRange(root, fromKey.intValue(), fromInclusive, toKey.intValue(), toInclusive, m);
        return m;
    }

    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap m = new MySplayMap();
        collectRange(root, Integer.MIN_VALUE, true, toKey.intValue(), inclusive, m);
        return m;
    }

    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap m = new MySplayMap();
        collectRange(root, fromKey.intValue(), inclusive, Integer.MAX_VALUE, true, m);
        return m;
    }

    private void collectRange(Node n, int from, boolean fromInc, int to, boolean toInc, MySplayMap m) {
        if (n == null) return;
        if (n.key > from || (fromInc && n.key == from)) collectRange(n.left, from, fromInc, to, toInc, m);
        if ((n.key > from || (fromInc && n.key == from)) && (n.key < to || (toInc && n.key == to))) m.put(n.key, n.val);
        if (n.key < to || (toInc && n.key == to)) collectRange(n.right, from, fromInc, to, toInc, m);
    }
    public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Integer key) { return containsKey((Object) key); }
    public boolean equals(Object o) { return this == o; }
    public int hashCode() { return System.identityHashCode(this); }
}