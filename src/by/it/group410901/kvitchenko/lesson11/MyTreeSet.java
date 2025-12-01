package by.it.group410901.kvitchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Класс MyTreeSet реализует Set (множество) с обязательным сохранением порядка.
// Для хранения используется сортированный массив.
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    // Массив для хранения элементов. Требуется, чтобы элементы были Comparable для сортировки.
    private E[] arr;
    // Текущее количество элементов в множестве.
    private int currentSize;

    // Конструктор по умолчанию.
    public MyTreeSet() {
        // Инициализация массива начальной емкостью 10.
        arr = (E[]) new Comparable[10];
        currentSize = 0;
    }

    @Override
    // Возвращает количество элементов.
    public int size() {
        return currentSize;
    }

    @Override
    // Очищает множество, создавая новый пустой массив.
    public void clear() {
        arr = (E[]) new Comparable[10];
        currentSize = 0;
    }

    @Override
    // Проверяет, пусто ли множество.
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    // Проверяет наличие элемента, используя бинарный поиск (indexOf).
    public boolean contains(Object obj) {
        return indexOf((E) obj) >= 0;
    }

    // Методы, не реализованные или реализованные как заглушки:
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] arr) {
        return null;
    }

    // Вспомогательный метод: выполняет БИНАРНЫЙ ПОИСК элемента.
    private int indexOf(E info) {
        int left = 0, right = currentSize - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            // arr[mid].compareTo(info): 0 - равны, < 0 - info больше, > 0 - info меньше
            int res = arr[mid].compareTo(info);
            if (res == 0) {
                // Элемент найден, возвращаем его индекс.
                return mid;
            } else if (res < 0) {
                // Искомый элемент больше, ищем в правой половине.
                left = mid + 1;
            } else {
                // Искомый элемент меньше, ищем в левой половине.
                right = mid - 1;
            }
        }
        // Элемент не найден.
        return -1;
    }

    @Override
    // Добавляет элемент, сохраняя сортированный порядок.
    public boolean add(E info) {
        // Проверяем на дубликаты (O(log N)).
        if (contains(info)) {
            return false;
        }

        // Увеличиваем размер массива при необходимости.
        if (currentSize == arr.length) {
            resize();
        }

        // Находим индекс, куда нужно вставить элемент, чтобы сохранить порядок.
        int insertIndex = findInsertionIndex(info);

        // Сдвигаем все последующие элементы вправо на одну позицию (O(N)).
        for (int i = currentSize; i > insertIndex; i--) {
            arr[i] = arr[i - 1];
        }
        // Вставляем новый элемент.
        arr[insertIndex] = info;
        currentSize++;
        return true;
    }

    // Вспомогательный метод: находит индекс для вставки нового элемента, чтобы массив остался сортированным.
    private int findInsertionIndex(E info) {
        int left = 0, right = currentSize - 1;
        // Бинарный поиск: ищем, где *должен* быть элемент.
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid].compareTo(info) < 0) {
                // info больше, двигаем левую границу.
                left = mid + 1;
            } else {
                // info меньше или равно, двигаем правую границу.
                right = mid - 1;
            }
        }
        // left будет указывать на первый элемент, больший чем info,
        // или на currentSize, если info - самый большой.
        return left;
    }

    // Вспомогательный метод: увеличивает размер внутреннего массива в два раза.
    private void resize() {
        E[] copy = (E[]) new Comparable[arr.length * 2];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        arr = copy;
    }

    @Override
    // Удаляет элемент.
    public boolean remove(Object obj) {
        // Находим индекс элемента (O(log N)).
        int index = indexOf((E) obj);
        if (index >= 0) {
            // Сдвигаем все последующие элементы влево, "затирая" удаленный элемент (O(N)).
            for (int i = index; i < currentSize - 1; i++) {
                arr[i] = arr[i + 1];
            }
            // Обнуляем последний элемент и уменьшаем размер.
            arr[currentSize - 1] = null;
            currentSize--;
            return true;
        }
        return false;
    }

    @Override
    // Проверяет, содержит ли множество все элементы из коллекции.
    public boolean containsAll(Collection<?> collection) {
        for (Object obj : collection) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // Добавляет все элементы из коллекции.
    public boolean addAll(Collection<? extends E> collection) {
        boolean isCorrect = false;
        for (E info: collection) {
            if (add(info)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    // Удаляет все элементы, содержащиеся в коллекции.
    public boolean removeAll(Collection<?> collection) {
        boolean isCorrect = false;
        for (Object obj : collection) {
            if (remove(obj)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    // Удаляет из этого множества все элементы, не содержащиеся в указанной коллекции (пересечение).
    public boolean retainAll(Collection<?> collection) {
        boolean isCorrect = false;
        // Идем с конца, чтобы при удалении элементов не нарушать порядок итерации.
        for (int i = currentSize - 1; i >= 0; i--) {
            if (!collection.contains(arr[i])) {
                // Используем remove, который сдвигает элементы.
                remove(arr[i]);
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    // Возвращает строковое представление в сортированном порядке.
    public String toString() {
        StringBuilder resultStr = new StringBuilder("[");
        for (int i = 0; i < currentSize; i++) {
            resultStr.append(arr[i]);
            if (i < currentSize - 1) {
                resultStr.append(", ");
            }
        }
        resultStr.append("]");
        return resultStr.toString();
    }
}