package by.it.group410902.grigorev.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        public int key;
        public int height;
        public String data;
        public Node left, right;

        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            height = 1;
        }

        public int height(Node n) {
            return n != null ? n.height : 0;
        }

        public int getBFactor(Node n) {
            if (n == null)
                return 0;
            return height(n.right) - height(n.left);
        }

        public void fixHeight() {
            int hl = height(this.left);
            int hr = height(this.right);
            this.height = Math.max(hl, hr) + 1;
        }

        public Node rotateRight() {
            Node tmpLeft = this.left;
            this.left = tmpLeft.right;
            tmpLeft.right = this;
            this.fixHeight();
            tmpLeft.fixHeight();
            return tmpLeft;
        }

        public Node rotateLeft() {
            Node tmpRight = this.right;
            this.right = tmpRight.left;
            tmpRight.left = this;
            this.fixHeight();
            tmpRight.fixHeight();
            return tmpRight;
        }

        public Node balance() {
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
            if (this.left == null)
                return this;
            else
                return this.left.findMin();
        }

        public Node removeMin() {
            if (this.left == null)
                return this.right;
            this.left = this.left.removeMin();
            return this.balance();
        }
    }

    private Node recGet(Node n, Integer key) {
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
        if (n == null)
            return new Node(key, data);
        if (key < n.key)
            n.left = recInsert(n.left, key, data);
        else
            n.right = recInsert(n.right, key, data);
        return n.balance();
    }

    private Node recRemove(Node n, Object key) {
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Node res = recGet(tree, (Integer) key);
        return res != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        Node tmp = recGet(tree, (Integer) key);
        if (tmp == null)
            return null;
        return tmp.data;
    }

    @Override
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

