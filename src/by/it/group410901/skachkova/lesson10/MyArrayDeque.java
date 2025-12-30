package by.it.group410901.skachkova.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E>
{
    private E[] elements;
    private int size;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() //конструктор
    {
        elements = (E[]) new Object[16];
        size = 0;
        head = 0;
        tail = 0;
    }

    @Override
    public String toString()
    {
        if (size == 0)
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++)
        {
            int index = (head + i) % elements.length; //потому что кольцо
            sb.append(elements[index]);
            if (i < size - 1)
            {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean add(E element)
    {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) //добавление в начало
    {
        if (element == null)
        {
            throw new NullPointerException();
        }

        if (size == elements.length)
        {
            resize(); //увеличение размера
        }

        head = (head - 1 + elements.length) % elements.length; //сдвиг head влево на 1
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }

        if (size == elements.length)
        {
            resize();
        }

        elements[tail] = element;
        tail = (tail + 1) % elements.length; //сдвиг конца вправо на 1
        size++;
    }

    @Override
    public E element()
    {
        return getFirst();
    }

    @Override
    public E getFirst()
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast()
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }
        return elements[(tail - 1 + elements.length) % elements.length];//элемент перед tail
    }

    @Override
    public E poll()
    {
        return pollFirst();
    }

    @Override
    public E pollFirst()
    {
        if (size == 0)
        {
            return null;
        }

        E element = elements[head];//запоминается 1й элемент
        elements[head] = null;//удаляется
        head = (head + 1) % elements.length;//head сдвигается вправо на 1
        size--;
        return element;//значение удалённого
    }

    @Override
    public E pollLast()
    {
        if (size == 0)
        {
            return null;
        }

        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    @SuppressWarnings("unchecked")
    private void resize()
    {
        int newCapacity = elements.length * 2; //создаётся массив в 2 раза больше
        E[] newElements = (E[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) //элементы копируются в новый массив
        {
            int index = (head + i) % elements.length;
            newElements[i] = elements[index];
        }

        elements = newElements;//замена старого массива новым
        head = 0;
        tail = size;
    }
    // Неиспользуемые методы интерфейса Deque
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { if (size == 0) throw new NoSuchElementException(); return pollFirst(); }
    @Override public E removeLast() { if (size == 0) throw new NoSuchElementException(); return pollLast(); }
    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size == 0 ? null : getLast(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}