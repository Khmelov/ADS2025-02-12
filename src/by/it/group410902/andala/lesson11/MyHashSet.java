package by.it.group410902.andala.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    // Внутренний класс для узла связного списка
    private static class Node<E> {
        E value;        // Значение элемента
        Node<E> next;   // Ссылка на следующий узел

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;  // Массив бакетов (корзин)
    private int size;         // Количество элементов в множестве
    private static final int DEFAULT_CAPACITY = 16;  // Начальный размер таблицы

    // Конструктор
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];  // Создаем массив бакетов
        size = 0;  // Начальный размер - 0
    }

    // Метод для вычисления индекса бакета для объекта
    private int index(Object o) {
        // Если объект null - помещаем в бакет 0, иначе используем хэш-код
        return (o == null ? 0 : Math.abs(o.hashCode())) % table.length;
    }

    // Добавление элемента в множество
    @Override
    public boolean add(E e) {
        int i = index(e);  // Вычисляем индекс бакета
        Node<E> current = table[i];  // Получаем голову связного списка для этого бакета

        // Проверяем, нет ли уже такого элемента в множестве
        while (current != null) {
            if ((e == null && current.value == null) ||
                    (e != null && e.equals(current.value))) {
                return false;  // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало связного списка
        table[i] = new Node<>(e, table[i]);
        size++;  // Увеличиваем счетчик элементов
        return true;  // Элемент успешно добавлен
    }

    // Удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        int i = index(o);  // Вычисляем индекс бакета
        Node<E> current = table[i];  // Голова связного списка
        Node<E> prev = null;  // Предыдущий узел

        // Ищем элемент для удаления
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {
                // Нашли элемент для удаления
                if (prev == null) {
                    table[i] = current.next;  // Удаляем голову списка
                } else {
                    prev.next = current.next;  // Удаляем из середины/конца
                }
                size--;  // Уменьшаем счетчик
                return true;  // Успешно удалили
            }
            prev = current;
            current = current.next;
        }
        return false;  // Элемент не найден
    }

    // Проверка наличия элемента в множестве
    @Override
    public boolean contains(Object o) {
        int i = index(o);  // Вычисляем индекс бакета
        Node<E> current = table[i];  // Голова связного списка

        // Ищем элемент в связном списке
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {
                return true;  // Элемент найден
            }
            current = current.next;
        }
        return false;  // Элемент не найден
    }

    // Очистка множества
    @Override
    public void clear() {
        // Обнуляем все бакеты
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;  // Сбрасываем счетчик
    }

    // Получение количества элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Строковое представление множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        // Проходим по всем бакетам
        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            // Проходим по всем элементам в связном списке бакета
            while (current != null) {
                if (!first) sb.append(", ");
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Проверка наличия всех элементов из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;  // Если хоть одного элемента нет
        }
        return true;  // Все элементы есть
    }

    // Добавление всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;  // Если добавили хотя бы один элемент
        }
        return changed;
    }

    // Удаление всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            if (remove(e)) changed = true;  // Если удалили хотя бы один элемент
        }
        return changed;
    }

    // Оставить только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        // Проходим по всем бакетам
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            Node<E> prev = null;
            // Проходим по связному списку
            while (current != null) {
                if (!c.contains(current.value)) {
                    // Удаляем элемент, если его нет в коллекции c
                    if (prev == null) {
                        table[i] = current.next;  // Удаляем голову
                    } else {
                        prev.next = current.next;  // Удаляем из середины/конца
                    }
                    size--;
                    changed = true;
                } else {
                    prev = current;  // Переходим к следующему
                }
                current = current.next;
            }
        }
        return changed;
    }

    // Итератор для обхода множества
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucketIndex = 0;      // Текущий индекс бакета
            Node<E> current = null;   // Текущий узел

            // Метод для перехода к следующему непустому бакету
            private void advance() {
                while (bucketIndex < table.length && current == null) {
                    current = table[bucketIndex++];  // Берем голову следующего бакета
                }
            }

            // Проверка наличия следующего элемента
            @Override
            public boolean hasNext() {
                advance();  // Переходим к следующему непустому бакету
                return current != null;
            }

            // Получение следующего элемента
            @Override
            public E next() {
                advance();  // Переходим к следующему непустому бакету
                if (current == null) throw new NoSuchElementException();
                E value = current.value;  // Берем значение
                current = current.next;   // Переходим к следующему узлу
                return value;
            }
        };
    }

    // Не реализованные методы (выбрасывают исключение)
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}