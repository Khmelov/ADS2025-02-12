package by.it.group451003.kharkevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    static class Node<E extends Comparable<E>> {
        E data;
        Node<E> left;
        Node<E> right;

        Node(E data) {
            this.data = data;
        }
    }

    Node<E> _root;
    int _count;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(_root, sb);
        return sb.append("]").toString();
    }

    void inOrderTraversal(Node<E> node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1)
            sb.append(", ");
        sb.append(node.data);
        inOrderTraversal(node.right, sb);
    }

    @Override
    public int size() {
        return _count;
    }

    @Override
    public boolean isEmpty() {
        return _count == 0;
    }

    boolean contains(Node<E> node, E element) {
        if (node == null) return false;
        int compare = element.compareTo(node.data);
        if (compare < 0)
            return contains(node.left, element);
        else if (compare > 0)
            return contains(node.right, element);
        else
            return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            return contains(_root, (E) o);
        } catch (ClassCastException e) {
            return false;
        }
    }

    Node<E> insert(Node<E> node, E element) {
        if (node == null)
            return new Node<>(element);
        int compare = element.compareTo(node.data);
        if (compare < 0)
            node.left = insert(node.left, element);
        else if (compare > 0)
            node.right = insert(node.right, element);
        return node;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        if (!contains(e)) {
            _root = insert(_root, e);
            _count++;
            return true;
        }
        return false;
    }

    Node<E> findMin(Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    Node<E> delete(Node<E> node, E element) {
        if (node == null) return null;
        int compare = element.compareTo(node.data);
        if (compare < 0) {
            node.left = delete(node.left, element);
        } else if (compare > 0) {
            node.right = delete(node.right, element);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            node.data = findMin(node.right).data;
            node.right = delete(node.right, node.data);
        }
        return node;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E element = (E) o;
            if (contains(element)) {
                _root = delete(_root, element);
                _count--;
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj: c) {
            if (!contains(obj))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E element: c) {
            if (add(element))
                isModified = true;
        }
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        // Создаем временное дерево для элементов, которые нужно сохранить
        MyTreeSet<E> tempSet = new MyTreeSet<>();

        // Используем итератор для обхода текущих элементов
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (c.contains(element)) {
                tempSet.add(element);
            } else {
                isModified = true;
            }
        }

        // Если были изменения, заменяем текущее дерево
        if (isModified) {
            this._root = tempSet._root;
            this._count = tempSet._count;
        }

        return isModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        for (Object obj: c) {
            if (remove(obj))
                isModified = true;
        }
        return isModified;
    }

    @Override
    public void clear() {
        _root = null;
        _count = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new TreeIterator();
    }

    private class TreeIterator implements Iterator<E> {
        private Node<E> next;
        private Node<E> lastReturned;

        public TreeIterator() {
            next = _root;
            // Go to the first element (smallest)
            while (next != null && next.left != null) {
                next = next.left;
            }
        }

        private Node<E> findNext(Node<E> node) {
            if (node == null) return null;

            // If there is right child, go to the smallest in right subtree
            if (node.right != null) {
                Node<E> current = node.right;
                while (current.left != null) {
                    current = current.left;
                }
                return current;
            }

            // Otherwise, go up until we find a node that is left child of its parent
            return findParentWithLeftChild(node);
        }

        private Node<E> findParentWithLeftChild(Node<E> node) {
            if (_root == null || node == _root) return null;

            Node<E> parent = null;
            Node<E> current = _root;
            E target = node.data;

            // Find the parent
            while (current != null) {
                int compare = target.compareTo(current.data);
                if (compare < 0) {
                    parent = current;
                    current = current.left;
                } else if (compare > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    break;
                }
            }

            // Now find the first parent where current node is in left subtree
            current = node;
            while (parent != null && current == parent.right) {
                current = parent;
                parent = findParent(parent);
            }

            return parent;
        }

        private Node<E> findParent(Node<E> node) {
            if (_root == null || node == _root) return null;

            Node<E> parent = null;
            Node<E> current = _root;
            E target = node.data;

            while (current != null) {
                int compare = target.compareTo(current.data);
                if (compare < 0) {
                    parent = current;
                    current = current.left;
                } else if (compare > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    break;
                }
            }
            return parent;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            E data = next.data;
            next = findNext(next);
            return data;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            MyTreeSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[_count];
        Iterator<E> iterator = iterator();
        for (int i = 0; i < _count && iterator.hasNext(); i++) {
            array[i] = iterator.next();
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < _count) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), _count);
        }

        Iterator<E> iterator = iterator();
        for (int i = 0; i < _count && iterator.hasNext(); i++) {
            a[i] = (T) iterator.next();
        }

        if (a.length > _count) {
            a[_count] = null;
        }

        return a;
    }
}