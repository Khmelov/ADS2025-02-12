package by.it.group410902.jalilova.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // узел элемента, хранит данные и ссылки для двусвязного списка и цепочки в бакете
    class Node<E> {
        E data; // само значение
        Node<E> previous; // предыдущий элемент в общем списке
        Node<E> next; // следующий элемент в общем списке
        Node<E> nextInSet; // следующий элемент в цепочке бакета (для коллизий)

        public Node(E data) {
            this.data = data;
        }
    }

    // двусвязный список для хранения элементов в порядке добавления
    class List<E> {
        Node<E> head; // первый элемент списка
        Node<E> tail; // последний элемент списка

        // добавление узла в конец списка
        void add(Node<E> node) {
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
                node.previous = tail;
            }
            tail = node;
        }

        // удаление узла из списка
        void remove(Node<E> node) {
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                head = head.next;
            }

            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                tail = tail.previous;
            }
        }

        // очистка списка
        void clear() {
            head = null;
            tail = null;
        }
    }

    Node<E>[] elements; // массив бакетов для хеширования
    static final int INITIAL_SIZE = 16; // начальный размер массива
    int size; // количество элементов в множестве
    List<E> list = new List<E>(); // список для хранения порядка добавления

    public MyLinkedHashSet() {
        this(INITIAL_SIZE);
    }

    public MyLinkedHashSet(int size) {
        elements = new Node[size]; // инициализация массива бакетов
    }

    // возвращает строковое представление множества в порядке добавления
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head; // начинаем с головы списка
        if (current != null) {
            sb.append(current.data);
            current = current.next;
        }
        while (current != null) {
            sb.append(", ");
            sb.append(current.data);
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size; // возвращаем количество элементов
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null); // очищаем массив бакетов
        size = 0;
        list.clear(); // очищаем список порядка добавления
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // проверяем, пусто ли множество
    }

    // вычисление индекса бакета по хешу элемента
    private int getHashCode(E element) {
        return element.hashCode() % elements.length;
    }

    @Override
    public boolean add(E e) {
        int index = getHashCode(e); // находим бакет
        Node<E> current = elements[index];
        // проверка на дубликаты
        while (current != null) {
            if (current.data.equals(e)) {
                return false; // элемент уже есть
            }
            current = current.nextInSet;
        }
        // создаем новый узел и вставляем в начало цепочки бакета
        Node<E> newNode = new Node<E>(e);
        newNode.nextInSet = elements[index];
        elements[index] = newNode;
        size++;
        list.add(newNode); // добавляем в конец списка порядка добавления

        // если заполнено больше 75%, увеличиваем массив и перераспределяем элементы
        if (size > elements.length * 0.75) {
            Node<E>[] newElements = new Node[elements.length * 2];
            current = list.head;
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length;
                current.nextInSet = newElements[newIndex];
                newElements[newIndex] = current;
                current = current.next;
            }
            elements = newElements;
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];
        // ищем элемент в цепочке бакета
        while (current != null) {
            if (current.data.equals(e)) {
                if (previous == null) {
                    elements[index] = current.nextInSet;
                } else {
                    previous.nextInSet = current.nextInSet;
                }
                size--;
                list.remove(current); // удаляем из списка порядка добавления
                return true;
            }
            previous = current;
            current = current.nextInSet;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> current = elements[index];
        // проверка наличия элемента в цепочке бакета
        while (current != null) {
            if (current.data.equals(e)) {
                return true;
            }
            current = current.nextInSet;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var object : c) {
            if (!contains(object)) {
                return false; // хотя бы один элемент отсутствует
            }
        }
        return true; // все элементы присутствуют
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (var element : c) {
            if (add(element)) { // если элемент реально добавлен
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (var object : c) {
            if (remove(object)) { // если элемент реально удален
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.clear();
            return true;
        }
        boolean isModified = false;
        // создаем временное множество для хранения пересечения
        MyLinkedHashSet<E> tempSet = new MyLinkedHashSet<>(elements.length);
        Node<E> current = list.head;
        while (current != null) {
            if (c.contains(current.data)) { // оставляем только элементы, которые есть в коллекции c
                tempSet.add(current.data);
                isModified = true;
            }
            current = current.next;
        }
        // заменяем старые данные на новые
        elements = tempSet.elements;
        list.head = tempSet.list.head;
        list.tail = tempSet.list.tail;
        size = tempSet.size;

        return isModified;
    }

    @Override
    public Iterator<E> iterator() {return null;}
    @Override
    public Object[] toArray() {return new Object[0];}
    @Override
    public <T> T[] toArray(T[] a) {return null;}
}
