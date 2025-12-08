package by.it.group410902.sinyutin.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size = 0;

    // Узел дерева с ссылкой на родителя
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Ядро Splay-дерева

    /**
     * Основная операция Splay: поднимает узел x к корню.
     */
    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            // 1. Zig step (родитель - корень)
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            }
            // 2. Zig-Zig
            else if ((x == p.left && p == g.left) || (x == p.right && p == g.right)) {
                // Сначала вращаем родителя вокруг деда, потом x вокруг родителя
                if (x == p.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else {
                    rotateLeft(g);
                    rotateLeft(p);
                }
            }
            // 3. Zig-Zag
            else {
                // Двойное вращение: сначала x вокруг родителя, потом x вокруг деда
                if (x == p.left) {
                    rotateRight(p);
                    rotateLeft(g);
                } else {
                    rotateLeft(p);
                    rotateRight(g);
                }
            }
        }
        root = x;
    }

    // Левое вращение
    private void rotateLeft(Node p) {
        Node x = p.right;
        p.right = x.left;
        if (x.left != null) x.left.parent = p;

        x.parent = p.parent;
        if (p.parent == null) root = x;
        else if (p == p.parent.left) p.parent.left = x;
        else p.parent.right = x;

        x.left = p;
        p.parent = x;
    }

    // Правое вращение
    private void rotateRight(Node p) {
        Node x = p.left;
        p.left = x.right;
        if (x.right != null) x.right.parent = p;

        x.parent = p.parent;
        if (p.parent == null) root = x;
        else if (p == p.parent.left) p.parent.left = x;
        else p.parent.right = x;

        x.right = p;
        p.parent = x;
    }

    // Поиск узла. Если найден - splay. Если нет - splay последнего посещенного.
    private Node findNode(Integer key) {
        if (root == null) return null;
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                splay(curr); // Найден -> в корень
                return curr;
            }
        }
        splay(last); // Не найден -> последний посещенный в корень
        return null;
    }

    // Основные методы Map

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
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // Splay дерево не отсортировано по значениям, полный обход
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        boolean match = (value == null ? n.value == null : value.equals(n.value));
        if (match) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node res = findNode((Integer) key);
        return res == null ? null : res.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        // Пытаемся найти ключ. findNode сделает splay ближайшего узла к корню.
        findNode(key);

        // После findNode(key), root содержит либо искомый ключ, либо ближайший
        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            String old = root.value;
            root.value = value;
            return old;
        }

        Node n = new Node(key, value);
        if (cmp < 0) {
            // Новый ключ меньше корня -> корень становится правым ребенком нового
            n.left = root.left;
            n.right = root;
            if (root.left != null) root.left.parent = n;
            root.left = null;
        } else {
            // Новый ключ больше корня -> корень становится левым ребенком нового
            n.right = root.right;
            n.left = root;
            if (root.right != null) root.right.parent = n;
            root.right = null;
        }

        root.parent = n;
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        if (root == null) return null;

        // Поднимаем удаляемый узел или ближайший в корень
        findNode((Integer) key);

        if (!root.key.equals(key)) {
            return null; // Ключ не найден
        }

        String oldVal = root.value;

        // Удаление корня
        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else {
            Node rightSubtree = root.right;
            root = root.left;
            root.parent = null;

            // Ищем максимум в левом поддереве и делаем splay
            // Так как это максимум, у него нет правого ребенка
            Node maxLeft = root;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft); // Теперь maxLeft это root, и у него root.right == null

            root.right = rightSubtree;
            if (rightSubtree != null) rightSubtree.parent = root;
        }
        size--;
        return oldVal;
    }

    // NavigableMap методы

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        // Наибольший ключ < key
        // Логика: ищем key. Если находим или не находим - он (или сосед) всплывает.
        // Анализируем root и спускаемся.
        findNode(key);
        // После поиска root - либо key, либо ближайший.
        if (root == null) return null;

        if (root.key.compareTo(key) < 0) {
            return root.key;
        }
        // Если root >= key, ищем max в левом поддереве
        Node curr = root.left;
        if (curr == null) return null;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        // Наибольший ключ <= key
        findNode(key);
        if (root == null) return null;
        if (root.key.compareTo(key) <= 0) return root.key;

        Node curr = root.left;
        if (curr == null) return null;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        // Наименьший ключ >= key
        findNode(key);
        if (root == null) return null;
        if (root.key.compareTo(key) >= 0) return root.key;

        Node curr = root.right;
        if (curr == null) return null;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        // Наименьший ключ > key
        findNode(key);
        if (root == null) return null;
        if (root.key.compareTo(key) > 0) return root.key;

        Node curr = root.right;
        if (curr == null) return null;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    // --- Реализация headMap/tailMap через копирование (для упрощения) ---

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        fillSubMap(root, sub, null, toKey, false, inclusive);
        return sub;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        fillSubMap(root, sub, fromKey, null, inclusive, false);
        return sub;
    }

    // Обязательные методы по заданию (упрощенные версии из интерфейса SortedMap)
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    private void fillSubMap(Node node, MySplayMap target,
                            Integer from, Integer to,
                            boolean fromInc, boolean toInc) {
        if (node == null) return;

        boolean lowOK = (from == null) || (fromInc ? node.key.compareTo(from) >= 0 : node.key.compareTo(from) > 0);
        boolean highOK = (to == null) || (toInc ? node.key.compareTo(to) <= 0 : node.key.compareTo(to) < 0);

        if (from == null || node.key.compareTo(from) > 0) // Optimization logic loosely applied
            fillSubMap(node.left, target, from, to, fromInc, toInc);

        if (lowOK && highOK) {
            target.put(node.key, node.value);
        }

        if (to == null || node.key.compareTo(to) < 0)
            fillSubMap(node.right, target, from, to, fromInc, toInc);
    }

    // =========================================================
    // ============ ToString ===================================
    // =========================================================

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderString(Node n, StringBuilder sb) {
        if (n != null) {
            inOrderString(n.left, sb);
            sb.append(n.key).append("=").append(n.value).append(", ");
            inOrderString(n.right, sb);
        }
    }

    // =========================================================
    // ============ Заглушки ===================================
    // =========================================================

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
}
