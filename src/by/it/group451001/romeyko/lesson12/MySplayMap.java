package by.it.group451001.romeyko.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;
        SplayNode parent;

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

    // ==================== ОСНОВНЫЕ ОПЕРАЦИИ SPLAY-ДЕРЕВА ====================

    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
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

    private void rotateRight(SplayNode y) {
        SplayNode x = y.left;
        if (x != null) {
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            x.parent = y.parent;
        }

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        if (x != null) {
            x.right = y;
        }
        y.parent = x;
    }

    private void splay(SplayNode x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                // Zig case
                if (x == x.parent.left) {
                    rotateRight(x.parent);
                } else {
                    rotateLeft(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                // Zig-zig case (left-left)
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                // Zig-zig case (right-right)
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                // Zig-zag case (left-right)
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                // Zig-zag case (right-left)
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        SplayNode last = null;

        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }

        return last;
    }

    // ==================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ NavigableMap ====================

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inorderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(SplayNode node, StringBuilder sb) {
        if (node != null) {
            inorderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inorderToString(node.right, sb);
        }
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

        SplayNode node = findNode(key);
        int cmp = key.compareTo(node.key);

        if (cmp == 0) {
            // Key already exists - update value
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        } else {
            // Insert new node
            SplayNode newNode = new SplayNode(key, value);
            newNode.parent = node;

            if (cmp < 0) {
                node.left = newNode;
            } else {
                node.right = newNode;
            }

            size++;
            splay(newNode);
            return null;
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        SplayNode node = findNode((Integer) key);
        if (node == null || !node.key.equals(key)) {
            return null;
        }

        String oldValue = node.value;

        splay(node);

        if (node.left == null) {
            // No left child
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (node.right == null) {
            // No right child
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            SplayNode leftTree = node.left;
            leftTree.parent = null;

            SplayNode maxLeft = leftTree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            splay(maxLeft);

            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }

            root = maxLeft;
        }

        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        SplayNode node = findNode((Integer) key);
        if (node != null && node.key.equals(key)) {
            splay(node);
            return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        SplayNode node = findNode((Integer) key);
        if (node != null && node.key.equals(key)) {
            splay(node);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            splay(node);
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        SplayNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }


        SplayNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        SplayNode result = lowerNode(root, key, null);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private SplayNode lowerNode(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerNode(node.left, key, candidate);
        } else {
            return lowerNode(node.right, key, node);
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        SplayNode result = floorNode(root, key, null);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private SplayNode floorNode(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return floorNode(node.left, key, candidate);
        } else if (cmp > 0) {
            return floorNode(node.right, key, node);
        } else {
            return node;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode result = ceilingNode(root, key, null);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private SplayNode ceilingNode(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return ceilingNode(node.right, key, candidate);
        } else if (cmp < 0) {
            return ceilingNode(node.left, key, node);
        } else {
            return node;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        SplayNode result = higherNode(root, key, null);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private SplayNode higherNode(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return higherNode(node.right, key, candidate);
        } else {
            return higherNode(node.left, key, node);
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        buildHeadMap(root, toKey, result);
        return result;
    }

    private void buildHeadMap(SplayNode node, Integer toKey, MySplayMap result) {
        if (node != null) {
            buildHeadMap(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            buildHeadMap(node.right, toKey, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        buildTailMap(root, fromKey, result);
        return result;
    }

    private void buildTailMap(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node != null) {
            buildTailMap(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            buildTailMap(node.right, fromKey, result);
        }
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


    // ==================== НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ====================

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
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
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
    public java.util.Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

}