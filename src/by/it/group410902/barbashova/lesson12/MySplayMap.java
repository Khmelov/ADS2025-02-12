package by.it.group410902.barbashova.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    Node Root; // Корень Splay-дерева

    // Внутренний класс, представляющий узел Splay-дерева
    class Node {
        Integer key;        // Ключ узла
        String value;      // Значение узла
        Node left, right;  // Левый и правый потомки
        Node parent;       // Родительский узел (используется в некоторых операциях)

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Строковое представление дерева в формате {key=value, ...}
    @Override
    public String toString() {
        if (Root == null)
            return "{}";
        StringBuilder sb = new StringBuilder().append("{");
        inOrderTraversal(Root, sb); // Обход в порядке возрастания ключей
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (левый-корень-правый)
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key + "=" + node.value + ", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size(Root);
    }

    // Рекурсивный метод для вычисления размера поддерева
    int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    // Проверяет, содержится ли ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null; // Использует get, который выполняет splay
    }

    // Проверяет, содержится ли значение в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValue(Root, value.toString());
    }

    // Рекурсивный поиск значения в дереве
    boolean containsValue(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (node.value.equals(value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Основная операция: поиск значения по ключу с последующей раскруткой
    @Override
    public String get(Object key) {
        Node found = SearchKey((Integer) key, Root);
        if (found != null) {
            // Раскручиваем найденный узел к корню
            Root = splay(Root, found.key);
            return found.value;
        }
        return null;
    }

    // Рекурсивный поиск узла по ключу
    Node SearchKey(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    // Добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (Root == null) {
            Root = new Node(key, value);
            return null;
        }

        // Раскручиваем дерево вокруг ключа
        Root = splay(Root, key);
        int cmp = key.compareTo(Root.key);

        if (cmp == 0) {
            // Ключ уже существует - обновление значения
            String oldValue = Root.value;
            Root.value = value;
            return oldValue;
        } else if (cmp < 0) {
            // Вставка нового узла как корня с Root в правом поддереве
            Node newNode = new Node(key, value);
            newNode.left = Root.left;
            newNode.right = Root;
            newNode.right.parent = newNode;
            Root.left = null;
            Root = newNode;
        } else {
            // Вставка нового узла как корня с Root в левом поддереве
            Node newNode = new Node(key, value);
            newNode.right = Root.right;
            newNode.left = Root;
            newNode.left.parent = newNode;
            Root.right = null;
            Root = newNode;
        }
        return null;
    }

    // Удаление узла по ключу
    @Override
    public String remove(Object key) {
        if (Root == null) {
            return null;
        }

        // Раскручиваем узел для удаления к корню
        Root = splay(Root, (Integer) key);
        int cmp = ((Integer) key).compareTo(Root.key);
        if (cmp != 0) {
            return null; // Ключ не найден
        }

        String removedValue = Root.value;

        if (Root.left == null) {
            // Нет левого поддерева - правый потомок становится корнем
            Root = Root.right;
            if (Root != null) {
                Root.parent = null;
            }
        } else {
            // Есть левое поддерево - раскручиваем максимум левого поддерева
            Node newRoot = Root.right;
            newRoot = splay(newRoot, (Integer) key);
            newRoot.left = Root.left;
            newRoot.left.parent = newRoot;
            Root = newRoot;
        }

        return removedValue;
    }

    // ОСНОВНАЯ ОПЕРАЦИЯ: раскрутка узла с заданным ключом к корню
    Node splay(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // Ключ в левом поддереве
            if (node.left == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                // Zig-Zig случай (левый-левый)
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                // Zig-Zag случай (левый-правый)
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            if (node.left == null) {
                return node;
            } else {
                return rotateRight(node);
            }
        } else if (cmp > 0) {
            // Ключ в правом поддереве
            if (node.right == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                // Zag-Zig случай (правый-левый)
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                // Zag-Zag случай (правый-правый)
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            if (node.right == null) {
                return node;
            } else {
                return rotateLeft(node);
            }
        } else {
            // Ключ найден в текущем узле
            return node;
        }
    }

    // Правый поворот (используется в раскрутке)
    Node rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.right = node;
        leftChild.parent = node.parent;
        node.parent = leftChild;
        return leftChild;
    }

    // Левый поворот (используется в раскрутке)
    Node rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        rightChild.parent = node.parent;
        node.parent = rightChild;
        return rightChild;
    }

    // Очистка дерева
    @Override
    public void clear() {
        Root = clear(Root);
    }

    // Рекурсивная очистка поддерева
    Node clear(Node node) {
        if (node == null)
            return null;
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    // Методы навигации по дереву

    // Наибольший ключ, меньший заданного
    @Override
    public Integer lowerKey(Integer key) {
        if (Root == null)
            return null;
        Node node = lowerKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    // Рекурсивный поиск узла с наибольшим ключом, меньшим заданного
    Node lowerKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison <= 0)
            return lowerKeyNode(key, node.left); // Ищем в левом поддереве
        Node rightResult = lowerKeyNode(key, node.right); // Ищем в правом поддереве
        if (rightResult != null)
            return rightResult;
        return node; // Текущий узел - кандидат
    }

    // Наибольший ключ, меньший или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        if (Root == null)
            return null;
        Node node = floorKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    // Рекурсивный поиск узла с наибольшим ключом, меньшим или равным заданному
    Node floorKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node; // Точное совпадение
        if (comparison < 0)
            return floorKeyNode(key, node.left); // Ищем в левом поддереве
        Node rightResult = floorKeyNode(key, node.right); // Ищем в правом поддереве
        if (rightResult != null)
            return rightResult;
        return node; // Текущий узел - кандидат
    }

    // Наименьший ключ, больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        if (Root == null)
            return null;
        Node node = ceilingKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    // Рекурсивный поиск узла с наименьшим ключом, большим или равным заданному
    Node ceilingKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node; // Точное совпадение
        if (comparison > 0)
            return ceilingKeyNode(key, node.right); // Ищем в правом поддереве
        Node leftResult = ceilingKeyNode(key, node.left); // Ищем в левом поддереве
        if (leftResult != null)
            return leftResult;
        return node; // Текущий узел - кандидат
    }

    // Наименьший ключ, больший заданного
    @Override
    public Integer higherKey(Integer key) {
        if (Root == null)
            return null;
        Node node = higherKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    // Рекурсивный поиск узла с наименьшим ключом, большим заданного
    Node higherKeyNode(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison >= 0)
            return higherKeyNode(key, node.right); // Ищем в правом поддереве
        Node leftResult = higherKeyNode(key, node.left); // Ищем в левом поддереве
        if (leftResult != null)
            return leftResult;
        return node; // Текущий узел - кандидат
    }

    // Подкарта с ключами меньше toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        headMap(Root, toKey, subMap);
        return subMap;
    }

    // Рекурсивное построение headMap
    void headMap(Node node, Integer toKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMap(node.right, toKey, subMap);
        }

        headMap(node.left, toKey, subMap);
    }

    // Подкарта с ключами больше или равными fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap();
        tailMap(Root, fromKey, subMap);
        return subMap;
    }

    // Рекурсивное построение tailMap
    void tailMap(Node node, Integer fromKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMap(node.left, fromKey, subMap);
        }

        tailMap(node.right, fromKey, subMap);
    }

    // Первый (наименьший) ключ в дереве
    @Override
    public Integer firstKey() {
        if (Root == null)
            return null;
        Node node = Root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    // Последний (наибольший) ключ в дереве
    @Override
    public Integer lastKey() {
        if (Root == null)
            return null;
        Node node = Root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    ///////////////////////////////////////////////////////////////
    // Нереализованные методы интерфейса NavigableMap
    ///////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок сортировки
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }
}