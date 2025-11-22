package by.it.group451003.kharkevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Начальный размер массива по умолчанию
    static final int INIT_SIZE = 10;
    // Массив для хранения элементов списка
    E[] list;
    // Текущее количество элементов в списке
    int curItem = 0;

    // Конструктор по умолчанию - создает список начального размера
    public ListC() {
        this(INIT_SIZE);
    }

    // Конструктор с заданным размером - создает список указанного размера
    public ListC(int size) {
        // Создаем массив объектов и приводим к типу E (из-за особенностей дженериков в Java)
        list = (E[]) new Object[size];
    }

    // Метод для представления списка в виде строки
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // Проходим по всем элементам списка
        for (int i = 0; i < curItem; ++i) {
            E curSym = list[i];
            sb.append(curSym);
            // Добавляем запятую между элементами, кроме последнего
            if (i < curItem - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли увеличивать массив
        if (curItem >= list.length) {
            // Создаем новый массив в два раза больше
            E[] _cList = (E[]) new Object[list.length * 2];
            // Копируем все элементы из старого массива в новый
            for (int i = 0; i < list.length; ++i) {
                _cList[i] = list[i];
            }
            list = _cList;
        }
        // Добавляем элемент в конец
        list[curItem] = e;
        curItem++;
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= curItem) {
            return null;
        }

        // Сохраняем удаляемый элемент для возврата
        E _rItem = list[index];

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < curItem - 1; ++i) {
            list[i] = list[i + 1];
        }

        // Очищаем последний элемент (теперь он дублируется)
        list[curItem - 1] = null;
        // Уменьшаем счетчик элементов
        curItem--;

        return _rItem;
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return curItem;
    }

    // Добавление элемента по указанному индексу
    @Override
    public void add(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index > curItem) {
            return;
        }

        // Проверяем, нужно ли увеличивать массив
        if (curItem >= list.length) {
            // Создаем новый массив в два раза больше
            E[] _cList = (E[]) new Object[list.length * 2];
            // Копируем все элементы
            for (int i = 0; i < list.length; ++i) {
                _cList[i] = list[i];
            }
            list = _cList;
        }

        // Сдвигаем все элементы начиная с индекса вправо
        for (int i = curItem; i > index; i--) {
            list[i] = list[i - 1];
        }

        // Вставляем новый элемент на освободившееся место
        list[index] = element;
        curItem++;
    }

    // Удаление первого вхождения указанного объекта
    @Override
    public boolean remove(Object o) {
        // Ищем объект в списке
        for (int i = 0; i < curItem; ++i) {
            // Безопасное сравнение (учитывает null)
            if (Objects.equals(o, list[i])) {
                // Сдвигаем элементы после найденного
                for (int j = i; j < curItem - 1; ++j) {
                    list[j] = list[j + 1];
                }
                // Очищаем последний элемент
                list[curItem - 1] = null;
                curItem--;
                return true;
            }
        }
        return false;
    }

    // Замена элемента по указанному индексу
    @Override
    public E set(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index >= curItem) {
            return null;
        }

        // Сохраняем старый элемент для возврата
        E _setItem = list[index];
        // Заменяем элемент
        list[index] = element;
        return _setItem;
    }

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return curItem == 0;
    }

    // Очищает список - создает новый массив начального размера
    @Override
    public void clear() {
        list = (E[]) new Object[INIT_SIZE];
        curItem = 0;
    }

    // Возвращает индекс первого вхождения указанного объекта
    @Override
    public int indexOf(Object o) {
        // Поиск с начала списка
        for (int i = 0; i < curItem; ++i) {
            if (Objects.equals(o, list[i])) {
                return i;
            }
        }
        return -1;
    }

    // Возвращает элемент по указанному индексу
    @Override
    public E get(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= curItem) {
            return null;
        }
        return list[index];
    }

    // Проверяет, содержит ли список указанный объект
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < curItem; ++i) {
            if (Objects.equals(o, list[i])) {
                return true;
            }
        }
        return false;
    }

    // Возвращает индекс последнего вхождения указанного объекта
    @Override
    public int lastIndexOf(Object o) {
        // Поиск с конца списка
        for (int i = curItem - 1; i >= 0; --i) {
            if (Objects.equals(o, list[i])) {
                return i;
            }
        }
        return -1;
    }

    // Проверяет, содержит ли список все элементы указанной коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        // Для каждого элемента коллекции проверяем его наличие в списке
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        // Добавляем каждый элемент коллекции
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    // Добавляет все элементы коллекции начиная с указанного индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверка корректности индекса
        if (index < 0 || index > curItem) {
            return false;
        }

        // Если коллекция пуста - ничего не делаем
        if (c.isEmpty()) {
            return false;
        }

        // Проверяем, нужно ли увеличивать массив
        if (curItem + c.size() >= list.length) {
            // Вычисляем новый размер (минимум в 2 раза больше или достаточно для новых элементов)
            int newSize = Math.max(list.length * 2, curItem + c.size() + 1);
            E[] newList = (E[]) new Object[newSize];
            // Копируем существующие элементы
            System.arraycopy(list, 0, newList, 0, curItem);
            list = newList;
        }

        // Сдвигаем существующие элементы вправо чтобы освободить место для новых
        for (int i = curItem - 1; i >= index; i--) {
            list[i + c.size()] = list[i];
        }

        // Вставляем новые элементы из коллекции
        int i = index;
        for (E item : c) {
            list[i] = item;
            i++;
        }

        // Обновляем счетчик элементов
        curItem += c.size();
        return true;
    }

    // Удаляет все элементы, которые содержатся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Проходим с конца чтобы избежать проблем со сдвигом индексов
        for (int i = curItem - 1; i >= 0; i--) {
            if (c.contains(list[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    // Удаляет все элементы, которые НЕ содержатся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Проходим с конца чтобы избежать проблем со сдвигом индексов
        for (int i = curItem - 1; i >= 0; i--) {
            if (!c.contains(list[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает подсписок - не реализовано
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    // Возвращает итератор списка с началом с указанного индекса - не реализовано
    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    // Возвращает итератор списка - не реализовано
    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    // Преобразует список в массив указанного типа - не реализовано полностью
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Преобразует список в массив объектов
    @Override
    public Object[] toArray() {
        // Создаем массив нужного размера
        Object[] result = new Object[curItem];
        // Копируем элементы
        for (int i = 0; i < curItem; i++) {
            result[i] = list[i];
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает итератор для обхода элементов списка
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            // Проверяет, есть ли следующий элемент
            @Override
            public boolean hasNext() {
                return currentIndex < curItem;
            }

            // Возвращает следующий элемент
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return list[currentIndex++];
            }
        };
    }

}