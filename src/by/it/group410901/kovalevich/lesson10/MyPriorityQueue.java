package by.it.group410901.kovalevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] data;
    private int size;

    public MyPriorityQueue() {
        data = (E[]) new Comparable[8]; // стартовая ёмкость
        size = 0;
    }


    private void ensureCapacity() {
        if (size < data.length) return;
        E[] newData = (E[]) new Comparable[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void swap(int i, int j) {
        E tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    // поднимаем элемент вверх
    private void siftUp(int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            // мин-куча: родитель должен быть <= ребёнка
            if (data[idx].compareTo(data[parent]) < 0) {
                swap(idx, parent);
                idx = parent;
            } else {
                break;
            }
        }
    }

    // опускаем элемент вниз
    private void siftDown(int idx) {
        while (true) {
            int left = idx * 2 + 1;
            int right = idx * 2 + 2;
            int smallest = idx;

            if (left < size && data[left].compareTo(data[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && data[right].compareTo(data[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == idx) {
                break;
            }
            swap(idx, smallest);
            idx = smallest;
        }
    }

    private void heapify() {
        // начинаем с последнего внутреннего узла и опускаем вниз
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
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
        // подчистим ссылки, чтобы не держать объекты
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        // выводим текущее внутреннее состояние массива кучи
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append(']');
        return sb.toString();
    }


    // бросает исключение при неудаче
    @Override
    public boolean add(E element) {
        offer(element);
        return true;
    }

    // вставка с приоритетом
    @Override
    public boolean offer(E element) {
        ensureCapacity();
        data[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    //  посмотреть минимальный элемент (корень кучи), не удаляя
    @Override
    public E peek() {
        if (size == 0) return null;
        return data[0];
    }

    //  как peek(), но бросает исключение если пусто
    @Override
    public E element() {
        E res = peek();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    // взять и удалить минимальный элемент, вернуть его.
    // если пусто — вернуть null
    @Override
    public E poll() {
        if (size == 0) return null;
        E res = data[0];

        // переносим последний элемент в корень
        size--;
        data[0] = data[size];
        data[size] = null;

        // восстанавливаем
        if (size > 0) {
            siftDown(0);
        }

        return res;
    }

    // бросает исключение если пусто
    @Override
    public E remove() {
        E res = poll();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            E cur = data[i];
            if (cur == null && element == null) return true;
            if (cur != null && cur.equals(element)) return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            E cur = data[i];
            if (o == null ? cur == null : o.equals(cur)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            offer(el);
            changed = true;
        }
        return changed;
    }

    //  удалить 1 вхождение элемента
    // вернёт true если что-то было удалено
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            E cur = data[i];
            if (o == null ? cur == null : o.equals(cur)) {
                // заменить этот элемент последним
                size--;
                data[i] = data[size];
                data[size] = null;
                // восстановить кучу локально
                if (i < size) {
                    siftUp(i);
                    siftDown(i);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E cur = data[i];
            if (!c.contains(cur)) { // оставляем только элементы, которых НЕТ в c
                data[newSize++] = cur;
            }
        }

        boolean changed = (newSize != size);

        // зануляем хвост
        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }

        size = newSize;
        heapify();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E cur = data[i];
            if (c.contains(cur)) { // оставляем только те элементы, которые входят в c
                data[newSize++] = cur;
            }
        }

        boolean changed = (newSize != size);

        // зануляем хвост
        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }

        size = newSize;
        heapify();
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
