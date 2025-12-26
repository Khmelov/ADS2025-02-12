package by.it.group451001.serganovskij.lesson12;
import java.util.Map;
import java.util.Objects;

/**
 * Реализация Map на основе AVL-дерева (сбалансированного бинарного дерева поиска)
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Внутренний класс узла AVL-дерева
     */
    private class Node {
        Integer key;        // Ключ узла
        String info;        // Значение узла
        Node left, right;   // Левый и правый потомки
        int height;         // Высота узла (для балансировки)

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
            this.height = 1; // Новая node имеет высоту 1
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в дереве

    public MyAvlMap() {
        root = null;
        size = 0;
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
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Получение значения по ключу
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer");
        }
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.info;
    }

    /**
     * Рекурсивный поиск узла по ключу
     */
    private Node getNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node;
        }
        return key < node.key ? getNode(node.left, key) : getNode(node.right, key);
    }

    /**
     * Добавление или обновление элемента
     * Возвращает предыдущее значение, если ключ существовал
     */
    @Override
    public String put(Integer key, String info) {
        String previousValue = get(key);
        root = putNode(root, key, info);
        return previousValue;
    }

    /**
     * Рекурсивное добавление узла с последующей балансировкой
     */
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

    /**
     * Удаление элемента по ключу
     */
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

    /**
     * Рекурсивное удаление узла с последующей балансировкой
     */
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

            // У узла два потомка - находим минимальный в правом поддереве
            Node min = getMin(node.right);
            node.key = min.key;
            node.info = min.info;
            node.right = removeNode(node.right, min.key);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    /**
     * Поиск узла с минимальным ключом в поддереве
     */
    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }
        return getNode(root, (Integer) obj) != null;
    }

    /**
     * Строковое представление в формате {key1=value1, key2=value2}
     */
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

    /**
     * Обход дерева в порядке возрастания (in-order) для построения строки
     */
    private void UpdateSB(Node node, StringBuilder result) {
        if (node != null) {
            UpdateSB(node.left, result);
            result.append(node.key).append("=").append(node.info).append(", ");
            UpdateSB(node.right, result);
        }
    }

    // === МЕТОДЫ БАЛАНСИРОВКИ AVL-ДЕРЕВА ===

    /**
     * Получение высоты узла (обработка null)
     */
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Вычисление баланс-фактора (разница высот левого и правого поддеревьев)
     */
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Балансировка узла при нарушении баланса
     */
    private Node balance(Node node) {
        int balance = getBalance(node);

        // Левое поддерево выше (Left-Left или Left-Right случаи)
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left); // Left-Right случай
            }
            return rotateRight(node); // Правое вращение
        }

        // Правое поддерево выше (Right-Right или Right-Left случаи)
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right); // Right-Left случай
            }
            return rotateLeft(node); // Левое вращение
        }

        return node; // Балансировка не требуется
    }

    /**
     * Правое вращение (для Left-Left случая)
     */
    private Node rotateRight(Node node) {
        Node root = node.left;
        node.left = root.right;
        root.right = node;
        // Обновление высот
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    /**
     * Левое вращение (для Right-Right случая)
     */
    private Node rotateLeft(Node node) {
        Node root = node.right;
        node.right = root.left;
        root.left = node;
        // Обновление высот
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj);
    }

    /**
     * Рекурсивный поиск значения в дереве
     */
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

    /**
     * Добавление всех элементов из другой карты
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}