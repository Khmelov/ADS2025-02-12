package by.it.group410902.derzhavskaya_e.lesson10;

import java.util.*;

//Реализация приоритетной очереди (минимальной кучи)

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    // Компаратор для сравнения элементов
    private Comparator<E> comp = Comparator.naturalOrder();

    // Основное хранилище кучи
    private E[] heap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, 0);

    // Текущее количество элементов в куче
    private int size = 0;

// вспомогательные методы

    // Метод поднимает последний добавленный элемент вверх (восстанавливает свойство кучи)
    public void go_up(){
        int i = size - 1;
        swift_up(i);
    }

    // Просеивание вверх: элемент под индексом i поднимается, пока не окажется на нужном месте
    public void swift_up(int i) {
        E x = heap[i];
        while (i > 0) {
            int parent = (i - 1) / 2; // индекс родителя
            // если родитель меньше или равен — останавливаемся
            if (comp.compare(x, heap[parent]) >= 0) {
                break;
            }
            heap[i] = heap[parent];
            i = parent;
        }
        heap[i] = x;
    }

    // Просеивание вниз от корня (после удаления элемента)
    public void go_down(){
        int i = 0;
        swift_down(i);
    }

    // Просеивание вниз от произвольного индекса i
    public void swift_down(int i) {
        E x = heap[i];
        int half = size / 2; // не трогаем листья
        while (i < half) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;
            // выбираем меньшего потомка
            if (right < size && comp.compare(heap[left], heap[right]) > 0) {
                smallest = right;
            }
            // если родитель меньше потомков — куча в порядке
            if (comp.compare(x, heap[smallest]) <= 0) {
                break;
            }
            heap[i] = heap[smallest];
            i = smallest;
        }
        heap[i] = x;
    }



    // Вставка нового элемента в кучу
    public void insert(E element){
        size++;
        // создаем новый массив на 1 элемент больше
        E[] arr2 = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size+1);
        // копируем старые элементы
        for(int i = 0; i < size - 1; i++){
            arr2[i] = heap[i];
        }
        // вставляем новый элемент в конец
        arr2[size-1] = element;
        heap = arr2;
        go_up(); // восстанавливаем структуру кучи
    }

    // Удаление минимального элемента (корня кучи)
    public void rem_el(){
        if(size == 0) return;

        // создаем новый массив на 1 меньше
        E[] arr2 = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size - 1);
        // перемещаем последний элемент в корень
        arr2[0] = heap[size - 1];
        size--;
        // копируем остальные элементы
        for(int i = 1; i < size; i++){
            arr2[i] = heap[i];
        }
        heap = arr2;
        go_down(); // восстанавливаем кучу
    }

// то, что просили реализовать

    @Override
    public String toString(){
        if(size == 0) return "[]";
        String out = "[";
        for(int i = 0; i < size(); i++){
            if(i != size()-1) out += heap[i] + ", ";
            else out += heap[i];
        }
        out += "]";
        return out;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void clear(){
        heap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, 0);
        size = 0;
    }


    @Override
    public boolean add(E element){
        insert(element);
        return true;
    }

    @Override
    public E remove(){
        if(size == 0) return null;
        E tmp = heap[0]; // сохраняем минимум
        rem_el();        // удаляем его
        return tmp;
    }

    // Рекурсивный поиск элемента в куче
    private boolean containsRecursive(E element, int index) {
        if (index >= size) return false;
        if (heap[index] == null) return false;
        if (heap[index].equals(element)) return true;
        // проверяем обе ветви рекурсивно
        return containsRecursive(element, 2*index + 1) || containsRecursive(element, 2*index + 2);
    }

    @Override
    public boolean contains(Object obj) {
        if(obj == null) return false;
        E element = (E) obj;
        return containsRecursive(element, 0);
    }

    @Override
    public boolean offer(E element){
        return add(element);
    }

    @Override
    public E poll(){
        if(size == 0) return null;
        E tmp = heap[0];
        remove();
        return tmp;
    }

    @Override
    public E peek(){
        if(size == 0) return null;
        return heap[0]; // минимальный элемент
    }

    @Override
    public E element(){
        return peek();
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }


    // Проверяет, содержатся ли все элементы коллекции c в очереди
    @Override
    public boolean containsAll(Collection<?> c){
        for (Object obj : c) {
            E element = (E) obj;
            if (!contains(element)) return false;
        }
        return true;
    }

    // Добавляет все элементы из коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c){
        if(c.size() == 0) return false;
        for (E element : c) {
            add(element);
        }
        return true;
    }

    // Удаляет конкретный элемент из очереди (по значению)
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                E moved = heap[--size];  // берем последний элемент
                heap[size] = null;

                if (i != size) {
                    heap[i] = moved;
                    swift_down(i); // пробуем опустить вниз
                    if (heap[i] == moved) {
                        swift_up(i); // если не сдвинулся — поднимаем вверх
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Удаляет все элементы, содержащиеся в коллекции c
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        E[] newHeap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size);
        int idx = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[idx++] = heap[i];
            } else {
                modified = true;
            }
        }

        heap = Arrays.copyOf(newHeap, idx);
        size = idx;

        // пересобираем кучу заново
        for (int i = size / 2 - 1; i >= 0; i--) {
            swift_down(i);
        }

        return modified;
    }

    // Оставляет только элементы, которые есть в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        E[] newHeap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size);
        int idx = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[idx++] = heap[i];
            } else {
                modified = true;
            }
        }

        heap = Arrays.copyOf(newHeap, idx);
        size = idx;

        // пересобираем кучу
        for (int i = size / 2 - 1; i >= 0; i--) {
            swift_down(i);
        }

        return modified;
    }

// заглушки

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
