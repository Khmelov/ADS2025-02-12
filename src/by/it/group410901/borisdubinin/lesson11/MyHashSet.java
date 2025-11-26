package by.it.group410901.borisdubinin.lesson11;

import by.it.group410901.borisdubinin.lesson10.MyLinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int[] PRIMES = {31, 67, 131, 257, 521, 1031, 2053, 4099, 8209};
    private MyLinkedList<E>[] array;
    private int size;

    public MyHashSet(){
        size = 0;
        array = (MyLinkedList<E>[]) new MyLinkedList[PRIMES[0]];
        // Инициализируем все ячейки
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyLinkedList<>();
        }
    }

    private int getNextPrime(int currentCapacity) {
        // Ищем первое простой число больше текущего размера
        for (int prime : PRIMES) {
            if (prime > currentCapacity) {
                return prime;
            }
        }
        // Если все числа в массиве меньше, вычисляем следующее
        return findNextPrime(currentCapacity * 2);
    }

    private int findNextPrime(int number) {
        while (!isPrime(number)) {
            number++;
        }
        return number;
    }

    private boolean isPrime(int number) {
        if (number < 2) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;

        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) return false;
        }
        return true;
    }

    private int getIndex(E element) {
        if (element == null) {
            return 0;
        }

        int hash = element.hashCode();

        // Дополнительные битовые операции
        hash = (hash ^ (hash >> 16)) * 0x45d9f3b;
        hash = (hash ^ (hash >> 16)) * 0x45d9f3b;
        hash = hash ^ (hash >> 16);

        // Учет размера массива
        return Math.abs(hash) % array.length;
    }

    private void ensureCapacity(){
        if(size <= array.length*0.75)
            return;

        MyLinkedList<E>[] oldArray = array;
        int oldCapacity = array.length;

        array = (MyLinkedList<E>[]) new MyLinkedList[getNextPrime(oldCapacity)];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyLinkedList<>();
        }
        size = 0;

        for(MyLinkedList<E> oldCell: oldArray){
            while(!oldCell.isEmpty()){
                E elem = oldCell.poll();
                add(elem);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (MyLinkedList<E> bucket : array) {
            if(!bucket.isEmpty()){
                String bucketStr = bucket.toString();
                sb.append(bucketStr, 1, bucketStr.length()-1);
                sb.append(", ");
            }
        }
        sb.setLength(sb.length()-2);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        array = (MyLinkedList<E>[]) new MyLinkedList[PRIMES[0]];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyLinkedList<>();
        }
    }

    @Override
    public boolean isEmpty() {
        return size < 1;
    }

    @Override
    public boolean add(E e) {
        // Проверяем необходимость расширения массива
        ensureCapacity();

        int index = getIndex(e);
        MyLinkedList<E> bucket = array[index];

        // Проверяем, нет ли уже такого элемента в bucket
        if (bucket.contains(e)) {
            return false; // Элемент уже существует
        }

        // Добавляем элемент в bucket
        bucket.add(e);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return array[0].remove(null);
        }
        boolean wasRemoved = array[getIndex((E) o)].remove(o);
        if(wasRemoved)
            size--;
        return wasRemoved;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return array[0].contains(null);
        }

        return array[getIndex((E)o)].contains(o);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

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
    public boolean containsAll(Collection<?> c) {
        return false;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}
