package by.it.group451004.levkovich.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer,String> {

    private AVL_tree_node root = null;
    private int number_of_element = 0;

    public MyAvlMap(){}

    @Override
    public String toString(){
        if (this.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        this.inorder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int size() {
        return this.number_of_element;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public void clear() {
        this.number_of_element = 0;
        this.root = null;
    }



    private int height(AVL_tree_node n) {
        return n == null ? 0 : n.Height;
    }

    private void updateHeight(AVL_tree_node n) {
        n.Height = 1 + Math.max(this.height(n.Left_child), this.height(n.Right_child));
    }

    private int balanceFactor(AVL_tree_node n) {
        return n == null ? 0 : this.height(n.Left_child) - this.height(n.Right_child);
    }



    private AVL_tree_node rotateRight(AVL_tree_node y) {
        AVL_tree_node x = y.Left_child;
        AVL_tree_node T2 = x.Right_child;

        x.Right_child = y;
        y.Left_child = T2;

        this.updateHeight(y);
        this.updateHeight(x);

        return x;
    }

    private AVL_tree_node rotateLeft(AVL_tree_node x) {
        AVL_tree_node y = x.Right_child;
        AVL_tree_node T2 = y.Left_child;

        y.Left_child = x;
        x.Right_child = T2;

        this.updateHeight(x);
        this.updateHeight(y);

        return y;
    }

    private AVL_tree_node balance(AVL_tree_node n) {
        this.updateHeight(n);
        int bf = this.balanceFactor(n);

        if (bf > 1) {
            if (this.balanceFactor(n.Left_child) < 0) {
                n.Left_child = this.rotateLeft(n.Left_child);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (this.balanceFactor(n.Right_child) > 0) {
                n.Right_child = this.rotateRight(n.Right_child);
            }
            return this.rotateLeft(n);
        }
        return n;
    }


    @Override
    public String put(Integer key, String value) {
        String old = this.get(key);
        root = this.put(root, key, value);
        return old;
    }

    private AVL_tree_node put(AVL_tree_node node, Integer key, String value) {
        if (node == null) {
            number_of_element++;
            return new AVL_tree_node(key, value);
        }
        if (key < node.Key) {
            node.Left_child = this.put(node.Left_child, key, value);
        } else if (key > node.Key) {
            node.Right_child = this.put(node.Right_child, key, value);
        } else {
            node.Value = value; // перезапись
            return node;
        }
        return this.balance(node);
    }

    private AVL_tree_node getMin(AVL_tree_node node) {
        return node.Left_child == null ? node : this.getMin(node.Left_child);
    }

    private AVL_tree_node removeMin(AVL_tree_node node) {
        if (node.Left_child == null) return node.Right_child;
        node.Left_child = this.removeMin(node.Left_child);
        return this.balance(node);
    }

    private AVL_tree_node remove(AVL_tree_node node, Integer key) {
        if (node == null) return null;

        if (key < node.Key) {
            node.Left_child = this.remove(node.Left_child, key);
        } else if (key > node.Key) {
            node.Right_child = this.remove(node.Right_child, key);
        } else {
            number_of_element--;
            if (node.Right_child == null) return node.Left_child;
            if (node.Left_child == null) return node.Right_child;

            AVL_tree_node min = this.getMin(node.Right_child);
            min.Right_child = this.removeMin(node.Right_child);
            min.Left_child = node.Left_child;
            node = this.balance(min);
        }
        return this.balance(node);
    }

    private String get(AVL_tree_node node, Integer key) {
        if (node == null) return null;
        if (key < node.Key) return this.get(node.Left_child, key);
        if (key > node.Key) return this.get(node.Right_child, key);
        return node.Value;
    }

    private boolean containsKey(AVL_tree_node node, Integer key) {
        if (node == null) return false;
        if (key < node.Key) return this.containsKey(node.Left_child, key);
        if (key > node.Key) return this.containsKey(node.Right_child, key);
        return true;
    }

    private void inorder(AVL_tree_node node, StringBuilder sb) {
        if (node == null) return;
        this.inorder(node.Left_child, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.Key).append("=").append(node.Value);
        this.inorder(node.Right_child, sb);
    }


    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String old = this.get((Integer) key);
        if (old != null) root = this.remove(root, (Integer) key);
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return this.get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return this.containsKey(root, (Integer) key);
    }


    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    private class AVL_tree_node{
        private int Key = 0, Height = 0;
        private String Value = "";
        private AVL_tree_node Left_child = null;
        private AVL_tree_node Right_child = null;
        AVL_tree_node(int Key, String Value){
            this.Key = Key;

            this.Value = Value;
            this.Height = 1;
        }
        AVL_tree_node(int Key, String Value, AVL_tree_node left_child){
            this.Key = Key;
            this.Value = Value;
            this.Left_child = left_child;
            this.Height = Math.max(2, left_child.Height + 1);
        }
        AVL_tree_node(int Key, String Value, AVL_tree_node left_child, AVL_tree_node right_child){
            this.Key = Key;
            this.Value = Value;
            this.Left_child = left_child;
            this.Right_child = right_child;
            this.Height = Math.max(Math.max(2, left_child.Height + 1), right_child.Height+1);
        }

        public String Get_by_key(){return this.Value;}
        public int Get_height(){return this.Height;}
        public AVL_tree_node Get_child(short which){
            switch (which){
                case 0:
                    return this.Left_child;
                case 1:
                    return this.Right_child;
                default:
                    throw new IllegalStateException("Unexpected value: " + which);
            }
        }
        public void Set_child(AVL_tree_node new_child, short which_child){
            switch (which_child){
                case 0:
                    this.Left_child = new_child;
                    this.Height = Math.max(this.Height, new_child.Height+1);
                    break;
                case 1:
                    this.Right_child = new_child;
                    this.Height = Math.max(this.Height, new_child.Height+1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + which_child);
            }
        }
    }

}