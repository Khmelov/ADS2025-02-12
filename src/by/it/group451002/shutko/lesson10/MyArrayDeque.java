package by.it.group451002.shutko.lesson10;

import java.util.*;


// Создать собственную реализацию двусторонней очереди (Deque) на базе
// массива без использования стандартных библиотек Java.
public class MyArrayDeque<E> implements Deque<E> {

    // Используется массив elements для хранения элементов
    private E[] elements = (E[]) new Object[]{};

    // Переменная size отслеживает текущее количество элементов
    int size = 0;
    @Override
    // формирует строковое представление
    public String toString() {
        StringBuilder sb  = new StringBuilder("[");
        String del = "";
        for (int i = 0; i< size; i++){
            sb.append(del).append(elements[i]);
            del = ", ";
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    // добавляет элемент в начало, сдвигая все эле-ты вправо
    public void addFirst(E e) {
        // Проверяется, заполнен ли текущий массив, если массив заполнен, создается новый массив большего размера
        // копируя все элементы из старого массива в новый
        if (size == elements.length) elements = Arrays.copyOf(elements,size*3/2+1);
        System.arraycopy(elements,0,elements ,1,size);
        elements[0] = e;
        size++;
    }
    @Override
    // добавляет элемент в конец (идентичен методу add())
    public void addLast(E e) {
        if (size == elements.length) elements = Arrays.copyOf(elements,size*3/2+1);
        elements[size] = e;
        size++;
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
    public E pollFirst() {
        return poll();
    }

    @Override
    // удаляет последний элемент (уменьшает размер, возвращает последний эл-т)
    public E pollLast() {
        if (size==0) return null;
        E del = elements[--size];
        return del;
    }

    @Override
    // возвращают первый элемент (идентичен методу element() )
    public E getFirst() {
        return size>0?elements[0]:null;
    }

    @Override
    // возвращают последний элемент
    public E getLast() {
        return size>0?elements[size-1]:null;
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
    // добавляет элемент в конец
    public boolean add(E e) {
        addLast(e);
        return true;
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
    // удаляют первый элемент
    public E poll() {
        if (size==0) return null;
        E del = elements[0];
        System.arraycopy(elements,1,elements ,0,size - 1);
        size--;
        return del;
    }

    @Override
    // возвращают первый элемент
    public E element() {
        return size>0?elements[0]:null;
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
    public int size() {
        return size;
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
