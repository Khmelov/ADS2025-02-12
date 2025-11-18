package by.it.group410901.kalach.lesson12;

import java.util.Map;
import java.util.Objects;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс для узла AVL-дерева
    private class Node {
        Integer key;        // Ключ узла
        String info;        // Значение узла
        Node left, right;   // Левый и правый потомки
        int height;         // Высота поддерева с корнем в этом узле

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
            this.height = 1; // Новая нода имеет высоту 1
        }
    }

    private Node root; // Корень AVL-дерева
    private int size;  // Количество элементов в карте

    // Конструктор по умолчанию
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // Возвращает количество элементов в карте
    @Override
    public int size() {
        return size;
    }

    // Проверяет, пуста ли карта
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает карту, удаляя все элементы
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer");
        }
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.info;
    }

    // Вспомогательный метод для поиска узла по ключу
    private Node getNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node;
        }
        return key < node.key ? getNode(node.left, key) : getNode(node.right, key);
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String info) {
        String previousValue = get(key);
        root = putNode(root, key, info);
        return previousValue;
    }

    // Вспомогательный метод для вставки узла
    private Node putNode(Node node, Integer key, String info) {
        if (node == null) {
            size++;
            return new Node(key, info);
        }
        if (key.equals(node.key)) {
            node.info = info; // Обновление значения существующего ключа
            return node;
        } else if (key < node.key) {
            node.left = putNode(node.left, key, info);
        } else {
            node.right = putNode(node.right, key, info);
        }

        // Обновление высоты и балансировка
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    // Удаляет элемент по ключу
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer");
        }
        String info = get((Integer) key);
        if (info != null) {
            root = removeNode(root, (Integer) key);
            return info;
        }
        return null;
    }

    // Вспомогательный метод для удаления узла
    private Node removeNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = removeNode(node.left, key);
        } else if (key > node.key) {
            node.right = removeNode(node.right, key);
        } else {
            // Найден узел для удаления
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            // У узла есть два потомка - находим минимальный элемент в правом поддереве
            Node min = getMin(node.right);
            node.key = min.key;
            node.info = min.info;
            node.right = removeNode(node.right, min.key);
        }

        // Обновление высоты и балансировка
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    // Находит узел с минимальным ключом в поддереве
    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Проверяет наличие ключа в карте
    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }
        return getNode(root, (Integer) obj) != null;
    }

    // Возвращает строковое представление карты
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{");
        UpdateSB(root, resultStr);
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2);
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    // Вспомогательный метод для построения строкового представления (обход в порядке возрастания)
    private void UpdateSB(Node node, StringBuilder result) {
        if (node != null) {
            UpdateSB(node.left, result);
            result.append(node.key).append("=").append(node.info).append(", ");
            UpdateSB(node.right, result);
        }
    }

    // Возвращает высоту узла (0 для null)
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    // Вычисляет баланс-фактор узла (разница высот левого и правого поддеревьев)
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    // Балансирует узел при необходимости
    private Node balance(Node node) {
        int balance = getBalance(node);

        // Левое поддерево слишком высокое
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left); // Двойной поворот: лево-правый
            }
            return rotateRight(node);
        }

        // Правое поддерево слишком высокое
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right); // Двойной поворот: право-левый
            }
            return rotateLeft(node);
        }

        return node;
    }

    // Правый поворот (для случая Left-Left)
    private Node rotateRight(Node node) {
        Node root = node.left;
        node.left = root.right;
        root.right = node;
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    // Левый поворот (для случая Right-Right)
    private Node rotateLeft(Node node) {
        Node root = node.right;
        node.right = root.left;
        root.left = node;
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    // Проверяет наличие значения в карте
    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj);
    }

    // Вспомогательный метод для поиска значения (обход в глубину)
    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false;
        }
        if (Objects.equals(node.info, obj)) {
            return true;
        }
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    // Не реализованные методы интерфейса Map

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Добавляет все элементы из другой карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}