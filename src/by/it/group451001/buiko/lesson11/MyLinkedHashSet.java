package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    // Начальная емкость хеш-таблицы - размер внутреннего массива при создании
    private static final int DEFAULT_CAPACITY = 16;
    // Коэффициент загрузки - определяет, когда нужно увеличивать таблицу
    // Когда заполнение достигает 75%, таблица увеличивается вдвое
    private static final float LOAD_FACTOR = 0.75f;

    /*LinkedHashSet — это гибридная структура данных, которая сочетает быстрый доступ хеш-таблицы
     с сохранением порядка добавления элементов через двусвязный список.
     */
    private static class LinkedNode<E> {
        E data;                    // Хранимый элемент
        LinkedNode<E> next;        // Следующий узел в цепочке коллизий (хеш-таблица)
        LinkedNode<E> after;       // Следующий узел в порядке добавления (связный список)
        LinkedNode<E> before;      // Предыдущий узел в порядке добавления (связный список)

        LinkedNode(E data) {
            this.data = data;
        }
    }

    /*реализовала множество на основе  структуры, где каждый узел содержит ссылки как для цепочки коллизий хеш-таблицы (next),
      так и для поддержания порядка добавления (before, after).
       */
    private LinkedNode<E>[] table;
    private LinkedNode<E> head; // первый добавленный элемент
    private LinkedNode<E> tail; // последний добавленный элемент
    private int size;

    /*При добавлении элементов метод add() помещает узел в соответствующую цепочку
     хеш-таблицы и одновременно в конец двусвязного списка, что гарантирует
     сохранение порядка итерации в порядке добавления.
     */
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (LinkedNode<E>[]) new LinkedNode[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    /*Для удаления элементов метод remove() корректно обновляет обе структуры — удаляет узел из цепочки коллизий
     и из двусвязного списка, поддерживая целостность обеих структур данных.
     */
    private int getIndex(Object key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return (hash & 0x7FFFFFFF) % table.length;
    }

    /*Все основные операции работают за амортизированное константное время O(1),
    а метод toString() выводит элементы в предсказуемом порядке их добавления, что обеспечивает удобство отладки и использования.
     */
    private boolean objectsEqual(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
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
    public boolean contains(Object o) {
        // Вычисляем индекс бакета для объекта
        int index = getIndex(o);
        LinkedNode<E> current = table[index];

        // Поиск в цепочке коллизий данного бакета
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                return true; // Элемент найден
            }
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean add(E e) {
        // Вычисляем индекс для нового элемента
        int index = getIndex(e);
        LinkedNode<E> current = table[index];

        // Проверка на существование элемента (дубликаты не допускаются)
        while (current != null) {
            if (objectsEqual(e, current.data)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Создание нового узла
        LinkedNode<E> newNode = new LinkedNode<>(e);

        // Добавление в цепочку коллизий хеш-таблицы
        // Вставляем в начало цепочки для эффективности
        newNode.next = table[index];
        table[index] = newNode;

        // Добавление в конец двусвязного списка (сохранение порядка добавления)
        if (tail == null) {
            // Первый элемент в множестве
            head = newNode;
            tail = newNode;
        } else {
            // Добавляем после текущего хвоста
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode; // Обновляем хвост
        }

        size++;

        // Проверка необходимости рехеширования
        // Если коэффициент загрузки превышен, увеличиваем таблицу
        if ((float) size / table.length > LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        // Вычисляем индекс бакета для удаляемого объекта
        int index = getIndex(o);
        LinkedNode<E> current = table[index];
        LinkedNode<E> prev = null;

        // Поиск элемента в цепочке коллизий
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                // Удаление из цепочки коллизий хеш-таблицы
                if (prev == null) {
                    // Удаляем первый элемент цепочки
                    table[index] = current.next;
                } else {
                    // Удаляем из середины или конца цепочки
                    prev.next = current.next;
                }

                // Удаление из двусвязного списка порядка
                if (current.before != null) {
                    // Если есть предыдущий узел, обновляем его ссылку
                    current.before.after = current.after;
                } else {
                    // Если это голова списка, обновляем голову
                    head = current.after;
                }

                if (current.after != null) {
                    // Если есть следующий узел, обновляем его ссылку
                    current.after.before = current.before;
                } else {
                    // Если это хвост списка, обновляем хвост
                    tail = current.before;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public void clear() {
        // Очистка всех бакетов хеш-таблицы
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Сброс указателей списка порядка
        head = null;
        tail = null;
        size = 0;
    }

    // Рехеширование таблицы - увеличение размера при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        // Сохраняем ссылку на старую таблицу
        LinkedNode<E>[] oldTable = table;
        // Увеличиваем емкость в 2 раза
        int newCapacity = oldTable.length * 2;
        table = (LinkedNode<E>[]) new LinkedNode[newCapacity];

        // Перехеширование всех элементов
        // Порядок в двусвязном списке сохраняется, меняются только индексы в таблице
        for (LinkedNode<E> node : oldTable) {
            LinkedNode<E> current = node;
            while (current != null) {
                // Сохраняем ссылку на следующий узел в цепочке
                LinkedNode<E> next = current.next;
                // Вычисляем новый индекс для текущего элемента
                int newIndex = getIndex(current.data);

                // Вставка в новую таблицу (в начало цепочки)
                current.next = table[newIndex];
                table[newIndex] = current;

                current = next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        LinkedNode<E> current = head;
        boolean first = true;

        // Обход элементов в порядке их добавления
        // Используем двусвязный список для сохранения порядка
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // Реализация дополнительных методов для уровня B

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции содержатся в множестве
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в коллекции
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Сохраняем только элементы, содержащиеся в коллекции
        boolean modified = false;
        LinkedNode<E> current = head;

        // Проход по всем элементам в порядке добавления
        // Используем двусвязный список для корректного обхода при удалении
        while (current != null) {
            LinkedNode<E> next = current.after; // Сохраняем ссылку до удаления
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // Оставшиеся методы интерфейса Set (заглушки)

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }
}
/*
* Ключевые особенности LinkedHashSet:
1. Гибридная структура данных
Хеш-таблица - для быстрого доступа O(1) к элементам

Двусвязный список - для сохранения порядка добавления элементов

2. Двойные связи узлов
Каждый узел имеет:

next - для цепочки коллизий в хеш-таблице

before/after - для поддержания порядка в двусвязном списке

3. Преимущества перед обычным HashSet
Предсказуемый порядок - элементы перебираются в порядке добавления

Эффективность - сохраняет O(1) производительность основных операций

Удобство отладки - toString() выводит элементы в понятном порядке

4. Сценарии использования
Когда важен порядок добавления элементов

Для LRU (Least Recently Used) кэшей

Когда нужен предсказуемый порядок итерации

5. Временная сложность
Добавление: O(1) в среднем случае

Удаление: O(1) в среднем случае

Поиск: O(1) в среднем случае

Обход в порядке добавления: O(n)
* */