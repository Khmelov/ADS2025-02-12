package by.it.group451002.yeshmanski.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size = 0;

    // Структура узла Красно-Черного дерева
    private static class Node {
        Integer key;
        String value;
        Node left = null;
        Node right = null;
        Node parent = null;
        boolean color = BLACK;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    // Обязательные методы Map

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
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // Для поиска значения нужно обойти все дерево (RBT отсортировано по ключам)
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
        Node p = getNode(key);
        return (p == null ? null : p.value);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        Node t = root;
        if (t == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        int cmp;
        Node parent;
        // Стандартный спуск BST
        do {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                String oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        } while (t != null);

        // Вставка нового узла
        Node e = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }

        // Новый узел всегда красный, затем чиним дерево
        e.color = RED;
        fixAfterInsertion(e);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node p = getNode(key);
        if (p == null) return null;

        String oldValue = p.value;
        deleteEntry(p);
        size--;
        return oldValue;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        buildString(root, sb);
        // Удаляем последнюю запятую и пробел
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // In-order обход для вывода по возрастанию
    private void buildString(Node p, StringBuilder sb) {
        if (p == null) return;
        buildString(p.left, sb);
        sb.append(p.key).append("=").append(p.value).append(", ");
        buildString(p.right, sb);
    }

    // =========================================================
    // ============ Методы SortedMap ===========================
    // =========================================================

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используем natural ordering
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node p = root;
        while (p.left != null) p = p.left;
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node p = root;
        while (p.right != null) p = p.right;
        return p.key;
    }

    // Возвращает новую карту с элементами строго меньше toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        fillSubMap(root, subMap, null, toKey);
        return subMap;
    }

    // Возвращает новую карту с элементами больше или равно fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        fillSubMap(root, subMap, fromKey, null);
        return subMap;
    }

    // Рекурсивное заполнение под-карты (range query)
    private void fillSubMap(Node node, MyRbMap target, Integer from, Integer to) {
        if (node == null) return;

        boolean tooLow = (from != null && node.key.compareTo(from) < 0);
        boolean tooHigh = (to != null && node.key.compareTo(to) >= 0);

        if (!tooLow) {
            fillSubMap(node.left, target, from, to);
        }

        if (!tooLow && !tooHigh) {
            target.put(node.key, node.value);
        }

        if (!tooHigh) {
            fillSubMap(node.right, target, from, to);
        }
    }

    // SubMap (полный диапазон) не требуется по заданию, заглушка
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented for this task");
    }

    // =========================================================
    // ============ Внутренняя логика RBT ======================
    // =========================================================

    private Node getNode(Object key) {
        if (key == null) return null;
        Comparable<Object> k = (Comparable<Object>) key;
        Node p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0) p = p.left;
            else if (cmp > 0) p = p.right;
            else return p;
        }
        return null;
    }

    // Балансировка после вставки
    private void fixAfterInsertion(Node x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x))); // Дядя
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x))); // Дядя
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    // Удаление узла и балансировка
    private void deleteEntry(Node p) {
        // Если у узла два ребенка, заменяем его преемником
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        // У узла replacement будет либо один ребенок (левый или правый), либо null
        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left = replacement;
            else
                p.parent.right = replacement;

            p.left = p.right = p.parent = null;

            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) {
            root = null;
        } else { // Узел без детей
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    // Балансировка после удаления
    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // Симметрично
                Node sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private Node successor(Node t) {
        if (t == null) return null;
        else if (t.right != null) {
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    // --- Помощники для безопасного доступа к свойствам (null-safe) ---

    private boolean colorOf(Node p) {
        return (p == null ? BLACK : p.color);
    }

    private Node parentOf(Node p) {
        return (p == null ? null : p.parent);
    }

    private void setColor(Node p, boolean c) {
        if (p != null) p.color = c;
    }

    private Node leftOf(Node p) {
        return (p == null) ? null : p.left;
    }

    private Node rightOf(Node p) {
        return (p == null) ? null : p.right;
    }

    // --- Повороты ---

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null)
                l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else
                p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    // --- Заглушки неиспользуемых методов ---
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}