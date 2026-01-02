package by.it.group451002.koltsov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    RBNode root;
    int size = 0;

    @Override
    public java.lang.String toString() {
        StringBuilder resStr = new StringBuilder("{");
        if (root != null)
            root.appendElemToString(resStr);
        return resStr + "}";
    }

    public RBNode grandparent(RBNode node)
    {
        if (node != null && node.parent != null)
            return node.parent.parent;
        return null;
    }

    public RBNode uncle(RBNode node)
    {
        RBNode grand = grandparent(node);
        if (grand != null)
        {
            if (grand.left == node.parent)
                return grand.right;
            return grand.left;
        }
        return null;
    }

    public void rotate_left(RBNode node)
    {
        RBNode pivot = node.right;

        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node)
                node.parent.left = pivot;
            else
                node.parent.right = pivot;
        }
        else
            root = pivot;

        node.right = pivot.left;
        if (pivot.left != null)
            pivot.left.parent = node;

        node.parent = pivot;
        pivot.left = node;
    }

    public void rotate_right(RBNode node)
    {
        RBNode pivot = node.left;

        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node)
                node.parent.left = pivot;
            else
                node.parent.right = pivot;
        }
        else
            root = pivot;

        node.left = pivot.right;
        if (pivot.right != null)
            pivot.right.parent = node;

        node.parent = pivot;
        pivot.right = node;
    }

    void insert_case1(RBNode node)
    {
        if (node.parent == null) {
            node.color = Color.BLACK;
            root = node;
        }
        else
            insert_case2(node);
    }

    public void insert_case2(RBNode node)
    {
        if (node.parent.color == Color.BLACK)
            return;
        else
            insert_case3(node);
    }

    void insert_case3(RBNode node)
    {
        RBNode uncle = uncle(node);

        if ((uncle != null) && (uncle.color == Color.RED)) {
            node.parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            RBNode grand = grandparent(node);
            grand.color = Color.RED;
            insert_case1(grand);
        } else {
            insert_case4(node);
        }
    }

    public void insert_case4(RBNode node)
    {
        RBNode grand = grandparent(node);

        if ((node == node.parent.right) && (node.parent == grand.left)) {
            rotate_left(node.parent);
            node = node.left;
        } else if ((node == node.parent.left) && (node.parent == grand.right)) {
            rotate_right(node.parent);
            node = node.right;
        }
        insert_case5(node);
    }

    public void insert_case5(RBNode node)
    {
        RBNode grand = grandparent(node);

        node.parent.color = Color.BLACK;
        grand.color = Color.RED;
        if ((node == node.parent.left) && (node.parent == grand.left)) {
            rotate_right(grand);
        } else {
            rotate_left(grand);
        }
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        SortedMap<Integer, String> resMap = new MyRbMap();
        root.addValuesToHeadMap(resMap, toKey);
        return resMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        SortedMap<Integer, String> resMap = new MyRbMap();
        root.addValuesToTailMap(resMap, fromKey);
        return resMap;
    }

    @Override
    public Integer firstKey() {
        if (root == null)
            return null;
        RBNode tempNode = root;
        while (tempNode.left != null)
            tempNode = tempNode.left;
        return tempNode.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null)
            return null;
        RBNode tempNode = root;
        while (tempNode.right != null)
            tempNode = tempNode.right;
        return tempNode.key;
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
        return getNodeByKey(key) != null;
    }

    public RBNode getNodeByKey(Object Key)
    {
        Integer key = (Integer) Key;
        RBNode Node = root;
        while (true)
        {
            if ((Integer)key < Node.key)
            {
                if (Node.left != null)
                    Node = Node.left;
                else
                    return null;
            }
            else if ((Integer)key > Node.key)
            {
                if (Node.right != null)
                    Node = Node.right;
                else
                    return null;
            }
            else
                break;
        }
        return Node;
    }

    @Override
    public boolean containsValue(Object value) {
        return root.getNodePtrByValue(value) != null;
    }

    @Override
    public String get(Object key) {
        RBNode node = getNodeByKey(key);
        if (node == null)
            return null;
        return getNodeByKey(key).value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new RBNode(key, value, null, Color.RED);
            size++;
            insert_case1(root);
            return null;
        }
        else {
            RBNode tempNode = root;
            while (true)
            {
                if (key < tempNode.key) {
                    if (tempNode.left == null)
                    {
                        tempNode.left = new RBNode(key, value, tempNode, Color.RED);
                        insert_case1(tempNode.left);
                        size++;
                        return null;
                    }
                    else
                        tempNode = tempNode.left;
                } else if (key > tempNode.key) {
                    if (tempNode.right == null)
                    {
                        tempNode.right = new RBNode(key, value, tempNode, Color.RED);
                        insert_case1(tempNode.right);
                        size++;
                        return null;
                    }
                    else
                        tempNode = tempNode.right;
                } else {
                    String prevValue = tempNode.value;
                    tempNode.value = value;
                    return prevValue;
                }
            }
        }
    }

    public RBNode sibling(RBNode node)
    {
        if (node == node.parent.left)
            return node.parent.right;
        else
            return node.parent.left;
    }

    public void replace_node(RBNode node, RBNode child) {
        if (child != null) {
            child.parent = node.parent;
        }

        if (node.parent != null) {
            if (node == node.parent.left) {
                node.parent.left = child;
            } else {
                node.parent.right = child;
            }
        }
        else
            root = null;
    }

    public boolean is_leaf(RBNode node)
    {
        return node == null;
    }

    public void delete_one_child(RBNode node)
    {
        RBNode child = is_leaf(node.right) ? node.left : node.right;

        replace_node(node, child);

        if (root == null)
            return;

        if (node.color == Color.BLACK) {
            if (child.color == Color.RED)
                child.color = Color.BLACK;
            else
                delete_case1(child);
        }
    }

    public void delete_case1(RBNode node)
    {
        if (node.parent != null)
            delete_case2(node);
    }

    void delete_case2(RBNode node)
    {
        RBNode sibling = sibling(node);

        if (sibling.color == Color.RED) {
            node.parent.color = Color.RED;
            sibling.color = Color.BLACK;
            if (node == node.parent.left)
                rotate_left(node.parent);
            else
                rotate_right(node.parent);
        }
        delete_case3(node);
    }

    public void delete_case3(RBNode node)
    {
        RBNode sibling = sibling(node);

        if  (
            (node.parent.color == Color.BLACK) &&
            (sibling.color == Color.BLACK) &&
            (sibling.left.color == Color.BLACK) &&
            (sibling.right.color == Color.BLACK)
        )
        {
            sibling.color = Color.RED;
            delete_case1(node.parent);
        } else
            delete_case4(node);
    }

    public void delete_case4(RBNode node)
    {
        RBNode sibling = sibling(node);

        if (
                (node.parent.color == Color.RED) &&
                (sibling.color == Color.BLACK) &&
                (sibling.left.color == Color.BLACK) &&
                (sibling.right.color == Color.BLACK)
        )
        {
            sibling.color = Color.RED;
            node.parent.color = Color.BLACK;
        } else
            delete_case5(node);
    }

    public void delete_case5(RBNode node)
    {
        RBNode sibling = sibling(node);

        if  (sibling.color == Color.BLACK) {
            if (
                    (node == node.parent.left) &&
                    (sibling.right.color == Color.BLACK) &&
                    (sibling.left.color == Color.RED)
            )
            {
                sibling.color = Color.RED;
                sibling.left.color = Color.BLACK;
                rotate_right(sibling);
            } else if (
                    (node == node.parent.right) &&
                    (sibling.left.color == Color.BLACK) &&
                    (sibling.right.color == Color.RED)
            )
            {
                sibling.color = Color.RED;
                sibling.right.color = Color.BLACK;
                rotate_left(sibling);
            }
        }
        delete_case6(node);
    }

    public void delete_case6(RBNode node)
    {
        RBNode sibling = sibling(node);

        sibling.color = node.parent.color;
        node.parent.color = Color.BLACK;

        if (node == node.parent.left) {
            sibling.right.color = Color.BLACK;
            rotate_left(node.parent);
        } else {
            sibling.left.color = Color.BLACK;
            rotate_right(node.parent);
        }
    }

    @Override
    public String remove(Object key) {
        RBNode deleteNode = getNodeByKey(key);
        if (deleteNode == null)
            return null;

        String returnValue = deleteNode.value;

        // В deleteNode указатель на удаляемый элемент
        if (deleteNode.left == null || deleteNode.right == null)
        {
            delete_one_child(deleteNode);
        }
        else
        {
            // Значит ребёнка два
            RBNode replaceNode =  deleteNode.left;
            while (replaceNode.right != null)
                replaceNode = replaceNode.right;
            // в replaceNode самый большой элемент правого поддерева
            deleteNode.key = replaceNode.key;
            deleteNode.value = replaceNode.value;

            delete_one_child(replaceNode);
        }
        size--;
        return returnValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
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