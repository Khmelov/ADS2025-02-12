package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    // Внутренний класс узла дерева
    // Красно-черное дерево - самобалансирующееся бинарное дерево поиска
    private static class Node {
        Integer key;      // Ключ - целое число
        String value;     // Значение - строка
        Node left;        // Левый потомок (меньшие ключи)
        Node right;       // Правый потомок (большие ключи)
        Node parent;      // Родительский узел (нужен для балансировки)
        boolean color;    // Цвет узла: true - красный, false - черный

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = true; // Новый узел всегда красный (для упрощения балансировки)
        }
    }

    private Node root;        // Корень дерева
    private int size = 0;     // Количество элементов в дереве
    private final Node NIL = new Node(null, null, null); // Фиктивный узел-лист (sentinel)

    public MyRbMap() {
        NIL.color = false; // Листья всегда черные
        root = NIL;        // Изначально дерево пустое
    }

    /*реализовала балансировку дерева через повороты (rotateLeft и rotateRight) и перекрашивание узлов,
     где после вставки красного узла метод fixInsert проверяет и исправляет возможные нарушения свойств красно-черного дерева.
     */
    private void rotateLeft(Node x) {
        Node y = x.right;   // y становится правым потомком x
        x.right = y.left;   // Левое поддерево y становится правым поддеревом x

        if (y.left != NIL)
            y.left.parent = x; // Обновляем родителя для левого поддерева y

        /*При удалении элементов метод deleteNode обрабатывает три случая (узел без потомков, с одним потомком, с двумя потомками)
     и вызывает fixDelete для восстановления баланса при удалении черного узла, что может нарушить черную высоту дерева.
     */
        y.parent = x.parent; // Переносим родителя от x к y

        if (x.parent == null)
            root = y;           // x был корнем
        else if (x == x.parent.left)
            x.parent.left = y;  // x был левым потомком
        else
            x.parent.right = y; // x был правым потомком

        /*Для работы с подмножествами реализованы методы headMap, tailMap и subMap, которые рекурсивно обходят дерево
     и создают новые карты с ключами в заданных диапазонах, сохраняя отсортированный порядок.
     */
        y.left = x;    // x становится левым потомком y
        x.parent = y;  // y становится родителем x
    }

    private void rotateRight(Node x) {
        Node y = x.left;    // y становится левым потомком x
        x.left = y.right;   // Правое поддерево y становится левым поддеревом x

        if (y.right != NIL)
            y.right.parent = x; // Обновляем родителя для правого поддерева y

        y.parent = x.parent; // Переносим родителя от x к y

        if (x.parent == null)
            root = y;           // x был корнем
        else if (x == x.parent.right)
            x.parent.right = y; // x был правым потомком
        else
            x.parent.left = y;  // x был левым потомком

        y.right = x;   // x становится правым потомком y
        x.parent = y;  // y становится родителем x
    }

    private void fixInsert(Node k) {
        // Пока родитель красный (нарушение свойства)
        while (k.parent != null && k.parent.color) {
            if (k.parent == k.parent.parent.left) {
                // Случай: родитель - левый потомок деда
                Node u = k.parent.parent.right; // Дядя

                if (u.color) {
                    // Случай 1: дядя красный
                    u.color = false;           // Дядя -> черный
                    k.parent.color = false;    // Родитель -> черный
                    k.parent.parent.color = true; // Дед -> красный
                    k = k.parent.parent;       // Переходим к деду
                } else {
                    if (k == k.parent.right) {
                        // Случай 2: узел - правый потомок (треугольник)
                        k = k.parent;
                        rotateLeft(k); // Превращаем в линейный случай
                    }
                    // Случай 3: узел - левый потомок (линейный случай)
                    k.parent.color = false;    // Родитель -> черный
                    k.parent.parent.color = true; // Дед -> красный
                    rotateRight(k.parent.parent); // Правый поворот
                }
            } else {
                // Симметричный случай: родитель - правый потомок деда
                Node u = k.parent.parent.left; // Дядя

                if (u.color) {
                    // Случай 1: дядя красный
                    u.color = false;           // Дядя -> черный
                    k.parent.color = false;    // Родитель -> черный
                    k.parent.parent.color = true; // Дед -> красный
                    k = k.parent.parent;       // Переходим к деду
                } else {
                    if (k == k.parent.left) {
                        // Случай 2: узел - левый потомок (треугольник)
                        k = k.parent;
                        rotateRight(k); // Превращаем в линейный случай
                    }
                    // Случай 3: узел - правый потомок (линейный случай)
                    k.parent.color = false;    // Родитель -> черный
                    k.parent.parent.color = true; // Дед -> красный
                    rotateLeft(k.parent.parent); // Левый поворот
                }
            }
            if (k == root) break; // Достигли корня
        }
        root.color = false; // Корень всегда черный
    }

    // Основные методы интерфейса

    @Override
    public String put(Integer key, String value) {
        Node y = null;  // Будет хранить родителя для нового узла
        Node x = root;  // Начинаем с корня

        // Поиск места для вставки
        while (x != NIL) {
            y = x;
            if (key.compareTo(x.key) < 0)
                x = x.left;   // Идем влево
            else if (key.compareTo(x.key) > 0)
                x = x.right;  // Идем вправо
            else {
                // Ключ уже существует - обновляем значение
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        // Создаем новый узел
        Node newNode = new Node(key, value, y);

        // Вставляем узел в дерево
        if (y == null)
            root = newNode;        // Дерево было пустым
        else if (key.compareTo(y.key) < 0)
            y.left = newNode;      // Вставляем как левого потомка
        else
            y.right = newNode;     // Вставляем как правого потомка

        // Инициализируем потомков как листья
        newNode.left = NIL;
        newNode.right = NIL;

        size++;
        fixInsert(newNode); // Восстанавливаем свойства дерева
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        // Добавляем все элементы из переданной карты
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String remove(Object key) {
        Node z = searchNode((Integer) key); // Находим узел для удаления
        if (z == NIL) return null; // Узел не найден

        String oldValue = z.value;
        deleteNode(z); // Удаляем узел
        size--;
        return oldValue;
    }

    // Удаление узла из дерева
    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;

        if (z.left == NIL) {
            // Случай 1: нет левого потомка
            x = z.right;
            transplant(z, z.right); // Заменяем z на его правого потомка
        } else if (z.right == NIL) {
            // Случай 2: нет правого потомка
            x = z.left;
            transplant(z, z.left); // Заменяем z на его левого потомка
        } else {
            // Случай 3: есть оба потомка
            y = minimum(z.right); // Находим преемника (минимальный в правом поддереве)
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right); // Заменяем y на его правого потомка
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y); // Заменяем z на y
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; // Сохраняем цвет z
        }

        if (!yOriginalColor) {
            fixDelete(x); // Восстанавливаем свойства если удалили черный узел
        }
    }

    // Замена поддерева u на поддерево v
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v; // u был корнем
        } else if (u == u.parent.left) {
            u.parent.left = v; // u был левым потомком
        } else {
            u.parent.right = v; // u был правым потомком
        }
        v.parent = u.parent; // Обновляем родителя для v
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node minimum(Node x) {
        while (x.left != NIL) {
            x = x.left; // Идем до самого левого узла
        }
        return x;
    }

    // Восстановление свойств после удаления
    private void fixDelete(Node x) {
        while (x != root && !x.color) {
            if (x == x.parent.left) {
                Node w = x.parent.right; // Брат

                if (w.color) {
                    // Случай 1: брат красный
                    w.color = false;
                    x.parent.color = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (!w.left.color && !w.right.color) {
                    // Случай 2: оба потомка брата черные
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.right.color) {
                        // Случай 3: правый потомок брата черный
                        w.left.color = false;
                        w.color = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    // Случай 4: правый потомок брата красный
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                // Симметричный случай
                Node w = x.parent.left; // Брат

                if (w.color) {
                    w.color = false;
                    x.parent.color = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (!w.right.color && !w.left.color) {
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.left.color) {
                        w.right.color = false;
                        w.color = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = false; // x становится черным
    }

    // Поиск узла по ключу
    private Node searchNode(Integer key) {
        Node x = root;
        while (x != NIL) {
            if (key.compareTo(x.key) < 0)
                x = x.left;   // Ищем в левом поддереве
            else if (key.compareTo(x.key) > 0)
                x = x.right;  // Ищем в правом поддереве
            else
                return x;     // Узел найден
        }
        return NIL; // Узел не найден
    }

    @Override
    public String get(Object key) {
        Node node = searchNode((Integer) key);
        return node != NIL ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return searchNode((Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValueHelper(Node node, Object value) {
        if (node == NIL) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValueHelper(node.left, value) ||
                containsValueHelper(node.right, value);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    // Методы для работы с подмножествами

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        headMapHelper(root, toKey, subMap);
        return subMap;
    }

    // Рекурсивное построение headMap (ключи < toKey)
    private void headMapHelper(Node node, Integer toKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMapHelper(node.right, toKey, subMap);
        }
        headMapHelper(node.left, toKey, subMap);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        tailMapHelper(root, fromKey, subMap);
        return subMap;
    }

    // Рекурсивное построение tailMap (ключи >= fromKey)
    private void tailMapHelper(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, subMap);
        }
        tailMapHelper(node.right, fromKey, subMap);
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node x = root;
        while (x.left != NIL) x = x.left; // Самый левый узел
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        Node x = root;
        while (x.right != NIL) x = x.right; // Самый правый узел
        return x.key;
    }

    // Остальные методы интерфейса

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapHelper(root, fromKey, toKey, subMap);
        return subMap;
    }

    // Рекурсивное построение subMap (fromKey <= ключи < toKey)
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMapHelper(node.left, fromKey, toKey, subMap);
        }
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0) {
            subMapHelper(node.right, fromKey, toKey, subMap);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        keySetHelper(root, keys);
        return keys;
    }

    // Рекурсивный обход для получения всех ключей
    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node == NIL) return;
        keySetHelper(node.left, keys);
        keys.add(node.key);
        keySetHelper(node.right, keys);
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        valuesHelper(root, values);
        return values;
    }

    // Рекурсивный обход для получения всех значений
    private void valuesHelper(Node node, List<String> values) {
        if (node == NIL) return;
        valuesHelper(node.left, values);
        values.add(node.value);
        valuesHelper(node.right, values);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Entry::getKey));
        entrySetHelper(root, entries);
        return entries;
    }

    // Рекурсивный обход для получения всех пар ключ-значение
    private void entrySetHelper(Node node, Set<Entry<Integer, String>> entries) {
        if (node == NIL) return;
        entrySetHelper(node.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        entrySetHelper(node.right, entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // Удаляем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (in-order) для строкового представления
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == NIL) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderTraversal(node.right, sb);
    }
}
/*
* Основные принципы красно-черного дерева:
1. Свойства красно-черного дерева
Каждый узел либо красный, либо черный

Корень всегда черный

Все листья (NIL) черные

У красного узла оба потомка черные (нет двух красных узлов подряд)

Все пути от узла до листьев содержат одинаковое количество черных узлов

2. Балансировка
Высота дерева: O(log n) гарантированно

Самобалансировка: при вставке и удалении выполняются повороты и перекрашивания

Меньше поворотов чем в АВЛ-деревьях, но менее строгая балансировка

3. Случаи при вставке
Дядя красный - перекрашивание

Дядя черный, треугольник - поворот для выравнивания

Дядя черный, линейный случай - поворот и перекрашивание

4. Случаи при удалении
Брат красный - поворот и переход к случаю 2, 3 или 4

Брат черный с черными потомками - перекрашивание

Брат черный с красным левым потомком - поворот для выравнивания

Брат черный с красным правым потомком - поворот и перекрашивание

5. Преимущества
Гарантированная производительность: O(log n) для всех операций

Эффективность на практике: меньше поворотов чем у АВЛ-деревьев

Используется в Java: TreeMap и TreeSet используют красно-черные деревья
* */