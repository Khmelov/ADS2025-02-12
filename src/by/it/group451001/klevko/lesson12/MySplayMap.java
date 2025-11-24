package by.it.group451001.klevko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size;

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node splay(Node root, Integer key) {
        if (root == null || key.equals(root.key)) return root;
        if (key.compareTo(root.key) < 0) {
            if (root.left == null) return root;
            if (key.compareTo(root.left.key) < 0) {
                root.left.left = splay(root.left.left, key);
                root = rotateRight(root);
            }
            else if (key.compareTo(root.left.key) > 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) root.left = rotateLeft(root.left);
            }
            if (root.left == null) return root;
            else return rotateRight(root);
        }
        else {
            if (root.right == null) return root;
            if (key.compareTo(root.right.key) > 0) {
                root.right.right = splay(root.right.right, key);
                root = rotateLeft(root);
            }
            else if (key.compareTo(root.right.key) < 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) root.right = rotateRight(root.right);
            }
            if (root.right == null) return root;
            else return rotateLeft(root);
        }
    }


    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        if (key.equals(root.key)) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
        Node newNode = new Node(key, value);
        if (key.compareTo(root.key) < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (root == null) return null;
        root = splay(root, intKey);
        if (!intKey.equals(root.key)) return null;
        String oldValue = root.value;
        if (root.left == null) root = root.right;
        else {
            Node temp = root.right;
            root = root.left;
            root = splay(root, intKey);
            root.right = temp;
        }
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (root == null) return null;
        root = splay(root, intKey);
        if (intKey.equals(root.key)) return root.value;
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
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
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    private void headMapHelper(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            result.put(node.key, node.value);
            headMapHelper(node.left, toKey, result);
            headMapHelper(node.right, toKey, result);
        } else headMapHelper(node.left, toKey, result);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    private void tailMapHelper(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            result.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, result);
            tailMapHelper(node.right, fromKey, result);
        } else tailMapHelper(node.right, fromKey, result);
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return lowerKeyHelper(root, key, null);
    }

    private Integer lowerKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) < 0) {
            best = node.key;
            return lowerKeyHelper(node.right, key, best);
        } else return lowerKeyHelper(node.left, key, best);
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return floorKeyHelper(root, key, null);
    }

    private Integer floorKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) <= 0) {
            best = node.key;
            return floorKeyHelper(node.right, key, best);
        } else return floorKeyHelper(node.left, key, best);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return ceilingKeyHelper(root, key, null);
    }

    private Integer ceilingKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) >= 0) {
            best = node.key;
            return ceilingKeyHelper(node.left, key, best);
        } else return ceilingKeyHelper(node.right, key, best);
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return higherKeyHelper(root, key, null);
    }

    private Integer higherKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) > 0) {
            best = node.key;
            return higherKeyHelper(node.left, key, best);
        } else return higherKeyHelper(node.right, key, best);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Нереализованные методы NavigableMap интерфейса (не требуются по заданию)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
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
