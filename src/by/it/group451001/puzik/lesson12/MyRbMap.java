package by.it.group451001.puzik.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color = BLACK;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root = null;
    private int size = 0;

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append('}');
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append('=').append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMapHelper(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMapHelper(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMapHelper(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMapHelper(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node node = getNode((Integer) key);
        return node != null ? node.value : null;
    }

    private Node getNode(Integer key) {
        Node node = root;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        Node node = root;
        Node parent = null;

        // Find insertion point
        while (node != null) {
            parent = node;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                // Key exists, update value
                String oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        // Create new node
        Node newNode = new Node(key, value);
        newNode.color = RED;

        if (parent == null) {
            root = newNode;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        size++;
        fixInsert(newNode);
        return null;
    }

    private void fixInsert(Node node) {
        while (node != root && node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    // Case 1: Uncle is red - recolor
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    // Case 2: Uncle is black
                    if (node == node.parent.right) {
                        // Case 2a: Left-right case
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 2b: Left-left case
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Symmetric case for right child
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    // Case 1: Uncle is red - recolor
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    // Case 2: Uncle is black
                    if (node == node.parent.left) {
                        // Case 2a: Right-left case
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Case 2b: Right-right case
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    @Override
    public String remove(Object key) {
        Node node = getNode((Integer) key);
        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node node) {
        // If node has two children, replace with successor
        if (node.left != null && node.right != null) {
            Node successor = findMin(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }

        // Node has at most one child
        Node child = (node.left != null) ? node.left : node.right;
        boolean isBlack = node.color == BLACK;

        if (child != null) {
            // Replace node with its child
            replaceNode(node, child);
            if (isBlack) {
                fixDelete(child);
            }
        } else if (node.parent == null) {
            // Node is root
            root = null;
        } else {
            // Node is a leaf
            if (isBlack) {
                fixDelete(node);
            }
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else {
                    node.parent.right = null;
                }
            }
        }
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void replaceNode(Node oldNode, Node newNode) {
        if (oldNode.parent == null) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }
        if (newNode != null) {
            newNode.parent = oldNode.parent;
        }
    }

    private void fixDelete(Node node) {
        while (node != root && (node == null || node.color == BLACK)) {
            if (node != null && node.parent != null && node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling != null && sibling.color == RED) {
                    // Case 1: Sibling is red
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if (sibling != null &&
                    (sibling.left == null || sibling.left.color == BLACK) &&
                    (sibling.right == null || sibling.right.color == BLACK)) {
                    // Case 2: Sibling and its children are black
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling != null && (sibling.right == null || sibling.right.color == BLACK)) {
                        // Case 3: Sibling's right child is black
                        if (sibling.left != null) {
                            sibling.left.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    // Case 4: Sibling's right child is red
                    if (sibling != null) {
                        sibling.color = node.parent.color;
                    }
                    if (node.parent != null) {
                        node.parent.color = BLACK;
                    }
                    if (sibling != null && sibling.right != null) {
                        sibling.right.color = BLACK;
                    }
                    if (node.parent != null) {
                        rotateLeft(node.parent);
                    }
                    node = root;
                }
            } else if (node != null && node.parent != null) {
                // Symmetric case for right child
                Node sibling = node.parent.left;
                if (sibling != null && sibling.color == RED) {
                    // Case 1: Sibling is red
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling != null &&
                    (sibling.right == null || sibling.right.color == BLACK) &&
                    (sibling.left == null || sibling.left.color == BLACK)) {
                    // Case 2: Sibling and its children are black
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling != null && (sibling.left == null || sibling.left.color == BLACK)) {
                        // Case 3: Sibling's left child is black
                        if (sibling.right != null) {
                            sibling.right.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    // Case 4: Sibling's left child is red
                    if (sibling != null) {
                        sibling.color = node.parent.color;
                    }
                    if (node.parent != null) {
                        node.parent.color = BLACK;
                    }
                    if (sibling != null && sibling.left != null) {
                        sibling.left.color = BLACK;
                    }
                    if (node.parent != null) {
                        rotateRight(node.parent);
                    }
                    node = root;
                }
            } else {
                break;
            }
        }
        if (node != null) {
            node.color = BLACK;
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
