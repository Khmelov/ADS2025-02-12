package by.it.group410902.yarmashuk.lesson12;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;


public class MyAvlMap implements Map<Object, Object> {


    private static class Node {
        Object key;
        Object value;
        Node left;
        Node right;
        int height;

        Node(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }


    // Вспомогательные методы для работы АВЛ-дерева

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }


    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Выполнение вращения
        x.right = y;
        y.left = T2;

        // Обновление высот
        updateHeight(y);
        updateHeight(x);

        return x;
    }


    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Выполнение вращения
        y.left = x;
        x.right = T2;

        // Обновление высот
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        // обновляем высоту узла
        updateHeight(node);

        int balance = getBalance(node);

        // Случай Left Left
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Случай Left Right
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Случай Right Right
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Случай Right Left
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }


    private Node findMin(Node node) {
        Node current = node;
        while (current != null && current.left != null) {
            current = current.left;
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object comparableKey, Object otherKey) {

        return ((Comparable<Object>) comparableKey).compareTo(otherKey);
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



    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean[] isFirst = {true};
        inOrderTraversal(root, sb, isFirst);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb, boolean[] isFirst) {
        if (node != null) {
            inOrderTraversal(node.left, sb, isFirst);

            if (!isFirst[0]) {
                sb.append(", "); // Добавляем разделитель перед каждым элементом, кроме первого
            }
            sb.append(node.key);
            sb.append("=");
            sb.append(node.value);
            isFirst[0] = false; // После добавления первого элемента, устанавливаем флаг в false

            inOrderTraversal(node.right, sb, isFirst);
        }
    }
    @Override
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        // Массив из одного элемента для передачи старого значения "по ссылке"
        // из рекурсивного метода.
        Object[] oldValueContainer = new Object[1];
        root = put(root, key, value, oldValueContainer);

        // Если oldValueContainer[0] равен null, значит, это была новая вставка
        // и размер должен быть увеличен.
        if (oldValueContainer[0] == null) {
            size++;
        }
        return oldValueContainer[0];
    }

    private Node put(Node node, Object key, Object value, Object[] oldValueContainer) {

        if (node == null) {
            return new Node(key, value);
        }

        int cmp = compare(node.key, key); // Сравниваем ключ узла с новым ключом
        if (cmp < 0) { // новый ключ больше, идем вправо
            node.right = put(node.right, key, value, oldValueContainer);
        } else if (cmp > 0) { //новый ключ меньше, идем влево
            node.left = put(node.left, key, value, oldValueContainer);
        } else { // Ключ уже существует
            oldValueContainer[0] = node.value;
            node.value = value;                // Обновляем значение
            return node;
        }


        return balance(node);
    }
    @Override
    public Object remove(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        // Массив из одного элемента для передачи удаленного значения "по ссылке"
        Object[] removedValueContainer = new Object[1];
        root = remove(root, key, removedValueContainer);

        // Если removedValueContainer[0] не равен null, значит, узел был успешно удален
        if (removedValueContainer[0] != null) {
            size--;
        }
        return removedValueContainer[0];
    }
    @Override
    public void putAll(Map<?, ?> m) {
    }
    private Node remove(Node node, Object key, Object[] removedValueContainer) {

        if (node == null) {
            return null; // Ключ не найден
        }

        int cmp = compare(node.key, key); // Сравниваем ключ узла с ключом для удаления
        if (cmp < 0) { //ключ для удаления больше, идем вправо
            node.right = remove(node.right, key, removedValueContainer);
        } else if (cmp > 0) { // ключ для удаления меньше, идем влево
            node.left = remove(node.left, key, removedValueContainer);
        } else { // node - это узел, который нужно удалить
            removedValueContainer[0] = node.value; // Сохраняем значение перед удалением

            // Случай, когда у узла 0 или 1 потомок
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // Случай, когда у узла 2 потомка:
                // Находим ин-ордер преемника (узел с минимальным ключом в правом поддереве)
                Node successor = findMin(node.right);

                // Копируем данные преемника в текущий узел
                node.key = successor.key;
                node.value = successor.value;
                // Удаляем преемника из правого поддерева.
                node.right = remove(node.right, successor.key, new Object[1]);
            }
        }
        if (node == null) {
            return null;
        }

        return balance(node);
    }
    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        Node node = get(root, key);
        return (node == null) ? null : node.value;
    }

    private Node get(Node node, Object key) {
        if (node == null) {
            return null;
        }

        int cmp = compare(node.key, key); // Сравниваем ключ узла с искомым ключом
        if (cmp < 0) { // node.key < key, значит key больше, идем вправо
            return get(node.right, key);
        } else if (cmp > 0) { // node.key > key, значит key меньше, идем влево
            return get(node.left, key);
        } else {
            return node; // Ключ найден
        }
    }
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        return get(root, key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }
    @Override
    public Set<Object> keySet() {
        return Set.of();
    }

    @Override
    public Collection<Object> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return Set.of();
    }
}

