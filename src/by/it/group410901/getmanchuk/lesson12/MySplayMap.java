package by.it.group410901.getmanchuk.lesson12;

import java.util.*;

public class MySplayMap<K extends Comparable<K>, V> implements NavigableMap<K, V> {

    private class Node {
        K key;
        V value;
        Node left, right, parent;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    @Override
    public int size() {
        return size;
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
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        root = splay(root, (K) key);
        if (root == null || !root.key.equals(key)) return null;
        return root.value;
    }

    @Override
    public V put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            V oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node newNode = new Node(key, value);
        if (cmp < 0) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            if (newNode.left != null) newNode.left.parent = newNode;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            if (newNode.right != null) newNode.right.parent = newNode;
        }
        root.parent = newNode;
        root = newNode;
        size++;
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        if (root == null) return null;

        root = splay(root, (K) key);

        if (!root.key.equals(key)) return null;

        V oldValue = root.value;

        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else {
            Node newRoot = root.right;
            root = root.left;
            root.parent = null;
            root = splay(root, (K) key);
            root.right = newRoot;
            if (newRoot != null) newRoot.parent = root;
        }

        size--;
        return oldValue;
    }

    private Node splay(Node node, K key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            if (node.left == null) return node;

            int cmp2 = key.compareTo(node.left.key);

            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }

            if (node.left == null) return node;
            return rotateRight(node);

        } else if (cmp > 0) {
            if (node.right == null) return node;

            int cmp2 = key.compareTo(node.right.key);

            if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }

            if (node.right == null) return node;
            return rotateLeft(node);

        } else {
            return node;
        }
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        if (x.right != null) x.right.parent = node;
        x.right = node;
        x.parent = node.parent;
        node.parent = x;
        return x;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        if (x.left != null) x.left.parent = node;
        x.left = node;
        x.parent = node.parent;
        node.parent = x;
        return x;
    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public K firstKey() {
        if (root == null) return null;
        Node node = root;
        while (node.left != null) node = node.left;
        root = splay(root, node.key);
        return root.key;
    }

    @Override
    public K lastKey() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) node = node.right;
        root = splay(root, node.key);
        return root.key;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        MySplayMap<K, V> result = new MySplayMap<>();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, K toKey, MySplayMap<K, V> result) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            headMap(node.left, toKey, result);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        MySplayMap<K, V> result = new MySplayMap<>();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, K fromKey, MySplayMap<K, V> result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, result);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public K lowerKey(K key) {
        Node result = lowerNode(root, key, null);
        return result != null ? result.key : null;
    }

    private Node lowerNode(Node node, K key, Node best) {
        if (node == null) return best;

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return lowerNode(node.left, key, best);
        } else {
            return lowerNode(node.right, key, node);
        }
    }

    @Override
    public K floorKey(K key) {
        Node result = floorNode(root, key, null);
        return result != null ? result.key : null;
    }

    private Node floorNode(Node node, K key, Node best) {
        if (node == null) return best;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return floorNode(node.left, key, best);
        } else if (cmp > 0) {
            return floorNode(node.right, key, node);
        } else {
            return node;
        }
    }

    @Override
    public K ceilingKey(K key) {
        Node result = ceilingNode(root, key, null);
        return result != null ? result.key : null;
    }

    private Node ceilingNode(Node node, K key, Node best) {
        if (node == null) return best;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return ceilingNode(node.left, key, node);
        } else if (cmp > 0) {
            return ceilingNode(node.right, key, best);
        } else {
            return node;
        }
    }

    @Override
    public K higherKey(K key) {
        Node result = higherNode(root, key, null);
        return result != null ? result.key : null;
    }

    private Node higherNode(Node node, K key, Node best) {
        if (node == null) return best;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return higherNode(node.left, key, node);
        } else {
            return higherNode(node.right, key, best);
        }
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

}
