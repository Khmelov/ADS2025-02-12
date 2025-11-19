package by.it.group451001.khomenkov.lesson12;

import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        RbNode parent;
        boolean color;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode root;
    private int size;
    private final RbNode NIL = new RbNode(null, null, BLACK); // Sentinel node

    public MyRbMap() {
        root = NIL;
        size = 0;
    }

    // ==================== ОСНОВНЫЕ ОПЕРАЦИИ КРАСНО-ЧЕРНОГО ДЕРЕВА ====================

    private void leftRotate(RbNode x) {
        RbNode y = x.right;
        x.right = y.left;

        if (y.left != NIL) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RbNode y) {
        RbNode x = y.left;
        y.left = x.right;

        if (x.right != NIL) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == NIL) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    private void insertFixup(RbNode z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                RbNode y = z.parent.parent.right;
                if (y.color == RED) {
                    // Case 1: Uncle is RED
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        // Case 2: Uncle is BLACK and z is right child
                        z = z.parent;
                        leftRotate(z);
                    }
                    // Case 3: Uncle is BLACK and z is left child
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                // Symmetric cases
                RbNode y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void transplant(RbNode u, RbNode v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private RbNode minimum(RbNode node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }

    private void deleteFixup(RbNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                RbNode w = x.parent.right;
                if (w.color == RED) {
                    // Case 1: Sibling is RED
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    // Case 2: Both siblings children are BLACK
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        // Case 3: Sibling's right child is BLACK
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Case 4: Sibling's right child is RED
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                // Symmetric cases
                RbNode w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // ==================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ SortedMap ====================

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inorderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(RbNode node, StringBuilder sb) {
        if (node != NIL) {
            inorderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inorderToString(node.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        RbNode y = NIL;
        RbNode x = root;

        // Find insertion position
        while (x != NIL) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                // Key already exists - update value
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        // Create new node
        RbNode z = new RbNode(key, value, RED);
        z.parent = y;
        z.left = NIL;
        z.right = NIL;

        if (y == NIL) {
            root = z;
        } else if (key.compareTo(y.key) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }

        size++;
        insertFixup(z);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        RbNode z = findNode((Integer) key);
        if (z == NIL) {
            return null;
        }

        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    private void deleteNode(RbNode z) {
        RbNode y = z;
        RbNode x;
        boolean yOriginalColor = y.color;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            deleteFixup(x);
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        RbNode node = findNode((Integer) key);
        return node == NIL ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(RbNode node, Object value) {
        if (node == NIL) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private RbNode findNode(Integer key) {
        RbNode current = root;
        while (current != NIL) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return NIL;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        RbNode current = root;
        while (current.right != NIL) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        buildHeadMap(root, toKey, result);
        return result;
    }

    private void buildHeadMap(RbNode node, Integer toKey, MyRbMap result) {
        if (node != NIL) {
            buildHeadMap(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            buildHeadMap(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        buildTailMap(root, fromKey, result);
        return result;
    }

    private void buildTailMap(RbNode node, Integer fromKey, MyRbMap result) {
        if (node != NIL) {
            buildTailMap(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            buildTailMap(node.right, fromKey, result);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ==================== НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ====================

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}