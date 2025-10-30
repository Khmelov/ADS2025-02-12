package by.it.group410901.borisdubinin.lesson12;

import java.util.*;

public class MyAvlMap<K, V> implements Map<K, V> {
    private static class AvlNode<K, V> {
        K key;
        V value;
        AvlNode<K, V> left;
        AvlNode<K, V> right;
        int height;
        int size;

        AvlNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
            this.size = 1;
        }
    }

    private AvlNode<K, V> root;
    private int size;
    private final Comparator<? super K> comparator;

    public MyAvlMap() {
        this.comparator = (k1, k2) -> ((Comparable<? super K>) k1).compareTo(k2);
    }

    public MyAvlMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////// ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ////////////////////////
    ///////////////////////////////////////////////////////////////////

    private int height(AvlNode<K, V> node) {
        return node == null ? 0 : node.height;
    }

    private int size(AvlNode<K, V> node) {
        return node == null ? 0 : node.size;
    }

    private int balanceFactor(AvlNode<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeightAndSize(AvlNode<K, V> node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
            node.size = size(node.left) + size(node.right) + 1;
        }
    }

    private AvlNode<K, V> rotateRight(AvlNode<K, V> y) {
        AvlNode<K, V> x = y.left;
        AvlNode<K, V> T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeightAndSize(y);
        updateHeightAndSize(x);

        return x;
    }

    private AvlNode<K, V> rotateLeft(AvlNode<K, V> x) {
        AvlNode<K, V> y = x.right;
        AvlNode<K, V> T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeightAndSize(x);
        updateHeightAndSize(y);

        return y;
    }

    private AvlNode<K, V> balance(AvlNode<K, V> node) {
        if (node == null) return null;

        updateHeightAndSize(node);

        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AvlNode<K, V> put(AvlNode<K, V> node, K key, V value, V[] oldValue) {
        if (node == null) {
            return new AvlNode<>(key, value);
        }

        int cmp = comparator.compare(key, node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private AvlNode<K, V> remove(AvlNode<K, V> node, K key, V[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = comparator.compare(key, node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                AvlNode<K, V> minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = removeMin(node.right);
            }
        }

        return balance(node);
    }

    private AvlNode<K, V> findMin(AvlNode<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private AvlNode<K, V> removeMin(AvlNode<K, V> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    private AvlNode<K, V> get(AvlNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = comparator.compare(key, node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    private void inOrderTraversal(AvlNode<K, V> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
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
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        V[] oldValue = (V[]) new Object[1];
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        V[] removedValue = (V[]) new Object[1];
        root = remove(root, (K) key, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        AvlNode<K, V> node = get(root, (K) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
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


    /// /////////////////////////////////////////////////////////////
    ///  ////////////////////////////////////////////////////////////
    ///  ////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
