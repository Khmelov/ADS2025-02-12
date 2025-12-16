package by.it.group451004.akbulatov.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.SortedMap;

@SuppressWarnings({"DuplicatedCode", "SuspiciousNameCombination"})
public class MyRbMap implements SortedMap<Integer, String>
{
    private static final boolean RED = true;
    private static final boolean BLUE = false;

    private static final class Node
    {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer k, String v, boolean color, Node parent)
        {
            this.key = k;
            this.value = v;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    public MyRbMap()
    {
        root = null;
        size = 0;
    }
    
    private Node getNode(Integer key)
    {
        Node x = root;
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    @Override
    public String get(Object key)
    {
        if (key == null) return null;
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key)
    {
        if (key == null) return false;
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value)
    {
        if (node == null) return false;

        if (value == null)
        {
            if (node.value == null) return true;
        }
        else
            if (value.equals(node.value)) return true;

        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public void clear()
    {
        root = null;
        size = 0;
    }

    private void rotateLeft(Node node)
    {
        Node newNode = node.right;
        node.right = newNode.left;

        if (newNode.left != null) newNode.left.parent = node;
        newNode.parent = node.parent;

        if (node.parent == null)
            root = newNode;
        else if (node == node.parent.left)
            node.parent.left = newNode;
        else
            node.parent.right = newNode;

        newNode.left = node;
        node.parent = newNode;
    }

    private void rotateRight(Node node)
    {
        Node newNode = node.left;
        node.left = newNode.right;

        if (newNode.right != null) newNode.right.parent = node;
        newNode.parent = node.parent;

        if (node.parent == null)
            root = newNode;
        else if (node == node.parent.right)
            node.parent.right = newNode;
        else
            node.parent.left = newNode;

        newNode.right = node;
        node.parent = newNode;
    }

    @Override
    public String put(Integer key, String value)
    {
        if (key == null) throw new NullPointerException("Null keys not supported");
        Node y = null;
        Node x = root;
        while (x != null)
        {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0)
            {
                String old = x.value;
                x.value = value;
                return old;
            }
            x = (cmp < 0) ? x.left : x.right;
        }

        Node z = new Node(key, value, RED, y);
        if (y == null)
            root = z;
        else if (key.compareTo(y.key) < 0)
            y.left = z;
        else
            y.right = z;

        size++;
        insertFixup(z);

        return null;
    }

    private void insertFixup(Node node)
    {
        while (node.parent != null && node.parent.color == RED)
        {
            // if parent itself is the left child
            if (node.parent == node.parent.parent.left)
            {
                // then parents uncle is on the right
                Node uncle = node.parent.parent.right;

                // if uncle is RED or null, then recolor
                if (uncle != null && uncle.color == RED)
                {
                    // grandparent to RED
                    node.parent.parent.color = RED;

                    // parent and uncle to BLUE
                    node.parent.color = BLUE;
                    uncle.color = BLUE;

                    // check further
                    node = node.parent.parent;
                }
                else
                {
                    // if this node is the right child
                    if (node == node.parent.right)
                    {
                        // perform a left rotation
                        node = node.parent;
                        rotateLeft(node);
                    }

                    node.parent.color = BLUE;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            }
            else
            {
                // uncle is on the left
                Node uncle = node.parent.parent.left;

                // if it's RED or null
                if (uncle != null && uncle.color == RED)
                {
                    // recolor grandparent to red
                    node.parent.parent.color = RED;

                    // parent and uncle to BLUE
                    node.parent.color = BLUE;
                    uncle.color = BLUE;

                    // check further
                    node = node.parent.parent;
                }
                else
                {
                    // if this node is the left child
                    if (node == node.parent.left)
                    {
                        // rotate right
                        node = node.parent;
                        rotateRight(node);
                    }

                    node.parent.color = BLUE;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = BLUE;
    }

    @Override
    public String remove(Object key)
    {
        if (key == null) return null;
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String old = z.value;
        deleteNode(z);
        return old;
    }

    private void transplant(Node a, Node b)
    {
        if (a.parent == null)
            root = b;
        else if (a == a.parent.left)
            a.parent.left = b;
        else
            a.parent.right = b;

        if (b != null)
            b.parent = a.parent;
    }

    private Node minimum(Node node)
    {
        while (node.left != null) node = node.left;
        return node;
    }

    private void deleteNode(Node node)
    {
        Node y = node;
        boolean originalColor = y.color;
        Node tmp;

        if (node.left == null)
        {
            tmp = node.right;
            transplant(node, node.right);
        }
        else if (node.right == null)
        {
            tmp = node.left;
            transplant(node, node.left);
        }
        else
        {
            y = minimum(node.right);
            originalColor = y.color;
            tmp = y.right;

            if (y.parent == node)
            {
                if (tmp != null)
                    tmp.parent = y;
            }
            else
            {
                transplant(y, y.right);
                y.right = node.right;
                if (y.right != null)
                    y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            if (y.left != null)
                y.left.parent = y;
            y.color = node.color;
        }

        size--;
        if (originalColor == BLUE)
            deleteFixup(tmp, (y.parent != null) ? y.parent : null);
    }

    private void deleteFixup(Node node, Node parent)
    {
        while ((node != root) && (node == null || node.color == BLUE))
        {
            if (parent == null) break;

            if (node == parent.left)
            {
                Node w = parent.right;
                if (w != null && w.color == RED)
                {
                    w.color = BLUE;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if ((w == null) || ((w.left == null || w.left.color == BLUE) && (w.right == null || w.right.color == BLUE)))
                {
                    if (w != null) w.color = RED;
                    node = parent;
                    parent = node.parent;
                }
                else
                {
                    if (w.right == null || w.right.color == BLUE)
                    {
                        w.left.color = BLUE;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    if (w != null) w.color = parent.color;
                    parent.color = BLUE;
                    if (w != null && w.right != null)
                        w.right.color = BLUE;
                    rotateLeft(parent);
                    node = root;
                    break;
                }
            }
            else
            {
                Node w = parent.left;
                if (w != null && w.color == RED)
                {
                    w.color = BLUE;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if ((w == null) || ((w.left == null || w.left.color == BLUE) && (w.right == null || w.right.color == BLUE)))
                {
                    if (w != null) w.color = RED;
                    node = parent;
                    parent = node.parent;
                }
                else
                {
                    if (w.left == null || w.left.color == BLUE)
                    {
                        w.right.color = BLUE;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    if (w != null) w.color = parent.color;
                    parent.color = BLUE;
                    if (w != null && w.left != null)
                        w.left.color = BLUE;
                    rotateRight(parent);
                    node = root;
                    break;
                }
            }
        }
        if (node != null) node.color = BLUE;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        Node x = root;
        java.util.Deque<Node> stack = new java.util.ArrayDeque<>();
        while (x != null || !stack.isEmpty())
        {
            while (x != null)
            {
                stack.push(x);
                x = x.left;
            }
            x = stack.pop();
            if (!first) sb.append(", ");
            sb.append(x.key).append('=').append(x.value);
            first = false;
            x = x.right;
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey)
    {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, null, toKey);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey)
    {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, fromKey, null);
        return m;
    }

    private void copyRange(Node x, MyRbMap target, Integer from, Integer to)
    {
        if (x == null) return;
        copyRange(x.left, target, from, to);
        boolean geFrom = (from == null) || (x.key.compareTo(from) >= 0);
        boolean ltTo = (to == null) || (x.key.compareTo(to) < 0);
        if (geFrom && ltTo) target.put(x.key, x.value);
        copyRange(x.right, target, from, to);
    }

    @Override
    public Integer firstKey()
    {
        if (root == null) throw new java.util.NoSuchElementException();

        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey()
    {
        if (root == null) throw new java.util.NoSuchElementException();

        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey)
    {
        MyRbMap m = new MyRbMap();
        copyRange(root, m, fromKey, toKey);
        return m;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet() not implemented");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values() not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
