package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла дерева
    // Splay-дерево - самокорректирующееся бинарное дерево поиска
    // где часто используемые элементы автоматически перемещаются ближе к корню для ускорения последующих обращений.
    private class Node {
        Integer key;      // Ключ - целое число
        String value;     // Значение - строка
        Node left;        // Левый потомок (меньшие ключи)
        Node right;       // Правый потомок (большие ключи)
        Node parent;      // Родительский узел (нужен для splay операций)

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;    // Корень дерева
    private int size = 0; // Количество элементов в дереве

    /*Я реализовала механизм самокорректировки через операцию splay(), которая при каждом доступе к узлу
     \перемещает его в корень дерева с помощью серии поворотов (zig, zig-zig, zig-zag),
     что обеспечивает амортизированную логарифмическую сложность операций.
     */
    private void rotateLeft(Node x) {
        Node y = x.right;   // y становится правым потомком x
        if (y != null) {
            x.right = y.left; // Левое поддерево y становится правым поддеревом x
            if (y.left != null) {
                y.left.parent = x; // Обновляем родителя для левого поддерева y
            }
            y.parent = x.parent; // Переносим родителя от x к y
        }

/*При вставке новых элементов метод put() выполняет поиск позиции и сразу перемещает новый узел в корень,
    а при удалении метод remove() сначала перемещает удаляемый узел в корень, а затем корректно объединяет его поддеревья.
     */
        if (x.parent == null) {
            root = y;           // x был корнем
        } else if (x == x.parent.left) {
            x.parent.left = y;  // x был левым потомком
        } else {
            x.parent.right = y; // x был правым потомком
        }

        /*Для навигации по ключам реализованы методы lowerKey(), floorKey(), ceilingKey() и higherKey(),
    которые находят соседние ключи относительно заданного и также выполняют splay-операцию для оптимизации последующих запросов.
     */
        if (y != null) {
            y.left = x;    // x становится левым потомком y
        }
        x.parent = y;      // y становится родителем x
    }

    // Правый поворот вокруг узла x (симметрично левому)
    private void rotateRight(Node x) {
        Node y = x.left;    // y становится левым потомком x
        if (y != null) {
            x.left = y.right; // Правое поддерево y становится левым поддеревом x
            if (y.right != null) {
                y.right.parent = x; // Обновляем родителя для правого поддерева y
            }
            y.parent = x.parent; // Переносим родителя от x к y
        }

        // Обновляем ссылку родителя на новый узел
        if (x.parent == null) {
            root = y;           // x был корнем
        } else if (x == x.parent.right) {
            x.parent.right = y; // x был правым потомком
        } else {
            x.parent.left = y;  // x был левым потомком
        }

        if (y != null) {
            y.right = x;   // x становится правым потомком y
        }
        x.parent = y;      // y становится родителем x
    }

    // Splay операция - перемещает узел в корень дерева
    // Основная операция splay-дерева, обеспечивающая самокорректировку
    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                // Случай 1: zig - родитель является корнем
                if (x.parent.left == x) {
                    rotateRight(x.parent); // Правый поворот если x - левый потомок
                } else {
                    rotateLeft(x.parent);  // Левый поворот если x - правый потомок
                }
            } else {
                Node parent = x.parent;
                Node grandParent = parent.parent;

                if (grandParent.left == parent && parent.left == x) {
                    // Случай 2: zig-zig - x, родитель и дед образуют левую цепочку
                    rotateRight(grandParent);
                    rotateRight(parent);
                } else if (grandParent.right == parent && parent.right == x) {
                    // Случай 3: zig-zig - x, родитель и дед образуют правую цепочку
                    rotateLeft(grandParent);
                    rotateLeft(parent);
                } else if (grandParent.left == parent && parent.right == x) {
                    // Случай 4: zig-zag - x правый потомок, родитель левый потомок
                    rotateLeft(parent);
                    rotateRight(grandParent);
                } else {
                    // Случай 5: zig-zag - x левый потомок, родитель правый потомок
                    rotateRight(parent);
                    rotateLeft(grandParent);
                }
            }
        }
        root = x; // x становится корнем
    }

    // Находит узел с заданным ключом (без splay)
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current; // Узел найден
            } else if (cmp < 0) {
                current = current.left; // Идем влево
            } else {
                current = current.right; // Идем вправо
            }
        }
        return null; // Узел не найден
    }

    // Находит узел или последний посещенный узел (для splay)
    // Используется когда узел не найден - делаем splay для последнего посещенного узла
    private Node findNodeForSplay(Integer key) {
        Node current = root;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current; // Узел найден
            } else if (cmp < 0) {
                current = current.left; // Идем влево
            } else {
                current = current.right; // Идем вправо
            }
        }
        return lastVisited; // Возвращаем последний посещенный узел
    }

    // Обход дерева in-order для сбора элементов
    private void inOrderTraversal(Node node, List<Map.Entry<Integer, String>> list) {
        if (node != null) {
            inOrderTraversal(node.left, list);  // Левое поддерево
            list.add(new AbstractMap.SimpleEntry<>(node.key, node.value)); // Текущий узел
            inOrderTraversal(node.right, list); // Правое поддерево
        }
    }

    // Основные методы интерфейса Map

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            // Дерево пустое - создаем корень
            root = new Node(key, value);
            size++;
            return null;
        }

        Node node = findNodeForSplay(key);
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            // Ключ уже существует - обновляем значение
            String oldValue = node.value;
            node.value = value;
            splay(node); // Перемещаем узел в корень
            return oldValue;
        } else {
            // Создаем новый узел
            Node newNode = new Node(key, value);
            newNode.parent = node;

            if (cmp < 0) {
                node.left = newNode; // Вставляем как левого потомка
            } else {
                node.right = newNode; // Вставляем как правого потомка
            }

            splay(newNode); // Перемещаем новый узел в корень
            size++;
            return null;
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node == null) {
            // Узел не найден - делаем splay для последнего посещенного узла
            Node lastVisited = findNodeForSplay(k);
            if (lastVisited != null) {
                splay(lastVisited);
            }
            return null;
        }

        splay(node); // Перемещаем удаляемый узел в корень
        String removedValue = node.value;

        // Удаление узла из корня
        if (node.left == null) {
            // Нет левого поддерева - правый потомок становится корнем
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            // Нет правого поддерева - левый потомок становится корнем
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            // Есть оба поддерева
            Node minRight = node.right;
            while (minRight.left != null) {
                minRight = minRight.left; // Находим минимальный в правом поддереве
            }
            splay(minRight); // Перемещаем преемника в корень
            minRight.left = node.left; // Присоединяем левое поддерево удаляемого узла
            node.left.parent = minRight;
        }
        size--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node != null) {
            splay(node); // Найденный узел перемещаем в корень
            return node.value;
        }
        // Узел не найден - делаем splay для последнего посещенного узла
        Node lastVisited = findNodeForSplay(k);
        if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Integer k = (Integer) key;

        Node node = findNode(k);
        if (node != null) {
            splay(node); // Найденный узел перемещаем в корень
            return true;
        }
        // Узел не найден - делаем splay для последнего посещенного узла
        Node lastVisited = findNodeForSplay(k);
        if (lastVisited != null) {
            splay(lastVisited);
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Методы для представлений

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        headMapRecursive(root, toKey, subMap);
        return subMap;
    }

    // Рекурсивное построение headMap (ключи < toKey)
    private void headMapRecursive(Node node, Integer toKey, MySplayMap subMap) {
        if (node != null) {
            headMapRecursive(node.left, toKey, subMap);
            if (node.key < toKey) {
                subMap.put(node.key, node.value);
            }
            headMapRecursive(node.right, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap();
        tailMapRecursive(root, fromKey, subMap);
        return subMap;
    }

    // Рекурсивное построение tailMap (ключи >= fromKey)
    private void tailMapRecursive(Node node, Integer fromKey, MySplayMap subMap) {
        if (node != null) {
            tailMapRecursive(node.left, fromKey, subMap);
            if (node.key >= fromKey) {
                subMap.put(node.key, node.value);
            }
            tailMapRecursive(node.right, fromKey, subMap);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left; // Самый левый узел
        }
        splay(current); // Перемещаем минимальный узел в корень
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right; // Самый правый узел
        }
        splay(current); // Перемещаем максимальный узел в корень
        return current.key;
    }

    // Методы навигации

    @Override
    public Integer lowerKey(Integer key) {
        // Наибольший ключ, меньший заданного
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key < key) {
            return node.key;
        } else {
            if (node.left == null) return null;
            Node current = node.left;
            while (current.right != null) {
                current = current.right; // Максимальный в левом поддереве
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        // Наибольший ключ, меньший или равный заданному
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key <= key) {
            return node.key;
        } else {
            if (node.left == null) return null;
            Node current = node.left;
            while (current.right != null) {
                current = current.right; // Максимальный в левом поддереве
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        // Наименьший ключ, больший или равный заданному
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key >= key) {
            return node.key;
        } else {
            if (node.right == null) return null;
            Node current = node.right;
            while (current.left != null) {
                current = current.left; // Минимальный в правом поддереве
            }
            splay(current);
            return current.key;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        // Наименьший ключ, больший заданного
        Node node = findNodeForSplay(key);
        splay(node);
        if (node.key > key) {
            return node.key;
        } else {
            if (node.right == null) return null;
            Node current = node.right;
            while (current.left != null) {
                current = current.left; // Минимальный в правом поддереве
            }
            splay(current);
            return current.key;
        }
    }

    // Метод toString для вывода элементов в порядке возрастания ключей
    @Override
    public String toString() {
        List<Map.Entry<Integer, String>> entries = new ArrayList<>();
        inOrderTraversal(root, entries);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Integer, String> entry = entries.get(i);
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (i < entries.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Остальные методы интерфейса (заглушки)

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}
/*
* Основные принципы Splay-дерева:
1. Самокорректирующаяся структура
Принцип локальности: часто используемые элементы перемещаются ближе к корню

Splay операция: при любом доступе к узлу он перемещается в корень

Амортизированная сложность: O(log n) для операций в среднем

2. Типы splay-операций
Zig (одиночный поворот):

Когда родитель является корнем

Один поворот для перемещения узла в корень

Zig-Zig (двойной поворот):

Когда узел, родитель и дед образуют прямую цепочку

Два поворота в одном направлении

Zig-Zag (зигзагообразный поворот):

Когда узел, родитель и дед образуют зигзаг

Повороты в разных направлениях

3. Преимущества splay-деревьев
Локальность доступа: часто используемые элементы быстро доступны

Простота реализации: нет хранения дополнительной информации (цвета, высоты)

Амортизированная эффективность: O(log n) для последовательности операций

Адаптивность: автоматически подстраивается под шаблон доступа

4. Особенности реализации
Splay при поиске: даже если элемент не найден, делаем splay для последнего узла

Splay при вставке: новый узел сразу перемещается в корень

Splay при удалении: родитель удаляемого узла перемещается в корень

5. Сравнение с другими деревьями
Против АВЛ: проще реализация, но нет гарантии строгой балансировки

Против красно-черных: лучше производительность для часто используемых элементов

Идеально для: кэширования, когда есть локальность обращений
* */