package by.it.group451001.russu.lesson12;

import java.util.*;

public class MySplayMap<E> implements NavigableMap<Integer, String> {
    private Node root;
    private int size;

    private class Node {
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
        size = 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public String put(Integer key, String value) {
        String oldValue = get(key);
        if (oldValue == null) {
            size++;
        }
        root = insert(root, key, value);
        root = splay(root, key);
        return oldValue;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = insert(node.left, key, value);
            if (node.left != null) node.left.parent = node;
        } else if (key > node.key) {
            node.right = insert(node.right, key, value);
            if (node.right != null) node.right.parent = node;
        } else {
            node.value = value;
        }

        return node;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;
        String oldValue = get(intKey);
        if (oldValue != null) {
            root = remove(root, intKey);
            size--;
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        node = splay(node, key);

        if (node.key.equals(key)) {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node min = findMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = remove(node.right, min.key);
            }
        }

        return node;
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;
        root = splay(root, intKey);
        if (root != null && root.key.equals(intKey)) {
            return root.value;
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    public boolean containsValue(String value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private Node splay(Node node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        if (key < node.key) {
            if (node.left == null) return node;

            if (key < node.left.key) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key > node.left.key) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }

            return (node.left == null) ? node : rotateRight(node);
        } else {
            if (node.right == null) return node;

            if (key > node.right.key) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (key < node.right.key) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }

            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
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

    public Integer firstKey() {
        if (root == null) return null;
        Node min = findMin(root);
        return min.key;
    }

    public Integer lastKey() {
        if (root == null) return null;
        Node max = findMax(root);
        return max.key;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    public MySplayMap headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key < toKey) {
                result.put(node.key, node.value);
            }
            headMap(node.right, toKey, result);
        }
    }

    public MySplayMap tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node != null) {
            tailMap(node.left, fromKey, result);
            if (node.key >= fromKey) {
                result.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    public Integer lowerKey(Integer key) {
        Node node = lower(root, key);
        return node == null ? null : node.key;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    private Node lower(Node node, Integer key) {
        if (node == null) return null;

        if (node.key >= key) {
            return lower(node.left, key);
        } else {
            Node right = lower(node.right, key);
            return right != null ? right : node;
        }
    }

    public Integer floorKey(Integer key) {
        Node node = floor(root, key);
        return node == null ? null : node.key;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    private Node floor(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.equals(key)) {
            return node;
        } else if (node.key > key) {
            return floor(node.left, key);
        } else {
            Node right = floor(node.right, key);
            return right != null ? right : node;
        }
    }

    public Integer ceilingKey(Integer key) {
        Node node = ceiling(root, key);
        return node == null ? null : node.key;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    private Node ceiling(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.equals(key)) {
            return node;
        } else if (node.key < key) {
            return ceiling(node.right, key);
        } else {
            Node left = ceiling(node.left, key);
            return left != null ? left : node;
        }
    }

    public Integer higherKey(Integer key) {
        Node node = higher(root, key);
        return node == null ? null : node.key;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    private Node higher(Node node, Integer key) {
        if (node == null) return null;

        if (node.key <= key) {
            return higher(node.right, key);
        } else {
            Node left = higher(node.left, key);
            return left != null ? left : node;
        }
    }

    public String toString() {
        if (size == 0) return "{}";
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
}