package by.it.group451003.sirotkin.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;
    private int size = 0;

    public MyRbMap() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        this.toStringInOrder(this.root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }

    private void toStringInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            this.toStringInOrder(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            this.toStringInOrder(node.right, sb);
        }

    }

    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        } else {
            String[] oldValue = new String[1];
            this.root = this.put(this.root, key, value, oldValue);
            this.root.color = false;
            return oldValue[0];
        }
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            ++this.size;
            return new Node(key, value, true);
        } else {
            if (key < node.key) {
                node.left = this.put(node.left, key, value, oldValue);
            } else {
                if (key <= node.key) {
                    oldValue[0] = node.value;
                    node.value = value;
                    return node;
                }

                node.right = this.put(node.right, key, value, oldValue);
            }

            if (this.isRed(node.right) && !this.isRed(node.left)) {
                node = this.rotateLeft(node);
            }

            if (this.isRed(node.left) && this.isRed(node.left.left)) {
                node = this.rotateRight(node);
            }

            if (this.isRed(node.left) && this.isRed(node.right)) {
                this.flipColors(node);
            }

            return node;
        }
    }

    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Integer)) {
            return null;
        } else {
            Integer intKey = (Integer)key;
            Node nodeToRemove = this.findNode(this.root, intKey);
            if (nodeToRemove != null && nodeToRemove.key.equals(intKey)) {
                String removedValue = nodeToRemove.value;
                if (!this.isRed(this.root.left) && !this.isRed(this.root.right)) {
                    this.root.color = true;
                }

                this.root = this.remove(this.root, intKey);
                if (!this.isEmpty()) {
                    this.root.color = false;
                }

                --this.size;
                return removedValue;
            } else {
                return null;
            }
        }
    }

    private Node remove(Node node, Integer key) {
        if (key < node.key) {
            if (!this.isRed(node.left) && !this.isRed(node.left.left)) {
                node = this.moveRedLeft(node);
            }

            node.left = this.remove(node.left, key);
        } else {
            if (this.isRed(node.left)) {
                node = this.rotateRight(node);
            }

            if (key.equals(node.key) && node.right == null) {
                return null;
            }

            if (!this.isRed(node.right) && !this.isRed(node.right.left)) {
                node = this.moveRedRight(node);
            }

            if (key.equals(node.key)) {
                Node minNode = this.findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = this.deleteMin(node.right);
            } else {
                node.right = this.remove(node.right, key);
            }
        }

        return this.balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return null;
        } else {
            if (!this.isRed(node.left) && !this.isRed(node.left.left)) {
                node = this.moveRedLeft(node);
            }

            node.left = this.deleteMin(node.left);
            return this.balance(node);
        }
    }

    private Node findNode(Node node, Integer key) {
        if (node == null) {
            return null;
        } else if (key < node.key) {
            return this.findNode(node.left, key);
        } else {
            return key > node.key ? this.findNode(node.right, key) : node;
        }
    }

    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Integer)) {
            return null;
        } else {
            Integer intKey = (Integer)key;
            return this.get(this.root, intKey);
        }
    }

    private String get(Node node, Integer key) {
        if (node == null) {
            return null;
        } else if (key < node.key) {
            return this.get(node.left, key);
        } else {
            return key > node.key ? this.get(node.right, key) : node.value;
        }
    }

    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    public boolean containsValue(Object value) {
        return this.containsValue(this.root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        } else if (value == null && node.value == null || value != null && value.equals(node.value)) {
            return true;
        } else {
            return this.containsValue(node.left, value) || this.containsValue(node.right, value);
        }
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap();
        this.headMap(this.root, toKey, headMap);
        return headMap;
    }

    private void headMap(Node node, Integer toKey, MyRbMap headMap) {
        if (node != null) {
            this.headMap(node.left, toKey, headMap);
            if (node.key < toKey) {
                headMap.put(node.key, node.value);
            }

            this.headMap(node.right, toKey, headMap);
        }

    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        this.tailMap(this.root, fromKey, tailMap);
        return tailMap;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node != null) {
            this.tailMap(node.left, fromKey, tailMap);
            if (node.key >= fromKey) {
                tailMap.put(node.key, node.value);
            }

            this.tailMap(node.right, fromKey, tailMap);
        }

    }

    public Integer firstKey() {
        if (this.root == null) {
            throw new NoSuchElementException();
        } else {
            return this.findMin(this.root).key;
        }
    }

    public Integer lastKey() {
        if (this.root == null) {
            throw new NoSuchElementException();
        } else {
            return this.findMax(this.root).key;
        }
    }

    private Node findMin(Node node) {
        while(node.left != null) {
            node = node.left;
        }

        return node;
    }

    private Node findMax(Node node) {
        while(node.right != null) {
            node = node.right;
        }

        return node;
    }

    private boolean isRed(Node node) {
        return node != null && node.color;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = true;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = true;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node h) {
        this.flipColors(h);
        if (this.isRed(h.right.left)) {
            h.right = this.rotateRight(h.right);
            h = this.rotateLeft(h);
            this.flipColors(h);
        }

        return h;
    }

    private Node moveRedRight(Node h) {
        this.flipColors(h);
        if (this.isRed(h.left.left)) {
            h = this.rotateRight(h);
            this.flipColors(h);
        }

        return h;
    }

    private Node balance(Node h) {
        if (this.isRed(h.right)) {
            h = this.rotateLeft(h);
        }

        if (this.isRed(h.left) && this.isRed(h.left.left)) {
            h = this.rotateRight(h);
        }

        if (this.isRed(h.left) && this.isRed(h.right)) {
            this.flipColors(h);
        }

        return h;
    }

    public Comparator<? super Integer> comparator() {
        return null;
    }

    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }
}