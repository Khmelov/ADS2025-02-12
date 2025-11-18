package by.it.group410902.bolshakova.lesson12;

import java.util.Map;

//  авл дерево это сбалансированное бинарное дерево поиска,
//  гле высота поддеревьев отличается не более чем на 1

public class MyAvlMap implements Map<Integer, String> {

    // Узел АВЛ-дерева
    private static class Node {
        int key;
        String value;
        Node left, right; //лев и прав потомки
        int height;

        Node(int key, String value) { //конструктор узла
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root; // корень узла
    private int size = 0; // количесвто элементов в дереве

    @Override
    public String toString() { // преобразование дерева в строку
        if (root == null) return "{}";  // если дерево пустое
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2); // Убираем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) { //обновление или добавление элемента
        String oldValue = get(key);
        root = insert(root, key, value); //вставим новый узел или обновим который есть
        if (oldValue == null) size++;
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            root = delete(root, (Integer) key);
            size--;
        }
        return oldValue;//удалённое значение
    }

    @Override
    public String get(Object key) { // нахождение значения по ключу
        if (!(key instanceof Integer)) return null; //проверка что ключ правильного типа
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) { //проверка наличия ключа
        return get(key) != null; //есть если get() не возращает null
    }

    @Override
    public int size() { return size; } //кол-во элементов

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { //проверка на пустоту
        return size == 0;
    }

    private Node find(Node node, int key) {//поиск узла по ключу
        while (node != null) { //пока не дошли до конца ветки
            if (key < node.key) node = node.left; //если ключ меньше - идти налево
            else if (key > node.key) node = node.right;//больше- направо
            else return node;
        }
        return null;
    }

    private Node insert(Node node, int key, String value) { // вставка узла
        if (node == null) return new Node(key, value); //новый узел

        if (key < node.key) {
            node.left = insert(node.left, key, value); //вставляем в <- поддерево
        } else if (key > node.key) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value; // Обновляем значение если ключ существует
            return node; // вернули без балнса
        }

        return balance(node);// с балансом
    }

    private Node delete(Node node, int key) {
        if (node == null) return null; // не нашли узел

        if (key < node.key) {
            node.left = delete(node.left, key); // поиск в левом поддереве
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            // Найден узел для удаления
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // У узла два потомка - находим минимальный в правом поддереве
            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = deleteMin(node.right); //удаление мин. узел из правого поддерева
        }

        return balance(node);
    }

    private Node deleteMin(Node node) {//удаление минимального
        if (node.left == null) return node.right; //нашли мин - возвращаем правого потомка
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node findMin(Node node) {//поиск мин узла в вподдереве
        while (node.left != null) node = node.left;
        return node; //самый левый узел
    }

    // Балансировка АВЛ-дерева
    private Node balance(Node node) {
        updateHeight(node);
        int balance = getBalance(node); // коэффицент баланса

        // левое поддерево высокое
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left); //левый поворот для левоо потомка
            }
            return rotateRight(node); // правый поворот для текущего узла
        }

        // правое поддерево высокое
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right); // правый поворот для правого потомка
            }
            return rotateLeft(node);// левый поворот для текущего узла
        }

        return node;
    }
//для случая (лево-лево)
    private Node rotateRight(Node y) {
        Node x = y.left;  // x становится новым корнем поддерева
        y.left = x.right; // Правое поддерево x становится левым поддеревом y
        x.right = y;  // y становится правым потомком x
        updateHeight(y);// он теперь ниже
        updateHeight(x);//он теперь корень
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;//Левое поддерево y становится правым поддеревом x
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        //высота = 1 + максимальная высота из поддеревьев
        }
    }

    private int height(Node node) { //получение высоты узла
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
        // + значение - левое поддерево выше
        // - значение - правое выше
        // 0 - поддеревья сбалансированы
    }
//для вывода в отсортироовочном порядке
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb); // обхождение левого поддерева
            // Добавляем текущий узел в формате "ключ=значение, "
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Неиспользуемые методы интерфейса Map
    @Override
    public boolean containsValue(Object value) { return false; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { }

    @Override
    public java.util.Set<Integer> keySet() { return null; }

    @Override
    public java.util.Collection<String> values() { return null; }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() { return null; }
}