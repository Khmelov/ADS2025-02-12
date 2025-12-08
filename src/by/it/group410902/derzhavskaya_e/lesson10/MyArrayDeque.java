package by.it.group410902.derzhavskaya_e.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;

// реализация двунаправленной очереди на кольцевом массиве
public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements; // массив для хранения элементов очереди
    private int head;     // индекс первого элемента в очереди
    private int tail;     // индекс, куда будет добавлен следующий элемент
    private int size;     // текущее количество элементов в очереди

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16]; // начальная емкость массива - 16 элементов
        head = 0; // начальная позиция головы
        tail = 0; // начальная позиция хвоста
        size = 0; // начальный размер очереди
    }
    // двусторонняя очередь позволяет добавлять и удалять элементы с обоих концов

    //строковое представление в формате [a, b, c]
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) sb.append(", ");
            current = (current + 1) % elements.length; // Круговой обход
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    //добавление в конец
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    //добавление в начало: сдвиг head назад
    @Override
    public void addFirst(E element) {
        if (size == elements.length) resizeArray();

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    //добавление в конец: запись в tail и сдвиг вперед
    @Override
    public void addLast(E element) {
        if (size == elements.length) resizeArray();

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    //удвоение размера массива с сохранением порядка элементов
    private void resizeArray() {
        E[] newElements = (E[]) new Object[elements.length * 2];

        if (head < tail) {
            //элементы в непрерывном блоке
            System.arraycopy(elements, head, newElements, 0, size);
        } else {
            //элементы разорваны: часть в конце, часть в начале массива
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, newElements, 0, firstPart);
            System.arraycopy(elements, 0, newElements, firstPart, tail);
        }

        elements = newElements;
        head = 0;
        tail = size;
    }

    //получение первого элемента без удаления
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[head];
    }

    //tail указывает на следующую позицию, поэтому берем предыдущую
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    //удаление и возврат первого элемента
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;

        E element = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    //удаление и возврат последнего элемента: сдвиг tail назад
    @Override
    public E pollLast() {
        if (size == 0) return null;

        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E peek() { return null; }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override public void push(E e) {}
    @Override public E pop() { return null; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public java.util.Iterator<E> iterator() { return null; }
    @Override public java.util.Iterator<E> descendingIterator() { return null; }
    @Override public void clear() {}
    @Override public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override public boolean removeAll(java.util.Collection<?> c) { return false; }
    @Override public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override public boolean isEmpty() { return false; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}

