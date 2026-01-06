package by.it.group410902.andala.lesson12;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Узел сплей-дерева
    private static class Node {
        Integer key;        // Ключ узла
        String info;        // Значение узла
        Node left;          // Левый потомок
        Node right;         // Правый потомок
        Node parent;        // Родительский узел

        // Конструктор узла
        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
        }
    }

    private Node root;          // Корень дерева
    private int currentSize = 0; // Количество элементов в дереве

    // Возвращает компаратор (null - естественный порядок)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    // Возвращает множество всех ключей
    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderTraversal(root, keys); // Обход дерева для сбора ключей
        return keys;
    }

    // Возвращает коллекцию всех значений
    @Override
    public Collection<String> values() {
        Collection<String> arr = new ArrayList<>();
        inOrderTraversalValues(root, arr); // Обход дерева для сбора значений
        return arr;
    }

    // Добавляет все элементы из переданной карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue()); // Добавляем каждую пару
        }
    }

    // Рекурсивный обход дерева для сбора ключей (левый-корень-правый)
    private void inOrderTraversal(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderTraversal(node.left, keys);   // Левое поддерево
            keys.add(node.key);                  // Текущий узел
            inOrderTraversal(node.right, keys);  // Правое поддерево
        }
    }

    // Рекурсивный обход дерева для сбора значений
    private void inOrderTraversalValues(Node node, Collection<String> values) {
        if (node != null) {
            inOrderTraversalValues(node.left, values);   // Левое поддерево
            values.add(node.info);                       // Текущий узел
            inOrderTraversalValues(node.right, values);  // Правое поддерево
        }
    }

    // Проверка наличия значения в дереве
    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj);
    }

    // Рекурсивный поиск значения
    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false; // Достигли конца ветки
        }
        if (obj.equals(node.info)) {
            return true; // Нашли значение
        }
        // Ищем в левом и правом поддеревьях
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    // ОСНОВНАЯ ОПЕРАЦИЯ: перемещение узла к корню
    private void splay(Node node) {
        while (node.parent != null) { // Пока узел не станет корнем
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Случай 1: ZIG - один уровень до корня
                if (node == parent.left) {
                    rotateRight(parent); // ZIG (правый поворот)
                } else {
                    rotateLeft(parent);  // ZIG (левый поворот)
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    // Случай 2: ZIG-ZIG (левый-левый)
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    // Случай 3: ZIG-ZIG (правый-правый)
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    // Случай 4: ZIG-ZAG (правый-левый)
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    // Случай 5: ZIG-ZAG (левый-правый)
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node; // Узел становится корнем
    }

    // Левый поворот
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    // Правый поворот
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    // Добавление или обновление элемента
    @Override
    public String put(Integer key, String info) {
        if (root == null) {
            // Дерево пустое - создаем корень
            root = new Node(key, info);
            currentSize++;
            return null;
        }

        Node node = root;
        Node parent = null;

        // Поиск места для вставки
        while (node != null) {
            parent = node;
            if (key.compareTo(node.key) < 0) {
                node = node.left; // Идем влево
            } else if (key.compareTo(node.key) > 0) {
                node = node.right; // Идем вправо
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = node.info;
                node.info = info;
                splay(node); // Перемещаем узел к корню
                return oldValue;
            }
        }

        // Создаем новый узел
        Node newNode = new Node(key, info);
        newNode.parent = parent;

        // Вставляем в нужное место
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode); // Перемещаем новый узел к корню
        currentSize++;
        return null;
    }

    // Удаление элемента
    @Override
    public String remove(Object obj) {
        Node node = findNode((Integer) obj);
        if (node == null) {
            return null; // Узел не найден
        }

        splay(node); // Перемещаем удаляемый узел к корню

        if (node.left == null) {
            // Нет левого поддерева - правый потомок становится корнем
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            // Есть левое поддерево
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;

            // Находим максимальный узел в левом поддереве
            Node maxLeft = findMax(root);
            splay(maxLeft); // Перемещаем его к корню

            // Присоединяем правое поддерево
            maxLeft.right = rightSubtree;
            if (rightSubtree != null) {
                rightSubtree.parent = maxLeft;
            }
        }

        currentSize--;
        return node.info;
    }

    // Поиск узла по ключу
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return current; // Найден
            }
        }
        return null; // Не найден
    }

    // Поиск максимального узла в поддереве
    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Получение значения по ключу
    @Override
    public String get(Object obj) {
        Node node = findNode((Integer) obj);
        if (node != null) {
            splay(node); // Перемещаем найденный узел к корню
            return node.info;
        }
        return null;
    }

    // Проверка наличия ключа
    @Override
    public boolean containsKey(Object obj) {
        return findNode((Integer) obj) != null;
    }

    // Количество элементов
    @Override
    public int size() {
        return currentSize;
    }

    // Очистка дерева
    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    // Проверка пустоты
    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Подкарта с ключами меньше toKey
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    // Рекурсивное построение headMap
    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        if (node.key.compareTo(toKey) < 0 || (inclusive && node.key.equals(toKey))) {
            result.put(node.key, node.info);
            headMap(node.right, toKey, inclusive, result);
        }
        headMap(node.left, toKey, inclusive, result);
    }

    // Подкарта с ключами >= fromKey
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    // Рекурсивное построение tailMap
    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) > 0 || (inclusive && node.key.equals(fromKey))) {
            result.put(node.key, node.info);
            tailMap(node.left, fromKey, inclusive, result);
        }
        tailMap(node.right, fromKey, inclusive, result);
    }

    // Минимальный ключ
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = findMin(root);
        splay(node); // Перемещаем минимальный узел к корню
        return node.key;
    }

    // Максимальный ключ
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = findMax(root);
        splay(node); // Перемещаем максимальный узел к корню
        return node.key;
    }

    // Поиск минимального узла
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Наибольший ключ, строго меньший заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node lowerNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) >= 0) {
            return lowerNode(node.left, key);
        } else {
            Node right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    // Наибольший ключ, меньший или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node floorNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) == 0) {
            return node;
        }
        if (node.key.compareTo(key) > 0) {
            return floorNode(node.left, key);
        } else {
            Node right = floorNode(node.right, key);
            return right != null ? right : node;
        }
    }

    // Наименьший ключ, больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node ceilingNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) == 0) {
            return node;
        }
        if (node.key.compareTo(key) < 0) {
            return ceilingNode(node.right, key);
        } else {
            Node left = ceilingNode(node.left, key);
            return left != null ? left : node;
        }
    }

    // Наименьший ключ, строго больший заданного
    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node higherNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) <= 0) {
            return higherNode(node.right, key);
        } else {
            Node left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }

    // Подкарта в диапазоне [fromKey, toKey)
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    // Рекурсивное построение subMap
    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) {
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, result);
        }

        if ((node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) &&
                (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey)))) {
            result.put(node.key, node.info);
        }

        if (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey))) {
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, result);
        }
    }

    // Строковое представление
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("{");
        toString(root, resultStr);
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2); // Удаляем последнюю ", "
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    private void toString(Node node, StringBuilder resultStr) {
        if (node != null) {
            toString(node.left, resultStr);
            resultStr.append(node.key).append("=").append(node.info).append(", ");
            toString(node.right, resultStr);
        }
    }

    // НЕ РЕАЛИЗОВАННЫЕ МЕТОДЫ
    @Override public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public java.util.Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public java.util.NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}