package by.it.group451004.volynets.lesson12;

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

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // вывод элементов в отсортированном порядке (симметричный обход)
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // удаляем последнюю ", "
        }
        sb.append("}");
        return sb.toString();
    }

    // добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        return oldValue[0];
    }

    // удаление элемента по ключу
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        String[] removedValue = new String[1];
        root = remove(root, (Integer) key, removedValue);
        return removedValue[0];
    }

    // получение значения по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        AvlNode node = get(root, (Integer) key);
        return node != null ? node.value : null;
    }

    // проверка наличия ключа
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы AVL-дерева          ///////
    /////////////////////////////////////////////////////////////////////////

    // симметричный обход дерева (левый-корень-правый)
    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // рекурсивное добавление с балансировкой
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
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    // рекурсивное удаление с балансировкой
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

            // узел с одним или нулем потомков
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // узел с двумя потомками - находим минимальный в правом поддереве
                AvlNode minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = remove(node.right, minNode.key, new String[1]);
            }
        }

        return node != null ? balance(node) : null;
    }

    // рекурсивный поиск узла по ключу
    private AvlNode get(AvlNode node, Integer key) {
        if (node == null) {
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

    // поиск узла с минимальным ключом в поддереве
    private AvlNode findMin(AvlNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private AvlNode balance(AvlNode node) {
        updateHeight(node);
        int balance = getBalance(node);

        //Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
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

    // правый поворот (баланс 2, левый 1/0)
    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // левый поворот (баланс -2, правый 1/0)
    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int height(AvlNode node) {
        return node != null ? node.height : 0;
    }

    private int getBalance(AvlNode node) {
        return node != null ? height(node.left) - height(node.right) : 0;
    }

//    +2 - левое поддерево сильно тяжелее
//    +1 - левое поддерево немного тяжелее
//    0 - идеальный баланс
//    -1 - правое поддерево немного тяжелее
//    -2 - правое поддерево сильно тяжелее

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Map - заглушки           ///////
    /////////////////////////////////////////////////////////////////////////

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