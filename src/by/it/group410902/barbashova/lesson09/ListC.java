package by.it.group410902.barbashova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // Внутренний массив для хранения элементов списка
    private E[] el;

    private int size;

    public ListC(){
        // Инициализируем массив с начальной емкостью 1
        el = (E[]) new Object[1];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Преобразование списка в строку в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(el[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли увеличивать емкость массива
        if (size == el.length) {
            // Увеличиваем емкость в 2 раза
            int newsize = el.length * 2;
            E[] newel = (E[]) new Object[newsize];
            // Копируем старые элементы в новый массив
            System.arraycopy(el, 0, newel, 0, size);
            el = newel;
        }
        // Добавляем элемент и увеличиваем счетчик
        el[size] = e;
        size++;
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        // Сохраняем удаляемый элемент
        E removed = el[index];
        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < size - 1; i++){
            el[i] = el[i + 1];
        }
        // Очищаем последнюю позицию и уменьшаем размер
        el[size - 1] = null;
        size--;
        return removed;
    }

    // Возвращает текущее количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Вставка элемента по указанному индексу
    @Override
    public void add(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        // Проверяем, нужно ли увеличивать емкость
        if (size == el.length) {
            int newsize = el.length * 2;
            E[] newel = (E[]) new Object[newsize];
            System.arraycopy(el, 0, newel, 0, size);
            el = newel;
        }
        // Сдвигаем элементы для освобождения места под новый элемент
        System.arraycopy(el, index, el, index + 1, size - index);
        // Вставляем элемент и увеличиваем размер
        el[index] = element;
        size++;
    }

    // Удаление первого вхождения указанного объекта
    @Override
    public boolean remove(Object o) {
        // Находим индекс объекта
        int index = indexOf(o);
        if (index >= 0) {
            // Удаляем по индексу
            remove(index);
            return true;
        }
        return false;
    }

    // Замена элемента по указанному индексу
    @Override
    public E set(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        // Сохраняем старый элемент
        E oldElement = el[index];
        // Заменяем на новый
        el[index] = element;
        return oldElement;
    }

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает список
    @Override
    public void clear() {
        size = 0;
        // Создаем новый массив минимальной емкости
        E[] newel = (E[]) new Object[1];
        el = newel;
    }

    // Поиск индекса первого вхождения объекта
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++){
            // Используем equals для сравнения (учитывает null)
            if (o == null ? el[i] == null : o.equals(el[i]))
                return i;
        }
        return -1;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return el[index];
    }

    // Проверяет, содержит ли список указанный объект
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск индекса последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--){
            if (o == null ? el[i] == null : o.equals(el[i]))
                return i;
        }
        return -1;
    }

    // Проверяет, содержит ли список все элементы указанной коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element: c) {
            if (!contains(element))
                return false;
        }
        return true;
    }

    // Добавляет все элементы коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e: c){
            add(e);
            modified = true;
        }
        return modified;
    }

    // Добавляет все элементы коллекции начиная с указанного индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверка корректности индекса
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (c == null) throw new NullPointerException();
        if (c.isEmpty()) return false;

        // Добавляем элементы по одному, начиная с указанного индекса
        int currentIndex = index;
        for (E element : c) {
            add(currentIndex, element);
            currentIndex++;
        }
        return true;
    }

    // Удаляет все элементы, которые содержатся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        // Проходим с конца чтобы избежать проблем со сдвигом индексов
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(el[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    // Удаляет все элементы, которые НЕ содержатся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        // Проходим с конца чтобы избежать проблем со сдвигом индексов
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(el[i])) {
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

    // Возвращает подсписок (не реализовано)
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    // Возвращает итератор списка с началом с указанного индекса (не реализовано)
    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    // Возвращает итератор списка (не реализовано)
    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    // Преобразует список в массив указанного типа (не реализовано)
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Преобразует список в массив Object[] (не реализовано)
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает итератор (не реализовано)
    @Override
    public Iterator<E> iterator() {
        return null;
    }
}