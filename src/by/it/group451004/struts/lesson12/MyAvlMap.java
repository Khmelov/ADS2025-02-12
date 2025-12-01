package by.it.group451004.struts.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    public static class Node<Integer, String> {
        Integer key;
        String value;
        Node<Integer, String> left;
        Node<Integer, String> right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private int size = 0;
    private Node<Integer, String> root;

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);

        root = insert(root, key, value);
        if (oldValue == null)
            size++;

        return oldValue;
    }

    @Override
    public String remove(Object key) {
        String value = get(key);
        if (value != null) {
            root = removeRecursively(root, key);
            size--;
        }
        return value;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public String get(Object key) {
        Node<Integer, String> current = root;
        while (current != null) {
            if (key.equals(current.key)) {
                return current.value;
            } else if ((Integer) key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private Node<Integer, String> removeRecursively(Node<Integer, String> node, Object key) {
        if (node == null) {
            return null;
        }

        if (key.equals(node.key)) {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node<Integer, String> minLargerNode = getMin(node.right);
                node.key = minLargerNode.key;
                node.value = minLargerNode.value;
                node.right = removeRecursively(node.right, minLargerNode.key);
            }
        } else if ((Integer) key < node.key) {
            node.left = removeRecursively(node.left, key);
        } else {
            node.right = removeRecursively(node.right, key);
        }
        return node;
    }
    private Node<Integer, String> getMin(Node<Integer, String> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        stringifyNode(sb, root);
        if (sb.length() > 2)
            sb.replace(sb.length() - 2, sb.length(), "}");
        else
            sb.append("}");

        return sb.toString();
    }

    private Node<Integer, String> insert(Node<Integer, String> node, Integer key, String value) {
        if (node == null)
            return new Node<>(key, value);

        if (key < node.key)
            node.left = insert(node.left, key, value);
        else if (key > node.key)
            node.right = insert(node.right, key, value);
        else
            node.value = value;

        return node;
    }

    private void stringifyNode(StringBuilder sb, Node<Integer, String> node) {
        if (node != null) {
            stringifyNode(sb, node.left);
            sb.append(node.key).append("=").append(node.value).append(", ");
            stringifyNode(sb, node.right);
        }
    }
}