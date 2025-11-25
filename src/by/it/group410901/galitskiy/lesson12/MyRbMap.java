package by.it.group410901.galitskiy.lesson12;
import java.util.*;
public class MyRbMap implements SortedMap<Integer, String> {

    // Красно-черное дерево использует следующие свойства для балансировки:
    // Каждый узел либо красный, либо черный.
    // Корень дерева всегда черный.
    // Все листья (пустые узлы) считаются черными.
    // У любого красного узла оба его дочерних узла черные.
    // Любой путь от узла до его листьев содержит одинаковое количество черных узлов.
    private enum colors {RED, BLACK}

    private class Node {
        // Представляет узел дерева. Содержит ключ, данные, ссылки на дочерние узлы, родителя и цвет узла
        public int key;
        public String data;
        public Node left, right, parent;
        public colors color;

        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.color = colors.RED;
        }

        // Выполняют левый или правый поворот дерева для восстановления сбалансированности
        public void rotateLeft() {
            Node r = this.right;
            Node p = this.parent;
            if (p != null)
                if (p.left == this)
                    p.left = r;
                else
                    p.right = r;
            this.right = r.left;
            if (r.left != null)
                r.left.parent = this;
            r.left = this;
            this.parent = r;
            r.parent = p;
        }

        public void rotateRight() {
            Node l = this.left;
            Node p = this.parent;
            if (p != null)
                if (p.left == this)
                    p.left = l;
                else
                    p.right = l;
            this.left = l.right;
            if (l.right != null)
                l.right.parent = this;
            l.right = this;
            this.parent = l;
            l.parent = p;
        }

        public Node findMin() {  // Находит узел с минимальным ключом в поддереве
            if (this.left == null)
                return this;
            else
                return this.left.findMin();
        }
    }

    private Node head = null;
    int size = 0;

    // Проверяют цвет узла
    private boolean isRed(Node n) {
        return n != null && n.color == colors.RED;
    }

    private boolean isBlack(Node n) {
        return (n == null || n.color == colors.BLACK);
    }

    private Node sibling(Node n) {
        // Возвращает "брата" узла, который используется при удалении для восстановления баланса
        if (n == n.parent.left)
            return n.parent.right;
        else
            return n.parent.left;
    }

    // Получают деда и дядю узла, которые используются при балансировке.
    private Node getGrandfather(Node n) {
        if (n != null && n.parent != null)
            return n.parent.parent;
        else
            return null;
    }

    private Node getUncle(Node n) {
        Node g = getGrandfather(n);
        if (g != null)
            if (g.left == n.parent)
                return g.right;
            else
                return g.left;
        else
            return null;
    }

    private Node getChild(Node n) {
        return n.right == null ? n.left : n.right;
    }

    private void insert(Integer key, String data) {
        // Добавляет новый узел в дерево. Узел всегда сначала вставляется как красный
        Node i = new Node(key, data);
        if (head == null)
            head = i;
        else {
            Node tmp = head;
            Node tmp_p = head;
            while (tmp != null) {
                tmp_p = tmp;
                if (key < tmp.key)
                    tmp = tmp.left;
                else
                    tmp = tmp.right;
            }
            i.parent = tmp_p;
            if (key < tmp_p.key)
                tmp_p.left = i;
            else
                tmp_p.right = i;
        }
        balanceInsert(i);
    }

    private void balanceInsert(Node n) {
        // Восстанавливает свойства красно-черного дерева после вставки узла.
        // Использует повороты и перекрашивание.
        if (n.parent == null) {
            n.color = colors.BLACK;
            return;
        }
        while (n.parent != null && n.parent.color == colors.RED) {
            Node g = getGrandfather(n);
            Node u = getUncle(n);
            if (g != null && g.left == n.parent) {
                if (u != null && u.color == colors.RED) {
                    n.parent.color = colors.BLACK;
                    u.color = colors.BLACK;
                    g.color = colors.RED;
                    n = g;
                } else {
                    if (n.parent.right == n) {
                        n = n.parent;
                        n.rotateLeft();
                    }
                    n.parent.color = colors.BLACK;
                    getGrandfather(n).color = colors.RED;
                    getGrandfather(n).rotateRight();
                }
            } else if (g != null && g.right == n.parent) {
                if (u != null && u.color == colors.RED) {
                    n.parent.color = colors.BLACK;
                    u.color = colors.BLACK;
                    g.color = colors.RED;
                    n = g;
                } else {
                    if (n.parent.left == n) {
                        n = n.parent;
                        n.rotateRight();
                    }
                    n.parent.color = colors.BLACK;
                    getGrandfather(n).color = colors.RED;
                    getGrandfather(n).rotateLeft();
                }
            }
            while (head.parent != null)
                head = head.parent;
        }
        head.color = colors.BLACK;
    }

    private void removeOneChild(Node n) {
        if (n.right == null)
            if (n.parent.left == n)
                n.parent.left = n.left;
            else
                n.parent.right = n.left;
        else if (n.parent.left == n)
            n.parent.left = n.right;
        else
            n.parent.right = n.right;
    }

    private void delete(Integer key) {
        // Удаляет узел по ключу. Если у узла два дочерних узла, заменяет его на
        // минимальный узел в правом поддереве. Затем вызывает балансировку
        Node deleted = recGet(head, key);
        if (deleted.right == null && deleted.left == null) {
            if (deleted == head)
                head = null;
            else {
                if (deleted.parent.left == deleted)
                    deleted.parent.left = null;
                else
                    deleted.parent.right = null;
            }
            return;
        }
        Node swapped = null;
        if (deleted.right != null && deleted.left != null) {
            swapped = deleted.right.findMin();
        }
        else
            swapped = getChild(deleted);
        deleted.data = swapped.data;
        deleted.key = swapped.key;
        removeOneChild(swapped);
        if (isBlack(swapped) || isBlack(deleted))
            balanceDelete(getChild(swapped));
        head.color = colors.BLACK;
    }

    private void balanceDelete(Node ch) {
        // Восстанавливает свойства красно-черного дерева после удаления узла
        while (ch != head && ch != null) {
            if (ch.parent.left == ch) {
                Node b = sibling(ch);
                if (isRed(b)) {
                    b.color = colors.BLACK;
                    ch.parent.color = colors.RED;
                    ch.parent.rotateLeft();
                }
                if (isBlack(b.right) && isBlack(b.left))
                    b.color = colors.RED;
                else {
                    if (isBlack(b.right)) {
                        b.left.color = colors.BLACK;
                        b.color = colors.RED;
                        b.rotateRight();
                    }
                    b.color = ch.parent.color;
                    ch.parent.color = colors.BLACK;
                    b.right.color = colors.BLACK;
                    ch.parent.rotateLeft();
                    ch = head;
                }
            } else {
                Node b = sibling(ch);
                if (isRed(b)) {
                    b.color = colors.BLACK;
                    ch.parent.color = colors.RED;
                    ch.parent.rotateLeft();
                }
                if (isBlack(b.right) && isBlack(b.left))
                    b.color = colors.RED;
                else {
                    if (isBlack(b.left)) {
                        b.right.color = colors.BLACK;
                        b.color = colors.RED;
                        b.rotateLeft();
                    }
                    b.color = ch.parent.color;
                    ch.parent.color = colors.BLACK;
                    b.left.color = colors.BLACK;
                    ch.parent.rotateRight();
                    ch = head;
                }
            }
        }
        while (head.parent != null)
            head = head.parent;
        head.color = colors.BLACK;
    }

    private void toStr(Node n, StringBuilder res) {
        if (n == null)
            return;
        toStr(n.left, res);
        res.append(n.key).append("=").append(n.data).append(", ");
        toStr(n.right, res);
    }

    @Override
    // Возвращает строковое представление дерева (обход ключей в порядке возрастания).
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(head, res);
        if (head != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));
        res.append("}");
        return String.valueOf(res);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    public void fillLower(Node n, Integer key) {
        if (n == null)
            return;
        fillLower(n.left, key);
        if (n.key < key)
            this.put(n.key, n.data);
        fillLower(n.right, key);
    }

    public void fillUpper(Node n, Integer key) {
        if (n == null)
            return;
        fillUpper(n.left, key);
        if (n.key >= key)
            this.put(n.key, n.data);
        fillUpper(n.right, key);
    }

    // Возвращают поддерево с ключами меньше/больше заданного.
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap tmp = new MyRbMap();
        tmp.fillLower(this.head, toKey);
        return tmp;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tmp = new MyRbMap();
        tmp.fillUpper(this.head, fromKey);
        return tmp;
    }

    //Возвращают минимальный/максимальный ключ в дереве
    @Override
    public Integer firstKey() {
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;
            tmp = tmp.left;
        }
        return tmp_p.key;
    }

    @Override
    public Integer lastKey() {
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;
            tmp = tmp.right;
        }
        return tmp_p.key;
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
    // Проверяет, существует ли узел с данным ключом
    public boolean containsKey(Object key) {
        Node res = recGet(head, (Integer) key);
        return res != null;
    }

    private Boolean ContainsValueF;

    private void recGetByValue(Node n, Object data) {
        if (n == null)
            return;
        recGetByValue(n.left, data);
        if (n.data.equals(data))
            ContainsValueF = true;
        recGetByValue(n.right, data);
    }

    @Override
    // Проверяет, существует ли узел с данным значением, рекурсивно обходя дерево
    public boolean containsValue(Object value) {
        ContainsValueF = false;
        recGetByValue(head, value);
        return ContainsValueF;
    }

    private Node recGet(Node n, Integer key) {
        // Рекурсивно ищет узел по ключу
        if (n == null)
            return null;
        else if (key < n.key)
            return recGet(n.left, key);
        else if (key > n.key)
            return recGet(n.right, key);
        else
            return n;
    }

    @Override
    public String get(Object key) {
        Node tmp = recGet(head, (Integer) key);
        if (tmp == null)
            return null;
        return tmp.data;
    }

    @Override
    // Добавляет ключ-значение, выполняя вставку и при необходимости балансировку.
    public String put(Integer key, String value) {
        Node retVal = recGet(head, key);
        if (retVal == null) {
            size++;
            insert(key, value);
            return null;
        }
        String pr = retVal.data;
        retVal.data = value;
        return pr;
    }

    @Override
    // Удаляет элемент по ключу, восстанавливает баланс
    public String remove(Object key) {
        String res = get(key);
        if (res != null) {
            size--;
            delete((Integer) key);
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    //  Удаляет все элементы из дерева
    public void clear() {
        head = null;
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
