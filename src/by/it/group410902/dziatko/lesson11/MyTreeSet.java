package by.it.group410902.dziatko.lesson11;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private Object[] Internal_array;
    int array_size = 10 , Number_of_elements = 0;

    MyTreeSet(){
        this.Internal_array = new Object[this.array_size];
    }

    MyTreeSet(int array_size){
        this.Internal_array = new Object[this.array_size];
        this.array_size = array_size;
    }

    @Override
    public String toString() {
        if(this.isEmpty()) return "[]";
        String ret_val = "[";
        for (int i = 0; i < this.Number_of_elements; i++) {
            ret_val += this.Internal_array[i].toString() + ", ";
        }
        ret_val = ret_val.substring(0,ret_val.length()-2) + "]";
        return ret_val;
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
        return this.Binary_search_for_contains(o) != -1;
    }

    private int Binary_search_for_contains(Object Target){
        int low = 0, high = this.Number_of_elements-1;
        E Targ = (E) Target;
        while(low <= high){
            int middle = (high + low)/2;
            if(this.Internal_array[middle].equals(Target)) return middle;
            else if (Targ.compareTo((E)this.Internal_array[middle]) < 0) {
                high = middle-1;
            }else{
                low = middle+1;
            }
        }
        return -1;
    }

    @Override
    public boolean add(E e) {
        int place = this.Binary_sarch_for_add(e);
        if(place == -1 || this.contains(e)) return false;
        for (int i = this.size()-1; i >= place; i--) {
            this.Internal_array[i+1] = this.Internal_array[i];
        }
        this.Internal_array[place] = e;
        this.Number_of_elements++;
        this.is_resize_nedded();
        return true;
    }

    private int Binary_sarch_for_add(E target){
        int low = 0, high = this.Number_of_elements-1;

        while(low <= high){
            int middle = (low + high)/2;
            if(this.Internal_array[middle].equals(target)) return middle;
            else if (target.compareTo((E)(this.Internal_array[middle])) < 0) {
                high = middle - 1;
            }else{
                low = middle + 1;
            }
        }
        return low;
    }

    @Override
    public boolean remove(Object o) {
        if(!this.contains(o)) return false;
        int place = this.Binary_search_for_contains(o);
        for (int i = place; i < this.Number_of_elements-1; i++) {
            this.Internal_array[i] = this.Internal_array[i+1];
        }
        this.Number_of_elements--;
        return true;
    }


    @Override
    public void clear() {
        this.array_size = 10;
        this.Internal_array = new Object[this.array_size];
        this.Number_of_elements = 0;

    }

    private void is_resize_nedded(){if(this.Number_of_elements == this.array_size) this.resize();}

    private void resize(){
        Object[] new_array = new Object[(int) (this.array_size*1.5)];
        System.arraycopy(this.Internal_array,0,new_array,0,this.Number_of_elements);
        this.Internal_array = new_array;
        this.array_size= (int)(this.array_size*1.5);
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        if(c.isEmpty()) return true;
        for(Object o : c){
            if(!this.contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()) return true;
        boolean modified = false;
        for(E e : c){
            if(this.add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c.isEmpty()){
            this.Internal_array = new Object[this.array_size];
            this.Number_of_elements = 0;
            return true;
        }
        Object[] new_array = new Object[this.array_size];
        int j = 0;
        for (int i = 0; i < this.size(); i++) {
            if(c.contains(this.Internal_array[i])) new_array[j++] = this.Internal_array[i];
        }
        boolean ret_val = this.Number_of_elements > j;
        this.Internal_array = new_array;
        this.Number_of_elements = j;
        return ret_val;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c.isEmpty()) return false;
        boolean modified = false;
        for(Object o : c){
            if(remove(o)) modified = true;
        }
        return modified;
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
