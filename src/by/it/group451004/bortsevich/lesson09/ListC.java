package by.it.group451004.bortsevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // Внутренний массив для хранения элементов списка
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int INITIAL_CAPACITY = 10;

    // Конструктор - инициализирует пустой список
    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // Вспомогательный метод для увеличения емкости массива
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        // Если текущей емкости недостаточно, увеличиваем массив
        if (minCapacity > elements.length) {
            // Увеличиваем емкость минимум в 1.5 раза
            int newCapacity = Math.max(elements.length + (elements.length >> 1), minCapacity);
            E[] newElements = (E[]) new Object[newCapacity];
            // Копируем элементы в новый массив
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Вспомогательный метод для проверки индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Вспомогательный метод для проверки индекса при добавлении
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////// Обязательные к реализации методы /////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        sb.append(elements[0]);

        for (int i = 1; i < size; i++) {
            sb.append(", ").append(elements[i]);
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        // Гарантируем достаточную емкость
        ensureCapacity(size + 1);
        // Добавляем элемент в конец
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Проверяем валидность индекса
        checkIndex(index);

        // Сохраняем удаляемый элемент
        E removedElement = elements[index];

        // Вычисляем количество элементов для сдвига
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            // Сдвигаем элементы влево
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        // Очищаем последнюю ссылку и уменьшаем размер
        elements[--size] = null;

        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        // Проверяем валидность индекса для добавления
        checkIndexForAdd(index);

        // Гарантируем достаточную емкость
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо чтобы освободить место
        System.arraycopy(elements, index, elements, index + 1, size - index);

        // Вставляем новый элемент
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Ищем индекс элемента
        int index = indexOf(o);
        if (index >= 0) {
            // Если нашли - удаляем по индексу
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        // Проверяем валидность индекса
        checkIndex(index);

        // Сохраняем старый элемент и заменяем новым
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем все ссылки для помощи сборщику мусора
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Проходим по массиву в поисках элемента
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (elements[i] == null) {
                    return i;
                }
            } else {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        // Проверяем валидность индекса
        checkIndex(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        // Используем indexOf для проверки наличия
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Проходим по массиву с конца
        for (int i = size - 1; i >= 0; i--) {
            if (o == null) {
                if (elements[i] == null) {
                    return i;
                }
            } else {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции c содержатся в этом списке
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Если коллекция пуста, ничего не делаем
        if (c.isEmpty()) {
            return false;
        }

        // Гарантируем достаточную емкость
        ensureCapacity(size + c.size());

        // Добавляем все элементы в конец
        for (E element : c) {
            elements[size++] = element;
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверяем валидность индекса для добавления
        checkIndexForAdd(index);

        // Если коллекция пуста, ничего не делаем
        if (c.isEmpty()) {
            return false;
        }

        int numNew = c.size();
        // Гарантируем достаточную емкость
        ensureCapacity(size + numNew);

        // Сдвигаем существующие элементы вправо
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }

        // Копируем новые элементы
        Iterator<? extends E> it = c.iterator();
        for (int i = 0; i < numNew; i++) {
            elements[index + i] = it.next();
        }

        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Флаг изменения списка
        boolean modified = false;

        // Проходим по списку и удаляем элементы, содержащиеся в c
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем счетчик, так как элементы сдвинулись
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Флаг изменения списка
        boolean modified = false;

        // Проходим по списку и удаляем элементы, НЕ содержащиеся в c
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем счетчик, так как элементы сдвинулись
                modified = true;
            }
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////// Опциональные к реализации методы /////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверяем валидность диапазона
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                    "fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        // Создаем новый список для подсписка
        ListC<E> subList = new ListC<>();
        // Копируем элементы из заданного диапазона
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(elements[i]);
        }
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // Простая реализация базового ListIterator
        return new ListIterator<E>() {
            private int currentIndex = index;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = currentIndex;
                return elements[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = --currentIndex;
                return elements[currentIndex];
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }

            @Override
            public void set(E e) {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.set(lastReturned, e);
            }

            @Override
            public void add(E e) {
                ListC.this.add(currentIndex++, e);
                lastReturned = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если массив слишком мал, создаем новый массив того же типа
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }

        // Копируем элементы в переданный массив
        System.arraycopy(elements, 0, a, 0, size);

        // Если в массиве остались лишние элементы, устанавливаем null после последнего
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        // Создаем новый массив Object и копируем элементы
        return java.util.Arrays.copyOf(elements, size);
    }

    /////////////////////////////////////////////////////////////////////////
    ////////// Методы для итератора (нужны для корректной отладки) //////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        // Простой итератор для прохода по элементам
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = currentIndex;
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }
        };
    }
}