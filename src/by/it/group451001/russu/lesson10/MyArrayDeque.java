package by.it.group451001.russu.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque
{
    E[] mas;
    int count;

    @Override
    public String toString()
    {
        if (count<=0) return "[]";
        StringBuilder sb = new StringBuilder();
        if (count>0){
            sb.append("[");


            for (int i = 0; i< count;i++)
            {
                sb.append(mas[i] + ", ");
            }
            sb.delete(sb.length()-2,sb.length());
            sb.append("]");}
        return sb.toString();
    }

    @Override
    public void addFirst(Object o)
    {
        E[] arr = (E[]) new Object[++count];
        arr[0] = (E)o;

        for (int i=1;i<count;i++)
        {
            arr[i] = mas[i-1];
        }
        mas = arr;
    }

    @Override
    public void addLast(Object o)
    {
        E[] arr = (E[]) new Object[++count];

        for (int i=0;i<count-1;i++)
        {
            arr[i] = mas[i];
        }
        arr[count-1] = (E)o;
        mas = arr;
    }

    @Override
    public boolean offerFirst(Object o) {
        return false;
    }

    @Override
    public boolean offerLast(Object o) {
        return false;
    }

    @Override
    public Object removeFirst() {
        return null;
    }

    @Override
    public Object removeLast() {
        return null;
    }

    @Override
    public Object pollFirst() {
        E elem = mas[0];
        E[] arr = (E[]) new Object[--count];

        for (int i=0;i<count;i++)
        {
            arr[i] = mas[i+1];
        }
        mas = arr;

        return elem;
    }

    @Override
    public Object pollLast() {
        E elem = mas[count-1];
        E[] arr = (E[]) new Object[--count];

        for (int i=0;i<count;i++)
        {
            arr[i] = mas[i];
        }
        mas = arr;

        return elem;
    }

    @Override
    public Object getFirst() {
        return mas[0];
    }

    @Override
    public Object getLast() {
        return mas[count-1];
    }

    @Override
    public Object peekFirst()
    {
        return null;
    }

    @Override
    public Object peekLast() {
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
    public boolean add(Object o)
    {
        E[] arr = (E[]) new Object[++count];

        for (int i=0;i<count-1;i++)
        {
            arr[i] = mas[i];
        }
        arr[count-1] = (E)o;
        mas = arr;
        return true;
    }

    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public Object remove() {
        return null;
    }

    @Override
    public Object poll() {
        E elem = mas[0];
        E[] arr = (E[]) new Object[--count];

        for (int i=0;i<count;i++)
        {
            arr[i] = mas[i+1];
        }
        mas = arr;

        return elem;
    }

    @Override
    public Object element() {
        if (count<=0) return null;
        return mas[0];
    }

    @Override
    public Object peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public void push(Object o) {

    }

    @Override
    public Object pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public Iterator descendingIterator() {
        return null;
    }
}
