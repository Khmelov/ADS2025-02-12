package by.it.group451001.zabelich.lesson12;

import java.util.Map;

/**
 * Реализация интерфейса Map на основе АВЛ-дерева.
 * АВЛ-дерево - это сбалансированное бинарное дерево поиска,
 * где для каждой вершины высота ее двух поддеревьев различается не более чем на
 * 1.
 * Гарантирует время выполнения O(log n) для основных операций.
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Внутренний класс для представления узла АВЛ-дерева.
     * Каждый узел содержит ключ, значение, высоту поддерева и ссылки на потомков.
     */
    private static class AvlNode {
        Integer key; // Ключ узла
        String value; // Значение узла
        int height; // Высота поддерева с корнем в этом узле
        AvlNode left; // Левый потомок
        AvlNode right; // Правый потомок

        AvlNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новая вершина имеет высоту 1
        }
    }

    private AvlNode root; // Корень АВЛ-дерева
    private int size; // Количество элементов в карте

    // Конструктор
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    /**
     * Возвращает количество элементов в карте.
     * 
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли карта.
     * 
     * @return true если карта пуста, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Удаляет все элементы из карты.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Добавляет или обновляет пару ключ-значение в карте.
     * Если ключ уже существует, значение обновляется.
     * 
     * @param key   ключ для добавления
     * @param value значение, ассоциированное с ключом
     * @return предыдущее значение, ассоциированное с ключом, или null если ключа не
     *         было
     */
    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1]; // Массив для возврата старого значения
        root = putRecursive(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++; // Увеличиваем размер только если добавили новый ключ
        }
        return oldValue[0];
    }

    /**
     * Рекурсивный метод для вставки пары ключ-значение в АВЛ-дерево.
     * После вставки выполняется балансировка дерева.
     */
    private AvlNode putRecursive(AvlNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            // Достигли места для вставки - создаем новый узел
            return new AvlNode(key, value);
        }

        // Сравниваем ключи для определения направления обхода
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // Ключ меньше - идем в левое поддерево
            node.left = putRecursive(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            // Ключ больше - идем в правое поддерево
            node.right = putRecursive(node.right, key, value, oldValue);
        } else {
            // Ключ найден - обновляем значение
            oldValue[0] = node.value; // Сохраняем старое значение
            node.value = value; // Устанавливаем новое значение
            return node; // Возвращаем узел без балансировки
        }

        // Обновляем высоту текущего узла
        updateHeight(node);

        // Балансируем дерево
        return balance(node);
    }

    /**
     * Удаляет ключ и ассоциированное значение из карты.
     * Реализация метода remove(Object key) из интерфейса Map.
     * 
     * @param key ключ для удаления (может быть любого типа, но должен быть Integer)
     * @return значение, ассоциированное с удаленным ключом, или null если ключ не
     *         найден
     */
    @Override
    public String remove(Object key) {
        // Проверяем тип ключа - должен быть Integer
        if (!(key instanceof Integer)) {
            return null; // Неправильный тип ключа
        }
        Integer intKey = (Integer) key;
        String[] removedValue = new String[1]; // Массив для возврата удаленного значения
        root = removeRecursive(root, intKey, removedValue);
        if (removedValue[0] != null) {
            size--; // Уменьшаем размер только если удалили существующий ключ
        }
        return removedValue[0];
    }

    /**
     * Рекурсивный метод для удаления ключа из АВЛ-дерева.
     * После удаления выполняется балансировка дерева.
     */
    private AvlNode removeRecursive(AvlNode node, Integer key, String[] removedValue) {
        if (node == null) {
            // Ключ не найден
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // Ищем в левом поддереве
            node.left = removeRecursive(node.left, key, removedValue);
        } else if (cmp > 0) {
            // Ищем в правом поддереве
            node.right = removeRecursive(node.right, key, removedValue);
        } else {
            // Ключ найден - удаляем узел
            removedValue[0] = node.value; // Сохраняем значение для возврата

            // Узел имеет не более одного потомка
            if (node.left == null || node.right == null) {
                AvlNode temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    // Нет потомков
                    return null;
                } else {
                    // Один потомок
                    return temp;
                }
            } else {
                // Узел имеет двух потомков - находим минимальный элемент в правом поддереве
                AvlNode minNode = findMin(node.right);
                // Копируем данные минимального узла
                node.key = minNode.key;
                node.value = minNode.value;
                // Удаляем минимальный узел из правого поддерева
                node.right = removeRecursive(node.right, minNode.key, new String[1]);
            }
        }

        // Обновляем высоту текущего узла
        updateHeight(node);

        // Балансируем дерево
        return balance(node);
    }

    /**
     * Находит узел с минимальным ключом в поддереве.
     * 
     * @param node корень поддерева
     * @return узел с минимальным ключом
     */
    private AvlNode findMin(AvlNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Возвращает значение, ассоциированное с указанным ключом.
     * 
     * @param key ключ для поиска (может быть любого типа, но должен быть Integer)
     * @return значение, ассоциированное с ключом, или null если ключ не найден
     */
    @Override
    public String get(Object key) {
        // Проверяем тип ключа - должен быть Integer
        if (!(key instanceof Integer)) {
            return null; // Неправильный тип ключа
        }
        Integer intKey = (Integer) key;
        return getRecursive(root, intKey);
    }

    /**
     * Рекурсивный метод для поиска значения по ключу в АВЛ-дереве.
     */
    private String getRecursive(AvlNode node, Integer key) {
        if (node == null) {
            return null; // Ключ не найден
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // Ищем в левом поддереве
            return getRecursive(node.left, key);
        } else if (cmp > 0) {
            // Ищем в правом поддереве
            return getRecursive(node.right, key);
        } else {
            // Ключ найден
            return node.value;
        }
    }

    /**
     * Проверяет, содержится ли указанный ключ в карте.
     * 
     * @param key ключ для проверки (может быть любого типа, но должен быть Integer)
     * @return true если ключ содержится в карте, false в противном случае
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Возвращает строковое представление карты в порядке возрастания ключей.
     * Формат: {key1=value1, key2=value2, ...}
     * 
     * @return строковое представление карты
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Центрированный обход дерева (in-order traversal) для построения строкового
     * представления.
     * Обеспечивает вывод элементов в порядке возрастания ключей.
     */
    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            // Рекурсивно обходим левое поддерево
            if (node.left != null) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Рекурсивно обходим правое поддерево
            if (node.right != null) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    // Методы для балансировки АВЛ-дерева

    /**
     * Возвращает высоту узла (обрабатывает случай null).
     */
    private int height(AvlNode node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Обновляет высоту узла на основе высот его потомков.
     */
    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    /**
     * Вычисляет баланс-фактор узла (разность высот правого и левого поддеревьев).
     */
    private int getBalance(AvlNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    /**
     * Выполняет правый поворот вокруг указанного узла.
     * Используется когда левое поддерево слишком высокое.
     */
    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Выполняет левый поворот вокруг указанного узла.
     * Используется когда правое поддерево слишком высокое.
     */
    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Балансирует узел, выполняя необходимые повороты.
     */
    private AvlNode balance(AvlNode node) {
        if (node == null) {
            return null;
        }

        int balance = getBalance(node);

        // Left Left Case - правый поворот
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case - левый поворот + правый поворот
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case - левый поворот
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case - правый поворот + левый поворот
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Балансировка не требуется
        return node;
    }

    // Методы интерфейса Map, которые не реализуются в данной версии

    /**
     * Проверяет, содержится ли указанное значение в карте.
     * Не реализовано в данной версии.
     */
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue not implemented");
    }

    /**
     * Копирует все элементы из указанной карты в текущую.
     * Не реализовано в данной версии.
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not implemented");
    }

    /**
     * Возвращает множество ключей карты.
     * Не реализовано в данной версии.
     */
    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    /**
     * Возвращает коллекцию значений карты.
     * Не реализовано в данной версии.
     */
    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    /**
     * Возвращает множество пар ключ-значение карты.
     * Не реализовано в данной версии.
     */
    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    // Методы по умолчанию из интерфейса Map (не требуют переопределения)

    /**
     * Возвращает значение по ключу или значение по умолчанию если ключ не найден.
     * Не реализовано в данной версии.
     */
    @Override
    public String getOrDefault(Object key, String defaultValue) {
        throw new UnsupportedOperationException("getOrDefault not implemented");
    }

    /**
     * Добавляет пару ключ-значение только если ключ отсутствует.
     * Не реализовано в данной версии.
     */
    @Override
    public String putIfAbsent(Integer key, String value) {
        throw new UnsupportedOperationException("putIfAbsent not implemented");
    }

    /**
     * Удаляет пару ключ-значение только если ключ сопоставлен с указанным
     * значением.
     * Не реализовано в данной версии.
     */
    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("remove with two parameters not implemented");
    }

    /**
     * Заменяет значение для ключа только если ключ сопоставлен с указанным
     * значением.
     * Не реализовано в данной версии.
     */
    @Override
    public boolean replace(Integer key, String oldValue, String newValue) {
        throw new UnsupportedOperationException("replace with three parameters not implemented");
    }

    /**
     * Заменяет значение для ключа только если ключ присутствует.
     * Не реализовано в данной версии.
     */
    @Override
    public String replace(Integer key, String value) {
        throw new UnsupportedOperationException("replace with two parameters not implemented");
    }
}