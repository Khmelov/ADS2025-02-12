package by.it.group451001.kazakov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // Реализация списка на основе двусвязного списка

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Внутренний класс узла списка
    private static class node<E>{
        node<E> next, prev;  // Ссылки на следующий и предыдущий узлы
        E data;              // Данные узла

        public node(){
            next = null;
            prev = null;
        }

        public node(node<E> next, node<E> prev, E data){
            this.next = next;
            this.prev = prev;
            this.data = data;
        }
    }

    int len = 0;              // Количество элементов в списке
    final node<E> head = new node<>();  // Фиктивная голова списка
    node<E> tail = head;      // Хвост списка

    // Возвращает строковое представление списка
    @Override
    public String toString() {
        if (len == 0)
            return "[]";
        String res = "[";
        node<E> tmp = head.next;
        // Проходим по всем элементам кроме последнего
        for (int i = 0; i < len - 1; i++){
            res += tmp.data.toString() + ", ";
            tmp = tmp.next;
        }
        res += tmp.data.toString();  // Добавляем последний элемент
        return res + ']';
    }

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E e) {
        len++;
        // Создаем новый узел и добавляем его после хвоста
        tail.next = new node<>(null, tail, e);
        tail = tail.next;  // Обновляем хвост
        return true;
    }

    // Удаляет элемент по индексу
    @Override
    public E remove(int index) {
        // Особый случай: удаление последнего элемента
        if (index == len - 1){
            var tmp = tail.data;
            tail = tail.prev;    // Перемещаем хвост назад
            tail.next = null;    // Обнуляем ссылку
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

    // Вставляет элемент на указанную позицию
    @Override
    public void add(int index, E element) {
        len++;
        // Ищем место для вставки
        var tmp = head;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        // Создаем новый узел и вставляем его
        tmp.next = new node<>(tmp.next, tmp, element);
        // Обновляем ссылки соседних узлов
        if (tmp.next.next != null)
            tmp.next.next.prev = tmp.next;
        else
            tail = tmp.next;  // Если вставляем в конец, обновляем хвост
    }

    // Удаляет первое вхождение объекта
    @Override
    public boolean remove(Object o) {
        // Линейный поиск объекта
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data)){
                // Перестраиваем ссылки
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

    // Заменяет элемент по индексу
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

    // Очищает список
    @Override
    public void clear() {
        len = 0;
        // Проходим от хвоста к голове, очищая ссылки
        for (; tail != head; tail = tail.prev){
            tail.data = null;   // Помогаем сборщику мусора
            tail.next = null;
        }
        head.next = null;  // Обнуляем ссылку от головы
    }

    // Находит индекс первого вхождения объекта
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

    // Возвращает элемент по индексу
    @Override
    public E get(int index) {
        var tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        return tmp.data;
    }

    // Проверяет наличие объекта в списке
    @Override
    public boolean contains(Object o) {
        for(var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data))
                return true;
        return false;
    }

    // Находит индекс последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o) {
        // Поиск с конца списка для эффективности
        var tmp = tail;
        for (int i = len - 1; i >= 0; i--)
            if (o.equals(tmp.data))
                return i;
            else tmp = tmp.prev;
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Не реализованные методы (возвращают значения по умолчанию)

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

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