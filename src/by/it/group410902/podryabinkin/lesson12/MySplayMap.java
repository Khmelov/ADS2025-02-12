package by.it.group410902.podryabinkin.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node{
        public Integer key;
        public String value;
        public Node left = null, right = null, parent = null;
        public Node(Integer key, String value){
            this.key = key;
            this.value = value;
        }
    }
    Node root;
    Integer size = 0;

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void balance(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                // Zig
                if (x == p.left)
                    rotateRight(p);
                else
                    rotateLeft(p);
            }
            else if (x == p.left && p == g.left) {
                // Zig-Zig (левый)
                rotateRight(g);
                rotateRight(p);
            }
            else if (x == p.right && p == g.right) {
                // Zig-Zig (правый)
                rotateLeft(g);
                rotateLeft(p);
            }
            else if (x == p.right && p == g.left) {
                // Zig-Zag (лево-право)
                rotateLeft(p);
                rotateRight(g);
            }
            else {
                // Zig-Zag (право-лево)
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }


    public String toString(){
        String outp = "{";
        outp = obhod(outp, root);
        if (outp.length() > 1)
            outp = outp.substring(0, outp.length() - 2);
        outp += "}";
        return outp;
    }

    public String obhod(String inp, Node n){
        if(n == null) return inp;
        if(n.left != null) inp = obhod(inp, n.left);
        inp = inp + n.key + "=" + n.value + ", ";
        if(n.right != null) inp = obhod(inp, n.right);
        return inp;
    }




    @Override
    public String get(Object key) {
        Node cur = root;
        while(cur != null){
            if(key == cur.key){
                String val = cur.value;
                balance(cur);
                return val;
            }
            else if((Integer)key > cur.key) cur = cur.right;
            else  cur = cur.left;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null){
            root = new Node(key, value);
            size++;
            return value;
        }
        Node cur = root;
        while (cur != null){
            if(cur.key > key){
                if(cur.left != null){
                    cur = cur.left;
                }
                else {
                    cur.left = new Node(key, value);
                    cur.left.parent = cur;
                    balance(cur);
                    size++;
                    return null;
                }
            }
            else if(cur.key < key){
                if(cur.right != null){
                    cur = cur.right;
                }
                else {
                    cur.right = new Node(key, value);
                    cur.right.parent = cur;
                    balance(cur);
                    size++;
                    return null;
                }
            }
            else {
                String oldValue = cur.value;
                cur.value = value;
                balance(cur);
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) return null;

        Node cur = root;
        while (cur != null) {
            if ((Integer) key < cur.key) {
                cur = cur.left;
            } else if ((Integer) key > cur.key) {
                cur = cur.right;
            } else {
                // Нашли узел
                String oldValue = cur.value;

                // Если у узла два ребёнка
                if (cur.left != null && cur.right != null) {
                    // Находим максимум в левом поддереве
                    Node maxLeft = cur.left;
                    while (maxLeft.right != null) maxLeft = maxLeft.right;

                    // Копируем значения
                    cur.key = maxLeft.key;
                    cur.value = maxLeft.value;

                    // Удаляем этот максимум
                    deleteNode(maxLeft);
                    balance(cur); // делаем splay
                } else {
                    // Один ребёнок или ни одного
                    deleteNode(cur);
                }

                size--;
                return oldValue;
            }
        }

        return null; // если не нашли ключ
    }
    private void deleteNode(Node node) {
        Node parent = node.parent;
        Node child = (node.left != null) ? node.left : node.right;

        if (child != null) child.parent = parent;

        if (parent == null) {
            root = child;
        } else if (node == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        if (parent != null) balance(parent);
    }


    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private boolean prim_check(Node s, String val){
        if(s.value == val) return true;
        boolean l = false, r = false;
        if(s.left != null) l = prim_check(s.left, val);
        if(s.right != null) r = prim_check(s.right, val);
        return l || r;
    }
    @Override
    public boolean containsValue(Object value) {
        return prim_check(root, value.toString());
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
    public void clear(){
        size = 0;
        root = null;

    }




    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        addHead(root, toKey, result);
        return result;
    }
    private void addHead(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        if (node.key < toKey) {
            addHead(node.left, toKey, result);
            result.put(node.key, node.value);
            addHead(node.right, toKey, result);
        } else {
            addHead(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        addTail(root, fromKey, result);
        return result;
    }
    private void addTail(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            addTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            addTail(node.right, fromKey, result);
        } else {
            addTail(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        Node cur = root;
        if(cur == null) return null;
        while(cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        Node cur = root;
        if(cur == null) return null;
        while(cur.right != null) cur = cur.right;
        return cur.key;
    }


    //ближайший ключ меньше
    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (key > cur.key) {
                res = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }
    //меньше или равно
    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (key >= cur.key) {
                res = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }
    //больше или равно
    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (key <= cur.key) {
                res = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }
    //больше
    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer res = null;
        while (cur != null) {
            if (key < cur.key) {
                res = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }





    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }



    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }



    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }



    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
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
    public void putAll(Map<? extends Integer, ? extends String> m) {

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
