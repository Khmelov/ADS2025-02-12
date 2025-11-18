package by.it.group410902.bolshakova.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел связного списка с сохранением порядка добавления
    protected static class LNode<E> {
        public E data;
        public LNode<E> next;
        public int pose; //порядок добавления

        public LNode(E data, int actPose) {
            this.data = data;
            this.pose = actPose;
            next = null;
        }
    }

    private int DefaultSize = 100;
    private int actSize = 0, lastPose = 0;

    // Внутренний список для разрешения коллизий
    protected static class MyList<E> {
        private LNode<E> head, tail;

        // Поиск элемента в цепочке
        public boolean contains(E o) {
            LNode<E> curr = head;
            while (curr != null && !curr.data.equals(o)) {
                curr = curr.next;
            }
            return curr != null;
        }
        // Добавление элемента
        public boolean add(E o, int actPose, boolean toCheck) {
            if (toCheck && contains(o)) {
                return false;
            }
            LNode<E> curr = new LNode<E>(o, actPose);
            if (tail == null) {          // если список пуст
                head = tail = curr;
            }
            else {                       // добавляем в конец
                tail.next = curr;
                tail = curr;
            }
            return true;
        }
        // Удаление элемента из цепочки
        public boolean remove(E o) {
            if (head == null) {
                return false;
            }
            // Возможность удаления первого элемента
            if (head.data.equals(o)) {
                head = head.next;
                return true;
            }
            // Ищем элемент перед удаляемым
            LNode<E> prev = head;
            while (prev.next != null && !prev.next.data.equals(o)) {
                prev = prev.next;
            }
            if (prev.next == null) {
                return false;
            }
            if (prev.next == tail) {
                prev.next = null;
                tail = prev;
            } else {
                prev.next = prev.next.next;
            }
            return true;
        }
    }
    // Хеш-таблица: массив связных списков
    private MyList[] map = new MyList[DefaultSize];
    {
        // Инициализация всех корзин пустыми списками
        for (int i = 0; i < DefaultSize; i++)
            map[i] = new MyList<E>();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        LNode<E>[] nods = new LNode[DefaultSize];

        for (int i = 0; i < DefaultSize; i++) { // собираем указатели на начала всех цепочек
            nods[i] = (LNode<E>) map[i].head;
        }

        //поиск минимального
        while (true) {
            LNode<E> minInf = new LNode<E>((E) new Object(), Integer.MAX_VALUE);
            int minI = -1;
            // ищем элемент с минимальным порядковым номером во всех цепочках
            for (int i = 0; i < DefaultSize; i++)
                if (nods[i] != null && nods[i].pose < minInf.pose) {
                    minInf = nods[i];
                    minI = i;
                }
            if (minI == -1) {
                break;
            }
            // Добавляем найденный элемент и двигаем указатель
            nods[minI] = nods[minI].next;
            res.append(minInf.data.toString()).append(", ");
        }
        return res.substring(0, res.length() - 2) + "]";
    }

    @Override
    public int size() {
        return actSize;
    }

    @Override
    public void clear() {
        actSize = 0;
        // Пересоздаем все списки
        for(int i = 0; i < DefaultSize; i++) {
            map[i] = new MyList<E>();
        }
    }
    @Override
    public boolean isEmpty() {
        return actSize == 0;
    }

    @Override
    public boolean add(E e) {
        // Вычисляем корзину, добавляем с текущим порядковым номером
        if (map[e.hashCode() % DefaultSize].add(e, lastPose++, true))
            actSize++;
        else {
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (map[o.hashCode()%DefaultSize].remove(o))
            actSize--;
        else {
            return false;
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return map[o.hashCode()%DefaultSize].contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean retBull = false;
        for (E o : c) {
            if (add(o)) {
                retBull = true;
            }
        }
        return retBull;
    }
    // Удалить все элементы, которые есть в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        int deleted = 0;
        for (int i = 0; i < DefaultSize; i++) {
            MyList<E> newMyList = new MyList<>(); // создаем новый список
            LNode<E> curr = map[i].head;
            while (curr != null) {
                if (!c.contains(curr.data)) {
                    // Сохраняем элементы, которых нет в коллекции
                    newMyList.add(curr.data, curr.pose, false);
                }
                else {
                    deleted++; // подсчитываем удаленные
                }
                curr = curr.next;
            }
            map[i] = newMyList; // заменяем старый список
        }
        actSize -= deleted;
        return deleted > 0;
    }
    // Оставить только элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        int deleted = 0;
        for (int i = 0; i < DefaultSize; i++) {
            MyList<E> newMyList = new MyList<>();
            LNode<E> curr = map[i].head;
            while (curr != null) {
                if (c.contains(curr.data)) {
                    // Сохраняем элементы, которые есть в коллекции
                    newMyList.add(curr.data, curr.pose, false);
                }
                else {
                    deleted++;
                }
                curr = curr.next;
            }
            map[i] = newMyList;
        }
        actSize -= deleted;
        return deleted > 0;
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
