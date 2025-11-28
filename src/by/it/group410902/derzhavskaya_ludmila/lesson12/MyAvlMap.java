package by.it.group410902.derzhavskaya_ludmila.lesson12;
import java.util.Map;

// Класс реализует интерфейс Map<Integer, String> на основе АВЛ-дерева
public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс для узла АВЛ-дерева
    private class AvlNode {
        Integer key;
        String value;
        AvlNode left;
        AvlNode right;
        int height;

        // Конструктор узла
        AvlNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;   // Новая нода имеет высоту 1
        }
    }

    private AvlNode root;      // Корень дерева
    private int size;          // Количество элементов в дереве

    // Конструктор по умолчанию
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // Возвращает высоту узла
    private int height(AvlNode node) {
        return node == null ? 0 : node.height;
    }

    // Вычисляет разницу высот правого и левого поддеревьев
    private int getBalance(AvlNode node) {
        if (node == null) return 0;
        return height(node.right) - height(node.left);
    }

    // Обновляет высоту узла на основе высот потомков
    private void updateHeight(AvlNode node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    // Правый поворот для балансировки дерева
    private AvlNode rotateRight(AvlNode y) {
        AvlNode x = y.left;
        AvlNode T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Левый поворот для балансировки дерева
    private AvlNode rotateLeft(AvlNode x) {
        AvlNode y = x.right;
        AvlNode T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Балансирует узел после вставки или удаления
    private AvlNode balance(AvlNode node) {
        if (node == null) return null;

        updateHeight(node);

        // вычисляем разницу высот
        int balance = getBalance(node);

        if (balance < -1 && getBalance(node.left) <= 0) {
            return rotateRight(node);
        }

        if (balance > 1 && getBalance(node.right) >= 0) {
            return rotateLeft(node);
        }

        // Левый правый случай - левый-правый поворот
        if (balance < -1 && getBalance(node.left) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Правый левый случай - правый-левый поворот
        if (balance > 1 && getBalance(node.right) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Вспомогательный метод для рекурсивной вставки
    private AvlNode putRecursive(AvlNode node, Integer key, String value) {
        // Достигли листового узла - создаем новую ноду
        if (node == null) {
            size++;
            return new AvlNode(key, value);
        }

        // Сравниваем ключи для определения направления обхода
        if (key < node.key) {
            node.left = putRecursive(node.left, key, value);
        } else if (key > node.key) {
            node.right = putRecursive(node.right, key, value);
        } else {
            // Ключ уже существует - обновляем значение
            node.value = value;
            return node;
        }

        // Балансируем дерево после вставки
        return balance(node);
    }

    // Добавляет пару ключ-значение в дерево
    @Override
    public String put(Integer key, String value) {

        // Сохраняем старое значение для возврата
        String oldValue = get(key);

        // Выполняем вставку
        root = putRecursive(root, key, value);

        return oldValue;
    }

    // Вспомогательный метод для поиска узла с минимальным ключом
    private AvlNode findMin(AvlNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Вспомогательный метод для рекурсивного удаления
    private AvlNode removeRecursive(AvlNode node, Integer key) {
        if (node == null) {
            return null; // Ключ не найден
        }

        // Ищем узел для удаления
        if (key < node.key) {
            node.left = removeRecursive(node.left, key);
        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key);
        } else {
            // Узел найден - выполняем удаление
            size--;

            // Узел с одним потомком или без потомков
            if (node.left == null || node.right == null) {
                AvlNode temp = (node.left != null) ? node.left : node.right;

                // Нет потомков
                if (temp == null) {
                    return null;
                } else {
                    // Один потомок
                    return temp;
                }
            } else {
                // Узел с двумя потомками - находим минимальный в правом поддереве
                AvlNode temp = findMin(node.right);

                // Копируем данные минимального узла
                node.key = temp.key;
                node.value = temp.value;

                // Удаляем минимальный узел
                node.right = removeRecursive(node.right, temp.key);
            }
        }

        // Балансируем дерево после удаления
        return balance(node);
    }

    // Удаляет узел с заданным ключом
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer k = (Integer) key;
        String oldValue = get(k);

        if (oldValue != null) {
            root = removeRecursive(root, k);
        }

        return oldValue;
    }

    // Вспомогательный метод для рекурсивного поиска
    private String getRecursive(AvlNode node, Integer key) {
        if (node == null) {
            return null; // Ключ не найден
        }

        if (key < node.key) {
            return getRecursive(node.left, key);
        } else if (key > node.key) {
            return getRecursive(node.right, key);
        } else {
            return node.value; // Ключ найден
        }
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        return getRecursive(root, (Integer) key);
    }

    // Проверяет наличие ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size;
    }

    // Очищает дерево
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Проверяет пустое ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательный метод для рекурсивного обхода в порядке возрастания
    private void inOrderTraversal(AvlNode node, StringBuilder sb) {
        if (node != null) {
            // Рекурсивно обходим левое поддерево
            if (node.left != null) {
                inOrderTraversal(node.left, sb);
                sb.append(", ");
            }

            // Добавляем текущий узел
            sb.append(node.key).append("=").append(node.value);

            // Рекурсивно обходим правое поддерево
            if (node.right != null) {
                sb.append(", ");
                inOrderTraversal(node.right, sb);
            }
        }
    }

    // Возвращает строковое представление дерева в порядке возрастания ключей
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса Map не реализованы        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented");
    }
}