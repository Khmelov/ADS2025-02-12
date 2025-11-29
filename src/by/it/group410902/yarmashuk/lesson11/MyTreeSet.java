package by.it.group410902.yarmashuk.lesson11;

import java.util.*;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final float GROWTH_FACTOR = 1.5f;

    private Object[] elements;
    private int size;
    private int capacity;

    public MyTreeSet() {
        this(DEFAULT_CAPACITY);
    }

    public MyTreeSet(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Начальная емкость не может быть отрицательной.");
        }
        this.capacity = initialCapacity;
        this.elements = new Object[capacity];
        this.size = 0;
    }


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
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;

    }
    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("MyTreeSet не допускает null-элементы.");
        }

        int index = binarySearch(e);

        if (index >= 0) {
            return false; // Элемент уже существует
        }

        // Вычисляем фактический индекс для вставки
        int insertionPoint = -index - 1;

        // Если массив заполнен, расширяем его
        if (size == capacity) {
            grow();
        }

        // Сдвигаем элементы вправо, чтобы освободить место для нового элемента

        if (insertionPoint < size) {
            System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        }

        elements[insertionPoint] = e;
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    private int binarySearch(Object o) {
        if (o == null) {
            throw new NullPointerException("MyTreeSet не допускает null-элементы.");
        }

        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare((E) o, midVal);

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // Элемент найден
            }
        }
        return -(low + 1); // Элемент не найден, возвращаем место для вставки
    }
    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (e1 == null || e2 == null) {
            throw new NullPointerException("MyTreeSet не допускает null-элементы.");
        }
        return ((Comparable<E>) e1).compareTo(e2);
    }
    private void grow() {
        int newCapacity = (int) (capacity * GROWTH_FACTOR);
        if (newCapacity <= capacity) {
            newCapacity = capacity + 1; // Гарантируем хотя бы +1
        }
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        this.elements = newElements;
        this.capacity = newCapacity;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("MyTreeSet не допускает null-элементы.");
        }

        int index = binarySearch(o);

        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево, чтобы удалить текущий
        // Используем System.arraycopy для эффективного сдвига
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Очищаем ссылку на последний элемент и уменьшаем размер
        return true;
    }
    @Override
    public boolean contains(Object o) {
        // binarySearch сам выбрасывает NullPointerException для null
        int index = binarySearch(o);
        return index >= 0; // Если индекс >= 0, значит элемент найден
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Коллекция не может быть null.");
        }

        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Коллекция не может быть null.");
        }
        boolean changed = false;

        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Коллекция не может быть null.");
        }
        boolean changed = false;

        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Коллекция не может быть null.");
        }
        boolean changed = false;

        // Создаем временный массив для хранения элементов, которые нужно сохранить.
        Object[] tempElements = new Object[size];
        int tempSize = 0;


        for (int i = 0; i < size; i++) {
            E currentElement = (E) elements[i];
            // Если внешний набор 'c' содержит текущий элемент, сохраняем его
            if (c.contains(currentElement)) {
                tempElements[tempSize++] = currentElement;
            } else {
                changed = true; // Элемент не будет сохранен, значит, набор изменится
            }
        }

        // Если набор изменился, копируем сохраненные элементы обратно в основной массив
        // и очищаем оставшиеся ячейки.
        if (changed) {
            System.arraycopy(tempElements, 0, elements, 0, tempSize);
            // Очищаем оставшиеся элементы в массиве
            for (int i = tempSize; i < size; i++) {
                elements[i] = null;
            }
            size = tempSize; // Обновляем размер
        }

        return changed;
    }


}
