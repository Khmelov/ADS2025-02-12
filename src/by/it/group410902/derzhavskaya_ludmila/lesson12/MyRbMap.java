package by.it.group410902.derzhavskaya_ludmila.lesson12;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

// Класс реализует интерфейс SortedMap<Integer, String> на основе красно-черного дерева
public class MyRbMap implements SortedMap<Integer, String> {

    // Константы для цветов узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Внутренний класс для узла красно-черного дерева
    private class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        RbNode parent;
        boolean color;

        // Конструктор узла
        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private RbNode root;       // Корень дерева
    private int size;          // Количество элементов в дереве
    private final RbNode NIL;  // Фиктивный листовой узел (всегда черный)

    // Конструктор
    public MyRbMap() {
        NIL = new RbNode(null, null, BLACK);
        root = NIL;
        size = 0;
    }

    // Проверяет является ли узел красным
    private boolean isRed(RbNode node) {
        return node != NIL && node.color == RED;
    }

    // Левый поворот вокруг узла x
    private void leftRotate(RbNode x) {
        RbNode y = x.right;    // y становится правым потомком x
        x.right = y.left;      // Левое поддерево y становится правым поддеревом x

        if (y.left != NIL) {
            y.left.parent = x; // Обновляем родителя для левого поддерева y
        }

        y.parent = x.parent;   // y наследует родителя от x

        // Обновляем ссылку родителя на новый узел y
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

    // Правый поворот вокруг узла y
    private void rightRotate(RbNode y) {
        RbNode x = y.left;     // x становится левым потомком y
        y.left = x.right;      // Правое поддерево x становится левым поддеревом y

        if (x.right != NIL) {
            x.right.parent = y;// Обновляем родителя для правого поддерева x
        }

        x.parent = y.parent;   // x наследует родителя от y

        // Обновляем ссылку родителя на новый узел x
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

    // Балансировка после вставки нового узла
    private void fixAfterInsert(RbNode z) {
        // Продолжаем пока родитель z красный (двойное красное нарушение)
        while (isRed(z.parent)) {
            if (z.parent == z.parent.parent.left) {
                // Родитель z - левый потомок деда
                RbNode y = z.parent.parent.right; // Дядя z

                if (isRed(y)) {
                    // Случай 1: дядя красный - перекрашиваем
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent; // Поднимаемся на уровень выше
                } else {
                    if (z == z.parent.right) {
                        // Случай 2: дядя черный, z - правый потомок
                        z = z.parent;
                        leftRotate(z); // Превращаем в случай 3
                    }
                    // Случай 3: дядя черный, z - левый потомок
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                // Симметричный случай: родитель z - правый потомок деда
                RbNode y = z.parent.parent.left; // Дядя z

                if (isRed(y)) {
                    // Случай 1: дядя красный
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        // Случай 2: дядя черный, z - левый потомок
                        z = z.parent;
                        rightRotate(z);
                    }
                    // Случай 3: дядя черный, z - правый потомок
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK; // Корень всегда черный
    }

    // Вставка узла в дерево
    @Override
    public String put(Integer key, String value) {

        RbNode y = NIL;  // Будет хранить родителя для нового узла
        RbNode x = root; // Начинаем поиск с корня

        while (x != NIL) {
            y = x;
            if (key < x.key) {
                x = x.left;
            } else if (key > x.key) {
                x = x.right;
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        // Создаем новый узел (красный по умолчанию - может нарушить свойства)
        RbNode z = new RbNode(key, value, RED);
        z.parent = y;
        z.left = NIL;
        z.right = NIL;

        // Вставляем узел в правильную позицию
        if (y == NIL) {
            root = z;          // Дерево было пустым
        } else if (key < y.key) {
            y.left = z;        // Вставляем как левого потомка
        } else {
            y.right = z;       // Вставляем как правого потомка
        }

        size++;
        fixAfterInsert(z); // Восстанавливаем красно-черные свойства
        return null;
    }

    // Поиск узла по ключу без изменения структуры дерева
    private RbNode findNode(Integer key) {
        RbNode current = root;
        while (current != NIL) {
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current; // Узел найден
            }
        }
        return NIL; // Узел не найден
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {

        RbNode node = findNode((Integer) key);
        return node != NIL ? node.value : null;
    }

    // Проверяет наличие ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // Рекурсивный содержится ли значение в дереве
    private boolean containsValueRecursive(RbNode node, Object value) {
        if (node == NIL) {
            return false;
        }

        // Проверяем текущий узел
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
    private RbNode findMinNode(RbNode node) {
        while (node.left != NIL) {
            node = node.left; // Идем все время влево
        }
        return node;
    }

    // Находит узел с максимальным ключом в поддереве
    private RbNode findMaxNode(RbNode node) {
        while (node.right != NIL) {
            node = node.right; // Идем все время вправо
        }
        return node;
    }

    // Заменяет поддерево с корнем u на поддерево с корнем v
    // Используется при удалении узлов
    private void transplant(RbNode u, RbNode v) {
        if (u.parent == NIL) {
            root = v;          // u был корнем
        } else if (u == u.parent.left) {
            u.parent.left = v; // u был левым потомком
        } else {
            u.parent.right = v;// u был правым потомком
        }
        v.parent = u.parent;   // Обновляем родителя для v
    }

    // Балансировка после удаления узла
    private void fixAfterDelete(RbNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                // x - левый потомок
                RbNode w = x.parent.right; // Брат x

                if (isRed(w)) {
                    // Случай 1: брат красный
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == BLACK && w.right.color == BLACK) {
                    // Случай 2: оба ребенка брата черные
                    w.color = RED;
                    x = x.parent; // Поднимаем проблему на уровень выше
                } else {
                    if (w.right.color == BLACK) {
                        // Случай 3: правый ребенок брата черный
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Случай 4: правый ребенок брата красный
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root; // Завершаем цикл
                }
            } else {
                // Симметричный случай: x - правый потомок
                RbNode w = x.parent.left; // Брат x

                if (isRed(w)) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; // Делаем x черным
    }

    // Удаляет узел по ключу
    @Override
    public String remove(Object key) {

        RbNode z = findNode((Integer) key);
        if (z == NIL) {
            return null; // Узел не найден
        }

        String oldValue = z.value;
        RbNode y = z;          // Узел который удаляется
        RbNode x;              // Узел который займет место y
        boolean yOriginalColor = y.color;

        if (z.left == NIL) {
            // Нет левого потомка - заменяем на правое поддерево
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            // Нет правого потомка - заменяем на левое поддерево
            x = z.left;
            transplant(z, z.left);
        } else {
            // Есть оба потомка - находим преемника (минимальный в правом поддереве)
            y = findMinNode(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                // Преемник является прямым потомком z
                x.parent = y;
            } else {
                // Преемник не является прямым потомком
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; // Сохраняем цвет z
        }

        // Если удалили черный узел, нужно восстановить баланс
        if (yOriginalColor == BLACK) {
            fixAfterDelete(x);
        }

        size--;
        return oldValue;
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size;
    }

    // Очищает дерево - сбрасываем корень и счетчик
    @Override
    public void clear() {
        root = NIL;
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
        return findMinNode(root).key;
    }

    // Возвращает последний (наибольший) ключ
    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return findMaxNode(root).key;
    }

    // Рекурсивное построение строки в порядке возрастания ключей (in-order traversal)
    private void inOrderToString(RbNode node, StringBuilder sb) {
        if (node != NIL) {
            // Обходим левое поддерево (все ключи меньше текущего)
            if (node.left != NIL) {
                inOrderToString(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Обходим правое поддерево (все ключи больше текущего)
            if (node.right != NIL) {
                sb.append(", ");
                inOrderToString(node.right, sb);
            }
        }
    }

    // Возвращает строковое представление дерева в формате {1=one, 2=two, 3=three}
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
    private void copySubTree(RbNode node, MyRbMap map) {
        if (node != NIL) {
            copySubTree(node.left, map);
            map.put(node.key, node.value);
            copySubTree(node.right, map);
        }
    }

    // Возвращает представление для ключей меньше заданного (исключая toKey)
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        buildHeadMap(root, toKey, result);
        return result;
    }

    // Рекурсивное построение headMap - копирует все ключи < toKey
    private void buildHeadMap(RbNode node, Integer toKey, MyRbMap map) {
        if (node != NIL) {
            if (node.key < toKey) {
                // Ключ удовлетворяет условию - копируем все левое поддерево,
                // текущий узел и проверяем правое поддерево
                buildHeadMap(node.left, toKey, map);
                map.put(node.key, node.value);
                buildHeadMap(node.right, toKey, map);
            } else {
                // Ключ слишком большой - проверяем только левое поддерево
                buildHeadMap(node.left, toKey, map);
            }
        }
    }

    // Возвращает представление для ключей больше или равных заданному
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        buildTailMap(root, fromKey, result);
        return result;
    }

    // Рекурсивное построение tailMap - копирует все ключи >= fromKey
    private void buildTailMap(RbNode node, Integer fromKey, MyRbMap map) {
        if (node != NIL) {
            if (node.key >= fromKey) {
                // Ключ удовлетворяет условию - копируем левое поддерево,
                // текущий узел и все правое поддерево
                buildTailMap(node.left, fromKey, map);
                map.put(node.key, node.value);
                buildTailMap(node.right, fromKey, map);
            } else {
                // Ключ слишком маленький - проверяем только правое поддерево
                buildTailMap(node.right, fromKey, map);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса SortedMap не реализованы  ///////
    /////////////////////////////////////////////////////////////////////////

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