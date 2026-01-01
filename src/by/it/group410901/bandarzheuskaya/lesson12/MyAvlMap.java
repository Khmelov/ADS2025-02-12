package by.it.group410901.bandarzheuskaya.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {
    private Node root;
    private int size;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;     //высота поддерева с корнем в этом узле

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {  //элементы выводятся в порядке возрастания ключей
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        //удаляем последнюю запятую и пробел
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) { //объодит дерево (левыый потомок текущий правый), ключи выводятся в порядке возрастания
        if (node != null) {
            inOrderToString(node.left, sb);     //идем в левое поддерево
            sb.append(node.key).append("=").append(node.value).append(", ");    //обрабатывает сам узел
            inOrderToString(node.right, sb);    //идет в правое поддерево
        }
    }

    //добавляем новую пару или обновляем значение(когда ключ уже есть)
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String[] oldValue = new String[1];      //массив с одним элементов для возврата значения из рекурсии
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null) {   //если true значит ключа не было, добавляем новый
            size++;
        }
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            return new Node(key, value);
        }
    //ищем место для вставки ключа
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {  //ключ меньше - идем в левое поддерево
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {   //больше - идем в правое
            node.right = put(node.right, key, value, oldValue);
        } else {
            //ключ уже существует - обновляем значение
            oldValue[0] = node.value;   //запоминаем старое значение
            node.value = value;     //обновляем на новое
            return node;
        }

        // Обновляем высоту текущего узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // балансируем дерево
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) { //проверяем является ли ключ Integer
            return null;
        }

        Integer intKey = (Integer) key;
        String[] removedValue = new String[1];
        root = remove(root, intKey, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            // Найден узел для удаления
            removedValue[0] = node.value;

            // Узел с одним или нулем потомков
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Узел с двумя потомками
            Node minNode = findMin(node.right); //назодим минимальный элемент в правом поддереве
            node.key = minNode.key;     //копируем данные из него в текущий
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key, new String[1]); //удаляем узел
        }

        // Обновляем высоту текущего узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Балансируем дерево
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key) { //находит значение по ключу и возвращает его
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer intKey = (Integer) key;
        Node node = get(root, intKey);
        return node != null ? node.value : null;
    }

    private Node get(Node node, Integer key) {
        if (node == null) { //дошли до пустого места - ключ не найден
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public boolean containsKey(Object key) {    //существует ли ключ в карте
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }

        Integer intKey = (Integer) key;
        return get(root, intKey) != null;
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

    // Вспомогательные методы для АВЛ-дерева

    private int height(Node node) {
        return node != null ? node.height : 0;
    }

    private int getBalance(Node node) {
        return node != null ? height(node.left) - height(node.right) : 0;
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        // Left Left Case (перекошено в леву сторону)
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case (перекос вправо)
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Остальные методы интерфейса Map не реализованы
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
