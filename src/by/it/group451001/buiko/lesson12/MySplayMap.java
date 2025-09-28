package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла дерева
    private class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    // Вспомогательные методы для балансировки дерева

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

    // Splay операция - перемещает узел в корень
    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) {
                    rotateRight(x.parent);
                } else {
                    rotateLeft(x.parent);
                }
            } else {
                Node parent = x.parent;
                Node grandParent = parent.parent;
                if (grandParent.left == parent && parent.left == x) {
                    rotateRight(grandParent);
                    rotateRight(parent);
                } else if (grandParent.right == parent && parent.right == x) {
                    rotateLeft(grandParent);
                    rotateLeft(parent);
                } else if (grandParent.left == parent && parent.right == x) {
                    rotateLeft(parent);
                    rotateRight(grandParent);
                } else {
                    rotateRight(parent);
                    rotateLeft(grandParent);
                }
            }
        }
        root = x;
    }

    // Находит узел с заданным ключом (без splay)
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    // Находит узел или последний посещенный узел (для splay)
    private Node findNodeForSplay(Integer key) {
        Node current = root;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return lastVisited;
    }

    // Обход дерева in-order
    private void inOrderTraversal(Node node, List<Map.Entry<Integer, String>> list) {
        if (node != null) {
            inOrderTraversal(node.left, list);
            list.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderTraversal(node.right, list);
        }
    }

    // Основные методы интерфейса Map

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node node = findNodeForSplay(key);
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            String oldValue = node.value;
            node.value = value;
            splay(node);
            return oldValue;
        } else {
            Node newNode = new Node(key, value);
            newNode.parent = node;
            if (cmp < 0) {
                node.left = newNode;
            } else {
                node.right = newNode;
            }
            splay(newNode);
            size++;
            return null;
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node == null) {
            // Если узел не найден, делаем splay для последнего посещенного узла
            Node lastVisited = findNodeForSplay(k);
            if (lastVisited != null) {
                splay(lastVisited);
            }
            return null;
        }

        splay(node);
        String removedValue = node.value;

        // Удаление узла
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            Node minRight = node.right;
            while (minRight.left != null) {
                minRight = minRight.left;
            }
            splay(minRight);
            minRight.left = node.left;
            node.left.parent = minRight;
        }
        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node != null) {
            splay(node);
            return node.value;
        }
        // Splay для последнего посещенного узла, если ключ не найден
        Node lastVisited = findNodeForSplay(k);
        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node != null) {
            splay(node);
            return true;
        }
        // Splay для последнего посещенного узла
        Node lastVisited = findNodeForSplay(k);
        if (lastVisited != null) {
            splay(lastVisited);
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
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

    // Методы для представлений

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        headMapRecursive(root, toKey, subMap);
        return subMap;
    }

    private void headMapRecursive(Node node, Integer toKey, MySplayMap subMap) {
        if (node != null) {
            headMapRecursive(node.left, toKey, subMap);
            if (node.key < toKey) {
                subMap.put(node.key, node.value);
            }
            headMapRecursive(node.right, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap();
        tailMapRecursive(root, fromKey, subMap);
        return subMap;
    }

    private void tailMapRecursive(Node node, Integer fromKey, MySplayMap subMap) {
        if (node != null) {
            tailMapRecursive(node.left, fromKey, subMap);
            if (node.key >= fromKey) {
                subMap.put(node.key, node.value);
            }
            tailMapRecursive(node.right, fromKey, subMap);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.key;
    }

    // Методы навигации

    @Override
    public Integer lowerKey(Integer key) {
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key < key) {
            return node.key;
        } else {
            if (node.left == null) return null;
            Node current = node.left;
            while (current.right != null) {
                current = current.right;
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key <= key) {
            return node.key;
        } else {
            if (node.left == null) return null;
            Node current = node.left;
            while (current.right != null) {
                current = current.right;
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key >= key) {
            return node.key;
        } else {
            if (node.right == null) return null;
            Node current = node.right;
            while (current.left != null) {
                current = current.left;
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key > key) {
            return node.key;
        } else {
            if (node.right == null) return null;
            Node current = node.right;
            while (current.left != null) {
                current = current.left;
            }
            splay(current);
            return current.key;
        }
    }

    // Метод toString для вывода элементов в порядке возрастания ключей
    @Override
    public String toString() {
        List<Map.Entry<Integer, String>> entries = new ArrayList<>();
        inOrderTraversal(root, entries);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Integer, String> entry = entries.get(i);
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (i < entries.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Остальные методы интерфейса (заглушки)

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

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}