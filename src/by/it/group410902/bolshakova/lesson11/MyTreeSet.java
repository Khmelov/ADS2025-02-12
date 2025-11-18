package by.it.group410902.bolshakova.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] mas = new Object[0];
    private int actSize = 0;

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < actSize - 1; i++) {
            res.append(mas[i].toString()).append(", ");  // все элементы кроме последнего
        }
        return res + mas[actSize - 1].toString() + "]";// последний элемент без запятой
    }

    @Override
    public int size() {
        return actSize;
    }

    @Override
    public void clear() {
        actSize = 0;
        mas = new Object[0];
    }

    @Override
    public boolean isEmpty() {
        return actSize == 0;
    }  // проверяем счетчик


    @Override
    public boolean add(E e) {
        int index = 0;
        //находим элементу место в отсортированном массиве
        while (index < actSize && ((Comparable<? super E>) mas[index]).compareTo(e) < 0) {
            index++;   // двигаемся пока текущий элемент меньше добавляемого
        }
        // если элемент уже существует
        if (!isEmpty() && index < actSize && mas[index].equals(e)) {
            return false;
        }
        // Расширяем массив если не хватает места
        if (mas.length == actSize) {
            mas = Arrays.copyOf(mas, actSize * 2 + 1);
        }
        actSize++;  // увеличиваем счетчик элементов
        // Сдвигаем элементы вправо для освобождения места
        for (int i = actSize - 1; i > index; i--) {
            mas[i] = mas[i - 1];
        }
        mas[index] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = 0;
        // Линейный поиск удаляемого элемента
        while (index < actSize && !mas[index].equals(o))
            index++;
        if (index == actSize)
            return false;
        // Сдвигаем элементы влево, затирая удаляемый
        for (int i = index; i < size() - 1; i++)
            mas[i] = mas[i + 1];
        actSize--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        // Линейный поиск по всему массиву
        for (int i = 0; i < actSize; i++)
            if (mas[i].equals(o)) {
                return true;
            }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции есть в множестве
        for (Object o : c) {
            if (!contains(o)) {
                return false; // если хоть одного нет
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean retBull = false;
        // Добавляем все элементы коллекции
        for (E o : c) {
            if (add(o)) {
                retBull = true; // отмечаем что хоть один элемент добавился
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean[] u = new boolean[actSize]; // массив меток для удаления
        int kol = 0, cnt = 0;   // kol - сколько удалить, cnt - счетчик для нового масс
        for (int i = 0; i < actSize; i++)
            if (c.contains(mas[i])) { // если элемент есть в коллекции c
                u[i] = true;
                kol++;
            }
        if (kol == 0)
            return false;
        // Создаем новый массив без удаленных элементов
        Object[] newArr = new Object[actSize - kol];
        for (int i = 0; i < actSize; i++)
            if (!u[i]) // если элемент непомечен для удаления
                newArr[cnt++] = mas[i];  // копируем в новый массив
        mas = newArr;
        actSize = actSize - kol;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean[] u = new boolean[actSize];
        int kol = 0, cnt = 0;
        // Размечаем элементы для сохранения
        for (int i = 0; i < actSize; i++)
            if (c.contains(mas[i])) {
                u[i] = true; // помечаем для сохранения
                kol++;
            }
        if (kol == actSize)
            return false;//ничего не изменилось
        // Создаем новый массив только с сохраняемыми элементами
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

