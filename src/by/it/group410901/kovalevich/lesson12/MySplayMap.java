package by.it.group410901.kovalevich.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer,String> {

    private static final class Node {
        int k; String v;
        Node l,r,p;
        Node(int k,String v){ this.k=k; this.v=v; }
    }

    private Node root;
    private int size;

    private void rotL(Node x){
        Node y = x.r;
        x.r = y.l; if (y.l!=null) y.l.p = x;
        y.p = x.p;
        if (x.p==null) root = y;
        else if (x==x.p.l) x.p.l = y; else x.p.r = y;
        y.l = x; x.p = y;
    }
    private void rotR(Node x){
        Node y = x.l;
        x.l = y.r; if (y.r!=null) y.r.p = x;
        y.p = x.p;
        if (x.p==null) root = y;
        else if (x==x.p.r) x.p.r = y; else x.p.l = y;
        y.r = x; x.p = y;
    }
    private void splay(Node x){
        if (x==null) return;
        while (x.p!=null){
            Node p=x.p, g=p.p;
            if (g==null){ if (x==p.l) rotR(p); else rotL(p); }
            else if (x==p.l && p==g.l){ rotR(g); rotR(p); }
            else if (x==p.r && p==g.r){ rotL(g); rotL(p); }
            else if (x==p.r && p==g.l){ rotL(p); rotR(g); }
            else { rotR(p); rotL(g); }
        }
    }

    private Node findNode(int k){
        Node cur=root, last=null;
        while (cur!=null){
            last=cur;
            if (k<cur.k) cur=cur.l;
            else if (k>cur.k) cur=cur.r;
            else return cur;
        }
        if (last!=null) splay(last);
        return null;
    }
    private static Node min(Node n){ while(n!=null && n.l!=null) n=n.l; return n; }
    private static Node max(Node n){ while(n!=null && n.r!=null) n=n.r; return n; }

    @Override public String put(Integer key, String value){
        if (key==null) throw new NullPointerException();
        if (root==null){ root=new Node(key,value); size=1; return null; }
        Node cur=root,p=null;
        while (cur!=null){
            p=cur;
            if (key<cur.k) cur=cur.l;
            else if (key>cur.k) cur=cur.r;
            else { String old=cur.v; cur.v=value; splay(cur); return old; }
        }
        Node n=new Node(key,value); n.p=p;
        if (key<p.k) p.l=n; else p.r=n;
        splay(n); size++; return null;
    }

    @Override public String remove(Object key){
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer)key);
        if (n==null) return null;
        splay(n);
        String old = n.v;
        Node L=n.l, R=n.r;
        if (L!=null) L.p=null;
        if (R!=null) R.p=null;
        if (L==null) root=R;
        else{
            Node m = max(L);
            splay(m);
            root.r = R;
            if (R!=null) R.p = root;
        }
        size--;
        return old;
    }

    @Override public String get(Object key){
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer)key);
        if (n!=null) splay(n);
        return n==null?null:n.v;
    }
    @Override public boolean containsKey(Object key){
        if (!(key instanceof Integer)) return false;
        Node n = findNode((Integer)key);
        return n!=null && n.k==(Integer)key;
    }

    @Override public boolean containsValue(Object value){
        return containsValue(root,value);
    }
    private boolean veq(String a, Object b){ return b==null? a==null : (b instanceof String && b.equals(a)); }
    private boolean containsValue(Node n, Object v){
        if (n==null) return false;
        if (veq(n.v,v)) return true;
        return containsValue(n.l,v) || containsValue(n.r,v);
    }

    @Override public int size(){ return size; }
    @Override public void clear(){ root=null; size=0; }
    @Override public boolean isEmpty(){ return size==0; }

    private void inorder(Node n, StringBuilder sb){
        if (n==null) return;
        inorder(n.l,sb);
        if (sb.length()>1) sb.append(", ");
        sb.append(n.k).append('=').append(n.v);
        inorder(n.r,sb);
    }
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inorder(root,sb);
        sb.append('}');
        return sb.toString();
    }

    @Override public Comparator<? super Integer> comparator(){ return null; }
    @Override public Integer firstKey(){ if (root==null) throw new NoSuchElementException(); Node m=min(root); splay(m); return m.k; }
    @Override public Integer lastKey(){ if (root==null) throw new NoSuchElementException(); Node m=max(root); splay(m); return m.k; }

    private Integer lowerFrom(Node n, int k){ Integer res=null; while(n!=null){ if (k<=n.k) n=n.l; else {res=n.k; n=n.r;} } return res; }
    private Integer floorFrom(Node n, int k){ Integer res=null; while(n!=null){ if (k<n.k) n=n.l; else {res=n.k; n=n.r;} } return res; }
    private Integer ceilFrom(Node n, int k){ Integer res=null; while(n!=null){ if (k>n.k) n=n.r; else {res=n.k; n=n.l;} } return res; }
    private Integer higherFrom(Node n, int k){ Integer res=null; while(n!=null){ if (k>=n.k) n=n.r; else {res=n.k; n=n.l;} } return res; }

    @Override public Integer lowerKey(Integer key){ return lowerFrom(root,key); }
    @Override public Integer floorKey(Integer key){ return floorFrom(root,key); }
    @Override public Integer ceilingKey(Integer key){ return ceilFrom(root,key); }
    @Override public Integer higherKey(Integer key){ return higherFrom(root,key); }

    @Override public NavigableMap<Integer,String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive){
        MySplayMap m = new MySplayMap();
        collectRange(root,fromKey,fromInclusive,toKey,toInclusive,m);
        return m;
    }
    private void collectRange(Node n, int from, boolean fi, int to, boolean ti, MySplayMap m){
        if (n==null) return;
        if (n.k>from || (fi && n.k==from)) collectRange(n.l,from,fi,to,ti,m);
        boolean okL = n.k>from || (fi && n.k==from);
        boolean okR = n.k<to   || (ti && n.k==to);
        if (okL && okR) m.put(n.k,n.v);
        if (n.k<to || (ti && n.k==to)) collectRange(n.r,from,fi,to,ti,m);
    }
    @Override public NavigableMap<Integer,String> headMap(Integer toKey, boolean inclusive){ return subMap(Integer.MIN_VALUE,true,toKey,inclusive); }
    @Override public NavigableMap<Integer,String> tailMap(Integer fromKey, boolean inclusive){ return subMap(fromKey,inclusive,Integer.MAX_VALUE,true); }
    @Override public SortedMap<Integer,String> headMap(Integer toKey){ return (SortedMap<Integer,String>) headMap(toKey,false); }
    @Override public SortedMap<Integer,String> tailMap(Integer fromKey){ return (SortedMap<Integer,String>) tailMap(fromKey,true); }
    @Override public NavigableMap<Integer,String> subMap(Integer fromKey, Integer toKey){ return subMap(fromKey,true,toKey,false); }

    @Override public void putAll(Map<? extends Integer, ? extends String> m){
        if (m==null) return;
        for (Entry<? extends Integer, ? extends String> e: m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override public Map.Entry<Integer,String> firstEntry(){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> lastEntry(){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> lowerEntry(Integer key){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> floorEntry(Integer key){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> ceilingEntry(Integer key){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> higherEntry(Integer key){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> pollFirstEntry(){ throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer,String> pollLastEntry(){ throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet(){ throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet(){ throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer,String> descendingMap(){ throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer,String>> entrySet(){ throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet(){ throw new UnsupportedOperationException(); }
    @Override public Collection<String> values(){ throw new UnsupportedOperationException(); }
}
