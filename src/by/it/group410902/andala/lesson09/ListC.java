package by.it.group410902.andala.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {


    // Внутренний класс узла двусвязного списка
    // Каждый узел хранит значение и ссылки на соседние узлы
    private static class Node<E> {
        E value;       // значение элемента
        Node<E> next;  // ссылка на следующий узел
        Node<E> prev;  // ссылка на предыдущий узел

        Node(E value) {
            this.value = value;
        }
    }

    // Ссылки на первый и последний элементы списка
    private Node<E> head; // первый элемент
    private Node<E> tail; // последний элемент
    private int size = 0; // количество элементов в списке

    // ----------------- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ -----------------

    // Возвращает узел по индексу.
    // Если индекс ближе к началу — идём от head, иначе — от tail.
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        Node<E> current;
        if (index < size / 2) {
            // проход от начала
            current = head;
            for (int i = 0; i < index; i++)
                current = current.next;
        } else {
            // проход от конца
            current = tail;
            for (int i = size - 1; i > index; i--)
                current = current.prev;
        }
        return current;
    }

    // ----------------- ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ -----------------

    // Добавляет элемент в конец списка.
    // Создаём новый узел и связываем его с tail.
    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e);
        if (head == null) {        // если список пустой
            head = tail = newNode; // новый узел — и голова, и хвост
        } else {
            tail.next = newNode;   // старый хвост указывает на новый
            newNode.prev = tail;   // новый узел ссылается на старый хвост
            tail = newNode;        // обновляем хвост
        }
        size++;
        return true;
    }

    // Вставка элемента по индексу.
    // Вставляем перед узлом, который сейчас стоит по этому индексу.
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        if (index == size) { // эквивалент add(element)
            add(element);
            return;
        }

        Node<E> newNode = new Node<>(element);
        Node<E> current = getNode(index); // узел, перед которым вставляем
        Node<E> prev = current.prev;

        newNode.next = current;   // новый -> текущий
        newNode.prev = prev;      // новый -> предыдущий
        current.prev = newNode;   // текущий.prev -> новый

        if (prev != null)
            prev.next = newNode;  // предыдущий.next -> новый
        else
            head = newNode;      // если вставка в начало, обновляем head

        size++;
    }

    // Удаление элемента по индексу.
    // Переподключаем соседние узлы и очищаем ссылки в удаляемом узле.
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node<E> node = getNode(index);
        E value = node.value;

        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev != null)
            prev.next = next;
        else
            head = next; // удалили голову

        if (next != null)
            next.prev = prev;
        else
            tail = prev; // удалили хвост

        // помощь сборщику мусора — убираем ссылки
        node.prev = null;
        node.next = null;
        node.value = null;

        size--;
        return value;
    }

    // Удаление по значению. Идём по списку от head и находим первый совпадающий.
    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(current.value, o)) {
                Node<E> prev = current.prev;
                Node<E> next = current.next;

                if (prev != null)
                    prev.next = next;
                else
                    head = next;

                if (next != null)
                    next.prev = prev;
                else
                    tail = prev;

                // очистка ссылок для GC
                current.prev = null;
                current.next = null;
                current.value = null;

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Замена значения в узле по индексу.
    @Override
    public E set(int index, E element) {
        Node<E> node = getNode(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    // Получение значения по индексу — используем getNode.
    @Override
    public E get(int index) {
        return getNode(index).value;
    }

    // Возвращает количество элементов в списке.
    @Override
    public int size() {
        return size;
    }

    // Полная очистка списка — пробегаем по всем узлам и стираем ссылки.
    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.prev = null;
            current.next = null;
            current.value = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    // Проверка пустой ли список.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Строковое представление: элементы в квадратных скобках через ", "
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null)
                sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // Проверяет наличие элемента, используя indexOf
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Возвращает индекс первого вхождения или -1
    @Override
    public int indexOf(Object o) {
        Node<E> current = head;
        int index = 0;
        while (current != null) {
            if (Objects.equals(current.value, o))
                return index;
            index++;
            current = current.next;
        }
        return -1;
    }

    // Возвращает индекс последнего вхождения, идя от tail
    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = tail;
        int index = size - 1;
        while (current != null) {
            if (Objects.equals(current.value, o))
                return index;
            index--;
            current = current.prev;
        }
        return -1;
    }

    // ----------------- ДОБАВЛЕННЫЕ МЕТОДЫ addAll -----------------

    // Добавляет все элементы из коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return false;

        for (E element : c) {
            add(element);
        }
        return true;
    }

    // Вставляет все элементы коллекции, начиная с указанного индекса.
    // Через временный ArrayList сохраняем порядок вставки.
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return false;
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        // временный список чтобы сохранить порядок при множественной вставке
        List<E> temp = new ArrayList<>();
        temp.addAll(c);

        // вставляем в обратном порядке: так элементы окажутся в исходном порядке
        for (int i = temp.size() - 1; i >= 0; i--) {
            add(index, temp.get(i));
        }

        return true;
    }

    // ----------------- УЛУЧШЕННЫЕ РЕАЛИЗАЦИИ -----------------

    // Проверяет, содержит ли список все элементы из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Удаляет из списка все элементы, которые содержатся в коллекции c
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove(); // итератор корректно обновит связки узлов
                modified = true;
            }
        }
        return modified;
    }

    // Сохраняет в списке только элементы, которые содержатся в c
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // Возвращает новый ListC, содержащий элементы в диапазоне [fromIndex, toIndex)
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();

        ListC<E> subList = new ListC<>();
        Node<E> current = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(current.value);
            current = current.next;
        }
        return subList;
    }

    // Копирует элементы в массив Object[]
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.value;
            current = current.next;
        }
        return array;
    }

    // Заполняет массив a значениями списка; при необходимости создаёт новый массив нужного типа
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException();

        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }

        Object[] result = a;
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            result[i] = current.value;
            current = current.next;
        }

        if (a.length > size) {
            a[size] = null; // соблюдаем контракт toArray
        }

        return a;
    }

    // ----------------- ПРОСТОЙ ИТЕРАТОР -----------------

    // Итератор проходит по списку от head к tail и поддерживает remove()
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;        // следующий для отдачи
            private Node<E> lastReturned = null;   // последний возвращённый узел (для remove)
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                E value = current.value;
                current = current.next;
                currentIndex++;
                return value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }

                Node<E> prev = lastReturned.prev;
                Node<E> next = lastReturned.next;

                if (prev != null) {
                    prev.next = next;
                } else {
                    head = next;
                }

                if (next != null) {
                    next.prev = prev;
                } else {
                    tail = prev;
                }

                // если current указывает на удалённый элемент — сдвигаем его
                if (current == lastReturned) {
                    current = next;
                } else {
                    currentIndex--;
                }

                size--;
                lastReturned = null;
            }
        };
    }

    // ----------------- ПРОСТЫЕ РЕАЛИЗАЦИИ СЛОЖНЫХ МЕТОДОВ -----------------

    // listIterator реализован просто: создаём временный ArrayList и возвращаем его listIterator.
    // Это удобно и корректно, но менее эффективно по памяти/времени.
    @Override
    public ListIterator<E> listIterator() {
        List<E> copy = new ArrayList<>();
        Node<E> current = head;
        while (current != null) {
            copy.add(current.value);
            current = current.next;
        }
        return copy.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        List<E> copy = new ArrayList<>();
        Node<E> current = head;
        while (current != null) {
            copy.add(current.value);
            current = current.next;
        }
        return copy.listIterator(index);
    }
}
