package by.it.group410901.tomashevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Узел бинарного дерева поиска
    class Node {
        E data;      // значение, хранимое в узле
        Node left;   // ссылка на левый дочерний узел
        Node right;  // ссылка на правый дочерний узел

        Node(E data) {         // конструктор узла
            this.data = data;  // сохраняем значение
        }
    }

    // Печать множества в отсортированном порядке (in-order обход дерева)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // создаём строку, начинаем с '['
        inOrderTraversal(_root, sb);               // вызываем рекурсивный обход дерева
        return sb.append("]").toString();          // закрываем ']' и возвращаем строку
    }

    // Симметричный обход (лево → корень → право)
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;          // если узла нет — выходим (база рекурсии)
        inOrderTraversal(node.left, sb);   // обходим левое поддерево
        if (sb.length() > 1)               // если не первый элемент — ставим запятую
            sb.append(", ");
        sb.append(node.data);              // добавляем значение текущего узла
        inOrderTraversal(node.right, sb);  // обходим правое поддерево
    }

    Node _root;   // ссылка на корень дерева
    int _count;   // количество элементов в дереве

    @Override
    public int size() {
        return _count; // возвращаем количество элементов
    }

    @Override
    public boolean isEmpty() {
        return _count == 0; // если элементов нет — возвращаем true
    }

    // Проверка, содержится ли элемент в поддереве
    boolean contains(Node node, E element) {
        if (node == null) return false;              // если поддерево пустое — элемента нет
        int compare = element.compareTo(node.data);  // сравниваем искомый элемент с текущим
        if (compare < 0)                             // если меньше — ищем в левом поддереве
            return contains(node.left, element);
        else if (compare > 0)                        // если больше — ищем в правом поддереве
            return contains(node.right, element);
        else                                         // если равно — элемент найден
            return true;
    }

    @Override
    public boolean contains(Object o) {
        return contains(_root, (E) o); // запускаем поиск от корня дерева
    }

    // Вставка элемента в дерево (рекурсивно)
    Node insert(Node node, E element) {
        if (node == null)                            // если место пустое (лист)
            return new Node(element);                // создаём новый узел
        int compare = element.compareTo(node.data);  // сравниваем значения
        if (compare < 0)                             // если меньше — идём влево
            node.left = insert(node.left, element);
        else if (compare > 0)                        // если больше — идём вправо
            node.right = insert(node.right, element);
        return node;                                 // возвращаем (возможно) обновлённый узел
    }

    @Override
    public boolean add(E e) {
        if (!contains(e)) {        // проверяем, что такого элемента ещё нет
            _root = insert(_root, e); // вставляем элемент (возможно обновляем корень)
            _count++;                 // увеличиваем счётчик элементов
            return true;              // возвращаем true — элемент добавлен
        }
        return false;                 // элемент уже есть, не добавляем
    }

    // Поиск минимального узла (самый левый в дереве)
    Node findMin(Node node) {
        while (node.left != null) { // идём по левым потомкам
            node = node.left;       // пока не дойдём до самого левого
        }
        return node;                // возвращаем минимальный узел
    }

    // Удаление узла из дерева
    Node delete(Node node, E element) {
        if (node == null) return null;                 // если узел пуст — ничего не делаем
        int compare = element.compareTo(node.data);    // сравниваем элемент с текущим
        if (compare < 0) {                             // если меньше — идём влево
            node.left = delete(node.left, element);
        } else if (compare > 0) {                      // если больше — идём вправо
            node.right = delete(node.right, element);
        } else {                                       // если элемент найден
            if (node.left == null) {                   // если нет левого поддерева
                return node.right;                     // возвращаем правое (удаляем текущий)
            } else if (node.right == null) {           // если нет правого поддерева
                return node.left;                      // возвращаем левое
            }
            // если есть оба поддерева — ищем минимальный элемент справа
            node.data = findMin(node.right).data;      // заменяем данные текущего узла
            node.right = delete(node.right, node.data); // удаляем дубликат справа
        }
        return node;                                   // возвращаем (возможно) обновлённый узел
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {                 // если элемент присутствует
            _root = delete(_root, (E) o);  // удаляем его из дерева
            _count--;                      // уменьшаем размер
            return true;                   // возвращаем true (удаление успешно)
        }
        return false;                      // элемента не было — возвращаем false
    }

    // Проверка: содержатся ли все элементы из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {         // перебираем все элементы коллекции
            if (!contains(obj))        // если хоть один не найден
                return false;          // возвращаем false
        }
        return true;                   // все элементы найдены
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;        // флаг изменения множества
        for (E element : c) {              // перебираем коллекцию
            if (add(element))              // добавляем каждый элемент
                isModified = true;         // если добавлено хотя бы одно — изменилось
        }
        return isModified;                 // возвращаем результат
    }

    // Оставляем только элементы, которые есть в другой коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {          // если переданная коллекция пуста
            this.clear();           // очищаем текущее множество
            return true;            // возвращаем true (изменилось)
        }
        boolean isModified = false; // флаг изменения
        MyTreeSet<E> retainSet = new MyTreeSet<>(); // создаём временное множество
        for (Object obj : c) {                    // перебираем элементы коллекции
            if (contains(obj)) {                  // если элемент есть в текущем множестве
                retainSet.add((E) obj);           // добавляем во временное множество
                isModified = true;                // помечаем изменение
            }
        }
        _root = retainSet._root;   // заменяем корень дерева новым
        _count = retainSet._count; // обновляем количество элементов
        return isModified;         // возвращаем true, если структура изменилась
    }

    // Удаление всех элементов коллекции из множества
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;     // флаг изменения множества
        for (Object obj : c) {          // перебираем все элементы коллекции
            if (remove(obj))            // удаляем каждый, если найден
                isModified = true;      // если удалён хоть один — изменилось
        }
        return isModified;              // возвращаем результат
    }

    @Override
    public void clear() {
        _root = null;  // обнуляем корень (дерево пустое)
        _count = 0;    // сбрасываем количество элементов
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Методы не реализованы, оставлены заглушки
    @Override
    public Iterator<E> iterator() {
        return null; // итератор пока не реализован
    }

    @Override
    public Object[] toArray() {
        return new Object[0]; // метод преобразования в массив пока не реализован
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null; // метод преобразования в массив указанного типа пока не реализован
    }
}
