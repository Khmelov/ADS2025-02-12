package by.it.group410902.andala.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    // Внутренний класс для узла
    private static class Node<E> {
        E value;                    // Значение элемента
        Node<E> nextInBucket;       // Ссылка на следующий узел в той же корзине (для разрешения коллизий)
        Node<E> prevInOrder;        // Ссылка на предыдущий узел в порядке добавления
        Node<E> nextInOrder;        // Ссылка на следующий узел в порядке добавления

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;        // Массив корзин (хэш-таблица)
    private int size;               // Количество элементов
    private static final int DEFAULT_CAPACITY = 16;  // Начальный размер таблицы

    // Указатели для поддержания порядка добавления
    private Node<E> headOrder;      // Первый добавленный элемент
    private Node<E> tailOrder;      // Последний добавленный элемент

    // Конструктор
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];  // Создаем массив корзин
        size = 0;  // Начальный размер - 0
    }

    // Метод для вычисления индекса корзины для объекта
    private int index(Object o) {
        // Если объект null - помещаем в корзину 0, иначе используем хэш-код
        return (o == null ? 0 : Math.abs(o.hashCode())) % table.length;
    }

    // Добавление элемента в множество
    @Override
    public boolean add(Object o) {
        int i = index(o);  // Вычисляем индекс корзины
        Node<E> current = table[i];  // Получаем голову связного списка для этой корзины

        // Проверяем, нет ли уже такого элемента в множестве
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {
                return false;  // Элемент уже существует
            }
            current = current.nextInBucket;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<>((E) o);

        // Добавляем в хэш-таблицу (в начало связного списка корзины)
        newNode.nextInBucket = table[i];
        table[i] = newNode;

        // Добавляем в двусвязный список для сохранения порядка
        if (tailOrder == null) {
            // Если множество пустое - новый элемент становится и головой и хвостом
            headOrder = tailOrder = newNode;
        } else {
            // Добавляем в конец списка порядка
            tailOrder.nextInOrder = newNode;
            newNode.prevInOrder = tailOrder;
            tailOrder = newNode;
        }

        size++;  // Увеличиваем счетчик
        return true;  // Элемент успешно добавлен
    }

    // Удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        int i = index(o);  // Вычисляем индекс корзины
        Node<E> current = table[i];  // Голова связного списка корзины
        Node<E> prev = null;  // Предыдущий узел в корзине

        // Ищем элемент для удаления в корзине
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {

                // Удаляем из хэш-таблицы (корзины)
                if (prev == null) {
                    table[i] = current.nextInBucket;  // Удаляем голову списка
                } else {
                    prev.nextInBucket = current.nextInBucket;  // Удаляем из середины/конца
                }

                // Удаляем из двусвязного списка порядка
                if (current.prevInOrder != null) {
                    // Если есть предыдущий в порядке - связываем его со следующим
                    current.prevInOrder.nextInOrder = current.nextInOrder;
                } else {
                    // Если это голова списка порядка - обновляем голову
                    headOrder = current.nextInOrder;
                }

                if (current.nextInOrder != null) {
                    // Если есть следующий в порядке - связываем его с предыдущим
                    current.nextInOrder.prevInOrder = current.prevInOrder;
                } else {
                    // Если это хвост списка порядка - обновляем хвост
                    tailOrder = current.prevInOrder;
                }

                size--;  // Уменьшаем счетчик
                return true;  // Успешно удалили
            }
            prev = current;
            current = current.nextInBucket;
        }

        return false;  // Элемент не найден
    }

    // Проверка наличия элемента в множестве
    @Override
    public boolean contains(Object o) {
        int i = index(o);  // Вычисляем индекс корзины
        Node<E> current = table[i];  // Голова связного списка корзины

        // Ищем элемент в корзине
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {
                return true;  // Элемент найден
            }
            current = current.nextInBucket;
        }
        return false;  // Элемент не найден
    }

    // Очистка множества
    @Override
    public void clear() {
        // Обнуляем все корзины
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Обнуляем указатели порядка
        headOrder = tailOrder = null;
        size = 0;  // Сбрасываем счетчик
    }

    // Получение количества элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Строковое представление множества (в порядке добавления)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = headOrder;  // Начинаем с первого добавленного элемента
        boolean first = true;

        // Проходим по всем элементам в порядке добавления
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.nextInOrder;
        }
        sb.append("]");
        return sb.toString();
    }

    // Проверка наличия всех элементов из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;  // Если хоть одного элемента нет
        }
        return true;  // Все элементы есть
    }

    // Добавление всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;  // Если добавили хотя бы один элемент
        }
        return changed;
    }

    // Удаление всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            if (remove(e)) changed = true;  // Если удалили хотя бы один элемент
        }
        return changed;
    }

    // Оставить только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = headOrder;

        // Проходим по всем элементам в порядке добавления
        while (current != null) {
            Node<E> next = current.nextInOrder;  // Сохраняем ссылку на следующий
            if (!c.contains(current.value)) {
                remove(current.value);  // Удаляем элемент, если его нет в коллекции
                changed = true;
            }
            current = next;  // Переходим к следующему
        }
        return changed;
    }

    // Не реализованные методы (выбрасывают исключение)
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}