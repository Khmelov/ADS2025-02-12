package by.it.group410902.kukhto.les12;
class MyRbMap implements java.util.SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }
    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    //минимальный ключ (самый левый узел)
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }
    //максимальный ключ
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    public String toString() {
        if (root == null) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
//рекурсивный обход, левый потомок -> текущий узел -> правый потомок
    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toString(node.right, sb);
        }
    }
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;

        Node node = root;
        while (node != null) {
            if (k < node.key) node = node.left;
            else if (k > node.key) node = node.right;
            else return node.value;
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }
    public String put(Integer key, String value) {
        if (key == null) return null;

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK;
        if (oldValue[0] == null) size++;
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new Node(key, value, RED);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        if (root == null) return null;

        String[] removedValue = new String[1];
        if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
        root = remove(root, k, removedValue);
        if (root != null) root.color = BLACK;
        if (removedValue[0] != null) size--;
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) return null;

        if (key < node.key) {
            if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = remove(node.left, key, removedValue);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.equals(node.key) && node.right == null) {
                removedValue[0] = node.value;
                return null;
            }
            if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.equals(node.key)) {
                removedValue[0] = node.value;
                Node min = min(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = removeMin(node.right);
            } else {
                node.right = remove(node.right, key, removedValue);
            }
        }
        return balance(node);
    }

    private Node removeMin(Node node) {
        if (node.left == null) return null;

        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }

        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node min(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }



    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }


// возвращает все элементы с ключами < toKey
    public java.util.SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key < toKey) {
            headMap(node.left, toKey, result);
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    public java.util.SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }



    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);
        return node;
    }
    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }
    // Остальные методы SortedMap
    public java.util.Comparator<? super Integer> comparator() { return null; }
    public java.util.SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}
