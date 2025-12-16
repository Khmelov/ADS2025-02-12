package by.it.group410902.plekhova.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/*Создайте class MyAvlMap, который реализует интерфейс Map<Integer, String>
    и работает на основе АВЛ-дерева

 */
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
    private int size = 0;

    public MyAvlMap() {
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        boolean first[] = new boolean[]{true};
        inorderToString(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inorderToString(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        inorderToString(node.left, sb, first);
        if (!first[0]) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        first[0] = false;
        inorderToString(node.right, sb, first);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null keys not supported");
        ResultHolder holder = new ResultHolder();
        root = insert(root, key, value, holder);
        if (holder.added) size++;
        return holder.old;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        ResultHolder holder = new ResultHolder();
        root = delete(root, key, holder);
        if (holder.removed) size--;
        return holder.old;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.value;
            if (cmp < 0) cur = cur.left; else cur = cur.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return get(keyObj) != null;
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



    private static class ResultHolder {
        String old = null;
        boolean added = false;
        boolean removed = false;
    }

    private Node insert(Node node, Integer key, String value, ResultHolder holder) {
        if (node == null) {
            holder.added = true;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            holder.old = node.value;
            node.value = value;
            return node;
        } else if (cmp < 0) {
            node.left = insert(node.left, key, value, holder);
        } else {
            node.right = insert(node.right, key, value, holder);
        }
        updateHeight(node);
        return balance(node);
    }

    private Node delete(Node node, Integer key, ResultHolder holder) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key, holder);
        } else if (cmp > 0) {
            node.right = delete(node.right, key, holder);
        } else {
            // found
            holder.old = node.value;
            holder.removed = true;
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // two children: replace with inorder successor
                Node successor = minNode(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = delete(node.right, successor.key, new ResultHolder());
            }
        }
        updateHeight(node);
        return balance(node);
    }

    private Node minNode(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
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
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }



    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode not implemented");
    }
}

