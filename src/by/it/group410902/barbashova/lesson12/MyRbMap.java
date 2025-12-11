package by.it.group410902.barbashova.lesson12;
import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    Node Root;

    enum Color {
        RED, BLACK
    }

    // Внутренний класс, представляющий узел красно-черного дерева
    class Node {
        Integer key;
        String value;
        Node parent;
        Node left, right;
        Color color;

        public Node(Color color, Integer key, String value) {
            this.color = color;
            this.key = key;
            this.value = value;
        }
    }


    @Override
    public String toString() {
        if (Root == null)
            return "{}";
        StringBuilder sb = new StringBuilder().append("{");
        inOrderTraversal(Root, sb); // Обход в порядке возрастания ключей
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход дерева (левый-корень-правый)
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key + "=" + node.value + ", ");
            inOrderTraversal(node.right, sb);
        }
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size(Root);
    }

    // Рекурсивный метод для вычисления размера поддерева
    int size(Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.left) + size(node.right) + 1;
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    // Проверяет, содержится ли ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return SearchKey((Integer) key, Root) != null;
    }

    // Рекурсивный поиск узла по ключу
    Node SearchKey(Integer key, Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    // Проверяет, содержится ли значение в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValue(Root, value);
    }

    // Рекурсивный поиск значения в дереве
    boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {
        Node node = SearchKey((Integer) key, Root);
        return (node != null) ? node.value : null;
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (Root == null) {
            // Создание корня (всегда черный)
            Root = new Node(Color.BLACK, key, value);
        } else {
            // Вставка нового узла (красный по умолчанию)
            Node newNode = new Node(Color.RED, key, value);
            Node current = Root;

            // Поиск места для вставки
            while (true) {
                newNode.parent = current;
                if (key.compareTo(current.key) < 0) {
                    if (current.left == null) {
                        current.left = newNode;
                        break;
                    } else {
                        current = current.left;
                    }
                } else if (key.compareTo(current.key) > 0) {
                    if (current.right == null) {
                        current.right = newNode;
                        break;
                    } else {
                        current = current.right;
                    }
                } else {
                    // Ключ уже существует - обновление значения
                    String oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
            }

            // Балансировка после вставки
            balanceAfterInsert(newNode);
        }
        return null;
    }

    // Балансировка дерева после вставки нового узла
    void balanceAfterInsert(Node node) {
        // Пока не достигнут корень и есть конфликт красного цвета
        while (node != Root && node.color == Color.RED && node.parent.color == Color.RED) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (parent == grandparent.left) {
                // Случай: родитель является левым потомком
                Node uncle = grandparent.right;

                // Случай 1: дядя красный
                if (uncle != null && uncle.color == Color.RED) {
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    node = grandparent; // Продолжаем проверку с дедушкой
                } else {
                    // Случай 2: дядя черный
                    if (node == parent.right) {
                        // Случай 2a: узел - правый потомок
                        node = parent;
                        RotateLeft(node);
                    }
                    // Случай 2b: узел - левый потомок
                    parent.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    RotateRight(grandparent);
                }
            } else {
                // Случай: родитель является правым потомком (симметрично)
                Node uncle = grandparent.left;

                if (uncle != null && uncle.color == Color.RED) {
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        RotateRight(node);
                    }
                    parent.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    RotateLeft(grandparent);
                }
            }
        }

        // Корень всегда черный
        Root.color = Color.BLACK;
    }

    // Поиск преемника (минимального узла в правом поддереве)
    Node getSuccessor(Node node) {
        Node successor = null;
        Node current = node.right;

        while (current != null) {
            successor = current;
            current = current.left;
        }

        return successor;
    }

    // Удаление узла из дерева
    void deleteNode(Node node) {
        Node replacement;

        // Узел имеет двух потомков
        if (node.left != null && node.right != null) {
            Node successor = getSuccessor(node);
            // Копируем данные преемника
            node.key = successor.key;
            node.value = successor.value;
            node = successor; // Удаляем преемника вместо исходного узла
        }

        // Определяем замену (единственный потомок или null)
        replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            // Узел имеет одного потомка
            replacement.parent = node.parent;
            if (node.parent == null) {
                Root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            // Балансировка если удаляемый узел был черным
            if (node.color == Color.BLACK) {
                balanceDeletion(replacement);
            }
        } else if (node.parent == null) {
            // Удаление корня
            Root = null;
        } else {
            // Узел без потомков
            if (node.color == Color.BLACK) {
                balanceDeletion(node);
            }

            // Удаление ссылки на узел
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
    }

    // Вспомогательные методы для работы с цветом
    Color getColor(Node node) {
        return (node == null) ? Color.BLACK : node.color;
    }

    void setColor(Node node, Color color) {
        if (node != null) {
            node.color = color;
        }
    }

    // Левый поворот
    void RotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        if (right.left != null)
            right.left.parent = node;
        right.parent = node.parent;
        if (node.parent == null)
            Root = right;
        else if (node == node.parent.left)
            node.parent.left = right;
        else
            node.parent.right = right;
        right.left = node;
        node.parent = right;
    }

    // Правый поворот
    void RotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        if (left.right != null)
            left.right.parent = node;
        left.parent = node.parent;
        if (node.parent == null)
            Root = left;
        else if (node == node.parent.right)
            node.parent.right = left;
        else
            node.parent.left = left;
        left.right = node;
        node.parent = left;
    }

    // Балансировка после удаления
    void balanceDeletion(Node node) {
        while (node != Root && getColor(node) == Color.BLACK) {
            if (node == node.parent.left) {
                // Случай: узел является левым потомком
                Node sibling = node.parent.right;

                // Случай 1: брат красный
                if (getColor(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(node.parent, Color.RED);
                    RotateLeft(node.parent);
                    sibling = node.parent.right;
                }

                // Случай 2: оба потомка брата черные
                if (getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = node.parent;
                } else {
                    // Случай 3: правый потомок брата черный
                    if (getColor(sibling.right) == Color.BLACK) {
                        setColor(sibling.left, Color.BLACK);
                        setColor(sibling, Color.RED);
                        RotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    // Случай 4: правый потомок брата красный
                    setColor(sibling, getColor(node.parent));
                    setColor(node.parent, Color.BLACK);
                    setColor(sibling.right, Color.BLACK);
                    RotateLeft(node.parent);
                    node = Root;
                }
            } else {
                // Случай: узел является правым потомком (симметрично)
                Node sibling = node.parent.left;

                if (getColor(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(node.parent, Color.RED);
                    RotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (getColor(sibling.right) == Color.BLACK && getColor(sibling.left) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = node.parent;
                } else {
                    if (getColor(sibling.left) == Color.BLACK) {
                        setColor(sibling.right, Color.BLACK);
                        setColor(sibling, Color.RED);
                        RotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    setColor(sibling, getColor(node.parent));
                    setColor(node.parent, Color.BLACK);
                    setColor(sibling.left, Color.BLACK);
                    RotateRight(node.parent);
                    node = Root;
                }
            }
        }

        setColor(node, Color.BLACK);
    }

    // Удаление узла по ключу
    @Override
    public String remove(Object key) {
        Node node = SearchKey((Integer) key, Root);
        if (node != null) {
            String oldValue = node.value;
            deleteNode(node);
            return oldValue;
        }
        return null;
    }

    // Очистка дерева
    @Override
    public void clear() {
        Root = clear(Root);
    }

    // Рекурсивная очистка поддерева
    Node clear(Node node) {
        if (node == null)
            return null;
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    // Методы интерфейса SortedMap

    // Возвращает первый (наименьший) ключ
    @Override
    public Integer firstKey() {
        Node first = firstNode(Root);
        return (first != null) ? first.key : null;
    }

    // Поиск узла с минимальным ключом
    Node firstNode(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Возвращает последний (наибольший) ключ
    @Override
    public Integer lastKey() {
        Node last = lastNode(Root);
        return (last != null) ? last.key : null;
    }

    // Поиск узла с максимальным ключом
    Node lastNode(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Возвращает подкарту с ключами меньше toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        headMap(Root, toKey, subMap);
        return subMap;
    }

    // Рекурсивное построение headMap
    void headMap(Node node, Integer toKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMap(node.right, toKey, subMap);
        }

        headMap(node.left, toKey, subMap);
    }

    // Возвращает подкарту с ключами больше или равными fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        tailMap(Root, fromKey, subMap);
        return subMap;
    }

    // Рекурсивное построение tailMap
    void tailMap(Node node, Integer fromKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMap(node.left, fromKey, subMap);
        }

        tailMap(node.right, fromKey, subMap);
    }

    ///////////////////////
    // Нереализованные методы интерфейса
    ///////////////////////

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок сортировки
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }
}