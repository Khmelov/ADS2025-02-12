package by.it.group410902.habrukovich.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> { //куча


    private int size_of_array;  // Текущая емкость массива
    private Object[] el_ar;     // Массив для хранения элементов кучи

    // Конструктор - создает массив начального размера
    public MyPriorityQueue() {
        this.size_of_array = 10;
        this.el_ar = new Object[this.size_of_array];
    }

    // Просеивание вверх (восстановление свойства кучи при добавлении)
    public void shiftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;  // Индекс родителя
            // Если текущий элемент меньше родителя - меняем местами
            if (isLess(this.el_ar[i], this.el_ar[parent])) {
                Object temp = this.el_ar[i];
                this.el_ar[i] = this.el_ar[parent];
                this.el_ar[parent] = temp;
                i = parent;  // Переходим к родителю
            } else {
                break;  // Свойство кучи восстановлено
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
        if (left_ch < size && isLess(this.el_ar[left_ch], this.el_ar[smallest])) {
            smallest = left_ch;
        }
        // Сравниваем с правым потомком
        if (right_ch < size && isLess(this.el_ar[right_ch], this.el_ar[smallest])) {
            smallest = right_ch;
        }
        // Если найден меньший потомок - меняем местами и продолжаем
        if (smallest != i) {
            Object temp = this.el_ar[smallest];
            this.el_ar[smallest] = this.el_ar[i];
            this.el_ar[i] = temp;
            this.shiftDown(smallest);  // Рекурсивно продолжаем
        }
    }

    // Преобразование в строку для вывода
    public String toString() {
        if (!this.isEmpty()) {
            String ret_val = "[";
            int size = this.size();
            for (int i = 0; i < size - 1; i++) {
                if (this.el_ar[i] != null)
                    ret_val += this.el_ar[i].toString() + ", ";
                else
                    ret_val += "null, ";
            }
            if (this.el_ar[size - 1] != null) {
                ret_val += this.el_ar[size - 1].toString() + "]";
            } else {
                ret_val += "null]";
            }
            return ret_val;
        }
        return "[]";
    }
    @Override
    public boolean offer(E e) {
        int size = this.size();
        if (size == this.size_of_array) this.resize();  // Увеличиваем массив если нужно
        this.el_ar[size] = e;
        this.shiftUp(size);
        return true;
    }

    @Override
    public boolean add(E e) {
        try {
            int size = this.size();
            if (size == this.size_of_array) this.resize();
            this.el_ar[size] = e;
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
        while (size + new_si >= this.size_of_array) this.resize();
        for (E ele : c) {
            this.el_ar[size] = ele;
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
            E ret = (E) this.el_ar[0];      // Сохраняем корень
            this.el_ar[0] = this.el_ar[size - 1];  // Последний элемент -> в корень
            this.el_ar[size - 1] = null;    // Удаляем последний элемент
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
        Object[] new_arr = new Object[this.size_of_array];
        // Копируем элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(this.el_ar[i])) {
                new_arr[j] = this.el_ar[i];
                j++;
            } else modified = true;
        }
        this.el_ar = new_arr;
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
            E ret = (E) this.el_ar[0];
            this.el_ar[0] = this.el_ar[size - 1];
            this.el_ar[size - 1] = null;
            this.shiftDown(0);
            return ret;
        }
        return null;
    }

    // Получение минимального элемента без удаления
    @Override
    public E element() {
        try {
            return (E) this.el_ar[0];
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    // Получение минимального элемента без удаления
    @Override
    public E peek() {
        if (!this.isEmpty()) {
            return (E) this.el_ar[0];
        }
        return null;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            if (o.equals(this.el_ar[i])) return true;
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
                    if (this.el_ar[i].equals(ele)) {
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
        for (int i = 0; i < this.size_of_array; i++) {
            if (this.el_ar[i] != null) {
                size++;
            }
        }
        return size;
    }

    // Проверка пустоты очереди
    @Override
    public boolean isEmpty() {
        return this.el_ar[0] == null;
    }

    // Удаление всех элементов, кроме тех что в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.el_ar = new Object[this.size_of_array];
            return true;
        }
        int size = this.size(), j = 0;
        // Сохраняем только элементы из коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(this.el_ar[i])) {
                this.el_ar[j++] = this.el_ar[i];
            }
        }
        // Очищаем оставшуюся часть массива
        Arrays.fill(this.el_ar, j, size, null);
        // Перестраиваем кучу
        for (int i = j / 2 - 1; i >= 0; i--) {
            this.shiftDown(i);
        }
        return j < size;
    }

    // Очистка очереди
    @Override
    public void clear() {
        Object[] clear_mass = new Object[this.size_of_array];
        this.el_ar = clear_mass;
    }

    // Увеличение размера массива в 1.5 раза
    private void resize() {
        int size = this.size_of_array;
        this.size_of_array = (int) (this.size_of_array * 1.5);
        Object[] temp = new Object[this.size_of_array];
        System.arraycopy(this.el_ar, 0, temp, 0, size);
        this.el_ar = temp;
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
