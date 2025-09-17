package by.it.group410902.kovalchuck.lesson01.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущий размер списка
    private int size;
    // Начальная емкость по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    // Конструктор
    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";  // Пустой список
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);  // Добавляем элемент
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
// Добавление элемента в конец списка
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size] = e;        // Добавляем элемент в конец
        size++;                    // Увеличиваем счетчик размера
        return true;
    }

    @Override
// Удаление элемента по индексу
    public E remove(int index) {
        checkIndex(index);  // Проверка корректности индекса

        E removedElement = elements[index];  // Сохраняем удаляемый элемент

        // Вычисляем количество элементов для сдвига
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            // Сдвигаем элементы влево
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null;  // Очищаем последнюю ячейку и уменьшаем размер
        return removedElement;    // Возвращаем удаленный элемент
    }

    @Override
// Возвращает текущий размер списка
    public int size() {
        return size;
    }

    @Override
// Вставка элемента по указанному индексу
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity(size + 1);  // Обеспечиваем достаточную емкость
        // Сдвигаем элементы вправо для освобождения места
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;  // Вставляем новый элемент
        size++;                     // Увеличиваем размер
    }

    @Override
// Удаление первого вхождения указанного объекта
    public boolean remove(Object o) {
        if (o == null) {
            // Поиск и удаление null элемента
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    remove(i);  // Удаляем по индексу
                    return true;
                }
            }
        } else {
            // Поиск и удаление не-null элемента
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    remove(i);  // Удаляем по индексу
                    return true;
                }
            }
        }
        return false;  // Элемент не найден
    }

    @Override
// Замена элемента по указанному индексу
    public E set(int index, E element) {
        checkIndex(index);           // Проверка корректности индекса
        E oldValue = elements[index];  // Сохраняем старое значение
        elements[index] = element;   // Устанавливаем новое значение
        return oldValue;             // Возвращаем старое значение
    }

    @Override
// Проверка пустоты списка
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
// Очистка списка
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;  // Обнуляем все ссылки
        }
        size = 0;  // Сбрасываем размер
    }

    @Override
// Поиск индекса первого вхождения элемента
    public int indexOf(Object o) {
        if (o == null) {
            // Поиск null элемента
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            // Поиск не-null элемента
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;  // Элемент не найден
    }

    @Override
// Получение элемента по индексу
    public E get(int index) {
        checkIndex(index);  // Проверка корректности индекса
        return elements[index];
    }

    @Override
// Проверка наличия элемента в списке
    public boolean contains(Object o) {
        return indexOf(o) != -1;  // Используем indexOf для поиска
    }

    @Override
// Поиск индекса последнего вхождения элемента
    public int lastIndexOf(Object o) {
        if (o == null) {
            // Поиск с конца для null элемента
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            // Поиск с конца для не-null элемента
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;  // Элемент не найден
    }

    @Override
// Проверка наличия всех элементов коллекции в списке
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {  // Если хотя бы один элемент отсутствует
                return false;
            }
        }
        return true;  // Все элементы найдены
    }

    @Override
// Добавление всех элементов коллекции в конец списка
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;  // Нечего добавлять
        }

        ensureCapacity(size + c.size());  // Обеспечиваем достаточную емкость
        for (E element : c) {
            elements[size++] = element;  // Добавляем элементы и увеличиваем размер
        }
        return true;
    }

    @Override
// Добавление всех элементов коллекции по указанному индексу
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;  // Нечего добавлять
        }

        int numNew = c.size();  // Количество новых элементов
        ensureCapacity(size + numNew);  // Обеспечиваем достаточную емкость

        int numMoved = size - index;  // Количество элементов для сдвига
        if (numMoved > 0) {
            // Сдвигаем существующие элементы вправо
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }

        // Вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }

        size += numNew;  // Увеличиваем размер
        return true;
    }

    @Override
// Удаление всех элементов, содержащихся в указанной коллекции
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;  // Нечего удалять
        }

        boolean modified = false;  // Флаг изменения списка
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);   // Удаляем элемент
                i--;         // Корректируем индекс из-за сдвига
                modified = true;  // Список был изменен
            }
        }
        return modified;
    }

    @Override
// Удаление всех элементов, НЕ содержащихся в указанной коллекции
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;  // Флаг изменения списка
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);   // Удаляем элемент, которого нет в коллекции
                i--;         // Корректируем индекс из-за сдвига
                modified = true;  // Список был изменен
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Проверяет корректность индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Обеспечивает достаточную емкость массива
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            E[] newElements = (E[]) new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListC<E> subList = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(elements[i]);
        }
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Проверяем, если переданный массив слишком мал для всех элементов
        if (a.length < size) {
            // Создаем новый массив нужного размера того же типа, что и переданный массив
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),  // Получаем тип компонентов массива
                    size);                            // Создаем массив нужного размера

            // Копируем все элементы из внутреннего массива в новый массив
            System.arraycopy(elements, 0,  // исходный массив и начальный индекс
                    result, 0,    // целевой массив и начальный индекс
                    size);        // количество копируемых элементов

            return result;  // Возвращаем новый массив
        }

        // Если переданный массив достаточно велик, копируем элементы в него
        System.arraycopy(elements, 0, a, 0, size);

        // Если в переданном массиве остались лишние ячейки после последнего элемента,
        // устанавливаем следующую ячейку в null
        if (a.length > size) {
            a[size] = null;  // Маркер конца значимых данных в массиве
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
// Возвращает итератор для последовательного обхода элементов списка
    public Iterator<E> iterator() {
        return new Itr();  // Создаем и возвращаем новый экземпляр итератора
    }

    // Реализация интерфейса Iterator для обхода элементов списка
    private class Itr implements Iterator<E> {
        int cursor = 0;       // Индекс следующего возвращаемого элемента
        int lastRet = -1;     // Индекс последнего возвращенного элемента (-1 если не было)

        @Override
        // Проверяет, есть ли следующий элемент в списке
        public boolean hasNext() {
            return cursor < size;  // true если курсор не достиг конца списка
        }

        @Override
        // Возвращает следующий элемент списка
        public E next() {
            int i = cursor;
            if (i >= size) {
                throw new java.util.NoSuchElementException();  // Нет больше элементов
            }
            cursor = i + 1;    // Перемещаем курсор на следующий элемент
            lastRet = i;       // Запоминаем индекс возвращенного элемента
            return elements[lastRet];  // Возвращаем элемент
        }

        @Override
        // Удаляет последний возвращенный элемент методом next()
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();  // next() не был вызван или remove() уже вызывался
            }
            ListC.this.remove(lastRet);  // Удаляем элемент из внешнего списка
            cursor = lastRet;            // Корректируем курсор после удаления
            lastRet = -1;                // Сбрасываем lastRet (удаление выполнено)
        }
    }

    // Реализация ListIterator - расширенный итератор с двунаправленным обходом
    private class ListItr extends Itr implements ListIterator<E> {
        // Конструктор с начальным положением итератора
        ListItr(int index) {
            cursor = index;  // Устанавливаем начальную позицию курсора
        }

        @Override
        // Проверяет, есть ли предыдущий элемент
        public boolean hasPrevious() {
            return cursor > 0;  // true если есть элементы перед текущей позицией
        }

        @Override
        // Возвращает предыдущий элемент и перемещает курсор назад
        public E previous() {
            int i = cursor - 1;  // Индекс предыдущего элемента
            if (i < 0) {
                throw new java.util.NoSuchElementException();  // Нет предыдущих элементов
            }
            cursor = i;       // Перемещаем курсор на предыдущую позицию
            lastRet = i;      // Запоминаем индекс возвращенного элемента
            return elements[i];  // Возвращаем элемент
        }

        @Override
        // Возвращает индекс следующего элемента
        public int nextIndex() {
            return cursor;  // Текущая позиция курсора = индекс следующего элемента
        }

        @Override
        // Возвращает индекс предыдущего элемента
        public int previousIndex() {
            return cursor - 1;  // Индекс элемента перед текущей позицией курсора
        }

        @Override
        // Заменяет последний возвращенный элемент
        public void set(E e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListC.this.set(lastRet, e);  // Заменяем элемент во внешнем списке
        }

        @Override
        // Добавляет новый элемент перед текущей позицией курсора
        public void add(E e) {
            int i = cursor;              // Текущая позиция курсора
            ListC.this.add(i, e);        // Добавляем элемент во внешний список
            cursor = i + 1;              // Сдвигаем курсор вперед
            lastRet = -1;                // Сбрасываем lastRet (add() сбрасывает состояние)
        }
    }
}