package by.it.group410902.harkavy.lesson12;

import java.util.*;

/** Минимальная NavigableMap на splay-дереве. */
public class MySplayMap implements NavigableMap<Integer, String> {

    // ---- узел дерева ----
    private static final class Node {
        int key;
        String val;
        Node left, right, parent;
        Node(int k, String v) { key = k; val = v; }
    }

    private Node root;
    private int size = 0;

    // ---- базовые повороты ----
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y; else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y; else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    // ---- splay поднимает x в корень ----
    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent, g = p.parent;
            if (g == null) { // zig
                if (x == p.left) rotateRight(p); else rotateLeft(p);
            } else if (x == p.left && p == g.left) { // zig-zig
                rotateRight(g); rotateRight(p);
            } else if (x == p.right && p == g.right) { // zig-zig
                rotateLeft(g); rotateLeft(p);
            } else if (x == p.right && p == g.left) { // zig-zag
                rotateLeft(p); rotateRight(g);
            } else { // x == p.left && p == g.right
                rotateRight(p); rotateLeft(g);
            }
        }
    }

    // ---- поиск узла по ключу (возвращает последний посещённый для последующего splay) ----
    private Node findLast(int key) {
        Node x = root, last = null;
        while (x != null) {
            last = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else return x;
        }
        return last;
    }

    // ---- максимум в поддереве ----
    private Node max(Node x) { while (x != null && x.right != null) x = x.right; return x; }
    private Node min(Node x) { while (x != null && x.left  != null) x = x.left;  return x; }

    // ---- склейка двух деревьев L и R, где все ключи L < все ключи R ----
    private Node join(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;
        Node m = max(L); // splay максимум слева и подвешиваем справа R
        splay(m);
        m.right = R;
        R.parent = m;
        return m;
    }

    // =====================================================================
    //                               ОБЯЗАТЕЛЬНО
    // =====================================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(sb, root);
        return sb.append("}").toString();
    }

    private void appendInOrder(StringBuilder sb, Node x) {
        if (x == null) return;
        appendInOrder(sb, x.left);
        if (sb.length() > 1) sb.append(", ");
        sb.append(x.key).append("=").append(x.val);
        appendInOrder(sb, x.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) { root = new Node(key, value); size = 1; return null; }

        Node last = findLast(key);
        if (last != null) splay(last); // подтянем ближайший к корню

        if (root.key == key) {
            String old = root.val;
            root.val = value;
            return old;
        }

        Node n = new Node(key, value);
        if (key < root.key) {
            // новый ключ меньше корня: левая часть остаётся слева
            n.left = root.left;
            if (n.left != null) n.left.parent = n;
            n.right = root;
            root.left = null;
            root.parent = n;
        } else {
            // новый ключ больше корня: правая часть остаётся справа
            n.right = root.right;
            if (n.right != null) n.right.parent = n;
            n.left = root;
            root.right = null;
            root.parent = n;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        if (root == null) return null;

        Node last = findLast(key);
        if (last != null) splay(last); // корень становится ближайшим к key

        if (root.key != key) return null; // такого ключа нет
        String old = root.val;

        // отделяем левое и правое поддеревья, затем склеиваем
        Node L = root.left;  if (L != null)  L.parent = null;
        Node R = root.right; if (R != null) R.parent = null;
        root.left = root.right = null;
        root = join(L, R);
        size--;
        return old;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        if (root == null) return null;
        Node last = findLast(key);
        if (last != null) splay(last);
        return (root != null && root.key == key) ? root.val : null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return get(keyObj) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // простой симметричный обход
        return containsValueDfs(root, value);
    }

    private boolean containsValueDfs(Node x, Object v) {
        if (x == null) return false;
        if (v == null ? x.val == null : v.equals(x.val)) return true;
        return containsValueDfs(x.left, v) || containsValueDfs(x.right, v);
    }

    @Override public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        // все ключи < toKey
        MySplayMap res = new MySplayMap();
        copyIf(root, res, Integer.MIN_VALUE, toKey, false, false);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        // все ключи >= fromKey
        MySplayMap res = new MySplayMap();
        copyIf(root, res, fromKey, Integer.MAX_VALUE, true, true);
        return res;
    }

    private void copyIf(Node x, MySplayMap dst,
                        int lo, int hi, boolean includeLo, boolean includeHi) {
        if (x == null) return;
        if (x.key > lo || (includeLo && x.key == lo)) copyIf(x.left, dst, lo, hi, includeLo, includeHi);
        boolean geLo = includeLo ? x.key >= lo : x.key > lo;
        boolean leHi = includeHi ? x.key <= hi : x.key < hi;
        if (geLo && leHi) dst.put(x.key, x.val);
        if (x.key < hi || (includeHi && x.key == hi)) copyIf(x.right, dst, lo, hi, includeLo, includeHi);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return max(root).key;
    }

    // ---- навигация по ключам ----
    @Override
    public Integer lowerKey(Integer key) {
        // строго меньше key
        Node x = root; Integer best = null;
        while (x != null) {
            if (key <= x.key) x = x.left;
            else { best = x.key; x = x.right; }
        }
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        // меньше либо равно key
        Node x = root; Integer best = null;
        while (x != null) {
            if (key < x.key) x = x.left;
            else { best = x.key; x = x.right; }
        }
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        // больше либо равно key
        Node x = root; Integer best = null;
        while (x != null) {
            if (key > x.key) x = x.right;
            else { best = x.key; x = x.left; }
        }
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        // строго больше key
        Node x = root; Integer best = null;
        while (x != null) {
            if (key >= x.key) x = x.right;
            else { best = x.key; x = x.left; }
        }
        return best;
    }

    // =====================================================================
    //                     ПРОЧИЕ МЕТОДЫ ИНТЕРФЕЙСА — НЕ НУЖНЫ
    // =====================================================================
    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }

    // NavigableMap дополнительные entry-варианты
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> firstEntry() { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> lastEntry() { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { unsupported(); return null; }
    @Override public Map.Entry<Integer, String> pollLastEntry() { unsupported(); return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { unsupported(); return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { unsupported(); return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { unsupported(); return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { unsupported(); return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { unsupported(); return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { unsupported(); return null; }

    // SortedMap/Map обязательные сигнатуры (не требуются заданием)
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { unsupported(); return null; }
    @Override public Set<Integer> keySet() { unsupported(); return null; }
    @Override public Collection<String> values() { unsupported(); return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { unsupported(); return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { unsupported(); }
}
