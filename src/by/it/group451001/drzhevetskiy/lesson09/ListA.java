    package by.it.group451001.drzhevetskiy.lesson09;

    import java.util.Collection;
    import java.util.Iterator;
    import java.util.List;
    import java.util.ListIterator;

    public class ListA<E> implements List<E> {

        //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

        /////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////
        //////               Обязательные к реализации методы             ///////
        /////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////
        ///

        private Object[] elements = new Object[10]; // начальная ёмкость
        private int size = 0;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(elements[i]);
                if (i < size - 1) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }


        @Override
        public boolean add(E e) {
            if (size == elements.length) {
                Object[] newArr = new Object[elements.length * 2];
                System.arraycopy(elements, 0, newArr, 0, elements.length);
                elements = newArr;
            }
            elements[size++] = e;
            return true;
        }


        @Override
        @SuppressWarnings("unchecked")
        public E remove(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
            E removed = (E) elements[index];
            int shift = size - index - 1;
            if (shift > 0) {
                System.arraycopy(elements, index + 1, elements, index, shift);
            }
            elements[--size] = null; // очистим хвост
            return removed;
        }


        @Override
        public int size() {
            return size;
        }


        /////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////
        //////               Опциональные к реализации методы             ///////
        /////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////

        @Override
        public void add(int index, E element) {

        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E set(int index, E element) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
            E old = (E) elements[index];
            elements[index] = element;
            return old;
        }



        @Override
        public boolean isEmpty() {
            return false;
        }


        @Override
        public void clear() {

        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E get(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
            return (E) elements[index];
        }


        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

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
