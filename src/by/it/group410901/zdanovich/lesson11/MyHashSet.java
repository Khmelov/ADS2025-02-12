package by.it.group410901.zdanovich.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Реализация собственного HashSet с сохранением порядка добавления (как LinkedHashSet)
public class MyHashSet<E> implements Set<E> {

    // Внутренний класс — узел списка и хэш-цепочки
    class Node<E> {
        E data;              // значение элемента
        Node<E> previous;    // ссылка на предыдущий элемент (для списка)
        Node<E> next;        // ссылка на следующий элемент (для списка)
        Node<E> nextInSet;   // ссылка на следующий элемент в цепочке одного бакета

        public Node(E data) {
            this.data = data; // сохраняем значение
        }
    }

    // Вспомогательный класс — двусвязный список для сохранения порядка вставки
    class List<E> {
        Node<E> head; // первый элемент
        Node<E> tail; // последний элемент

        // Добавление узла в конец списка
        void add(Node<E> node) {
            if (head == null) {   // если список пуст
                head = node;      // новый элемент становится головой
            } else {
                tail.next = node; // связываем старый хвост с новым элементом
                node.previous = tail; // новый знает своего предыдущего
            }
            tail = node;          // обновляем хвост
        }

        // Удаление узла из списка
        void remove(Node<E> node) {
            if (node.previous != null) {             // если есть предыдущий
                node.previous.next = node.next;      // переподключаем ссылки
            } else {
                head = head.next;                    // если удаляем голову
            }

            if (node.next != null) {                 // если есть следующий
                node.next.previous = node.previous;  // переподключаем ссылки
            } else {
                tail = tail.previous;                // если удаляем хвост
            }
        }

        // Полная очистка списка
        void clear() {
            head = null; // удаляем голову
            tail = null; // удаляем хвост
        }
    }

    Node<E>[] elements;               // массив бакетов для хэш-таблицы
    static final int INITIAL_SIZE = 16; // начальный размер таблицы
    int size;                          // текущее количество элементов
    List<E> list = new List<E>();      // двусвязный список для порядка вставки

    // Конструктор по умолчанию
    public MyHashSet() {
        this(INITIAL_SIZE); // вызывает конструктор с размером по умолчанию
    }

    // Конструктор с указанием размера
    public MyHashSet(int size) {
        elements = new Node[size]; // создаем массив бакетов
    }

    // Преобразование множества в строку, например: [a, b, c]
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // начинаем строку
        Node<E> current = list.head;               // начинаем с головы списка
        if (current != null) {
            sb.append(current.data);               // добавляем первый элемент
            current = current.next;                // переходим к следующему
        }
        while (current != null) {                  // пока не конец списка
            sb.append(", ");                       // добавляем запятую
            sb.append(current.data);               // добавляем элемент
            current = current.next;                // переходим дальше
        }
        sb.append("]");                            // закрываем скобку
        return sb.toString();                      // возвращаем строку
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size; // просто возвращаем размер
    }

    // Очистка множества
    @Override
    public void clear() {
        Arrays.fill(elements, null); // очищаем массив бакетов
        size = 0;                    // обнуляем размер
        list.clear();                // очищаем связный список
    }

    // Проверка, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0; // возвращаем true, если размер = 0
    }

    // Функция хэширования (находит индекс в массиве бакетов)
    private int getHashCode(E element) {
        return element.hashCode() % elements.length; // вычисляем индекс
    }

    // Добавление элемента в множество
    @Override
    public boolean add(E e) {
        int index = getHashCode(e);     // вычисляем индекс бакета
        Node<E> current = elements[index]; // берем первый элемент цепочки

        // Проверяем, есть ли уже такой элемент (дубликат)
        while (current != null) {
            if (current.data.equals(e)) {
                return false; // если дубликат — не добавляем
            }
            current = current.nextInSet; // идем дальше по цепочке
        }

        // Создаем новый узел
        Node<E> newNode = new Node<E>(e);
        newNode.nextInSet = elements[index]; // вставляем в начало цепочки
        elements[index] = newNode;           // обновляем ссылку в бакете
        size++;                              // увеличиваем размер
        list.add(newNode);                   // добавляем в список (для порядка)

        // Проверяем, нужно ли увеличить таблицу
        if (size > elements.length * 0.75) { // если заполнена на 75%
            Node<E>[] newElements = new Node[elements.length * 2]; // удваиваем размер
            current = list.head;             // идем по списку
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length; // новый индекс
                current.nextInSet = newElements[newIndex];                   // вставляем в цепочку
                newElements[newIndex] = current;                             // обновляем бакет
                current = current.next;                                      // переходим дальше
            }
            elements = newElements; // заменяем старый массив новым
        }
        return true; // добавление успешно
    }

    // Удаление элемента
    @Override
    public boolean remove(Object o) {
        E e = (E)o;                       // приводим объект к типу E
        int index = getHashCode(e);       // находим индекс бакета
        Node<E> previous = null;          // предыдущий узел в цепочке
        Node<E> current = elements[index]; // текущий узел
        while (current != null) {         // идем по цепочке
            if (current.data.equals(e)) { // если нашли элемент
                if (previous == null) {   // если это первый в цепочке
                    elements[index] = current.nextInSet; // удаляем из бакета
                } else {
                    previous.nextInSet = current.nextInSet; // переподключаем цепочку
                }
                size--;                   // уменьшаем размер
                list.remove(current);     // удаляем из связного списка
                return true;              // удаление успешно
            }
            previous = current;           // двигаем указатели
            current = current.nextInSet;
        }
        return false;                     // элемент не найден
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        E e = (E)o;                       // приводим тип
        int index = getHashCode(e);       // вычисляем индекс бакета
        Node<E> current = elements[index]; // получаем цепочку
        while (current != null) {         // идем по ней
            if (current.data.equals(e)) { // если нашли совпадение
                return true;              // элемент найден
            }
            current = current.nextInSet;  // переходим дальше
        }
        return false;                     // не найден
    }

    // Проверка, что множество содержит все элементы другой коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (var object : c) {          // идем по коллекции
            if (!contains(object)) {    // если хоть один отсутствует
                return false;           // возвращаем false
            }
        }
        return true;                    // все элементы есть
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;           // флаг изменения множества
        for (var element : c) {           // перебираем коллекцию
            if (add(element)) {           // добавляем элемент
                result = true;            // если что-то добавлено — отмечаем
            }
        }
        return result;                    // возвращаем результат
    }

    // Удаление всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;           // флаг изменения
        for (var object : c) {            // перебираем коллекцию
            if (remove(object)) {         // если элемент удален
                result = true;            // отмечаем
            }
        }
        return result;                    // возвращаем результат
    }

    // Оставляем только элементы, которые есть в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {       // если коллекция пуста
            this.clear();        // очищаем множество
            return true;         // считаем, что изменили
        }
        boolean isModified = false;                // флаг изменения
        MyHashSet<E> tempSet = new MyHashSet<>(elements.length); // временное множество
        Node<E> current = list.head;               // начинаем с головы
        while (current != null) {                  // идем по всем элементам
            if (c.contains(current.data)) {        // если элемент есть в коллекции
                tempSet.add(current.data);         // добавляем во временное множество
                isModified = true;                 // отмечаем изменение
            }
            current = current.next;                // переходим к следующему
        }

        // заменяем текущее множество новым
        elements = tempSet.elements;               // заменяем бакеты
        list.head = tempSet.list.head;             // обновляем голову
        list.tail = tempSet.list.tail;             // обновляем хвост
        size = tempSet.size;                       // обновляем размер

        return isModified;                         // возвращаем флаг изменения
    }

    // Методы-заглушки, не реализованы в этом варианте
    @Override public Iterator<E> iterator() {return null;}    // итератор не реализован
    @Override public Object[] toArray() {return new Object[0];} // преобразование в массив не реализовано
    @Override public <T> T[] toArray(T[] a) {return null;}      // тоже не реализовано
}
