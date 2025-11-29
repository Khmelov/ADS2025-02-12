package by.it.group410902.yarmashuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Entry<E> {
        E data;
        Entry<E> hashTableNext;
        Entry<E> before;
        Entry<E> after;

        Entry(E data) {
            this.data = data;
            this.hashTableNext = null;
            this.before = null;
            this.after = null;
        }
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry<E>[] table;
    private int size;
    private int capacity;
    private float loadFactor;

    private Entry<E> head; // Голова списка, сохраняющего порядок добавления (самый первый добавленный элемент)
    private Entry<E> tail; // Хвост списка, сохраняющего порядок добавления (самый последний добавленный элемент)

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Entry<E>[]) new Entry[capacity];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.head = null;
        this.tail = null;
    }
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        this.capacity = initialCapacity;
        this.table = (Entry<E>[]) new Entry[capacity];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.head = null;
        this.tail = null;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive and not NaN.");
        }
        this.capacity = initialCapacity;
        this.table = (Entry<E>[]) new Entry[capacity];
        this.size = 0;
        this.loadFactor = loadFactor;
        this.head = null;
        this.tail = null;
    }
    private int getBucketIndex(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        return (hash & 0x7fffffff) % capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Entry<E> current = head;
        boolean firstElement = true;

        while (current != null) {
            if (!firstElement) {
                sb.append(", ");
            }
            sb.append(current.data);
            firstElement = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    public int size() {
        return size;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        this.table = (Entry<E>[]) new Entry[capacity];
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int bucketIndex = getBucketIndex(o);
        Entry<E> current = table[bucketIndex];
        while (current != null) {
            if (o == null) {
                if (current.data == null) {
                    return true;
                }
            } else {
                if (o.equals(current.data)) {
                    return true;
                }
            }
            current = current.hashTableNext;
        }
        return false;
    }

    @Override
    public boolean add(E e) {

        int bucketIndex = getBucketIndex(e);
        Entry<E> currentInBucket = table[bucketIndex];
        while (currentInBucket != null) {
            if (e == null) {
                if (currentInBucket.data == null) {
                    return false;
                }
            } else {
                if (e.equals(currentInBucket.data)) {
                    return false;
                }
            }
            currentInBucket = currentInBucket.hashTableNext;
        }


        if (size >= capacity * loadFactor) {
            resize();
            bucketIndex = getBucketIndex(e);
        }

        Entry<E> newEntry = new Entry<>(e);

        newEntry.hashTableNext = table[bucketIndex];
        table[bucketIndex] = newEntry;


        if (head == null) {
            head = newEntry;
            tail = newEntry;
        } else {
            newEntry.before = tail;
            tail.after = newEntry;
            tail = newEntry;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int bucketIndex = getBucketIndex(o);
        Entry<E> currentInBucket = table[bucketIndex];
        Entry<E> previousInBucket = null;


        while (currentInBucket != null) {
            boolean found = false;
            if (o == null) {
                if (currentInBucket.data == null) {
                    found = true;
                }
            } else {
                if (o.equals(currentInBucket.data)) {
                    found = true;
                }
            }

            if (found) {

                if (previousInBucket == null) {  // Удаляем головной Entry бакета
                    table[bucketIndex] = currentInBucket.hashTableNext;
                } else {
                    previousInBucket.hashTableNext = currentInBucket.hashTableNext; // Удаляем Entry из середины/конца
                }

                //  Удаление из списка порядка добавления
                if (currentInBucket.before == null) {
                    head = currentInBucket.after;
                } else {
                    currentInBucket.before.after = currentInBucket.after;
                }

                if (currentInBucket.after == null) {  // Если удаляемый Entry является хвостом списка порядка
                    tail = currentInBucket.before;
                } else {
                    currentInBucket.after.before = currentInBucket.before;
                }


                currentInBucket.before = null;
                currentInBucket.after = null;
                currentInBucket.hashTableNext = null;

                size--;
                return true;
            }

            previousInBucket = currentInBucket;
            currentInBucket = currentInBucket.hashTableNext;
        }
        return false;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null.");
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null.");
        }
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null.");
        }
        boolean changed = false;

        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null.");
        }
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (!c.contains(element)) { // Если текущий элемент нашего набора не содержится в коллекции 'c'
                it.remove(); // Удаляем его с помощью метода remove() итератора
                modified = true;
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2; // Удваиваем емкость
        Entry<E>[] newTable = (Entry<E>[]) new Entry[capacity]; // Создаем новый, больший массив бакетов

        // Перехешируем все элементы из старой таблицы в новую.

        Entry<E> currentInOrder = head;
        while (currentInOrder != null) {
            int newBucketIndex = getBucketIndex(currentInOrder.data);

            currentInOrder.hashTableNext = newTable[newBucketIndex];
            newTable[newBucketIndex] = currentInOrder;

            currentInOrder = currentInOrder.after;
        }
        this.table = newTable;
    }
    @Override
    public Iterator<E> iterator() {
        return new MyLinkedHashSetIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    private class MyLinkedHashSetIterator implements Iterator<E> {
        private Entry<E> current = head; // Текущий Entry, который будет возвращен следующим вызовом next()
        private Entry<E> lastReturned = null; // Последний Entry, который был возвращен next() (для remove())

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = current; // Сохраняем ссылку на текущий Entry перед тем, как перейти к следующему
            current = current.after; // Переходим к следующему Entry в порядке добавления
            return lastReturned.data; // Возвращаем данные из сохраненного Entry
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                // Если next() еще не был вызван, или remove() уже был вызван после последнего next()
                throw new IllegalStateException("next() has not yet been called, or remove() has already been called after the last call to next().");
            }
            // Вызываем внешний метод remove() для элемента, чтобы обеспечить согласованность
            // с внутренними структурами MyLinkedHashSet (хеш-таблица + двусвязный список).
            // Важно: поскольку MyLinkedHashSet.this.remove(Object) работает по значению,
            // и `current` итератора уже был продвинут на следующий элемент после `lastReturned`,
            // то удаление `lastReturned.data` не нарушает итерацию.
            MyLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null; // Сбрасываем lastReturned, чтобы предотвратить двойное удаление
        }
    }


}

