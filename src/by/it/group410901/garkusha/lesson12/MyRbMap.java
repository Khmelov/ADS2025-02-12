package by.it.group410901.garkusha.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key; // Ключ (число) - по нему происходит сортировка
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public String toString() {
        if (root == null) return "{}";
        List<String> pairs = new ArrayList<>();
        inOrderTraversal(root, pairs);
        return "{" + String.join(", ", pairs) + "}";
    }

    private void inOrderTraversal(Node node, List<String> pairs) {
        if (node != null) {
            inOrderTraversal(node.left, pairs); // Список для пар ключ=значение
            pairs.add(node.key + "=" + node.value); // Обходом дерева
            inOrderTraversal(node.right, pairs);
        }
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue); // Рекурсивная вставка
        root.color = BLACK;
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }

        // Поиск места для вставки
        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        // Балансировка
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        if (root == null) return null;

        String[] removedValue = new String[1];
        if (!containsKey(key)) return null;

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = remove(root, (Integer) key, removedValue);
        if (root != null) root.color = BLACK;
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (key < node.key) {
            if (!isRed(node.left) && node.left != null && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(node.left, key, removedValue);
        } else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key.equals(node.key) && (node.right == null)) {
                removedValue[0] = node.value;
                size--;
                return null;
            }
            if (node.right != null && !isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (key.equals(node.key)) {
                Node minNode = findMin(node.right);
                removedValue[0] = node.value;
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = deleteMin(node.right);
                size--;
            } else {
                node.right = remove(node.right, key, removedValue);
            }
        }
        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (node.right != null && isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (node.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null; // Проверка типа
        Node node = get(root, (Integer) key);
        return node != null ? node.value : null;
    }

    private Node get(Node node, Integer key) {
        if (node == null) return null;
        if (key < node.key) return get(node.left, key);
        if (key > node.key) return get(node.right, key);
        return node;
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
        if (Objects.equals(value, node.value)) return true;
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
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return findMax(root).key;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    // Возвращает часть карты с ключами МЕНЬШЕ заданного
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key < toKey) {
                result.put(node.key, node.value);
                headMap(node.right, toKey, result);
            }
        }
    }

    @Override
    // Возвращает часть карты с ключами БОЛЬШЕ ИЛИ РАВНЫМИ заданного
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMap(node.right, fromKey, result);
            if (node.key >= fromKey) {
                result.put(node.key, node.value);
                tailMap(node.left, fromKey, result);
            }
        }
    }

    // Вспомогательные методы

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        y.color = x.color;
        x.color = RED;
        return y;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        x.color = y.color;
        y.color = RED;
        return x;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if (node.left != null) node.left.color = !node.left.color;
        if (node.right != null) node.right.color = !node.right.color;
    }

    // Неиспользуемые методы (заглушки)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }
}