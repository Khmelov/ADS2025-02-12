package by.it.group451004.akbulatov.lesson12;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class MySplayMap implements NavigableMap<Integer, String>
{
    private Node root;
    private int size = 0;

    private static class Node
    {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node node) { this.key = key; this.value = value; parent = node; }
    }

    private void rotateRight(Node root)
    {
        Node newRoot = root.left;
        if (newRoot == null) return;

        root.left = newRoot.right;
        if (newRoot.right != null)
            newRoot.right.parent = root;

        newRoot.parent = root.parent;

        if (root.parent == null)
            this.root = newRoot;
        else if (root == root.parent.right)
            root.parent.right = newRoot;
        else
            root.parent.left = newRoot;

        newRoot.right = root;
        root.parent = newRoot;
    }

    private void rotateLeft(Node root)
    {
        Node newRoot = root.right;
        if (newRoot == null) return;

        root.right = newRoot.left;
        if (newRoot.left != null)
            newRoot.left.parent = root;

        newRoot.parent = root.parent;
        if (root.parent == null)
            this.root = newRoot;
        else if (root == root.parent.left)
            root.parent.left = newRoot;
        else
            root.parent.right = newRoot;

        newRoot.left = root;
        root.parent = newRoot;
    }

    private void splay(Node x)
    {
        if (x == null) return;
        while (x.parent != null)
        {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null)
            {
                // zig
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            }
            else if (x == p.left && p == g.left)
            {
                // zig-zig
                rotateRight(g);
                rotateRight(p);
            }
            else if (x == p.right && p == g.right)
            {
                // zig-zig
                rotateLeft(g);
                rotateLeft(p);
            }
            else if (x == p.right && p == g.left)
            {
                // zig-zag
                rotateLeft(p);
                rotateRight(g);
            }
            else
            {
                // zig-zag
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    // --------------------- helpers --------------------------------------
    private Node findNode(Integer key)
    {
        Node cur = root;
        while (cur != null)
        {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    private Node findClosest(Node start, Integer key)
    {
        Node cur = start;
        Node last = null;
        while (cur != null)
        {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return last;
    }

    private Node subtreeMin(Node n)
    {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node subtreeMax(Node n)
    {
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }

    private Node predecessor(Node n)
    {
        if (n == null) return null;
        if (n.left != null) return subtreeMax(n.left);
        Node p = n.parent;
        Node cur = n;
        while (p != null && cur == p.left) { cur = p; p = p.parent; }
        return p;
    }

    private Node successor(Node n)
    {
        if (n == null) return null;
        if (n.right != null) return subtreeMin(n.right);
        Node p = n.parent;
        Node cur = n;
        while (p != null && cur == p.right) { cur = p; p = p.parent; }
        return p;
    }

    // --------------------- Map / NavigableMap required methods ------------

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        // in-order traversal
        boolean first = true;
        Node cur = root;
        Deque<Node> stack = new ArrayDeque<>();
        while (cur != null || !stack.isEmpty())
        {
            while (cur != null)
            {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            if (!first)
                sb.append(", ");

            first = false;
            sb.append(cur.key).append("=").append(cur.value);
            cur = cur.right;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value)
    {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node cur = root;
        Node parent = null;
        while (cur != null)
        {
            parent = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0)
            {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        Node n = new Node(key, value, parent);
        if (key.compareTo(parent.key) < 0)
            parent.left = n;
        else
            parent.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String remove(Object k)
    {
        if (!(k instanceof Integer key)) return null;
        Node n = findNode(key);
        if (n == null) return null;
        splay(n);
        String old = n.value;
        // remove root
        if (n.left == null)
        {
            transplant(n, n.right);
            if (root != null)
                root.parent = null;
        }
        else if (n.right == null)
        {
            transplant(n, n.left);
            if (root != null)
                root.parent = null;
        }
        else
        {
            // both children exist
            Node leftSub = n.left;
            leftSub.parent = null;
            Node maxLeft = subtreeMax(leftSub);
            // splay maxLeft to root of leftSub
            // to attach right subtree
            // we can splay in the whole tree by setting root to leftSub, splaying maxLeft, then attach
            root = leftSub;
            splay(maxLeft);
            // now maxLeft is root and has no right child
            maxLeft.right = n.right;
            if (n.right != null)
                n.right.parent = maxLeft;
        }
        size--;
        // ensure parent of current root is null
        if (root != null)
            root.parent = null;
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;

        if (v != null)
            v.parent = u.parent;
    }

    @Override
    public String get(Object k)
    {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node cur = root;
        while (cur != null)
        {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0)
            {
                splay(cur);
                return cur.value;
            }
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object k)
    {
        if (!(k instanceof Integer)) return false;
        Integer key = (Integer) k;
        Node n = findNode(key);
        if (n != null) splay(n);
        return n != null;
    }

    @Override
    public boolean containsValue(Object value)
    {
        Node cur = root;
        Deque<Node> stack = new ArrayDeque<>();
        while (cur != null || !stack.isEmpty())
        {
            while (cur != null)
            {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            if (Objects.equals(cur.value, value))
            {
                splay(cur);
                return true;
            }
            cur = cur.right;
        }
        return false;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void clear()
    {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey)
    {
        MySplayMap m = new MySplayMap();
        traverseAndPut(root, m, node -> node.key.compareTo(toKey) < 0);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey)
    {
        MySplayMap m = new MySplayMap();
        traverseAndPut(root, m, node -> node.key.compareTo(fromKey) >= 0);
        return m;
    }

    private interface NodePredicate
    {
        boolean test(Node n);
    }

    private void traverseAndPut(Node n, MySplayMap m, NodePredicate pred)
    {
        if (n == null) return;

        traverseAndPut(n.left, m, pred);
        if (pred.test(n))
            m.put(n.key, n.value);
        traverseAndPut(n.right, m, pred);
    }

    @Override
    public Integer firstKey()
    {
        if (root == null) throw new NoSuchElementException();

        Node n = subtreeMin(root);
        return n.key;
    }

    @Override
    public Integer lastKey()
    {
        if (root == null) throw new NoSuchElementException();
        Node n = subtreeMax(root);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key)
    {
        Integer ans = findRelative(key, Relation.LOWER);
        return ans;
    }

    @Override
    public Integer floorKey(Integer key)
    {
        Integer ans = findRelative(key, Relation.FLOOR);
        return ans;
    }

    @Override
    public Integer ceilingKey(Integer key)
    {
        Integer ans = findRelative(key, Relation.CEILING);
        return ans;
    }

    @Override
    public Integer higherKey(Integer key)
    {
        Integer ans = findRelative(key, Relation.HIGHER);
        return ans;
    }

    private enum Relation { LOWER, FLOOR, CEILING, HIGHER }

    private Integer findRelative(Integer key, Relation rel)
    {
        Node cur = root;
        Integer candidate = null;
        while (cur != null)
        {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0)
            {
                return switch (rel) {
                    case LOWER -> {
                        Node p = predecessor(cur);
                        yield p == null ? null : p.key;
                    }
                    case FLOOR -> cur.key;
                    case CEILING -> cur.key;
                    case HIGHER -> {
                        Node s = successor(cur);
                        yield s == null ? null : s.key;
                    }
                };
            }
            if (cmp < 0)
            {
                // cur.key > key
                if (rel == Relation.CEILING || rel == Relation.HIGHER) candidate = cur.key;
                cur = cur.left;
            }
            else
            {
                // cur.key < key
                if (rel == Relation.FLOOR || rel == Relation.LOWER) candidate = cur.key;
                cur = cur.right;
            }
        }

        if (candidate == null) return null;

        if (rel == Relation.LOWER || rel == Relation.HIGHER)
        {
            if (candidate.equals(key))
            {
                Node node = findNode(key);
                if (node != null)
                {
                    return (rel == Relation.LOWER) ? (predecessor(node) == null ? null : predecessor(node).key)
                            : (successor(node) == null ? null : successor(node).key);
                }
                else
                {
                    if (rel == Relation.LOWER) return lowerKey(key);
                    else return higherKey(key);
                }
            }
        }
        return candidate;
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }

    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }

    // methods required by Map interface but not explicitly in the task
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { throw new UnsupportedOperationException(); }

}
