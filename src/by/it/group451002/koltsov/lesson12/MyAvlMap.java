package by.it.group451002.koltsov.lesson12;

import java.util.*;

public class MyAvlMap<Integer, String> implements Map<Integer, String> {
    AvlNode root;
    int size = 0;

    @Override
    public java.lang.String toString() {
        StringBuilder resStr = new StringBuilder("{");
        if (root != null)
            root.appendElemToString(resStr);
        return resStr + "}";
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
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return root.containsValue(value);
    }

    @Override
    public String get(Object key) {
        AvlNode tempNode = root;
        while (true) {
            if (tempNode != null) {
                if ((int)key < tempNode.key)
                    tempNode = tempNode.left;
                else if ((int)key > tempNode.key)
                    tempNode = tempNode.right;
                else
                    return (String) tempNode.value;
            }
            else
                return null;
        }
    }

    @Override
    public String put(Integer key, String value) {
        boolean isAdded =  false;
        if (root == null) {
            root = new AvlNode((java.lang.Integer) key, (java.lang.String) value, null);
            isAdded = true;
            size++;
        }
        else {
            AvlNode tempNode = root;
            do {
                if ((java.lang.Integer) key < tempNode.key) {
                    if (tempNode.left == null) {
                        tempNode.left = new AvlNode((java.lang.Integer) key, (java.lang.String) value, tempNode);
                        isAdded = true;
                        size++;
                        tempNode.left.checkHeight();
                    }
                    else {
                        tempNode = tempNode.left;
                    }
                }
                else if ((java.lang.Integer) key > tempNode.key) {
                    if (tempNode.right == null) {
                        tempNode.right = new AvlNode((java.lang.Integer) key, (java.lang.String) value, tempNode);
                        isAdded = true;
                        size++;
                        tempNode.right.checkHeight();
                    }
                    else {
                        tempNode = tempNode.right;
                    }
                }
                else
                {
                    String temp = (String) tempNode.value;
                    tempNode.value = (java.lang.String) value;
                    return temp;
                }
            } while (!isAdded);
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        AvlNode elemToRemove = getNodePtrByKey((Integer) key);
        if (elemToRemove == null)
            return null;
        if (elemToRemove.left == null && elemToRemove.right == null) {
            if (elemToRemove.parent != null)
            {
                if (elemToRemove.parent.right == elemToRemove)
                    elemToRemove.parent.right = null;
                else
                    elemToRemove.parent.left = null;
                elemToRemove.parent.checkHeightAfterRemove();
            }
            size--;
            return (String) elemToRemove.value;
        } else if (elemToRemove.left != null && elemToRemove.left.right == null || elemToRemove.right == null) {
            elemToRemove.left.parent = elemToRemove.parent;
            if (elemToRemove.right != null) {
                elemToRemove.left.right = elemToRemove.right;
                elemToRemove.right.parent = elemToRemove.left;
            }
            if (elemToRemove.parent != null)
            {
                if (elemToRemove.parent.right == elemToRemove)
                    elemToRemove.parent.right = elemToRemove.left;
                else
                    elemToRemove.parent.left = elemToRemove.left;
                elemToRemove.parent.checkHeightAfterRemove();
            }
            else {
                root = elemToRemove.left;
                root.checkHeightAfterRemove();
            }
            size--;
            return (String) elemToRemove.value;
        } else if (elemToRemove.right != null && elemToRemove.right.left == null || elemToRemove.left == null) {
            elemToRemove.right.parent = elemToRemove.parent;
            if (elemToRemove.left != null) {
                elemToRemove.right.left = elemToRemove.left;
                elemToRemove.left.parent = elemToRemove.right;
            }
            if (elemToRemove.parent != null)
            {
                if (elemToRemove.parent.right == elemToRemove)
                    elemToRemove.parent.right = elemToRemove.right;
                else
                    elemToRemove.parent.left = elemToRemove.right;
                elemToRemove.parent.checkHeightAfterRemove();
            }
            else {
                root = elemToRemove.right;
                root.checkHeightAfterRemove();
            }
            size--;
            return (String) elemToRemove.value;
        } else {
            // Значит просто подтянуть какого-нибудь ребёнка не получится. В таком случае ищем узел с самым большим ключём из правого поддерева
            // И вставляем его на место удаляемого элемента
            AvlNode tempNode = elemToRemove.left.right;
            while (tempNode.right != null)
                tempNode = tempNode.right;
            if (tempNode.left != null)
            {
                tempNode.left.parent = tempNode.parent;
                tempNode.parent.right = tempNode.left;
            }
            else
                tempNode.parent.right = null;
            String tempValue = (String) elemToRemove.value;
            elemToRemove.key = tempNode.key;
            elemToRemove.value = tempNode.value;
            size--;
            tempNode.parent.checkHeightAfterRemove();
            return tempValue;
        }
    }

    public AvlNode getNodePtrByKey(Integer value) {
        return root.getNodePtrByKey((java.lang.Integer) value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
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