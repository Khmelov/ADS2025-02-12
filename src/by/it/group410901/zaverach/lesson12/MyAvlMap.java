package by.it.group410901.zaverach.lesson12;


import java.util.Map;
import java.util.Set;
import java.util.Collection;


public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        if (n != null) n.height = 1 + Math.max(height(n.left), height(n.right));
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

    private Node rebalance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("MyAvlMap does not support null keys");
        Holder old = new Holder();
        root = insert(root, key, value, old);
        if (!old.present) size++;
        return old.value;
    }

    private static class Holder {
        String value = null;
        boolean present = false;
    }

    private Node insert(Node node, int key, String value, Holder old) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value, old);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, old);
        } else {
            old.value = node.value;
            old.present = true;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null; //instanceof возвращает true,
        int key = (Integer) keyObj;                    // если объект является экземпляром указанного типа,
        Node cur = root;                               // и false в противном случае
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return get(keyObj) != null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        Holder old = new Holder();
        root = delete(root, key, old);
        if (old.present) {
            size--;
            return old.value;
        } else {
            return null;
        }
    }

    private Node minValueNode(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private Node delete(Node node, int key, Holder old) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key, old);
        } else if (key > node.key) {
            node.right = delete(node.right, key, old);
        } else {
            old.present = true;
            old.value = node.value;
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node succ = minValueNode(node.right);
                node.key = succ.key;
                node.value = succ.value;
                node.right = delete(node.right, succ.key, new Holder());
            }
        }

        if (node == null) return null;
        return rebalance(node);
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
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (root != null) {
            ToStringState st = new ToStringState();
            inorderToString(root, sb, st);
        }
        sb.append("}");
        return sb.toString();
    }

    private static class ToStringState { boolean first = true; }

    private void inorderToString(Node node, StringBuilder sb, ToStringState st) {
        if (node == null) return;
        inorderToString(node.left, sb, st);
        if (!st.first) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        st.first = false;
        inorderToString(node.right, sb, st);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue is not supported");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll is not supported");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet is not supported");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values is not supported");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet is not supported");
    }
}
