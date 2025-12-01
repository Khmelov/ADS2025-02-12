package by.it.group451002.stsefanovich.lesson12;

import java.util.NavigableMap;

/**
 * Реализация интерфейса NavigableMap на основе splay-дерева.
 * Splay-дерево - это самобалансирующееся бинарное дерево поиска,
 * которое перемещает недавно доступные элементы к корню дерева,
 * обеспечивая быстрый доступ к часто используемым элементам.
 * Основная операция - "splay" (расширение), которая перемещает узел в корень.
 */
public class MySplayMap implements NavigableMap<Integer, String> {

    /**
     * Внутренний класс для представления узла splay-дерева.
     * Каждый узел содержит ключ, значение и ссылки на потомков.
     */
    private static class SplayNode {
        Integer key;           // Ключ узла
        String value;          // Значение узла
        SplayNode left;        // Левый потомок
        SplayNode right;       // Правый потомок
        SplayNode parent;      // Родительский узел

        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode root;    // Корень splay-дерева
    private int size;          // Количество элементов в карте

    /**
     * Конструктор создает пустую карту.
     */
    public MySplayMap() {
        root = null;
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
        root = null;
        size = 0;
    }

    /**
     * Добавляет или обновляет пару ключ-значение в карте.
     * После вставки выполняется операция splay для перемещения нового узла в корень.
     * @param key ключ для добавления
     * @param value значение, ассоциированное с ключом
     * @return предыдущее значение, ассоциированное с ключом, или null если ключа не было
     */
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            // Дерево пустое - создаем корень
            root = new SplayNode(key, value);
            size++;
            return null;
        }

        // Ищем место для вставки
        SplayNode current = root;
        SplayNode parent = null;

        while (current != null) {
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
                splay(current); // Splay к корню
                return oldValue;
            }
        }

        // Создаем новый узел
        SplayNode newNode = new SplayNode(key, value);
        newNode.parent = parent;

        // Вставляем узел в дерево
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        splay(newNode); // Splay нового узла к корню
        return null;
    }

    /**
     * Основная операция splay-дерева - перемещает узел в корень дерева.
     * Выполняет серию поворотов (zig, zig-zig, zig-zag) для поднятия узла.
     * @param node узел для перемещения в корень
     */
    private void splay(SplayNode node) {
        while (node.parent != null) {
            if (node.parent.parent == null) {
                // Zig: узел является непосредственным потомком корня
                if (node == node.parent.left) {
                    rotateRight(node.parent);
                } else {
                    rotateLeft(node.parent);
                }
            } else if (node == node.parent.left && node.parent == node.parent.parent.left) {
                // Zig-zig: узел и родитель - левые потомки
                rotateRight(node.parent.parent);
                rotateRight(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.right) {
                // Zig-zig: узел и родитель - правые потомки
                rotateLeft(node.parent.parent);
                rotateLeft(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.left) {
                // Zig-zag: узел - правый потомок, родитель - левый потомок
                rotateLeft(node.parent);
                rotateRight(node.parent);
            } else {
                // Zig-zag: узел - левый потомок, родитель - правый потомок
                rotateRight(node.parent);
                rotateLeft(node.parent);
            }
        }
        root = node;
    }

    /**
     * Левый поворот вокруг узла x.
     */
    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        if (y != null) {
            y.left = x;
        }
        x.parent = y;
    }

    /**
     * Правый поворот вокруг узла y.
     */
    private void rotateRight(SplayNode y) {
        SplayNode x = y.left;
        if (x != null) {
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            x.parent = y.parent;
        }

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        if (x != null) {
            x.right = y;
        }
        y.parent = x;
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
        SplayNode node = findNode(intKey);
        if (node == null) {
            return null; // Ключ не найден
        }

        String removedValue = node.value;

        // Splay удаляемого узла к корню
        splay(node);

        // Удаляем узел из дерева
        if (node.left == null) {
            // Нет левого поддерева - заменяем правым поддеревом
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (node.right == null) {
            // Нет правого поддерева - заменяем левым поддеревом
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            // Есть оба поддерева - находим максимальный элемент в левом поддереве
            SplayNode maxLeft = findMax(node.left);

            // Splay максимального элемента левого поддерева к корню
            splay(maxLeft);

            // Присоединяем правое поддерево удаляемого узла
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;
            }

            root = maxLeft;
            root.parent = null;
        }

        size--;
        return removedValue;
    }

    /**
     * Возвращает значение, ассоциированное с указанным ключом.
     * После доступа выполняется операция splay для перемещения узла в корень.
     * @param key ключ для поиска
     * @return значение, ассоциированное с ключом, или null если ключ не найден
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        SplayNode node = findNode(intKey);
        if (node != null) {
            splay(node); // Splay найденного узла к корню
            return node.value;
        }
        return null;
    }

    /**
     * Находит узел с заданным ключом в дереве.
     */
    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
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
    private boolean containsValueRecursive(SplayNode node, Object value) {
        if (node == null) {
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
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

    /**
     * Рекурсивное построение headMap.
     */
    private void headMapRecursive(SplayNode node, Integer toKey, MySplayMap result) {
        if (node == null) {
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
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    /**
     * Рекурсивное построение tailMap.
     */
    private void tailMapRecursive(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node == null) {
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
    private SplayNode findMin(SplayNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Находит узел с максимальным ключом в поддереве.
     */
    private SplayNode findMax(SplayNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;//
    }

    /**
     * Возвращает наибольший ключ, строго меньший заданного ключа.
     * @param key ключ для сравнения
     * @return наибольший ключ меньше заданного, или null если такого ключа нет
     */
    @Override
    public Integer lowerKey(Integer key) {
        SplayNode result = lowerKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    /**
     * Рекурсивный поиск наибольшего ключа, строго меньшего заданного.
     */
    private SplayNode lowerKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) < 0) {
            // Текущий узел меньше ключа - может быть кандидатом
            candidate = node;
            // Ищем больший ключ в правом поддереве
            return lowerKeyRecursive(node.right, key, candidate);
        } else {
            // Текущий узел >= ключа - ищем в левом поддереве
            return lowerKeyRecursive(node.left, key, candidate);
        }
    }

    /**
     * Возвращает наибольший ключ, меньший или равный заданному ключу.
     * @param key ключ для сравнения
     * @return наибольший ключ <= заданного, или null если такого ключа нет
     */
    @Override
    public Integer floorKey(Integer key) {
        SplayNode result = floorKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    /**
     * Рекурсивный поиск наибольшего ключа, меньшего или равного заданному.
     */
    private SplayNode floorKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) <= 0) {
            // Текущий узел <= ключа - может быть кандидатом
            candidate = node;
            // Ищем больший ключ в правом поддереве
            return floorKeyRecursive(node.right, key, candidate);
        } else {
            // Текущий узел > ключа - ищем в левом поддереве
            return floorKeyRecursive(node.left, key, candidate);
        }
    }

    /**
     * Возвращает наименьший ключ, больший или равный заданному ключу.
     * @param key ключ для сравнения
     * @return наименьший ключ >= заданного, или null если такого ключа нет
     */
    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode result = ceilingKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    /**
     * Рекурсивный поиск наименьшего ключа, большего или равного заданному.
     */
    private SplayNode ceilingKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) >= 0) {
            // Текущий узел >= ключа - может быть кандидатом
            candidate = node;
            // Ищем меньший ключ в левом поддереве
            return ceilingKeyRecursive(node.left, key, candidate);
        } else {
            // Текущий узел < ключа - ищем в правом поддереве
            return ceilingKeyRecursive(node.right, key, candidate);
        }
    }

    /**
     * Возвращает наименьший ключ, строго больший заданного ключа.
     * @param key ключ для сравнения
     * @return наименьший ключ больше заданного, или null если такого ключа нет
     */
    @Override
    public Integer higherKey(Integer key) {
        SplayNode result = higherKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    /**
     * Рекурсивный поиск наименьшего ключа, строго большего заданного.
     */
    private SplayNode higherKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;
        }

        if (node.key.compareTo(key) > 0) {
            // Текущий узел > ключа - может быть кандидатом
            candidate = node;
            // Ищем меньший ключ в левом поддереве
            return higherKeyRecursive(node.left, key, candidate);
        } else {
            // Текущий узел <= ключа - ищем в правом поддереве
            return higherKeyRecursive(node.right, key, candidate);
        }
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
    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            // Рекурсивно обходим левое поддерево
            if (node.left != null) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Рекурсивно обходим правое поддерево
            if (node.right != null) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    // Методы интерфейса NavigableMap, которые не реализуются в данной версии

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("lowerEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("floorEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("ceilingEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("higherEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("firstEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("lastEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("pollFirstEntry not implemented");
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("pollLastEntry not implemented");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("descendingMap not implemented");
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("navigableKeySet not implemented");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("subMap with booleans not implemented");
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException("headMap with boolean not implemented");
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException("tailMap with boolean not implemented");
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("descendingKeySet not implemented");
    }

    // Методы интерфейса Map, которые не реализуются в данной версии

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