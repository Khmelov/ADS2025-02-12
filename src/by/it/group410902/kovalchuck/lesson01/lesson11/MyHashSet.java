package by.it.group410902.kovalchuck.lesson01.lesson11;

import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;    // Начальный размер массива
    private static final double LOAD_FACTOR = 0.75;    // Коэффициент заполнения для рехеширования

    private Node<E>[] table;  // Массив узлов
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Внутренний класс для узлов односвязного списка
    private static class Node<E> {
        E data;
        Node<E> next;  // Ссылка на следующий узел в цепочке

        Node(E data) {
            this.data = data;
        }
    }

    // Возвращаем количество элементов
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;  // Удаляем
        }
        size = 0;  // Сбрасываем счетчик
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, нет ли уже такого элемента
        if (contains(element)) {
            return false;  // Элемент уже есть - не добавляем
        }

        // Проверяем необходимость увеличения таблицы
        if (size >= table.length * LOAD_FACTOR) {
            resize();  // Увеличиваем таблицу если заполнена на 75%
        }

        // Вычисляем индекс в массиве по хеш-коду
        int index = getIndex(element);
        Node<E> newNode = new Node<>(element);  // Создаем новый узел

        // Вставляем в цепочку
        if (table[index] == null) {
            // Цепочка пустая - просто добавляем
            table[index] = newNode;
        } else {
            // Цепочка не пустая - идем до конца и добавляем
            Node<E> current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;  // Добавляем в конец цепочки
        }

        size++;  // Увеличиваем счетчик элементов
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;  // null элементы не поддерживаются
        }

        // Находим цепочку, в которой может быть элемент
        int index = getIndex(element);
        Node<E> current = table[index];  // Начало цепочки
        Node<E> prev = null;            // Предыдущий узел

        // Проходим по цепочке
        while (current != null) {
            if (element.equals(current.data)) {
                // Нашли элемент для удаления
                if (prev == null) {
                    // Удаляем первый элемент цепочки
                    table[index] = current.next;
                } else {
                    // Удаляем из середины/конца цепочки
                    prev.next = current.next;
                }
                size--;  // Уменьшаем счетчик
                return true;
            }
            prev = current;
            current = current.next;  // Переходим к следующему узлу
        }

        return false;  // Элемент не найден
    }

    //Проверяет наличие элемента в множестве
    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;  // null элементы не поддерживаются
        }

        // Находим цепочку, в которой может быть элемент
        int index = getIndex(element);
        Node<E> current = table[index];  // Начало цепочки

        // Линейный поиск по цепочке
        while (current != null) {
            if (element.equals(current.data)) {
                return true;  // Элемент найден
            }
            current = current.next;  // Переходим к следующему узлу
        }

        return false;  // Элемент не найден
    }

    private int getIndex(Object element) {
        int hashCode = element.hashCode();  // Получаем хеш-код объекта
        return Math.abs(hashCode) % table.length;
    }

    //Увеличиваем размер таблицы в 2 раза и перераспределяем элементы
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;  // Сохраняем старую таблицу
        table = new Node[oldTable.length * 2];  // Создаем новую таблицу в 2 раза больше
        size = 0;  // Сбрасываем счетчик

        // Перебираем все элементы старой таблицы
        for (Node<E> node : oldTable) {
            Node<E> current = node;
            // Перебираем все элементы в цепочке
            while (current != null) {
                add(current.data);  // Добавляем элемент в новую таблицу
                current = current.next;  // Переходим к следующему узлу
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";  // Пустое множество
        }

        StringBuilder sb = new StringBuilder("[");
        boolean firstElement = true;  // Флаг для правильной расстановки запятых

        // Проходим по всем ячейкам таблицы
        for (Node<E> node : table) {
            Node<E> current = node;
            // Проходим по всем элементам цепочки
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");  // Добавляем разделитель после первого элемента
                }
                sb.append(current.data);  // Добавляем данные элемента
                firstElement = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}