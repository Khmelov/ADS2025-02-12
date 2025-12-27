package by.it.group451003.sirotkin.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private Node root;
    private int size = 0;

    public MyAvlMap() {
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
            return oldValue[0];
        }
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            ++this.size;
            return new Node(key, value);
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

            return this.balance(node);
        }
    }

    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Integer)) {
            return null;
        } else {
            Integer intKey = (Integer)key;
            String[] removedValue = new String[1];
            this.root = this.remove(this.root, intKey, removedValue);
            return removedValue[0];
        }
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        } else {
            if (key < node.key) {
                node.left = this.remove(node.left, key, removedValue);
            } else if (key > node.key) {
                node.right = this.remove(node.right, key, removedValue);
            } else {
                removedValue[0] = node.value;
                --this.size;
                if (node.left == null) {
                    return node.right;
                }

                if (node.right == null) {
                    return node.left;
                }

                Node minNode = this.findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = this.remove(node.right, minNode.key, new String[1]);
            }

            return this.balance(node);
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

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : this.height(node.left) - this.height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = Math.max(this.height(node.left), this.height(node.right)) + 1;
        }

    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;
        x.right = y;
        y.left = T;
        this.updateHeight(y);
        this.updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;
        y.left = x;
        x.right = T;
        this.updateHeight(x);
        this.updateHeight(y);
        return y;
    }

    private Node balance(Node node) {
        if (node == null) {
            return null;
        } else {
            this.updateHeight(node);
            int balance = this.balanceFactor(node);
            if (balance > 1) {
                if (this.balanceFactor(node.left) < 0) {
                    node.left = this.rotateLeft(node.left);
                }

                return this.rotateRight(node);
            } else if (balance < -1) {
                if (this.balanceFactor(node.right) > 0) {
                    node.right = this.rotateRight(node.right);
                }

                return this.rotateLeft(node);
            } else {
                return node;
            }
        }
    }

    private Node findMin(Node node) {
        while(node.left != null) {
            node = node.left;
        }

        return node;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
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

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }
}
