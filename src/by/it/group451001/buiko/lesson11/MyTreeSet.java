package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyTreeSet<E> implements Set<E> {
    // Каждый узел содержит данные и ссылки на левое и правое поддеревья
    private static class TreeNode<E> {
        E data;           // Хранимый элемент
        TreeNode<E> left;  // Ссылка на левое поддерево (меньшие элементы)
        TreeNode<E> right; // Ссылка на правое поддерево (большие элементы)

        TreeNode(E data) {
            this.data = data;
        }
    }

    /*TreeSet — это коллекция, которая хранит уникальные элементы в отсортированном порядке,
    используя структуру бинарного дерева поиска, где для каждого узла левые потомки меньше родителя, а правые — больше.
     */
    private TreeNode<E> root;
    private int size;

    /*реализовала множество на основе бинарного дерева поиска (BST),
      где все операции выполняются рекурсивно, что обеспечивает естественное упорядочивание элементов
      согласно их сравнению через Comparable или hashCode.
     */
    public MyTreeSet() {
        root = null;
        size = 0;
    }

    /*Для добавления элементов метод add() рекурсивно проходит по дереву, сравнивая новый
   элемент с текущими узлами и вставляя его в правильную позицию для сохранения свойства бинарного дерева поиска.
    */
    @Override
    public int size() {
        return size;
    }

    /*При удалении элементов метод remove() обрабатывает три случая: узел без потомков, с одним
     потомком и с двумя потомками, где в последнем случае находится минимальный элемент правого поддерева для замены удаляемого узла.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    /*Все основные операции работают за логарифмическое время O(log n) в среднем случае,
    а метод toString() выводит элементы в отсортированном порядке благодаря in-order обходу дерева.
    */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        if (a instanceof Comparable && b instanceof Comparable) {
            return ((Comparable<E>) a).compareTo(b);
        }
        return Integer.compare(a.hashCode(), b.hashCode());
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        E element = (E) o;
        return containsRecursive(root, element);
    }

    private boolean containsRecursive(TreeNode<E> node, E element) {
        if (node == null) {
            return false; // Достигли листа - элемент не найден
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            // Искомый элемент меньше текущего - идем в левое поддерево
            return containsRecursive(node.left, element);
        } else if (cmp > 0) {
            // Искомый элемент больше текущего - идем в правое поддерево
            return containsRecursive(node.right, element);
        } else {
            // Элемент найден
            return true;
        }
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false; // Дубликаты не допускаются
        }
        root = addRecursive(root, e);
        size++;
        return true;
    }

    // Рекурсивное добавление элемента в дерево
    private TreeNode<E> addRecursive(TreeNode<E> node, E element) {
        if (node == null) {
            // Достигли места для вставки - создаем новый узел
            return new TreeNode<>(element);
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            // Элемент меньше текущего - добавляем в левое поддерево
            node.left = addRecursive(node.left, element);
        } else if (cmp > 0) {
            // Элемент больше текущего - добавляем в правое поддерево
            node.right = addRecursive(node.right, element);
        }
        // Если cmp == 0, элемент уже существует (проверено в add)

        return node;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E element = (E) o;
        if (!contains(element)) {
            return false; // Элемент не существует
        }
        root = removeRecursive(root, element);
        size--;
        return true;
    }

    // Рекурсивное удаление элемента из дерева
    private TreeNode<E> removeRecursive(TreeNode<E> node, E element) {
        if (node == null) {
            return null;
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            // Искомый элемент меньше - ищем в левом поддереве
            node.left = removeRecursive(node.left, element);
        } else if (cmp > 0) {
            // Искомый элемент больше - ищем в правом поддереве
            node.right = removeRecursive(node.right, element);
        } else {
            // Узел для удаления найден
            if (node.left == null) {
                // Случай 1: Нет левого потомка - заменяем правым
                return node.right;
            } else if (node.right == null) {
                // Случай 2: Нет правого потомка - заменяем левым
                return node.left;
            }

            // Случай 3: Есть оба потомка
            // Находим минимальный элемент в правом поддереве (преемник)
            node.data = findMin(node.right);
            // Удаляем найденный минимальный элемент из правого поддерева
            node.right = removeRecursive(node.right, node.data);
        }

        return node;
    }

    // Поиск минимального элемента в поддереве
    // В бинарном дереве поиска минимальный элемент всегда в самом левом узле
    private E findMin(TreeNode<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Вспомогательный метод для обхода в порядке возрастания (in-order traversal)
    // Левый -> Корень -> Правый
    // Для BST такой обход дает элементы в отсортированном порядке
    private void inOrderTraversal(TreeNode<E> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);  // Сначала левое поддерево
            if (sb.length() > 1) { // Уже есть "[" - значит это не первый элемент
                sb.append(", ");
            }
            sb.append(node.data);             // Затем текущий узел
            inOrderTraversal(node.right, sb); // Затем правое поддерево
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(root, sb);
        sb.append("]");
        return sb.toString();
    }

    // Реализация методов для работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции содержатся в множестве
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в коллекции
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Сохраняем только элементы, содержащиеся в коллекции c
        boolean modified = false;

        // Создаем временный набор для элементов, которые нужно сохранить
        MyTreeSet<E> toRetain = new MyTreeSet<>();

        // Собираем элементы, присутствующие в коллекции c
        for (Object element : c) {
            @SuppressWarnings("unchecked")
            E elem = (E) element;
            if (contains(elem)) {
                toRetain.add(elem);
            }
        }

        // Если размеры отличаются, значит нужно удалить некоторые элементы
        if (toRetain.size() != size) {
            clear();
            // Добавляем обратно только те элементы, которые нужно сохранить
            addAllFromTree(toRetain.root);
            modified = true;
        }

        return modified;
    }

    // Вспомогательный метод для добавления элементов из другого дерева
    // Используется в retainAll для восстановления дерева
    private void addAllFromTree(TreeNode<E> node) {
        if (node != null) {
            add(node.data);              // Добавляем текущий узел
            addAllFromTree(node.left);   // Добавляем левое поддерево
            addAllFromTree(node.right);  // Добавляем правое поддерево
        }
    }

    // Вспомогательный метод для преобразования дерева в массив (для отладки)
    // Выполняет in-order обход для сохранения порядка сортировки
    private void toArrayRecursive(TreeNode<E> node, Object[] array, int[] index) {
        if (node != null) {
            toArrayRecursive(node.left, array, index);  // Левые элементы
            array[index[0]++] = node.data;              // Текущий элемент
            toArrayRecursive(node.right, array, index); // Правые элементы
        }
    }

    // Оставшиеся методы интерфейса Set (заглушки)

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        // Создаем массив и заполняем его элементами в отсортированном порядке
        Object[] array = new Object[size];
        int[] index = {0}; // Используем массив для передачи по ссылке
        toArrayRecursive(root, array, index);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }
}

/*
* Ключевые особенности TreeSet:
1. Структура данных
Бинарное дерево поиска (BST) - самобалансирующееся дерево

Отсортированный порядок - элементы хранятся в естественном порядке

Рекурсивные алгоритмы - для всех операций с деревом

2. Свойства бинарного дерева поиска
Левые потомки < Родитель < Правые потомки

In-order обход дает отсортированную последовательность

Средняя сложность операций: O(log n)

3. Операции с деревом
Добавление:

Сравниваем с корнем, идем влево/вправо

Вставляем в правильную позицию для сохранения порядка

Удаление:

Случай 1: Нет потомков - просто удаляем

Случай 2: Один потомок - заменяем удаляемый узел потомком

Случай 3: Два потомка - находим преемника в правом поддереве

Поиск:

Бинарный поиск по дереву

Сравниваем и идем в соответствующее поддерево

4. Преимущества
Автоматическая сортировка - элементы всегда в порядке

Эффективный поиск - O(log n) в сбалансированном дереве

Динамический размер - не требует начальной емкости

5. Временная сложность
Добавление: O(log n) в среднем, O(n) в худшем

Удаление: O(log n) в среднем, O(n) в худшем

Поиск: O(log n) в среднем, O(n) в худшем

Обход: O(n) для всех элементов
* */