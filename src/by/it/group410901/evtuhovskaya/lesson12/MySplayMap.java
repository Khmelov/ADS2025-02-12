package by.it.group410901.evtuhovskaya.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
public class MySplayMap implements NavigableMap<Integer, String> {

    //самобалансирующееся дерево, где частоиспользуемые структуры идут ближе к корню

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    private void rotateRight(Node x) {
        Node y = x.left;    //y - левый потомок x, который станет новым корнем поддерева
        x.left = y.right;   //перемещаем правое поддерево у к х

        if (y.right != null) y.right.parent = x; //если у у был правый потомок, теперь его родитель х
        y.parent = x.parent;     // y наследует родителя от x

        if (x.parent == null) root = y; //если x был корнем всего дерева, теперь корнем становится y
        else if (x == x.parent.left) x.parent.left = y; //если x был левым потомком своего родителя
        else x.parent.right = y;

        y.right = x;    //делаем х правым потомком у
        x.parent = y;   //обновляем родителя для х
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

    private void splay(Node x) {
        if (x == null) return;

        while (x.parent != null) {
            if (x.parent.parent == null) {  // родитель является корнем
                if (x == x.parent.left)     //х - левый потомок корня
                    rotateRight(x.parent);  //правый поворот вокруг корня
                else
                    rotateLeft(x.parent);
            }
            else if (x == x.parent.left && x.parent == x.parent.parent.left) { //x - левый потомок, и родитель - левый потомок
                rotateRight(x.parent.parent);   //сначала поворот вокруг дедушки
                rotateRight(x.parent);          //затем поворот вокруг родителя
            }
            else if (x == x.parent.right && x.parent == x.parent.parent.right) {    // x - правый потомок, и родитель - правый потомок
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            }
            else if (x == x.parent.right && x.parent == x.parent.parent.left) { // x - правый потомок, но родитель - левый потомок
                rotateLeft(x.parent);
                rotateRight(x.parent);
            }
            else {
                rotateRight(x.parent);  // x - левый потомок, но родитель - правый потомок
                rotateLeft(x.parent);
            }
        }
    }

    private Node getNode(Integer key) {
        Node t = root;  //корень - начало поиска
        Node last = null;   //последний посещенный узел
        while (t != null) {
            last = t;
            int cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else {
                splay(t);   //перемещаем найденный узел к корню
                return t;
            }
        }
        if (last != null) splay(last);  //если не найден, перемещаем последний посещенный узел к корню
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value)))
            return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node t = root;
        Node parent = null; //запоминаем родителя для нового узла
        int cmp = 0;

        while (t != null) {
            parent = t;

            cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else {
                String old = t.value;
                t.value = value;    //обновляем значение
                splay(t);           //перемещаем обновленный узел к корню
                return old;         //возврат старого значения
            }
        }

        Node n = new Node(key, value, parent);
        if (cmp < 0) parent.left = n;
        else parent.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = getNode((Integer) key); //находим узел для удаления

        if (node == null) return null;  //ключ не нашли

        String oldValue = node.value;   //сохраняем старое значение

        if (node.left != null) {    //у узла есть левое поддерево
            Node maxLeft = maximum(node.left);
            splay(maxLeft); //перемещаем максимальный элемент левого поддерева к корню
            maxLeft.right = node.right; //соединяем его с правым поддеревом node

            if (node.right != null) node.right.parent = maxLeft;    //обновляем родителя

            root = maxLeft;// maxLeft становится новым корнем
            maxLeft.parent = null;
        }
        else if (node.right != null) { // у узла есть только правое поддерево
            root = node.right;  //оно становится корнем
            root.parent = null;
        } else {    //у узла нет потомков
            root = null;    //дерево становится пустым
        }
        size--;
        return oldValue;
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

    private void toStringHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringHelper(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toStringHelper(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringHelper(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private Node minimum(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maximum(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public Integer firstKey() {
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        return maximum(root).key;
    }

    //ищем наибольший ключ, который строго меньше заданного ключа
    private Integer lower(Node node, Integer key) {
        Integer res = null;

        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp <= 0) //Текущий узел ≥ искомого ключа
                node = node.left; //в левом поддереве ключи гарантированно меньше
            else {
                res = node.key; //запоминаем кандидата
                node = node.right;  //ищем кандидата больше
            }
        }
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lower(root, key);
    }

    //наибольший ключ, который меньше или равен заданному ключу
    private Integer floor(Node node, Integer key) {
        Integer res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) //Текущий узел > искомого ключа
                node = node.left;
            else {
                res = node.key;
                node = node.right;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        return floor(root, key);
    }

    //наименьший ключ, который больше или равен заданному ключу
    private Integer ceiling(Node node, Integer key) {
        Integer res = null;

        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp > 0) //Текущий узел < искомого ключа
                node = node.right;
            else {
                res = node.key;
                node = node.left;
            }
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return ceiling(root, key);
    }

    //наименьший ключ, который строго больше заданного ключа
    private Integer higher(Node node, Integer key) {
        Integer res = null;

        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp >= 0) //Текущий узел ≤ искомого ключа
                node = node.right;
            else {
                res = node.key;
                node = node.left;
            }
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        return higher(root, key);
    }

    // headMap / tailMap
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        fillHeadMap(root, toKey, map);
        return map;
    }

    private void fillHeadMap(Node node, Integer toKey, MySplayMap map) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
            fillHeadMap(node.left, toKey, map);
            fillHeadMap(node.right, toKey, map);
        } else {
            fillHeadMap(node.left, toKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        fillTailMap(root, fromKey, map);
        return map;
    }

    private void fillTailMap(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            fillTailMap(node.left, fromKey, map);
            fillTailMap(node.right, fromKey, map);
        } else {
            fillTailMap(node.right, fromKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return headMap(toKey);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return tailMap(fromKey);
    }

    // ===== Остальные методы NavigableMap =====
    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
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

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    // ===== Map интерфейс =====
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}