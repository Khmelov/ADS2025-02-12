package by.it.group410901.danilova.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    //на основе Красно-чёрного дерева
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;//красный или чёрный

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap()
    {
        root = null;
        size = 0;
    }

    // Вспомогательные методы для красно-черного дерева
    private boolean isRed(Node node)
    {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node h)
    {
        Node x = h.right; //правый потомок - новый корень
        h.right = x.left; //левый потомок х теперь правый потомок h
        x.left = h; //h теперь левый потомок х
        x.color = h.color; //х наследует цвет
        h.color = RED; //h становится красным
        return x;
    }

    private Node rotateRight(Node h)
    {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) //смена цветов узла и его потомков
    {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private Node put(Node h, Integer key, String value, String[] oldValue)
    {       //вставка элемента в дерево
        if (h == null)
        {
            size++;
            return new Node(key, value, RED);//новые узлы всегда красные
        }

        // рекурсивный поиск места для вставки
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
        {
            h.left = put(h.left, key, value, oldValue);
        }
        else if (cmp > 0)
        {
            h.right = put(h.right, key, value, oldValue);
        }
        else
        {
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        // балансировка
        if (isRed(h.right) && !isRed(h.left))
        {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left))
        {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right))
        {
            flipColors(h);
        }

        return h;
    }

    // Основные методы Map
    @Override
    public String put(Integer key, String value)
    {
        String[] oldValue = new String[1]; //запоминает старое значение
        root = put(root, key, value, oldValue);
        if (root != null)
        {
            root.color = BLACK; //корень всегда чёрный
        }
        return oldValue[0];
    }

    @Override
    public String remove(Object key)
    {
        if (!(key instanceof Integer)) return null;//проверка ключа
        Integer k = (Integer) key;

        if (!containsKey(k)) return null;

        String oldValue = get(k);

        // упрощенное удаление
        root = removeSimple(root, k);
        size--;

        return oldValue;
    }

    private Node removeSimple(Node node, Integer key)
    {
        if (node == null) return null; //клч не найден

        int cmp = key.compareTo(node.key);
        if (cmp < 0)//поиск в левом поддереве
        {
            node.left = removeSimple(node.left, key);
        }
        else if (cmp > 0)//поиск в правом поддереве
        {
            node.right = removeSimple(node.right, key);
        }
        else//ключ найден
        {
            //удаление если 1 потомок
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            //удаление с 2 потомками
            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = removeSimple(node.right, minNode.key);
        }
        return node;
    }

    private Node findMin(Node node)
    {
        while (node.left != null)
        {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key)//возвращает значение по ключу
    {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node x = root;
        while (x != null)
        {
            int cmp = k.compareTo(x.key);
            if (cmp < 0)
            {
                x = x.left;
            }
            else if (cmp > 0)
            {
                x = x.right;
            }
            else
            {
                return x.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        String v = (String) value;
        return containsValue(root, v);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
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

    // Методы SortedMap
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;

        if (node.key.compareTo(toKey) < 0) {
            headMap(node.left, toKey, result);
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
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
        return null; // natural ordering
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
