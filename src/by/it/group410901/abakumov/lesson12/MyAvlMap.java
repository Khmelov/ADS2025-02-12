package by.it.group410901.abakumov.lesson12;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

// Реализация АВЛ-дерева — дерева, которое само себя балансирует
// Всегда остаётся почти ровным, чтобы поиск был быстрым
public class MyAvlMap implements Map<Integer, String> {

    // Узел дерева — хранит ключ, значение и связи
    private static final class Node {
        Integer key;       // ключ (для поиска)
        String value;      // значение по ключу
        Node left;         // левый ребёнок (ключи меньше)
        Node right;        // правый ребёнок (ключи больше)
        int height;        // высота поддерева с этим узлом

        // Создаём узел
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;  // корень дерева
    private int size = 0; // сколько элементов в карте

    // Возвращает высоту узла (0, если узла нет)
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    // Пересчитывает высоту узла
    private void updateHeight(Node n) {
        // Высота = 1 + максимум из высот левого и правого ребёнка
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    // Разница высот левого и правого поддерева
    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Поворот вправо (чтобы сбалансировать)
    private Node rotateRight(Node y) {
        Node x = y.left;           // x — левый ребёнок y
        Node T2 = x.right;         // T2 — правое поддерево x

        x.right = y;               // y становится правым ребёнком x
        y.left = T2;               // T2 становится левым ребёнком y

        updateHeight(y);           // обновляем высоту y
        updateHeight(x);           // обновляем высоту x

        return x;                  // x — новый корень поддерева
    }

    // Поворот влево (зеркально)
    private Node rotateLeft(Node x) {
        Node y = x.right;          // y — правый ребёнок x
        Node T2 = y.left;          // T2 — левое поддерево y

        y.left = x;                // x становится левым ребёнком y
        x.right = T2;              // T2 становится правым ребёнком x

        updateHeight(x);           // обновляем высоту x
        updateHeight(y);           // обновляем высоту y

        return y;                  // y — новый корень поддерева
    }

    // Балансируем узел после вставки или удаления
    private Node balance(Node node) {
        updateHeight(node);        // сначала обновляем высоту
        int bf = balanceFactor(node); // считаем разницу высот

        // Левое поддерево слишком высокое
        if (bf > 1) {
            // Если левое поддерево "перекошено" вправо
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // сначала поворот влево
            }
            return rotateRight(node);  // потом поворот вправо
        }

        // Правое поддерево слишком высокое
        if (bf < -1) {
            // Если правое поддерево "перекошено" влево
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right); // сначала вправо
            }
            return rotateLeft(node);   // потом влево
        }

        return node;               // дерево уже сбалансировано
    }

    // Добавляем пару (ключ, значение)
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("ключ не может быть null");

        Holder prev = new Holder();        // хранит старое значение
        root = insert(root, key, value, prev); // вставляем

        if (prev.found == null) size++;    // если ключ был новый — увеличиваем размер
        return prev.found;                 // возвращаем старое значение
    }

    // Класс для передачи старого значения
    private static final class Holder {
        String found = null;               // сюда запишем старое значение
    }

    // Рекурсивная вставка
    private Node insert(Node node, Integer key, String value, Holder prev) {
        if (node == null) {
            return new Node(key, value);   // создаём новый узел
        }

        int cmp = key.compareTo(node.key); // сравниваем ключи

        if (cmp == 0) {                    // ключ уже есть
            prev.found = node.value;       // запоминаем старое значение
            node.value = value;            // обновляем
            return node;
        } else if (cmp < 0) {              // ключ меньше
            node.left = insert(node.left, key, value, prev); // идём влево
        } else {                           // ключ больше
            node.right = insert(node.right, key, value, prev); // идём вправо
        }

        return balance(node);              // балансируем и возвращаем узел
    }

    // Удаляем по ключу
    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null; // не тот тип
        Integer key = (Integer) keyObj;

        Holder prev = new Holder();
        root = remove(root, key, prev);    // удаляем

        if (prev.found != null) size--;    // если что-то удалили — уменьшаем размер
        return prev.found;                 // возвращаем старое значение
    }

    // Рекурсивное удаление
    private Node remove(Node node, Integer key, Holder prev) {
        if (node == null) return null;     // не нашли

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {                     // ключ меньше
            node.left = remove(node.left, key, prev); // ищем слева
        } else if (cmp > 0) {              // ключ больше
            node.right = remove(node.right, key, prev); // ищем справа
        } else {                           // нашли!
            prev.found = node.value;       // сохраняем значение

            // Один или ноль детей
            if (node.left == null || node.right == null) {
                Node tmp = (node.left != null) ? node.left : node.right;
                return tmp;                // заменяем узел его ребёнком
            } else {
                // Два ребёнка — ищем минимальный в правом поддереве
                Node min = minNode(node.right);
                node.key = min.key;        // копируем ключ
                node.value = min.value;    // копируем значение
                node.right = remove(node.right, min.key, new Holder()); // удаляем минимум
            }
        }

        if (node != null) node = balance(node); // балансируем
        return node;
    }

    // Находим узел с минимальным ключом
    private Node minNode(Node n) {
        Node cur = n;
        while (cur.left != null) cur = cur.left; // идём влево до конца
        return cur;
    }

    // Получаем значение по ключу
    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;

        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.value;     // нашли
            cur = cmp < 0 ? cur.left : cur.right; // идём в нужную сторону
        }
        return null;                            // не нашли
    }

    // Есть ли ключ
    @Override
    public boolean containsKey(Object keyObj) {
        return get(keyObj) != null;             // просто проверяем get
    }

    // Сколько элементов
    @Override
    public int size() {
        return size;
    }

    // Очистить всё
    @Override
    public void clear() {
        root = null;                            // убираем корень
        size = 0;                               // обнуляем счётчик
    }

    // Пусто ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Строковое представление: {1=один, 2=два}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        InOrderPrinter p = new InOrderPrinter(sb); // печатаем в порядке ключей
        p.print(root);
        sb.append('}');
        return sb.toString();
    }

    // Печать в порядке возрастания ключей
    private static final class InOrderPrinter {
        private final StringBuilder sb;
        private boolean first = true;           // первый элемент без запятой

        InOrderPrinter(StringBuilder sb) { this.sb = sb; }

        void print(Node n) {
            if (n == null) return;
            print(n.left);                      // левое поддерево
            if (!first) sb.append(", ");        // запятая между элементами
            first = false;
            sb.append(n.key).append("=").append(n.value); // ключ=значение
            print(n.right);                     // правое поддерево
        }
    }

    // Есть ли значение
    @Override
    public boolean containsValue(Object value) {
        return containsValueIn(root, value);    // ищем в дереве
    }

    // Рекурсивный поиск значения
    private boolean containsValueIn(Node n, Object value) {
        if (n == null) return false;            // дошли до конца
        if (value == null) {
            if (n.value == null) return true;   // ищем null
        } else if (value.equals(n.value)) {
            return true;                        // нашли
        }
        return containsValueIn(n.left, value) || // ищем слева
                containsValueIn(n.right, value);  // или справа
    }

    // Не реализованы (не нужны по заданию)
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll не поддерживается");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet не поддерживается");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet не поддерживается");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values не поддерживается");
    }
}