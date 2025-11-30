package by.it.group451001.yarkovich.lesson12;

import java.util.SortedMap;

/**
 * Реализация интерфейса SortedMap на основе красно-черного дерева.
 * Красно-черное дерево - это самобалансирующееся бинарное дерево поиска,
 * которое гарантирует логарифмическое время выполнения основных операций.
 */
public class MyRbMap implements SortedMap<Integer, String> {

    // Константы для цветов узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * Внутренний класс для представления узла красно-черного дерева.
     * Каждый узел содержит ключ, значение, цвет и ссылки на потомков и родителя.
     */
    private static class RbNode {
        Integer key;            // Ключ узла
        String value;          // Значение узла
        boolean color;         // Цвет узла (RED или BLACK)
        RbNode left;           // Левый потомок
        RbNode right;          // Правый потомок
        RbNode parent;         // Родительский узел

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode root;       // Корень красно-черного дерева
    private int size;          // Количество элементов в карте
    private final RbNode NIL;  // Специальный листовой узел (NIL)

    /**
     * Конструктор создает пустую карту с NIL-узлом в качестве корня.
     */
    public MyRbMap() {
        NIL = new RbNode(null, null, BLACK);
        root = NIL;
        size = 0;
    }

    /**
     * Возвращает количество элементов в карте.
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли карта.
     * @return true если карта пуста, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Удаляет все элементы из карты.
     */
    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    /**
     * Добавляет или обновляет пару ключ-значение в карте.
     * Если ключ уже существует, значение обновляется.
     * @param key ключ для добавления
     * @param value значение, ассоциированное с ключом
     * @return предыдущее значение, ассоциированное с ключом, или null если ключа не было
     */
    @Override
    public String put(Integer key, String value) {
        // Ищем место для вставки как в обычном бинарном дереве поиска
        RbNode parent = NIL;
        RbNode current = root;

        while (current != NIL) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        // Создаем новый узел красного цвета
        RbNode newNode = new RbNode(key, value, RED);
        newNode.left = NIL;
        newNode.right = NIL;
        newNode.parent = parent;

        // Вставляем узел в дерево
        if (parent == NIL) {
            root = newNode; // Дерево было пустым
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;

        // Балансируем дерево после вставки
        fixInsert(newNode);

        return null;
    }

    /**
     * Балансировка дерева после вставки для сохранения свойств красно-черного дерева.
     * Обрабатывает три основных случая нарушения свойств.
     */
    private void fixInsert(RbNode node) {
        // Пока родитель узла красный (нарушение свойства 4)
        while (node != root && node.parent.color == RED) {

            if (node.parent == node.parent.parent.left) {
                // Родитель является левым потомком деда
                RbNode uncle = node.parent.parent.right;

                if (uncle.color == RED) {
                    // Случай 1: дядя красный - перекрашиваем
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent; // Переходим к деду
                } else {
                    // Случай 2: дядя черный
                    if (node == node.parent.right) {
                        // Случай 2a: узел является правым потомком - левый поворот
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Случай 2b: узел является левым потомком - правый поворот и перекрашивание
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Родитель является правым потомком деда (симметричный случай)
                RbNode uncle = node.parent.parent.left;

                if (uncle.color == RED) {
                    // Случай 1: дядя красный - перекрашиваем
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    // Случай 2: дядя черный
                    if (node == node.parent.left) {
                        // Случай 2a: узел является левым потомком - правый поворот
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Случай 2b: узел является правым потомком - левый поворот и перекрашивание
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }

        // Корень всегда должен быть черным (свойство 2)
        root.color = BLACK;
    }

    /**
     * Удаляет ключ и ассоциированное значение из карты.
     * @param key ключ для удаления
     * @return значение, ассоциированное с удаленным ключом, или null если ключ не найден
     */
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        // Находим узел для удаления
        RbNode node = findNode(root, intKey);
        if (node == NIL) {
            return null; // Ключ не найден
        }

        String removedValue = node.value;
        deleteNode(node);
        size--;

        return removedValue;
    }

    /**
     * Удаляет узел из красно-черного дерева.
     * Обрабатывает три случая: узел без потомков, с одним потомком и с двумя потомками.
     */
    private void deleteNode(RbNode node) {
        RbNode y = node;           // Узел, который будет фактически удален
        RbNode x;                  // Узел, который займет место y
        boolean yOriginalColor = y.color;

        if (node.left == NIL) {
            // Узел имеет только правого потомка или не имеет потомков
            x = node.right;
            transplant(node, node.right);
        } else if (node.right == NIL) {
            // Узел имеет только левого потомка
            x = node.left;
            transplant(node, node.left);
        } else {
            // Узел имеет двух потомков - находим преемника (минимальный в правом поддереве)
            y = findMin(node.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == node) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }

            // Заменяем удаляемый узел его преемником
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        // Если удаленный узел был черным, нужно балансировать дерево
        if (yOriginalColor == BLACK) {
            fixDelete(x);
        }
    }

    /**
     * Балансировка дерева после удаления для сохранения свойств красно-черного дерева.
     */
    private void fixDelete(RbNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                // x является левым потомком
                RbNode w = x.parent.right; // Брат x

                if (w.color == RED) {
                    // Случай 1: брат красный
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == BLACK && w.right.color == BLACK) {
                    // Случай 2: оба потомка брата черные
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        // Случай 3: левый потомок брата красный, правый черный
                        w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    // Случай 4: правый потомок брата красный
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                // Симметричный случай: x является правым потомком
                RbNode w = x.parent.left;

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    /**
     * Заменяет поддерево с корнем u на поддерево с корнем v.
     */
    private void transplant(RbNode u, RbNode v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    /**
     * Возвращает значение, ассоциированное с указанным ключом.
     * @param key ключ для поиска
     * @return значение, ассоциированное с ключом, или null если ключ не найден
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        RbNode node = findNode(root, intKey);
        return (node != NIL) ? node.value : null;
    }

    /**
     * Находит узел с заданным ключом в дереве.
     */
    private RbNode findNode(RbNode node, Integer key) {
        while (node != NIL) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return NIL;
    }

    /**
     * Проверяет, содержится ли указанный ключ в карте.
     * @param key ключ для проверки
     * @return true если ключ содержится в карте, false в противном случае
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Проверяет, содержится ли указанное значение в карте.
     * Выполняет линейный поиск по всему дереву.
     * @param value значение для поиска
     * @return true если значение содержится в карте, false в противном случае
     */
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    /**
     * Рекурсивный поиск значения в дереве.
     */
    private boolean containsValueRecursive(RbNode node, Object value) {
        if (node == NIL) {
            return false;
        }

        // Проверяем текущий узел
        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;
        }

        // Рекурсивно проверяем левое и правое поддеревья
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    /**
     * Возвращает вид карты для ключей, строго меньших заданного ключа.
     * @param toKey верхняя граница (исключающая)
     * @return отсортированная карта, содержащая только ключи меньше toKey
     */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

    /**
     * Рекурсивное построение headMap.
     */
    private void headMapRecursive(RbNode node, Integer toKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            // Ключ меньше toKey - добавляем и проверяем оба поддерева
            headMapRecursive(node.left, toKey, result);
            result.put(node.key, node.value);
            headMapRecursive(node.right, toKey, result);
        } else {
            // Ключ >= toKey - проверяем только левое поддерево
            headMapRecursive(node.left, toKey, result);
        }
    }

    /**
     * Возвращает вид карты для ключей, больших или равных заданному ключу.
     * @param fromKey нижняя граница (включающая)
     * @return отсортированная карта, содержащая только ключи >= fromKey
     */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    /**
     * Рекурсивное построение tailMap.
     */
    private void tailMapRecursive(RbNode node, Integer fromKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            // Ключ >= fromKey - добавляем и проверяем оба поддерева
            tailMapRecursive(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMapRecursive(node.right, fromKey, result);
        } else {
            // Ключ < fromKey - проверяем только правое поддерево
            tailMapRecursive(node.right, fromKey, result);
        }
    }

    /**
     * Возвращает первый (наименьший) ключ в карте.
     * @return первый ключ
     * @throws java.util.NoSuchElementException если карта пуста
     */
    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        return findMin(root).key;
    }

    /**
     * Возвращает последний (наибольший) ключ в карте.
     * @return последний ключ
     * @throws java.util.NoSuchElementException если карта пуста
     */
    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        return findMax(root).key;
    }

    /**
     * Находит узел с минимальным ключом в поддереве.
     */
    private RbNode findMin(RbNode node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }

    /**
     * Находит узел с максимальным ключом в поддереве.
     */
    private RbNode findMax(RbNode node) {
        while (node.right != NIL) {
            node = node.right;
        }
        return node;
    }

    /**
     * Возвращает строковое представление карты в порядке возрастания ключей.
     * Формат: {key1=value1, key2=value2, ...}
     * @return строковое представление карты
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Центрированный обход дерева для построения строкового представления.
     */
    private void inOrderTraversal(RbNode node, StringBuilder sb) {
        if (node != NIL) {
            // Рекурсивно обходим левое поддерево
            if (node.left != NIL) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Рекурсивно обходим правое поддерево
            if (node.right != NIL) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    // Методы поворотов для балансировки

    /**
     * Левый поворот вокруг узла x.
     */
    private void rotateLeft(RbNode x) {
        RbNode y = x.right;
        x.right = y.left;

        if (y.left != NIL) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    /**
     * Правый поворот вокруг узла y.
     */
    private void rotateRight(RbNode y) {
        RbNode x = y.left;
        y.left = x.right;

        if (x.right != NIL) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == NIL) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    // Остальные методы интерфейса SortedMap...

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not implemented");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }
}