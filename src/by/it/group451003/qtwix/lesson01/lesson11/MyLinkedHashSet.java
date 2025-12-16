package by.it.group451003.qtwix.lesson01.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    // Узел множества, который хранит данные и ссылки для двух структур:
    // 1) двусвязного списка (previous / next),
    // 2) цепочки коллизий в хэш-таблице (nextInSet).
    class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;
        Node<E> nextInSet;

        public Node(E data) {
            this.data = data;
        }
    }

    // Вспомогательный класс для поддержки порядка вставки элементов (как LinkedHashSet)
    class List<E> {
        Node<E> head; // первый элемент
        Node<E> tail; // последний элемент

        // Добавление узла в конец списка
        void add(Node<E> node) {
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
                node.previous = tail;
            }
            tail = node;
        }

        // Удаление узла из списка
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

    // Массив "бакетов" для хэш-таблицы
    Node<E>[] elements;
    // Начальный размер таблицы
    static final int INITIAL_SIZE = 16;
    // Количество элементов в множестве
    int size;
    // Список для сохранения порядка вставки
    List<E> list = new List<E>();

    // Конструктор по умолчанию
    public MyLinkedHashSet() {
        this(INITIAL_SIZE);
    }

    // Конструктор с заданным размером
    public MyLinkedHashSet(int size) {
        elements = new Node[size];
    }

    // Строковое представление множества (элементы в порядке вставки)
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head;
        if (current != null) {
            sb.append(current.data);
            current = current.next;
        }
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
        Arrays.fill(elements, null); // очищаем таблицу
        size = 0;                    // сбрасываем размер
        list.clear();                // очищаем список порядка
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Хэш-функция для вычисления индекса бакета
    private int getHashCode(E element) {
        return element.hashCode() % elements.length;
    }

    // Добавление элемента
    @Override
    public boolean add(E e) {
        int index = getHashCode(e);
        Node<E> current = elements[index];

        // Проверка, что элемента ещё нет (уникальность множества)
        while (current != null) {
            if (current.data.equals(e)) {
                return false;
            }
            current = current.nextInSet;
        }

        // Создание нового узла и добавление его в бакет
        Node<E> newNode = new Node<E>(e);
        newNode.nextInSet = elements[index];
        elements[index] = newNode;

        // Увеличиваем размер
        size++;

        // Добавляем в список для сохранения порядка вставки
        list.add(newNode);

        // Проверка на заполненность (>75%), тогда увеличиваем таблицу
        if (size > elements.length * 0.75) {
            Node<E>[] newElements = new Node[elements.length * 2];
            current = list.head;
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length;
                current.nextInSet = newElements[newIndex];
                newElements[newIndex] = current;
                current = current.next;
            }
            elements = newElements;
        }
        return true;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];

        while (current != null) {
            if (current.data.equals(e)) {
                // Удаление из бакета
                if (previous == null) {
                    elements[index] = current.nextInSet;
                } else {
                    previous.nextInSet = current.nextInSet;
                }
                // Уменьшаем размер
                size--;
                // Удаляем из списка
                list.remove(current);
                return true;
            }
            previous = current;
            current = current.nextInSet;
        }
        return false;
    }

    // Проверка, содержится ли элемент
    @Override
    public boolean contains(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> current = elements[index];

        while (current != null) {
            if (current.data.equals(e)) {
                return true;
            }
            current = current.nextInSet;
        }
        return false;
    }

    // Проверка: содержатся ли ВСЕ элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (var object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (var element : c) {
            if (add(element)) {
                result = true;
            }
        }
        return result;
    }

    // Удаление всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (var object : c) {
            if (remove(object)) {
                result = true;
            }
        }
        return result;
    }

    // Оставляем только элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.clear();
            return true;
        }

        boolean isModified = false;
        // Временное множество для пересечения
        MyLinkedHashSet<E> tempSet = new MyLinkedHashSet<>(elements.length);
        Node<E> current = list.head;

        while (current != null) {
            if (c.contains(current.data)) {
                tempSet.add(current.data);
                isModified = true;
            }
            current = current.next;
        }

        // Переносим данные из временного множества
        elements = tempSet.elements;
        list.head = tempSet.list.head;
        list.tail = tempSet.list.tail;
        size = tempSet.size;

        return isModified;
    }

    // Методы пока не реализованы
    @Override
    public Iterator<E> iterator() {return null;}
    @Override
    public Object[] toArray() {return new Object[0];}
    @Override
    public <T> T[] toArray(T[] a) {return null;}
}