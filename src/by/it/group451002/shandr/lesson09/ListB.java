package by.it.group451002.shandr.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListB<E> implements List<E> {

    // Внутренний класс Node для реализации двусвязного списка
    private static class Node<E> {
        E data;        // Данные, хранящиеся в узле
        Node<E> next;  // Ссылка на следующий узел
        Node<E> prev;  // Ссылка на предыдущий узел

        /**
         * Конструктор узла с данными
         * @param data данные для хранения в узле
         */
        Node(E data) {
            this.data = data;
        }

        /**
         * Конструктор узла с данными и ссылками на соседние узлы
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

    private Node<E> head; // Первый элемент списка
    private Node<E> tail; // Последний элемент списка
    private int size;     // Количество элементов в списке

    /**
     * Конструктор по умолчанию
     * Создает пустой двусвязный список
     */
    public ListB() {
        head = null;
        tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка
     * @return строка в формате [element1, element2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean add(E e) {
        // Создаем новый узел
        Node<E> newNode = new Node<>(e);
        if (tail == null) {
            // Если список пуст, новый узел становится и головой и хвостом
            head = newNode;
            tail = newNode;
        } else {
            // Добавляем новый узел после текущего хвоста
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс удаляемого элемента
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Находим узел для удаления и удаляем его
        Node<E> nodeToRemove = getNode(index);
        return unlink(nodeToRemove);
    }

    /**
     * Возвращает количество элементов в списке
     * @return размер списка
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Вставляет элемент в указанную позицию
     * @param index индекс, по которому будет вставлен элемент
     * @param element элемент для вставки
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public void add(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == size) {
            // Добавление в конец (используем существующий метод add)
            add(element);
        } else if (index == 0) {
            // Добавление в начало
            Node<E> newNode = new Node<>(element);
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            size++;
        } else {
            // Добавление в середину
            Node<E> current = getNode(index);
            Node<E> newNode = new Node<>(element, current.prev, current);
            current.prev.next = newNode;
            current.prev = newNode;
            size++;
        }
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     */
    @Override
    public boolean remove(Object o) {
        // Поиск узла для удаления
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Заменяет элемент в указанной позиции
     * @param index индекс заменяемого элемента
     * @param element новый элемент
     * @return предыдущий элемент в этой позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E set(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Находим узел и заменяем его данные
        Node<E> node = getNode(index);
        E oldValue = node.data;
        node.data = element;
        return oldValue;
    }

    /**
     * Проверяет, пуст ли список
     * @return true если список пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Удаляет все элементы из списка
     */
    @Override
    public void clear() {
        // Очистка всех ссылок для помощи сборщику мусора
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.data = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Возвращает индекс первого вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс элемента или -1 если не найден
     */
    @Override
    public int indexOf(Object o) {
        // Поиск первого вхождения элемента
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Возвращает элемент по указанному индексу
     * @param index индекс элемента
     * @return элемент в указанной позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E get(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).data;
    }

    /**
     * Проверяет наличие элемента в списке
     * @param o элемент для поиска
     * @return true если элемент найден
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Возвращает индекс последнего вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс элемента или -1 если не найден
     */
    @Override
    public int lastIndexOf(Object o) {
        // Поиск последнего вхождения элемента
        int index = size - 1;
        Node<E> current = tail;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет наличие всех элементов коллекции в списке
     * @param c коллекция для проверки
     * @return true если все элементы присутствуют
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в конец списка
     * @param c коллекция для добавления
     * @return true если список изменился
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E element : c) {
            add(element);
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции начиная с указанной позиции
     * @param index индекс, с которого начинать вставку
     * @param c коллекция для добавления
     * @return true если список изменился
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        int i = index;
        for (E element : c) {
            add(i++, element);
        }
        return true;
    }

    /**
     * Удаляет все элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если список изменился
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.data)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /**
     * Сохраняет только элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если список изменился
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.data)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /**
     * Возвращает подсписок от fromIndex (включительно) до toIndex (исключительно)
     * @param fromIndex начальный индекс (включительно)
     * @param toIndex конечный индекс (исключительно)
     * @return подсписок указанного диапазона
     * @throws IndexOutOfBoundsException если индексы некорректны
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверка корректности индексов
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListB<E> subList = new ListB<>();
        Node<E> current = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(current.data);
            current = current.next;
        }
        return subList;
    }

    /**
     * Возвращает двунаправленный итератор, начинающийся с указанной позиции
     * @param index начальная позиция итератора
     * @return list iterator начинающийся с указанной позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new ListIterator<E>() {
            private Node<E> lastReturned = null;
            private Node<E> next = (index == size) ? null : getNode(index);
            private int nextIndex = index;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = next;
                next = next.next;
                nextIndex++;
                return lastReturned.data;
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                next = (next == null) ? tail : next.prev;
                lastReturned = next;
                nextIndex--;
                return lastReturned.data;
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                Node<E> lastNext = lastReturned.next;
                unlink(lastReturned);
                if (next == lastReturned) {
                    next = lastNext;
                } else {
                    nextIndex--;
                }
                lastReturned = null;
            }

            @Override
            public void set(E e) {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                lastReturned.data = e;
            }

            @Override
            public void add(E e) {
                lastReturned = null;
                if (next == null) {
                    ListB.this.add(e);
                } else {
                    ListB.this.add(nextIndex, e);
                }
                nextIndex++;
            }
        };
    }

    /**
     * Возвращает двунаправленный итератор для списка
     * @return list iterator начинающийся с начала списка
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Преобразует список в массив указанного типа
     * @param a массив для заполнения
     * @return массив содержащий все элементы списка
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаем новый массив нужного типа и размера
            T[] result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            Node<E> current = head;
            for (int i = 0; i < size; i++) {
                result[i] = (T) current.data;
                current = current.next;
            }
            return result;
        }

        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            a[i] = (T) current.data;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Преобразует список в массив объектов
     * @return массив содержащий все элементы списка
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            result[i] = current.data;
            current = current.next;
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает итератор для последовательного обхода списка
     * @return итератор элементов списка
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.next;
                return lastReturned.data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает узел по индексу
     * @param index индекс узла
     * @return узел по указанному индексу
     */
    private Node<E> getNode(int index) {
        // Оптимизация: выбираем направление обхода от начала или конца
        if (index < (size >> 1)) {
            // Обход от начала (для элементов в первой половине)
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Обход от конца (для элементов во второй половине)
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    /**
     * Удаляет узел из списка и возвращает его данные
     * @param node узел для удаления
     * @return данные удаленного узла
     */
    private E unlink(Node<E> node) {
        E element = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        // Обновляем ссылки соседних узлов
        if (prev == null) {
            // Удаление первого элемента
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            // Удаление последнего элемента
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        // Очищаем данные и уменьшаем размер
        node.data = null;
        size--;
        return element;
    }
}