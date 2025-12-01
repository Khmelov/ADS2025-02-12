package by.it.group410902.andala.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {
    // тут просто есть (remove(Object), lastIndexOf, contains
    // Массив для хранения элементов списка
    private E[] elements = (E[]) new Object[10]; // начальная ёмкость — 10
    private int size = 0; // текущее количество элементов в списке

    // Увеличивает размер массива в 2 раза при переполнении
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArr = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, elements.length); // копируем старые элементы
            elements = newArr;
        }
    }

    // ============= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =============

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity();         // проверка на переполнение
        elements[size++] = e;     // добавляем элемент и увеличиваем размер
        return true;
    }

    // Вставка элемента по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity(); // проверка на переполнение
        // сдвигаем элементы вправо от позиции index
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);
        E removed = elements[index]; // сохраняем удаляемый элемент
        // сдвигаем элементы влево, чтобы закрыть "дырку"
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // обнуляем последний элемент
        return removed;
    }

    // Удаление элемента по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                remove(i); // удаляем по индексу
                return true;
            }
        }
        return false;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);
        return elements[index];
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);
        E old = elements[index];     // сохраняем старое значение
        elements[index] = element;   // заменяем новым
        return old;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    // Проверка, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Преобразование списка в строку
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

    // Поиск первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o))
                return i;
        }
        return -1;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o))
                return i;
        }
        return -1;
    }

    // ============= НЕОБЯЗАТЕЛЬНЫЕ МЕТОДЫ (заглушки) =============

    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> iterator() { return null; }
}
