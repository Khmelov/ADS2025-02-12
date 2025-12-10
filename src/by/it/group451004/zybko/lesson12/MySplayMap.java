package by.it.group451004.zybko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

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

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        if (y != null) {
            y.left = x;
        }
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        if (y != null) {
            y.right = x;
        }
        x.parent = y;
    }

    private void splay(Node x) {
        while (x != null && x.parent != null) {
            Node parent = x.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig
                if (x == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                if (x == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (x == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (x == parent.right && parent == grandparent.left) {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {

                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
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

    private Node findMin(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node findMax(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if (root == null) {
            root = new Node(key, value);
            size++;
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

        Node newNode = new Node(key, value);
        newNode.parent = parent;

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
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        if (node == null) {
            return null;
        }

        String removedValue = node.value;

        if (node.left == null) {
            // Нет левого ребенка
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (node.right == null) {
            // Нет правого ребенка
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            // Есть оба ребенка
            Node minRight = findMin(node.right);

            if (minRight.parent != node) {
                // Перенаправляем ссылки
                minRight.parent.left = minRight.right;
                if (minRight.right != null) {
                    minRight.right.parent = minRight.parent;
                }
                minRight.right = node.right;
                if (minRight.right != null) {
                    minRight.right.parent = minRight;
                }
            }

            minRight.left = node.left;
            if (minRight.left != null) {
                minRight.left.parent = minRight;
            }

            root = minRight;
            root.parent = null;
            splay(root);
        }

        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        return containsValueRecursive(root, (String) value);
    }

    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            splay(node);
            return true;
        }
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
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

    // NavigableMap методы
    @Override
    public Integer firstKey() {
        if (root == null) {
            return null;
        }
        Node min = findMin(root);
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }
        Node max = findMax(root);
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node result = findLowerKey(root, key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private Node findLowerKey(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp <= 0) {
            return findLowerKey(node.left, key);
        } else {
            Node rightResult = findLowerKey(node.right, key);
            return rightResult != null ? rightResult : node;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        Node result = findFloorKey(root, key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private Node findFloorKey(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return findFloorKey(node.left, key);
        } else {
            Node rightResult = findFloorKey(node.right, key);
            return rightResult != null ? rightResult : node;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node result = findCeilingKey(root, key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private Node findCeilingKey(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return findCeilingKey(node.right, key);
        } else {
            Node leftResult = findCeilingKey(node.left, key);
            return leftResult != null ? leftResult : node;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        Node result = findHigherKey(root, key);
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    private Node findHigherKey(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp >= 0) {
            return findHigherKey(node.right, key);
        } else {
            Node leftResult = findHigherKey(node.left, key);
            return leftResult != null ? leftResult : node;
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

    private void headMapRecursive(Node node, Integer toKey, MySplayMap result) {
        if (node != null) {
            headMapRecursive(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMapRecursive(node.right, toKey, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    private void tailMapRecursive(Node node, Integer fromKey, MySplayMap result) {
        if (node != null) {
            tailMapRecursive(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMapRecursive(node.right, fromKey, result);
        }
    }

    // Вспомогательные методы для обхода в порядке возрастания
    private void inOrderTraversal(Node node, List<Map.Entry<Integer, String>> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderTraversal(node.right, result);
        }
    }

    @Override
    public String toString() {
        List<Map.Entry<Integer, String>> entries = new ArrayList<>();
        inOrderTraversal(root, entries);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(entries.get(i).getKey())
                    .append("=")
                    .append(entries.get(i).getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    // Остальные методы интерфейса - не реализуем
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> firstEntry() { return null; }
    @Override
    public Entry<Integer, String> lastEntry() { return null; }
    @Override
    public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override
    public Entry<Integer, String> pollLastEntry() { return null; }
    @Override
    public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override
    public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override
    public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { }
}