package by.it.group451004.volynets.lesson12;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;
        SplayNode parent;

        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root = splay(root, key); // "растягиваем" дерево
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer intKey = (Integer) key;
        if (!containsKey(intKey)) {
            return null;
        }

        root = splay(root, intKey); // перемещаем удаляемый узел в корень

        String removedValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            SplayNode newRoot = root.right;
            newRoot = splay(newRoot, intKey);
            if (newRoot != null) {
                newRoot.left = root.left;
            }
            root = newRoot;
        }

        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        root = splay(root, (Integer) key); // "растягиваем" дерево к найденному ключу
        return root != null && root.key.equals(key) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        return containsValue(root, (String) value);
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
    public Integer firstKey() {
        if (root == null) {
            return null;
        }
        SplayNode min = findMin(root);
        root = splay(root, min.key); // "растягиваем" минимальный элемент к корню
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }
        SplayNode max = findMax(root);
        root = splay(root, max.key); // "растягиваем" максимальный элемент к корню
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        SplayNode node = lowerNode(root, key);
        if (node != null) {
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        SplayNode node = floorNode(root, key);
        if (node != null) {
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode node = ceilingNode(root, key);
        if (node != null) {
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        SplayNode node = higherNode(root, key);
        if (node != null) {
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Методы NavigableMap                        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы Splay-дерева       ///////
    /////////////////////////////////////////////////////////////////////////

    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private SplayNode put(SplayNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new SplayNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
            if (node.left != null) {
                node.left.parent = node;
            }
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
            if (node.right != null) {
                node.right.parent = node;
            }
        } else {
            oldValue[0] = node.value;
            node.value = value;
        }

        return node;
    }

    // Основная операция Splay-дерева - "растягивание"
    private SplayNode splay(SplayNode node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        if (key.compareTo(node.key) < 0) {
            // Ключ в левом поддереве
            if (node.left == null) return node;

            if (key.compareTo(node.left.key) < 0) {
                // Zig-Zig (левый-левый)
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key.compareTo(node.left.key) > 0) {
                // Zig-Zag (левый-правый)
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return node.left == null ? node : rotateRight(node);
        } else {
            // Ключ в правом поддереве
            if (node.right == null) return node;

            if (key.compareTo(node.right.key) < 0) {
                // Zag-Zig (правый-левый)
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (key.compareTo(node.right.key) > 0) {
                // Zag-Zag (правый-правый)
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return node.right == null ? node : rotateLeft(node);
        }
    }

    private SplayNode rotateRight(SplayNode y) {
        SplayNode x = y.left;
        y.left = x.right;
        x.right = y;
        return x;
    }

    private SplayNode rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private boolean containsValue(SplayNode node, String value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private SplayNode findMin(SplayNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode findMax(SplayNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void headMap(SplayNode node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node != null) {
            headMap(node.left, toKey, inclusive, result);
            if (inclusive ? node.key <= toKey : node.key < toKey) {
                result.put(node.key, node.value);
            }
            headMap(node.right, toKey, inclusive, result);
        }
    }

    private void tailMap(SplayNode node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node != null) {
            tailMap(node.left, fromKey, inclusive, result);
            if (inclusive ? node.key >= fromKey : node.key > fromKey) {
                result.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, inclusive, result);
        }
    }

    private SplayNode lowerNode(SplayNode node, Integer key) {
        if (node == null) return null;

        if (key.compareTo(node.key) <= 0) {
            return lowerNode(node.left, key);
        } else {
            SplayNode right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    private SplayNode floorNode(SplayNode node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return floorNode(node.left, key);
        } else {
            SplayNode right = floorNode(node.right, key);
            return right != null ? right : node;
        }
    }

    private SplayNode ceilingNode(SplayNode node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return ceilingNode(node.right, key);
        } else {
            SplayNode left = ceilingNode(node.left, key);
            return left != null ? left : node;
        }
    }

    private SplayNode higherNode(SplayNode node, Integer key) {
        if (node == null) return null;

        if (key.compareTo(node.key) >= 0) {
            return higherNode(node.right, key);
        } else {
            SplayNode left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы - заглушки               ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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