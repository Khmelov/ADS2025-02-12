package by.it.group410902.podryabinkin.lesson10;

import java.util.*;

public class MyPriorityQueue <E extends Comparable<E>> implements Queue<E> {

    private Comparator<E> comp = Comparator.naturalOrder();

    private E[] heap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, 0);


    private int size = 0;

    //немного переписал 3lesson
    public void go_up(){
        int i = size - 1;
        swift_up(i);
        /*while (i > 0 && comp.compare(heap[i], heap[(i - 1) / 2]) < 0) {
            int p = (i - 1) / 2;
            E tmp = heap[p];
            heap[p] = heap[i];
            heap[i] = tmp;
            i = p;
        } */
    }
    public void swift_up(int i) {
        E x = heap[i];
        while (i > 0) {
            int parent = (i - 1) / 2;  // родитель
            if (comp.compare(x, heap[parent]) >= 0) {
                break;
            }
            heap[i] = heap[parent];
            i = parent;
        }
        heap[i] = x;
    }
    /*public void swift_up(int i){
        while (i > 0 && comp.compare(heap[i], heap[(i - 1) / 2]) < 0) {
            int p = (i - 1) / 2;
            E tmp = heap[p];
            heap[p] = heap[i];
            heap[i] = tmp;
            i = p;
        }
    }
    */
    public void go_down(){
        int i = 0;
        swift_down(i);
        /*while (true){
            int left = 2*i + 1;
            int right = 2*i + 2;
            int largest = i;

            if (left < size && comp.compare(heap[left], heap[largest]) < 0) largest = left;
            if (right < size && comp.compare(heap[right], heap[largest]) < 0) largest = right;

            if (largest != i) {
                E tmp = heap[i];
                heap[i] = heap[largest];
                heap[largest] = tmp;
                i = largest;
            } else {
                break;
            }
        }
        */


    }
    //тот же go down, только для произвольного элемента
    public void swift_down(int i) {
        E x = heap[i];
        int half = size / 2;
        //пускай, как в JDK считает только нужную половину
        while (i < half) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;
            if (right < size && comp.compare(heap[left], heap[right]) > 0) {
                smallest = right;
            }
            if (comp.compare(x, heap[smallest]) <= 0) {
                break;
            }
            heap[i] = heap[smallest];
            i = smallest;
        }
        heap[i] = x;
    }
    /*public void swift_down(int i){
        while (true){
            int left = 2*i + 1;
            int right = 2*i + 2;
            int largest = i;


            if (left < size && comp.compare(heap[left], heap[largest]) < 0) largest = left;
            if (right < size && comp.compare(heap[right], heap[largest]) < 0) largest = right;


            if (largest != i) {
                E tmp = heap[i];
                heap[i] = heap[largest];
                heap[largest] = tmp;
                i = largest;
            } else {
                break;
            }
        }
    }
    */
    public void insert(E element){
        size++;
        E[] arr2 = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size+1);
        for(int i = 0; i < size - 1; i++){
            arr2[i] = heap[i];
        }
        arr2[size-1] = element;
        heap = arr2;
        go_up();
    }
    public void rem_el(){
        if(size == 0) return;

        E[] arr2 = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size - 1);;
        arr2[0] = heap[size - 1];
        size--;
        for(int i = 1; i < size; i++){
            arr2[i] = heap[i];
        }
        heap = arr2;
        go_down();

    }


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
        E tmp = heap[0];
        rem_el();
        return tmp;
    }

    private boolean containsRecursive(E element, int index) {


        if (index >= size) return false;
        if (heap[index] == null) return false;
        if (heap[index].equals(element)) return true;
        // Для безопасности проверяем обе ветви, т.к. элемент может быть где угодно
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
        if(add(element)) return true;
        return false;
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
        return heap[0];
    }
    @Override
    public E element(){
        return peek();
    }
    @Override
    public boolean isEmpty(){
        if(size == 0) return true;
        return false;
    }



    @Override
    public boolean containsAll(Collection<?> c){
        for (Object obj : c) {
            E element = (E) obj;
            if (!contains(element)) return false;
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        if(c.size() == 0) return false;
        for (E element : c) {
            add(element);
        }
        return true;
    }

    /*@Override
    public boolean removeAll(Collection<?> c) {
        boolean ch = false;

        for (Object obj : c) {
            while (remove(obj)) { // удаляем все вхождения
                ch = true;
            }
        }

        return ch;
    }
    */

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                E moved = heap[--size];  // последний элемент
                heap[size] = null;

                if (i != size) {
                    heap[i] = moved;
                    swift_down(i); // сначала вниз
                    if (heap[i] == moved) {
                        swift_up(i); // если не сдвинулся вниз, пробуем вверх
                    }
                }
                return true;
            }
        }
        return false;
    }


    /*public boolean removeAll(Collection<?> c){
        boolean allgood = true;
        for (Object obj : c) {
            E element = (E) obj;
            if(size == 0) return false;
            boolean found = false;
            for(int i = 0; i < size; i++){
                if(element == heap[i]){
                    E tmp = heap[0];
                    heap[0] = heap[i];
                    heap[i] = tmp;
                    remove();
                    go_up();
                    found = true;
                    break;
                }
            }
            if(!found) allgood = false;
        }
        return allgood;
    }
    */
    /*@Override
    public boolean retainAll(Collection<?> c) {


        Objects.requireNonNull(c);
        boolean modified = false;

        // Эффективная реализация O(n)
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся элементы
        Arrays.fill(heap, newSize, size, null);
        size = newSize;

        // Перестраиваем кучу
        if (modified) {
            for (int i = size / 2 - 1; i >= 0; i--) {
                swift_down(i);
            }
        }

        return modified;
    }
    */

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Новый массив для оставшихся элементов
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

        // Полностью перестраиваем кучу
        for (int i = size / 2 - 1; i >= 0; i--) {
            swift_down(i);
        }

        return modified;
    }

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

        // Полностью перестраиваем кучу
        for (int i = size / 2 - 1; i >= 0; i--) {
            swift_down(i);
        }

        return modified;
    }


    /*public boolean retainAll(Collection<?> c){
        boolean have_ret = false;
        E[] arr2 = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, c.size());
        int counter = 0;
        for (Object obj : c) {
            E element = (E) obj;
            for(int i = 0; i < size; i++){
                if(heap[i] != null){
                    if(element == heap[i]){
                        arr2[counter] = heap[i];
                        heap[i] = null;
                        counter++;
                        have_ret = true;
                        break;
                    }
                }
            }
        }
        clear();
        int count = 0;
        for(; count < arr2.length; count++){
            if(arr2[count] == null) break;
        }

        size = count;
        heap = (E[]) java.lang.reflect.Array.newInstance(Comparable.class, size);

        for (int i = 0; i < size; i++) {
            heap[i] = arr2[i];
        }

        // делаем heapify (O(n)), чтобы порядок был как у PriorityQueue
        for (int i = size / 2 - 1; i >= 0; i--) {
            swift_down(i);
        }

        return have_ret;
    }
    */








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
