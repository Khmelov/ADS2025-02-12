package by.it.group451002.vishnevskiy.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;   // Цвет красного узла
    private static final boolean BLACK = false; // Цвет чёрного узла

    private Node root;     // Корень дерева
    private int size = 0;  // Количество элементов

    // Узел красно-чёрного дерева
    private static class Node {
        Integer key;       // Ключ
        String value;      // Значение
        Node left = null;  // Левый ребенок
        Node right = null; // Правый ребенок
        Node parent = null;// Родитель
        boolean color = BLACK; // Цвет узла (по умолчанию чёрный)

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent; // Связь с родителем
        }
    }

    // ==================== Методы Map ====================

    @Override
    public int size() {
        return size; // Возвращаем количество элементов
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверка пустоты
    }

    @Override
    public void clear() {
        root = null; // Удаляем ссылку на корень
        size = 0;    // Размер = 0
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null; // Проверяем существование узла
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value); // Поиск значения в обходе
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false; // Конец ветки
        if (value == null ? node.value == null : value.equals(node.value))
            return true; // Нашли значение
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value); // Продолжаем обход
    }

    @Override
    public String get(Object key) {
        Node p = getNode(key); // Находим узел
        return p == null ? null : p.value; // Если есть — вернуть value
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        Node t = root;
        if (t == null) {               // Если дерево пустое
            root = new Node(key, value, null);
            size = 1;
            return null;               // Старого значения нет
        }

        Node parent;
        int cmp;

        // Обычная вставка BST
        do {
            parent = t;                                  // Запоминаем родителя
            cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;                     // Идем влево
            else if (cmp > 0) t = t.right;               // Идем вправо
            else {                                       // Ключ существует — обновление
                String old = t.value;
                t.value = value;
                return old;                              // Возвращаем старое значение
            }
        } while (t != null);

        // Создаем новый красный узел
        Node e = new Node(key, value, parent);
        if (cmp < 0) parent.left = e;
        else parent.right = e;
        e.color = RED;                     // Все новые узлы — красные

        fixAfterInsertion(e);              // Восстанавливаем КЧ свойства
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node p = getNode(key);   // Находим удаляемый узел
        if (p == null) return null;

        String old = p.value;
        deleteEntry(p);          // Запускаем удаление + фиксацию
        size--;
        return old;              // Возвращаем старое значение
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);             // In-order обход дерева
        sb.setLength(sb.length() - 2);     // Убираем последнюю запятую
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node p, StringBuilder sb) {
        if (p == null) return;
        buildString(p.left, sb);
        sb.append(p.key).append("=").append(p.value).append(", ");
        buildString(p.right, sb);
    }

    // ===================== SortedMap Методы =====================

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используем натуральный порядок ключей
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node p = root;
        while (p.left != null) p = p.left; // Минимальный элемент
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node p = root;
        while (p.right != null) p = p.right; // Максимальный элемент
        return p.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillSubMap(root, sub, null, toKey); // Всё < toKey
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillSubMap(root, sub, fromKey, null); // Всё ≥ fromKey
        return sub;
    }

    private void fillSubMap(Node node, MyRbMap target, Integer from, Integer to) {
        if (node == null) return;

        boolean tooLow = (from != null && node.key < from);      // Узел ниже диапазона
        boolean tooHigh = (to != null && node.key >= to);        // Узел выше диапазона

        if (!tooLow) fillSubMap(node.left, target, from, to);    // Идём влево

        if (!tooLow && !tooHigh) target.put(node.key, node.value); // Узел в диапазоне

        if (!tooHigh) fillSubMap(node.right, target, from, to);  // Идём вправо
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    //=================== Внутренние методы RBT ===================

    private Node getNode(Object key) {
        if (key == null) return null;
        Comparable<Object> k = (Comparable<Object>) key;

        Node p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0) p = p.left;
            else if (cmp > 0) p = p.right;
            else return p; //Нашли узел
        }
        return null;
    }

    // ----- Балансировка вставки RBT -----
    private void fixAfterInsertion(Node x) {

        x.color = RED; // Новый узел — всегда красный

        // Пока нарушается правило "красный не может иметь красного родителя"
        while (x != root && x.parent.color == RED) {

            // Если родитель — левый ребенок деда
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {

                Node y = rightOf(parentOf(parentOf(x))); // Дядя

                if (colorOf(y) == RED) { // Случай 1: дядя красный → перекрашиваем
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x)); // Поднимаемся вверх
                }

                else { // Дядя чёрный

                    if (x == rightOf(parentOf(x))) { // Случай 2: триугольник → поворот влево
                        x = parentOf(x);
                        rotateLeft(x);
                    }

                    // Случай 3: линия → перекрашиваем + поворот вправо
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            }

            // Симметрично аналогичные случаи
            else {
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

        root.color = BLACK; // Корень всегда чёрный
    }

    // ----- Удаление узла -----
    private void deleteEntry(Node p) {

        // Если два ребёнка → заменяем successor'ом
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;      // Копируем ключ
            p.value = s.value;  // Копируем значение
            p = s;              // Удаляем преемника
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {

            replacement.parent = p.parent;
            if (p.parent == null) root = replacement;
            else if (p == p.parent.left) p.parent.left = replacement;
            else p.parent.right = replacement;

            p.left = p.right = p.parent = null;

            if (p.color == BLACK)
                fixAfterDeletion(replacement); // Восстанавливаем свойства

        } else if (p.parent == null) {
            root = null; // Дерево стало пустым
        } else {

            if (p.color == BLACK)
                fixAfterDeletion(p); // Фиксация подвешенного чёрного узла

            if (p.parent != null) {
                if (p == p.parent.left) p.parent.left = null;
                else p.parent.right = null;
                p.parent = null;
            }
        }
    }

    // ----- Балансировка после удаления -----
    private void fixAfterDeletion(Node x) {

        while (x != root && colorOf(x) == BLACK) {

            if (x == leftOf(parentOf(x))) {

                Node sib = rightOf(parentOf(x)); // Брат узла

                if (colorOf(sib) == RED) {      // Случай 1: брат красный → поворот
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {

                    setColor(sib, RED);          // Случай 2: оба ребенка черные → вверх
                    x = parentOf(x);
                }

                else {
                    if (colorOf(rightOf(sib)) == BLACK) { // Случай 3: далеко чёрный
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }

                    // Случай 4: дальний ребёнок красный
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root; // Завершаем
                }
            }

            // Симметрично правая версия
            else {

                Node sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {

                    setColor(sib, RED);
                    x = parentOf(x);
                }

                else {
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

        setColor(x, BLACK); // Компенсируем "двойной чёрный"
    }

    private Node successor(Node t) {
        if (t == null) return null;
        if (t.right != null) {                    // Минимальный элемент в правом поддереве
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        }
        Node p = t.parent;
        Node ch = t;
        while (p != null && ch == p.right) {      // Поднимаемся вверх, пока идём справа
            ch = p;
            p = p.parent;
        }
        return p;
    }

    // =================== Вспомогательные методы ===================

    private boolean colorOf(Node p) { return p == null ? BLACK : p.color; }
    private Node parentOf(Node p) { return p == null ? null : p.parent; }
    private void setColor(Node p, boolean c) { if (p != null) p.color = c; }
    private Node leftOf(Node p) { return (p == null ? null : p.left); }
    private Node rightOf(Node p) { return (p == null ? null : p.right); }

    // =================== Повороты ===================

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;               // Правый ребёнок
            p.right = r.left;               // Перенос поддерева r.left

            if (r.left != null)
                r.left.parent = p;          // Перепривязка родителя

            r.parent = p.parent;            // r заменяет p в структуре

            if (p.parent == null) root = r; // p был корнем
            else if (p.parent.left == p) p.parent.left = r;
            else p.parent.right = r;

            r.left = p;                     // p становится левым ребёнком r
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;                // Левый ребёнок
            p.left = l.right;

            if (l.right != null)
                l.right.parent = p;

            l.parent = p.parent;

            if (p.parent == null) root = l;
            else if (p.parent.right == p) p.parent.right = l;
            else p.parent.left = l;

            l.right = p;
            p.parent = l;
        }
    }

    // =================== Заглушки ===================

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
