package by.it.group410902.derzhavskaya_ludmila.lesson11;
import java.util.Set;
// на основе массива с адресацией по хеш-коду
// и односвязным списком для элементов с коллизиями
public class MyHashSet<E> implements Set<E> {

    // Внутренний класс для узлов связного списка (для разрешения коллизий)
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    // Массив бакетов для хранения элементов
    private Node<E>[] buckets;

    // Начальный размер массива бакетов
    private static final int INITIAL_CAPACITY = 16;

    // Коэффициент загрузки для определения момента увеличения размера
    private static final double LOAD_FACTOR = 0.75;

    private int size = 0;

    // Конструктор по умолчанию
    public MyHashSet() {
        // Создаем массив бакетов начального размера
        buckets = new Node[INITIAL_CAPACITY];
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
        // Создаем новый пустой массив бакетов
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }


    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, не превышен ли коэффициент загрузки
        if (size >= buckets.length * LOAD_FACTOR) {
            Node<E>[] oldBuckets = buckets;

            // Создаем новый массив бакетов в два раза больше
            buckets = new Node[oldBuckets.length * 2];
            size =0;

            // Перемещаем все элементы из старого массива в новый
            for (Node<E> head : oldBuckets) {
                Node<E> current = head;
                while (current != null) {
                    add(current.data);
                    current = current.next;
                }
            }
        }

        int index = getHashIndex(element);

        // Проверяем, есть ли уже такой элемент
        Node<E> current = buckets[index];
        while (current != null) {
            if (current.data.equals(element)) {
                return false;
            }
            current = current.next;
        }

        // Создаем новый узел и добавляем его в начало списка
        Node<E> newNode = new Node<>(element);
        newNode.next = buckets[index];
        buckets[index] = newNode;

        size++;
        return true;
    }


    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = getHashIndex(element);

        // Ищем элемент в связном списке бакета
        Node<E> current = buckets[index];
        Node<E> previous = null;

        while (current != null) {
            if (current.data.equals(element)) {
                if (previous == null) {
                    // Удаляем голову списка
                    buckets[index] = current.next;
                } else {
                    // Удаляем элемент из середины или конца списка
                    previous.next = current.next;
                }

                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }


    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int index = getHashIndex(element);

        // Ищем элемент в связном списке бакета
        Node<E> current = buckets[index];
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }


    private int getHashIndex(Object element) {
        // Вычисляем хеш-код элемента
        int hashCode = element.hashCode();

        // Применяем модульную операцию для получения индекса в пределах массива
        return (hashCode & 0x7FFFFFFF) % buckets.length;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean firstElement = true;

        for (Node<E> head : buckets) {
            Node<E> current = head;
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");
                } else {
                    firstElement = false;
                }

                sb.append(current.data.toString());
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    //////    Остальные методы интерфейса Set - не реализованы

    @Override
    public java.util.Iterator<E> iterator() {
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
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}