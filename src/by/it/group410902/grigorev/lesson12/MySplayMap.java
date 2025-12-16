package by.it.group410902.grigorev.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        public int key;
        public String data;
        public Node left, right, parent;

        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        public Node(Node n) {
            this.key = n.key;
            this.data = n.data;
            this.left = n.left;
            this.right = n.right;
            this.parent = n.parent;
        }

        public Node findMin() {
            if (this.left == null)
                return this;
            else
                return this.left.findMin();
        }
    }

    private Node head = null;
    int size = 0;

    private void rotate(Node parent, Node child) {
        Node g = parent.parent;
        if (g != null)
            if (g.left == parent)
                g.left = child;
            else
                g.right = child;
        if (parent.left == child) {
            parent.left = child.right;
            child.right = parent;
        } else {
            parent.right = child.left;
            child.left = parent;
        }
        keepParent(child);
        keepParent(parent);
        child.parent = g;
    }

    private void setParent(Node child, Node parent) {
        if (child != null)
            child.parent = parent;
    }

    private void keepParent(Node n) {
        setParent(n.left, n);
        setParent(n.right, n);
    }

    private Node splay(Node n) {
        Node parent = n.parent;
        if (parent == null)
            return n;
        Node g = parent.parent;
        if (g == null) {
            rotate(parent, n);
            return n;
        }
        Boolean isZigZig = (g.left == parent && parent.left == n) || (g.right == parent && parent.right == n);
        if (isZigZig) {
            rotate(g, parent);
            rotate(parent, n);
        } else {
            rotate(parent, n);
            rotate(g, n);
        }
        return splay(n);
    }

    private Node recGet(Node n, Integer key) {
        if (n == null)
            return null;
        if (n.key == key) {
            return splay(n);
        }
        if (key < n.key && n.left != null)
            return recGet(n.left, key);
        if (key > n.key && n.right != null)
            return recGet(n.right, key);
        return splay(n);
    }

    private class SplitNode {
        Node left;
        Node right;

        public SplitNode(Node l, Node r) {
            left = l;
            right = r;
        }
    }

    private SplitNode split(Node root, Integer key) {
        if (root == null)
            return new SplitNode(null, null);
        root = recGet(root, key);
        if (root.key == key) {
            setParent(root.left, null);
            setParent(root.right, null);
            return new SplitNode(root.left, root.right);
        }
        if (root.key < key) {
            Node r = null;
            if (root.right != null)
                r = root.right;
            root.right = null;
            setParent(r, null);
            return new SplitNode(root, r);
        } else {
            Node l = null;
            if (root.left != null)
                l = root.left;
            root.left = null;
            setParent(l, null);
            return new SplitNode(l, root);
        }
    }

    private Node insert(Node root, Integer key, String data) {
        SplitNode s = split(root, key);
        root = new Node(key, data);
        root.left = s.left;
        root.right = s.right;
        keepParent(root);
        return root;
    }

    private Node merge(Node left, Node right) {
        if (right == null)
            return left;
        if (left == null)
            return right;
        right = recGet(right, left.key);
        right.left = left;
        setParent(left, right);
        return right;
    }

    private Node delete(Node root, Integer key) {
        root = recGet(root, key);
        setParent(root.left, null);
        setParent(root.right, null);
        return merge(root.left, root.right);
    }


    private void toStr(Node n, StringBuilder res) {
        if (n == null)
            return;
        toStr(n.left, res);
        res.append(n.key).append("=").append(n.data).append(", ");
        toStr(n.right, res);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(head, res);
        if (head != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));
        res.append("}");
        return String.valueOf(res);
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    private Integer FMax = null;

    private void recGetLower(Node n, Integer key) {
        if (n == null)
            return;
        recGetLower(n.left, key);
        if (FMax == null && n.key < key)
            FMax = n.key;
        else if (n.key < key && FMax < n.key)
            FMax = n.key;
        recGetLower(n.right, key);
    }

    @Override
    public Integer lowerKey(Integer key) {
        FMax = null;
        recGetLower(head, key);
        return FMax;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    private void recGetFloor(Node n, Integer key) {
        if (n == null)
            return;
        recGetFloor(n.left, key);
        if (FMax == null && n.key <= key)
            FMax = n.key;
        else if (n.key <= key && FMax < n.key)
            FMax = n.key;
        recGetFloor(n.right, key);
    }

    @Override
    public Integer floorKey(Integer key) {
        FMax = null;
        recGetFloor(head, key);
        return FMax;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    private void recGetCeiling(Node n, Integer key) {
        if (n == null)
            return;
        recGetCeiling(n.left, key);
        if (FMin == null && n.key >= key)
            FMin = n.key;
        else if (n.key >= key && FMin > n.key)
            FMin = n.key;
        recGetCeiling(n.right, key);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        FMin = null;
        recGetCeiling(head, key);
        return FMin;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    private Integer FMin;

    private void recGetHigher(Node n, Integer key) {
        if (n == null)
            return;
        recGetHigher(n.left, key);
        if (FMin == null && n.key > key)
            FMin = n.key;
        else if (n.key > key && FMin > n.key)
            FMin = n.key;
        recGetHigher(n.right, key);
    }

    @Override
    public Integer higherKey(Integer key) {
        FMin = null;
        recGetHigher(head, key);
        return FMin;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    public void fillUpper(Node n, Integer key) {
        if (n == null)
            return;
        fillUpper(n.left, key);
        if (n.key >= key)
            this.put(n.key, n.data);
        fillUpper(n.right, key);
    }

    public void fillLower(Node n, Integer key) {
        if (n == null)
            return;
        fillLower(n.left, key);
        if (n.key < key)
            this.put(n.key, n.data);
        fillLower(n.right, key);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap tmp = new MySplayMap();
        tmp.fillLower(this.head, toKey);
        return tmp;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap tmp = new MySplayMap();
        tmp.fillUpper(this.head, fromKey);
        return tmp;
    }

    @Override
    public Integer firstKey() {
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;
            tmp = tmp.left;
        }
        return tmp_p.key;
    }

    @Override
    public Integer lastKey() {
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;
            tmp = tmp.right;
        }
        return tmp_p.key;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        head = recGet(head, (Integer) key);
        return key.equals(head.key);
    }

    private Boolean ContainsValueF;

    private void recGetByValue(Node n, Object data) {
        if (n == null)
            return;
        recGetByValue(n.left, data);
        if (ContainsValueF)
            return;
        if (n.data.equals(data)) {
            ContainsValueF = true;
            splay(n);
            return;
        }
        recGetByValue(n.right, data);
    }

    @Override
    public boolean containsValue(Object value) {
        ContainsValueF = false;
        recGetByValue(head, value);
        return ContainsValueF;
    }

    @Override
    public String get(Object key) {
        head = recGet(head, (Integer) key);
        if (key.equals(head.key))
            return head.data;
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        head = recGet(head, key);
        if (head == null || head.key != key) {
            size++;
            head = insert(head, key, value);
            return null;
        }
        String pr = head.data;
        head.data = value;
        return pr;
    }

    @Override
    public String remove(Object key) {
        String res = get(key);
        if (res != null) {
            size--;
            head = delete(head, (Integer) key);
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        size = 0;
        head = null;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}

