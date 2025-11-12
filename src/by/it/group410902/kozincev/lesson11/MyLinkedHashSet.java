package by.it.group410902.kozincev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

class LinkedNode<E> {
    final int hash;
    final E value;
    LinkedNode<E> next;
    LinkedNode<E> before;
    LinkedNode<E> after;

    LinkedNode(int hash, E value, LinkedNode<E> next) {
        this.hash = hash;
        this.value = value;
        this.next = next;
    }
}

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private LinkedNode<E>[] table;
    private int size;

    private LinkedNode<E> head; // Глобальный указатель на первый добавленный элемент
    private LinkedNode<E> tail; // Глобальный указатель на последний добавленный элемент

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        // Инициализация массива "корзин"
        this.table = new LinkedNode[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private int getIndex(int hash) {
        return hash & (table.length - 1);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    // Вспомогательный метод для связывания нового узла в глобальный двусвязный список
    private void linkNodeLast(LinkedNode<E> newNode) {
        if (head == null) {
            head = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
        }
        tail = newNode;
    }

    // Вспомогательный метод для удаления узла из глобального двусвязного списка
    private void unlinkNode(LinkedNode<E> node) {
        LinkedNode<E> before = node.before;
        LinkedNode<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int index = getIndex(h);
        LinkedNode<E> current = table[index];

        // 1. Проверка на дубликат в текущем списке корзины
        while (current != null) {
            if (current.hash == h && (current.value == e || (e != null && e.equals(current.value)))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // 2. Добавление нового элемента в начало списка корзины
        LinkedNode<E> newNode = new LinkedNode<>(h, e, table[index]);
        table[index] = newNode;

        // 3. Связывание нового элемента в конец глобального двусвязного списка
        linkNodeLast(newNode);

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = getIndex(h);
        LinkedNode<E> current = table[index];
        LinkedNode<E> prev = null;

        while (current != null) {
            if (current.hash == h && (current.value == o || (o != null && o.equals(current.value)))) {
                // Найден! Удаляем из списка корзины
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из глобального списка
                unlinkNode(current);

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int index = getIndex(h);
        LinkedNode<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.value == o || (o != null && o.equals(current.value)))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очистка массива корзин
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Очистка ссылок глобального порядка
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        LinkedNode<E> current = head; // Начинаем с первого добавленного элемента

        while (current != null) {
            sb.append(current.value);
            if (current.after != null) {
                sb.append(", "); // Разделитель: запятая с пробелом
            }
            current = current.after;
        }

        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Создадим временную коллекцию, чтобы избежать ConcurrentModificationException,
        // хотя здесь мы не используем стандартную коллекцию, но подход хорош.
        // Итерируемся по удаляемой коллекции и вызываем remove(o) для каждого элемента
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        if (isEmpty()) {
            return false;
        }

        // Необходимо пройти по всем элементам MyLinkedHashSet и удалить те,
        // которых НЕТ в коллекции c.
        LinkedNode<E> current = head;
        while (current != null) {
            LinkedNode<E> next = current.after; // Сохраняем ссылку на следующий элемент перед потенциальным удалением
            if (!c.contains(current.value)) {
                remove(current.value); // remove обновляет size и связи head/tail
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // ================== Необязательные методы ==================
    // Оставляем заглушки для остальных методов Set/Collection.

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException("Iterator not implemented"); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException("toArray not implemented"); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException("toArray not implemented"); }
}