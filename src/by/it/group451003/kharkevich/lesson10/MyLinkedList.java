package by.it.group451003.kharkevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E> {
    // Внутренний класс для узла списка
    static class MyLinkedListNode<E> {
        public E Data; // Данные узла
        public MyLinkedListNode<E> Previous; // Ссылка на предыдущий узел
        public MyLinkedListNode<E> Next; // Ссылка на следующий узел

        public MyLinkedListNode(E data) {
            Data = data; // Инициализация данных
        }
    }

    MyLinkedListNode<E> _head; // Первый элемент списка
    MyLinkedListNode<E> _tail; // Последний элемент списка
    int _size; // Размер списка

    // Конструктор
    public MyLinkedList() {
        _head = null; // Начальное значение головы
        _tail = null; // Начальное значение хвоста
        _size = 0; // Начальный размер
    }

    // Преобразование списка в строку
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Строитель строки
        sb.append('['); // Начало списка
        MyLinkedListNode<E> temp = _head; // Временный указатель на голову

        // Проход по всем элементам списка
        for (int i = 0; i < _size; i++) {
            sb.append(temp.Data); // Добавление данных текущего узла
            if (i < _size - 1) {
                sb.append(", "); // Добавление запятой между элементами
            }
            temp = temp.Next; // Переход к следующему узлу
        }
        sb.append(']'); // Конец списка
        return sb.toString(); // Возврат строки
    }

    @Override
    public boolean add(E e) {
        addLast(e); // Добавление в конец списка
        return true; // Всегда возвращает true
    }

    // Удаление элемента по индексу
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= _size) {
            throw new IndexOutOfBoundsException();
        }

        // Поиск узла по индексу
        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < index; i++) {
            temp = temp.Next;
        }
        E e = temp.Data; // Сохранение данных удаляемого узла

        // Обновление ссылок для удаления узла из списка
        if (temp.Previous != null) {
            temp.Previous.Next = temp.Next; // Следующий предыдущего = следующий текущего
        } else {
            _head = temp.Next; // Если удаляем голову - обновляем голову
        }

        if (temp.Next != null) {
            temp.Next.Previous = temp.Previous; // Предыдущий следующего = предыдущий текущего
        } else {
            _tail = temp.Previous; // Если удаляем хвост - обновляем хвост
        }

        _size--; // Уменьшение размера
        return e; // Возврат удаленного элемента
    }

    @Override
    public boolean remove(Object o) {
        MyLinkedListNode<E> temp = _head; // Начинаем с головы

        // Поиск элемента для удаления
        while (temp != null) {
            if (Objects.equals(o, temp.Data)) {
                // Обновление ссылок аналогично remove(int index)
                if (temp.Previous != null) {
                    temp.Previous.Next = temp.Next;
                } else {
                    _head = temp.Next;
                }

                if (temp.Next != null) {
                    temp.Next.Previous = temp.Previous;
                } else {
                    _tail = temp.Previous;
                }

                _size--; // Уменьшение размера
                return true; // Элемент найден и удален
            }
            temp = temp.Next; // Переход к следующему узлу
        }
        return false; // Элемент не найден
    }

    @Override
    public int size() {
        return _size; // Возврат размера списка
    }

    @Override
    public void addFirst(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e); // Создание нового узла

        // Если список не пуст
        if (_head != null) {
            node.Next = _head; // Новый узел указывает на старую голову
            _head.Previous = node; // Старая голова указывает на новый узел
        }
        _head = node; // Новый узел становится головой

        // Если список был пуст
        if (_tail == null) {
            _tail = node; // Хвост тоже указывает на новый узел
        }

        _size++; // Увеличение размера
    }

    @Override
    public void addLast(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e); // Создание нового узла

        // Если список не пуст
        if (_tail != null) {
            _tail.Next = node; // Старый хвост указывает на новый узел
            node.Previous = _tail; // Новый узел указывает на старый хвост
        }
        _tail = node; // Новый узел становится хвостом

        // Если список был пуст
        if (_head == null) {
            _head = node; // Голова тоже указывает на новый узел
        }

        _size++; // Увеличение размера
    }

    @Override
    public E element() {
        return getFirst(); // Получение первого элемента
    }

    @Override
    public E getFirst() {
        if (_size == 0) {
            throw new NoSuchElementException(); // Если список пуст - исключение
        }
        return _head.Data; // Возврат данных головы
    }

    @Override
    public E getLast() {
        if (_size == 0) {
            throw new NoSuchElementException(); // Если список пуст - исключение
        }
        return _tail.Data; // Возврат данных хвоста
    }

    @Override
    public E poll() {
        return pollFirst(); // Удаление первого элемента
    }

    @Override
    public E pollFirst() {
        if (_size == 0) {
            return null; // Если список пуст - возврат null
        }
        E e = _head.Data; // Сохранение данных головы
        _head = _head.Next; // Сдвиг головы на следующий узел

        // Обновление ссылок
        if (_head != null) {
            _head.Previous = null; // Новая голова не имеет предыдущего
        } else {
            _tail = null; // Если список стал пуст - хвост тоже null
        }

        _size--; // Уменьшение размера
        return e; // Возврат удаленного элемента
    }

    @Override
    public E pollLast() {
        if (_size == 0) {
            return null; // Если список пуст - возврат null
        }
        E e = _tail.Data; // Сохранение данных хвоста
        _tail = _tail.Previous; // Сдвиг хвоста на предыдущий узел

        // Обновление ссылок
        if (_tail != null) {
            _tail.Next = null; // Новый хвост не имеет следующего
        } else {
            _head = null; // Если список стал пуст - голова тоже null
        }

        _size--; // Уменьшение размера
        return e; // Возврат удаленного элемента
    }

    // Дополнительные методы с реализацией
    @Override
    public boolean offerFirst(E e) {
        addFirst(e); // Добавление в начало
        return true; // Всегда возвращает true
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e); // Добавление в конец
        return true; // Всегда возвращает true
    }

    @Override
    public E removeFirst() {
        if (_size == 0) {
            throw new NoSuchElementException(); // Если список пуст - исключение
        }
        return pollFirst(); // Удаление первого элемента
    }

    @Override
    public E removeLast() {
        if (_size == 0) {
            throw new NoSuchElementException(); // Если список пуст - исключение
        }
        return pollLast(); // Удаление последнего элемента
    }

    @Override
    public E peekFirst() {
        if (_size == 0) {
            return null; // Если список пуст - возврат null
        }
        return _head.Data; // Возврат данных головы без удаления
    }

    @Override
    public E peekLast() {
        if (_size == 0) {
            return null; // Если список пуст - возврат null
        }
        return _tail.Data; // Возврат данных хвоста без удаления
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o); // Удаление первого вхождения
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        MyLinkedListNode<E> temp = _tail; // Начинаем с хвоста

        // Поиск элемента с конца
        while (temp != null) {
            if (Objects.equals(o, temp.Data)) {
                // Обновление ссылок аналогично remove(Object o)
                if (temp.Previous != null) {
                    temp.Previous.Next = temp.Next;
                } else {
                    _head = temp.Next;
                }

                if (temp.Next != null) {
                    temp.Next.Previous = temp.Previous;
                } else {
                    _tail = temp.Previous;
                }

                _size--; // Уменьшение размера
                return true; // Элемент найден и удален
            }
            temp = temp.Previous; // Переход к предыдущему узлу
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e); // Добавление в конец
    }

    @Override
    public E remove() {
        return removeFirst(); // Удаление первого элемента
    }

    @Override
    public E peek() {
        return peekFirst(); // Просмотр первого элемента
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг изменения
        // Добавление всех элементов коллекции
        for (E e : c) {
            if (add(e)) {
                modified = true; // Если добавление успешно - устанавливаем флаг
            }
        }
        return modified; // Возврат флага изменения
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        Iterator<E> iterator = iterator(); // Итератор для безопасного удаления

        // Удаление всех элементов, содержащихся в коллекции c
        while (iterator.hasNext()) {
            E element = iterator.next(); // Следующий элемент
            if (c.contains(element)) {
                iterator.remove(); // Удаление через итератор
                modified = true; // Установка флага изменения
            }
        }
        return modified; // Возврат флага изменения
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        Iterator<E> iterator = iterator(); // Итератор для безопасного удаления

        // Удаление всех элементов, НЕ содержащихся в коллекции c
        while (iterator.hasNext()) {
            E element = iterator.next(); // Следующий элемент
            if (!c.contains(element)) {
                iterator.remove(); // Удаление через итератор
                modified = true; // Установка флага изменения
            }
        }
        return modified; // Возврат флага изменения
    }

    @Override
    public void clear() {
        // Очистка всех узлов
        MyLinkedListNode<E> temp = _head;
        while (temp != null) {
            MyLinkedListNode<E> next = temp.Next; // Сохранение ссылки на следующий
            temp.Previous = null; // Очистка предыдущей ссылки
            temp.Next = null; // Очистка следующей ссылки
            temp.Data = null; // Очистка данных
            temp = next; // Переход к следующему узлу
        }
        _head = null; // Очистка головы
        _tail = null; // Очистка хвоста
        _size = 0; // Сброс размера
    }

    @Override
    public void push(E e) {
        addFirst(e); // Добавление в начало (стековая операция)
    }

    @Override
    public E pop() {
        return removeFirst(); // Удаление из начала (стековая операция)
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверка содержания всех элементов коллекции c
        for (Object o : c) {
            if (!contains(o)) {
                return false; // Если хотя бы один не найден - false
            }
        }
        return true; // Все элементы найдены
    }

    @Override
    public boolean contains(Object o) {
        MyLinkedListNode<E> temp = _head; // Начинаем с головы

        // Поиск элемента в списке
        while (temp != null) {
            if (Objects.equals(o, temp.Data)) {
                return true; // Элемент найден
            }
            temp = temp.Next; // Переход к следующему узлу
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean isEmpty() {
        return _size == 0; // Проверка пустоты списка
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private MyLinkedListNode<E> current = _head; // Текущий узел
            private MyLinkedListNode<E> lastReturned = null; // Последний возвращенный узел

            @Override
            public boolean hasNext() {
                return current != null; // Проверка наличия следующего элемента
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(); // Если нет следующего - исключение
                }
                lastReturned = current; // Сохранение последнего возвращенного
                E data = current.Data; // Данные текущего узла
                current = current.Next; // Переход к следующему узлу
                return data; // Возврат данных
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException(); // Если next() не вызывался - исключение
                }

                MyLinkedListNode<E> nodeToRemove = lastReturned; // Узел для удаления

                // Обновление ссылок для удаления узла
                if (nodeToRemove.Previous != null) {
                    nodeToRemove.Previous.Next = nodeToRemove.Next;
                } else {
                    _head = nodeToRemove.Next;
                }

                if (nodeToRemove.Next != null) {
                    nodeToRemove.Next.Previous = nodeToRemove.Previous;
                } else {
                    _tail = nodeToRemove.Previous;
                }

                _size--; // Уменьшение размера
                lastReturned = null; // Сброс последнего возвращенного
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[_size]; // Создание массива
        MyLinkedListNode<E> temp = _head; // Начинаем с головы

        // Копирование элементов в массив
        for (int i = 0; i < _size; i++) {
            array[i] = temp.Data; // Копирование данных
            temp = temp.Next; // Переход к следующему узлу
        }
        return array; // Возврат массива
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если массив слишком мал - создаем новый
        if (a.length < _size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), _size);
        }

        MyLinkedListNode<E> temp = _head; // Начинаем с головы

        // Копирование элементов в массив
        for (int i = 0; i < _size; i++) {
            a[i] = (T) temp.Data; // Копирование данных с приведением типа
            temp = temp.Next; // Переход к следующему узлу
        }

        // Если массив больше размера списка
        if (a.length > _size) {
            a[_size] = null; // Установка null после последнего элемента
        }

        return a; // Возврат массива
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private MyLinkedListNode<E> current = _tail; // Начинаем с хвоста

            @Override
            public boolean hasNext() {
                return current != null; // Проверка наличия следующего элемента
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(); // Если нет следующего - исключение
                }
                E data = current.Data; // Данные текущего узла
                current = current.Previous; // Переход к предыдущему узлу
                return data; // Возврат данных
            }
        };
    }
}