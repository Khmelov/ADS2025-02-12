package by.it.group410901.abakumov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<? super E>> implements Set<E> {
    private Object[] data; // Массив для хранения элементов множества (всегда отсортирован)
    private int size; // Количество элементов в множестве
    private static final int DEFAULT_CAPACITY = 10; // Начальный размер массива

    public MyTreeSet() {
        this.data = new Object[DEFAULT_CAPACITY]; // создаем массив на 10 элементов
        this.size = 0; // изначально множество пустое
    }

    // Метод сравнивает два элемента (используется в сортировке и поиске)
    private int cmp(E a, E b) {
        return a.compareTo(b);
    }

    // Бинарный поиск элемента в массиве (работает быстро, т.к. массив отсортирован)
    private int binarySearch(E key) {
        int lo = 0; // нижняя граница поиска
        int hi = size - 1; // верхняя граница поиска

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2; // середина диапазона
            E midVal = (E) data[mid]; // элемент из середины
            int c = key.compareTo(midVal); // сравнение искомого элемента и среднего

            if (c > 0) {
                lo = mid + 1; // элемент больше — ищем справа
            } else if (c < 0) {
                hi = mid - 1; // элемент меньше — ищем слева
            } else {
                return mid; // нашли элемент — возвращаем индекс
            }
        }

        // если элемент не найден, возвращаем отрицательное значение
        // (-точка_вставки - 1)
        return -lo - 1;
    }

    // Проверка и увеличение размера массива при нехватке места
    private void ensureCapacity(int minCapacity) {
        if (data.length >= minCapacity) return; // если места хватает — выходим

        int newCap = data.length * 2; // удваиваем размер
        if (newCap < minCapacity) newCap = minCapacity; // если всё равно мало — берём нужное

        Object[] nd = new Object[newCap]; // создаем новый массив большего размера
        for (int i = 0; i < size; i++) nd[i] = data[i]; // копируем старые элементы
        data = nd; // заменяем старый массив новым
    }

    @Override
    public String toString() {
        // Преобразуем множество в строку вида [a, b, c]
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", "); // добавляем запятую между элементами
            sb.append(data[i] == null ? "null" : data[i].toString()); // добавляем элемент
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size; // возвращаем количество элементов
    }

    @Override
    public void clear() {
        // очищаем все элементы массива
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0; // сбрасываем размер в 0
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // множество пустое, если size == 0
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // null-значения не поддерживаются
        try {
            E key = (E) o;
            return binarySearch(key) >= 0; // проверяем, есть ли элемент
        } catch (ClassCastException ex) {
            return false; // если тип не совпадает — возвращаем false
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException("MyTreeSet does not allow null elements");

        int idx = binarySearch(e); // ищем элемент
        if (idx >= 0) return false; // если уже есть — не добавляем

        int ins = -idx - 1; // вычисляем позицию для вставки
        ensureCapacity(size + 1); // проверяем, хватает ли места

        // сдвигаем элементы вправо, чтобы освободить место для нового
        for (int i = size; i > ins; i--) data[i] = data[i - 1];

        data[ins] = e; // вставляем элемент в нужное место
        size++; // увеличиваем количество элементов
        return true; // успешно добавили
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false; // null не поддерживается
        try {
            E key = (E) o;
            int idx = binarySearch(key); // ищем элемент
            if (idx < 0) return false; // если не найден — нечего удалять

            // сдвигаем элементы влево, затирая удаляемый элемент
            for (int i = idx; i < size - 1; i++) data[i] = data[i + 1];

            data[size - 1] = null; // последний элемент теперь пуст
            size--; // уменьшаем размер
            return true; // элемент удалён
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        // проверяем, содержатся ли все элементы из другой коллекции
        for (Object o : c) {
            if (!contains(o)) return false; // если хоть один отсутствует — false
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        // добавляем все элементы по очереди
        for (E e : c) {
            if (e == null) throw new NullPointerException("null element in collection");
            if (add(e)) changed = true; // если добавили хоть один — множество изменилось
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        // удаляем все элементы, которые есть в коллекции c
        for (Object o : c) {
            while (remove(o)) changed = true; // пока элемент есть — удаляем
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int write = 0; // позиция для записи "нужных" элементов

        // оставляем только те, что есть в другой коллекции
        for (int read = 0; read < size; read++) {
            Object elem = data[read];
            if (c.contains(elem)) {
                data[write++] = elem; // сохраняем элемент
            } else {
                changed = true; // если удалили — отмечаем изменение
            }
        }

        // очищаем хвост массива после переноса
        for (int i = write; i < size; i++) data[i] = null;
        size = write; // обновляем размер
        return changed;
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
}
