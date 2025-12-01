package by.it.group410901.kalach.lesson12;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла Splay-дерева
    private static class Node {
        Integer key;        // Ключ узла
        String info;        // Значение узла
        Node left, right;   // Левый и правый потомки
        Node parent;        // Родительский узел (нужен для операции splay)

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
        }
    }

    private Node root;          // Корень дерева
    private int currentSize = 0; // Текущий размер карты

    // Возвращает компаратор для ключей (null означает естественный порядок)
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используем естественный порядок для Integer
    }

    // Возвращает множество ключей в отсортированном порядке
    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderTraversal(root, keys);
        return keys;
    }

    // Возвращает коллекцию значений в порядке возрастания ключей
    @Override
    public Collection<String> values() {
        Collection<String> arr = new ArrayList<>();
        inOrderTraversalValues(root, arr);
        return arr;
    }

    // Добавляет все элементы из переданной карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // Вспомогательный метод для симметричного обхода дерева (сбор ключей)
    private void inOrderTraversal(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderTraversal(node.left, keys);
            keys.add(node.key);
            inOrderTraversal(node.right, keys);
        }
    }

    // Вспомогательный метод для симметричного обхода дерева (сбор значений)
    private void inOrderTraversalValues(Node node, Collection<String> values) {
        if (node != null) {
            inOrderTraversalValues(node.left, values);
            values.add(node.info);
            inOrderTraversalValues(node.right, values);
        }
    }

    // Проверяет наличие значения в карте
    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj);
    }

    // Рекурсивный вспомогательный метод для поиска значения
    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false;
        }
        if (obj.equals(node.info)) { // Сравнение как Object
            return true;
        }
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    // Основная операция Splay-дерева - перемещение узла в корень
    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Случай 1: Zig - узел является прямым потомком корня
                if (node == parent.left) {
                    rotateRight(parent); // Zig (правый поворот)
                } else {
                    rotateLeft(parent);  // Zig (левый поворот)
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    // Случай 2: Zig-Zig (правый-правый)
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    // Случай 3: Zig-Zig (левый-левый)
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    // Случай 4: Zig-Zag (левый-правый)
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    // Случай 5: Zig-Zag (правый-левый)
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

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String info) {
        if (root == null) {
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
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = node.info;
                node.info = info;
                splay(node); // Splay к корню
                return oldValue;
            }
        }

        // Создание нового узла
        Node newNode = new Node(key, info);
        newNode.parent = parent;
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode); // Splay нового узла к корню
        currentSize++;
        return null;
    }

    // Удаляет элемент по ключу
    @Override
    public String remove(Object obj) {
        Node node = findNode((Integer) obj);
        if (node == null) {
            return null;
        }

        splay(node); // Перемещаем удаляемый узел в корень

        String removedValue = node.info;

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
            Node maxLeft = findMax(root); // Находим максимум в левом поддереве
            splay(maxLeft); // Splay максимума в корень
            maxLeft.right = rightSubtree; // Присоединяем правое поддерево
            if (rightSubtree != null) {
                rightSubtree.parent = maxLeft;
            }
        }

        currentSize--;
        return removedValue;
    }

    // Вспомогательный метод для поиска узла по ключу
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    // Находит узел с максимальным ключом в поддереве
    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object obj) {
        Node node = findNode((Integer) obj);
        if (node != null) {
            splay(node); // Splay найденного узла к корню
            return node.info;
        }
        return null;
    }

    // Проверяет наличие ключа в карте
    @Override
    public boolean containsKey(Object obj) {
        return findNode((Integer) obj) != null;
    }

    // Возвращает количество элементов в карте
    @Override
    public int size() {
        return currentSize;
    }

    // Очищает карту
    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    // Проверяет, пуста ли карта
    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Возвращает представление части карты для ключей меньше toKey
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    // Возвращает представление части карты для ключей меньше toKey (с включением границы)
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    // Вспомогательный метод для построения headMap
    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(toKey) < 0 || (inclusive && node.key.equals(toKey))) {
            result.put(node.key, node.info);
            headMap(node.right, toKey, inclusive, result);
        }
        headMap(node.left, toKey, inclusive, result);
    }

    // Возвращает представление части карты для ключей больше или равных fromKey
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    // Возвращает представление части карты для ключей больше fromKey (с включением границы)
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    // Вспомогательный метод для построения tailMap
    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) > 0 || (inclusive && node.key.equals(fromKey))) {
            result.put(node.key, node.info);
            tailMap(node.left, fromKey, inclusive, result);
        }
        tailMap(node.right, fromKey, inclusive, result);
    }

    // Возвращает первый (наименьший) ключ в карте
    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMin(root);
        splay(node); // Splay минимального элемента к корню
        return node.key;
    }

    // Возвращает последний (наибольший) ключ в карте
    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMax(root);
        splay(node); // Splay максимального элемента к корню
        return node.key;
    }

    // Находит узел с минимальным ключом в поддереве
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Возвращает наибольший ключ, строго меньший заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для поиска lowerKey
    private Node lowerNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) >= 0) {
            return lowerNode(node.left, key);
        } else {
            Node right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    // Возвращает наибольший ключ, меньший или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для поиска floorKey
    private Node floorNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
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

    // Возвращает наименьший ключ, больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для поиска ceilingKey
    private Node ceilingNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
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

    // Возвращает наименьший ключ, строго больший заданного
    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для поиска higherKey
    private Node higherNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) <= 0) {
            return higherNode(node.right, key);
        } else {
            Node left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }

    // Возвращает представление части карты для ключей от fromKey до toKey
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    // Возвращает представление части карты с указанием включения границ
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    // Вспомогательный метод для построения subMap
    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
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

    // Возвращает строковое представление карты в формате {key1=value1, key2=value2}
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("{");
        toString(root, resultStr);
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2); // Убираем последнюю запятую и пробел
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    // Вспомогательный метод для построения строкового представления
    private void toString(Node node, StringBuilder resultStr) {
        if (node != null) {
            toString(node.left, resultStr);
            resultStr.append(node.key).append("=").append(node.info).append(", ");
            toString(node.right, resultStr);
        }
    }

    // НЕ РЕАЛИЗОВАННЫЕ МЕТОДЫ ИНТЕРФЕЙСА NavigableMap

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}