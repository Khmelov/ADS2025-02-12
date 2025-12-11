package by.it.group451002.spitsyna.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private int size;  // Текущая емкость массива
    private Object[] arr;     // Массив для хранения элементов кучи

    // Конструктор - создает массив начального размера
    public MyPriorityQueue() {
        this.size = 10;
        this.arr = new Object[this.size];
    }

    // Просеивание вверх (восстановление свойства кучи при добавлении)
    public void shiftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;  // Индекс родителя
            // Если текущий элемент меньше родителя - меняем местами
            if (isLess(this.arr[i], this.arr[parent])) {
                Object temp = this.arr[i];
                this.arr[i] = this.arr[parent];
                this.arr[parent] = temp;
                i = parent;  // Переходим к родителю
            } else {
                break;
            }
        }
    }

    // Просеивание вниз (восстановление свойства кучи при удалении)
    private void shiftDown(int i) {
        int smallest = i;      // Индекс наименьшего элемента
        int left_ch = 2 * i + 1;   // Левый потомок
        int right_ch = 2 * i + 2;  // Правый потомок
        int size = this.size();

        // Сравниваем с левым потомком
        if (left_ch < size && isLess(this.arr[left_ch], this.arr[smallest])) {
            smallest = left_ch;
        }
        // Сравниваем с правым потомком
        if (right_ch < size && isLess(this.arr[right_ch], this.arr[smallest])) {
            smallest = right_ch;
        }
        // Если найден меньший потомок - меняем местами и продолжаем
        if (smallest != i) {
            Object temp = this.arr[smallest];
            this.arr[smallest] = this.arr[i];
            this.arr[i] = temp;
            this.shiftDown(smallest);  // Рекурсивно продолжаем
        }
    }

    // Преобразование в строку для вывода
    public String toString() {
        if (!this.isEmpty()) {
            String ret_val = "[";
            int size = this.size();
            for (int i = 0; i < size - 1; i++) {
                if (this.arr[i] != null)
                    ret_val += this.arr[i].toString() + ", ";
                else
                    ret_val += "null, ";
            }
            if (this.arr[size - 1] != null) {
                ret_val += this.arr[size - 1].toString() + "]";
            } else {
                ret_val += "null]";
            }
            return ret_val;
        }
        return "[]";
    }

    // Добавление элемента
    @Override
    public boolean offer(E e) {
        int size = this.size();
        if (size == this.size) this.resize();  // Увеличиваем массив если нужно
        this.arr[size] = e;
        this.shiftUp(size);  // Восстанавливаем свойства кучи
        return true;
    }

    // Добавление элемента
    @Override
    public boolean add(E e) {
        try {
            int size = this.size();
            if (size == this.size) this.resize();
            this.arr[size] = e;
            this.shiftUp(size);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        int new_si = c.size(), size = this.size();
        // Увеличиваем массив пока не хватит места
        while (size + new_si >= this.size) this.resize();
        for (E ele : c) {
            this.arr[size] = ele;
            this.shiftUp(size);  // Восстанавливаем кучу после каждого добавления
            size++;
        }
        return true;
    }

    // Удаление и возврат минимального элемента
    @Override
    public E remove() {
        try {
            int size = this.size();
            E ret = (E) this.arr[0];      // Сохраняем корень
            this.arr[0] = this.arr[size - 1];  // Последний элемент -> в корень
            this.arr[size - 1] = null;    // Удаляем последний элемент
            this.shiftDown(0);              // Восстанавливаем кучу
            return ret;
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    // Удаление всех элементов из коллекции c
    @Override
    public boolean removeAll(Collection<?> c) {
        int size = this.size(), j = 0;
        boolean modified = false;
        Object[] new_arr = new Object[this.size];
        // Копируем элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(this.arr[i])) {
                new_arr[j] = this.arr[i];
                j++;
            } else modified = true;
        }
        this.arr = new_arr;
        // Перестраиваем кучу
        for (int i = j / 2 - 1; i >= 0; i--) {
            this.shiftDown(i);
        }
        return modified;
    }

    // Удаление и возврат минимального элемента (возвращает null если пусто)
    @Override
    public E poll() {
        if (!this.isEmpty()) {
            int size = this.size();
            E ret = (E) this.arr[0];
            this.arr[0] = this.arr[size - 1];
            this.arr[size - 1] = null;
            this.shiftDown(0);
            return ret;
        }
        return null;
    }

    // Получение минимального элемента без удаления
    @Override
    public E element() {
        try {
            return (E) this.arr[0];
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    // Получение минимального элемента без удаления
    @Override
    public E peek() {
        if (!this.isEmpty()) {
            return (E) this.arr[0];
        }
        return null;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            if (o.equals(this.arr[i])) return true;
        }
        return false;
    }

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.isEmpty()) return true;
        if (!this.isEmpty()) {
            int size = this.size();
            for (Object ele : c) {
                boolean contains = false;
                for (int i = 0; i < size; i++) {
                    if (this.arr[i].equals(ele)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) return false;
            }
            return true;
        }
        return false;
    }

    // Получение количества элементов
    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.arr[i] != null) {
                size++;
            }
        }
        return size;
    }

    // Проверка пустоты очереди
    @Override
    public boolean isEmpty() {
        return this.arr[0] == null;
    }

    // Удаление всех элементов, кроме тех что в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.arr = new Object[this.size];
            return true;
        }
        int size = this.size(), j = 0;
        // Сохраняем только элементы из коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(this.arr[i])) {
                this.arr[j++] = this.arr[i];
            }
        }
        // Очищаем оставшуюся часть массива
        Arrays.fill(this.arr, j, size, null);
        // Перестраиваем кучу
        for (int i = j / 2 - 1; i >= 0; i--) {
            this.shiftDown(i);
        }
        return j < size;
    }

    // Очистка очереди
    @Override
    public void clear() {
        Object[] clear_mass = new Object[this.size];
        this.arr = clear_mass;
    }

    // Увеличение размера массива в 1.5 раза
    private void resize() {
        int size = this.size;
        this.size = (int) (this.size * 1.5);
        Object[] temp = new Object[this.size];
        System.arraycopy(this.arr, 0, temp, 0, size);
        this.arr = temp;
    }

    // Сравнение двух элементов (f < s)
    private boolean isLess(Object f, Object s) {
        E fir = (E) f;
        E sec = (E) s;
        return fir.compareTo(sec) < 0;
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

    @Override
    public boolean remove(Object o) {
        return false;
    }
}