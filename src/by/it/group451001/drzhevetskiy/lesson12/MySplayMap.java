package by.it.group451001.drzhevetskiy.lesson12;


import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        Node(int k, String v) { key = k; value = v; }
    }

    private Node root;
    private int size = 0;


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
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node gp = p.parent;

            if (gp == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == gp.left) {
                rotateRight(gp);
                rotateRight(p);
            } else if (x == p.right && p == gp.right) {
                rotateLeft(gp);
                rotateLeft(p);
            } else if (x == p.right && p == gp.left) {
                rotateLeft(p);
                rotateRight(gp);
            } else {
                rotateRight(p);
                rotateLeft(gp);
            }
        }
    }

    private Node findNode(int key) {
        Node n = root;
        Node last = root;

        while (n != null) {
            last = n;
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else {
                splay(n);
                return n;
            }
        }
        splay(last);
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return (n == null ? null : n.value);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;

        if (node.value != null && node.value.equals(value)) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }


    private boolean containsValue(Node n, String val) {
        if (n == null) return false;
        if ((val == null && n.value == null) ||
                (val != null && val.equals(n.value)))
            return true;
        return containsValue(n.left, val) || containsValue(n.right, val);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        Node n = root;
        Node parent = null;

        while (n != null) {
            parent = n;
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else {
                String old = n.value;
                n.value = value;
                splay(n);
                return old;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = parent;

        if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;

        splay(newNode);
        size++;
        return null;
    }


    @Override
    public String remove(Object keyObj) {
        int key = (Integer) keyObj;
        Node n = findNode(key);
        if (n == null) return null;

        String old = n.value;

        if (n.left == null) {
            transplant(n, n.right);
        }
        else if (n.right == null) {
            transplant(n, n.left);
        }
        else {
            Node m = n.right;
            while (m.left != null) m = m.left;

            if (m.parent != n) {
                transplant(m, m.right);
                m.right = n.right;
                m.right.parent = m;
            }

            transplant(n, m);
            m.left = n.left;
            m.left.parent = m;
        }

        size--;
        return old;
    }

    private void transplant(Node a, Node b) {
        if (a.parent == null) root = b;
        else if (a == a.parent.left) a.parent.left = b;
        else a.parent.right = b;
        if (b != null) b.parent = a.parent;
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node n = root;
        while (n.left != null) n = n.left;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node n = root;
        while (n.right != null) n = n.right;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return boundKey(key, -1);
    }

    @Override
    public Integer floorKey(Integer key) {
        return boundKey(key, 0);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return boundKey(key, 1);
    }

    @Override
    public Integer higherKey(Integer key) {
        return boundKey(key, 2);
    }

    private Integer boundKey(int key, int mode) {
        Node n = root;
        Node best = null;

        while (n != null) {
            if (key < n.key) {
                if (mode == 1 || mode == 2) best = n;
                n = n.left;
            } else if (key > n.key) {
                if (mode == -1 || mode == 0) best = n;
                n = n.right;
            } else {
                if (mode == -1) return (n.left != null ? maxNode(n.left).key : bestKey(best));
                if (mode == 0) return n.key;
                if (mode == 1) return n.key;
                if (mode == 2) return (n.right != null ? minNode(n.right).key : bestKey(best));
            }
        }

        return bestKey(best);
    }

    private Integer bestKey(Node n) {
        return (n == null ? null : n.key);
    }

    private Node minNode(Node n) { while (n.left != null) n = n.left; return n; }
    private Node maxNode(Node n) { while (n.right != null) n = n.right; return n; }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap m = new MySplayMap();
        addHead(root, m, toKey);
        return m;
    }

    private void addHead(Node n, MySplayMap m, int key) {
        if (n == null) return;
        if (n.key < key) {
            m.put(n.key, n.value);
            addHead(n.left, m, key);
            addHead(n.right, m, key);
        } else addHead(n.left, m, key);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        addTail(root, m, fromKey);
        return m;
    }

    private void addTail(Node n, MySplayMap m, int key) {
        if (n == null) return;
        if (n.key >= key) {
            m.put(n.key, n.value);
            addTail(n.left, m, key);
            addTail(n.right, m, key);
        } else addTail(n.right, m, key);
    }


    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }


    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer k){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> floorEntry(Integer k){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer k){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> higherEntry(Integer k){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> firstEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> lastEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> pollFirstEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> pollLastEntry(){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> descendingMap(){throw new UnsupportedOperationException();}

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override public NavigableMap<Integer, String> subMap(Integer a, boolean b,Integer c, boolean d){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> headMap(Integer k, boolean incl){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> tailMap(Integer k, boolean incl){throw new UnsupportedOperationException();}

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override public java.util.Set<Integer> keySet(){throw new UnsupportedOperationException();}
    @Override public java.util.Set<Entry<Integer,String>> entrySet(){throw new UnsupportedOperationException();}
    @Override public java.util.Collection<String> values(){throw new UnsupportedOperationException();}
    @Override public void putAll(Map<? extends Integer, ? extends String> m){throw new UnsupportedOperationException();}
}

