package by.it.group451001.kazakov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // Реализация списка на основе двусвязного списка

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Внутренний класс узла двусвязного списка
    private static class node<E>{
        node<E> next, prev;  // Ссылки на следующий и предыдущий узлы
        E data;              // Хранимые данные

        // Конструктор по умолчанию
        public node(){
            next = null;
            prev = null;
        }

        // Конструктор с параметрами
        public node(node<E> next, node<E> prev, E data){
            this.next = next;
            this.prev = prev;
            this.data = data;
        }
    }

    int len = 0;              // Текущее количество элементов
    final node<E> head = new node<>();  // Фиктивный головной узел
    node<E> tail = head;      // Указатель на хвост списка

    // Строковое представление списка в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        if (len == 0)
            return "[]";
        String res = "[";
        // Проходим по всем элементам кроме последнего
        for (var tmp = head.next; tmp.next != null; tmp = tmp.next)
            res += tmp.data.toString() + ", ";
        res += tail.data.toString();  // Добавляем последний элемент
        return res + ']';
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        len++;
        // Создаем новый узел и добавляем после текущего хвоста
        tail.next = new node<>(null, tail, e);
        tail = tail.next;  // Обновляем указатель на хвост
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        // Особый случай: удаление последнего элемента
        if (index == len - 1){
            var tmp = tail.data;
            tail = tail.prev;    // Перемещаем хвост назад
            tail.next = null;    // Обнуляем ссылку на следующий элемент
            len--;
            return tmp;
        }
        else{
            len--;
            // Ищем узел для удаления
            node<E> tmp = head.next;
            for (int i = 0; i < index; i++)
                tmp = tmp.next;
            // Перестраиваем ссылки, исключая удаляемый узел
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
            return tmp.data;
        }
    }

    @Override
    public int size() {
        return len;
    }

    // Вставка элемента на указанную позицию
    @Override
    public void add(int index, E element) {
        len++;
        // Ищем узел, после которого будем вставлять
        var tmp = head;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        // Создаем новый узел и вставляем в список
        tmp.next = new node<>(tmp.next, tmp, element);
        // Обновляем ссылки соседних узлов
        if (tmp.next.next != null)
            tmp.next.next.prev = tmp.next;
        else
            tail = tmp.next;  // Если вставили в конец, обновляем хвост
    }

    // Удаление первого вхождения объекта
    @Override
    public boolean remove(Object o) {
        // Линейный поиск объекта в списке
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data)){
                // Перестраиваем ссылки для удаления узла
                tmp.prev.next = tmp.next;
                if (tmp.next != null)
                    tmp.next.prev = tmp.prev;
                else
                    tail = tmp.prev;  // Если удаляем последний элемент
                len--;
                return true;
            }
        return false;
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        // Ищем нужный узел
        var tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        var g = tmp.data;   // Сохраняем старое значение
        tmp.data = element; // Устанавливаем новое значение
        return g;           // Возвращаем старое значение
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    // Очистка списка
    @Override
    public void clear() {
        len = 0;
        // Проходим от хвоста к голове, очищая ссылки и данные
        for (; tail != head; tail = tail.prev){
            tail.data = null;   // Помогаем сборщику мусора
            tail.next = null;
        }
        head.next = null;  // Обнуляем ссылку от головы
    }

    // Поиск индекса первого вхождения объекта
    @Override
    public int indexOf(Object o) {
        var tmp = head.next;
        for (int i = 0; i < len; i++)
            if (o.equals(tmp.data))
                return i;
            else
                tmp = tmp.next;
        return -1;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        var tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        return tmp.data;
    }

    // Проверка наличия объекта в списке
    @Override
    public boolean contains(Object o) {
        for(var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data))
                return true;
        return false;
    }

    // Поиск индекса последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o) {
        // Поиск с конца для эффективности
        var tmp = tail;
        for (int i = len - 1; i >= 0; i--)
            if (o.equals(tmp.data))
                return i;
            else tmp = tmp.prev;
        return -1;
    }

    // Проверка, содержит ли список все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (var i : c)
            if (!contains(i))  // Если хотя бы один элемент не найден
                return false;
        return true;
    }

    // Добавление всех элементов коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (var i : c)
            add(i);  // Добавляем каждый элемент
        return !c.isEmpty();  // Возвращаем true если коллекция не пуста
    }

    // Вставка всех элементов коллекции начиная с указанной позиции
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int i1 = index;
        for (var i : c){
            add(i1, i);  // Вставляем элементы по порядку
            i1++;        // Увеличиваем позицию для следующего элемента
        }
        return !c.isEmpty();
    }

    // Удаление всех элементов, содержащихся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        // Проходим по всему списку
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (c.contains(tmp.data)){
                flag = true;  // Нашли элемент для удаления
                // Перестраиваем ссылки
                tmp.prev.next = tmp.next;
                tmp.data = null;  // Очищаем данные
                if (tmp.next != null)
                    tmp.next.prev = tmp.prev;
                else
                    tail = tmp.prev;  // Если удаляем последний элемент
                len--;
            }
        return flag;
    }

    // Удаление всех элементов, кроме содержащихся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean flag = false;
        // Проходим по всему списку
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (!c.contains(tmp.data)){  // Если элемент НЕ нужно сохранять
                flag = true;
                // Удаляем элемент
                tmp.prev.next = tmp.next;
                tmp.data = null;
                if (tmp.next != null)
                    tmp.next.prev = tmp.prev;
                else
                    tail = tmp.prev;
                len--;
            }
        return flag;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Не реализованные методы

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}