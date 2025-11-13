package by.it.group451002.koltsov.lesson11;


//    Задание на уровень C
//
//    Создайте class MyTreeSet<E>, который реализует интерфейс Set<E>
//    и работает на основе отсортированного массива (любым способом)
//    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
//
//    Метод toString() должен выводить элементы в порядке их возрастания
//    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
//    быть такими же как в методе toString() обычной коллекции
//
//    /////////////////////////////////////////////////////////////////////////
//    //////               Обязательные к реализации методы             ///////
//    /////////////////////////////////////////////////////////////////////////
//
//    toString()
//    size()
//    clear()
//    isEmpty()
//    add(Object)
//    remove(Object)
//    contains(Object)
//
//    containsAll(Collection)
//    addAll(Collection)
//    removeAll(Collection)
//    retainAll(Collection)

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    // Эти методы нужно реализовать
    int size = 0;
    Object[] elems = new Object[0];

    public void grow(int elemsNum) {
        if (size + elemsNum > elems.length)
        {
            Object[] newElems = new Object[(size + elemsNum) * 2];
            System.arraycopy(elems, 0, newElems, 0, size);
            elems = newElems;
        }
    }

    public int getValueIndex(E value) {
        int right = size - 1;
        if (right == -1)
            return -1;
        int left = 0;
        do {
            if (((Comparable<E>)value).compareTo((E)elems[(left + right) / 2]) > 0) {
                // Если элемент больше чем элемент в середине массива, сдвигаем левую границу на середину
                left = (left + right) / 2;
            } else if (((Comparable<E>)value).compareTo((E)elems[(left + right) / 2]) < 0) {
                // Если элемент меньше чем элемент в середине массива, сдвигаем правую границу на середину
                right = (left + right) / 2;
            } else {
                return (left + right) / 2;
            }

            // Если разница между границами >= 1 элемент
            // Возвращаем либо индекс элемента либо индекс, после которого должен следовать элемент
            if (right - left <= 1) {
                if (((Comparable<E>)value).compareTo((E)elems[left]) < 0)
                    return left - 1;
                else if (((Comparable<E>)value).compareTo((E)elems[left]) == 0)
                    return left;
                else if (((Comparable<E>)value).compareTo((E)elems[right]) < 0)
                    return left;
                else if (((Comparable<E>)value).compareTo((E)elems[right]) == 0)
                    return right;
                else if(((Comparable<E>)value).compareTo((E)elems[right]) > 0)
                    return right;
            }
        } while (true);
    }

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < size; i++)
            resStr.append(elems[i].toString()).append(", ");
        if (size > 0)
        {
            resStr.deleteCharAt(resStr.length() - 1);
            resStr.deleteCharAt(resStr.length() - 1);
        }
        resStr.append("]");
        return resStr.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        elems = new Object[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object o) {
        int index = getValueIndex((E)o);
        if (index != -1 && elems[index].equals(o))
            return false;
        grow(1);
        for (int i = size; i > index + 1; i--)
            elems[i] = elems[i - 1];
        elems[index + 1] = o;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getValueIndex((E)o);
        if (index == -1 || !elems[index].equals(o))
            return false;
        for (int i = index; i < size - 1; i++)
            elems[i] = elems[i + 1];
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = getValueIndex((E)o);
        if (index != -1 && elems[index].equals(o))
            return true;
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        boolean IsElemAdded = false;
        for (Object o : c)
            if (add(o))
                IsElemAdded = true;
        return IsElemAdded;
    }

    @Override
    public boolean removeAll(Collection c) {
        boolean IsElemRemoved = false;
        for (Object o : c)
            if (remove(o))
                IsElemRemoved = true;
        return IsElemRemoved;
    }

    @Override
    public boolean retainAll(Collection c) {
        boolean IsElemRemoved = false;
        MyList<Object> deleteList = new MyList<Object>();
        for (int i = 0; i < size; i++)
            if (!c.contains(elems[i]))
                deleteList.add(elems[i]);
        int deleteListSize = deleteList.size;
        for (int i = 0; i < deleteListSize; i++)
            if (remove(deleteList.pop()))
                IsElemRemoved = true;
        return IsElemRemoved;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    // Дальше можно реализовывать

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
