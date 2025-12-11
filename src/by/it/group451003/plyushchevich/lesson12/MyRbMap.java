package by.it.group451003.plyushchevich.lesson12;

import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;


public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * Важные инварианты R-B дерева:
     * 1) К каждому листу (null) от любой вершины идёт одинаковое число чёрных узлов.
     * 2) Корень всегда чёрный.
     * 3) Красный узел не имеет красного родителя (т.е. нет двух красных подряд) ИЛИ у красного дети черные.
     * 4) Черная высота у корня постоянна
     * Поле parent помогает при операциях вставки/удаления, чтобы найти родителя
     * при поворотах и при движении вверх по дереву.
     */
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }


    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) return false;
        return get((Integer) key) != null;
    }


    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node cur = root;
        Integer k = (Integer) key;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            if (cmp == 0) return cur.value;
            else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return null;
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("MyRbMap does not support null keys");

        Node y = null; // будет родителем для нового узла
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                return old;
            } else if (cmp < 0) x = x.left;
            else x = x.right;
        }

        // по стандартной стратегии вставка нового узла — красная
        Node z = new Node(key, value, RED, y);  // красный реже ломает КЧ - дерево
        if (y == null)
            root = z;
        else if (key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;
        size++;

        // Восстанавливаем свойства красно-черного дерева
        insertFixup(z);
        return null; //  тк мы создали новый ключ
    }

    /**
     * insertFixup: восстановление свойств R-B-дерева после вставки красного узла z.
     *
     * Идея метода (стандартный алгоритм CLRS):
     * Пока у z родитель красный (нарушение свойства: красный не может иметь красного родителя),
     * рассматриваем 2 симметричных случая: parent — левый ребёнок grandparent, или parent — правый.
     * В каждом случае анализируем цвет «дяди» (uncle).
     *
     * Случаи:
     * - Если дядя красный: перекрашиваем parent и uncle в чёрный, grandparent в красный
     *   и поднимаемся вверх (z = grandparent).
     * - Если дядя чёрный и z — «внутренний» ребёнок (например, правый ребёнок у левогo parent),
     *   сначала делаем поворот вокруг parent (внутренний поворот), затем — внешний поворот
     *   и перекраску (чтобы восстановить свойства).
     *
     * В конце обязательно красим корень в чёрный.
     */
    private void insertFixup(Node z) {
        // Пока есть родитель и родитель красный — продолжаем фиксап
        while (z.parent != null && z.parent.color == RED) {
            // Проверяем, является ли parent левым ребёнком grandparent
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // uncle — правый ребёнок grandparent
                if (colorOf(y) == RED) {
                    // CASE 1: uncle красный — перекрашиваем и двигаемся вверх
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent; // поднимаемся на уровень выше
                } else {
                    // uncle чёрный
                    if (z == z.parent.right) {
                        // CASE 2: z — правый ребёнок (внутренний)
                        z = z.parent; // приводим к внешнему случаю
                        rotateLeft(z); // левый поворот вокруг parent
                    }
                    // CASE 3: z — левый ребёнок (внешний)
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent); // правый поворот вокруг grandparent
                }
            } else {
                // симметричный случай: parent — правый ребёнок grandparent
                Node y = z.parent.parent.left; // uncle — левый ребёнок grandparent
                if (colorOf(y) == RED) {
                    // CASE 1 смещённый
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        // CASE 2: внутренний случай по симметрии
                        z = z.parent;
                        rotateRight(z);
                    }
                    // CASE 3 симметричный
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        // Вне цикла — корень гарантированно должен быть чёрным
        root.color = BLACK;
    }


    /**
     * rotateLeft(x): левый поворот вокруг узла x.
     *
     * Действия:
     * - Пусть y = x.right
     * - x.right = y.left
     * - если y.left != null -> y.left.parent = x
     * - y.parent = x.parent
     * - если x.parent == null -> root = y, иначе заменяем ссылку у родителя
     * - y.left = x; x.parent = y
     *
     * Повороты меняют структуру, но сохраняют свойства BST (in-order порядок ключей).
     */
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;                    // x был корнем
        else if (x == x.parent.left) x.parent.left = y;    // x был левым ребёнком
        else x.parent.right = y;                           // x был правым ребёнком
        y.left = x;
        x.parent = y;
    }

    /**
     * rotateRight(y): правый поворот вокруг узла y.
     * Симметрично rotateLeft.
     */
    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }


    /**
     * remove(key): удаление узла с данным ключом.
     * Возвращает старое значение или null если ключ отсутствует.
     *
     * Алгоритм:
     * 1) Находим узел z с ключом key.
     * 2) Если z отсутствует — возвращаем null.
     * 3) Выполняется стандартная операция BST-удаления с транплантацией и поиском преемника
     *    (если у узла два ребёнка).
     * 4) Если цвет удаляемого (или заменённого) узла был чёрным — вызываем deleteFixup,
     *    чтобы восстановить свойства R-B-дерева (корректировка чёрных высот и цветов).
     */
    @Override
    public String remove(Object key) {
        if (key == null) return null;
        Node z = root;
        Integer k = (Integer) key;
        // Поиск узла с ключом k
        while (z != null) {
            int cmp = k.compareTo(z.key);
            if (cmp == 0) break;
            else if (cmp < 0) z = z.left;
            else z = z.right;
        }
        if (z == null) return null;
        String old = z.value;
        deleteNode(z);
        size--;
        return old;
    }

    /**
     * deleteNode(z): удаление узла z по CLRS-алгоритму.
     *
     * Переменные:
     * - y — узел, который фактически удаляется из дерева (или заменяется). Изначально y = z.
     * - x — сын y, который переместится на место y (возможно null).
     * - yOriginalColor — исходный цвет y; если он чёрный — требуется fixup после удаления.
     */
    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x; // узел, который займёт место y
        Node xParent = null; // хранит родителя x, если x == null — нужен для fixup

        if (z.left == null) {
            // У z нет левого ребёнка — заменяем z на правого ребёнка
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            // У z нет правого ребёнка — заменяем z на левого
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            // У z оба ребёнка — найдём successor (минимум в правом поддереве)
            y = treeMinimum(z.right);
            yOriginalColor = y.color;
            x = y.right; // x — узел, который займёт место y в дереве
            if (y.parent == z) {
                // Если successor — непосредственный правый ребёнок z
                xParent = y; // при x == null parent должен быть y
                if (x != null) x.parent = y;
            } else {
                // Иначе переносим y на место своего правого ребёнка
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }
            // Заменяем z на y
            transplant(z, y);
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            // Сохраняем цвет z в y (чтобы структура цвета соответствовала до удаления)
            y.color = z.color;
        }
        // Если удалённый (или перемещённый) узел был чёрным — требуется восстановление
        if (yOriginalColor == BLACK) {
            deleteFixup(x, xParent);
        }
    }

    /**
     * transplant(u,v): заменяет поддерево с корнем u на поддерево с корнем v.
     */
    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    /**
     * treeMinimum(node): находит минимальный узел (по ключу) в поддереве node.
     * Используется для поиска successor при удалении.
     */
    private Node treeMinimum(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    /**
     * deleteFixup(x, parentIfXNull): восстановление специфических свойств R-B-дерева
     * после удаления. Очень аккуратный и технически нагруженный алгоритм.
     *
     * Пояснения аргументов:
     * - x — узел, который занял позицию удалённого узла (может быть null).
     * - parentIfXNull — родитель x в случае, когда x == null; нужен, чтобы корректно
     *   подниматься вверх по дереву, если x == null (нет sentinel nil в нашей реализации).
     *
     * Смысл функции: если удалённое место по чёрной высоте нарушило баланс (удалён чёрный узел),
     * то пока x не будет «восстановлен» (или пока не дойдём до корня) — применяем набор
     * операций, основанных на цвете брата и его детей, выполняя повороты/перекраски.
     *
     * Алгоритм следует классическому CLRS с учётом отсутствия sentinel-nil: везде надо
     * проверять на null и считать null как чёрную «листовую» вершину.
     */
    private void deleteFixup(Node x, Node parentIfXNull) {
        Node current = x;
        Node parent = (x != null) ? x.parent : parentIfXNull;

        // Пока current не корень и current чёрный — нужно исправлять дефект
        while ((current != root) && colorOf(current) == BLACK) {
            if (parent == null) break; // safety: если нет родителя — выходим

            if (current == parent.left) {
                // current — левый ребёнок
                Node w = parent.right; // брат

                if (colorOf(w) == RED) {
                    // CASE 1: брат красный — перекрашиваем и поворачиваем
                    // После rotateLeft(parent) брат станет чёрным и новые отношения
                    // позволят перейти к другим случаям.
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right; // обновлённый брат
                }

                // CASE 2: оба ребёнка брата чёрные -> перекрасить брата и подняться вверх
                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    if (w != null) w.color = RED;
                    current = parent;
                    parent = current.parent;
                } else {
                    // CASE 3/4: у брата есть хотя бы один красный ребёнок
                    if (colorOf(w.right) == BLACK) {
                        // Если правый ребёнок брата чёрный, но левый красный — сначала rotateRight(w)
                        if (w.left != null) w.left.color = BLACK;
                        if (w != null) w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    // Теперь правый ребёнок брата гарантированно красный — делаем одну перекраску и rotateLeft(parent)
                    if (w != null) w.color = parent.color;
                    parent.color = BLACK;
                    if (w != null && w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    current = root; // выход из цикла
                    break;
                }
            } else {
                // симметричный случай: current — правый ребёнок
                Node w = parent.left;

                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }

                if (colorOf(w.right) == BLACK && colorOf(w.left) == BLACK) {
                    if (w != null) w.color = RED;
                    current = parent;
                    parent = current.parent;
                } else {
                    if (colorOf(w.left) == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        if (w != null) w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    if (w != null) w.color = parent.color;
                    parent.color = BLACK;
                    if (w != null && w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    current = root;
                    break;
                }
            }
        }
        if (current != null) current.color = BLACK; // убедимся, что текущий узел чёрный
    }

    // Вспомогательная функция — безопасный доступ к цвету узла (null считается чёрным)
    private boolean colorOf(Node n) { return (n == null) ? BLACK : n.color; }

    // --------------------- toString (inorder) ---------------------

    /**
     * toString: возвращает представление карты в порядке возрастания ключей.
     * Формат совпадает со стандартным: {k1=v1, k2=v2, ...}
     * Для этого используется inorder обход дерева — он возвращает элементы в отсортированном порядке.
     */
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

    // Вспомогательный класс для аккуратного формирования строки при рекурсивном обходе
    private static class InorderBuilder { StringBuilder builder = new StringBuilder(); boolean first = true; }

    // Рекурсивный inorder обход: левое -> узел -> правое
    private void inorder(Node node, InorderBuilder ib) {
        if (node == null) return;
        inorder(node.left, ib);
        if (!ib.first) ib.builder.append(", ");
        ib.builder.append(node.key).append("=").append(node.value);
        ib.first = false;
        inorder(node.right, ib);
    }

    // --------------------- headMap / tailMap ---------------------

    /**
     * headMap(toKey): возвращает новый MyRbMap, содержащий все элементы с ключами < toKey.
     * Важно: возвращаемая карта — новая, а не view оригинальной.
     */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        headTailCollect(root, res, null, toKey);
        return res;
    }

    /**
     * tailMap(fromKey): возвращает новый MyRbMap, содержащий все элементы с ключами >= fromKey.
     */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        headTailCollect(root, res, fromKey, null);
        return res;
    }

    /**
     * headTailCollect: обход дерева и сбор элементов в новый MyRbMap в указанном диапазоне.
     * fromKey == null — нижней границы нет. toKey == null — верхней границы нет.
     * Включаем элементы, удовлетворяющие условию: (fromKey == null || key >= fromKey) &&
     * (toKey == null || key < toKey).
     */
    private void headTailCollect(Node node, MyRbMap res, Integer fromKey, Integer toKey) {
        if (node == null) return;
        // Обход inorder, но ранний выход с пропуском несоответствующих ветвей
        if (fromKey == null || node.key.compareTo(fromKey) >= 0) headTailCollect(node.left, res, fromKey, toKey);
        if ((fromKey == null || node.key.compareTo(fromKey) >= 0) && (toKey == null || node.key.compareTo(toKey) < 0)) {
            res.put(node.key, node.value);
        }
        if (toKey == null || node.key.compareTo(toKey) < 0) headTailCollect(node.right, res, fromKey, toKey);
    }

    // --------------------- firstKey / lastKey ---------------------

    /**
     * firstKey: минимальный ключ в карте — самый левый узел.
     */
    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    /**
     * lastKey: максимальный ключ в карте — самый правый узел.
     */
    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    // --------------------- Методы, которые не требовались (заглушки) ---------------------

    @Override public java.util.Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
