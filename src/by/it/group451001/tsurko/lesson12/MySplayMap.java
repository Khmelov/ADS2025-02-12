package by.it.group451001.tsurko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static final class Node {
        int key;
        String value;
        Node left, right, parent;
        Node(int k, String v) { key = k; value = v; }
    }

    private Node root;
    private int size;

    // ======= Splay core =======

    private void rotateLeft(Node x) {
        Node y = x.right;
        Node p = x.parent;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.left = x;
        x.parent = y;
        y.parent = p;
        if (p == null) root = y;
        else if (p.left == x) p.left = y; else p.right = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        Node p = x.parent;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.right = x;
        x.parent = y;
        y.parent = p;
        if (p == null) root = y;
        else if (p.left == x) p.left = y; else p.right = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p); else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                rotateRight(g); rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g); rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                rotateLeft(p); rotateRight(g);
            } else {
                rotateRight(p); rotateLeft(g);
            }
        }
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

    private Node findNode(int key, boolean splayFoundOrLast) {
        Node cur = root, last = null;
        while (cur != null) {
            last = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else { if (splayFoundOrLast) splay(cur); return cur; }
        }
        if (splayFoundOrLast && last != null) splay(last);
        return null;
    }

    // ======= Basic ops =======

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null key");
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root, parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }
        Node n = new Node(key, value);
        n.parent = parent;
        if (key < parent.key) parent.left = n; else parent.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String remove(Object k) {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node n = findNode(key, true);
        if (n == null) return null; // root is now last accessed due to splay in findNode
        String old = n.value;

        Node left = n.left;
        Node right = n.right;
        if (left != null) left.parent = null;
        if (right != null) right.parent = null;

        // remove node
        n.left = n.right = n.parent = null;

        if (left == null) {
            root = right;
        } else {
            Node m = subtreeMax(left);
            splay(m);
            root.right = right;
            if (right != null) right.parent = root;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object k) {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node n = findNode(key, true);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object k) {
        if (!(k instanceof Integer)) return false;
        Integer key = (Integer) k;
        Node n = findNode(key, true);
        return n != null;
    }

    @Override
    public boolean containsValue(Object v) {
        return dfsContainsValue(root, v);
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

    // ======= Ordered queries =======

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node m = subtreeMin(root);
        splay(m);
        return m.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node m = subtreeMax(root);
        splay(m);
        return m.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException("null key");
        Node cur = root, cand = null;
        while (cur != null) {
            if (key <= cur.key) {
                cur = cur.left;
            } else {
                cand = cur;
                cur = cur.right;
            }
        }
        if (cand != null) splay(cand);
        return cand == null ? null : cand.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException("null key");
        Node cur = root, cand = null;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else {
                cand = cur;
                cur = cur.right;
            }
        }
        if (cand != null) splay(cand);
        return cand == null ? null : cand.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException("null key");
        Node cur = root, cand = null;
        while (cur != null) {
            if (key > cur.key) {
                cur = cur.right;
            } else {
                cand = cur;
                cur = cur.left;
            }
        }
        if (cand != null) splay(cand);
        return cand == null ? null : cand.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException("null key");
        Node cur = root, cand = null;
        while (cur != null) {
            if (key >= cur.key) {
                cur = cur.right;
            } else {
                cand = cur;
                cur = cur.left;
            }
        }
        if (cand != null) splay(cand);
        return cand == null ? null : cand.key;
    }


    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException("null key");
        MySplayMap m = new MySplayMap();
        inOrderCopy(root, m, Integer.MIN_VALUE, toKey, !inclusive, true);
        return m;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException("null key");
        MySplayMap m = new MySplayMap();
        inOrderCopy(root, m, fromKey, Integer.MAX_VALUE, false, inclusive);
        return m;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        MySplayMap m = new MySplayMap();
        inOrderCopy(root, m, fromKey, toKey, !toInclusive, fromInclusive);
        return m;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    private void inOrderCopy(Node n, MySplayMap dst, int from, int to, boolean toExclusive, boolean fromInclusive) {
        if (n == null) return;
        inOrderCopy(n.left, dst, from, to, toExclusive, fromInclusive);
        boolean geFrom = fromInclusive ? n.key >= from : n.key > from;
        boolean ltTo = toExclusive ? n.key < to : n.key <= to;
        if (geFrom && ltTo) dst.put(n.key, n.value);
        inOrderCopy(n.right, dst, from, to, toExclusive, fromInclusive);
    }

    // ======= toString (in-order ascending) =======

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = new boolean[]{true};
        inOrderToString(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inOrderToString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        inOrderToString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inOrderToString(n.right, sb, first);
    }

    // ======= Other required Map/NavigableMap stubs =======

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) return;
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
}
