package by.it.group410902.latipov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    // Вспомогательные методы для splay-дерева

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

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }
    }

    private void splay(Node x) {
        if (x == null) return;

        while (x.parent != null) {
            Node parent = x.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig
                if (x == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (x == parent.left && parent == grandparent.left) {
                // Zig-zig right
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (x == parent.right && parent == grandparent.right) {
                // Zig-zig left
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (x == parent.right && parent == grandparent.left) {
                // Zig-zag
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                // Zig-zag
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node lastVisited = null;

        while (current != null) {
            lastVisited = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                splay(current);
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    private Node getMaxNode(Node node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node getMinNode(Node node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Обход дерева для toString()
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1 && !sb.toString().endsWith("{")) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }

    // Вспомогательные методы для поиска значений
    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) return false;
        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    // Вспомогательные методы для headMap и tailMap
    private void collectKeysLessThan(Node node, Integer toKey, List<Integer> keys) {
        if (node == null) return;
        collectKeysLessThan(node.left, toKey, keys);
        if (node.key.compareTo(toKey) < 0) {
            keys.add(node.key);
        }
        collectKeysLessThan(node.right, toKey, keys);
    }

    private void collectKeysGreaterOrEqual(Node node, Integer fromKey, List<Integer> keys) {
        if (node == null) return;
        collectKeysGreaterOrEqual(node.left, fromKey, keys);
        if (node.key.compareTo(fromKey) >= 0) {
            keys.add(node.key);
        }
        collectKeysGreaterOrEqual(node.right, fromKey, keys);
    }

    // Реализация обязательных методов

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    private String remove(Integer key) {
        if (key == null || root == null) return null;

        Node node = findNode(key);
        if (node == null) return null;

        String removedValue = node.value;

        // Если у узла нет левого потомка
        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        }
        // Если у узла нет правого потомка
        else if (node.right == null) {
            root = node.left;
            root.parent = null;
        }
        // Если у узла есть оба потомка
        else {
            Node maxLeft = getMaxNode(node.left);
            // Отсоединяем maxLeft от его родителя
            if (maxLeft.parent != node) {
                maxLeft.parent.right = maxLeft.left;
                if (maxLeft.left != null) {
                    maxLeft.left.parent = maxLeft.parent;
                }
                maxLeft.left = node.left;
                node.left.parent = maxLeft;
            }

            maxLeft.right = node.right;
            node.right.parent = maxLeft;
            maxLeft.parent = null;
            root = maxLeft;
        }

        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        return get((Integer) key);
    }

    private String get(Integer key) {
        if (key == null || root == null) return null;

        Node node = findNode(key);
        return (node != null) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof Integer)) return false;
        return containsKey((Integer) key);
    }

    private boolean containsKey(Integer key) {
        if (key == null) return false;
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue((String) value);
    }

    private boolean containsValue(String value) {
        return containsValueRecursive(root, value);
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException("toKey cannot be null");
        }

        List<Integer> keys = new ArrayList<>();
        collectKeysLessThan(root, toKey, keys);

        // Создаем новую карту с выбранными ключами
        MySplayMap result = new MySplayMap();
        for (Integer key : keys) {
            String value = get(key);
            if (value != null) {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey cannot be null");
        }

        List<Integer> keys = new ArrayList<>();
        collectKeysGreaterOrEqual(root, fromKey, keys);

        // Создаем новую карту с выбранными ключами
        MySplayMap result = new MySplayMap();
        for (Integer key : keys) {
            String value = get(key);
            if (value != null) {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException("Map is empty");
        }
        Node min = getMinNode(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException("Map is empty");
        }
        Node max = getMaxNode(root);
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Node current = root;
        Node best = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                current = current.left;
            } else {
                best = current;
                current = current.right;
            }
        }

        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Node current = root;
        Node best = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                splay(current);
                return current.key;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                best = current;
                current = current.right;
            }
        }

        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Node current = root;
        Node best = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                splay(current);
                return current.key;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                best = current;
                current = current.left;
            }
        }

        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Node current = root;
        Node best = null;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp >= 0) {
                current = current.right;
            } else {
                best = current;
                current = current.left;
            }
        }

        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    // Остальные методы интерфейса (необязательные для реализации)

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    // Методы Map интерфейса

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}