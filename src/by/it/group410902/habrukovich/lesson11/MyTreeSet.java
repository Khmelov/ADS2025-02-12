package by.it.group410902.habrukovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
public class MyTreeSet<E extends Comparable<E>> implements Set<E> { //отсортированное множество
    private Object[] elements;
    private int size;

    public MyTreeSet()
    {
        elements = new Object[10];
        size = 0;
    }

    // Увеличивает размер массива при необходимости
    private void ensureCapacity()
    {
        if (size >= elements.length)
        {

            Object[] newArr = new Object[elements.length * 2];
            for (int i = 0; i < size; i++)
            {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    // Бинарный поиск: возвращает индекс элемента или точку вставки
    private int binarySearch(E e)
    {
        int left = 0, right = size - 1;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            int cmp = ((E) elements[mid]).compareTo(e);
            if (cmp == 0) return mid;
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return -left - 1;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return binarySearch(e) >= 0;
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
    public boolean add(E e) {
        int index = binarySearch(e);
        if (index >= 0) return false; // Элемент уже существует

        ensureCapacity();
        index = -index - 1; // Получаем реальную позицию вставки
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];// Сдвигаем элементы вправо
        }
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        int index = binarySearch(e);
        if (index < 0) return false; // Элемент не найден

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1]; // Сдвигаем элементы влево
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    // Оставить только те элементы, которые есть в коллекции
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Возвращает строковое представление множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
