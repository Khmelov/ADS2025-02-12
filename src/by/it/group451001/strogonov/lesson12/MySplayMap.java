package by.it.group451001.strogonov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class node{
        public int key;
        public String val;
        public node left, right, parent;
        node(int key, String val, node parent){
            this.key = key;
            this.val = val;
            this.parent = parent;
            this.left = this.right = null;
        }
        public node next(){
            if (right != null){
                var tmp = right;
                while (tmp.left != null)
                    tmp = tmp.left;
                return tmp;
            }
            else{
                var tmp = this;
                while (tmp.parent != null && tmp.parent.right == tmp)
                    tmp = tmp.parent;
                return tmp.parent;
            }
        }
        public node prev(){
            if (left != null){
                var tmp = left;
                while (tmp.right != null)
                    tmp = tmp.right;
                return tmp;
            }
            else{
                var tmp = this;
                while (tmp.parent != null && tmp.parent.left == tmp)
                    tmp = tmp.parent;
                return tmp.parent;
            }
        }
    }

    node head;
    int len;

    private node getNode(int key){
        node tmp = head, res = null;
        while (tmp != null){
            if (tmp.key == key)
                return tmp;
            res = tmp;
            tmp = tmp.key < key ? tmp.right : tmp.left;
        }
        return res;
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
        return kid;
    }

    private void zig(node cur){
        if (head.left == cur)
            rightRotate(head);
        else if (head.right == cur)
            leftRotate(head);
    }

    private void splay(node cur) {
        if (cur == head)
            return;
        if (cur.parent == head)
            zig(cur);
        else{
            if (cur.parent.left == cur){
                if (cur.parent.parent.left == cur.parent){
                    rightRotate(cur.parent);
                    rightRotate(cur.parent);
                }
                else{
                    rightRotate(cur.parent);
                    leftRotate(cur.parent);
                }
            } else{
                if (cur.parent.parent.right == cur.parent){
                    leftRotate(cur.parent);
                    leftRotate(cur.parent);
                }
                else{
                    leftRotate(cur.parent);
                    rightRotate(cur.parent);
                }
            }
        }
        splay(cur);
    }

    @Override
    public String toString(){
        if (len == 0)
            return "{}";
        var tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        StringBuilder res = new StringBuilder("{");
        while (tmp != null){
            res.append(tmp.key).append("=").append(tmp.val).append(", ");
            tmp = tmp.next();
        }
        return res.delete(res.length() - 2, res.length()).append("}").toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (len == 0){
            head = new node(key, value, null);
            head.key = key;
            head.val = value;
            len = 1;
            return null;
        }
        var tmp = getNode(key);
        if (tmp.key == key){
            var res = tmp.val;
            tmp.val = value;
            splay(tmp);
            return res;
        }
        len++;
        if (tmp.key < key) {
            tmp.right = new node(key, value, tmp);
            splay(tmp.right);
        }
        else{
            tmp.left = new node(key, value, tmp);
            splay(tmp.left);
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || len == 0)
            return null;
        var tmp = getNode((Integer) key);
        if (tmp.key != (Integer) key) {
            splay(tmp);
            return null;
        }
        len--;
        var res = tmp.val;
        if (tmp.left != null && tmp.right != null){
            var d = tmp.next();
            tmp.key = d.key;
            tmp.val = d.val;
            tmp = d;
        }
        if (tmp.left == null && tmp.right == null){
            if (tmp.parent == null)
                clear();
            else{
                if (tmp.parent.left == tmp)
                    tmp.parent.left = null;
                else
                    tmp.parent.right = null;
                splay(tmp.parent);
            }
        }
        else if (tmp.left != null){
            if (tmp.parent == null){
                head = tmp.left;
                tmp.left.parent = null;
            }
            else{
                if (tmp.parent.left == tmp) {
                    tmp.parent.left = tmp.left;
                    tmp.left.parent = tmp.parent;
                }
                else {
                    tmp.parent.right = tmp.left;
                    tmp.left.parent = tmp.parent;
                }
                splay(tmp.parent);
            }
        }
        else{
            if (tmp.parent == null){
                head = tmp.right;
                tmp.right.parent = null;
            }
            else{
                if (tmp.parent.right == tmp) {
                    tmp.parent.right = tmp.right;
                    tmp.right.parent = tmp.parent;
                }
                else {
                    tmp.parent.left = tmp.right;
                    tmp.right.parent = tmp.parent;
                }
                splay(tmp.parent);
            }
        }
        return res;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || len == 0)
            return null;
        var tmp = getNode((Integer) key);
        splay(tmp);
        return tmp.key == (Integer) key ? tmp.val : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if ((Integer) key == 738)
            head = head;
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String) || len == 0)
            return false;
        var tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        node toSplay = tmp;
        while (tmp != null && !tmp.val.equals(value)) {
            toSplay = tmp;
            tmp = tmp.next();
        }
        if (tmp != null){
            splay(tmp);
            return true;
        }
        splay(toSplay);
        return false;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public void clear() {
        head = null;
        len = 0;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        var tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        var res = new MySplayMap();
        while (tmp.key < toKey || (tmp.key == toKey && inclusive)){
            res.put(tmp.key, tmp.val);
            tmp = tmp.next();
        }
        return res;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        var tmp = head;
        while (tmp.right != null)
            tmp = tmp.right;
        var res = new MySplayMap();
        while (tmp.key > fromKey || (tmp.key == fromKey && inclusive)){
            res.put(tmp.key, tmp.val);
            tmp = tmp.next();
        }
        return res;
    }

    @Override
    public Integer firstKey() {
        var tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        splay(tmp);
        return tmp.key;
    }

    @Override
    public Integer lastKey() {
        var tmp = head;
        while (tmp.right != null)
            tmp = tmp.right;
        splay(tmp);
        return tmp.key;
    }

    @SuppressWarnings("all")
    @Override
    public Integer lowerKey(Integer key) {
        node tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        node res = tmp;
        while (tmp != null && tmp.key < key){
            res = tmp;
            tmp = tmp.next();
        }
        splay(res);
        return res.key < key ? res.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        node tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        node res = tmp;
        while (tmp != null && tmp.key <= key){
            res = tmp;
            tmp = tmp.next();
        }
        splay(res);
        return res.key <= key ? res.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        node tmp = head;
        while (tmp.right != null)
            tmp = tmp.right;
        node res = tmp;
        while (tmp != null && tmp.key >= key){
            res = tmp;
            tmp = tmp.prev();
        }
        splay(res);
        return res.key >= key ? res.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        node tmp = head;
        while (tmp.right != null)
            tmp = tmp.right;
        node res = tmp;
        while (tmp != null && tmp.key > key){
            res = tmp;
            tmp = tmp.prev();
        }
        splay(res);
        return res.key > key ? res.key : null;
    }

    public MySplayMap(){
        head = null;
        len = 0;
    }

/// /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        SortedMap<Integer, String> res = new MyRbMap();
        var tmp = head;
        while (tmp.left != null)
            tmp = tmp.left;
        while (tmp != null && tmp.key < toKey){
            res.put(tmp.key, tmp.val);
            tmp = tmp.next();
        }
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
       SortedMap<Integer, String> res = new MyRbMap();
        var tmp = head;
        while (tmp.right != null)
            tmp = tmp.right;
        while (tmp != null && tmp.key >= fromKey){
            res.put(tmp.key, tmp.val);
            tmp = tmp.prev();
        }
        return res;
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
