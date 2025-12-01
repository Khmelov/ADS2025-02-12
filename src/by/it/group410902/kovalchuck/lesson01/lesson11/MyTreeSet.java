package by.it.group410902.kovalchuck.lesson01.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    // Хранилище элементов - отсортированный массив
    private Object[] elements;

    private int size;
    // Компаратор для сравнения элементов
    private Comparator<E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this(null);
    }

    // Конструктор с компаратором - позволяет задать свой порядок сортировки
    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<E> comparator) {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;

        if (comparator != null) {
            this.comparator = comparator;
        } else {
            // Если компаратор не задан, используем естественный порядок
            this.comparator = (e1, e2) -> {
                if (e1 == null || e2 == null) {
                    throw new NullPointerException("Cannot compare null elements");
                }
                return ((Comparable<E>) e1).compareTo(e2);
            };
        }
    }

    // Возвращаем количество элементов в множестве
    @Override
    public int size() {
        return size;
    }

    // Проверяем, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает множество - удаляет все элементы
    @Override
    public void clear() {
        // Обнуляем ссылки
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    // Добавляет элемент в множество с сохранением порядка сортировки
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Ищем позицию элемента с помощью бинарного поиска
        int index = binarySearch(element);
        if (index >= 0) {
            return false; // Элемент уже существует, не добавляем
        }

        int insertIndex = -index - 1;

        // Проверяем, нужно ли увеличивать массив
        if (size == elements.length) {
            ensureCapacity();
        }

        // Сдвигаем элементы вправо, чтобы освободить место для нового элемента
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = element;
        size++;

        return true;
    }

    // Удаляет элемент из множества
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        // Ищем элемент бинарным поиском
        int index = binarySearch((E) element);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево, чтобы удалить найденный элемент
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // Очищаем последнюю ячейку

        return true;
    }

    // Проверяем, содержится ли элемент в множестве
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }
        return binarySearch((E) element) >= 0;
    }

    // Проверяет, содержатся ли все элементы коллекции в множестве
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            // проверяет дубликаты и поддерживает сортировку
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // Удаляем все элементы коллекции из множества
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

    // Оставляет в множестве только элементы, содержащиеся в заданной коллекции
    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        // Если коллекция пустая, очищаем множество
        if (c.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        // Выбираем оптимальный алгоритм в зависимости от типа коллекции
        if (isSortedCollection(c)) {
            return retainAllSorted(c); // Быстрый алгоритм для отсортированных коллекций
        }

        return retainAllStandard(c); // Стандартный алгоритм
    }

    // алгоритм  для отсортированных коллекций
    @SuppressWarnings("unchecked")
    private boolean retainAllSorted(Collection<?> c) {
        // Преобразуем коллекцию в массив и сортируем его
        Object[] otherArray = c.toArray();
        Arrays.sort(otherArray, (Comparator<Object>) this.comparator);

        // Создаем временный массив для хранения сохраняемых элементов
        Object[] retained = new Object[Math.min(size, otherArray.length)];
        int retainedSize = 0;

        int i = 0; // Указатель для текущего множества
        int j = 0; // Указатель для коллекции
        boolean modified = false;

        //обходим обе коллекции одновременно
        while (i < size && j < otherArray.length) {
            E current = (E) elements[i];
            Object other = otherArray[j];

            int cmp = comparator.compare(current, (E) other);

            if (cmp < 0) {
                // Текущий элемент меньше элемента из коллекции - удаляем его
                modified = true;
                i++;
            } else if (cmp > 0) {
                // Элемент коллекции меньше - переходим к следующему элементу коллекции
                j++;
            } else {
                // Элементы равны - сохраняем текущий элемент
                retained[retainedSize++] = current;
                i++;
                j++;
            }
        }

        // Если остались элементы в текущем множестве - они будут удалены
        if (i < size) {
            modified = true;
        }

        if (modified) {
            // Заменяем исходный массив на отфильтрованный
            elements = retained;
            size = retainedSize;
        }

        return modified;
    }

    // Стандартная реализация для неотсортированных коллекций
    private boolean retainAllStandard(Collection<?> c) {
        boolean modified = false;
        Object[] retained = new Object[size];
        int retainedSize = 0;

        // Проходим по всем элементам множества
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                // Элемент содержится в коллекции - сохраняем его
                retained[retainedSize++] = elements[i];
            } else {
                modified = true; // Элемент будет удален
            }
        }

        if (modified) {
            elements = retained;
            size = retainedSize;
        }

        return modified;
    }

    // Проверяет, является ли коллекция отсортированной
    @SuppressWarnings("unchecked")
    private boolean isSortedCollection(Collection<?> c) {
        // Проверяем известные отсортированные типы
        if (c instanceof MyTreeSet || c instanceof java.util.SortedSet) {
            return true;
        }

        // Для небольших коллекций считаем их отсортированными
        if (c.size() < 2) return true;

        // Проверяем порядок элементов в коллекции
        Object[] array = c.toArray();
        try {
            for (int i = 1; i < array.length; i++) {
                // Если найден элемент, нарушающий порядок - коллекция не отсортирована
                if (comparator.compare((E) array[i-1], (E) array[i]) > 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            // В случае ошибки считаем коллекцию неотсортированной
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // вычисление середины
            E midVal = (E) elements[mid];
            int cmp = comparator.compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1; // Искомый элемент в правой половине
            } else if (cmp > 0) {
                high = mid - 1; // Искомый элемент в левой половине
            } else {
                return mid; // Элемент найден
            }
        }
        // Элемент не найден, возвращаем позицию для вставки
        return -(low + 1);
    }

    // Увеличивает емкость массива при необходимости
    private void ensureCapacity() {
        int newCapacity = elements.length * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    // Внутренний класс итератора для обхода элементов
    private class TreeSetIterator implements Iterator<E> {
        private int currentIndex = 0;     // Текущая позиция в массиве
        private int lastReturned = -1;    // Индекс последнего возвращенного элемента

        // Проверяем, есть ли следующий элемент
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        // Возвращает следующий элемент
        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in set");
            }
            lastReturned = currentIndex;
            return (E) elements[currentIndex++];
        }

        // Удаляет текущий элемент
        @Override
        public void remove() {
            if (lastReturned == -1) {
                throw new IllegalStateException("next() must be called before remove()");
            }

            MyTreeSet.this.remove(elements[lastReturned]);
            currentIndex = lastReturned; // Корректируем индекс после удаления
            lastReturned = -1; // Сбрасываем флаг
        }
    }

    // Возвращает итератор для обхода элементов
    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

    // Возвращает строковое представление множества
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // Возвращает первый элемент множества
    @SuppressWarnings("unchecked")
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return (E) elements[0];
    }

    // Возвращает последний элемент множества
    @SuppressWarnings("unchecked")
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return (E) elements[size - 1];
    }

    // Преобразует множество в массив
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    // Преобразует множество в массив заданного типа
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если массив слишком маленький, создаем новый
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        // Копируем элементы в существующий массив
        System.arraycopy(elements, 0, a, 0, size);
        // Устанавливаем null после последнего элемента
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}