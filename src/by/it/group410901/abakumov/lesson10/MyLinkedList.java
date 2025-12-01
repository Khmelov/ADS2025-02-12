package by.it.group410901.abakumov.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Deque;

public class MyLinkedList<E> implements Deque<E> {
    //Узел списка: внутренний класс, представляющий элемент двусвязного списка
    private static class Node<E> {
        E item; // Значение элемента
        Node<E> prev; // Ссылка на предыдущий узел
        Node<E> next; // Ссылка на следующий узел

        // Конструктор узла: инициализирует значение и ссылки
        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    //Поля списка: голова (первый узел), хвост (последний узел) и размер списка
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    //Конструктор: создает пустой список
    public MyLinkedList() {}


    @Override
    // Преобразование списка в строку: [elem1, elem2, ...]
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; // Начинаем с головы
        while (current != null) { // Проходим по всем узлам
            sb.append(current.item);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }

    @Override
    // Добавление элемента: добавляет в конец и всегда возвращает true
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Удаление по индексу: проверяет индекс, находит узел и удаляет его
    public E remove(int index) {
        checkIndex(index); // Проверка валидности индекса
        Node<E> current = getNode(index); // Получение узла по индексу
        return unlink(current); // Удаление узла и возврат значения
    }

    @Override
    // Удаление первого вхождения объекта: ищет по значению и удаляет
    public boolean remove(Object element) {
        Node<E> current = head; // Начинаем поиск с головы
        while (current != null) {
            // Сравнение с учетом null (используем equals для non-null)
            if ((element == null && current.item == null) ||
                    (element != null && element.equals(current.item))) {
                unlink(current); // Удаляем найденный узел
                return true;
            }
            current = current.next;
        }
        return false; // Не найдено
    }

    // Возврат размера списка
    public int size() {
        return size;
    }

    @Override
    // Добавление в начало: создает новый узел и обновляет голову
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head); // Новый узел ссылается на текущую голову
        if (head != null) head.prev = newNode; // Если список не пуст, обновляем prev у старой головы
        head = newNode; // Новая голова
        if (tail == null) tail = head; // Если был пуст, tail тоже на новый узел
        size++; // Увеличиваем размер
    }

    @Override
    // Добавление в конец: создает новый узел и обновляет хвост
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null); // Новый узел ссылается на текущий хвост
        if (tail != null) tail.next = newNode; // Если список не пуст, обновляем next у старого хвоста
        tail = newNode; // Новый хвост
        if (head == null) head = tail; // Если был пуст, head тоже на новый узел
        size++; // Увеличиваем размер
    }

    @Override
    // Получение первого элемента (с исключение если пуст): бросает исключение если пусто
    public E element() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    // Аналогично element(): возвращает первый элемент или исключение
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    // Получение последнего элемента: бросает исключение если пусто
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    // Как pollFirst
    public E poll() {
        return pollFirst();
    }

    @Override
    // Удаление первого (возвращает null если пуст): удаляет голову если существует
    public E pollFirst() {
        if (head == null) return null;
        return unlink(head); // Удаляем и возвращаем значение головы
    }

    @Override
    // Удаление последнего (возвращает null если пуст): удаляет хвост если существует
    public E pollLast() {
        if (tail == null) return null;
        return unlink(tail); // Удаляем и возвращаем значение хвоста
    }

    // Проверка индекса: бросает исключение если индекс вне границ [0, size)
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
    }

    // Получение узла по индексу: оптимизировано - от головы если index < size/2, иначе от хвоста
    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            // Идем с начала для малых индексов
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            // Идем с конца для больших индексов (экономим шаги)
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    // Удаление узла: обновляет ссылки соседей, голову/хвост если нужно, уменьшает size
    private E unlink(Node<E> node) {
        E element = node.item; // Сохраняем значение для возврата
        Node<E> prev = node.prev; // Предыдущий узел
        Node<E> next = node.next; // Следующий узел

        // Если нет предыдущего - это была голова, новая голова = next
        if (prev == null) head = next;
        else prev.next = next; // Иначе связываем prev с next

        // Если нет следующего - это был хвост, новый хвост = prev
        if (next == null) tail = prev;
        else next.prev = prev; // Иначе связываем next с prev

        size--; // Уменьшаем размер
        return element; // Возвращаем удаленное значение
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }


}
