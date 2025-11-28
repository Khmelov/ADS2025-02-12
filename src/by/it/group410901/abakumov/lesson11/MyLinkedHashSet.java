package by.it.group410901.abakumov.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16; // Начальная емкость хэш-таблицы
    private static final float LOAD_FACTOR = 0.75f; // Фактор загрузки для расширения таблицы
    private Node<E>[] table; // Хэш-таблица: массив бакетов (списков узлов)
    private int size; // Текущее количество элементов в сете
    private int threshold; // Порог для расширения (capacity * load_factor)

    // Список для сохранения порядка вставки (head - первый добавленный)
    private Node<E> headInsertion; // Голова двусвязного списка порядка вставки
    private Node<E> tailInsertion; // Хвост двусвязного списка порядка вставки
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY]; // Инициализация пустой таблицы
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR); // Вычисление начального порога
    }

    private static final class Node<T> {
        final T value; // Значение элемента (неизменяемое)
        Node<T> nextInBucket; // односвязный список для бакета (следующий в коллизии)
        // ссылки для списка порядка вставки (двусвязный для удобного удаления)
        Node<T> prevInsertion; // Предыдущий в порядке вставки
        Node<T> nextInsertion; // Следующий в порядке вставки
        Node(T value) {
            this.value = value; // Конструктор: только значение, ссылки null по умолчанию
        }
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode(); // Хэш для null = 0
        // spread bits similar to HashMap's spread (simple) — простое перемешивание битов для лучшего распределения
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % length; // Индекс бакета: положительный хэш modulo длины таблицы
    }

    private void resizeIfNeeded() {
        if (size < threshold) return; // Нет нужды в расширении, если размер меньше порога
        int newCap = table.length * 2; // Удвоение емкости
        Node<E>[] newTable = (Node<E>[]) new Node[newCap]; // Новая пустая таблица
        for (Node<E> node : table) { // Перебор всех бакетов старой таблицы
            while (node != null) { // Перебор цепочки в бакете
                Node<E> next = node.nextInBucket; // Сохраняем следующий для продолжения
                int idx = indexFor(node.value, newCap); // Новый индекс в расширенной таблице
                // insert at head of new bucket (singly) — вставка в голову бакета (односвязный)
                node.nextInBucket = newTable[idx];
                newTable[idx] = node;
                node = next; // Переход к следующему узлу
            }
        }
        table = newTable; // Замена таблицы
        threshold = (int) (newCap * LOAD_FACTOR); // Обновление порога
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded(); // Проверяем и расширяем таблицу при необходимости
        int idx = indexFor(e, table.length); // Вычисляем индекс бакета
        Node<E> node = table[idx]; // Начинаем поиск в бакете
        while (node != null) {
            if (Objects.equals(node.value, e)) return false; // уже есть — дубликат, ничего не добавляем
            node = node.nextInBucket; // Продолжаем поиск в цепочке
        }
        Node<E> newNode = new Node<>(e); // Создаем новый узел
        // вставляем в начало бакета (односвязный список) — для простоты и скорости
        newNode.nextInBucket = table[idx];
        table[idx] = newNode;
        // добавляем в конец списка вставки — сохраняем порядок добавления
        if (tailInsertion == null) {
            headInsertion = tailInsertion = newNode; // Первый элемент: голова и хвост совпадают
        } else {
            tailInsertion.nextInsertion = newNode; // Присоединяем к хвосту
            newNode.prevInsertion = tailInsertion;
            tailInsertion = newNode; // Новый хвост
        }
        size++; // Увеличиваем размер
        return true; // Успешно добавлено
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o, table.length); // Индекс бакета
        Node<E> node = table[idx]; // Поиск в бакете
        while (node != null) {
            if (Objects.equals(node.value, o)) return true; // Найдено по значению
            node = node.nextInBucket;
        }
        return false; // Не найдено
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length); // Индекс бакета
        Node<E> node = table[idx]; // Текущий узел в бакете
        Node<E> prev = null; // Предыдущий для удаления в односвязном списке
        while (node != null) {
            if (Objects.equals(node.value, o)) { // Найден элемент
                // удалить из бакета (односвязный: обновляем ссылки)
                if (prev == null) table[idx] = node.nextInBucket;
                else prev.nextInBucket = node.nextInBucket;
                // удалить из списка вставки (двусвязный: обновляем prev/next)
                if (node.prevInsertion != null) node.prevInsertion.nextInsertion = node.nextInsertion;
                else headInsertion = node.nextInsertion; // Если голова
                if (node.nextInsertion != null) node.nextInsertion.prevInsertion = node.prevInsertion;
                else tailInsertion = node.prevInsertion; // Если хвост
                size--; // Уменьшаем размер
                return true; // Успешно удалено
            }
            prev = node; // Сдвиг для следующей итерации
            node = node.nextInBucket;
        }
        return false; // Не найдено
    }

    @Override
    public int size() {
        return size; // Возвращаем текущее количество элементов
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null; // Очищаем все бакеты
        headInsertion = tailInsertion = null; // Сбрасываем список вставки
        size = 0; // Размер в ноль
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Пусто, если размер zero
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Строитель строки
        sb.append('['); // Начало списка
        Node<E> cur = headInsertion; // Начинаем с первого вставленного
        boolean first = true; // Флаг для запятых
        while (cur != null) {
            if (!first) sb.append(", "); // Запятая между элементами
            first = false;
            sb.append(cur.value); // Добавляем значение
            cur = cur.nextInsertion; // Следующий в порядке вставки
        }
        sb.append(']'); // Конец списка
        return sb.toString(); // Возвращаем строковое представление в порядке вставки
    }

    // ----- Методы для работы с коллекциями -----
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false; // Проверяем каждый элемент коллекции
        return true; // Все содержатся
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false; // Флаг изменения
        for (E e : c) changed |= add(e); // Добавляем по одному, OR для любого изменения
        return changed; // Было ли добавлено хоть что-то
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false; // Флаг изменения
        for (Object o : c) changed |= remove(o); // Удаляем по одному
        return changed; // Было ли удалено
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false; // Флаг изменения
        // пройдёмся по списку вставки, удаляя те, которых нет в c — эффективно по порядку
        Node<E> cur = headInsertion; // Начинаем с головы
        while (cur != null) {
            Node<E> next = cur.nextInsertion; // Сохраняем следующий (remove может сдвинуть)
            if (!c.contains(cur.value)) { // Если не в c
                // используем remove(Object) — он корректно обновит таблицу и список вставки
                remove(cur.value); // Удаляем через основной метод
                changed = true; // Отмечаем изменение
            }
            cur = next; // Переход дальше
        }
        return changed; // Были ли удаления
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
}