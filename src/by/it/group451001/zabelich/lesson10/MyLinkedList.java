package by.it.group451001.zabelich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс Node (Узел) для реализации двусвязного списка
    // Каждый узел содержит: данные, ссылку на предыдущий узел и ссылку на следующий
    // узел
    private static class Node<E> {
        E data; // Данные, хранящиеся в узле
        Node<E> prev; // Ссылка на предыдущий узел в списке
        Node<E> next; // Ссылка на следующий узел в списке

        // Конструктор узла
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    // Ссылка на первый узел списка (head)
    // Если список пуст, first = null
    private Node<E> first;

    // Ссылка на последний узел списка (tail)
    // Если список пуст, last = null
    private Node<E> last;

    // Количество элементов в списке
    // Используется для быстрого получения размера без обхода всего списка
    private int size;

    // Конструктор по умолчанию
    // Инициализирует пустой список
    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Обязательные к реализации методы ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка в формате [element1, element2, ...]
     * Время выполнения: O(n) - нужно пройти по всем элементам списка
     */
    @Override
    public String toString() {
        // Если список пуст, возвращаем пустые скобки
        if (size == 0) {
            return "[]";
        }

        // StringBuilder для эффективного построения строки
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        // Начинаем с первого узла
        Node<E> current = first;

        // Проходим по всем узлам списка
        while (current != null) {
            sb.append(current.data);

            // Если есть следующий узел, добавляем запятую и пробел
            if (current.next != null) {
                sb.append(", ");
            }

            // Переходим к следующему узлу
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка (эквивалентно addLast)
     * Возвращает true при успешном добавлении
     * Время выполнения: O(1) - так как есть ссылка на последний элемент
     */
    @Override
    public boolean add(E element) {
        // Делегируем добавление методу addLast
        addLast(element);
        return true;
    }

    /**
     * Удаляет элемент по индексу
     * Возвращает удаленный элемент
     * Время выполнения: O(n) - в худшем случае нужно пройти до нужного индекса
     */
    public E remove(int index) {
        // Проверяем валидность индекса
        checkElementIndex(index);

        // Находим узел по индексу
        Node<E> nodeToRemove = getNode(index);

        // Удаляем узел и возвращаем его данные
        return unlink(nodeToRemove);
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * Возвращает true если элемент был найден и удален
     * Время выполнения: O(n) - нужно найти элемент в списке
     */
    public boolean remove(Object element) {
        // Ищем узел с указанным элементом
        Node<E> nodeToRemove = findNodeByElement(element);

        // Если узел не найден, возвращаем false
        if (nodeToRemove == null) {
            return false;
        }

        // Удаляем найденный узел
        unlink(nodeToRemove);
        return true;
    }

    /**
     * Возвращает количество элементов в списке
     * Время выполнения: O(1) - просто возвращаем значение поля size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в начало списка
     * Время выполнения: O(1) - просто создаем новый узел и обновляем ссылки
     */
    @Override
    public void addFirst(E element) {
        // Сохраняем ссылку на текущий первый узел
        Node<E> oldFirst = first;

        // Создаем новый узел, который будет новым первым
        // prev = null (перед ним ничего нет), next = старый первый узел
        Node<E> newNode = new Node<>(element, null, oldFirst);

        // Обновляем первый узел списка
        first = newNode;

        // Если список был пуст, новый узел также становится последним
        if (oldFirst == null) {
            last = newNode;
        } else {
            // Иначе обновляем ссылку у старого первого узла
            oldFirst.prev = newNode;
        }

        // Увеличиваем счетчик элементов
        size++;
    }

    /**
     * Добавляет элемент в конец списка
     * Время выполнения: O(1) - так как есть ссылка на последний элемент
     */
    @Override
    public void addLast(E element) {
        // Сохраняем ссылку на текущий последний узел
        Node<E> oldLast = last;

        // Создаем новый узел, который будет новым последним
        // prev = старый последний узел, next = null (после него ничего нет)
        Node<E> newNode = new Node<>(element, oldLast, null);

        // Обновляем последний узел списка
        last = newNode;

        // Если список был пуст, новый узел также становится первым
        if (oldLast == null) {
            first = newNode;
        } else {
            // Иначе обновляем ссылку у старого последнего узла
            oldLast.next = newNode;
        }

        // Увеличиваем счетчик элементов
        size++;
    }

    /**
     * Возвращает первый элемент без удаления (эквивалентно getFirst)
     * Бросает исключение если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент без удаления
     * Бросает исключение если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E getFirst() {
        // Проверяем что список не пуст
        checkNotEmpty();

        // Возвращаем данные первого узла
        return first.data;
    }

    /**
     * Возвращает последний элемент без удаления
     * Бросает исключение если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E getLast() {
        // Проверяем что список не пуст
        checkNotEmpty();

        // Возвращаем данные последнего узла
        return last.data;
    }

    /**
     * Удаляет и возвращает первый элемент (эквивалентно pollFirst)
     * Возвращает null если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент
     * Возвращает null если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E pollFirst() {
        // Если список пуст, возвращаем null
        if (first == null) {
            return null;
        }

        // Сохраняем данные первого узла для возврата
        E data = first.data;

        // Удаляем первый узел
        unlinkFirst();

        return data;
    }

    /**
     * Удаляет и возвращает последний элемент
     * Возвращает null если список пуст
     * Время выполнения: O(1)
     */
    @Override
    public E pollLast() {
        // Если список пуст, возвращаем null
        if (last == null) {
            return null;
        }

        // Сохраняем данные последнего узла для возврата
        E data = last.data;

        // Удаляем последний узел
        unlinkLast();

        return data;
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Вспомогательные методы ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Вспомогательный метод для получения узла по индексу
     * Время выполнения: O(n) - в худшем случае
     */
    private Node<E> getNode(int index) {
        // Проверяем валидность индекса
        checkElementIndex(index);

        // Оптимизация: если индекс в первой половине списка, идем с начала
        if (index < (size >> 1)) {
            Node<E> current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        // Иначе идем с конца
        else {
            Node<E> current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    /**
     * Вспомогательный метод для поиска узла по элементу
     * Возвращает null если элемент не найден
     */
    private Node<E> findNodeByElement(Object element) {
        // Обрабатываем случай когда ищем null
        if (element == null) {
            for (Node<E> current = first; current != null; current = current.next) {
                if (current.data == null) {
                    return current;
                }
            }
        }
        // Обрабатываем случай когда ищем не-null элемент
        else {
            for (Node<E> current = first; current != null; current = current.next) {
                if (element.equals(current.data)) {
                    return current;
                }
            }
        }
        return null;
    }

    /**
     * Вспомогательный метод для удаления узла из списка
     * Корректно обновляет ссылки соседних узлов
     */
    private E unlink(Node<E> node) {
        // Сохраняем данные узла для возврата
        E data = node.data;

        // Сохраняем ссылки на соседние узлы
        Node<E> prevNode = node.prev;
        Node<E> nextNode = node.next;

        // Обновляем ссылки: если есть предыдущий узел, его next теперь указывает на
        // следующий узел
        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            // Если предыдущего узла нет, значит удаляем первый узел
            first = nextNode;
        }

        // Обновляем ссылки: если есть следующий узел, его prev теперь указывает на
        // предыдущий узел
        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            // Если следующего узла нет, значит удаляем последний узел
            last = prevNode;
        }

        // Очищаем ссылки удаляемого узла (помощь сборщику мусора)
        node.data = null;
        node.prev = null;
        node.next = null;

        // Уменьшаем счетчик элементов
        size--;

        return data;
    }

    /**
     * Вспомогательный метод для удаления первого узла
     */
    private void unlinkFirst() {
        // Сохраняем ссылку на первый узел
        Node<E> oldFirst = first;

        // Первым узлом становится следующий за старым первым
        first = oldFirst.next;

        // Если новый первый узел существует, обнуляем его prev ссылку
        if (first != null) {
            first.prev = null;
        } else {
            // Если список стал пустым, обнуляем last
            last = null;
        }

        // Очищаем ссылки старого первого узла
        oldFirst.data = null;
        oldFirst.next = null;

        // Уменьшаем счетчик элементов
        size--;
    }

    /**
     * Вспомогательный метод для удаления последнего узла
     */
    private void unlinkLast() {
        // Сохраняем ссылку на последний узел
        Node<E> oldLast = last;

        // Последним узлом становится предыдущий перед старым последним
        last = oldLast.prev;

        // Если новый последний узел существует, обнуляем его next ссылку
        if (last != null) {
            last.next = null;
        } else {
            // Если список стал пустым, обнуляем first
            first = null;
        }

        // Очищаем ссылки старого последнего узла
        oldLast.data = null;
        oldLast.prev = null;

        // Уменьшаем счетчик элементов
        size--;
    }

    /**
     * Проверяет что индекс находится в допустимых пределах [0, size-1]
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Проверяет что список не пуст
     */
    private void checkNotEmpty() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Остальные методы интерфейса Deque (заглушки) ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0)
            throw new NoSuchElementException();
        return remove(0);
    }

    @Override
    public E removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        return remove(0);
    }

    @Override
    public E removeLast() {
        if (size == 0)
            throw new NoSuchElementException();
        return remove(size - 1);
    }

    @Override
    public E peek() {
        return (size == 0) ? null : getFirst();
    }

    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // Остальные методы интерфейса не реализованы
    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}