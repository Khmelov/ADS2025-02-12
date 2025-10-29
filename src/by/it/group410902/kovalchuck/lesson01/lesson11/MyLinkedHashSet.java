package by.it.group410902.kovalchuck.lesson01.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Arrays;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head;     // Первый элемент в порядке добавления
    private Node<E> tail;     // Последний элемент в порядке добавления
    private int size;

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;  // Инициализируем указатели порядка
        tail = null;
    }

    // Усложненная структура узла
    private static class Node<E> {
        E data;
        Node<E> next;         // Следующий в цепочке коллизий
        Node<E> prevInBucket; // Предыдущий в цепочке коллизий
        Node<E> nextInOrder;  // Следующий в порядке добавления
        Node<E> prevInOrder;  // Предыдущий в порядке добавления

        Node(E data) {
            this.data = data;
        }
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
    public void clear() {
        Arrays.fill(table, null);  // Очищаем хеш-таблицу
        head = null;               // Разрываем глобальную цепь
        tail = null;
        size = 0;
    }

    // Добавление теперь работает с двумя структурами
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверка на дубликат
        if (contains(element)) {
            return false;
        }

        // Рехеширование при необходимости
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(element);
        Node<E> newNode = new Node<>(element);

        // Добавление в хеш-таблицу
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            // Идем до конца цепочки и добавляем
            Node<E> current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
            newNode.prevInBucket = current;  // Двунаправленная связь в цепочке
        }

        //Добавление в глобальный список порядка
        if (head == null) {
            // Первый элемент в множестве
            head = newNode;
            tail = newNode;
        } else {
            // Добавляем в конец глобальной цепи
            tail.nextInOrder = newNode;
            newNode.prevInOrder = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    // Удаление должно удалять из обеих структур
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> prevInBucket = null;

        // Поиск в цепочке коллизий
        while (current != null) {
            if (element.equals(current.data)) {
                // Удаление из хеш-таблицы
                if (prevInBucket == null) {
                    table[index] = current.next;
                    if (current.next != null) {
                        current.next.prevInBucket = null;
                    }
                } else {
                    prevInBucket.next = current.next;
                    if (current.next != null) {
                        current.next.prevInBucket = prevInBucket;
                    }
                }

                // Удаление из глобального списка порядка
                removeFromOrderList(current);

                size--;
                return true;
            }
            prevInBucket = current;
            current = current.next;
        }

        return false;
    }

    // Вспомогательный метод для удаления из глобальной цепи порядка
    private void removeFromOrderList(Node<E> node) {
        // Обновляем ссылки у соседей
        if (node.prevInOrder != null) {
            node.prevInOrder.nextInOrder = node.nextInOrder;
        } else {
            // Удаляемый элемент был головой
            head = node.nextInOrder;
        }

        if (node.nextInOrder != null) {
            node.nextInOrder.prevInOrder = node.prevInOrder;
        } else {
            // Удаляемый элемент был хвостом
            tail = node.prevInOrder;
        }

        // Очищаем ссылки у удаляемого узла
        node.prevInOrder = null;
        node.nextInOrder = null;
    }

    // Поиск работает только через хеш-таблицу
    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (element.equals(current.data)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Методы для работы с коллекциями
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {  // сама проверяет дубликаты
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // оставляет только элементы из переданной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Проходим по всем элементам в порядке добавления
        Node<E> current = head;
        java.util.ArrayList<Object> toRemove = new java.util.ArrayList<>();

        while (current != null) {
            if (!c.contains(current.data)) {
                toRemove.add(current.data);
                modified = true;
            }
            current = current.nextInOrder;
        }

        // Удаляем все элементы, которых нет в коллекции
        for (Object element : toRemove) {
            remove(element);
        }

        return modified;
    }

    // Вычисление индекса в хеш-таблице
    private int getIndex(Object element) {
        int hashCode = element.hashCode();
        return Math.abs(hashCode) % table.length;
    }

    // Рехеширование с сохранением порядка элементов
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        // Сохраняем старый порядок и сбрасываем глобальную цепь
        Node<E> oldHead = head;
        head = null;
        tail = null;
        size = 0;

        // Перестраиваем таблицу, сохраняя исходный порядок
        Node<E> current = oldHead;
        while (current != null) {
            // Сохраняем ссылку на следующий перед добавлением
            Node<E> nextInOrder = current.nextInOrder;

            // Добавляем элемент в новую таблицу (вызываем логику add)
            int index = getIndex(current.data);
            Node<E> newNode = new Node<>(current.data);

            // Добавление в хеш-таблицу
            if (table[index] == null) {
                table[index] = newNode;
            } else {
                Node<E> tableCurrent = table[index];
                while (tableCurrent.next != null) {
                    tableCurrent = tableCurrent.next;
                }
                tableCurrent.next = newNode;
                newNode.prevInBucket = tableCurrent;
            }

            // Добавление в глобальную цепь порядка
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.nextInOrder = newNode;
                newNode.prevInOrder = tail;
                tail = newNode;
            }

            size++;
            current = nextInOrder;
        }
    }

    //выводит элементы в порядке добавления
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;  // Начинаем с первого добавленного элемента
        boolean firstElement = true;

        // Проходим по глобальной цепи порядка
        while (current != null) {
            if (!firstElement) {
                sb.append(", ");
            }
            sb.append(current.data);
            firstElement = false;
            current = current.nextInOrder;  // Следующий в порядке добавления
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы интерфейса Set, которые не требуются по заданию
    @Override
    public java.util.Iterator<E> iterator() {
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