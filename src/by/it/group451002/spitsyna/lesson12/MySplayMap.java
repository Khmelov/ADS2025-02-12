package by.it.group451002.spitsyna.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    class Node {
        int key;
        String value;
        Node left, right, parent;

        Node(int key, String value){
            this.key = key;
            this.value = value;
        }
    }

    private Node root = null;
    private int size = 0;

    //левый поворот вокруг p
    private void leftRotate(Node p){
        Node pivot = p.right;
        pivot.parent = p.parent;

        if (p.parent != null) {
            if (p.parent.right == p)
                p.parent.right = pivot;
            else
                p.parent.left = pivot;
        }
        else
            root = pivot;

        p.right = pivot.left;
        if (pivot.left != null)
            pivot.left.parent = p;

        pivot.left = p;
        p.parent = pivot;
    }

    //правый поворот вокруг p
    private void rightRotate(Node p){
        Node pivot = p.left;
        pivot.parent = p.parent;

        if (p.parent != null){
            if (p.parent.left == p)
                p.parent.left = pivot;
            else
                p.parent.right = pivot;
        }
        else
            root = pivot;

        //pivot отдает своего правого ребенка elemAround в качестве левого ребенка
        p.left = pivot.right;
        if (pivot.right != null)
            pivot.right.parent = p;

        p.parent = pivot;
        pivot.right = p;
    }

    private void splay(Node p){
        while (p.parent != null){
            Node parent = p.parent;
            Node grandparent = p.parent.parent;

            if (grandparent == null){
                //Zig - одиночный поворот
                if (p == parent.right)
                    leftRotate(parent);
                else
                    rightRotate(parent);
            }
            else if (p == parent.left && grandparent.left == parent){
                //Zig-zig (ребенок, родитель и дед - все по одну линию слева)
                rightRotate(grandparent);
                rightRotate(parent);
            }
            else if (p == parent.right && grandparent.right == parent){
                //Zig-Zig (ребенок, родитель и дед - все по одну линию справа)
                leftRotate(grandparent);
                leftRotate(parent);
            }
            else {
                //Zig-Zag (не стоят по одну линию)
                if (p == parent.left){
                    rightRotate(parent);
                    leftRotate(grandparent);
                }
                else {
                    leftRotate(parent);
                    rightRotate(grandparent);
                }
            }
        }
        root = p;
    }

    private Node findNode (Node p, int key){
        Node current = p;

        while (current != null){
            if (key > current.key)
                current = current.right;
            else if (key < current.key)
                current = current.left;
            else
                return current;
        }
        return null;
    }

    private void inOrder(StringBuilder str, Node p){
        if (p == null)
            return;

        inOrder(str, p.left);

        str.append(p.key);
        str.append("=");
        str.append(p.value);
        str.append(", ");

        inOrder(str, p.right);
    }

    //Обязательные для реализации методы
    public String toString(){
        StringBuilder str = new StringBuilder("{");

        inOrder(str, root);

        if (str.length() > 2)
            str.delete(str.length()-2, str.length());
        str.append("}");
        return str.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;

        //ищем место, куда вставить, если такой ключ уже существует, то просто меняем его значение
        while (current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                // Ключ уже существует - обновляем значение
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = parent;

        if (key < parent.key)
            parent.left = newNode;
        else
            parent.right = newNode;
        size++;
        splay(newNode);
        return null;
    }

    private Node findMin(Node p){
        if (p.left == null)
            return p;
        return findMin(p.left);
    }

    @Override
    public String remove(Object key) {
        Node delNode = findNode(root, (int)key);
        if (delNode == null)
            return null;

        splay(delNode);//поднимаем удаляемый узел к корню
        String delValue = delNode.value; //сохраняем значение удаляемого узла

        if (delNode.left == null){//если у удаляемого узла нет левого ребенка
            root = delNode.right;
            if (root != null)
                root.parent = null;
        }
        else if (delNode.right == null){//если у удаляемого узла нет правого ребенка
            root = delNode.left;
            if (root != null)
                root.parent = null;
        }
        else { //если есть два ребенка
            Node min = findMin(delNode.right);
            splay(min);

            min.left = delNode.left;
            if (delNode.left != null)
                delNode.left.parent = min;

            root = min;
        }
        size--;
        return delValue;
    }

    @Override
    public String get(Object key) {
        Node findElem = findNode(root, (int)key);
        if (findElem == null)
            return null;
        splay(findElem); //поднимаем найденный элемент к корню
        return findElem.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Node findNode = findNode(root, (int) key);
        return findNode != null;
    }

    private Node checkValue(Node p, String value){
        if (p == null)
            return null;

        if (Objects.equals(p.value, value))
            return p;

        Node leftRes = checkValue(p.left, value);
        if (leftRes != null)
            return leftRes;
        return checkValue(p.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        Node findNode = checkValue(root, value.toString());
        return findNode != null;
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

    private void createNavigableMapLess(Node p, int keyToCmp, MySplayMap res){
        if (p == null)
            return;

        if (p.key < keyToCmp) {
            // Узел подходит - обрабатываем левое поддерево, добавляем узел, обрабатываем правое
            createNavigableMapLess(p.left, keyToCmp, res);
            res.put(p.key, p.value);
            createNavigableMapLess(p.right, keyToCmp, res);
        } else {
            // Узел не подходит - идем только в левое поддерево (там могут быть меньшие элементы)
            createNavigableMapLess(p.left, keyToCmp, res);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap newMap = new MySplayMap();
        createNavigableMapLess(root, toKey, newMap);
        return newMap;
    }

    private void createNavigableMapMore(Node p, int keyToCmp, MySplayMap res){
        if (p == null)
            return;

        if (p.key >=keyToCmp) {
            // Узел подходит - обрабатываем левое поддерево, добавляем узел, обрабатываем правое
            createNavigableMapMore(p.left, keyToCmp, res);
            res.put(p.key, p.value);
            createNavigableMapMore(p.right, keyToCmp, res);
        } else {
            // Узел не подходит - идем только в правое поддерево (там могут быть большие элементы)
            createNavigableMapMore(p.right, keyToCmp, res);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap newMap = new MySplayMap();
        createNavigableMapMore(root, fromKey, newMap);
        return newMap;
    }

    @Override
    public Integer firstKey() {
        Node current = root;
        while (current.left != null)
            current = current.left;
        return current.key;
    }

    @Override
    public Integer lastKey() {
        Node current = root;
        while (current.right != null)
            current = current.right;
        return current.key;
    }

    private Node lowerKey(Node p, int key) {
        if (p == null)
            return null;

        if (p.key >= key) {
            // Текущий ключ слишком большой - ищем в левом поддереве
            return lowerKey(p.left, key);
        } else {
            //Текущий ключ меньше искомого
            //Проверяем правое поддерево - нет ли там большего подходящего ключа
            Node rightResult = lowerKey(p.right, key);
            if (rightResult != null)
                return rightResult;
            else
                return p; //если дошли до конца правой ветки, то возвращаем самый последний
        }
    }

    //ищем наибольший ключ, который строго меньше заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node lowerKey = lowerKey(root, key);
        if (lowerKey == null)
            return null;
        return lowerKey.key;
    }

    private Node floorKey(Node p, int key) {
        if (p == null)
            return null;

        if (p.key == key)
            return p;
        else if (p.key >= key) {
            // Текущий ключ слишком большой - ищем в левом поддереве
            return floorKey(p.left, key);
        } else {
            //Текущий ключ меньше искомого
            //Проверяем правое поддерево - нет ли там большего подходящего ключа
            Node rightResult = floorKey(p.right, key);
            if (rightResult != null)
                return rightResult;
            else
                return p; //если дошли до конца правой ветки, то возвращаем самый последний
        }
    }

    //ищем наибольший ключ, который меньше или равен заданному
    @Override
    public Integer floorKey(Integer key) {
        Node floorKey = floorKey(root, key);
        if (floorKey == null)
            return null;
        return floorKey.key;
    }

    private Node ceilingKey(Node p, int key) {
        if (p == null)
            return null;

        if (p.key == key)
            return p;
        else if (p.key <= key) {
            // Текущий ключ слишком маленький - ищем в правом поддереве
            return ceilingKey(p.right, key);
        } else {
            //Текущий ключ больше искомого
            //Проверяем левое поддерево - нет ли там меньшего подходящего ключа
            Node leftResult = ceilingKey(p.left, key);
            if (leftResult != null)
                return leftResult;
            else
                return p; //если дошли до конца левой ветки, то возвращаем самый последний
        }
    }

    //ищем наименьший ключ, который больше или равен заданному
    @Override
    public Integer ceilingKey(Integer key) {
        Node ceilingKey = ceilingKey(root, key);
        if (ceilingKey == null)
            return null;
        return ceilingKey.key;
    }

    private Node higherKey(Node p, int key) {
        if (p == null)
            return null;

        if (p.key <= key) {
            // Текущий ключ слишком маленький - ищем в правом поддереве
            return higherKey(p.right, key);
        } else {
            //Текущий ключ больше искомого
            //Проверяем левое поддерево - нет ли там меньшего подходящего ключа
            Node leftResult = higherKey(p.left, key);
            if (leftResult != null)
                return leftResult;
            else
                return p; //если дошли до конца левой ветки, то возвращаем самый последний
        }
    }

    //ищем наименьший ключ, который строго больше заданного
    @Override
    public Integer higherKey(Integer key) {
        Node higherKey = higherKey(root, key);
        if (higherKey == null)
            return null;
        return higherKey.key;
    }

    //Необязательные для реализации методы
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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
