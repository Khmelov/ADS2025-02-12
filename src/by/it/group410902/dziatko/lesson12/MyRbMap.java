package by.it.group410902.dziatko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private static final class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color = RED;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    @Override
    public String toString() {
        if (this.root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node n = this.minimum(root);
        boolean first = true;
        while (n != null) {
            if (!first) sb.append(", ");
            sb.append(n.key).append('=').append(n.value);
            first = false;
            n = this.successor(n);
        }
        sb.append('}');
        return sb.toString();
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Null keys not supported");
        if (root == null) {
            root = new Node(key, value, null);
            root.color = BLACK;
            size = 1;
            return null;
        }

        Node p = root, parent = null;
        int cmp = 0;
        while (p != null) {
            parent = p;
            cmp = this.compareKeys(key, p.key);
            if (cmp < 0) p = p.left;
            else if (cmp > 0) p = p.right;
            else {
                String old = p.value;
                p.value = value;
                return old;
            }
        }

        Node x = new Node(key, value, parent);
        if (cmp < 0) parent.left = x;
        else parent.right = x;

        this.fixAfterInsertion(x);
        size++;
        return null;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right;
                if (colorOf(y) == RED) {
                    setColor(x.parent, BLACK);
                    setColor(y, BLACK);
                    setColor(x.parent.parent, RED);
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        this.rotateLeft(x);
                    }
                    setColor(x.parent, BLACK);
                    setColor(x.parent.parent, RED);
                    this.rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left;
                if (colorOf(y) == RED) {
                    setColor(x.parent, BLACK);
                    setColor(y, BLACK);
                    setColor(x.parent.parent, RED);
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        this.rotateRight(x);
                    }
                    setColor(x.parent, BLACK);
                    setColor(x.parent.parent, RED);
                    this.rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node p = this.findNode(key);
        if (p == null) return null;
        String old = p.value;
        this.deleteNode(p);
        size--;
        return old;
    }

    private void deleteNode(Node p) {
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null) ? p.left : p.right;

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) root = replacement;
            else if (p == p.parent.left) p.parent.left = replacement;
            else p.parent.right = replacement;

            if (colorOf(p) == BLACK) this.fixAfterDeletion(replacement);
        } else if (p.parent == null) {
            root = null;
        } else {
            if (colorOf(p) == BLACK) this.fixAfterDeletion(p);
            if (p.parent != null) {
                if (p == p.parent.left) p.parent.left = null;
                else if (p == p.parent.right) p.parent.right = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    this.rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        this.rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    this.rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    this.rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        this.rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    this.rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    @Override
    public String get(Object keyObj) {
        Integer key = (Integer) keyObj;
        if (key == null) return null;
        Node n = this.findNode(key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        Integer key = (Integer) keyObj;
        if (key == null) return false;
        return this.findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        if (root == null) return false;
        if (!(valueObj instanceof String)) return false;
        String value = (String) valueObj;
        Node n = this.minimum(root);
        while (n != null) {
            if (value == null) {
                if (n.value == null) return true;
            } else {
                if (value.equals(n.value)) return true;
            }
            n = this.successor(n);
        }
        return false;
    }

    @Override
    public Integer firstKey() {
        Node m = this.minimum(root);
        if (m == null) throw new java.util.NoSuchElementException("Map is empty");
        return m.key;
    }

    @Override
    public Integer lastKey() {
        Node m = this.maximum(root);
        if (m == null) throw new java.util.NoSuchElementException("Map is empty");
        return m.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException("toKey is null");
        MyRbMap res = new MyRbMap();
        Node n = this.minimum(root);
        while (n != null && this.compareKeys(n.key, toKey) < 0) {
            res.put(n.key, n.value);
            n = successor(n);
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException("fromKey is null");
        MyRbMap res = new MyRbMap();
        Node n = this.minimum(root);
        while (n != null) {
            if (this.compareKeys(n.key, fromKey) >= 0) {
                res.put(n.key, n.value);
            }
            n = successor(n);
        }
        return res;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    private static boolean colorOf(Node n) { return (n == null) ? BLACK : n.color; }
    private static Node parentOf(Node n) { return (n == null) ? null : n.parent; }
    private static Node leftOf(Node n) { return (n == null) ? null : n.left; }
    private static Node rightOf(Node n) { return (n == null) ? null : n.right; }
    private static void setColor(Node n, boolean c) { if (n != null) n.color = c; }

    private int compareKeys(Integer a, Integer b) {
        return (a < b) ? -1 : (a > b ? 1 : 0);
    }

    private Node findNode(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = this.compareKeys(key, x.key);
            if (cmp == 0) return x;
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    private Node minimum(Node x) {
        if (x == null) return null;
        while (x.left != null) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        if (x == null) return null;
        while (x.right != null) x = x.right;
        return x;
    }

    private Node successor(Node x) {
        if (x == null) return null;
        if (x.right != null) return this.minimum(x.right);
        Node p = x.parent;
        while (p != null && x == p.right) {
            x = p;
            p = p.parent;
        }
        return p;
    }

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
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {return null;}

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Set<Integer> keySet() {return Set.of();}

    @Override
    public Collection<String> values() {return List.of();}

    @Override
    public Set<Entry<Integer, String>> entrySet() {return Set.of();}
}