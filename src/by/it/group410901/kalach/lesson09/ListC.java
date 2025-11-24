package by.it.group410901.kalach.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements; // Массив для хранения элементов списка
    private int size; // Текущее количество элементов в списке
    private static final int DEFAULT_CAPACITY = 10; // Начальная ёмкость массива

    // Конструктор: создаёт пустой список с начальной ёмкостью массива
    public ListC() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает строковое представление списка в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавляет элемент в конец списка, увеличивая массив при необходимости
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            // Удваивает размер массива, если он заполнен
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size] = e;
        size++;
        return true; // Всегда возвращает true, так как добавление успешно
    }

    // Удаляет элемент по указанному индексу и возвращает его
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];
        // Сдвигает элементы влево, чтобы закрыть "дыру" после удаления
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // Очищает последний элемент
        size--;
        return removedElement;
    }

    // Возвращает текущее количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент по указанному индексу, сдвигая элементы вправо
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size == elements.length) {
            // Удваивает размер массива, если он заполнен
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        // Сдвигает элементы вправо от индекса
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Удаляет первое вхождение указанного объекта из списка
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true; // Возвращает true, если элемент найден и удалён
            }
        }
        return false; // Возвращает false, если элемент не найден
    }

    // Заменяет элемент по указанному индексу и возвращает старый элемент
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает список, удаляя все элементы
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // Очищает ссылки для сборщика мусора
        }
        size = 0;
    }

    // Возвращает индекс первого вхождения объекта или -1, если его нет
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    // Возвращает элемент по указанному индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    // Проверяет, содержится ли объект в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Возвращает индекс последнего вхождения объекта или -1, если его нет
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    // Проверяет, содержит ли список все элементы из указанной коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы из указанной коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    // Добавляет все элементы из коллекции начиная с указанного индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }

    // Удаляет все элементы из списка, которые содержатся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    // Оставляет в списке только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает подсписок от fromIndex (включительно) до toIndex (исключительно)
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        }
        ListA<E> subList = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add((E) elements[i]);
        }
        return subList;
    }

    // Возвращает итератор списка, начиная с указанного индекса (не реализовано)
    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator not implemented");
    }

    // Возвращает итератор списка с начала (не реализовано)
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    // Преобразует список в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаёт новый массив нужного типа и размера, если переданный массив мал
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T element = (T) elements[i];
            a[i] = element;
        }
        if (a.length > size) {
            a[size] = null; // Устанавливает null в конец, если массив больше
        }
        return a;
    }

    // Возвращает массив объектов, содержащий все элементы списка
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
