package by.it.group451002.mishchenko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private Node root;  // Корень АВЛ-дерева
    private int size;   // Количество элементов в дереве

    // Узел АВЛ-дерева с высотой поддерева
    private static class Node {
        Integer key;    // Ключ узла
        String value;   // Значение узла
        Node left;      // Левый потомок
        Node right;     // Правый потомок
        int height;     // Высота поддерева с корнем в этом узле

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новый узел имеет высоту 1
        }
    }

    // Получить высоту узла (для null возвращает 0)
    private int height(Node n) {
        return n != null ? n.height : 0;
    }

    // Вычислить баланс-фактор (разность высот правого и левого поддеревьев)
    private int balanceFactor(Node n) {
        return height(n.right) - height(n.left);
    }

    // Обновить высоту узла на основе высот потомков
    private void updateHeight(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);
        n.height = (Math.max(hl, hr)) + 1;
    }

    // Правый поворот вокруг узла y
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // Левый поворот вокруг узла x
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // Балансировка узла после вставки или удаления
    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        // Левое поддерево выше (левый-левый или левый-правый случай)
        if (bf == -2) {
            if (balanceFactor(n.left) > 0) n.left = rotateLeft(n.left); // Лево-правый случай
            return rotateRight(n); // Лево-левый случай
        }
        // Правое поддерево выше (правый-правый или правый-левый случай)
        if (bf == 2) {
            if (balanceFactor(n.right) < 0) n.right = rotateRight(n.right); // Право-левый случай
            return rotateLeft(n); // Право-правый случай
        }
        return n; // Узел сбалансирован
    }

    // Рекурсивная вставка элемента в дерево
    private Node put(Node n, Integer key, String value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }

        if (key < n.key) n.left = put(n.left, key, value);
        else if (key > n.key) n.right = put(n.right, key, value);
        else n.value = value; // Обновление значения существующего ключа

        return balance(n); // Балансировка после вставки
    }

    // Найти узел с минимальным ключом в поддереве
    private Node findMin(Node n) {
        while (n != null && n.left != null) n = n.left;
        return n;
    }

    // Удалить узел с минимальным ключом из поддерева
    private Node removeMin(Node n) {
        if (n == null) return null;
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    // Рекурсивное удаление узла по ключу
    private Node remove(Node n, Integer key) {
        if (n == null) return null;

        if (key < n.key) n.left = remove(n.left, key);
        else if (key > n.key) n.right = remove(n.right, key);
        else {
            // Узел найден - удаляем его
            Node left = n.left;
            Node right = n.right;
            size--;
            if (right == null) return left;
            Node min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }

        return balance(n); // Балансировка после удаления
    }

    // Поиск узла по ключу (нерекурсивный)
    private Node getNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
            else return current;
        }
        return null;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        // Удаляем последнюю запятую и пробел
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (в порядке возрастания ключей)
    private void inOrder(Node n, StringBuilder sb) {
        if (n != null) {
            inOrder(n.left, sb);
            sb.append(n.key).append("=").append(n.value).append(", ");
            inOrder(n.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        root = put(root, key, value);
        return old;
    }

    @Override
    public String remove(Object key) {
        Integer k = (Integer) key;
        String old = get(k);
        root = remove(root, k);
        return old;
    }

    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n != null ? n.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(value, n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = new java.util.HashSet<>();
        collectKeys(root, set);
        return set;
    }

    // Сбор всех ключей в множество (симметричный обход)
    private void collectKeys(Node n, Set<Integer> set) {
        if (n != null) {
            collectKeys(n.left, set);
            set.add(n.key);
            collectKeys(n.right, set);
        }
    }

    @Override
    public Collection<String> values() {
        Collection<String> list = new java.util.ArrayList<>();
        collectValues(root, list);
        return list;
    }

    // Сбор всех значений в коллекцию (симметричный обход)
    private void collectValues(Node n, Collection<String> list) {
        if (n != null) {
            collectValues(n.left, list);
            list.add(n.value);
            collectValues(n.right, list);
        }
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        Set<Map.Entry<Integer, String>> set = new java.util.HashSet<>();
        collectEntries(root, set);
        return set;
    }

    // Сбор всех пар ключ-значение (симметричный обход)
    private void collectEntries(Node n, Set<Map.Entry<Integer, String>> set) {
        if (n != null) {
            collectEntries(n.left, set);
            set.add(new java.util.AbstractMap.SimpleImmutableEntry<>(n.key, n.value));
            collectEntries(n.right, set);
        }
    }
}