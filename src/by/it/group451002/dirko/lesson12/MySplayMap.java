package by.it.group451002.dirko.lesson12;

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

    Node myTree = null;
    int size = 0;

    private void print(Node currNode, StringBuilder res) {
        if (currNode.left != null)
            print(currNode.left, res);
        res.append(currNode.key + "=" + currNode.value + ", ");
        if (currNode.right != null)
            print(currNode.right, res);
    }

    public String toString() {
        StringBuilder res = new StringBuilder("{");
        if (myTree != null) print(myTree, res);
        if (res.length() > 1) res.delete(res.length() - 2, res.length());
        res.append("}");
        return res.toString();
    }

    private Node rotateLeft(Node currNode) {
        Node tempNode = currNode.right;
        currNode.right = tempNode.left;
        tempNode.left = currNode;
        return tempNode;
    }

    private Node rotateRight(Node currNode) {
        Node tempNode = currNode.left;
        currNode.left = tempNode.right;
        tempNode.right = currNode;
        return tempNode;
    }

    private Node splay(Node root, int key) {
        if (root == null || root.key == key)
            return root;

        if (key < root.key) {
            if (root.left == null) return root;

            if (key < root.left.key) {
                root.left.left = splay(root.left.left, key);
                root = rotateRight(root);
            }
            else if (key > root.left.key) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null)
                    root.left = rotateLeft(root.left);
            }
            return (root.left == null) ? root : rotateRight(root);
        }
        else {
            if (root.right == null) return root;

            if (key < root.right.key) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rotateRight(root.right);
            }
            else if (key > root.right.key) {
                root.right.right = splay(root.right.right, key);
                root = rotateLeft(root);
            }
            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    private Node insert(Node root, int key, String value) {
        if (root == null)
            return new Node(key, value);

        root = splay(root, key);

        if (root.key == key) {
            root.value = value;
            return root;
        }

        Node newNode = new Node(key, value);
        if (key < root.key) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        }
        else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        return newNode;
    }

    @Override
    public String put(Integer key, String value) {
        String res = get(key);
        myTree = insert(myTree, key, value);
        if (res == null) size++;
        return res;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node tempNode = myTree;
        Node lowerNode = null;
        while (tempNode != null) {
            if (key <= tempNode.key)
                tempNode = tempNode.left;
            else {
                lowerNode = tempNode;
                tempNode = tempNode.right;
            }
        }
        return (lowerNode == null) ? null : lowerNode.key;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node tempNode = myTree;
        Node lowerNode = null;
        while (tempNode != null) {
            if (key < tempNode.key)
                tempNode = tempNode.left;
            else if (key > tempNode.key){
                lowerNode = tempNode;
                tempNode = tempNode.right;
            }
            else return tempNode.key;
        }
        return (lowerNode == null) ? null : lowerNode.key;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node tempNode = myTree;
        Node biggerNode = null;
        while (tempNode != null) {
            if (key > tempNode.key)
                tempNode = tempNode.right;
            else if (key < tempNode.key){
                biggerNode = tempNode;
                tempNode = tempNode.left;
            }
            else return tempNode.key;
        }
        return (biggerNode == null) ? null : biggerNode.key;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node tempNode = myTree;
        Node biggerNode = null;
        while (tempNode != null) {
            if (key >= tempNode.key)
                tempNode = tempNode.right;
            else {
                biggerNode = tempNode;
                tempNode = tempNode.left;
            }
        }
        return (biggerNode == null) ? null : biggerNode.key;
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

    private void findAllNodes(NavigableMap<Integer, String> myMap, Node currNode, Integer key, boolean isLess) {
        if ((isLess && currNode.key < key) || (!isLess && currNode.key >= key))
            myMap.put(currNode.key, currNode.value);
        if (currNode.left != null)
            findAllNodes(myMap, currNode.left, key, isLess);
        if (currNode.right != null)
            findAllNodes(myMap, currNode.right, key, isLess);
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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap myMap = new MySplayMap();
        if (myTree != null) findAllNodes(myMap, myTree, toKey, true);
        return myMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap myMap = new MySplayMap();
        if (myTree != null) findAllNodes(myMap, myTree, fromKey, false);
        return myMap;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Integer firstKey() {
        Node tempNode = myTree;
        while (tempNode.left != null) {
            tempNode = tempNode.left;
        }
        return tempNode.key;
    }

    @Override
    public Integer lastKey() {
        Node tempNode = myTree;
        while (tempNode.right != null) {
            tempNode = tempNode.right;
        }
        return tempNode.key;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
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
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) { return get(key) != null; }

    private boolean forwardPass(Node currNode, Object value) {
        boolean result = false;
        if (currNode.value == value) return true;
        if (currNode.left != null)
            result = forwardPass(currNode.left, value);
        if (result) return true;
        if (currNode.right != null)
            result = forwardPass(currNode.right, value);
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        return myTree != null && forwardPass(myTree, value);
    }

    @Override
    public String get(Object key) {
        Node currNode = myTree;
        while (currNode != null) {
            if ((Integer)key < currNode.key)
                currNode = currNode.left;
            else if ((Integer)key > currNode.key)
                currNode = currNode.right;
            else return currNode.value;
        }
        return null;
    }

    private Node delete(Node root, int key) {
        if (root == null) return root;

        root = splay(root, key);

        if (root.key != key)
            return root;

        if (root.left == null)
            root = root.right;
        else {
            Node tempNode = root;
            root = splay(root.left, key);
            root.right = tempNode.right;
        }

        return root;
    }

    @Override
    public String remove(Object key) {
        String res = get(key);
        if (res == null) return null;
        myTree = delete(myTree, (Integer)key);

        size--;
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        myTree = null;
        size = 0;
    }
}
