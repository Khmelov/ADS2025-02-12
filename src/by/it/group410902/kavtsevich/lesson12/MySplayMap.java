package by.it.group410902.kavtsevich.lesson12;

import java.util.*;
//Вид бинарного дерева поиска с функцией самобалансировки
public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;    // корень splay-дерева, меняется после каждой операции
    private int size;     // количество элементов в карте

    // Узел splay-дерева
    private class Node {
        Integer key;      // ключ элемента
        String value;    // значение элемента
        Node left;       // левый потомок (меньшие ключи)
        Node right;      // правый потомок (большие ключи)

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Основная операция splay - перемещает узел с заданным ключом в корень
    private Node splay(Node root, Integer key) {
        // Базовый случай: пустое дерево или ключ найден в корне
        if (root == null || key.equals(root.key)) return root;

        // Ключ находится в левом поддереве
        if (key.compareTo(root.key) < 0) {
            // Левое поддерево пусто - ключ не найден
            if (root.left == null) return root;

            // Zig-Zig случай (левый-левый)
            if (key.compareTo(root.left.key) < 0) {
                // Рекурсивно поднимаем узел влево-влево
                root.left.left = splay(root.left.left, key);
                // Правый поворот вокруг корня
                root = rotateRight(root);
            }
            // Zig-Zag случай (левый-правый)
            else if (key.compareTo(root.left.key) > 0) {
                // Рекурсивно поднимаем узел влево-вправо
                root.left.right = splay(root.left.right, key);
                // Левый поворот вокруг левого потомка если нужно
                if (root.left.right != null) root.left = rotateLeft(root.left);
            }

            // Выполняем финальный поворот если левый потомок существует
            if (root.left == null) return root;
            else return rotateRight(root);
        }
        // Ключ находится в правом поддереве
        else {
            // Правое поддерево пусто - ключ не найден
            if (root.right == null) return root;

            // Zag-Zag случай (правый-правый)
            if (key.compareTo(root.right.key) > 0) {
                // Рекурсивно поднимаем узел вправо-вправо
                root.right.right = splay(root.right.right, key);
                // Левый поворот вокруг корня
                root = rotateLeft(root);
            }
            // Zag-Zig случай (правый-левый)
            else if (key.compareTo(root.right.key) < 0) {
                // Рекурсивно поднимаем узел вправо-влево
                root.right.left = splay(root.right.left, key);
                // Правый поворот вокруг правого потомка если нужно
                if (root.right.left != null) root.right = rotateRight(root.right);
            }

            // Выполняем финальный поворот если правый потомок существует
            if (root.right == null) return root;
            else return rotateLeft(root);
        }
    }

    // Правый поворот
    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Левый поворот
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        // Пустое дерево - создаем корень
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        // Поднимаем узел с ключом (или ближайший) в корень
        root = splay(root, key);

        // Ключ уже существует - обновляем значение
        if (key.equals(root.key)) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        // Создаем новый узел и вставляем его в корень
        Node newNode = new Node(key, value);
        if (key.compareTo(root.key) < 0) {
            // Новый ключ меньше корня - делаем новый узел корнем
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            // Новый ключ больше корня - делаем новый узел корнем
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;

        // Пустое дерево
        if (root == null) return null;

        // Поднимаем удаляемый узел в корень
        root = splay(root, intKey);

        // Ключ не найден
        if (!intKey.equals(root.key)) return null;

        String oldValue = root.value;

        // Удаление корня
        if (root.left == null) {
            // Нет левого поддерева - правый потомок становится корнем
            root = root.right;
        } else {
            // Есть левое поддерево
            Node temp = root.right;
            root = root.left;
            // Поднимаем максимальный элемент левого поддерева в корень
            root = splay(root, intKey);
            root.right = temp;
        }
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (root == null) return null;

        // Поднимаем узел с ключом в корень (если существует)
        root = splay(root, intKey);

        // Проверяем совпадение ключа в корне
        if (intKey.equals(root.key)) return root.value;
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;

        // Проверяем текущий узел
        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }

        // Рекурсивно проверяем поддеревья
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        // Ищем самый левый узел (минимальный ключ)
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        // Ищем самый правый узел (максимальный ключ)
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    // Рекурсивное построение headMap (ключи < toKey)
    private void headMapHelper(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            // Ключ меньше границы - добавляем и обходим оба поддерева
            result.put(node.key, node.value);
            headMapHelper(node.left, toKey, result);
            headMapHelper(node.right, toKey, result);
        } else {
            // Ключ больше или равен - идем только в левое поддерево
            headMapHelper(node.left, toKey, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    // Рекурсивное построение tailMap (ключи >= fromKey)
    private void tailMapHelper(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            // Ключ больше или равен границы - добавляем и обходим оба поддерева
            result.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, result);
            tailMapHelper(node.right, fromKey, result);
        } else {
            // Ключ меньше - идем только в правое поддерево
            tailMapHelper(node.right, fromKey, result);
        }
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return lowerKeyHelper(root, key, null);
    }

    // Наибольший ключ строго меньше заданного
    private Integer lowerKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) < 0) {
            // Текущий ключ меньше искомого - обновляем лучший результат
            best = node.key;
            // Ищем еще больший ключ в правом поддереве
            return lowerKeyHelper(node.right, key, best);
        } else {
            // Текущий ключ больше или равен - ищем в левом поддереве
            return lowerKeyHelper(node.left, key, best);
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return floorKeyHelper(root, key, null);
    }

    // Наибольший ключ меньше или равный заданному
    private Integer floorKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) <= 0) {
            // Текущий ключ меньше или равен искомому - обновляем лучший результат
            best = node.key;
            // Ищем еще больший ключ в правом поддереве
            return floorKeyHelper(node.right, key, best);
        } else {
            // Текущий ключ больше - ищем в левом поддереве
            return floorKeyHelper(node.left, key, best);
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return ceilingKeyHelper(root, key, null);
    }

    // Наименьший ключ больше или равный заданному
    private Integer ceilingKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) >= 0) {
            // Текущий ключ больше или равен искомому - обновляем лучший результат
            best = node.key;
            // Ищем еще меньший подходящий ключ в левом поддереве
            return ceilingKeyHelper(node.left, key, best);
        } else {
            // Текущий ключ меньше - ищем в правом поддереве
            return ceilingKeyHelper(node.right, key, best);
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        return higherKeyHelper(root, key, null);
    }

    // Наименьший ключ строго больше заданного
    private Integer higherKeyHelper(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key.compareTo(key) > 0) {
            // Текущий ключ больше искомого - обновляем лучший результат
            best = node.key;
            // Ищем еще меньший подходящий ключ в левом поддереве
            return higherKeyHelper(node.left, key, best);
        } else {
            // Текущий ключ меньше или равен - ищем в правом поддереве
            return higherKeyHelper(node.right, key, best);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // удаляем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (вывод в отсортированном порядке)
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Нереализованные методы NavigableMap интерфейса (не требуются по заданию)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}
