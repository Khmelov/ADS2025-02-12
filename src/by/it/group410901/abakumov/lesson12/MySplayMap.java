package by.it.group410901.abakumov.lesson12;

// Реализация Splay-дерева — дерева, которое само себя балансирует
// Часто используемые элементы поднимаются ближе к корню
class MySplayMap implements java.util.NavigableMap<Integer, String> {

    // Класс для узла дерева
    private static class Node {
        Integer key;      // ключ узла
        String value;     // значение по ключу
        Node left;        // левый ребёнок
        Node right;       // правый ребёнок
        Node parent;      // родитель узла (нужен для поворотов)

        // Создаём узел с ключом и значением
        Node(Integer key, String value) {
            this.key = key;       // сохраняем ключ
            this.value = value;   // сохраняем значение
            this.left = null;     // левое поддерево пустое
            this.right = null;    // правое поддерево пустое
            this.parent = null;   // родителя пока нет
        }
    }

    private Node root;  // корень дерева
    private int size;   // сколько элементов в дереве

    // Создаём пустое дерево
    public MySplayMap() {
        root = null;    // корня нет
        size = 0;       // элементов нет
    }

    // Поднимаем узел x к корню дерева
    private void splay(Node x) {
        // Повторяем, пока x не станет корнем (у корня нет родителя)
        while (x.parent != null) {                    // пока у x есть родитель

            Node parent = x.parent;                   // это родитель x
            Node grandparent = parent.parent;         // это дедушка (может быть null)

            // Родитель x — это корень (дедушки нет)
            if (grandparent == null) {
                if (x == parent.left) {               // x — левый ребёнок родителя
                    rotateRight(parent);              // делаем поворот вправо
                } else {                              // x — правый ребёнок
                    rotateLeft(parent);               // делаем поворот влево
                }
                // После этого x поднялся на 1 уровень выше
            }
            // Есть и родитель, и дедушка
            else {
                // x — слева от родителя
                if (x == parent.left) {
                    if (parent == grandparent.left) { // родитель тоже слева от дедушки
                        rotateRight(grandparent);     // поворот вправо вокруг дедушки
                        rotateRight(parent);          // ещё один поворот вправо
                        // x поднялся на 2 уровня сразу!
                    } else {                          // родитель справа от дедушки
                        rotateRight(parent);          // поворот вправо вокруг родителя
                        rotateLeft(grandparent);      // потом влево вокруг дедушки
                        // x "перепрыгнул" через родителя и дедушку
                    }
                }
                // x — справа от родителя
                else {
                    if (parent == grandparent.right) { // родитель справа от дедушки
                        rotateLeft(grandparent);       // поворот влево вокруг дедушки
                        rotateLeft(parent);            // ещё один влево
                    } else {                           // родитель слева от дедушки
                        rotateLeft(parent);            // влево вокруг родителя
                        rotateRight(grandparent);      // потом вправо вокруг дедушки
                    }
                }
            }
        }

        // x стал корнем
        root = x;                                     // обновляем корень дерева
    }

    // Поворот вправо вокруг узла x
    private void rotateRight(Node x) {
        Node y = x.left;                              // y — левый ребёнок x

        x.left = y.right;
        if (y.right != null) {                        // если у y был правый ребёнок
            y.right.parent = x;                       // его родитель теперь x
        }

        y.parent = x.parent;                          // y занимает место x

        // Если x был корнем — y становится новым корнем
        if (x.parent == null) {
            root = y;                                 // y — новый корень
        }
        // x был правым ребёнком своего родителя
        else if (x == x.parent.right) {
            x.parent.right = y;                       // y на место x
        }
        // x был левым ребёнком
        else {
            x.parent.left = y;                        // y на место x
        }

        y.right = x;                                  // x становится правым ребёнком y
        x.parent = y;                                 // родитель x теперь y
    }

    // Поворот влево вокруг узла x
    private void rotateLeft(Node x) {
        Node y = x.right;                             // y — правый ребёнок x

        x.right = y.left;
        if (y.left != null) {                         // если у y был левый ребёнок
            y.left.parent = x;                        // его родитель теперь x
        }

        y.parent = x.parent;                          // y занимает место x

        // Если x был корнем — y становится новым корнем
        if (x.parent == null) {
            root = y;                                 // y — новый корень
        }
        // x был левым ребёнком
        else if (x == x.parent.left) {
            x.parent.left = y;                        // y на место x
        }
        // x был правым ребёнком
        else {
            x.parent.right = y;                       // y на место x
        }

        y.left = x;                                   // x становится левым ребёнком y
        x.parent = y;                                 // родитель x теперь y
    }

    // Вставка ключа и значения
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException(); // ключ не может быть null

        // Дерево пустое — создаём первый узел
        if (root == null) {
            root = new Node(key, value);              // создаём корень
            size++;                                   // увеличиваем размер
            return null;                              // раньше значения не было
        }

        Node current = root;                          // начинаем с корня
        Node parent = null;                           // родитель текущего узла

        // Ищем место для вставки
        while (current != null) {
            parent = current;                         // запоминаем родителя
            int cmp = key.compareTo(current.key);     // сравниваем ключи
            if (cmp < 0) {                            // ключ меньше
                current = current.left;               // идём влево
            } else if (cmp > 0) {                     // ключ больше
                current = current.right;              // идём вправо
            } else {                                  // ключ уже есть
                String old = current.value;           // старое значение
                current.value = value;                // обновляем
                splay(current);                       // поднимаем узел к корню
                return old;                           // возвращаем старое
            }
        }

        // Создаём новый узел
        Node newNode = new Node(key, value);          // новый узел
        newNode.parent = parent;                      // его родитель — последний узел

        // Вставляем как в обычное дерево поиска
        if (key.compareTo(parent.key) < 0) {          // слева
            parent.left = newNode;
        } else {                                      // справа
            parent.right = newNode;
        }

        size++;                                       // увеличиваем размер
        splay(newNode);                               // поднимаем новый узел к корню
        return null;                                  // раньше не было
    }

    // Получить значение по ключу
    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        Node node = findNode((Integer) key);          // ищем узел
        return node != null ? node.value : null;      // возвращаем значение или null
    }

    // Поиск узла по ключу
    private Node findNode(Integer key) {
        Node current = root;                          // начинаем с корня
        while (current != null) {                     // пока не дошли до конца
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {                            // ключ меньше
                current = current.left;
            } else if (cmp > 0) {                     // ключ больше
                current = current.right;
            } else {                                  // нашли
                splay(current);                       // поднимаем к корню
                return current;                       // возвращаем узел
            }
        }
        return null;                                  // не нашли
    }

    // Удаление по ключу
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();

        Node node = findNode((Integer) key);          // ищем и поднимаем
        if (node == null) return null;                // не нашли

        String value = node.value;                    // сохраняем значение
        size--;                                       // уменьшаем размер

        // Нет левого поддерева
        if (node.left == null) {
            root = node.right;                        // правое поддерево становится деревом
            if (root != null) root.parent = null;     // обнуляем родителя
        }
        // Есть левое поддерево
        else {
            Node maxLeft = node.left;                 // ищем максимум в левом поддереве
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft);                           // поднимаем максимум к корню
            maxLeft.right = node.right;               // присоединяем правое поддерево
            if (node.right != null) {
                node.right.parent = maxLeft;
            }
            root = maxLeft;                           // новый корень
            root.parent = null;
        }
        return value;                                 // возвращаем удалённое значение
    }

    // Есть ли ключ
    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        return findNode((Integer) key) != null;       // ищем — если нашли, то есть
    }

    // Есть ли значение
    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false; // не строка
        return containsValue(root, (String) value);   // ищем в дереве
    }

    // Поиск значения в поддереве
    private boolean containsValue(Node node, String value) {
        if (node == null) return false;               // дошли до конца
        if (node.value.equals(value)) return true;    // нашли
        return containsValue(node.left, value) ||     // ищем слева
                containsValue(node.right, value);      // или справа
    }

    // Размер дерева
    @Override
    public int size() { return size; }

    // Пустое ли дерево
    @Override
    public boolean isEmpty() { return size == 0; }

    // Очистить дерево
    @Override
    public void clear() {
        root = null;                                  // убираем корень
        size = 0;                                     // обнуляем размер
    }

    // Все элементы с ключом < toKey
    @Override
    public java.util.NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();         // новое дерево
        headMap(root, toKey, inclusive, result);      // заполняем
        return result;
    }

    // Обход для headMap
    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;                     // конец ветки
        int cmp = node.key.compareTo(toKey);          // сравниваем
        if (cmp < 0 || (inclusive && cmp == 0)) {     // подходит
            headMap(node.left, toKey, inclusive, result);  // левое
            result.put(node.key, node.value);              // добавляем
            headMap(node.right, toKey, inclusive, result); // правое
        } else {                                      // не подходит
            headMap(node.left, toKey, inclusive, result);  // только левое
        }
    }

    // Все элементы с ключом >= fromKey
    @Override
    public java.util.NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    // Обход для tailMap
    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            tailMap(node.left, fromKey, inclusive, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, inclusive, result);
        } else {
            tailMap(node.right, fromKey, inclusive, result);
        }
    }

    // Минимальный ключ
    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node current = root;
        while (current.left != null) current = current.left; // идём влево
        splay(current);                                      // поднимаем к корню
        return current.key;                                  // возвращаем ключ
    }

    // Максимальный ключ
    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node current = root;
        while (current.right != null) current = current.right; // идём вправо
        splay(current);
        return current.key;
    }

    // Ключ строго меньше заданного
    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp < 0) {                               // меньше
                result = current;                        // запоминаем
                current = current.right;                 // ищем больше
            } else {
                current = current.left;
            }
        }
        if (result != null) splay(result);
        return result != null ? result.key : null;
    }

    // Ключ <= заданного
    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp <= 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (result != null) splay(result);
        return result != null ? result.key : null;
    }

    // Ключ >= заданного
    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp >= 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (result != null) splay(result);
        return result != null ? result.key : null;
    }

    // Ключ строго больше заданного
    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        Node result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp > 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (result != null) splay(result);
        return result != null ? result.key : null;
    }

    // Строковое представление
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb);                           // заполняем
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убираем ", "
        sb.append("}");
        return sb.toString();
    }

    // Обход в порядке возрастания ключей
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);                      // левое
        sb.append(node.key).append("=").append(node.value).append(", ");
        toString(node.right, sb);                     // правое
    }

    // Перегрузки для SortedMap
    @Override public java.util.SortedMap<Integer, String> headMap(Integer toKey) { return headMap(toKey, false); }
    @Override public java.util.SortedMap<Integer, String> tailMap(Integer fromKey) { return tailMap(fromKey, true); }

    // Не реализованные методы
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public java.util.Map.Entry<Integer, String> firstEntry() { return null; }
    @Override public java.util.Map.Entry<Integer, String> lastEntry() { return null; }
    @Override public java.util.Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public java.util.Map.Entry<Integer, String> pollLastEntry() { return null; }
    @Override public java.util.NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public java.util.NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public java.util.NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public java.util.NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public java.util.SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public java.util.Comparator<? super Integer> comparator() { return null; }
    @Override public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { return null; }
    @Override public java.util.Set<Integer> keySet() { return null; }
    @Override public java.util.Collection<String> values() { return null; }
    @Override public void putAll(java.util.Map<? extends Integer, ? extends String> m) {}
}