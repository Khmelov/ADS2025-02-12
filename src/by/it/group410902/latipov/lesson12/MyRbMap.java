package by.it.group410902.latipov.lesson12;

import java.util.Comparator;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
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
    private int size;
    private final Comparator<? super Integer> comparator;

    public MyRbMap() {
        size = 0;
        comparator = null;
    }

    public MyRbMap(Comparator<? super Integer> comparator) {
        size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
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
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK;
        if (oldValue[0] == null) size++;
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    // Упрощенное удаление - перестраиваем дерево
    public String remove(Integer key) {
        if (key == null) throw new NullPointerException();

        String value = get(key);
        if (value == null) return null;

        // Перестраиваем дерево без удаляемого элемента
        root = rebuildTreeWithoutKey(root, key);
        size--;
        return value;
    }

    // Рекурсивно перестраиваем дерево без указанного ключа
    private Node rebuildTreeWithoutKey(Node node, Integer keyToRemove) {
        if (node == null) return null;

        if (node.key.equals(keyToRemove)) {
            // Пропускаем удаляемый узел
            Node newLeft = rebuildTreeWithoutKey(node.left, keyToRemove);
            Node newRight = rebuildTreeWithoutKey(node.right, keyToRemove);

            if (newLeft == null) return newRight;
            if (newRight == null) return newLeft;

            // Если оба поддерева не пустые, находим минимальный в правом поддереве
            Node minNode = findMin(newRight);
            Node newRoot = new Node(minNode.key, minNode.value, BLACK);
            newRoot.left = newLeft;
            newRoot.right = rebuildTreeWithoutKey(newRight, minNode.key);
            return balance(newRoot);
        }

        int cmp = compare(keyToRemove, node.key);
        if (cmp < 0) {
            node.left = rebuildTreeWithoutKey(node.left, keyToRemove);
        } else {
            node.right = rebuildTreeWithoutKey(node.right, keyToRemove);
        }
        return balance(node);
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        if (!(key instanceof Integer)) return null;

        Node node = root;
        while (node != null) {
            int cmp = compare((Integer) key, node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) throw new NullPointerException();
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
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
        if (root == null) throw new java.util.NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return max(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap(comparator);
        headMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap(comparator);
        tailMap(root, fromKey, result);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы КЧ-дерева            ///////
    /////////////////////////////////////////////////////////////////////////

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node put(Node h, Integer key, String value, String[] oldValue) {
        if (h == null) return new Node(key, value, RED);

        int cmp = compare(key, h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value, oldValue);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value, oldValue);
        } else {
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        // Балансировка для вставки
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private int compare(Integer a, Integer b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return a.compareTo(b);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node max(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private void headMap(Node node, Integer toKey, MyRbMap map) {
        if (node != null) {
            headMap(node.left, toKey, map);
            if (compare(node.key, toKey) < 0) {
                map.put(node.key, node.value);
                headMap(node.right, toKey, map);
            }
        }
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node != null) {
            tailMap(node.right, fromKey, map);
            if (compare(node.key, fromKey) >= 0) {
                map.put(node.key, node.value);
                tailMap(node.left, fromKey, map);
            }
        }
    }

    // Балансировка для перестроенного дерева
    private Node balance(Node h) {
        if (h == null) return null;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы - необязательные к реализации     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return comparator;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return null;
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public java.util.Collection<String> values() {
        return null;
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}