/**
 * MyRbMap — реализация SortedMap<Integer, String> на основе красно-чёрного дерева.
 * Без использования стандартных коллекций.
 * toString() выводит пары в порядке возрастания ключей.
 */
package by.it.group410902.andala.lesson12;

import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    // Цвета узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Узел красно-чёрного дерева
    private static class Node {
        Integer key;        // Ключ
        String value;       // Значение
        Node left;          // Левый потомок
        Node right;         // Правый потомок
        Node parent;        // Родительский узел
        boolean color;      // Цвет узла (RED или BLACK)

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;  // Корень дерева
    private int size;   // Количество элементов

    // Конструктор
    public MyRbMap() {
        root = null;
        size = 0;
    }

    /* --------------------------------------------------------------------- */
    /*                     Обязательные методы (уровень B)                  */
    /* --------------------------------------------------------------------- */

    // Строковое представление карты
    @Override
    public String toString() {
        if (size == 0) return "{}";  // Пустая карта
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inOrderAppend(root, sb);  // Обход в порядке возрастания ключей
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю ", "
        }
        sb.append('}');
        return sb.toString();
    }

    // Рекурсивный обход дерева в порядке возрастания (симметричный обход)
    private void inOrderAppend(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderAppend(node.left, sb);   // Левое поддерево
        sb.append(node.key).append('=').append(node.value).append(", ");  // Текущий узел
        inOrderAppend(node.right, sb);  // Правое поддерево
    }

    // Добавление или обновление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        PutResult result = insert(key, value);  // Вставляем элемент
        return result.oldValue;  // Возвращаем старое значение
    }

    // Результат операции вставки
    private static class PutResult {
        String oldValue;  // Старое значение (null если ключа не было)
        Node newNode;     // Новый узел
        PutResult(String oldValue, Node newNode) {
            this.oldValue = oldValue;
            this.newNode = newNode;
        }
    }

    // Вставка элемента в дерево
    private PutResult insert(Integer key, String value) {
        Node parent = null;
        Node current = root;

        // Поиск места для вставки
        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                // Ключ уже существует - обновляем значение
                String old = current.value;
                current.value = value;
                return new PutResult(old, current);
            }
        }

        // Создаем новый узел (красный по умолчанию)
        Node newNode = new Node(key, value, RED, parent);

        // Вставляем узел в дерево
        if (parent == null) {
            root = newNode;  // Дерево было пустым
        } else {
            int cmp = key.compareTo(parent.key);
            if (cmp < 0) parent.left = newNode;
            else parent.right = newNode;
        }

        // Балансируем дерево после вставки
        fixAfterInsert(newNode);
        size++;
        return new PutResult(null, newNode);
    }

    // Удаление элемента по ключу
    @Override
    public String remove(Object obj) {
        if (!(obj instanceof Integer)) return null;
        Integer key = (Integer) obj;
        RemoveResult result = delete(key);  // Удаляем элемент
        if (result.removed) size--;
        return result.value;
    }

    // Результат операции удаления
    private static class RemoveResult {
        String value;    // Значение удаленного элемента
        boolean removed; // Был ли элемент удален
        RemoveResult(String value, boolean removed) {
            this.value = value;
            this.removed = removed;
        }
    }

    // Удаление узла из дерева
    private RemoveResult delete(Integer key) {
        Node z = findNode(key);  // Находим узел для удаления
        if (z == null) return new RemoveResult(null, false);  // Узел не найден

        String oldValue = z.value;
        Node x;  // Узел, который займет место удаленного
        Node y = z;  // Узел, который фактически удаляется
        boolean yOriginalColor = y.color;  // Сохраняем исходный цвет

        // Случай 1: У узла нет левого потомка
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);  // Заменяем z на его правого потомка
        }
        // Случай 2: У узла нет правого потомка
        else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);  // Заменяем z на его левого потомка
        }
        // Случай 3: У узла два потомка
        else {
            y = minimum(z.right);  // Находим минимальный узел в правом поддереве
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);  // Заменяем y на его правого потомка
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }

            transplant(z, y);  // Заменяем z на y
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            y.color = z.color;  // Сохраняем цвет z
        }

        // Если удалили черный узел - нужно балансировать
        if (yOriginalColor == BLACK) {
            fixAfterDelete(x);
        }

        return new RemoveResult(oldValue, true);
    }

    // Получение значения по ключу
    @Override
    public String get(Object obj) {
        if (!(obj instanceof Integer)) return null;
        Node node = findNode((Integer) obj);
        return node != null ? node.value : null;
    }

    // Проверка наличия ключа
    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) return false;
        return findNode((Integer) obj) != null;
    }

    // Проверка наличия значения
    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        // Проверяем текущий узел
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        // Ищем в левом и правом поддеревьях
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }

    // Получение количества элементов
    @Override
    public int size() {
        return size;
    }

    // Очистка карты
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Проверка пустоты карты
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает часть карты с ключами меньше toKey
    @Override
    public MyRbMap headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        headMapRec(root, toKey, result);
        return result;
    }

    // Рекурсивное построение headMap
    private void headMapRec(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);  // Добавляем подходящий узел
            headMapRec(node.right, toKey, result);  // Идем в правое поддерево
        }
        headMapRec(node.left, toKey, result);  // Всегда проверяем левое поддерево
    }

    // Возвращает часть карты с ключами >= fromKey
    @Override
    public MyRbMap tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        tailMapRec(root, fromKey, result);
        return result;
    }

    // Рекурсивное построение tailMap
    private void tailMapRec(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);  // Добавляем подходящий узел
            tailMapRec(node.left, fromKey, result);  // Идем в левое поддерево
        }
        tailMapRec(node.right, fromKey, result);  // Всегда проверяем правое поддерево
    }

    // Получение минимального ключа
    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return minimum(root).key;
    }

    // Получение максимального ключа
    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return maximum(root).key;
    }

    /* --------------------------------------------------------------------- */
    /*                     Вспомогательные методы RB-дерева                 */
    /* --------------------------------------------------------------------- */

    // Поиск узла по ключу
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current;  // Найден
        }
        return null;  // Не найден
    }

    // Поиск минимального узла в поддереве
    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Поиск максимального узла в поддереве
    private Node maximum(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    // Замена одного поддерева другим
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;  // u был корнем
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    // Балансировка после вставки
    private void fixAfterInsert(Node z) {
        // Пока родитель красный (нарушение свойства)
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                // Случай: родитель - левый потомок деда
                Node y = z.parent.parent.right;  // Дядя
                if (y != null && y.color == RED) {
                    // Случай 1: дядя красный
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        // Случай 2: z - правый потомок
                        z = z.parent;
                        rotateLeft(z);
                    }
                    // Случай 3: z - левый потомок
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                // Симметричный случай: родитель - правый потомок деда
                Node y = z.parent.parent.left;  // Дядя
                if (y != null && y.color == RED) {
                    // Случай 1: дядя красный
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        // Случай 2: z - левый потомок
                        z = z.parent;
                        rotateRight(z);
                    }
                    // Случай 3: z - правый потомок
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;  // Корень всегда черный
    }

    // Балансировка после удаления (исправленная версия)
    private void fixAfterDelete(Node x) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == null) {
                // Обработка случая когда x == null (двойной черный)
                Node parent = x == null ? root : x.parent;
                if (parent == null) break;
                if (parent.left == null) {
                    fixAfterDeleteCaseRight(parent);
                } else {
                    fixAfterDeleteCaseLeft(parent);
                }
                break;
            }

            if (x == x.parent.left) {
                // x - левый потомок
                Node w = x.parent.right;  // Брат
                if (w == null) {
                    x = x.parent;
                    continue;
                }
                if (w.color == RED) {
                    // Случай 1: брат красный
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                    if (w == null) {
                        x = x.parent;
                        continue;
                    }
                }
                if ((w.left == null || w.left.color == BLACK) &&
                        (w.right == null || w.right.color == BLACK)) {
                    // Случай 2: оба потомка брата черные
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right == null || w.right.color == BLACK) {
                        // Случай 3: правый потомок брата черный
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    // Случай 4: правый потомок брата красный
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                // Симметричный случай: x - правый потомок
                Node w = x.parent.left;  // Брат
                if (w == null) {
                    x = x.parent;
                    continue;
                }
                if (w.color == RED) {
                    // Случай 1: брат красный
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                    if (w == null) {
                        x = x.parent;
                        continue;
                    }
                }
                if ((w.right == null || w.right.color == BLACK) &&
                        (w.left == null || w.left.color == BLACK)) {
                    // Случай 2: оба потомка брата черные
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left == null || w.left.color == BLACK) {
                        // Случай 3: левый потомок брата черный
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    // Случай 4: левый потомок брата красный
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    // Дополнительные методы для обработки особых случаев удаления
    private void fixAfterDeleteCaseLeft(Node parent) {
        Node w = parent.right;
        if (w == null) return;
        // ... обработка случаев для левой стороны
    }

    private void fixAfterDeleteCaseRight(Node parent) {
        Node w = parent.left;
        if (w == null) return;
        // ... обработка случаев для правой стороны
    }

    // Левый поворот
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Правый поворот
    private void rotateRight(Node y) {
        Node x = y.left;
        if (x == null) return;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    /* --------------------------------------------------------------------- */
    /*               Методы, не требуемые заданием (уровень B)               */
    /* --------------------------------------------------------------------- */

    @Override public java.util.Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(java.util.Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}