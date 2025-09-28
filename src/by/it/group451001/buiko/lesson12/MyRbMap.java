package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    // Внутренний класс узла дерева
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color; // true - красный, false - черный

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = true; // Новый узел всегда красный
        }
    }

    private Node root;
    private int size = 0;
    private final Node NIL = new Node(null, null, null); // Фиктивный узел-лист

    public MyRbMap() {
        NIL.color = false; // Листья всегда черные
        root = NIL;
    }

    // Вспомогательные методы для балансировки
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void fixInsert(Node k) {
        while (k.parent != null && k.parent.color) {
            if (k.parent == k.parent.parent.left) {
                Node u = k.parent.parent.right;
                if (u.color) {
                    u.color = false;
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rotateRight(k.parent.parent);
                }
            } else {
                Node u = k.parent.parent.left;
                if (u.color) {
                    u.color = false;
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rotateRight(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rotateLeft(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        root.color = false;
    }

    // Основные методы интерфейса
    @Override
    public String put(Integer key, String value) {
        Node y = null;
        Node x = root;

        while (x != NIL) {
            y = x;
            if (key.compareTo(x.key) < 0) x = x.left;
            else if (key.compareTo(x.key) > 0) x = x.right;
            else {
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        Node newNode = new Node(key, value, y);
        if (y == null) root = newNode;
        else if (key.compareTo(y.key) < 0) y.left = newNode;
        else y.right = newNode;

        newNode.left = NIL;
        newNode.right = NIL;
        size++;
        fixInsert(newNode);
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String remove(Object key) {
        Node z = searchNode((Integer) key);
        if (z == NIL) return null;
        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (!yOriginalColor) {
            fixDelete(x);
        }
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != NIL) {
            x = x.left;
        }
        return x;
    }

    private void fixDelete(Node x) {
        while (x != root && !x.color) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.color && !w.right.color) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.right.color) {
                        w.left.color = false;
                        w.color = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (!w.right.color && !w.left.color) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.left.color) {
                        w.right.color = false;
                        w.color = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }

    private Node searchNode(Integer key) {
        Node x = root;
        while (x != NIL) {
            if (key.compareTo(x.key) < 0) x = x.left;
            else if (key.compareTo(x.key) > 0) x = x.right;
            else return x;
        }
        return NIL;
    }

    @Override
    public String get(Object key) {
        Node node = searchNode((Integer) key);
        return node != NIL ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return searchNode((Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == NIL) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValueHelper(node.left, value) ||
                containsValueHelper(node.right, value);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    // Методы для работы с подмножествами
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        headMapHelper(root, toKey, subMap);
        return subMap;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMapHelper(node.right, toKey, subMap);
        }
        headMapHelper(node.left, toKey, subMap);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        tailMapHelper(root, fromKey, subMap);
        return subMap;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, subMap);
        }
        tailMapHelper(node.right, fromKey, subMap);
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node x = root;
        while (x.left != NIL) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node x = root;
        while (x.right != NIL) x = x.right;
        return x.key;
    }

    // Остальные методы интерфейса
    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, fromKey, toKey, subMap);
        return subMap;
    }

    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMapHelper(node.left, fromKey, toKey, subMap);
        }
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0) {
            subMapHelper(node.right, fromKey, toKey, subMap);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        keySetHelper(root, keys);
        return keys;
    }

    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node == NIL) return;
        keySetHelper(node.left, keys);
        keys.add(node.key);
        keySetHelper(node.right, keys);
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        valuesHelper(root, values);
        return values;
    }

    private void valuesHelper(Node node, List<String> values) {
        if (node == NIL) return;
        valuesHelper(node.left, values);
        values.add(node.value);
        valuesHelper(node.right, values);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Entry::getKey));
        entrySetHelper(root, entries);
        return entries;
    }

    private void entrySetHelper(Node node, Set<Entry<Integer, String>> entries) {
        if (node == NIL) return;
        entrySetHelper(node.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        entrySetHelper(node.right, entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == NIL) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderTraversal(node.right, sb);
    }
}