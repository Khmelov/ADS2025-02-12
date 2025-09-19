package by.it.group451004.struts.lesson09;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    //пжлст, не воруйте лабу, я на неё реально потратил 8+ часов ;(

    private static class ListElement<E> {
        ListElement<E> next = null;
        ListElement<E> prev = null;

        E item;

        public ListElement(E element) {
            item = element;
        }
    }

    public interface MyListIterator<E> extends ListIterator<E> {
        E get();
        void jumpInList(int index);
    }

    private ListElement<E> first = null;
    private ListElement<E> last = null;

    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        ListElement<E> current = first;
        builder.append("[");
        for (int i = 0; i < size - 1; i++) {
            builder.append(current.item.toString()).append(", ");
            current = current.next;
        }
        if (size > 0)
            builder.append(current.item);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean add(E e) {
        ListElement<E> newElement = new ListElement<>(e);
        if (first == null) {
            first = newElement;
        } else {
            last.next = newElement;
            newElement.prev = last;
        }
        last = newElement;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < -1)
            throw new IndexOutOfBoundsException();
        if (index >= size)
            throw new IndexOutOfBoundsException();

        Iterator<E> iterator = iterator();
        E item = null;
        for (int i = 0; i < index; i++) {
            if (!iterator.hasNext())
                throw new IndexOutOfBoundsException();
            item = iterator.next();
        }

        iterator.remove();
        return item;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < -1)
            throw new IndexOutOfBoundsException();
        if (index >= size)
            throw new IndexOutOfBoundsException();

        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        iterator.add(element);
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = iterator();
        E item = iterator.next();
        while (iterator.hasNext() && !o.equals(item)) {
            item = iterator.next();
        }
        if (iterator.hasNext() || o.equals(item)) {
            iterator.remove();
            return true;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < -1)
            throw new IndexOutOfBoundsException();
        if (index >= size)
            throw new IndexOutOfBoundsException();

        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        E oldItem = iterator.get();
        for (int i = 0; i < index; i++) {
            oldItem = iterator.next();
        }
        iterator.set(element);

        return oldItem;
    }


    @Override
    public boolean isEmpty() {
        return (size == 0);
    }


    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        if (first.item == o)
            return 0;
        int index = 0;
        while (iterator.hasNext()) {
            E item = iterator.next();
            index++;
            if (o.equals(item))
                return index;
        }

        return -1;
    }

    @Override
    public E get(int index) {
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator(index);
        return iterator.get();
    }

    @Override
    public boolean contains(Object o) {
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        E item = iterator.get();
        if (o.equals(item))
            return true;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (o.equals(item))
                return true;
        }

        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        ListElement<E> element = last;
        if (o.equals(element.item))
            return size - 1;
        int index = size - 1;
        while (element.prev != null) {
            element = element.prev;
            index--;
            if (o.equals(element.item))
                return index;
        }

        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed = add(e) || changed;
        }

        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            add(index, e);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed = remove(o) || changed;
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        boolean changed = false;
        do {
            if (c.contains(iterator.get())) {
                iterator.remove();
                changed = true;
            } else {
                iterator.next();
            }
        } while (iterator.hasNext());

        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> result = new ListA<>();
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator(fromIndex);
        for (int i = 0; i < toIndex - fromIndex; i++) {
            result.add(iterator.get());
        }

        return result;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.generateListIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.generateListIterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        T[] array = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        while (iterator.hasNext()) {
            array[iterator.nextIndex()] = (T) iterator.next();
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        MyListIterator<E> iterator = (MyListIterator<E>) listIterator();
        array[0] = iterator.get();
        while (iterator.hasNext()) {
            array[iterator.nextIndex()] = iterator.next();
        }

        return array;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return this.generateListIterator();
    }

    private MyListIterator<E> generateListIterator() {
        return generateListIterator(-1);
    }

    private MyListIterator<E> generateListIterator(int index) {
        MyListIterator<E> iterator = new MyListIterator<E>() {
            ListElement<E> current = first;
            int index = 0;

            @Override
            public E get() {
                return current.item;
            }

            @Override
            public void jumpInList(int index) {
                if (current != first)
                    current = first;

                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            }

            @Override
            public boolean hasNext() {
                return current != null && current.next != null;
            }

            @Override
            public E next() {
                current = current.next;
                index++;
                return current.item;
            }

            @Override
            public boolean hasPrevious() {
                return current != null && current.prev != null;
            }

            @Override
            public E previous() {
                current = current.prev;
                index--;
                return current.item;
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                if (current == first) {
                    first = first.next;
                }
                if (current == last) {
                    last = last.prev;
                }

                if (current.prev != null) {
                    current.prev.next = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }
                size--;
            }

            @Override
            public void set(E e) {
                current.item = e;
            }

            @Override
            public void add(E e) {
                ListElement<E> newElement = new ListElement<>(e);

                newElement.next = current;
                newElement.prev = current.prev;
                current.prev = newElement;
                newElement.prev.next = newElement;

                if (last == current) {
                    last = newElement;
                }

                size++;
            }
        };
        if (index > 0)
            iterator.jumpInList(index);
        return iterator;
    }
}
