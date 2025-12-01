package by.it.group451002.spitsyna.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private enum Color {RED, BLACK};

    private static class Node{
        Node parent, left, right;
        int key;
        String value;
        Color color;

        private static Node NIL = new Node();

        public Node(int key, String value){
            this.key = key;
            this.value = value;
            this.color = Color.RED;//новый узел всегда красный
            this.left = NIL;
            this.right = NIL;
            this.parent = NIL;
        }

        private Node(){
            this.color = Color.BLACK;
            this.left = this;
            this.right = this;
            this.parent = this;
        }

        boolean isNIL(){
            return this == NIL;
        }
    }

    private Node root = Node.NIL;
    private int size = 0;

    //поиск дедушки
    private Node grandparent(Node p){
        if (!p.isNIL() && !p.parent.isNIL())
            return p.parent.parent;
        return Node.NIL;
    }

    //поиск дяди
    private Node uncle(Node p){
        Node grandparent = grandparent(p);//находим дедушку
        if (grandparent.isNIL())
            return Node.NIL;
        if (grandparent.right != p.parent)
            return grandparent.right;
        else
            return grandparent.left;
    }

    //левый поворот вокруг p
    private void leftRotate(Node p){
        Node pivot = p.right; //новый корень после поворота
        pivot.parent = p.parent;//меняем родителя у pivot

        if (!p.parent.isNIL()){//меняем ребенка у родителя p на pivot
            if (p.parent.left == p)
                p.parent.left = pivot;
            else
                p.parent.right = pivot;
        }
        else
            root = pivot;

        //отдаем левого ребенка pivot p в качестве правого ребенка(левый ребенок у p остается)
        p.right = pivot.left;
        if (!pivot.left.isNIL())
            pivot.left.parent = p;

        //p становится левым ребенком pivot
        p.parent = pivot;
        pivot.left = p;
    }

    //правый поворот вокруг p
    private void rightRotate(Node p){
        Node pivot = p.left;
        pivot.parent = p.parent;

        if (!p.parent.isNIL()){
            if (p.parent.left == p)
                p.parent.left = pivot;
            else
                p.parent.right = pivot;
        }
        else
            root = pivot;

        //pivot отдает своего правого ребенка elemAround в качестве левого ребенка
        p.left = pivot.right;
        if (!pivot.right.isNIL())
            pivot.right.parent = p;

        p.parent = pivot;
        pivot.right = p;
    }

    private void balanceInsert(Node p){
        Node current = p;

        while (!current.parent.isNIL() && current.parent.color == Color.RED) {
            if (!uncle(current).isNIL() && uncle(current).color == Color.RED){
                // Случай 1 - дядя красный
                current.parent.color = Color.BLACK;
                uncle(current).color = Color.BLACK;
                grandparent(current).color = Color.RED;
                current = grandparent(current);
            }
            else {
                if (current.parent == grandparent(current).left) {
                    // Случай 2 - дядя черный и current - правый ребенок
                    if (current == current.parent.right) {
                        current = current.parent;
                        leftRotate(current);
                    }
                    // Случай 3 - дядя черный и current - левый ребенок
                    current.parent.color = Color.BLACK;
                    grandparent(current).color = Color.RED;
                    rightRotate(grandparent(current));
                }
                else {
                    // Случай 2 - дядя черный и current - левый ребенок
                    if (current == current.parent.left){
                        current = current.parent;
                        rightRotate(current);
                    }
                    // Случай 3 - дядя черный и current - правый ребенок
                    current.parent.color = Color.BLACK;
                    grandparent(current).color = Color.RED;
                    leftRotate(grandparent(current));
                }
            }
        }

        root.color = Color.BLACK;
    }

    private void insert(Node p, int key, String value) {
        if (p.isNIL()) {
            Node newNode = new Node(key, value);
            if (root.isNIL()) {
                root = newNode;
            }
            balanceInsert(newNode);
            return;
        }

        if (key > p.key) {
            if (p.right.isNIL()) {
                p.right = new Node(key, value);
                p.right.parent = p;
                balanceInsert(p.right);
            } else {
                insert(p.right, key, value);
            }
        } else if (key < p.key) {
            if (p.left.isNIL()) {
                p.left = new Node(key, value);
                p.left.parent = p;
                balanceInsert(p.left);
            } else {
                insert(p.left, key, value);
            }
        } else {
            p.value = value; // Обновление значения существующего ключа
        }
    }

    private void balanceRemove(Node p){
        while (p != root &&  p.color == Color.BLACK){
            // если p - левый ребенок
            if (p == p.parent.left){
                Node brother = p.parent.right;

                //если брат красный
                if (brother.color == Color.RED){
                    p.parent.color = Color.RED;
                    brother.color = Color.BLACK;
                    leftRotate(p.parent);
                    brother = p.parent.right;
                }

                //если брат черный
                if (brother.color == Color.BLACK){
                    //если оба ребенка брата черные
                    if (brother.left.color == Color.BLACK && brother.right.color == Color.BLACK){
                        brother.color = Color.RED;
                        p = p.parent;
                    }
                    else {
                        //если левый ребенок брата красный, а правый - черный
                        if (brother.left.color == Color.RED && brother.right.color == Color.BLACK) {
                            brother.left.color = Color.BLACK;
                            brother.color = Color.RED;
                            rightRotate(brother);
                            brother = p.parent.right;
                        }
                        //если правый ребенок брата красный(левый любой)
                        if  (brother.right.color == Color.RED) {
                            brother.color = p.parent.color;
                            brother.parent.color = Color.BLACK;
                            brother.right.color = Color.BLACK;
                            leftRotate(brother.parent);
                            p = root;
                        }
                    }
                }
            }
            else {
                //если p - правый ребенок
                Node brother = p.parent.left;
                //если брат красный
                if (brother.color == Color.RED){
                    p.parent.color = Color.RED;
                    brother.color = Color.BLACK;
                    rightRotate(p.parent);
                    brother = p.parent.left;
                }

                //если брат черный
                if (brother.color == Color.BLACK){
                    //если оба ребенка брата черные
                    if (brother.left.color == Color.BLACK && brother.right.color == Color.BLACK){
                        brother.color = Color.RED;
                        p = p.parent;
                    }
                    else {
                        //если левый ребенок брата черный, а правый - красный
                        if (brother.left.color == Color.BLACK && brother.right.color == Color.RED) {
                            brother.left.color = Color.BLACK;
                            brother.color = Color.RED;
                            leftRotate(brother);
                            brother = p.parent.left;
                        }
                        //если левый ребенок брата красный(правый любой)
                        if  (brother.left.color == Color.RED) {
                            brother.color = brother.parent.color;
                            brother.parent.color = Color.BLACK;
                            brother.right.color = Color.BLACK;
                            rightRotate(brother.parent);
                            p = root;
                        }
                    }
                }
            }
        }
        p.color = Color.BLACK;
    }

    private Node findMin(Node p){
        if (p.isNIL() || p.left.isNIL())
            return p;
        return findMin(p.left);
    }

    private Node findNode(Node p, int key){
        Node currNode = p;
        while (!currNode.isNIL()){
            if (key > currNode.key)
                currNode = currNode.right;
            else if (key < currNode.key)
                currNode = currNode.left;
            else {
                return currNode;
            }
        }
        return null;
    }

    private void deleteNode(Node p) {
        Node newNode; //узел, который займет место удаляемого элемента
        Color delColor = p.color;

        //Нет левого потомка
        if (p.left.isNIL()) {
            newNode = p.right;
            swap(p, p.right);//меняем p на его правого ребенка(p удаляется)
        }
        //Нет правого потомка
        else if (p.right.isNIL()) {
            newNode = p.left;
            swap(p, p.left);//меняем p на его левого ребенка(p удаляется)
        }
        //Есть оба потомка
        else {
            Node min = findMin(p.right); // Находим минимальный в правом поддереве
            p.key = min.key;
            p.value = min.value;
            deleteMin(min);
            return;
        }

        // Балансировка только если удалили черный узел
        if (delColor == Color.BLACK) {
            balanceRemove(newNode);
        }
    }

    private void deleteMin(Node min){
        Color deleteColor = min.color;
        Node newNode = min.right;

        swap(min, min.right);

        if (deleteColor == Color.BLACK)
            balanceRemove(newNode);
    }

    // Замена узла p на узел q
    private void swap(Node p, Node q) {
        if (p.parent.isNIL()) {
            root = q;
        } else if (p == p.parent.left) {
            p.parent.left = q;
        } else {
            p.parent.right = q;
        }
        q.parent = p.parent;
    }

    private void inOrder(StringBuilder str, Node p){
        if (p.isNIL())
            return;

        inOrder(str, p.left);

        str.append(p.key);
        str.append("=");
        str.append(p.value);
        str.append(", ");

        inOrder(str, p.right);
    }

    //обязательные для реализации методы
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
        Node findNode = findNode(root, key);
        String oldValue;
        if (findNode == null)
            oldValue = null;
        else
            oldValue = findNode.value;

        if (root.isNIL()) {
            root = new Node(key, value);
            root.color = Color.BLACK; // Корень всегда черный
            size++;
        }
        else {
            if (findNode == null) {
                size++;
            }
            insert(root, key, value);
        }

        return oldValue;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode(root, (int)key);
        String delValue;

        if (node == null)
            delValue = null;
        else {
            delValue = node.value;
            deleteNode(node);
            size--;
        }
        return delValue;
    }

    @Override
    public String get(Object key) {
        Node findNode = findNode(root, (int)key);
        if (findNode == null)
            return null;
        return findNode.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return (findNode(root, (int)key) != null);
    }

    private Node checkValue(Node p, String value){
        if (p.isNIL())
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
        Node newNode = checkValue(root, value.toString());
        return newNode != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = Node.NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void createSortedMapLess(Node p, int keyToCmp, MyRbMap res){
        if (p.isNIL())
            return;

        if (p.key < keyToCmp) {
            // Узел подходит - обрабатываем левое поддерево, добавляем узел, обрабатываем правое
            createSortedMapLess(p.left, keyToCmp, res);
            res.put(p.key, p.value);
            createSortedMapLess(p.right, keyToCmp, res);
        } else {
            // Узел не подходит - идем только в левое поддерево (там могут быть меньшие элементы)
            createSortedMapLess(p.left, keyToCmp, res);
        }
    }

    //функция для нахождения элементов, которые меньше заданного ключа, и сформировать из них дерево
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap newSortedMap = new MyRbMap();
        createSortedMapLess(root, toKey, newSortedMap);
        return newSortedMap;
    }

    private void createSortedMapMore(Node p, int keyToCmp, MyRbMap res){
        if (p.isNIL())
            return;

        createSortedMapMore(p.right, keyToCmp, res);

        if (keyToCmp <= p.key){
            res.put(p.key, p.value);
            createSortedMapMore(p.left, keyToCmp, res);
        }
    }

    //функция для нахождения элементов, которые больше или равны заданному ключу, и сформировать из них дерево
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap newSortedMap = new MyRbMap();
        createSortedMapMore(root, fromKey, newSortedMap);
        return newSortedMap;
    }

    @Override
    public Integer firstKey() {
        Node currNode = root;
        while (!currNode.left.isNIL())
            currNode = currNode.left;

        return currNode.key;
    }

    @Override
    public Integer lastKey() {
        Node currNode = root;
        while (!currNode.right.isNIL())
            currNode = currNode.right;

        return currNode.key;
    }

    //Необязательные для реализации методы
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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
