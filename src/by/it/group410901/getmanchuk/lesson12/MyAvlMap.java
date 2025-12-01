package by.it.group410901.getmanchuk.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    // внутренний класс Node хранит одну пару (key, value) и ссылки на левое и правое поддерево

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        Node(Integer k, String v) { key = k; value = v; }
    }

    // корень дерева и количество элементов

    private Node root;
    private int size = 0;

    // Ищет место, куда вставить новый узел.
    //
    // Если ключ уже есть — заменяет значение и возвращает старое.
    //
    // Если ключ новый — создаёт новый Node и вставляет его в левое или правое поддерево.

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                return old;
            }
            cur = cmp < 0 ? cur.left : cur.right;
        }
        Node n = new Node(key, value);
        if (cmp < 0) parent.left = n; else parent.right = n;
        size++;
        return null;
    }

    // идём от корня вниз, сравнивая ключи — стандартный поиск в бинарном дереве

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = root;
        while (n != null) {
            int cmp = ((Integer) key).compareTo(n.key);
            if (cmp == 0) return n.value;
            n = cmp < 0 ? n.left : n.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // удаляет узел с данным ключом, используя рекурсивный метод

    // У узла нет детей → возвращаем null.
    //
    //Один ребёнок → возвращаем его.
    //
    //Два ребёнка → ищем наименьшего потомка справа.

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node dummy = new Node(null, null);
        root = removeRec(root, k, dummy);
        if (dummy.value != null) {
            size--;
            return dummy.value;
        }
        return null;
    }

    private Node removeRec(Node node, Integer key, Node removed) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = removeRec(node.left, key, removed);
        else if (cmp > 0) node.right = removeRec(node.right, key, removed);
        else {
            removed.value = node.value;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node succ = node.right;
            while (succ.left != null) succ = succ.left;
            node.key = succ.key;
            node.value = succ.value;
            node.right = removeRec(node.right, succ.key, new Node(null,null));
        }
        return node;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        List<String> parts = new ArrayList<>();
        inorder(root, parts);
        return "{" + String.join(", ", parts) + "}";
    }

    private void inorder(Node n, List<String> parts) {
        if (n == null) return;
        inorder(n.left, parts);
        parts.add(n.key + "=" + n.value);
        inorder(n.right, parts);
    }

    @Override public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> s = new LinkedHashSet<>();
        fillKeys(root, s);
        return s;
    }

    private void fillKeys(Node n, Set<Integer> s) {
        if (n == null) return;
        fillKeys(n.left, s);
        s.add(n.key);
        fillKeys(n.right, s);
    }

    @Override
    public Collection<String> values() {
        List<String> v = new ArrayList<>();
        fillVals(root, v);
        return v;
    }

    private void fillVals(Node n, List<String> v) {
        if (n == null) return;
        fillVals(n.left, v);
        v.add(n.value);
        fillVals(n.right, v);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer,String>> set = new LinkedHashSet<>();
        fillEntries(root, set);
        return set;
    }

    private void fillEntries(Node n, Set<Entry<Integer,String>> set) {
        if (n == null) return;
        fillEntries(n.left, set);
        set.add(new AbstractMap.SimpleEntry<>(n.key, n.value));
        fillEntries(n.right, set);
    }
}