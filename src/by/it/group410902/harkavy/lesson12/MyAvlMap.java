package by.it.group410902.harkavy.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // узел АВЛ-дерева
    private static class Node {
        int key;
        String val;
        int height;      // высота поддерева
        Node left;
        Node right;
        Node(int key, String val) {
            this.key = key;
            this.val = val;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    // --------- базовые утилиты АВЛ ---------

    private int h(Node x) { return x == null ? 0 : x.height; }

    private int bf(Node x) { return x == null ? 0 : h(x.left) - h(x.right); } // баланс-фактор

    private void upd(Node x) { x.height = Math.max(h(x.left), h(x.right)) + 1; }

    // правый поворот вокруг y
    private Node rotRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        upd(y);
        upd(x);
        return x;
    }

    // левый поворот вокруг x
    private Node rotLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        upd(x);
        upd(y);
        return y;
    }

    // восстановление баланса
    private Node rebalance(Node x) {
        upd(x);
        int b = bf(x);
        if (b > 1) {                     // левое тяжёлое
            if (bf(x.left) < 0)          // большая правая у левого — сначала поворот влево
                x.left = rotLeft(x.left);
            return rotRight(x);          // затем вправо
        }
        if (b < -1) {                    // правое тяжёлое
            if (bf(x.right) > 0)         // большая левая у правого — сначала вправо
                x.right = rotRight(x.right);
            return rotLeft(x);           // затем влево
        }
        return x;
    }

    // --------- вставка/обновление ---------

    private static class PutResult {
        Node node;
        String prev; // предыдущее значение (если ключ существовал)
        boolean inserted; // true если был новый ключ
    }

    private PutResult putRec(Node x, int key, String val) {
        if (x == null) {
            PutResult r = new PutResult();
            r.node = new Node(key, val);
            r.inserted = true;
            return r;
        }
        PutResult r;
        if (key < x.key) {
            r = putRec(x.left, key, val);
            x.left = r.node;
        } else if (key > x.key) {
            r = putRec(x.right, key, val);
            x.right = r.node;
        } else {
            // ключ уже есть — просто меняем значение
            r = new PutResult();
            r.prev = x.val;
            x.val = val;
            r.node = x;
            r.inserted = false;
            return r;
        }
        x = rebalance(x);
        r.node = x;
        return r;
    }

    // --------- удаление ---------

    private static class RemoveResult {
        Node node;
        String prev; // удалённое значение или null, если ключа не было
        boolean removed;
    }

    private Node minNode(Node x) { while (x.left != null) x = x.left; return x; }

    private Node removeMin(Node x) {
        if (x.left == null) return x.right;
        x.left = removeMin(x.left);
        return rebalance(x);
    }

    private RemoveResult removeRec(Node x, int key) {
        RemoveResult r = new RemoveResult();
        if (x == null) { r.node = null; return r; }

        if (key < x.key) {
            r = removeRec(x.left, key);
            x.left = r.node;
            if (r.removed) r.node = rebalance(x);
            else r.node = x;
            return r;
        } else if (key > x.key) {
            r = removeRec(x.right, key);
            x.right = r.node;
            if (r.removed) r.node = rebalance(x);
            else r.node = x;
            return r;
        } else {
            // нашли узел
            r.prev = x.val;
            r.removed = true;
            if (x.right == null) { r.node = x.left; return r; }
            if (x.left == null)  { r.node = x.right; return r; }
            // два ребёнка: берём минимальный в правом поддереве
            Node min = minNode(x.right);
            x.key = min.key;
            x.val = min.val;
            x.right = removeMin(x.right);   // удаляем перенесённый узел
            r.node = rebalance(x);          // балансируем
            return r;
        }
    }

    // --------- поиск ---------

    private Node find(Node x, int key) {
        while (x != null) {
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else return x;
        }
        return null;
    }

    // ===================== обязательные методы =====================

    @Override
    public String toString() {
        // симметричный обход: ключи по возрастанию
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(sb, root, true);
        return sb.append("}").toString();
    }

    private void appendInOrder(StringBuilder sb, Node x, boolean firstHolder) {
        // firstHolder: используем sb.length() чтобы понимать, ставить ли запятую
        if (x == null) return;
        appendInOrder(sb, x.left, firstHolder);
        if (sb.length() > 1) sb.append(", "); // после первой фигурной скобки
        sb.append(x.key).append("=").append(x.val);
        appendInOrder(sb, x.right, firstHolder);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        PutResult r = putRec(root, key, value);
        root = r.node;
        if (r.inserted) size++;
        return r.prev; // null если ключа не было
    }

    @Override
    public String remove(Object key) {
        if (key == null) return null;
        if (!(key instanceof Integer)) return null;
        RemoveResult r = removeRec(root, (Integer) key);
        root = r.node;
        if (r.removed) size--;
        return r.prev; // null если не было
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Node n = find(root, (Integer) key);
        return n == null ? null : n.val;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof Integer)) return false;
        return find(root, (Integer) key) != null;
    }

    @Override public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override public boolean isEmpty() { return size == 0; }

    // ===================== остальное не требуется =====================

    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }

    @Override public boolean containsValue(Object value) { unsupported(); return false; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { unsupported(); }
    @Override public Set<Integer> keySet() { unsupported(); return null; }
    @Override public Collection<String> values() { unsupported(); return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { unsupported(); return null; }
}
