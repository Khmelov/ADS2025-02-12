package by.it.group410902.gavlev.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root = null;
    private int size = 0;

    private int compare(Integer a, Integer b) {
        return a.compareTo(b);
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

    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.right == x.parent) {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            } else {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            }
        }
    }

    private Node subtreeMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node subtreeMax(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = compare(key, cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) {
                if (cur.left == null) break;
                cur = cur.left;
            } else {
                if (cur.right == null) break;
                cur = cur.right;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root;
        while (true) {
            int cmp = compare(key, cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) {
                if (cur.left == null) {
                    Node n = new Node(key, value);
                    cur.left = n;
                    n.parent = cur;
                    splay(n);
                    size++;
                    return null;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    Node n = new Node(key, value);
                    cur.right = n;
                    n.parent = cur;
                    splay(n);
                    size++;
                    return null;
                }
                cur = cur.right;
            }
        }
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
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        if (n == null) return null;
        String old = n.value;
        Node L = n.left;
        Node R = n.right;
        if (L != null) L.parent = null;
        if (R != null) R.parent = null;
        if (L == null) {
            root = R;
        } else {
            Node maxLeft = subtreeMax(L);
            splay(maxLeft);
            maxLeft.right = R;
            if (R != null) R.parent = maxLeft;
            root = maxLeft;
        }
        size--;
        return old;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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
    public Integer firstKey() {
        if (root == null) return null;
        Node min = subtreeMin(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node max = subtreeMax(root);
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key < key) {
                res = cur.key;
                cur = cur.right;
            } else cur = cur.left;
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key.equals(key)) return cur.key;
            if (cur.key < key) {
                res = cur.key;
                cur = cur.right;
            } else cur = cur.left;
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key.equals(key)) return cur.key;
            if (cur.key > key) {
                res = cur.key;
                cur = cur.left;
            } else cur = cur.right;
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (cur.key > key) {
                res = cur.key;
                cur = cur.left;
            } else cur = cur.right;
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        headRec(root, sub, toKey);
        return sub;
    }

    private void headRec(Node node, MySplayMap sub, Integer toKey) {
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
        MySplayMap sub = new MySplayMap();
        tailRec(root, sub, fromKey);
        return sub;
    }

    private void tailRec(Node node, MySplayMap sub, Integer fromKey) {
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
        StringBuilder sb = new StringBuilder("{");
        toStringRec(root, sb);
        sb.delete(sb.length() - 2, sb.length()); // удаляем последнюю запятую и пробел
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
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}