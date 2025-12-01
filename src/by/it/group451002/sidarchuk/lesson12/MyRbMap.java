package by.it.group451002.sidarchuk.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        this.size = 0;
    }

    // Основные операции

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String oldValue = get(key);
        root = put(root, key, value);
        if (root != null) {
            root.color = BLACK;
        }
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            if (node.right != null) node.right.parent = node;
        } else {
            node.value = value;
        }

        // Балансировка
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null || root == null) {
            return null;
        }

        String oldValue = get(key);
        if (oldValue != null) {
            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }
            root = remove(root, key);
            if (root != null) {
                root.color = BLACK;
            }
            size--;
        }
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }
            if (node.right != null && !isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                Node min = min(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = remove(node.right, key);
            }
        }
        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node == null) return null;
        if (node.left == null) {
            return null;
        }
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node min(Node node) {
        if (node == null) return null;
        if (node.left == null) return node;
        return min(node.left);
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (node.right != null && isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (node.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (node == null) return null;

        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get((Integer) key);
    }

    public String get(Integer key) {
        Node node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node get(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return get((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Вспомогательные методы для балансировки

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        if (right.left != null) right.left.parent = node;
        right.left = node;
        right.parent = node.parent;
        node.parent = right;
        right.color = node.color;
        node.color = RED;
        return right;
    }

    private Node rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        if (left.right != null) left.right.parent = node;
        left.right = node;
        left.parent = node.parent;
        node.parent = left;
        left.color = node.color;
        node.color = RED;
        return left;
    }

    private void flipColors(Node node) {
        if (node != null) {
            node.color = !node.color;
            if (node.left != null) node.left.color = !node.left.color;
            if (node.right != null) node.right.color = !node.right.color;
        }
    }

    // Методы интерфейса Map

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
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

    // Методы SortedMap

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node minNode = min(root);
        return minNode != null ? minNode.key : null;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node maxNode = max(root);
        return maxNode != null ? maxNode.key : null;
    }

    private Node max(Node node) {
        if (node == null) return null;
        if (node.right == null) return node;
        return max(node.right);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return new SubMap(null, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return new SubMap(fromKey, null);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null || fromKey.compareTo(toKey) >= 0) {
            throw new IllegalArgumentException();
        }
        return new SubMap(fromKey, toKey);
    }

    // Внутренний класс для представления подкарт
    private class SubMap implements SortedMap<Integer, String> {
        private final Integer fromKey;
        private final Integer toKey;

        SubMap(Integer fromKey, Integer toKey) {
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        @Override
        public String put(Integer key, String value) {
            checkRange(key);
            return MyRbMap.this.put(key, value);
        }

        @Override
        public String get(Object key) {
            if (!(key instanceof Integer)) return null;
            Integer k = (Integer) key;
            return containsKey(k) ? MyRbMap.this.get(k) : null;
        }

        @Override
        public String remove(Object key) {
            if (!(key instanceof Integer)) return null;
            Integer k = (Integer) key;
            return containsKey(k) ? MyRbMap.this.remove(k) : null;
        }

        @Override
        public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            Integer k = (Integer) key;
            return inRange(k) && MyRbMap.this.containsKey(k);
        }

        @Override
        public boolean containsValue(Object value) {
            for (Entry<Integer, String> entry : entrySet()) {
                if (value == null ? entry.getValue() == null : value.equals(entry.getValue())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Set<Entry<Integer, String>> entrySet() {
            Set<Entry<Integer, String>> set = new HashSet<>();
            addEntries(root, set);
            return set;
        }

        private void addEntries(Node node, Set<Entry<Integer, String>> set) {
            if (node != null) {
                addEntries(node.left, set);
                if (inRange(node.key)) {
                    set.add(new SimpleEntry(node.key, node.value));
                }
                addEntries(node.right, set);
            }
        }

        @Override
        public Integer firstKey() {
            Node node = firstNode();
            if (node == null) throw new NoSuchElementException();
            return node.key;
        }

        @Override
        public Integer lastKey() {
            Node node = lastNode();
            if (node == null) throw new NoSuchElementException();
            return node.key;
        }

        private Node firstNode() {
            Node node = root;
            while (node != null && node.left != null) {
                node = node.left;
            }
            while (node != null && !inRange(node.key)) {
                node = successor(node);
            }
            return node;
        }

        private Node lastNode() {
            Node node = root;
            while (node != null && node.right != null) {
                node = node.right;
            }
            while (node != null && !inRange(node.key)) {
                node = predecessor(node);
            }
            return node;
        }

        private Node successor(Node node) {
            if (node == null) return null;
            if (node.right != null) {
                node = node.right;
                while (node.left != null) {
                    node = node.left;
                }
                return node;
            }
            Node parent = node.parent;
            while (parent != null && node == parent.right) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }

        private Node predecessor(Node node) {
            if (node == null) return null;
            if (node.left != null) {
                node = node.left;
                while (node.right != null) {
                    node = node.right;
                }
                return node;
            }
            Node parent = node.parent;
            while (parent != null && node == parent.left) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            checkRange(toKey);
            return new SubMap(fromKey, toKey);
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            checkRange(fromKey);
            return new SubMap(fromKey, toKey);
        }

        @Override
        public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            if (fromKey != null && toKey != null && fromKey.compareTo(toKey) >= 0) {
                throw new IllegalArgumentException();
            }
            return new SubMap(fromKey, toKey);
        }

        @Override
        public Comparator<? super Integer> comparator() {
            return null;
        }

        private boolean inRange(Integer key) {
            return (fromKey == null || key.compareTo(fromKey) >= 0) &&
                    (toKey == null || key.compareTo(toKey) < 0);
        }

        private void checkRange(Integer key) {
            if (!inRange(key)) {
                throw new IllegalArgumentException("Key out of range");
            }
        }

        @Override
        public int size() {
            int count = 0;
            for (Entry<Integer, String> entry : entrySet()) {
                count++;
            }
            return count;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends String> m) {
            for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public Set<Integer> keySet() {
            Set<Integer> set = new HashSet<>();
            for (Entry<Integer, String> entry : entrySet()) {
                set.add(entry.getKey());
            }
            return set;
        }

        @Override
        public Collection<String> values() {
            List<String> list = new ArrayList<>();
            for (Entry<Integer, String> entry : entrySet()) {
                list.add(entry.getValue());
            }
            return list;
        }

        @Override
        public void clear() {
            Iterator<Entry<Integer, String>> iterator = entrySet().iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    // Остальные методы интерфейса

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = new HashSet<>();
        inOrderKeys(root, set);
        return set;
    }

    private void inOrderKeys(Node node, Set<Integer> set) {
        if (node != null) {
            inOrderKeys(node.left, set);
            set.add(node.key);
            inOrderKeys(node.right, set);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> list = new ArrayList<>();
        inOrderValues(root, list);
        return list;
    }

    private void inOrderValues(Node node, List<String> list) {
        if (node != null) {
            inOrderValues(node.left, list);
            list.add(node.value);
            inOrderValues(node.right, list);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> set = new HashSet<>();
        inOrderEntries(root, set);
        return set;
    }

    private void inOrderEntries(Node node, Set<Entry<Integer, String>> set) {
        if (node != null) {
            inOrderEntries(node.left, set);
            set.add(new SimpleEntry(node.key, node.value));
            inOrderEntries(node.right, set);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    // Простой класс для представления Entry
    private static class SimpleEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private String value;

        SimpleEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            return Objects.equals(key, entry.getKey()) &&
                    Objects.equals(value, entry.getValue());
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                    (value == null ? 0 : value.hashCode());
        }
    }
}
