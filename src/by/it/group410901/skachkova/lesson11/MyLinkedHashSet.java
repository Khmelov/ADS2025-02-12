package by.it.group410901.skachkova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> { //сохраняет порядок добавления элементов
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table; //массив бакетов
    private Node<E> head; //первый элемент в порядке добавления
    private Node<E> tail; //последний
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet()
    {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<E> {
        E data;
        Node<E> next; //следующий в цепочки
        Node<E> before; //предыдущий узел в двусвязном списке
        Node<E> after; //следующий узел в лвусвязном списке

        Node(E data)
        {
            this.data = data;
        }
    }

    private int getIndex(Object key)
    {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean equals(Object a, Object b)
    {
        return (a == b) || (a != null && a.equals(b));
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
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) //проверка наличия
    {
        int index = getIndex(o); //находим бакет в хеш-таблице
        Node<E> current = table[index];//берём цепочку с бакетом

        while (current != null)
        {
            if (equals(o, current.data))
            {
                return true; //нашлось
            }
            current = current.next;
        }
        return false;//нет
    }

    @Override
    public boolean add(E e)
    {
        if (contains(e))
        {
            return false; //без дубликатов
        }

        if (size + 1 > table.length * LOAD_FACTOR)
        {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        // добавление в хеш-теблицу (в начало)
        if (table[index] == null)
        {
            table[index] = newNode;
        }
        else
        {
            newNode.next = table[index];
            table[index] = newNode;
        }

        // добавление в двусвязный свисок(в конеч)
        if (tail == null) //если первый элемент
        {
            head = newNode;
            tail = newNode;
        }
        else
        {//в конец
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) //поиск в хеш-таблице
        {
            if (equals(o, current.data))
            {
                // удаление
                if (prev == null)
                {
                    table[index] = current.next;//если голова
                }
                else
                {
                    prev.next = current.next;//не голова
                }

                // удаление из двесвязного списка
                if (current.before != null)
                {
                    current.before.after = current.after; //обновляем предыдущий
                }
                else
                {
                    head = current.after;//удаляем глову
                }

                if (current.after != null)
                {
                    current.after.before = current.before;
                }
                else
                {
                    tail = current.before;
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
    public boolean containsAll(Collection<?> c) //содержится ли всё
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
    public boolean retainAll(Collection<?> c) //оставляет только указанное
    {
        boolean modified = false;
        Node<E> current = head;
        while (current != null)
        {
            if (!c.contains(current.data))
            {
                Node<E> next = current.after;
                remove(current.data);
                current = next;
                modified = true;
            }
            else
            {
                current = current.after;
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private void resize()
    {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        Node<E> current = head;
        head = null;
        tail = null;

        while (current != null) {

            E data = current.data;
            int index = getIndex(data);
            Node<E> newNode = new Node<>(data);

            //добавл. в новую хеш-таблицу
            if (table[index] == null) {
                table[index] = newNode;
            } else {
                newNode.next = table[index];
                table[index] = newNode;
            }

            //добавл. в список
            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.after = newNode;
                newNode.before = tail;
                tail = newNode;
            }

            size++;
            current = current.after;
        }
    }

    @Override
    public String toString()
    {
        if (isEmpty())
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null)
        {
            if (!first)
            {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Required by interface but not implemented for this test
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