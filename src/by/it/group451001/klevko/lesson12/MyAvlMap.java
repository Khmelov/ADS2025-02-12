package by.it.group451001.klevko.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    class Node{
        int key;
        String data;
        Node right;
        Node left;
        int height;

        Node(int key, String data){
            this.key = key;
            this.data = data;
            this.right = null;
            this.left = null;
            this.height = 0;
        }
    }

    private int max(int one, int two){
        if (one > two) return one;
        else return two;
    }

    private void updateHeight(Node root){
        if (root == null) return;
        int right = root.right == null ? 0 : root.right.height;
        int left = root.left == null ? 0 : root.left.height;
        root.height = 1 + max(right, left);
    }

    private void rebalance(Node root) {
        int balance = root.left.height - root.right.height;
    }

    //возвращаем новый корень чтобы на месте присвоить куда надо
    private Node rightRotate(Node root){
        /* A
          /
         B
        /
       C */
        Node B = root.left, BRight = B.right;
        B.right = root;
        root.left = BRight;
        updateHeight(B);
        updateHeight(root);
        return B;
    }

    //возвращаем новый корень чтобы на месте присвоить куда надо
    private Node leftRotate(Node root){
        /*   A
              \
               B
                \
                 C */
        Node B = root.right, BLeft = B.left;
        B.left = root;
        root.right = BLeft;
        updateHeight(B);
        updateHeight(root);
        return B;
    }

    private Node leftRightRotate(){
        /*   A
              \
               B
              /
             C */
        //leftRotate()
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        return "";
    }

    @Override
    public String put(Integer key, String value) {
        return "";
    }

    @Override
    public String remove(Object key) {
        return "";
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {

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
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
