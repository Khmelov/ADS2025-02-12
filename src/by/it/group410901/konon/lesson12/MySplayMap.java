package by.it.group410901.konon.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private class Node {
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

    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node splay(Node root, Integer key) {
        if (root == null || root.key.equals(key)) return root;

        if (key < root.key) {
            if (root.left == null) return root;
            if (key < root.left.key) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            } else if (key > root.left.key) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }
            return (root.left == null) ? root : rightRotate(root);
        } else {
            if (root.right == null) return root;
            if (key > root.right.key) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            } else if (key < root.right.key) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            }
            return (root.right == null) ? root : leftRotate(root);
        }
    }

    @Override
    public String put(Integer key, String value) {
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
    public String get(Object key) {
        if (root == null) return null;
        root = splay(root, (Integer) key);
        return (root.key.equals(key)) ? root.value : null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) return null;
        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;
        String old = root.value;
        if (root.left == null) root = root.right;
        else {
            Node temp = root.right;
            root = root.left;
            root = splay(root, (Integer) key);
            root.right = temp;
        }
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object val) {
        if (node == null) return false;
        if (node.value == null) {
            if (val == null) return true;
        } else if (node.value.equals(val)) return true;
        return containsValueRec(node.left, val) || containsValueRec(node.right, val);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        buildString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        buildString(node.right, sb);
    }

    private void collect(Node node, List<Integer> keys) {
        if (node == null) return;
        collect(node.left, keys);
        keys.add(node.key);
        collect(node.right, keys);
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        buildHead(root, map, toKey);
        return map;
    }

    private void buildHead(Node node, MySplayMap map, Integer key) {
        if (node == null) return;
        if (node.key < key) {
            map.put(node.key, node.value);
            buildHead(node.left, map, key);
            buildHead(node.right, map, key);
        } else {
            buildHead(node.left, map, key);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        buildTail(root, map, fromKey);
        return map;
    }

    private void buildTail(Node node, MySplayMap map, Integer key) {
        if (node == null) return;
        if (node.key >= key) {
            map.put(node.key, node.value);
            buildTail(node.left, map, key);
            buildTail(node.right, map, key);
        } else {
            buildTail(node.right, map, key);
        }
    }

    @Override
    public Integer lowerKey(Integer key) {
        List<Integer> keys = new ArrayList<>();
        collect(root, keys);
        Integer res = null;
        for (Integer k : keys)
            if (k < key) res = k;
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        List<Integer> keys = new ArrayList<>();
        collect(root, keys);
        Integer res = null;
        for (Integer k : keys)
            if (k <= key) res = k;
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        List<Integer> keys = new ArrayList<>();
        collect(root, keys);
        for (Integer k : keys)
            if (k >= key) return k;
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        List<Integer> keys = new ArrayList<>();
        collect(root, keys);
        for (Integer k : keys)
            if (k > key) return k;
        return null;
    }

    @Override public Map.Entry<Integer, String> lowerEntry(Integer key){return null;}
    @Override public Map.Entry<Integer, String> floorEntry(Integer key){return null;}
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key){return null;}
    @Override public Map.Entry<Integer, String> higherEntry(Integer key){return null;}
    @Override public Map.Entry<Integer, String> firstEntry(){return null;}
    @Override public Map.Entry<Integer, String> lastEntry(){return null;}
    @Override public Map.Entry<Integer, String> pollFirstEntry(){return null;}
    @Override public Map.Entry<Integer, String> pollLastEntry(){return null;}
    @Override public NavigableMap<Integer, String> descendingMap(){return null;}
    @Override public NavigableSet<Integer> navigableKeySet(){return null;}
    @Override public NavigableSet<Integer> descendingKeySet(){return null;}
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive){return null;}
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive){return null;}
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive){return null;}
    @Override public Comparator<? super Integer> comparator(){return null;}
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey){return null;}
    @Override public Set<Integer> keySet(){return null;}
    @Override public Collection<String> values(){return null;}
    @Override public Set<Entry<Integer, String>> entrySet(){return null;}
}
