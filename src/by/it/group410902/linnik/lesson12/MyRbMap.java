package by.it.group410902.linnik.lesson12;

import java.util.*;
/*Каждый узел либо красный, либо черный

Корень всегда черный

Все листья (NIL) черные

У красного узла оба потомка черные (нет двух красных узлов подряд)

Все пути от узла до листьев содержат одинаковое количество черных узлов*/
public class MyRbMap implements SortedMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean isRed;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.isRed = true;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    private String get(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        if (cmp > 0) return get(node.right, key);
        return node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.isRed = false;
        if (oldValue[0] == null) size++;
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    private boolean isRed(Node node) {
        return node != null && node.isRed;
    }

    private Node rotateLeft(Node h) { //когда правый потомок красный, а левый черный
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.isRed = h.isRed;
        h.isRed = true;
        return x;
    }

    private Node rotateRight(Node h) { //когда левый потомок и его левый потомок красные
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.isRed = h.isRed;
        h.isRed = true;
        return x;
    }

    private void flipColors(Node h) { //когда оба потомка красные
        h.isRed = !h.isRed;
        if (h.left != null) h.left.isRed = !h.left.isRed;
        if (h.right != null) h.right.isRed = !h.right.isRed;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String value = get(key);
        if (value != null) {
            root = remove(root, (Integer) key);
            if (root != null) root.isRed = false;
            size--;
        }
        return value;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

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

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) { //меньше кей
        HeadMap map = new HeadMap(toKey);
        buildHeadMap(root, toKey, map);
        return map;
    }

    private void buildHeadMap(Node node, Integer toKey, HeadMap map) {
        if (node == null) return;
        buildHeadMap(node.left, toKey, map);
        if (node.key < toKey) {
            map.keys.add(node.key);
            map.values.add(node.value);
        }
        buildHeadMap(node.right, toKey, map);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { //больше= кей
        TailMap map = new TailMap(fromKey);
        buildTailMap(root, fromKey, map);
        return map;
    }

    private void buildTailMap(Node node, Integer fromKey, TailMap map) {
        if (node == null) return;
        buildTailMap(node.left, fromKey, map);
        if (node.key >= fromKey) {
            map.keys.add(node.key);
            map.values.add(node.value);
        }
        buildTailMap(node.right, fromKey, map);
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";

        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }


    private class HeadMap implements SortedMap<Integer, String> {
        List<Integer> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        Integer toKey;

        HeadMap(Integer toKey) {
            this.toKey = toKey;
        }

        @Override public int size() { return keys.size(); }
        @Override public boolean isEmpty() { return keys.isEmpty(); }

        @Override
        public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            Integer k = (Integer) key;
            return k < toKey && keys.contains(k);
        }

        @Override
        public String get(Object key) {
            if (!containsKey(key)) return null;
            int index = keys.indexOf(key);
            return values.get(index);
        }

        @Override public Integer firstKey() { return keys.get(0); }
        @Override public Integer lastKey() { return keys.get(keys.size() - 1); }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            HeadMap map = new HeadMap(toKey);
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) < toKey) {
                    map.keys.add(keys.get(i));
                    map.values.add(values.get(i));
                }
            }
            return map;
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            TailMap map = new TailMap(fromKey);
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) >= fromKey) {
                    map.keys.add(keys.get(i));
                    map.values.add(values.get(i));
                }
            }
            return map;
        }

        // Остальные методы - заглушки
        @Override public String put(Integer key, String value) { throw new UnsupportedOperationException(); }
        @Override public String remove(Object key) { throw new UnsupportedOperationException(); }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
        @Override public void clear() { throw new UnsupportedOperationException(); }
        @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
        @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
        @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
        @Override public Comparator<? super Integer> comparator() { return null; }
        @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
        @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    }

    private class TailMap implements SortedMap<Integer, String> {
        List<Integer> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        Integer fromKey;

        TailMap(Integer fromKey) {
            this.fromKey = fromKey;
        }

        @Override public int size() { return keys.size(); }
        @Override public boolean isEmpty() { return keys.isEmpty(); }

        @Override
        public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            Integer k = (Integer) key;
            return k >= fromKey && keys.contains(k);
        }

        @Override
        public String get(Object key) {
            if (!containsKey(key)) return null;
            int index = keys.indexOf(key);
            return values.get(index);
        }

        @Override public Integer firstKey() { return keys.get(0); }
        @Override public Integer lastKey() { return keys.get(keys.size() - 1); }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            HeadMap map = new HeadMap(toKey);
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) < toKey) {
                    map.keys.add(keys.get(i));
                    map.values.add(values.get(i));
                }
            }
            return map;
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            TailMap map = new TailMap(fromKey);
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) >= fromKey) {
                    map.keys.add(keys.get(i));
                    map.values.add(values.get(i));
                }
            }
            return map;
        }

        // Остальные методы - заглушки
        @Override public String put(Integer key, String value) { throw new UnsupportedOperationException(); }
        @Override public String remove(Object key) { throw new UnsupportedOperationException(); }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
        @Override public void clear() { throw new UnsupportedOperationException(); }
        @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
        @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
        @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
        @Override public Comparator<? super Integer> comparator() { return null; }
        @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
        @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    }

    // Оставшиеся методы интерфейса
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}