package by.it.group451003.sirotkin.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class MySplayMap implements NavigableMap<Integer, String> {
    private Node root;
    private int size = 0;

    public MySplayMap() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        this.toStringInOrder(this.root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }

    private void toStringInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            this.toStringInOrder(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            this.toStringInOrder(node.right, sb);
        }

    }

    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        } else {
            Node node = this.findNode(key);
            if (node != null && node.key.equals(key)) {
                String oldValue = node.value;
                node.value = value;
                this.splay(node);
                return oldValue;
            } else {
                Node newNode = new Node(key, value);
                if (this.root == null) {
                    this.root = newNode;
                } else if (key < node.key) {
                    node.left = newNode;
                    newNode.parent = node;
                } else {
                    node.right = newNode;
                    newNode.parent = node;
                }

                ++this.size;
                this.splay(newNode);
                return null;
            }
        }
    }

    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Integer)) {
            return null;
        } else {
            Integer intKey = (Integer)key;
            Node node = this.findNode(intKey);
            if (node != null && node.key.equals(intKey)) {
                String removedValue = node.value;
                this.splay(node);
                if (node.left == null) {
                    this.root = node.right;
                    if (this.root != null) {
                        this.root.parent = null;
                    }
                } else if (node.right == null) {
                    this.root = node.left;
                    if (this.root != null) {
                        this.root.parent = null;
                    }
                } else {
                    Node maxLeft = this.findMax(node.left);
                    this.splay(maxLeft);
                    maxLeft.right = node.right;
                    if (node.right != null) {
                        node.right.parent = maxLeft;
                    }

                    this.root = maxLeft;
                }

                --this.size;
                return removedValue;
            } else {
                return null;
            }
        }
    }

    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Integer)) {
            return null;
        } else {
            Integer intKey = (Integer)key;
            Node node = this.findNode(intKey);
            if (node != null && node.key.equals(intKey)) {
                this.splay(node);
                return node.value;
            } else {
                return null;
            }
        }
    }

    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    public boolean containsValue(Object value) {
        return this.containsValue(this.root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        } else if (value == null && node.value == null || value != null && value.equals(node.value)) {
            this.splay(node);
            return true;
        } else {
            return this.containsValue(node.left, value) || this.containsValue(node.right, value);
        }
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Integer lowerKey(Integer key) {
        Node node = this.findLower(key);
        return node != null ? node.key : null;
    }

    public Integer floorKey(Integer key) {
        Node node = this.findFloor(key);
        return node != null ? node.key : null;
    }

    public Integer ceilingKey(Integer key) {
        Node node = this.findCeiling(key);
        return node != null ? node.key : null;
    }

    public Integer higherKey(Integer key) {
        Node node = this.findHigher(key);
        return node != null ? node.key : null;
    }

    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return this.headMap(toKey, false);
    }

    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap headMap = new MySplayMap();
        this.headMap(this.root, toKey, inclusive, headMap);
        return headMap;
    }

    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return this.tailMap(fromKey, true);
    }

    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap tailMap = new MySplayMap();
        this.tailMap(this.root, fromKey, inclusive, tailMap);
        return tailMap;
    }

    public Integer firstKey() {
        if (this.root == null) {
            throw new NoSuchElementException();
        } else {
            Node node = this.findMin(this.root);
            this.splay(node);
            return node.key;
        }
    }

    public Integer lastKey() {
        if (this.root == null) {
            throw new NoSuchElementException();
        } else {
            Node node = this.findMax(this.root);
            this.splay(node);
            return node.key;
        }
    }

    public Comparator<? super Integer> comparator() {
        return null;
    }

    private Node findNode(Integer key) {
        Node current = this.root;
        Node parent = null;

        while(current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;
            } else {
                if (key <= current.key) {
                    return current;
                }

                current = current.right;
            }
        }

        return parent;
    }

    private Node findLower(Integer key) {
        Node current = this.root;
        Node candidate = null;

        while(current != null) {
            if (current.key < key) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (candidate != null) {
            this.splay(candidate);
        }

        return candidate;
    }

    private Node findFloor(Integer key) {
        Node current = this.root;
        Node candidate = null;

        while(current != null) {
            if (current.key <= key) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        if (candidate != null) {
            this.splay(candidate);
        }

        return candidate;
    }

    private Node findCeiling(Integer key) {
        Node current = this.root;
        Node candidate = null;

        while(current != null) {
            if (current.key >= key) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (candidate != null) {
            this.splay(candidate);
        }

        return candidate;
    }

    private Node findHigher(Integer key) {
        Node current = this.root;
        Node candidate = null;

        while(current != null) {
            if (current.key > key) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (candidate != null) {
            this.splay(candidate);
        }

        return candidate;
    }

    private Node findMin(Node node) {
        while(node.left != null) {
            node = node.left;
        }

        return node;
    }

    private Node findMax(Node node) {
        while(node.right != null) {
            node = node.right;
        }

        return node;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap headMap) {
        if (node != null) {
            label16: {
                this.headMap(node.left, toKey, inclusive, headMap);
                if (inclusive) {
                    if (node.key > toKey) {
                        break label16;
                    }
                } else if (node.key >= toKey) {
                    break label16;
                }

                headMap.put(node.key, node.value);
            }

            this.headMap(node.right, toKey, inclusive, headMap);
        }

    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap tailMap) {
        if (node != null) {
            label16: {
                this.tailMap(node.left, fromKey, inclusive, tailMap);
                if (inclusive) {
                    if (node.key < fromKey) {
                        break label16;
                    }
                } else if (node.key <= fromKey) {
                    break label16;
                }

                tailMap.put(node.key, node.value);
            }

            this.tailMap(node.right, fromKey, inclusive, tailMap);
        }

    }

    private void splay(Node node) {
        while(node.parent != null) {
            if (node.parent.parent == null) {
                if (node == node.parent.left) {
                    this.rotateRight(node.parent);
                } else {
                    this.rotateLeft(node.parent);
                }
            } else if (node == node.parent.left && node.parent == node.parent.parent.left) {
                this.rotateRight(node.parent.parent);
                this.rotateRight(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.right) {
                this.rotateLeft(node.parent.parent);
                this.rotateLeft(node.parent);
            } else if (node == node.parent.right && node.parent == node.parent.parent.left) {
                this.rotateLeft(node.parent);
                this.rotateRight(node.parent);
            } else {
                this.rotateRight(node.parent);
                this.rotateLeft(node.parent);
            }
        }

        this.root = node;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }

            y.parent = x.parent;
        }

        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        if (y != null) {
            y.left = x;
        }

        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        if (x != null) {
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }

            x.parent = y.parent;
        }

        if (y.parent == null) {
            this.root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        if (x != null) {
            x.right = y;
        }

        y.parent = x;
    }

    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
