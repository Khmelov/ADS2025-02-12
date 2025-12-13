package by.it.group410901.skachkova.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap; //массив для хранения кучи
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue()
    {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator)
    {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear()
    {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e)
    {
        return offer(e);
    }

    @Override
    public boolean offer(E e) //добавление элемента
    {
        if (e == null)
        {
            throw new NullPointerException();
        }

        if (size == heap.length)
        {
            resize();
        }

        heap[size] = e;
        siftUp(size); //просеивание вверх
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove()
    {
        if (size == 0)
        {
            throw new java.util.NoSuchElementException();
        }
        return poll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll()
    {
        if (size == 0)
        {
            return null;
        }

        E result = (E) heap[0]; //минимальный элемент (корень)
        size--;
        heap[0] = heap[size]; //последний элемент ставится в корень
        heap[size] = null; //последняя позиция очищается
        if (size > 0)
        {
            siftDown(0); //просеивание нового корня вниз
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E element()
    {
        if (size == 0)
        {
            throw new java.util.NoSuchElementException();
        }
        return (E) heap[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() //выдача минимального элеента
    {
        return size == 0 ? null : (E) heap[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) //проверка наличия
    {
        for (int i = 0; i < size; i++)//линейный поиск
        {
            if (equals(o, heap[i]))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) //все элементы из коллекции
    {
        for (Object element : c)
        {
            if (!contains(element))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        boolean modified = false;
        for (E element : c)
        {
            if (offer(element))
            {
                modified = true;//добавл. по одному
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) //удаление вчсех указанных элементов
    {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) //копирование не удаляемых
        {
            if (!c.contains(heap[i]))
            {
                temp[newSize++] = heap[i];
            }
            else
            {
                modified = true;
            }
        }

        if (modified)
        {
            heap = temp;
            size = newSize;
            for (int i = (size - 2) / 2; i >= 0; i--) //перестраивание кучи
            {
                siftDown(i);
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) //оставить только указанные элементы
    {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) //копирование только оставляемых
        {
            if (c.contains(heap[i]))
            {
                temp[newSize++] = heap[i];
            }
            else
            {
                modified = true;
            }
        }

        if (modified) //перестраивание кучи
        {
            heap = temp;
            size = newSize;
            for (int i = (size - 2) / 2; i >= 0; i--)
            {
                siftDown(i);
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    //восстан. сво-ва кучи при удалении элемента снизу
    private void siftUp(int index) //просеивание вверх
    {
        E element = (E) heap[index]; //элемент для просеивания
        while (index > 0)
        {
            int parent = (index - 1) >>> 1; //нахождение родителя
            E parentElement = (E) heap[parent];
            if (compare(element, parentElement) >= 0) // Если элемент >= родителя, то останавливаемся
            {
                break;
            }
            heap[index] = parentElement; //обмен местами с родителем
            index = parent;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    //восстан. сво-ва кучи при удалении корня
    private void siftDown(int index)
    {
        E element = (E) heap[index];
        int half = size >>> 1;
        while (index < half)
        {
            int child = (index << 1) + 1;//левый ребенок
            E childElement = (E) heap[child];
            int right = child + 1;//правый ребенок
            if (right < size && compare(childElement, (E) heap[right]) > 0) //выбор меньшего ребенка
            {
                child = right;
                childElement = (E) heap[child];
            }
            if (compare(element, childElement) <= 0) // Если элемент <= меньшего ребенка, то останавливаемся
            {
                break;
            }
            heap[index] = childElement; //обмен местами с меньшим ребенком
            index = child;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b)
    {
        if (comparator != null)
        {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b); //естественныц порядок
    }

    private boolean equals(Object a, Object b)
    {
        return (a == b) || (a != null && a.equals(b));
    }

    private void resize()
    {
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    @Override
    public String toString()
    {
        if (isEmpty())
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++)
        {
            if (i > 0)
            {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator()
    {
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

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}