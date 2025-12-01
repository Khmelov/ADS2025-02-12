package by.it.group410901.abakumov.lesson12;
import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private class Node {
        Integer key; // ключ узла
        String value; // значение узла
        Node left, right, parent; // ссылки на детей и родителя
        boolean red; // цвет узла: true — красный, false — черный

        Node(Integer key, String value, boolean red, Node parent) { this.key = key; this.value = value; this.red = red; this.parent = parent; } // конструктор узла
    }

    private Node root; // корень дерева
    private int size = 0; // количество элементов

    // -------------------- Вспомогательные операции --------------------

    private boolean isRed(Node n) { return n != null && n.red; } // проверка: красный ли узел
    private void setRed(Node n) { if (n != null) n.red = true; } // пометить красным
    private void setBlack(Node n) { if (n != null) n.red = false; } // пометить черным

    private void rotateLeft(Node p) { // левый поворот вокруг p
        Node r = p.right; // правый ребёнок p
        p.right = r.left; // переставляем левое поддерево r в правую ветвь p
        if (r.left != null) r.left.parent = p; // обновляем родителя у перемещённого поддерева
        r.parent = p.parent; // r встаёт на место p в дереве
        if (p.parent == null) root = r; // если p был корнем — r теперь корень
        else if (p.parent.left == p) p.parent.left = r; // иначе корректируем ссылку родителя
        else p.parent.right = r; // аналогично для правого случая
        r.left = p; // p становится левым ребёнком r
        p.parent = r; // обновляем родителя p
    }

    private void rotateRight(Node p) { // правый поворот вокруг p
        Node l = p.left; // левый ребёнок p
        p.left = l.right; // переставляем правое поддерево l в левую ветвь p
        if (l.right != null) l.right.parent = p; // обновляем родителя у перемещённого поддерева
        l.parent = p.parent; // l встаёт на место p в дереве
        if (p.parent == null) root = l; // если p был корнем — l теперь корень
        else if (p.parent.right == p) p.parent.right = l; // иначе корректируем ссылку родителя
        else p.parent.left = l; // аналогично для левого случая
        l.right = p; // p становится правым ребёнком l
        p.parent = l; // обновляем родителя p
    }

    private Node minimum(Node n) { // найти минимальный (левый) узел в поддереве n
        while (n.left != null) n = n.left; // спускаемся влево до конца
        return n;
    }

    private Node successor(Node t) { // найти следующего (succ) узла по ключу (in-order)
        if (t == null) return null; // если нет узла — нет и преемника
        if (t.right != null) return minimum(t.right); // минимальный в правом поддереве
        Node p = t.parent;
        Node ch = t;
        while (p != null && ch == p.right) { ch = p; p = p.parent; } // поднимаемся, пока мы правый ребёнок
        return p;
    }

    // -------------------- Основные публичные методы --------------------

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException(); // не поддерживаем null-ключи
        if (root == null) { // если дерево пустое
            root = new Node(key, value, false, null); // корень — всегда черный
            size++; // увеличиваем размер
            return null;
        }

        Node parent = null; // будущий родитель для нового узла
        Node current = root; // начинаем с корня
        int cmp = 0;

        while (current != null) { // ищем позицию для вставки
            parent = current;
            cmp = key.compareTo(current.key); // сравнение ключей
            if (cmp < 0) current = current.left; // идём влево
            else if (cmp > 0) current = current.right; // идём вправо
            else { // ключ уже есть — заменяем значение
                String old = current.value; // сохраняем старое значение
                current.value = value; // заменяем
                return old; // возвращаем старое значение
            }
        }

        Node newNode = new Node(key, value, true, parent); // новый узел — красный по умолчанию
        if (cmp < 0) parent.left = newNode; else parent.right = newNode; // прикрепляем к родителю
        fixAfterInsertion(newNode); // балансируем дерево после вставки
        size++; // обновляем размер
        return null;
    }

    private void fixAfterInsertion(Node x) { // восстановление свойств красно-чёрного дерева после вставки
        setRed(x); // вставленный узел помечаем красным (в put уже так, но для явности)
        while (x != null && x != root && isRed(x.parent)) { // пока родитель красный и не дошли до корня
            if (x.parent == x.parent.parent.left) { // если родитель — левый ребёнок дедушки
                Node y = x.parent.parent.right; // y — "дядя"
                if (isRed(y)) { // случай 1: дядя красный — перекраска
                    setBlack(x.parent); // родитель -> черный
                    setBlack(y); // дядя -> черный
                    setRed(x.parent.parent); // дедушка -> красный
                    x = x.parent.parent; // продолжаем проверять вверх
                } else { // дядя черный
                    if (x == x.parent.right) { // случай 2: мы — правый ребёнок
                        x = x.parent; // поворот подготавливающий к случаю 3
                        rotateLeft(x); // левый поворот вокруг родителя
                    }
                    // случай 3:
                    setBlack(x.parent); // родитель -> черный
                    setRed(x.parent.parent); // дедушка -> красный
                    rotateRight(x.parent.parent); // правый поворот вокруг дедушки
                }
            } else { // симметричный случай: родитель — правый ребёнок дедушки
                Node y = x.parent.parent.left; // y — "дядя"
                if (isRed(y)) { // случай 1 симметричный
                    setBlack(x.parent); // родитель -> черный
                    setBlack(y); // дядя -> черный
                    setRed(x.parent.parent); // дедушка -> красный
                    x = x.parent.parent; // продолжаем вверх
                } else { // дядя черный
                    if (x == x.parent.left) { // случай 2 симметричный
                        x = x.parent; // подготовка
                        rotateRight(x); // правый поворот вокруг родителя
                    }
                    // случай 3 симметричный:
                    setBlack(x.parent); // родитель -> черный
                    setRed(x.parent.parent); // дедушка -> красный
                    rotateLeft(x.parent.parent); // левый поворот вокруг дедушки
                }
            }
        }
        setBlack(root); // корень всегда черный
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException(); // не поддерживаем null-ключи
        Node n = getNode((Integer) key); // ищем узел
        return n == null ? null : n.value; // возвращаем значение или null
    }

    private Node getNode(Integer key) { // поиск узла по ключу
        Node p = root; // начинаем с корня
        while (p != null) {
            int cmp = key.compareTo(p.key); // сравнение ключей
            if (cmp < 0) p = p.left; // идём влево
            else if (cmp > 0) p = p.right; // идём вправо
            else return p; // нашли
        }
        return null; // не нашли
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException(); // не поддерживаем null-ключи
        Node node = getNode((Integer) key); // ищем узел
        if (node == null) return null; // если нет — возвращаем null
        String old = node.value; // сохраняем старое значение
        deleteNode(node); // удаляем узел и восстанавливаем свойства дерева
        size--; // уменьшаем размер
        return old; // возвращаем старое значение
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private void deleteNode(Node p) { // удаление узла p из дерева
        if (p.left != null && p.right != null) { // если два сына
            Node s = successor(p); // находим преемника (минимум в правом поддереве)
            p.key = s.key; // копируем ключ преемника в p
            p.value = s.value; // копируем значение
            p = s; // теперь будем удалять s (у него не может быть двух детей)
        }

        Node replacement = (p.left != null) ? p.left : p.right; // один ребёнок или null

        if (replacement != null) { // если есть один ребёнок — заменяем p на replacement
            replacement.parent = p.parent; // связываем replacement с родителем p
            if (p.parent == null) root = replacement; // если p был корнем
            else if (p == p.parent.left) p.parent.left = replacement; // иначе ставим влево
            else p.parent.right = replacement; // или вправо
            p.left = p.right = p.parent = null; // очищаем ссылки p
            if (!p.red) fixAfterDeletion(replacement); // если удаляемый был чёрным — нужно восстановление
        } else if (p.parent == null) { // p — лист и корень (единственный узел)
            root = null; // дерево стало пустым
        } else { // p — лист (без детей) и не корень
            if (!p.red) fixAfterDeletion(p); // если p был черным — восстановление с "двойной чёрностью"
            if (p.parent != null) { // удаляем p из родителя
                if (p == p.parent.left) p.parent.left = null; else p.parent.right = null;
                p.parent = null; // очищаем родителя
            }
        }
    }

    private void fixAfterDeletion(Node x) { // восстановление после удаления
        while (x != root && !isRed(x)) { // пока x не корень и x черный (или null рассматриваем как черный)
            if (x == x.parent.left) { // если x — левый ребёнок
                Node sib = x.parent.right; // sibling — правый брат
                if (isRed(sib)) { // 1) брат красный -> превращаем в случай где брат черный
                    setBlack(sib); // брат -> черный
                    setRed(x.parent); // родитель -> красный
                    rotateLeft(x.parent); // поворот влево вокруг родителя
                    sib = x.parent.right; // обновляем ссылку на брата
                }
                if (!isRed(sib.left) && !isRed(sib.right)) { // 2) оба сына брата чёрные
                    setRed(sib); // пометить брата красным
                    x = x.parent; // поднимаемся вверх, проблема может переместиться вверх
                } else {
                    if (!isRed(sib.right)) { // 3) правый сын брата черный, левый красный
                        setBlack(sib.left); // левый сын брата -> черный
                        setRed(sib); // брат -> красный
                        rotateRight(sib); // поворот вокруг брата вправо
                        sib = x.parent.right; // обновляем sib
                    }
                    // 4) правый сын брата красный
                    if (sib != null) { // защита от null, хотя sib не должен быть null здесь
                        sib.red = x.parent.red; // брат наследует цвет родителя
                        setBlack(x.parent); // родитель -> черный
                        setBlack(sib.right); // правый сын брата -> черный
                    }
                    rotateLeft(x.parent); // финальный левый поворот вокруг родителя
                    x = root; // выходим из цикла
                }
            } else { // симметричный блок: x — правый ребёнок
                Node sib = x.parent.left; // брат — левый
                if (isRed(sib)) { // 1) брат красный
                    setBlack(sib); // брат -> черный
                    setRed(x.parent); // родитель -> красный
                    rotateRight(x.parent); // правый поворот вокруг родителя
                    sib = x.parent.left; // обновляем sib
                }
                if (!isRed(sib.left) && !isRed(sib.right)) { // 2) оба сына брата чёрные
                    setRed(sib); // брат -> красный
                    x = x.parent; // поднимаемся вверх
                } else {
                    if (!isRed(sib.left)) { // 3) левый сын брата черный, правый красный
                        setBlack(sib.right); // правый сын брата -> черный
                        setRed(sib); // брат -> красный
                        rotateLeft(sib); // левый поворот вокруг брата
                        sib = x.parent.left; // обновляем sib
                    }
                    // 4) левый сын брата красный
                    if (sib != null) { // защита от null
                        sib.red = x.parent.red; // брат наследует цвет родителя
                        setBlack(x.parent); // родитель -> черный
                        setBlack(sib.left); // левый сын брата -> черный
                    }
                    rotateRight(x.parent); // финальный правый поворот вокруг родителя
                    x = root; // выходим
                }
            }
        }
        setBlack(x); // помечаем x черным (включая случай x == null — проверяем в setBlack)
    }

    @Override
    public boolean containsKey(Object key) { return getNode((Integer) key) != null; } // наличие ключа

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) // если передали не строку — сразу false
            return false;
        return containsValueRecursive(root, (String) value); // иначе продолжаем обычную проверку
    }

    private boolean containsValueRecursive(Node n, String value) { // обход для поиска значения
        if (n == null) return false; // если узла нет
        if (value.equals(n.value)) return true; // нашли значение
        return containsValueRecursive(n.left, value) || containsValueRecursive(n.right, value); // ищем в поддеревьях
    }

    @Override
    public int size() { return size; } // возвращаем размер

    @Override
    public void clear() { root = null; size = 0; } // очищаем дерево

    @Override
    public boolean isEmpty() { return size == 0; } // проверка пустоты

    // -------------------- Методы диапазонов --------------------

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) { // ключи < toKey
        MyRbMap map = new MyRbMap(); // создаём новую карту
        copyRange(root, map, null, toKey); // копируем нужный диапазон
        return map; // возвращаем подмапу
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { // ключи >= fromKey
        MyRbMap map = new MyRbMap(); // новая карта
        copyRange(root, map, fromKey, null); // копируем нужный диапазон
        return map;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { // [fromKey, toKey)
        MyRbMap map = new MyRbMap(); // новая карта
        copyRange(root, map, fromKey, toKey); // копируем элементы в диапазоне
        return map;
    }

    private void copyRange(Node node, MyRbMap map, Integer fromKey, Integer toKey) { // in-order копирование заданного диапазона
        if (node == null) return; // нет узла — выходим
        if (fromKey == null || node.key.compareTo(fromKey) >= 0) copyRange(node.left, map, fromKey, toKey); // если левое поддерево может содержать нужные ключи
        if ((fromKey == null || node.key.compareTo(fromKey) >= 0) && (toKey == null || node.key.compareTo(toKey) < 0)) map.put(node.key, node.value); // если ключ в диапазоне — добавляем
        if (toKey == null || node.key.compareTo(toKey) < 0) copyRange(node.right, map, fromKey, toKey); // если правое поддерево может содержать нужные ключи
    }

    @Override
    public Integer firstKey() { // минимальный ключ
        if (root == null) throw new NoSuchElementException(); // если пусто — исключение
        return minimum(root).key; // левый самый элемент
    }

    @Override
    public Integer lastKey() { // максимальный ключ
        if (root == null) throw new NoSuchElementException(); // если пусто — исключение
        Node n = root; while (n.right != null) n = n.right; return n.key; // правый самый
    }

    // -------------------- toString --------------------

    @Override
    public String toString() { // в порядке возрастания ключей, формат как у стандартных коллекций
        StringBuilder sb = new StringBuilder(); // строим строку
        sb.append("{"); // открывающая скобка
        inorderToString(root, sb); // in-order обход
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // удаляем последнюю ", "
        sb.append("}"); // закрывающая скобка
        return sb.toString(); // возвращаем строку
    }

    private void inorderToString(Node n, StringBuilder sb) {
        if (n == null) return; // если узла нет — выходим из рекурсии

        inorderToString(n.left, sb); // сначала обходим левое поддерево (все меньшие ключи)

        sb.append(n.key)              // добавляем ключ текущего узла
                .append("=")                // добавляем знак равенства
                .append(n.value)            // добавляем значение
                .append(", ");              // добавляем запятую и пробел для разделения элементов

        inorderToString(n.right, sb); // потом обходим правое поддерево (все большие ключи)
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }
}
