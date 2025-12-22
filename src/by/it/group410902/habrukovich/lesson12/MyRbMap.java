package by.it.group410902.habrukovich.lesson12;

import java.util.*;


public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private boolean isRed(Node node) {
        if (node == null) return false;  // null узлы считаются черными
        return node.color == RED;
    }

    private Node rotateLeft(Node h) {
        // Сохраняем правый потомок
        Node x = h.right;
        // Переносим левое поддерево x в правое h
        h.right = x.left;
        // Делаем h левым потомком x
        x.left = h;
        // Сохраняем исходный цвет h
        x.color = h.color;
        // Окрашиваем h в красный
        h.color = RED;
        return x;  // возвращаем новую корневую ноду
    }

    private Node rotateRight(Node h) {
        // Сохраняем левый потомок
        Node x = h.left;
        // Переносим правое поддерево x в левое h
        h.left = x.right;

        x.right = h;

        x.color = h.color;

        h.color = RED;
        return x;  // возвращаем новую корневую ноду
    }

    private void flipColors(Node h) {
        if (h != null) {
            // Инвертируем цвет текущего узла
            h.color = !h.color;
            // Инвертируем цвета потомков
            if (h.left != null) h.left.color = !h.left.color;
            if (h.right != null) h.right.color = !h.right.color;
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        // Проверяем существующее значение
        String oldValue = get(key);
        // Рекурсивно добавляем элемент
        root = putHelper(root, key, value);
        // Гарантируем что корень всегда черный
        root.color = BLACK;
        // Увеличиваем размер только для новых ключей
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node putHelper(Node h, Integer key, String value) {
        // Базовый случай: создаем новый красный узел
        if (h == null) return new Node(key, value, RED);

        // Сравниваем ключи для определения направления
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            // Идем в левое поддерево
            h.left = putHelper(h.left, key, value);
        } else if (cmp > 0) {
            // Идем в правое поддерево
            h.right = putHelper(h.right, key, value);
        } else {
            // Ключ существует - обновляем значение
            h.value = value;
            return h;
        }

        // Балансировка после вставки:

        // Случай 1: правый потомок красный, левый - черный
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }

        // Случай 2: левый потомок и его левый потомок красные
        if (isRed(h.left) && isRed(h.left != null ? h.left.left : null)) {
            h = rotateRight(h);
        }

        // Случай 3: оба потомка красные
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        return h;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        // Проверяем существование ключа
        if (!containsKey(intKey)) return null;

        // Сохраняем старое значение
        String oldValue = get(intKey);
        // Временное окрашивание корня для упрощения алгоритма
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        // Рекурсивно удаляем элемент
        root = removeHelper(root, intKey);
        // Восстанавливаем черный корень
        if (root != null) {
            root.color = BLACK;
        }
        size--;
        return oldValue;
    }

    private Node removeHelper(Node h, Integer key) {
        if (h == null) return null;

        // Поиск в левом поддереве
        if (key.compareTo(h.key) < 0) {
            // Проверяем необходимость перемещения красного узла
            if (h.left != null && !isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            if (h.left != null) {
                h.left = removeHelper(h.left, key);
            }
        } else {
            // Поиск в правом поддереве или удаление текущего узла

            // Упрощаем удаление через поворот
            if (isRed(h.left)) {
                h = rotateRight(h);
            }

            // Найден узел для удаления без правого потомка
            if (key.compareTo(h.key) == 0 && h.right == null) {
                return null;
            }

            // Проверяем необходимость перемещения красного узла вправо
            if (h.right != null && !isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }

            if (key.compareTo(h.key) == 0) {
                // Узел с двумя потомками: находим минимальный в правом поддереве
                Node x = getMin(h.right);
                // Копируем данные минимального узла
                h.key = x.key;
                h.value = x.value;
                // Удаляем минимальный узел из правого поддерева
                h.right = removeMin(h.right);
            } else {
                // Продолжаем поиск в правом поддереве
                if (h.right != null) {
                    h.right = removeHelper(h.right, key);
                }
            }
        }
        // Балансируем дерево после удаления
        return balance(h);
    }

    private Node moveRedLeft(Node h) {
        // Временное увеличение черной высоты
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            // Двойное вращение для корректного перемещения
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            // Восстанавливаем цвета
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        // Временное увеличение черной высоты
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            // Восстанавливаем цвета
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (h == null) return null;
        // Балансировка аналогичная putHelper
        if (isRed(h.right)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    private Node removeMin(Node h) {
        if (h == null) return null;
        // Достигли минимального узла
        if (h.left == null) return null;
        // Проверяем необходимость перемещения красного узла
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        // Рекурсивно удаляем минимальный узел
        h.left = removeMin(h.left);
        // Балансируем дерево
        return balance(h);
    }

    private Node getMin(Node h) {
        if (h == null) return null;
        // Идем до самого левого узла
        while (h.left != null) {
            h = h.left;
        }
        return h;
    }

    private Node getMax(Node h) {
        if (h == null) return null;
        // Идем до самого правого узла
        while (h.right != null) {
            h = h.right;
        }
        return h;
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        return getHelper(root, intKey);
    }

    private String getHelper(Node node, Integer key) {
        if (node == null) return null;  // ключ не найден
        // Сравниваем ключи
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // Ищем в левом поддереве
            return getHelper(node.left, key);
        } else if (cmp > 0) {
            // Ищем в правом поддереве
            return getHelper(node.right, key);
        } else {
            // Ключ найден
            return node.value;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof Integer)) return false;
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;

        // Проверяем текущий узел
        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }
        // Рекурсивно проверяем левое и правое поддеревья
        return containsValueHelper(node.left, value) ||
                containsValueHelper(node.right, value);
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
        if (isEmpty()) throw new NoSuchElementException();
        return getMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return getMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        // Сравниваем ключ с границей
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            // Ключ меньше границы - добавляем в результат
            result.put(node.key, node.value);
            // Обходим оба поддерева
            headMapHelper(node.left, toKey, result);
            headMapHelper(node.right, toKey, result);
        } else {
            // Ключ больше или равен - идем только в левое поддерево
            headMapHelper(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        // Сравниваем ключ с границей
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            // Ключ больше или равен границы - добавляем в результат
            result.put(node.key, node.value);
            // Обходим оба поддерева
            tailMapHelper(node.left, fromKey, result);
            tailMapHelper(node.right, fromKey, result);
        } else {
            // Ключ меньше - идем только в правое поддерево
            tailMapHelper(node.right, fromKey, result);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        // Удаляем последнюю запятую и пробел
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            // Симметричный обход: левое поддерево -> узел -> правое поддерево
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }
    // Нереализованные методы SortedMap интерфейса (не требуются по заданию)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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
