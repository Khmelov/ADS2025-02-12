package by.it.group451002.shandr.lesson12;

import java.util.*;

/*
* Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    и работает на основе splay-дерева
* */

// Splay-дерево - это самобалансирующееся бинарное дерево поиска с особенностью:
// после любой операции (поиск, вставка, удаление) найденный узел "поднимается" к корню
// Это обеспечивает "локальность" - часто используемые элементы быстрее доступны
public class MySplayMap implements NavigableMap<Integer, String> {
    // Основная идея дерева Splay Tree — доступ к недавно используемым узлам осуществляется
    // быстрее благодаря операции "splay" (вращения узлов для продвижения их к корню дерева).
    // Принцип "кэширования": часто используемые элементы автоматически перемещаются ближе к корню

    private class Node {
        // представляет узел дерева с ключом, значением и ссылками на левый, правый дочерние узлы и родителя.
        public int key;
        public String data;
        public Node left, right, parent;

        // Конструктор создает новый узел с заданным ключом и значением
        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        // Конструктор копирования - создает копию существующего узла
        public Node(Node n) {
            this.key = n.key;
            this.data = n.data;
            this.left = n.left;
            this.right = n.right;
            this.parent = n.parent;
        }

        // Находит узел с минимальным ключом в текущем поддереве
        // В бинарном дереве поиска минимальный элемент всегда в самом левом узле
        public Node findMin() {
            if (this.left == null)
                return this;           // Если левого потомка нет, текущий узел - минимальный
            else
                return this.left.findMin();  // Рекурсивно идем влево
        }
    }

    private Node head = null;  // Корень splay-дерева
    int size = 0;              // Количество элементов в дереве

    // Выполняет вращение (левое или правое) между родителем и ребенком
    // Вращение - основная операция для перестройки дерева при splay-операции
    private void rotate(Node parent, Node child) {
        Node g = parent.parent;  // Дед - родитель родителя

        // Обновляем ссылку деда на нового ребенка
        if (g != null)
            if (g.left == parent)
                g.left = child;   // Если родитель был левым потомком, теперь ребенок занимает его место
            else
                g.right = child;  // Если родитель был правым потомком, теперь ребенок занимает его место

        // Выполняем собственно вращение
        if (parent.left == child) {
            // Правое вращение (child поднимается, parent опускается)
            parent.left = child.right;  // Правый потомок ребенка становится левым потомком родителя
            child.right = parent;       // Родитель становится правым потомком ребенка
        } else {
            // Левое вращение (child поднимается, parent опускается)
            parent.right = child.left;  // Левый потомок ребенка становится правым потомком родителя
            child.left = parent;        // Родитель становится левым потомком ребенка
        }

        // Обновляем ссылки на родителя для всех затронутых узлов
        keepParent(child);
        keepParent(parent);
        child.parent = g;  // Новый родитель ребенка - дед
    }

    // Устанавливает ссылку на родителя для узла-потомка
    // Важно для поддержания корректной структуры дерева
    private void setParent(Node child, Node parent) {
        if (child != null)
            child.parent = parent;
    }

    // Устанавливает правильные ссылки на родителя для обоих потомков узла
    // Используется после операций, изменяющих структуру дерева
    private void keepParent(Node n) {
        setParent(n.left, n);   // Левый потомок указывает на n как на родителя
        setParent(n.right, n);  // Правый потомок указывает на n как на родителя
    }


    // Основная операция splay-дерева - "растягивание" узла к корню
    // Выполняет балансировку дерева, делая узел корнем через серию вращений
    // После splay-операции узел n становится корнем дерева
    private Node splay(Node n) {
        if (n == null) return null;

        Node parent = n.parent;
        if (parent == null)
            return n;  // Узел уже корень - ничего не делаем

        Node g = parent.parent;  // Дед узла

        if (g == null) {
            // Случай "Zig": у узла есть родитель, но нет деда
            // Выполняем одно вращение
            rotate(parent, n);
            return n;
        }

        // Определяем тип вращения: Zig-Zig или Zig-Zag
        Boolean isZigZig = (g.left == parent && parent.left == n) ||
                (g.right == parent && parent.right == n);

        if (isZigZig) {
            // Zig-Zig случай: узел, родитель и дед образуют "прямую линию"
            // Два вращения в одном направлении
            rotate(g, parent);  // Сначала вращаем родителя и деда
            rotate(parent, n);  // Затем вращаем узел и родителя
        } else {
            // Zig-Zag случай: узел, родитель и дед образуют "зигзаг"
            // Два вращения в разных направлениях
            rotate(parent, n);  // Сначала вращаем узел и родителя
            rotate(g, n);       // Затем вращаем узел и деда
        }

        // Рекурсивно продолжаем пока узел не станет корнем
        return splay(n);
    }

    // Рекурсивно ищет узел по ключу и выполняет splay для найденного узла
    // Если узел не найден, splay выполняется для последнего посещенного узла
    private Node recGet(Node n, Integer key) {
        if (n == null)
            return null;

        if (n.key == key) {
            // Ключ найден - выполняем splay и возвращаем узел
            return splay(n);
        }

        // Продолжаем поиск в левом или правом поддереве
        if (key < n.key && n.left != null)
            return recGet(n.left, key);
        if (key > n.key && n.right != null)
            return recGet(n.right, key);

        // Ключ не найден - выполняем splay для последнего посещенного узла
        return splay(n);
    }

    // Вспомогательный класс для хранения результата операции split
    // Содержит два поддерева: с ключами меньше и больше заданного
    private class SplitNode {
        Node left;   // Поддерево с ключами меньше заданного
        Node right;  // Поддерево с ключами больше заданного

        public SplitNode(Node l, Node r) {
            left = l;
            right = r;
        }
    }

    // Разделяет дерево на две части: с ключами меньше и больше заданного ключа
    // Основная операция для реализации вставки и удаления
    private SplitNode split(Node root, Integer key) {
        if (root == null)
            return new SplitNode(null, null);

        // Выполняем splay для ключа - ближайший узел становится корнем
        root = recGet(root, key);

        if (root.key == key) {
            // Ключ найден - разделяем на левое и правое поддеревья
            setParent(root.left, null);   // Отсоединяем левое поддерево
            setParent(root.right, null);  // Отсоединяем правое поддерево
            return new SplitNode(root.left, root.right);
        }

        if (root.key < key) {
            // Все ключи в дереве меньше заданного - правое поддерево может быть больше
            Node r = null;
            if (root.right != null)
                r = root.right;
            root.right = null;      // Отсоединяем правое поддерево
            setParent(r, null);     // Сбрасываем родителя для правого поддерева
            return new SplitNode(root, r);
        } else {
            // Все ключи в дереве больше заданного - левое поддерево может быть меньше
            Node l = null;
            if (root.left != null)
                l = root.left;
            root.left = null;       // Отсоединяем левое поддерево
            setParent(l, null);     // Сбрасываем родителя для левого поддерева
            return new SplitNode(l, root);
        }
    }


    // Вставляет новый узел с заданным ключом и значением
    // Использует операцию split для нахождения правильной позиции
    private Node insert(Node root, Integer key, String data) {
        // Разделяем дерево на части относительно вставляемого ключа
        SplitNode s = split(root, key);

        // Создаем новый узел
        root = new Node(key, data);

        // Левое поддерево из split становится левым потомком
        root.left = s.left;
        // Правое поддерево из split становится правым потомком
        root.right = s.right;

        // Обновляем ссылки на родителя
        keepParent(root);

        return root;
    }

    // Объединяет два поддерева в одно
    // Используется при удалении узлов
    private Node merge(Node left, Node right) {
        if (right == null)
            return left;  // Если правое поддерево пустое, возвращаем левое
        if (left == null)
            return right; // Если левое поддерево пустое, возвращаем правое

        // Находим максимальный элемент в левом поддереве и делаем его корнем
        right = recGet(right, left.key);

        // Левое поддерево становится левым потомком нового корня
        right.left = left;
        setParent(left, right);  // Обновляем ссылку на родителя

        return right;
    }

    // Удаляет узел по ключу
    // Использует операции split и merge
    private Node delete(Node root, Integer key) {
        // Находим и "поднимаем" узел с заданным ключом
        root = recGet(root, key);

        // Отсоединяем потомков от удаляемого узла
        setParent(root.left, null);
        setParent(root.right, null);

        // Объединяем левое и правое поддеревья
        return merge(root.left, root.right);
    }

    // Рекурсивный метод для построения строкового представления дерева
    // Использует inorder обход (левый-корень-правый) для вывода элементов в отсортированном порядке
    private void toStr(Node n, StringBuilder res) {
        if (n == null)
            return;
        toStr(n.left, res);  // Сначала левое поддерево (меньшие ключи)
        res.append(n.key).append("=").append(n.data).append(", ");  // Текущий узел
        toStr(n.right, res); // Затем правое поддерево (большие ключи)
    }

    @Override
    // Возвращает строковое представление дерева в формате {key1=value1, key2=value2, ...}
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(head, res);
        if (head != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));  // Убираем последнюю запятую и пробел
        res.append("}");
        return String.valueOf(res);
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;  // Не реализовано - возвращаем null
    }

    private Integer FMax = null;  // Временная переменная для поиска максимального значения

    // Рекурсивно ищет наибольший ключ, который меньше заданного
    private void recGetLower(Node n, Integer key) {
        if (n == null)
            return;
        recGetLower(n.left, key);    // Сначала левое поддерево
        if (FMax == null && n.key < key)
            FMax = n.key;            // Первый найденный ключ меньше заданного
        else if (n.key < key && FMax < n.key)
            FMax = n.key;            // Нашли ключ больше текущего максимума, но меньше заданного
        recGetLower(n.right, key);   // Затем правое поддерево
    }

    @Override
    // Возвращает ближайший меньший ключ (наибольший ключ, который меньше заданного)
    public Integer lowerKey(Integer key) {
        FMax = null;
        recGetLower(head, key);
        return FMax;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;  // Не реализовано
    }


    // Рекурсивно ищет наибольший ключ, который меньше или равен заданному
    private void recGetFloor(Node n, Integer key) {
        if (n == null)
            return;
        recGetFloor(n.left, key);       // Сначала левое поддерево
        if (FMax == null && n.key <= key)
            FMax = n.key;               // Первый найденный ключ меньше или равный заданному
        else if (n.key <= key && FMax < n.key)
            FMax = n.key;               // Нашли ключ больше текущего максимума, но меньше или равный заданному
        recGetFloor(n.right, key);      // Затем правое поддерево
    }

    @Override
    // Возвращает ближайший меньший или равный ключ (наибольший ключ, который ≤ заданному)
    public Integer floorKey(Integer key) {
        FMax = null;
        recGetFloor(head, key);
        return FMax;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;  // Не реализовано
    }

    private Integer FMin;  // Временная переменная для поиска минимального значения

    // Рекурсивно ищет наименьший ключ, который больше или равен заданному
    private void recGetCeiling(Node n, Integer key) {
        if (n == null)
            return;
        recGetCeiling(n.left, key);     // Сначала левое поддерево
        if (FMin == null && n.key >= key)
            FMin = n.key;               // Первый найденный ключ больше или равный заданному
        else if (n.key >= key && FMin > n.key)
            FMin = n.key;               // Нашли ключ меньше текущего минимума, но больше или равный заданному
        recGetCeiling(n.right, key);    // Затем правое поддерево
    }

    @Override
    // Возвращает ближайший больший или равный ключ (наименьший ключ, который ≥ заданному)
    public Integer ceilingKey(Integer key) {
        FMin = null;
        recGetCeiling(head, key);
        return FMin;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;  // Не реализовано
    }

    // Рекурсивно ищет узел с ближайшим ключом, который больше `key`
    private void recGetHigher(Node n, Integer key) {
        if (n == null)
            return;
        recGetHigher(n.left, key);      // Сначала левое поддерево
        if (FMin == null && n.key > key)
            FMin = n.key;               // Первый найденный ключ больше заданного
        else if (n.key > key && FMin > n.key)
            FMin = n.key;               // Нашли ключ меньше текущего минимума, но больше заданного
        recGetHigher(n.right, key);     // Затем правое поддерево
    }

    @Override
    // Возвращает ближайший больший ключ (наименьший ключ, который больше заданного)
    public Integer higherKey(Integer key) {
        FMin = null;
        recGetHigher(head, key);
        return FMin;
    }

    // Методы NavigableMap, которые не реализованы в данной версии
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }


    // Рекурсивно добавляет в дерево узлы с ключами >= `key`.
    // Используется для реализации tailMap()
    public void fillUpper(Node n, Integer key) {
        if (n == null)
            return;
        fillUpper(n.left, key);     // Сначала левое поддерево
        if (n.key >= key)
            this.put(n.key, n.data);  // Добавляем узел если ключ >= заданного
        fillUpper(n.right, key);    // Затем правое поддерево
    }

    // Рекурсивно добавляет в дерево узлы с ключами < `key`.
    // Используется для реализации headMap()
    public void fillLower(Node n, Integer key) {
        if (n == null)
            return;
        fillLower(n.left, key);     // Сначала левое поддерево
        if (n.key < key)
            this.put(n.key, n.data);   // Добавляем узел если ключ < заданного
        fillLower(n.right, key);    // Затем правое поддерево
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;  // Используется естественный порядок Integer
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;  // Не реализовано
    }

    @Override
    // Возвращает подмножество дерева с ключами < `toKey`.
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap tmp = new MySplayMap();
        tmp.fillLower(this.head, toKey);  // Рекурсивно копируем подходящие узлы в новое дерево
        return tmp;
    }

    @Override
    // Возвращает подмножество дерева с ключами >= `fromKey`.
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap tmp = new MySplayMap();
        tmp.fillUpper(this.head, fromKey);  // Рекурсивно копируем подходящие узлы в новое дерево
        return tmp;
    }

    @Override
    // Возвращает первый (минимальный) ключ
    // В бинарном дереве поиска минимальный ключ всегда в самом левом узле
    public Integer firstKey() {
        if (head == null) throw new NoSuchElementException("Map is empty");
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;      // Запоминаем текущий узел
            tmp = tmp.left;   // Двигаемся влево (к меньшим ключам)
        }
        return tmp_p.key;
    }

    @Override
    // Возвращает последний (максимальный) ключ
    // В бинарном дереве поиска максимальный ключ всегда в самом правом узле
    public Integer lastKey() {
        if (head == null) throw new NoSuchElementException("Map is empty");
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;      // Запоминаем текущий узел
            tmp = tmp.right;  // Двигаемся вправо (к большим ключам)
        }
        return tmp_p.key;
    }

    @Override
    // Возвращает количество элементов в дереве
    public int size() {
        return size;
    }

    @Override
    // Проверяет, пусто ли дерево
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // Проверяет, содержит ли дерево заданный ключ
    // После проверки выполняет splay для найденного или последнего узла
    public boolean containsKey(Object key) {
        head = recGet(head, (Integer) key);
        return key.equals(head.key);  // Проверяем, совпадает ли ключ корня с искомым
    }

    private Boolean ContainsValueF;  // Флаг для поиска по значению

    // Рекурсивно обходит дерево в поисках узла с заданным значением.
    // Если значение найдено, выполняет splay для этого узла
    private void recGetByValue(Node n, Object data) {
        if (n == null)
            return;
        recGetByValue(n.left, data);    // Ищем в левом поддереве
        if (ContainsValueF)             // Если уже нашли - выходим
            return;
        if (n.data.equals(data)) {      // Значение найдено
            ContainsValueF = true;
            splay(n);                   // "Поднимаем" найденный узел к корню
            return;
        }
        recGetByValue(n.right, data);   // Ищем в правом поддереве
    }


    @Override
    // Проверяет, существует ли узел с заданным значением в дереве.
    // Флаг `ContainsValueF` устанавливается в true, если значение найдено.
    // Внимание: поиск по значению требует полного обхода дерева O(n)
    public boolean containsValue(Object value) {
        ContainsValueF = false;
        recGetByValue(head, value);
        return ContainsValueF;
    }

    @Override
    // Возвращает значение по ключу
    // После поиска выполняет splay для найденного или последнего узла
    public String get(Object key) {
        head = recGet(head, (Integer) key);  // Ищем ключ и выполняем splay
        if (key.equals(head.key))            // Проверяем, нашли ли нужный ключ
            return head.data;
        return null;
    }

    @Override
    // Добавляет пару ключ-значение в дерево
    // Если ключ уже существует, обновляет значение и возвращает старое
    // Если ключа нет, вставляет новый узел и возвращает null
    public String put(Integer key, String value) {
        head = recGet(head, key);  // Ищем ключ и выполняем splay

        if (head == null || head.key != key) {
            // Ключ не найден - вставляем новый узел
            size++;
            head = insert(head, key, value);
            return null;
        }

        // Ключ найден - обновляем значение
        String pr = head.data;   // Сохраняем старое значение
        head.data = value;       // Устанавливаем новое значение
        return pr;               // Возвращаем старое значение
    }

    @Override
    // Удаляет пару ключ-значение по ключу
    // Возвращает значение удаленного элемента или null если ключ не найден
    public String remove(Object key) {
        String res = get(key);      // Получаем значение перед удалением (и выполняем splay)
        if (res != null) {
            size--;
            head = delete(head, (Integer) key);  // Удаляем узел
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // Не реализовано - требует последовательного вызова put() для всех элементов m
    }

    @Override
    // Очищает дерево
    public void clear() {
        size = 0;
        head = null;  // Удаляем ссылку на корень
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
}






