package by.it.group410901.garkusha.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public String toString() {
        if (root == null) return "{}";
        List<String> pairs = new ArrayList<>();
        inOrderTraversal(root, pairs);
        return "{" + String.join(", ", pairs) + "}";
    }

    private void inOrderTraversal(Node node, List<String> pairs) {
        if (node != null) {
            inOrderTraversal(node.left, pairs);
            pairs.add(node.key + "=" + node.value);
            inOrderTraversal(node.right, pairs);
        }
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root = splay(root, key);
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
        }
        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        if (root == null) return null;

        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;

        String removedValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            Node newRoot = root.right;
            root = splay(root.left, (Integer) key);
            root.right = newRoot;
        }
        size--;
        return removedValue;
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
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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
        Node min = root;
        while (min.left != null) min = min.left;
        root = splay(root, min.key);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node max = root;
        while (max.right != null) max = max.right;
        root = splay(root, max.key);
        return max.key;
    }

    @Override
    // Поиск наибольшего ключа, который меньше заданного
    public Integer lowerKey(Integer key) {
        if (root == null) return null;
        root = splay(root, key);
        if (root.key < key) return root.key;

        Node node = root.left;
        if (node == null) return null;

        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    // Наибольший ключ ≤ заданного
    public Integer floorKey(Integer key) {
        if (root == null) return null;
        root = splay(root, key);
        if (root.key <= key) return root.key;

        Node node = root.left;
        if (node == null) return null;

        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    // Наименьший ключ ≥ заданного
    public Integer ceilingKey(Integer key) {
        if (root == null) return null;
        root = splay(root, key);
        if (root.key >= key) return root.key;

        Node node = root.right;
        if (node == null) return null;

        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    // Наименьшего ключа, который больше заданного
    public Integer higherKey(Integer key) {
        if (root == null) return null;
        root = splay(root, key);
        if (root.key > key) return root.key;

        Node node = root.right;
        if (node == null) return null;

        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key < toKey) {
                result.put(node.key, node.value);
                headMap(node.right, toKey, result);
            }
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node != null) {
            tailMap(node.right, fromKey, result);
            if (node.key >= fromKey) {
                result.put(node.key, node.value);
                tailMap(node.left, fromKey, result);
            }
        }
    }

    // Splay операция
    private Node splay(Node node, Integer key) {
        if (node == null) return null;

        if (key < node.key) {
            if (node.left == null) return node;

            if (key < node.left.key) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key > node.left.key) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null)
                    node.left = rotateLeft(node.left);
            }
            return (node.left == null) ? node : rotateRight(node);

        } else if (key > node.key) {
            if (node.right == null) return node;

            if (key < node.right.key) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null)
                    node.right = rotateRight(node.right);
            } else if (key > node.right.key) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return (node.right == null) ? node : rotateLeft(node);
        }
        return node;
    }

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

    // Неиспользуемые методы
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
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { }
}