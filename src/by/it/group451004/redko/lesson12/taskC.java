package by.it.group451004.redko.lesson12;

import java.util.*;

public class taskC {
}

class MySplayMap implements NavigableMap<Integer, String> {
    
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Node root;
    private int size;
    
    public MySplayMap() {
        this.root = null;
        this.size = 0;
    }
    
    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
            
            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node;
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
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        if (root == null) {
            root = new Node(key, value);
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
                splay(current);
                return oldValue;
            }
        }
        
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        
        size++;
        splay(newNode);
        return null;
    }
    
    @Override
    public String get(Object key) {
        if (key == null) {
            return null;
        }
        
        Node node = getNode((Integer) key);
        if (node != null) {
            splay(node);
            return node.value;
        }
        return null;
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
        splay(node);
        
        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node right = node.right;
            root = node.left;
            root.parent = null;
            Node maxNode = findMax(root);
            splay(maxNode);
            root.right = right;
            if (right != null) {
                right.parent = root;
            }
        }
        
        size--;
        return oldValue;
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        Node node = getNode((Integer) key);
        if (node != null) {
            splay(node);
            return true;
        }
        return false;
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
        Node node = findMin(root);
        splay(node);
        return node.key;
    }
    
    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMax(root);
        splay(node);
        return node.key;
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
    public Integer lowerKey(Integer key) {
        Node result = lowerNode(key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }
    
    private Node lowerNode(Integer key) {
        Node current = root;
        Node result = null;
        
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                current = current.left;
            } else {
                result = current;
                current = current.right;
            }
        }
        
        return result;
    }
    
    @Override
    public Integer floorKey(Integer key) {
        Node result = floorNode(key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }
    
    private Node floorNode(Integer key) {
        Node current = root;
        Node result = null;
        
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                result = current;
                current = current.right;
            } else {
                return current;
            }
        }
        
        return result;
    }
    
    @Override
    public Integer ceilingKey(Integer key) {
        Node result = ceilingNode(key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }
    
    private Node ceilingNode(Integer key) {
        Node current = root;
        Node result = null;
        
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0) {
                current = current.right;
            } else if (cmp < 0) {
                result = current;
                current = current.left;
            } else {
                return current;
            }
        }
        
        return result;
    }
    
    @Override
    public Integer higherKey(Integer key) {
        Node result = higherNode(key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }
    
    private Node higherNode(Integer key) {
        Node current = root;
        Node result = null;
        
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp >= 0) {
                current = current.right;
            } else {
                result = current;
                current = current.left;
            }
        }
        
        return result;
    }
    
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMapHelper(root, toKey, result);
        return result;
    }
    
    private void headMapHelper(Node node, Integer toKey, MySplayMap result) {
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
        MySplayMap result = new MySplayMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }
    
    private void tailMapHelper(Node node, Integer fromKey, MySplayMap result) {
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
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
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

