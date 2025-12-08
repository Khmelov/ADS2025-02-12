package by.it.group410901.bandarzheuskaya.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

@SuppressWarnings("unchecked")
public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        // null означает естественный порядок ключей
        return null;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;   //у - правый потомок х
        x.right = y.left;   // перемещаем левое поддерево у к х
        if (y.left != null) y.left.parent = x;  //если у y был левый потомок B, теперь его родителем становится x
        y.parent = x.parent;    //обновляем родителя у
        if (x.parent == null) root = y; //если x был корнем, теперь корнем становится y
        else if (x == x.parent.left) x.parent.left = y; //если x был левым потомком своего родителя
        else x.parent.right = y;//если x был правым потомком своего родителя
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    //восстанавливает св-ва красно-черного дерева после вставки
    private void fixAfterInsertion(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            //родитель z - левый потомок дедушки
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; //дядя-правый потомок дедушки
                //если дядя красный
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK; //родитель черный
                    y.color = BLACK; //дядя черный
                    z.parent.parent.color = RED; //дедушка красный
                    z = z.parent.parent; //переходим к дедушке и проверяем выше
                } else { //дядя черный
                    if (z == z.parent.right) { //z - правый потомок
                        z = z.parent;
                        rotateLeft(z); //поворачиваем влево для послед изменений цветов
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else { //родитель z - правый потомок дедушки
                Node y = z.parent.parent.left;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    //добавляем новое значение или обновляем, если ключ уже сущ
    public String put(Integer key, String value) {
        if (root == null) { //проверка пустого дерева
            root = new Node(key, value, BLACK, null);
            size++;
            return null;
        }

        Node t = root;
        Node parent = null;
        int cmp = 0;

        while (t != null) {
            parent = t; //запоминаем текущий узел как родителя

            cmp = key.compareTo(t.key);

            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else {      //ключ существует - обновляем значение
                String old = t.value;
                t.value = value;
                return old;
            }
        }
        Node e = new Node(key, value, RED, parent); //создаем новый узел - всегда красный
        if (cmp < 0) parent.left = e;
        else parent.right = e;

        fixAfterInsertion(e);
        size++;
        return null;
    }

    private Node getNode(Integer key) {
        Node t = root;
        while (t != null) {
            int cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else return t;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
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

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public String remove(Object key) {
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    private void deleteNode(Node z) {
        if (z.left != null && z.right != null) {    //у удаляемого узла два потомка
            Node y = minimum(z.right);  //преемник - минимальный узел в правом поддереве
            z.key = y.key; //копируем
            z.value = y.value;
            z = y; //удаляем преемника
        }

        Node replacement = (z.left != null) ? z.left : z.right; //находим замену для удаляемого узла

        if (replacement != null) { //есть хотя бы один потомок
            replacement.parent = z.parent; //пропускаем удаляемый узел, связываем родителя с потомком
            if (z.parent == null) root = replacement; //если корень дерева
            else if (z == z.parent.left) z.parent.left = replacement; //z был левым потомком
            else z.parent.right = replacement;
        } else if (z.parent == null) root = null; //у узла нет потомков и z-единственный узел в дереве
        else {
            if (z == z.parent.left) z.parent.left = null; //убираем ссылку у родителей
            else if (z == z.parent.right) z.parent.right = null;
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

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

    @Override
    public Integer firstKey() { return minimum(root).key; }

    @Override
    public Integer lastKey() { return maximum(root).key; }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, map, toKey);
        return map;
    }

    //собираем все элементы с ключами меньше toKEy в новую карту
    private void headMap(Node node, MyRbMap map, Integer toKey) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value); //добавляем текущий узел в результирующую карту
            //рекурсивно обходим все поддерево
            headMap(node.left, map, toKey);
            headMap(node.right, map, toKey);
        } else headMap(node.left, map, toKey); //если узел не подходит - рекурсивно ищем в левом поддереве
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, map, fromKey);
        return map;
    }
    //собираем все элементы с ключами больше fromKEy в новую карту
    private void tailMap(Node node, MyRbMap map, Integer fromKey) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            tailMap(node.left, map, fromKey);
            tailMap(node.right, map, fromKey);
        } else tailMap(node.right, map, fromKey);
    }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap map = new MyRbMap();
        subMap(root, map, fromKey, toKey);
        return map;
    }

    private void subMap(Node node, MyRbMap map, Integer fromKey, Integer toKey) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
        }
        if (node.key.compareTo(fromKey) > 0) subMap(node.left, map, fromKey, toKey);
        if (node.key.compareTo(toKey) < 0) subMap(node.right, map, fromKey, toKey);
    }

    // Методы Map/Collection, не обязательные
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
