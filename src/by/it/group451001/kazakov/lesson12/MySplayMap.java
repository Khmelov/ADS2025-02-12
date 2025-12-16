package by.it.group451001.kazakov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Внутренний класс для узла Splay-дерева
    private static class SplayNode {
        Integer key;        // Ключ узла
        String value;       // Значение узла
        SplayNode left;     // Левый потомок
        SplayNode right;    // Правый потомок
        SplayNode parent;   // Родительский узел

        // Конструктор узла
        SplayNode(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private SplayNode root;  // Корень дерева
    private int size;        // Количество элементов в дереве

    // Конструктор - создает пустое дерево
    public MySplayMap() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;  // Возвращает количество элементов
    }

    @Override
    public boolean isEmpty() {
        return size == 0;  // Проверяет, пусто ли дерево
    }

    @Override
    public void clear() {
        root = null;  // Удаляет корень (сборщик мусора удалит остальные узлы)
        size = 0;     // Сбрасывает счетчик размера
    }

    @Override
    public String put(Integer key, String value) {
        // Если дерево пустое, создаем корневой узел
        if (root == null) {
            root = new SplayNode(key, value);
            size++;
            return null;
        }

        // Поиск места для вставки нового узла
        SplayNode current = root;
        SplayNode parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;      // Идем в левое поддерево
            } else if (cmp > 0) {
                current = current.right;     // Идем в правое поддерево
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = current.value;
                current.value = value;
                splay(current);  // Поднимаем узел к корню
                return oldValue; // Возвращаем старое значение
            }
        }

        // Создаем новый узел
        SplayNode newNode = new SplayNode(key, value);
        newNode.parent = parent;

        // Вставляем узел в нужное место
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;   // Вставляем как левого потомка
        } else {
            parent.right = newNode;  // Вставляем как правого потомка
        }

        size++;           // Увеличиваем счетчик размера
        splay(newNode);   // Поднимаем новый узел к корню
        return null;      // Ключ был новый, возвращаем null
    }

    // Splay операция - поднимает узел к корню дерева
    // Основная операция Splay-дерева, которая перемещает часто используемые узлы ближе к корню
    private void splay(SplayNode node) {
        // Пока узел не станет корнем
        while (node.parent != null) {
            // Случай 1: родитель - корень (Zig)
            if (node.parent.parent == null) {
                if (node == node.parent.left) {
                    rotateRight(node.parent);  // Правый поворот
                } else {
                    rotateLeft(node.parent);   // Левый поворот
                }
            }
            // Случай 2: Zig-Zig (левый-левый или правый-правый)
            else if (node == node.parent.left && node.parent == node.parent.parent.left) {
                rotateRight(node.parent.parent);  // Два правых поворота
                rotateRight(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.right) {
                rotateLeft(node.parent.parent);   // Два левых поворота
                rotateLeft(node.parent);
            }
            // Случай 3: Zig-Zag (левый-правый или правый-левый)
            else if (node == node.parent.right && node.parent == node.parent.parent.left) {
                rotateLeft(node.parent);      // Левый затем правый поворот
                rotateRight(node.parent);
            } else {
                rotateRight(node.parent);     // Правый затем левый поворот
                rotateLeft(node.parent);
            }
        }
        root = node;  // Обновляем корень дерева
    }

    // Левый поворот вокруг узла x
    // Используется для балансировки дерева
    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;  // y - правый потомок x

        if (y != null) {
            x.right = y.left;          // Перемещаем левое поддерево y к x
            if (y.left != null) {
                y.left.parent = x;     // Обновляем родителя для левого поддерева y
            }
            y.parent = x.parent;       // y наследует родителя x
        }

        // Обновляем ссылку родителя x на y
        if (x.parent == null) {
            root = y;                  // Если x был корнем, y становится корнем
        } else if (x == x.parent.left) {
            x.parent.left = y;         // Если x был левым потомком
        } else {
            x.parent.right = y;        // Если x был правым потомком
        }

        if (y != null) {
            y.left = x;  // x становится левым потомком y
        }
        x.parent = y;     // y становится родителем x
    }

    // Правый поворот вокруг узла y
    // Используется для балансировки дерева
    private void rotateRight(SplayNode y) {
        SplayNode x = y.left;   // x - левый потомок y

        if (x != null) {
            y.left = x.right;          // Перемещаем правое поддерево x к y
            if (x.right != null) {
                x.right.parent = y;    // Обновляем родителя для правого поддерева x
            }
            x.parent = y.parent;       // x наследует родителя y
        }

        // Обновляем ссылку родителя y на x
        if (y.parent == null) {
            root = x;                  // Если y был корнем, x становится корнем
        } else if (y == y.parent.left) {
            y.parent.left = x;         // Если y был левым потомком
        } else {
            y.parent.right = x;        // Если y был правым потомком
        }

        if (x != null) {
            x.right = y;  // y становится правым потомком x
        }
        y.parent = x;     // x становится родителем y
    }

    @Override
    public String remove(Object key) {
        // Проверяем тип ключа
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        // Ищем узел для удаления
        SplayNode node = findNode(intKey);
        if (node == null) {
            return null;  // Узел не найден
        }

        String removedValue = node.value;  // Сохраняем значение для возврата

        // Поднимаем удаляемый узел к корню
        splay(node);

        // Случай 1: нет левого поддерева
        if (node.left == null) {
            root = node.right;         // Правое поддерево становится корнем
            if (root != null) {
                root.parent = null;    // Убираем ссылку на родителя
            }
        }
        // Случай 2: нет правого поддерева
        else if (node.right == null) {
            root = node.left;          // Левое поддерево становится корнем
            if (root != null) {
                root.parent = null;    // Убираем ссылку на родителя
            }
        }
        // Случай 3: есть оба поддерева
        else {
            // Находим максимальный узел в левом поддереве
            SplayNode maxLeft = findMax(node.left);
            // Поднимаем его к корню левого поддерева
            splay(maxLeft);
            // Присоединяем правое поддерево удаляемого узла
            maxLeft.right = node.right;
            if (node.right != null) {
                node.right.parent = maxLeft;  // Обновляем родителя
            }
            root = maxLeft;            // Новый корень
            root.parent = null;        // Убираем ссылку на родителя
        }

        size--;              // Уменьшаем счетчик размера
        return removedValue; // Возвращаем удаленное значение
    }

    @Override
    public String get(Object key) {
        // Проверяем тип ключа
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        // Ищем узел с заданным ключом
        SplayNode node = findNode(intKey);
        if (node != null) {
            splay(node);     // Поднимаем найденный узел к корню
            return node.value;
        }
        return null;  // Узел не найден
    }

    // Поиск узла по ключу (без splay)
    // Вспомогательный метод для внутреннего использования
    private SplayNode findNode(Integer key) {
        SplayNode current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;      // Идем влево
            } else if (cmp > 0) {
                current = current.right;     // Идем вправо
            } else {
                return current;  // Узел найден
            }
        }
        return null;  // Узел не найден
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;  // Используем get, который делает splay
    }

    @Override
    public boolean containsValue(Object value) {
        // Рекурсивный поиск значения по всему дереву
        return containsValueRecursive(root, value);
    }

    // Рекурсивный поиск значения
    // Обходит все узлы дерева в поиске указанного значения
    private boolean containsValueRecursive(SplayNode node, Object value) {
        if (node == null) {
            return false;  // Достигли конца ветки
        }

        // Проверяем текущий узел
        if ((value == null && node.value == null) ||
                (value != null && value.equals(node.value))) {
            return true;  // Значение найдено
        }

        // Рекурсивно ищем в левом и правом поддеревьях
        return containsValueRecursive(node.left, value) ||
                containsValueRecursive(node.right, value);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        // Создаем новую карту для результата
        MySplayMap result = new MySplayMap();
        // Рекурсивно добавляем ключи меньше toKey
        headMapRecursive(root, toKey, result);
        return result;
    }

    // Рекурсивное построение headMap
    // Добавляет все ключи, меньшие toKey, в результирующую карту
    private void headMapRecursive(SplayNode node, Integer toKey, MySplayMap result) {
        if (node == null) {
            return;  // Конец ветки
        }

        // Если ключ узла меньше toKey
        if (node.key.compareTo(toKey) < 0) {
            headMapRecursive(node.left, toKey, result);  // Обходим левое поддерево
            result.put(node.key, node.value);            // Добавляем текущий узел
            headMapRecursive(node.right, toKey, result); // Обходим правое поддерево
        } else {
            // Идем только в левое поддерево (там ключи меньше)
            headMapRecursive(node.left, toKey, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        // Создаем новую карту для результата
        MySplayMap result = new MySplayMap();
        // Рекурсивно добавляем ключи >= fromKey
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    // Рекурсивное построение tailMap
    // Добавляет все ключи, большие или равные fromKey, в результирующую карту
    private void tailMapRecursive(SplayNode node, Integer fromKey, MySplayMap result) {
        if (node == null) {
            return;  // Конец ветки
        }

        // Если ключ узла >= fromKey
        if (node.key.compareTo(fromKey) >= 0) {
            tailMapRecursive(node.left, fromKey, result);  // Обходим левое поддерево
            result.put(node.key, node.value);              // Добавляем текущий узел
            tailMapRecursive(node.right, fromKey, result); // Обходим правое поддерево
        } else {
            // Идем только в правое поддерево (там ключи больше)
            tailMapRecursive(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        // Находим минимальный ключ (самый левый узел)
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Map is empty");
        }
        // Находим максимальный ключ (самый правый узел)
        return findMax(root).key;
    }

    // Поиск узла с минимальным ключом
    // В бинарном дереве поиска минимальный ключ находится в самом левом узле
    private SplayNode findMin(SplayNode node) {
        while (node.left != null) {
            node = node.left;  // Идем до самого левого узла
        }
        return node;
    }

    // Поиск узла с максимальным ключом
    // В бинарном дереве поиска максимальный ключ находится в самом правом узле
    private SplayNode findMax(SplayNode node) {
        while (node.right != null) {
            node = node.right;  // Идем до самого правого узла
        }
        return node;
    }

    @Override
    public Integer lowerKey(Integer key) {
        // Находим наибольший ключ, меньший заданного
        SplayNode result = lowerKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    // Рекурсивный поиск lowerKey
    // Ищет наибольший ключ, который строго меньше заданного
    private SplayNode lowerKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;  // Возвращаем лучшего кандидата
        }

        if (node.key.compareTo(key) < 0) {
            // Текущий узел меньше ключа - обновляем кандидата и идем вправо
            candidate = node;
            return lowerKeyRecursive(node.right, key, candidate);
        } else {
            // Текущий узел >= ключа - идем влево
            return lowerKeyRecursive(node.left, key, candidate);
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        // Находим наибольший ключ, меньший или равный заданному
        SplayNode result = floorKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    // Рекурсивный поиск floorKey
    // Ищет наибольший ключ, который меньше или равен заданному
    private SplayNode floorKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;  // Возвращаем лучшего кандидата
        }

        if (node.key.compareTo(key) <= 0) {
            // Текущий узел <= ключа - обновляем кандидата и идем вправо
            candidate = node;
            return floorKeyRecursive(node.right, key, candidate);
        } else {
            // Текущий узел > ключа - идем влево
            return floorKeyRecursive(node.left, key, candidate);
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        // Находим наименьший ключ, больший или равный заданному
        SplayNode result = ceilingKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    // Рекурсивный поиск ceilingKey
    // Ищет наименьший ключ, который больше или равен заданному
    private SplayNode ceilingKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;  // Возвращаем лучшего кандидата
        }

        if (node.key.compareTo(key) >= 0) {
            // Текущий узел >= ключа - обновляем кандидата и идем влево
            candidate = node;
            return ceilingKeyRecursive(node.left, key, candidate);
        } else {
            // Текущий узел < ключа - идем вправо
            return ceilingKeyRecursive(node.right, key, candidate);
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        // Находим наименьший ключ, больший заданного
        SplayNode result = higherKeyRecursive(root, key, null);
        return result != null ? result.key : null;
    }

    // Рекурсивный поиск higherKey
    // Ищет наименьший ключ, который строго больше заданного
    private SplayNode higherKeyRecursive(SplayNode node, Integer key, SplayNode candidate) {
        if (node == null) {
            return candidate;  // Возвращаем лучшего кандидата
        }

        if (node.key.compareTo(key) > 0) {
            // Текущий узел > ключа - обновляем кандидата и идем влево
            candidate = node;
            return higherKeyRecursive(node.left, key, candidate);
        } else {
            // Текущий узел <= ключа - идем вправо
            return higherKeyRecursive(node.right, key, candidate);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";  // Пустая карта
        }

        // Строим строковое представление в формате {key1=value1, key2=value2}
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);  // Обход в порядке возрастания
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (левый-корень-правый)
    // Обеспечивает вывод ключей в отсортированном порядке
    private void inOrderTraversal(SplayNode node, StringBuilder sb) {
        if (node != null) {
            // Обходим левое поддерево
            if (node.left != null) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Обходим правое поддерево
            if (node.right != null) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    // ============================================================
    // Нереализованные методы интерфейса NavigableMap
    // Они выбрасывают исключение UnsupportedOperationException
    // ============================================================

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
    public java.util.Comparator<? super Integer> comparator() {
        return null;  // Используется естественный порядок сортировки
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}