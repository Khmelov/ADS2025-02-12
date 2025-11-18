package by.it.group410901.kvitchenko.lesson11;

import java.util.Set;
import java.util.Iterator;

// Класс MyHashSet реализует интерфейс Set<E> (множество) с помощью хеш-таблицы (массива связных списков).
public class MyHashSet<E> implements Set<E> {
    // Константа, определяющая начальную емкость хеш-таблицы.
    private static final int CAPACITY = 10;

    // Основное хранилище: массив, где каждый элемент является началом связного списка.
    private Node<E>[] nodeLists;
    // Текущее количество элементов, хранящихся в множестве.
    private int currentSize;

    @SuppressWarnings("unchecked")
    // Конструктор по умолчанию.
    public MyHashSet() {
        // Инициализация массива корзин.
        nodeLists = (Node<E>[]) new Node[CAPACITY];
        currentSize = 0;
    }

    // Внутренний статический класс, представляющий узел в связном списке (элемент данных).
    private static class Node<E> {
        // Хранимое значение (элемент множества).
        E info;
        // Ссылка на следующий узел в списке (реализация цепочек).
        Node<E> next;

        Node(E value, Node<E> next) {
            this.info = value;
            this.next = next;
        }
    }

    // Вспомогательный метод для вычисления индекса (корзины) в массиве по хеш-коду объекта.
    private int getIndex(Object obj) {
        // Вычисляем хеш-код (для null используем 0).
        int hash = obj == null ? 0 : obj.hashCode();
        // Применяем побитовое AND (0x7FFFFFFF) для получения абсолютного значения и
        // операцию модуло (%) для отображения хеша на допустимый индекс массива.
        return (hash & 0x7FFFFFFF) % nodeLists.length;
    }

    @Override
    // Добавляет элемент в множество. Возвращает true, если элемент был добавлен.
    public boolean add(Object obj) {
        E e = (E) obj;
        // Находим индекс корзины.
        int index = getIndex(e);
        // Получаем начало связного списка (корзины).
        Node<E> node = nodeLists[index];

        // Проходим по списку, чтобы проверить, существует ли уже такой элемент.
        while (node != null) {
            // Проверка равенства с учетом null-элементов и метода equals().
            if (node.info == null && e == null ||
                    node.info != null && node.info.equals(e)) {
                // Элемент уже существует, не добавляем.
                return false;
            }
            node = node.next;
        }

        // Элемент отсутствует. Создаем новый узел и вставляем его в начало списка (голова).
        nodeLists[index] = new Node<>(e, nodeLists[index]);
        currentSize++;
        return true;
    }

    @Override
    // Проверяет, содержит ли множество указанный элемент.
    public boolean contains(Object obj) {
        // Находим индекс корзины.
        int ind = getIndex(obj);
        // Получаем начало связного списка.
        Node<E> node = nodeLists[ind];

        // Проходим по списку, ища элемент.
        while (node != null) {
            // Проверка равенства.
            if (node.info == null && obj == null ||
                    node.info != null && node.info.equals(obj)) {
                // Элемент найден.
                return true;
            }
            node = node.next;
        }

        // Элемент не найден.
        return false;
    }

    @Override
    // Удаляет указанный элемент из множества. Возвращает true, если элемент был удален.
    public boolean remove(Object obj) {
        // Находим индекс корзины.
        int index = getIndex(obj);
        // Узел, по которому идет проход.
        Node<E> node = nodeLists[index];
        // Ссылка на предыдущий узел, необходима для удаления из связного списка.
        Node<E> nodePrev = null;

        // Проходим по списку, ища элемент для удаления.
        while (node != null) {
            // Проверка равенства.
            if (node.info == null && obj == null ||
                    node.info != null && node.info.equals(obj)) {
                // Элемент найден, удаляем его из списка:
                if (nodePrev == null) {
                    // Если удаляемый элемент - первый (голова списка).
                    nodeLists[index] = node.next;
                } else {
                    // Если элемент в середине или конце списка, переназначаем ссылку next предыдущего.
                    nodePrev.next = node.next;
                }
                currentSize--;
                return true;
            }
            // Переходим к следующему узлу, запоминая текущий как предыдущий.
            nodePrev = node;
            node = node.next;
        }

        // Элемент не найден.
        return false;
    }

    @Override
    // Удаляет все элементы из множества.
    public void clear() {
        // Устанавливаем все начала списков в null.
        for (int i = 0; i < nodeLists.length; i++) {
            nodeLists[i] = null;
        }
        // Сбрасываем счетчик размера.
        currentSize = 0;
    }

    @Override
    // Возвращает текущее количество элементов в множестве.
    public int size() {
        return currentSize;
    }

    @Override
    // Проверяет, пусто ли множество.
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Просто для красивого вывода — порядок не гарантирован
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("[");
        boolean isCorrect = true;

        // Проход по всем корзинам.
        for (Node<E> list : nodeLists) {
            Node<E> node = list;
            // Проход по каждому связному списку в корзине.
            while (node != null) {
                if (!isCorrect) {
                    resultStr.append(", ");
                }
                resultStr.append(node.info);
                isCorrect = false;
                node = node.next;
            }
        }

        resultStr.append("]");
        return resultStr.toString();
    }

    @Override public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    @Override public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    @Override public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}