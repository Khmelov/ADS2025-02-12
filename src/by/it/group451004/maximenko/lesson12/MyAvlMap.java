package by.it.group451004.maximenko.lesson12;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Objects;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    // -------------------- Вспомогательные методы для AVL --------------------

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // поворот
        x.right = y;
        y.left = T2;

        // обновление высот
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // поворот
        y.left = x;
        x.right = T2;

        // обновление высот
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node rebalance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        // Left heavy
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        // Right heavy
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    // -------------------- Поиск --------------------

    private Node findNode(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return node;
            node = (cmp < 0) ? node.left : node.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return false;
        return findNode(root, (Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return null;
        Node n = findNode(root, (Integer) key);
        return n == null ? null : n.value;
    }

    // -------------------- Вставка --------------------

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        Holder prev = new Holder();
        root = insert(root, key, value, prev);
        if (prev.found) return prev.value;
        size++;
        return null;
    }

    // вспомогательный контейнер для возвращаемого предыдущего значения
    private static class Holder {
        boolean found = false;
        String value = null;
    }

    private Node insert(Node node, Integer key, String value, Holder prev) {
        if (node == null) {
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            prev.found = true;
            prev.value = node.value;
            node.value = value;
            return node;
        } else if (cmp < 0) {
            node.left = insert(node.left, key, value, prev);
        } else {
            node.right = insert(node.right, key, value, prev);
        }
        return rebalance(node);
    }

    // -------------------- Удаление --------------------

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return null;
        RemHolder rh = new RemHolder();
        root = remove(root, (Integer) key, rh);
        if (rh.removed) {
            size--;
            return rh.prevValue;
        } else {
            return null;
        }
    }

    private static class RemHolder {
        boolean removed = false;
        String prevValue = null;
    }

    private Node remove(Node node, Integer key, RemHolder rh) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, rh);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, rh);
        } else {
            // найден узел
            rh.removed = true;
            rh.prevValue = node.value;
            // если один из детей null
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // оба ребенка существуют: заменить на минимальный в правом поддереве
            Node successor = minNode(node.right);
            // копируем ключ/значение преемника в текущий узел
            node.key = successor.key;
            node.value = successor.value;
            // удалить преемника из правого поддерева
            node.right = remove(node.right, successor.key, new RemHolder()); // здесь не нужно возвращать значение
        }
        return rebalance(node);
    }

    private Node minNode(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    // -------------------- Прочие обязательные методы --------------------

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

    // -------------------- toString() — in-order traversal --------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        InOrderPrinter p = new InOrderPrinter(sb);
        p.traverse(root);
        sb.append("}");
        return sb.toString();
    }

    private static class InOrderPrinter {
        private final StringBuilder sb;
        private boolean first = true;

        InOrderPrinter(StringBuilder sb) {
            this.sb = sb;
        }

        void traverse(Node node) {
            if (node == null) return;
            traverse(node.left);
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(node.key).append("=").append(node.value);
            traverse(node.right);
        }
    }

    // -------------------- Остальные методы Map — необязательные (throw) --------------------

    @Override
    public boolean containsValue(Object value) {
        // можно реализовать поиском по всему дереву, но для простоты:
        throw new UnsupportedOperationException("containsValue is not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet is not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values is not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet is not implemented");
    }

    // Optional overrides for equals/hashCode могут быть добавлены при необходимости.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;
        // Можно реализовать полное сравнение, но опустим для краткости
        throw new UnsupportedOperationException("equals is not implemented");
    }

    @Override
    public int hashCode() {
        // Можно реализовать обход и суммирование хешей, но опустим
        throw new UnsupportedOperationException("hashCode is not implemented");
    }

    // -------------------- Тестовый пример в main --------------------
    // Для проверки можно раскомментировать main и запустить.
    public static void main(String[] args) {
        MyAvlMap map = new MyAvlMap();
        map.put(5, "five");
        map.put(2, "two");
        map.put(8, "eight");
        map.put(1, "one");
        map.put(3, "three");
        System.out.println(map); // ожидаемо: {1=one, 2=two, 3=three, 5=five, 8=eight}
        System.out.println("get(3) = " + map.get(3));
        System.out.println("containsKey(4) = " + map.containsKey(4));
        map.remove(2);
        System.out.println("after remove 2: " + map);
        System.out.println("size = " + map.size());
    }
}

