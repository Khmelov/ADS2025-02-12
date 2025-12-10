package by.it.group451002.popeko.lesson12;

import java.util.*;

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

    // Основные методы Map
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node node = findNode(key);
        int cmp = key.compareTo(node.key);

        if (cmp == 0) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        } else {
            Node newNode = new Node(key, value, node);
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
        if (root == null) return null;

        Node node = findNode((Integer) key);
        if (!key.equals(node.key)) {
            splay(node);
            return null;
        }

        String removedValue = node.value;
        removeNode(node);
        return removedValue;
    }

    private void removeNode(Node node) {
        if (node.left == null) {
            transplant(node, node.right);
        } else if (node.right == null) {
            transplant(node, node.left);
        } else {
            Node min = minNode(node.right);
            if (min.parent != node) {
                transplant(min, min.right);
                min.right = node.right;
                min.right.parent = min;
            }
            transplant(node, min);
            min.left = node.left;
            min.left.parent = min;
        }
        size--;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    @Override
    public String get(Object key) {
        if (root == null) return null;

        Node node = findNode((Integer) key);
        splay(node);
        return key.equals(node.key) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String toString() {
        List<String> entries = new ArrayList<>();
        inOrderTraversal(root, entries);
        return "{" + String.join(", ", entries) + "}";
    }

    private void inOrderTraversal(Node node, List<String> entries) {
        if (node != null) {
            inOrderTraversal(node.left, entries);
            entries.add(node.key + "=" + node.value);
            inOrderTraversal(node.right, entries);
        }
    }

    // Методы NavigableMap
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node min = minNode(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node max = maxNode(root);
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (root == null) return null;
        Node node = findNode(key);
        splay(node);

        if (node.key.compareTo(key) < 0) {
            return node.key;
        }
        if (node.left != null) {
            Node pred = maxNode(node.left);
            splay(pred);
            return pred.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (root == null) return null;
        Node node = findNode(key);
        splay(node);

        if (node.key.compareTo(key) <= 0) {
            return node.key;
        }
        if (node.left != null) {
            Node pred = maxNode(node.left);
            splay(pred);
            return pred.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null) return null;
        Node node = findNode(key);
        splay(node);

        if (node.key.compareTo(key) >= 0) {
            return node.key;
        }
        if (node.right != null) {
            Node succ = minNode(node.right);
            splay(succ);
            return succ.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (root == null) return null;
        Node node = findNode(key);
        splay(node);

        if (node.key.compareTo(key) > 0) {
            return node.key;
        }
        if (node.right != null) {
            Node succ = minNode(node.right);
            splay(succ);
            return succ.key;
        }
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        addToHeadMap(root, toKey, inclusive, result);
        return result;
    }

    private void addToHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;
        addToHeadMap(node.left, toKey, inclusive, result);
        if (inclusive ? node.key.compareTo(toKey) <= 0 : node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        addToHeadMap(node.right, toKey, inclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        addToTailMap(root, fromKey, inclusive, result);
        return result;
    }

    private void addToTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;
        addToTailMap(node.left, fromKey, inclusive, result);
        if (inclusive ? node.key.compareTo(fromKey) >= 0 : node.key.compareTo(fromKey) > 0) {
            result.put(node.key, node.value);
        }
        addToTailMap(node.right, fromKey, inclusive, result);
    }

    // Простые методы
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

    // Вспомогательные методы для splay-дерева
    private Node findNode(Integer key) {
        Node node = root;
        Node last = root;
        while (node != null) {
            last = node;
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return last;
    }

    private void splay(Node node) {
        while (node != null && node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {
                // Zig-zig right
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {
                // Zig-zig left
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {
                // Zig-zag
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                // Zig-zag
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
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

    private Node minNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node maxNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Не реализованные методы
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public Comparator<? super Integer> comparator() {
        return null;
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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
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
}