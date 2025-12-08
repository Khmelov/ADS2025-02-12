package by.it.group410902.derzhavskaya_e.lesson12;

// Реализация Map<Integer, String> на основе АВЛ-дерева
public class MyAvlMap implements java.util.Map<Integer, String> {

    // Узел дерева
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
    public boolean containsKey(Object key) {
        return get((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        Node node = root;
        while (node != null) {
            int cmp = ((Integer) key).compareTo(node.key);
            if (cmp == 0)
                return node.value;
            else if (cmp < 0)
                node = node.left;
            else
                node = node.right;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = insert(root, key, value, oldValue);
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        String[] removedValue = new String[1];
        root = delete(root, (Integer) key, removedValue);
        return removedValue[0];
    }


    private Node insert(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = insert(node.left, key, value, oldValue);
        else if (cmp > 0)
            node.right = insert(node.right, key, value, oldValue);
        else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    private Node delete(Node node, Integer key, String[] removedValue) {
        if (node == null)
            return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = delete(node.left, key, removedValue);
        else if (cmp > 0)
            node.right = delete(node.right, key, removedValue);
        else {
            removedValue[0] = node.value;
            size--;
            // Удаление узла
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key, new String[1]);
        }

        updateHeight(node);
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }


    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node balance(Node node) {
        int bf = balanceFactor(node);
        if (bf == 2) {
            if (balanceFactor(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        if (bf == -2) {
            if (balanceFactor(node.left) > 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        return node;
    }

    private Node rotateLeft(Node a) {
        Node b = a.right;
        a.right = b.left;
        b.left = a;
        updateHeight(a);
        updateHeight(b);
        return b;
    }

    private Node rotateRight(Node a) {
        Node b = a.left;
        a.left = b.right;
        b.right = a;
        updateHeight(a);
        updateHeight(b);
        return b;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1)
            sb.setLength(sb.length() - 2); // убираем последнюю запятую и пробел
        return sb.append("}").toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    @Override public boolean containsValue(Object value) { return false; }
    @Override public void putAll(java.util.Map<? extends Integer, ? extends String> m) {}
    @Override public java.util.Set<Integer> keySet() { return null; }
    @Override public java.util.Collection<String> values() { return null; }
    @Override public java.util.Set<Entry<Integer, String>> entrySet() { return null; }
}
