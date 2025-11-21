package by.it.group410902.kovalchuck.lesson01.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  // Новый узел имеет высоту 1
        }
    }

    private Node root;  // Корень
    private int size;   // Количество элементов в карте

    //Конструктор пустой карты
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    //Возвращает высоту узла.
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    //Вычисляет разность высот левого и правого поддеревьев.
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    //Обновляет высоту узла на основе высот его потомков.
    private void updateHeight(Node node) {
        if (node != null) {
            // Высота = максимальная высота потомков + 1
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    //Выполняет правый поворот вокруг узла y.
    private Node rotateRight(Node y) {
        // Сохраняем ссылки на необходимые узлы
        Node x = y.left;
        Node T2 = x.right;

        // Выполняем поворот
        x.right = y;
        y.left = T2;

        // Обновляем высоты затронутых узлов
        updateHeight(y);
        updateHeight(x);

        return x;  // x становится новым корнем
    }

    //Выполняет левый поворот вокруг узла x.
    private Node rotateLeft(Node x) {
        // Сохраняем ссылки на необходимые узлы
        Node y = x.right;
        Node T2 = y.left;

        // Выполняем поворот
        y.left = x;
        x.right = T2;

        // Обновляем высоты затронутых узлов
        updateHeight(x);
        updateHeight(y);

        return y;  // y становится новым корнем
    }

    //Балансирует узел, если это необходимо, выполняя соответствующие повороты.
    private Node balance(Node node) {
        if (node == null) return null;

        // Обновляем высоту текущего узла
        updateHeight(node);
        int balance = balanceFactor(node);

        // Left Left Case - требуется один правый поворот
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case - требуется левый поворот левого потомка, затем правый поворот текущего узла
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case - требуется один левый поворот
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case - требуется правый поворот правого потомка, затем левый поворот текущего узла
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Балансировка не требуется
        return node;
    }

    //Находит узел с минимальным ключом в поддереве.
    private Node minValueNode(Node node) {
        Node current = node;
        // Минимальный ключ находится в самом левом узле
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    //Добавляет или обновляет пару ключ-значение в карте.
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        // Массив для возврата старого значения через рекурсию
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);

        // Если ключа не было, увеличиваем размер
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    //Рекурсивный вспомогательный метод для вставки узла
    private Node put(Node node, Integer key, String value, String[] oldValue) {
        // Достигли места вставки - создаем новый узел
        if (node == null) {
            return new Node(key, value);
        }

        // Сравниваем ключи для определения направления обхода
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // Ключ меньше - идем в левое поддерево
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            // Ключ больше - идем в правое поддерево
            node.right = put(node.right, key, value, oldValue);
        } else {
            // Ключ найден - обновляем значение
            oldValue[0] = node.value;
            node.value = value;
            return node;  // Балансировка не требуется при обновлении
        }

        // Балансируем дерево после вставки
        return balance(node);
    }

    //Удаляет ключ и связанное с ним значение из карты
    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null; // Ключ не Integer - не может быть в карте
        }

        Integer intKey = (Integer) key;
        // Массив для возврата удаленного значения через рекурсию
        String[] removedValue = new String[1];
        root = remove(root, intKey, removedValue);

        // Если ключ был найден и удален, уменьшаем размер
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    // Рекурсивный вспомогательный метод для удаления узла.
    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;  // Ключ не найден
        }

        // Сравниваем ключи для определения направления обхода
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // Ключ меньше - ищем в левом поддереве
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            // Ключ больше - ищем в правом поддереве
            node.right = remove(node.right, key, removedValue);
        } else {
            // Ключ найден - удаляем узел
            removedValue[0] = node.value;

            // Узел с одним потомком или без потомков
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // Узел с двумя потомками - находим минимальный узел в правом поддереве
                Node temp = minValueNode(node.right);
                // Заменяем ключ и значение на найденные
                node.key = temp.key;
                node.value = temp.value;
                // Удаляем минимальный узел из правого поддерева
                node.right = remove(node.right, temp.key, new String[1]);
            }
        }

        // Если дерево стало пустым после удаления
        if (node == null) {
            return null;
        }

        // Балансируем дерево после удаления
        return balance(node);
    }

    //Возвращает значение, связанное с указанным ключом.
    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null; // Ключ не Integer - не может быть в карте
        }

        Integer intKey = (Integer) key;
        // Итеративный поиск по дереву
        Node current = root;
        while (current != null) {
            int cmp = intKey.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;      // Идем влево
            } else if (cmp > 0) {
                current = current.right;     // Идем вправо
            } else {
                return current.value;        // Ключ найден
            }
        }
        return null;  // Ключ не найден
    }

    //Проверяет, содержится ли указанный ключ в карте.
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false; // Ключ не Integer - не может быть в карте
        }

        Integer intKey = (Integer) key;
        // Итеративный поиск по дереву
        Node current = root;
        while (current != null) {
            int cmp = intKey.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;      // Идем влево
            } else if (cmp > 0) {
                current = current.right;     // Идем вправо
            } else {
                return true;                 // Ключ найден
            }
        }
        return false;  // Ключ не найден
    }

    //Возвращает количество элементов в карте.
    @Override
    public int size() {
        return size;
    }

    //Удаляет все элементы из карты.
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    //Проверяет, пуста ли карта.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //Возвращает строковое представление карты
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";  // Пустая карта
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);  // Обход в порядке возрастания
        // Удаляем лишнюю запятую и пробел после последнего элемента
        if (sb.length() > 1) {
            // Убираем последнюю запятую и пробел
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    //Вспомогательный метод для симметричного обхода дерева (левый-корень-правый).
    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            // Рекурсивно обходим левое поддерево
            inOrderToString(node.left, sb);

            // Добавляем текущий элемент в формате key=value
            sb.append(node.key).append("=").append(node.value).append(", ");

            // Рекурсивно обходим правое поддерево
            inOrderToString(node.right, sb);
        }
    }

    // Проверяет, содержится ли указанное значение в карте.
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue not implemented");
    }

    //Копирует все элементы из указанной карты в текущую.
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not implemented");
    }

    //Возвращает множество ключей карты.
    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    //Возвращает коллекцию значений карты.
    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    //Возвращает множество пар ключ-значение карты.
    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }
}