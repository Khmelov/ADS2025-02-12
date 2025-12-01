package by.it.group410902.shahov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла Splay-дерева
    private static class Node {
        public int key;           // Ключ узла
        public String data;       // Данные узла
        public Node left = null;  // Левый потомок
        public Node right = null; // Правый потомок
        public Node parent;       // Родительский узел

        public Node(int key, String data, Node parent) {
            this.key = key;
            this.data = data;
            this.parent = parent;
        }
    }

    private Node root = null; // Корень дерева
    private int size = 0;     // Количество элементов в дереве

    // Правый поворот узла (для балансировки)
    private void rightRotate(Node node) {
        Node left = node.left;
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

    // Левый поворот узла (для балансировки)
    private void leftRotate(Node node) {
        Node right = node.right;
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

    // Splay операция - перемещает узел в корень дерева
    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (node == parent.left) {
                // Zig случаи
                if (grandparent == null) {
                    rightRotate(parent);
                } else {
                    if (parent == grandparent.left) {
                        // Zig-zig
                        rightRotate(grandparent);
                        rightRotate(parent);
                    } else {
                        // Zig-zag
                        rightRotate(parent);
                        leftRotate(grandparent);
                    }
                }
            } else {
                // Zag случаи
                if (grandparent == null) {
                    leftRotate(parent);
                } else {
                    if (parent == grandparent.right) {
                        // Zag-zag
                        leftRotate(grandparent);
                        leftRotate(parent);
                    } else {
                        // Zag-zig
                        leftRotate(parent);
                        rightRotate(grandparent);
                    }
                }
            }
        }
    }

    // Поиск узла по ключу с последующим splay
    private Node search(int key) {
        Node node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else {
                splay(node); // Найденный узел перемещается в корень
                return node;
            }
        }
        return null;
    }

    // Проверка типа ключа
    private boolean isInvalidKeyType(Object o) {
        return !(o instanceof Integer);
    }

    // Проверка типа значения
    private boolean isInvalidValueType(Object o) {
        return !(o instanceof String);
    }

    // Строковое представление дерева в формате {key1=value1, key2=value2}
    @Override
    public String toString() {
        if (root == null)
            return "{}";

        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.replace(sb.length() - 2, sb.length(), "}");
        return sb.toString();
    }

    // Рекурсивный обход дерева для построения строки
    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            sb.append(node.key).append("=").append(node.data).append(", ");
            toString(node.right, sb);
        }
    }

    // Находит наибольший ключ, меньший заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node lower = findLower(root, key);
        if (lower == null)
            return null;
        splay(lower); // Перемещаем найденный узел в корень
        return lower.key;
    }

    // Вспомогательный метод для поиска lowerKey
    private Node findLower(Node node, int key) {
        Node lower = null;
        while (node != null) {
            if (key > node.key) {
                lower = node; // Потенциальный кандидат
                node = node.right; // Ищем большее значение
            } else
                node = node.left; // Ищем меньшее значение
        }
        return lower;
    }

    // Находит наибольший ключ, меньший или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        if (search(key) != null) // Если ключ существует
            return key;
        return lowerKey(key); // Иначе ищем меньший
    }

    // Находит наименьший ключ, больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        if (search(key) != null) // Если ключ существует
            return key;
        return higherKey(key); // Иначе ищем больший
    }

    // Находит наименьший ключ, больший заданного
    @Override
    public Integer higherKey(Integer key) {
        Node higher = findHigher(root, key);
        if (higher == null)
            return null;
        splay(higher); // Перемещаем найденный узел в корень
        return higher.key;
    }

    // Вспомогательный метод для поиска higherKey
    private Node findHigher(Node node, int key) {
        Node higher = null;
        while (node != null) {
            if (key < node.key) {
                higher = node; // Потенциальный кандидат
                node = node.left; // Ищем меньшее значение
            } else
                node = node.right; // Ищем большее значение
        }
        return higher;
    }

    // Возвращает подкарту с ключами меньше toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null)
            throw new NullPointerException("The key cannot be null");

        SortedMap<Integer, String> sortedMap = new MySplayMap();
        setToKey(root, toKey, sortedMap);
        return sortedMap;
    }

    // Рекурсивное заполнение headMap
    private void setToKey(Node node, int toKey, SortedMap<Integer, String> sortedMap) {
        if (node == null)
            return;

        setToKey(node.left, toKey, sortedMap);

        if (node.key < toKey) {
            sortedMap.put(node.key, node.data);
            setToKey(node.right, toKey, sortedMap);
        }
    }

    // Возвращает подкарту с ключами больше или равными fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null)
            throw new NullPointerException("The key cannot be null");

        SortedMap<Integer, String> sortedMap = new MySplayMap();
        setFromKey(root, fromKey, sortedMap);
        return sortedMap;
    }

    // Рекурсивное заполнение tailMap
    private void setFromKey(Node node, int fromKey, SortedMap<Integer, String> sortedMap) {
        if (node == null)
            return;

        setFromKey(node.right, fromKey, sortedMap);

        if (node.key >= fromKey) {
            sortedMap.put(node.key, node.data);
            setFromKey(node.left, fromKey, sortedMap);
        }
    }

    // Возвращает наименьший ключ в дереве
    @Override
    public Integer firstKey() {
        if (root == null)
            return null;

        Node node = root;
        while (node.left != null)
            node = node.left;
        splay(node); // Перемещаем минимальный узел в корень
        return node.key;
    }

    // Возвращает наибольший ключ в дереве
    @Override
    public Integer lastKey() {
        if (root == null)
            return null;

        Node node = root;
        while (node.right != null)
            node = node.right;
        splay(node); // Перемещаем максимальный узел в корень
        return node.key;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверяет наличие ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        if (isInvalidKeyType(key)) {
            return false;
        }
        return search((int)key) != null;
    }

    // Проверяет наличие значения в дереве
    @Override
    public boolean containsValue(Object value) {
        if (isInvalidValueType(value)) {
            return false;
        }
        return containsValue(root, (String)value);
    }

    // Рекурсивный поиск значения
    private boolean containsValue(Node node, String value) {
        if (node == null)
            return false;
        if (value.equals(node.data)) {
            splay(node); // При нахождении перемещаем узел в корень
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Получает значение по ключу
    @Override
    public String get(Object key) {
        if (isInvalidKeyType(key))
            throw new ClassCastException("The key is not of type Integer");
        Node result = search((int)key);
        return result == null ? null : result.data;
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String value) {
        var node = search(key);
        if (node == null) {
            size++;
            put((int)key, value); // Вставка нового узла
            return null;
        }
        else {
            var oldValue = node.data;
            node.data = value; // Обновление существующего узла
            return oldValue;
        }
    }

    // Вставка нового узла в дерево
    private void put(int key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            return;
        }

        Node currentNode = root;
        Node parent = null;

        // Поиск места для вставки
        while (currentNode != null) {
            parent = currentNode;
            if (key < currentNode.key)
                currentNode = currentNode.left;
            else if (key > currentNode.key)
                currentNode = currentNode.right;
        }

        // Создание и вставка нового узла
        Node newNode = new Node(key, value, parent);
        if (key < parent.key)
            parent.left = newNode;
        else
            parent.right = newNode;
        splay(newNode); // Перемещаем новый узел в корень
    }

    // Удаляет узел по ключу
    @Override
    public String remove(Object key) {
        if (isInvalidKeyType(key))
            throw new ClassCastException("The key is not of type Integer");

        var node = search((int)key);
        if (node != null) {
            size--;
            remove(node);
        }
        return node == null ? null : node.data;
    }

    // Удаление узла из дерева
    private void remove(Node node) {
        if (node.left == null) {
            // Нет левого поддерева - поднимаем правое
            root = node.right;
            if (root != null)
                root.parent = null;
        } else {
            // Есть левое поддерево
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;
            Node maxLeft = findMax(root); // Находим максимум в левом поддереве
            splay(maxLeft); // Поднимаем его в корень
            root.right = rightSubtree; // Присоединяем правое поддерево
            if (rightSubtree != null)
                rightSubtree.parent = root;
        }
    }

    // Находит узел с максимальным ключом в поддереве
    private Node findMax(Node node) {
        while (node.right != null)
            node = node.right;
        return node;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    // Не реализованные методы NavigableMap интерфейса
    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}
