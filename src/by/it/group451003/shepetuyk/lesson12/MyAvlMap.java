package by.it.group451003.shepetuyk.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap<Integer extends Comparable<Integer>, String> implements Map<Integer, String> {

    private class Node {
        int height;
        Integer key;
        String value;
        Node left;
        Node right;
        Node (Integer key, String value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 0;
        }
    }

    void updateHeight(Node node) {
        if (node != null)
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    int getHeight(Node node) {
        return node == null ? -1 : node.height;
    }

    int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    void rebalance(Node node) {
        if (getBalance(node) == 2) {
            if (getBalance(node.left) == -1)
                leftRotate(node.left);
            rightRotate(node);
        }
        else if (getBalance(node) == -2) {
            if (getBalance(node.right) == 1)
                rightRotate(node.right);
            leftRotate(node);
        }
    }

    void rightRotate(Node parent) {
        Node nLeft = parent.left;
        Node bufferParent = parent;
        Node bufferChild = nLeft.right;
        parent = nLeft;
        parent.right = bufferParent;
        parent.right.left = bufferChild;
        updateHeight(parent);
        updateHeight(parent.right);
    }

    void leftRotate(Node parent) {
        Node nRight = parent.right;
        Node bufferParent = parent;
        Node bufferChild = nRight.left;
        parent = nRight;
        parent.left = bufferParent;
        parent.left.right = bufferChild;
        updateHeight(parent);
        updateHeight(parent.left);
    }

    Node getMax(Node node) {
        if (node == null)
            return null;
        if (node.right == null)
            return node;
        return getMax(node.right);
    }

    java.lang.String getLeftRight(Node node) {
        return node == null ? "" : getLeftRight(node.left) + node.key + "=" + node.value + ", " + getLeftRight(node.right);
    }

    Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        } else if (node.key.compareTo(key) > 0) {
            node.left = insert(node.left, key, value);
        } else if (node.key.compareTo(key) < 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
        }
        updateHeight(node);
        return node;
    }

    Node delete(Node node, Integer key) {
        if (node == null)
            return null;
        else if (key.compareTo(node.key) < 0)
            node.left = delete(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = delete(node.right, key);
        else {
            if (node.left == null || node.right == null)
                node = (node.left == null) ? node.right : node.left;
            else {
                Node max = getMax(node.left);
                node.key = max.key;
                node.value = max.value;
                node.right = delete(node.right, max.key);
            }
        }
        updateHeight(node);
        rebalance(node);

        return node;
    }

    private int size;
    private Node treeRoot;

    MyAvlMap() {
        this.size = 0;
        this.treeRoot = null;
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        if (treeRoot == null) {
            treeRoot = new Node(key, value);
            size++;
            return null;
        }

        String tempValue = get(key);
        size += tempValue == null ? 1 : 0;
        insert(treeRoot, key, value);

        return tempValue;
    }

    @Override
    public String remove(Object key) {
        String tempValue = get(key);

        if (tempValue != null) {
            size--;
            delete(treeRoot, (Integer)key);
        }

        return tempValue;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.treeRoot = null;
    }

    /////////////////////

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

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
