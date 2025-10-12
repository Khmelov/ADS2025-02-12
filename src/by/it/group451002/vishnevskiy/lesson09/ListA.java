package by.it.group451002.vishnevskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.Objects;

// Класс ListA<E> реализует интерфейс List<E>,
// создаваем собственный список на основе динамического массива.
public class ListA<E> implements List<E> {

    // Массив для хранения элементов списка
    private E[] elements = (E[]) new Object[10]; // начальная ёмкость — 10 элементов
    private int size = 0; // количество реально добавленных элементов

    // Метод для красивого вывода списка в виде строки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // добавляем элемент
            if (i < size - 1)
                sb.append(", "); // разделяем запятой
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавление нового элемента в конец списка
    @Override
    public boolean add(E e) {
        // если массив заполнен — увеличиваем его размер в 2 раза
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);

        // вставляем элемент в конец
        elements[size++] = e;
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        // проверка корректности индекса
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        // сохраняем удаляемый элемент, чтобы вернуть его
        E removed = elements[index];

        // сдвигаем все элементы после index на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // уменьшаем размер и очищаем последний элемент
        elements[--size] = null;
        return removed;
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Вставка элемента по индексу
    @Override
    public void add(int index, E element) {
        // проверка индекса
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        // при необходимости увеличиваем массив
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);

        // сдвигаем элементы вправо, чтобы освободить место
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        // вставляем элемент
        elements[index] = element;
        size++;
    }

    // Удаление элемента по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) { // сравнение с учётом null
                remove(i); // используем remove(int index)
                return true;
            }
        }
        return false;
    }

    // Замена элемента по индексу, возвращает старое значение
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        E old = elements[index];
        elements[index] = element;
        return old;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // очищаем ссылки
        }
        size = 0;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return elements[index];
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверка, содержит ли список элемент
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, elements[i]))
                return i;
        return -1;
    }

    // Поиск последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--)
            if (Objects.equals(o, elements[i]))
                return i;
        return -1;
    }

    // Остальные методы не требуются для базового функционала

    @Override public Iterator<E> iterator() { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }

    // Добавление всех элементов другой коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    // Добавление коллекции по индексу
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int i = index;
        for (E e : c) {
            add(i++, e);
        }
        return !c.isEmpty();
    }

    // Проверка: содержит ли список все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    // Удаление всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            while (remove(o)) changed = true;
        return changed;
    }

    // Оставляет только те элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i--); // после удаления нужно вернуть индекс назад
                changed = true;
            }
        }
        return changed;
    }

    // Возврат массива с копией данных
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    // Возврат массива указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
}

