package by.it.group451004.rak.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size = 0;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    //================================================================================================================//

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        if (y != null) y.right = x;
        x.parent = y;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        if (y != null) y.left = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
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

    private Node findNode(Integer key) {
        Node x = root;
        Node last = null;
        while (x != null) {
            last = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                splay(x);
                return x;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    //================================================================================================================//

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }
        Node x = root;
        Node parent = null;
        int cmp = 0;
        while (x != null) {
            parent = x;
            cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                String old = x.value;
                x.value = value;
                splay(x);
                return old;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return n != null ? n.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value)))
            return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private boolean containsValueRecursive(Node n, String value) {
        if (n == null) return false;
        if (n.value.equals(value)) {
            splay(n);
            return true;
        }
        return containsValueRecursive(n.left, value) || containsValueRecursive(n.right, value);
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node node = findNode(key);
        if (node == null || !node.key.equals(key)) return null; // ключ не найден
        splay(node);
        String oldValue = node.value;

        if (node.left == null) {
            if (node.right != null) {
                node.right.parent = null;
            }
            root = node.right;
        } else {
            Node maxLeft = node.left;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) node.right.parent = maxLeft;
            root = maxLeft;
            maxLeft.parent = null;
        }

        size--;
        return oldValue;
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

    private Node minNode(Node n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maxNode(Node n) {
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public Integer firstKey() {
        Node n = minNode(root);
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = maxNode(root);
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    private Node lowerNode(Node keyNode, Integer key) {
        Node res = null;
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp > 0) {
                res = x;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    private Node floorNode(Node keyNode, Integer key) {
        Node res = null;
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            if (cmp > 0) {
                res = x;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    private Node ceilingNode(Node keyNode, Integer key) {
        Node res = null;
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            if (cmp < 0) {
                res = x;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    private Node higherNode(Node keyNode, Integer key) {
        Node res = null;
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                res = x;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = lowerNode(root, key);
        return n != null ? n.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = floorNode(root, key);
        return n != null ? n.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = ceilingNode(root, key);
        return n != null ? n.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(root, key);
        return n != null ? n.key : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove last ", "
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        fillHeadMap(root, map, toKey);
        return map;
    }

    private void fillHeadMap(Node n, MySplayMap map, Integer toKey) {
        if (n == null) return;
        if (n.key.compareTo(toKey) < 0) map.put(n.key, n.value);
        fillHeadMap(n.left, map, toKey);
        fillHeadMap(n.right, map, toKey);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        fillTailMap(root, map, fromKey);
        return map;
    }

    private void fillTailMap(Node n, MySplayMap map, Integer fromKey) {
        if (n == null) return;
        if (n.key.compareTo(fromKey) >= 0) map.put(n.key, n.value);
        fillTailMap(n.left, map, fromKey);
        fillTailMap(n.right, map, fromKey);
    }

    @Override public Map.Entry<Integer, String> lowerEntry(Integer key){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> floorEntry(Integer key){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> higherEntry(Integer key){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> firstEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> lastEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> pollFirstEntry(){throw new UnsupportedOperationException();}
    @Override public Map.Entry<Integer, String> pollLastEntry(){throw new UnsupportedOperationException();}
    @Override public java.util.Set<Integer> keySet(){throw new UnsupportedOperationException();}
    @Override public java.util.Collection<String> values(){throw new UnsupportedOperationException();}
    @Override public java.util.Set<Map.Entry<Integer, String>> entrySet(){throw new UnsupportedOperationException();}
    @Override public NavigableMap<Integer, String> descendingMap(){throw new UnsupportedOperationException();}
    @Override public NavigableSet<Integer> navigableKeySet(){throw new UnsupportedOperationException();}
    @Override public NavigableSet<Integer> descendingKeySet(){throw new UnsupportedOperationException();}
    @Override public Comparator<? super Integer> comparator(){throw new UnsupportedOperationException();}
    @Override public void putAll(Map<? extends Integer, ? extends String> m){throw new UnsupportedOperationException();}
}
