package by.it.group410902.dziatko.lesson11;

import by.it.group410902.dziatko.lesson10.MyLinkedList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private MyLinkedList<E>[] Internal_list;
    private int Size_of_array = 16, Number_of_elements = 0;//base length of an hashset
    private  double Load_factor = 0.75;//base load factor in hashset

    public MyHashSet(){
        this.Internal_list = new MyLinkedList[this.Size_of_array];
        for (int i = 0; i < this.Size_of_array; i++) {
            this.Internal_list[i] = new MyLinkedList<E>();
        }
    }

    public MyHashSet(int size_of_HashSet){
        this.Size_of_array = size_of_HashSet;
        this.Internal_list = new MyLinkedList[this.Size_of_array];
        for (int i = 0; i < this.Size_of_array; i++) {
            this.Internal_list[i] = new MyLinkedList<E>();
        }
    }

    public MyHashSet(int size_of_HashSet, double load_factor_of_hash_set) {
        this.Size_of_array = size_of_HashSet;
        this.Load_factor = load_factor_of_hash_set;
        this.Internal_list = new MyLinkedList[this.Size_of_array];
        for (int i = 0; i < this.Size_of_array; i++) {
            this.Internal_list[i] = new MyLinkedList<E>();
        }
    }

    public String toString(){
        if(!this.isEmpty()) {
            String ret_val = "[";
            for (int i = 0; i < this.Size_of_array; i++) {
                if(!this.Internal_list[i].isEmpty())
                    ret_val += this.Internal_list[i].to_string_for_internal_storage() + ", ";
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
        return this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)].contains(o);
    }

    @Override
    public boolean add(E e) {
        if(e == null) return false;
        if(this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)].contains(e)) return false;
        this.Internal_list[Math.abs(e.hashCode() % this.Size_of_array)].add(e);
        this.Number_of_elements++;
        if (this.Is_resize_neded()) this.resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(this.Internal_list[Math.abs(o.hashCode()%this.Size_of_array)].remove(o)){
            this.Number_of_elements--;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.Internal_list = new MyLinkedList[this.Size_of_array];
        this.Number_of_elements = 0;
        for (int i = 0; i < this.Size_of_array; i++) {
            this.Internal_list[i] = new MyLinkedList<E>();
        }
    }

    private boolean Is_resize_neded(){
        return this.Number_of_elements >= this.Size_of_array * this.Load_factor;
    }

    private void resize(){
        int new_size = this.Size_of_array*2;
        MyHashSet<E> new_int_class = new MyHashSet<>(new_size);
        for (int i = 0; i < new_size; i++) {
            new_int_class.Internal_list[i] = new MyLinkedList<E>();
        }
        for (int i = 0; i < this.Size_of_array; i++) {
            if(!this.Internal_list[i].isEmpty()){
                Object[] Inner_List = Internal_list[i].toArray();
                for (int j = 0; j < Inner_List.length; j++) {
                    new_int_class.add((E) Inner_List[j]);
                }
            }
        }
        this.Internal_list = new_int_class.Internal_list;
        this.Size_of_array = new_size;
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
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
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
