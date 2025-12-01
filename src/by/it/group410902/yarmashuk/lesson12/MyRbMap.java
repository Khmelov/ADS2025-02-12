package by.it.group410902.yarmashuk.lesson12;


import java.util.*;

public class MyRbMap implements SortedMap<Object, Object> {

        // Цвета узлов
        private static final boolean RED = true;
        private static final boolean BLACK = false;


        private static class Node {
            Object key;
            Object value;
            Node left;
            Node right;
            Node parent;
            boolean color; // true = RED, false = BLACK


            Node(Object key, Object value, Node parent, boolean color) {
                this.key = key;
                this.value = value;
                this.parent = parent;
                this.color = color;
                this.left = NIL;
                this.right = NIL;
            }


            private Node() {
                this.key = null;
                this.value = null;
                this.parent = NIL;
                this.color = BLACK;
                this.left = NIL;
                this.right = NIL;
            }

            @Override
            public String toString() {
                return "(" + key + (color == RED ? ":R" : ":B") + ")";
            }
        }

        private static final Node NIL = new Node();


        private Node root;
        private int size;

        public MyRbMap() {
            this.root = NIL;
            this.size = 0;
        }

        // Вспомогательные методы для работы красно-черного дерева

        private boolean getColor(Node node) {
            return node.color;
        }

        private void setColor(Node node, boolean color) {
            if (node != NIL) { // Цвет NIL узла всегда BLACK, его нельзя изменить
                node.color = color;
            }
        }
        @SuppressWarnings("unchecked")
        private int compare(Object obj1, Object obj2) {
            return ((Comparable<Object>) obj1).compareTo(obj2);
        }

        private void rotateLeft(Node x) {
            Node y = x.right; // y становится правым потомком x
            x.right = y.left; // Левый потомок y становится правым потомком x
            if (y.left != NIL) {
                y.left.parent = x;
            }
            y.parent = x.parent; // Родитель x становится родителем y

            if (x.parent == NIL) { // Если x был корнем
                this.root = y;
            } else if (x == x.parent.left) { // Если x был левым потомком
                x.parent.left = y;
            } else { // Если x был правым потомком
                x.parent.right = y;
            }
            y.left = x; // x становится левым потомком y
            x.parent = y;
        }

        private void rotateRight(Node y) {
            Node x = y.left; // x становится левым потомком y
            y.left = x.right; // Правый потомок x становится левым потомком y
            if (x.right != NIL) {
                x.right.parent = y;
            }
            x.parent = y.parent; // Родитель y становится родителем x

            if (y.parent == NIL) { // Если y был корнем
                this.root = x;
            } else if (y == y.parent.left) { // Если y был левым потомком
                y.parent.left = x;
            } else { // Если y был правым потомком
                y.parent.right = x;
            }
            x.right = y; // y становится правым потомком x
            y.parent = x;
        }
        private void fixAfterInsert(Node z) {
            // Пока родитель z красный (это нарушает свойство 4: "нет двух подряд красных узлов")
            while (getColor(z.parent) == RED) {
                // Если родитель z - левый потомок его родителя (дедушки)
                if (z.parent == z.parent.parent.left) {
                    Node y = z.parent.parent.right; // y - это дядя (uncle) z

                    if (getColor(y) == RED) { // Case 1: Дядя y красный
                        // Перекрашиваем родителя, дядю в BLACK, дедушку в RED
                        setColor(z.parent, BLACK);
                        setColor(y, BLACK);
                        setColor(z.parent.parent, RED);
                        z = z.parent.parent; // Переходим к дедушке, чтобы проверить свойства выше
                    } else { // Дядя y черный (или NIL)
                        if (z == z.parent.right) { // Case 2: z - правый потомок
                            // Левое вращение вокруг родителя z (преобразует в Case 3)
                            z = z.parent;
                            rotateLeft(z);
                        }
                        // Case 3: z - левый потомок (либо изначально, либо после Case 2)
                        // Перекрашиваем родителя в BLACK, дедушку в RED
                        setColor(z.parent, BLACK);
                        setColor(z.parent.parent, RED);
                        // Правое вращение вокруг дедушки
                        rotateRight(z.parent.parent);
                    }
                } else { // Симметрично: Родитель z - правый потомок его родителя (дедушки)
                    Node y = z.parent.parent.left; // y - это дядя (uncle) z

                    if (getColor(y) == RED) { // Case 1: Дядя y красный
                        setColor(z.parent, BLACK);
                        setColor(y, BLACK);
                        setColor(z.parent.parent, RED);
                        z = z.parent.parent;
                    } else { // Дядя y черный (или NIL)
                        if (z == z.parent.left) { // Case 2: z - левый потомок
                            z = z.parent;
                            rotateRight(z);
                        }
                        // Case 3: z - правый потомок
                        setColor(z.parent, BLACK);
                        setColor(z.parent.parent, RED);
                        rotateLeft(z.parent.parent);
                    }
                }
            }
            setColor(root, BLACK); // Корень всегда должен быть черным
        }

        private void transplant(Node u, Node v) {
            if (u.parent == NIL) { // u - корень
                root = v;
            } else if (u == u.parent.left) { // u - левый потомок
                u.parent.left = v;
            } else { // u - правый потомок
                u.parent.right = v;
            }
            v.parent = u.parent; // v теперь имеет родителя u
        }

        private Node treeMinimum(Node node) {
            if (node == NIL) return NIL; // Если поддерево пусто, минимум - NIL
            while (node.left != NIL) {
                node = node.left;
            }
            return node;
        }

        private void fixAfterDelete(Node x) {
            // Продолжаем, пока x не станет корнем и его цвет черный (проблема "двойной черный")
            while (x != root && getColor(x) == BLACK) {
                if (x == x.parent.left) { // x - левый потомок
                    Node w = x.parent.right; // w - брат x
                    if (getColor(w) == RED) { // Case 1: Брат w - красный
                        setColor(w, BLACK);
                        setColor(x.parent, RED);
                        rotateLeft(x.parent);
                        w = x.parent.right; // w теперь черный брат
                    }
                    // Теперь w - черный
                    if (getColor(w.left) == BLACK && getColor(w.right) == BLACK) { // Case 2: w и оба его потомка черные
                        setColor(w, RED);
                        x = x.parent; // Переходим к родителю x
                    } else {
                        if (getColor(w.right) == BLACK) { // Case 3: w - черный, его правый потомок - черный, левый - красный
                            setColor(w.left, BLACK);
                            setColor(w, RED);
                            rotateRight(w);
                            w = x.parent.right; // w теперь черный брат с красным правым потомком (преобразует в Case 4)
                        }
                        // Case 4: w - черный, его правый потомок - красный
                        setColor(w, getColor(x.parent));
                        setColor(x.parent, BLACK);
                        setColor(w.right, BLACK);
                        rotateLeft(x.parent);
                        x = root; // Завершаем цикл, проблема решена
                    }
                } else { // Симметрично: x - правый потомок
                    Node w = x.parent.left; // w - брат x
                    if (getColor(w) == RED) { // Case 1: Брат w - красный
                        setColor(w, BLACK);
                        setColor(x.parent, RED);
                        rotateRight(x.parent);
                        w = x.parent.left; // w теперь черный брат
                    }
                    // Теперь w - черный
                    if (getColor(w.left) == BLACK && getColor(w.right) == BLACK) { // Case 2: w и оба его потомка черные
                        setColor(w, RED);
                        x = x.parent; // Переходим к родителю x
                    } else {
                        if (getColor(w.left) == BLACK) { // Case 3: w - черный, его левый потомок - черный, правый - красный
                            setColor(w.right, BLACK);
                            setColor(w, RED);
                            rotateLeft(w);
                            w = x.parent.left; // w теперь черный брат с красным левым потомком (преобразует в Case 4)
                        }
                        // Case 4: w - черный, его левый потомок - красный
                        setColor(w, getColor(x.parent));
                        setColor(x.parent, BLACK);
                        setColor(w.left, BLACK);
                        rotateRight(x.parent);
                        x = root; // Завершаем цикл, проблема решена
                    }
                }
            }
            setColor(x, BLACK); // В конце x становится черным (если он был красный, или если стал корнем после операций)
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
            root = NIL;
            size = 0;
        }
        @Override
        public String toString() {
            if (root == NIL) {
                return "{}";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{");

            boolean[] isFirst = {true};
            inOrderTraversal(root, sb, isFirst);
            sb.append("}");
            return sb.toString();
        }
        private void inOrderTraversal(Node node, StringBuilder sb, boolean[] isFirst) {
            if (node != NIL) {
                inOrderTraversal(node.left, sb, isFirst);

                if (!isFirst[0]) {
                    sb.append(", "); // Добавляем разделитель перед каждым элементом, кроме первого
                }
                sb.append(node.key);
                sb.append("=");
                // Если значение равно null, StringBuilder автоматически выведет "null"
                sb.append(node.value);
                isFirst[0] = false; // После добавления первого элемента, устанавливаем флаг в false

                inOrderTraversal(node.right, sb, isFirst);
            }
        }
        @Override
        public Object put(Object key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null.");
            }

            Node z = new Node(key, value, NIL, RED); // Новый узел всегда красный

            Node y = NIL; // y будет родителем для нового узла z
            Node x = root; // x используется для обхода дерева

            // Ищем место для вставки нового узла
            while (x != NIL) {
                y = x;
                int cmp = compare(key, x.key);
                if (cmp < 0) {
                    x = x.left;
                } else if (cmp > 0) {
                    x = x.right;
                } else { // Ключ уже существует, обновляем значение и возвращаем старое
                    Object oldValue = x.value;
                    x.value = value;
                    return oldValue;
                }
            }

            z.parent = y; // Устанавливаем родителя для нового узла
            if (y == NIL) { // Дерево было пустым, z становится корнем
                root = z;
            } else {
                int cmp = compare(key, y.key);
                if (cmp < 0) {
                    y.left = z;
                } else {
                    y.right = z;
                }
            }

            size++;
            fixAfterInsert(z); // Восстанавливаем свойства красно-черного дерева
            return null; // Для новой вставки возвращаем null
        }
        @Override
        public Object remove(Object key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null.");
            }

            Node z = search(key); // Находим узел для удаления
            if (z == NIL) { // Ключ не найден
                return null;
            }

            Object removedValue = z.value;
            Node y = z; // y - узел, который будет фактически удален из дерева
            boolean yOriginalColor = getColor(y); // Сохраняем цвет y

            Node x; // x - потомок y, который займет место y

            if (z.left == NIL) { // У z нет левого потомка (может быть 0 или 1 потомок)
                x = z.right;
                transplant(z, z.right);
            } else if (z.right == NIL) { // У z нет правого потомка (1 левый потомок)
                x = z.left;
                transplant(z, z.left);
            } else { // У узла z два потомка
                y = treeMinimum(z.right); // y - ин-ордер преемник z
                yOriginalColor = getColor(y); // Сохраняем цвет преемника
                x = y.right; // Преемник y не имеет левого потомка, поэтому x - его правый потомок (может быть NIL)

                if (y.parent == z) { // y - прямой правый потомок z
                    x.parent = y; // В этом случае x.parent указывает на y
                } else {
                    transplant(y, y.right); // Заменяем y его правым потомком
                    y.right = z.right;
                    y.right.parent = y;
                }
                transplant(z, y); // Заменяем z на y
                y.left = z.left;
                y.left.parent = y;
                setColor(y, getColor(z)); // y наследует цвет z
            }

            if (yOriginalColor == BLACK) { // Если удаленный узел был черным, нужно балансировать
                fixAfterDelete(x);
            }

            size--;
            return removedValue;
        }

    @Override
    public void putAll(Map<?, ?> m) {

    }

    private Node search(Object key) {
            Node current = root;
            while (current != NIL) {
                int cmp = compare(key, current.key); // Сравниваем искомый ключ с ключом узла
                if (cmp < 0) { // key < current.key
                    current = current.left;
                } else if (cmp > 0) { // key > current.key
                    current = current.right;
                } else {
                    return current; // Ключ найден
                }
            }
            return NIL; // Ключ не найден
        }
        @Override
        public Object get(Object key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null.");
            }
            Node node = search(key);
            return (node == NIL) ? null : node.value;
        }

        @Override
        public boolean containsKey(Object key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null.");
            }
            return search(key) != NIL;
        }
        @Override
        public boolean containsValue(Object value) {
            // Выполняем ин-ордер обход для поиска значения. Это O(n).
            return containsValueRecursive(root, value);
        }

        private boolean containsValueRecursive(Node node, Object value) {
            if (node == NIL) {
                return false;
            }

            // Проверяем левое поддерево
            if (containsValueRecursive(node.left, value)) {
                return true;
            }

            // Проверяем текущий узел
            if (value == null) {
                if (node.value == null) {
                    return true;
                }
            } else {
                if (value.equals(node.value)) { // Используем equals для сравнения объектов
                    return true;
                }
            }

            // Проверяем правое поддерево
            return containsValueRecursive(node.right, value);
        }

        @Override
        public Object firstKey() {
            if (isEmpty()) {
                throw new IllegalStateException("Map is empty.");
            }
            return treeMinimum(root).key;
        }

        /**
         * Возвращает последний (наибольший) ключ, содержащийся в этой мапе.
         *
         * @return Последний ключ
         * @throws IllegalStateException если мапа пуста
         */
        @Override
        public Object lastKey() {
            if (isEmpty()) {
                throw new IllegalStateException("Map is empty.");
            }
            Node current = root;
            while (current.right != NIL) {
                current = current.right;
            }
            return current.key;
        }

    @Override
    public Set<Object> keySet() {
        return Set.of();
    }

    @Override
    public Collection<Object> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return Set.of();
    }

    @Override
        public SortedMap<Object, Object> headMap(Object toKey) {
            if (toKey == null) {
                throw new IllegalArgumentException("toKey cannot be null.");
            }
            MyRbMap subMap = new MyRbMap();
            // upperBound: toKey (эксклюзивно). lowerBound: нет (null).
            inOrderCopy(root, subMap, null, toKey, false, false);
            return subMap;
        }
        @Override
        public SortedMap<Object, Object> tailMap(Object fromKey) {
            if (fromKey == null) {
                throw new IllegalArgumentException("fromKey cannot be null.");
            }
            MyRbMap subMap = new MyRbMap();
            // lowerBound: fromKey (включительно). upperBound: нет (null).
            inOrderCopy(root, subMap, fromKey, null, true, false);
            return subMap;
        }

    @Override
    public Comparator<? super Object> comparator() {
        return null;
    }

    @Override
        public SortedMap<Object, Object> subMap(Object fromKey, Object toKey) {
            if (fromKey == null || toKey == null) {
                throw new IllegalArgumentException("fromKey and toKey cannot be null.");
            }
            if (compare(fromKey, toKey) > 0) { // fromKey > toKey
                throw new IllegalArgumentException("fromKey cannot be greater than toKey.");
            }

            MyRbMap subMap = new MyRbMap();
            // fromKey: включительно, toKey: исключительно
            inOrderCopy(root, subMap, fromKey, toKey, true, false);
            return subMap;
        }
        /**
         * Вспомогательный рекурсивный метод для обхода дерева и копирования элементов
         * в другую мапу в заданных границах.
         * Метод использует отсечение ветвей (pruning) для повышения эффективности.
         *
         * @param node Текущий узел
         * @param targetMap Целевая мапа, в которую копируются элементы
         * @param lowerBound Нижняя граница ключей (null, если нет нижней границы)
         * @param upperBound Верхняя граница ключей (null, если нет верхней границы)
         * @param isLowerInclusive Флаг, указывающий, включать ли lowerBound (если не null)
         * @param isUpperInclusive Флаг, указывающий, включать ли upperBound (если не null)
         */
        private void inOrderCopy(Node node, MyRbMap targetMap, Object lowerBound, Object upperBound,
                                 boolean isLowerInclusive, boolean isUpperInclusive) {
            if (node == NIL) {
                return;
            }

            boolean currentKeyIsTooSmall = false;
            if (lowerBound != null) {
                int cmp = compare(node.key, lowerBound);
                if (cmp < 0 || (cmp == 0 && !isLowerInclusive)) {
                    currentKeyIsTooSmall = true;
                }
            }

            boolean currentKeyIsTooLarge = false;
            if (upperBound != null) {
                int cmp = compare(node.key, upperBound);
                if (cmp > 0 || (cmp == 0 && !isUpperInclusive)) {
                    currentKeyIsTooLarge = true;
                }
            }

            // Рекурсивно обходим левое поддерево, если оно может содержать элементы в диапазоне
            // (т.е. текущий узел не слишком мал для нижней границы, или еще не слишком велик для верхней)
            // Если текущий ключ слишком велик для верхней границы, то все в его правом поддереве тоже будет слишком велико.
            // Поэтому, если `!currentKeyIsTooLarge`, можно рекурсивно идти налево.
            // Более точная логика: идем влево, если `node.key` строго больше `lowerBound` ИЛИ
            // (`node.key` равно `lowerBound` И `lowerBound` включителен).
            // Это неверно. Идем влево, если есть шанс найти элемент слева от `node` в диапазоне.
            // Это значит, что `node.key` еще не превысил `upperBound` (эксклюзивно) или `upperBound` (включительно)
            // и может быть, что `node.left.key` будет в диапазоне.
            // Простой способ: если `node.key` НЕ слишком мал для нижней границы, то его левый потомок
            // может быть в диапазоне, или `node.key` находится в диапазоне, и его левые дети могут быть в диапазоне
            // Если `node.key` слишком велик для диапазона (currentKeyIsTooLarge), то мы все равно
            // должны попробовать пойти налево, потому что элементы слева могут быть меньше и входить в диапазон.
            // Мы НЕ идем налево, если `node.key` слишком мал для диапазона.
            if (!currentKeyIsTooSmall) {
                inOrderCopy(node.left, targetMap, lowerBound, upperBound, isLowerInclusive, isUpperInclusive);
            }

            // Если текущий ключ находится в диапазоне, добавляем его
            if (!currentKeyIsTooSmall && !currentKeyIsTooLarge) {
                targetMap.put(node.key, node.value);
            }

            // Рекурсивно обходим правое поддерево, если оно может содержать элементы в диапазоне
            // Мы НЕ идем направо, если `node.key` слишком велик для диапазона.
            if (!currentKeyIsTooLarge) {
                inOrderCopy(node.right, targetMap, lowerBound, upperBound, isLowerInclusive, isUpperInclusive);
            }
        }

    }









