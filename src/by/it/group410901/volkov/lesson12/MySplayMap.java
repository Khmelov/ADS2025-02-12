package by.it.group410901.volkov.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Comparator;

/**
 * Реализация NavigableMap на основе splay-дерева
 * Splay-дерево - это самобалансирующееся бинарное дерево поиска,
     * где после каждого доступа к узлу он перемещается в корень дерева (splay операция)
     * Это обеспечивает локальность доступа: часто используемые элементы находятся ближе к корню
     * Время выполнения: O(log n) амортизированно
 */
public class MySplayMap implements NavigableMap<Integer, String> {
    
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Node root;
    private int size;
    
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
        return containsValueRecursive(root, value);
    }
    
    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }
    
    @Override
    public String get(Object key) {
        Node node = findNode(root, (Integer) key);
        if (node != null) {
            root = splay(node);
            return node.value;
        }
        return null;
    }
    
    private Node findNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return findNode(node.left, key);
        if (cmp > 0) return findNode(node.right, key);
        return node;
    }
    
    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        if (oldValue == null) {
            size++;
        }
        root = putRecursive(root, null, key, value);
        // После вставки splay узел с данным ключом
        Node targetNode = findNode(root, key);
        if (targetNode != null) {
            root = splay(targetNode);
        }
        return oldValue;
    }
    
    private Node putRecursive(Node node, Node parent, Integer key, String value) {
        if (node == null) {
            Node newNode = new Node(key, value);
            newNode.parent = parent;
            return newNode;
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, node, key, value);
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, node, key, value);
        } else {
            node.value = value;
        }
        
        return node;
    }
    
    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            // get уже сделал splay, так что node с key теперь в корне
            Node nodeToRemove = root;
            
            if (nodeToRemove.left == null) {
                root = nodeToRemove.right;
                if (root != null) root.parent = null;
            } else if (nodeToRemove.right == null) {
                root = nodeToRemove.left;
                if (root != null) root.parent = null;
            } else {
                // Узел имеет оба потомка
                // Находим минимальный узел в правом поддереве
                Node minNode = findMin(nodeToRemove.right);
                
                // Splay минимальный узел к корню правого поддерева
                // Для этого временно делаем правое поддерево корнем
                Node rightSubtree = nodeToRemove.right;
                Node leftSubtree = nodeToRemove.left;
                
                // Splay minNode в правом поддереве
                root = rightSubtree;
                Node splayedMin = splay(minNode);
                
                // Присоединяем левое поддерево удаляемого узла к minNode
                splayedMin.left = leftSubtree;
                if (leftSubtree != null) {
                    leftSubtree.parent = splayedMin;
                }
                
                root = splayedMin;
            }
            size--;
        }
        return oldValue;
    }
    
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * Выполняет splay операцию: перемещает узел в корень дерева
     * Использует zig, zig-zig и zig-zag повороты для балансировки
     * @param node узел для перемещения в корень
     * @return узел, который стал корнем после splay
     */
    private Node splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
            
            if (grandparent == null) {
                // Zig: узел - прямой потомок корня
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                // Zig-zig: узел и родитель на одной стороне
                if (node == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } 
                // Zig-zag: узел и родитель на разных сторонах
                else if (node == parent.left && parent == grandparent.right) {
                    rotateRight(parent);
                    rotateLeft(grandparent);
                } else {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                }
            }
        }
        return node;
    }
    
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }
    }
    
    private void rotateRight(Node y) {
        Node x = y.left;
        if (x != null) {
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            x.parent = y.parent;
            if (y.parent == null) {
                root = x;
            } else if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
            x.right = y;
            y.parent = x;
        }
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
    
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }
    
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, inclusive, result);
        return result;
    }
    
    private void headMapRecursive(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node != null) {
            headMapRecursive(node.left, toKey, inclusive, result);
            int cmp = node.key.compareTo(toKey);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                result.put(node.key, node.value);
            }
            headMapRecursive(node.right, toKey, inclusive, result);
        }
    }
    
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, inclusive, result);
        return result;
    }
    
    private void tailMapRecursive(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node != null) {
            tailMapRecursive(node.left, fromKey, inclusive, result);
            int cmp = node.key.compareTo(fromKey);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                result.put(node.key, node.value);
            }
            tailMapRecursive(node.right, fromKey, inclusive, result);
        }
    }
    
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }
    
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }
    
    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        root = splay(node);
        return node.key;
    }
    
    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        root = splay(node);
        return node.key;
    }
    
    @Override
    public Entry<Integer, String> firstEntry() {
        Integer key = firstKey();
        return key != null ? new SimpleEntry(key, get(key)) : null;
    }
    
    @Override
    public Entry<Integer, String> lastEntry() {
        Integer key = lastKey();
        return key != null ? new SimpleEntry(key, get(key)) : null;
    }
    
    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Entry<Integer, String> entry = firstEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
    }
    
    @Override
    public Entry<Integer, String> pollLastEntry() {
        Entry<Integer, String> entry = lastEntry();
        if (entry != null) {
            remove(entry.getKey());
        }
        return entry;
    }
    
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        Integer lowerKey = lowerKey(key);
        return lowerKey != null ? new SimpleEntry(lowerKey, get(lowerKey)) : null;
    }
    
    @Override
    public Integer lowerKey(Integer key) {
        Node node = findLowerNode(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }
    
    private Node findLowerNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return findLowerNode(node.left, key);
        } else {
            Node right = findLowerNode(node.right, key);
            return right != null ? right : node;
        }
    }
    
    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Integer floorKey = floorKey(key);
        return floorKey != null ? new SimpleEntry(floorKey, get(floorKey)) : null;
    }
    
    @Override
    public Integer floorKey(Integer key) {
        Node node = findFloorNode(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }
    
    private Node findFloorNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return findFloorNode(node.left, key);
        } else if (cmp > 0) {
            Node right = findFloorNode(node.right, key);
            return right != null ? right : node;
        } else {
            return node;
        }
    }
    
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Integer ceilingKey = ceilingKey(key);
        return ceilingKey != null ? new SimpleEntry(ceilingKey, get(ceilingKey)) : null;
    }
    
    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeilingNode(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }
    
    private Node findCeilingNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return findCeilingNode(node.right, key);
        } else if (cmp < 0) {
            Node left = findCeilingNode(node.left, key);
            return left != null ? left : node;
        } else {
            return node;
        }
    }
    
    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Integer higherKey = higherKey(key);
        return higherKey != null ? new SimpleEntry(higherKey, get(higherKey)) : null;
    }
    
    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigherNode(root, key);
        if (node != null) {
            root = splay(node);
            return node.key;
        }
        return null;
    }
    
    private Node findHigherNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return findHigherNode(node.right, key);
        } else {
            Node left = findHigherNode(node.left, key);
            return left != null ? left : node;
        }
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRecursive(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void toStringRecursive(Node node, StringBuilder sb) {
        if (node != null) {
            toStringRecursive(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringRecursive(node.right, sb);
        }
    }
    
    private static class SimpleEntry implements Entry<Integer, String> {
        private final Integer key;
        private String value;
        
        SimpleEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public Integer getKey() {
            return key;
        }
        
        @Override
        public String getValue() {
            return value;
        }
        
        @Override
        public String setValue(String value) {
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
