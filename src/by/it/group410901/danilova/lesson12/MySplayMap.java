package by.it.group410901.danilova.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    //на основе splay-дерева (самобалансирующегося бинарного дерева поиска)
    //часто используемые элементы ближе к корню
    private static class Node
    {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value)
        {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public MySplayMap()
    {
        root = null;
        size = 0;
    }

    // Splay-операция - перемещает узел с заданным ключом в корень
    private Node splay(Node h, Integer key)
    {
        if (h == null) return null;//пустое дерево

        int cmp1 = key.compareTo(h.key);
        if (cmp1 < 0) //ключ в левом поддереве
        {
            if (h.left == null) return h;//ключ не найден
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) //левое левое
            {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            }
            else if (cmp2 > 0) //левое правое
            {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                {
                    h.left = rotateLeft(h.left);
                }
            }
            return h.left == null ? h : rotateRight(h);
        }
        else if (cmp1 > 0) //значит в правом
        {
            if (h.right == null) return h;
            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0)
            {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null)
                {
                    h.right = rotateRight(h.right);
                }
            }
            else if (cmp2 > 0)
            {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }
            return h.right == null ? h : rotateLeft(h);
        }
        else
        {
            return h;
        }
    }

    private Node rotateRight(Node h)
    {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h)
    {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    // Основные методы Map
    @Override
    public String put(Integer key, String value)
    {//ДОБАВЛЕНИЕ ИЛИ ОБНОВЛЕНИЕ ЭЛЕМЕНТА
        if (root == null) //если дерево пустое
        {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key); //перемещение узла с ключом в корень
        int cmp = key.compareTo(root.key);

        if (cmp == 0) //если ключ существует, то значение обновляется
        {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node newNode = new Node(key, value);//если не существкет, то созлаётся новый
        //решвает куда вставить новый узел
        if (cmp < 0)
        {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        }
        else
        {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) //удаление по ключу
    {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        if (root == null) return null;

        root = splay(root, k);//перемешение удаляемого в корень
        if (k.compareTo(root.key) != 0)//действительно ли ключ существует
        {
            return null;
        }

        String removedValue = root.value;//сохранение значения удаляемого

        //удаление корня и объединение поддеревьев
        if (root.left == null)
        {
            root = root.right;
        }
        else
        {
            Node newRoot = root.right;
            root = splay(root.left, k);
            root.right = newRoot;
        }
        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) //возвращает значение по ключу
    {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        if (root == null) return null;

        root = splay(root, k);
        if (k.compareTo(root.key) == 0) //проверяет нудный ли ключ найден
        {
            return root.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        String v = (String) value;
        return containsValue(root, v);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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

    // Методы SortedMap
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;

        if (node.key.compareTo(toKey) < 0) {
            headMap(node.left, toKey, result);
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    // Методы NavigableMap
    @Override
    public Integer lowerKey(Integer key) {
        if (root == null) return null;

        Node current = root;
        Integer result = null;

        while (current != null) {
            if (current.key.compareTo(key) < 0) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (root == null) return null;

        Node current = root;
        Integer result = null;

        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp <= 0) {
                result = current.key;
                if (cmp == 0) break;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null) return null;

        Node current = root;
        Integer result = null;

        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp >= 0) {
                result = current.key;
                if (cmp == 0) break;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (root == null) return null;

        Node current = root;
        Integer result = null;

        while (current != null) {
            if (current.key.compareTo(key) > 0) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove last ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
    }

    // Не реализованные методы NavigableMap
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

    // Не реализованные методы Map
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

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
