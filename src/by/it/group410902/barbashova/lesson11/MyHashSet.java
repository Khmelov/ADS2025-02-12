package by.it.group410902.barbashova.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс Node для представления элемента в множестве
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
            tail = node;
        }

        // Удаление элемента из списка
        void remove(Node<E> node) {
            // Обновляем ссылки у предыдущего элемента
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                // Если удаляем голову, обновляем голову
                head = head.next;
            }

            // Обновляем ссылки у следующего элемента
            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                // Если удаляем хвост, обновляем хвост
                tail = tail.previous;
            }
        }

        // Очистка списка
        void clear() {
            head = null;
            tail = null;
        }
    }

    // Основные поля класса
    Node<E>[] elements;        // Массив бакетов хэш-таблицы
    static final int INITIAL_SIZE = 16;  // Начальный размер хэш-таблицы
    int size;                  // Количество элементов в множестве
    List<E> list = new List<E>();  // Двусвязный список для сохранения порядка добавления

    // Конструкторы
    public MyHashSet() {
        this(INITIAL_SIZE);  // Конструктор по умолчанию
    }

    public MyHashSet(int size) {
        elements = new Node[size];  // Конструктор с заданным размером
    }

    // Преобразование множества в строку (в порядке добавления элементов)
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head;
        if (current != null) {
            sb.append(current.data);
            current = current.next;
        }
        // Проходим по всем элементам списка
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
        return size;  // Возвращает количество элементов
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);  // Очищаем массив бакетов
        size = 0;                     // Сбрасываем счетчик
        list.clear();                 // Очищаем список
    }

    @Override
    public boolean isEmpty() {
        return size == 0;  // Проверяем пустое ли множество
    }

    // Вычисление индекса в хэш-таблице для элемента
    private int getHashCode(E element) {
        return element.hashCode() % elements.length;  // Простая хэш-функция
    }

    @Override
    public boolean add(E e) {
        int index = getHashCode(e);  // Получаем индекс бакета
        Node<E> current = elements[index];

        // Проверяем нет ли уже такого элемента в цепочке коллизий
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

        // Добавляем в двусвязный список для сохранения порядка
        list.add(newNode);

        // Проверяем необходимость расширения хэш-таблицы (load factor = 0.75)
        if (size > elements.length * 0.75) {
            Node<E>[] newElements = new Node[elements.length * 2];  // Увеличиваем в 2 раза

            // Перехэширование всех элементов
            current = list.head;
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length;  // Новый индекс
                current.nextInSet = newElements[newIndex];  // Добавляем в начало новой цепочки
                newElements[newIndex] = current;
                current = current.next;  // Переходим к следующему элементу в списке
            }
            elements = newElements;  // Заменяем старый массив новым
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];

        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                // Удаляем из цепочки коллизий
                if (previous == null) {
                    elements[index] = current.nextInSet;  // Удаляем первый элемент цепочки
                } else {
                    previous.nextInSet = current.nextInSet;  // Удаляем из середины/конца
                }
                size--;
                // Удаляем из двусвязного списка
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
        int index = getHashCode(e);
        Node<E> current = elements[index];

        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                return true;  // Элемент найден
            }
            current = current.nextInSet;
        }
        return false;  // Элемент не найден
    }

    // Операции с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции содержатся в множестве
        for (var object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции
        boolean result = false;
        for (var element : c) {
            if (add(element)) {
                result = true;  // Хотя бы один элемент был добавлен
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
                result = true;  // Хотя бы один элемент был удален
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оставляем только элементы, которые содержатся в коллекции c
        if (c.isEmpty()) {
            this.clear();  // Если коллекция пуста, очищаем множество
            return true;
        }

        boolean isModified = false;
        // Создаем временное множество для хранения элементов, которые нужно сохранить
        MyHashSet<E> tempSet = new MyHashSet<>(elements.length);

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

        // Заменяем текущее множество временным
        elements = tempSet.elements;
        list.head = tempSet.list.head;
        list.tail = tempSet.list.tail;
        size = tempSet.size;

        return isModified;
    }



    @Override
    public Iterator<E> iterator() {return null;}

    @Override
    public Object[] toArray() {return new Object[0];}

    @Override
    public <T> T[] toArray(T[] a) {return null;}
}