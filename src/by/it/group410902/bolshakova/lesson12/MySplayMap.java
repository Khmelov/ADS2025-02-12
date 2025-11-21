package by.it.group410902.bolshakova.lesson12;
import java.util.*;

// Splay-дерево, самобалансирующееся бинарное дерево поиска,
// которое перемещает часто используемые элементы ближе к корню
public class MySplayMap implements NavigableMap<Integer, String> {

    // Узел Splay-дерева
    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        // Конструктор узла
        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root = null;
    private int size = 0;

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {  // Добавление или обновление элемента
        String oldValue = get(key); // get() уже делает splay
        if (oldValue == null) {
            size++;
            insert(key, value);
        } else {
            // Элемент уже находится в корне после get()
            root.value = value;
        }
        return oldValue;
    }

    @Override
    public String remove(Object key) { // Удаление элемента по ключу
        if (!(key instanceof Integer)) return null;

        String oldValue = get(key); // get() перемещает элемент в корень если найден
        if (oldValue != null) {
            size--;
            removeRoot();
        }
        return oldValue;
    }

    @Override
    public String get(Object key) { // Получение значения по ключу
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key); // Ищем узел (с splay)
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {// Проверка наличия ключа
        return get(key) != null;// Используем get() который делает splay
    }

    @Override
    public boolean containsValue(Object value) { // Проверка наличия значения
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value); // Рекурсивный поиск
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Навигационные методы
    @Override
    public Integer lowerKey(Integer key) {// Наибольший ключ меньше заданного
        Node lower = findLower(root, key); // Ищем меньший ключ
        if (lower != null) splay(lower);
        return lower == null ? null : lower.key;
    }
    // Наибольший ключ меньше или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        if (find(key) != null) return key;  // Точное совпадение
        return lowerKey(key); // Ближайший меньший
    }

    @Override
    public Integer ceilingKey(Integer key) {    // Наименьший ключ больше или равный заданному
        if (find(key) != null) return key;
        return higherKey(key);  // Ближайший больший
    }

    @Override
    public Integer higherKey(Integer key) {  // Наименьший ключ больше заданного
        Node higher = findHigher(root, key);// Ищем больший ключ
        if (higher != null) splay(higher);
        return higher == null ? null : higher.key;
    }

    @Override
    public Integer firstKey() { // наименьший ключ
        if (root == null) return null;
        Node min = findMin(root); // Ищем минимальный узел
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {//наибольший ключ
        if (root == null) return null; // Ищем максимальный узел
        Node max = findMax(root);
        splay(max);
        return max.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {    // Карта с ключами меньше toKey
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        buildHeadMap(root, toKey, result); //заполняем
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { // Карта с ключами больше или равными fromKey
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        buildTailMap(root, fromKey, result);
        return result;
    }

    private Node find(Integer key) {    // Поиск узла с перемещением в корень (splay)
        Node node = root;
        while (node != null) {
            if (key < node.key) node = node.left;
            else if (key > node.key) node = node.right;
            else {
                splay(node); //если нашли перемещаем в корень
                return node;
            }
        }
        return null;
    }
    // Вставка нового узла
    private void insert(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);//первый элемент
            return;
        }

        Node current = root;
        Node parent = null;
        // Ищем место для вставки
        while (current != null) {
            parent = current;
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
        }
        // Создаем новый узел
        Node newNode = new Node(key, value, parent);
        if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;

        splay(newNode);
    }
    // Удаление корневого узла
    private void removeRoot() {
        if (root.left == null) {
            // Нет левого поддерева
            root = root.right;
            if (root != null) root.parent = null;
        } else if (root.right == null) {     // Нет правого поддерева
            root = root.left;
            if (root != null) root.parent = null;
        } else {   // Есть оба поддерева
            Node leftSubtree = root.left;
            Node rightSubtree = root.right;
            // Отсоединяем поддеревья
            leftSubtree.parent = null;
            rightSubtree.parent = null;

            // Находим максимум в левом поддереве и делаем его корнем
            Node maxLeft = findMax(leftSubtree);
            splay(maxLeft); // maxLeft - корень левого поддерева

            maxLeft.right = rightSubtree;
            rightSubtree.parent = maxLeft;
            root = maxLeft; // Новый корень
        }
    }

    // Splay операция - перемещает узел в корень
    private void splay(Node node) {
        while (node.parent != null) { // Пока не добрались до корня
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Zig родитель является корнем
                if (node == parent.left) rightRotate(parent);
                else leftRotate(parent);
            } else {
                if (parent == grandparent.left) {
                    if (node == parent.left) {
                        // Zig-zig
                        rightRotate(grandparent);
                        rightRotate(parent);
                    } else {
                        // Zig-zag
                        leftRotate(parent);
                        rightRotate(grandparent);
                    }
                } else {
                    if (node == parent.right) {
                        // Zig-zig
                        leftRotate(grandparent);
                        leftRotate(parent);
                    } else {
                        // Zig-zag
                        rightRotate(parent);
                        leftRotate(grandparent);
                    }
                }
            }
        }
        root = node;// Обновляем корень
    }
    // Правый поворот
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;   // Правое поддерево y становится левым x
        if (y.right != null) y.right.parent = x;

        y.parent = x.parent;  // y наследует родителя x
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        y.right = x;
        x.parent = y;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;

        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private Node findLower(Node node, Integer key) {
        Node lower = null;
        while (node != null) {
            if (key > node.key) {
                lower = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return lower;
    }

    private Node findHigher(Node node, Integer key) {
        Node higher = null;
        while (node != null) {
            if (key < node.key) {
                higher = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return higher;
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) {
            splay(node);
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private void buildHeadMap(Node node, Integer toKey, MySplayMap map) {
        if (node == null) return;
        buildHeadMap(node.left, toKey, map);
        if (node.key < toKey) {
            map.put(node.key, node.value);
            buildHeadMap(node.right, toKey, map);
        }
    }

    private void buildTailMap(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) return;
        buildTailMap(node.right, fromKey, map);
        if (node.key >= fromKey) {
            map.put(node.key, node.value);
            buildTailMap(node.left, fromKey, map);
        }
    }

    // Не реализованные методы NavigableMap
    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
}