package by.it.group410902.kozincev.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        public Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
            this.left = null;
            this.right = null;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        updateHeight(p);
        updateHeight(q);
        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        updateHeight(q);
        updateHeight(p);
        return p;
    }

    private Node balance(Node p) {
        updateHeight(p);

        if (balanceFactor(p) == 2) {
            if (balanceFactor(p.right) < 0) {
                p.right = rotateRight(p.right);
            }
            return rotateLeft(p);
        }


        if (balanceFactor(p) == -2) {
            if (balanceFactor(p.left) > 0) {
                p.left = rotateLeft(p.left);
            }
            return rotateRight(p);
        }

        return p;
    }

    private Node findMin(Node node) {
        return node.left != null ? findMin(node.left) : node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        String[] oldValueContainer = new String[1];
        root = putRecursive(root, key, value, oldValueContainer);
        return oldValueContainer[0];
    }

    private Node putRecursive(Node p, Integer key, String value, String[] oldValueContainer) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(p.key);

        if (cmp < 0) {
            p.left = putRecursive(p.left, key, value, oldValueContainer);
        } else if (cmp > 0) {
            p.right = putRecursive(p.right, key, value, oldValueContainer);
        } else {
            oldValueContainer[0] = p.value;
            p.value = value;
            return p;
        }

        return balance(p);
    }


    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;

        String[] oldValueContainer = new String[1];
        root = removeRecursive(root, (Integer) key, oldValueContainer);
        if (oldValueContainer[0] != null) {
            size--;
        }
        return oldValueContainer[0];
    }

    private Node removeRecursive(Node p, Integer key, String[] oldValueContainer) {
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);

        if (cmp < 0) {
            p.left = removeRecursive(p.left, key, oldValueContainer);
        } else if (cmp > 0) {
            p.right = removeRecursive(p.right, key, oldValueContainer);
        } else {
            oldValueContainer[0] = p.value;

            if (p.left == null && p.right == null) {
                return null;
            }
            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }


            Node min = findMin(p.right);
            p.key = min.key;
            p.value = min.value;
            p.right = removeMin(p.right);
        }

        return balance(p);
    }


    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Node current = root;
        Integer targetKey = (Integer) key;

        while (current != null) {
            int cmp = targetKey.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
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
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left, sb);
        sb.append(node.toString()).append(", ");
        inOrderTraversal(node.right, sb);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented for test");
    }
}