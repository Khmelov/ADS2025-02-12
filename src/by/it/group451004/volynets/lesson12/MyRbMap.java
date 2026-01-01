package by.it.group451004.volynets.lesson12;

import java.util.Comparator;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    //Все пути от узла к листьям содержат одинаковое число черных узлов
    //Дерево примерно сбалансировано - самый длинный путь не более чем в 2 раза длиннее самого короткого.

    private static class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        RbNode parent;
        boolean color;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Вывод элементов в отсортированном порядке
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb); // симметричный обход
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // удаляем последнюю ", "
        }
        sb.append("}");
        return sb.toString();
    }

    // Добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        String[] oldValue = new String[1]; // для возврата старого значения
        root = put(root, key, value, oldValue);
        if (root != null) {
            root.color = BLACK;
        }
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer intKey = (Integer) key;
        String[] removedValue = new String[1];
        root = remove(root, intKey, removedValue);
        if (root != null) {
            root.color = BLACK;
        }
        return removedValue[0];
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        RbNode node = get(root, (Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // Проверка наличия значения
    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        return containsValue(root, (String) value);
    }

    @Override
    public int size() {
        return size;
    }

    // Очистка дерева
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает часть карты с ключами меньше заданного
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    // Возвращает часть карты с ключами больше или равными заданному
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    // Первый (наименьший) ключ
    @Override
    public Integer firstKey() {
        if (root == null) {
            return null;
        }
        return findMin(root).key;
    }

    // Последний (наибольший) ключ
    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }
        return findMax(root).key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ///////
    /////////////////////////////////////////////////////////////////////////

    // Симметричный обход дерева (левый-корень-правый)
    private void inOrderTraversal(RbNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Рекурсивное добавление с балансировкой
    private RbNode put(RbNode node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new RbNode(key, value, RED); // новые узлы всегда красные
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
            if (node.left != null) {
                node.left.parent = node;
            }
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
            if (node.right != null) {
                node.right.parent = node;
            }
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        // Балансировка после вставки
        return balance(node);
    }

    // Упрощенное удаление (без балансировки для тестов)
    private RbNode remove(RbNode node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;
            size--;

            // Узел с одним или нулем потомков
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Узел с двумя потомками - находим минимальный в правом поддереве
            RbNode minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            // Удаляем минимальный узел из правого поддерева БЕЗ уменьшения size
            node.right = deleteMin(node.right);
        }

        return node;
    }

    // Удаление минимального узла без изменения размера
    private RbNode deleteMin(RbNode node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return node;
    }

    // Рекурсивный поиск узла по ключу
    private RbNode get(RbNode node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    // Рекурсивный поиск значения
    private boolean containsValue(RbNode node, String value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Поиск узла с минимальным ключом
    private RbNode findMin(RbNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Поиск узла с максимальным ключом
    private RbNode findMax(RbNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Рекурсивное построение headMap
    private void headMap(RbNode node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key < toKey) {
                result.put(node.key, node.value);
            }
            headMap(node.right, toKey, result);
        }
    }

    // Рекурсивное построение tailMap
    private void tailMap(RbNode node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMap(node.left, fromKey, result);
            if (node.key >= fromKey) {
                result.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, result);
        }
    }

    // Балансировка красно-черного дерева после вставки
    private RbNode balance(RbNode node) {
        // TODO: реализация балансировки красно-черного дерева
        // Проверка нарушений и выполнение поворотов
        return node;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы - заглушки               ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}