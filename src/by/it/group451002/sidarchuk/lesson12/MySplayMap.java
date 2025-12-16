package by.it.group451002.sidarchuk.lesson12;

import java.util.NavigableMap;

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
        this.root = null;
        this.size = 0;
    }

    // Вспомогательные методы для splay-дерева

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        if (y != null) {
            y.left = x;
        }
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        if (y != null) {
            y.right = x;
        }
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) {
                    rotateRight(x.parent);
                } else {
                    rotateLeft(x.parent);
                }
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
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                splay(current);
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    private Node findMax(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Основные методы интерфейса

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = parent;

        if (parent == null) {
            root = newNode;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer integerKey = (Integer) key;
        Node node = findNode(integerKey);
        if (node == null) {
            return null;
        }

        String removedValue = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (node.right == null) {
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node maxLeft = findMax(node.left);
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;
            if (root != null) {
                root.parent = null;
            }
        }

        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer integerKey = (Integer) key;
        Node node = findNode(integerKey);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }

        Integer integerKey = (Integer) key;
        return findNode(integerKey) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        String stringValue = (String) value;
        return containsValueRecursive(root, stringValue);
    }

    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
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

    // Реализация headMap и tailMap

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, inclusive, result);
        return result;
    }

    private void headMapRecursive(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        headMapRecursive(node.left, toKey, inclusive, result);

        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }

        if (cmp < 0) {
            headMapRecursive(node.right, toKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, inclusive, result);
        return result;
    }

    private void tailMapRecursive(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        tailMapRecursive(node.right, fromKey, inclusive, result);

        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }

        if (cmp > 0 || (inclusive && cmp == 0)) {
            tailMapRecursive(node.left, fromKey, inclusive, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            return null;
        }
        Node minNode = findMin(root);
        splay(minNode);
        return minNode.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }
        Node maxNode = findMax(root);
        splay(maxNode);
        return maxNode.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node result = lowerKeyRecursive(root, key, null);
        if (result != null) {
            splay(result);
        }
        return result != null ? result.key : null;
    }

    private Node lowerKeyRecursive(Node node, Integer key, Node candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerKeyRecursive(node.left, key, candidate);
        } else {
            return lowerKeyRecursive(node.right, key, node);
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node result = floorKeyRecursive(root, key, null);
        if (result != null) {
            splay(result);
        }
        return result != null ? result.key : null;
    }

    private Node floorKeyRecursive(Node node, Integer key, Node candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return floorKeyRecursive(node.left, key, candidate);
        } else {
            return floorKeyRecursive(node.right, key, node);
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node result = ceilingKeyRecursive(root, key, null);
        if (result != null) {
            splay(result);
        }
        return result != null ? result.key : null;
    }

    private Node ceilingKeyRecursive(Node node, Integer key, Node candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return ceilingKeyRecursive(node.left, key, node);
        } else {
            return ceilingKeyRecursive(node.right, key, candidate);
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node result = higherKeyRecursive(root, key, null);
        if (result != null) {
            splay(result);
        }
        return result != null ? result.key : null;
    }

    private Node higherKeyRecursive(Node node, Integer key, Node candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return higherKeyRecursive(node.right, key, candidate);
        } else {
            return higherKeyRecursive(node.left, key, node);
        }
    }

    // Остальные методы интерфейса (не реализованы)

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null; // natural ordering
    }

    @Override
    public java.util.SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}