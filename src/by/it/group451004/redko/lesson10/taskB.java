package by.it.group451004.redko.lesson10;

import java.util.*;

class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> toRemove;
        if (index == 0) {
            toRemove = head;
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else if (index == size - 1) {
            toRemove = tail;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null;
            }
        } else {
            Node<E> current = getNode(index);
            toRemove = current;
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }

        size--;
        return toRemove.data;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            for (Node<E> current = head; current != null; current = current.next) {
                if (current.data == null) {
                    removeNode(current);
                    return true;
                }
            }
        } else {
            for (Node<E> current = head; current != null; current = current.next) {
                if (element.equals(current.data)) {
                    removeNode(current);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    // Вспомогательные методы
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private void removeNode(Node<E> node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        size--;
    }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E removeFirst() {
        if (head == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (tail == null) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() { return head == null ? null : head.data; }

    @Override
    public E peekLast() { return tail == null ? null : tail.data; }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> current = tail; current != null; current = current.prev) {
                if (current.data == null) {
                    removeNode(current);
                    return true;
                }
            }
        } else {
            for (Node<E> current = tail; current != null; current = current.prev) {
                if (o.equals(current.data)) {
                    removeNode(current);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (Node<E> current = head; current != null; current = current.next) {
                if (current.data == null) return true;
            }
        } else {
            for (Node<E> current = head; current != null; current = current.next) {
                if (o.equals(current.data)) return true;
            }
        }
        return false;
    }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E data = current.data;
                current = current.prev;
                return data;
            }
        };
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Эти методы не поддерживаются в связном списке
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}