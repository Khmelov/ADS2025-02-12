package by.it.group451001.kazakov.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {
    // Внутренний класс для узла AVL-дерева
    private static class Node {
        Integer key;        // Ключ узла
        String value;       // Значение узла
        Node left, right;   // Левый и правый потомки
        int height;         // Высота поддерева с корнем в этом узле

        // Конструктор узла
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  // Новая нода имеет высоту 1
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в дереве

    // Вспомогательные методы для работы с высотой и балансом

    // Возвращает высоту узла (для null возвращает 0)
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // Вычисляет баланс-фактор узла (разница высот левого и правого поддеревьев)
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Обновляет высоту узла на основе высот потомков
    private void updateHeight(Node node) {
        if (node != null) {
            // Высота = максимальная из высот потомков + 1
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    // Повороты для балансировки

    // Правый поворот вокруг узла y
    private Node rotateRight(Node y) {
        Node x = y.left;    // x - левый потомок y
        Node T2 = x.right;  // T2 - правое поддерево x

        x.right = y;        // y становится правым потомком x
        y.left = T2;        // T2 становится левым потомком y

        updateHeight(y);    // Обновляем высоту y (потомок изменился)
        updateHeight(x);    // Обновляем высоту x (стал родителем)

        return x;           // Возвращаем новый корень поддерева
    }

    // Левый поворот вокруг узла x
    private Node rotateLeft(Node x) {
        Node y = x.right;   // y - правый потомок x
        Node T2 = y.left;   // T2 - левое поддерево y

        y.left = x;         // x становится левым потомком y
        x.right = T2;       // T2 становится правым потомком x

        updateHeight(x);    // Обновляем высоту x (потомок изменился)
        updateHeight(y);    // Обновляем высоту y (стал родителем)

        return y;           // Возвращаем новый корень поддерева
    }

    // Балансировка узла
    private Node balance(Node node) {
        updateHeight(node);                    // Обновляем высоту текущего узла
        int balance = balanceFactor(node);     // Вычисляем баланс-фактор

        // Left-Left случай: перевес в левом поддереве левого потомка
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);          // Простой правый поворот
        }
        // Left-Right случай: перевес в правом поддереве левого потомка
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left); // Сначала левый поворот для левого потомка
            return rotateRight(node);          // Затем правый поворот для текущего узла
        }
        // Right-Right случай: перевес в правом поддереве правого потомка
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);           // Простой левый поворот
        }
        // Right-Left случай: перевес в левом поддереве правого потомка
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right); // Сначала правый поворот для правого потомка
            return rotateLeft(node);            // Затем левый поворот для текущего узла
        }

        return node;  // Балансировка не требуется
    }

    // Рекурсивная вставка узла
    private Node put(Node node, Integer key, String value) {
        // Достигли места вставки - создаем новый узел
        if (node == null) {
            size++;              // Увеличиваем счетчик размера
            return new Node(key, value);
        }

        // Рекурсивный поиск места для вставки
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);    // Идем в левое поддерево
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);  // Идем в правое поддерево
        } else {
            node.value = value;  // Ключ уже существует - обновляем значение
        }

        // Балансировка на обратном пути рекурсии
        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;      // Не поддерживаем null ключи

        String oldValue = get(key);        // Получаем старое значение (если есть)
        root = put(root, key, value);      // Рекурсивная вставка

        return oldValue;                   // Возвращаем старое значение или null
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node min(Node node) {
        while (node != null && node.left != null) {
            node = node.left;  // Идем до самого левого узла
        }
        return node;
    }

    // Рекурсивное удаление узла
    private Node remove(Node node, Integer key) {
        if (node == null) {
            return null;  // Узел не найден
        }

        // Поиск удаляемого узла
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);      // Ищем в левом поддереве
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);    // Ищем в правом поддереве
        } else {
            // Узел найден - выполняем удаление
            size--;  // Уменьшаем счетчик размера

            // Случай 1: у узла нет левого потомка
            if (node.left == null) {
                return node.right;  // Заменяем на правого потомка
            }
            // Случай 2: у узла нет правого потомка
            else if (node.right == null) {
                return node.left;   // Заменяем на левого потомка
            }
            // Случай 3: у узла есть оба потомка
            else {
                Node minNode = min(node.right);          // Находим минимальный в правом поддереве
                node.key = minNode.key;                  // Заменяем ключ
                node.value = minNode.value;              // Заменяем значение
                node.right = remove(node.right, minNode.key); // Удаляем минимальный узел
            }
        }

        // Балансировка на обратном пути рекурсии
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;  // Проверка типа ключа

        String oldValue = get((Integer) key);        // Получаем старое значение
        if (oldValue != null) {
            root = remove(root, (Integer) key);      // Рекурсивное удаление
        }
        return oldValue;                             // Возвращаем старое значение
    }

    // Рекурсивный поиск значения по ключу
    private String get(Node node, Integer key) {
        if (node == null) {
            return null;  // Ключ не найден
        }

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
        if (node == null) {
            return false;  // Ключ не найден
        }

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

    @Override
    public int size() {
        return size;  // Возвращает количество элементов
    }

    @Override
    public void clear() {
        root = null;  // Удаляем корень (сборщик мусора удалит остальные узлы)
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
            sb.append(", ");       // Добавляем разделитель (если уже есть элементы)

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

    // Неподдерживаемые методы интерфейса Map

    @Override
    public boolean containsValue(Object value) {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}