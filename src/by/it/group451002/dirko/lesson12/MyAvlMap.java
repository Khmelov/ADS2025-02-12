package by.it.group451002.dirko.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        int key;
        String value;
        int height = 0;
        Node left, right;

        public Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    Node myTree = null;
    int size = 0;

    private Node rotateLeft(Node currNode) {
        Node tempNode = currNode.right;
        currNode.right = tempNode.left;
        tempNode.left = currNode;

        currNode.height = Math.max(height(currNode.left), height(currNode.right)) + 1;
        tempNode.height = Math.max(height(tempNode.right), currNode.height) + 1;
        return tempNode;
    }

    private Node rotateRight(Node currNode) {
        Node tempNode = currNode.left;
        currNode.left = tempNode.right;
        tempNode.right = currNode;

        currNode.height = Math.max(height(currNode.left), height(currNode.right)) + 1;
        tempNode.height = Math.max(height(tempNode.left), currNode.height) + 1;
        return tempNode;
    }

    private Node bigRotateLeft(Node currNode) {
        currNode.right = rotateRight(currNode.right);
        return rotateLeft(currNode);
    }

    private Node bigRotateRight(Node currNode) {
        currNode.left = rotateLeft(currNode.left);
        return rotateRight(currNode);
    }

    public void print(Node currNode, StringBuilder res) {
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

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return (get(key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        Node currNode = myTree;
        while (currNode != null) {
            if ((int)key < currNode.key)
                currNode = currNode.left;
            else if ((int)key > currNode.key)
                currNode = currNode.right;
            else return currNode.value;
        }
        return null;
    }

    private int height(Node currNode) {
        if (currNode == null) return -1;
        return currNode.height;
    }

    private int countHeight(Node currNode) {
        return height(currNode.left) - height(currNode.right);
    }

    private Node balanceTreeAfterAdd(Node currNode, int key) {
        if (countHeight(currNode) < -1) {
            if (key > currNode.right.key)
                currNode = rotateLeft(currNode);
            else currNode = bigRotateLeft(currNode);
        }
        else if (countHeight(currNode) > 1) {
            if (key < currNode.left.key)
                currNode = rotateRight(currNode);
            else currNode = bigRotateRight(currNode);
        }
        return currNode;
    }

    private Node balanceTreeAfterRemove(Node currNode) {
        if (countHeight(currNode) < -1) {
            if (countHeight(currNode.right) <= 0)
                currNode = rotateLeft(currNode);
            else currNode = bigRotateLeft(currNode);
        }
        else if (countHeight(currNode) > 1) {
            if (countHeight(currNode.left) >= 0)
                currNode = rotateRight(currNode);
            else currNode = bigRotateRight(currNode);
        }
        return currNode;
    }

    private Node add(Integer key, String value, Node currNode) {
        if (currNode == null) return new Node(key, value);
        if (key < currNode.key)
            currNode.left = add(key, value, currNode.left);
        else if (key > currNode.key)
            currNode.right = add(key, value, currNode.right);
        else {
            currNode.value = value;
            return currNode;
        }
        currNode = balanceTreeAfterAdd(currNode, key);
        currNode.height = Math.max(height(currNode.left), height(currNode.right)) + 1;
        return currNode;
    }

    @Override
    public String put(Integer key, String value) {
        String res = get(key);
        myTree = add(key, value, myTree);
        if (res == null) size++;
        return res;
    }

    private Node removeNode(int key, Node currNode) {
        if (key < currNode.key)
            currNode.left = removeNode(key, currNode.left);
        else if (key > currNode.key)
            currNode.right = removeNode(key, currNode.right);
        else if (currNode.left == null && currNode.right == null)
            return null;
        else if (currNode.left == null)
            return currNode.right;
        else if (currNode.right == null)
            return currNode.left;
        else {
            Node tempNode;
            boolean isLeft = true;
            if (currNode.height >= 0) {
                tempNode = currNode.left;
                while (tempNode.right != null)
                    tempNode = tempNode.right;
            }
            else {
                isLeft = false;
                tempNode = currNode.right;
                while (tempNode.left != null)
                    tempNode = tempNode.left;
            }
            currNode.key = tempNode.key;
            currNode.value = tempNode.value;
            if (isLeft)
                currNode.left = removeNode(tempNode.key, currNode.left);
            else currNode.right = removeNode(tempNode.key, currNode.right);
        }
        currNode = balanceTreeAfterRemove(currNode);
        currNode.height = Math.max(height(currNode.left), height(currNode.right)) + 1;
        return currNode;
    }

    @Override
    public String remove(Object key) {
        String result = get(key);
        if (result == null) return null;
        myTree = removeNode((int)key, myTree);
        size--;
        return result;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        myTree = null;
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
