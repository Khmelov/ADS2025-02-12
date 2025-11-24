package by.it.group410901.kalach.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    // Константы для цветов узлов (Красно-Черное дерево)
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Внутренний класс для узла Красно-Черного дерева
    private class Node {
        Integer key;        // Ключ узла
        String info;        // Значение узла
        Node left, right;   // Левый и правый потомки
        boolean color;      // Цвет узла (RED или BLACK)

        Node(Integer key, String info, boolean color) {
            this.key = key;
            this.info = info;
            this.color = color;
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в карте

    // Конструктор по умолчанию
    public MyRbMap() {
        size = 0;
    }

    // Возвращает строковое представление карты в формате {key1=value1, key2=value2}
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{");
        inorderTraversal(root, resultStr);
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2); // Убираем последнюю запятую и пробел
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    // Вспомогательный метод для симметричного обхода дерева (возрастающий порядок ключей)
    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inorderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.info).append(", ");
            inorderTraversal(node.right, sb);
        }
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        String oldInfo = get(key);
        root = put(root, key, value);
        root.color = BLACK; // Корень всегда черный
        return oldInfo;
    }

    // Рекурсивный вспомогательный метод для вставки узла
    private Node put(Node node, Integer key, String info) {
        if (node == null) {
            size++;
            return new Node(key, info, RED); // Новые узлы всегда красные
        }

        int res = key.compareTo(node.key);
        if (res < 0) {
            node.left = put(node.left, key, info);
        } else if (res > 0) {
            node.right = put(node.right, key, info);
        } else {
            node.info = info; // Обновление значения существующего ключа
        }

        // Балансировка дерева после вставки
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    // Добавляет все элементы из переданной карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        if (map == null) throw new NullPointerException("Map cannot be null");
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // Удаляет элемент по ключу
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        if (!containsKey(k)) return null;
        String oldValue = get(k);
        root = remove(root, k);
        if (root != null) root.color = BLACK; // Корень всегда черный
        return oldValue;
    }

    // Рекурсивный вспомогательный метод для удаления узла
    private Node remove(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left)) node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && (node.right == null)) {
                size--;
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node min = min(node.right);
                node.key = min.key;
                node.info = min.info;
                node.right = deleteMin(node.right);
                size--;
            } else {
                node.right = remove(node.right, key);
            }
        }
        return balance(node);
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null");
        if (!(obj instanceof Integer)) return null;
        Integer k = (Integer) obj;
        Node node = root;
        // Итеративный поиск в двоичном дереве
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.info;
        }
        return null;
    }

    // Проверяет наличие ключа в карте
    @Override
    public boolean containsKey(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null");
        if (!(obj instanceof Integer)) return false;
        Integer k = (Integer) obj;
        return get(k) != null;
    }

    // Проверяет наличие значения в карте
    @Override
    public boolean containsValue(Object obj) {
        if (obj == null) throw new NullPointerException("Value cannot be null");
        return containsValue(root, obj.toString());
    }

    // Рекурсивный вспомогательный метод для поиска значения
    private boolean containsValue(Node node, String info) {
        if (node == null) return false;
        if (info.equals(node.info)) return true;
        return containsValue(node.left, info) || containsValue(node.right, info);
    }

    // Возвращает количество элементов в карте
    @Override
    public int size() {
        return size;
    }

    // Очищает карту
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Проверяет, пуста ли карта
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает представление части карты для ключей меньше toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap();
        headMapHelper(root, toKey, headMap);
        return headMap;
    }

    // Вспомогательный метод для построения headMap
    private void headMapHelper(Node node, Integer toKey, MyRbMap headMap) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            headMap.put(node.key, node.info);
            headMapHelper(node.right, toKey, headMap);
        }
        headMapHelper(node.left, toKey, headMap);
    }

    // Возвращает представление части карты для ключей больше или равных fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        tailMapHelper(root, fromKey, tailMap);
        return tailMap;
    }

    // Вспомогательный метод для построения tailMap
    private void tailMapHelper(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            tailMap.put(node.key, node.info);
            tailMapHelper(node.left, fromKey, tailMap);
            tailMapHelper(node.right, fromKey, tailMap);
        } else {
            tailMapHelper(node.right, fromKey, tailMap);
        }
    }

    // Возвращает первый (наименьший) ключ в карте
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    // Возвращает последний (наибольший) ключ в карте
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    // Возвращает компаратор для ключей (null означает естественный порядок)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    // Возвращает множество ключей
    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        keySetHelper(root, keys);
        return keys;
    }

    // Вспомогательный метод для построения множества ключей
    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node != null) {
            keySetHelper(node.left, keys);
            keys.add(node.key);
            keySetHelper(node.right, keys);
        }
    }

    // Возвращает коллекцию значений
    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        valuesHelper(root, values);
        return values;
    }

    // Вспомогательный метод для построения коллекции значений
    private void valuesHelper(Node node, List<String> values) {
        if (node != null) {
            valuesHelper(node.left, values);
            values.add(node.info);
            valuesHelper(node.right, values);
        }
    }

    // Возвращает множество пар ключ-значение
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        Set<Map.Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        entrySetHelper(root, entries);
        return entries;
    }

    // Вспомогательный метод для построения множества entry
    private void entrySetHelper(Node node, Set<Map.Entry<Integer, String>> entries) {
        if (node != null) {
            entrySetHelper(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.info));
            entrySetHelper(node.right, entries);
        }
    }

    // Возвращает представление части карты для ключей от fromKey (включительно) до toKey (исключительно)
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, fromKey, toKey, subMap);
        return subMap;
    }

    // Вспомогательный метод для построения subMap
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.info);
            subMapHelper(node.left, fromKey, toKey, subMap);
            subMapHelper(node.right, fromKey, toKey, subMap);
        } else if (node.key.compareTo(fromKey) < 0) {
            subMapHelper(node.right, fromKey, toKey, subMap);
        } else {
            subMapHelper(node.left, fromKey, toKey, subMap);
        }
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ КРАСНО-ЧЕРНОГО ДЕРЕВА ===

    // Проверяет, является ли узел красным
    private boolean isRed(Node node) {
        if (node == null) return false;
        return node.color == RED;
    }

    // Левое вращение
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    // Правое вращение
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    // Смена цветов узла и его потомков
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Перемещение красного узла влево (для удаления)
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Перемещение красного узла вправо (для удаления)
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // Балансировка узла
    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    // Находит узел с минимальным ключом в поддереве
    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Удаляет узел с минимальным ключом из поддерева
    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
        node.left = deleteMin(node.left);
        return balance(node);
    }
}