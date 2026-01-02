package by.it.group451002.vishnevskiy.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MySplayMap implements Map<Integer, String> {

    private Node root;        // корень AVL-дерева
    private int size = 0;     // количество элементов

    // Узел AVL-дерева
    private static class Node {
        Integer key;          // ключ
        String value;         // значение
        Node left;            // левый ребёнок
        Node right;           // правый ребёнок
        int height;           // высота узла

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  //новый узел имеет высоту 1
        }
    }

    // Высота узла (если null — 0)
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    //Фактор баланса узла (разница высот поддеревьев)
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Пересчёт высоты по детям
    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    // Правый поворот
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // выполняем поворот
        x.right = y;
        y.left = T2;

        // обновляем высоты после вращения
        updateHeight(y);
        updateHeight(x);

        return x;  // новый корень поддерева
    }

    // Левый поворот
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // выполняем поворот
        y.left = x;
        x.right = T2;

        // обновляем высоты
        updateHeight(x);
        updateHeight(y);

        return y;  // новый корень поддерева
    }

    // --- ВСТАВКА В AVL ДЕРЕВО ---
    private Node insert(Node node, Integer key, String value) {

        // Обычное BST-вставка
        if (node == null) {
            return new Node(key, value); // новый узел
        }

        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, value);  // идём влево
        }
        else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key, value); // идём вправо
        }
        else {
            node.value = value;  // ключ существует → обновляем значение
            return node;
        }

        // Обновляем высоту
        updateHeight(node);

        // Проверка баланса
        int balance = getBalance(node);

        // 4 стандартных случая дисбаланса:

        // Left-Left
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);

        // Right-Right
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);

        // Left-Right
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // возвращаем (не изменённый или сбалансированный)
    }

    // --- УДАЛЕНИЕ В AVL ---
    private Node deleteNode(Node root, Integer key) {

        // Обычное удаление из BST
        if (root == null) return null;

        if (key.compareTo(root.key) < 0)
            root.left = deleteNode(root.left, key);       // идём влево
        else if (key.compareTo(root.key) > 0)
            root.right = deleteNode(root.right, key);     // идём вправо
        else {
            // Нашли удаляемый узел

            // Узел с 0 или 1 ребёнком
            if (root.left == null || root.right == null) {
                Node temp = (root.left != null) ? root.left : root.right;

                // нет детей
                if (temp == null) {
                    temp = root;
                    root = null;
                }
                else {
                    root = temp; // один ребёнок → поднимаем его
                }
            }
            else {
                // Узел с двумя детьми → берём inorder-преемника
                Node temp = minValueNode(root.right);

                root.key = temp.key;       // копируем ключ
                root.value = temp.value;   // копируем значение

                // удаляем преемника
                root.right = deleteNode(root.right, temp.key);
            }
        }

        if (root == null) return null;

        // Пересчёт высоты
        updateHeight(root);

        // Балансировка
        int balance = getBalance(root);

        // Left-Left
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left-Right
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right-Right
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right-Left
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root; // возвращаем сбалансированный узел
    }

    // Минимальный узел (самый левый)
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    // Поиск узла по ключу (обычный BST поиск)
    private Node getNode(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0) return getNode(node.left, key);  // идём влево
        if (cmp > 0) return getNode(node.right, key); // идём вправо
        return node;                                  // нашли
    }

    // ===== Методы интерфейса Map =====

    @Override
    public int size() {
        return size; // количество элементов
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // пусто ли дерево
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null; // есть ли ключ
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value; // возвращаем значение
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key); // сохраняем старое значение
        if (oldValue == null)
            size++;                 // ключ новый → увеличиваем size

        root = insert(root, key, value); // вставка в дерево
        return oldValue;                 // возвращаем старое значение
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String oldValue = get(key); // значение перед удалением
        if (oldValue != null) {
            root = deleteNode(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    @Override
    public void clear() {
        root = null; // очищаем дерево
        size = 0;    // размер = 0
    }

    // --- Вывод карты в виде {k=v, k=v} ---
    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb); // симметричный обход (по возрастанию)

        sb.setLength(sb.length() - 2); // удаляем ", "
        sb.append("}");
        return sb.toString();
    }

    // Обход in-order
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Методы, не требуемые по заданию
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
