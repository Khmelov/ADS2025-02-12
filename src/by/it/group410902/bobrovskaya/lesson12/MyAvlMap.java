package by.it.group410902.bobrovskaya.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private static class AvlNode {
        Integer key;
        String value;
        AvlNode left;
        AvlNode right;
        int height;

        AvlNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AvlNode root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb); // симметричный обход дерева
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // удаление последней запятой и пробела
        }
        sb.append("}");
        return sb.toString();
    }
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue); // вставка в дерево
        return oldValue[0];
    }

    // удаление элемента по ключу
    @Override
    public String remove(Object key) {
        String[] removedValue = new String[1]; // используется для возврата удалённого значения
        root = remove(root, (Integer) key, removedValue);
        return removedValue[0];
    }

    // рекурсивная вставка узла
    private AvlNode put(AvlNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new AvlNode(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else { // обновление узла
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    // рекурсивное удаление узла
    private AvlNode remove(AvlNode node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;
            size--;

            // случай с одним или нулем потомков
            if (node.left == null || node.right == null) {
                AvlNode temp = (node.left != null) ? node.left : node.right;
                return temp;
            } else {
                // случай с двумя потомками
                AvlNode temp = findMin(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key, new String[1]);
            }
        }

        return balance(node);
    }
    // добавление или обновление пары ключ → значение

    // получение значения по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get(root, (Integer) key);
    }

    // проверка наличия ключа
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return get(root, (Integer) key) != null;
    }

    // возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // очищает дерево
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // возвращает высоту узла
    private int height(AvlNode node) {
        return node == null ? 0 : node.height;
    }

    // вычисляет баланс узла
    private int balanceFactor(AvlNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // обновляет высоту узла
    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    // правый поворот
    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // левый поворот
    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // балансировка узла
    private AvlNode balance(AvlNode node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balance = balanceFactor(node);

        // левый левый случай
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // левый правый случай
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // правый правый случай
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // правый левый случай
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // находит минимальный узел в поддереве
    private AvlNode findMin(AvlNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // рекурсивный поиск значения по ключу
    private String get(AvlNode node, Integer key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    // симметричный обход дерева для формирования строки
    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // не реализованные методы интерфейса map
    @Override
    public boolean containsValue(Object value) {throw new UnsupportedOperationException("containsValue");}

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {throw new UnsupportedOperationException("putAll");}

    @Override
    public java.util.Set<Integer> keySet() {throw new UnsupportedOperationException("keySet");}

    @Override
    public java.util.Collection<String> values() {throw new UnsupportedOperationException("values");}

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {throw new UnsupportedOperationException("entrySet");}
}
