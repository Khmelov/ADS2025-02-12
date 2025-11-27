package by.it.group410902.shahov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private Object[] a;        // Внутренний массив для хранения элементов в отсортированном порядке
    private int size;          // Текущее количество элементов в множестве

    // Конструктор по умолчанию - создает множество с начальной емкостью 10
    public MyTreeSet() {
        a = new Object[10];
        size = 0;
    }

    // Возвращает строковое представление множества в отсортированном порядке
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(a[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    // Возвращает количество элементов в множестве
    @Override
    public int size(){return this.size;}

    // Очищает множество, удаляя все элементы
    @Override
    public void clear(){
        for (int i = 0; i < size; i++) a[i] = null;
        size = 0;
    }

    // Проверяет, пусто ли множество
    @Override
    public boolean isEmpty(){return this.size == 0;}

    // Добавляет элемент в множество, сохраняя порядок сортировки
    @Override
    public boolean add(E e){
        if(e == null) throw new NullPointerException(); // Не поддерживает null элементы

        // Находим позицию для вставки с помощью бинарного поиска
        int index = findIndex(e);
        if(index >= 0) return false; // Элемент уже существует

        // Вычисляем позицию для вставки (преобразуем отрицательный результат бинарного поиска)
        int insertion = -index - 1;

        // Гарантируем достаточную емкость массива
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо чтобы освободить место для нового элемента
        for(int i = size; i > insertion; i--) a[i] = a[i - 1];

        // Вставляем элемент на нужную позицию
        a[insertion] = e;
        size++;
        return true;
    }

    // Удаляет элемент из множества
    @Override
    public boolean remove(Object o){
        if(o == null) return false; // Не поддерживает null элементы

        // Находим индекс элемента с помощью бинарного поиска
        int index = findIndex(o);
        if(index < 0) return false; // Элемент не найден

        // Сдвигаем элементы влево чтобы удалить элемент
        for(int i = index; i < size - 1; i++) a[i] = a[i+1];

        // Очищаем последнюю позицию и уменьшаем размер
        a[size - 1] = null;
        size--;
        return true;
    }

    // Проверяет наличие элемента в множестве
    @Override
    public boolean contains(Object o){
        if(o == null) return false; // Не поддерживает null элементы
        return findIndex(o) >= 0;   // Элемент найден если индекс неотрицательный
    }

    // Проверяет, содержатся ли все элементы коллекции в множестве
    @Override
    public boolean containsAll(Collection<?> c){
        for(Object element: c){
            if(!contains(element)) return false;
        }
        return true;
    }

    // Добавляет все элементы из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean modified = false;
        for(E element: c){
            if(add(element)) modified = true; // Хотя бы один элемент добавлен
        }
        return modified;
    }

    // Удаляет все элементы, содержащиеся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c){
        boolean modified = false;
        for(Object element: c){
            if(remove(element)) modified = true; // Хотя бы один элемент удален
        }
        return modified;
    }

    // Оставляет только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c){
        boolean modified = false;
        int i = 0;
        // Проходим по всем элементам множества
        while(i < size){
            if(!c.contains(a[i])){
                remove(a[i]); // Удаляем элемент если его нет в коллекции c
                modified = true;
            }
            else i++; // Переходим к следующему элементу только если текущий не удален
        }
        return modified;
    }

    // Бинарный поиск элемента в отсортированном массиве
    // Возвращает неотрицательный индекс если элемент найден,
    // или отрицательное значение -(insertion point + 1) если элемент не найден
    private int findIndex(Object o) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1; // Безопасное вычисление среднего индекса
            int cmp = compare(a[mid], o);
            if (cmp == 0) return mid;    // Элемент найден
            if (cmp < 0) lo = mid + 1;   // Искомый элемент в правой половине
            else hi = mid - 1;           // Искомый элемент в левой половине
        }
        return -(lo + 1); // Элемент не найден, возвращаем позицию для вставки
    }

    // Гарантирует, что массив имеет достаточную емкость для minCapacity элементов
    private void ensureCapacity(int minCapacity) {
        if (a.length >= minCapacity) return; // Текущей емкости достаточно

        // Увеличиваем емкость в 2 раза или до minCapacity если этого недостаточно
        int newCap = a.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;

        // Создаем новый массив и копируем элементы
        Object[] na = new Object[newCap];
        System.arraycopy(a, 0, na, 0, size);
        a = na;
    }

    // Сравнивает два объекта с использованием Comparable интерфейса
    @SuppressWarnings("unchecked")
    private int compare(Object x, Object y) {
        return ((Comparable<Object>) x).compareTo(y);
    }

    // Не реализованные методы интерфейса Set
    public java.util.Iterator<E> iterator() {throw new UnsupportedOperationException();}
    public Object[] toArray() {throw new UnsupportedOperationException();}
    public <T> T[] toArray(T[] ts) {throw new UnsupportedOperationException();}
    public boolean equals(Object o) {throw new UnsupportedOperationException();}
    public int hashCode() {throw new UnsupportedOperationException();}
}
