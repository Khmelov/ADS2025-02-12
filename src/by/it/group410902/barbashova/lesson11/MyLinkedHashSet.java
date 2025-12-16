package by.it.group410902.barbashova.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Реализация LinkedHashSet - множества с сохранением порядка добавления элементов
public class MyLinkedHashSet<E> implements Set<E> {

    // Внутренний класс Node для представления элемента
    class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;
        Node<E> nextInSet;   // Ссылка на следующий элемент в цепочке коллизий хэш-таблицы

        public Node(E data) {
            this.data = data;
        }
    }

    class List<E> {
        Node<E> head;
        Node<E> tail;

        // Добавление элемента в конец списка
        void add(Node<E> node) {
            if (head == null) {
                head = node;
            } else {

                tail.next = node;
                node.previous = tail;
            }
            // Обновляем хвост списка
            tail = node;
        }

        // Удаление элемента из списка
        void remove(Node<E> node) {
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                head = head.next;
            }

            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                tail = tail.previous;
            }
        }

        // Очистка списка
        void clear() {
            head = null;
            tail = null;
        }
    }

    // Поля класса
    Node<E>[] elements;
    static final int INITIAL_SIZE = 16;
    int size;
    List<E> list = new List<E>();  // Двусвязный список для сохранения порядка добавления

    // Конструкторы
    public MyLinkedHashSet() {
        this(INITIAL_SIZE);
    }

    public MyLinkedHashSet(int size) {
        elements = new Node[size];  // Конструктор с заданным размером
    }

    // Строковое представление множества (элементы в порядке добавления)
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head;
        // Добавляем первый элемент если он существует
        if (current != null) {
            sb.append(current.data);
            current = current.next;
        }
        // Добавляем остальные элементы через запятую
        while (current != null) {
            sb.append(", ");
            sb.append(current.data);
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);
        size = 0;                     // Сбрасываем счетчик элементов
        list.clear();                 // Очищаем двусвязный список
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вычисление индекса в хэш-таблице для элемента
    private int getHashCode(E element) {
        return element.hashCode() % elements.length;
    }

    @Override
    public boolean add(E e) {
        int index = getHashCode(e);
        Node<E> current = elements[index];

        // Проверяем наличие элемента в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                return false;  // Элемент уже существует
            }
            current = current.nextInSet;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<E>(e);
        // Добавляем в начало цепочки коллизий
        newNode.nextInSet = elements[index];
        elements[index] = newNode;
        size++;

        list.add(newNode);

        if (size > elements.length * 0.75) {

            Node<E>[] newElements = new Node[elements.length * 2];

            // Перехэширование всех элементов
            current = list.head;
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length;
                current.nextInSet = newElements[newIndex];
                newElements[newIndex] = current;
                current = current.next;
            }
            elements = newElements;  // Заменяем старую таблицу новой
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];

        // Поиск элемента в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                // Удаление из цепочки коллизий хэш-таблицы
                if (previous == null) {
                    elements[index] = current.nextInSet;
                } else {
                    previous.nextInSet = current.nextInSet;
                }
                size--;
                // Удаление из двусвязного списка (порядка добавления)
                list.remove(current);
                return true;
            }
            previous = current;
            current = current.nextInSet;
        }
        return false;  // Элемент не найден
    }

    @Override
    public boolean contains(Object o) {
        E e = (E)o;
        int index = getHashCode(e);  // Вычисляем индекс
        Node<E> current = elements[index];

        // Поиск элемента в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                return true;
            }
            current = current.nextInSet;
        }
        return false;
    }

    // Методы для работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции содержатся в множестве
        for (var object : c) {
            if (!contains(object)) {
                return false;  // Если хотя бы один элемент не найден
            }
        }
        return true;  // Все элементы найдены
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы из коллекции
        boolean result = false;
        for (var element : c) {
            if (add(element)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы коллекции из множества
        boolean result = false;
        for (var object : c) {
            if (remove(object)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оставляем только элементы, которые содержатся в коллекции
        if (c.isEmpty()) {
            this.clear();  // Если коллекция пуста, очищаем всё множество
            return true;
        }

        boolean isModified = false;
        // Создаем временное множество для хранения пересечения
        MyLinkedHashSet<E> tempSet = new MyLinkedHashSet<>(elements.length);

        // Проходим по всем элементам в порядке добавления
        Node<E> current = list.head;
        while (current != null) {
            if (c.contains(current.data)) {
                // Если элемент есть в коллекции c, добавляем во временное множество
                tempSet.add(current.data);
                isModified = true;
            }
            current = current.next;
        }

        // Заменяем текущее множество временным (сохраняем порядок добавления)
        elements = tempSet.elements;
        list.head = tempSet.list.head;
        list.tail = tempSet.list.tail;
        size = tempSet.size;

        return isModified;
    }


    @Override
    public Iterator<E> iterator() {return null;}  // Итератор для обхода элементов

    @Override
    public Object[] toArray() {return new Object[0];}  // Преобразование в массив Object[]

    @Override
    public <T> T[] toArray(T[] a) {return null;}  // Преобразование в массив типа T
}