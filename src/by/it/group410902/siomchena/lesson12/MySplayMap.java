package by.it.group410902.siomchena.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { root = null; size = 0; }

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
        root = splay(root, (Integer) key);
        return root != null && root.key.equals(key) ? root.value : null;
    }

    @Override
    public String put(Integer key, String value) { //найти ближайшее значение, поднять, вставить новый корень
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        if (root.key.equals(key)) {
            String old = root.value;
            root.value = value;
            return old;
        }

        Node newNode = new Node(key, value);
        if (key < root.key) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) { //найти удаляемый, поднять, убрать
        if (!(key instanceof Integer)) return null;
        if (root == null) return null;

        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;

        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node newRoot = splay(root.left, (Integer) key);
            newRoot.right = root.right;
            root = newRoot;
        }
        size--;
        return oldValue;
    }

    private Node splay(Node node, Integer key) {
        if (node == null) return null;

        if (key < node.key) {
            if (node.left == null) return node;
            //левый левый
            if (key < node.left.key) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            }
            //левый правый
            else if (key > node.left.key) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null)
                    node.left = rotateLeft(node.left);
            }
            return node.left == null ? node : rotateRight(node);
        }

        else if (key > node.key) {
            if (node.right == null) return node;
            //правый правый
            if (key > node.right.key) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            //правый левый
            else if (key < node.right.key) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null)
                    node.right = rotateRight(node.right);
            }
            return node.right == null ? node : rotateLeft(node);
        }
        return node;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    @Override
    public Integer firstKey() { //поднять минимум в корень
        if (root == null) throw new NoSuchElementException();
        Node min = root;
        while (min.left != null) min = min.left;
        root = splay(root, min.key);
        return min.key;
    }

    @Override
    public Integer lastKey() { //поднять максимум в корень
        if (root == null) throw new NoSuchElementException();
        Node max = root;
        while (max.right != null) max = max.right;
        root = splay(root, max.key);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) { //наиб. ключ меньше кей
        Node node = findLower(root, key, null);
        if (node != null) root = splay(root, node.key);
        return node != null ? node.key : null;
    }

    private Node findLower(Node node, Integer key, Node best) { //наименьш ключбольше кей
        if (node == null) return best;
        if (node.key < key) return findLower(node.right, key, node);
        else return findLower(node.left, key, best);
    }

    @Override
    public Integer floorKey(Integer key) { //наиб ключ меньш=кей
        Node node = findFloor(root, key, null);
        if (node != null) root = splay(root, node.key);
        return node != null ? node.key : null;
    }

    private Node findFloor(Node node, Integer key, Node best) {//наим ключ больш=кей
        if (node == null) return best;
        if (node.key <= key) return findFloor(node.right, key, node);
        else return findFloor(node.left, key, best);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeiling(root, key, null);
        if (node != null) root = splay(root, node.key);
        return node != null ? node.key : null;
    }

    private Node findCeiling(Node node, Integer key, Node best) {
        if (node == null) return best;
        if (node.key >= key) return findCeiling(node.left, key, node);
        else return findCeiling(node.right, key, best);
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigher(root, key, null);
        if (node != null) root = splay(root, node.key);
        return node != null ? node.key : null;
    }

    private Node findHigher(Node node, Integer key, Node best) {
        if (node == null) return best;
        if (node.key > key) return findHigher(node.left, key, node);
        else return findHigher(node.right, key, best);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return new SubMap(null, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return new SubMap(fromKey, null);
    }

    private class SubMap extends AbstractMap<Integer, String> implements SortedMap<Integer, String> {
        final Integer from, to;

        SubMap(Integer from, Integer to) {
            this.from = from;
            this.to = to;
        }

        @Override public int size() {
            int count = 0;
            for (Integer k : keySet()) count++;
            return count;
        }

        @Override public boolean isEmpty() { return !keySet().iterator().hasNext(); }
        @Override public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            Integer k = (Integer) key;
            return inRange(k) && MySplayMap.this.containsKey(k);
        }
        @Override public String get(Object key) {
            return containsKey(key) ? MySplayMap.this.get(key) : null;
        }

        @Override public Integer firstKey() {
            for (Integer k : keySet()) return k;
            throw new NoSuchElementException();
        }

        @Override public Integer lastKey() {
            Integer last = null;
            for (Integer k : keySet()) last = k;
            if (last == null) throw new NoSuchElementException();
            return last;
        }

        @Override public Set<Integer> keySet() {
            Set<Integer> keys = new TreeSet<>();
            addKeys(root, keys);
            return keys;
        }

        private void addKeys(Node node, Set<Integer> keys) {
            if (node == null) return;
            addKeys(node.left, keys);
            if (inRange(node.key)) keys.add(node.key);
            addKeys(node.right, keys);
        }

        private boolean inRange(Integer key) {
            return (from == null || key >= from) && (to == null || key < to);
        }

        @Override public String put(Integer key, String value) { throw new UnsupportedOperationException(); }
        @Override public String remove(Object key) { throw new UnsupportedOperationException(); }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
        @Override public void clear() { throw new UnsupportedOperationException(); }
        @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
        @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
        @Override public Comparator<? super Integer> comparator() { return null; }
        @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
        @Override public SortedMap<Integer, String> headMap(Integer toKey) { throw new UnsupportedOperationException(); }
        @Override public SortedMap<Integer, String> tailMap(Integer fromKey) { throw new UnsupportedOperationException(); }
        @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
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

    @Override public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}