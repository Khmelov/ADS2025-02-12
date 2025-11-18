package by.it.group410901.kvitchenko.lesson12;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;

// Класс MySplayMap реализует NavigableMap с помощью Splay-дерева.
public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс Node представляет узел в Splay-дереве.
    private static class Node {
        Integer key;
        String info;
        Node left, right, parent; // Ссылка на родителя для splay-операции

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
        }
    }

    private Node root;           // Корень Splay-дерева
    private int currentSize = 0; // Количество элементов в карте


    // Методы интерфейса Map и SortedMap
    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используем естественный порядок (для Integer)
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }


    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderTraversal(root, keys); // Инфиксный обход для получения ключей
        return keys;
    }

    @Override
    public Collection<String> values() {
        Collection<String> arr = new ArrayList<>();
        inOrderTraversalValues(root, arr); // Инфиксный обход для получения значений
        return arr;
    }

    // Рекурсивный инфиксный обход для сбора ключей.
    private void inOrderTraversal(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderTraversal(node.left, keys);
            keys.add(node.key);
            inOrderTraversal(node.right, keys);
        }
    }

    // Рекурсивный инфиксный обход для сбора значений.
    private void inOrderTraversalValues(Node node, Collection<String> values) {
        if (node != null) {
            inOrderTraversalValues(node.left, values);
            values.add(node.info);
            inOrderTraversalValues(node.right, values);
        }
    }

    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj); // Инициируем поиск значения
    }

    // Рекурсивный обход всего дерева для поиска значения. O(N).
    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false;
        }
        if (obj.equals(node.info)) {
            return true;
        }
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    // Поиск узла по ключу без выполнения операции splay.
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


    // Выполняет операцию Splay (Разбиение): перемещает узел в корень.
    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                // Случай Zig (узел - дочерний элемент корня)
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                // Случай Zig-Zig (прямой путь)
                if (node == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                }
                // Случай Zig-Zag (ломаный путь)
                else if (node == parent.right && parent == grandparent.left) {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else { // node == parent.left && parent == grandparent.right
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node; // Узел становится новым корнем
    }

    // Выполняет левый поворот.
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        // Перенаправление родителя:
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

    // Выполняет правый поворот.
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        // Перенаправление родителя:
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


    @Override
    // Добавляет новую пару или обновляет существующую. Найденный/вставленный узел перемещается в корень (splay).
    public String put(Integer key, String info) {
        if (root == null) {
            root = new Node(key, info);
            currentSize++;
            return null;
        }

        Node node = root;
        Node parent = null;
        while (node != null) {
            parent = node;
            if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                // Ключ найден: обновляем значение и splay.
                String oldValue = node.info;
                node.info = info;
                splay(node);
                return oldValue;
            }
        }

        // Ключ не найден: вставляем новый узел.
        Node newNode = new Node(key, info);
        newNode.parent = parent;
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode); // Перемещаем новый узел в корень
        currentSize++;
        return null;
    }

    @Override
    // Удаляет отображение для ключа. Удаляемый узел splay в корень, затем дерево перестраивается.
    public String remove(Object obj) {
        Integer key = (Integer) obj;
        Node node = findNode(key); // Ищем узел
        if (node == null) {
            return null;
        }

        splay(node); // Перемещаем удаляемый узел в корень

        String removedValue = node.info;

        // Разделение дерева:
        if (node.left == null) {
            // Если нет левого поддерева, новое дерево - правое поддерево.
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            // Если есть левое поддерево:
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;
            // Находим максимальный элемент в левом поддереве и splay его в корень.
            Node maxLeft = findMax(root);
            splay(maxLeft);
            // Присоединяем исходное правое поддерево к новому корню.
            maxLeft.right = rightSubtree;
            if (rightSubtree != null) {
                rightSubtree.parent = maxLeft;
            }
        }

        currentSize--;
        return removedValue;
    }

    @Override
    // Возвращает значение по ключу. Найденный узел перемещается в корень (splay).
    public String get(Object obj) {
        Node node = findNode((Integer) obj);
        if (node != null) {
            splay(node); // Перемещаем найденный узел в корень
            return node.info;
        }
        return null;
    }

    @Override
    // Проверяет наличие ключа.
    public boolean containsKey(Object obj) {
        return findNode((Integer) obj) != null;
    }


    // Находит минимальный элемент (самый левый).
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Находит максимальный элемент (самый правый).
    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMin(root);
        splay(node); // Splay min-узел в корень
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMax(root);
        splay(node); // Splay max-узел в корень
        return node.key;
    }


    // Находит ключ, строго меньший, чем заданный (lower).
    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для lowerKey.
    private Node lowerNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) >= 0) {
            return lowerNode(node.left, key);
        } else {
            Node right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    // Находит ключ, меньший или равный заданному (floor).
    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для floorKey.
    private Node floorNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) == 0) return node;

        if (node.key.compareTo(key) > 0) {
            return floorNode(node.left, key);
        } else {
            Node right = floorNode(node.right, key);
            return right != null ? right : node;
        }
    }

    // Находит ключ, больший или равный заданному (ceiling).
    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для ceilingKey.
    private Node ceilingNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) == 0) return node;

        if (node.key.compareTo(key) < 0) {
            return ceilingNode(node.right, key);
        } else {
            Node left = ceilingNode(node.left, key);
            return left != null ? left : node;
        }
    }

    // Находит ключ, строго больший, чем заданный (higher).
    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Вспомогательный метод для higherKey.
    private Node higherNode(Node node, Integer key) {
        if (node == null) return null;

        if (node.key.compareTo(key) <= 0) {
            return higherNode(node.right, key);
        } else {
            Node left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }


    // Методы для подкарт
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false); // По умолчанию toKey не включен
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    // Рекурсивный обход для headMap.
    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        boolean check = node.key.compareTo(toKey) < 0 || (inclusive && node.key.equals(toKey));

        if (check) {
            result.put(node.key, node.info);
            headMap(node.right, toKey, inclusive, result);
        }
        headMap(node.left, toKey, inclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true); // По умолчанию fromKey включен
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    // Рекурсивный обход для tailMap.
    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;

        boolean check = node.key.compareTo(fromKey) > 0 || (inclusive && node.key.equals(fromKey));

        if (check) {
            result.put(node.key, node.info);
            tailMap(node.left, fromKey, inclusive, result);
        }
        tailMap(node.right, fromKey, inclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false); // По умолчанию [fromKey, toKey)
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    // Рекурсивный обход для subMap.
    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) return;

        boolean isGreaterOrEqualFrom = node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey));
        boolean isLesserOrEqualTo = node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey));

        // Идем влево, если есть шанс найти подходящие ключи.
        if (isGreaterOrEqualFrom) {
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, result);
        }

        // Обрабатываем текущий узел.
        if (isGreaterOrEqualFrom && isLesserOrEqualTo) {
            result.put(node.key, node.info);
        }

        // Идем вправо, если есть шанс найти подходящие ключи.
        if (isLesserOrEqualTo) {
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, result);
        }
    }


    @Override
    // Возвращает строковое представление карты в отсортированном порядке.
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("{");
        toString(root, resultStr); // Инфиксный обход
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2);
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    // Вспомогательный рекурсивный метод для инфиксного обхода.
    private void toString(Node node, StringBuilder resultStr) {
        if (node != null) {
            toString(node.left, resultStr);
            resultStr.append(node.key).append("=").append(node.info).append(", ");
            toString(node.right, resultStr);
        }
    }


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