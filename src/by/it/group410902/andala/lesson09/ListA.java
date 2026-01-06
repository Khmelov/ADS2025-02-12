package by.it.group410902.andala.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    // Массив для хранения элементов списка
    private E[] elements = (E[]) new Object[10]; // начальный размер — 10
    private int size = 0;                        // текущее количество элементов

    // Увеличивает размер массива, если он заполнен
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArr = (E[]) new Object[elements.length * 2]; // удваиваем размер
            System.arraycopy(elements, 0, newArr, 0, elements.length); // копируем старые элементы
            elements = newArr;
        }
    }

    // ---------- ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ----------

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity();             // проверка на переполнение
        elements[size++] = e;         // добавляем элемент и увеличиваем size
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);

        E removed = elements[index];  // сохраняем удаляемый элемент
        int moved = size - index - 1; // сколько элементов нужно сдвинуть
        if (moved > 0)
            System.arraycopy(elements, index + 1, elements, index, moved); // сдвигаем
        elements[--size] = null;      // обнуляем последний элемент
        return removed;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Возвращает строковое представление списка
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
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    // Вставка элемента по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity(); // проверка на переполнение
        System.arraycopy(elements, index, elements, index + 1, size - index); // сдвигаем вправо
        elements[index] = element;
        size++;
    }

    // Удаление элемента по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                remove(i); // используем remove по индексу
                return true;
            }
        }
        return false;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    // ---------- ПРОСТЫЕ РЕАЛИЗАЦИИ ОСТАВШИХСЯ МЕТОДОВ ----------

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Поиск первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(elements[i], o))
                return i;
        return -1;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // ---------- МЕТОДЫ-ЗАГЛУШКИ (не реализованы полностью) ----------

    @Override public int lastIndexOf(Object o) { return -1; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
