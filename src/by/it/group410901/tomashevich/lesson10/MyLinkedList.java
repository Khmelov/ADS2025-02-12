package by.it.group410901.tomashevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Реализация двусвязного списка, который реализует интерфейс Deque<E>
public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс, описывающий узел списка
    class MyLinkedListNode<E> {
        public E Data;                       // данные, хранящиеся в узле
        public MyLinkedListNode<E> Previous; // ссылка на предыдущий узел
        public MyLinkedListNode<E> Next;     // ссылка на следующий узел

        // Конструктор узла, принимает данные
        public MyLinkedListNode(E data) {
            Data = data; // сохраняем значение
        }
    }

    // Ссылки на первый и последний элемент списка
    MyLinkedListNode<E> _head;
    MyLinkedListNode<E> _tail;
    // Количество элементов в списке
    int _size;

    // Конструктор: создаёт пустой список
    MyLinkedList() {
        _head = null; // изначально голова отсутствует
        _tail = null; // и хвост тоже
        _size = 0;    // размер равен 0
    }

    // Метод для красивого вывода списка в виде строки: [a, b, c]
    public String toString() {
        StringBuilder sb = new StringBuilder(); // создаём StringBuilder для эффективного соединения строк
        sb.append('[');                         // открывающая квадратная скобка
        MyLinkedListNode<E> temp = _head;       // начинаем с головы списка
        for (int i = 0; i < _size; i++) {       // цикл по всем элементам списка
            sb.append(temp.Data);               // добавляем данные текущего узла
            if (i < _size - 1) {                // если это не последний элемент
                sb.append(", ");                // добавляем запятую и пробел для форматирования
            }
            temp = temp.Next;                   // переходим к следующему узлу
        }
        sb.append(']');                         // закрывающая скобка
        return sb.toString();                   // возвращаем собранную строку
    }

    // Добавление элемента в конец (использует addLast)
    @Override
    public boolean add(E e) {
        addLast(e); // вызываем метод добавления в конец списка
        return true; // возвращаем true, так как элемент успешно добавлен
    }

    // Удаление элемента по индексу
    public E remove(int index) {
        if (index < 0 || index >= _size) { // если индекс выходит за пределы списка
            return null;                   // возвращаем null
        }

        MyLinkedListNode<E> temp = _head; // начинаем с головы
        for (int i = 0; i < index; i++) { // идём до нужного индекса
            temp = temp.Next;             // переходим к следующему узлу
        }

        E e = temp.Data; // сохраняем данные удаляемого узла

        // Если у узла есть предыдущий элемент
        if (temp.Previous != null) {
            temp.Previous.Next = temp.Next; // связываем предыдущий и следующий узлы, пропуская текущий
        } else {
            _head = temp.Next; // если удаляется голова — сдвигаем её
        }

        // Если у узла есть следующий элемент
        if (temp.Next != null) {
            temp.Next.Previous = temp.Previous; // указываем предыдущий для следующего узла
        } else {
            _tail = temp.Previous; // если удаляется хвост — обновляем ссылку на хвост
        }

        _size--; // уменьшаем размер списка
        return e; // возвращаем удалённый элемент
    }

    // Удаление первого вхождения объекта по значению
    @Override
    public boolean remove(Object o) {
        MyLinkedListNode<E> temp = _head; // начинаем с головы
        int index = 0;                    // счётчик индекса
        while (temp != null) {            // пока не достигнут конец списка
            if (temp.Data.equals(o)) {    // если нашли совпадение по equals
                remove(index);            // удаляем элемент по индексу
                return true;              // возвращаем true — удаление прошло успешно
            }
            index++;                      // переходим к следующему индексу
            temp = temp.Next;             // переходим к следующему элементу
        }
        return false;                     // если не нашли элемент — возвращаем false
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return _size; // возвращаем текущее значение счётчика _size
    }

    // Добавление элемента в начало списка
    @Override
    public void addFirst(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e); // создаём новый узел с данными
        if (_head != null) {               // если список не пуст
            node.Next = _head;             // новый узел указывает на старую голову
            _head.Previous = node;         // старая голова указывает назад на новый узел
        }
        _head = node;                      // новый узел становится головой списка

        if (_tail == null) {               // если это первый элемент в списке
            _tail = node;                  // он становится и хвостом
        }

        _size++;                           // увеличиваем количество элементов
    }

    // Добавление элемента в конец списка
    @Override
    public void addLast(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e); // создаём новый узел
        if (_tail != null) {              // если хвост уже существует
            _tail.Next = node;            // старый хвост теперь указывает на новый узел
            node.Previous = _tail;        // новый узел указывает назад на старый хвост
        }
        _tail = node;                     // новый узел становится хвостом

        if (_head == null) {              // если список был пуст
            _head = node;                 // новый элемент становится головой
        }

        _size++;                          // увеличиваем размер списка
    }

    // Возвращает первый элемент (аналог метода element() из Queue)
    @Override
    public E element() {
        return getFirst(); // просто вызывает getFirst()
    }

    // Получить значение первого узла
    @Override
    public E getFirst() {
        if (_size == 0) {  // если список пуст
            return null;   // возвращаем null
        }
        return _head.Data; // иначе возвращаем значение головы
    }

    // Получить значение последнего узла
    @Override
    public E getLast() {
        if (_size == 0) {  // если список пуст
            return null;   // возвращаем null
        }
        return _tail.Data; // иначе возвращаем значение хвоста
    }

    // Удалить первый элемент (аналог poll() из Queue)
    @Override
    public E poll() {
        return pollFirst(); // используем уже реализованный метод удаления первого
    }

    // Удалить и вернуть первый элемент
    @Override
    public E pollFirst() {
        if (_size == 0) {  // если список пуст
            return null;   // возвращаем null
        }

        E e = _head.Data;        // сохраняем данные головы
        _head = _head.Next;      // сдвигаем голову на следующий элемент

        if (_head != null) {     // если новый первый элемент существует
            _head.Previous = null; // обнуляем его ссылку назад
        } else {
            _tail = null;        // если список стал пустым — обнуляем хвост
        }

        _size--;                 // уменьшаем размер
        return e;                // возвращаем удалённое значение
    }

    // Удалить и вернуть последний элемент
    @Override
    public E pollLast() {
        if (_size == 0) {  // если список пуст
            return null;   // возвращаем null
        }

        E e = _tail.Data;        // сохраняем данные хвоста
        _tail = _tail.Previous;  // двигаем хвост на один элемент назад

        if (_tail != null) {     // если остался элемент в списке
            _tail.Next = null;   // обнуляем ссылку на следующий (так как это новый хвост)
        } else {
            _head = null;        // если список теперь пуст — обнуляем голову
        }

        _size--;                 // уменьшаем размер
        return e;                // возвращаем удалённые данные
    }

    // -----------------------------------------------------------
    // Методы-заглушки (пока не реализованы)
    // -----------------------------------------------------------

    @Override
    public boolean offerFirst(E e) { return false; }
    @Override
    public boolean offerLast(E e) { return false; }
    @Override
    public E removeFirst() { return null; }
    @Override
    public E removeLast() { return null; }
    @Override
    public E peekFirst() { return null; }
    @Override
    public E peekLast() { return null; }
    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }
    @Override
    public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean offer(E e) { return false; }
    @Override
    public E remove() { return null; }
    @Override
    public E peek() { return null; }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public void clear() { }
    @Override
    public void push(E e) { }
    @Override
    public E pop() { return null; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean contains(Object o) { return false; }
    @Override
    public boolean isEmpty() { return false; }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Iterator<E> descendingIterator() { return null; }
}
