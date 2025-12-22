package by.it.group410902.kukhto.les12;
import java.util.*;
class MyAvlMap implements java.util.Map<Integer, String> {
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

    public String toString() {
        if (root == null) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        java.util.Stack<Node> stack = new java.util.Stack<>();
        Node current = root;
        boolean first = true;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.key).append("=").append(current.value);
            first = false;

            current = current.right;
        }

        sb.append("}");
        return sb.toString();
    }
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public String put(Integer key, String value) {
        if (key == null) {
            return null;
        }

        Node[] result = new Node[1];
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue, result);
        if (oldValue[0] == null && result[0] != null) {
            size++;
        }
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue, Node[] result) {
        if (node == null) {
            result[0] = new Node(key, value);
            return result[0];
        }
          //дошли до места вставки
        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue, result);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue, result);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            result[0] = node;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;

        String[] removedValue = new String[1];
        root = remove(root, k, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = remove(node.left, key, removedValue);
        } else if (key > node.key) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;
            //узел без потомков
            if (node.left == null && node.right == null) {
                return null;
                //узел с одним потомком
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                //узел с двумя потомками
                Node successor = findMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = removeMin(node.right);
            }
        }

        updateHeight(node);
        return balance(node);
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        updateHeight(node);
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;

        Node current = root;
        while (current != null) {
            if (k < current.key) {
                current = current.left;
            } else if (k > current.key) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }




    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public java.util.Set<Integer> keySet() {
        return java.util.Collections.emptySet();
    }

    public java.util.Collection<String> values() {
        return java.util.Collections.emptyList();
    }

    public java.util.Set<Entry<Integer, String>> entrySet() {
        return java.util.Collections.emptySet();
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node balance(Node node) {
        int balanceFactor = balanceFactor(node);

        if (balanceFactor > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
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
}