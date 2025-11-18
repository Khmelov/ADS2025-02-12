package by.it.group410901.kvitchenko.lesson12; // Объявление пакета.

import java.util.Map; // Импорт интерфейса Map.
import java.util.Objects; // Импорт класса Objects для сравнения объектов.

// Класс MyAvlMap реализует интерфейс Map<Integer, String> с использованием АВЛ-дерева.
public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс Node представляет узел в АВЛ-дереве.
    private class Node {
        Integer key;      // Ключ узла.
        String info;      // Значение узла.
        Node left, right; // Левый и правый дочерние узлы.
        int height;       // Высота поддерева, корнем которого является этот узел.

        // Конструктор для создания нового узла.
        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
            this.height = 1; // Новый узел - лист, его высота равна 1.
        }
    }

    private Node root; // Корень всего АВЛ-дерева.
    private int size;  // Общее количество элементов (пар ключ-значение).

    // Конструктор по умолчанию.
    public MyAvlMap() {
        root = null; // Инициализация пустого дерева.
        size = 0;
    }


    // Реализация методов интерфейса Map

    @Override
    public int size() {
        return size; // Возвращает количество элементов.
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверяет, пусто ли дерево.
    }

    @Override
    public void clear() {
        root = null; // Очистка дерева обнулением корня.
        size = 0;    // Сброс счетчика размера.
    }

    @Override
    // Возвращает значение по ключу (O(logN)).
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer"); // Проверка типа ключа.
        }
        Node node = getNode(root, (Integer) key); // Поиск узла по ключу.
        return node == null ? null : node.info; // Возвращает значение или null.
    }

    // Вспомогательная рекурсивная функция для поиска узла.
    private Node getNode(Node node, Integer key) {
        if (node == null) {
            return null; // Узел не найден.
        }
        if (key.equals(node.key)) {
            return node; // Узел найден.
        }
        // Рекурсивный спуск влево или вправо.
        return key < node.key ? getNode(node.left, key) : getNode(node.right, key);
    }



    @Override
    // Добавляет новую пару или обновляет значение.
    public String put(Integer key, String info) {
        String previousValue = get(key); // Получаем старое значение для возврата.
        root = putNode(root, key, info); // Рекурсивная вставка и балансировка.
        return previousValue;
    }

    // Рекурсивная вставка в BST, обновление высоты и балансировка АВЛ-дерева.
    private Node putNode(Node node, Integer key, String info) {
        if (node == null) {
            size++; // Увеличиваем размер при вставке нового узла.
            return new Node(key, info); // Вставляем новый узел.
        }

        // 1. Стандартный поиск и обновление
        if (key.equals(node.key)) {
            node.info = info; // Обновление значения.
            return node;
        }
        // Рекурсивный спуск
        else if (key < node.key) {
            node.left = putNode(node.left, key, info);
        } else {
            node.right = putNode(node.right, key, info);
        }

        // 2. Обновление высоты текущего узла (на обратном пути).
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // 3. Балансировка поддерева.
        return balance(node); // Возвращаем (возможно) новый корень сбалансированного поддерева.
    }



    @Override
    // Удаляет отображение для указанного ключа (O(logN)).
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer"); // Проверка типа ключа.
        }
        String info = get((Integer) key); // Получаем значение, чтобы проверить, существует ли ключ.
        if (info != null) {
            root = removeNode(root, (Integer) key); // Рекурсивное удаление и балансировка.
            return info;
        }
        return null; // Ключ не был найден.
    }

    // Рекурсивное удаление в BST и балансировка АВЛ-дерева.
    private Node removeNode(Node node, Integer key) {
        if (node == null) {
            return null; // Узел не найден.
        }

        // 1. Поиск узла для удаления
        if (key < node.key) {
            node.left = removeNode(node.left, key); // Спуск влево.
        } else if (key > node.key) {
            node.right = removeNode(node.right, key); // Спуск вправо.
        } else {
            // 2. Узел найден. Обработка случаев удаления:
            if (node.left == null) {
                size--;
                return node.right; // Случай 0 или 1 потомка (правый).
            } else if (node.right == null) {
                size--;
                return node.left;  // Случай 1 потомка (левый).
            }

            // 3. Узел с 2 потомками: замена на преемника.
            Node min = getMin(node.right); // Находим минимальный в правом поддереве.
            node.key = min.key;            // Копируем ключ преемника.
            node.info = min.info;          // Копируем значение преемника.
            // Рекурсивно удаляем преемника из правого поддерева.
            node.right = removeNode(node.right, min.key);
        }

        // Если узел стал null после удаления, нет смысла балансировать.
        if (node == null) return null;

        // 4. Обновление высоты и балансировка на обратном пути.
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    // Находит узел с минимальным ключом (крайний левый в поддереве).
    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left; // Двигаемся влево.
        }
        return node;
    }


    // Возвращает высоту узла (0, если null).
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    // Вычисляет фактор баланса (Height(Left) - Height(Right)).
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    // Выполняет повороты для восстановления свойства АВЛ-дерева.
    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) { // Левое поддерево слишком высокое (LL или LR)
            if (getBalance(node.left) < 0) { // LR-случай
                node.left = rotateLeft(node.left); // Малый левый поворот (внутренний)
            }
            return rotateRight(node); // Большой правый поворот (внешний)
        }

        if (balance < -1) { // Правое поддерево слишком высокое (RR или RL)
            if (getBalance(node.right) > 0) { // RL-случай
                node.right = rotateRight(node.right); // Малый правый поворот (внутренний)
            }
            return rotateLeft(node); // Большой левый поворот (внешний)
        }

        return node; // Узел сбалансирован.
    }

    // Выполняет правый поворот.
    private Node rotateRight(Node node) {
        Node root = node.left;      // Новый корень.
        node.left = root.right;     // Перемещаем правое поддерево нового корня.
        root.right = node;          // Текущий узел становится правым потомком.

        // Обновление высот: сначала дочерний (node), затем новый корень (root).
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    // Выполняет левый поворот.
    private Node rotateLeft(Node node) {
        Node root = node.right;     // Новый корень.
        node.right = root.left;     // Перемещаем левое поддерево нового корня.
        root.left = node;           // Текущий узел становится левым потомком.

        // Обновление высот: сначала дочерний (node), затем новый корень (root).
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }


    // Вспомогательные методы Map

    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }
        return getNode(root, (Integer) obj) != null; // Проверяет наличие узла.
    }

    @Override
    // Генерирует строковое представление карты (In-Order обход).
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{"); // Начало строки.
        UpdateSB(root, resultStr); // Рекурсивное заполнение.
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2); // Удаление ", " в конце.
        }
        resultStr.append("}"); // Конец строки.
        return resultStr.toString();
    }

    // Рекурсивный In-Order обход: Лево -> Корень -> Право.
    private void UpdateSB(Node node, StringBuilder result) {
        if (node != null) {
            UpdateSB(node.left, result); // Левое поддерево.
            result.append(node.key).append("=").append(node.info).append(", "); // Корень.
            UpdateSB(node.right, result); // Правое поддерево.
        }
    }

    @Override
    // Ищет значение во всем дереве (O(N)).
    public boolean containsValue(Object obj) {
        return containsValue(root, obj); // Вызов рекурсивной функции.
    }

    // Рекурсивный обход для поиска значения.
    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false; // Достигнут null.
        }
        if (Objects.equals(node.info, obj)) {
            return true; // Значение найдено.
        }
        // Ищем в левом ИЛИ правом поддереве.
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}