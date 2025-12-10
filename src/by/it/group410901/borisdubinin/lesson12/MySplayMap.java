package by.it.group410901.borisdubinin.lesson12;

import java.util.*;

public class MySplayMap<K, V> implements NavigableMap<K, V> {
    private static class SplayNode<K, V> {
        K key;
        V value;
        SplayNode<K, V> left;
        SplayNode<K, V> right;
        SplayNode<K, V> parent;

        SplayNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode<K, V> root;
    private int size;
    private final Comparator<? super K> comparator;

    @SuppressWarnings("unchecked")
    public MySplayMap() {
        this.comparator = (k1, k2) -> ((Comparable<? super K>) k1).compareTo(k2);
    }

    public MySplayMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ////////////////////////
    ///////////////////////////////////////////////////////////////////

    private void rotateLeft(SplayNode<K, V> x) {
        SplayNode<K, V> y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
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

    private void rotateRight(SplayNode<K, V> x) {
        SplayNode<K, V> y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
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

    private void splay(SplayNode<K, V> x) {
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

    private SplayNode<K, V> findNode(K key) {
        SplayNode<K, V> current = root;
        SplayNode<K, V> lastVisited = null;

        while (current != null) {
            lastVisited = current;
            int cmp = comparator.compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    private SplayNode<K, V> getNode(K key) {
        SplayNode<K, V> current = root;
        while (current != null) {
            int cmp = comparator.compare(key, current.key);
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

    private SplayNode<K, V> minNode(SplayNode<K, V> node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode<K, V> maxNode(SplayNode<K, V> node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void inOrderTraversal(SplayNode<K, V> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private boolean containsValue(SplayNode<K, V> node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private SplayNode<K, V> lowerNode(K key) {
        SplayNode<K, V> current = root;
        SplayNode<K, V> candidate = null;

        while (current != null) {
            int cmp = comparator.compare(key, current.key);
            if (cmp <= 0) {
                current = current.left;
            } else {
                candidate = current;
                current = current.right;
            }
        }
        return candidate;
    }

    private SplayNode<K, V> floorNode(K key) {
        SplayNode<K, V> current = root;
        SplayNode<K, V> candidate = null;

        while (current != null) {
            int cmp = comparator.compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                candidate = current;
                current = current.right;
            } else {
                return current;
            }
        }
        return candidate;
    }

    private SplayNode<K, V> ceilingNode(K key) {
        SplayNode<K, V> current = root;
        SplayNode<K, V> candidate = null;

        while (current != null) {
            int cmp = comparator.compare(key, current.key);
            if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return candidate;
    }

    private SplayNode<K, V> higherNode(K key) {
        SplayNode<K, V> current = root;
        SplayNode<K, V> candidate = null;

        while (current != null) {
            int cmp = comparator.compare(key, current.key);
            if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return candidate;
    }

    private void buildHeadMap(SplayNode<K, V> node, K toKey, MySplayMap<K, V> result) {
        if (node == null) return;

        int cmp = comparator.compare(node.key, toKey);
        if (cmp < 0) {
            result.put(node.key, node.value);
            buildHeadMap(node.right, toKey, result);
        }
        buildHeadMap(node.left, toKey, result);
    }

    private void buildTailMap(SplayNode<K, V> node, K fromKey, MySplayMap<K, V> result) {
        if (node == null) return;

        int cmp = comparator.compare(node.key, fromKey);
        if (cmp >= 0) {
            result.put(node.key, node.value);
            buildTailMap(node.left, fromKey, result);
        }
        buildTailMap(node.right, fromKey, result);
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ОСНОВНЫЕ МЕТОДЫ ///////////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (isEmpty()) {
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

    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        if (root == null) {
            root = new SplayNode<>(key, value);
            size++;
            return null;
        }

        SplayNode<K, V> current = root;
        SplayNode<K, V> parent = null;

        while (current != null) {
            parent = current;
            int cmp = comparator.compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                V oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }

        SplayNode<K, V> newNode = new SplayNode<>(key, value);
        newNode.parent = parent;

        int cmp = comparator.compare(key, parent.key);
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
    public V remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        SplayNode<K, V> node = getNode((K) key);
        if (node == null) return null;

        splay(node);

        V removedValue = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            SplayNode<K, V> newRoot = minNode(node.right);
            if (newRoot.parent != node) {
                if (newRoot.right != null) {
                    newRoot.right.parent = newRoot.parent;
                }
                newRoot.parent.left = newRoot.right;
                newRoot.right = node.right;
                newRoot.right.parent = newRoot;
            }
            newRoot.left = node.left;
            newRoot.left.parent = newRoot;
            root = newRoot;
            root.parent = null;
        }

        size--;
        return removedValue;
    }

    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        SplayNode<K, V> node = findNode((K) key);
        return node != null && comparator.compare((K) key, node.key) == 0 ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
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
    public K firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        SplayNode<K, V> min = minNode(root);
        splay(min);
        return min.key;
    }

    @Override
    public K lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        SplayNode<K, V> max = maxNode(root);
        splay(max);
        return max.key;
    }

    @Override
    public K lowerKey(K key) {
        SplayNode<K, V> node = lowerNode(key);
        if (node != null) splay(node);
        return node != null ? node.key : null;
    }

    @Override
    public K floorKey(K key) {
        SplayNode<K, V> node = floorNode(key);
        if (node != null) splay(node);
        return node != null ? node.key : null;
    }

    @Override
    public K ceilingKey(K key) {
        SplayNode<K, V> node = ceilingNode(key);
        if (node != null) splay(node);
        return node != null ? node.key : null;
    }

    @Override
    public K higherKey(K key) {
        SplayNode<K, V> node = higherNode(key);
        if (node != null) splay(node);
        return node != null ? node.key : null;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        MySplayMap<K, V> result = new MySplayMap<>(comparator);
        buildHeadMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        MySplayMap<K, V> result = new MySplayMap<>(comparator);
        buildTailMap(root, fromKey, result);
        return result;
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super K> comparator() { return null; }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) { return null; }

    @Override
    public Entry<K, V> lowerEntry(K key) { return null; }

    @Override
    public Entry<K, V> floorEntry(K key) { return null; }

    @Override
    public Entry<K, V> ceilingEntry(K key) { return null; }

    @Override
    public Entry<K, V> higherEntry(K key) { return null; }

    @Override
    public Entry<K, V> firstEntry() { return null; }

    @Override
    public Entry<K, V> lastEntry() { return null; }

    @Override
    public Entry<K, V> pollFirstEntry() { return null; }

    @Override
    public Entry<K, V> pollLastEntry() { return null; }

    @Override
    public NavigableMap<K, V> descendingMap() { return null; }

    @Override
    public NavigableSet<K> navigableKeySet() { return null; }

    @Override
    public NavigableSet<K> descendingKeySet() { return null; }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return null; }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return null; }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return null; }

    @Override
    public Set<K> keySet() { return null; }

    @Override
    public Collection<V> values() { return null; }

    @Override
    public Set<Entry<K, V>> entrySet() { return null; }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) { }
}