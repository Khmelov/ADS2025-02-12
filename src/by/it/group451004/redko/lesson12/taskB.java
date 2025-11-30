package by.it.group451004.redko.lesson12;

import java.util.*;

public class taskB {
}

class MyRbMap implements SortedMap<Integer, String> {
    
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;
        
        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }
    
    private Node root;
    private int size;
    
    public MyRbMap() {
        this.root = null;
        this.size = 0;
    }
    
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        if (root == null) {
            root = new Node(key, value, BLACK);
            size++;
            return null;
        }
        
        Node parent = null;
        Node current = root;
        int cmp = 0;
        
        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }
        
        Node newNode = new Node(key, value, RED);
        newNode.parent = parent;
        
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        
        size++;
        fixAfterInsertion(newNode);
        return null;
    }
    
    private void fixAfterInsertion(Node node) {
        while (node != root && node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }
    
    private void rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        
        if (right.left != null) {
            right.left.parent = node;
        }
        
        right.parent = node.parent;
        
        if (node.parent == null) {
            root = right;
        } else if (node == node.parent.left) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }
        
        right.left = node;
        node.parent = right;
    }
    
    private void rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        
        if (left.right != null) {
            left.right.parent = node;
        }
        
        left.parent = node.parent;
        
        if (node.parent == null) {
            root = left;
        } else if (node == node.parent.right) {
            node.parent.right = left;
        } else {
            node.parent.left = left;
        }
        
        left.right = node;
        node.parent = left;
    }
    
    @Override
    public String get(Object key) {
        if (key == null) {
            return null;
        }
        
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
    }
    
    private Node getNode(Integer key) {
        Node current = root;
        
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        
        return null;
    }
    
    @Override
    public String remove(Object key) {
        if (key == null) {
            return null;
        }
        
        Node node = getNode((Integer) key);
        if (node == null) {
            return null;
        }
        
        String oldValue = node.value;
        deleteNode(node);
        return oldValue;
    }
    
    private void deleteNode(Node node) {
        size--;
        
        if (node.left != null && node.right != null) {
            Node successor = findMin(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }
        
        Node replacement = (node.left != null) ? node.left : node.right;
        
        if (replacement != null) {
            replacement.parent = node.parent;
            
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            
            node.left = node.right = node.parent = null;
            
            if (node.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == BLACK) {
                fixAfterDeletion(node);
            }
            
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
    }
    
    private void fixAfterDeletion(Node node) {
        while (node != root && colorOf(node) == BLACK) {
            if (node == leftOf(parentOf(node))) {
                Node sibling = rightOf(parentOf(node));
                
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    rotateLeft(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }
                
                if (colorOf(leftOf(sibling)) == BLACK && colorOf(rightOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(rightOf(sibling)) == BLACK) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    rotateLeft(parentOf(node));
                    node = root;
                }
            } else {
                Node sibling = leftOf(parentOf(node));
                
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    rotateRight(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }
                
                if (colorOf(rightOf(sibling)) == BLACK && colorOf(leftOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(leftOf(sibling)) == BLACK) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rotateRight(parentOf(node));
                    node = root;
                }
            }
        }
        
        setColor(node, BLACK);
    }
    
    private boolean colorOf(Node node) {
        return node == null ? BLACK : node.color;
    }
    
    private Node parentOf(Node node) {
        return node == null ? null : node.parent;
    }
    
    private Node leftOf(Node node) {
        return node == null ? null : node.left;
    }
    
    private Node rightOf(Node node) {
        return node == null ? null : node.right;
    }
    
    private void setColor(Node node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }
    
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return getNode((Integer) key) != null;
    }
    
    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }
    
    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) {
            return false;
        }
        
        if (Objects.equals(node.value, value)) {
            return true;
        }
        
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
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
    public void clear() {
        root = null;
        size = 0;
    }
    
    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return findMin(root).key;
    }
    
    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return findMax(root).key;
    }
    
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapHelper(root, toKey, result);
        return result;
    }
    
    private void headMapHelper(Node node, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }
        
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            headMapHelper(node.left, toKey, result);
            result.put(node.key, node.value);
            headMapHelper(node.right, toKey, result);
        } else {
            headMapHelper(node.left, toKey, result);
        }
    }
    
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }
    
    private void tailMapHelper(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }
        
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            tailMapHelper(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMapHelper(node.right, fromKey, result);
        } else {
            tailMapHelper(node.right, fromKey, result);
        }
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        
        StringBuilder sb = new StringBuilder("{");
        toStringHelper(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
    
    private void toStringHelper(Node node, StringBuilder sb) {
        if (node != null) {
            toStringHelper(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringHelper(node.right, sb);
        }
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

