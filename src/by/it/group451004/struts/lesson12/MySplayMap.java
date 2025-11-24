package by.it.group451004.struts.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node<K, V> {
        public K key;
        public V data;
        public Node<K, V> left = null;
        public Node<K, V> right = null;
        public Node<K, V> parent;
        public Node(K key, V data, Node<K, V> parent) {
            this.key = key;
            this.data = data;
            this.parent = parent;
        }
    }
    private Node<Integer, String> root = null;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        stringifyNode(root, sb);
        if (sb.length() > 2)
            sb.replace(sb.length() - 2, sb.length(), "}");
        else
            sb.append("}");

        return sb.toString();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node<Integer, String> lower = null;

        Node<Integer, String> current = root;
        while (current != null) {
            if (key > current.key) {
                lower = current;
                current = current.right;
            } else
                current = current.left;
        }

        if (lower == null)
            return null;

        splay(lower);
        return lower.key;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (searchByKey(key) != null)
            return key;
        return lowerKey(key);
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (searchByKey(key) != null)
            return key;
        return higherKey(key);
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node<Integer, String> higher = null;
        Node<Integer, String> current = root;
        while (current != null) {
            if (key < current.key) {
                higher = current;
                current = current.left;
            } else
                current = current.right;
        }

        if (higher == null)
            return null;

        splay(higher);
        return higher.key;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        SortedMap<Integer, String> sortedMap = new MySplayMap();
        setToRecursively(root, toKey, sortedMap);

        return sortedMap;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        SortedMap<Integer, String> sortedMap = new MySplayMap();
        setToRecursively(root, toKey, sortedMap);

        return sortedMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null)
            throw new NullPointerException("The key cannot be null");

        SortedMap<Integer, String> sortedMap = new MySplayMap();
        setFromRecursively(root, fromKey, sortedMap);

        return sortedMap;
    }

    @Override
    public Integer firstKey() {
        if (root == null)
            return null;

        Node<Integer, String> node = root;
        while (node.left != null)
            node = node.left;

        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null)
            return null;

        Node<Integer, String> node = root;
        while (node.right != null)
            node = node.right;

        splay(node);
        return node.key;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
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
    public boolean containsKey(Object key) {
        return searchByKey((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) // Не очень понял, почему тесты пытаются передать сюда число (ключ)
            return false;               // Ибо пришлось потратить пару часиков, чтобы понять прикол этого

        return searchByValue(root, (String) value);
    }

    @Override
    public String get(Object key) {
        Node<Integer, String> result = searchByKey((Integer) key);
        return result == null ? null : result.data;
    }

    @Override
    public String put(Integer key, String value) {
        Node<Integer, String> node = searchByKey(key);
        if (node == null) {
            size++;
            if (root == null) {
                root = new Node<>(key, value, null);
                return value;
            }

            Node<Integer, String> currentNode = root;
            Node<Integer, String> parent = null;

            while (currentNode != null) {
                parent = currentNode;
                if (key < currentNode.key)
                    currentNode = currentNode.left;
                else if (key > currentNode.key)
                    currentNode = currentNode.right;
            }

            Node<Integer, String> newNode = new Node<>(key, value, parent);
            if (key < parent.key)
                parent.left = newNode;
            else
                parent.right = newNode;
            splay(newNode);
            return null;
        }
        else {
            String oldValue = node.data;
            node.data = value;
            return oldValue;
        }
    }

    @Override
    public String remove(Object key) {
        Node<Integer, String> node = searchByKey((Integer) key);
        if (node != null) {
            size--;

            if (node.left == null) {
                root = node.right;

                if (root != null)
                    root.parent = null;
            } else {
                Node<Integer, String> rightSubtree = node.right;
                root = node.left;
                root.parent = null;

                Node<Integer, String> max = root;
                while (max.right != null)
                    max = max.right;

                splay(max);

                root.right = rightSubtree;

                if (rightSubtree != null)
                    rightSubtree.parent = root;
            }
        }
        return node == null ? null : node.data;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    private void rotateToLeft(Node<Integer, String> node) {
        Node<Integer, String> right = node.right;
        node.right = right.left;

        if (right.left != null)
            right.left.parent = node;
        right.parent = node.parent;

        if (node.parent == null)
            root = right;
        else if (node == node.parent.left)
            node.parent.left = right;
        else
            node.parent.right = right;

        right.left = node;
        node.parent = right;
    }

    private void rotateToRight(Node<Integer, String> node) {
        Node<Integer, String> left = node.left;
        node.left = left.right;

        if (left.right != null)
            left.right.parent = node;
        left.parent = node.parent;

        if (node.parent == null)
            root = left;
        else if (node == node.parent.right)
            node.parent.right = left;
        else
            node.parent.left = left;

        left.right = node;
        node.parent = left;
    }

    private Node<Integer, String> searchByKey(Integer key) {
        Node<Integer, String> node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else {
                splay(node);
                return node;
            }
        }
        return null;
    }

    private boolean searchByValue(Node<Integer, String> node, String value) {
        if (node == null)
            return false;
        if (value.equals(node.data)) {
            splay(node);
            return true;
        }

        return searchByValue(node.left, value) || searchByValue(node.right, value);
    }

    private void setFromRecursively(Node<Integer, String> node, int fromKey, SortedMap<Integer, String> sortedMap) {
        if (node == null)
            return;

        setFromRecursively(node.right, fromKey, sortedMap);

        if (node.key >= fromKey) {
            sortedMap.put(node.key, node.data);
            setFromRecursively(node.left, fromKey, sortedMap);
        }
    }

    private void setToRecursively(Node<Integer, String> node, Integer toKey, SortedMap<Integer, String> sortedMap) {
        if (node == null)
            return;

        setToRecursively(node.left, toKey, sortedMap);

        if (node.key < toKey) {
            sortedMap.put(node.key, node.data);
            setToRecursively(node.right, toKey, sortedMap);
        }
    }

    private void stringifyNode(Node<Integer, String> node, StringBuilder sb) {
        if (node != null) {
            stringifyNode(node.left, sb);
            sb.append(node.key).append("=").append(node.data).append(", ");
            stringifyNode(node.right, sb);
        }
    }

    private void splay(Node<Integer, String> node) {
        while (node.parent != null) {
            Node<Integer, String> parent = node.parent;
            Node<Integer, String> parParent = parent.parent;

            if (node == parent.left) {
                if (parParent == null) {
                    rotateToRight(parent);
                } else {
                    if (parent == parParent.left) {
                        rotateToRight(parParent);
                        rotateToRight(parent);
                    } else {
                        rotateToRight(parent);
                        rotateToLeft(parParent);
                    }
                }
            } else {
                if (parParent == null) {
                    rotateToLeft(parent);
                } else {
                    if (parent == parParent.right) {
                        rotateToLeft(parParent);
                        rotateToLeft(parent);
                    } else {
                        rotateToLeft(parent);
                        rotateToRight(parParent);
                    }
                }
            }
        }
    }
}
