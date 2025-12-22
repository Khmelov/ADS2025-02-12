package by.it.group451003.plyushchevich.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

/**
 * Реализация Map<Integer, String> на основе АВЛ-дерева (самобалансирующееся двоичное дерево поиска).
 */
public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 0;
        }
    }

    private Node root;
    private int size;


    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) return false;
        return get((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node cur = root;
        Integer k = (Integer) key;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            if (cmp == 0) return cur.value;
            else if (cmp < 0) cur = cur.left;  // меньше -> слева
            else cur = cur.right;  // больше -> справа
        }
        return null;
    }


    @Override
    public String put(Integer key, String value) {
        // Вставка: возвращаем предыдущее значение, если ключ уже был, иначе null.
        if (key == null) throw new NullPointerException("MyAvlMap does not support null keys");
        final String[] old = new String[1];
        root = insertRec(root, key, value, old);
        if (old[0] == null) return null; else return old[0];
    }


    private Node insertRec(Node node, Integer key, String value, String[] old) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRec(node.left, key, value, old);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key, value, old);
        } else {
            old[0] = node.value;
            node.value = value;
            return node;
        }
        updateHeight(node);

        // Балансируем и возвращаем (возможно новый) корень этого поддерева
        return rebalance(node);
    }

    @Override
    public String remove(Object key) {
        if (key == null) return null;
        final String[] old = new String[1];
        root = removeRec(root, (Integer) key, old);
        return old[0]; // если ключа не было — old[0] останется null
    }

    /**
     * Алгоритм удаления:
     * - Находим узел по BST-логике.
     * - Если узел не найден — возвращаем node.
     * - Если найден:
     *   - Если один из детей пуст — заменяем узел на непустого ребёнка.
     *   - Если оба ребёнка есть — находим следующий по порядку (минимум в правом поддереве, либо может быть максимум левого),
     *     копируем его ключ/значение в текущий узел и рекурсивно удаляем минимум из правого поддерева.
     * - После удаления выполняем updateHeight и rebalance для текущего узла.
     */
    private Node removeRec(Node node, Integer key, String[] old) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = removeRec(node.left, key, old);
            } else if (cmp > 0) {
                node.right = removeRec(node.right, key, old);
        } else {
            old[0] = node.value;
            size--;

            if (node.left == null)
                return node.right; // заменяем на правое поддерево (может быть null)
            else if (node.right == null) return node.left; // заменяем на левое
            else {
                // Оба поддерева существуют: ищем минимум в правом поддереве (successor)
                Node min = findMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = removeMin(node.right);
            }
        }

        if (node == null)
            return null;

        updateHeight(node);
        return rebalance(node);
    }


    private Node findMin(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private Node removeMin(Node node) {
        if (node.left == null) return node.right; // этот узел — минимум, заменяем на правое поддерево
        node.left = removeMin(node.left);
        updateHeight(node);
        return rebalance(node);
    }


    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * Вычисляет фактор баланса: height(left) - height(right).
     * В АВЛ-дереве допустимые значения: -1, 0, 1. Если вне этого диапазона — нужно балансировать.
     */
    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    /**
     * Балансировка узла: в зависимости от фактора баланса применяем нужные повороты.
     * Возвращает новый корень этого поддерева после поворотов.
     */
    private Node rebalance(Node node) {
        int bf = balanceFactor(node);
        // Левое поддерево "тяжелее"
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // LR
            }
            return rotateRight(node);  // LL
        }
        // Правое поддерево "тяжелее"
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);  // RL
            }
            return rotateLeft(node);  // RR
        }
        return node;
    }

    /**
     *           y                x
     *          / \     ->      / \
     *         x   T3          T1  y
     *        / \                / \
     *       T1 T2              T2 T3
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     *          x                 y
     *         / \    ->        / \
     *        T1  y            x  T3
     *           / \         / \
     *          T2 T3       T1 T2
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    //toString (в порядке возрастания ключей)
    @Override
    public String toString() {
        // Формат: {k1=v1, k2=v2}
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        // получить ключи в возрастающем порядке
        InorderBuilder ib = new InorderBuilder();
        inorder(root, ib);
        sb.append(ib.builder.toString());
        sb.append("}");
        return sb.toString();
    }


    private static class InorderBuilder {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
    }

    private void inorder(Node node, InorderBuilder ib) {
        if (node == null) return;
        inorder(node.left, ib);
        if (!ib.first) ib.builder.append(", ");
        ib.builder.append(node.key).append("=").append(node.value);
        ib.first = false;
        inorder(node.right, ib);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue() не реализован");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll() не реализован");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet() не реализован");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values() не реализован");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet() не реализован");
    }
}
