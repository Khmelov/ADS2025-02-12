package by.it.group410902.sinyutin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация Set<E> с сохранением порядка вставки (LinkedHashSet),
 * основанная на хеш-таблице и двух типах узлов, без использования
 * стандартных коллекций.
 */
public class MyLinkedHashSet<E> implements Set<E> {

    // --- 1. Внутренние классы узлов ---

    // Узел глобального двусвязного списка, который хранит порядок вставки.
    private static class InsertionNode<E> {
        E value;
        InsertionNode<E> prev;
        InsertionNode<E> next;

        public InsertionNode(E value, InsertionNode<E> prev, InsertionNode<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    // Узел, используемый в бакетах хеш-таблицы для разрешения коллизий.
    // Содержит ссылку на соответствующий узел InsertionNode.
    private static class BucketNode<E> {
        E value;
        BucketNode<E> next;
        InsertionNode<E> insertionLink; // Ссылка на узел в двусвязном списке

        public BucketNode(E value, BucketNode<E> next, InsertionNode<E> insertionLink) {
            this.value = value;
            this.next = next;
            this.insertionLink = insertionLink;
        }
    }

    // --- 2. Поля MyLinkedHashSet ---

    private static final int DEFAULT_CAPACITY = 16;

    // Массив бакетов для хеш-таблицы
    private BucketNode<E>[] buckets;

    private int size;

    // Голова и хвост двусвязного списка для сохранения порядка вставки.
    // Используем "фиктивные" узлы (sentinel nodes) для упрощения операций add/remove.
    private InsertionNode<E> head; // Указывает на первый элемент
    private InsertionNode<E> tail; // Указывает на последний элемент

    // --- 3. Конструктор ---

    public MyLinkedHashSet() {
        this.buckets = (BucketNode<E>[]) new BucketNode[DEFAULT_CAPACITY];
        this.size = 0;

        // Инициализация фиктивных узлов для двусвязного списка.
        // head.next = tail, tail.prev = head
        head = new InsertionNode<>(null, null, null);
        tail = new InsertionNode<>(null, head, null);
        head.next = tail;
    }

    // --- 4. Приватный метод для хеширования ---

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % buckets.length;
    }

    /**
     * Вспомогательный метод для поиска BucketNode по объекту.
     */
    private BucketNode<E> findBucketNode(Object o, int index) {
        BucketNode<E> current = buckets[index];
        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Вспомогательный метод: удаляет InsertionNode из двусвязного списка.
     */
    private void unlinkInsertionNode(InsertionNode<E> node) {
        if (node != null && node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            // Обнуляем связи узла для GC
            node.prev = null;
            node.next = null;
        }
    }

    // --- 5. Обязательные базовые методы ---

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем массив бакетов
        this.buckets = (BucketNode<E>[]) new BucketNode[buckets.length];
        this.size = 0;

        // Сбрасываем двусвязный список
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public boolean contains(Object o) {
        return findBucketNode(o, getIndex(o)) != null;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);

        // 1. Проверка на дубликаты через хеш-таблицу
        if (findBucketNode(e, index) != null) {
            return false;
        }

        // 2. Создание узла для двусвязного списка и привязка его к хвосту (сохранение порядка вставки)
        InsertionNode<E> newInsertionNode = new InsertionNode<>(e, tail.prev, tail);
        tail.prev.next = newInsertionNode;
        tail.prev = newInsertionNode;

        // 3. Создание узла для бакета и привязка его к InsertionNode
        BucketNode<E> newBucketNode = new BucketNode<>(e, buckets[index], newInsertionNode);
        buckets[index] = newBucketNode;

        this.size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        BucketNode<E> currentBucket = buckets[index];
        BucketNode<E> previousBucket = null;

        while (currentBucket != null) {
            boolean found = (o == null && currentBucket.value == null) || (o != null && o.equals(currentBucket.value));

            if (found) {
                // 1. Удаление из хеш-таблицы (бакета)
                if (previousBucket == null) {
                    buckets[index] = currentBucket.next; // Удаление головы
                } else {
                    previousBucket.next = currentBucket.next; // Удаление из середины/хвоста
                }

                // 2. Удаление из двусвязного списка
                unlinkInsertionNode(currentBucket.insertionLink);

                this.size--;
                return true;
            }

            previousBucket = currentBucket;
            currentBucket = currentBucket.next;
        }
        return false; // Элемент не найден
    }

    // --- 6. Методы Set<E>, работающие с коллекциями ---

    // *Обратите внимание: поскольку Collection<?> c должна быть обработана без
    // использования стандартных коллекций, предполагается, что Collection<?>
    // в тестах будет реализована в виде Object[]. Если тесты используют
    // стандартный интерфейс Collection, то для его итерации требуется
    // реализация Iterator, что усложняет задачу. Здесь принимается, что
    // Collection<?> c может быть итерирована через Iterator, который должен
    // быть предоставлен тестовой системой.*

    @Override
    public boolean containsAll(Collection<?> c) {
        // Требуется Iterator, который должен быть предоставлен Collection c
        if (c == null) return false;

        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        boolean modified = false;

        Iterator<? extends E> it = c.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;

        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (remove(it.next())) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;

        // Создаем временный набор для элементов, которые нужно сохранить
        // *ВНИМАНИЕ: Для строгого соблюдения правил "БЕЗ использования других классов
        // СТАНДАРТНОЙ БИБЛИОТЕКИ", мы не можем использовать стандартный HashSet.
        // В реальном тесте здесь пришлось бы создавать копию MyLinkedHashSet для
        // временного хранения или использовать Object[].*

        // В рамках данного задания (Уровень B), мы используем коллекцию c
        // как справочник. Итерируем по текущему MyLinkedHashSet и удаляем,
        // если элемент НЕ содержится в Collection c.

        InsertionNode<E> current = head.next;
        while (current != tail) {
            E element = current.value;

            // Заранее сохраняем next, так как current может быть удален
            InsertionNode<E> next = current.next;

            if (!c.contains(element)) {
                // Если элемент не должен быть сохранен, удаляем его.
                remove(element);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // --- 7. Метод toString() (сохранение порядка вставки) ---

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        InsertionNode<E> current = head.next; // Начинаем с первого реального элемента
        boolean first = true;

        // Обход по глобальному двусвязному списку (InsertionNode),
        // который гарантирует порядок вставки
        while (current != tail) {
            if (!first) {
                sb.append(", "); // Разделитель: запятая с пробелом
            }
            sb.append(current.value);
            first = false;
            current = current.next;
        }

        sb.append(']');
        return sb.toString();
    }

    // --- 8. Неподдерживаемые/Оставшиеся методы Set<E> ---

    @Override
    public Iterator<E> iterator() {
        // Требует создания собственного Iterator, что выходит за рамки обязательного списка.
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }
}