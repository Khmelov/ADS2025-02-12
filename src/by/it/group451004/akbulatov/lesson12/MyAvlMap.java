package by.it.group451004.akbulatov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String>
{
    private static class Node
    {
        String value;
        Integer key;

        int height;

        Node left;
        Node right;

        Node(Integer key, String value)
        {
            this.value = value;
            this.height = 1;;
            this.key = key;
        }
    }

    private Node root;
    private int size = 0;

    private String oldNodeValue;

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public void clear()
    {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key)
    {
        return getNode(root, (Integer) key) != null;
    }

    @Override
    public String get(Object key)
    {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return (node == null) ? null : node.value;
    }

    @Override
    public String put(Integer key, String value)
    {
        oldNodeValue = null;
        root = insert(root, key, value);
        if (oldNodeValue == null) size++;
        return oldNodeValue;
    }

    @Override
    public String remove(Object key)
    {
        if (!(key instanceof Integer)) return null;
        oldNodeValue = null;

        if (containsKey(key))
        {
            root = delete(root, (Integer) key);
            size--;
        }

        return oldNodeValue;
    }

    @Override
    public String toString()
    {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        inOrderTraversal(root, sb);

        // delete the ", " at the end
        sb.setLength(sb.length() - 2);
        sb.append("}");

        return sb.toString();
    }

    private int height(Node N)
    {
        if (N == null) return 0;
        return N.height;
    }

    private int getBalance(Node N)
    {
        if (N == null) return 0;
        return height(N.left) - height(N.right);
    }

    private void updateHeight(Node N)
    {
        N.height = Math.max(height(N.left), height(N.right)) + 1;
    }

    private Node getNode(Node node, Integer key)
    {
        if (node == null) return null;

        if (key.equals(node.key)) return node;

        if (key < node.key)
            return getNode(node.left, key);
        else
            return getNode(node.right, key);
    }

    private Node rightRotate(Node root)
    {
        Node newRoot = root.left;
        Node tmp = newRoot.right;

        newRoot.right = root;
        root.left = tmp;

        updateHeight(root);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node leftRotate(Node root)
    {
        Node newRoot = root.right;
        Node tmp = newRoot.left;

        newRoot.left = root;
        root.right = tmp;

        updateHeight(root);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node insert(Node node, Integer key, String value)
    {
        if (node == null)
            return new Node(key, value);

        if (key < node.key)
            node.left = insert(node.left, key, value);
        else if (key > node.key)
            node.right = insert(node.right, key, value);
        else
        {
            oldNodeValue = node.value;
            node.value = value;
            return node;
        }

        updateHeight(node);

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.left.key)
        {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Right Left Case
        if (balance < -1 && key < node.right.key)
        {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private Node delete(Node root, Integer key)
    {
        if (root == null) return root;

        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            oldNodeValue = root.value;

            if ((root.left == null) || (root.right == null))
            {
                Node temp = (root.left == null) ? root.right : root.left;
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else
                    root = temp;
            }
            else
            {
                Node inOrdSucc = minValueNode(root.right);
                root.value = inOrdSucc.value;
                root.key = inOrdSucc.key;

                String nodeValue = oldNodeValue;
                root.right = delete(root.right, inOrdSucc.key);
                oldNodeValue = nodeValue;
            }
        }

        if (root == null) return root;

        updateHeight(root);

        int balance = getBalance(root);

        // Left Left
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node minValueNode(Node node)
    {
        Node current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    private void inOrderTraversal(Node node, StringBuilder sb)
    {
        if (node != null)
        {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // --- Заглушки для остальных методов интерфейса Map (чтобы код компилировался) ---
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
}