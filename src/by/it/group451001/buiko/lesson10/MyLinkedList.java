package by.it.group451001.buiko.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    private Node<E> first;
    private Node<E> last;
    private int size;


   /* Дек (double-ended queue) — это двусторонняя очередь,
   которая позволяет эффективно добавлять и удалять элементы как в начале, так и в конце коллекции.
    */
    private static class Node<E> {
        E item;        // Данные, хранящиеся в узле
        Node<E> next;  // Ссылка на следующий узел
        Node<E> prev;  // Ссылка на предыдущий узел

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /* реализовала дек на основе двусвязного списка,
   где каждый узел содержит ссылки на предыдущий и
   следующий элементы, что обеспечивает быстрый доступ к обоим концам.
    */
    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    /*Для добавления элементов используются методы addFirst() и addLast(),
     которые создают новые узлы и обновляют ссылки соседних элементов за время O(1).
     */
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        // Проходим по всем узлам списка
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /* Для удаления элементов применяются методы removeFirst() и removeLast(),
   которые корректно обрабатывают ссылки и предотвращают утечки памяти через обнуление полей удаляемых узлов.
    */
    @Override
    public boolean add(E element) {
        // Добавление элемента в конец списка
        addLast(element);
        return true;
    }

    /*Все основные операции (добавление, удаление, получение элементов с обоих концов)
   работают за константное время O(1), а поиск узла по индексу оптимизирован за счет выбора направления обхода от начала или конца списка.
    */
    public E remove(int index) {
        // Проверка корректности индекса
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object element) {
        // Линейный поиск элемента для удаления
        for (Node<E> x = first; x != null; x = x.next) {
            if (Objects.equals(element, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        linkFirst(element);
    }

    @Override
    public void addLast(E element) {
        linkLast(element);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return (first == null) ? null : unlinkFirst();
    }

    @Override
    public E pollLast() {
        return (last == null) ? null : unlinkLast();
    }

    // Добавление элемента в начало списка
    private void linkFirst(E element) {
        // Сохраняем ссылку на текущий первый узел
        Node<E> f = first;
        // Создаем новый узел, который будет первым
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) {
            // Если список был пуст, новый узел становится и первым и последним
            last = newNode;
        } else {
            // Иначе обновляем ссылку предыдущего первого узла
            f.prev = newNode;
        }
        size++;
    }

    // Добавление элемента в конец списка
    private void linkLast(E element) {
        // Сохраняем ссылку на текущий последний узел
        Node<E> l = last;
        // Создаем новый узел, который будет последним
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            // Если список был пуст, новый узел становится и первым и последним
            first = newNode;
        } else {
            // Иначе обновляем ссылку следующего предыдущего последнего узла
            l.next = newNode;
        }
        size++;
    }

    // Удаление первого узла
    private E unlinkFirst() {
        Node<E> f = first;
        E element = f.item;
        Node<E> next = f.next;
        // Очищаем ссылки удаляемого узла для помощи сборщику мусора
        f.item = null;
        f.next = null;
        first = next;
        if (next == null) {
            // Если был только один элемент, список становится пустым
            last = null;
        } else {
            // Иначе обнуляем ссылку на предыдущий у нового первого узла
            next.prev = null;
        }
        size--;
        return element;
    }

    // Удаление последнего узла
    private E unlinkLast() {
        Node<E> l = last;
        E element = l.item;
        Node<E> prev = l.prev;
        // Очищаем ссылки удаляемого узла
        l.item = null;
        l.prev = null;
        last = prev;
        if (prev == null) {
            // Если был только один элемент, список становится пустым
            first = null;
        } else {
            // Иначе обнуляем ссылку на следующий у нового последнего узла
            prev.next = null;
        }
        size--;
        return element;
    }

    // Удаление произвольного узла из середины списка
    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        // Обновляем ссылки соседних узлов
        if (prev == null) {
            // Если удаляем первый узел
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            // Если удаляем последний узел
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        // Очищаем данные узла
        x.item = null;
        size--;
        return element;
    }

    // Поиск узла по индексу (оптимизированный - с начала или с конца)
    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            // Если индекс в первой половине - ищем с начала
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            // Если индекс во второй половине - ищем с конца
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    // Проверка корректности индекса
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Остальные методы интерфейса Deque

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (first == null) throw new NoSuchElementException();
        return unlinkFirst();
    }

    @Override
    public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return unlinkLast();
    }

    @Override
    public E peekFirst() {
        return (first == null) ? null : first.item;
    }

    @Override
    public E peekLast() {
        return (last == null) ? null : last.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        // Поиск с конца для удаления последнего вхождения
        for (Node<E> x = last; x != null; x = x.prev) {
            if (Objects.equals(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // Линейный поиск по всему списку
        for (Node<E> x = first; x != null; x = x.next) {
            if (Objects.equals(o, x.item)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescItr();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            modified |= remove(o);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        // Последовательно очищаем все узлы
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    // Итератор для прямого обхода (от первого к последнему)
    private class Itr implements Iterator<E> {
        private Node<E> next = first;      // Следующий возвращаемый узел
        private Node<E> lastReturned = null; // Последний возвращенный узел

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            unlink(lastReturned);
            lastReturned = null;
        }
    }

    // Итератор для обратного обхода (от последнего к первому)
    private class DescItr implements Iterator<E> {
        private Node<E> next = last;       // Следующий возвращаемый узел
        private Node<E> lastReturned = null; // Последний возвращенный узел

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = next;
            next = next.prev;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            unlink(lastReturned);
            lastReturned = null;
        }
    }
}

/*
* Ключевые особенности реализации:
Двусвязная структура - каждый узел имеет ссылки на предыдущий и следующий элементы

Оптимизированный доступ - поиск по индексу осуществляется с начала или с конца в зависимости от позиции

Эффективные операции - добавление/удаление с обоих концов за O(1)

Два итератора - для прямого и обратного обхода

Полная совместимость с интерфейсом Deque - можно использовать как стек, очередь или дек
* */
