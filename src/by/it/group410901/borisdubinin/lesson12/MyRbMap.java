package by.it.group410901.borisdubinin.lesson12;

import java.util.*;

public class MyRbMap<K, V> implements SortedMap<K, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode<K, V> {
        K key;
        V value;
        RbNode<K, V> left;
        RbNode<K, V> right;
        RbNode<K, V> parent;
        boolean color;

        RbNode(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode<K, V> root;
    private int size;
    private final Comparator<? super K> comparator;

    @SuppressWarnings("unchecked")
    public MyRbMap() {
        this.comparator = (k1, k2) -> ((Comparable<? super K>) k1).compareTo(k2);
    }

    public MyRbMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ////////////////////////
    ///////////////////////////////////////////////////////////////////

    private boolean isRed(RbNode<K, V> node) {
        return node != null && node.color == RED;
    }

    private RbNode<K, V> rotateLeft(RbNode<K, V> h) {
        RbNode<K, V> x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.left = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private RbNode<K, V> rotateRight(RbNode<K, V> h) {
        RbNode<K, V> x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.right = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(RbNode<K, V> h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private RbNode<K, V> put(RbNode<K, V> h, K key, V value, V[] oldValue) {
        if (h == null) {
            return new RbNode<>(key, value, RED);
        }

        int cmp = comparator.compare(key, h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value, oldValue);
            if (h.left != null) h.left.parent = h;
        } else if (cmp > 0) {
            h.right = put(h.right, key, value, oldValue);
            if (h.right != null) h.right.parent = h;
        } else {
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        // Балансировка
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private RbNode<K, V> getNode(K key) {
        RbNode<K, V> x = root;
        while (x != null) {
            int cmp = comparator.compare(key, x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x;
        }
        return null;
    }

    private RbNode<K, V> findMin(RbNode<K, V> x) {
        while (x != null && x.left != null) {
            x = x.left;
        }
        return x;
    }

    private RbNode<K, V> findMax(RbNode<K, V> x) {
        while (x != null && x.right != null) {
            x = x.right;
        }
        return x;
    }

    private void inOrderTraversal(RbNode<K, V> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private boolean containsValue(RbNode<K, V> node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private RbNode<K, V> remove(RbNode<K, V> h, K key, V[] removedValue) {
        if (h == null) return null;

        int cmp = comparator.compare(key, h.key);
        if (cmp < 0) {
            h.left = remove(h.left, key, removedValue);
            if (h.left != null) h.left.parent = h;
        } else if (cmp > 0) {
            h.right = remove(h.right, key, removedValue);
            if (h.right != null) h.right.parent = h;
        } else {
            removedValue[0] = h.value;
            if (h.left == null) return h.right;
            if (h.right == null) return h.left;

            RbNode<K, V> t = h;
            h = findMin(t.right);
            h.right = removeMin(t.right);
            h.left = t.left;
            if (h.left != null) h.left.parent = h;
            if (h.right != null) h.right.parent = h;
        }
        return h;
    }

    private RbNode<K, V> removeMin(RbNode<K, V> h) {
        if (h.left == null) return h.right;
        h.left = removeMin(h.left);
        if (h.left != null) h.left.parent = h;
        return h;
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
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        V[] oldValue = (V[]) new Object[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK; // Корень всегда черный
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    @Override
    public V remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        V[] removedValue = (V[]) new Object[1];
        root = remove(root, (K) key, removedValue);
        if (root != null) root.color = BLACK;
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        RbNode<K, V> node = getNode((K) key);
        return node == null ? null : node.value;
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
        return findMin(root).key;
    }

    @Override
    public K lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return findMax(root).key;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        MyRbMap<K, V> result = new MyRbMap<>(comparator);
        buildHeadMap(root, toKey, result);
        return result;
    }

    private void buildHeadMap(RbNode<K, V> node, K toKey, MyRbMap<K, V> result) {
        if (node == null) return;

        int cmp = comparator.compare(node.key, toKey);
        if (cmp < 0) {
            result.put(node.key, node.value);
            buildHeadMap(node.right, toKey, result);
        }
        buildHeadMap(node.left, toKey, result);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        MyRbMap<K, V> result = new MyRbMap<>(comparator);
        buildTailMap(root, fromKey, result);
        return result;
    }

    private void buildTailMap(RbNode<K, V> node, K fromKey, MyRbMap<K, V> result) {
        if (node == null) return;

        int cmp = comparator.compare(node.key, fromKey);
        if (cmp >= 0) {
            result.put(node.key, node.value);
            buildTailMap(node.left, fromKey, result);
        }
        buildTailMap(node.right, fromKey, result);
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return null;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    }
}
