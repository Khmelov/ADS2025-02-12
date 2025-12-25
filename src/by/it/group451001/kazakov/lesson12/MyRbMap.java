package by.it.group451001.kazakov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    // Внутренний класс для узла красно-черного дерева
    private static class Node {
        Integer key;        // Ключ узла
        String value;       // Значение узла
        Node left, right, parent;  // Левый, правый потомки и родитель
        boolean isRed;      // Цвет узла: true - красный, false - черный

        // Конструктор узла (новые узлы всегда красные)
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.isRed = true;  // По умолчанию новый узел красный
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в дереве

    // Проверяет, является ли узел красным (null узлы считаются черными)
    private boolean isRed(Node node) {
        return node != null && node.isRed;
    }

    // Левый поворот вокруг узла h
    private Node rotateLeft(Node h) {
        if (h == null || h.right == null) return h;  // Проверка возможности поворота

        Node x = h.right;    // x - правый потомок h
        h.right = x.left;    // Перемещаем левое поддерево x к h

        if (x.left != null)
            x.left.parent = h;  // Обновляем родителя для левого поддерева x

        x.left = h;          // h становится левым потомком x
        x.parent = h.parent; // x наследует родителя h
        h.parent = x;        // x становится родителем h

        x.isRed = h.isRed;   // x получает цвет h
        h.isRed = true;      // h становится красным

        if (h == root)
            root = x;        // Обновляем корень если нужно

        return x;            // Возвращаем новый корень поддерева
    }

    // Правый поворот вокруг узла h
    private Node rotateRight(Node h) {
        if (h == null || h.left == null) return h;  // Проверка возможности поворота

        Node x = h.left;     // x - левый потомок h
        h.left = x.right;    // Перемещаем правое поддерево x к h

        if (x.right != null)
            x.right.parent = h;  // Обновляем родителя для правого поддерева x

        x.right = h;         // h становится правым потомком x
        x.parent = h.parent; // x наследует родителя h
        h.parent = x;        // x становится родителем h

        x.isRed = h.isRed;   // x получает цвет h
        h.isRed = true;      // h становится красным

        if (h == root)
            root = x;        // Обновляем корень если нужно

        return x;            // Возвращаем новый корень поддерева
    }

    // Смена цветов узла и его потомков (инверсия)
    private void flipColors(Node h) {
        if (h == null || h.left == null || h.right == null) return;

        h.isRed = !h.isRed;           // Инвертируем цвет текущего узла
        h.left.isRed = !h.left.isRed; // Инвертируем цвет левого потомка
        h.right.isRed = !h.right.isRed; // Инвертируем цвет правого потомка
    }

    // Балансировка дерева после вставки
    private Node fixUp(Node node) {
        if (node == null) return node;

        // Случай 1: правый потомок красный, левый - черный -> левый поворот
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }

        // Случай 2: левый потомок и его левый потомок красные -> правый поворот
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }

        // Случай 3: оба потомка красные -> смена цветов
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;  // Возвращаем сбалансированный узел
    }

    // Рекурсивная вставка узла в дерево
    private Node put(Node node, Integer key, String value, Node parent) {
        // Достигли места вставки - создаем новый узел
        if (node == null) {
            size++;                    // Увеличиваем счетчик размера
            Node newNode = new Node(key, value);
            newNode.parent = parent;   // Устанавливаем родителя
            return newNode;            // Возвращаем новый узел
        }

        // Рекурсивный поиск места для вставки
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value, node);  // Идем влево
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value, node); // Идем вправо
        } else {
            node.value = value;  // Ключ существует - обновляем значение
        }

        // Балансировка на обратном пути рекурсии
        return fixUp(node);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;  // Не поддерживаем null ключи

        String oldValue = get(key);    // Получаем старое значение
        root = put(root, key, value, null);  // Рекурсивная вставка

        if (root != null)
            root.isRed = false;  // Корень всегда черный

        return oldValue;  // Возвращаем старое значение
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node min(Node node) {
        while (node != null && node.left != null) {
            node = node.left;  // Идем до самого левого узла
        }
        return node;
    }

    // Подготовка к удалению: делаем левого потомка красным
    private Node moveRedLeft(Node h) {
        if (h == null) return h;

        flipColors(h);  // Смена цветов

        // Если у правого потомка есть красный левый потомок
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);  // Правый поворот правого потомка
            h = rotateLeft(h);               // Левый поворот текущего узла
            flipColors(h);                   // Смена цветов обратно
        }

        return h;
    }

    // Подготовка к удалению: делаем правого потомка красным
    private Node moveRedRight(Node h) {
        if (h == null) return h;

        flipColors(h);  // Смена цветов

        // Если у левого потомка есть красный левый потомок
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);  // Правый поворот
            flipColors(h);       // Смена цветов обратно
        }

        return h;
    }

    // Балансировка дерева после удаления
    private Node fixUpAfterDelete(Node node) {
        if (node == null) return null;

        // Балансировка аналогичная fixUp, но с дополнительными проверками
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && node.left.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    // Удаление узла с минимальным ключом из поддерева
    private Node deleteMin(Node node) {
        if (node == null) return null;

        // Нашли минимальный узел - удаляем его
        if (node.left == null) {
            size--;  // Уменьшаем счетчик размера
            return null;
        }

        // Если левый потомок черный и его левый потомок тоже черный
        if (!isRed(node.left) && (node.left.left == null || !isRed(node.left.left))) {
            node = moveRedLeft(node);  // Делаем левого потомка красным
        }

        node.left = deleteMin(node.left);  // Рекурсивно удаляем минимальный

        return fixUpAfterDelete(node);  // Балансируем дерево
    }

    // Рекурсивное удаление узла по ключу
    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        // Поиск удаляемого узла в левом поддереве
        if (key.compareTo(node.key) < 0) {
            // Если нужно, делаем левого потомка красным
            if (node.left != null && !isRed(node.left) &&
                    (node.left.left == null || !isRed(node.left.left))) {
                node = moveRedLeft(node);
            }
            node.left = remove(node.left, key);  // Рекурсивный вызов для левого поддерева
        }
        // Поиск в правом поддереве или найден узел для удаления
        else {
            // Если левый потомок красный - правый поворот
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            // Найден узел для удаления и у него нет правого потомка
            if (key.compareTo(node.key) == 0 && node.right == null) {
                size--;  // Уменьшаем счетчик размера
                return null;
            }

            // Если нужно, делаем правого потомка красным
            if (node.right != null && !isRed(node.right) &&
                    (node.right.left == null || !isRed(node.right.left))) {
                node = moveRedRight(node);
            }

            // Найден узел для удаления
            if (key.compareTo(node.key) == 0) {
                Node minNode = min(node.right);          // Находим минимальный в правом поддереве
                node.key = minNode.key;                  // Заменяем ключ
                node.value = minNode.value;              // Заменяем значение
                node.right = deleteMin(node.right);      // Удаляем минимальный узел из правого поддерева
            } else {
                node.right = remove(node.right, key);    // Рекурсивный вызов для правого поддерева
            }
        }

        return fixUpAfterDelete(node);  // Балансируем дерево после удаления
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;  // Проверка типа ключа

        String oldValue = get((Integer) key);        // Получаем старое значение
        if (oldValue != null) {
            root = remove(root, (Integer) key);      // Рекурсивное удаление
            if (root != null)
                root.isRed = false;                  // Корень всегда черный
        }
        return oldValue;  // Возвращаем старое значение
    }

    // Рекурсивный поиск значения по ключу
    private String get(Node node, Integer key) {
        if (node == null) return null;  // Ключ не найден

        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);    // Ищем в левом поддереве
        } else if (key.compareTo(node.key) > 0) {
            return get(node.right, key);   // Ищем в правом поддереве
        } else {
            return node.value;             // Ключ найден - возвращаем значение
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;  // Проверка типа ключа
        return get(root, (Integer) key);             // Рекурсивный поиск
    }

    // Рекурсивная проверка наличия ключа
    private boolean containsKey(Node node, Integer key) {
        if (node == null) return false;  // Ключ не найден

        if (key.compareTo(node.key) < 0) {
            return containsKey(node.left, key);    // Ищем в левом поддереве
        } else if (key.compareTo(node.key) > 0) {
            return containsKey(node.right, key);   // Ищем в правом поддереве
        } else {
            return true;                           // Ключ найден
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;  // Проверка типа ключа
        return containsKey(root, (Integer) key);      // Рекурсивная проверка
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValue(Node node, String value) {
        if (node == null) return false;  // Достигли конца ветки

        // Проверяем значение в текущем узле
        if (node.value == null ? value == null : node.value.equals(value)) {
            return true;  // Значение найдено
        }

        // Рекурсивно ищем в левом и правом поддеревьях
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;  // Проверка типа значения
        return containsValue(root, (String) value);    // Рекурсивный поиск
    }

    @Override
    public int size() {
        return size;  // Возвращает количество элементов
    }

    @Override
    public void clear() {
        root = null;  // Удаляем корень
        size = 0;     // Сбрасываем счетчик размера
    }

    @Override
    public boolean isEmpty() {
        return size == 0;  // Проверяем пустоту дерева
    }

    // Рекурсивное построение строкового представления (симметричный обход)
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;  // Конец ветки

        toString(node.left, sb);   // Обходим левое поддерево

        if (sb.length() > 1)
            sb.append(", ");       // Добавляем разделитель

        sb.append(node.key).append("=").append(node.value);  // Добавляем пару ключ-значение

        toString(node.right, sb);  // Обходим правое поддерево
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);        // Рекурсивное построение содержимого
        sb.append("}");
        return sb.toString();      // Возвращаем строковое представление
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;  // Используется естественный порядок сортировки
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();  // Метод не реализован
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();  // Создаем новую карту для результата
        headMap(root, toKey, result);    // Рекурсивно заполняем
        return result;
    }

    // Рекурсивное построение headMap (ключи < toKey)
    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;  // Конец ветки

        if (node.key.compareTo(toKey) < 0) {
            // Ключ подходит - добавляем и обходим оба поддерева
            result.put(node.key, node.value);
            headMap(node.left, toKey, result);
            headMap(node.right, toKey, result);
        } else {
            // Ключ слишком большой - идем только влево
            headMap(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();    // Создаем новую карту для результата
        tailMap(root, fromKey, result);    // Рекурсивно заполняем
        return result;
    }

    // Рекурсивное построение tailMap (ключи >= fromKey)
    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;  // Конец ветки

        if (node.key.compareTo(fromKey) >= 0) {
            // Ключ подходит - добавляем и обходим оба поддерева
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, result);
            tailMap(node.right, fromKey, result);
        } else {
            // Ключ слишком маленький - идем только вправо
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null)
            throw new NoSuchElementException();  // Дерево пустое

        Node node = min(root);  // Находим узел с минимальным ключом
        return node.key;        // Возвращаем ключ
    }

    @Override
    public Integer lastKey() {
        if (root == null)
            throw new NoSuchElementException();  // Дерево пустое

        Node node = root;
        while (node.right != null) {
            node = node.right;  // Идем до самого правого узла
        }
        return node.key;        // Возвращаем ключ
    }

    // Ниже следуют нереализованные методы интерфейса SortedMap
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}