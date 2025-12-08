package by.it.group410902.derzhavskaya_e.lesson11;

// Реализация LinkedHashSet: хеш-таблица + порядок вставки (двусвязный список)
public class MyLinkedHashSet<E> implements java.util.Set<E> {

    // Узел для хранения элемента: и в цепочке, и в порядке вставки
    private static class Node<E> {
        E value;
        Node<E> next;     // следующий в цепочке (для коллизий)
        Node<E> prevOrder; // предыдущий по порядку добавления
        Node<E> nextOrder; // следующий по порядку добавления

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table; // массив цепочек для хеш-таблицы
    private Node<E> headOrder; // первый элемент по порядку добавления
    private Node<E> tailOrder; // последний элемент по порядку добавления
    private int size;          // количество элементов

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16]; // стандартная начальная емкость
        size = 0;
    }

    // Количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        headOrder = tailOrder = null;
        size = 0;
    }

    // Добавление элемента, если его ещё нет
    @Override
    public boolean add(E value) {
        int index = getIndex(value);
        Node<E> current = table[index];

        // проверка на дубликат
        while (current != null) {
            if (equalsValue(current.value, value))
                return false;
            current = current.next;
        }

        // создаём новый узел
        Node<E> newNode = new Node<>(value);
        newNode.next = table[index];
        table[index] = newNode;

        // вставка в список порядка
        if (tailOrder == null) {
            headOrder = tailOrder = newNode;
        } else {
            tailOrder.nextOrder = newNode;
            newNode.prevOrder = tailOrder;
            tailOrder = newNode;
        }

        size++;
        return true;
    }

    // Проверка, содержится ли элемент
    @Override
    public boolean contains(Object value) {
        int index = getIndex(value);
        Node<E> current = table[index];

        while (current != null) {
            if (equalsValue(current.value, value))
                return true;
            current = current.next;
        }
        return false;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object value) {
        int index = getIndex(value);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (equalsValue(current.value, value)) {
                // удалить из цепочки
                if (prev == null)
                    table[index] = current.next;
                else
                    prev.next = current.next;

                // удалить из порядка вставки
                if (current.prevOrder != null)
                    current.prevOrder.nextOrder = current.nextOrder;
                else
                    headOrder = current.nextOrder;

                if (current.nextOrder != null)
                    current.nextOrder.prevOrder = current.prevOrder;
                else
                    tailOrder = current.prevOrder;

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // Преобразование множества в строку (в порядке добавления)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = headOrder;
        boolean first = true;

        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.nextOrder;
        }
        return sb.append("]").toString();
    }

    // Получение индекса ячейки по хеш-коду
    private int getIndex(Object value) {
        if (value == null) return 0;
        return (value.hashCode() & 0x7fffffff) % table.length;
    }

    // Сравнение значений с учётом null
    private boolean equalsValue(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // методы для работы с коллекциями

    // Проверяет, содержатся ли ВСЕ элементы другой коллекции
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    // Добавляет все элементы из другой коллекции
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    // Удаляет все элементы, которые есть в другой коллекции
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (remove(o))
                modified = true;
        return modified;
    }

    // Оставляет только те элементы, которые есть в другой коллекции
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean modified = false;
        Node<E> current = headOrder;

        while (current != null) {
            Node<E> next = current.nextOrder; // заранее сохраняем
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override public java.util.Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
