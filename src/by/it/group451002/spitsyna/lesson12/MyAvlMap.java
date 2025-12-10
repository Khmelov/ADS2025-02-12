package by.it.group451002.spitsyna.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    //узел дерева
    private class Node{
        int key;
        String value;
        int height; //длина самого длинного пути от этого узла вниз до самого дальнего листа
        Node right;
        Node left;

        Node(int key, String value){
            this.key = key;
            this.value = value;
            this.height = 1;
            this.right = null;
            this.left = null;
        }
    }

    Node root = null;
    int size = 0;

    private int height(Node p){
        if (p == null)
            return 0;
        return p.height;
    }

    //функция для определения, сбалансирован узел или нет
    private int balanceFactor(Node p){
        return height(p.right) - height(p.left);
    }

    private void fixHeight(Node p){
        int rHeight = height(p.right);
        int lHeight = height(p.left);

        if (rHeight - lHeight > 0)
            p.height = rHeight;
        else
            p.height = lHeight;

    }

    private Node rightRotate(Node p){
        Node pivot = p.left; //pivot - новый корень после поворота
        p.left = pivot.right;
        pivot.right = p;

        fixHeight(p);
        fixHeight(pivot);
        return pivot;
    }

    private Node leftRotate(Node p){
        Node pivot = p.right;
        p.right = pivot.left;
        pivot.left = p;

        fixHeight(p);
        fixHeight(pivot);

        return pivot;
    }

    //Балансировка узла p
    private Node balance(Node p){
        fixHeight(p);
        if (balanceFactor(p) == 2){
            //если у правого поддерева левое поддерево тяжелее, то нужно выполнять большой поворот: сначала правый, потом левый
            if (balanceFactor(p.right) < 0)
                p.right = rightRotate(p.right);
            return leftRotate(p);
        }
        else if (balanceFactor(p) == -2){
            //если у левого поддерева правое поддерево тяжелее, то нужно выполнить большой поворот: сначала левый, потом правый
            if (balanceFactor(p.left) > 0)
                p.left = leftRotate(p.left);
            return rightRotate(p);
        }
        return p;
    }

    private Node insert(Node p, int key, String value){
        if (p == null)
            return new Node(key, value);;

        if (key > p.key)
            p.right = insert(p.right, key, value);
        else if (key < p.key)
            p.left = insert(p.left, key, value);
        else{
            p.value = value;
            return p;
        }

        //После возвращения из рекурсии нужно проводить балансировку текущего узла
        return balance(p);
    }

    //поиск минимального элемента в левом поддереве
    private Node findMin(Node p){
        if (p.left == null)
            return p;
        return findMin(p.left);
    }

    //удаление узла с минимальным ключом из дерева p
    private Node removeMin(Node p){
        if (p.left == null)
            return p.right;
        p.left = removeMin(p.left);
        //При подъеме из рекурсии балансируем каждый узел по пути к p
        return balance(p);
    }

    private Node remove(Node root, int key){
        if (root == null)
            return null;

        if (key > root.key)
            root.right = remove(root.right, key);
        else if (key < root.key)
            root.left = remove(root.left, key);
        else {
            Node leftChild = root.left;
            Node rightChild = root.right;
            root = null;

            //если нет правого поддерева, то просто возвращаем левое
            if (rightChild == null)
                return leftChild;

            //ищем минимальный в правом поддереве
            Node min = findMin(rightChild);
            min.left = leftChild;
            min.right = removeMin(rightChild);
            return balance(min);
        }

        //На выходе из рекурсии выполняем балансировку
        return balance(root);
    }

    //симметричный обход
    private void inOrder(StringBuilder str, Node root){
        if (root == null){
            return;
        }

        inOrder(str, root.left);

        str.append(root.key);
        str.append("=");
        str.append(root.value);
        str.append(", ");

        inOrder(str, root.right);
    }

    //функция поиска элемента в дереве
    private Node findElem(Node p, int key){
        if (p == null)
            return null;

        if (key > p.key){
            return findElem(p.right, key);
        }
        else if (key < p.key)
            return findElem(p.left, key);
        else {
            return p;
        }
    }

    //Обязательные для реализации методы
    public String toString(){
        StringBuilder str = new StringBuilder("{");

        inOrder(str, root);
        if (str.length()-2 > 0)
            str.delete(str.length()-2,str.length());
        str.append("}");

        return str.toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node findElem = findElem(root, key);
        String oldValue;
        if (findElem == null){
            size++;
            oldValue = null;
        }
        else
            oldValue = findElem.value;

        root = insert(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        Node findElem = findElem(root,(int)key);
        String delValue;
        if (findElem == null)
            delValue = null;
        else {
            delValue = findElem.value;
            size--;
        }

        root = remove(root, (int)key);

        return delValue;
    }

    @Override
    public String get(Object key) {
        Node elem = findElem(root,(int) key);
        if (elem == null)
            return null;

        return elem.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (findElem(root, (int) key) == null)
            return false;

        return true;
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
        return (size == 0);
    }

    //Необязательные для реализации методы
    @Override
    public boolean containsValue(Object value) {
        return false;
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
