package lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] elements = (E[])(new Object[10]);
    int size = 0;
   @Override
   public String toString()
   {
       StringBuilder sb = new StringBuilder("[");
       var it = iterator();
       if(it.hasNext())
           sb.append(it.next());
       while (it.hasNext())
       {
           sb.append(", ");
           sb.append(it.next());
       }
       sb.append("]");
       return sb.toString();
   }
   private int getParent(int index)
   {
       return (index - 1) / 2;
   }
   private int getLeft(int index)
   {
       return index * 2 + 1;
   }
   private  int getRight(int index)
   {
       return index * 2 + 2;
   }
   private void siftUp()
   {
      int pointer = size - 1;
      int parent = getParent(pointer);
      E temp = null;
      while (((Comparable<E>)(elements[pointer])).compareTo(elements[parent]) < 0)
      {
          temp = elements[pointer];
          elements[pointer] = elements[parent];
          elements[parent] = temp;
          pointer = parent;
          parent = getParent(pointer);
      }
   }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (E e : this) {
            if (Objects.equals(o, e))
                return true;
        }
        return false;
    }

    private class MyIterator implements Iterator<E> {

        int cursor = 0;
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            return elements[cursor++];
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
    private void grow()
    {
        E[] newElements = (E[])(new Object[elements.length * 2]);
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
    @Override
    public boolean add(E e) {

       if(size == elements.length)
           grow();
       elements[size++] = e;
       siftUp();
       return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
       boolean res = true;
       for (Object o : c) {
           res = res && contains(o);
       }
       return res;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
       for(E o : c){
           add(o);
       }
       return !c.isEmpty();
    }

    private void heapify()
    {
        for(int i = (size-1) / 2; i >= 0; i--)
        {
            siftDown(i);
        }
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        E[] newElements = (E[])(new Object[elements.length]);
        int oldSize = size;
        size = 0;
        for(int i = 0; i < oldSize; i++) {
            if (!c.contains(elements[i])) {
                newElements[size++] = (E) elements[i];
            }
        }
        elements = newElements;
        heapify();
        return oldSize != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        E[] newElements = (E[])(new Object[elements.length]);
        int oldSize = size;
        size = 0;
        for(int i = 0; i < oldSize; i++) {
            if (c.contains(elements[i])) {
                newElements[size++] = (E) elements[i];
            }
        }
        elements = newElements;
        heapify();
        return oldSize != size;
    }

    @Override
    public void clear() {
        elements = (E[])(new Object[10]);
        size = 0;
    }

    @Override
    public boolean offer(E e) {
       if(size == elements.length)
           grow();
       elements[size++] = e;
       siftUp();
       return true;
    }
    private void siftDown()
    {
        siftDown(0);
    }
    private void siftDown(int pointer)
    {
        if (pointer >= size)
            return;
        int lchild = getLeft(pointer);
        int rchild = getRight(pointer);
        int maxChild = pointer;
        if(lchild < size && ((Comparable<E>)(elements[pointer])).compareTo(elements[lchild]) > 0)
            maxChild = lchild;
        if(rchild < size && ((Comparable<E>)(elements[maxChild])).compareTo(elements[rchild]) > 0)
            maxChild = rchild;
        if(maxChild != pointer)
        {
            E temp = elements[pointer];
            elements[pointer] = elements[maxChild];
            elements[maxChild] = temp;
            siftDown(maxChild);
        }

    }
    @Override
    public E remove() {
       if(size == 0)
           throw new NoSuchElementException();
       E ret = elements[0];
       elements[0] = elements[--size];
       siftDown();
       return ret;
    }

    @Override
    public E poll() {
       if(size == 0)
           return null;
        return remove();
    }

    @Override
    public E element() {
        if(size == 0)
            throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E peek() {
        if(size == 0)
            return null;
        return elements[0];
    }
}
