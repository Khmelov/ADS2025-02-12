package by.it.group410901.tomashevich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;  // обозначение красного цвета узла
    private static final boolean BLACK = false; // обозначение чёрного цвета узла

    // Класс, описывающий узел красно-чёрного дерева
    private class Node {
        Integer key;     // ключ
        String info;     // значение
        Node left, right; // ссылки на левое и правое поддеревья
        boolean color;   // цвет узла

        Node(Integer key, String info, boolean color) {
            this.key = key;      // сохраняем ключ
            this.info = info;    // сохраняем значение
            this.color = color;  // сохраняем цвет
        }
    }

    private Node root; // корень дерева
    private int size;  // количество элементов в дереве

    public MyRbMap() {
        size = 0; // инициализация пустого дерева
    }

    @Override
    // Возвращает строковое представление дерева в формате {ключ=значение, ...}.
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{"); // начинаем построение строки
        inorderTraversal(root, resultStr); // рекурсивно обходим дерево в порядке возрастания
        if (resultStr.length() > 1) { // если есть элементы
            resultStr.setLength(resultStr.length() - 2); // убираем последнюю запятую и пробел
        }
        resultStr.append("}"); // закрываем фигурной скобкой
        return resultStr.toString(); // возвращаем строку
    }

    // Рекурсивно выполняет симметричный обход дерева для построения строкового представления.
    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node != null) { // если узел не пустой
            inorderTraversal(node.left, sb); // обходим левое поддерево
            sb.append(node.key).append("=").append(node.info).append(", "); // добавляем пару "ключ=значение"
            inorderTraversal(node.right, sb); // обходим правое поддерево
        }
    }

    @Override
    // Добавляет или обновляет пару ключ-значение в дереве, возвращая старое значение.
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null"); // запрещаем null-ключи
        String oldInfo = get(key); // запоминаем старое значение по ключу (если есть)
        root = put(root, key, value); // вставляем или обновляем узел
        root.color = BLACK; // корень всегда должен быть чёрным
        return oldInfo; // возвращаем старое значение
    }
    // Рекурсивно вставляет узел с ключом и значением, выполняя балансировку LLRB.
    private Node put(Node node, Integer key, String info) {
        if (node == null) { // если достигли места вставки
            size++; // увеличиваем размер карты
            return new Node(key, info, RED); // создаём новый красный узел
        }

        int res = key.compareTo(node.key); // сравниваем ключи
        if (res < 0) { // если ключ меньше
            node.left = put(node.left, key, info); // идём влево
        } else if (res > 0) { // если больше
            node.right = put(node.right, key, info); // идём вправо
        } else { // ключ совпадает
            node.info = info; // обновляем значение
        }

        // Выполняем стандартные преобразования LLRB
        if (isRed(node.right) && !isRed(node.left))
            node = rotateLeft(node); // если правый красный, а левый чёрный — поворот влево
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node); // если две левые красные подряд — поворот вправо
        if (isRed(node.left) && isRed(node.right)) flipColors(node); // если оба ребёнка красные — меняем цвета

        return node; // возвращаем сбалансированный узел
    }
    // Добавляет все пары ключ-значение из переданной карты в текущее дерево.
    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        if (map == null) throw new NullPointerException("Map cannot be null"); // проверяем аргумент
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) { // для каждой пары из переданной карты
            put(entry.getKey(), entry.getValue()); // добавляем в текущее дерево
        }
    }
    // Удаляет узел с указанным ключом, возвращая его значение, и балансирует дерево.
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Key cannot be null"); // запрещаем null-ключи
        if (!(key instanceof Integer)) return null; // если тип не Integer, просто выходим
        Integer k = (Integer) key; // приведение типа
        if (!containsKey(k)) return null; // если ключа нет, ничего не делаем
        String oldValue = get(k); // запоминаем удаляемое значение
        root = remove(root, k); // рекурсивно удаляем узел
        if (root != null) root.color = BLACK; // корень должен остаться чёрным
        return oldValue; // возвращаем значение
    }
    // Рекурсивно удаляет узел с указанным ключом, выполняя балансировку дерева.
    private Node remove(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) { // если ключ меньше
            if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node); // готовим левую ветвь к удалению
            node.left = remove(node.left, key); // идём влево
        } else {
            if (isRed(node.left)) node = rotateRight(node); // если левый красный — поворот вправо
            if (key.compareTo(node.key) == 0 && (node.right == null)) { // найден узел без правого поддерева
                size--; // уменьшаем размер
                return null; // удаляем узел
            }
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node); // подготавливаем правую сторону
            if (key.compareTo(node.key) == 0) { // найден узел для удаления
                Node min = min(node.right); // ищем минимум справа
                node.key = min.key; // заменяем ключ
                node.info = min.info; // заменяем значение
                node.right = deleteMin(node.right); // удаляем дубль
                size--; // уменьшаем размер
            } else {
                node.right = remove(node.right, key); // продолжаем удаление справа
            }
        }
        return balance(node); // балансируем дерево
    }
    // Возвращает значение, связанное с указанным ключом, или null, если ключа нет.
    @Override
    public String get(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null"); // запрещаем null-ключи
        if (!(obj instanceof Integer)) return null; // если не Integer — возвращаем null
        Integer k = (Integer) obj; // приводим тип
        Node node = root; // начинаем с корня
        while (node != null) { // пока не дошли до конца
            int cmp = k.compareTo(node.key); // сравниваем ключи
            if (cmp < 0) node = node.left; // идём влево
            else if (cmp > 0) node = node.right; // идём вправо
            else return node.info; // нашли — возвращаем значение
        }
        return null; // не нашли
    }
    // Проверяет, содержит ли дерево указанный ключ.
    @Override
    public boolean containsKey(Object obj) {
        if (obj == null) throw new NullPointerException("Key cannot be null"); // null-ключи не допускаются
        if (!(obj instanceof Integer)) return false; // если тип не Integer — возвращаем false
        Integer k = (Integer) obj; // приводим тип
        return get(k) != null; // проверяем, есть ли значение
    }
    // Проверяет, содержит ли дерево указанное значение.
    @Override
    public boolean containsValue(Object obj) {
        if (obj == null) throw new NullPointerException("Value cannot be null"); // null-значения не допускаются
        return containsValue(root, obj.toString()); // рекурсивный поиск значения
    }
    // Рекурсивно ищет значение в дереве, сравнивая его с info узлов.
    private boolean containsValue(Node node, String info) {
        if (node == null) return false; // если дошли до конца
        if (info.equals(node.info)) return true; // если нашли совпадение
        // продолжаем поиск в поддеревьях
        return containsValue(node.left, info) || containsValue(node.right, info);
    }
    // Возвращает количество узлов в дереве.
    @Override
    public int size() {
        return size; // возвращаем количество элементов
    }
    // Очищает дерево, обнуляя корень и размер.
    @Override
    public void clear() {
        root = null; // обнуляем корень
        size = 0; // сбрасываем размер
    }
    // Проверяет, пустое ли дерево.
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяем, пусто ли дерево
    }
    // Возвращает подкарту с ключами, меньшими указанного ключа.
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap(); // создаём новую карту
        headMapHelper(root, toKey, headMap); // добавляем элементы меньше toKey
        return headMap; // возвращаем результат
    }
    // Рекурсивно собирает элементы с ключами, меньшими toKey, в новую карту.
    private void headMapHelper(Node node, Integer toKey, MyRbMap headMap) {
        if (node == null) return; // конец рекурсии
        if (node.key.compareTo(toKey) < 0) { // если ключ меньше toKey
            headMap.put(node.key, node.info); // добавляем элемент
            headMapHelper(node.right, toKey, headMap); // обходим правое поддерево
        }
        headMapHelper(node.left, toKey, headMap); // обходим левое поддерево
    }
    // Возвращает подкарту с ключами, большими или равными указанного ключа.
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap(); // создаём новую карту
        tailMapHelper(root, fromKey, tailMap); // добавляем элементы >= fromKey
        return tailMap; // возвращаем результат
    }
    // Рекурсивно собирает элементы с ключами >= fromKey в новую карту.
    private void tailMapHelper(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) return; // конец рекурсии
        if (node.key.compareTo(fromKey) >= 0) { // если ключ >= fromKey
            tailMap.put(node.key, node.info); // добавляем элемент
            tailMapHelper(node.left, fromKey, tailMap); // обходим левое поддерево
            tailMapHelper(node.right, fromKey, tailMap); // обходим правое поддерево
        } else {
            tailMapHelper(node.right, fromKey, tailMap); // иначе идём вправо
        }
    }
    // Возвращает наименьший ключ в дереве или выбрасывает исключение, если дерево пустое.
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException(); // если пусто — ошибка
        Node node = root; // начинаем с корня
        while (node.left != null) node = node.left; // идём влево до конца
        return node.key; // возвращаем минимальный ключ
    }
    // Возвращает наибольший ключ в дереве или выбрасывает исключение, если дерево пустое.
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException(); // если пусто — ошибка
        Node node = root; // начинаем с корня
        while (node.right != null) node = node.right; // идём вправо до конца
        return node.key; // возвращаем максимальный ключ
    }
    // Возвращает компаратор для ключей (null для естественного порядка Integer).
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // используется естественный порядок сравнения Integer
    }
    // Возвращает множество всех ключей в дереве в порядке возрастания.
    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>(); // создаём набор ключей
        keySetHelper(root, keys); // добавляем все ключи из дерева
        return keys; // возвращаем набор
    }
    // Рекурсивно собирает ключи из дерева в указанное множество.
    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node != null) { // если узел существует
            keySetHelper(node.left, keys); // обходим левое поддерево
            keys.add(node.key); // добавляем ключ
            keySetHelper(node.right, keys); // обходим правое поддерево
        }
    }
    // Возвращает коллекцию всех значений в дереве.
    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>(); // создаём список значений
        valuesHelper(root, values); // добавляем значения из дерева
        return values; // возвращаем список
    }
    // Рекурсивно собирает значения из дерева в указанный список.
    private void valuesHelper(Node node, List<String> values) {
        if (node != null) { // если узел не пустой
            valuesHelper(node.left, values); // обходим левое поддерево
            values.add(node.info); // добавляем значение
            valuesHelper(node.right, values); // обходим правое поддерево
        }
    }
    // Возвращает множество всех пар ключ-значение в дереве.
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        Set<Map.Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Map.Entry::getKey)); // создаём упорядоченный набор
        entrySetHelper(root, entries); // добавляем все пары
        return entries; // возвращаем результат
    }
    // Рекурсивно собирает пары ключ-значение из дерева в указанное множество.
    private void entrySetHelper(Node node, Set<Map.Entry<Integer, String>> entries) {
        if (node != null) { // если узел существует
            entrySetHelper(node.left, entries); // обходим левое поддерево
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.info)); // добавляем пару ключ=значение
            entrySetHelper(node.right, entries); // обходим правое поддерево
        }
    }
    // Возвращает подкарту с ключами в диапазоне [fromKey, toKey).
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap(); // создаём новую карту
        subMapHelper(root, fromKey, toKey, subMap); // добавляем элементы в диапазоне
        return subMap; // возвращаем результат
    }
    // Рекурсивно собирает элементы с ключами в диапазоне [fromKey, toKey) в новую карту.
    private void subMapHelper(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) return; // конец рекурсии
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) { // если ключ в диапазоне
            subMap.put(node.key, node.info); // добавляем элемент
            subMapHelper(node.left, fromKey, toKey, subMap); // обходим левое поддерево
            subMapHelper(node.right, fromKey, toKey, subMap); // обходим правое поддерево
        } else if (node.key.compareTo(fromKey) < 0) { // если ключ меньше fromKey
            subMapHelper(node.right, fromKey, toKey, subMap); // идём вправо
        } else {
            subMapHelper(node.left, fromKey, toKey, subMap); // иначе идём влево
        }
    }
    // Проверяет, является ли узел красным (null считается чёрным).
    private boolean isRed(Node node) {
        if (node == null) return false; // null считается чёрным
        return node.color == RED; // проверяем цвет
    }
    // Выполняет левое вращение узла для балансировки дерева.
    private Node rotateLeft(Node h) {
        Node x = h.right; // берём правого ребёнка
        h.right = x.left; // переносим левое поддерево x вправо
        x.left = h; // делаем h левым ребёнком x
        x.color = h.color; // передаём цвет
        h.color = RED; // новый потомок становится красным
        return x; // возвращаем новый корень поддерева
    }
    // Выполняет правое вращение узла для балансировки дерева.
    private Node rotateRight(Node h) {
        Node x = h.left; // берём левого ребёнка
        h.left = x.right; // переносим правое поддерево x влево
        x.right = h; // делаем h правым ребёнком x
        x.color = h.color; // передаём цвет
        h.color = RED; // новый потомок становится красным
        return x; // возвращаем новый корень поддерева
    }
    // Инвертирует цвета узла и его детей для поддержания свойств LLRB.
    private void flipColors(Node h) {
        h.color = !h.color; // инвертируем цвет текущего узла
        h.left.color = !h.left.color; // инвертируем цвет левого ребёнка
        h.right.color = !h.right.color; // инвертируем цвет правого ребёнка
    }
    // Перемещает красный цвет влево для подготовки к удалению.
    private Node moveRedLeft(Node h) {
        flipColors(h); // меняем цвета для подготовки к повороту
        if (isRed(h.right.left)) { // если правый-левый красный
            h.right = rotateRight(h.right); // выполняем поворот вправо
            h = rotateLeft(h); // затем поворот влево
            flipColors(h); // возвращаем цвета
        }
        return h; // возвращаем новый корень
    }
    // Перемещает красный цвет вправо для подготовки к удалению.
    private Node moveRedRight(Node h) {
        flipColors(h); // меняем цвета
        if (isRed(h.left.left)) { // если два левых красных подряд
            h = rotateRight(h); // выполняем поворот вправо
            flipColors(h); // восстанавливаем цвета
        }
        return h; // возвращаем узел
    }
    // Балансирует поддерево, устраняя нарушения свойств красно-чёрного дерева.
    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h); // если правый красный — поворот влево
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h); // если две левые красные — поворот вправо
        if (isRed(h.left) && isRed(h.right)) flipColors(h); // если оба ребёнка красные — меняем цвета
        return h; // возвращаем сбалансированный узел
    }
    // Находит узел с минимальным ключом в поддереве.
    private Node min(Node node) {
        while (node.left != null) node = node.left; // идём влево до самого минимума
        return node; // возвращаем минимальный узел
    }
    // Удаляет узел с минимальным ключом из поддерева, возвращая сбалансированное поддерево.
    private Node deleteMin(Node node) {
        if (node.left == null) return null; // если левого поддерева нет — удаляем узел
        if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node); // балансируем при удалении
        node.left = deleteMin(node.left); // удаляем минимальный узел рекурсивно
        return balance(node); //
    }
}