package by.it.group451004.zarivniak.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;

        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if (root == null) {
            root = new SplayNode(key, value);
            size++;
            return null;
        }

        root = splay(root, key);
        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        } else if (cmp < 0) {
            SplayNode newNode = new SplayNode(key, value);
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            root = newNode;
            size++;
            return null;
        } else {
            SplayNode newNode = new SplayNode(key, value);
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            root = newNode;
            size++;
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException("Map cannot be null");
        }
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        if (root == null) {
            return null;
        }

        root = splay(root, (Integer) key);

        if (root.key.equals(key)) {
            String removedValue = root.value;

            if (root.left == null) {
                root = root.right;
            } else {
                SplayNode newRoot = root.left;
                newRoot = splay(newRoot, (Integer) key);
                newRoot.right = root.right;
                root = newRoot;
            }
            size--;
            return removedValue;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        if (root == null) {
            return null;
        }

        root = splay(root, (Integer) key);
        return root.key.equals(key) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        if (root == null) {
            return false;
        }

        root = splay(root, (Integer) key);
        return root.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
        if (root == null) {
            return null;
        }

        SplayNode min = root;
        while (min.left != null) {
            min = min.left;
        }
        root = splay(root, min.key);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }

        SplayNode max = root;
        while (max.right != null) {
            max = max.right;
        }
        root = splay(root, max.key);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) < 0) {
            return root.key;
        }

        if (root.left != null) {
            SplayNode node = root.left;
            while (node.right != null) {
                node = node.right;
            }
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) <= 0) {
            return root.key;
        }

        if (root.left != null) {
            SplayNode node = root.left;
            while (node.right != null) {
                node = node.right;
            }
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) >= 0) {
            return root.key;
        }

        if (root.right != null) {
            SplayNode node = root.right;
            while (node.left != null) {
                node = node.left;
            }
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) > 0) {
            return root.key;
        }

        if (root.right != null) {
            SplayNode node = root.right;
            while (node.left != null) {
                node = node.left;
            }
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private SplayNode splay(SplayNode node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                return node;
            }

            if (key.compareTo(node.left.key) < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key.compareTo(node.left.key) > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return (node.left == null) ? node : rotateRight(node);
        } else {
            if (node.right == null) {
                return node;
            }

            if (key.compareTo(node.right.key) < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (key.compareTo(node.right.key) > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    private SplayNode rotateRight(SplayNode h) {
        SplayNode x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private SplayNode rotateLeft(SplayNode h) {
        SplayNode x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    private boolean containsValue(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }

        if (Objects.equals(value, node.value)) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private void headMap(SplayNode node, Integer toKey, MySplayMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMap(node.right, toKey, result);
        }
    }

    private void tailMap(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node != null) {
            tailMap(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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

    // Методы NavigableMap (не реализованы)
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
}