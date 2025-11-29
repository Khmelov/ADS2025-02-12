package by.it.group410902.yarmashuk.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size;
    private final Comparator<? super E> comparator;


    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.comparator = null;
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }


    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
        for (int i = 0; i < size; i++) {
            elements[i] = null; // Освобождаем ссылки
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E element = poll();
        if (element == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        return element;
    }
    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not permitted");
        }
        ensureCapacity();
        elements[size] = element;
        heapifyUp(size);
        size++;
        return true;
    }
    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E first = elements[0];
        elements[0] = elements[size - 1]; // Перемещаем последний элемент в корень
        elements[size - 1] = null; // Очищаем ссылку на последний элемент
        size--;
        heapifyDown(0); // Восстанавливаем свойство кучи
        return first;
    }
    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return elements[0];
    }
    @Override
    public E element() {
        E element = peek();
        if (element == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        return element;
    }
    @Override
    public boolean containsAll(Collection<?> c) {

        if (c.isEmpty()) {
            return true; // Пустая коллекция всегда содержится
        }

        // Если размер переданной коллекции больше размера очереди, то она точно не может содержаться
        if (c.size() > size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        // Если входная коллекция пуста, ничего не делаем.
        if (c.isEmpty()) {
            return false;
        }

        boolean changed = false;

        for (E element : c) {

            if (this.add(element)) {
                changed = true;
            }
        }

        return changed;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false; // Нет смысла удалять, если коллекция пуста
        }


        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }

        // Если ничего не удалено, возвращаем false.
        if (newSize == size) {
            return false;
        }

        // Очищаем оставшиеся старые элементы
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;

        // Перестраиваем кучу. Это O(n).
        // Проходимся от последнего родителя вниз к корню.
        for (int i = parent(size - 1); i >= 0; i--) {
            heapifyDown(i);
        }

        return true;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            clear(); // Если коллекция пуста, очищаем всю очередь
            return true;
        }

        // Строим новый массив, содержащий только те элементы, которые есть в коллекции c.

        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }

        // Если ничего не изменено, возвращаем false.
        if (newSize == size) {
            return false;
        }

        // Очищаем оставшиеся старые элементы
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;

        for (int i = parent(size - 1); i >= 0; i--) {
            heapifyDown(i);
        }

        return true;
    }




    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int expectedSize = size; // Для проверки ConcurrentModificationException

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (expectedSize != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[currentIndex++];
            }

        };
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int numElementsToAdd) {
        int requiredCapacity = size + numElementsToAdd;
        if (requiredCapacity > elements.length) {
            int newCapacity = elements.length;
            while (newCapacity < requiredCapacity) {
                newCapacity *= 2;
            }
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }
    private int compare(E a, E b) {
        if (a == null || b == null) {
            // Обработка null, если они разрешены (в нашей реализации не разрешены, но для безопасности)
            throw new NullPointerException("Cannot compare null elements");
        }
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            // Допустим, что элементы реализуют Comparable, иначе будет ClassCastException
            @SuppressWarnings("unchecked")
            Comparable<? super E> ca = (Comparable<? super E>) a;
            return ca.compareTo(b);
        }
    }


    private int parent(int i) {
        return (i - 1) / 2;
    }


    private int leftChild(int i) {
        return 2 * i + 1;
    }


    private int rightChild(int i) {
        return 2 * i + 2;
    }


    private void heapifyUp(int index) {
        while (index > 0 && compare(elements[index], elements[parent(index)]) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }


    private void heapifyDown(int index) {
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;

        // Ищем наименьший элемент среди текущего, левого и правого потомков
        if (left < size && compare(elements[left], elements[smallest]) < 0) {
            smallest = left;
        }
        if (right < size && compare(elements[right], elements[smallest]) < 0) {
            smallest = right;
        }

        // Если наименьший не текущий элемент, меняем их местами и продолжаем рекурсивно
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }


    private void swap(int i, int j) {
        E temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
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
    public boolean remove(Object o) {
        return false;
    }

}
