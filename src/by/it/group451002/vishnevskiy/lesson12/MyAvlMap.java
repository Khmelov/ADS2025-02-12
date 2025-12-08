package by.it.group451002.vishnevskiy.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;          //Корень AVL-дерева
    private int size = 0;       // Количество элементов в дереве

    // Узел AVL-дерева — хранит key, value, ссылки на детей и высоту
    private static class Node {
        Integer key;           // Ключ
        String value;          // Значение
        Node left;             // Левый ребенок
        Node right;            // Правый ребенок
        int height;            // Высота поддерева в этом узле

        Node(Integer key, String value) {
            this.key = key;    //Сохраняем ключ
            this.value = value;//Сохраняем значение
            this.height = 1;   // Новый узел всегда высоты 1
        }
    }

    // Безопасная высота: null → 0
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    // Фактор баланса = высота(left) - высота(right)
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Обновление высоты узла после изменений детей
    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    // Правый поворот вокруг y
    private Node rightRotate(Node y) {
        Node x = y.left;     // x — левый ребенок y
        Node T2 = x.right;   // Сохраняем правое поддерево x

        x.right = y;         // Делаем вращение
        y.left = T2;         // Переносим T2

        updateHeight(y);     // Обновляем высоту y (теперь он ниже)
        updateHeight(x);     // Обновляем высоту x (он стал выше)

        return x;            // Новый корень поддерева
    }

    // Левый поворот вокруг x
    private Node leftRotate(Node x) {
        Node y = x.right;    // y — правый ребенок x
        Node T2 = y.left;    // Сохраняем левое поддерево y

        y.left = x;          // Выполняем вращение
        x.right = T2;        // Переносим T2

        updateHeight(x);     // Обновляем высоту x
        updateHeight(y);     // Обновляем высоту y

        return y;            // Новый корень поддерева
    }

    // Вставка ключа в дерево
    private Node insert(Node node, Integer key, String value) {

        //Обычная вставка в бинарное дерево поиска
        if (node == null) {
            return new Node(key, value); // Новый узел
        }

        // Выбираем направление вставки
        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, value);   // Влево
        } else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key, value); // Вправо
        } else {
            node.value = value; // Если ключ уже есть — заменяем значение
            return node;
        }

        updateHeight(node);             // Обновляем высоту
        int balance = getBalance(node); // Получаем баланс

        // 4 базовых случая дисбаланса:

        // Left-Left
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Right-Right
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Left-Right
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // Балансировка не требуется
    }

    // Удаление узла по ключу
    private Node deleteNode(Node root, Integer key) {

        // Обычное удаление в BST
        if (root == null) return null;

        if (key < root.key) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.key) {
            root.right = deleteNode(root.right, key);
        } else {

            // Найден удаляемый узел

            // Один ребенок или нет детей
            if (root.left == null || root.right == null) {
                Node temp = (root.left != null) ? root.left : root.right;

                if (temp == null) {
                    root = null; // Нет детей
                } else {
                    root = temp; // Один ребенок
                }
            } else {

                // Два ребенка — берём минимальный узел справа
                Node temp = minValueNode(root.right);

                root.key = temp.key;     // Копируем ключ
                root.value = temp.value; // Копируем значение

                root.right = deleteNode(root.right, temp.key);
            }
        }

        if (root == null) return null;

        updateHeight(root);              // Обновляем высоту
        int balance = getBalance(root);  // Проверяем баланс

        // Балансировка как в insert

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

        return root;
    }

    // Поиск минимального узла (самый левый)
    private Node minValueNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Поиск узла по ключу
    private Node getNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return getNode(node.left, key);
        if (cmp > 0) return getNode(node.right, key);
        return node;
    }

    // ---------------- Методы интерфейса Map ----------------

    @Override
    public int size() {
        return size; // Возвращаем количество элементов
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Пусто?
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false; // Неверный тип
        return getNode(root, (Integer) key) != null; // Проверка существования
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null; // Если не Integer
        Node n = getNode(root, (Integer) key);
        return n == null ? null : n.value; // Возвращаем значение
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);  // Запоминаем прежнее значение
        if (old == null) size++; // Если ключ новый — увеличиваем размер
        root = insert(root, key, value); // Вставляем
        return old; // Возвращаем старое значение
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String old = get(key);  // Запоминаем старое значение
        if (old != null) {
            root = deleteNode(root, (Integer) key); // Удаляем
            size--;                                 // Уменьшаем размер
        }
        return old; // Возвращаем удалённое значение
    }

    @Override
    public void clear() {
        root = null; // Удаляем дерево
        size = 0;    // Обнуляем размер
    }

    // Преобразование карты в строку вида {1=A, 3=Q, 9=Z}
    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb); // Вставляем элементы в отсортированном порядке

        sb.setLength(sb.length() - 2); // Удаляем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход: левый - корень - правый
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderTraversal(node.right, sb);
    }

    // Методы, которые по заданию реализовывать не нужно
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
