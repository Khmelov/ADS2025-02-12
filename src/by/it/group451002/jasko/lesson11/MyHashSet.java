package by.it.group451002.jasko.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

// Реализация множества на основе хеш-таблицы через односвязный список
public class MyHashSet<E> implements Collection<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private final Node<E>[] buckets; // Массив бакетов (односвязные списки)
    private int size; // Количество элементов в множестве

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY]; // Инициализация массива
        size = 0;
    }

    // Внутренний класс для узла односвязного списка
    private static class Node<E> {
        E value; // Значение узла
        Node<E> next; // Следующий узел

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Вычисляет индекс в массиве по хеш-коду
    private int getIndex(Object o) {
        if (o == null) return 0; // Для null всегда индекс 0
        return Math.abs(o.hashCode()) % buckets.length;
    }

    @Override
    public int size() {
        return size; // Возвращает текущий размер множества
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверяет, пусто ли множество
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // null не поддерживается
        int index = getIndex(o); // Получаем индекс в массиве
        Node<E> current = buckets[index]; // Начинаем поиск в списке

        while (current != null) {
            if (current.value.equals(o)) return true; // Найден элемент
            current = current.next; // Переходим к следующему узлу
        }
        return false; // Элемент не найден
    }

    // Возвращает итератор, обходящий все элементы в произвольном порядке
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Node<E> current = null; // Текущий узел
            int bucketIndex = 0; // Индекс текущего бакета
            final Node<E> bucket = null; // Бакет, в котором ищем

            @Override
            public boolean hasNext() {
                if (current != null && current.next != null) return true; // Проверяем в текущем списке
                for (int i = bucketIndex; i < buckets.length; i++) {
                    if (buckets[i] != null) return true; // Ищем следующий непустой бакет
                }
                return false;
            }

            @Override
            public E next() {
                if (current == null || current.next == null) { // Если текущий список кончился
                    while (bucketIndex < buckets.length && buckets[bucketIndex] == null) {
                        bucketIndex++; // Ищем следующий непустой бакет
                    }
                    if (bucketIndex < buckets.length) {
                        current = buckets[bucketIndex++]; // Переходим к следующему списку
                    }
                } else {
                    current = current.next; // Переходим к следующему узлу в списке
                }
                assert current != null;
                return current.value; // Возвращаем значение
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size]; // Создаём массив нужного размера
        int i = 0;
        for (E e : this) arr[i++] = e; // Проходим по всем элементам
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) { // Если переданный массив мал
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size); // Создаём новый
        }
        int i = 0;
        for (E e : this) a[i++] = (T) e; // Заполняем массив
        if (a.length > size) a[size] = null; // Если массив больше, ставим null в конец
        return a;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new IllegalArgumentException("Null not allowed"); // null запрещён
        int index = getIndex(e); // Получаем индекс
        Node<E> current = buckets[index]; // Ищем в списке

        while (current != null) {
            if (current.value.equals(e)) return false; // Если элемент уже есть — не добавляем
            current = current.next;
        }

        buckets[index] = new Node<>(e, buckets[index]); // Добавляем в список
        size++; // Увеличиваем счётчик кол-ва элементов
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false; // null не поддерживается
        int index = getIndex(o); // Получаем индекс
        Node<E> current = buckets[index]; // Начинаем поиск
        Node<E> prev = null; // Предыдущий узел

        while (current != null) {
            if (current.value.equals(o)) { // Если нашли элемент
                if (prev == null) buckets[index] = current.next; // Удаляем первый узел
                else prev.next = current.next; // Удаляем из середины
                size--; // Уменьшаем счётчик
                return true;
            }
            prev = current; // Переходим к следующему
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) if (!contains(obj)) return false; // Если хотя бы одного нет — false
        return true; // Все элементы найдены
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг, был ли добавлен хотя бы один элемент
        for (E e : c) if (add(e)) modified = true; // Добавляем каждый элемент
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один элемент
        for (Object obj : c) if (remove(obj)) modified = true; // Удаляем каждый элемент
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один элемент
        for (Iterator<E> it = iterator(); it.hasNext(); ) { // Проходим по итератору
            if (!c.contains(it.next())) { // Если элемент не в коллекции — удаляем
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        Arrays.fill(buckets, null); // Обнуляем все бакеты
        size = 0; // Сбрасываем счётчик
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // Начинаем формировать строку
        boolean first = true;

        for (Node<E> bucket : buckets) { // Проходим по всем бакетам
            Node<E> current = bucket;
            while (current != null) { // Проходим по списку в бакете
                if (!first) sb.append(", "); // Добавляем разделитель
                sb.append(current.value); // Добавляем элемент
                first = false;
                current = current.next; // Переходим к следующему узлу
            }
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