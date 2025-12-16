package by.it.group410902.dziatko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

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
            if(index == 0){
                this.start = temp.get_next();
                return temp.get_value();
            }
            List_Node<E> prev = null;
            for (int i = 0; i < index; i++) {
                prev = temp;
                temp = temp.get_next();
            }
            prev.set_next(temp.get_next());
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
        if(!this.start.get_value().equals(o)) {
            List_Node<E> temp = this.start.get_next();
            List_Node<E> prev = this.start;
            while (temp != null) {
                if (temp.get_value().equals(o)) {
                    prev.set_next(temp.get_next());
                    return true;
                }
                prev = temp;
                temp = temp.get_next();
            }
        }else{
            this.start = this.start.get_next();
            return true;
        }
        return false;
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

    @Override
    public boolean containsAll(Collection<?> c) {
        if(this.start != null) {
            for (Object e : c) {
                List_Node<E> temp = this.start;
                while (temp != null && !temp.get_value().equals(e)) {
                    temp = temp.get_next();
                }
                if(temp == null){
                    return false;
                }
            }
            return true;
        }else{
            return c.isEmpty();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()){
            return false;
        }
        if(this.start != null){
           List_Node<E> temp = this.start;
           while(temp.is_next_not_empty()){
               temp = temp.get_next();
           }
           for(E element : c) {
               temp.set_next(new List_Node<E>(element));
               temp = temp.get_next();
           }
        }else{
            List_Node<E> temp = null;
            boolean first = true;
            for(E element : c){
                if(first) {
                    this.start = new List_Node<E>(element);
                    first = false;
                    temp = this.start;
                }else{
                    temp.set_next(new List_Node<E>(element));
                    temp = temp.get_next();
                }
            }
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (index < 0 || index > this.size()) throw new IndexOutOfBoundsException();
        if (c.isEmpty()) return false;

        List_Node<E> new_start = null;
        List_Node<E> new_finish = null;
        for (E ele : c) {
            List_Node<E> temp = new List_Node<>(ele);
            if (new_start == null) {
                new_start = temp;
                new_finish = temp;
            } else {
                new_finish.set_next(temp);
                new_finish = temp;
            }
        }

        if (this.start == null) {
            this.start = new_start;
        } else if (index == 0) {
            new_finish.set_next(this.start);
            this.start = new_start;
        } else {
            List_Node<E> prev = this.start;
            for (int i = 1; i < index; i++) {
                prev = prev.get_next();
            }
            List_Node<E> next = prev.get_next();
            prev.set_next(new_start);
            new_finish.set_next(next);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        if(this.start != null){
            for (Object ob : c) {
                List_Node<E> temp = this.start;
                List_Node<E> prev = null;
                while (temp != null) {
                    if(temp.get_value().equals(ob)){
                        removed = true;
                        if(prev == null){
                            this.start = temp.get_next();
                        }else {
                            prev.set_next(temp.get_next());
                        }
                        temp = temp.get_next();
                    }else {
                        prev = temp;
                        temp = temp.get_next();
                    }
                }
            }
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(this.start != null){
            List_Node<E> temp = this.start;
            List_Node<E> prev = null;
            boolean in_here = false, all_here = true;
            while(temp != null){
                for(Object ob : c){
                    if(temp.get_value().equals(ob)){
                        in_here = true;
                        break;
                    }
                }
                if(!in_here){
                    all_here =false;
                    if(temp==this.start){
                        this.start = this.start.get_next();
                    }else{
                        prev.set_next(temp.get_next());
                    }
                }else {
                    in_here=false;
                    prev = temp;
                }
                temp = temp.get_next();
            }
            return !all_here;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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

class List_Node<E>{
    private E value = null;
    private List_Node<E> next = null;
    public List_Node(E val, List_Node<E> link){
        this.value = val;
        this.next = link;
    }
    public List_Node(E val){
        this.value = val;
    }
    public List_Node(){}
    public E get_value(){
        return this.value;
    }
    public void set_value(E e){
        this.value = e;
    }
    public boolean is_next_not_empty(){
        return this.next != null;
    }
    public List_Node<E> get_next(){
        return this.next;
    }
    public void set_next(List_Node<E> node){
        this.next = node;
    }
}
