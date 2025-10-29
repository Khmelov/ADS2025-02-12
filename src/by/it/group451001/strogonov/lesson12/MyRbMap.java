package by.it.group451001.strogonov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static enum direction{lef, rig}
    private static enum color{red, black}

    private color getClr(node n){
        return n == null || n.clr == color.black ? color.black : color.red;
    }
    private static class node{
        public Integer key;
        public String  val;
        public color clr;
        public node left_child, right_child, parent;
        public node(Integer key, String val, color clr, node parent){
            this.clr = clr;
            this.key = key;
            this.val = val;
            this.parent = parent;
        }
    }

    private int len;
    private node head;

    private void rightRotate(node tmp){
        if (tmp == head)
            head = tmp.left_child;
        else
            if (tmp.parent.left_child == tmp)
                tmp.parent.left_child = tmp.left_child;
            else
                tmp.parent.right_child = tmp.left_child;
        tmp.left_child.parent = tmp.parent;
        var d = tmp.left_child;
        tmp.left_child = d.right_child;
        if (d.right_child != null)
            d.right_child.parent = tmp;
        d.right_child = tmp;
        tmp.parent = d;
        head.clr = color.black;
    }

    private void leftRotate(node tmp){
        if (tmp == head)
            head = tmp.right_child;
        else
        if (tmp.parent.left_child == tmp)
            tmp.parent.left_child = tmp.right_child;
        else
            tmp.parent.right_child = tmp.right_child;
        tmp.right_child.parent = tmp.parent;
        var d = tmp.right_child;
        tmp.right_child = d.left_child;
        if (d.left_child != null)
            d.left_child.parent = tmp;
        d.left_child = tmp;
        tmp.parent = d;
        head.clr = color.black;
    }

    private void BigleftRotate(node tmp){
        var left_child = tmp.left_child;
        var left_grandchild = left_child.right_child;

        if (left_grandchild.left_child != null)
            left_grandchild.left_child.parent = left_child;
        left_child.right_child = left_grandchild.left_child;

        if (left_grandchild.right_child != null)
            left_grandchild.right_child.parent = tmp;
        tmp.left_child = left_grandchild.right_child;

        left_grandchild.parent = tmp.parent;

        tmp.parent = left_grandchild;
        left_grandchild.right_child = tmp;

        left_child.parent = left_grandchild;
        left_grandchild.left_child = left_child;
    }

    private void BigrightRotate(node tmp){
        var right_child = tmp.right_child;
        var right_grandchild = right_child.left_child;

        if (right_grandchild.right_child != null)
            right_grandchild.right_child.parent = tmp;
        tmp.right_child = right_grandchild.right_child;

        if (right_grandchild.left_child != null)
            right_grandchild.left_child.parent = right_child;
        right_child.left_child = right_grandchild.left_child;

        right_grandchild.parent = tmp.parent;

        tmp.parent = right_grandchild;
        right_grandchild.left_child = tmp;

        right_child.parent = right_grandchild;
        right_grandchild.right_child = right_child;
    }

    private void fixInsertion(node cur){
        if (cur == head) {
            head.clr = color.black;
            return;
        }
        if (cur.parent.clr == color.black)
            return;
        var uncle = cur.parent == cur.parent.parent.left_child ? cur.parent.parent.right_child
                : cur.parent.parent.left_child;
        if (getClr(uncle) == color.red){
            uncle.clr = color.black;
            cur.parent.clr = color.black;
            cur.parent.parent.clr = color.red;
            fixInsertion(cur.parent.parent);
            return;
        }
        var parent = cur.parent;
        var grand = cur.parent.parent;
        if (cur == parent.right_child && parent == grand.left_child){
            cur.parent = grand;
            grand.left_child = cur;
            parent.parent = cur;
            parent.right_child = cur.left_child;
            if (cur.left_child != null)
                cur.left_child.parent = parent;
            cur.left_child = parent;
            var t = parent;
            parent = cur;
            cur = t;
        }
        if (cur == parent.left_child && parent == grand.right_child){
            cur.parent = grand;
            grand.right_child = cur;
            parent.parent = cur;
            parent.left_child = cur.right_child;
            if (cur.right_child != null)
                cur.right_child.parent = parent;
            cur.right_child = parent;
            var t = parent;
            parent = cur;
            cur = t;
        }
        grand.clr = color.red;
        parent.clr = color.black;
        if (cur == parent.left_child) {
            rightRotate(grand);
            return;
        }
        if (cur != parent.right_child) {
            head.clr = color.black;
            System.out.print("пиздец");
        }
        else {
            leftRotate(grand);
        }
    }

    private void fixDeleting(node cur, direction dir){
        if (dir == direction.rig){
            if (cur.clr == color.red &&
               getClr(cur.left_child.left_child) == color.black &&
               getClr(cur.left_child.right_child) == color.black){
                    cur.left_child.clr = color.red;
                    cur.clr = color.black;
                    return;
            } //КЧ1
            if (cur.clr == color.red && getClr(cur.left_child.left_child) == color.red){
                cur.clr = color.black;
                cur.left_child.clr = color.red;
                cur.left_child.left_child.clr = color.black;
                rightRotate(cur);
                return;
            }//КЧ2
            if (cur.clr == color.black && cur.left_child.clr == color.red &&
            getClr(cur.left_child.right_child.left_child) == color.black && getClr(cur.left_child.right_child.right_child) == color.black){
                cur.left_child.clr = color.black;
                cur.left_child.right_child.clr = color.red;
                rightRotate(cur);
                return;
            }//ЧК3
            if (cur.left_child.clr == color.red && getClr(cur.left_child.right_child.left_child) == color.red){
                cur.left_child.right_child.left_child.clr = color.black;
                BigleftRotate(cur);
            }//ЧК4
            if (getClr(cur.left_child.right_child) == color.red){
                cur.left_child.right_child.clr = color.black;
                BigleftRotate(cur);
            }//ЧК5
            if (cur.clr == color.black && cur.left_child.clr == color.black){
                cur.left_child.clr = color.red;
                fixDeleting(cur.parent, dir);
                return;
            }//ЧК6
            System.out.print("пизда");
            return;
        }
        if (cur.clr == color.red &&
                getClr(cur.right_child.left_child) == color.black &&
                getClr(cur.right_child.right_child) == color.black){
            cur.right_child.clr = color.red;
            cur.clr = color.black;
            return;
        } //КЧ1
        if (cur.clr == color.red && getClr(cur.right_child.right_child) == color.red){
            cur.clr = color.black;
            cur.right_child.clr = color.red;
            cur.right_child.right_child.clr = color.black;
            leftRotate(cur);
            return;
        }//КЧ2
        if (cur.clr == color.black && cur.right_child.clr == color.red &&
                getClr(cur.right_child.left_child.left_child) == color.black && getClr(cur.right_child.left_child.right_child) == color.black){
            cur.right_child.clr = color.black;
            cur.right_child.left_child.clr = color.red;
            leftRotate(cur);
            return;
        }//ЧК3
        if (cur.right_child.clr == color.red && getClr(cur.right_child.left_child.right_child) == color.red){
            cur.right_child.left_child.right_child.clr = color.black;
            BigrightRotate(cur);
        }//ЧК4
        if (getClr(cur.right_child.left_child) == color.red){
            cur.right_child.left_child.clr = color.black;
            BigrightRotate(cur);
        }//ЧК5
        if (cur.clr == color.black && cur.right_child.clr == color.black){
            cur.right_child.clr = color.red;
            fixDeleting(cur.parent, dir);
            return;
        }//ЧК6
        System.out.print("пизда");
    }

    private node next(node cur){
        if (cur.right_child != null){
            cur = cur.right_child;
            while (cur.left_child != null)
                cur = cur.left_child;
            return cur;
        }
        while (cur.parent != null && cur.parent.right_child == cur)
            cur = cur.parent;
        return cur.parent;
    }

    private node prev(node cur){
        if (cur.left_child != null){
            cur = cur.left_child;
            while (cur.right_child != null)
                cur = cur.right_child;
            return cur;
        }
        while (cur.parent != null && cur.parent.left_child == cur)
            cur = cur.parent;
        return cur.parent;
    }

    private node getNode(Integer key){
        node prev = null, cur = head;
        while (cur != null){
            if (Objects.equals(cur.key, key))
                return cur;
            prev = cur;
            cur = key < cur.key ? cur.left_child : cur.right_child;
        }
        return prev;
    }

    public MyRbMap(){
        head = null;
        len = 0;
    }

    @Override
    public String toString(){
        if (len == 0)
            return "{}";
        var res = new StringBuilder("{");
        var tmp = head;
        while (tmp.left_child != null)
            tmp = tmp.left_child;
        var end = lastKey();
        while (tmp != null){
            res.append(tmp.key).append('=').append(tmp.val).append(", ");
            tmp = next(tmp);
        }
        res.deleteCharAt(res.length() - 1);
        res.replace(res.length() - 1, res.length(), "}");
        return res.toString();
    }


    @Override
    public String put(Integer key, String value) {
        if (key == 1276){
            head.clr = color.black;
        }
        if (len == 0){
            head = new node(key, value, color.black, null);
            len = 1;
            return null;
        }
        var tmp = getNode(key);
        if (Objects.equals(tmp.key, key)){
            var res = tmp.val;
            tmp.val = value;
            return res;
        }
        len++;
        if (key < tmp.key) {
            tmp.left_child = new node(key, value, color.red, tmp);
            tmp = tmp.left_child;
        }
        else {
            tmp.right_child = new node(key, value, color.red, tmp);
            tmp = tmp.right_child;
        }
        fixInsertion(tmp);
        return null;
    }

    @SuppressWarnings("all")
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || len == 0) {
            return null;
        } //Валидация
        if (len == 1){
            if (key.equals(head.key)){
                var res = head.val;
                len = 0;
                head = null;
                return res;
            }
            return null;
        } //Если надо только голову обнулить
        var tmp = getNode((Integer) key);
        if (!key.equals(tmp.key))
            return null;
        len--;
        final var res = tmp.val;
        if (tmp.left_child != null && tmp.right_child != null) {
            final var d = next(tmp);
            tmp.key = d.key;
            tmp.val = d.val;
            tmp = d;
        }
        var kid = tmp.left_child != null ? tmp.left_child : tmp.right_child;
        if (kid != null){
            tmp.val = kid.val;
            tmp.key = kid.key;
            if (kid == tmp.left_child)
                tmp.left_child = null;
            else
                tmp.right_child = null;
            return res;
        }
        if (tmp.clr == color.red){
            if (tmp.parent.left_child == tmp)
                tmp.parent.left_child = null;
            else
                tmp.parent.right_child = null;
            return res;
        }

        if (tmp == tmp.parent.right_child){
            tmp.parent.right_child = null;
            fixDeleting(tmp.parent, direction.rig);
        }
        else{
            tmp.parent.left_child = null;
            fixDeleting(tmp.parent, direction.lef);
        }
        return res;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer))
            return null;
        var tmp = getNode((Integer) key);
        return tmp.key.equals(key) ? tmp.val : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || len == 0)
            return false;
        return Objects.equals(getNode((Integer) key).key, key);
    }

    private boolean dfs(String target, node cur){
        if (cur.val.equals(target))
            return true;
        if (cur.left_child != null && cur.right_child != null)
            return dfs(target, cur.left_child) || dfs(target, cur.right_child);
        if (cur.left_child != null)
            return dfs(target, cur.left_child);
        if (cur.right_child != null)
            return dfs(target, cur.right_child);
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (len == 0)
            return false;
        if (value.getClass() != String.class)
            return false;
        return dfs((String) value, head);
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public void clear() {
        head = null;
        len = 0;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        SortedMap<Integer, String> res = new MyRbMap();
        var tmp = head;
        while (tmp.left_child != null)
            tmp = tmp.left_child;
        while (tmp != null && tmp.key < toKey){
            res.put(tmp.key, tmp.val);
            tmp = next(tmp);
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        SortedMap<Integer, String> res = new MyRbMap();
        var tmp = head;
        while (tmp.right_child != null)
            tmp = tmp.right_child;
        while (tmp != null && tmp.key >= fromKey){
            res.put(tmp.key, tmp.val);
            tmp = prev(tmp);
        }
        return res;
    }

    @Override
    public Integer firstKey() {
        var tmp = head;
        while (tmp.left_child != null)
            tmp = tmp.left_child;
        return tmp.key;
    }

    @Override
    public Integer lastKey() {
        var tmp = head;
        while (tmp.right_child != null)
            tmp = tmp.right_child;
        return tmp.key;
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////
    /// Эти методы не требуют реализации
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
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
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}
