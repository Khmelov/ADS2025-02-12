package by.it.group410902.podryabinkin.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {


    class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;
        public Node(Integer key, String value){
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;

        }
    }

    private Node root = null;
    private Integer size = 0;



    //!!!!не путать повороты с обозначением дисбаланса!!!!!!!!

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    // Правый поворот (LL)
    private Node LL(Node y) {
        if (y == null || y.left == null) return y; // безопасно

        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }
    // Левый
    private Node RR(Node x) {
        if (x == null || x.right == null) return x; // безопасно

        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }
    private Node LR(Node node) {
        node.left = LL(node.left);
        return RR(node);
    }
    private Node RL(Node node){
        node.right = RR(node.right);
        return LL(node);

    }

    private String toStringDop(String inp, Node cur){
        if(cur == null) return inp;
        if(cur.left != null) inp = toStringDop(inp, cur.left);
        inp += cur.key + "=" + cur.value + ", ";
        if(cur.right != null) inp = toStringDop(inp, cur.right);
        return inp;
    }
    public String toString(){
        String outp = "{";
        outp = toStringDop(outp, root);
        //убираем запятую и пробел
        if (outp.endsWith(", ")) outp = outp.substring(0, outp.length() - 2);
        outp += "}";
        return outp;
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

    @Override
    public String get(Object key) {
        Node cur = root;
        while (cur != null){
            if(cur.key.equals(key)) return cur.value;
            else if(cur.key < (Integer) key){
                cur = cur.right;
            }
            else if (cur.key > (Integer) key) {
                cur = cur.left;
            }
        }
        return null;
    }


    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value);
        } else if (key > node.key) {
            node.right = put(node.right, key, value);
        } else {
            // ключ уже есть
            node.value = value;
            return node;
        }

        // Обновляем высоту
        node.height = 1 + Math.max(height(node.left), height(node.right));
        // Баланс
        int balance = height(node.left) - height(node.right);
        // Случаи дисбаланса
        if (balance > 1 && key < node.left.key) return LL(node);      // LL
        if (balance < -1 && key > node.right.key) return RR(node);    // RR
        if (balance > 1 && key > node.left.key) return LR(node);      // LR
        if (balance < -1 && key < node.right.key) return RL(node);    // RL

        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key); // если нужно вернуть старое значение
        root = put(root, key, value);
        return oldValue;
    }


    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        // ищем
        if (key < node.key) node.left = remove(node.left, key);
        else if (key > node.key) node.right = remove(node.right, key);
        else {

            if (node.left == null || node.right == null) {
                Node temp = node.left;
                if(node.left == null) temp = node.right;
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                // Узел с двумя потомками
                Node cur1 = node.right;
                while (cur1.left != null) cur1 = cur1.left;
                Node temp = cur1; // наименьший в правом поддереве

                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key);
            }
        }

        if (node == null) return null;

        //высота
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // балансируем
        int balance = height(node.left) - height(node.right);

        if (balance > 1 && height(node.left.left) >= height(node.left.right)) return LL(node);
        if (balance > 1 && height(node.left.left) < height(node.left.right)) return LR(node);
        if (balance < -1 && height(node.right.right) >= height(node.right.left)) return RR(node);
        if (balance < -1 && height(node.right.right) < height(node.right.left)) return RL(node);

        return node;
    }
    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        root = remove(root, (Integer) key);
        return oldValue;
    }

    @Override
    public boolean containsKey(Object key) {
        Node cur = root;
        while(cur != null){
            if(cur.key == (Integer) key) return true;
            else if(cur.key < (Integer) key) cur = cur.right;
            else if(cur.key > (Integer) key) cur = cur.left;
        }
        return false;
    }



    @Override
    public boolean containsValue(Object value) {
        return false;
    }



    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

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
