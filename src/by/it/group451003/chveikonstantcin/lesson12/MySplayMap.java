package by.it.group451003.chveikonstantcin.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

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

    private Node root;
    private int size;

    public MySplayMap() {
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringInOrder(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void toStringInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            toStringInOrder(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringInOrder(node.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        Node node = findNode(key);
        if (node != null && node.key.equals(key)) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        }

        Node newNode = new Node(key, value);
        if (root == null) {
            root = newNode;
        } else if (key < node.key) {
            node.left = newNode;
            newNode.parent = node;
        } else {
            node.right = newNode;
            newNode.parent = node;
        }
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        Node node = findNode(intKey);
        if (node == null || !node.key.equals(intKey)) {
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
            Node maxLeft = findMax(node.left);
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;
        }

        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        Node node = findNode(intKey);
        if (node != null && node.key.equals(intKey)) {
            splay(node);
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
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) {
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
        Node node = findLower(key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findFloor(key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeiling(key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigher(key);
        return node != null ? node.key : null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap headMap = new MySplayMap();
        headMap(root, toKey, inclusive, headMap);
        return headMap;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap tailMap = new MySplayMap();
        tailMap(root, fromKey, inclusive, tailMap);
        return tailMap;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMin(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMax(root);
        splay(node);
        return node.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current;
            }
        }
        return parent;
    }

    private Node findLower(Integer key) {
        Node current = root;
        Node candidate = null;

        while (current != null) {
            if (current.key < key) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (candidate != null) {
            splay(candidate);
        }
        return candidate;
    }

    private Node findFloor(Integer key) {
        Node current = root;
        Node candidate = null;

        while (current != null) {
            if (current.key <= key) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (candidate != null) {
            splay(candidate);
        }
        return candidate;
    }

    private Node findCeiling(Integer key) {
        Node current = root;
        Node candidate = null;

        while (current != null) {
            if (current.key >= key) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (candidate != null) {
            splay(candidate);
        }
        return candidate;
    }

    private Node findHigher(Integer key) {
        Node current = root;
        Node candidate = null;

        while (current != null) {
            if (current.key > key) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (candidate != null) {
            splay(candidate);
        }
        return candidate;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap headMap) {
        if (node != null) {
            headMap(node.left, toKey, inclusive, headMap);
            if (inclusive ? node.key <= toKey : node.key < toKey) {
                headMap.put(node.key, node.value);
            }
            headMap(node.right, toKey, inclusive, headMap);
        }
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap tailMap) {
        if (node != null) {
            tailMap(node.left, fromKey, inclusive, tailMap);
            if (inclusive ? node.key >= fromKey : node.key > fromKey) {
                tailMap.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, inclusive, tailMap);
        }
    }

    private void splay(Node node) {
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}