package by.it.group410901.kalach.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    // Внутренний статический класс для узлов списка
    private static class Node<E> {
        E v;                // Значение элемента
        Node<E> prev;       // Ссылка на предыдущий узел
        Node<E> next;       // Ссылка на следующий узел

        // Конструктор узла
        Node(E v){ this.v=v; }
    }

    // Поля класса
    private Node<E> head;   // Первый элемент списка
    private Node<E> tail;   // Последний элемент списка
    private int size;       // Количество элементов в списке

    // Конструктор по умолчанию
    public MyLinkedList(){}

    // Преобразование списка в строку для вывода
    @Override
    public String toString(){
        if(size==0) return "[]";  // Пустой список
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> n = head;
        // Проходим по всем элементам списка
        while(n!=null){
            sb.append(String.valueOf(n.v));
            n = n.next;
            if(n!=null) sb.append(", ");  // Добавляем запятую, если не последний элемент
        }
        sb.append(']');
        return sb.toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E element){
        addLast(element);
        return true;
    }

    // Удаление элемента по индексу
    public E remove(int index){
        // Проверка корректности индекса
        if(index<0 || index>=size) throw new IndexOutOfBoundsException();
        Node<E> cur;

        // Оптимизация: ищем с начала или с конца в зависимости от позиции
        if(index < (size>>1)){  // size>>1 = size/2 (битовый сдвиг)
            // Ищем с начала списка
            cur = head;
            for(int i=0;i<index;i++) cur = cur.next;
        } else {
            // Ищем с конца списка
            cur = tail;
            for(int i=size-1;i>index;i--) cur = cur.prev;
        }
        E val = cur.v;
        unlink(cur);  // Удаляем узел
        return val;
    }

    // Удаление первого найденного элемента по значению
    @Override
    public boolean remove(Object o){
        Node<E> n = head;
        // Проходим по всем элементам списка
        while(n!=null){
            // Сравниваем значения (учитываем случай null)
            if(o==null ? n.v==null : o.equals(n.v)){
                unlink(n);  // Удаляем узел
                return true;
            }
            n = n.next;
        }
        return false;  // Элемент не найден
    }

    // Возвращает количество элементов в списке
    @Override
    public int size(){ return size; }

    // Добавление элемента в начало списка
    @Override
    public void addFirst(E element){
        if(element==null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if(size==0){
            // Если список пустой, новый элемент становится и головой и хвостом
            head = tail = n;
        } else {
            // Вставляем новый элемент перед текущей головой
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    // Добавление элемента в конец списка
    @Override
    public void addLast(E element){
        if(element==null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if(size==0){
            // Если список пустой, новый элемент становится и головой и хвостом
            head = tail = n;
        } else {
            // Вставляем новый элемент после текущего хвоста
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
        size++;
    }

    // Получение первого элемента без удаления (бросает исключение если пусто)
    @Override
    public E element(){ return getFirst(); }

    // Получение первого элемента
    @Override
    public E getFirst(){
        if(size==0) throw new NoSuchElementException();
        return head.v;
    }

    // Получение последнего элемента
    @Override
    public E getLast(){
        if(size==0) throw new NoSuchElementException();
        return tail.v;
    }

    // Извлечение первого элемента с удалением (возвращает null если пусто)
    @Override
    public E poll(){ return pollFirst(); }

    // Извлечение первого элемента
    @Override
    public E pollFirst(){
        if(size==0) return null;
        E v = head.v;
        unlink(head);  // Удаляем головной элемент
        return v;
    }

    // Извлечение последнего элемента
    @Override
    public E pollLast(){
        if(size==0) return null;
        E v = tail.v;
        unlink(tail);  // Удаляем хвостовой элемент
        return v;
    }

    // Внутренний метод для удаления узла из списка
    private void unlink(Node<E> n){
        Node<E> p = n.prev;  // Предыдущий узел
        Node<E> q = n.next;  // Следующий узел

        // Обновляем ссылки соседних узлов
        if(p==null)
            head = q;        // Если удаляем голову, обновляем голову
        else
            p.next = q;      // Иначе предыдущий узел ссылается на следующий

        if(q==null)
            tail = p;        // Если удаляем хвост, обновляем хвост
        else
            q.prev = p;      // Иначе следующий узел ссылается на предыдущий

        // Очищаем ссылки удаляемого узла (помощь сборщику мусора)
        n.prev = n.next = null;
        n.v = null;
        size--;

        // Если список стал пустым, сбрасываем ссылки
        if(size==0){ head = tail = null; }
    }

    @Override public boolean offerFirst(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e){ throw new UnsupportedOperationException(); }
    @Override public E peek(){ throw new UnsupportedOperationException(); }
    @Override public E peekFirst(){ throw new UnsupportedOperationException(); }
    @Override public E peekLast(){ throw new UnsupportedOperationException(); }
    // Удаление с исключением если пусто
    @Override public E remove(){
        E r = pollFirst();
        if(r==null) throw new NoSuchElementException();
        return r;
    }
    @Override public E removeFirst(){ return remove(); }
    @Override public E removeLast(){
        E r = pollLast();
        if(r==null) throw new NoSuchElementException();
        return r;
    }
    @Override public void push(E e){ throw new UnsupportedOperationException(); }
    @Override public E pop(){ throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator(){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator(){ throw new UnsupportedOperationException(); }
    // Проверка на пустоту
    @Override public boolean isEmpty(){ return size==0; }
    // Очистка списка
    @Override public void clear(){ head = tail = null; size = 0; }
    @Override public boolean containsAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c){ throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public Object[] toArray(){ throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a){ throw new UnsupportedOperationException(); }
}