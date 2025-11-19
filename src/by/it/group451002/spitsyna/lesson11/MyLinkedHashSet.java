package by.it.group451002.spitsyna.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E>{
        E value;
        Node<E> next;
        Node<E> before; // указатель на предыдущий
        Node<E> after; //указатель на следующий

        Node(E value){
            this.value = value;
        }
    }

    private int capacity = 16;
    private Node<E>[] arr = (Node<E>[]) new Node[capacity];
    private int size = 0;
    final double LOAD_COEF = 0.75;
    private Node<E> head = null;
    private Node<E> tail = null;

    private int hashFunc(E elem){
        if (elem == null)
            return 0;
        int hashCode = elem.hashCode();
        return Math.abs(hashCode % capacity);
    }

    private void checkSize(){
        if (size >= LOAD_COEF*capacity){
            capacity *= 2;
            Node<E>[] newArr = (Node<E>[]) new Node[capacity];

            for (int i = 0; i < arr.length; i++){
                Node<E> list = arr[i];
                while (list != null){
                    Node<E> temp = list.next;

                    list.next = newArr[hashFunc(list.value)];
                    newArr[hashFunc(list.value)] = list.next;

                    list = temp;
                }
            }
            arr = newArr;
        }
    }

    //Обязательные для реализации методы
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        Node<E> currElem = head;

        while (currElem != null){
            str.append(currElem.value);

            if (currElem.after != null)
                str.append(", ");
            currElem = currElem.after;
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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object o) {
        boolean flag = false;

        Node<E> currList = arr[hashFunc((E)o)];
        while (currList != null && !flag){
            if (Objects.equals(currList.value, o))
                flag = true;
            currList = currList.next;
        }
        return flag;
    }

    @Override
    public boolean add(E e) {
        if (!contains(e)){
            Node<E> newElem = new Node<>(e);
            newElem.next = arr[hashFunc(e)];

            //добавление в двунаправленный список
            if (head == null && tail == null){
                head = newElem;
                tail = head;
            }
            else {
                tail.after = newElem;
                newElem.before = tail;
                tail = newElem;
            }
            arr[hashFunc(e)] = newElem;

            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)){
            //удаляем из хеш-таблицы
            Node<E> currList = arr[hashFunc((E) o)];
            boolean flag;
            Node<E> elemToRem = null;
            //если элемент в начале
            if (Objects.equals(currList.value, o)) {
                arr[hashFunc((E) o)] = currList.next;
                elemToRem = currList;
            }
            else {
                flag = false;
                //если элемент не в начале
                while (currList != null && currList.next != null && !flag) {
                    if (Objects.equals(currList.next.value, o)) {
                        elemToRem = currList.next;
                        currList.next = currList.next.next;
                        flag = true;
                    }
                    currList = currList.next;
                }
            }

            //удаляем из двунаправленного списка
            //если это единственный элемент
            if (head == tail){
                head = null;
                tail = null;
            }
            //если это первый элемент, но не единственный
            else if (elemToRem == head){
                elemToRem.after.before = null;
                head = elemToRem.after;
            }
            //если это последний элемент, но не единственный
            else if (elemToRem == tail){
                elemToRem.before.after = null;
                tail = elemToRem.before;
            }
            else {
                //в остальных случаях
                elemToRem.after.before = elemToRem.before;
                elemToRem.before.after = elemToRem.after;
            }
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Object[] collecArr = c.toArray();
        for (int i = 0; i < collecArr.length; i++){
            if (!contains(collecArr[i]))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {//коллекция состоит из элементов типа E и его наследников
        Object[] arrToAdd = c.toArray();
        boolean flag = false;
        for (int i = 0; i < arrToAdd.length; i++){
            if (add((E) arrToAdd[i])){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Object[] arrToRem = c.toArray();
        boolean flag = false;
        for (int i = 0; i < arrToRem.length; i++){
            if (remove(arrToRem[i]))
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean flag = false;
        for (int i = 0; i < arr.length; i++){
            Node<E> currList = arr[i];
            while (currList != null){
                if (!c.contains(currList.value)){
                    remove(currList.value);
                    flag = true;
                }
                currList = currList.next;
            }
        }
        return flag;
    }

    //Необязательные для реализации методы
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

}
