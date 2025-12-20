package by.it.group451003.galuzo.lesson12;


import java.util.*;

public class MySplayMap<Integer extends Comparable<Integer>, String> implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        Node (Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = null;
            left = null;
            right = null;
        }
    }

    private Node treeRoot;
    private int size;

    MySplayMap() {
        treeRoot = null;
        size = 0;
    }

    Node leftRotate(Node node) {
        Node rNode = node.right;
        node.right = rNode.left;
        rNode.left = node;
        return rNode;
    }

    Node rightRotate(Node node) {
        Node lNode = node.left;
        node.left = lNode.right;
        lNode.right = node;
        return lNode;
    }

    Node splay(Node root, Integer key) {
        if (root == null || root.key == key)
            return root;

        if (root.key.compareTo(key) > 0) {
            if (root.left == null)
                return root;

            if (root.left.key.compareTo(key) > 0) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            }
            else if (root.left.key.compareTo(key) < 0) {
                root.left.right = splay(root.left.right, key);

                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }

            return (root.left == null) ? root : rightRotate(root);
        }

        else {
            if (root.right == null)
                return root;

            if (root.right.key.compareTo(key) > 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            }
            else if (root.right.key.compareTo(key) < 0) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }

            return (root.right == null) ? root : leftRotate(root);
        }
    }

    Node insert(Node node, Node parent, Integer key, String value) {
        if (node == null)
            return new Node(key, value, parent);

        if (key.compareTo(node.key) > 0)
            node.right = insert(node.right, node, key, value);
        else if (key.compareTo(node.key) < 0)
            node.left = insert(node.left, node, key, value);
        else {
            node.value = value;
            return node;
        }

        return node;
    }

    Node delete(Node root, Integer key) {
        if (root == null)
            return null;

        root = splay(root, key);

        if (root.key.compareTo(key) != 0)
            return root;

        if (root.left == null) {
            root = root.right;
        }
        else {
            Node tempNode = root;
            root = splay(root.left, key);
            root.right = tempNode.right;
        }

        return root;
    }

    void getToKeyNodes(MySplayMap tree, Node node, Integer keyTo) {
        if (node == null)
            return;

        if(node.key.compareTo(keyTo) >= 0 ) {
            getToKeyNodes(tree, node.left, keyTo);
            return;
        }

        getToKeyNodes(tree, node.left, keyTo);
        getToKeyNodes(tree, node.right, keyTo);
        tree.put(node.key, node.value);
    }

    void getFromKeyNodes(MySplayMap tree, Node node, Integer keyFrom) {
        if (node == null)
            return;
        if(node.key.compareTo(keyFrom) < 0) {
            getFromKeyNodes(tree, node.right, keyFrom);
            return;
        }

        getFromKeyNodes(tree, node.left, keyFrom);
        getFromKeyNodes(tree, node.right, keyFrom);
        tree.put(node.key, node.value);
    }

    Node getMin(Node node) {
        if (node == null)
            return null;
        if (node.left == null)
            return node;
        return getMin(node.left);
    }

    Node getMax(Node node) {
        if (node == null)
            return null;
        if (node.right == null)
            return node;
        return getMax(node.right);
    }

    boolean findValue(Node node, String value) {
        if (node != null && (value.equals(node.value) || findValue(node.left, value) || findValue(node.right, value)))
            return true;
        return false;
    }

    java.lang.String getLeftRight(Node node) {
        return node == null ? "" : getLeftRight(node.left) + node.key + "=" + node.value + ", " + getLeftRight(node.right);
    }

    public java.lang.String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("{");
        strbldr.append(getLeftRight(treeRoot));

        int sbLength = strbldr.length();
        if (sbLength > 1)
            strbldr.delete(sbLength - 2, sbLength);

        strbldr.append("}");
        return strbldr.toString();
    }

    @Override
    public Integer lowerKey(Integer key) {
        Integer lowerKey = null;
        Node tempNode = treeRoot;
        while (tempNode != null){
            lowerKey = key.compareTo(tempNode.key) > 0 ? tempNode.key : lowerKey;
            tempNode = lowerKey == tempNode.key ? tempNode.right : tempNode.left;
        }
        return lowerKey;
    }

    @Override
    public Integer floorKey(Integer key) {
        Integer lowerKey = null;
        Node tempNode = treeRoot;
        while (tempNode != null){
            lowerKey = key.compareTo(tempNode.key) >= 0 ? tempNode.key : lowerKey;
            tempNode = lowerKey == tempNode.key ? tempNode.right : tempNode.left;
        }
        return lowerKey;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Integer higherKey = null;
        Node tempNode = treeRoot;
        while (tempNode != null){
            higherKey = key.compareTo(tempNode.key) <= 0 ? tempNode.key : higherKey;
            tempNode = higherKey == tempNode.key ? tempNode.left : tempNode.right;
        }
        return higherKey;
    }

    @Override
    public Integer higherKey(Integer key) {
        Integer higherKey = null;
        Node tempNode = treeRoot;
        while (tempNode != null){
            higherKey = key.compareTo(tempNode.key) < 0 ? tempNode.key : higherKey;
            tempNode = higherKey == tempNode.key ? tempNode.left : tempNode.right;
        }
        return higherKey;
    }

    @Override
    public Integer firstKey() {
        return getMin(treeRoot).key;
    }

    @Override
    public Integer lastKey() {
        return getMax(treeRoot).key;
    }

    @Override
    public boolean containsKey(Object key) {
        Node tempNode = treeRoot;
        while (tempNode != null) {
            if (tempNode.key.equals(key))
                return true;
            tempNode = tempNode.key.compareTo((Integer)key) < 0 ? tempNode.right : tempNode.left;
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return findValue(treeRoot, (String)value);
    }

    @Override
    public String get(Object key) {
        Node tempNode = treeRoot;
        while (tempNode != null) {
            if (tempNode.key.equals(key))
                return tempNode.value;
            tempNode = tempNode.key.compareTo((Integer)key) < 0 ? tempNode.right : tempNode.left;
        }

        return null;
    }

    @Override
    public String put(Integer key, String value) {
        String tempValue = get(key);
        size += tempValue == null ? 1 : 0;
        treeRoot = splay(insert(treeRoot, null, key, value), key);

        return tempValue;
    }

    @Override
    public String remove(Object key) {
        String tempValue = get(key);

        if (tempValue != null) {
            size--;
            treeRoot = delete(treeRoot, (Integer)key);
        }

        return tempValue;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subTree = new MySplayMap();
        getToKeyNodes(subTree, treeRoot, toKey);
        return subTree;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subTree = new MySplayMap();
        getFromKeyNodes(subTree, treeRoot, fromKey);
        return subTree;
    }

    @Override
    public void clear() {
        treeRoot = null;
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

    ///////////////////////////

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }
    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }
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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }
    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }
    @Override
    public Collection<String> values() {
        return List.of();
    }
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }
}
