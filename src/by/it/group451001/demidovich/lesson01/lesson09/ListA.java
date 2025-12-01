package by.it.group451001.demidovich.lesson01.lesson09;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    static class Node<E> {//список
        E value;
        Node<E> next;
        Node(E val) { value = val; }
    }
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {//Формирует строковое представление списка в формате
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> node = new Node<>(e);//Создать новый узел
        if (head == null) { //Если список пуст
            head = tail = node; //указывают на новый узел
        } else {
            tail.next = node;//Присоединить новый узел к tail.next
            tail = node;//Обновить tail на новый узел
        }
        size++;//Увеличить size
        return true;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @Override
    public E remove(int index) {//Проверить корректность индекса
        checkIndex(index);
        Node<E> cur = head;
        Node<E> prev = null;
        for (int i = 0; i < index; i++) {//Найти удаляемый узел и предыдущий узел
            prev = cur;
            cur = cur.next;
        }
        E val = cur.value;
        if (prev == null) {//Удаление головы (prev == null): обновить head
            head = cur.next;
            if (head == null) tail = null;//Удаление хвоста: обновить tail
        } else {
            prev.next = cur.next;//Удалить узел, перенаправив ссылки
            if (prev.next == null) tail = prev;
        }
        size--;//Уменьшить size
        return val;
    }

    @Override
    public int size() {//Просто возвращает значение поля size
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }


    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        Node<E> newNode = new Node<>(element);
        if (index == 0) {//index = 0: вставка в начало
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
        } else if (index == size) {//index = size: вставка в конец (вызов add(e))
            add(element);
            return;
        } else {//0 < index < size: вставка в середину
            Node<E> cur = head;
            for (int i = 0; i < index - 1; i++) cur = cur.next;
            newNode.next = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {

        Node<E> cur = head;//Поиск первого вхождения элемента
        Node<E> prev = null;//Удаление аналогично remove(int index)
        while (cur != null) {//Возвращает true если элемент был найден и удален
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {//Найти узел по индексу
        checkIndex(index);//Заменить значение
        Node<E> cur = head;//Вернуть старое значение
        for (int i = 0; i < index; i++) cur = cur.next;
        E old = cur.value;
        cur.value = element;
        return old;
    }


    @Override
    public boolean isEmpty() {//проверка пустоты списка

        return size == 0;
    }


    @Override
    public void clear() {//очистка списка
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {//поиск первого вхождения

        Node<E> cur = head;
        int idx = 0;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) return idx;
            cur = cur.next;
            idx++;
        }
        return -1;
    }

    @Override
    public E get(int index) {//получение элемента по индексу

        checkIndex(index);
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.value;
    }

    @Override
    public boolean contains(Object o) {// проверка наличия элемента

        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {//Проходит весь список, запоминая последнее найденное вхождение.
        Node<E> cur = head;
        int idx = 0;
        int last = -1;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) last = idx;
            cur = cur.next;
            idx++;
        }
        return last;
    }

    @Override
    public boolean containsAll(Collection<?> c) {//Проверяет, что все элементы коллекции c содержатся в списке.
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {//Добавляет все элементы коллекции в конец списка.
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {//Создать цепочку новых узлов из коллекции
        checkIndexForAdd(index);//Вставить цепочку в нужную позицию
        if (c.isEmpty()) return false;//Обновить ссылки head и tail при необходимости
        Node<E> cur = head;
        Node<E> prev = null;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        Node<E> firstNew = null;
        Node<E> lastNew = null;
        for (E e : c) {
            Node<E> n = new Node<>(e);
            if (firstNew == null) firstNew = lastNew = n;
            else {
                lastNew.next = n;
                lastNew = n;
            }
            size++;
        }
        if (prev == null) {
            lastNew.next = head;
            head = firstNew;
            if (tail == null) tail = lastNew;
        } else {
            lastNew.next = prev.next;
            prev.next = firstNew;
            if (lastNew.next == null) tail = lastNew;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {//Удаляет все элементы, которые содержатся в коллекции c.
        boolean changed = false;
        Node<E> cur = head;
        Node<E> prev = null;
        while (cur != null) {
            if (c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    cur = head;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                    cur = prev.next;
                }
                size--;
                changed = true;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {//Удаляет все элементы, которые НЕ содержатся в коллекции c (сохраняет только пересечение).
        boolean changed = false;
        Node<E> cur = head;
        Node<E> prev = null;
        while (cur != null) {
            if (!c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    cur = head;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                    cur = prev.next;
                }
                size--;
                changed = true;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {//Создает новый список, содержащий элементы с fromIndex до toIndex-1.
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(this.get(i));
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {//Создает массив Object[] и заполняет его элементами списка
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) (a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size));
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            arr[i++] = (T) cur.value;
            cur = cur.next;
        }
        if (arr.length > size) arr[size] = null;
        return arr;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            arr[i++] = cur.value;
            cur = cur.next;
        }
        return arr;
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
