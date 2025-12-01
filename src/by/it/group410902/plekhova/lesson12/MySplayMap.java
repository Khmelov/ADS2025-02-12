package by.it.group410902.plekhova.lesson12;

import java.util.*;

/*
* Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    и работает на основе splay-дерева
* */

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size = 0;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer k, String v) {
            key = k;
            value = v;
        }
    }



    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
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
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    /** Подтягивание (splay) узла x к корню */
    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.right == x.parent) {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            } else {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            }
        }
    }

    private Node subtreeMin(Node x) {
        if (x == null) return null;
        while (x.left != null) x = x.left;
        return x;
    }

    private Node subtreeMax(Node x) {
        if (x == null) return null;
        while (x.right != null) x = x.right;
        return x;
    }

    private void replaceRoot(Node n) {
        root = n;
        if (n != null) n.parent = null;
    }



    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("null keys not supported");
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node z = root, p = null;
        while (z != null) {
            p = z;
            int cmp = key.compareTo(z.key);
            if (cmp == 0) {
                String old = z.value;
                z.value = value;
                splay(z);
                return old;
            } else if (cmp < 0) z = z.left;
            else z = z.right;
        }
        Node n = new Node(key, value);
        n.parent = p;
        if (key.compareTo(p.key) < 0) p.left = n;
        else p.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String get(Object k) {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node z = root;
        while (z != null) {
            int cmp = key.compareTo(z.key);
            if (cmp == 0) {
                splay(z);
                return z.value;
            } else if (cmp < 0) z = z.left;
            else z = z.right;
        }
        return null;
    }

    @Override
    public String remove(Object k) {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node z = root;
        while (z != null) {
            int cmp = key.compareTo(z.key);
            if (cmp == 0) break;
            else if (cmp < 0) z = z.left;
            else z = z.right;
        }
        if (z == null) return null;
        splay(z);
        String old = z.value;

        // стандартный способ удаления в splay: разделить и соединить
        if (z.left == null) {
            replaceRoot(z.right);
        } else {
            Node left = z.left;
            Node right = z.right;
            left.parent = null;
            // назначаем корнем левое поддерево, подтягиваем максимум в него
            root = left;
            Node m = subtreeMax(left);
            splay(m); // теперь m — корень, m.right == null
            m.right = right;
            if (right != null) right.parent = m;
            root = m;
            root.parent = null;
        }
        size--;
        return old;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object v) {
        if (n == null) return false;
        if (v == null ? n.value == null : v.equals(n.value)) return true;
        return containsValueRec(n.left, v) || containsValueRec(n.right, v);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }



    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException("Map is empty");
        Node m = subtreeMin(root);
        splay(m);
        return m.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException("Map is empty");
        Node m = subtreeMax(root);
        splay(m);
        return m.key;
    }

    // возвращает наибольший ключ, который строго меньше key
    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root, candidate = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp < 0) { candidate = cur; cur = cur.right; }
            else cur = cur.left;
        }
        return candidate == null ? null : candidate.key;
    }

    //возвращает наибольший ключ, который меньше или равен key
    @Override
    public Integer floorKey(Integer key) {
        Node cur = root, candidate = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp == 0) { candidate = cur; break; }
            if (cmp < 0) { candidate = cur; cur = cur.right; }
            else cur = cur.left;
        }
        return candidate == null ? null : candidate.key;
    }

    //возвращает наименьший ключ, который больше или равен key
    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root, candidate = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp == 0) { candidate = cur; break; }
            if (cmp > 0) { candidate = cur; cur = cur.left; }
            else cur = cur.right;
        }
        return candidate == null ? null : candidate.key;
    }

    //возвращает наименьший ключ, который строго больше key
    @Override
    public Integer higherKey(Integer key) {
        Node cur = root, candidate = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp > 0) { candidate = cur; cur = cur.left; }
            else cur = cur.right;
        }
        return candidate == null ? null : candidate.key;
    }


    @Override public Map.Entry<Integer, String> lowerEntry(Integer key)  { Integer k = lowerKey(key);  return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k)); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key)  { Integer k = floorKey(key);  return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k)); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key){ Integer k = ceilingKey(key);return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k)); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { Integer k = higherKey(key); return k == null ? null : new AbstractMap.SimpleEntry<>(k, get(k)); }

    @Override public Map.Entry<Integer, String> firstEntry() { if (isEmpty()) return null; Integer k = firstKey(); return new AbstractMap.SimpleEntry<>(k, get(k)); }
    @Override public Map.Entry<Integer, String> lastEntry()  { if (isEmpty()) return null; Integer k = lastKey();  return new AbstractMap.SimpleEntry<>(k, get(k)); }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        if (isEmpty()) return null;
        Integer k = firstKey();
        String v = remove(k);
        return new AbstractMap.SimpleEntry<>(k, v);
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        if (isEmpty()) return null;
        Integer k = lastKey();
        String v = remove(k);
        return new AbstractMap.SimpleEntry<>(k, v);
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean[] first = new boolean[]{true};
        inorderToString(root, sb, first);
        sb.append('}');
        return sb.toString();
    }

    private void inorderToString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        inorderToString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        inorderToString(n.right, sb, first);
    }


    //  возвращает все элементы карты Map, где ключ <= fromKey
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        headRec(root, res, toKey, inclusive);
        return res;
    }

    private void headRec(Node n, MySplayMap res, Integer toKey, boolean inclusive) {
        if (n == null) return;
        headRec(n.left, res, toKey, inclusive);
        int cmp = n.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) res.put(n.key, n.value);
        headRec(n.right, res, toKey, inclusive);
    }

    //  возвращает все элементы карты Map, где ключ ≥ fromKey
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap res = new MySplayMap();
        tailRec(root, res, fromKey, inclusive);
        return res;
    }

    private void tailRec(Node n, MySplayMap res, Integer fromKey, boolean inclusive) {
        if (n == null) return;
        tailRec(n.left, res, fromKey, inclusive);
        int cmp = n.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) res.put(n.key, n.value);
        tailRec(n.right, res, fromKey, inclusive);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap res = new MySplayMap();
        subRec(root, res, fromKey, fromInclusive, toKey, toInclusive);
        return res;
    }

    private void subRec(Node n, MySplayMap res, Integer fromKey, boolean fromInc, Integer toKey, boolean toInc) {
        if (n == null) return;
        subRec(n.left, res, fromKey, fromInc, toKey, toInc);
        boolean geFrom = (n.key.compareTo(fromKey) > 0) || (fromInc && n.key.compareTo(fromKey) == 0);
        boolean ltTo = (n.key.compareTo(toKey) < 0) || (toInc && n.key.compareTo(toKey) == 0);
        if (geFrom && ltTo) res.put(n.key, n.value);
        subRec(n.right, res, fromKey, fromInc, toKey, toInc);
    }



    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> s = new LinkedHashSet<>();
        fillKeys(root, s);
        return s;
    }

    private void fillKeys(Node n, Set<Integer> s) {
        if (n == null) return;
        fillKeys(n.left, s);
        s.add(n.key);
        fillKeys(n.right, s);
    }

    @Override
    public Collection<String> values() {
        List<String> v = new ArrayList<>();
        fillValues(root, v);
        return v;
    }

    private void fillValues(Node n, Collection<String> c) {
        if (n == null) return;
        fillValues(n.left, c);
        c.add(n.value);
        fillValues(n.right, c);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> s = new LinkedHashSet<>();
        fillEntries(root, s);
        return s;
    }

    private void fillEntries(Node n, Set<Entry<Integer, String>> s) {
        if (n == null) return;
        fillEntries(n.left, s);
        s.add(new AbstractMap.SimpleEntry<>(n.key, n.value));
        fillEntries(n.right, s);
    }



    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException("descendingMap not implemented"); }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        // возвращаем стандартный NavigableSet на основе ключей
        return new TreeSet<>(keySet());
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        NavigableSet<Integer> s = navigableKeySet();
        return s.descendingSet();
    }

    @Override public NavigableMap<Integer, String> headMap(Integer toKey) { return headMap(toKey, false); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey) { return tailMap(fromKey, true); }

    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

}
