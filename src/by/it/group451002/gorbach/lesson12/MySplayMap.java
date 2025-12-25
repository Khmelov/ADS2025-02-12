package by.it.group451002.gorbach.lesson12;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.NavigableMap;
import java.util.Comparator;
import java.util.NavigableSet;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;

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

        Node newNode = new Node(key, value, parent);
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) {
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
        if (!(key instanceof Integer)) {
            return null;
        }

        String value = get(key);
        if (value == null) {
            return null;
        }

        Node node = root;
        while (node != null) {
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                break;
            }
        }

        if (node != null) {
            splay(node);
            remove(node);
            size--;
            return value;
        }

        return null;
    }

    private void remove(Node node) {
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
            Node min = node.right;
            while (min.left != null) {
                min = min.left;
            }

            if (min.parent != node) {
                min.parent.left = min.right;
                if (min.right != null) {
                    min.right.parent = min.parent;
                }
                min.right = node.right;
                min.right.parent = min;
            }

            min.left = node.left;
            min.left.parent = min;
            root = min;
            root.parent = null;
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = root;
        Node lastVisited = null;

        while (node != null) {
            lastVisited = node;
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                splay(node);
                return node.value;
            }
        }

        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
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
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(key);
        return node != null ? node.key : null;
    }

    private Node lowerNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (current.key.compareTo(key) < 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(key);
        return node != null ? node.key : null;
    }

    private Node floorNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp <= 0) {
                result = current;
                if (cmp == 0) break;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(key);
        return node != null ? node.key : null;
    }

    private Node ceilingNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp >= 0) {
                result = current;
                if (cmp == 0) break;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(key);
        return node != null ? node.key : null;
    }

    private Node higherNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (current.key.compareTo(key) > 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        splay(node);
        return node.key;
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
        if (node == null) {
            return;
        }
        headMap(node.left, toKey, inclusive, result);
        if (inclusive ? node.key.compareTo(toKey) <= 0 : node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        headMap(node.right, toKey, inclusive, result);
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
        if (node == null) {
            return;
        }
        tailMap(node.left, fromKey, inclusive, result);
        if (inclusive ? node.key.compareTo(fromKey) >= 0 : node.key.compareTo(fromKey) > 0) {
            result.put(node.key, node.value);
        }
        tailMap(node.right, fromKey, inclusive, result);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
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

    // Исправленный метод comparator()
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    // Исправленные методы с правильными возвращаемыми типами
    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    // Не реализованные методы интерфейса NavigableMap
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}