package by.it.group410902.dziatko.lesson11;

import by.it.group410902.dziatko.lesson10.MyLinkedList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private LinkedHashSet_Node<E>[] Internal_list;
    private int Size_of_array = 16, Number_of_elements = 0;//base length of an hashset
    private double Load_factor = 0.75;//base load factor in hashset
    private LinkedHashSet_Node<E> first = null, last = null;

    public MyLinkedHashSet(){
        this.Internal_list = new LinkedHashSet_Node[this.Size_of_array];
    }

    public MyLinkedHashSet(int size_of_HashSet){
        this.Size_of_array = size_of_HashSet;
        this.Internal_list = new LinkedHashSet_Node[this.Size_of_array];
    }

    public MyLinkedHashSet(int size_of_HashSet, double load_factor_of_hash_set) {
        this.Size_of_array = size_of_HashSet;
        this.Load_factor = load_factor_of_hash_set;
        this.Internal_list = new LinkedHashSet_Node[this.Size_of_array];
    }

    public String toString(){
        if(!this.isEmpty()) {
            String ret_val = "[";
            LinkedHashSet_Node<E> temp = this.first;
            while (temp != null){
                ret_val += temp.value.toString() + ", ";
                temp = temp.next;
            }
            ret_val = ret_val.substring(0,ret_val.length()-2) + "]";
            return ret_val;
        }return "[]";
    }

    @Override
    public int size() {
        return this.Number_of_elements;
    }

    @Override
    public boolean isEmpty() {
        return this.Number_of_elements == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)] == null) return false;
        LinkedHashSet_Node<E> temp = this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)];
        while(temp != null){
            if(temp.value.equals(o)) return true;
            temp = temp.next_collision;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if(e == null || this.contains(e)) return false;
        LinkedHashSet_Node<E> last = null;
        if(this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)] == null) {
            this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)] = new LinkedHashSet_Node<>(e, this.last);
            last = this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)];
        }else {
            last = this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)];
            while (last.next_collision != null) {
                last = last.next_collision;
            }
            last.Set_Next_collision(new LinkedHashSet_Node<>(e,this.last));
            last = last.next_collision;
        }
        if(this.first == null) {
            this.first = this.Internal_list[Math.abs(e.hashCode()%this.Size_of_array)];
            this.last = this.first;
        }else {
            this.last.next = last;
            this.last = this.last.next;
        }
        this.Number_of_elements++;
        if (this.Is_resize_neded()) this.resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(o == null || this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)] == null) return false;
        LinkedHashSet_Node<E> temp = this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)];
        LinkedHashSet_Node<E> temp_back = null;
        while(temp != null && !temp.value.equals(o)){
            temp_back = temp;
            temp = temp.next_collision;
        }
        if(temp == null || (temp.next_collision == null && !temp.value.equals(o))) return false;
        if (temp == this.last) {
            this.last = temp.back;
            temp.back.next = null;
        } else if (temp == this.first) {
            this.first = temp.next;
            temp.next.back = null;
        } else {
            temp.back.next = temp.next;
            temp.next.back = temp.back;
        }
        if(temp_back == null && temp.next_collision != null){
            this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)] = temp.next_collision;
        }else if(temp_back == null) this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)] = null;
        else temp_back.next_collision = temp.next_collision;
        this.Number_of_elements--;
        return true;
    }

    @Override
    public void clear() {
        this.Internal_list = new LinkedHashSet_Node[this.Size_of_array];
        this.Number_of_elements = 0;
        this.first = null;
        this.last = null;
    }

    private boolean Is_resize_neded(){
        return this.Number_of_elements >= this.Size_of_array * this.Load_factor;
    }

    private void resize(){
        int new_size = this.Size_of_array*2;
        MyLinkedHashSet<E> new_int_class = new MyLinkedHashSet<>(new_size);
        LinkedHashSet_Node<E> temp = this.first;
        while(temp != null){
            new_int_class.add(temp.value);
            temp = temp.next;
        }
        this.Internal_list = new_int_class.Internal_list;
        this.Size_of_array = new_size;
        this.first = new_int_class.first;
        this.last = new_int_class.last;
    }





    @Override
    public boolean containsAll(Collection<?> c) {
        if(c.isEmpty()) return true;
        for(Object element : c){
            if(element == null || !this.contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()) return true;
        for(E element : c){
            if(element == null) return false;
            this.add(element);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c.isEmpty()) {
            this.Internal_list = new LinkedHashSet_Node[this.Size_of_array];
            this.Number_of_elements = 0;
            this.first = null;
            this.last = null;
            return true;
        }
        MyLinkedHashSet<E> Internal_set = new MyLinkedHashSet<>(this.Size_of_array);
        LinkedHashSet_Node<E> temp = this.first;
        while (temp!=null){
            if(c.contains(temp.value)) Internal_set.add(temp.value);
            temp = temp.next;
        }
        boolean ret_val = this.Number_of_elements > Internal_set.Number_of_elements;
        this.Internal_list = Internal_set.Internal_list;
        this.Number_of_elements = Internal_set.Number_of_elements;
        this.first = Internal_set.first;
        this.last = Internal_set.last;
        return ret_val;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c.isEmpty()) return false;
        boolean ret_val = false;
        for(Object element : c){
            if(element == null) return false;
            if(this.remove(element) && !ret_val) ret_val = true;
        }
        return ret_val;
    }








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

class LinkedHashSet_Node<E>{
    E value = null;
    LinkedHashSet_Node<E> next = null;
    LinkedHashSet_Node<E> back = null;
    LinkedHashSet_Node<E> next_collision = null;

    LinkedHashSet_Node(E value) {
        this.value = value;
    }

    LinkedHashSet_Node(E value, LinkedHashSet_Node<E> back) {
        this.value = value;
        this.Set_back(back);
    }

    public void Set_next(LinkedHashSet_Node<E> next) {
        this.next = next;
    }

    public void Set_back(LinkedHashSet_Node<E> back){
        this.back = back;
    }

    public void Set_Next_collision(LinkedHashSet_Node<E> collision){
        this.next_collision = collision;
    }
}
