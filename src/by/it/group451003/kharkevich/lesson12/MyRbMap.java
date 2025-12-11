package by.it.group451003.kharkevich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    Node root; // Корень красно-черного дерева - начальная точка доступа
    int size;  // Счетчик количества элементов в дереве

    // Перечисление для цветов узлов красно-черного дерева
    enum Color {
        RED,   // Красный цвет - используется для новых узлов
        BLACK  // Черный цвет - используется для корня и некоторых других узлов
    }

    // Внутренний класс для узла красно-черного дерева
    static class Node {
        Integer key;        // Ключ узла - целое число для сравнения и сортировки
        String value;       // Значение узла - строка, связанная с ключом
        Node parent, left, right; // Ссылки на родителя, левого и правого потомков
        Color color;        // Цвет узла - RED или BLACK

        // Конструктор создает новый узел с заданными цветом, ключом и значением
        public Node(Color color, Integer key, String value) {
            this.color = color; // Устанавливаем цвет узла
            this.key = key;     // Устанавливаем ключ узла
            this.value = value; // Устанавливаем значение узла
        }
    }

    // Преобразует дерево в строку в формате {key1=value1, key2=value2, ...}
    @Override
    public String toString() {
        if (root == null)
            return "{}"; // Если дерево пустое, возвращаем пустые фигурные скобки
        StringBuilder sb = new StringBuilder().append("{"); // Создаем StringBuilder с открывающей скобкой
        inOrderTraversal(root, sb); // Запускаем обход дерева для заполнения данными
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        sb.append("}"); // Добавляем закрывающую скобку
        return sb.toString(); // Возвращаем готовую строку
    }

    // Вспомогательный метод для обхода дерева в порядке возрастания
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) { // Если текущий узел существует
            inOrderTraversal(node.left, sb);   // Рекурсивно обходим левое поддерево (меньшие ключи)
            sb.append(node.key).append("=").append(node.value).append(", "); // Добавляем текущий узел в строку
            inOrderTraversal(node.right, sb);  // Рекурсивно обходим правое поддерево (большие ключи)
        }
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size; // Просто возвращаем значение счетчика size
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0; // Дерево пусто, если размер равен нулю
    }

    // Проверяет, содержится ли указанный ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return searchKey((Integer) key, root) != null; // Если поиск вернул не null, ключ существует
    }

    // Рекурсивный поиск узла по ключу в дереве
    private Node searchKey(Integer key, Node node) {
        if (node == null)
            return null; // Если дошли до null, ключ не найден
        int comparison = key.compareTo(node.key); // Сравниваем искомый ключ с ключом текущего узла
        if (comparison == 0)
            return node; // Ключи равны - нашли нужный узел

        // Рекурсивно ищем в левом или правом поддереве в зависимости от результата сравнения
        return searchKey(key, comparison < 0 ? node.left : node.right);
    }

    // Проверяет, содержится ли указанное значение в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value); // Запускаем рекурсивный поиск значения по всему дереву
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false; // Достигли конца ветки, значение не найдено
        }
        if (Objects.equals(value, node.value)) {
            return true; // Нашли узел с искомым значением
        }
        // Рекурсивно ищем в левом и/или правом поддереве
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Возвращает значение, связанное с указанным ключом
    @Override
    public String get(Object key) {
        Node node = searchKey((Integer) key, root); // Ищем узел с заданным ключом
        return (node != null) ? node.value : null; // Возвращаем значение или null если узел не найден
    }

    // Добавляет пару ключ-значение в дерево или обновляет значение существующего ключа
    @Override
    public String put(Integer key, String value) {
        String oldValue = null; // Переменная для хранения старого значения
        Node node = searchKey(key, root); // Ищем узел с заданным ключом
        if (node != null) {
            oldValue = node.value; // Сохраняем старое значение
            node.value = value;    // Обновляем значение узла
        } else {
            // Ключа нет в дереве - добавляем новый узел
            if (root == null) {
                root = new Node(Color.BLACK, key, value); // Создаем корень (всегда черный)
            } else {
                Node newNode = new Node(Color.RED, key, value); // Создаем новый узел (красный по умолчанию)
                Node current = root;
                // Находим правильную позицию для вставки нового узла
                while (true) {
                    newNode.parent = current; // Устанавливаем текущий узел как родителя нового
                    if (key.compareTo(current.key) < 0) {
                        if (current.left == null) {
                            current.left = newNode; // Вставляем новый узел как левого потомка
                            break; // Вставка завершена
                        } else {
                            current = current.left; // Переходим к левому потомку
                        }
                    } else if (key.compareTo(current.key) > 0) {
                        if (current.right == null) {
                            current.right = newNode; // Вставляем новый узел как правого потомка
                            break; // Вставка завершена
                        } else {
                            current = current.right; // Переходим к правому потомку
                        }
                    }
                }
                balanceAfterInsert(newNode); // Балансируем дерево после вставки нового узла
            }
            size++; // Увеличиваем счетчик элементов
        }
        return oldValue; // Возвращаем старое значение (или null если ключа не было)
    }

    // Балансирует дерево после вставки нового узла
    private void balanceAfterInsert(Node node) {
        // Пока не дошли до корня и нарушены свойства красно-черного дерева
        while (node != root && node.color == Color.RED && node.parent.color == Color.RED) {
            Node parent = node.parent; // Родитель текущего узла
            Node grandparent = parent.parent; // Дедушка текущего узла

            if (parent == grandparent.left) { // Родитель является левым потомком дедушки
                Node uncle = grandparent.right; // Дядя - правый потомок дедушки
                if (uncle != null && uncle.color == Color.RED) {
                    // Случай 1: дядя красный
                    parent.color = Color.BLACK;    // Перекрашиваем родителя в черный
                    uncle.color = Color.BLACK;     // Перекрашиваем дядю в черный
                    grandparent.color = Color.RED; // Перекрашиваем дедушку в красный
                    node = grandparent; // Продолжаем проверку с дедушки
                } else {
                    if (node == parent.right) {
                        // Случай 2: узел является правым потомком (треугольная конфигурация)
                        node = parent; // Перемещаемся к родителю
                        rotateLeft(node); // Выполняем левый поворот
                    }
                    // Случай 3: узел является левым потомком (линейная конфигурация)
                    parent.color = Color.BLACK;    // Перекрашиваем родителя в черный
                    grandparent.color = Color.RED; // Перекрашиваем дедушку в красный
                    rotateRight(grandparent); // Выполняем правый поворот
                }
            } else { // Родитель является правым потомком дедушки
                Node uncle = grandparent.left; // Дядя - левый потомок дедушки
                if (uncle != null && uncle.color == Color.RED) {
                    // Случай 1: дядя красный
                    parent.color = Color.BLACK;    // Перекрашиваем родителя в черный
                    uncle.color = Color.BLACK;     // Перекрашиваем дядю в черный
                    grandparent.color = Color.RED; // Перекрашиваем дедушку в красный
                    node = grandparent; // Продолжаем проверку с дедушки
                } else {
                    if (node == parent.left) {
                        // Случай 2: узел является левым потомком (треугольная конфигурация)
                        node = parent; // Перемещаемся к родителю
                        rotateRight(node); // Выполняем правый поворот
                    }
                    // Случай 3: узел является правым потомком (линейная конфигурация)
                    parent.color = Color.BLACK;    // Перекрашиваем родителя в черный
                    grandparent.color = Color.RED; // Перекрашиваем дедушку в красный
                    rotateLeft(grandparent); // Выполняем левый поворот
                }
            }
        }

        root.color = Color.BLACK; // Корень всегда должен быть черным
    }

    // Находит преемника (узел с наименьшим ключом в правом поддереве)
    private Node getSuccessor(Node node) {
        Node successor = node.right; // Начинаем с правого потомка
        while (successor != null && successor.left != null) {
            successor = successor.left; // Идем влево пока возможно
        }
        return successor; // Возвращаем преемника
    }

    // Удаляет указанный узел из дерева
    private void deleteNode(Node node) {
        Node replacement; // Узел для замены удаляемого узла

        // Если у удаляемого узла два потомка
        if (node.left != null && node.right != null) {
            Node successor = getSuccessor(node); // Находим преемника (минимальный в правом поддереве)
            node.key = successor.key;     // Заменяем ключ удаляемого узла на ключ преемника
            node.value = successor.value; // Заменяем значение удаляемого узла на значение преемника
            node = successor; // Теперь удаляем преемника вместо исходного узла
        }

        // Определяем узел для замены (потомок удаляемого узла)
        replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            // Есть потомок для замены
            replacement.parent = node.parent; // Обновляем родителя у потомка
            if (node.parent == null) {
                root = replacement; // Если удаляли корень, потомок становится корнем
            } else if (node == node.parent.left) {
                node.parent.left = replacement; // Заменяем левого потомка у родителя
            } else {
                node.parent.right = replacement; // Заменяем правого потомка у родителя
            }

            // Если удаляемый узел был черным, балансируем дерево
            if (node.color == Color.BLACK) {
                balanceDeletion(replacement); // Балансировка после удаления
            }
        } else if (node.parent == null) {
            // Удаляем корень без потомков
            root = null; // Дерево становится пустым
        } else {
            // Узел без потомков
            if (node.color == Color.BLACK) {
                balanceDeletion(node); // Балансируем перед удалением
            }

            // Удаляем ссылку на узел у родителя
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null; // Удаляем ссылку на левого потомка
                } else if (node == node.parent.right) {
                    node.parent.right = null; // Удаляем ссылку на правого потомка
                }
                node.parent = null; // Обнуляем ссылку на родителя
            }
        }
    }

    // Возвращает цвет узла (для null-узлов возвращает BLACK)
    private Color getColor(Node node) {
        return (node == null) ? Color.BLACK : node.color; // Null-узлы считаются черными
    }

    // Устанавливает цвет узла
    private void setColor(Node node, Color color) {
        if (node != null) {
            node.color = color; // Устанавливаем цвет только для существующих узлов
        }
    }

    // Выполняет левый поворот вокруг указанного узла
    private void rotateLeft(Node node) {
        Node right = node.right;   // Правый потомок станет новым корнем поддерева
        node.right = right.left;   // Левое поддерево правого потомка становится правым поддеревом узла
        if (right.left != null)
            right.left.parent = node; // Обновляем родителя у перемещаемого поддерева
        right.parent = node.parent;   // Обновляем родителя у нового корня
        if (node.parent == null)
            root = right;             // Если узел был корнем, новый корень становится корнем дерева
        else if (node == node.parent.left)
            node.parent.left = right; // Обновляем левого потомка у родителя
        else
            node.parent.right = right; // Обновляем правого потомка у родителя
        right.left = node;          // Узел становится левым потомком нового корня
        node.parent = right;        // Обновляем родителя у узла
    }

    // Выполняет правый поворот вокруг указанного узла
    private void rotateRight(Node node) {
        Node left = node.left;     // Левый потомок станет новым корнем поддерева
        node.left = left.right;    // Правое поддерево левого потомка становится левым поддеревом узла
        if (left.right != null)
            left.right.parent = node; // Обновляем родителя у перемещаемого поддерева
        left.parent = node.parent;    // Обновляем родителя у нового корня
        if (node.parent == null)
            root = left;              // Если узел был корнем, новый корень становится корнем дерева
        else if (node == node.parent.right)
            node.parent.right = left; // Обновляем правого потомка у родителя
        else
            node.parent.left = left;  // Обновляем левого потомка у родителя
        left.right = node;         // Узел становится правым потомком нового корня
        node.parent = left;        // Обновляем родителя у узла
    }

    // Балансирует дерево после удаления узла
    private void balanceDeletion(Node node) {
        while (node != root && getColor(node) == Color.BLACK) {
            if (node == node.parent.left) { // Узел является левым потомком
                Node sibling = node.parent.right; // Брат - правый потомок родителя
                if (getColor(sibling) == Color.RED) {
                    // Случай 1: брат красный
                    setColor(sibling, Color.BLACK);     // Перекрашиваем брата в черный
                    setColor(node.parent, Color.RED);   // Перекрашиваем родителя в красный
                    rotateLeft(node.parent);            // Левый поворот вокруг родителя
                    sibling = node.parent.right;        // Обновляем ссылку на брата
                }
                if (getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.BLACK) {
                    // Случай 2: оба потомка брата черные
                    setColor(sibling, Color.RED); // Перекрашиваем брата в красный
                    node = node.parent;           // Переходим к родителю для дальнейшей балансировки
                } else {
                    if (getColor(sibling.right) == Color.BLACK) {
                        // Случай 3: правый потомок брата черный, левый - красный
                        setColor(sibling.left, Color.BLACK); // Перекрашиваем левого потомка брата в черный
                        setColor(sibling, Color.RED);        // Перекрашиваем брата в красный
                        rotateRight(sibling);                // Правый поворот вокруг брата
                        sibling = node.parent.right;         // Обновляем ссылку на брата
                    }
                    // Случай 4: правый потомок брата красный
                    setColor(sibling, getColor(node.parent)); // Брат получает цвет родителя
                    setColor(node.parent, Color.BLACK);       // Перекрашиваем родителя в черный
                    setColor(sibling.right, Color.BLACK);     // Перекрашиваем правого потомка брата в черный
                    rotateLeft(node.parent);                  // Левый поворот вокруг родителя
                    node = root;                              // Завершаем балансировку
                }
            } else { // Узел является правым потомком (симметрично)
                Node sibling = node.parent.left; // Брат - левый потомок родителя
                if (getColor(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(node.parent, Color.RED);
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (getColor(sibling.right) == Color.BLACK && getColor(sibling.left) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = node.parent;
                } else {
                    if (getColor(sibling.left) == Color.BLACK) {
                        setColor(sibling.right, Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    setColor(sibling, getColor(node.parent));
                    setColor(node.parent, Color.BLACK);
                    setColor(sibling.left, Color.BLACK);
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }

        setColor(node, Color.BLACK); // Окрашиваем узел в черный цвет
    }

    // Удаляет узел с заданным ключом из дерева
    @Override
    public String remove(Object key) {
        Node node = searchKey((Integer) key, root); // Ищем узел с заданным ключом
        if (node != null) {
            String oldValue = node.value; // Сохраняем старое значение
            deleteNode(node);             // Удаляем узел из дерева
            size--;                       // Уменьшаем счетчик элементов
            return oldValue;              // Возвращаем старое значение
        }
        return null; // Ключ не найден, возвращаем null
    }

    // Полностью очищает дерево
    @Override
    public void clear() {
        root = null; // Обнуляем корень (вся структура дерева становится недоступной)
        size = 0;    // Сбрасываем счетчик размера в ноль
    }

    // Возвращает первый (наименьший) ключ в дереве
    @Override
    public Integer firstKey() {
        Node first = firstNode(root); // Находим узел с наименьшим ключом
        return (first != null) ? first.key : null; // Возвращаем ключ или null если дерево пустое
    }

    // Находит узел с наименьшим ключом в поддереве
    private Node firstNode(Node node) {
        if (node == null) return null; // Дерево пустое
        while (node.left != null) {
            node = node.left; // Идем влево пока возможно
        }
        return node; // Возвращаем узел с наименьшим ключом
    }

    // Возвращает последний (наибольший) ключ в дереве
    @Override
    public Integer lastKey() {
        Node last = lastNode(root); // Находим узел с наибольшим ключом
        return (last != null) ? last.key : null; // Возвращаем ключ или null если дерево пустое
    }

    // Находит узел с наибольшим ключом в поддереве
    private Node lastNode(Node node) {
        if (node == null) return null; // Дерево пустое
        while (node.right != null) {
            node = node.right; // Идем вправо пока возможно
        }
        return node; // Возвращаем узел с наибольшим ключом
    }

    // Возвращает подкарту с ключами меньше указанного ключа
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap(); // Создаем новую карту для результата
        headMap(root, toKey, subMap);   // Заполняем ее подходящими ключами
        return subMap; // Возвращаем подкарту
    }

    // Рекурсивно заполняет подкарту ключами меньше toKey
    private void headMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) {
            return; // Конец ветки
        }

        headMap(node.left, toKey, subMap); // Обходим левое поддерево

        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value); // Добавляем подходящий ключ в подкарту
            headMap(node.right, toKey, subMap); // Обходим правое поддерево
        }
    }

    // Возвращает подкарту с ключами больше или равными указанному ключу
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap(); // Создаем новую карту для результата
        tailMap(root, fromKey, subMap); // Заполняем ее подходящими ключами
        return subMap; // Возвращаем подкарту
    }

    // Рекурсивно заполняет подкарту ключами больше или равными fromKey
    private void tailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) {
            return; // Конец ветки
        }

        if (node.key.compareTo(fromKey) >= 0) {
            tailMap(node.left, fromKey, subMap); // Обходим левое поддерево
            subMap.put(node.key, node.value);    // Добавляем подходящий ключ в подкарту
        }

        tailMap(node.right, fromKey, subMap); // Обходим правое поддерево
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок сортировки ключей
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }
}