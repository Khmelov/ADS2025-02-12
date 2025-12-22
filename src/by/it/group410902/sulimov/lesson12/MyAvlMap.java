package by.it.group410902.sulimov.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

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

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // Вспомогательные методы для АВЛ-дерева
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

    private Node balance(Node node) {
        if (node == null) return null;

        updateHeight(node);
        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }

        return balance(node);
    }

    private Node minValueNode(Node node) {
        if (node == null) return null;
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                Node temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key);
            }
        }

        if (node == null) return null;

        return balance(node);
    }

    private String get(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    private boolean containsKey(Node node, Integer key) {
        if (node == null) return false;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            toString(node.right, sb);
        }
    }

    // Обязательные методы
    @Override
    public String toString() {
        if (size == 0) return "{}";

        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        String oldValue = get(key);
        root = put(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;

        Integer k = (Integer) key;
        String oldValue = get(k);
        root = remove(root, k);
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;

        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return false;

        return containsKey(root, (Integer) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, (String) value);
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

    // Остальные методы интерфейса Map - не реализованы
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}