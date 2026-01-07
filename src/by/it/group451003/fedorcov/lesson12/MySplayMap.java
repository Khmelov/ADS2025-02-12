package by.it.group451003.fedorcov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;
        SplayNode parent;

        SplayNode(Integer key, String value, SplayNode parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private SplayNode root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
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
        if (!(key instanceof Integer)) return false;
        SplayNode node = find((Integer) key);
        if (node != null) {
            splay(node);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(SplayNode node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        SplayNode node = find((Integer) key);
        if (node != null) {
            splay(node);
            return node.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        SplayNode node = find(key);
        if (node != null) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        }

        if (root == null) {
            root = new SplayNode(key, value, null);
            size = 1;
            return null;
        }

        node = root;
        SplayNode parent = null;
        while (node != null) {
            parent = node;
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            }
        }

        SplayNode newNode = new SplayNode(key, value, parent);
        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        SplayNode node = find((Integer) key);
        if (node == null) return null;

        String oldValue = node.value;
        splay(node);

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            SplayNode maxLeft = max(node.left);
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) node.right.parent = maxLeft;
            root = maxLeft;
            root.parent = null;
        }

        size--;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(SplayNode node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        headMap(node.left, toKey, result);
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        headMap(node.right, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        tailMap(node.left, fromKey, result);
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
        }
        tailMap(node.right, fromKey, result);
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        SplayNode minNode = min(root);
        splay(minNode);
        return minNode.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        SplayNode maxNode = max(root);
        splay(maxNode);
        return maxNode.key;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer lowerKey(Integer key) {
        SplayNode node = root;
        Integer result = null;
        while (node != null) {
            if (node.key < key) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (result != null) {
            SplayNode found = find(result);
            if (found != null) splay(found);
        }
        return result;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer floorKey(Integer key) {
        SplayNode node = root;
        Integer result = null;
        while (node != null) {
            if (node.key <= key) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (result != null) {
            SplayNode found = find(result);
            if (found != null) splay(found);
        }
        return result;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode node = root;
        Integer result = null;
        while (node != null) {
            if (node.key >= key) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (result != null) {
            SplayNode found = find(result);
            if (found != null) splay(found);
        }
        return result;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer higherKey(Integer key) {
        SplayNode node = root;
        Integer result = null;
        while (node != null) {
            if (node.key > key) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (result != null) {
            SplayNode found = find(result);
            if (found != null) splay(found);
        }
        return result;
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
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (root == null) return "{}";

        List<String> entries = new ArrayList<>();
        inOrderTraversal(root, entries);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(entries.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(SplayNode node, List<String> entries) {
        if (node == null) return;
        inOrderTraversal(node.left, entries);
        entries.add(node.key + "=" + node.value);
        inOrderTraversal(node.right, entries);
    }

    private SplayNode find(Integer key) {
        SplayNode node = root;
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    private void splay(SplayNode node) {
        while (node.parent != null) {
            if (node.parent.parent == null) {
                if (node == node.parent.left) {
                    rotateRight(node.parent);
                } else {
                    rotateLeft(node.parent);
                }
            } else if (node == node.parent.left && node.parent == node.parent.parent.left) {
                rotateRight(node.parent.parent);
                rotateRight(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.right) {
                rotateLeft(node.parent.parent);
                rotateLeft(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.left) {
                rotateLeft(node.parent);
                rotateRight(node.parent);
            } else {
                rotateRight(node.parent);
                rotateLeft(node.parent);
            }
        }
        root = node;
    }

    private void rotateLeft(SplayNode node) {
        SplayNode rightChild = node.right;
        if (rightChild == null) return;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(SplayNode node) {
        SplayNode leftChild = node.left;
        if (leftChild == null) return;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    private SplayNode min(SplayNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private SplayNode max(SplayNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
}