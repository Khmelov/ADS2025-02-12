package by.it.group410902.habrukovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> //упорядоченная хеш-таблица
{

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент
    private int size;
    private final float loadFactor;
    private static class Node<E>
    {
        final E element;
        final int hash;
        Node<E> next; // для коллизий в хеш-таблице
        Node<E> before, after; // для поддержания порядка добавления

        Node(E element, int hash, Node<E> next)
        {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы
    public MyLinkedHashSet()
    {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor)
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
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    @Override
    public boolean add(E element)
    {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем существование элемента
        Node<E> current = table[index];
        while (current != null)
        {
            if (current.hash == hash && (element == current.element || (element != null && element.equals(current.element))))
            {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<>(element, hash, table[index]);

        // Добавляем в хеш-таблицу
        table[index] = newNode;

        // Добавляем в конец списка порядка
        linkNodeLast(newNode);

        size++;

        // Проверяем необходимость рехеширования
        if (size > table.length * loadFactor)
        {
            resize();
        }
        return true;
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

                // Удаляем из хеш-таблицы
                if (prev == null)
                {
                    table[index] = current.next;
                } else
                {
                    prev.next = current.next;
                }

                // Удаляем из списка порядка
                unlinkNode(current);

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear()
    {
        if (size != 0)
        {
            for (int i = 0; i < table.length; i++)
            {
                table[i] = null;
            }
            head = tail = null;
            size = 0;
        }
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

        Node<E> current = head;
        boolean firstElement = true;
        while (current != null)
        {
            if (!firstElement)
            {
                sb.append(", ");
            }
            sb.append(current.element);
            firstElement = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Вспомогательные методы для поддержания порядка
    private void linkNodeLast(Node<E> node)
    {
        if (tail == null)
        {
            //если список пуст
            head = tail = node;
        } else
        {
            //добавляем в конец
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node)
    {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null)
        {
            head = after; //удаляем первый элемент
        } else
        {
            before.after = after;
        }

        if (after == null)
        {
            tail = before; //удаляем последний элемент
        } else
        {
            after.before = before;
        }

        node.before = node.after = null; //очищаем ссылки удаленных
    }

    // Исправленный retainAll
    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;

        // Собираем элементы для удаления
        Object[] toRemove = new Object[size];
        int count = 0;

        Node<E> current = head;
        while (current != null)
        {
            boolean found = false;
            for (Object obj : c)
            {
                if (obj == current.element || (obj != null && obj.equals(current.element)))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                toRemove[count++] = current.element;
            }
            current = current.after;
        }

        // Удаляем элементы
        for (int i = 0; i < count; i++)
        {
            if (remove(toRemove[i]))
            {
                modified = true;
            }
        }

        return modified;
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
    public boolean contains(Object element)
    {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null)
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
    public boolean containsAll(Collection<?> c)
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
            if (add(element))
            {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean modified = false;
        for (Object element : c)
        {
            if (remove(element))
            {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    private int hash(Object element)
    {
        if (element == null) return 0;
        int h = element.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length)
    {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize()
    {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        for (int i = 0; i < table.length; i++)
        {
            Node<E> current = table[i];
            while (current != null)
            {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
    }
}
