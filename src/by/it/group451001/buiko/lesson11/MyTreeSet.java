package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    // Внутренний класс для узла бинарного дерева
    private static class TreeNode<E> {
        E data;
        TreeNode<E> left;
        TreeNode<E> right;

        TreeNode(E data) {
            this.data = data;
        }
    }

    private TreeNode<E> root;
    private int size;

    // Конструктор
    public MyTreeSet() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательный метод для сравнения элементов
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        // Используем Comparable если доступен, иначе hashCode
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
            return false;
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            return containsRecursive(node.left, element);
        } else if (cmp > 0) {
            return containsRecursive(node.right, element);
        } else {
            return true;
        }
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        root = addRecursive(root, e);
        size++;
        return true;
    }

    private TreeNode<E> addRecursive(TreeNode<E> node, E element) {
        if (node == null) {
            return new TreeNode<>(element);
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            node.left = addRecursive(node.left, element);
        } else if (cmp > 0) {
            node.right = addRecursive(node.right, element);
        }

        return node;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E element = (E) o;
        if (!contains(element)) {
            return false;
        }
        root = removeRecursive(root, element);
        size--;
        return true;
    }

    private TreeNode<E> removeRecursive(TreeNode<E> node, E element) {
        if (node == null) {
            return null;
        }

        int cmp = compare(element, node.data);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, element);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, element);
        } else {
            // Узел для удаления найден
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // У узла два потомка - находим минимальный элемент в правом поддереве
            node.data = findMin(node.right);
            node.right = removeRecursive(node.right, node.data);
        }

        return node;
    }

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

    // Вспомогательный метод для обхода в порядке возрастания (in-order)
    private void inOrderTraversal(TreeNode<E> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            if (sb.length() > 1) { // Уже есть "["
                sb.append(", ");
            }
            sb.append(node.data);
            inOrderTraversal(node.right, sb);
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
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
    private void addAllFromTree(TreeNode<E> node) {
        if (node != null) {
            add(node.data);
            addAllFromTree(node.left);
            addAllFromTree(node.right);
        }
    }

    // Вспомогательный метод для преобразования дерева в массив (для отладки)
    private void toArrayRecursive(TreeNode<E> node, Object[] array, int[] index) {
        if (node != null) {
            toArrayRecursive(node.left, array, index);
            array[index[0]++] = node.data;
            toArrayRecursive(node.right, array, index);
        }
    }

    // Оставшиеся методы интерфейса Set (заглушки)

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int[] index = {0};
        toArrayRecursive(root, array, index);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }
}