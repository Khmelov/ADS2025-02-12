package by.it.group451001.puzik.lesson12;

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

    private Node root = null;
    private int size = 0;

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append('}');
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append('=').append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
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

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node node = findNode((Integer) key);
        if (node != null) {
            splay(node);
            return node.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node node = findNode(key);
        if (node != null) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        }

        // Insert new node
        Node current = root;
        Node parent = null;
        while (current != null) {
            parent = current;
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node newNode = new Node(key, value);
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

    @Override
    public String remove(Object key) {
        Node node = findNode((Integer) key);
        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        splay(node);

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else {
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;
            Node max = findMax(root);
            splay(max);
            root.right = rightSubtree;
            if (rightSubtree != null) rightSubtree.parent = root;
        }

        size--;
        return oldValue;
    }

    // ---- Splay tree operations ----

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig case
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    // Zig-zig (left-left)
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    // Zig-zig (right-right)
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    // Zig-zag (left-right)
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    // Zig-zag (right-left)
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;

        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        if (x == null) return;

        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }

        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        x.right = y;
        y.parent = x;
    }

    private Node findNode(Integer key) {
        Node current = root;
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

    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ---- NavigableMap methods ----

    @Override
    public Integer lowerKey(Integer key) {
        Node node = findLowerNode(key, false);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findLowerNode(key, true);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findHigherNode(key, true);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigherNode(key, false);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node findLowerNode(Integer key, boolean inclusive) {
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    private Node findHigherNode(Integer key, boolean inclusive) {
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node min = findMin(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node max = findMax(root);
        splay(max);
        return max.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMapHelper(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMapHelper(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMapHelper(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMapHelper(node.right, fromKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap result = new MyRbMap();
        subMapHelper(root, fromKey, toKey, result);
        return result;
    }

    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node != null) {
            subMapHelper(node.left, fromKey, toKey, result);
            if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            subMapHelper(node.right, fromKey, toKey, result);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ---- boilerplate unsupported ----
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
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
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
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
    public NavigableMap<Integer, String> descendingMap() {
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
