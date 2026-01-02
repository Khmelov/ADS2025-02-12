package by.it.group410901.skachkova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16; //начальный размер
    private static final float LOAD_FACTOR = 0.75f; //коэфициент загрузки

    private Node<E>[] table; //массив бакетов
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet()  //конструктор
    {
        table = new Node[DEFAULT_CAPACITY];// массив из 16 бакетов
        size = 0;
    }

    private static class Node<E>
    {
        E data;
        Node<E> next;

        Node(E data)
        {
            this.data = data;
        }
    }

    private int getIndex(Object key) //вычисление индекса бакета
    {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean equals(Object a, Object b) //безопасное сравнение
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
    public void clear()
    {
        table = new Node[DEFAULT_CAPACITY]; //создаёт новую пустую таблицу
        size = 0;
    }

    @Override
    public boolean contains(Object o) //проверка наличия
    {
        int index = getIndex(o); //находим бакет
        Node<E> current = table[index]; //берёс=м цепочку этого бакета

        while (current != null) //проходим по цепочке
        {
            if (equals(o, current.data))
            {
                return true; //элемент нашёлся
            }
            current = current.next;
        }
        return false; //нет
    }

    @Override
    public boolean add(E e) //добавление элемента
    {
        if (contains(e)) //если элемент есть - не добавляем
        {
            return false;
        }

        if (size + 1 > table.length * LOAD_FACTOR) //проверяем нужно ли расширять
        {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        if (table[index] == null) //добавл. в начало цепочки
        {
            table[index] = newNode; //если цепоска пустая
        }
        else
        {
            newNode.next = table[index];//новый узел указывает на старую голову
            table[index] = newNode; //новый узел новая голова
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) //удаление
    {
        int index = getIndex(o);
        Node<E> current = table[index]; //текущий узел
        Node<E> prev = null; //предыдущий узел

        while (current != null)
        {
            if (equals(o, current.data))
            {
                if (prev == null)
                {
                    table[index] = current.next;
                }
                else
                {
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

    @SuppressWarnings("unchecked")
    private void resize()
    {
        Node<E>[] oldTable = table;//сохранение старой таблицы
        table = new Node[oldTable.length * 2]; //создание новой таблицы в 2 раза больше
        size = 0;

        for (Node<E> node : oldTable)  //перехеширование всех элементов
        {
            while (node != null)
            {
                add(node.data); //добавление каждого элемента в новувю таблицу
                node = node.next;
            }
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
        boolean first = true;
        //обходим все бакеты и все цепочки
        for (Node<E> node : table)
        {
            while (node != null)
            {
                if (!first)
                {
                    sb.append(", ");
                }
                sb.append(node.data);
                first = false;
                node = node.next;
            }
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

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}