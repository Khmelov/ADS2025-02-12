package by.it.group410902.shahov.lesson11;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private int len;                    // Текущее количество элементов в множестве
    private LinkedList<E>[] arr;        // Массив связных списков для хэш-таблицы (разрешение коллизий)
    final private int startLen = 32;    // Начальный размер массива по умолчанию
    private LinkedList<E>[] q;          // Специальный список для сохранения порядка добавления элементов

    // Конструктор по умолчанию - создает множество с начальным размером 32
    @SuppressWarnings("all")
    public MyLinkedHashSet(){
        len = 0;
        arr = new LinkedList[startLen]; // Инициализация массива бакетов
        q = new LinkedList[1];          // Массив из одного элемента для хранения списка порядка
        q[0] = new LinkedList<E>();     // Список, сохраняющий порядок добавления элементов
    }

    // Конструктор с заданным начальным размером
    @SuppressWarnings("all")
    public MyLinkedHashSet(int startLen){
        len = 0;
        arr = new LinkedList[startLen]; // Массив бакетов заданного размера
        q = new LinkedList[1];
        q[0] = new LinkedList<E>();     // Список для сохранения порядка
    }

    // Возвращает строковое представление множества в порядке добавления элементов
    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        StringBuilder s = new StringBuilder("[");
        // Используем список q[0] для сохранения порядка итерации
        for (var i : q[0]){
            s.append(i);
            s.append(", ");
        }
        // Удаляем последнюю запятую и пробел
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.append("]").toString();
    }

    // Возвращает количество элементов в множестве
    @Override
    public int size(){return this.len;}

    // Очищает множество, удаляя все элементы
    @Override
    public void clear(){
        // Очищаем все бакеты в хэш-таблице
        for (int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
        // Очищаем список порядка
        q[0].clear();
        len = 0;
    }

    // Проверяет, пусто ли множество
    @Override
    public boolean isEmpty(){return this.len == 0;}

    // Добавляет элемент в множество
    @Override
    public boolean add(E o){
        // Если элемент уже существует, возвращаем false
        if (contains(o))
            return false;

        // Добавляем элемент в список порядка
        q[0].add(o);
        len++;

        // Вычисляем хэш и добавляем в соответствующий бакет
        var hash = o.hashCode() % arr.length;
        if (arr[hash] == null)
            arr[hash] = new LinkedList<E>(); // Создаем новый бакет если нужно
        return arr[hash].add(o);
    }

    // Удаляет элемент из множества
    @Override
    public boolean remove(Object o) {
        // Вычисляем хэш для поиска
        var hash = o.hashCode() % arr.length;
        if (arr[hash] == null)
            return false; // Бакет пуст - элемента нет

        // Удаляем элемент из хэш-таблицы и списка порядка
        if (arr[hash].remove(o)){
            len--;
            q[0].remove(o);
            return true;
        }
        return false;
    }

    // Проверяет наличие элемента в множестве
    @Override
    public boolean contains(Object o) {
        // Вычисляем хэш и ищем в соответствующем бакете
        var hash = o.hashCode() % arr.length;
        return arr[hash] != null && arr[hash].contains(o);
    }

    // Проверяет, содержатся ли все элементы коллекции в множестве
    @Override
    public boolean containsAll(Collection<?> c){
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true; // Хотя бы один элемент был добавлен
            }
        }
        return modified;
    }

    // Удаляет все элементы, содержащиеся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c){
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true; // Хотя бы один элемент был удален
            }
        }
        return modified;
    }

    // Оставляет только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c){
        boolean modified = false;
        // Создаем копию для безопасной итерации (чтобы избежать ConcurrentModificationException)
        LinkedList<E> copy = new LinkedList<>(q[0]);
        for (E element : copy) {
            // Удаляем элементы, которых нет в коллекции c
            if (!c.contains(element)) {
                remove(element);
                modified = true;
            }
        }
        return modified;
    }

    // Не реализованные методы интерфейса Set
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
}
