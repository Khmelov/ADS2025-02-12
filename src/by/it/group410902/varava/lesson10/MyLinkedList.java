package by.it.group410902.varava.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Реализация интерфейса Deque на основе двусвязного списка
 * @param <E> тип элементов в деке
 */
public class MyLinkedList<E> implements Deque<E> {

    /**
     * Внутренний класс для представления узла списка
     * @param <E> тип данных в узле
     */
    private static class Node<E> {
        E data;        // Данные узла
        Node<E> prev;  // Ссылка на предыдущий узел
        Node<E> next;  // Ссылка на следующий узел

        /**
         * Конструктор для создания узла с данными
         * @param data данные для хранения в узле
         */
        Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }

        /**
         * Конструктор для создания узла с данными и ссылками
         * @param data данные для хранения в узле
         * @param prev ссылка на предыдущий узел
         * @param next ссылка на следующий узел
         */
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    // Поля класса
    private Node<E> head; // Первый узел в списке (начало дека)
    private Node<E> tail; // Последний узел в списке (конец дека)
    private int size;     // Количество элементов в списке

    /**
     * Конструктор по умолчанию - создает пустой дек
     */
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Представление дека в виде строки
     * @return строковое представление элементов дека в порядке от первого к последнему
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        // Проходим по всем узлам списка от начала до конца
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

    /**
     * Добавляет элемент в конец дека
     * @param element добавляемый элемент
     * @return true (как указано в интерфейсе Collection)
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Удаляет элемент по индексу (дополнительный метод, не из интерфейса Deque)
     * @param index индекс удаляемого элемента
     * @return данные удаленного элемента
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    public E remove(int index) {
        checkElementIndex(index); // Проверяем корректность индекса
        Node<E> node = getNode(index); // Находим узел по индексу
        E data = node.data;
        removeNode(node); // Удаляем узел из списка
        return data;
    }

    /**
     * Удаляет первое вхождение указанного элемента из дека
     * @param element элемент для удаления
     * @return true если элемент был найден и удален, false в противном случае
     */
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            // Поиск null элемента с начала списка
            Node<E> current = head;
            while (current != null) {
                if (current.data == null) {
                    removeNode(current);
                    return true;
                }
                current = current.next;
            }
        } else {
            // Поиск не-null элемента с начала списка
            Node<E> current = head;
            while (current != null) {
                if (element.equals(current.data)) {
                    removeNode(current);
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    /**
     * Возвращает количество элементов в деке
     * @return текущий размер дека
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в начало дека
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        Node<E> newNode = new Node<>(element);
        if (head == null) {
            // Если список пуст, новый узел становится и head и tail
            head = tail = newNode;
        } else {
            // Вставляем новый узел перед текущим head
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Добавляет элемент в конец дека
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            // Если список пуст, новый узел становится и head и tail
            head = tail = newNode;
        } else {
            // Вставляем новый узел после текущего tail
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return head.data;
    }

    /**
     * Возвращает последний элемент дека без удаления
     * @return последний элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return tail.data;
    }

    /**
     * Извлекает и удаляет первый элемент дека
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Извлекает и удаляет первый элемент дека
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }

        E data = head.data;
        if (head == tail) {
            // Если в списке только один элемент
            head = tail = null;
        } else {
            // Удаляем head и перемещаем указатель на следующий узел
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Извлекает и удаляет последний элемент дека
     * @return последний элемент дека или null, если дек пуст
     */
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }

        E data = tail.data;
        if (head == tail) {
            // Если в списке только один элемент
            head = tail = null;
        } else {
            // Удаляем tail и перемещаем указатель на предыдущий узел
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    /**
     * Удаляет указанный узел из списка
     * @param node узел для удаления
     */
    private void removeNode(Node<E> node) {
        // Обновляем ссылки соседних узлов
        if (node.prev == null) {
            // Удаляем head
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            // Удаляем tail
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        size--;
    }

    /**
     * Находит узел по индексу (оптимизированный поиск)
     * @param index индекс искомого узла
     * @return узел с указанным индексом
     */
    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            // Если индекс в первой половине списка, ищем с начала
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Если индекс во второй половине списка, ищем с конца
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    /**
     * Проверяет корректность индекса
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////         Реализованные методы интерфейса Deque            ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет, пуст ли дек
     * @return true если дек пуст, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в конец дека (аналогично add)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * Добавляет элемент в начало дека (аналогично addFirst)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Добавляет элемент в конец дека (аналогично addLast)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E remove() {
        if (head == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    /**
     * Удаляет и возвращает последний элемент дека
     * @return последний элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollLast();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E peekFirst() {
        return head == null ? null : head.data;
    }

    /**
     * Возвращает последний элемент дека без удаления
     * @return последний элемент дека или null, если дек пуст
     */
    @Override
    public E peekLast() {
        return tail == null ? null : tail.data;
    }

    /**
     * Удаляет первое вхождение указанного элемента (с начала)
     * @param o элемент для удаления
     * @return true если элемент был найден и удален, false в противном случае
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * Удаляет последнее вхождение указанного элемента (с конца)
     * @param o элемент для удаления
     * @return true если элемент был найден и удален, false в противном случае
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            // Поиск null элемента с конца списка
            Node<E> current = tail;
            while (current != null) {
                if (current.data == null) {
                    removeNode(current);
                    return true;
                }
                current = current.prev;
            }
        } else {
            // Поиск не-null элемента с конца списка
            Node<E> current = tail;
            while (current != null) {
                if (o.equals(current.data)) {
                    removeNode(current);
                    return true;
                }
                current = current.prev;
            }
        }
        return false;
    }

    /**
     * Добавляет элемент в начало дека (стековая операция)
     * @param e добавляемый элемент
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Извлекает и удаляет первый элемент дека (стековая операция)
     * @return первый элемент дека
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /**
     * Проверяет, содержится ли указанный элемент в деке
     * @param o искомый элемент
     * @return true если элемент найден, false в противном случае
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            // Поиск null элемента
            Node<E> current = head;
            while (current != null) {
                if (current.data == null) {
                    return true;
                }
                current = current.next;
            }
        } else {
            // Поиск не-null элемента
            Node<E> current = head;
            while (current != null) {
                if (o.equals(current.data)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    /**
     * Очищает дек, удаляя все элементы
     */
    @Override
    public void clear() {
        // Последовательно очищаем все узлы для помощи сборщику мусора
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.prev = null;
            current.next = null;
            current.data = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса Deque - не реализованы     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Spliterator<E> spliterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }
}