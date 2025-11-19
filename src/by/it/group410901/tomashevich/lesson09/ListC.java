package by.it.group410901.tomashevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
    Задание:
    Реализовать более полный аналог списка (динамического массива).
    Поддержка операций: добавление, удаление, поиск, очистка, работа с коллекциями
    (addAll, removeAll, retainAll).
*/
public class ListC<E> implements List<E> {

    // Массив для хранения элементов
    private E[] elements = (E[]) new Object[10];
    // Количество элементов в списке
    private int size = 0;

    // Увеличение массива
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    // Проверка индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Представление списка в виде строки
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавляет элемент в конец
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = e;
        return true;
    }

    // Удаляет элемент по индексу
    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    // Размер списка
    @Override
    public int size() {
        return size;
    }

    // Вставляет элемент по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == elements.length) {
            grow();
        }
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаляет элемент по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // Заменяет элемент по индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    // Проверяет пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает список
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Индекс первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    // Возвращает элемент по индексу
    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    // Проверяет наличие элемента
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Индекс последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    // Проверяет, содержит ли список все элементы другой коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) return false;
        }
        return true;
    }

    // Добавляет все элементы другой коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return !c.isEmpty();
    }

    // Добавляет коллекцию в указанное место
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        int added = 0;
        for (E e : c) {
            add(index + added, e);
            added++;
        }
        return added > 0;
    }

    // Удаляет все элементы, которые есть в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            while (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    // Оставляет только те элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i--);
                modified = true;
            }
        }
        return modified;
    }

    // Остальные методы оставлены пустыми (необязательная реализация)
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public Iterator<E> iterator() { return null; }
}
