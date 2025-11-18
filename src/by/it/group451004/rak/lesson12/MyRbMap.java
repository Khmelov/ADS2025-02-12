package by.it.group451004.rak.lesson12;

import java.util.*;

public class MyRbMap<K extends Comparable<K>, V> implements SortedMap<K, V> {
    private Node root = null;
    private final Comparator<? super K> cmp;
    private int size = 0;
    private int modCount = 0;

    public MyRbMap(Comparator<? super K> cmp) {
        this.cmp = cmp;
    }

    public MyRbMap() {
        this.cmp = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убрать последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    private Node grandparent(Node n) {
        if ((n != null && (n.parent != null)))
            return n.parent.parent;
        else
            return null;
    }

    private Node uncle(Node n) {
        Node g = grandparent(n);
        if (g == null)
            return null;
        if (n.parent == g.left)
            return g.right;
        else
            return g.left;
    }

    private void rotateLeft(Node a) {
        Node b = a.right;
        b.parent = a.parent;
        if (a.parent != null) {
            if (a.parent.left == a) {
                a.parent.left = b;
            } else {
                a.parent.right = b;
            }
        } else {
            root = b;
        }
        a.right = b.left;
        if (b.left != null)
            b.left.parent = a;
        b.left = a;
        a.parent = b;
    }

    private void rotateRight(Node a) {
        Node b = a.left;
        b.parent = a.parent;
        if (a.parent != null) {
            if (a.parent.left == a)
                a.parent.left = b;
            else
                a.parent.right = b;
        } else {
            root = b;
        }
        a.left = b.right;
        if (b.right != null)
            b.right.parent = a;
        b.right = a;
        a.parent = b;
    }

    private void insertCase1(Node n) {
        //Случай 1: нет parent
        if (n.parent == null)
            n.isRed = false;
        else
            insertCase2(n);
    }

    private void insertCase2(Node n) {
        //Случай 2: parent черный - ничего не менять
        //Доп: есть parent
        if (n.parent.isRed)
            insertCase3(n);
    }

    private void insertCase3(Node n) {
        //Случай 3: uncle красный
        //Доп: есть красный parent
        //У parent есть parent

        Node u = uncle(n);
        if (u != null && u.isRed) {
            n.parent.isRed = false;
            u.isRed = false;
            Node g = grandparent(n);
            g.isRed = true;
            insertCase1(g);
        } else {
            insertCase4(n);
        }
    }

    private void insertCase4(Node n) {
        //Случай 4: n правый потомок, p левый потомок (+симметрия)
        //Доп: есть красный parent
        //У parent есть parent
        //uncle черный
        Node p = n.parent;
        Node g = grandparent(n);
        if (p.right == n && g.left == p) {
            rotateLeft(p);
            n = n.left;
        } else if (p.left == n && g.right == p) {
            rotateRight(p);
            n = n.right;
        }
        insertCase5(n);
    }

    private void insertCase5(Node n) {
        //Случай 4: n левый потомок, p левый потомок (+симметрия)
        //Доп: есть красный parent
        //У parent есть parent
        //uncle черный
        Node g = grandparent(n);
        n.parent.isRed = false;
        g.isRed = true;
        if ((n == n.parent.left) && (n.parent == g.left))
            rotateRight(g);
        else
            rotateLeft(g);
    }

    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("null keys not supported");

        Node existing = getNode(key);
        if (existing != null) {
            V old = existing.setValue(value);
            return old;
        }

        Node parent = null;
        Node current = root;

        while (current != null) {
            parent = current;
            int cmpRes = (cmp == null) ? key.compareTo(current.getKey()) : cmp.compare(key, current.getKey());
            if (cmpRes > 0) current = current.right;
            else current = current.left;
        }

        Node newNode = new Node(key, value, parent);
        if (parent == null)
            root = newNode;
        else if ((cmp == null ? key.compareTo(parent.getKey()) : cmp.compare(key, parent.getKey())) > 0)
            parent.right = newNode;
        else
            parent.left = newNode;

        insertCase1(newNode);
        size++;
        modCount++;
        return null;
    }

    private Node getNode(Object key) {
        if (key == null) return null;
        Node m = root;
        K k = (K) key;
        while (m != null) {
            int cmpRes = (cmp == null ? k.compareTo(m.getKey()) : cmp.compare(k, m.getKey()));
            if (cmpRes > 0)
                m = m.right;
            else if (cmpRes < 0)
                m = m.left;
            else
                return m;
        }
        return null;
    }

    private Node replaceWithPredecessor(Node m) {
        Node pred = m.left;
        while (pred.right != null)
            pred = pred.right;

        m.key = pred.key;
        m.value = pred.value;
        return pred;
        //приведение 2-x детей к 0 или 1
    }

    private void deleteOneChild(Node m) {
        //Доп: Ребенок красный
        Node child = m.left != null ? m.left : m.right;

        if (m.parent == null) {
            root = child;
        } else if (m.parent.right == m) {
            m.parent.right = child;
        } else {
            m.parent.left = child;
        }

        if (child != null) { //необяз условие
            child.parent = m.parent;
            child.isRed = false;
        }
        m.parent = null;
    }


    private void deleteCase1_NodeIsRoot(Node m) {
        if (m.parent == null) {
            root = null;
        } else {
            deleteCase2_SiblingIsRed(m);
            if (m.parent != null) {
                if (m.parent.left == m)
                    m.parent.left = null;
                else
                    m.parent.right = null;
            }
            m.parent = null;
        }
    }

    private void deleteCase2_SiblingIsRed(Node m) {
        Node s = sibling(m);
        if (s != null && s.isRed) { //s != null всегда
            if (m.parent.left == m) {
                rotateLeft(m.parent);
            } else {
                rotateRight(m.parent);
            }
            m.parent.isRed = false; //проверить
            s.isRed = true;
        }
        deleteCase3_LeftSiblingsChildIsRedAndRightIsBlack(m);
    }

    private void deleteCase3_LeftSiblingsChildIsRedAndRightIsBlack(Node m) {
        Node s = sibling(m);
        if (s == null) return; //невозможно

        if (m.parent.left == m) {
            if (s.left != null && s.left.isRed && (s.right == null || !s.right.isRed)) {
                rotateRight(s);
                s.isRed = true;
                if (s.parent != null) s.parent.isRed = false;
            }
        } else {
            if (s.right != null && s.right.isRed && (s.left == null || !s.left.isRed)) {
                rotateLeft(s);
                s.isRed = true;
                if (s.parent != null) s.parent.isRed = false;
            }
        }
        deleteCase4_RightSiblingsChildIsRed(m);
    }

    private void deleteCase4_RightSiblingsChildIsRed(Node m) {
        Node s = sibling(m);
        if (s == null) return;

        if (m.parent.left == m) {
            if (s.right != null && s.right.isRed) {
                boolean parentColor = m.parent.isRed;
                boolean siblingsLeftChildColor = s.left != null && s.left.isRed;

                rotateLeft(m.parent);

                m.parent.isRed = false;
                m.parent.parent.isRed = parentColor;
                if (m.parent.right != null) m.parent.right.isRed = siblingsLeftChildColor;
                //очистка памяти
            } else {
                deleteCase5_BothSiblingsChildrenAreBlack(m);
            }
        } else {
            if (s.left != null && s.left.isRed) {
                boolean parentColor = m.parent.isRed;
                boolean siblingsRightChildColor = s.right != null && s.right.isRed;

                rotateRight(m.parent);

                m.parent.isRed = false;
                m.parent.parent.isRed = parentColor;
                if (m.parent.left != null) m.parent.left.isRed = siblingsRightChildColor;
            } else {
                deleteCase5_BothSiblingsChildrenAreBlack(m);
            }
        }
    }

    private void deleteCase5_BothSiblingsChildrenAreBlack(Node m) {
        Node s = sibling(m);
        if (m.parent != null) {
            if (m.parent.left == m)
                m.parent.left = null;
            else
                m.parent.right = null;
        }
        m.parent = null;

        if (s != null) {
            s.isRed = true;
            if (s.parent != null && !s.parent.isRed)
                deleteCase1_NodeIsRoot(s.parent);
        }
    }

    private Node sibling(Node n) {
        if (n == null || n.parent == null)
            return null;
        return (n == n.parent.left) ? n.parent.right : n.parent.left;
    }

    @Override
    public V remove(Object key) {
        Node m = getNode(key);
        if (m == null) return null;

        V oldValue = m.getValue();

        if (m.right != null && m.left != null)
            m = replaceWithPredecessor(m); //теперь у m до 1 потомка
        if (m.left != null || m.right != null)
            deleteOneChild(m);
        else {
            deleteCase1_NodeIsRoot(m);
        }
        size--;
        modCount++;
        return oldValue;
    }

    @Override
    public V get(Object key) { //throw new NullPointerException(); и null
        Node current = root;
        K k = (K) key;
        while (current != null) {
            int cmpKeyWithNode = cmp == null ? k.compareTo(current.getKey()) : cmp.compare(k, current.getKey());

            if (cmpKeyWithNode > 0) {
                current = current.right;
            } else if (cmpKeyWithNode < 0) {
                current = current.left;
            } else {
                return current.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValueRecursive(n.left, value) || containsValueRecursive(n.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        modCount++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        MyRbMap<K,V> map = new MyRbMap<>();
        for (Entry<K,V> e : this.entrySet()) {
            if ((cmp == null ? e.getKey().compareTo(toKey) : cmp.compare(e.getKey(), toKey)) < 0)
                map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        MyRbMap<K,V> map = new MyRbMap<>();
        for (Entry<K,V> e : this.entrySet()) {
            if ((cmp == null ? e.getKey().compareTo(fromKey) : cmp.compare(e.getKey(), fromKey)) >= 0)
                map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    @Override
    public K firstKey() {
        if (root == null)
            throw new NoSuchElementException();
        Node current = root;
        while (current.left != null)
            current = current.left;
        return current.getKey();
    }

    @Override
    public K lastKey() {
        if (root == null)
            throw new NoSuchElementException();
        Node current = root;
        while (current.right != null)
            current = current.right;
        return current.getKey();
    }

    //================================================================================================================//

    @Override
    public Comparator<? super K> comparator() {
        return cmp;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        MyRbMap<K,V> map = new MyRbMap<>();
        for (Entry<K,V> e : this.entrySet()) {
            if ((cmp == null ? e.getKey().compareTo(fromKey) : cmp.compare(e.getKey(), fromKey)) >= 0
                    && (cmp == null ? e.getKey().compareTo(toKey) : cmp.compare(e.getKey(), toKey)) < 0) {
                map.put(e.getKey(), e.getValue());
            }
        }
        return map;
    }

    @Override
    public Set<K> keySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<K> iterator() {
                Iterator<Entry<K,V>> it = MyRbMap.this.entrySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public K next() {
                        return it.next().getKey();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return MyRbMap.this.size();
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<>() {
            @Override
            public Iterator<V> iterator() {
                Iterator<Entry<K,V>> it = MyRbMap.this.entrySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public V next() {
                        return it.next().getValue();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return MyRbMap.this.size();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new RBIterator(root);
            }

            @Override
            public int size() {
                return MyRbMap.this.size();
            }
        };
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    private class Node implements Entry<K,V>{
        boolean isRed = true;
        K key;
        V value;
        Node parent = null, left = null, right = null;

        Node(K key, V value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> e
                    && (key==null ? e.getKey()==null : key.equals(e.getKey()))
                    && (value==null ? e.getValue()==null : value.equals(e.getValue()));
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

    private class RBIterator implements Iterator<Entry<K, V>> {
        int expectedModCount;
        Node lastReturned;

        private final Stack<Node> stack = new Stack<>();

        RBIterator(Node root){
            expectedModCount = modCount;
            lastReturned = null;
            pushLeft(root);
        }

        private void pushLeft(Node n){
            Node cur = n;
            while (cur != null){
                stack.push(cur);
                cur = cur.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Entry<K, V> next() {
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
            if (!hasNext()) throw new NoSuchElementException();
            Node node = stack.pop();
            lastReturned = node;
            if (node.right != null) pushLeft(node.right);
            return node;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}