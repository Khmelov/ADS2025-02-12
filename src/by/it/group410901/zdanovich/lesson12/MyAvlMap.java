package by.it.group410901.zdanovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    // реализует упрощённый Map, использующий сбалансированное дерево AVL для хранения пар "ключ-значение".
    private class Node {  // класс, представляющий узел дерева
        public int key;  // ключ узла
        public int height;  // высота узла (используется для балансировки)
        public String data;  // значение узла
        public Node left, right;  // ссылки на левого и правого ребёнка

        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            height = 1;
        }


        public int height(Node n) {
            return n != null ? n.height : 0;
        } // возвращает высоту узла

        public int getBFactor(Node n) {
            // вычисляет баланс-фактор узла (разница высот правого и левого поддеревьев)
            if (n == null)
                return 0;
            return height(n.right) - height(n.left);
        }

        public void fixHeight() {  // обновляет высоту текущего узла.
            int hl = height(this.left);
            int hr = height(this.right);
            this.height = Math.max(hl, hr) + 1;
        }

        public Node rotateRight() {
            // выполняет правый поворот для балансировки дерева
            Node tmpLeft = this.left;
            this.left = tmpLeft.right;
            tmpLeft.right = this;
            this.fixHeight();
            tmpLeft.fixHeight();
            return tmpLeft;
        }

        public Node rotateLeft() {
            // выполняет левый поворот для балансировки дерева
            Node tmpRight = this.right;
            this.right = tmpRight.left;
            tmpRight.left = this;
            this.fixHeight();
            tmpRight.fixHeight();
            return tmpRight;
        }

        public Node balance() {
            // проверяет баланс-фактор и выполняет необходимые повороты
            fixHeight();
            if (getBFactor(this) == 2) {
                if (getBFactor(this.right) < 0)
                    this.right = this.right.rotateRight();
                return rotateLeft();
            }
            if (getBFactor(this) == -2) {
                if (getBFactor(this.left) > 0)
                    this.left = this.left.rotateLeft();
                return rotateRight();
            }
            return this;
        }

        public Node findMin() {
            // ищет узел с минимальным ключом в поддереве
            if (this.left == null)
                return this;
            else
                return this.left.findMin();
        }

        public Node removeMin() {
            // удаляет узел с минимальным ключом из поддерева
            if (this.left == null)
                return this.right;
            this.left = this.left.removeMin();
            return this.balance();
        }
    }

    private Node recGet(Node n, Integer key) {  // Рекурсивно ищет узел с заданным ключом
        if (n == null)
            return null;
        else if (key < n.key)
            return recGet(n.left, key);
        else if (key > n.key)
            return recGet(n.right, key);
        else
            return n;
    }

    private Node recInsert(Node n, Integer key, String data) {
        // Рекурсивно вставляет новый узел в дерево, балансируя его после добавления
        if (n == null)
            return new Node(key, data);
        if (key < n.key)
            n.left = recInsert(n.left, key, data);
        else
            n.right = recInsert(n.right, key, data);
        return n.balance();
    }

    private Node recRemove(Node n, Object key) {
        // Рекурсивно удаляет узел с заданным ключом, балансируя дерево после удаления
        if (n == null)
            return null;
        if ((Integer) key < n.key)
            n.left = recRemove(n.left, key);
        else if ((Integer) key > n.key)
            n.right = recRemove(n.right, key);
        else {
            Node l = n.left;
            Node r = n.right;
            n = null;
            if (r == null)
                return l;
            Node min = r.findMin();
            min.right = r.removeMin();
            min.left = l;
            min.balance();
            return min;
        }
        return n.balance();
    }

    private Node tree = null;
    private int size = 0;

    private void toStr(Node n, StringBuilder res) {
        // Построение строкового представления дерева в порядке возрастания ключей
        if (n == null)
            return;
        toStr(n.left, res);
        res.append(n.key).append("=").append(n.data).append(", ");
        toStr(n.right, res);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(tree, res);
        if (tree != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));
        res.append("}");
        return String.valueOf(res);
    }

    @Override
    // Возвращает количество узлов в дереве
    public int size() {
        return size;
    }

    @Override
    // Проверяет, пустое ли дерево (size == 0)
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // Проверяет, существует ли узел с данным ключом в дереве (recGet)
    public boolean containsKey(Object key) {
        Node res = recGet(tree, (Integer) key);
        return res != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    // Возвращает значение, связанное с ключом, или null, если ключ не найден (recGet)
    public String get(Object key) {
        Node tmp = recGet(tree, (Integer) key);
        if (tmp == null)
            return null;
        return tmp.data;
    }

    @Override
    // Добавляет новую пару ключ-значение:
    // Если ключ уже существует, обновляет значение и возвращает старое.
    // Если ключа нет, вставляет новый узел и балансирует дерево
    public String put(Integer key, String value) {
        Node retVal = recGet(tree, key);
        if (retVal == null) {
            size++;
            tree = recInsert(tree, key, value);
            return null;
        }
        String pr = retVal.data;
        retVal.data = value;
        return pr;
    }


    @Override
    // Удаляет узел с данным ключом:
    // Если ключ найден, удаляет узел через recRemove, возвращает значение.
    // Если ключ не найден, возвращает null
    public String remove(Object key) {
        String res = get(key);
        if (res != null) {
            size--;
            tree = recRemove(tree, key);
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    // Очищает дерево
    public void clear() {
        tree = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}
