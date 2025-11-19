package by.it.group451002.jasko.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

// Реализация множества с сохранением порядка добавления элементов
public class MyLinkedHashSet<E> implements Collection<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private final Node<E>[] buckets; // Хеш-таблица
    private LinkedNode<E> head; // Начало двусвязного списка
    private LinkedNode<E> tail; // Конец двусвязного списка
    private int size; // Количество элементов

    // Узел для хеш-таблицы, ссылается на узел двусвязного списка
    private static class Node<E> {
        LinkedNode<E> linkedNode; // Ссылка на двусвязный узел
        Node<E> next; // Следующий узел в списке коллизий

        Node(LinkedNode<E> linkedNode, Node<E> next) {
            this.linkedNode = linkedNode;
            this.next = next;
        }
    }

    // Узел двусвязного списка, хранит значение и связи
    private static class LinkedNode<E> {
        E value; // Значение узла
        LinkedNode<E> prev; // Предыдущий узел
        LinkedNode<E> next; // Следующий узел

        LinkedNode(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY]; // Инициализация массива
        size = 0;
    }

    // Вычисляет индекс в массиве по хеш-коду
    private int getIndex(Object o) {
        if (o == null) return 0; // Для null — индекс 0
        return Math.abs(o.hashCode()) % buckets.length;
    }

    @Override
    public int size() { return size; } // Возвращает текущий размер

    @Override
    public boolean isEmpty() { return size == 0; } // Проверяет, пусто ли множество

    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // null не поддерживается
        int index = getIndex(o); // Получаем индекс
        Node<E> current = buckets[index]; // Начинаем поиск в списке

        while (current != null) {
            if (current.linkedNode.value.equals(o)) return true; // Сравниваем значение
            current = current.next; // Переходим к следующему узлу
        }
        return false; // Элемент не найден
    }

    // Возвращает итератор, обходящий элементы в порядке добавления
    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator();
    }

    // Итератор с поддержкой remove()
    private class LinkedIterator implements Iterator<E> {
        LinkedNode<E> current = head; // Текущий узел
        LinkedNode<E> lastReturned = null; // Последний возвращённый узел

        @Override
        public boolean hasNext() { return current != null; } // Проверяем, есть ли следующий

        @Override
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException(); // Проверка на конец
            lastReturned = current; // Запоминаем узел
            E value = current.value; // Получаем значение
            current = current.next; // Переходим к следующему
            return value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException(); // Проверка на корректность вызова

            LinkedNode<E> nodeToRemove = lastReturned; // Узел для удаления
            lastReturned = null; // Сбрасываем

            // Удаляем из двусвязного списка
            if (nodeToRemove.prev != null) nodeToRemove.prev.next = nodeToRemove.next; // Обновляем связь
            else head = nodeToRemove.next; // Если удаляем первый — обновляем head
            if (nodeToRemove.next != null) nodeToRemove.next.prev = nodeToRemove.prev; // Обновляем связь
            else tail = nodeToRemove.prev; // Если удаляем последний — обновляем tail

            // Удаляем из хеш-таблицы
            int index = getIndex(nodeToRemove.value); // Получаем индекс
            Node<E> bucket = buckets[index]; // Начинаем поиск в списке
            Node<E> prevBucket = null;

            while (bucket != null) {
                if (bucket.linkedNode == nodeToRemove) { // Нашли нужный узел
                    if (prevBucket != null) prevBucket.next = bucket.next; // Удаляем из списка
                    else buckets[index] = bucket.next; // Удаляем первый
                    break;
                }
                prevBucket = bucket; // Переходим к следующему
                bucket = bucket.next;
            }

            size--; // Уменьшаем счётчик
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size]; // Создаём массив
        int i = 0;
        for (E e : this) arr[i++] = e; // Проходим по элементам
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) { // Если массив мал — создаём новый
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) a[i++] = (T) e; // Заполняем
        if (a.length > size) a[size] = null; // Если больше — ставим null
        return a;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new IllegalArgumentException("Null not allowed"); // null запрещён
        int index = getIndex(e); // Получаем индекс
        Node<E> current = buckets[index]; // Начинаем поиск

        while (current != null) {
            if (current.linkedNode.value.equals(e)) return false; // Если уже есть — не добавляем
            current = current.next;
        }

        LinkedNode<E> newNode = new LinkedNode<>(e); // Создаём новый узел

        if (tail == null) { // Если список пуст
            head = tail = newNode; // Новый — и первый, и последний
        } else {
            tail.next = newNode; // Добавляем в конец
            newNode.prev = tail;
            tail = newNode;
        }

        buckets[index] = new Node<>(newNode, buckets[index]); // Добавляем в хеш-таблицу
        size++; // Увеличиваем счётчик
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false; // null не поддерживается
        int index = getIndex(o); // Получаем индекс
        Node<E> current = buckets[index]; // Начинаем поиск
        Node<E> prevBucket = null; // Предыдущий узел в списке коллизий

        while (current != null) {
            if (current.linkedNode.value.equals(o)) { // Нашли элемент
                LinkedNode<E> nodeToRemove = current.linkedNode; // Узел двусвязного списка
                if (nodeToRemove.prev != null) nodeToRemove.prev.next = nodeToRemove.next; // Удаляем из списка
                else head = nodeToRemove.next; // Обновляем head
                if (nodeToRemove.next != null) nodeToRemove.next.prev = nodeToRemove.prev; // Удаляем из списка
                else tail = nodeToRemove.prev; // Обновляем tail
                if (prevBucket != null) prevBucket.next = current.next; // Удаляем из хеш-таблицы
                else buckets[index] = current.next; // Обновляем бакет
                size--; // Уменьшаем счётчик
                return true;
            }
            prevBucket = current; // Переходим к следующему
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) if (!contains(obj)) return false; // Если нет хотя бы одного — false
        return true; // Все есть
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг, был ли добавлен хотя бы один
        for (E e : c) if (add(e)) modified = true; // Добавляем
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один
        for (Object obj : c) if (remove(obj)) modified = true; // Удаляем
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один
        for (Iterator<E> it = iterator(); it.hasNext(); ) { // Проходим по итератору
            if (!c.contains(it.next())) { // Если нет в коллекции — удаляем
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        Arrays.fill(buckets, null); // Обнуляем бакеты
        head = null; // Обнуляем двусвязный список
        tail = null;
        size = 0; // Сбрасываем счётчик
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // Начинаем строку
        LinkedNode<E> current = head; // Начинаем с начала списка
        boolean first = true;

        while (current != null) { // Проходим по двусвязному списку
            if (!first) sb.append(", "); // Добавляем разделитель
            sb.append(current.value); // Добавляем значение
            first = false;
            current = current.next; // Переходим к следующему
        }

        sb.append("]"); // Закрываем строку
        return sb.toString();
    }

    // Неиспользуемые методы
    @Override
    public boolean removeIf(java.util.function.Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }
}