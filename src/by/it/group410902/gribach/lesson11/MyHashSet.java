package by.it.group410902.gribach.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Данный класс использует массив списков (MyList), где каждый список хранит элементы
// с одинаковым хеш-кодом, что помогает избежать коллизий при добавлении элементов с одинаковыми хешами
public class MyHashSet<E> implements Set<E> {

    // class LNode<E> представляет узел односвязного списка.
    // Каждый узел хранит данные и ссылку на следующий узел.
    protected static class LNode<E> {
        public E data;
        public LNode<E> next;

        LNode(E data) {
            this.data = data;
            next = null;
        }
    }
    private int DefaultSize = 100;  // начальный размер массива для хранения списков (100)
    private int actSize = 0;  // отслеживает текущее количество элементов в MyHashSet

    // class MyList<E> представляет односвязный список с методами add, remove, и contains,
    // чтобы управлять элементами в каждом сегменте хеш-таблицы.
    protected static class MyList<E>{
        private LNode<E> head, tail;
        public boolean contains(E o){  // Проверяет, содержится ли элемент o в списке
            // Последовательно проходит по каждому узлу списка (начиная с head),
            // сравнивая data каждого узла с o. Если элемент найден, возвращает true.
            // Если элемент не найден, возвращает false
            LNode<E> curr = head;
            while(curr!=null && !curr.data.equals(o)) {
                curr = curr.next;
            }
            return curr != null;
        }

        public boolean add(E o){  // Добавляет элемент o в конец списка, если его там нет
            if(contains(o)) {
                // Если элемент уже есть, метод возвращает false, и добавление не происходит
                return false;
            }
            LNode<E> curr = new LNode<E>(o);  // Если элемента нет, создаётся новый узел curr с данными o
            if(tail == null) {
                head = tail = curr; // Если список пуст (tail == null), новый узел становится и head, и tail
            }
            else {
                tail.next = curr; // Если список не пуст, новый узел добавляется в конец, а указатель tail обновляется
                tail = curr;
            }
            return true;
        }
        public boolean remove(E o){  // Удаляет элемент o из списка, если он там присутствует
            if(head == null) {  // если список пуст
                return false;
            }
            if(head.data.equals(o)){
                head = head.next;
                return true;
            }
            // Если элемент находится не в head, метод перебирает узлы, пока не найдёт нужный элемент,
            // обновляя ссылки узлов для удаления
            LNode<E> prev = head;
            while(prev.next!=null && !prev.next.data.equals(o)) {
                prev = prev.next;
            }
            if(prev.next == null) {
                return false;
            }
            if(prev.next == tail) {
                prev.next = null;
                tail = prev;
            }
            else {
                prev.next = prev.next.next;
            }
            return true;
        }
    }

    // массив списков (MyList), который используется для хранения элементов,
    // распределённых по индексам, рассчитанным на основе их хеш-кодов.
    private MyList[] map = new MyList[DefaultSize];
    {
        for(int i = 0; i < DefaultSize; i++) {
            map[i] = new MyList<E>();
        }
    }

    @Override
    // возвращает строковое представление MyHashSet, выводя все элементы
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < DefaultSize; i++) {
            LNode curr = map[i].head;
            while(curr!=null){
                res.append(curr.data.toString()).append(", ");
                curr = curr.next;
            }
        }
        return res.substring(0,res.length()-2)+"]";
    }

    @Override
    // возвращает текущее количество элементов в MyHashSet
    public int size() {
        return actSize;
    }

    @Override
    // очищает MyHashSet, сбрасывая его состояние
    public void clear() {
        actSize = 0;
        for(int i = 0; i < DefaultSize; i++) {
            map[i] = new MyList<E>();
        }
    }
    @Override
    // возвращает true, если MyHashSet пуст, иначе false
    public boolean isEmpty() {
        return actSize == 0;
    }

    @Override
    // добавляет элемент в MyHashSet. Рассчитывает индекс на основе хеш-кода элемента
    // и добавляет его в соответствующий список MyList по этому индексу,
    // если элемент отсутствует в этом списке. Увеличивает actSize, если добавление прошло успешно.
    public boolean add(E e) {
        if(map[e.hashCode()%DefaultSize].add(e)) {
            actSize++;
        }
        else {
            return false;
        }
        return true;
    }
    @Override
    // удаляет элемент из MyHashSet. Рассчитывает индекс на основе хеш-кода
    // и удаляет элемент из соответствующего списка.
    // Уменьшает actSize, если удаление прошло успешно.
    public boolean remove(Object o) {
        if(map[o.hashCode()%DefaultSize].remove(o))
            actSize--;
        else {
            return false;
        }
        return true;
    }

    @Override
    // проверяет наличие элемента в MyHashSet. Находит индекс на основе хеш-кода
    // и проверяет, есть ли элемент в соответствующем списке.
    public boolean contains(Object o) {
        return map[o.hashCode()%DefaultSize].contains(o);
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
}
