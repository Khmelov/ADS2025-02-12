package by.it.group451001.serganovskij.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    // ВНУТРЕННИЙ КЛАСС УЗЛА связного списка
    private static class Node<E> {
        E v;          // Данные узла
        Node<E> prev; // Ссылка на предыдущий узел
        Node<E> next; // Ссылка на следующий узел

        // КОНСТРУКТОР узла
        Node(E v){
            this.v = v;
        }
    }

    // УКАЗАТЕЛИ на начало и конец списка
    private Node<E> head; // Первый элемент
    private Node<E> tail; // Последний элемент

    // РАЗМЕР списка
    private int size;

    // КОНСТРУКТОР пустого списка
    public MyLinkedList(){}

    /////////////////////////////////////////////////////////////////////////
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ РЕАЛИЗАЦИИ
    /////////////////////////////////////////////////////////////////////////

    // СТРОКОВОЕ ПРЕДСТАВЛЕНИЕ списка в формате [элемент1, элемент2, ...]
    @Override
    public String toString(){
        if(size == 0) return "[]"; // Пустой список

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        // ПОСЛЕДОВАТЕЛЬНЫЙ ОБХОД всех узлов от head до tail
        Node<E> n = head;
        while(n != null){
            sb.append(String.valueOf(n.v)); // Добавление значения
            n = n.next; // Переход к следующему узлу
            if(n != null) sb.append(", "); // Запятая между элементами
        }

        sb.append(']');
        return sb.toString();
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В КОНЕЦ списка
    @Override
    public boolean add(E element){
        addLast(element); // Делегирование методу addLast
        return true; // Всегда возвращает true для списков
    }

    // УДАЛЕНИЕ ЭЛЕМЕНТА ПО ИНДЕКСУ
    public E remove(int index){
        // ПРОВЕРКА КОРРЕКТНОСТИ ИНДЕКСА
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node<E> cur;

        // ОПТИМИЗАЦИЯ: выбор направления обхода (с начала или с конца)
        if(index < (size >> 1)){ // Если индекс в первой половине
            cur = head;
            for(int i = 0; i < index; i++) cur = cur.next; // Обход от начала
        } else { // Если индекс во второй половине
            cur = tail;
            for(int i = size - 1; i > index; i--) cur = cur.prev; // Обход с конца
        }

        E val = cur.v;
        unlink(cur); // Удаление узла
        return val;
    }

    // УДАЛЕНИЕ ПЕРВОГО ВХОЖДЕНИЯ ОБЪЕКТА
    @Override
    public boolean remove(Object o){
        Node<E> n = head;

        // ПОИСК узла с заданным значением
        while(n != null){
            // NULL-БЕЗОПАСНОЕ сравнение
            if(o == null ? n.v == null : o.equals(n.v)){
                unlink(n); // Удаление найденного узла
                return true;
            }
            n = n.next;
        }
        return false; // Объект не найден
    }

    // ПОЛУЧЕНИЕ РАЗМЕРА списка
    @Override
    public int size(){
        return size;
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В НАЧАЛО списка
    @Override
    public void addFirst(E element){
        if(element == null) throw new NullPointerException(); // Запрет null

        Node<E> n = new Node<>(element); // Создание нового узла

        if(size == 0){ // Если список пуст
            head = tail = n;
        } else { // Если есть элементы
            n.next = head; // Новый узел указывает на старый head
            head.prev = n; // Старый head указывает на новый узел
            head = n;      // Новый узел становится head
        }
        size++;
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В КОНЕЦ списка
    @Override
    public void addLast(E element){
        if(element == null) throw new NullPointerException(); // Запрет null

        Node<E> n = new Node<>(element); // Создание нового узла

        if(size == 0){ // Если список пуст
            head = tail = n;
        } else { // Если есть элементы
            tail.next = n; // Старый tail указывает на новый узел
            n.prev = tail; // Новый узел указывает на старый tail
            tail = n;      // Новый узел становится tail
        }
        size++;
    }

    // ПОЛУЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА (без удаления)
    @Override
    public E element(){
        return getFirst(); // Синоним getFirst
    }

    // ПОЛУЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА
    @Override
    public E getFirst(){
        if(size == 0) throw new NoSuchElementException(); // Пустой список
        return head.v;
    }

    // ПОЛУЧЕНИЕ ПОСЛЕДНЕГО ЭЛЕМЕНТА
    @Override
    public E getLast(){
        if(size == 0) throw new NoSuchElementException(); // Пустой список
        return tail.v;
    }

    // ИЗВЛЕЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА (с удалением)
    @Override
    public E poll(){
        return pollFirst(); // Синоним pollFirst
    }

    // ИЗВЛЕЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА
    @Override
    public E pollFirst(){
        if(size == 0) return null; // Пустой список
        E v = head.v;
        unlink(head); // Удаление head
        return v;
    }

    // ИЗВЛЕЧЕНИЕ ПОСЛЕДНЕГО ЭЛЕМЕНТА
    @Override
    public E pollLast(){
        if(size == 0) return null; // Пустой список
        E v = tail.v;
        unlink(tail); // Удаление tail
        return v;
    }

    /////////////////////////////////////////////////////////////////////////
    // ВСПОМОГАТЕЛЬНЫЙ МЕТОД ДЛЯ УДАЛЕНИЯ УЗЛА
    /////////////////////////////////////////////////////////////////////////

    // УДАЛЕНИЕ УЗЛА ИЗ СПИСКА с корректным обновлением связей
    private void unlink(Node<E> n){
        Node<E> p = n.prev; // Предыдущий узел
        Node<E> q = n.next; // Следующий узел

        // ОБНОВЛЕНИЕ СВЯЗЕЙ:
        if(p == null) {
            head = q; // Если удаляем head, то head становится следующий
        } else {
            p.next = q; // Иначе предыдущий указывает на следующий
        }

        if(q == null) {
            tail = p; // Если удаляем tail, то tail становится предыдущий
        } else {
            q.prev = p; // Иначе следующий указывает на предыдущий
        }

        // ОЧИСТКА ССЫЛОК удаляемого узла (для сборщика мусора)
        n.prev = n.next = null;
        n.v = null;

        size--;

        // СБРОС указателей если список пуст
        if(size == 0){
            head = tail = null;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ (заглушки)
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean offerFirst(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e){ throw new UnsupportedOperationException(); }
    @Override public E peek(){ throw new UnsupportedOperationException(); }
    @Override public E peekFirst(){ throw new UnsupportedOperationException(); }
    @Override public E peekLast(){ throw new UnsupportedOperationException(); }

    @Override public E remove(){
        E r = pollFirst();
        if(r == null) throw new NoSuchElementException();
        return r;
    }

    @Override public E removeFirst(){ return remove(); }

    @Override public E removeLast(){
        E r = pollLast();
        if(r == null) throw new NoSuchElementException();
        return r;
    }

    @Override public void push(E e){ throw new UnsupportedOperationException(); }
    @Override public E pop(){ throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator(){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator(){ throw new UnsupportedOperationException(); }

    @Override public boolean isEmpty(){ return size == 0; }

    @Override public void clear(){
        head = tail = null;
        size = 0;
    }

    @Override public boolean containsAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c){ throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public Object[] toArray(){ throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a){ throw new UnsupportedOperationException(); }
}