package by.it.group410901.borisdubinin.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E>{

    private static final int DEFAULT_CAPACITY = 32;

    private final Comparator<E> comparator;

    private Object[] array;
    private int size;

    public MyPriorityQueue(){
        this(null);
    }
    public MyPriorityQueue(Comparator<E> comparator){
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    private int compare(E left, E right){
        if(comparator != null)
            return comparator.compare(left, right);
        else {
            Comparable<? super E> comparableLeft = (Comparable<? super E>) left;
            return comparableLeft.compareTo(right);
        }
    }

    private void siftUp(int index){
        int parent = (index-1)/2;
        while(compare((E)array[parent], (E)array[index]) > 0){
            Object temp = array[parent];
            array[parent] = array[index];
            array[index] = temp;

            index = parent;
            parent = (index-1)/2;
        }
    }
    private void siftDown(int index){
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && compare((E) array[leftChild], (E) array[smallest]) < 0) {
                smallest = leftChild;
            }

            if (rightChild < size && compare((E) array[rightChild], (E) array[smallest]) < 0) {
                smallest = rightChild;
            }

            if (smallest == index) {
                break;
            }

            Object temp = array[index];
            array[index] = array[smallest];
            array[smallest] = temp;

            index = smallest;
        }
    }

    private void increaseCapacity(){
        Object[] newArray = new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString(){
        if(size < 1)
            return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            sb.append(", ");
        }

        sb.setLength(sb.length()-1);
        sb.setCharAt(sb.length()-1, ']');

        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size < 1;
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException();

        if(size == array.length)
            increaseCapacity();

        array[size] = e;
        siftUp(size);
        size++;

        return true;
    }
    @Override
    public boolean offer(E e) {
        return add(e);
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c == null)
            throw new NullPointerException();
        if(c.isEmpty())
            return false;

        for(E e : c){
            add(e);
        }

        return true;
    }

    @Override
    public E remove() {
        if(size < 1)
            throw new NoSuchElementException();

        E root = (E)array[0];
        array[0] = array[size-1];
        size--;
        siftDown(0);

        return root;
    }
    @Override
    public E poll() {
        if(size < 1)
            return null;

        return remove();
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();

        if (size == 0) return false;

        // Создаем новый массив только для сохраняемых элементов
        Object[] newArray = new Object[array.length];
        int newIndex = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains((E) array[i])) {
                newArray[newIndex++] = array[i];
            }
        }

        // Заменяем старый массив новым
        array = newArray;
        boolean changed = size != newIndex;
        size = newIndex;

        // Перестраиваем кучу
        heapify();

        return changed;
    }
    @Override
    public void clear() {
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public E element() {
        if(size < 1)
            throw new NoSuchElementException();

        return (E)array[0];
    }
    @Override
    public E peek() {
        if(size < 1)
            return null;

        return (E)array[0];
    }

    @Override
    public boolean contains(Object o) {
        if(o == null)
            return false;

        for(int i = 0; i < size; i++){
            if(o.equals(array[i]))
                return true;
        }

        return false;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        if(c == null)
            throw new NullPointerException();
        if(c.isEmpty())
            return true;

        for(Object o : c){
            if(!contains(o))
                return false;
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();

        if (size == 0) return false;

        // Создаем новый массив только для сохраняемых элементов
        Object[] newArray = new Object[array.length];
        int newIndex = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains((E) array[i])) {
                newArray[newIndex++] = array[i];
            }
        }

        // Заменяем старый массив новым
        array = newArray;
        boolean changed = size != newIndex;
        size = newIndex;

        // Перестраиваем кучу
        heapify();

        return changed;
    }

    private void heapify() {
        // Начинаем с последнего не-листового узла
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /// //////////////////////////////////////////////////////////////////////////////

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
    public boolean remove(Object o) {
        return false;
    }
}
