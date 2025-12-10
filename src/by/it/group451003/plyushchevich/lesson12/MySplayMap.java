package by.it.group451003.plyushchevich.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

/**
 * MySplayMap
 * ----------
 * Реализация NavigableMap<Integer, String> на основе splay-дерева.
 *
 * Упрощения и ограничения:
 * - Не используются вспомогательные структуры стандартной библиотеки для хранения узлов;
 * - Реализованы обязательные методы задания уровня C (см. комментарии ниже);
 * - Остальные методы интерфейса NavigableMap/Map оставлены как UnsupportedOperationException.
 *
 * Ключевая идея splay-дерева:
 * - Каждый доступ (поиск, вставка, удаление) завершается операцией "splay" — поднятием
 *   последнего посещённого узла к корню через серию локальных вращений (zig/zig-zig/zig-zag).
 * - Благодаря этому часто запрашиваемые элементы оказываются ближе к корню, и амортизированная
 *   стоимость операций становится O(log n).
 *
 * Инварианты структуры:
 * - Дерево остаётся бинарным деревом поиска (BST): для любого узла все ключи в левом поддереве
 *   меньше его ключа, а в правом — больше.
 * - Splay-операция не нарушает BST-порядок; она только меняет структуру повёрнутыми узлами.
 *
 * Формат вывода toString(): как у стандартной Map — {k1=v1, k2=v2}
 */
public class MySplayMap implements NavigableMap<Integer, String> {

    /**
     * Внутренний узел splay-дерева.
     */
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root; // корень splay-дерева
    private int size;  // количество пар ключ-значение

    public MySplayMap() {
        root = null;
        size = 0;
    }


    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { root = null; size = 0; }

    /**
     * findNode(key)
     * - Если найден — возвращает этот узел и НЕ выполняет splay.
     * - Если не найден — возвращает последний посещённый узел (null, если дерево пустое).
     */
    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur; // найден
            else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return last;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // Zig: x — ребёнок корня
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                // Zig-zig (лево-лево)
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                // Zig-zig (право-право)
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                // Zig-zag (лево-право)
                rotateLeft(p);
                rotateRight(g);
            } else {
                // Zig-zag (право-лево)
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x; // после подъёма x станет корнем
    }


    private void rotateLeft(Node x) {
        // Левый поворот вокруг x: y = x.right; x.right = y.left; y.left = x; исправляем parent-ссылки
        Node y = x.right;
        if (y == null) return; // безопасная защита
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        // Правый поворот вокруг x: y = x.left; x.left = y.right; y.right = x; исправляем parent-ссылки
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    // ------------------ get / containsKey / containsValue ------------------

    @Override
    public boolean containsKey(Object key) {
        if (key == null) return false;
        Node found = findNode((Integer) key);
        if (found == null) return false;
        if (found.key.equals((Integer) key)) {
            splay(found);
            return true;
        } else {
            // если нет точного совпадения — сглаживаем последний посещённый узел (усиление локальности)
            splay(found);
            return false;
        }
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node found = findNode((Integer) key);
        if (found == null) return null;
        if (found.key.equals((Integer) key)) {
            // если нашли, splay и вернём значение
            splay(found);
            return found.value;
        } else {
            // splay последнего посещённого узла — полезно для будущих запросов
            splay(found);
            return null;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }


    /**
     * put: вставка нового ключа или обновление существующего.
     * Алгоритм:
     * - Если дерево пустое — создаём корень (без splay: он уже корень).
     * - Иначе ищем позицию, вставляем как лист, затем splay вставленного узла.
     * - Возвращаем старое значение, если ключ уже существовал.
     */
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("MySplayMap does not support null keys");
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                // ключ уже есть — обновляем значение и splay существующий узел
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        // вставка как лист
        Node node = new Node(key, value, parent);
        if (cmp < 0) parent.left = node; else parent.right = node;
        size++;
        splay(node); // поднимаем вставленный узел к корню
        return null;
    }

    // ------------------ remove ------------------

    /**
     * remove: удаление ключа.
     * Алгоритм splay-дерева для удаления:
     * 1) Найти узел z. Если отсутствует — вернуть null.
     * 2) splay(z) — поднимаем его к корню.
     * 3) После этого левое поддерево root.left и правое root.right не имеют взаимных связей.
     *    Можно присоединить: сделать root = join(root.left, root.right) — где join удаляет корень и
     *    соединяет два дерева, сохраняя BST-структуру.
     */
    @Override
    public String remove(Object key) {
        if (key == null) return null;
        Node found = findNode((Integer) key);
        if (found == null || !found.key.equals((Integer) key)) {
            if (found != null) splay(found); // сглаживаем последний доступ
            return null; // нет такого ключа
        }
        splay(found); // теперь found == root
        String old = root.value;
        // Разъединяем левое и правое поддеревья
        Node left = root.left;
        Node right = root.right;
        if (left != null) left.parent = null;
        if (right != null) right.parent = null;
        // Удалим корень и соединим left и right
        root = join(left, right);
        size--;
        return old;
    }

    /**
     * join(a,b): объединяет два BST-дерева a и b, где все ключи в a < все ключи в b.
     * Мы используем стратегию: если a == null -> return b; иначе найти максимум в a,
     * splay(max) чтобы он стал корнем a, затем установить a.right = b.
     */
    private Node join(Node a, Node b) {
        if (a == null) return b;
        // найти максимум в a
        Node max = a;
        while (max.right != null) max = max.right;
        splay(max); // поднимаем максимум к корню a
        // теперь max.right == null
        max.right = b;
        if (b != null) b.parent = max;
        return max;
    }

    // ------------------ toString (inorder) ------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        InorderBuilder ib = new InorderBuilder();
        inorder(root, ib);
        sb.append(ib.builder.toString());
        sb.append("}");
        return sb.toString();
    }

    private static class InorderBuilder { StringBuilder builder = new StringBuilder(); boolean first = true; }
    private void inorder(Node node, InorderBuilder ib) {
        if (node == null) return;
        inorder(node.left, ib);
        if (!ib.first) ib.builder.append(", ");
        ib.builder.append(node.key).append("=").append(node.value);
        ib.first = false;
        inorder(node.right, ib);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap res = new MySplayMap();
        headTailCollect(root, res, null, toKey);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap res = new MySplayMap();
        headTailCollect(root, res, fromKey, null);
        return res;
    }

    private void headTailCollect(Node node, MySplayMap res, Integer fromKey, Integer toKey) {
        if (node == null) return;
        if (fromKey == null || node.key.compareTo(fromKey) >= 0) headTailCollect(node.left, res, fromKey, toKey);
        if ((fromKey == null || node.key.compareTo(fromKey) >= 0) && (toKey == null || node.key.compareTo(toKey) < 0)) {
            res.put(node.key, node.value);
        }
        if (toKey == null || node.key.compareTo(toKey) < 0) headTailCollect(node.right, res, fromKey, toKey);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        // splay(cur); // можно сгладить для улучшения локальности
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        // splay(cur);
        return cur.key;
    }
    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root;
        Node candidate = null;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp <= 0) cur = cur.left; // current.key >= key -> идём влево
            else { candidate = cur; cur = cur.right; }
        }
        if (last != null) splay(last);
        return (candidate == null) ? null : candidate.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root; Node candidate = null; Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else { candidate = cur; if (cmp == 0) break; cur = cur.right; }
        }
        if (last != null) splay(last);
        return (candidate == null) ? null : candidate.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root; Node candidate = null; Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp > 0) cur = cur.right;
            else { candidate = cur; if (cmp == 0) break; cur = cur.left; }
        }
        if (last != null) splay(last);
        return (candidate == null) ? null : candidate.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root; Node candidate = null; Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp >= 0) cur = cur.right;
            else { candidate = cur; cur = cur.left; }
        }
        if (last != null) splay(last);
        return (candidate == null) ? null : candidate.key;
    }

     /**
     * SortedMap.comparator() — возвращает Comparator для ключей или null, если используется естественный порядок.
     * Очень важно реализовать этот метод, иначе класс не удовлетворяет контракту SortedMap.
     */
    @Override
    public java.util.Comparator<? super Integer> comparator() {
        // Мы используем natural ordering у Integer — поэтому возвращаем null.
        return null;
    }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }

    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }

    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
