package by.it.group451003.avrusevich.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    Node Root;

    class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        if (Root == null)
            return "{}";
        StringBuilder sb = new StringBuilder().append("{");
        inOrderTraversal(Root, sb);
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("}");
        return sb.toString();
    }

    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key + "=" + node.value + ", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public int size() {
        return size(Root);
    }

    int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(Root, value.toString());
    }

    boolean containsValue(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (node.value.equals(value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node found = SearchKey((Integer) key, Root);
        if (found != null) {
            Root = splay(Root, found.key);
            return found.value;
        }
        return null;
    }

    Node SearchKey(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (Root == null) {
            Root = new Node(key, value);
            return null;
        }

        Root = splay(Root, key);
        int cmp = key.compareTo(Root.key);
        if (cmp == 0) {
            String oldValue = Root.value;
            Root.value = value;
            return oldValue;
        } else if (cmp < 0) {
            Node newNode = new Node(key, value);
            newNode.left = Root.left;
            newNode.right = Root;
            newNode.right.parent = newNode;
            Root.left = null;
            Root = newNode;
        } else {
            Node newNode = new Node(key, value);
            newNode.right = Root.right;
            newNode.left = Root;
            newNode.left.parent = newNode;
            Root.right = null;
            Root = newNode;
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (Root == null) {
            return null;
        }

        Root = splay(Root, (Integer) key);
        int cmp = ((Integer) key).compareTo(Root.key);
        if (cmp != 0) {
            return null;
        }

        String removedValue = Root.value;

        if (Root.left == null) {
            Root = Root.right;
            if (Root != null) {
                Root.parent = null;
            }
        } else {
            Node newRoot = Root.right;
            newRoot = splay(newRoot, (Integer) key);
            newRoot.left = Root.left;
            newRoot.left.parent = newRoot;
            Root = newRoot;
        }

        return removedValue;
    }

    Node splay(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            if (node.left == null) {
                return node;
            } else {
                return rotateRight(node);
            }
        } else if (cmp > 0) {
            if (node.right == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            if (node.right == null) {
                return node;
            } else {
                return rotateLeft(node);
            }
        } else {
            return node;
        }
    }

    Node rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.right = node;
        leftChild.parent = node.parent;
        node.parent = leftChild;
        return leftChild;
    }

    Node rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        rightChild.parent = node.parent;
        node.parent = rightChild;
        return rightChild;
    }

    @Override
    public void clear() {
        Root = clear(Root);
    }

    Node clear(Node node) {
        if (node == null)
            return null;
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (Root == null)
            return null;
        Node node = lowerKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    Node lowerKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison <= 0)
            return lowerKeyNode(key, node.left);
        Node rightResult = lowerKeyNode(key, node.right);
        if (rightResult != null)
            return rightResult;
        return node;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (Root == null)
            return null;
        Node node = floorKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    Node floorKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;
        if (comparison < 0)
            return floorKeyNode(key, node.left);
        Node rightResult = floorKeyNode(key, node.right);
        if (rightResult != null)
            return rightResult;
        return node;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (Root == null)
            return null;
        Node node = ceilingKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    Node ceilingKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;
        if (comparison > 0)
            return ceilingKeyNode(key, node.right);
        Node leftResult = ceilingKeyNode(key, node.left);
        if (leftResult != null)
            return leftResult;
        return node;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (Root == null)
            return null;
        Node node = higherKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    Node higherKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison >= 0)
            return higherKeyNode(key, node.right);
        Node leftResult = higherKeyNode(key, node.left);
        if (leftResult != null)
            return leftResult;
        return node;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        headMap(Root, toKey, subMap);
        return subMap;
    }

    void headMap(Node node, Integer toKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMap(node.right, toKey, subMap);
        }

        headMap(node.left, toKey, subMap);
    }


    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap();
        tailMap(Root, fromKey, subMap);
        return subMap;
    }

    void tailMap(Node node, Integer fromKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMap(node.left, fromKey, subMap);
        }

        tailMap(node.right, fromKey, subMap);
    }

    @Override
    public Integer firstKey() {
        if (Root == null)
            return null;
        Node node = Root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (Root == null)
            return null;
        Node node = Root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    ///////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }
}