package by.it.group410901.tomashevich.lesson12;

import java.util.Map;
import java.util.Objects;

public class MyAvlMap implements Map<Integer, String> {
    private class Node {
        Integer key;
        String info;
        Node left, right;
        int height;

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // Возвращает количество элементов в карте
    @Override
    public int size() {
        return size; // возвращает количество элементов в карте
    }

    // Проверяет, пустая ли карта
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяет, пустая ли карта
    }

    // Очищает карту, удаляя все узлы
    @Override
    public void clear() {
        root = null; // очищает дерево
        size = 0;    // сбрасывает размер
    }

    // Получает значение по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) { // проверяем, что ключ имеет тип Integer
            throw new ClassCastException("Key must be an Integer"); // иначе выбрасываем исключение
        }
        Node node = getNode(root, (Integer) key); // ищем узел с данным ключом
        return node == null ? null : node.info; // возвращаем значение, если найдено
    }

    // Ищет узел по ключу в дереве
    private Node getNode(Node node, Integer key) {
        if (node == null) { // если узел не найден
            return null;
        }
        if (key.equals(node.key)) { // если ключ совпадает
            return node; // возвращаем найденный узел
        }
        // рекурсивно ищем в левом или правом поддереве
        return key < node.key ? getNode(node.left, key) : getNode(node.right, key);
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String info) {
        String previousValue = get(key); // сохраняем старое значение (если ключ уже есть)
        root = putNode(root, key, info); // вставляем или обновляем узел
        return previousValue; // возвращаем предыдущее значение
    }

    // Вставляет узел в дерево и балансирует его
    private Node putNode(Node node, Integer key, String info) {
        if (node == null) { // если место пустое — создаём новый узел
            size++; // увеличиваем размер карты
            return new Node(key, info); // возвращаем новый узел
        }
        if (key.equals(node.key)) { // если ключ уже существует
            node.info = info; // обновляем значение
            return node; // возвращаем узел без изменений структуры
        } else if (key < node.key) { // если ключ меньше — идём влево
            node.left = putNode(node.left, key, info);
        } else { // иначе идём вправо
            node.right = putNode(node.right, key, info);
        }

        // обновляем высоту узла после вставки
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // возвращаем сбалансированный узел
        return balance(node);
    }

    // Удаляет узел по ключу и возвращает его значение
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) { // проверяем тип ключа
            throw new ClassCastException("Key must be an Integer");
        }
        String info = get((Integer) key); // ищем значение по ключу
        if (info != null) { // если найдено
            root = removeNode(root, (Integer) key); // удаляем узел
            return info; // возвращаем удалённое значение
        }
        return null; // если ключ не найден
    }

    // Удаляет узел из дерева и балансирует его
    private Node removeNode(Node node, Integer key) {
        if (node == null) { // если узел не найден
            return null;
        }

        if (key < node.key) { // если ключ меньше — идём влево
            node.left = removeNode(node.left, key);
        } else if (key > node.key) { // если больше — вправо
            node.right = removeNode(node.right, key);
        } else { // нашли узел для удаления
            if (node.left == null) { // если нет левого поддерева
                size--; // уменьшаем размер
                return node.right; // возвращаем правое поддерево
            } else if (node.right == null) { // если нет правого поддерева
                size--; // уменьшаем размер
                return node.left; // возвращаем левое поддерево
            }

            // если оба поддерева есть — ищем минимум справа
            Node min = getMin(node.right);
            node.key = min.key; // заменяем ключ текущего узла
            node.info = min.info; // и значение
            node.right = removeNode(node.right, min.key); // удаляем дубликат справа
        }

        // обновляем высоту узла
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // балансируем дерево после удаления
        return balance(node);
    }

    // Находит узел с минимальным ключом
    private Node getMin(Node node) {
        while (node.left != null) { // идём влево до конца
            node = node.left;
        }
        return node; // возвращаем минимальный узел
    }

    // Проверяет наличие ключа в карте
    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) { // проверяем тип ключа
            return false;
        }
        return getNode(root, (Integer) obj) != null; // возвращаем true, если узел найден
    }

    // Возвращает строковое представление карты
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{"); // начинаем строку вывода
        UpdateSB(root, resultStr); // рекурсивно добавляем элементы
        if (resultStr.length() > 1) { // если есть хотя бы один элемент
            resultStr.setLength(resultStr.length() - 2); // убираем последнюю запятую
        }
        resultStr.append("}"); // закрываем фигурной скобкой
        return resultStr.toString(); // возвращаем строку
    }

    // Формирует строковое представление узлов дерева
    private void UpdateSB(Node node, StringBuilder result) {
        if (node != null) { // если узел не пустой
            UpdateSB(node.left, result); // обходим левое поддерево
            result.append(node.key).append("=").append(node.info).append(", "); // добавляем пару ключ=значение
            UpdateSB(node.right, result); // обходим правое поддерево
        }
    }

    // Возвращает высоту узла
    private int getHeight(Node node) {
        return node == null ? 0 : node.height; // возвращает высоту узла
    }

    // Вычисляет баланс узла
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right); // возвращает баланс узла
    }

    // Балансирует дерево после операций
    private Node balance(Node node) {
        int balance = getBalance(node); // вычисляем баланс узла

        if (balance > 1) { // левое поддерево тяжелее
            if (getBalance(node.left) < 0) { // левый ребёнок имеет правый перевес
                node.left = rotateLeft(node.left); // делаем малый левый поворот
            }
            return rotateRight(node); // выполняем правый поворот
        }

        if (balance < -1) { // правое поддерево тяжелее
            if (getBalance(node.right) > 0) { // правый ребёнок имеет левый перевес
                node.right = rotateRight(node.right); // делаем малый правый поворот
            }
            return rotateLeft(node); // выполняем левый поворот
        }

        return node; // если баланс в норме — возвращаем узел без изменений
    }

    // Выполняет правый поворот узла
    private Node rotateRight(Node node) {
        Node root = node.left; // выбираем левое поддерево в качестве нового корня
        node.left = root.right; // переносим правое поддерево нового корня налево
        root.right = node; // делаем старый корень правым потомком
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right)); // пересчитываем высоту старого корня
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right)); // пересчитываем высоту нового корня
        return root; // возвращаем новый корень
    }

    // Выполняет левый поворот узла
    private Node rotateLeft(Node node) {
        Node root = node.right; // выбираем правое поддерево в качестве нового корня
        node.right = root.left; // переносим левое поддерево нового корня вправо
        root.left = node; // делаем старый корень левым потомком
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right)); // пересчитываем высоту старого корня
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right)); // пересчитываем высоту нового корня
        return root; // возвращаем новый корень
    }

    // Проверяет наличие значения в карте
    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj); // запускаем рекурсивный поиск значения
    }

    // Рекурсивно ищет значение в дереве
    private boolean containsValue(Node node, Object obj) {
        if (node == null) { // если дошли до конца — значение не найдено
            return false;
        }
        if (Objects.equals(node.info, obj)) { // если значение совпадает
            return true;
        }
        // иначе ищем в левом и правом поддеревьях
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    // Возвращает набор пар ключ-значение (не реализован)
    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented yet"); // метод не реализован
    }

    // Возвращает набор ключей (не реализован)
    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented yet"); // метод не реализован
    }

    // Возвращает коллекцию значений (не реализован)
    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented yet"); // метод не реализован
    }

    // Добавляет все пары ключ-значение из переданной карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) { // перебираем все пары из карты m
            put(entry.getKey(), entry.getValue()); // добавляем их в текущее дерево
        }
    }
}