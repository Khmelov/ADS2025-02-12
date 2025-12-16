package by.it.group410902.barbashova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Внутренний класс Node для представления узла дерева
    class Node {
        E data;
        Node left;
        Node right;

        Node(E data) {
            this.data = data;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(_root, sb);  // Обход дерева в порядке возрастания
        return sb.append("]").toString();
    }

    // Рекурсивный обход дерева в порядке "левый-корень-правый"
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;  // Базовый случай рекурсии

        // Рекурсивный обход левого поддерева (меньшие элементы)
        inOrderTraversal(node.left, sb);

        // Добавление текущего элемента в строку
        if (sb.length() > 1)  // Если уже есть элементы, добавляем разделитель
            sb.append(", ");
        sb.append(node.data);

        // Рекурсивный обход правого поддерева (большие элементы)
        inOrderTraversal(node.right, sb);
    }

    Node _root;  // Корень бинарного дерева поиска
    int _count;  // Количество элементов в множестве

    @Override
    public int size() {
        return _count;  // Возвращает количество элементов
    }

    @Override
    public boolean isEmpty() {
        return _count == 0;  // Проверяет пустое ли множество
    }

    // Рекурсивный поиск элемента в дереве
    boolean contains(Node node, E element) {
        if (node == null) return false;  // Элемент не найден

        int compare = element.compareTo(node.data);  // Сравнение элементов

        if (compare < 0)
            return contains(node.left, element);   // Ищем в левом поддереве
        else if (compare > 0)
            return contains(node.right, element);  // Ищем в правом поддереве
        else
            return true;  // Элемент найден
    }

    @Override
    public boolean contains(Object o) {
        return contains(_root, (E) o);  // Запуск рекурсивного поиска от корня
    }

    // Рекурсивная вставка элемента в дерево
    Node insert(Node node, E element) {
        if (node == null)
            return new Node(element);  // Создаем новый узел если достигли листа

        int compare = element.compareTo(node.data);  // Сравнение элементов

        if (compare < 0)
            node.left = insert(node.left, element);   // Вставляем в левое поддерево
        else if (compare > 0)
            node.right = insert(node.right, element); // Вставляем в правое поддерево
        // Если compare == 0, элемент уже существует - ничего не делаем

        return node;  // Возвращаем текущий узел
    }

    @Override
    public boolean add(E e) {
        if (!contains(e)) {  // Проверяем что элемента еще нет
            _root = insert(_root, e);  // Рекурсивная вставка
            _count++;  // Увеличиваем счетчик
            return true;
        }
        return false;  // Элемент уже существует
    }

    // Поиск минимального элемента в поддереве
    // В бинарном дереве поиска минимальный элемент - самый левый
    Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;  // Двигаемся влево пока возможно
        }
        return node;
    }

    // Рекурсивное удаление элемента из дерева
    Node delete(Node node, E element) {
        if (node == null) return null;  // Элемент не найден

        int compare = element.compareTo(node.data);  // Сравнение элементов

        if (compare < 0) {
            node.left = delete(node.left, element);   // Ищем в левом поддереве
        } else if (compare > 0) {
            node.right = delete(node.right, element); // Ищем в правом поддереве
        } else {
            // Элемент найден - рассматриваем три случая:

            // Случай 1: У узла нет левого потомка
            if (node.left == null) {
                return node.right;  // Заменяем узел на правого потомка
            }
            // Случай 2: У узла нет правого потомка
            else if (node.right == null) {
                return node.left;   // Заменяем узел на левого потомка
            }
            // Случай 3: У узла есть оба потомка
            else {
                // Находим минимальный элемент в правом поддереве
                Node minNode = findMin(node.right);
                // Заменяем данные текущего узла на минимальные из правого поддерева
                node.data = minNode.data;
                // Удаляем минимальный элемент из правого поддерева
                node.right = delete(node.right, minNode.data);
            }
        }
        return node;  // Возвращаем текущий узел
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {  // Проверяем что элемент существует
            _root = delete(_root, (E) o);  // Рекурсивное удаление
            _count--;  // Уменьшаем счетчик
            return true;
        }
        return false;  // Элемент не найден
    }

    // Методы для работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции содержатся в множестве
        for (Object obj: c) {
            if (!contains(obj))
                return false;  // Если хотя бы один элемент не найден
        }
        return true;  // Все элементы найдены
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы из коллекции
        boolean isModified = false;
        for (E element: c) {
            if (add(element)) {
                isModified = true;  // Отмечаем что хотя бы один элемент был добавлен
            }
        }
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оставляем только элементы, которые содержатся в коллекции c
        if (c.isEmpty()) {
            this.clear();  // Если коллекция пуста, очищаем всё множество
            return true;
        }

        boolean isModified = false;
        // Создаем временное множество для хранения пересечения
        MyTreeSet<E> retainSet = new MyTreeSet<>();

        for (Object obj : c) {
            if (contains(obj)) {
                // Если элемент есть в коллекции c и в текущем множестве, добавляем
                retainSet.add((E) obj);
                isModified = true;
            }
        }

        // Заменяем текущее дерево временным
        _root = retainSet._root;
        _count = retainSet._count;

        return isModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы коллекции из множества
        boolean isModified = false;
        for (Object obj: c) {
            if (remove(obj)) {
                isModified = true;  // Отмечаем что хотя бы один элемент был удален
            }
        }
        return isModified;
    }

    @Override
    public void clear() {
        _root = null;   // Удаляем ссылку на корень (дерево будет собрано GC)
        _count = 0;     // Сбрасываем счетчик
    }


    @Override
    public Iterator<E> iterator() {
        return null;  // Итератор для обхода элементов в отсортированном порядке
    }

    @Override
    public Object[] toArray() {
        return new Object[0];  // Преобразование в массив Object[]
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;  // Преобразование в массив типа T
    }
}