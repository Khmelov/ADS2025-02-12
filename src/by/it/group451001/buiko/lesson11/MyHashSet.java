package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    // Начальная емкость хеш-таблицы - размер внутреннего массива при создании
    private static final int DEFAULT_CAPACITY = 16;
    // Коэффициент загрузки - определяет, когда нужно увеличивать таблицу
    // Когда заполнение достигает 75%, таблица увеличивается вдвое
    private static final float LOAD_FACTOR = 0.75f;

    // Внутренний класс для узла односвязного списка
    // Используется для разрешения коллизий методом цепочек
    private static class Node<E> {
        E data;        // Хранимый элемент
        Node<E> next;  // Ссылка на следующий узел в цепочке

        Node(E data) {
            this.data = data;
        }
    }

    // Массив бакетов (корзин) - основная структура хеш-таблицы
    // Каждый бакет содержит цепочку узлов для разрешения коллизий
    private Node<E>[] table;
    // Текущее количество элементов в множестве
    private int size;

    /*HashSet — это коллекция, которая хранит уникальные элементы,
    используя хеш-таблицу для быстрого доступа, где каждый элемент
    должен иметь корректно реализованные методы hashCode() и equals().
     */
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }
    /*реализовала множество на основе хеш-таблицы с методом цепочек,
      где коллизии разрешаются путем организации связных списков в каждом бакете,
      что обеспечивает эффективное хранение даже при совпадении хешей.
       */
    private int getIndex(Object key) {
        if (key == null) return 0; // null всегда попадает в бакет 0
        int hash = key.hashCode();
        return (hash & 0x7FFFFFFF) % table.length;
    }

    /* Для добавления элементов используется метод add(),
   который вычисляет индекс бакета через хеш-функцию,
   проверяет отсутствие дубликатов в цепочке и вставляет новый узел
    в начало списка за время O(1) в среднем случае.
    */
    @Override
    public int size() {
        return size;
    }

    /*При достижении коэффициента загрузки 0.75 автоматически выполняется рехеширование
    — увеличение таблицы вдвое с перераспределением всех элементов,
     что поддерживает эффективность операций при росте количества данных.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /*Все основные операции (добавление, удаление, поиск) работают за амортизированное константное время O(1),
    а метод toString() обеспечивает корректное строковое представление множества через обход всех бакетов и цепочек.
     */
    @Override
    public boolean contains(Object o) {
        // Вычисляем индекс бакета для данного объекта
        int index = getIndex(o);
        Node<E> current = table[index];

        // Поиск элемента в цепочке коллизий
        // Проходим по всем узлам в цепочке данного бакета
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
        Node<E> current = table[index];

        // Проверка на существование элемента
        // Проходим по цепочке, чтобы убедиться, что элемент еще не добавлен
        while (current != null) {
            if (objectsEqual(e, current.data)) {
                return false; // Элемент уже существует - дубликаты не допускаются
            }
            current = current.next;
        }

        // Добавление нового элемента в начало цепочки
        // Создаем новый узел и добавляем его в начало цепочки
        Node<E> newNode = new Node<>(e);
        newNode.next = table[index]; // Старый первый узел становится следующим
        table[index] = newNode;      // Новый узел становится первым
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
        Node<E> current = table[index];
        Node<E> prev = null;

        // Поиск и удаление элемента из цепочки
        // Проходим по цепочке, сохраняя ссылку на предыдущий узел
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                // Элемент найден - удаляем узел из цепочки
                if (prev == null) {
                    // Удаление первого элемента цепочки
                    table[index] = current.next;
                } else {
                    // Удаление из середины или конца цепочки
                    prev.next = current.next;
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
        // Очистка всех бакетов таблицы
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Вспомогательный метод для сравнения объектов с учетом null
    // Корректно обрабатывает сравнение null значений
    private boolean objectsEqual(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }

    // Метод для увеличения размера таблицы при переполнении
    // Увеличивает таблицу вдвое и перераспределяет все элементы
    @SuppressWarnings("unchecked")
    private void resize() {
        // Сохраняем ссылку на старую таблицу
        Node<E>[] oldTable = table;
        // Увеличиваем емкость в 2 раза
        int newCapacity = oldTable.length * 2;
        table = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        // Проходим по всем бакетам старой таблицы
        for (Node<E> node : oldTable) {
            Node<E> current = node;
            while (current != null) {
                // Сохраняем ссылку на следующий узел перед изменением
                Node<E> next = current.next;
                // Вычисляем новый индекс для текущего элемента
                int newIndex = getIndex(current.data);

                // Вставка в новую таблицу
                // Добавляем узел в начало цепочки нового бакета
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
        boolean firstElement = true;

        // Обход всех бакетов хеш-таблицы
        // Проходим по всем бакетам и всем цепочкам
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");
                }
                sb.append(current.data);
                firstElement = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Необязательные методы интерфейса Set (заглушки)
    // Эти методы не требуются для базовой реализации

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

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll not implemented");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll not implemented");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll not implemented");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not implemented");
    }
}

/*
* Принципы работы хеш-таблицы:
1. Структура данных
Массив бакетов - основная таблица фиксированного размера

Цепочки узлов - для разрешения коллизий (метод цепочек)

Коэффициент загрузки - определяет момент увеличения таблицы

2. Ключевые операции
Добавление - вычисляем хеш, находим бакет, проверяем дубликаты, добавляем в начало цепочки

Поиск - вычисляем хеш, ищем в цепочке соответствующего бакета

Удаление - находим элемент в цепочке и удаляем узел

3. Обработка коллизий
Когда разные ключи имеют одинаковый хеш (попадают в один бакет), они хранятся в виде односвязного списка.

4. Динамическое расширение
Когда количество элементов превышает емкость × коэффициент загрузки, таблица увеличивается вдвое и все элементы перераспределяются.

5. Временная сложность
В среднем случае: O(1) для добавления, поиска, удаления

В худшем случае: O(n) когда все элементы в одном бакете


* */