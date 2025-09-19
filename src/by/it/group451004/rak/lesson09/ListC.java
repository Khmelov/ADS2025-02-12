package by.it.group451004.rak.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    //LINKED LIST

    private class Node {
        public E value;
        public Node prev;
        public Node next;

        public Node(E value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node first;
    private Node last;
    private Node currentNode;
    private int currentIndex;
    private int count;

    public ListC() {
        first = null;
        last = null;
        currentNode = null;
        currentIndex = -1;
        count = 0;
    }

    public ListC(Collection<?> c) {
        first = null;
        last = null;
        currentNode = null;
        currentIndex = -1;
        count = 0;
        for (var el : c)
            add((E) el);
    }

    private void updateCurrentNode(int index) {
        if (index >= count || index < 0)
            throw new IndexOutOfBoundsException();
        //три случая
        //firstIndex currentIndex lastIndex index
        //0 5 10 : 7
        //7 - 0 = 7 шагов с начала
        //10 - 7 = 3 шага с конца
        //Abs(7-5) = 2 шага с конца

        //count как минимум 1
        int distanceFromStart = index; //всегда >= 0
        int distanceFromEnd = count - 1 - index; //всегда >= 0
        int distanceFromCurrent = Math.abs(index - currentIndex); //всегда >= 0
        if (distanceFromStart <= distanceFromEnd && distanceFromStart <= distanceFromCurrent) {
            currentIndex = 0;
            currentNode = first;
        } else if (distanceFromEnd <= distanceFromCurrent) {
            currentIndex = count - 1;
            currentNode = last;
        }
        while (currentIndex != index) {
            if (currentIndex > index) {
                currentNode = currentNode.prev;
                currentIndex--;
            } else {
                currentNode = currentNode.next;
                currentIndex++;
            }
        }
    }

    @Override
    public String toString() {
        if (count == 0) {
            return "[]";
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        currentIndex = 0;
        currentNode = first;
        for (int i = 0; i < count; i++) {
            if (i > 0) result.append(", ");
            if (currentNode.value != null) {
                result.append(currentNode.value);
            } else {
                result.append("null");
            }
            currentIndex++;
            currentNode = currentNode.next;
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public boolean add(E e) {
        add(count, e);
        return true;
    }

    @Override
    public E remove(int index) {
        updateCurrentNode(index); //исключение, если count 0
        E result = currentNode.value;

        if (currentNode.prev != null) {
            currentNode.prev.next = currentNode.next;
        } else {
            first = currentNode.next;
        }

        if (currentNode.next != null) {
            currentNode.next.prev = currentNode.prev;
        } else {
            last = currentNode.prev;
        }

        if (currentNode.next != null) {
            currentNode = currentNode.next;
        } else if (currentNode.prev != null) {
            currentNode = currentNode.prev;
            currentIndex--;
        } else {
            currentNode = null;
            currentIndex = -1;
        }

        count--;
        return result;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void add(int index, E element) {
        if (index == count) { //в том числе обработка случая пустого списка
            Node newNode = new Node(element, last, null); //last и first может быть null, если список пустой
            if (last != null)
                last.next = newNode;
            last = newNode;
            if (count == 0)
                first = last;
            count++;
        } else {
            updateCurrentNode(index);   //список содержит как минимум 1 элемент,
            //так как проверка (index >= count || index < 0)
            //currentNode не равен null в любом случае
            Node newNode = new Node(element, currentNode.prev, currentNode);
            if (currentNode.prev != null)
                currentNode.prev.next = newNode;
            currentNode.prev = newNode;
            if (index == 0)
                first = newNode;
            currentIndex++;
            count++;
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        updateCurrentNode(index);
        E result = currentNode.value;
        currentNode.value = element;
        return result;
    }


    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void clear() {
        while (!isEmpty())
            remove(0);
    }

    @Override
    public int indexOf(Object o) {
        if (isEmpty())
            return -1;
        currentNode = first;
        currentIndex = 0;

        do {
            if (currentNode.value.equals(o))
                return currentIndex;
            currentIndex++;
            currentNode = currentNode.next;
        } while (currentNode != null);
        currentNode = first;
        currentIndex = 0;
        return -1;
    }

    @Override
    public E get(int index) {
        updateCurrentNode(index);
        return currentNode.value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (isEmpty())
            return -1;
        currentNode = last;
        currentIndex = count - 1;

        do {
            if (currentNode.value.equals(o))
                return currentIndex;
            currentIndex--;
            currentNode = currentNode.prev;
        } while (currentNode != null);
        currentNode = last;
        currentIndex = count - 1;
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var element : c)
            if (!contains(element))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (var element : c)
            add(element);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (var element : c)
            add(index++, element); //что-то неправильное
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try {
            for (int i = count - 1; i >= 0; i--) {
                if (!c.contains(get(i)))
                    remove(i);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= count)
            throw new IndexOutOfBoundsException();
        if (toIndex < 0 || toIndex >= count)
            throw new IndexOutOfBoundsException();

        List<E> result = new ListC<E>();
        for (int i = fromIndex; i <= toIndex; i++) {
            result.add(get(i));
        }

        return result;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < count) {
            return (T[]) (toArray());
        } else {
            for (int i = 0; i < a.length; i++) {
                if (i < count) {
                    a[i] = (T)get(i);
                } else {
                    a[i] = null;
                }
            }
            return a;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[count];
        for (int i = 0; i < count; i++)
            arr[i] = get(i);
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int pointer = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return pointer < count;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = pointer;
            return get(pointer++);
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastRet); // вызываем remove у твоего списка
            pointer = lastRet; // сдвигаем курсор назад
            lastRet = -1;
        }
    }

    private class ListItr implements ListIterator<E> {

        int pointer;
        int lastRet = -1;

        public ListItr(int index) {
            pointer = index;
        }

        @Override
        public boolean hasNext() {
            return pointer < count && pointer >= 0;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = pointer;
            return get(pointer++);
        }

        @Override
        public boolean hasPrevious() {
            return pointer < count && pointer > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            lastRet = --pointer;
            return get(pointer);
        }

        @Override
        public int nextIndex() {
            return pointer;
        }

        @Override
        public int previousIndex() {
            return pointer - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            if (lastRet < pointer) pointer--;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(pointer++, e);
            lastRet = -1;
        }
    }
}
