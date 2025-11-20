package by.it.group451004.kozlov.lesson12;

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

    private Node root;
    private int size;

    public MySplayMap() {
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        String oldValue = get(key);
        root = put(root, key, value);
        root = splay(root, key);
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            if (node.right != null) node.right.parent = node;
        } else {
            node.value = value;
        }

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String oldValue = get(key);
        if (oldValue != null) {
            root = splay(root, (Integer) key);
            root = remove(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        node = splay(node, key);
        if (node.key.equals(key)) {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key);
        }
        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;

        root = splay(root, (Integer) key);
        return (root != null && root.key.equals(key)) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        return findMax(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        return (node != null) ? node.key : null;
    }

    private Node lowerNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerNode(node.left, key);
        } else {
            Node rightResult = lowerNode(node.right, key);
            return (rightResult != null) ? rightResult : node;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        return (node != null) ? node.key : null;
    }

    private Node floorNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return floorNode(node.left, key);
        } else {
            Node rightResult = floorNode(node.right, key);
            return (rightResult != null) ? rightResult : node;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        return (node != null) ? node.key : null;
    }

    private Node ceilingNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return ceilingNode(node.right, key);
        } else {
            Node leftResult = ceilingNode(node.left, key);
            return (leftResult != null) ? leftResult : node;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        return (node != null) ? node.key : null;
    }

    private Node higherNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return higherNode(node.right, key);
        } else {
            Node leftResult = higherNode(node.left, key);
            return (leftResult != null) ? leftResult : node;
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        headMap(node.left, toKey, inclusive, result);

        boolean shouldInclude = inclusive ? node.key.compareTo(toKey) <= 0 : node.key.compareTo(toKey) < 0;
        if (shouldInclude) {
            result.put(node.key, node.value);
        }

        if (node.key.compareTo(toKey) < 0 || (inclusive && node.key.compareTo(toKey) <= 0)) {
            headMap(node.right, toKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        tailMap(node.right, fromKey, inclusive, result);

        boolean shouldInclude = inclusive ? node.key.compareTo(fromKey) >= 0 : node.key.compareTo(fromKey) > 0;
        if (shouldInclude) {
            result.put(node.key, node.value);
        }

        if (node.key.compareTo(fromKey) > 0 || (inclusive && node.key.compareTo(fromKey) >= 0)) {
            tailMap(node.left, fromKey, inclusive, result);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
    }

    // Splay tree операции
    private Node splay(Node node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) return node;

            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return (node.left == null) ? node : rotateRight(node);
        } else {
            if (node.right == null) return node;

            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.right = h;
        x.parent = h.parent;
        h.parent = x;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.left = h;
        x.parent = h.parent;
        h.parent = x;
        return x;
    }

    private Node findMin(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node findMax(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Не реализованные методы NavigableMap
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }

    @Override
    public Comparator<? super Integer> comparator() { throw new UnsupportedOperationException(); }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}