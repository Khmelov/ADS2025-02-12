package by.it.group410902.kovalchuck.lesson01.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {


    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size;
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    // Проверяет, содержится ли ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node node = find((Integer) key);
        if (node != null) {
            // Перемещаем найденный узел в корень
            splay(node);
            return true;
        }
        return false;
    }

    // Проверяет, содержится ли значение в дереве
    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) {
            // При нахождении значения перемещаем узел в корень
            splay(node);
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Получение значения по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        if (node != null) {
            // Перемещаем найденный узел в корень
            splay(node);
            return node.value;
        }
        return null;
    }

    // Поиск узла по ключу без splay операции
    private Node find(Integer key) {
        Node current = root;
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

    // Добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        // Если дерево пустое, создаем корень
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;
        // Поиск места для вставки
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
                splay(current);
                return oldValue;
            }
        }

        // Создаем новый узел
        Node newNode = new Node(key, value, parent);
        int cmp = key.compareTo(parent.key);
        // Вставляем в нужное место
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        // Перемещаем новый узел в корень
        splay(newNode);
        return null;
    }

    // Удаление элемента по ключу
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        if (node == null) return null;

        String oldValue = node.value;
        // Перемещаем удаляемый узел в корень
        splay(node);

        // Случай 1: нет левого поддерева
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        }
        // Случай 2: нет правого поддерева
        else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        }
        // Случай 3: есть оба поддерева
        else {
            // Находим минимальный элемент в правом поддереве
            Node min = min(node.right);
            // Если min не является непосредственным правым потомком
            if (min.parent != node) {
                // Перестраиваем связи
                if (min.right != null) {
                    min.right.parent = min.parent;
                }
                min.parent.left = min.right;
                min.right = node.right;
                min.right.parent = min;
            }
            // Присоединяем левое поддерево
            min.left = node.left;
            min.left.parent = min;
            min.parent = null;
            root = min;
        }

        size--;
        return oldValue;
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node min(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Поиск узла с максимальным ключом в поддереве
    private Node max(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Splay операция - перемещение узла в корень
    private void splay(Node node) {
        while (node.parent != null) {
            if (node.parent.parent == null) {
                // Zig - один поворот
                if (node.parent.left == node) {
                    rotateRight(node.parent);
                } else {
                    rotateLeft(node.parent);
                }
            } else if (node.parent.left == node && node.parent.parent.left == node.parent) {
                // Zig-Zig (правые повороты)
                rotateRight(node.parent.parent);
                rotateRight(node.parent);
            } else if (node.parent.right == node && node.parent.parent.right == node.parent) {
                // Zig-Zig (левые повороты)
                rotateLeft(node.parent.parent);
                rotateLeft(node.parent);
            } else if (node.parent.left == node && node.parent.parent.right == node.parent) {
                // Zig-Zag (правый-левый)
                rotateRight(node.parent);
                rotateLeft(node.parent);
            } else {
                // Zig-Zag (левый-правый)
                rotateLeft(node.parent);
                rotateRight(node.parent);
            }
        }
        root = node;
    }

    // Левый поворот
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        if (rightChild == null) return;

        // Перенастраиваем левое поддерево правого потомка
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        // Обновляем родителя правого потомка
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        // Делаем node левым потомком rightChild
        rightChild.left = node;
        node.parent = rightChild;
    }

    // Правый поворот
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        if (leftChild == null) return;

        // Перенастраиваем правое поддерево левого потомка
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        // Обновляем родителя левого потомка
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        // Делаем node правым потомком leftChild
        leftChild.right = node;
        node.parent = leftChild;
    }

    // Очистка дерева
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Строковое представление дерева
    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // Обход дерева в порядке возрастания для toString
    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderToString(node.right, sb);
    }

    // Получение первого (наименьшего) ключа
    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node minNode = min(root);
        splay(minNode);
        return minNode.key;
    }

    // Получение последнего (наибольшего) ключа
    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node maxNode = max(root);
        splay(maxNode);
        return maxNode.key;
    }

    // Наибольший ключ, строго меньший заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node node = findLower(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Поиск наибольшего ключа, строго меньшего заданного
    private Node findLower(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                // Идем влево - текущий ключ слишком большой
                current = current.left;
            } else {
                // Нашли кандидата, идем вправо для поиска большего
                candidate = current;
                current = current.right;
            }
        }
        return candidate;
    }

    // Наибольший ключ, меньший или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        Node node = findFloor(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Поиск наибольшего ключа, меньшего или равного заданному
    private Node findFloor(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                // Идем влево - текущий ключ слишком большой
                current = current.left;
            } else if (cmp > 0) {
                // Нашли кандидата, идем вправо для поиска большего
                candidate = current;
                current = current.right;
            } else {
                // Нашли точное совпадение
                return current;
            }
        }
        return candidate;
    }

    // Наименьший ключ, больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeiling(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Поиск наименьшего ключа, большего или равного заданному
    private Node findCeiling(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                // Нашли кандидата, идем влево для поиска меньшего
                candidate = current;
                current = current.left;
            } else if (cmp > 0) {
                // Идем вправо - текущий ключ слишком маленький
                current = current.right;
            } else {
                // Нашли точное совпадение
                return current;
            }
        }
        return candidate;
    }

    // Наименьший ключ, строго больший заданного
    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigher(key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    // Поиск наименьшего ключа, строго большего заданного
    private Node findHigher(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                // Нашли кандидата, идем влево для поиска меньшего
                candidate = current;
                current = current.left;
            } else {
                // Идем вправо - текущий ключ слишком маленький
                current = current.right;
            }
        }
        return candidate;
    }

    // Получение части карты до указанного ключа (исключая)
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    // Рекурсивное построение headMap
    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            // Ключ подходит - добавляем
            result.put(node.key, node.value);
            // Обходим правое поддерево
            headMap(node.right, toKey, result);
        }
        // Всегда обходим левое поддерево
        headMap(node.left, toKey, result);
    }

    // Получение части карты от указанного ключа (включая)
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    // Рекурсивное построение tailMap
    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            // Ключ подходит - добавляем
            result.put(node.key, node.value);
            // Обходим левое поддерево
            tailMap(node.left, fromKey, result);
        }
        // Всегда обходим правое поддерево
        tailMap(node.right, fromKey, result);
    }

    // Методы, не реализованные в данной реализации

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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
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