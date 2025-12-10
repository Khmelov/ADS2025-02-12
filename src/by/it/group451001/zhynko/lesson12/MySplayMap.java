package by.it.group451001.zhynko.lesson12;

import java.util.NavigableMap;

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
        if (root == null) {
            root = new SplayNode(key, value);
            size++;
            return null;
        }

        SplayNode current = root;
        SplayNode parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }

        SplayNode newNode = new SplayNode(key, value);
        newNode.parent = parent;

        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        splay(newNode);
        return null;
    }

    private void splay(SplayNode node) {
        while (node.parent != null) {
            if (node.parent.parent == null) {
                if (node == node.parent.left) {
                    rotateRight(node.parent);
                } else {
                    rotateLeft(node.parent);
                }
            } else if (node == node.parent.left && node.parent == node.parent.parent.left) {
                rotateRight(node.parent.parent);
                rotateRight(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.right) {
                rotateLeft(node.parent.parent);
                rotateLeft(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.left) {
                rotateLeft(node.parent);
                rotateRight(node.parent);
            } else {
                rotateRight(node.parent);
                rotateLeft(node.parent);
            }
        }
        root = node;
    }

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
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        if (x != null) {
            x.right = y;
        }
        y.parent = x;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        SplayNode node = findNode(intKey);
        if (node == null) {
            return null;
        }

        String removedValue = node.value;
        splay(node);

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
            SplayNode maxLeft = findMax(node.left);
            splay(maxLeft);

            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }

            root = maxLeft;
            root.parent = null;
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

        SplayNode node = findNode(intKey);
        if (node != null) {
            splay(node);
            return node.value;
        }
        return null;
    }

    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }

        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;
        }

        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

    private void headMapRecursive(SplayNode node, Integer toKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            headMapRecursive(node.left, toKey, result);
            result.put(node.key, node.value);
            headMapRecursive(node.right, toKey, result);
        } else {
            headMapRecursive(node.left, toKey, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    private void tailMapRecursive(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            tailMapRecursive(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMapRecursive(node.right, fromKey, result);
        } else {
            tailMapRecursive(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        return findMax(root).key;
    }

    private SplayNode findMin(SplayNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode findMax(SplayNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public Integer lowerKey(Integer key) {
        SplayNode result = lowerKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    private SplayNode lowerKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) < 0) {
            candidate = node;
            return lowerKeyRecursive(node.right, key, candidate);
        } else {
            return lowerKeyRecursive(node.left, key, candidate);
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        SplayNode result = floorKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    private SplayNode floorKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) <= 0) {
            candidate = node;
            return floorKeyRecursive(node.right, key, candidate);
        } else {
            return floorKeyRecursive(node.left, key, candidate);
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode result = ceilingKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    private SplayNode ceilingKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) >= 0) {
            candidate = node;
            return ceilingKeyRecursive(node.left, key, candidate);
        } else {
            return ceilingKeyRecursive(node.right, key, candidate);
        }
    }


    @Override
    public Integer higherKey(Integer key) {
        SplayNode result = higherKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    private SplayNode higherKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) > 0) {
            candidate = node;
            return higherKeyRecursive(node.left, key, candidate);
        } else {
            return higherKeyRecursive(node.right, key, candidate);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            if (node.left != null) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            sb.append(node.key).append("=").append(node.value);

            if (node.right != null) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("lowerEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("floorEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("ceilingEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("higherEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("firstEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("lastEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("pollFirstEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("pollLastEntry not implemented");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("descendingMap not implemented");
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("navigableKeySet not implemented");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("subMap with booleans not implemented");
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException("headMap with boolean not implemented");
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException("tailMap with boolean not implemented");
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("descendingKeySet not implemented");
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not implemented");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }
}