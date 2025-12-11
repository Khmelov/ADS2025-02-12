package by.it.group410902.derzhavskaya_ludmila.lesson12;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

// Класс реализует интерфейс NavigableMap<Integer, String> на основе splay-дерева
public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла splay-дерева
    private class SplayNode {
        Integer key;
        String value;
        SplayNode left;
        SplayNode right;
        SplayNode parent;

        // Конструктор узла
        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private SplayNode root;    // Корень дерева
    private int size;          // Количество элементов в дереве

    // Конструктор
    public MySplayMap() {
        root = null;
        size = 0;
    }

    // Операция splay - перемещает узел в корень дерева через серию поворотов
    private void splay(SplayNode x) {
        // Продолжаем пока узел не станет корнем
        while (x.parent != null) {
            SplayNode parent = x.parent;
            SplayNode grandparent = parent.parent;

            if (grandparent == null) {
                // Zig-операция: узел находится непосредственно под корнем
                if (x == parent.left) {
                    rotateRight(parent);  // Правый поворот если x - левый потомок
                } else {
                    rotateLeft(parent);   // Левый поворот если x - правый потомок
                }
            } else {
                if (x == parent.left && parent == grandparent.left) {
                    // Zig-zig (левый-левый): и x и parent являются левыми потомками
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (x == parent.right && parent == grandparent.right) {
                    // Zig-zig (правый-правый): и x и parent являются правыми потомками
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (x == parent.right && parent == grandparent.left) {
                    // Zig-zag (левый-правый): x - правый потомок, parent - левый потомок
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    // Zig-zag (правый-левый): x - левый потомок, parent - правый потомок
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        // После всех поворотов x становится корнем дерева
        root = x;
    }

    // Левый поворот вокруг узла x
    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;  // y становится правым потомком x
        if (y != null) {
            x.right = y.left;   // Левое поддерево y становится правым поддеревом x
            if (y.left != null) {
                y.left.parent = x;  // Обновляем родителя для левого поддерева y
            }
            y.parent = x.parent;
        }

        // Обновляем ссылку родителя на новый узел y
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

    // Правый поворот вокруг узла y
    private void rotateRight(SplayNode y) {
        SplayNode x = y.left;   // x становится левым потомком y
        if (x != null) {
            y.left = x.right;   // Правое поддерево x становится левым поддеревом y
            if (x.right != null) {
                x.right.parent = y;  // Обновляем родителя для правого поддерева x
            }
            x.parent = y.parent;
        }

        // Обновляем ссылку родителя на новый узел x
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        if (x != null) {
            x.right = y;
        }
        y.parent = x;
    }

    // Вставка узла в дерево
    @Override
    public String put(Integer key, String value) {

        SplayNode current = root;
        SplayNode parent = null;

        // Поиск места для вставки
        while (current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;    // Идем влево если ключ меньше
            } else if (key > current.key) {
                current = current.right;   // Идем вправо если ключ больше
            } else {
                // Ключ уже существует - обновляем значение и делаем splay
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }

        // Создаем новый узел для вставки
        SplayNode newNode = new SplayNode(key, value);
        newNode.parent = parent;

        // Вставляем узел в правильную позицию
        if (parent == null) {
            root = newNode;
        } else if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        splay(newNode);  // Перемещаем новый узел в корень
        return null;
    }

    // Поиск узла по ключу (без splay) - внутренний вспомогательный метод
    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        while (current != null) {
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    // Поиск узла по ключу с splay - если узел найден, он перемещается в корень
    private SplayNode findWithSplay(Integer key) {
        SplayNode current = root;
        SplayNode lastVisited = null;      // Последний узел который мы посетили
        SplayNode lastComparison = null;   // Узел с которым сравнивали

        while (current != null) {
            lastVisited = current;
            if (key < current.key) {
                current = current.left;
                lastComparison = lastVisited;
            } else if (key > current.key) {
                current = current.right;
                lastComparison = lastVisited;
            } else {
                splay(current);  // Нашли узел - перемещаем в корень
                return current;
            }
        }

        // Если ключ не найден, делаем splay последнего посещенного узла
        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {

        // Используем поиск с splay - это переместит найденный узел в корень
        SplayNode node = findWithSplay((Integer) key);
        return node != null ? node.value : null;
    }

    // Проверяет наличие ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValueRecursive(SplayNode node, Object value) {
        if (node == null) {
            return false;
        }

        // Проверяем текущий узел - сравниваем значения с учетом возможного null
        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;
        }

        // Рекурсивно проверяем левое и правое поддерево
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    // Проверяет наличие значения в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    // Находит узел с минимальным ключом в поддереве
    private SplayNode findMinNode(SplayNode node) {
        while (node != null && node.left != null) {
            node = node.left;  // Идем все время влево
        }
        return node;
    }

    // Находит узел с максимальным ключом в поддереве
    private SplayNode findMaxNode(SplayNode node) {
        while (node != null && node.right != null) {
            node = node.right;  // Идем все время вправо
        }
        return node;
    }

    // Удаляет узел по ключу
    @Override
    public String remove(Object key) {

        // Находим узел для удаления (с splay - он станет корнем)
        SplayNode node = findWithSplay((Integer) key);
        if (node == null) {
            return null;  // Узел не найден
        }

        String oldValue = node.value;

        // Если у узла нет левого потомка - просто заменяем его правым поддеревом
        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        }
        // Если у узла нет правого потомка - просто заменяем его левым поддеревом
        else if (node.right == null) {
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        }
        // Если у узла есть оба потомка - находим минимальный узел в правом поддереве
        else {
            SplayNode minRight = findMinNode(node.right);

            // Если минимальный узел не является прямым правым потомком
            if (minRight.parent != node) {
                // Переносим правое поддерево минимального узла на его место
                minRight.parent.left = minRight.right;
                if (minRight.right != null) {
                    minRight.right.parent = minRight.parent;
                }
                // Минимальный узел получает правое поддерево удаляемого узла
                minRight.right = node.right;
                node.right.parent = minRight;
            }

            // Минимальный узел получает левое поддерево удаляемого узла
            minRight.left = node.left;
            node.left.parent = minRight;
            root = minRight;
            root.parent = null;
        }

        size--;
        return oldValue;
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size;
    }

    // Очищает дерево - просто обнуляем корень и счетчик
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Проверяет пустое ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает первый (наименьший) ключ
    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        SplayNode minNode = findMinNode(root);
        if (minNode != null) {
            splay(minNode);  // Перемещаем минимальный узел в корень
        }
        return minNode.key;
    }

    // Возвращает последний (наибольший) ключ
    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        SplayNode maxNode = findMaxNode(root);
        if (maxNode != null) {
            splay(maxNode);  // Перемещаем максимальный узел в корень
        }
        return maxNode.key;
    }

    // Находит наибольший ключ, который меньше заданного
    @Override
    public Integer lowerKey(Integer key) {
        SplayNode current = root;
        SplayNode candidate = null;  // Кандидат - наибольший ключ меньше заданного

        while (current != null) {
            if (key <= current.key) {
                // Текущий ключ слишком большой - идем влево к меньшим ключам
                current = current.left;
            } else {
                // Нашли ключ меньше заданного - запоминаем как кандидата
                candidate = current;
                current = current.right;
            }
        }

        if (candidate != null) {
            splay(candidate);  // Перемещаем найденный узел в корень
            return candidate.key;
        }
        return null;  // Не найден ключ меньше заданного
    }

    // Находит наибольший ключ, который меньше или равен заданному
    @Override
    public Integer floorKey(Integer key) {
        SplayNode current = root;
        SplayNode candidate = null;

        while (current != null) {
            if (key < current.key) {
                // Текущий ключ слишком большой - идем влево
                current = current.left;
            } else if (key > current.key) {
                // Нашли ключ меньше заданного - запоминаем и идем вправо
                candidate = current;
                current = current.right;
            } else {
                // Нашли точное совпадение - перемещаем в корень и возвращаем
                splay(current);
                return current.key;
            }
        }

        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    // Находит наименьший ключ, который больше или равен заданному
    @Override
    public Integer ceilingKey(Integer key) {
        SplayNode current = root;
        SplayNode candidate = null;

        while (current != null) {
            if (key > current.key) {
                // Текущий ключ слишком маленький - идем вправо
                current = current.right;
            } else if (key < current.key) {
                // Нашли ключ больше заданного - запоминаем и идем влево
                candidate = current;
                current = current.left;
            } else {
                // Нашли точное совпадение
                splay(current);
                return current.key;
            }
        }

        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    // Находит наименьший ключ, который больше заданного
    @Override
    public Integer higherKey(Integer key) {
        SplayNode current = root;
        SplayNode candidate = null;

        while (current != null) {
            if (key >= current.key) {
                // Текущий ключ слишком маленький или равен - идем вправо
                current = current.right;
            } else {
                // Нашли ключ больше заданного - запоминаем и идем влево
                candidate = current;
                current = current.left;
            }
        }

        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    // Рекурсивное построение строки в порядке возрастания ключей
    // Левое поддерево -> Текущий узел -> Правое поддерево
    private void inOrderToString(SplayNode node, StringBuilder sb) {
        if (node != null) {
            // Обходим левое поддерево
            if (node.left != null) {
                inOrderToString(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел в формате "ключ=значение"
            sb.append(node.key).append("=").append(node.value);

            // Обходим правое поддерево
            if (node.right != null) {
                sb.append(", ");
                inOrderToString(node.right, sb);
            }
        }
    }

    // Возвращает строковое представление дерева в формате
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Вспомогательный метод для рекурсивного копирования поддерева в карту
    private void copySubTree(SplayNode node, MySplayMap map) {
        if (node != null) {
            copySubTree(node.left, map);
            map.put(node.key, node.value);  // Вставка автоматически делает splay
            copySubTree(node.right, map);
        }
    }

    // Возвращает представление для ключей меньше заданного (исключая toKey)
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    // Возвращает представление для ключей больше или равных заданному
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    // Рекурсивное построение headMap - копирует все ключи удовлетворяющие условию
    private void buildHeadMap(SplayNode node, Integer toKey, boolean inclusive, MySplayMap map) {
        if (node != null) {
            buildHeadMap(node.left, toKey, inclusive, map);

            // Проверяем условие включения: <= toKey если inclusive, < toKey если нет
            if (inclusive ? node.key <= toKey : node.key < toKey) {
                map.put(node.key, node.value);
                buildHeadMap(node.right, toKey, inclusive, map);
            }
        }
    }

    // Рекурсивное построение tailMap - копирует все ключи удовлетворяющие условию
    private void buildTailMap(SplayNode node, Integer fromKey, boolean inclusive, MySplayMap map) {
        if (node != null) {
            buildTailMap(node.right, fromKey, inclusive, map);

            // Проверяем условие включения: >= fromKey если inclusive, > fromKey если нет
            if (inclusive ? node.key >= fromKey : node.key > fromKey) {
                map.put(node.key, node.value);
                buildTailMap(node.left, fromKey, inclusive, map);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса NavigableMap не реализованы ///////
    /////////////////////////////////////////////////////////////////////////

    // Все методы ниже не реализованы, так как не требуются по заданию
    // Они бросают исключение UnsupportedOperationException

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        buildHeadMap(root, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        buildTailMap(root, fromKey, inclusive, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Comparator<? super Integer> comparator() {
        // Для естественного порядка (сортировка по возрастанию) возвращаем null
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented");
    }
}