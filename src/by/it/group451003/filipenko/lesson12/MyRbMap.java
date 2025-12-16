package by.it.group451003.filipenko.lesson12;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MyRbMap<Integer extends Comparable<Integer>, String> implements SortedMap<Integer, String> {
    enum Color {
        RED,
        BLACK
    }

    private class Node {
        Color color;
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        Node (Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            left = null;
            right = null;
            color = Color.RED;
        }
    }

    private Node treeRoot;
    private int size;

    MyRbMap() {
        this.treeRoot = null;
        this.size = 0;
    }

    void rightRotate(Node parent) {
        Node nLeft = parent.left;
        Node bufferParent = parent;
        Node bufferChild = nLeft.right;
        parent = nLeft;
        parent.right = bufferParent;
        parent.right.left = bufferChild;
    }

    void leftRotate(Node parent) {
        Node nRight = parent.right;
        Node bufferParent = parent;
        Node bufferChild = nRight.left;
        parent = nRight;
        parent.left = bufferParent;
        parent.left.right = bufferChild;
    }

    void rebalance(Node node) {
        while (node != treeRoot && node.parent != null && node.parent.color == Color.RED) {
            if (node.parent.parent != null && node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                }
                else if (uncle != null) {
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rightRotate(node.parent.parent);
                }
            }
            else if (node.parent.parent != null) {
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                }
                else if (uncle != null) {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    leftRotate(node.parent.parent);
                }
            }
        }
        treeRoot.color = Color.BLACK;
    }

    Node insert(Node node, Node parent, Integer key, String value) {
        if (node == null) {
            return new Node(key, value, parent);
        }

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

    void getToKeyNodes(MyRbMap tree, Node node, Integer keyTo) {
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

    void getFromKeyNodes(MyRbMap tree, Node node, Integer keyFrom) {
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

    boolean findValue(Node node, String value) {
        if (node != null && (value.equals(node.value) || findValue(node.left, value) || findValue(node.right, value)))
            return true;
        return false;
    }

    void transplantNode(Node parentNodeTo, Node nodeTo, Node nodeFrom) {
        if (nodeTo == treeRoot)
            treeRoot = nodeFrom;
        else if (nodeTo != null && nodeTo == parentNodeTo.left)
            parentNodeTo.left = nodeFrom;
        else if (nodeTo != null) {
            parentNodeTo.right = nodeFrom;
        }
    }

    void deleteFix(Node node) {
        while (node != treeRoot && node.color == Color.BLACK) {
            Node brother;
            if (node == node.parent.left) {
                brother = node.parent.right;
                if (brother.color == Color.RED) {
                    brother.color = Color.BLACK;
                    node.parent.color = Color.RED;
                    leftRotate(node.parent);
                    brother = node.parent.right;
                }

                if (brother.left.color == Color.BLACK && brother.right.color == Color.BLACK) {
                    brother.color = Color.RED;
                    node = node.parent;
                }
                else {
                    if (brother.right.color == Color.BLACK) {
                        brother.left.color = Color.BLACK;
                        brother.color = Color.RED;
                        rightRotate(brother);
                        brother = node.parent.right;
                    }
                    brother.color = node.parent.color;
                    node.parent.color = Color.BLACK;
                    brother.right.color = Color.BLACK;
                    leftRotate(node.parent);
                    node = treeRoot;
                }
            }
            else {
                brother = node.parent.left;
                if (brother.color == Color.RED) {
                    brother.color = Color.BLACK;
                    node.parent.color = Color.RED;
                    rightRotate(node.parent);
                    brother = node.parent.left;
                }

                if (brother.left.color == Color.BLACK && brother.right.color == Color.BLACK) {
                    rightRotate(node.parent);
                    brother = node.parent.left;
                }
                else {
                    if (brother.left.color == Color.BLACK){
                        brother.right.color = Color.BLACK;
                        brother.color = Color.RED;
                        leftRotate(brother);
                        brother = node.parent.left;
                    }
                    brother.color = node.parent.color;
                    node.parent.color = Color.BLACK;
                    brother.left.color = Color.BLACK;
                    rightRotate(node.parent);
                    node = treeRoot;
                }
            }
        }
        node.color = Color.BLACK;
    }

    void delete(Integer key) {

        Node delNode = null, parent = null, tempNode = treeRoot;
        while (tempNode != null) {
            if (tempNode.key.compareTo(key) == 0) {
                delNode = tempNode;
                break;
            }
            parent = tempNode;
            tempNode = tempNode.key.compareTo(key) < 0 ? tempNode.right : tempNode.left;
        }

        if (delNode != null) {
            Node child = null;
            Color bufferColor = delNode.color;
            if (delNode.left == null ^ delNode.right == null) {
                child = delNode.left == null ? delNode.right : delNode.left;
                transplantNode(parent, delNode, child);
            }
            else {
                Node minNode = getMin(delNode.right);
                if (minNode != null) {
                    delNode.key = minNode.key;
                    delNode.value = minNode.value;
                    bufferColor = minNode.color;
                    child = minNode.left == null ? minNode.right : minNode.left;
                    if (child == null) {
                        child = delNode.right;
                        Node tempMin = delNode.right, tempPar = null;
                        while (tempMin.left != null) {
                            tempPar = tempMin;
                            tempMin = tempMin.left;
                        }
                        tempPar.left =  null;
                    }
                    transplantNode(delNode, minNode, child);
                }
                else {
                    if (parent.key.compareTo(key) > 0)
                        parent.left = null;
                    else parent.right = null;
                }
            }

            if (bufferColor == Color.BLACK)
                deleteFix(child);
        }
    }

    Node findNode(Integer key) {
        Node needNode = treeRoot;
        while (!needNode.key.equals(key)) {
            needNode = needNode.key.compareTo(key) > 0 ? needNode.left : needNode.right;
        }

        return needNode;
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
    public Integer firstKey() {
        return getMin(treeRoot).key;
    }

    @Override
    public Integer lastKey() {
        return getMax(treeRoot).key;
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
        treeRoot = insert(treeRoot, null, key, value);

        System.out.println(treeRoot.key);
        return tempValue;
    }

    @Override
    public String remove(Object key) {
        String tempValue = get(key);

        if (tempValue != null) {
            size--;
            delete((Integer)key);
        }

        return tempValue;
    }

    @Override
    public void clear() {
        treeRoot = null;
        size = 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subTree = new MyRbMap();
        getToKeyNodes(subTree, treeRoot, toKey);
        return subTree;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subTree = new MyRbMap();
        getFromKeyNodes(subTree, treeRoot, fromKey);
        return subTree;
    }

    /////////////////////////////

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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }
}
