package by.it.group410902.kovalchuck.lesson01.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

//Реализация двусторонней очереди (Deque) на основе двусвязного списка
public class MyLinkedList<E> implements Deque<E> {

    //Внутренний класс для представления узла списка
    private static class Node<E> {
        E data;        // Данные, хранящиеся в узле
        Node<E> prev;  // Ссылка на предыдущий узел
        Node<E> next;  // Ссылка на следующий узел

        //Конструктор узла
        Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node<E> head; // Первый элемент списка
    private Node<E> tail; // Последний элемент списка
    private int size;     // Количество элементов в списке

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    //Преобразование списка в строковое представление
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    //Добавление элемента в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    //Удаление элемента по индексу
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Оптимизация для удаления первого и последнего элемента
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            // Получение узла по индексу
            Node<E> current = getNode(index);
            E removedData = current.data;

            // Перелинковка соседних узлов
            current.prev.next = current.next;
            current.next.prev = current.prev;

            // Очистка ссылок удаляемого узла
            current.prev = null;
            current.next = null;
            size--;

            return removedData;
        }
    }

    //Удаление первого вхождения указанного элемента
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }

        // Поиск элемента в списке
        Node<E> current = head;
        int index = 0;
        while (current != null) {
            if (element.equals(current.data)) {
                remove(index);
                return true;
            }
            current = current.next;
            index++;
        }
        return false;
    }

    //Получение количества элементов в списке
    @Override
    public int size() {
        return size;
    }

    //Добавление элемента в начало списка
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        Node<E> newNode = new Node<>(element);
        if (size == 0) {
            // Если список пустой, новый узел становится и головой и хвостом
            head = newNode;
            tail = newNode;
        } else {
            // Вставка нового узла перед текущей головой
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    //Добавление элемента в конец списка
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        Node<E> newNode = new Node<>(element);
        if (size == 0) {
            // Если список пустой, новый узел становится и головой и хвостом
            head = newNode;
            tail = newNode;
        } else {
            // Вставка нового узла после текущего хвоста
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    //Получение первого элемента списка без удаления
    @Override
    public E element() {
        return getFirst();
    }

    //Получение первого элемента списка без удаления
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    //Получение последнего элемента списка без удаления
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    //Извлечение и удаление первого элемента списка
    @Override
    public E poll() {
        return pollFirst();
    }

    //Извлечение и удаление первого элемента списка
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E data = head.data;
        if (size == 1) {
            // Если в списке один элемент, очищаем оба указателя
            head = null;
            tail = null;
        } else {
            // Перемещаем голову на следующий элемент
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    //Извлечение и удаление последнего элемента списка
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E data = tail.data;
        if (size == 1) {
            // Если в списке один элемент, очищаем оба указателя
            head = null;
            tail = null;
        } else {
            // Перемещаем хвост на предыдущий элемент
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    //Вспомогательный метод для получения узла по индексу
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;
        if (index < size / 2) {
            // Поиск с начала списка для первых половины элементов
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // Поиск с конца списка для второй половины элементов
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    //Проверка пустоты списка
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //Добавление элемента в конец списка

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    //Добавление элемента в начало списка

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    //Добавление элемента в конец списка
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    //Удаление первого элемента списка
    @Override
    public E remove() {
        return removeFirst();
    }

    //Удаление первого элемента списка
    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    //Удаление последнего элемента списка
    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    //Получение первого элемента без удаления
    @Override
    public E peek() {
        return peekFirst();
    }

    //Получение первого элемента без удаления
    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    //Получение последнего элемента без удаления
    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    //Удаление первого вхождения элемента
    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    //Удаление последнего вхождения элемента
    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    //Добавление всех элементов коллекции
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    //Добавление элемента в начало списка
    @Override
    public void push(E e) {
        addFirst(e);
    }

    //Удаление первого элемента списка
    @Override
    public E pop() {
        return removeFirst();
    }

    //Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    //Получение итератора
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    //Получение обратного итератора
    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    //Очистка списка
    @Override
    public void clear() {
        // Последовательно очищаем все узлы
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    //Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    //Удаление всех элементов коллекции
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    //Сохранение только указанных элементов
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    //Преобразование списка в массив
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    //Преобразование списка в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}