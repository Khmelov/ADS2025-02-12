package by.it.group451001.romeyko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
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

    private Node rebalance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        // Right heavy
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Null keys not supported");
        Holder prev = new Holder();
        root = putRec(root, key, value, prev);
        if (prev.found == false) size++;
        return prev.oldValue;
    }

    private static class Holder {
        boolean found = false;
        String oldValue = null;
    }

    private Node putRec(Node node, int key, String value, Holder prev) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = putRec(node.left, key, value, prev);
        } else if (key > node.key) {
            node.right = putRec(node.right, key, value, prev);
        } else {
            prev.found = true;
            prev.oldValue = node.value;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    // get / containsKey

    @Override
    public String get(Object keyObj) {
        if (keyObj == null) return null;
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (keyObj == null || !(keyObj instanceof Integer)) return false;
        int key = (Integer) keyObj;
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return true;
        }
        return false;
    }

    // remove (удаление по ключу)

    @Override
    public String remove(Object keyObj) {
        if (keyObj == null || !(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        HolderOld old = new HolderOld();
        root = removeRec(root, key, old);
        if (old.found) size--;
        return old.oldValue;
    }

    private static class HolderOld {
        boolean found = false;
        String oldValue = null;
    }

    private Node removeRec(Node node, int key, HolderOld old) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = removeRec(node.left, key, old);
        } else if (key > node.key) {
            node.right = removeRec(node.right, key, old);
        } else {
            old.found = true;
            old.oldValue = node.value;
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            else {
                Node succ = node.right;
                while (succ.left != null) succ = succ.left;
                node.key = succ.key;
                node.value = succ.value;
                node.right = removeRec(node.right, succ.key, new HolderOld()); // здесь не важно старое значение
            }
        }
        return rebalance(node);
    }

    // toString (in-order)

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        InOrderCollector c = new InOrderCollector();
        inOrder(root, c);
        for (int i = 0; i < c.count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(c.keys[i]).append("=").append(c.values[i]);
        }
        sb.append('}');
        return sb.toString();
    }

    private static class InOrderCollector {
        int[] keys = new int[16];
        String[] values = new String[16];
        int count = 0;
        void add(int k, String v) {
            if (count == keys.length) {
                int newCap = keys.length * 2;
                int[] nk = new int[newCap];
                String[] nv = new String[newCap];
                System.arraycopy(keys, 0, nk, 0, keys.length);
                System.arraycopy(values, 0, nv, 0, values.length);
                keys = nk;
                values = nv;
            }
            keys[count] = k;
            values[count] = v;
            count++;
        }
    }

    private void inOrder(Node node, InOrderCollector c) {
        if (node == null) return;
        inOrder(node.left, c);
        c.add(node.key, node.value);
        inOrder(node.right, c);
    }

    // size, clear, isEmpty

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

    // методы Map, которые не требуются
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { throw new UnsupportedOperationException(); }
    @Override public String putIfAbsent(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
    @Override public boolean replace(Integer key, String oldValue, String newValue) { throw new UnsupportedOperationException(); }
    @Override public String replace(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public String getOrDefault(Object key, String defaultValue) { throw new UnsupportedOperationException(); }
    @Override public void forEach(java.util.function.BiConsumer<? super Integer, ? super String> action) { throw new UnsupportedOperationException(); }
    @Override public void replaceAll(java.util.function.BiFunction<? super Integer, ? super String, ? extends String> function) { throw new UnsupportedOperationException(); }
    @Override public String computeIfAbsent(Integer key, java.util.function.Function<? super Integer, ? extends String> mappingFunction) { throw new UnsupportedOperationException(); }
    @Override public String computeIfPresent(Integer key, java.util.function.BiFunction<? super Integer, ? super String, ? extends String> remappingFunction) { throw new UnsupportedOperationException(); }
    @Override public String compute(Integer key, java.util.function.BiFunction<? super Integer, ? super String, ? extends String> remappingFunction) { throw new UnsupportedOperationException(); }
    @Override public String merge(Integer key, String value, java.util.function.BiFunction<? super String, ? super String, ? extends String> remappingFunction) { throw new UnsupportedOperationException(); }
}
