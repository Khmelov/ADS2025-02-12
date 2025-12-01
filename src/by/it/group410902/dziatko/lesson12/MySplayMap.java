package by.it.group410902.dziatko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size;

    private static final class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }



    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        this.inorderString(root, sb, true);
        sb.append('}');
        return sb.toString();
    }

    private void inorderString(Node n, StringBuilder sb, boolean firstCall) {
        if (n == null) return;
        this.inorderString(n.left, sb, firstCall);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        this.inorderString(n.right, sb, false);
    }




    private Node findNode(Integer key) {
        Node z = root;
        while (z != null) {
            int cmp = this.compare(key, z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else return z;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node z = root, parent = null;
        int cmp = 0;
        while (z != null) {
            parent = z;
            cmp = this.compare(key, z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else {
                String old = z.value;
                z.value = value;
                this.splay(z);
                return old;
            }
        }
        Node n = new Node(key, value, parent);
        if (cmp < 0) parent.left = n;
        else parent.right = n;
        this.splay(n);
        size++;
        return null;
    }

    @Override
    public String get(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node n = this.findNode(key);
        if (n != null) {
            this.splay(n);
            return n.value;
        }
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node n = this.findNode(key);
        if (n == null) return null;
        this.splay(n);
        String old = n.value;
        if (n.left == null) {
            replace(n, n.right);
        } else if (n.right == null) {
            replace(n, n.left);
        } else {
            Node min = this.subtreeMin(n.right);
            if (min.parent != n) {
                replace(min, min.right);
                min.right = n.right;
                min.right.parent = min;
            }
            this.replace(n, min);
            min.left = n.left;
            min.left.parent = min;
        }
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private void replace(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        return this.findNode((Integer) keyObj) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        if(!(valueObj instanceof String)) return false;
        String value = (String) valueObj;
        return this.containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, String value) {
        if (n == null) return false;
        if ((value == null && n.value == null) || (value != null && value.equals(n.value)))
            return true;
        return this.containsValueRec(n.left, value) || this.containsValueRec(n.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return this.subtreeMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return this.subtreeMax(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = this.compare(key, n.key);
            if (cmp <= 0) n = n.left;
            else {
                res = n;
                n = n.right;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = this.compare(key, n.key);
            if (cmp < 0) n = n.left;
            else {
                res = n;
                n = n.right;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = this.compare(key, n.key);
            if (cmp > 0) n = n.right;
            else {
                res = n;
                n = n.left;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = root, res = null;
        while (n != null) {
            int cmp = this.compare(key, n.key);
            if (cmp >= 0) n = n.right;
            else {
                res = n;
                n = n.left;
            }
        }
        return res == null ? null : res.key;
    }


    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return this.headMap(toKey,false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();

        class Walker {
            void walk(Node n) {
                if (n == null) return;
                this.walk(n.left);
                int cmp = compare(n.key, toKey);
                if (cmp < 0 || (inclusive && cmp == 0)) {
                    res.put(n.key, n.value);
                    this.walk(n.right);
                }
            }
        }
        new Walker().walk(root);

        return res;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }


    private void inorderHead(Node n, MySplayMap res, Integer toKey, boolean inclusive) {
        if (n == null) return;
        int cmp = compare(n.key, toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            inorderHead(n.left, res, toKey, inclusive);
            res.put(n.key, n.value);
            inorderHead(n.right, res, toKey, inclusive);
        } else {
            inorderHead(n.left, res, toKey, inclusive);
        }
    }


    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        this.inorderTail(root, res, fromKey, inclusive);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return this.tailMap(fromKey,true);
    }

    private void inorderTail(Node n, MySplayMap res, Integer fromKey, boolean inclusive) {
        if (n == null) return;
        inorderTail(n.left, res, fromKey, inclusive);
        int cmp = this.compare(n.key, fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            res.put(n.key, n.value);
        }
        inorderTail(n.right, res, fromKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        MySplayMap res = new MySplayMap();
        this.inorderSub(root, res, fromKey, fromInclusive, toKey, toInclusive);
        return res;
    }

    private void inorderSub(Node n, MySplayMap res,
                            Integer fromKey, boolean fromInclusive,
                            Integer toKey, boolean toInclusive) {
        if (n == null) return;
        this.inorderSub(n.left, res, fromKey, fromInclusive, toKey, toInclusive);
        int cmpLow = this.compare(n.key, fromKey);
        int cmpHigh = this.compare(n.key, toKey);
        boolean okLow = (cmpLow > 0) || (fromInclusive && cmpLow == 0);
        boolean okHigh = (cmpHigh < 0) || (toInclusive && cmpHigh == 0);
        if (okLow && okHigh) res.put(n.key, n.value);
        this.inorderSub(n.right, res, fromKey, fromInclusive, toKey, toInclusive);
    }

    private Node subtreeMin(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private Node subtreeMax(Node x) {
        while (x.right != null) x = x.right;
        return x;
    }

    private int compare(Integer a, Integer b) {
        return (a < b) ? -1 : (a > b ? 1 : 0);
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

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) this.rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                this.rotateRight(x.parent.parent);
                this.rotateRight(x.parent);
            } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                this.rotateLeft(x.parent.parent);
                this.rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.right == x.parent) {
                this.rotateRight(x.parent);
                this.rotateLeft(x.parent);
            } else {
                this.rotateLeft(x.parent);
                this.rotateRight(x.parent);
            }
        }
    }


    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

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
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
