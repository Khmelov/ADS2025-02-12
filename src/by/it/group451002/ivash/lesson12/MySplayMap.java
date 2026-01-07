    package by.it.group451002.ivash.lesson12;

    import java.util.*;

    public class MySplayMap implements NavigableMap<Integer, String> {

        private class Node {
            Integer key;
            String value;
            Node left, right, parent;

            Node(Integer key, String value) {
                this.key = key;
                this.value = value;
            }
        }

        private Node root;
        private int size;

        // ================== SPLAY OPERATIONS ==================
        private void rotateRight(Node x) {
            Node y = x.left;
            if (y != null) {
                x.left = y.right;
                if (y.right != null) y.right.parent = x;
                y.parent = x.parent;
            }
            if (x.parent == null) root = y;
            else if (x == x.parent.right) x.parent.right = y;
            else x.parent.left = y;
            if (y != null) y.right = x;
            x.parent = y;
        }

        private void rotateLeft(Node x) {
            Node y = x.right;
            if (y != null) {
                x.right = y.left;
                if (y.left != null) y.left.parent = x;
                y.parent = x.parent;
            }
            if (x.parent == null) root = y;
            else if (x == x.parent.left) x.parent.left = y;
            else x.parent.right = y;
            if (y != null) y.left = x;
            x.parent = y;
        }

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

        private Node findNode(Integer key) {
            Node x = root;
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if (cmp < 0) x = x.left;
                else if (cmp > 0) x = x.right;
                else return x;
            }
            return null;
        }

        // ================== MAP METHODS ==================

        @Override
        public String put(Integer key, String value) {
            if (root == null) {
                root = new Node(key, value);
                size = 1;
                return null;
            }
            Node x = root;
            Node parent = null;
            int cmp = 0;
            while (x != null) {
                parent = x;
                cmp = key.compareTo(x.key);
                if (cmp < 0) x = x.left;
                else if (cmp > 0) x = x.right;
                else {
                    String old = x.value;
                    x.value = value;
                    splay(x);
                    return old;
                }
            }
            Node n = new Node(key, value);
            n.parent = parent;
            if (cmp < 0) parent.left = n;
            else parent.right = n;
            splay(n);
            size++;
            return null;
        }

        @Override
        public String get(Object key) {
            Node n = findNode((Integer) key);
            if (n != null) splay(n);
            return n != null ? n.value : null;
        }

        @Override
        public boolean containsKey(Object key) {
            Node n = findNode((Integer) key);
            if (n != null) splay(n);
            return n != null;
        }

        @Override
        public boolean containsValue(Object value) {
            return containsValue(root, value);
        }

        private boolean containsValue(Node x, Object value) {
            if (x == null) return false;
            if (Objects.equals(x.value, value)) return true;
            return containsValue(x.left, value) || containsValue(x.right, value);
        }

        @Override
        public String remove(Object key) {
            Node n = findNode((Integer) key);
            if (n == null) return null;
            splay(n);
            String old = n.value;
            if (n.left == null) transplant(n, n.right);
            else if (n.right == null) transplant(n, n.left);
            else {
                Node y = minimum(n.right);
                if (y.parent != n) {
                    transplant(y, y.right);
                    y.right = n.right;
                    y.right.parent = y;
                }
                transplant(n, y);
                y.left = n.left;
                y.left.parent = y;
            }
            size--;
            return old;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends String> m) {

        }

        private void transplant(Node u, Node v) {
            if (u.parent == null) root = v;
            else if (u == u.parent.left) u.parent.left = v;
            else u.parent.right = v;
            if (v != null) v.parent = u.parent;
        }

        private Node minimum(Node x) {
            while (x.left != null) x = x.left;
            return x;
        }

        private Node maximum(Node x) {
            while (x.right != null) x = x.right;
            return x;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            root = null;
            size = 0;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public Integer firstKey() {
            if (root == null) throw new NoSuchElementException();
            Node n = minimum(root);
            splay(n);
            return n.key;
        }

        @Override
        public Integer lastKey() {
            if (root == null) throw new NoSuchElementException();
            Node n = maximum(root);
            splay(n);
            return n.key;
        }

        @Override
        public Integer lowerKey(Integer key) {
            Node x = root;
            Integer res = null;
            while (x != null) {
                if (x.key < key) {
                    res = x.key;
                    x = x.right;
                } else {
                    x = x.left;
                }
            }
            return res;
        }

        @Override
        public Integer floorKey(Integer key) {
            Node x = root;
            Integer res = null;
            while (x != null) {
                if (x.key <= key) {
                    res = x.key;
                    x = x.right;
                } else {
                    x = x.left;
                }
            }
            return res;
        }

        @Override
        public Integer ceilingKey(Integer key) {
            Node x = root;
            Integer res = null;
            while (x != null) {
                if (x.key >= key) {
                    res = x.key;
                    x = x.left;
                } else {
                    x = x.right;
                }
            }
            return res;
        }

        @Override
        public Integer higherKey(Integer key) {
            Node x = root;
            Integer res = null;
            while (x != null) {
                if (x.key > key) {
                    res = x.key;
                    x = x.left;
                } else {
                    x = x.right;
                }
            }
            return res;
        }

        @Override
        public MySplayMap headMap(Integer toKey) {
            MySplayMap map = new MySplayMap();
            headMap(root, toKey, map);
            return map;
        }

        private void headMap(Node x, Integer toKey, MySplayMap map) {
            if (x == null) return;
            headMap(x.left, toKey, map);
            if (x.key.compareTo(toKey) < 0) map.put(x.key, x.value);
            headMap(x.right, toKey, map);
        }

        @Override
        public MySplayMap tailMap(Integer fromKey) {
            MySplayMap map = new MySplayMap();
            tailMap(root, fromKey, map);
            return map;
        }

        private void tailMap(Node x, Integer fromKey, MySplayMap map) {
            if (x == null) return;
            tailMap(x.left, fromKey, map);
            if (x.key.compareTo(fromKey) >= 0) map.put(x.key, x.value);
            tailMap(x.right, fromKey, map);
        }

        // ================== toString ==================
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            toString(root, sb);
            if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove last ", "
            sb.append("}");
            return sb.toString();
        }

        private void toString(Node x, StringBuilder sb) {
            if (x == null) return;
            toString(x.left, sb);
            sb.append(x.key).append("=").append(x.value).append(", ");
            toString(x.right, sb);
        }

        // ================== UNUSED METHODS ==================
        @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {throw new UnsupportedOperationException();}
        @Override public NavigableMap<Integer, String> descendingMap() {throw new UnsupportedOperationException();}
        @Override public Set<Integer> keySet() {throw new UnsupportedOperationException();}
        @Override public Collection<String> values() {throw new UnsupportedOperationException();}
        @Override public Set<Entry<Integer, String>> entrySet() {throw new UnsupportedOperationException();}
        @Override public NavigableSet<Integer> navigableKeySet() {throw new UnsupportedOperationException();}

        @Override
        public Comparator<? super Integer> comparator() {
            return null;
        }

        @Override public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {throw new UnsupportedOperationException();}
        @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {throw new UnsupportedOperationException();}
        @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> lowerEntry(Integer key) {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> floorEntry(Integer key) {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> ceilingEntry(Integer key) {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> higherEntry(Integer key) {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> firstEntry() {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> lastEntry() {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> pollFirstEntry() {throw new UnsupportedOperationException();}
        @Override public Entry<Integer, String> pollLastEntry() {throw new UnsupportedOperationException();}
        @Override public NavigableSet<Integer> descendingKeySet() {throw new UnsupportedOperationException();}
    }
