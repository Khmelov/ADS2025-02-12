package by.it.group451001.buiko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elementData;

    public ListA(int capacity) {
        if(capacity > 0) {
            elementData = new Object[capacity];
        }
        else {
            elementData = new Object[0];
        }
    }
    public ListA() {
        this(0);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String sb = new String();
        // Если список пустой, сразу возвращаем "[]"
        if(size == 0) {return "[]";}

        // Начинаем формировать строку с открывающей скобки и первого элемента
        sb += '[' + elementData[0].toString();

        // Добавляем остальные элементы через запятую
        for (int i = 1; i < size; i++) {
            sb += ", " + elementData[i].toString();
        }

        // Закрываем скобку и возвращаем результат
        sb += "]";
        return sb;
    }

    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли увеличивать вместимость массива
        if(size == elementData.length) {
            // Создаем новый массив с увеличенной вместимостью:
            // старая длина + 50% от старой длины + 1 (стандартный подход в ArrayList)
            Object[] newElementData = new Object[elementData.length + (elementData.length >> 1) + 1];

            // Копируем все элементы из старого массива в новый
            System.arraycopy(elementData, 0, newElementData, 0, elementData.length);

            // Заменяем ссылку на массив
            elementData = newElementData;
        }

        // Добавляем новый элемент в конец и увеличиваем счетчик размера
        elementData[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Сохраняем ссылку на удаляемый элемент для возврата
        E Old = (E)elementData[index];

        // Сдвигаем все элементы справа от удаляемого на одну позицию влево
        // Это перезапишет удаляемый элемент
        System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);

        // Обнуляем последнюю ссылку (теперь она дублируется) и уменьшаем размер
        elementData[--size] = null;

        // Возвращаем удаленный элемент
        return Old;
    }

    @Override
    public int size() {
        // Возвращаем текущее количество элементов в списке
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
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
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

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
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
