package by.it.group451002.spitsyna.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E>{
        E value;
        Node<E> next; //изначально инициализируется в null

        Node(E value){
            this.value = value;
        }
    }
    private int capacity = 16;
    private Node<E>[] arr = (Node<E>[]) new Node[capacity];
    private int size = 0;
    final double LOAD_COEF = 0.75;

    //хеш-функция
    private int hashFunc(E elem){
        if (elem == null)
            return 0;
        int hashCode = elem.hashCode();
        return Math.abs(hashCode % capacity);
    }

    private void checkSize(){
        if (capacity*LOAD_COEF <= size){
            Node<E>[] newArr = (Node<E>[]) new Node[capacity*2];
            capacity *= 2;
            //переопределяем позиции элементов для нового массива
            for (int i = 0; i < arr.length; i++){
                Node<E> currElem = arr[i];
                //просматриваем весь список элементов с одинаковым хеш-кодом, определяя их место в новом массиве
                while (currElem != null){
                    Node<E> temp = currElem.next; //сохраняем ссылку на следующий элемент

                    currElem.next = newArr[hashFunc(currElem.value)];
                    newArr[hashFunc(currElem.value)] = currElem;

                    currElem = temp;
                }
            }
            arr = newArr;
        }
    }

    //обязательные методы
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        Node<E> list;
        int count = 0;
        for (int i = 0; i < arr.length; i++){
            list = arr[i];
            while (list != null){
                str.append(list.value);
                count++;
                if (count < size)
                    str.append(", ");
                list = list.next;
            }
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
        for (int i = 0; i < arr.length; i++){
            arr[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object o) {
        boolean flag = false;
        int hashCode = hashFunc((E) o);
        Node<E> list = arr[hashCode];
        while (list != null && !flag){
            if (Objects.equals(list.value, o))
                flag = true;
            list = list.next;
        }
        return flag;
    }

    @Override
    public boolean add(Object o) {
        checkSize();

        if (contains(o))
            return false;
        else {
            int hashCode = hashFunc((E) o);
            Node<E> elem = new Node<>((E) o);
            elem.next = arr[hashCode];
            arr[hashCode] = elem;
            size++;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)){
            int hashCode = hashFunc((E)o);
            Node<E> currElem = arr[hashCode];
            //если удаляем первый элемент
            if (Objects.equals(currElem.value, o))
                arr[hashCode] = currElem.next;
            else {
                boolean flag = false;
                while (currElem != null && currElem.next != null && !flag) {
                    if (Objects.equals(currElem.next.value, o)) {
                        currElem.next = currElem.next.next;
                        flag = true;
                    }
                    currElem = currElem.next;
                }
            }
            size--;
            return true;
        }
        return false;
    }

    //Необязательные методы
    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
