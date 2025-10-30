package by.it.group410902.kavtsevich.lesson12;

import java.util.*;

// Реализация Map на основе AVL-дерева
public class MyAvlMap implements Map<Integer, String> {

    private Node root;    // корень дерева
    private int size;     // количество элементов

    // Узел AVL-дерева
    private class Node {
        Integer key;      // ключ
        String value;    // значение
        Node left;       // левый потомок
        Node right;      // правый потомок
        int height;      // высота узла

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  // начальная высота
        }
    }

    // Возвращает высоту узла
    private int getHeight(Node node) {
        if (node == null) return 0; //0 для null
        return node.height;
    }

    // Вычисляет баланс-фактор (разница высот левого и правого поддеревьев)
    private int getBalance(Node node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    // Обновляет высоту узла на основе высот потомков
    private void updateHeight(Node node) {
        if (node != null)
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    // Правый поворот для балансировки (Left-Left случай)
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        updateHeight(y);
        updateHeight(x);
        return x;  // новый корень
    }

    // Левый поворот для балансировки (Right-Right случай)
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        updateHeight(x);
        updateHeight(y);
        return y;  // новый корень
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);  // проверяем существующее значение
        root = putHelper(root, key, value);
        if (oldValue == null) size++;  // увеличиваем размер если ключ новый
        return oldValue;
    }

    // Рекурсивно добавляет элемент и балансирует дерево
    private Node putHelper(Node node, Integer key, String value) {
        // Базовый случай - создаем новый узел
        if (node == null) return new Node(key, value);

        // Ищем место для вставки
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = putHelper(node.left, key, value);
        else if (cmp > 0)
            node.right = putHelper(node.right, key, value);
        else {
            // Ключ существует - обновляем значение
            node.value = value;
            return node;
        }

        // Балансировка после вставки
        updateHeight(node);
        int balance = getBalance(node);

        // Left-Left случай
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rotateRight(node);

        // Right-Right случай
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return rotateLeft(node);

        // Left-Right случай (двойное вращение)
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Left случай (двойное вращение)
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;  // балансировка не требуется
    }

    // Удаляет ключ из карты
    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        String oldValue = get(intKey);
        if (oldValue != null) {
            root = removeHelper(root, intKey);
            size--;
        }
        return oldValue;
    }

    // Рекурсивно удаляет элемент и балансирует дерево
    private Node removeHelper(Node node, Integer key) {
        if (node == null) return node;

        // Ищем удаляемый узел
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = removeHelper(node.left, key);
        else if (cmp > 0)
            node.right = removeHelper(node.right, key);
        else {
            // Узел найден - удаляем

            // Случай 1: узел имеет 0 или 1 потомка
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    // Нет потомков
                    node = null;
                } else {
                    // Один потомок
                    node = temp;
                }
            } else {
                // Случай 2: узел имеет двух потомков
                // Находим минимальный элемент в правом поддереве
                Node temp = getMinValueNode(node.right);
                // Копируем данные
                node.key = temp.key;
                node.value = temp.value;
                // Удаляем минимальный элемент
                node.right = removeHelper(node.right, temp.key);
            }
        }

        if (node == null) return node;

        // Балансировка после удаления
        updateHeight(node);
        int balance = getBalance(node);

        // Left-Left
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left-Right
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Right-Left
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Находит узел с минимальным ключом в поддереве
    private Node getMinValueNode(Node node) {
        Node curr = node;
        while (curr.left != null) curr = curr.left;
        return curr;
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        return getHelper(root, intKey);
    }

    // Рекурсивно ищет значение по ключу
    private String getHelper(Node node, Integer key) {
        if (node == null) return null;  // ключ не найден

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            return getHelper(node.left, key);
        else if (cmp > 0)
            return getHelper(node.right, key);
        else
            return node.value;  // ключ найден
    }

    // Проверяет наличие ключа в карте
    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof Integer)) return false;
        return get(key) != null;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверяет пуста ли карта
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает карту
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Возвращает строковое представление карты
    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);  // удаляем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    // Обход дерева в порядке возрастания ключей
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }
    //Нереализованные методы Map
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

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
}