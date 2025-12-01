package by.it.group410902.jalilova.lesson12;

class MySplayMap implements java.util.NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;


    public MySplayMap() {
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

    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toString(node.right, sb);
        }
    }

    public Integer firstKey() { // идем по левым ссылкам до самого малого ключа
        if (root == null) throw new java.util.NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        root = splay(root, node.key);
        return node.key;
    }

    public Integer lastKey() { // идем по правым ссылкам до самого большого ключа
        if (root == null) throw new java.util.NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        root = splay(root, node.key);
        return node.key;
    }

    public String put(Integer key, String value) {
        if (key == null) return null;

        if (root == null) { //дерево пустое
            root = new Node(key, value); //новый узел - корень
            size++;
            return null;
        }

        root = splay(root, key); //подтягивает ближайший узел с этим ключом (или место где он должен быть)
// если ключ совпал - обновляем значение
        if (key.equals(root.key)) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
// если ключ меньше/больше создаем новый узел
        Node newNode = new Node(key, value);
        if (key < root.key) { //делаем новый корень, его правое поддерево, левое - всё что меньше ключа
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else { //наоборот
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }


    public String remove(Integer key) {
        if (key == null || root == null) return null;

        root = splay(root, key); //подтягиваем нужный ключ к корню

        if (!key.equals(root.key)) {
            return null;
        }

        String removedValue = root.value;

        if (root.left == null) { //левое поддерево пустое
            root = root.right; //корень становится правым
        } else { //берем правое поддерево
            Node newRoot = root.right;
            if (newRoot == null) {
                root = root.left;
            } else { //сплеим по тому же ключу
                newRoot = splay(newRoot, key);
                newRoot.left = root.left; //присоединяем левое дерево
                root = newRoot;
            }
        }
        size--;
        return removedValue;
    }
    public String get(Integer key) {
        if (key == null || root == null) return null;

        root = splay(root, key);
        return key.equals(root.key) ? root.value : null;
    }

    public boolean containsKey(Integer key) {
        return get(key) != null;
    }

    public boolean containsValue(String value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value))) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    public java.util.NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        if (node.key < toKey) {
            headMap(node.left, toKey, result);
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    public java.util.NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    public Integer lowerKey(Integer key) { //находит ближайший меньший чем ключ
        if (root == null) return null;
        root = splay(root, key);
        if (root.key < key) return root.key;
        if (root.left != null) {
            Node node = root.left;
            while (node.right != null) node = node.right;
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    public Integer floorKey(Integer key) { //меньший или равный ключу
        if (root == null) return null;
        root = splay(root, key);
        if (root.key <= key) return root.key;
        if (root.left != null) {
            Node node = root.left;
            while (node.right != null) node = node.right;
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    public Integer ceilingKey(Integer key) { //больший или равный ключу
        if (root == null) return null;
        root = splay(root, key);
        if (root.key >= key) return root.key;
        if (root.right != null) {
            Node node = root.right;
            while (node.left != null) node = node.left;
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    public Integer higherKey(Integer key) { //больший чем ключ
        if (root == null) return null;
        root = splay(root, key);
        if (root.key > key) return root.key;
        if (root.right != null) {
            Node node = root.right;
            while (node.left != null) node = node.left;
            root = splay(root, node.key);
            return node.key;
        }
        return null;
    }

    private Node splay(Node node, Integer key) {
        if (node == null) return null; //нашли узел, возвращаем его

        if (key < node.key) { //ключ в левом поддереве
            if (node.left == null) return node;
            if (key < node.left.key) { // ключ в левом поддереве левого zig zig (левый - левый)
                node.left.left = splay(node.left.left, key); //рекурсивно сплеим
                node = rotateRight(node); //двойной правый поворот
            } else if (key > node.left.key) { //ключ в правом поддереве левого поддерева zig zag (левый - правый)
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left); //левый поворот
                }
            }
            return node.left == null ? node : rotateRight(node); //финальный поворот zig
        } else if (key > node.key) { // ключ в правом поддереве
            if (node.right == null) return node;
            if (key > node.right.key) { // ключ в правом поддереве правого zag zag (правый - правый)
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (key < node.right.key) { //ключ в левом поддереве правого zag zig (правый - левый)
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }
            return node.right == null ? node : rotateLeft(node); //финальный поворот zag
        } else {
            return node;
        }
    }

    private Node rotateRight(Node node) {
        // запоминаем левого потомка текущего корня как новый корень
        Node newRoot = node.left;
        // переподвешиваем правого потомка нового корня к левому потомку старого
        node.left = newRoot.right;
        // старый корень становится правым потомком нового корня
        newRoot.right = node;
        //возвращаем новый корень
        return newRoot;
    }

    private Node rotateLeft(Node node) {
        // запоминаем правого потомка текущего корня как новый корень
        Node newRoot = node.right;
        // переподвешиваем левого потомка нового корня к правому потомку старого
        node.right = newRoot.left;
        // старый корень становится левым потомком нового корня
        newRoot.left = node;
        //возвращаем новый корень
        return newRoot;
    }

    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    public java.util.Map.Entry<Integer, String> firstEntry() { return null; }
    public java.util.Map.Entry<Integer, String> lastEntry() { return null; }
    public java.util.Map.Entry<Integer, String> pollFirstEntry() { return null; }
    public java.util.Map.Entry<Integer, String> pollLastEntry() { return null; }
    public java.util.NavigableMap<Integer, String> descendingMap() { return null; }
    public java.util.NavigableSet<Integer> navigableKeySet() { return null; }
    public java.util.NavigableSet<Integer> descendingKeySet() { return null; }
    public java.util.NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    public java.util.NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    public java.util.NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    public java.util.NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    public java.util.Comparator<? super Integer> comparator() { return null; }
    public java.util.Set<Integer> keySet() { return null; }
    public java.util.Collection<String> values() { return null; }
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { return null; }
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) { }
    public String remove(Object key) { return remove((Integer) key); }
    public boolean containsKey(Object key) { return key instanceof Integer && containsKey((Integer) key); }
    public boolean containsValue(Object value) { return value instanceof String && containsValue((String) value); }
    public String get(Object key) { return key instanceof Integer ? get((Integer) key) : null; }
}