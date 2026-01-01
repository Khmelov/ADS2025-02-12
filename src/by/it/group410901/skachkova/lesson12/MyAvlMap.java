package by.it.group410901.skachkova.lesson12;

import java.util.Map;
//сбалансированное бинарное дерево поиска
public class MyAvlMap implements Map<Integer, String> {

    private static class Node
    {
        Integer key;
        String value; //значение
        Node left; //левый потомок
        Node right; //правый потомок
        int height; //высота узла в дереве

        Node(Integer key, String value)
        {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap()
    {
        root = null;
        size = 0;
    }

    // Вспомогательные методы для AVL-дерева
    private int height(Node node) //выдаёт высоту узла
    {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) //балансирует
    {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) //обновляет высоту узла на основе высот потомков
    {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    private Node rotateRight(Node y)
    {
        Node x = y.left;//х - левый потомок у
        Node T2 = x.right;//правое поддерево х

        x.right = y; //х теперь родитель у
        y.left = T2; //т2 левое поддерево у

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x)
    {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node)
    {
        if (node == null) return null;

        updateHeight(node);
        int balance = balanceFactor(node);

        // Left Left Case - нужен правой поворот
        if (balance > 1 && balanceFactor(node.left) >= 0)
        {
            return rotateRight(node);
        }

        // Left Right Case - левый + правый поворот
        if (balance > 1 && balanceFactor(node.left) < 0)
        {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case - левый поворот
        if (balance < -1 && balanceFactor(node.right) <= 0)
        {
            return rotateLeft(node);
        }

        // Right Left Case - правый + левый поворот
        if (balance < -1 && balanceFactor(node.right) > 0)
        {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Основные методы Map
    @Override
    public String put(Integer key, String value) //добавление или обновление
    {
        String[] oldValue = new String[1]; //запоминает старое значение
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null)
        {
            size++;//увеличивает размер если ключ новый
        }
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue)
    {              //добавление или обновление
        if (node == null)
        {
            return new Node(key, value);//создаёт новый узел
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) //если ключ меньше - идём влево
        {
            node.left = put(node.left, key, value, oldValue);
        }
        else if (cmp > 0) //если больше - вправо
        {
            node.right = put(node.right, key, value, oldValue);
        }
        else //ключ совпал - заменяем значение
        {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node); //балансирует после вставки
    }

    @Override
    public String remove(Object key)
    {
        if (!(key instanceof Integer))
        {
            return null;
        }
        Integer k = (Integer) key;
        String[] removedValue = new String[1]; //запоминает удаляемое
        root = remove(root, k, removedValue);
        if (removedValue[0] != null)
        {
            size--; //уменьшаем размер если ключ существовал
        }
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue)
    {
        if (node == null)
        {
            return null; //если ключ не найден
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
        {
            node.left = remove(node.left, key, removedValue);
        }
        else if (cmp > 0)
        {
            node.right = remove(node.right, key, removedValue);
        }
        else
        {
            removedValue[0] = node.value;//ключ найден

            if (node.left == null || node.right == null)//узел с 1 или 0 потомками
            {
                node = (node.left == null) ? node.right : node.left;
            }
            else
            {//узел с 2 потомками
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = remove(node.right, minNode.key, new String[1]);
            }
        }

        if (node == null)
        {
            return null;
        }

        return balance(node); //балансируем после удаления
    }

    private Node findMin(Node node)
    {
        while (node.left != null)
        {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key)//поиск
    {
        if (!(key instanceof Integer))
        {
            return null;
        }
        Integer k = (Integer) key;
        Node node = get(root, k);
        return node == null ? null : node.value;
    }

    private Node get(Node node, Integer key) //тоже поиск
    {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key); //ищет слева
        if (cmp > 0) return get(node.right, key); //ищет справа
        return node;
    }

    @Override
    public boolean containsKey(Object key) //проверяет, существует ли указанный ключ в карте
    {
        if (!(key instanceof Integer))
        {
            return false; // если ключ не Integer - сразу false
        }
        Integer k = (Integer) key;
        return get(root, k) != null;
    }

    @Override
    public int size()
    {
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb); //обход для сортировки
        if (sb.length() > 1)
        {
            sb.setLength(sb.length() - 2); // убираем последнюю ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb)
    {
        if (node != null)
        {
            inOrderToString(node.left, sb);//сначала левое поддерево
            sb.append(node.key).append("=").append(node.value).append(", ");//текущий узел
            inOrderToString(node.right, sb);//потом правое поддерево
        }
    }

    // не реализованные методы
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

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}