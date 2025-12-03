package by.it.group451003.kharkevich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс для узла AVL-дерева
    static class AVLNode {
        int key;            // Ключ - целое число, по которому происходит сортировка
        String value;       // Значение - строка, связанная с ключом
        int height;         // Высота поддерева - для контроля баланса
        AVLNode left, right; // Ссылки на левого и правого потомка

        // Конструктор создает новый узел с заданными ключом и значением
        AVLNode(int key, String value) {
            this.key = key;         // Устанавливаем ключ
            this.value = value;     // Устанавливаем значение
            this.height = 1;        // Новый узел всегда имеет высоту 1
        }
    }

    AVLNode root; // Корень дерева - начальная точка для всех операций
    int size;     // Счетчик количества элементов в дереве

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

    // Преобразует дерево в строку в формате {key1=value1, key2=value2, ...}
    @Override
    public String toString() {
        if (root == null)
            return "{}"; // Если корня нет, возвращаем пустые фигурные скобки
        StringBuilder sb = new StringBuilder().append('{'); // Создаем StringBuilder с открывающей скобкой
        inOrderTraversal(root, sb); // Запускаем обход дерева для заполнения данными
        sb.replace(sb.length() - 2, sb.length(), "}"); // Заменяем последнюю запятую и пробел на закрывающую скобку
        return sb.toString(); // Возвращаем готовую строку
    }

    // Вспомогательный метод для обхода дерева в порядке возрастания (in-order traversal)
    private void inOrderTraversal(AVLNode node, StringBuilder sb) {
        if (node != null) { // Если текущий узел существует
            inOrderTraversal(node.left, sb);   // Рекурсивно обходим левое поддерево (меньшие ключи)
            sb.append(node.key).append("=").append(node.value).append(", "); // Добавляем текущий узел в строку
            inOrderTraversal(node.right, sb);  // Рекурсивно обходим правое поддерево (большие ключи)
        }
    }

    // Проверяет, содержится ли указанный ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return search((Integer) key, root) != null; // Если поиск вернул не null, ключ существует
    }

    // Рекурсивный поиск узла по ключу в дереве
    private AVLNode search(Integer key, AVLNode node) {
        if (node == null)
            return null; // Если дошли до null, ключ не найден
        int comparison = key.compareTo(node.key); // Сравниваем искомый ключ с ключом текущего узла
        if (comparison == 0)
            return node; // Ключи равны - нашли нужный узел

        // Рекурсивно ищем в левом или правом поддереве в зависимости от результата сравнения
        return search(key, comparison < 0 ? node.left : node.right);
    }

    // Возвращает значение, связанное с указанным ключом
    @Override
    public String get(Object key) {
        AVLNode result = search((Integer) key, root); // Ищем узел с заданным ключом
        return result == null ? null : result.value; // Возвращаем значение или null если узел не найден
    }

    // Добавляет пару ключ-значение в дерево или обновляет значение существующего ключа
    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key); // Получаем старое значение (если ключ уже существовал)
        root = put(root, key, value); // Рекурсивно добавляем/обновляем узел
        return oldValue; // Возвращаем старое значение (или null если ключа не было)
    }

    // Рекурсивный метод для вставки нового узла или обновления существующего
    private AVLNode put(AVLNode node, Integer key, String value) {
        if (node == null) {
            size++; // Увеличиваем счетчик элементов при добавлении нового узла
            return new AVLNode(key, value); // Создаем и возвращаем новый узел
        }

        int comparison = key.compareTo(node.key); // Сравниваем ключи
        if (comparison < 0)
            node.left = put(node.left, key, value); // Ключ меньше - рекурсивно вставляем в левое поддерево
        else if (comparison > 0)
            node.right = put(node.right, key, value); // Ключ больше - рекурсивно вставляем в правое поддерево
        else {
            // Ключ уже существует - обновляем значение
            if (!node.value.equals(value)) {
                node.value = value; // Обновляем значение только если оно изменилось
            }
            return node; // Возвращаем узел без балансировки (структура дерева не изменилась)
        }

        return balance(node); // Балансируем дерево после вставки и возвращаем результат
    }

    // Возвращает высоту узла (0 для null-узла)
    private int height(AVLNode node) {
        return node == null ? 0 : node.height; // Для существующего узла возвращаем его высоту, для null - 0
    }

    // Вычисляет фактор баланса узла (разность высот левого и правого поддеревьев)
    private int balanceFactor(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right); // Левое поддерево минус правое
    }

    // Выполняет правый поворот для балансировки дерева
    private AVLNode rotateRight(AVLNode node) {
        AVLNode newRoot = node.left;    // Левый потомок становится новым корнем поддерева
        node.left = newRoot.right;      // Правое поддерево нового корня становится левым поддеревом старого корня
        newRoot.right = node;           // Старый корень становится правым потомком нового корня

        // Обновляем высоты узлов после поворота
        node.height = 1 + Math.max(height(node.left), height(node.right)); // Пересчитываем высоту старого корня
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right)); // Пересчитываем высоту нового корня
        return newRoot; // Возвращаем новый корень поддерева
    }

    // Выполняет левый поворот для балансировки дерева
    private AVLNode rotateLeft(AVLNode node) {
        AVLNode newRoot = node.right;   // Правый потомок становится новым корнем поддерева
        node.right = newRoot.left;      // Левое поддерево нового корня становится правым поддеревом старого корня
        newRoot.left = node;            // Старый корень становится левым потомком нового корня

        // Обновляем высоты узлов после поворота
        node.height = 1 + Math.max(height(node.left), height(node.right)); // Пересчитываем высоту старого корня
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right)); // Пересчитываем высоту нового корня
        return newRoot; // Возвращаем новый корень поддерева
    }

    // Балансирует узел после операций вставки или удаления
    private AVLNode balance(AVLNode node) {
        if (node == null)
            return node; // Пустой узел не требует балансировки

        // Пересчитываем высоту текущего узла
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balanceFactor = balanceFactor(node); // Вычисляем текущий фактор баланса

        // Левый перевес (левое поддерево выше правого на 2 или более)
        if (balanceFactor > 1) {
            // Случай "левый-правый" - левый потомок имеет правое поддерево выше левого
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // Сначала выполняем левый поворот для левого потомка
            }
            return rotateRight(node); // Затем выполняем правый поворот для текущего узла
        }

        // Правый перевес (правое поддерево выше левого на 2 или более)
        if (balanceFactor < -1) {
            // Случай "правый-левый" - правый потомок имеет левое поддерево выше правого
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right); // Сначала выполняем правый поворот для правого потомка
            }
            return rotateLeft(node); // Затем выполняем левый поворот для текущего узла
        }

        return node; // Балансировка не требуется, возвращаем узел без изменений
    }

    // Удаляет узел с заданным ключом из дерева
    @Override
    public String remove(Object key) {
        String oldValue = get(key); // Сохраняем старое значение перед удалением
        if (oldValue != null) { // Если ключ существовал в дереве
            root = remove(root, (Integer) key); // Рекурсивно удаляем узел
            size--; // Уменьшаем счетчик элементов
        }
        return oldValue; // Возвращаем старое значение удаленного узла
    }

    // Рекурсивный метод для удаления узла с заданным ключом
    private AVLNode remove(AVLNode node, Integer key) {
        if (node == null)
            return null; // Ключ не найден, возвращаем null

        int comparison = key.compareTo(node.key); // Сравниваем ключи
        if (comparison < 0) {
            node.left = remove(node.left, key); // Ключ меньше - ищем в левом поддереве
        } else if (comparison > 0) {
            node.right = remove(node.right, key); // Ключ больше - ищем в правом поддереве
        } else {
            // Нашли узел для удаления

            // Узел с одним потомком или без потомков
            if (node.left == null) {
                return node.right; // Заменяем узел на его правое поддерево (может быть null)
            } else if (node.right == null) {
                return node.left;  // Заменяем узел на его левое поддерево
            } else {
                // Узел с двумя потомками - находим минимальный узел в правом поддереве
                AVLNode minNode = minValueNode(node.right);
                node.key = minNode.key;     // Заменяем ключ текущего узла на ключ минимального узла
                node.value = minNode.value; // Заменяем значение текущего узла на значение минимального узла
                node.right = removeMinNode(node.right); // Удаляем минимальный узел из правого поддерева
            }
        }

        return balance(node); // Балансируем дерево после удаления и возвращаем результат
    }

    // Удаляет узел с минимальным ключом в заданном поддереве
    private AVLNode removeMinNode(AVLNode node) {
        if (node.left == null) {
            return node.right; // Нашли минимальный узел - возвращаем его правое поддерево
        }
        node.left = removeMinNode(node.left); // Рекурсивно ищем минимальный узел в левом поддереве
        return balance(node); // Балансируем после удаления и возвращаем результат
    }

    // Находит узел с минимальным ключом в заданном поддереве
    private AVLNode minValueNode(AVLNode node) {
        return node.left == null ? node : minValueNode(node.left); // Идем влево пока возможно
    }

    // Полностью очищает дерево
    @Override
    public void clear() {
        root = null; // Обнуляем корень (вся структура дерева становится недоступной)
        size = 0;    // Сбрасываем счетчик размера в ноль
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Set<Integer> keySet() {
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
}