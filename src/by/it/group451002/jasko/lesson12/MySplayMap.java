package by.it.group451002.jasko.lesson12;

import java.util.NavigableMap;
import java.util.Comparator;
import java.util.Set;
import java.util.Map;
import java.util.Collection;

public class MySplayMap implements NavigableMap<Integer, String> {
    private Node root;  // Корень splay-дерева
    private int size;   // Количество элементов в дереве

    // Узел splay-дерева
    private static class Node {
        Integer key;    // Ключ узла
        String value;   // Значение узла
        Node left;      // Левый потомок
        Node right;     // Правый потомок
        Node parent;    // Родительский узел

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    // Вставка узла в дерево с последующим splay
    private void insertNode(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return;
        }

        Node current = root;
        while (true) {
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new Node(key, value, current);
                    size++;
                    splay(current.left); // Поднимаем новый узел наверх
                    return;
                }
                current = current.left;
            } else if (key > current.key) {
                if (current.right == null) {
                    current.right = new Node(key, value, current);
                    size++;
                    splay(current.right); // Поднимаем новый узел наверх
                    return;
                }
                current = current.right;
            } else {
                String old = current.value;
                current.value = value; // Обновление существующего ключа
                splay(current); // Поднимаем узел наверх
                return;
            }
        }
    }

    // Splay операция - поднятие узла n в корень
    private void splay(Node n) {
        while (n.parent != null) {
            Node p = n.parent; // Родитель
            Node g = p.parent;
            if (g == null) {
                // zig: родитель - корень
                if (n == p.left) rotateRight(p); // n слева - правый поворот
                else rotateLeft(p); // n справа - правый поворот
            } else if (n == p.left && p == g.left) {
                // zig-zig: левый-левый случай
                rotateRight(g);
                rotateRight(p);
            } else if (n == p.right && p == g.right) {
                // zig-zig: правый-правый случай
                rotateLeft(g);
                rotateLeft(p);
            } else if (n == p.left && p == g.right) {
                // zig-zag: левый-правый случай
                rotateRight(p);
                rotateLeft(g);
            } else {
                // zig-zag: правый-левый случай
                rotateLeft(p);
                rotateRight(g);
            }
        }
        root = n; // n теперь корень
    }

    // Поиск узла по ключу с последующим splay
    private Node find(Integer key) {
        Node current = root;
        while (current != null) {
            if (key < current.key) current = current.left;
            else if (key > current.key) current = current.right;
            else break;
        }
        if (current != null) splay(current); // Поднимаем найденный узел наверх
        return current;
    }

    // Правый поворот вокруг узла n
    private void rotateRight(Node n) {
        Node p = n.parent;
        Node T = n.left;
        if (T != null) {
            n.left = T.right;
            if (T.right != null) T.right.parent = n;
            T.right = n;
        }
        n.parent = T;
        if (p != null) {
            if (n == p.left) p.left = T;
            else p.right = T;
        }
        if (T != null) T.parent = p;
        if (root == n) root = T;
    }

    // Левый поворот вокруг узла n
    private void rotateLeft(Node n) {
        Node p = n.parent;
        Node T = n.right;
        if (T != null) {
            n.right = T.left;
            if (T.left != null) T.left.parent = n;
            T.left = n;
        }
        n.parent = T;
        if (p != null) {
            if (n == p.left) p.left = T;
            else p.right = T;
        }
        if (T != null) T.parent = p;
        if (root == n) root = T;
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

    // Найти следующий узел в порядке возрастания
    private Node findNext(Node n) {
        if (n == null) return null;
        if (n.right != null) return findMin(n.right); // Минимум в правом поддереве
        Node p = n.parent;
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }

    // Найти предыдущий узел в порядке убывания
    private Node findPrev(Node n) {
        if (n == null) return null;
        if (n.left != null) return findMax(n.left); // Максимум в левом поддереве
        Node p = n.parent;
        while (p != null && n == p.left) {
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
        insertNode(key, value);
        return old;
    }

    @Override
    public String remove(Object key) {
        Node n = find((Integer) key);
        if (n == null) return null;
        String old = n.value;

        // Удаление узла с splay
        if (n.left == null) {
            // Нет левого потомка - поднимаем правый
            root = n.right;
            if (root != null) root.parent = null;
        } else if (n.right == null) {
            // Нет правого потомка - поднимаем левый
            root = n.left;
            root.parent = null;
        } else {
            // Есть оба потомка - находим преемника
            Node min = findMin(n.right);
            n.key = min.key;
            n.value = min.value;
            // Удаляем преемника
            if (min.parent.left == min) {
                min.parent.left = min.right;
            } else {
                min.parent.right = min.right;
            }
            if (min.right != null) min.right.parent = min.parent;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node n = find((Integer) key);
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
    public Integer lowerKey(Integer key) {
        // Наибольший ключ строго меньше заданного
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (key > current.key) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        // Наибольший ключ меньше или равный заданному
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (key >= current.key) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        // Наименьший ключ больше или равный заданному
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (key <= current.key) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        // Наименьший ключ строго больше заданного
        Node current = root;
        Integer result = null;
        while (current != null) {
            if (key < current.key) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return new SubMap(null, toKey);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return new SubMap(fromKey, null);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return new SubMap(fromKey, toKey);
    }

    // Остальные методы NavigableMap - необязательные
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
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
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    // Внутренний класс для представления подкарты
    private class SubMap implements NavigableMap<Integer, String> {
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
            return MySplayMap.this.put(key, value);
        }

        @Override
        public String get(Object key) {
            // Проверка границ перед поиском
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return null;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return null;
            return MySplayMap.this.get(key);
        }

        @Override
        public boolean containsKey(Object key) {
            // Проверка границ перед проверкой наличия ключа
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return false;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return false;
            return MySplayMap.this.containsKey(key);
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
            java.util.List<Integer> keysToRemove = new java.util.ArrayList<>();
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
                MySplayMap.this.remove(key);
            }
        }

        @Override
        public String remove(Object key) {
            // Проверка границ перед удалением
            if (fromKey != null && ((Integer) key).compareTo(fromKey) < 0) return null;
            if (toKey != null && ((Integer) key).compareTo(toKey) >= 0) return null;
            return MySplayMap.this.remove(key);
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
        public Integer lowerKey(Integer key) {
            // Наибольший ключ строго меньше заданного в пределах границ
            Integer result = null;
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                if (current.key.compareTo(key) < 0) {
                    result = current.key;
                }
                current = findNext(current);
            }
            return result;
        }

        @Override
        public Integer floorKey(Integer key) {
            // Наибольший ключ меньше или равный заданному в пределах границ
            Integer result = null;
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                if (current.key.compareTo(key) <= 0) {
                    result = current.key;
                }
                current = findNext(current);
            }
            return result;
        }

        @Override
        public Integer ceilingKey(Integer key) {
            // Наименьший ключ больше или равный заданному в пределах границ
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                if (current.key.compareTo(key) >= 0) return current.key;
                current = findNext(current);
            }
            return null;
        }

        @Override
        public Integer higherKey(Integer key) {
            // Наименьший ключ строго больше заданного в пределах границ
            Node current = findMin(root);
            while (current != null) {
                if (fromKey != null && current.key.compareTo(fromKey) < 0) {
                    current = findNext(current);
                    continue;
                }
                if (toKey != null && current.key.compareTo(toKey) >= 0) break;
                if (current.key.compareTo(key) > 0) return current.key;
                current = findNext(current);
            }
            return null;
        }

        @Override
        public NavigableMap<Integer, String> headMap(Integer toKey) {
            return new SubMap(fromKey, toKey);
        }

        @Override
        public NavigableMap<Integer, String> tailMap(Integer fromKey) {
            return new SubMap(fromKey, toKey);
        }

        @Override
        public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
            return new SubMap(fromKey, toKey);
        }

        // Остальные методы - необязательные
        @Override
        public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
            throw new UnsupportedOperationException();
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
        public java.util.NavigableSet<Integer> navigableKeySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.NavigableSet<Integer> descendingKeySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> firstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> lastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NavigableMap<Integer, String> descendingMap() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> lowerEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> floorEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> ceilingEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map.Entry<Integer, String> higherEntry(Integer key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends String> m) {
            throw new UnsupportedOperationException();
        }
    }
}