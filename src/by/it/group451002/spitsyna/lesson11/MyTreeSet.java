package by.it.group451002.spitsyna.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyTreeSet<E> implements Set<E>{
    private int capacity = 16;
    private Object[] arr = new Object[capacity];
    private int size = 0;

    private void checkCapacity(){
        if (size+1 > capacity){
            capacity *= 2;
            Object[] newArr = new Object[capacity];

            for (int i = 0; i < arr.length; i++)
                newArr[i] = arr[i];
            arr = newArr;
        }
    }

    private int compare(E e1, E e2){
        return ((Comparable<E>) e1).compareTo(e2); //возвращает >0 , если e1>e2; возвращает <0, если e1<e2
    }

    private int binarySearch(E elem){
        int left = 0;
        int right = size-1;
        int mid = 0;

        while (right >= left){
            mid = (left+right) / 2;
            int cmp = compare((E) arr[mid], elem);

            if (cmp > 0)
                right = mid - 1;
            if (cmp < 0)
                left = mid + 1;
            if (cmp == 0)
                return mid;
        }
        return -left-1;
    }

    //Обязательные для реализации
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        for (int i = 0; i < size; i++){
            str.append(arr[i]);
            if (i < size-1)
                str.append(", ");
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < arr.length; i++)
            arr[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object o) {
        checkCapacity();
        int index = binarySearch((E) o);
        if (index < 0){
            index = -(index + 1);
            //сдвигаем элементы массива
            for (int i = size; i > index; i--)
                arr[i] = arr[i-1];

            arr[index] = (E) o;
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch((E) o);
        if (index >= 0){
            //сдвигаем элементы
            for (int i = index; i < size-1; i++){
                arr[i] = arr[i+1];
            }
            arr[--size] = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (binarySearch((E) o) < 0)
            return false;
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        Object[] collecArr = c.toArray();
        for (int i = 0; i < collecArr.length; i++){
            if (!contains(collecArr[i]))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection c) {
        Object[] collecArr = c.toArray();
        boolean flag = false;
        for (int i = 0; i < collecArr.length; i++){
            if (add(collecArr[i]))
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection c) {
        Object[] collecArr = c.toArray();
        boolean flag = false;
        for (int i = 0; i < collecArr.length; i++){
            if (remove(collecArr[i]))
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection c) {
        boolean flag = false;
        //идем с конца
        for (int i = size-1; i >= 0; i--){
            if (!c.contains(arr[i])){
                remove(arr[i]);
                flag = true;
            }
        }
        return flag;
    }

    //Необязательные для реализации
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
}
