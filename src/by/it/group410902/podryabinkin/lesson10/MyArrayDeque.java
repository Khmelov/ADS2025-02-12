package by.it.group410902.podryabinkin.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] arr = (E[]) new Object[0];

    @Override
    public String toString(){
        if(arr.length == 0) return "[]";
        String out = "[";
        for(int i = 0; i < arr.length; i++){
            out += arr[i];
            if(i != arr.length - 1) out += ", ";
        }
        out += "]";
        return out;
    }
    @Override
    public int size(){
        return arr.length;
    }

    @Override
    public boolean add(E element){
        E[] arr2 = (E[]) new Object[arr.length + 1]; //java не умеет в автоматический интерпретатор
        for(int i = 0; i < arr.length; i++){
            arr2[i] = arr[i];
        }
        arr2[arr2.length-1] = element;
        arr = arr2;
        return true;
    }

    @Override
    public void addFirst(E element){
        E[] arr2 = (E[]) new Object[arr.length + 1]; //java не умеет в автоматический интерпретатор
        for(int i = 1; i < arr.length + 1; i++){
            arr2[i] = arr[i -1];
        }
        arr2[0] = element;
        arr = arr2;
    }
    @Override
    public void addLast(E element){
        add(element);
    }

    //то же, что и getFirst, но из какого-то другого интерфейса наследуется
    @Override
    public E element(){
        if(arr.length == 0){
//            throw new NoSuchElementException("Deque is empty");
            return null;
        }
        return arr[0];
    }
    @Override
    public E getFirst(){
        if(arr.length == 0){
//            throw new NoSuchElementException("Deque is empty");
            return null;
        }
        return arr[0];
    }
    @Override
    public E getLast(){
        if(arr.length == 0){
//            throw new NoSuchElementException("Deque is empty");
            return null;
        }
        return arr[arr.length-1];
    }

    @Override
    public E poll(){
        if(arr.length == 0){
            return null;
        }
        E tmp = arr[0];
        E[] arr2 = (E[]) new Object[arr.length-1]; //java не умеет в автоматический интерпретатор
        for(int i = 1; i < arr.length; i++){
            arr2[i-1] = arr[i];
        }
        arr = arr2;
        return tmp;
    }
    @Override
    public E pollFirst(){
        if(arr.length == 0){
            return null;
        }
        E tmp = arr[0];
        E[] arr2 = (E[]) new Object[arr.length-1]; //java не умеет в автоматический интерпретатор
        for(int i = 1; i < arr.length; i++){
            arr2[i-1] = arr[i];
        }
        arr = arr2;
        return tmp;
    }
    @Override
    public E pollLast() {
        if(arr.length == 0){
            return null;
        }
        E tmp = arr[arr.length - 1];
        E[] arr2 = (E[]) new Object[arr.length-1]; //java не умеет в автоматический интерпретатор
        for(int i = 0; i < arr.length - 1; i++){
            arr2[i] = arr[i];
        }
        arr = arr2;
        return tmp;
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


}
