package by.it.group410901.getmanchuk.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    // ===== Узел дерева =====
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        Node(Integer k, String v) { key = k; value = v; }
    }

    private Node root;
    private int size = 0;

    // ===== Вспомогательные методы =====
    private Node findMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node findMax(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    // ===== Основные методы SortedMap =====
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
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object v) {
        if (n == null) return false;
        if (Objects.equals(n.value, v)) return true;
        return containsValueRec(n.left, v) || containsValueRec(n.right, v);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node n = root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key==null");
        Node[] res = insert(root, key, value);
        root = res[0];
        Node existed = res[1];
        if (existed == null) size++;
        return existed == null ? null : existed.value;
    }

    private Node[] insert(Node n, Integer k, String v) {
        if (n == null) return new Node[]{new Node(k, v), null};
        if (k.compareTo(n.key) < 0) {
            Node[] res = insert(n.left, k, v);
            n.left = res[0];
            return new Node[]{n, res[1]};
        } else if (k.compareTo(n.key) > 0) {
            Node[] res = insert(n.right, k, v);
            n.right = res[0];
            return new Node[]{n, res[1]};
        } else {
            Node existed = new Node(n.key, n.value);
            n.value = v;
            return new Node[]{n, existed};
        }
    }

    // исправленное удаление — теперь возвращает правильное значение
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node[] res = delete(root, k);
        root = res[0];
        Node removed = res[1];
        if (removed != null) size--;
        return removed == null ? null : removed.value;
    }

    private Node[] delete(Node n, Integer k) {
        if (n == null) return new Node[]{null, null};
        Node removed = null;
        if (k.compareTo(n.key) < 0) {
            Node[] res = delete(n.left, k);
            n.left = res[0];
            removed = res[1];
        } else if (k.compareTo(n.key) > 0) {
            Node[] res = delete(n.right, k);
            n.right = res[0];
            removed = res[1];
        } else {
            removed = new Node(n.key, n.value); // запоминаем правильный удалённый
            if (n.left == null) return new Node[]{n.right, removed};
            if (n.right == null) return new Node[]{n.left, removed};
            Node min = findMin(n.right);
            n.key = min.key;
            n.value = min.value;
            Node[] res = delete(n.right, min.key);
            n.right = res[0];
        }
        return new Node[]{n, removed};
    }

    // ===== Минимальный и максимальный ключи =====
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return findMax(root).key;
    }

    // ===== Подкарты =====
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillSub(root, sub, k -> k < toKey);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillSub(root, sub, k -> k >= fromKey);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillSub(root, sub, k -> k >= fromKey && k < toKey);
        return sub;
    }

    private void fillSub(Node n, MyRbMap sub, java.util.function.Predicate<Integer> f) {
        if (n == null) return;
        fillSub(n.left, sub, f);
        if (f.test(n.key)) sub.put(n.key, n.value);
        fillSub(n.right, sub, f);
    }

    // ===== Вывод =====
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        printInOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void printInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        printInOrder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        printInOrder(n.right, sb);
    }

    // ===== Неиспользуемые методы интерфейса =====
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { for (var e : m.entrySet()) put(e.getKey(), e.getValue()); }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
}