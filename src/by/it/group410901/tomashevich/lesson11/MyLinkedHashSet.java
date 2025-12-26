package by.it.group410901.tomashevich.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Класс узла, представляющего элемент множества
    class Node<E> {
        E data;              // Данные (значение элемента)
        Node<E> previous;    // Ссылка на предыдущий элемент в списке
        Node<E> next;        // Ссылка на следующий элемент в списке
        Node<E> nextInSet;   // Ссылка на следующий элемент в цепочке (для коллизий)

        public Node(E data) {
            this.data = data; // Инициализация поля данных
        }
    }

    // Класс двусвязного списка для сохранения порядка вставки элементов
    class List<E> {
        Node<E> head; // Первый элемент списка
        Node<E> tail; // Последний элемент списка

        // Добавление узла в конец списка
        void add(Node<E> node) {
            if (head == null) {       // Если список пуст
                head = node;          // Новый элемент становится головой
            } else {                  // Если уже есть элементы
                tail.next = node;     // Последний элемент указывает на новый
                node.previous = tail; // Новый элемент указывает на старый хвост
            }
            tail = node;              // Новый элемент становится хвостом
        }

        // Удаление узла из списка
        void remove(Node<E> node) {
            if (node.previous != null) {          // Если есть предыдущий элемент
                node.previous.next = node.next;   // Переподключаем ссылки
            } else {                              // Если удаляем голову
                head = head.next;                 // Сдвигаем голову
            }

            if (node.next != null) {              // Если есть следующий элемент
                node.next.previous = node.previous; // Переподключаем ссылки назад
            } else {                              // Если удаляем хвост
                tail = tail.previous;             // Сдвигаем хвост
            }
        }

        // Очистка списка (удаление всех ссылок)
        void clear() {
            head = null; // Сбрасываем голову
            tail = null; // Сбрасываем хвост
        }
    }

    Node<E>[] elements;          // Массив бакетов (цепочек элементов)
    static final int INITIAL_SIZE = 16; // Начальный размер таблицы
    int size;                    // Текущее количество элементов
    List<E> list = new List<E>(); // Список для хранения порядка вставки

    // Конструктор без параметров
    public MyLinkedHashSet() {
        this(INITIAL_SIZE); // Используем размер по умолчанию
    }

    // Конструктор с заданным размером
    public MyLinkedHashSet(int size) {
        elements = new Node[size]; // Инициализация массива бакетов
    }

    // Строковое представление множества
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // Начинаем со скобки
        Node<E> current = list.head;               // Начинаем с первого элемента
        if (current != null) {                     // Если список не пуст
            sb.append(current.data);               // Добавляем первый элемент
            current = current.next;                // Переходим к следующему
        }
        while (current != null) {                  // Пока есть элементы
            sb.append(", ");                       // Разделитель
            sb.append(current.data);               // Добавляем значение
            current = current.next;                // Двигаемся дальше
        }
        sb.append("]");                            // Закрывающая скобка
        return sb.toString();                      // Возвращаем строку
    }

    // Возвращает количество элементов в множестве
    @Override
    public int size() {
        return size; // Просто возвращаем счётчик
    }

    // Полная очистка множества
    @Override
    public void clear() {
        Arrays.fill(elements, null); // Очищаем все бакеты (обнуляем массив)
        size = 0;                    // Обнуляем счётчик элементов
        list.clear();                // Очищаем двусвязный список
    }

    // Проверка, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0; // Если элементов нет, возвращаем true
    }

    // Хэш-функция: вычисляет индекс для элемента
    private int getHashCode(E element) {
        return element.hashCode() % elements.length; // Остаток от деления хэша на длину массива
    }

    // Добавление элемента в множество
    @Override
    public boolean add(E e) {
        int index = getHashCode(e);           // Вычисляем индекс бакета
        Node<E> current = elements[index];    // Получаем первый элемент цепочки в этом бакете

        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if (current.data.equals(e)) {     // Если элемент найден — не добавляем
                return false;                 // Возвращаем false (множество не изменилось)
            }
            current = current.nextInSet;      // Переходим к следующему в цепочке
        }

        // Создаём новый узел для добавления
        Node<E> newNode = new Node<E>(e);
        newNode.nextInSet = elements[index];  // Новый узел указывает на предыдущий первый элемент цепочки
        elements[index] = newNode;            // Новый узел становится первым в бакете

        size++;                               // Увеличиваем размер множества

        list.add(newNode);                    // Добавляем в связный список для сохранения порядка вставки

        // Проверяем, не превышен ли порог заполненности (75%)
        if (size > elements.length * 0.75) {
            Node<E>[] newElements = new Node[elements.length * 2]; // Увеличиваем размер таблицы вдвое

            current = list.head;              // Начинаем перехэширование со списка
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length; // Новый индекс
                current.nextInSet = newElements[newIndex]; // Переносим в новый бакет
                newElements[newIndex] = current;           // Обновляем ссылку
                current = current.next;                    // Переходим к следующему
            }
            elements = newElements; // Присваиваем новую таблицу
        }

        return true; // Элемент успешно добавлен
    }

    // Удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        E e = (E) o;                         // Приводим объект к типу E
        int index = getHashCode(e);          // Вычисляем индекс бакета
        Node<E> previous = null;             // Предыдущий элемент в цепочке
        Node<E> current = elements[index];   // Текущий элемент цепочки

        // Ищем элемент в цепочке
        while (current != null) {
            if (current.data.equals(e)) {    // Если нашли нужный элемент
                if (previous == null) {      // Если элемент первый в цепочке
                    elements[index] = current.nextInSet; // Просто двигаем голову цепочки
                } else {                     // Если элемент не первый
                    previous.nextInSet = current.nextInSet; // Пропускаем текущий узел
                }
                size--;                      // Уменьшаем размер множества
                list.remove(current);        // Удаляем элемент из двусвязного списка
                return true;                 // Элемент успешно удалён
            }
            previous = current;              // Сохраняем текущий как предыдущий
            current = current.nextInSet;     // Идём дальше по цепочке
        }
        return false;                        // Элемент не найден
    }

    // Проверка, содержится ли элемент
    @Override
    public boolean contains(Object o) {
        E e = (E) o;                         // Приводим к типу E
        int index = getHashCode(e);          // Вычисляем индекс
        Node<E> current = elements[index];   // Берём первый элемент в цепочке

        while (current != null) {            // Перебираем цепочку
            if (current.data.equals(e)) {    // Если нашли совпадение
                return true;                 // Возвращаем true
            }
            current = current.nextInSet;     // Переходим к следующему элементу
        }
        return false;                        // Элемент не найден
    }

    // Проверка, содержатся ли все элементы из другой коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (var object : c) {               // Перебираем все элементы коллекции
            if (!contains(object)) {         // Если хотя бы один отсутствует
                return false;                // Возвращаем false
            }
        }
        return true;                         // Все элементы найдены
    }

    // Добавление всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;              // Флаг изменения множества
        for (var element : c) {              // Перебираем все элементы коллекции
            if (add(element)) {              // Добавляем каждый элемент
                result = true;               // Если хотя бы один добавился — изменилось
            }
        }
        return result;                       // Возвращаем результат
    }

    // Удаление всех элементов коллекции из множества
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;              // Флаг изменения
        for (var object : c) {               // Перебираем элементы
            if (remove(object)) {            // Удаляем каждый, если найден
                result = true;               // Если что-то удалено — изменилось
            }
        }
        return result;                       // Возвращаем результат
    }

    // Оставляем только те элементы, которые есть в заданной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {                   // Если коллекция пуста
            this.clear();                    // Очищаем множество полностью
            return true;                     // Возвращаем true (изменилось)
        }

        boolean isModified = false;          // Флаг изменения
        MyLinkedHashSet<E> tempSet = new MyLinkedHashSet<>(elements.length); // Временное множество
        Node<E> current = list.head;         // Начинаем с головы списка

        while (current != null) {            // Перебираем все элементы
            if (c.contains(current.data)) {  // Если элемент есть в коллекции
                tempSet.add(current.data);   // Добавляем его во временное множество
                isModified = true;           // Отмечаем изменение
            }
            current = current.next;          // Идём дальше
        }

        // Переносим ссылки из временного множества
        elements = tempSet.elements;         // Заменяем таблицу
        list.head = tempSet.list.head;       // Заменяем голову списка
        list.tail = tempSet.list.tail;       // Заменяем хвост списка
        size = tempSet.size;                 // Обновляем размер

        return isModified;                   // Возвращаем, было ли изменение
    }

    // Методы не реализованы
    @Override
    public Iterator<E> iterator() {return null;}
    @Override
    public Object[] toArray() {return new Object[0];}
    @Override
    public <T> T[] toArray(T[] a) {return null;}
}
