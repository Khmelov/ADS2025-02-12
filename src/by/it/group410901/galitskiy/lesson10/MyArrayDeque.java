package by.it.group410901.galitskiy.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
@SuppressWarnings("all")
public class MyArrayDeque<E> implements Deque<E>{
    private static final int defaultSize = 10;  // начальный размер массива elements, по умолчанию 10
    private int currentSize;  // текущий размер очереди
    private E[] elements;  // массив для хранения элементов очереди

    public MyArrayDeque() {  //  инициализирует elements с размером по умолчанию и устанавливает currentSize в 0
        elements = (E[]) new Object[defaultSize];
        currentSize = 0;
    }

    @Override
    // возвращает строковое представление очереди, перечисляя элементы через запятую в квадратных скобках
    public String toString() {
        String tempStr = "[";

        for (int i = 0; i < currentSize; i++) {
            tempStr += elements[i];
            if (i != currentSize - 1) {
                tempStr += ", ";
            }
        }

        tempStr += ']';

        return tempStr.toString();
    }

    @Override
    // возвращает количество элементов в очереди (currentSize)
    public int size() {
        return currentSize;
    }

    @Override
    // добавляет элемент в конец очереди через метод addLast
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    // добавляет элемент e в начало очереди. Если массив заполнен, его размер удваивается,
    // элементы смещаются вправо, и новый элемент помещается на первую позицию.
    public void addFirst(E e) {
        if (currentSize == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];

            System.arraycopy(elements, 0, newElements, 0, currentSize);
            elements = newElements;
        }

        currentSize++;
        for (int i = currentSize - 1; i > 0; i--) {
            elements[i] = elements[i - 1];
        }

        elements[0] = e;
    }

    @Override
    // добавляет элемент e в конец очереди. Если массив заполнен, его размер удваивается,
    // новый элемент добавляется в конец.
    public void addLast(E e) {
        if (currentSize == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];

            System.arraycopy(elements, 0, newElements, 0, currentSize);
            elements = newElements;
        }

        elements[currentSize++] = e;
    }

    @Override
    //  возвращает первый элемент очереди через метод getFirst
    public E element() {
        return getFirst();
    }

    @Override
    // возвращает первый элемент очереди, если она не пуста; иначе — null
    public E getFirst() {
        if (currentSize > 0)
            return elements[0];
        return null;
    }

    @Override
    // возвращает последний элемент очереди, если она не пуста; иначе — null
    public E getLast() {
        if (currentSize > 0)
            return elements[currentSize - 1];
        return null;
    }

    @Override
    // удаляет и возвращает первый элемент очереди через метод pollFirst
    public E poll() {
        return pollFirst();
    }

    @Override
    // удаляет и возвращает первый элемент. Сдвигает оставшиеся элементы влево
    public E pollFirst() {
        if (currentSize == 0) {
            return null;
        }

        E polledElement = elements[0];

        currentSize--;

        for (int i = 0; i < currentSize; i++) {
            elements[i] = elements[i + 1];
        }

        return polledElement;
    }

    @Override
    // удаляет и возвращает последний элемент, если очередь не пуста; иначе — null.
    public E pollLast() {
        if (currentSize == 0) {
            return null;
        }

        return elements[--currentSize];
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }


    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }


    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }


    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

}
