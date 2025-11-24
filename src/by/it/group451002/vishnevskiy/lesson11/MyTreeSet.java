package by.it.group451002.vishnevskiy.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Задача заключалась в реализации TreeSet - множества,
// которое хранит элементы в отсортированном порядке, используя
// отсортированный массив, без готовых классов Java Collections Framework.
//                toString()
//                size()
//                clear()
//                isEmpty()
//                add(Object)
//                remove(Object)
//                contains(Object)
//
//                containsAll(Collection)
//                addAll(Collection)
//                removeAll(Collection)
//                retainAll(Collection)

public class MyTreeSet<E> implements Set<E> {


    private Object[] mas = new Object[0];  //Массив для хранения элементов
    private int actSize = 0;  // Текущий размер множества (количество элементов)

    @Override
    //Формирует строковое представление множества в формате [elem1, elem2, ...]
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < actSize - 1; i++) {
            res.append(mas[i].toString()).append(", ");
        }
        return res + mas[actSize - 1].toString() + "]";
    }

    @Override
    // Возвращает количество элементов в множестве (actSize)
    public int size() {
        return actSize;
    }

    @Override
    // Очищает множество,устанавливая размер в 0 и сбрасывая массив
    public void clear() {
        actSize = 0;
        mas = new Object[0];
    }

    @Override
    // Проверяет, пусто ли множество (actSize == 0)
    public boolean isEmpty() {
        return actSize == 0;
    }


    @Override
    // Добавляет элемент e в множество
    public boolean add(E e) {
        // Определяет позицию, куда вставить элемент (сравнивает через Comparable)
        // Вставляет элемент в массив,сдвигая элементы для сохранения порядка
        // Увеличивает actSize. Возвращает true, если элемент добавлен, и false, если уже существует
        int index = 0;
        while (index < actSize && ((Comparable<? super E>) mas[index]).compareTo(e) < 0) {
            index++;
        }
        if (!isEmpty() && index < actSize && mas[index].equals(e)) {
            return false;
        }
        if (mas.length == actSize) {
            mas = Arrays.copyOf(mas, actSize * 2 + 1);
        }
        actSize++;
        for (int i = actSize - 1; i > index; i--) {
            mas[i] = mas[i - 1];
        }
        mas[index] = e;
        return true;
    }

    @Override
    // Удаляет элемент o из множества
    public boolean remove(Object o) {
        // Находит элемент в массиве. Сдвигает все последующие элементы, заполняя "пустое место".
        // Уменьшает actSize. Возвращает true,если элемент удалён, и false, если он отсутствовал
        int index = 0;
        while (index < actSize && !mas[index].equals(o))
            index++;
        if (index == actSize)
            return false;
        for (int i = index; i < size() - 1; i++)
            mas[i] = mas[i + 1];
        actSize--;
        return true;
    }

    @Override
    // Проверяет, содержится ли элемент o в множестве. Возвращает true, если элемент найден, иначе false
    public boolean contains(Object o) {
        for (int i = 0; i < actSize; i++)
            if (mas[i].equals(o)) {
                return true;
            }
        return false;
    }

    @Override
    // Проверяет,содержатся ли все элементы из коллекции c в множестве
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // Добавляет все элементы из коллекции c в множество.Возвращает true, если хотя бы один элемент был добавлен
    public boolean addAll(Collection<? extends E> c) {
        boolean retBull = false;
        for (E o : c) {
            if (add(o)) {
                retBull = true;
            }
        }
        return true;
    }
    @Override
    // Удаляет из множества все элементы, которые содержатся в коллекции c
    public boolean removeAll(Collection<?> c) {
        // Используется массив u для пометки элементов, которые нужно удалить
        // Формируется новый массив без удалённых элементов. Возвращает true,
        //если хотя бы один элемент был удалён.
        boolean[] u = new boolean[actSize];
        int kol = 0, cnt = 0;
        for (int i = 0; i < actSize; i++)
            if (c.contains(mas[i])) {
                u[i] = true;
                kol++;
            }
        if (kol == 0)
            return false;
        Object[] newArr = new Object[actSize - kol];
        for (int i = 0; i < actSize; i++)
            if (!u[i])
                newArr[cnt++] = mas[i];
        mas = newArr;
        actSize = actSize - kol;
        return true;
    }

    @Override
    // Оставляет в множестве только те элементы, которые присутствуют в коллекции c
    public boolean retainAll(Collection<?> c) {
        // Используется массив u для пометки элементов, которые нужно оставить.
        //Формируется новый массив с оставшимися элементами. Возвращает true,
        // если хотя бы один элемент был удалён.
        boolean[] u = new boolean[actSize];
        int kol = 0, cnt = 0;
        for (int i = 0; i< actSize; i++)
            if (c.contains(mas[i])) {
                u[i] = true;
                kol++;
            }
        if (kol == actSize)
            return false;
        Object[] newArr = new Object[kol];
        for (int i = 0; i < actSize; i++)
            if (u[i])
                newArr[cnt++] = mas[i];
        mas = newArr;
        actSize = kol;
        return true;
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