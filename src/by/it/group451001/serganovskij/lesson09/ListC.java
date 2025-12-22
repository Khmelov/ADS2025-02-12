package by.it.group451001.serganovskij.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // ВНУТРЕННЯЯ СТРУКТУРА ДАННЫХ
    private static final int INITIAL_CAPACITY = 10; // Начальная емкость массива
    private Object[] elements; // Массив для хранения элементов
    private int size; // Текущее количество элементов

    // КОНСТРУКТОР - создает пустой список
    public ListC() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    // БАЗОВЫЕ МЕТОДЫ СПИСКА (ОБЯЗАТЕЛЬНАЯ РЕАЛИЗАЦИЯ)
    /////////////////////////////////////////////////////////////////////////

    // СТРОКОВОЕ ПРЕДСТАВЛЕНИЕ - формат [элемент1, элемент2, ...]
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // ДОБАВЛЕНИЕ В КОНЕЦ - с автоматическим расширением массива
    @Override
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    // УДАЛЕНИЕ ПО ИНДЕКСУ - со сдвигом элементов
    @Override
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) elements[index];

        // Сдвиг элементов для заполнения пустоты
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // Очистка последней ссылки
        return removedElement;
    }

    // ПОЛУЧЕНИЕ РАЗМЕРА СПИСКА
    @Override
    public int size() {
        return size;
    }

    // ВСТАВКА ПО ИНДЕКСУ - со сдвигом элементов вправо
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity();

        // Сдвиг элементов для освобождения места
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // УДАЛЕНИЕ ПЕРВОГО ВХОЖДЕНИЯ ОБЪЕКТА (с null-безопасностью)
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // ЗАМЕНА ЭЛЕМЕНТА ПО ИНДЕКСУ - возвращает старый элемент
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // ПРОВЕРКА НА ПУСТОЙ СПИСОК
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ПОЛНАЯ ОЧИСТКА СПИСКА
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // ПОИСК ИНДЕКСА ПЕРВОГО ВХОЖДЕНИЯ
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    // ПОЛУЧЕНИЕ ЭЛЕМЕНТА ПО ИНДЕКСУ
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    // ПРОВЕРКА НАЛИЧИЯ ЭЛЕМЕНТА
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // ПОИСК ИНДЕКСА ПОСЛЕДНЕГО ВХОЖДЕНИЯ
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    // МЕТОДЫ ДЛЯ РАБОТЫ С КОЛЛЕКЦИЯМИ
    /////////////////////////////////////////////////////////////////////////

    // ПРОВЕРКА НАЛИЧИЯ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    // ДОБАВЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ В КОНЕЦ
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for (E item : c) add(item);
        return true;
    }

    // ДОБАВЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ ПО ИНДЕКСУ
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) return false;

        int i = index;
        for (E item : c) add(i++, item);
        return true;
    }

    // УДАЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ, ПРИСУТСТВУЮЩИХ В КОЛЛЕКЦИИ
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Коррекция индекса после сдвига
                modified = true;
            }
        }
        return modified;
    }

    // СОХРАНЕНИЕ ТОЛЬКО ЭЛЕМЕНТОВ ИЗ КОЛЛЕКЦИИ
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Коррекция индекса после сдвига
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    // РАСШИРЕННЫЕ МЕТОДЫ (ОПЦИОНАЛЬНАЯ РЕАЛИЗАЦИЯ)
    /////////////////////////////////////////////////////////////////////////

    // СОЗДАНИЕ ПОДСПИСКА - часть элементов от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex - 1); // toIndex исключается
        if (fromIndex > toIndex) throw new IllegalArgumentException("fromIndex > toIndex");

        ListC<E> subList = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add((E) elements[i]);
        }
        return subList;
    }

    // СОЗДАНИЕ LISTITERATOR С НАЧАЛА СПИСКА
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    // СОЗДАНИЕ LISTITERATOR С УКАЗАННОЙ ПОЗИЦИИ
    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);
        return new ListIterator<E>() {
            private int currentIndex = index;
            private int lastReturned = -1; // Для отслеживания последней операции

            // ПРОВЕРКА НАЛИЧИЯ СЛЕДУЮЩЕГО ЭЛЕМЕНТА
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            // ПОЛУЧЕНИЕ СЛЕДУЮЩЕГО ЭЛЕМЕНТА
            @Override
            public E next() {
                lastReturned = currentIndex;
                return (E) elements[currentIndex++];
            }

            // ПРОВЕРКА НАЛИЧИЯ ПРЕДЫДУЩЕГО ЭЛЕМЕНТА
            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            // ПОЛУЧЕНИЕ ПРЕДЫДУЩЕГО ЭЛЕМЕНТА
            @Override
            public E previous() {
                lastReturned = currentIndex - 1;
                return (E) elements[--currentIndex];
            }

            // ИНДЕКС СЛЕДУЮЩЕГО ЭЛЕМЕНТА
            @Override
            public int nextIndex() {
                return currentIndex;
            }

            // ИНДЕКС ПРЕДЫДУЩЕГО ЭЛЕМЕНТА
            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            // УДАЛЕНИЕ ПОСЛЕДНЕГО ВОЗВРАЩЕННОГО ЭЛЕМЕНТА
            @Override
            public void remove() {
                if (lastReturned == -1) throw new IllegalStateException();
                ListC.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }

            // ЗАМЕНА ПОСЛЕДНЕГО ВОЗВРАЩЕННОГО ЭЛЕМЕНТА
            @Override
            public void set(E e) {
                if (lastReturned == -1) throw new IllegalStateException();
                ListC.this.set(lastReturned, e);
            }

            // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В ТЕКУЩУЮ ПОЗИЦИЮ
            @Override
            public void add(E e) {
                ListC.this.add(currentIndex++, e);
                lastReturned = -1; // Сброс после добавления
            }
        };
    }

    // ПРЕОБРАЗОВАНИЕ В МАССИВ ОБЪЕКТОВ
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    // ПРЕОБРАЗОВАНИЕ В ТИПИЗИРОВАННЫЙ МАССИВ
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создание нового массива нужного типа и размера
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // Маркер конца согласно контракту
        }
        return a;
    }

    /////////////////////////////////////////////////////////////////////////
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ И ЗАГЛУШКИ
    /////////////////////////////////////////////////////////////////////////

    // УВЕЛИЧЕНИЕ ЕМКОСТИ МАССИВА ПРИ НЕОБХОДИМОСТИ
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // ПРОВЕРКА КОРРЕКТНОСТИ ИНДЕКСА ДЛЯ ДОСТУПА
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // ПРОВЕРКА КОРРЕКТНОСТИ ИНДЕКСА ДЛЯ ВСТАВКИ
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // ПРОСТОЙ ИТЕРАТОР С ПОДДЕРЖКОЙ УДАЛЕНИЯ
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                lastReturned = currentIndex;
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturned == -1) throw new IllegalStateException();
                ListC.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }
        };
    }
}