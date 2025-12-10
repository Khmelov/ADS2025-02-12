package by.it.group410902.skobyalko.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
@SuppressWarnings("all")

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private static final int defaultSize = 10;
    private int currentSize; // текущий размер очереди (количество элементов)
    private E[] heap; // массив, используемый для хранения элементов кучи

    public MyPriorityQueue() { // инициализирует heap с начальным размером defaultSize и устанавливает currentSize в 0.
        heap = (E[]) new Comparable[defaultSize];
        currentSize = 0;
    }

    void swap(int x, int y) { // меняет местами элементы в массиве heap по индексам x и y
        E temp = heap[x];
        heap[x] = heap[y];
        heap[y] = temp;
    }

    // (просеивание вниз): используется после удаления корневого (например, минимального) элемента.
    // Оставшиеся элементы перемещаются вниз, чтобы на вершине снова оказался минимальный элемент.
    void siftDown(int i) {  // восстанавливает свойства кучи, начиная с узла i вниз по дереву
        int child1, child2, min;

        while (2 * i + 1 < currentSize) {
            child1 = 2 * i + 1;
            child2 = 2 * i + 2;
            min = child1;

            if ((child2 < currentSize) && (heap[child2].compareTo(heap[min])) < 0) {
                min = child2;
            }

            if (heap[i].compareTo(heap[min]) < 0) {
                break;
            }

            swap(i, min);
            i = min;
        }
    }

    //  (просеивание вверх): используется после добавления нового элемента.
    //  Новый элемент поднимается вверх, если его значение меньше значения родительского узла.
    void siftUp(int i) {  // восстанавливает свойства кучи, начиная с узла i вверх к корню
        while (heap[i].compareTo(heap[(i - 1) / 2]) < 0) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    // применяется после изменения кучи для приведения ее
    // в корректное состояние, начиная с узлов, близких к листьям.
    void heapify() {
        for (int i = (currentSize / 2); i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    //  возвращает строковое представление очереди, перечисляя элементы кучи в текущем порядке
    public String toString() {
        String tempStr = "[";

        for (int i = 0; i < currentSize; i++) {
            tempStr += heap[i];
            if (i < currentSize - 1) {
                tempStr += ", ";
            }
        }
        tempStr += "]";
        return tempStr.toString();
    }

    @Override
    // возвращает количество элементов в очереди (currentSize)
    public int size() {
        return currentSize;
    }

    @Override
    // очищает очередь, создавая новый пустой массив для heap и сбрасывая currentSize
    public void clear() {
        heap = (E[]) new Comparable[defaultSize];
        currentSize = 0;
    }

    @Override
    // добавляет элемент e в очередь. Если массив полон,
    // увеличивает его размер вдвое, вставляет элемент в конец кучи и вызывает siftUp
    public boolean add(E e) {
        if (currentSize == heap.length) {
            E[] newHeap = (E[]) new Comparable[currentSize * 2];

            System.arraycopy(heap, 0, newHeap, 0, currentSize);
            heap = newHeap;
        }

        heap[currentSize] = e;
        siftUp(currentSize);
        currentSize++;
        return true;
    }

    @Override
    // удаляет и возвращает минимальный элемент (корень кучи).
    // Меняет местами первый и последний элемент, уменьшает currentSize,
    // вызывает siftDown для восстановления кучи.
    public E remove() {
        if (currentSize == 0) {
            return null;
        }

        E temp = heap[0];
        currentSize--;
        heap[0] = heap[currentSize];
        siftDown(0);

        return temp;
    }

    @Override
    //проверяет, существует ли элемент o в куче
    public boolean contains(Object o) {
        for (int i = 0; i < currentSize; i++) {
            if (heap[i].equals(o)) {
                return true;
            }
        }
        return false;
    }


    @Override
    // эквивалентен add(e), добавляет элемент и возвращает true
    public boolean offer(E e) {
        return add(e);
    }


    @Override
    // эквивалентен remove(), удаляет и возвращает минимальный элемент
    public E poll() {
        return remove();
    }

    @Override
    // возвращают минимальный элемент (корень кучи) без удаления
    public E element() {
        if (currentSize == 0) {
            return null;
        }

        return heap[0];
    }

    @Override
    // возвращают минимальный элемент (корень кучи) без удаления
    public E peek() {
        return element();
    }

    @Override
    // проверяет, содержатся ли все элементы коллекции c в куче
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }

        return true;
    }

    @Override
    // добавляет все элементы из коллекции c в очередь
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = false;
        for (E item : c) {
            if (add(item)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    // удаляет из кучи все элементы, содержащиеся в c, возвращает true, если были удаленные элементы
    public boolean removeAll(Collection<?> c) {
        int cursor1 = 0, cursor2 = 0, counter = 0;

        for (cursor2 = 0; cursor2 < currentSize; cursor2++) {
            if (!c.contains(heap[cursor2])) {
                heap[cursor1++] = heap[cursor2];
            } else {
                counter++;
            }

        }

        if (counter != 0) {
            currentSize -= counter;
            heapify();
            return true;
        }

        return false;
    }

    @Override
    // оставляет в куче только элементы, присутствующие в коллекции c,
    // возвращает true, если произошли изменения
    public boolean retainAll(Collection<?> c) {
        int cursor1 = 0, cursor2 = 0, counter = 0;

        for (cursor2 = 0; cursor2 < currentSize; cursor2++) {
            if (c.contains(heap[cursor2])) {
                heap[cursor1++] = heap[cursor2];
            } else {
                counter++;
            }
        }

        if (counter != 0) {
            currentSize -= counter;
            heapify();
            return true;
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
