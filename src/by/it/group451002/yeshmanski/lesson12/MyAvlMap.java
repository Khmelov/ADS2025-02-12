package by.it.group451002.yeshmanski.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;
    private int size = 0;

    // Внутренний класс узла дерева
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }


    // Получение высоты узла (безопасно для null)
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    // Получение фактора баланса
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Обновление высоты узла на основе детей
    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    // Правый поворот (вокруг y)
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        updateHeight(y);
        updateHeight(x);

        return x; // Новый корень поддерева
    }

    // Левый поворот (вокруг x)
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        updateHeight(x);
        updateHeight(y);

        return y; // Новый корень поддерева
    }

    // --- Основные операции ---

    private Node insert(Node node, Integer key, String value) {

        if (node == null) {

            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Ключи равны, обновляем значение
            node.value = value;
            return node;
        }

        // Обновляем высоту текущего узла
        updateHeight(node);

        // Балансировка узла
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private Node deleteNode(Node root, Integer key) {
        // 1. Обычное удаление BST
        if (root == null) return root;

        if (key.compareTo(root.key) < 0) {
            root.left = deleteNode(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            root.right = deleteNode(root.right, key);
        } else {
            // Узел найден
            // Узел с одним или нулем детей
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp; // Копируем содержимое непустого ребенка
                }
            } else {
                // Узел с двумя детьми: получаем преемника (мин. в правом поддереве)
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.value = temp.value; // Важно скопировать и значение
                root.right = deleteNode(root.right, temp.key);
            }
        }

        if (root == null) return root;

        // 2. Обновляем высоту
        updateHeight(root);

        // 3. Балансировка
        int balance = getBalance(root);

        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node getNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return getNode(node.left, key);
        else if (cmp > 0) return getNode(node.right, key);
        else return node;
    }

    // --- Реализация обязательных методов интерфейса Map ---

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
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key); // Сохраняем старое значение для возврата
        if (oldValue == null) {
            size++; // Если элемента не было, размер увеличивается
        }
        root = insert(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String oldValue = get(key);
        if (oldValue != null) {
            root = deleteNode(root, (Integer) key);
            size--;
            return oldValue;
        }
        return null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // --- Метод toString() ---

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        // Удаляем последнюю запятую и пробел
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход (In-Order) для сортировки по возрастанию
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key)
                    .append("=")
                    .append(node.value)
                    .append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // --- Заглушки для остальных методов Map (не требуются по заданию) ---

    @Override
    public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}