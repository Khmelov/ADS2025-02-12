package by.it.group451003.burshtyn.lesson12;
import java.util.NavigableMap;
import java.util.NoSuchElementException;

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
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        String[] oldValue = new String[1];
        root = putRecursive(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
        root = splay(findNode(root, key));
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;

        Node node = findNode(root, intKey);
        if (node == null) return null;

        String removedValue = node.value;
        root = removeRecursive(root, intKey);
        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;

        Node node = findNode(root, intKey);
        if (node != null) {
            root = splay(node);
            return node.value;
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
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        Node min = findMin(root);
        root = splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        Node max = findMax(root);
        root = splay(max);
        return max.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = findLower(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findFloor(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeiling(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigher(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private Node splay(Node node) {
        if (node == null) return null;

        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {

                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {

                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {

                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {

                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
        return node;
    }

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

    private void rotateRight(Node y) {
        Node x = y.left;
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

    private Node putRecursive(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, key, value, oldValue);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, key, value, oldValue);
            if (node.right != null) node.right.parent = node;
        } else {
            oldValue[0] = node.value;
            node.value = value;
        }
        return node;
    }

    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key);
            if (node.right != null) node.right.parent = node;
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node temp = node;
            node = findMin(temp.right);
            node.right = deleteMin(temp.right);
            node.left = temp.left;
            if (node.left != null) node.left.parent = node;
            if (node.right != null) node.right.parent = node;
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        if (node.left != null) node.left.parent = node;
        return node;
    }

    private Node findNode(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
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

    private Node findLower(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp <= 0) {
                node = node.left;
            } else {
                result = node;
                node = node.right;
            }
        }
        return result;
    }

    private Node findFloor(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                result = node;
                node = node.right;
            }
        }
        return result;
    }

    private Node findCeiling(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                result = node;
                node = node.left;
            }
        }
        return result;
    }

    private Node findHigher(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp >= 0) {
                node = node.right;
            } else {
                result = node;
                node = node.left;
            }
        }
        return result;
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void headMapRecursive(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        headMapRecursive(node.left, toKey, inclusive, result);
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }
        if (cmp < 0) {
            headMapRecursive(node.right, toKey, inclusive, result);
        }
    }

    private void tailMapRecursive(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        tailMapRecursive(node.right, fromKey, inclusive, result);
        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            result.put(node.key, node.value);
        }
        if (cmp > 0) {
            tailMapRecursive(node.left, fromKey, inclusive, result);
        }
    }
}