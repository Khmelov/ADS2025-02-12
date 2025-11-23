package by.it.group410902.kozincev.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node parent;
        Node left;
        Node right;

        public Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {
        this.root = null;
        this.size = 0;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else {
                if (x == p.left && p == g.left) {
                    rotateRight(g);
                    rotateRight(p);
                }
                else if (x == p.right && p == g.right) {
                    rotateLeft(g);
                    rotateLeft(p);
                }
                else if (x == p.right && p == g.left) {
                    rotateLeft(p);
                    rotateRight(g);
                }
                else {
                    rotateRight(p);
                    rotateLeft(g);
                }
            }
        }
        this.root = x;
    }

    private Node findNodeAndSplay(Integer key) {
        Node current = root;
        Node last = null;

        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (last != null) {
            splay(last);
        }
        return null;
    }

    private Node treeMinimum(Node node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node treeMaximum(Node node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node existing = findNodeAndSplay(key);

        if (root != null && key.equals(root.key)) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node newNode = new Node(key, value, null);

        if (key.compareTo(root.key) < 0) {
            newNode.left = root.left;
            if (root.left != null) root.left.parent = newNode;
            newNode.right = root;
            root.parent = newNode;
            root.left = null;
        } else {
            newNode.right = root.right;
            if (root.right != null) root.right.parent = newNode;
            newNode.left = root;
            root.parent = newNode;
            root.right = null;
        }

        root = newNode;
        root.parent = null;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        Node z = findNodeAndSplay((Integer) key);

        if (z == null || !z.key.equals(key)) {
            return null;
        }

        String oldValue = z.value;
        size--;

        Node leftTree = z.left;
        Node rightTree = z.right;

        if (leftTree == null) {
            root = rightTree;
            if (root != null) root.parent = null;
        } else {
            leftTree.parent = null;

            this.root = leftTree;

            Node maxNode = treeMaximum(this.root);
            splay(maxNode);

            root.right = rightTree;
            if (rightTree != null) {
                rightTree.parent = root;
            }
        }
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNodeAndSplay((Integer) key);

        if (node != null && node.key.equals(key)) {
            return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;

        return containsValueRecursive(root, (String) value);
    }

    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
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

    private Integer findBoundaryKey(Integer key, boolean inclusive, boolean lower) {
        Node current = root;
        Integer resultKey = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                if (inclusive) {
                    splay(current);
                    return current.key;
                }
                if (lower) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            } else if (cmp < 0) {
                if (!lower) {
                    resultKey = current.key;
                }
                current = current.left;
            } else {
                if (lower) {
                    resultKey = current.key;
                }
                current = current.right;
            }
        }

        if (resultKey != null) {
            findNodeAndSplay(resultKey);
        } else if (root != null) {
            findNodeAndSplay(key);
        }

        return resultKey;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return findBoundaryKey(key, false, true);
    }

    @Override
    public Integer floorKey(Integer key) {
        return findBoundaryKey(key, true, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findBoundaryKey(key, true, false);
    }

    @Override
    public Integer higherKey(Integer key) {
        return findBoundaryKey(key, false, false);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node min = treeMinimum(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node max = treeMaximum(root);
        splay(max);
        return max.key;
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left, sb);
        sb.append(node.toString()).append(", ");
        inOrderTraversal(node.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        subMapRecursive(root, toKey, inclusive, true, subMap);
        return subMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        subMapRecursive(root, fromKey, inclusive, false, subMap);
        return subMap;
    }

    private void subMapRecursive(Node node, Integer boundaryKey, boolean inclusive, boolean isHeadMap, MySplayMap subMap) {
        if (node == null) return;

        int cmp = node.key.compareTo(boundaryKey);
        boolean fitsBoundary;

        if (isHeadMap) {
            fitsBoundary = inclusive ? (cmp <= 0) : (cmp < 0);

            if (fitsBoundary) {
                subMap.put(node.key, node.value);

                subMapRecursive(node.left, boundaryKey, inclusive, isHeadMap, subMap);

                subMapRecursive(node.right, boundaryKey, inclusive, isHeadMap, subMap);

            } else {
                subMapRecursive(node.left, boundaryKey, inclusive, isHeadMap, subMap);
            }

        } else {
            fitsBoundary = inclusive ? (cmp >= 0) : (cmp > 0);

            if (fitsBoundary) {
                subMap.put(node.key, node.value);

                subMapRecursive(node.right, boundaryKey, inclusive, isHeadMap, subMap);

                subMapRecursive(node.left, boundaryKey, inclusive, isHeadMap, subMap);

            } else {
                subMapRecursive(node.right, boundaryKey, inclusive, isHeadMap, subMap);
            }
        }
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Not implemented for test");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not implemented for test");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented for test");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented for test");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented for test");
    }
}