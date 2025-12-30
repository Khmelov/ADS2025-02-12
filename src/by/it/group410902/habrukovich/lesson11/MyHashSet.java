package by.it.group410902.habrukovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> //хеш-таблица
{
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    // Узел односвязного списка для разрешения коллизий
    private static class Node<E>
    {
        final E element;
        final int hash;
        Node<E> next;

        Node(E element, int hash, Node<E> next)
        {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }
    // Конструктор по умолчанию
    public MyHashSet()
    {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // Конструктор с указанием начальной емкости
    public MyHashSet(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    //Основной конструктор
    public MyHashSet(int initialCapacity, float loadFactor)
    {
        if (initialCapacity <= 0)
        {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
        {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity]; // инициализация таблицы
        this.size = 0;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size==0;
    }

    @Override
    public void clear()
    {
        if(size!=0)
        {
            for(int i=0; i<table.length; i++)
            {
                table[i]=null;
            }
            size = 0;
        }
    }

    @Override
    public boolean add(E element)
    {
        int hash = hash(element); //вычисляем хеш
        int index = indexFor(hash, table.length); //находим индекс в таблице

        Node<E> current = table[index];

        while(current!=null)// Проверяем, существует ли элемент уже
        {
            if (current.hash == hash && (element == current.element || (element != null && element.equals(current.element))))
            {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        table[index] = new Node<>(element, hash, table[index]);// Добавляем новый элемент в начало списка
        size++;

        if (size > table.length * loadFactor) {
            resize();
        }
        return true;
    }
    @Override
    public boolean contains(Object element)
    {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while(current!=null)
        {
            if (current.hash == hash && (element == current.element || (element != null && element.equals(current.element))))
            {
                return true;
            }
            current = current.next;
        }
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

    @Override
    public boolean remove(Object element)
    {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null)
        {
            if (current.hash == hash && (element == current.element || (element != null && element.equals(current.element))))
            {
                if (prev == null)
                {
                    // Удаляем первый элемент в списке
                    table[index] = current.next;
                } else
                {
                    // Удаляем элемент из середины или конца списка
                    prev.next = current.next;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
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
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }


    @Override
    public String toString()
    {
        if (isEmpty())
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean firstElement = true;
        for (int i = 0; i < table.length; i++)
        {
            Node<E> current = table[i];
            while (current != null)
            {
                if (!firstElement)
                {
                    sb.append(", ");
                }
                sb.append(current.element);
                firstElement = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private int hash(Object element)
    {
        if (element == null)
        {
            return 0;
        }
        int h = element.hashCode();
        return h ^ (h >>> 16);
    }

    //Вычисляет индекс в массиве на основе хеш-кода
    private int indexFor(int hash, int length)
    {
        return hash & (length - 1);
    }


    // Увеличивает размер таблицы и перераспределяет элементы
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        // Перераспределяем все элементы
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);

                // Вставляем в начало списка новой таблицы
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
    }

}
