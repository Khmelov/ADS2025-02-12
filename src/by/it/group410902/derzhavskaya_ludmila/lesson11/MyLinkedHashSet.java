package by.it.group410902.derzhavskaya_ludmila.lesson11;
import java.util.Set;

// массива с адресацией по хеш-коду
// и односвязным списком для элементов с коллизиями с сохранением порядка добавления
public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;     // Ссылка на следующий узел в бакете
        Node<E> after;    // Ссылка на следующий узел в порядке добавления
        Node<E> before;   // Ссылка на предыдущий узел в порядке добавления

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets;

    private static final int INITIAL_CAPACITY = 16;

    // Коэффициент загрузки для определения момента увеличения размера
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<E> head;
    private Node<E> tail;

    // Конструктор по умолчанию
    public MyLinkedHashSet() {
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
        buckets = new Node[INITIAL_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, не превышен ли коэффициент загрузки, увеличиваем
        if (size >= buckets.length * LOAD_FACTOR) {
            Node<E>[] oldBuckets = buckets;

            // Создаем новый массив бакетов в два раза больше
            buckets = new Node[oldBuckets.length * 2];

            Node<E> current = head;
            head = null;
            tail = null;
            size = 0;

            // Перемещаем все элементы из старого массива в новый
            while (current != null) {
                Node<E> nextInOrder = current.after;

                // Сбрасываем ссылки  для повторного добавления
                current.next = null;
                current.before = null;
                current.after = null;

                add(current.data);

                current = nextInOrder;
            }
        }

        int index = getHashIndex(element);

        // Проверяем, есть ли уже такой элемент в бакете
        Node<E> current = buckets[index];
        while (current != null) {
            if (current.data.equals(element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);

        // Добавляем узел в бакет (в начало списка коллизий)
        newNode.next = buckets[index];
        buckets[index] = newNode;

        // Добавляем узел в конец двусвязного списка (для сохранения порядка)
        if (tail == null) {
            // Если список пуст, новый узел становится и головой и хвостом
            head = newNode;
            tail = newNode;
        } else {
            // Добавляем узел после текущего хвоста
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

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
                    // Удаляем голову списка коллизий
                    buckets[index] = current.next;
                } else {
                    // Удаляем элемент из середины или конца списка коллизий
                    previous.next = current.next;
                }

                // Удаляем из двусвязного списка порядка
                Node<E> before = current.before;
                Node<E> after = current.after;

                // Обновляем ссылки предыдущего узла
                if (before == null) {
                    head = after;
                } else {
                    before.after = after;
                }

                // Обновляем ссылки следующего узла
                if (after == null) {
                    tail = before;
                } else {
                    after.before = before;
                }

                // Обнуляем ссылки удаленного узла
                current.before = null;
                current.after = null;

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

    @Override
    public boolean containsAll(java.util.Collection<?> collection) {
        for (Object element : collection) {
            // Если элемент не содержится в множестве
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> collection) {
        boolean p = false;
        for (E element : collection) {
            if (add(element)) {
                p = true;
            }
        }

        return p;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> collection) {
        boolean p = false;

        for (Object element : collection) {
            if (remove(element)) {
                p = true;
            }
        }

        return p;
    }

    // Сохраняет только те элементы, которые содержатся в переданной коллекции
    @Override
    public boolean retainAll(java.util.Collection<?> collection) {
        boolean p = false;

        // Создаем временный список для элементов, которые нужно удалить
        java.util.ArrayList<E> toRemove = new java.util.ArrayList<>();

        // Проходим по всем элементам множества в порядке добавления
        Node<E> current = head;
        while (current != null) {
            if (!collection.contains(current.data)) {
                toRemove.add(current.data);
                p = true;
            }
            current = current.after;
        }

        // Удаляем все помеченные элементы
        for (E element : toRemove) {
            remove(element);
        }

        return p;
    }

    private int getHashIndex(Object element) {
        int hashCode = element.hashCode();

        // Применяем модульную операцию для получения индекса в пределах массива
        return (hashCode & 0x7FFFFFFF) % buckets.length;
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean firstElement = true;

        // Проходим по двусвязному списку в порядке добавления
        Node<E> current = head;
        while (current != null) {
            if (!firstElement) {
                sb.append(", ");
            } else {
                firstElement = false;
            }

            sb.append(current.data.toString());
            current = current.after;
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
}