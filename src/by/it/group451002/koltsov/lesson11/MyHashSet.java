package by.it.group451002.koltsov.lesson11;

//    Задание на уровень А
//
//    Создайте class MyHashSet<E>, который реализует интерфейс Set<E>
//    и работает на основе массива с адресацией по хеш-коду
//    и односвязным списком для элементов с коллизиями
//    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
//
//    Метод toString() может выводить элементы в произвольном порядке
//    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
//    быть такими же как в методе toString() обычной коллекции
//
//    /////////////////////////////////////////////////////////////////////////
//    //////               Обязательные к реализации методы             ///////
//    /////////////////////////////////////////////////////////////////////////
//
//    size()
//    clear()
//    isEmpty()
//    add(Object)
//    remove(Object)
//    contains(Object)

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    final int arrSize = 100;
    int size = 0;
    Object elems[] = new Object[arrSize];

    public MyHashSet() {
        for (int i = 0; i < arrSize; i++) {
            elems[i] = new MyList<E>();
        }
    }

    // Методы, необходимые для реализации

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elems.length; i++) {
            ((MyList<E>)elems[i]).clear();
        }
        size = 0;
    }

    @Override
    public String toString() {
        boolean isElemAdded = false;
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < elems.length; i++) {
            Node<E> tempNode = ((MyList<E>)elems[i]).head;
            for (int j = 0; j < ((MyList<E>)elems[i]).size; j++) {
                isElemAdded = true;
                tempNode = tempNode.next;
                resStr.append(tempNode.value.toString() + ", ");
            }
        }

        if (isElemAdded) {
            resStr.deleteCharAt(resStr.length() - 1);
            resStr.deleteCharAt(resStr.length() - 1);
        }
        resStr.append("]");

        return resStr.toString();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (((MyList<E>)elems[hash(e)]).add(e))
        {
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (((MyList<E>)elems[hash((E)o)]).remove((E)o))
        {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return ((MyList<E>)elems[hash((E)o)]).contains((E)o);
    }

    public int hash(E value) {
        return value.hashCode() % arrSize;
    }

    // Методы дальше можно не реализовывать

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
