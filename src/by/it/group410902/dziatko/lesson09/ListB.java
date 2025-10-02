package by.it.group410902.dziatko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private List_Node<E> start = null;

    @Override
    public String toString() {
        if(this.start != null){
            String to_str = "[" + this.start.get_value().toString();
            List_Node<E> temp = this.start.get_next();
            while (temp != null) {
                try {
                    to_str += ", " + temp.get_value().toString();
                    temp = temp.get_next();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            to_str += "]";
            return to_str;
        }else {
            return "[]";
        }
    }

    @Override
    public boolean add(E e) {
        if(this.start != null){
            List_Node<E> temp = this.start;
            while(temp.is_next_not_empty()){
                temp = temp.get_next();
            }
            temp.set_next(new List_Node<E>(e));
        }else{
            this.start = new List_Node<E>(e);
        }
        return true;
    }

    @Override
    public E remove(int index) {
        if(index < this.size() && index > -1) {
            List_Node<E> temp = this.start;
            List_Node<E> prev = null;
            for (int i = 0; i < index; i++) {
                prev = temp;
                temp = temp.get_next();
            }
            if(temp.is_next_not_empty()){
                prev.set_next(temp.get_next());
            }else{
                prev.set_next(null);
            }
            return temp.get_value();
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        List_Node<E> temp = this.start;
        if(temp != null){
            int size_of_list = 1;
            while(temp.is_next_not_empty()){
                size_of_list++;
                temp = temp.get_next();
            }
            return size_of_list;
        }else{
            return 0;
        }
    }

    @Override
    public void add(int index, E element) {
        if(index < this.size() && index > -1) {
            List_Node<E> temp = this.start;
            List_Node<E> prev = null;
            for (int i = 0; i < index; i++) {
                prev = temp;
                temp = temp.get_next();
            }
            if(prev != null) {
                prev.set_next(new List_Node<E>(element));
                prev.get_next().set_next(temp);
            }else{
                this.start = new List_Node<E>(element);
                this.start.set_next(temp);
            }
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = false;
        if(!this.start.get_value().equals(o)) {
            List_Node<E> temp = this.start.get_next();
            List_Node<E> prev = this.start;
            while (temp != null) {
                if (temp.get_value().equals(o)) {
                    prev.set_next(temp.get_next());
                    removed = true;
                    break;
                }
                prev = temp;
                temp = temp.get_next();
            }
        }else{
            this.start = this.start.get_next();
            removed = true;
        }
        return removed;
    }

    @Override
    public E set(int index, E element) {
        if(index < this.size() && index > -1){
            List_Node<E> temp = this.start;
            for (int i = 0; i < index; i++) {
                temp = temp.get_next();
            }
            E prev_val = temp.get_value();
            temp.set_value(element);
            return prev_val;
        }else{
            return null;
        }

    }


    @Override
    public boolean isEmpty() {
        return this.start == null;
    }


    @Override
    public void clear() {
        this.start = null;
    }

    @Override
    public int indexOf(Object o) {
        if(this.start != null){
            int ind = 0, size = this.size();
            List_Node<E> temp = this.start;
            while (ind < size && !temp.get_value().equals(o)) {
                ind++;
                temp = temp.get_next();
            }
            if(ind == size){
                return -1;
            }else {
                return ind;
            }
        }else{
            return -1;
        }
    }

    @Override
    public E get(int index) {
        if(index < this.size() && index > -1){
            List_Node<E> temp = this.start;
            for (int i = 0; i < index; i++) {
                temp = temp.get_next();
            }
            return temp.get_value();
        }else {
            return null;
        }
    }

    @Override
    public boolean contains(Object o) {
        int size = this.size();
        List_Node<E> temp = this.start;
        for (int i = 0; i < size; i++) {
            if(temp.get_value().equals(o)){
                return true;
            }
            temp = temp.get_next();
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        int size = this.size(), index = -1;
        List_Node<E> temp = this.start;
        for (int i = 0; i < size; i++) {
            if(temp.get_value().equals(o)){
                index = i;
            }
            temp = temp.get_next();
        }
        return index;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


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
