package by.it.group410901.zubchonak.lesson12;

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

    private Node splay(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // Ключ в левом поддереве
            if (node.left == null) return node;

            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                // Zig-Zig: два левых поворота
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                // Zig-Zag: левый-правый
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null)
                    node.left = rotateLeft(node.left);
            }
            return node.left == null ? node : rotateRight(node);
        } else if (cmp > 0) {
            if (node.right == null) return node;

            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                // Zag-Zig: правый-левый
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null)
                    node.right = rotateRight(node.right);
            } else if (cmp2 > 0) {
                // Zag-Zag: два правых поворота
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return node.right == null ? node : rotateLeft(node);
        } else {
            return node;
        }
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        root = splay(root, (Integer) key);
        return root != null && root.key.equals(key) ? root.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);
        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            // // Нашли существующий ключ - обновляем значение
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        } else if (cmp < 0) {
            //Вставляем слева от корня
            Node newNode = new Node(key, value);
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            root = newNode;
            size++;
            return null;
        } else {
            // Вставляем слева от корня
            Node newNode = new Node(key, value);
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            root = newNode;
            size++;
            return null;
        }
    }

    @Override
    public String remove(Object key) {
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderKeys(root, keys);
        return keys;
    }

    private void inOrderKeys(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderKeys(node.left, keys);
            keys.add(node.key);
            inOrderKeys(node.right, keys);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValues(root, values);
        return values;
    }

    private void inOrderValues(Node node, List<String> values) {
        if (node != null) {
            inOrderValues(node.left, values);
            values.add(node.value);
            inOrderValues(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparingInt(Entry::getKey));
        inOrderEntries(root, entries);
        return entries;
    }

    private void inOrderEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            inOrderEntries(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderEntries(node.right, entries);
        }
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        Integer lowerKey = lowerKey(key);
        return lowerKey == null ? null : new AbstractMap.SimpleEntry<>(lowerKey, get(lowerKey));
    }

    @Override
    public Integer lowerKey(Integer key) {
        return findExtremeKey(root, key, false, false);
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Integer floorKey = floorKey(key);
        return floorKey == null ? null : new AbstractMap.SimpleEntry<>(floorKey, get(floorKey));
    }

    @Override
    public Integer floorKey(Integer key) {
        return findExtremeKey(root, key, true, false);
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Integer ceilingKey = ceilingKey(key);
        return ceilingKey == null ? null : new AbstractMap.SimpleEntry<>(ceilingKey, get(ceilingKey));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findExtremeKey(root, key, true, true);
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Integer higherKey = higherKey(key);
        return higherKey == null ? null : new AbstractMap.SimpleEntry<>(higherKey, get(higherKey));
    }

    @Override
    public Integer higherKey(Integer key) {
        return findExtremeKey(root, key, false, true);
    }

    private Integer findExtremeKey(Node node, Integer key, boolean allowEqual, boolean findHigher) {
        if (node == null) return null;

        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);

            if ((findHigher && cmp < 0) || (!findHigher && cmp > 0)) {
                result = node.key;
                node = findHigher ? node.left : node.right;
            } else if (allowEqual && cmp == 0) {
                return node.key;
            } else {
                node = findHigher ? node.right : node.left;
            }
        }
        return result;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        Integer firstKey = firstKey();
        return firstKey == null ? null : new AbstractMap.SimpleEntry<>(firstKey, get(firstKey));
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        Integer lastKey = lastKey();
        return lastKey == null ? null : new AbstractMap.SimpleEntry<>(lastKey, get(lastKey));
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Integer firstKey = firstKey();
        if (firstKey == null) return null;

        String value = get(firstKey);
        remove(firstKey);
        return new AbstractMap.SimpleEntry<>(firstKey, value);
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Integer lastKey = lastKey();
        if (lastKey == null) return null;

        String value = get(lastKey);
        remove(lastKey);
        return new AbstractMap.SimpleEntry<>(lastKey, value);
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        addInRange(root, fromKey, toKey, subMap);
        return subMap;
    }

    private void addInRange(Node node, Integer fromKey, Integer toKey, MySplayMap subMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) > 0) {
            addInRange(node.left, fromKey, toKey, subMap);
        }
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0) {
            addInRange(node.right, fromKey, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return subMap(firstKey(), toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap tailMap = new MySplayMap();
        addFromKey(root, fromKey, tailMap);
        return tailMap;
    }

    private void addFromKey(Node node, Integer fromKey, MySplayMap tailMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            addFromKey(node.left, fromKey, tailMap);
            tailMap.put(node.key, node.value);
        }
        addFromKey(node.right, fromKey, tailMap);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
    }
}