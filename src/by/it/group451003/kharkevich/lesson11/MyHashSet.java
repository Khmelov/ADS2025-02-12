package by.it.group451003.kharkevich.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    // Внутренний класс Node для хранения элементов
    static class Node<E> {
        E data;                    // Данные элемента
        Node<E> previous;          // Ссылка на предыдущий элемент в двусвязном списке
        Node<E> next;              // Ссылка на следующий элемент в двусвязном списке
        Node<E> nextInSet;         // Ссылка на следующий элемент в цепочке коллизий

        public Node(E data) {
            this.data = data;
        }
    }

    // Вспомогательный класс для управления двусвязным списком
    static class List<E> {
        Node<E> head;  // Первый элемент списка
        Node<E> tail;  // Последний элемент списка

        // Добавление узла в конец списка
        void add(Node<E> node) {
            if (head == null) {
                head = node;        // Если список пуст, новый узел становится головой
            } else {
                tail.next = node;   // Текущий хвост ссылается на новый узел
                node.previous = tail; // Новый узел ссылается на старый хвост
            }

            tail = node;            // Новый узел становится новым хвостом
        }

        // Удаление узла из списка
        void remove(Node<E> node) {
            if (node.previous != null) {
                node.previous.next = node.next;  // Обход узла с предыдущего
            } else {
                head = head.next;                // Если узел - голова, смещаем голову
            }

            if (node.next != null) {
                node.next.previous = node.previous; // Обход узла со следующего
            } else {
                tail = tail.previous;            // Если узел - хвост, смещаем хвост
            }
        }

        // Очистка списка
        void clear() {
            head = null;
            tail = null;
        }
    }

    Node<E>[] elements;            // Массив бакетов (корзин) хэш-таблицы
    static final int INITIAL_SIZE = 16;  // Начальный размер хэш-таблицы
    int size;                      // Количество элементов в множестве
    List<E> list;                  // Двусвязный список для сохранения порядка добавления

    // Конструктор по умолчанию
    public MyHashSet() {
        this(INITIAL_SIZE);
    }

    // Конструктор с заданным размером
    @SuppressWarnings("unchecked")
    public MyHashSet(int size) {
        elements = new Node[size];  // Инициализация массива бакетов
        list = new List<>();        // Инициализация двусвязного списка
    }

    // Строковое представление множества
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head;
        if (current != null) {
            sb.append(current.data);  // Добавляем первый элемент
            current = current.next;
        }
        while (current != null) {
            sb.append(", ");
            sb.append(current.data);  // Добавляем остальные элементы через запятую
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;  // Возвращает количество элементов
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);  // Очищаем все бакеты
        size = 0;                     // Сбрасываем счетчик
        list.clear();                 // Очищаем двусвязный список
    }

    @Override
    public boolean isEmpty() {
        return size == 0;  // Проверяем пустое ли множество
    }

    // Вычисление хэш-кода для индекса в массиве
    private int getHashCode(E element) {
        return Math.abs(element.hashCode()) % elements.length;  // Берем модуль для попадания в диапазон массива
    }

    @Override
    public boolean add(E e) {
        int index = getHashCode(e);     // Вычисляем индекс бакета
        Node<E> current = elements[index];
        // Проверяем цепочку коллизий на наличие дубликата
        while (current != null) {
            if (current.data.equals(e)) {
                return false;  // Элемент уже существует
            }
            current = current.nextInSet;
        }
        // Создаем новый узел
        Node<E> newNode = new Node<>(e);
        newNode.nextInSet = elements[index];  // Добавляем в начало цепочки
        elements[index] = newNode;
        size++;
        list.add(newNode);  // Добавляем в двусвязный список
        // Проверяем необходимость увеличения размера
        if (size > elements.length * 0.75) {
            resize();
        }
        return true;
    }

    // Увеличение размера хэш-таблицы
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newElements = new Node[elements.length * 2];  // Увеличиваем размер в 2 раза
        Node<E> current = list.head;
        // Перераспределяем все элементы по новым бакетам
        while (current != null) {
            int newIndex = Math.abs(current.data.hashCode()) % newElements.length;  // Новый индекс
            current.nextInSet = newElements[newIndex];  // Добавляем в начало цепочки нового бакета
            newElements[newIndex] = current;
            current = current.next;
        }
        elements = newElements;  // Заменяем старый массив новым
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];
        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                // Удаляем из цепочки коллизий
                if (previous == null) {
                    elements[index] = current.nextInSet;  // Если первый в цепочке
                } else {
                    previous.nextInSet = current.nextInSet;  // Если в середине/конце
                }
                size--;
                list.remove(current);  // Удаляем из двусвязного списка
                return true;
            }
            previous = current;
            current = current.nextInSet;
        }
        return false;  // Элемент не найден
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        int index = getHashCode(e);
        Node<E> current = elements[index];
        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.data.equals(e)) {
                return true;
            }
            current = current.nextInSet;
        }
        return false;  // Элемент не найден
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем наличие всех элементов коллекции
        for (Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        // Добавляем все элементы коллекции
        for (E element : c) {
            if (add(element)) {
                result = true;  // Хотя бы один элемент был добавлен
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        // Удаляем все элементы коллекции
        for (Object object : c) {
            if (remove(object)) {
                result = true;  // Хотя бы один элемент был удален
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        // Создаем итератор для обхода всех элементов
        Iterator<Node<E>> iterator = new Iterator<Node<E>>() {
            private Node<E> current = list.head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Node<E> next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = current;
                current = current.next;
                return lastReturned;
            }
        };

        // Удаляем элементы, которых нет в коллекции c
        while (iterator.hasNext()) {
            Node<E> node = iterator.next();
            if (!c.contains(node.data)) {
                remove(node.data);
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = list.head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = current;
                E data = current.data;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(lastReturned.data);  // Используем remove из внешнего класса
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = list.head;
        // Копируем элементы в массив
        for (int i = 0; i < size && current != null; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если переданный массив слишком мал, создаем новый
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        Node<E> current = list.head;
        // Копируем элементы в массив
        for (int i = 0; i < size && current != null; i++) {
            a[i] = (T) current.data;
            current = current.next;
        }

        // Если массив больше чем нужно, устанавливаем маркер конца
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}