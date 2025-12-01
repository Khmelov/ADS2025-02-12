package by.it.group410901.kvitchenko.lesson11;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

// Класс, реализующий Set (множество) с сохранением порядка вставки элементов.
// Использует комбинацию Хеш-таблицы (для быстрого поиска) и Двусвязного списка (для порядка).
public class MyLinkedHashSet<E> implements Set<E> {
    // Начальная емкость массива хеш-таблицы (количество корзин).
    private static final int CAPACITY = 10;
    // Массив корзин хеш-таблицы (для O(1) поиска/удаления).
    private Node<E>[] hashTable;
    // Указатель на первый элемент в двусвязном списке (для сохранения порядка).
    private Node<E> head;
    // Указатель на последний элемент в двусвязном списке.
    private Node<E> tail;
    // Текущее количество элементов в множестве.
    private int currentSize;

    // Конструктор по умолчанию.
    public MyLinkedHashSet() {
        // Инициализация массива корзин.
        hashTable = new Node[CAPACITY];
        currentSize = 0;
        // Начало и конец списка пусты.
        head = tail = null;
    }

    // Внутренний класс для узла, который будет использоваться в двух структурах:
    // 1. В связном списке корзины (через `next` в рамках той же корзины).
    // 2. В глобальном двусвязном списке (через `next` и `prev` для сохранения порядка).
    private static class Node<E> {
        // Хранимое значение.
        E info;
        // Ссылка на следующий узел В ГЛОБАЛЬНОМ СПИСКЕ ПОРЯДКА.
        Node<E> next;
        // Ссылка на предыдущий узел В ГЛОБАЛЬНОМ СПИСКЕ ПОРЯДКА.
        Node<E> prev;

        Node(E info) {
            this.info = info;
            this.next = this.prev = null;
        }
    }

    @Override
    // Возвращает количество элементов в множестве.
    public int size() {
        return currentSize;
    }

    @Override
    // Очищает множество, сбрасывая все структуры.
    public void clear() {
        // Создание нового пустого массива корзин.
        hashTable = new Node[CAPACITY];
        // Сброс указателей порядка.
        head = tail = null;
        currentSize = 0;
    }

    @Override
    // Проверяет, пусто ли множество.
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    // Проверяет, содержит ли множество указанный элемент.
    public boolean contains(Object o) {
        // Вычисляем индекс корзины.
        int index = getIndex(o);
        // Получаем начало списка в корзине.
        Node<E> currentHashTable = hashTable[index];

        // Проходим по связному списку в корзине.
        while (currentHashTable != null) {
            // Сравнение элементов с учетом null.
            if (Objects.equals(currentHashTable.info, o)) {
                return true;
            }
            // ВАЖНО: здесь currentHashTable.next - это ссылка на следующий узел ВНУТРИ КОРЗИНЫ (цепочки).
            currentHashTable = currentHashTable.next;
        }
        return false;
    }

    @Override
    // Предоставляет итератор, который обходит элементы В ПОРЯДКЕ ИХ ВСТАВКИ (через `head`).
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            // Начинаем обход с головы глобального двусвязного списка.
            Node<E> currentHashTable = head;

            @Override
            public boolean hasNext() {
                return currentHashTable != null;
            }

            @Override
            public E next() {
                E info = currentHashTable.info;
                // Переходим к следующему элементу В ГЛОБАЛЬНОМ СПИСКЕ.
                currentHashTable = currentHashTable.next;
                return info;
            }
        };
    }

    @Override
    // Преобразует множество в массив Object[], сохраняя порядок вставки.
    public Object[] toArray() {
        Object[] arr = new Object[currentSize];
        // Обход по глобальному списку порядка, начиная с головы.
        Node<E> currentHashTable = head;
        int index = 0;
        while (currentHashTable != null) {
            arr[index] = currentHashTable.info;
            index++;
            currentHashTable = currentHashTable.next;
        }
        return arr;
    }

    @Override
    // Преобразует множество в массив T[], сохраняя порядок вставки.
    public <T> T[] toArray(T[] t) {
        // Если переданный массив слишком мал, создается новый массив нужного размера.
        if (t.length < currentSize) {
            t = (T[]) Array.newInstance(t.getClass().getComponentType(), currentSize);
        }
        // Обход по глобальному списку порядка.
        Node<E>  currentHashTable = head;
        int index = 0;
        while ( currentHashTable != null) {
            t[index++] = (T)  currentHashTable.info;
            currentHashTable =  currentHashTable.next;
        }
        return t;
    }

    @Override
    // Добавляет элемент, сохраняя его в хеш-таблице и в конце глобального списка порядка.
    public boolean add(E info) {
        // 1. Проверка на дубликат (используется O(1) поиск в хеш-таблице).
        if (contains(info)) {
            return false;
        }

        int index = getIndex(info);
        Node<E>  newHashTable = new Node<>(info);


        // 2. Добавление в ХЕШ-ТАБЛИЦУ (в конец связного списка корзины):
        if (hashTable[index] == null) {
            // Если корзина пуста, новый узел становится головой списка корзины.
            hashTable[index] = newHashTable;
        } else {
            // Если корзина не пуста, идем в конец связного списка корзины.
            Node<E> current = hashTable[index];
            while (current.next != null) { // ВАЖНО: здесь current.next - ссылка ВНУТРИ КОРЗИНЫ.
                current = current.next;
            }
            // Добавляем новый узел в конец списка корзины.
            current.next = newHashTable;
        }

        // 3. Добавление в ГЛОБАЛЬНЫЙ ДВУСВЯЗНЫЙ СПИСОК ПОРЯДКА (в конец):
        if (tail == null) {
            // Если список пуст, новый узел становится и головой, и хвостом.
            head = tail = newHashTable;
        } else {
            // Присоединение к хвосту глобального списка.
            tail.next = newHashTable;
            newHashTable.prev = tail;
            tail = newHashTable;
        }

        currentSize++;
        return true;
    }

    @Override
    // Удаляет элемент, удаляя его из хеш-таблицы и из глобального списка порядка.
    public boolean remove(Object obj) {
        int index = getIndex(obj);
        Node<E> currentHashTable = hashTable[index];
        Node<E> prevHashTable = null;

        // 1. Удаление из ХЕШ-ТАБЛИЦЫ (из связного списка корзины):
        while (currentHashTable != null) {
            if (Objects.equals(currentHashTable.info, obj)) {
                // Изменение ссылок ВНУТРИ КОРЗИНЫ (цепного списка)
                if (prevHashTable == null) {
                    // Удаление головы корзины.
                    hashTable[index] = currentHashTable.next;
                } else {
                    // Удаление элемента из середины/конца корзины.
                    prevHashTable.next = currentHashTable.next;
                }

                // 2. Удаление из ГЛОБАЛЬНОГО ДВУСВЯЗНОГО СПИСКА ПОРЯДКА:

                // Изменение ссылки `next` предыдущего элемента глобального списка.
                if (currentHashTable.prev != null) {
                    currentHashTable.prev.next = currentHashTable.next;
                } else {
                    // Если удаляется голова глобального списка.
                    head = currentHashTable.next;
                }

                // Изменение ссылки `prev` следующего элемента глобального списка.
                if (currentHashTable.next != null) {
                    currentHashTable.next.prev = currentHashTable.prev;
                } else {
                    // Если удаляется хвост глобального списка.
                    tail = currentHashTable.prev;
                }

                currentSize--;
                return true;
            }
            // Переход к следующему элементу ВНУТРИ КОРЗИНЫ.
            prevHashTable = currentHashTable;
            currentHashTable = currentHashTable.next;
        }
        return false;
    }

    @Override
    // Проверяет, содержит ли множество все элементы из указанной коллекции.
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // Добавляет все элементы из коллекции. Возвращает true, если было добавлено хотя бы что-то.
    public boolean addAll(Collection<? extends E> collection) {
        boolean isCorrect = false;
        for (E value : collection) {
            if (add(value)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    // Удаляет из этого множества все элементы, не содержащиеся в указанной коллекции (операция пересечения).
    public boolean retainAll(Collection<?> collection) {
        boolean isCorrect = false;
        // Обход элементов по порядку вставки (через глобальный список).
        Node<E> currentHashTable = head;

        while (currentHashTable != null) {
            // Если элемент множества НЕ содержится в коллекции-параметре, его нужно удалить.
            if (!collection.contains(currentHashTable.info)) {
                Node<E> removeHashTable = currentHashTable;
                // Сначала переходим к следующему, чтобы не потерять его после удаления текущего.
                currentHashTable = currentHashTable.next;

                remove(removeHashTable.info);
                isCorrect = true;
            } else {
                currentHashTable = currentHashTable.next;
            }
        }
        return isCorrect;
    }

    @Override
    // Удаляет из этого множества все элементы, которые содержатся в указанной коллекции (операция разности).
    public boolean removeAll(Collection<?> c) {
        boolean isCorrect = false;
        for (Object o : c) {
            if (remove(o)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    // Вспомогательный метод для вычисления индекса корзины.
    private int getIndex(Object info) {
        // Используется абсолютное значение хеш-кода, взятое по модулю длины массива.
        return Math.abs(Objects.hashCode(info)) % hashTable.length;
    }

    @Override
    // Возвращает строковое представление множества, сохраняя порядок вставки.
    public String toString() {
        StringBuilder resultStr = new StringBuilder("[");
        // Обход по глобальному списку порядка.
        Node<E> currentHashTable = head;
        while (currentHashTable != null) {
            resultStr.append(currentHashTable.info).append(", ");
            currentHashTable = currentHashTable.next;
        }
        // Удаление лишних ", ".
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2);
        }
        resultStr.append("]");
        return resultStr.toString();
    }
}