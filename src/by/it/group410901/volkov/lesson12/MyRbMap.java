package by.it.group410901.volkov.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.SortedMap;
import java.util.Comparator;

/**
 * Реализация SortedMap на основе красно-черного дерева
 * Красно-черное дерево - это самобалансирующееся бинарное дерево поиска,
 * где каждый узел имеет цвет (красный или черный) и соблюдаются определенные правила
 */
public class MyRbMap implements SortedMap<Integer, String> {
    
    // Константы для цветов узлов красно-черного дерева
    private static final boolean RED = true;    // Красный цвет
    private static final boolean BLACK = false; // Черный цвет
    
    /**
     * Узел красно-черного дерева
     * Красно-черное дерево - самобалансирующееся БДП со следующими свойствами:
     * 1. Каждый узел красный или черный
     * 2. Корень всегда черный
     * 3. Красные узлы не могут иметь красных детей
     * 4. Все пути от корня к листьям содержат одинаковое количество черных узлов
     */
    private static class Node {
        Integer key;        // Ключ узла
        String value;       // Значение узла
        Node left;          // Левый потомок
        Node right;         // Правый потомок
        boolean color;     // Цвет узла (RED или BLACK)
        
        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
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
        return containsValueRecursive(root, value);
    }
    
    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }
    
    @Override
    public String get(Object key) {
        Node node = findNode(root, (Integer) key);
        return node != null ? node.value : null;
    }
    
    private Node findNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return findNode(node.left, key);
        if (cmp > 0) return findNode(node.right, key);
        return node;
    }
    
    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = putRecursive(root, key, value);
        root.color = BLACK;
        if (oldValue == null) size++;
        return oldValue;
    }
    
    /**
     * Рекурсивно вставляет или обновляет узел в красно-черном дереве
     * После вставки выполняет балансировку для поддержания свойств КЧ-дерева
     * @param node текущий узел
     * @param key ключ для вставки
     * @param value значение для вставки
     * @return корень поддерева после вставки и балансировки
     */
    private Node putRecursive(Node node, Integer key, String value) {
        if (node == null) {
            // Новые узлы всегда вставляются красными
            return new Node(key, value, RED);
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, key, value);
        } else {
            // Ключ уже существует, обновляем значение
            node.value = value;
            return node;
        }
        
        // Балансировка: исправляем нарушения свойств КЧ-дерева
        
        // Случай 1: правое поддерево красное, левое черное - поворот влево
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        // Случай 2: левое поддерево и его левый потомок красные - поворот вправо
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        // Случай 3: оба потомка красные - перекрашивание
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        
        return node;
    }
    
    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            root = removeRecursive(root, (Integer) key);
            if (root != null) root.color = BLACK;
            size--;
        }
        return oldValue;
    }
    
    private Node removeRecursive(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = removeRecursive(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = removeRecursive(node.right, key);
            }
        }
        return balance(node);
    }
    
    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }
    
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    private boolean isRed(Node node) {
        return node != null && node.color == RED;
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
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }
    
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }
    
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }
    
    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }
    
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        root = null;
        size = 0;
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
        return null;
    }
    
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapRecursive(root, toKey, result);
        return result;
    }
    
    private void headMapRecursive(Node node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMapRecursive(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMapRecursive(node.right, toKey, result);
        }
    }
    
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }
    
    private void tailMapRecursive(Node node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMapRecursive(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMapRecursive(node.right, fromKey, result);
        }
    }
    
    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }
    
    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRecursive(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void toStringRecursive(Node node, StringBuilder sb) {
        if (node != null) {
            toStringRecursive(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringRecursive(node.right, sb);
        }
    }
}
