package by.it.group451002.stsefanovich.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    // Вспомогательные методы для работы с высотой и балансом
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    // Повороты для балансировки
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // Балансировка узла
    private Node balance(Node node) {
        updateHeight(node);
        int balance = balanceFactor(node);

        // Left-Left
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        // Left-Right
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Right-Right
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        // Right-Left
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    // Вставка узла
    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value; // Обновляем значение, если ключ уже существует
        }

        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String oldValue = get(key);
        root = put(root, key, value);
        return oldValue;
    }

    // Поиск минимального узла
    private Node min(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Удаление узла
    private Node remove(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
        } else {
            size--;
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node minNode = min(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = remove(node.right, minNode.key);
            }
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String oldValue = get((Integer) key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
        }
        return oldValue;
    }

    // Поиск значения по ключу
    private String get(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    // Проверка наличия ключа
    private boolean containsKey(Node node, Integer key) {
        if (node == null) {
            return false;
        }
        if (key.compareTo(node.key) < 0) {
            return containsKey(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey(root, (Integer) key);
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

    // Формирование строки в порядке возрастания ключей
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Неподдерживаемые методы интерфейса Map
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
//
    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}