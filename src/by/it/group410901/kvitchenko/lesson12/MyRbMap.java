package by.it.group410901.kvitchenko.lesson12; // Объявление пакета.

import java.util.*; // Импорт всех необходимых утилит (Map, Set, Comparator, SortedMap и т.д.).

// Класс MyRbMap реализует интерфейс SortedMap<Integer, String> на основе Красно-черного дерева (LLRB).
public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;   // Константа для красного цвета.
    private static final boolean BLACK = false;  // Константа для черного цвета.

    // Внутренний класс Node представляет узел в Красно-черном дереве.
    private class Node {
        Integer key;      // Ключ узла.
        String info;      // Значение узла.
        Node left, right; // Левый и правый дочерние узлы.
        boolean color;    // Цвет узла (RED или BLACK).

        // Конструктор для создания нового узла.
        Node(Integer key, String info, boolean color) {
            this.key = key;
            this.info = info;
            this.color = color;
        }
    }

    private Node root; // Корень дерева.
    private int size;  // Количество элементов в карте.

    // Конструктор по умолчанию.
    public MyRbMap() {
        size = 0; // Инициализация размера.
    }


    @Override
    // Возвращает строковое представление карты (например, "{k1=v1, k2=v2}").
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{"); // Начало строки.
        inorderTraversal(root, resultStr); // Обход для сбора элементов.
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2); // Удаление последней ", ".
        }
        resultStr.append("}"); // Конец строки.
        return resultStr.toString();
    }

    // Рекурсивный инфиксный обход (Л-К-П) для получения отсортированного порядка.
    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inorderTraversal(node.left, sb); // Обход левого поддерева.
            sb.append(node.key).append("=").append(node.info).append(", "); // Обработка текущего узла.
            inorderTraversal(node.right, sb); // Обход правого поддерева.
        }
    }


    // Операция вставки

    @Override
    // Добавляет или обновляет пару ключ-значение (O(logN)).
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null"); // Проверка на null.
        String oldInfo = get(key); // Получаем старое значение.
        root = put(root, key, value); // Рекурсивная вставка и балансировка.
        root.color = BLACK; // Корень всегда должен быть черным.
        return oldInfo; // Возвращаем старое значение.
    }

    // Рекурсивная вставка и балансировка LLRB-дерева.
    private Node put(Node node, Integer key, String info) {
        // Базовый случай: вставляем новый красный узел.
        if (node == null) {
            size++;
            return new Node(key, info, RED);
        }

        // Стандартный поиск BST:
        int res = key.compareTo(node.key);
        if (res < 0) {
            node.left = put(node.left, key, info); // Идем влево.
        } else if (res > 0) {
            node.right = put(node.right, key, info); // Идем вправо.
        } else {
            node.info = info; // Ключ найден, обновляем значение.
        }

        // Балансировка LLRB:
        // 1. Исправляем наклон вправо: правый красный, левый черный.
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);

        // 2. Исправляем двойной красный слева: левый красный и его левый дочерний узел красный.
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);

        // 3. Расщепляем 4-узел: оба дочерних узла красные.
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node; // Возвращаем (возможно) новый корень поддерева.
    }

    @Override
    // Вставляет все пары из другой Map.
    public void putAll(Map<? extends Integer, ? extends String> map) {
        if (map == null) throw new NullPointerException("Map cannot be null");
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }


    // Операция удаления

    @Override
    // Удаляет отображение для указанного ключа (O(logN)).
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        if (!containsKey(k)) return null; // Если ключа нет.

        String oldValue = get(k); // Получаем старое значение.

        // Перекрашиваем корень в красный (если оба дочерних узла черные) для упрощения удаления.
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = remove(root, k); // Рекурсивное удаление.

        if (root != null) root.color = BLACK; // Корень всегда черный.
        return oldValue;
    }

    // Рекурсивное удаление и балансировка LLRB-дерева.
    private Node remove(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) { // Идем влево
            // Гарантируем, что у левого узла есть "красная" связь (moveRedLeft).
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(node.left, key); // Рекурсивный вызов.
        } else { // Идем вправо или нашли узел
            if (isRed(node.left)) // Исправляем временный наклон вправо (после rotateRight).
                node = rotateRight(node);

            // Случай 0 или 1 потомка (правого) для узла-листа или узла, который будет удален.
            if (key.compareTo(node.key) == 0 && (node.right == null)) {
                size--;
                return null; // Удаляем узел.
            }

            // Гарантируем, что у правого узла есть "красная" связь (moveRedRight).
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);

            if (key.compareTo(node.key) == 0) {
                // Узел с 2 потомками: замена на преемника.
                Node min = min(node.right); // Находим минимальный в правом поддереве.
                node.key = min.key;        // Копируем ключ.
                node.info = min.info;      // Копируем значение.
                node.right = deleteMin(node.right); // Рекурсивно удаляем преемника.
            } else {
                node.right = remove(node.right, key); // Идем вправо.
            }
        }
        return balance(node); // Балансировка на обратном пути.
    }


    // Вспомогательные методы для КЧ-дерева (LLRB)

    // Проверяет, является ли узел красным (null - черный).
    private boolean isRed(Node node) {
        if (node == null) return false;
        return node.color == RED;
    }

    // Левый поворот: исправляет наклон вправо.
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color; // Цвет родителя переходит к x.
        h.color = RED;    // h становится красным (дочерним).
        return x;
    }

    // Правый поворот: исправляет двойной красный слева.
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color; // Цвет родителя переходит к x.
        h.color = RED;    // h становится красным (дочерним).
        return x;
    }

    // Перекрашивание: расщепляет 4-узел.
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Перемещение красной связи влево (для удаления).
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) { // Проверка на 4-узел справа.
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Перемещение красной связи вправо (для удаления).
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { // Проверка на 4-узел слева.
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // Балансировка узла (выполняет 3 проверки LLRB-свойств).
    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h); // 1. Исправляем наклон вправо.
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h); // 2. Исправляем двойной красный слева.
        if (isRed(h.left) && isRed(h.right)) flipColors(h); // 3. Расщепляем 4-узел.
        return h;
    }

    // Находит узел с минимальным ключом (крайний левый).
    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Удаляет минимальный узел в поддереве.
    private Node deleteMin(Node node) {
        if (node.left == null) {
            size--; // Уменьшаем размер при удалении.
            return null;
        }

        // Гарантируем "красную" связь для левого дочернего узла.
        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left); // Рекурсивное удаление.
        return balance(node); // Балансировка.
    }


    // Вспомогательные методы SortedMap и Map

    @Override
    // Возвращает значение по ключу (O(logN)).
    public String get(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null");
        if (!(obj instanceof Integer)) return null;
        Integer k = (Integer) obj;
        Node node = root;
        // Стандартный поиск в BST.
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.info; // Узел найден.
        }
        return null; // Узел не найден.
    }

    @Override
    // Проверяет, содержит ли карта ключ.
    public boolean containsKey(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null");
        if (!(obj instanceof Integer)) return false;
        Integer k = (Integer) obj;
        return get(k) != null;
    }

    @Override
    // Проверяет, содержит ли карта значение (O(N)).
    public boolean containsValue(Object obj) {
        if (obj == null) throw new NullPointerException("Value cannot be null");
        return containsValue(root, obj.toString()); // Вызываем рекурсивный обход.
    }

    // Рекурсивный обход для поиска значения.
    private boolean containsValue(Node node, String info) {
        if (node == null) return false;
        if (info.equals(node.info)) return true; // Значение найдено.
        // Ищем в левом ИЛИ правом поддереве.
        return containsValue(node.left, info) || containsValue(node.right, info);
    }

    @Override
    // Возвращает количество элементов.
    public int size() {
        return size;
    }

    @Override
    // Очищает карту.
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    // Проверяет, пуста ли карта.
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // Возвращает часть карты с ключами < toKey.
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap();
        headMapHelper(root, toKey, headMap); // Рекурсивный сбор элементов.
        return headMap;
    }

    // Вспомогательный метод для headMap.
    private void headMapHelper(Node node, Integer toKey, MyRbMap headMap) {
        if (node == null) return;

        if (node.key.compareTo(toKey) < 0) { // Если ключ подходит.
            headMap.put(node.key, node.info);
            headMapHelper(node.right, toKey, headMap); // Ищем больше ключи, но все еще < toKey.
        }
        // Всегда ищем влево, так как там могут быть ключи < toKey.
        headMapHelper(node.left, toKey, headMap);
    }

    @Override
    // Возвращает часть карты с ключами >= fromKey.
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        tailMapHelper(root, fromKey, tailMap); // Рекурсивный сбор элементов.
        return tailMap;
    }

    // Вспомогательный метод для tailMap.
    private void tailMapHelper(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) { // Если ключ подходит.
            tailMap.put(node.key, node.info);
            tailMapHelper(node.left, fromKey, tailMap); // Ищем меньшие ключи (все еще >= fromKey).
            tailMapHelper(node.right, fromKey, tailMap); // Ищем большие ключи.
        } else {
            // Ключ < fromKey, ищем только справа.
            tailMapHelper(node.right, fromKey, tailMap);
        }
    }

    @Override
    // Возвращает наименьший ключ.
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left; // Идем до самого левого.
        return node.key;
    }

    @Override
    // Возвращает наибольший ключ.
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right; // Идем до самого правого.
        return node.key;
    }

    @Override
    // Возвращает компаратор (null для естественного порядка).
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    // Возвращает Set ключей (в отсортированном порядке).
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>(); // Используем TreeSet для сортировки.
        keySetHelper(root, keys);
        return keys;
    }

    // Вспомогательный метод для сбора ключей In-Order обходом.
    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node != null) {
            keySetHelper(node.left, keys);
            keys.add(node.key);
            keySetHelper(node.right, keys);
        }
    }

    @Override
    // Возвращает Collection значений (в порядке ключей).
    public Collection<String> values() {
        List<String> values = new ArrayList<>(); // Используем List.
        valuesHelper(root, values);
        return values;
    }

    // Вспомогательный метод для сбора значений In-Order обходом.
    private void valuesHelper(Node node, List<String> values) {
        if (node != null) {
            valuesHelper(node.left, values);
            values.add(node.info);
            valuesHelper(node.right, values);
        }
    }

    @Override
    // Возвращает Set пар ключ-значение (Map.Entry).
    public Set<Map.Entry<Integer, String>> entrySet() {
        // TreeSet для сортировки по ключам.
        Set<Map.Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        entrySetHelper(root, entries);
        return entries;
    }

    // Вспомогательный метод для сбора пар Map.Entry In-Order обходом.
    private void entrySetHelper(Node node, Set<Map.Entry<Integer, String>> entries) {
        if (node != null) {
            entrySetHelper(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.info));
            entrySetHelper(node.right, entries);
        }
    }

    @Override
    // Возвращает часть карты с ключами в диапазоне [fromKey, toKey).
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, fromKey, toKey, subMap); // Рекурсивный сбор элементов.
        return subMap;
    }

    // Вспомогательный метод для subMap (эффективный обход диапазона).
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) return;

        // Ключ находится в диапазоне [fromKey, toKey).
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.info);
            subMapHelper(node.left, fromKey, toKey, subMap); // Ищем меньшие ключи.
            subMapHelper(node.right, fromKey, toKey, subMap); // Ищем большие ключи.
        }
        // Ключ меньше, чем fromKey. Ищем только справа.
        else if (node.key.compareTo(fromKey) < 0) {
            subMapHelper(node.right, fromKey, toKey, subMap);
        }
        // Ключ больше или равен toKey. Ищем только слева.
        else {
            subMapHelper(node.left, fromKey, toKey, subMap);
        }
    }
}