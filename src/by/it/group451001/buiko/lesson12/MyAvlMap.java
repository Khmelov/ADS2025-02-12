package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    // АВЛ-дерево - сбалансированное бинарное дерево поиска
    private static class Node {
        Integer key;      // Ключ - целое число
        String value;     // Значение - строка
        Node left;        // Левый потомок (меньшие ключи)
        Node right;       // Правый потомок (большие ключи)
        int height;       // Высота узла (для балансировки)

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новый узел всегда имеет высоту 1
        }
    }

    private Node root;    // Корень АВЛ-дерева
    private int size = 0; // Количество элементов в дереве

    // Возвращает высоту узла (для null возвращает 0)
    // Высота узла - длина самого длинного пути от этого узла до листа
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }


    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }


    private Node rotateRight(Node y) {
        Node x = y.left;   // x становится новым корнем
        Node T2 = x.right; // Поддерево, которое нужно переподвесить

        // Выполняем поворот
        x.right = y;
        y.left = T2;


        updateHeight(y);
        updateHeight(x);

        return x; // Возвращаем новый корень
    }

   /* MyAvlMap — сбалансированным бинарным деревом поиска, для каждого узла высота поддеревьев отличается не более чем на 1.*/
    private Node rotateLeft(Node x) {
        Node y = x.right;  // y становится новым корнем
        Node T2 = y.left;  // Поддерево, которое нужно переподвесить

        // Выполняем поворот
        y.left = x;
        x.right = T2;

         /*реализовала самобалансирующуюся структуру, где каждый узел хранит свою высоту, а методы rotateLeft() и rotateRight()
        выполняют повороты для поддержания баланса при операциях вставки и удаления.
        */
        updateHeight(x);
        updateHeight(y);

        return y; // Возвращаем новый корень
    }

    /*При добавлении элементов метод put() рекурсивно находит позицию для нового узла,
    а на обратном пути проверяет баланс-фактор и выполняет нужные повороты (LL, RR, LR, RL) для восстановления сбалансированности дерева.
     */
    private Node balance(Node node) {
        if (node == null) return null;

        updateHeight(node);
        int balance = getBalance(node);

        /*Для удаления элементов метод remove() обрабатывает три случая (узел без потомков, с одним потомком, с двумя потомками)
        и также балансирует дерево на обратном пути рекурсии, обеспечивая сохранение свойств АВЛ-дерева.
        */
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Право-правый случай (RR): перевес в правом поддереве правого потомка
        // Решение: левый поворот
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Лево-правый случай (LR): перевес в правом поддереве левого потомка
        // Решение: левый поворот левого потомка, затем правый поворот текущего узла
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Право-левый случай (RL): перевес в левом поддереве правого потомка
        // Решение: правый поворот правого потомка, затем левый поворот текущего узла
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Балансировка не требуется
        return node;
    }

    // Вспомогательный метод для вставки узла в дерево
    // Рекурсивно находит место для вставки и балансирует дерево на обратном пути
    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value); // Создаем новый узел
        }

        // Сравниваем ключи для определения направления обхода
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = put(node.left, key, value);  // Идем влево
        else if (cmp > 0)
            node.right = put(node.right, key, value); // Идем вправо
        else
            node.value = value; // Ключ существует - обновляем значение

        // Балансируем дерево на обратном пути рекурсии
        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key); // Сохраняем старое значение
        root = put(root, key, value); // Вставляем новый узел
        return oldValue; // Возвращаем старое значение
    }

    // Поиск узла с минимальным ключом в поддереве
    // В АВЛ-дереве минимальный ключ всегда в самом левом узле
    private Node findMin(Node node) {
        return node.left == null ? node : findMin(node.left);
    }

    // Удаление узла с минимальным ключом из поддерева
    private Node removeMin(Node node) {
        if (node.left == null) {
            size--;
            return node.right; // Правый потомок становится на место удаленного
        }
        node.left = removeMin(node.left);
        return balance(node); // Балансируем на обратном пути
    }

    // Вспомогательный метод для удаления узла
    private Node remove(Node node, Integer key) {
        if (node == null) return null; // Ключ не найден

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key); // Ищем в левом поддереве
        } else if (cmp > 0) {
            node.right = remove(node.right, key); // Ищем в правом поддереве
        } else {
            // Узел для удаления найден
            if (node.left == null || node.right == null) {
                // Случай 1: нет левого или правого потомка
                size--;
                return (node.left != null) ? node.left : node.right;
            } else {
                // Случай 2: есть оба потомка
                // Находим минимальный узел в правом поддереве (преемник)
                Node minNode = findMin(node.right);
                // Заменяем ключ и значение удаляемого узла
                node.key = minNode.key;
                node.value = minNode.value;
                // Удаляем найденный минимальный узел
                node.right = removeMin(node.right);
            }
        }
        return balance(node); // Балансируем дерево
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key); // Сохраняем старое значение
        if (oldValue != null) {
            root = remove(root, (Integer) key); // Удаляем узел
        }
        return oldValue; // Возвращаем старое значение
    }

    // Вспомогательный метод для поиска значения по ключу
    // Рекурсивный обход дерева
    private String get(Node node, Integer key) {
        if (node == null) return null; // Ключ не найден

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            return get(node.left, key);  // Ищем в левом поддереве
        else if (cmp > 0)
            return get(node.right, key); // Ищем в правом поддереве
        else
            return node.value; // Ключ найден
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательный метод для построения строки (ин-порядок обхода)
    // Обход: левое поддерево -> текущий узел -> правое поддерево
    // Дает отсортированный вывод ключей
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;

        toString(node.left, sb); // Сначала левое поддерево
        if (sb.length() > 1) sb.append(", "); // Добавляем разделитель
        sb.append(node.key).append('=').append(node.value); // Текущий узел
        toString(node.right, sb); // Затем правое поддерево
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        return sb.append("}").toString();
    }

    // Остальные методы интерфейса Map не реализованы
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

/*
* Основные принципы АВЛ-дерева:
1. Балансировка
Баланс-фактор: разница высот левого и правого поддеревьев

Допустимые значения: -1, 0, 1

Нарушение баланса: когда |баланс-фактор| > 1

2. Типы нарушений баланса
LL (Left-Left) случай:

Перевес в левом поддереве левого потомка

Решение: правый поворот

RR (Right-Right) случай:

Перевес в правом поддереве правого потомка

Решение: левый поворот

LR (Left-Right) случай:

Перевес в правом поддереве левого потомка

Решение: левый поворот + правый поворот

RL (Right-Left) случай:

Перевес в левом поддереве правого потомка

Решение: правый поворот + левый поворот

3. Преимущества АВЛ-деревьев
Гарантированная сбалансированность: высота = O(log n)

Эффективные операции: поиск, вставка, удаление за O(log n)

Предсказуемая производительность: нет вырождения в список

4. Сравнение с другими структурами
Против обычного BST: избегает вырождения в связанный список

Против красно-черных деревьев: более строгая балансировка, но больше поворотов

5. Временная сложность
Поиск: O(log n)

Вставка: O(log n)

Удаление: O(log n)

Балансировка: O(1) для каждого узла на пути
* */