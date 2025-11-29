package by.it.group410902.yarmashuk.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Object, Object> {

    // Внутренний статический класс для представления узла Splay-дерева
    private static class Node {
        Object key;
        Object value;
        Node left;
        Node right;

        Node(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        // Приватный конструктор для использования внутри splay-операции (для dummy-узлов)
        private Node(Object key, Object value, Node left, Node right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    private Node root; // Корень Splay-дерева
    private int size;  // Количество элементов в мапе

    public MySplayMap() {
        this.root = null;
        this.size = 0;
    }


    // Вспомогательные методы Splay-дерева

    @SuppressWarnings("unchecked")
    private int compare(Object obj1, Object obj2) {
        return ((Comparable<Object>) obj1).compareTo(obj2);
    }

    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private void splay(Object key) {
        if (root == null) return; // Если дерево пусто, делать нечего

        // Создаем фиктивные узлы для построения левого и правого поддеревьев
        Node dummy = new Node(null, null); // Заглушка для корня левой/правой части
        Node leftMax = dummy;  // Указывает на самый правый узел в накопленной левой части
        Node rightMin = dummy; // Указывает на самый левый узел в накопленной правой части

        Node current = root; // Текущий обрабатываемый узел при обходе

        while (true) {
            int cmp = compare(key, current.key);

            if (cmp < 0) { // Искомый ключ меньше, идем в левое поддерево
                if (current.left == null) break; // Ключ не найден, current - ближайший. Останавливаемся.

                // Проверка на Zig-Zig (лево-лево)
                if (compare(key, current.left.key) < 0) {
                    current = rotateRight(current); // Выполняем правое вращение
                    if (current.left == null) break; // Если current.left стал null после вращения, останавливаемся.
                }

                // Прикрепляем 'current' к правой части (его ключ теперь больше `key`)
                rightMin.left = current;
                rightMin = current;
                current = current.left; // Переходим к левому потомку current
            } else if (cmp > 0) { // Искомый ключ больше, идем в правое поддерево
                if (current.right == null) break; // Ключ не найден, current - ближайший. Останавливаемся.

                // Проверка на Zig-Zig (право-право)
                if (compare(key, current.right.key) > 0) {
                    current = rotateLeft(current); // Выполняем левое вращение
                    if (current.right == null) break; // Если current.right стал null после вращения, останавливаемся.
                }

                // Прикрепляем 'current' к левой части (его ключ теперь меньше `key`)
                leftMax.right = current;
                leftMax = current;
                current = current.right; // Переходим к правому потомку current
            } else { // Ключ совпадает с current.key
                break; // Ключ найден, current - узел для Splay-операции
            }
        }

        // Пересобираем дерево:
        // 'current' - это узел, который перемещен в корень.
        // 'dummy.right' (который был 'leftMax.right') - это корень накопленного левого поддерева.
        // 'dummy.left' (который был 'rightMin.left') - это корень накопленного правого поддерева.

        // Самый правый узел в левой части указывает на изначального левого потомка 'current'.
        leftMax.right = current.left;
        // Самый левый узел в правой части указывает на изначального правого потомка 'current'.
        rightMin.left = current.right;

        // Новым левым потомком 'current' становится корень накопленной левой части.
        current.left = dummy.right;
        // Новым правым потомком 'current' становится корень накопленной правой части.
        current.right = dummy.left;

        root = current; // Обновляем глобальный корень
    }

    private Node treeMinimum(Node node) {
        if (node == null) return null;
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node treeMaximum(Node node) {
        if (node == null) return null;
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
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
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        // Используем массив из одного элемента для флага, чтобы корректно
        // обрабатывать запятые (не ставить перед первым элементом).
        boolean[] isFirst = {true};
        inOrderTraversalToString(root, sb, isFirst);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversalToString(Node node, StringBuilder sb, boolean[] isFirst) {
        if (node != null) {
            inOrderTraversalToString(node.left, sb, isFirst);

            if (!isFirst[0]) {
                sb.append(", "); // Добавляем разделитель перед каждым элементом, кроме первого
            }
            sb.append(node.key);
            sb.append("=");
            // StringBuilder корректно обрабатывает null-значения, выводя "null"
            sb.append(node.value);
            isFirst[0] = false; // После добавления первого элемента, устанавливаем флаг в false

            inOrderTraversalToString(node.right, sb, isFirst);
        }
    }
    @Override
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        if (root == null) { // Если дерево пусто, новый узел становится корнем
            root = new Node(key, value);
            size++;
            return null;
        }

        splay(key); // Перемещаем узел с ключом (или ближайший) в корень

        int cmp = compare(key, root.key);
        if (cmp == 0) { // Ключ уже существует, обновляем значение
            Object oldValue = root.value;
            root.value = value;
            return oldValue;
        } else { // Ключ не существует, вставляем новый узел
            Node newNode = new Node(key, value);
            if (cmp < 0) { // Новый ключ меньше ключа корня
                newNode.left = root.left; // Левый потомок старого корня становится левым потомком нового узла
                newNode.right = root;     // Старый корень становится правым потомком нового узла
                root.left = null;         // Отсоединяем левого потомка от старого корня
            } else { // Новый ключ больше ключа корня
                newNode.right = root.right; // Правый потомок старого корня становится правым потомком нового узла
                newNode.left = root;      // Старый корень становится левым потомком нового узла
                root.right = null;        // Отсоединяем правого потомка от старого корня
            }
            root = newNode; // Новый узел становится новым корнем
            size++;
            return null;
        }
    }
    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (root == null) {
            return null;
        }

        splay(key); // Перемещаем узел с ключом (или ближайший) в корень

        if (compare(key, root.key) == 0) {
            return root.value; // Ключ найден
        } else {
            return null; // Ключ не найден
        }
    }

    @Override
    public Object remove(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (root == null) {
            return null;
        }

        splay(key); // Перемещаем узел с ключом (или ближайший) в корень

        if (compare(key, root.key) != 0) { // Ключ не найден в корне после Splay-операции
            return null;
        }

        Object oldValue = root.value;
        size--;

        Node leftChild = root.left;
        Node rightChild = root.right;

        if (leftChild == null) { // У удаляемого узла нет левого потомка
            root = rightChild; // Правый потомок становится новым корнем
        } else if (rightChild == null) { // У удаляемого узла нет правого потомка
            root = leftChild; // Левый потомок становится новым корнем
        } else { // У удаляемого узла есть оба потомка
            // Делаем левого потомка временным новым корнем
            root = leftChild;
            // Выполняем splay-операцию для (изначально удаляемого) ключа в этом новом поддереве (старом левом потомке).
            // Это перемещает самый большой элемент в этом поддереве в его корень.
            splay(key);
            // Присоединяем правое поддерево (изначально удаляемого узла) к правому потомку нового корня.
            root.right = rightChild;
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<?, ?> m) {

    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (root == null) {
            return false;
        }

        splay(key); // Перемещаем ключ (или ближайший) в корень
        return compare(key, root.key) == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        // O(N) операция, требует полного обхода дерева
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) {
            return false;
        }
        // Проверяем текущий узел
        if (value == null) {
            if (node.value == null) {
                return true;
            }
        } else {
            if (value.equals(node.value)) { // Используем equals для сравнения объектов
                return true;
            }
        }
        // Проверяем левое и правое поддеревья
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }
    @Override
    public Object firstKey() {
        if (isEmpty()) {
            throw new IllegalStateException("Map is empty.");
        }
        Node minNode = treeMinimum(root); // Находим узел с минимальным ключом (без Splay)
        splay(minNode.key); // Перемещаем его в корень
        return root.key;
    }

    @Override
    public Object lastKey() {
        if (isEmpty()) {
            throw new IllegalStateException("Map is empty.");
        }
        Node maxNode = treeMaximum(root); // Находим узел с максимальным ключом (без Splay)
        splay(maxNode.key); // Перемещаем его в корень
        return root.key;
    }

    @Override
    public Set<Object> keySet() {
        return Set.of();
    }

    @Override
    public Collection<Object> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return Set.of();
    }

    @Override
    public Entry<Object, Object> lowerEntry(Object key) {
        return null;
    }

    @Override
    public Object lowerKey(Object key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        if (root == null) return null;

        splay(key); // Перемещаем ключ (или ближайший узел) в корень

        // Если root.key строго меньше `key`, то root.key является искомым `lowerKey`.
        if (compare(root.key, key) < 0) {
            return root.key;
        } else { // root.key >= key (root.key - это `key` или что-то большее)
            // `lowerKey` должен быть в левом поддереве.
            Node maxInLeft = treeMaximum(root.left);
            return (maxInLeft != null) ? maxInLeft.key : null;
        }
    }

    @Override
    public Entry<Object, Object> floorEntry(Object key) {
        return null;
    }

    @Override
    public Object floorKey(Object key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        if (root == null) return null;

        splay(key); // Перемещаем ключ (или ближайший узел) в корень

        // Если root.key меньше или равен `key`, то root.key является искомым `floorKey`.
        if (compare(root.key, key) <= 0) {
            return root.key;
        } else { // root.key > key
            // `floorKey` должен быть в левом поддереве.
            Node maxInLeft = treeMaximum(root.left);
            return (maxInLeft != null) ? maxInLeft.key : null;
        }
    }

    @Override
    public Entry<Object, Object> ceilingEntry(Object key) {
        return null;
    }

    @Override
    public Object ceilingKey(Object key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        if (root == null) return null;

        splay(key); // Перемещаем ключ (или ближайший узел) в корень

        // Если root.key больше или равен `key`, то root.key является искомым `ceilingKey`.
        if (compare(root.key, key) >= 0) {
            return root.key;
        } else { // root.key < key
            // `ceilingKey` должен быть в правом поддереве.
            Node minInRight = treeMinimum(root.right);
            return (minInRight != null) ? minInRight.key : null;
        }
    }

    @Override
    public Entry<Object, Object> higherEntry(Object key) {
        return null;
    }

    @Override
    public Object higherKey(Object key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        if (root == null) return null;

        splay(key); // Перемещаем ключ (или ближайший узел) в корень

        // Если root.key строго больше `key`, то root.key является искомым `higherKey`.
        if (compare(root.key, key) > 0) {
            return root.key;
        } else { // root.key <= key
            // `higherKey` должен быть в правом поддереве.
            Node minInRight = treeMinimum(root.right);
            return (minInRight != null) ? minInRight.key : null;
        }
    }

    @Override
    public Entry<Object, Object> firstEntry() {
        return null;
    }

    @Override
    public Entry<Object, Object> lastEntry() {
        return null;
    }

    @Override
    public Entry<Object, Object> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Object, Object> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Object, Object> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Object> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Object> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Object, Object> subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Object, Object> headMap(Object toKey) {
        return headMap(toKey, false); // По умолчанию headMap является эксклюзивным по toKey
    }

    @Override
    public NavigableMap<Object, Object> headMap(Object toKey, boolean inclusive) {
        if (toKey == null) {
            throw new IllegalArgumentException("toKey cannot be null.");
        }
        MySplayMap subMap = new MySplayMap();
        // lowerBound: null, upperBound: toKey.
        // isLowerInclusive: false (неважно), isUpperInclusive: inclusive.
        inOrderCopy(root, subMap, null, toKey, false, inclusive);
        return subMap;
    }

    @Override
    public NavigableMap<Object, Object> tailMap(Object fromKey) {
        return tailMap(fromKey, true); // По умолчанию tailMap является инклюзивным по fromKey
    }

    @Override
    public NavigableMap<Object, Object> tailMap(Object fromKey, boolean inclusive) {
        if (fromKey == null) {
            throw new IllegalArgumentException("fromKey cannot be null.");
        }
        MySplayMap subMap = new MySplayMap();
        // lowerBound: fromKey, upperBound: null.
        // isLowerInclusive: inclusive, isUpperInclusive: false (неважно).
        inOrderCopy(root, subMap, fromKey, null, inclusive, false);
        return subMap;
    }

    @Override
    public Comparator<? super Object> comparator() {
        return null;
    }

    @Override
    public SortedMap<Object, Object> subMap(Object fromKey, Object toKey) {
        return null;
    }

    private void inOrderCopy(Node node, MySplayMap targetMap, Object lowerBound, Object upperBound,
                             boolean isLowerInclusive, boolean isUpperInclusive) {
        if (node == null) {
            return;
        }

        boolean currentKeyIsTooSmall = false;
        if (lowerBound != null) {
            int cmp = compare(node.key, lowerBound);
            if (cmp < 0 || (cmp == 0 && !isLowerInclusive)) {
                currentKeyIsTooSmall = true;
            }
        }

        boolean currentKeyIsTooLarge = false;
        if (upperBound != null) {
            int cmp = compare(node.key, upperBound);
            if (cmp > 0 || (cmp == 0 && !isUpperInclusive)) {
                currentKeyIsTooLarge = true;
            }
        }

        // Обходим левое поддерево, если оно может содержать элементы в диапазоне
        // (т.е. текущий узел не слишком мал для нижней границы, или еще не слишком велик для верхней)
        if (!currentKeyIsTooSmall) {
            inOrderCopy(node.left, targetMap, lowerBound, upperBound, isLowerInclusive, isUpperInclusive);
        }

        // Добавляем текущий узел, если его ключ находится в диапазоне
        if (!currentKeyIsTooSmall && !currentKeyIsTooLarge) {
            targetMap.put(node.key, node.value);
        }

        // Обходим правое поддерево, если оно может содержать элементы в диапазоне
        if (!currentKeyIsTooLarge) {
            inOrderCopy(node.right, targetMap, lowerBound, upperBound, isLowerInclusive, isUpperInclusive);
        }
    }






}




