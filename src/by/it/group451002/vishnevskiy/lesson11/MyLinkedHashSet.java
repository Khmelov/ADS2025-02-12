package by.it.group451002.vishnevskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

//     Создайте class MyLinkedHashSet<E> (хеш-таблицы, которая сохраняет порядок добавления элементов),
//     который
//     работает на основе массива с адресацией по хеш-коду
//    и односвязным списком для элементов с коллизиями
//    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
//                toString()
//                size()
//                clear()
//                isEmpty()
//                add(Object)
//                remove(Object)
//                contains(Object)
//
//                containsAll(Collection)
//                addAll(Collection)
//                removeAll(Collection)
//                retainAll(Collection)

public class MyLinkedHashSet<E> implements Set<E> {


    //Узел, хранящий данные, ссылку на следующий узел (next) и
    // позицию вставки (pose) для отслеживания порядка.
    protected static class LNode<E> {
        public E data;
        public LNode<E> next;
        public int pose;

        public LNode(E data, int actPose) {
            this.data = data;
            this.pose = actPose;
            next = null;
        }
    }

    private int DefaultSize = 100; // начальный размер массива для хранения списков (100)
    private int actSize = 0, lastPose = 0;  // actSize- Количество элементов в MyLinkedHashSet
    // lastPose -Указывает на текущую позицию последнего добавленного элемента для поддержки порядка вставки

    // Односвязный список, поддерживающий методы добавления, удаления и поиска узлов.
    protected static class MyList<E> {
        private LNode<E> head, tail;
        //  Проверяет,содержится ли элемент o в связном списке.
        //  Пробегает по узлам,сравнивая data каждого узла с o.
        // Если элемент найден, возвращает true, иначе false
        public boolean contains(E o) {
            LNode<E> curr = head;
            while (curr != null && !curr.data.equals(o)) {
                curr = curr.next;
            }
            return curr != null;
        }

        // Добавляет новый узел с элементом o в конец списка, если его там нет
        //(toCheck определяет, проверять ли наличие).Возвращает true, если элемент
        // успешно добавлен, и false,если уже существует.
        public boolean add(E o, int actPose, boolean toCheck) {
            if (toCheck && contains(o)) {
                return false;
            }
            LNode<E> curr = new LNode<E>(o, actPose);
            if (tail == null) {
                head = tail = curr;
            }
            else {
                tail.next = curr;
                tail = curr;
            }
            return true;
        }

        // Удаляет узел с элементом o из списка, если он там присутствует.
        //Возвращает true при успешном удалении и false, если элемент отсутствует.
        public boolean remove(E o) {
            if (head == null) {
                return false;
            }
            if (head.data.equals(o)) {
                head = head.next;
                return true;
            }
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

    // Хеш-таблица с массивом списков,где каждый индекс соответствует
    //списку MyList для хранения элементов с определёнными хешами.
    private MyList[] map = new MyList[DefaultSize];
    {
        for (int i = 0; i < DefaultSize; i++)
            map[i] = new MyList<E>();
    }
    @Override
    // Формирует строку, представляющую множество.Для этого элементы обрабатываются в порядке pose
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        LNode<E>[] nods = new LNode[DefaultSize];
        for (int i = 0; i < DefaultSize; i++) {
            nods[i] = (LNode<E>) map[i].head;
        }
        while (true) {
            LNode<E> minInf = new LNode<E>((E) new Object(), Integer.MAX_VALUE);
            int minI = -1;
            for (int i = 0; i < DefaultSize; i++)
                if (nods[i] != null && nods[i].pose < minInf.pose) {
                    minInf = nods[i];
                    minI = i;
                }
            if (minI == -1) {
                break;
            }
            nods[minI] = nods[minI].next;
            res.append(minInf.data.toString()).append(", ");
        }
        return res.substring(0, res.length() - 2) + "]";
    }

    @Override
    //Возвращает количество элементов в MyLinkedHashSet
    public int size() {
        return actSize;
    }

    @Override
    // Очищает MyLinkedHashSet путем обнуления всех списков и сброса счетчика размера(actSize)
    public void clear() {
        actSize = 0;
        for(int i = 0; i < DefaultSize; i++) {
            map[i] = new MyList<E>();
        }
    }
    @Override
    // Возвращает true, если множество пустое
    public boolean isEmpty() {
        return actSize == 0;
    }

    @Override
    //Добавляет элемент в множество, используя хеш-таблицу для нахождения соответствующего списка.
    // Увеличивает actSize,если элемент добавлен
    public boolean add(E e) {
        if (map[e.hashCode() % DefaultSize].add(e, lastPose++, true))
            actSize++;
        else {
            return false;
        }
        return true;
    }

    @Override
    // Удаляет элемент из множества, используя его хеш для нахождения соответствующего списка,
    // и уменьшает actSize,если удаление выполнено
    public boolean remove(Object o) {
        if (map[o.hashCode()%DefaultSize].remove(o))
            actSize--;
        else {
            return false;
        }
        return true;
    }

    @Override
    // Проверяет наличие элемента в MyLinkedHashSet.
    public boolean contains(Object o) {
        return map[o.hashCode()%DefaultSize].contains(o);
    }

    @Override
    // Возвращает true,если все элементы коллекции c содержатся в множестве
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    //Добавляет все элементы из коллекции c в множество.
    // Возвращает true,если хотя бы один элемент был добавлен.
    public boolean addAll(Collection<? extends E> c) {
        boolean retBull = false;
        for (E o : c) {
            if (add(o)) {
                retBull = true;
            }
        }
        return retBull;
    }

    @Override
    // Удаляет все элементы из множества, которые присутствуют в коллекции c.
    // Возвращает true,если хотя бы один элемент был удалён.
    public boolean removeAll(Collection<?> c) {
        int deleted = 0;
        for (int i= 0;i < DefaultSize; i++) {
            MyList<E> newMyList = new MyList<>();
            LNode<E> curr = map[i].head;
            while (curr != null) {
                if (!c.contains(curr.data)) {
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
    //Удаляет все элементы из множества, которые не присутствуют в коллекции c.
    // Возвращает true,если хотя бы один элемент был удалён
    public boolean retainAll(Collection<?> c) {
        int deleted = 0;
        for (int i = 0; i < DefaultSize; i++) {
            MyList<E> newMyList = new MyList<>();
            LNode<E> curr = map[i].head;
            while (curr != null) {
                if (c.contains(curr.data)) {
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