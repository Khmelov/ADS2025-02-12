package by.it.group410901.borisdubinin.lesson10;


import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 32;
    private Object[] array;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }
    public MyArrayDeque(int startCapacity){
        if(startCapacity <= 1)
            startCapacity = DEFAULT_CAPACITY;

        array = new Object[startCapacity];
        head = -1;                      //перед первым элементом, т.к. добавление сначала смещает head вперед
        tail = array.length;                //то же самое
        size = 0;
    }

    private void increaseCapacity(){
        Object[] newArray = new Object[array.length * 2];
        for(int i = 0; i <= head; i++){
            newArray[i] = array[i];
        }
        if(tail == array.length){
            tail = newArray.length;
        }
        else {
            int j = newArray.length-1;
            for (int i = array.length-1; i >= tail; i--, j--) {
                newArray[j] = array[i];
            }
            tail = j+1;
        }
        array = newArray;
    }

    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString(){
        if(size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder("[");
        for(int i = head; i >= 0; i--){
            sb.append(array[i]);
            sb.append(", ");
        }
        for(int i = array.length-1; i >= tail; i--){
            sb.append(array[i]);
            sb.append(", ");
        }

        if(sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");

        return sb.toString();
    }
    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean add(E element){
        if(element == null)
            throw new NullPointerException("Null элементы не разрешены");

        addLast(element);
        return true;
    }
    @Override
    public void addFirst(E element){
        if(element == null)
            throw new NullPointerException("Null элементы не разрешены");

        if(++size > array.length)
            increaseCapacity();
        array[++head] = element;
    }
    @Override
    public void addLast(E element){
        if(element == null)
            throw new NullPointerException("Null элементы не разрешены");

        if(++size > array.length)
            increaseCapacity();
        array[--tail] = element;
    }

    @Override
    public E element() {
        if(size == 0)
            return null;
        return getFirst();
    }
    @Override
    public E getFirst() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        int first;
        if(head == -1)
            first = array.length-1;
        else
            first = head;

        return (E)array[first];
    }
    @Override
    public E getLast() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        int last;
        if(tail == array.length)
            last = 0;
        else
            last = tail;

        return (E)array[last];
    }

    @Override
    public E poll() {
        return pollFirst();
    }
    @Override
    public E pollFirst() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        size--;
        if(head == -1){
            E first = (E)array[array.length-1];
            for(int i = array.length-2; i >= tail; i--)
                array[i+1] = array[i];
            tail++;
            return first;
        }
        else {
            head--;
            return (E)array[head+1];
        }

    }
    @Override
    public E pollLast() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        size--;
        if(tail == array.length){
            E last = (E)array[0];
            for(int i = 1; i <= head; i++)
                array[i-1] = array[i];
            head--;
            return last;
        }
        else {
            tail++;
            return (E)array[tail-1];
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////


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
}
