package by.it.group410902.podryabinkin.lesson12;

import java.util.*;

//Пометка для себя
/* Есть только 3 ситуации для балансировки
Обозначения
S - сын (то что добавили)
P - родитель
G - дед
U - дядя

алгоритмы относительно сына
case 1 P и U красные
Перекрашиваем P и U в чёрный, G в красный, G становится S для следующей итерации

case 2 P красный U чёрный
Делаем левый или правый малый поворот поворот (сын переходи в другой лист)

case 3

 */

public class MyRbMap implements SortedMap<Integer, String> {

    private class Node{
        Integer key = null;
        String value = null;
        Node left = null;
        Node right = null;
        Node parent = null;
        boolean isRed ;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inorderToString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убираем ", " в конце
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inorderToString(node.right, sb);
    }


    private Node root = null;
    private Node tail = null;
    Integer size = 0;

    //алгоритмы балансировки
    private void leftR(Node p){
        Node s = p.right;
        p.right = s.left;
        s.left = p;
        //проверяем на косячность в корне
        if(p.parent == null) root = s;
        //проверяем, каких политических взглядов был родитель
        else if (p == p.parent.left) p.parent.left = s;
        else p.parent.right = s;
        s.parent = p.parent;
        p.parent = s;

    }
    private void rightR(Node p){
        Node s = p.left;
        p.left = s.right;
        s.right = p;
        //проверяем на косячность в корне
        if(p.parent == null) root = s;
            //проверяем, каких политических взглядов был родитель
        else if (p == p.parent.left) p.parent.left = s;
        else p.parent.right = s;
        s.parent = p.parent;
        p.parent = s;
    }
    private void balance(Node s){
        //когда новое дерево
        if (s.parent == null) {
            s.isRed = false; // корень всегда чёрный
            root = s;
            return;
        }
        //родитель чёрный, баланс ОК
        if (!s.parent.isRed) return;
        //отец и дядя
        Node p = s.parent;
        Node g = p.parent;

        if (g == null) {
            // родитель — корень, значит он должен стать чёрным
            //Если пояснить -- балансировка не требуется так как тут общая высота всего 2
            p.isRed = false;
            return;
        }

        s.isRed = true; // новый узел ВСЕГДА красный поначалу



        //Смотрим, кто есть кто
        if(s.parent == null) return;
        while(s != root && s.parent.isRed){

            //Дядя, кто ты? Либерал или коммунист?
            Node u = (p == g.left) ? g.right : g.left;

            if(u != null && u.isRed){ // Case 1
                //Меняем только цвета и идём вверх
                p.isRed = false;
                u.isRed = false;
                g.isRed = true;
                s = g;
            } else {
                if(s == p.right && p == g.left){ // Case 2
                    //Лажа справа в конце по левому
                    leftR(p);
                    s = p;
                    p = s.parent;
                } else if(s == p.left && p == g.right){
                    rightR(p);
                    s = p;
                    p = s.parent;
                }
                // Case 3 (выполняется что с case 2, что без него)
                //Лажа в самом левом по левому
                p.isRed = false;
                g.isRed = true;
                if(s == p.left && p == g.left) rightR(g);
                else if(s == p.right && p == g.right) leftR(g);
                break;
            }
        }
        root.isRed = false;
    }
    public String put(Integer key, String value) {
        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
            else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        Node s = new Node();
        s.key = key;
        s.value = value;
        s.isRed = true;
        s.parent = parent;

        if (parent == null)
            root = s;
        else if (key < parent.key)
            parent.left = s;
        else
            parent.right = s;

        balance(s);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        //ищем такой же ключ
        Node current = root;
        Node parent = null;

        //ищем такой же ключ
        while(current != null) {
            parent = current;
            if ((Integer)key < current.key) current = current.left;
            else if ((Integer)key > current.key) current = current.right;
            else {
                return current.value;
            }
        }
        return null;
    }
    public Node getNode(Object key) {
        //ищем такой же ключ
        Node current = root;
        Node parent = null;

        //ищем такой же ключ
        while(current != null) {
            parent = current;
            if ((Integer)key < current.key) current = current.left;
            else if ((Integer)key > current.key) current = current.right;
            else {
                return current;
            }
        }
        return null;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) v.parent = u.parent;
    }
    //минимальный узел
    private Node minimum(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    // его надо много, так что напишем
    private boolean isRed(Node n) {
        return n != null && n.isRed;
    }

    // Универсальный deleteFixup (работает корректно, если x может быть null).
    private void deleteFixup(Node del) {
        // Перебираем все 4 случая балансировки..... убейте меня......
        while (del != root && !isRed(del))
        {
            Node parent = (del == null) ? null : del.parent;
            if (parent == null) break; // если x == null и parent == null — корень, выходим
            if (del == parent.left)
            {
                Node brother = parent.right; // брат
                // Case 1: брат красный
                if (isRed(brother))
                {
                    brother.isRed = false;
                    parent.isRed = true;
                    leftR(parent);
                    brother = parent.right;
                }
                // Case 2: брат чёрный(или отсутствует) и дети тоже
                if ((brother == null) ||
                        (!isRed(brother.left) && !isRed(brother.right)))
                {
                    if (brother != null) brother.isRed = true;
                    del = parent;
                }
                else
                {
                    // Case 3: брат чёрный, левый сын красный, правый сын чёрный упаси его...
                    if (!isRed(brother.right))
                    {
                        if (brother.left != null) brother.left.isRed = false;
                        brother.isRed = true;
                        rightR(brother);
                        brother = parent.right;
                    }
                    // Case 4: какой-то ещё случай
                    if (brother != null) brother.isRed = parent.isRed;
                    parent.isRed = false;
                    if (brother != null && brother.right != null) brother.right.isRed = false;
                    leftR(parent);
                    del = root;
                }
            }
            //симметрично всё перписываем под левака
            else {
                Node brother = parent.left;
                // Case 1: брат красный
                if (isRed(brother))
                {
                    brother.isRed = false;
                    parent.isRed = true;
                    rightR(parent);
                    brother = parent.left;
                }
                // Case 2: брат чёрный(или отсутствует) и дети тоже
                if ((brother == null) || (!isRed(brother.left) && !isRed(brother.right)))
                {
                    if (brother != null) brother.isRed = true;
                    del = parent;
                } else
                {
                    // Case 3: брат чёрный, левый сын красный, правый сын чёрный упаси его...
                    if (!isRed(brother.left))
                    {
                        if (brother.right != null) brother.right.isRed = false;
                        brother.isRed = true;
                        leftR(brother);
                        brother = parent.left;
                    }
                    // Case 4: какой-то ещё случай
                    if (brother != null) brother.isRed = parent.isRed;
                    parent.isRed = false;
                    if (brother != null && brother.left != null) brother.left.isRed = false;
                    rightR(parent);
                    del = root;
                }
            }
        }
        if (del != null) del.isRed = false;
    }

    @Override
    public String remove(Object key1) {
        Integer key = (Integer) key1;
        Node z = getNode(key);
        if (z == null) return null;

        String oldVal = z.value;
        Node y = z;
        boolean yOriginalIsRed = y.isRed;
        Node x;

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right); // successor
            yOriginalIsRed = y.isRed;
            x = y.right; // может быть null

            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            y.isRed = z.isRed;
        }

        // уменьшение размера — только если удаление успешно
        size--;

        if (!yOriginalIsRed) {
            // x может быть null — deleteFixup корректно обработает это
            deleteFixup(x);
        }
        if (root != null) root.isRed = false;
        return oldVal;
    }
    @Override
    public boolean containsKey(Object key) {
        //ищем такой же ключ
        Node current = root;
        Node parent = null;

        //ищем такой же ключ
        while(current != null) {
            parent = current;
            if ((Integer)key < current.key) current = current.left;
            else if ((Integer)key > current.key) current = current.right;
            else {
                return true;
            }
        }
        return false;
    }
    //Самый тупой рекурсивный поиск
    private boolean prim_search(Node cur, String val){
        if(cur.value == val) return true;
        boolean r = false, l = false;
        if(cur.right != null) r = prim_search(cur.right, val);
        if(cur.left != null)  l = prim_search(cur.left, val);
        return r || l;
    }
    @Override
    public boolean containsValue(Object value) {
        String val = value.toString();
        return prim_search(root, val);
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
    public void clear() {
        size = 0;
        root = null;
        tail = null;
    }



    @Override
    public Integer firstKey() {
        Node cur = root;
        if(cur == null) return null;
        while(cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        Node cur = root;
        if(cur == null) return null;
        while(cur.right != null) cur = cur.right;
        return cur.key;
    }
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        addHead(root, toKey, result);
        return result;
    }

    private void addHead(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key < toKey) {
            addHead(node.left, toKey, result);
            result.put(node.key, node.value);
            addHead(node.right, toKey, result);
        } else {
            addHead(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        addTail(root, fromKey, result);
        return result;
    }

    private void addTail(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            addTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            addTail(node.right, fromKey, result);
        } else {
            addTail(node.right, fromKey, result);
        }
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
