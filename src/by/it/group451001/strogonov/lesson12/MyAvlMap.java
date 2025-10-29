package by.it.group451001.strogonov.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class node{
        public int key, d, h;
        public String val;
        public node parent, left, right;
        public node(int key, String val, node parent){
            this.key = key;
            this.val = val;
            this.parent = parent;
            this.left = this.right = null;
            h = 1;
            d = 0;
        }
        public void recalculate(){
            h = Math.max(left == null ? 0 : left.h, right == null ? 0 : right.h) + 1;
            d = (left == null ? 0 : left.h) - (right == null ? 0 : right.h);
        }
    }

    private node prev(node cur){
        if (cur.left != null){
            cur = cur.left;
            while (cur.right != null)
                cur = cur.right;
        }
        else{
            while (cur.parent != null && cur == cur.parent.left)
                cur = cur.parent;
            cur = cur.parent;
        }
        return cur;
    }

    private node next(node cur){
        if (cur.right != null){
            cur = cur.right;
            while (cur.left != null)
                cur = cur.left;
        }
        else{
            while (cur.parent != null && cur == cur.parent.right)
                cur = cur.parent;
            cur = cur.parent;
        }
        return cur;
    }

    private node leftRotate(node cur){
        var kid = cur.right;
        if (cur == head)
            head = kid;
        cur.right = kid.left;
        if (kid.left != null)
            kid.left.parent = cur;
        kid.parent = cur.parent;
        if (cur.parent != null)
            if (cur == cur.parent.left)
                cur.parent.left = kid;
            else
                cur.parent.right = kid;
        kid.left = cur;
        cur.parent = kid;
        cur.recalculate();
        kid.recalculate();
        return kid;
    }

    private node rightRotate(node cur){
        var kid = cur.left;
        if (cur == head)
            head = kid;
        cur.left = kid.right;
        if (kid.right != null)
            kid.right.parent = cur;
        kid.parent = cur.parent;
        if (cur.parent != null)
            if (cur == cur.parent.left)
                cur.parent.left = kid;
            else
                cur.parent.right = kid;
        kid.right = cur;
        cur.parent = kid;
        cur.recalculate();
        kid.recalculate();
        return kid;
    }

    private node getNode(Integer key){
        node cur = head, res = null;
        while (cur != null){
            if (cur.key == key)
                return cur;
            res = cur;
            cur = cur.key < key ? cur.right : cur.left;
        }
        return res;
    }

    private void balance(node cur){
        if (cur == null)
            return;
        cur.recalculate();
        if (cur.d == -2)
            if (cur.right.d == 0 || cur.right.d == -1)
                cur = leftRotate(cur);
            else{
                rightRotate(cur.right);
                cur = leftRotate(cur);
            }
        else
        if (cur.d == 2)
            if (cur.left.d == 0 || cur.left.d == 1)
                cur = rightRotate(cur);
            else{
                leftRotate(cur.left);
                cur = rightRotate(cur);
            }
        balance(cur.parent);
    }

    private int len;
    private node head;

    public MyAvlMap(){
        len = 0;
        head = null;
    }

    @Override
    public String toString(){
        if (len == 0)
            return "{}";
        StringBuilder res = new StringBuilder("{");
        for (var tmp = firstNode(); tmp != null; tmp = next(tmp))
            res.append(tmp.key).append("=").append(tmp.val).append(", ");
        res.deleteCharAt(res.length() - 1).deleteCharAt(res.length() - 1).append("}");
        return res.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (len == 0){
            len++;
            head = new node(key, value, null);
            return null;
        }
        var tmp = getNode(key);
        if (tmp.key == key){
            var res = tmp.val;
            tmp.val = value;
            return res;
        }
        len++;
        if (tmp.key < key)
            tmp.right = new node(key, value, tmp);
        else
            tmp.left = new node(key, value, tmp);
        balance(tmp);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || len == 0)
            return null;
        var tmp = getNode((Integer) key);
        if (tmp.key != (Integer) key)
            return null;
        var res = tmp.val;
        len--;
        if (tmp.left != null && tmp.right != null){
            var d = next(tmp);
            tmp.key = d.key;
            tmp.val = d.val;
            tmp = d;
        }
        if (tmp.left != null){
            tmp.left.parent = tmp.parent;
            if (tmp.parent != null)
                if (tmp.parent.left == tmp)
                    tmp.parent.left = tmp.left;
                else
                    tmp.parent.right = tmp.left;
            else
                head = tmp.left;
        }
        else if (tmp.right != null){
            tmp.right.parent = tmp.parent;
            if (tmp.parent != null)
                if (tmp.parent.right == tmp)
                    tmp.parent.right = tmp.right;
                else
                    tmp.parent.left = tmp.right;
            else
                head = tmp.right;
        }
        else{
            if (tmp.parent != null)
                if (tmp.parent.right == tmp)
                    tmp.parent.right = null;
                else
                    tmp.parent.left = null;
            else
                head = null;
        }
        balance(tmp.parent);
        return res;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof  Integer) || len == 0)
            return null;
        var tmp = head;
        while (tmp != null && tmp.key != (Integer) key)
            if (tmp.key < (Integer) key)
                tmp = tmp.right;
            else
                tmp = tmp.left;
        return tmp == null ? null : tmp.val;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public void clear() {
        len = 0;
        head = null;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    private node firstNode(){
        var res = head;
        while (res.left != null)
            res = res.left;
        return res;
    }
/// ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean containsValue(Object value) {
        return false;
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
