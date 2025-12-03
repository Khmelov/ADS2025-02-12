package by.it.group451003.sorokin.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {
    private Node root;
    private int size;

    // Узел splay-дерева
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public MySplayMap() {
        this.root = null;
        this.size = 0;
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
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);
        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            // Ключ уже существует
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node newNode = new Node(key, value);
        if (cmp < 0) {
            newNode.left = root.left;
            newNode.right = root;
            if (root.left != null) root.left.parent = newNode;
            root.left = null;
            root.parent = newNode;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            if (root.right != null) root.right.parent = newNode;
            root.right = null;
            root.parent = newNode;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        if (root == null) {
            return null;
        }

        root = splay(root, intKey);
        if (intKey.compareTo(root.key) != 0) {
            return null;
        }

        String removedValue = root.value;
        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else {
            Node newRoot = root.right;
            root = splay(root.left, intKey);
            root.right = newRoot;
            if (newRoot != null) newRoot.parent = root;
        }
        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        if (root == null) {
            return null;
        }

        root = splay(root, intKey);
        return intKey.compareTo(root.key) == 0 ? root.value : null;
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
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Splay операция
    private Node splay(Node node, Integer key) {
        if (node == null) return null;

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
            return node.left == null ? node : rotateRight(node);
        } else if (cmp > 0) {
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
            return node.right == null ? node : rotateLeft(node);
        } else {
            return node;
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

    // Вспомогательные методы для навигации
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

    // Методы NavigableMap
    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return findMax(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (isEmpty()) return null;

        Node result = lowerKey(root, key);
        return result != null ? result.key : null;
    }

    private Node lowerKey(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerKey(node.left, key);
        } else {
            Node rightResult = lowerKey(node.right, key);
            return rightResult != null ? rightResult : node;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (isEmpty()) return null;

        Node result = floorKey(root, key);
        return result != null ? result.key : null;
    }

    private Node floorKey(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return floorKey(node.left, key);
        } else {
            Node rightResult = floorKey(node.right, key);
            return rightResult != null ? rightResult : node;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (isEmpty()) return null;

        Node result = ceilingKey(root, key);
        return result != null ? result.key : null;
    }

    private Node ceilingKey(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return ceilingKey(node.right, key);
        } else {
            Node leftResult = ceilingKey(node.left, key);
            return leftResult != null ? leftResult : node;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (isEmpty()) return null;

        Node result = higherKey(root, key);
        return result != null ? result.key : null;
    }

    private Node higherKey(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return higherKey(node.right, key);
        } else {
            Node leftResult = higherKey(node.left, key);
            return leftResult != null ? leftResult : node;
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        headMap(node.left, toKey, inclusive, result);

        int cmp = toKey.compareTo(node.key);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
            headMap(node.right, toKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        tailMap(node.right, fromKey, inclusive, result);

        int cmp = fromKey.compareTo(node.key);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, inclusive, result);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrderToString(node.right, sb);
        }
    }

    // Методы интерфейса NavigableMap, которые не требуются по заданию

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("lowerEntry not implemented");
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("floorEntry not implemented");
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("ceilingEntry not implemented");
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("higherEntry not implemented");
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("firstEntry not implemented");
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("lastEntry not implemented");
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("pollFirstEntry not implemented");
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("pollLastEntry not implemented");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("descendingMap not implemented");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("navigableKeySet not implemented");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("descendingKeySet not implemented");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    // Обязательные методы Object
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;

        Map<?,?> m = (Map<?,?>) o;
        if (m.size() != size()) return false;

        try {
            return containsAllEntries(m);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    private boolean containsAllEntries(Map<?,?> m) {
        for (Node node : getAllNodes()) {
            Object value = m.get(node.key);
            if (value == null ? node.value != null : !value.equals(node.value)) {
                return false;
            }
        }
        return true;
    }

    private java.util.List<Node> getAllNodes() {
        java.util.List<Node> nodes = new java.util.ArrayList<>();
        inOrderCollect(root, nodes);
        return nodes;
    }

    private void inOrderCollect(Node node, java.util.List<Node> nodes) {
        if (node != null) {
            inOrderCollect(node.left, nodes);
            nodes.add(node);
            inOrderCollect(node.right, nodes);
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Node node : getAllNodes()) {
            h += node.key.hashCode() ^ (node.value == null ? 0 : node.value.hashCode());
        }
        return h;
    }
}
