package by.it.group451001.kazakov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] v;          // Внутренний массив для хранения элементов в отсортированном порядке
    int len;                // Текущее количество элементов
    final int startLen = 8; // Начальный размер массива

    // Увеличение размера массива при заполнении
    @SuppressWarnings("unchecked")
    private void extend_len(){
        var new_v = (E[]) new Comparable[v.length << 1];  // Новый массив в 2 раза больше
        System.arraycopy(v, 0, new_v, 0, v.length);       // Копируем элементы
        v = new_v;                                         // Заменяем старый массив
    }

    // Бинарный поиск позиции для вставки элемента
    private int binpos(E target, int l, int r){
        if (r - l <= 1)  // Базовый случай: осталось 1-2 элемента
            return v[l].compareTo(target) >= 0 ? l : r;  // Возвращаем подходящую позицию

        var m = (r + l) >> 1;  // Середина диапазона
        // Рекурсивно ищем в левой или правой половине
        return v[m].compareTo(target) < 0 ? binpos(target, m + 1, r) : binpos(target, l, m);
    }

    // Вставка элемента на указанную позицию
    private boolean insert(E target, int index){
        if (len + 1 == v.length)  // Проверка необходимости расширения
            extend_len();
        // Сдвигаем элементы вправо для освобождения места
        System.arraycopy(v, index, v, index + 1, len++ - index);
        v[index] = target;  // Вставляем элемент
        return true;
    }

    // Удаление элемента по индексу
    private boolean delete(int index){
        // Сдвигаем элементы влево для заполнения пустоты
        System.arraycopy(v, index + 1, v, index, len-- - index - 1);
        return true;
    }

    // Строковое представление множества
    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < len; i++)
            s.append(v[i]).append(", ");  // Добавляем элементы через запятую
        // Удаляем последнюю запятую и пробел, добавляем закрывающую скобку
        return s.deleteCharAt(s.length() - 1).deleteCharAt(s.length() - 1).append("]").toString();
    }

    // Поиск первой позиции, где элемент >= target (нижняя граница)
    private int lower_bound(E target){
        if (len == 0)  // Если множество пусто
            return 0;
        if (v[len - 1].compareTo(target) < 0)  // Если target больше всех элементов
            return len;
        return binpos(target, 0, len - 1);  // Ищем позицию бинарным поиском
    }

    // Конструктор - инициализация массива
    @SuppressWarnings("all")
    public MyTreeSet(){
        v = (E[]) new Comparable[startLen];
        len = 0;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    // Проверка наличия элемента в множестве
    @SuppressWarnings("all")
    @Override
    public boolean contains(Object o) {
        var tmp = lower_bound((E) o);  // Ищем позицию элемента
        return tmp != len && v[tmp].equals(o);  // Проверяем, что элемент найден
    }

    // Не реализованные методы
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

    // Добавление элемента в множество (с сохранением уникальности и порядка)
    @Override
    public boolean add(E e) {
        var tmp = lower_bound(e);  // Ищем позицию для вставки
        if (len == 0 || tmp == len)  // Если множество пусто или элемент больше всех
            return insert(e, tmp);
        // Вставляем только если элемент не существует
        return !v[tmp].equals(e) && insert(e, tmp);
    }

    // Удаление элемента из множества
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        var tmp = lower_bound((E) o);  // Ищем позицию элемента
        // Удаляем если элемент найден
        return tmp != len && v[tmp].equals(o) && delete(tmp);
    }

    // Проверка, что множество содержит все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (var i : c)
            if (!contains(i))  // Если хотя бы один элемент не найден
                return false;
        return true;
    }

    // Добавление всех элементов коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c) {
        var tmp = len;  // Запоминаем исходный размер
        for (var i : c)
            add(i);     // Добавляем каждый элемент
        return tmp != len;  // Возвращаем true если множество изменилось
    }

    // Удаление всех элементов, кроме содержащихся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        var tmp = len;  // Запоминаем исходный размер
        // Проходим по всем элементам множества
        for (int i = 0; i < len;)
            if (!c.contains(v[i]))  // Если элемент не содержится в коллекции
                delete(i);          // Удаляем его
            else
                i++;                // Иначе переходим к следующему
        return tmp != len;  // Возвращаем true если множество изменилось
    }

    // Удаление всех элементов, содержащихся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        var tmp = len;  // Запоминаем исходный размер
        for (var i : c)
            remove(i);  // Удаляем каждый элемент коллекции
        return tmp != len;  // Возвращаем true если множество изменилось
    }

    // Очистка множества
    @SuppressWarnings("all")
    @Override
    public void clear() {
        v = (E[]) new Comparable[startLen];  // Создаем новый массив
        len = 0;                             // Сбрасываем счетчик
    }
}