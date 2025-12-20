package by.it.group451003.kharkevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E>
{

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    static int _initialSize = 8; // Начальный размер массива по умолчанию
    E[] _list; // Массив для хранения элементов списка
    int _currentItemIndex = 0; // Текущее количество элементов и индекс для добавления следующего

    // Конструктор по умолчанию - создает список с начальным размером
    public ListB()
    {
        this(_initialSize);
    }

    // Конструктор с параметром - создает список с указанным размером
    public ListB(int size)
    {
        _list = (E[]) new Object[size]; // Создание массива объектов и приведение к типу E[]
    }

    // Метод для представления списка в виде строки
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(); // Используем StringBuilder для эффективной конкатенации

        sb.append('['); // Начинаем с открывающей скобки
        for (int i = 0; i < _currentItemIndex; i++) // Проходим по всем элементам
        {
            sb.append(_list[i]); // Добавляем текущий элемент
            if (i < _currentItemIndex - 1) // Если это не последний элемент
            {
                sb.append(", "); // Добавляем запятую и пробел
            }
        }
        sb.append("]"); // Закрывающая скобка

        return sb.toString(); // Возвращаем полученную строку
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e)
    {
        // Проверяем, нужно ли расширять массив
        if (_currentItemIndex == _list.length)
        {
            // Создаем новый массив в два раза больше
            E[] listCopy = (E[]) new Object[_list.length * 2];
            // Копируем все элементы из старого массива в новый
            for (int i = 0; i < _list.length; i++)
            {
                listCopy[i] = _list[i];
            }
            _list = listCopy; // Заменяем старый массив новым
        }

        // Добавляем элемент в конец
        _list[_currentItemIndex] = e;
        _currentItemIndex++; // Увеличиваем счетчик элементов

        return true; // Всегда возвращаем true по контракту интерфейса
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index)
    {
        // Проверка корректности индекса
        if (index < 0 || index >= _currentItemIndex)
        {
            return null; // Неверный индекс - возвращаем null
        }

        E removedItem = _list[index]; // Сохраняем удаляемый элемент

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < _currentItemIndex - 1; i++)
        {
            _list[i] = _list[i + 1];
        }

        _currentItemIndex--; // Уменьшаем счетчик элементов

        return removedItem; // Возвращаем удаленный элемент
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return _currentItemIndex; // Текущий индекс равен количеству элементов
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Вставка элемента на определенную позицию
    @Override
    public void add(int index, E element)
    {
        // Проверка корректности индекса
        if (index < 0 || index > _currentItemIndex)
        {
            return; // Неверный индекс - выходим
        }

        // Проверяем, нужно ли расширять массив
        if (_currentItemIndex == _list.length)
        {
            E[] listCopy = (E[]) new Object[_list.length * 2];
            for (int i = 0; i < _list.length; i++)
            {
                listCopy[i] = _list[i];
            }
            _list = listCopy;
        }

        // Сдвигаем все элементы от конца до позиции вставки вправо
        for (int i = _currentItemIndex; i > index; i--)
        {
            _list[i] = _list[i - 1];
        }

        // Вставляем новый элемент
        _list[index] = element;
        _currentItemIndex++; // Увеличиваем счетчик элементов
    }

    // Удаление первого вхождения объекта
    @Override
    public boolean remove(Object o)
    {
        // Ищем объект в массиве
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i])) // Нашли совпадение
            {
                E removedItem = _list[i]; // Сохраняем удаляемый элемент

                // Сдвигаем все последующие элементы влево
                for (int j = i; j < _currentItemIndex - 1; j++)
                {
                    _list[j] = _list[j + 1];
                }

                _currentItemIndex--; // Уменьшаем счетчик элементов

                return true; // Успешно удалили
            }
        }

        return false; // Объект не найден
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element)
    {
        // Проверка корректности индекса
        if (index < 0 || index >= _currentItemIndex)
        {
            return null; // Неверный индекс
        }

        E setItem = _list[index]; // Сохраняем старый элемент
        _list[index] = element; // Заменяем на новый

        return setItem; // Возвращаем старый элемент
    }

    // Проверка, пуст ли список
    @Override
    public boolean isEmpty() {
        return _currentItemIndex == 0; // Пуст, если нет элементов
    }

    // Очистка списка
    @Override
    public void clear()
    {
        _list = (E[]) new Object[_initialSize]; // Создаем новый массив начального размера
        _currentItemIndex = 0; // Сбрасываем счетчик элементов
    }

    // Поиск индекса первого вхождения объекта
    @Override
    public int indexOf(Object o)
    {
        // Линейный поиск с начала массива
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i])) // Нашли совпадение
            {
                return i; // Возвращаем индекс
            }
        }

        return -1; // Объект не найден
    }

    // Получение элемента по индексу
    @Override
    public E get(int index)
    {
        // Проверка корректности индекса
        if (index < 0 || index >= _currentItemIndex)
        {
            return null; // Неверный индекс
        }

        return _list[index]; // Возвращаем элемент
    }

    // Проверка наличия объекта в списке
    @Override
    public boolean contains(Object o)
    {
        // Используем indexOf для проверки наличия
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i])) // Нашли совпадение
            {
                return true; // Объект найден
            }
        }

        return false; // Объект не найден
    }

    // Поиск индекса последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o)
    {
        // Линейный поиск с конца массива
        for (int i = _currentItemIndex - 1; i >= 0; i--)
        {
            if (o.equals(_list[i])) // Нашли совпадение
            {
                return i; // Возвращаем индекс
            }
        }

        return -1; // Объект не найден
    }

    // Проверка, содержит ли список все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c)
    {
        // Проверяем каждый элемент коллекции
        for (Object item : c)
        {
            if (!contains(item)) // Если хотя бы один элемент не найден
            {
                return false; // Возвращаем false
            }
        }

        return true; // Все элементы найдены
    }

    // Добавление всех элементов коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        // Добавляем каждый элемент коллекции
        for (Object item : c)
        {
            add((E) item); // Приводим тип и добавляем
        }

        return true; // Всегда возвращаем true
    }

    // Добавление всех элементов коллекции начиная с указанной позиции
    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        // Добавляем каждый элемент на указанную позицию
        for (Object item : c)
        {
            add(index, (E) item); // Приводим тип и добавляем
            index++; // Увеличиваем индекс для следующего элемента
        }

        return true; // Всегда возвращаем true
    }

    // Удаление всех элементов, содержащихся в коллекции
    @Override
    public boolean removeAll(Collection<?> c)
    {
        // Удаляем каждый элемент коллекции
        for (Object item : c)
        {
            if (!remove((E) item)) // Если не удалось удалить какой-то элемент
            {
                return false; // Возвращаем false
            }
        }

        return true; // Все элементы удалены
    }

    // Удаление всех элементов, НЕ содержащихся в коллекции (оставляет только пересечение)
    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean retained = false; // Флаг изменения списка

        // Проходим по всем элементам списка
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (!c.contains(_list[i])) // Если элемент не содержится в коллекции
            {
                remove(i); // Удаляем его
                i--; // Корректируем индекс после удаления
                retained = true; // Устанавливаем флаг изменения
            }
        }

        return retained; // Возвращаем true если список изменился
    }

    // Создание подсписка
    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        // Создаем новый список нужного размера (используется ListA вместо ListB - возможная ошибка)
        ListA<E> subList = new ListA<E>(toIndex - fromIndex + 1);

        // Копируем элементы из исходного списка в подсписок
        for (int i = 0; i < subList.size(); i++)
        {
            subList.add(_list[i + fromIndex]);
        }

        return subList; // Возвращаем подсписок
    }

    // Методы, которые не реализованы (возвращают null)

    @Override
    public ListIterator<E> listIterator(int index)
    {
        return null; // Не реализовано
    }

    @Override
    public ListIterator<E> listIterator() {
        return null; // Не реализовано
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null; // Не реализовано
    }

    @Override
    public Object[] toArray()
    {
        return _list; // Возвращаем внутренний массив
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null; // Не реализовано
    }

}