package by.it.group451002.dirko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private enum Color { RED, BLACK; };

    private class Node {
        Integer key;
        String value;
        Color color = Color.RED;
        Node father, left, right;

        Node(Integer key, String value, Color color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    final Node empty = new Node(null, null, Color.BLACK);
    Node myTree = empty;
    int size = 0;

    private void print(Node currNode, StringBuilder res) {
        if (currNode.left != empty)
            print(currNode.left, res);
        res.append(currNode.key + "=" + currNode.value + ", ");
        if (currNode.right != empty)
            print(currNode.right, res);
    }

    public String toString() {
        StringBuilder res = new StringBuilder("{");
        if (myTree != empty) print(myTree, res);
        if (res.length() > 1) res.delete(res.length() - 2, res.length());
        res.append("}");
        return res.toString();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    private void findAllNodes(SortedMap<Integer, String> myMap, Node currNode, Integer key, boolean isLess) {
        if ((isLess && currNode.key < key) || (!isLess && currNode.key >= key))
            myMap.put(currNode.key, currNode.value);
        if (currNode.left != empty)
            findAllNodes(myMap, currNode.left, key, isLess);
        if (currNode.right != empty)
            findAllNodes(myMap, currNode.right, key, isLess);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap myMap = new MyRbMap();
        if (myTree != null) findAllNodes(myMap, myTree, toKey, true);
        return myMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap myMap = new MyRbMap();
        if (myTree != null) findAllNodes(myMap, myTree, fromKey, false);
        return myMap;
    }

    @Override
    public Integer firstKey() {
        Node tempNode = myTree;
        while (tempNode.left != empty) {
            tempNode = tempNode.left;
        }
        return (tempNode != empty) ? tempNode.key : null;
    }

    @Override
    public Integer lastKey() {
        Node tempNode = myTree;
        while (tempNode.left != empty) {
            tempNode = tempNode.right;
        }
        return (tempNode != empty) ? tempNode.key : null;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private boolean forwardPass(Node currNode, Object value) {
        boolean result = false;
        if (currNode.value == value) return true;
        if (currNode.left != empty)
            result = forwardPass(currNode.left, value);
        if (result) return true;
        if (currNode.right != empty)
            result = forwardPass(currNode.right, value);
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        return forwardPass(myTree, value);
    }

    private Node getNode(Object key) {
        Node tempNode = myTree;
        while (tempNode != empty) {
            if ((Integer)key < tempNode.key)
                tempNode = tempNode.left;
            else if ((Integer)key > tempNode.key)
                tempNode = tempNode.right;
            else return tempNode;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        Node resNode = getNode(key);
        return (resNode == null) ? null: resNode.value;
    }

    private Node grandParent(Node currNode) {
        if (currNode != null && currNode.father != null)
            return currNode.father.father;
        return null;
    }

    private Node uncle(Node currNode) {
        Node grand = grandParent(currNode);
        if (grand == null)
            return null;
        if (currNode.father == grand.left)
            return grand.right;
        return grand.left;
    }

    private Node brother(Node currNode) {
        if (currNode == currNode.father.left)
            return currNode.father.right;
        else return currNode.father.left;
    }

    private void rotateLeft(Node currNode) {
        Node pivot = currNode.right;

        pivot.father = currNode.father;
        if (currNode.father == null)
            myTree = pivot;
        else if (currNode.father.left == currNode)
            currNode.father.left = pivot;
        else currNode.father.right = pivot;

        currNode.right = pivot.left;
        if (pivot.left != null)
            pivot.left.father = currNode;

        currNode.father = pivot;
        pivot.left = currNode;
    }

    private void rotateRight(Node currNode) {
        Node pivot = currNode.left;

        pivot.father = currNode.father;
        if (currNode.father == null)
            myTree = pivot;
        else if (currNode.father.left == currNode)
            currNode.father.left = pivot;
        else currNode.father.right = pivot;

        currNode.left = pivot.right;
        if (pivot.right != null)
            pivot.right.father = currNode;

        currNode.father = pivot;
        pivot.right = currNode;
    }

    private void insertCase1(Node currNode) {
        if (currNode.father == null)
            currNode.color = Color.BLACK;
        else insertCase2(currNode);
    }

    private void insertCase2(Node currNode) {
        if (currNode.father.color == Color.BLACK)
            return;
        else insertCase3(currNode);
    }

    private void insertCase3(Node currNode) {
        Node uncle = uncle(currNode);
        if (uncle != null && uncle.color == Color.RED) {
            currNode.father.color = Color.BLACK; uncle.color = Color.BLACK;
            Node grand = grandParent(currNode);
            grand.color = Color.RED;
            insertCase1(currNode);
        }
        else insertCase4(currNode);
    }

    private void insertCase4(Node currNode) {
        Node grand = grandParent(currNode);
        if (currNode == currNode.father.right && currNode.father == grand.left) {
            rotateLeft(currNode.father);
            currNode = currNode.left;
        }
        else if (currNode == currNode.father.left && currNode.father == grand.right) {
            rotateRight(currNode.father);
            currNode = currNode.right;
        }
        insertCase5(currNode);
    }

    private void insertCase5(Node currNode) {
        Node grand = grandParent(currNode);

        currNode.father.color = Color.BLACK;
        grand.color = Color.RED;
        if (currNode == currNode.father.left && currNode.father == grand.left)
            rotateRight(grand);
        else rotateLeft(grand);
    }

    private void add(Integer key, String value, Node currNode) {
        Node tempNode = currNode;
        Node fatherNode = null;
        while (tempNode != empty) {
            fatherNode = tempNode;
            if (key < tempNode.key)
                tempNode = tempNode.left;
            else if (key > tempNode.key)
                tempNode = tempNode.right;
            else {
                tempNode.value = value;
                return;
            }
        }

        tempNode = new Node(key, value, Color.RED);
        tempNode.father = fatherNode;
        tempNode.left = empty; tempNode.right = empty;

        if (fatherNode != null) {
            if (key < fatherNode.key)
                fatherNode.left = tempNode;
            else fatherNode.right = tempNode;
        }

        insertCase1(tempNode);

        if (fatherNode == null)
            myTree = tempNode;
    }

    @Override
    public String put(Integer key, String value) {
        String res = get(key);
        add(key, value, myTree);
        if (res == null) size++;
        return res;
    }

    private void replace_node(Node currNode, Node child) {
        child.father = currNode.father;
        if (currNode == currNode.father.left)
            currNode.father.left = child;
        else currNode.father.right = child;
    }

    private void deleteCase1(Node currNode) {
        if (currNode.father != null)
            deleteCase2(currNode);
    }

    private void deleteCase2(Node currNode) {
        Node brother = brother(currNode);

        if (currNode.color == Color.RED) {
            currNode.father.color = Color.RED;
            brother.color = Color.BLACK;
            if (currNode == currNode.father.left)
                rotateLeft(currNode.father);
            else rotateRight(currNode.father);
        }
        deleteCase3(currNode);
    }

    private void deleteCase3(Node currNode) {
        Node brother = brother(currNode);

        if (
                currNode.father.color == Color.BLACK &&
                        brother.color == Color.BLACK &&
                        brother.left.color == Color.BLACK &&
                        brother.right.color == Color.BLACK
        ) {
            brother.color = Color.RED;
            deleteCase1(currNode);
        }
        else deleteCase4(currNode);
    }

    private void deleteCase4(Node currNode) {
        Node brother = brother(currNode);

        if (
                currNode.father.color == Color.RED &&
                        brother.color == Color.BLACK &&
                        brother.left.color == Color.BLACK &&
                        brother.right.color == Color.BLACK
        ) {
            brother.color = Color.RED;
            currNode.father.color = Color.BLACK;
        }
        else deleteCase5(currNode);
    }

    private void deleteCase5(Node currNode) {
        Node brother = brother(currNode);

        if (brother.color == Color.BLACK) {
            if (
                    currNode == currNode.father.left &&
                            brother.right.color == Color.BLACK &&
                            brother.left.color == Color.RED
            ) {
                brother.color = Color.RED;
                brother.left.color = Color.BLACK;
                rotateRight(brother);
            } else if (
                    (currNode == currNode.father.right) &&
                            brother.left.color == Color.BLACK &&
                            brother.right.color == Color.RED
            ) {
                brother.color = Color.RED;
                brother.right.color = Color.BLACK;
                rotateLeft(brother);
            }
        }
        deleteCase6(currNode);
    }

    private void deleteCase6(Node currNode) {
        Node brother = brother(currNode);

        brother.color = currNode.father.color;
        currNode.father.color = Color.BLACK;

        if (currNode == currNode.father.left) {
            brother.right.color = Color.BLACK;
            rotateLeft(currNode.father);
        }
        else {
            brother.left.color = Color.BLACK;
            rotateRight(currNode.father);
        }
    }

    private void deleteOneChild(Node currNode) {
        Node child = (currNode.right == empty) ? currNode.left : currNode.right;

        replace_node(currNode, child);
        if (currNode.color == Color.BLACK) {
            if (child.color == Color.RED)
                child.color = Color.BLACK;
            else deleteCase1(child);
        }
    }

    private void deleteTwoChild(Node currNode) {
        if (currNode.left == empty) {
            if (currNode.right != empty)
                deleteOneChild(currNode);
            else if (currNode.father == null)
                myTree = empty;
            else if (currNode == currNode.father.left)
                currNode.father.left = empty;
            else currNode.father.right = empty;
            return;
        }

        Node tempNode = currNode.left;
        while (tempNode.right != empty)
            tempNode = tempNode.right;

        currNode.key = tempNode.key;
        currNode.value = tempNode.value;
        deleteOneChild(tempNode);
    }

    @Override
    public String remove(Object key) {
        Node tempNode = getNode(key);
        String res = (tempNode == null) ? null : tempNode.value;
        if (tempNode != null) {
            deleteTwoChild(tempNode);
            size--;
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        myTree = empty;
        size = 0;
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
}
