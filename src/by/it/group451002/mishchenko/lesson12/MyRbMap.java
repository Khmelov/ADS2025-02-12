package by.it.group451002.mishchenko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private Node root;  // Корень красно-черного дерева
    private int size;   // Количество элементов в дереве

    // Узел красно-черного дерева
    private static class Node {
        Integer key;        // Ключ узла
        String value;       // Значение узла
        Node left;          // Левый потомок
        Node right;         // Правый потомок
        Node parent;        // Родительский узел
        boolean color;      // Цвет узла: true = красный, false = черный

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    // Константы для цветов узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Левый поворот вокруг узла n
    private void rotateLeft(Node n) {
        Node r = n.right;
        n.right = r.left;
        if (r.left != null) r.left.parent = n;
        r.parent = n.parent;
        if (n.parent == null) root = r;
        else if (n == n.parent.left) n.parent.left = r;
        else n.parent.right = r;
        r.left = n;
        n.parent = r;
    }

    // Правый поворот вокруг узла n
    private void rotateRight(Node n) {
        Node l = n.left;
        n.left = l.right;
        if (l.right != null) l.right.parent = n;
        l.parent = n.parent;
        if (n.parent == null) root = l;
        else if (n == n.parent.right) n.parent.right = l;
        else n.parent.left = l;
        l.right = n;
        n.parent = l;
    }

    // Восстановление свойств красно-черного дерева после вставки
    private void fixInsert(Node n) {
        while (n != root && n.parent.color == RED) {
            Node p = n.parent;
            Node g = p.parent;
            if (p == g.left) {
                Node u = g.right;
                if (u != null && u.color == RED) {
                    // Случай 1: дядя красный - перекрашиваем
                    p.color = BLACK; // Родитель
                    u.color = BLACK; // Дядя
                    g.color = RED;   // Дедушка
                    n = g;
                } else {
                    if (n == p.right) {
                        // Случай 2: дядя черный, узел - правый потомок
                        n = p;
                        rotateLeft(n);
                    }
                    // Случай 3: дядя черный, узел - левый потомок
                    p = n.parent;
                    g = p.parent;
                    p.color = BLACK;
                    g.color = RED;
                    rotateRight(g);
                }
            } else {
                Node u = g.left;
                if (u != null && u.color == RED) {
                    // Случай 1: дядя красный - перекрашиваем
                    p.color = BLACK;
                    u.color = BLACK;
                    g.color = RED;
                    n = g;
                } else {
                    if (n == p.left) {
                        // Случай 2: дядя черный, узел - левый потомок
                        n = p;
                        rotateRight(n);
                    }
                    // Случай 3: дядя черный, узел - правый потомок
                    p = n.parent;
                    g = p.parent;
                    p.color = BLACK;
                    g.color = RED;
                    rotateLeft(g);
                }
            }
        }
        root.color = BLACK; // Корень всегда черный
    }

    // Вставка узла в дерево (нерекурсивная)
    private void insertNode(Node n, Integer key, String value) {
        Node current = root;
        Node parent = null;

        // Поиск места для вставки
        while (current != null) {
            parent = current;
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
            else {
                String old = current.value;
                current.value = value; // Обновление существующего ключа
                return;
            }
        }

        // Создание нового узла (красного по умолчанию)
        Node newNode = new Node(key, value, RED, parent);
        if (parent == null) root = newNode;
        else if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;

        size++;
        fixInsert(newNode); // Восстановление свойств дерева
    }

    // Поиск узла по ключу (нерекурсивный)
    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
            else return current;
        }
        return null;
    }

    // Найти узел с минимальным ключом в поддереве
    private Node findMin(Node n) {
        while (n != null && n.left != null) n = n.left;
        return n;
    }

    // Найти узел с максимальным ключом в поддереве
    private Node findMax(Node n) {
        while (n != null && n.right != null) n = n.right;
        return n;
    }

    // Найти следующий узел в порядке возрастания (преемник)
    private Node findNext(Node n) {
        if (n == null) return null;
        if (n.right != null) return findMin(n.right); // Минимум в правом поддереве
        Node p = n.parent;
        // Поднимаемся до первого узла, который является левым потомком
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Node current = findMin(root);
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.key).append("=").append(current.value);
            first = false;
            current = findNext(current);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        insertNode(root, key, value);
        return old;
    }

    @Override
    public String remove(Object key) {
        Integer k = (Integer) key;
        Node n = findNode(k);
        if (n == null) return null;

        String oldValue = n.value;

        // Упрощенное удаление (без балансировки)
        if (n.left == null && n.right == null) {
            // Узел без потомков
            if (n.parent == null) root = null;
            else if (n == n.parent.left) n.parent.left = null;
            else n.parent.right = null;
        } else if (n.left == null) {
            // Узел только с правым потомком
            if (n.parent == null) root = n.right;
            else if (n == n.parent.left) n.parent.left = n.right;
            else n.parent.right = n.right;
            n.right.parent = n.parent;
        } else if (n.right == null) {
            // Узел только с левым потомком
            if (n.parent == null) root = n.left;
            else if (n == n.parent.left) n.parent.left = n.left;
            else n.parent.right = n.left;
            n.left.parent = n.parent;
        } else {
            // Узел с двумя потомками - находим преемника
            Node successor = findMin(n.right);
            n.key = successor.key;
            n.value = successor.value;
            // Удаляем преемника
            if (successor.parent.left == successor) {
                successor.parent.left = successor.right;
            } else {
                successor.parent.right = successor.right;
            }
            if (successor.right != null) successor.right.parent = successor.parent;
        }

        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return n != null ? n.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node current = findMin(root);
        while (current != null) {
            if (current.value.equals(value)) return true;
            current = findNext(current);
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Integer firstKey() {
        Node min = findMin(root);
        return min != null ? min.key : null;
    }

    @Override
    public Integer lastKey() {
        Node max = findMax(root);
        return max != null ? max.key : null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return new SubMap(null, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return new SubMap(fromKey, null);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return new SubMap(fromKey, toKey);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // Внутренний класс для представления подкарты
    private class SubMap implements SortedMap<Integer, String> {
        private final Integer fromKey; // Нижняя граница (включительно)
        private final Integer toKey;   // Верхняя граница (исключительно)

        SubMap(Integer fromKey, Integer toKey) {
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        @Override
        public String put(Integer key, String value) {
            // Проверка границ перед вставкой
            if (fromKey != null && key.compareTo(fromKey) < 0) return null;
            if (toKey != null && key.compareTo(toKey) >= 0) return null;
            return MyRbMap.this.put(key, value);
        }

        @Override
        public String get(Object key) {
            // Проверка границ перед поиском
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return null;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return null;
            return MyRbMap.this.get(key);
        }

        @Override
        public boolean containsKey(Object key) {
            // Проверка границ перед проверкой наличия ключа
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return false;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return false;
            return MyRbMap.this.containsKey(key);
        }

        @Override
        public int size() {
            // Подсчет элементов в пределах границ
            int count = 0;
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                count++;
                current = findNext(current);
            }
            return count;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public void clear() {
            // Удаление всех элементов в пределах границ
            ArrayList<Integer> keysToRemove = new ArrayList<>();
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                keysToRemove.add(current.key);
                current = findNext(current);
            }
            for (Integer key : keysToRemove) {
                MyRbMap.this.remove(key);
            }
        }

        @Override
        public String remove(Object key) {
            // Проверка границ перед удалением
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return null;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return null;
            return MyRbMap.this.remove(key);
        }

        @Override
        public boolean containsValue(Object value) {
            // Поиск значения в пределах границ
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                if (current.value.equals(value)) return true;
                current = findNext(current);
            }
            return false;
        }

        @Override
        public Integer firstKey() {
            // Первый ключ в пределах границ
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                return current.key;
            }
            return null;
        }

        @Override
        public Integer lastKey() {
            // Последний ключ в пределах границ
            Node current = findMax(root);
            while (current != null) {
                if (toKey != null && current.key.compareTo(toKey) >= 0) {
                    current = findPrev(current);
                    continue;
                }
                if (fromKey != null && current.key.compareTo(fromKey) < 0) break;
                return current.key;
            }
            return null;
        }

        @Override
        public SortedMap<Integer, String> headMap(Integer toKey) {
            return new SubMap(fromKey, toKey);
        }

        @Override
        public SortedMap<Integer, String> tailMap(Integer fromKey) {
            return new SubMap(fromKey, toKey);
        }

        @Override
        public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            return new SubMap(fromKey, toKey);
        }

        @Override
        public Comparator<? super Integer> comparator() {
            return null;
        }

        @Override
        public Set<Integer> keySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> values() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Map.Entry<Integer, String>> entrySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends String> m) {
            for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        // Найти предыдущий узел в порядке убывания
        private Node findPrev(Node n) {
            if (n == null) return null;
            if (n.left != null) return findMax(n.left); // Максимум в левом поддереве
            Node p = n.parent;
            // Поднимаемся до первого узла, который является правым потомком
            while (p != null && n == p.left) {
                n = p;
                p = p.parent;
            }
            return p;
        }
    }
}