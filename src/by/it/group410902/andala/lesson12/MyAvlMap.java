/**
 * MyAvlMap — реализация Map<Integer, String> на основе АВЛ-дерева.
 * Без использования стандартных коллекций.
 * toString() выводит пары в порядке возрастания ключей: {1=one, 2=two}
 */
package by.it.group410902.andala.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    /** Узел АВЛ-дерева */
    private static class Node {
        Integer key;        // Ключ (целое число)
        String value;       // Значение (строка)
        Node left;          // Левый потомок
        Node right;         // Правый потомок
        int height;         // Высота поддерева

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  // Новая нода имеет высоту 1
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в дереве

    // Конструктор
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    /* --------------------------------------------------------------------- */
    /*                     Обязательные методы (уровень A)                  */
    /* --------------------------------------------------------------------- */

    // Строковое представление карты в формате {1=one, 2=two}
    @Override
    public String toString() {
        if (size == 0) return "{}";  // Пустая карта
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendInOrder(root, sb);  // Обход в порядке возрастания ключей
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // удалить последнюю ", "
        }
        sb.append('}');
        return sb.toString();
    }

    // Рекурсивный обход дерева в порядке возрастания (левый-корень-правый)
    private void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        appendInOrder(node.left, sb);   // Сначала левое поддерево
        sb.append(node.key).append('=').append(node.value).append(", ");  // Текущий узел
        appendInOrder(node.right, sb);  // Затем правое поддерево
    }

    // Добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();  // Ключ не может быть null
        PutResult result = putRec(root, key, value);  // Рекурсивное добавление
        root = result.node;  // Обновляем корень (может измениться после балансировки)
        String old = result.oldValue;
        if (old == null) size++;  // Увеличиваем размер только если добавили новый ключ
        return old;  // Возвращаем старое значение или null
    }

    // Вспомогательный класс для возврата результата операции put
    private static class PutResult {
        Node node;       // Новый корень поддерева
        String oldValue; // Старое значение (null если ключа не было)
        PutResult(Node node, String oldValue) {
            this.node = node;
            this.oldValue = oldValue;
        }
    }

    // Рекурсивное добавление элемента в дерево
    private PutResult putRec(Node node, Integer key, String value) {
        if (node == null) {
            // Дошли до места вставки - создаем новую ноду
            return new PutResult(new Node(key, value), null);
        }

        int cmp = key.compareTo(node.key);
        PutResult result;

        if (cmp < 0) {
            // Ключ меньше - идем в левое поддерево
            result = putRec(node.left, key, value);
            node.left = result.node;
        } else if (cmp > 0) {
            // Ключ больше - идем в правое поддерево
            result = putRec(node.right, key, value);
            node.right = result.node;
        } else {
            // Ключ найден - обновляем значение
            String old = node.value;
            node.value = value;
            return new PutResult(node, old);
        }

        // После вставки обновляем высоту и балансируем дерево
        updateHeight(node);
        return new PutResult(balance(node), result.oldValue);
    }

    // Удаление элемента по ключу
    @Override
    public String remove(Object obj) {
        if (!(obj instanceof Integer)) return null;  // Проверка типа
        Integer key = (Integer) obj;
        RemoveResult result = removeRec(root, key);  // Рекурсивное удаление
        root = result.node;  // Обновляем корень
        if (result.removed) size--;  // Уменьшаем размер если удалили
        return result.value;  // Возвращаем значение удаленного элемента
    }

    // Вспомогательный класс для возврата результата операции remove
    private static class RemoveResult {
        Node node;      // Новый корень поддерева
        String value;   // Значение удаленного элемента
        boolean removed; // Был ли элемент удален
        RemoveResult(Node node, String value, boolean removed) {
            this.node = node;
            this.value = value;
            this.removed = removed;
        }
    }

    // Рекурсивное удаление элемента из дерева
    private RemoveResult removeRec(Node node, Integer key) {
        if (node == null) {
            // Ключ не найден
            return new RemoveResult(null, null, false);
        }

        int cmp = key.compareTo(node.key);
        RemoveResult result;

        if (cmp < 0) {
            // Ищем в левом поддереве
            result = removeRec(node.left, key);
            node.left = result.node;
        } else if (cmp > 0) {
            // Ищем в правом поддереве
            result = removeRec(node.right, key);
            node.right = result.node;
        } else {
            // Ключ найден - удаляем узел
            String oldValue = node.value;

            // Случай 1: У узла нет левого потомка
            if (node.left == null) {
                return new RemoveResult(node.right, oldValue, true);
            }
            // Случай 2: У узла нет правого потомка
            else if (node.right == null) {
                return new RemoveResult(node.left, oldValue, true);
            }
            // Случай 3: У узла два потомка
            else {
                // Находим минимальный элемент в правом поддереве
                Node min = findMin(node.right);
                // Заменяем ключ и значение текущего узла на найденный минимальный
                node.key = min.key;
                node.value = min.value;
                // Удаляем минимальный элемент из правого поддерева
                RemoveResult r = removeRec(node.right, min.key);
                node.right = r.node;
                return new RemoveResult(balance(node), oldValue, true);
            }
        }

        // После удаления обновляем высоту и балансируем
        updateHeight(node);
        return new RemoveResult(balance(node), result.value, result.removed);
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node findMin(Node node) {
        while (node.left != null) node = node.left;  // Идем до самого левого узла
        return node;
    }

    // Получение значения по ключу
    @Override
    public String get(Object obj) {
        if (!(obj instanceof Integer)) return null;
        Node node = getNode(root, (Integer) obj);  // Поиск узла
        return node != null ? node.value : null;   // Возврат значения или null
    }

    // Рекурсивный поиск узла по ключу
    private Node getNode(Node node, Integer key) {
        if (node == null) return null;  // Ключ не найден
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return getNode(node.left, key);   // Ищем слева
        if (cmp > 0) return getNode(node.right, key);  // Ищем справа
        return node;  // Ключ найден
    }

    // Проверка наличия ключа
    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) return false;
        return getNode(root, (Integer) obj) != null;  // Ищем узел с таким ключом
    }

    // Получение количества элементов
    @Override
    public int size() {
        return size;
    }

    // Очистка карты
    @Override
    public void clear() {
        root = null;  // Удаляем ссылку на корень
        size = 0;     // Сбрасываем счетчик
    }

    // Проверка пустоты карты
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /* --------------------------------------------------------------------- */
    /*                     АВЛ-балансировка                                 */
    /* --------------------------------------------------------------------- */

    // Получение высоты узла (для null возвращаем 0)
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // Обновление высоты узла на основе высот потомков
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Вычисление баланс-фактора (разница высот правого и левого поддеревьев)
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.right) - height(node.left);
    }

    // Правый поворот (для случая left-left)
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t = x.right;
        x.right = y;
        y.left = t;
        updateHeight(y);  // Сначала обновляем высоту потомка
        updateHeight(x);  // Затем высоту нового корня
        return x;
    }

    // Левый поворот (для случая right-right)
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t = y.left;
        y.left = x;
        x.right = t;
        updateHeight(x);  // Сначала обновляем высоту потомка
        updateHeight(y);  // Затем высоту нового корня
        return y;
    }

    // Балансировка узла
    private Node balance(Node node) {
        if (node == null) return null;

        updateHeight(node);
        int bf = balanceFactor(node);

        // Right-heavy (правое поддерево выше)
        if (bf > 1) {
            // Right-left case: нужно сначала повернуть правое поддерево вправо
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);  // Затем левый поворот текущего узла
        }
        // Left-heavy (левое поддерево выше)
        if (bf < -1) {
            // Left-right case: нужно сначала повернуть левое поддерево влево
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);  // Затем правый поворот текущего узла
        }
        // Узел сбалансирован
        return node;
    }

    /* --------------------------------------------------------------------- */
    /*               Методы, не требуемые заданием (уровень A)               */
    /* --------------------------------------------------------------------- */

    // Остальные методы интерфейса Map не реализованы
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(java.util.Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public String putIfAbsent(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
    @Override public boolean replace(Integer key, String oldValue, String newValue) { throw new UnsupportedOperationException(); }
    @Override public String replace(Integer key, String value) { throw new UnsupportedOperationException(); }
    @Override public String compute(Integer key, java.util.function.BiFunction<? super Integer, ? super String, ? extends String> rf) { throw new UnsupportedOperationException(); }
    @Override public String computeIfAbsent(Integer key, java.util.function.Function<? super Integer, ? extends String> mf) { throw new UnsupportedOperationException(); }
    @Override public String computeIfPresent(Integer key, java.util.function.BiFunction<? super Integer, ? super String, ? extends String> rf) { throw new UnsupportedOperationException(); }
    @Override public String merge(Integer key, String value, java.util.function.BiFunction<? super String, ? super String, ? extends String> rf) { throw new UnsupportedOperationException(); }
    @Override public void forEach(java.util.function.BiConsumer<? super Integer, ? super String> action) { throw new UnsupportedOperationException(); }
    @Override public void replaceAll(java.util.function.BiFunction<? super Integer, ? super String, ? extends String> function) { throw new UnsupportedOperationException(); }
}