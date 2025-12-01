package by.it.group451004.struts.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private static class Node<Integer, String> {
        public Integer key;
        public String data;
        public Node<Integer, String> left = null;
        public Node<Integer, String> right = null;
        public boolean color = RED;

        public Node(Integer key, String data) {
            this.key = key;
            this.data = data;
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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        SortedMap<Integer, String> sortedMap = new MyRbMap();
        setToRecursively(root, toKey, sortedMap);

        return sortedMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        SortedMap<Integer, String> sortedMap = new MyRbMap();
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

        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null)
            return null;

        Node<Integer, String> node = root;
        while (node.right != null)
            node = node.right;

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
            root = putRecursively(root, key, value);
            size++;
            return null;
        } else {
            String oldValue = node.data;
            node.data = value;
            return oldValue;
        }
    }

    private Node<Integer, String> putRecursively(Node<Integer, String> node, int key, String value) {
        if (node == null)
            return new Node<>(key, value);

        if (key < node.key)
            node.left = putRecursively(node.left, key, value);
        else if (key > node.key)
            node.right = putRecursively(node.right, key, value);
        else
            node.data = value;

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            size--;
            root = removeRecursively(root, (int) key);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    private Node<Integer, String> searchByKey(Integer key) {
        Node<Integer, String> node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else
                return node;
        }
        return null;
    }

    private boolean searchByValue(Node<Integer, String> node, String value) {
        if (node == null)
            return false;
        if (value.equals(node.data))
            return true;
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

    private void setToRecursively(Node<Integer, String> node, int toKey, SortedMap<Integer, String> sortedMap) {
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

    private Node<Integer, String> rightRotate(Node<Integer, String> node)
    {
        Node<Integer, String> left = node.left;
        node.left = left.right;
        left.right = node;

        left.color = node.color;
        node.color = RED;
        return left;
    }

    private Node<Integer, String> leftRotate(Node<Integer, String> node)
    {
        Node<Integer, String> right = node.right;
        node.right = right.left;
        right.left = node;

        right.color = node.color;
        node.color = RED;
        return right;
    }

    private boolean isRedNode(Node<Integer, String> node) {
        return node != null && node.color == RED;
    }

    private void swapColors(Node<Integer, String> node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private Node<Integer, String> balance(Node<Integer, String> node) {
        if (isRedNode(node.right) && !isRedNode(node.left))
            node = leftRotate(node);
        if (isRedNode(node.left) && isRedNode(node.left.left))
            node = rightRotate(node);
        if (isRedNode(node.left) && isRedNode(node.right))
            swapColors(node);

        return node;
    }
    private Node<Integer, String> setRedAsLeft(Node<Integer, String> node) {
        swapColors(node);
        if (isRedNode(node.right.left)) {
            node.right = rightRotate(node.right);
            node = leftRotate(node);
            swapColors(node);
        }
        return node;
    }
    private Node<Integer, String> setRedAsRight(Node<Integer, String> node) {
        swapColors(node);
        if (isRedNode(node.left.left)) {
            node = rightRotate(node);
            swapColors(node);
        }
        return node;
    }
    
    private Node<Integer, String> removeRecursively(Node<Integer, String> node, Integer key) {
        if (key < node.key) {
            if (!isRedNode(node.left) && !isRedNode(node.left.left))
                node = setRedAsLeft(node);
            node.left = removeRecursively(node.left, key);
        }
        else {
            if (isRedNode(node.left))
                node = rightRotate(node);
            if (key.equals(node.key) && node.right == null)
                return null;
            if (!isRedNode(node.right) && !isRedNode(node.right.left))
                node = setRedAsRight(node);

            if (key.equals(node.key)) {
                Node<Integer, String> min = node.right;
                while (min.left != null)
                    min = min.left;

                node.key = min.key;
                node.data = min.data;
                node.right = removeMinNodeRecursively(node.right);
            }
            else
                node.right = removeRecursively(node.right, key);
        }
        return balance(node);
    }

    private Node<Integer, String> removeMinNodeRecursively(Node<Integer, String> node) {
        if (node.left == null)
            return node.right;

        if (!isRedNode(node.left) && !isRedNode(node.left.left))
            node = setRedAsLeft(node);

        node.left = removeMinNodeRecursively(node.left);
        return balance(node);
    }
}
