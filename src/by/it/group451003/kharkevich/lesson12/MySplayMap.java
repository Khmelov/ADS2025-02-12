package by.it.group451003.kharkevich.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    Node root; // Корень splay-дерева - начальная точка для всех операций
    int size;  // Счетчик количества элементов в дереве

    // Внутренний класс для узла splay-дерева
    static class Node {
        Integer key;        // Ключ узла - целое число для сравнения и сортировки
        String value;       // Значение узла - строка, связанная с ключом
        Node left, right, parent; // Ссылки на левого, правого потомков и родителя

        // Конструктор создает новый узел с заданными ключом и значением
        Node(Integer key, String value) {
            this.key = key;     // Устанавливаем ключ узла
            this.value = value; // Устанавливаем значение узла
        }
    }

    // Преобразует дерево в строку в формате {key1=value1, key2=value2, ...}
    @Override
    public String toString() {
        if (root == null)
            return "{}"; // Если дерево пустое, возвращаем пустые фигурные скобки
        StringBuilder sb = new StringBuilder().append("{"); // Создаем StringBuilder с открывающей скобкой
        inOrderTraversal(root, sb); // Запускаем обход дерева для заполнения данными
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        sb.append("}"); // Добавляем закрывающую скобку
        return sb.toString(); // Возвращаем готовую строку
    }

    // Вспомогательный метод для обхода дерева в порядке возрастания
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) { // Если текущий узел существует
            inOrderTraversal(node.left, sb);   // Рекурсивно обходим левое поддерево (меньшие ключи)
            sb.append(node.key).append("=").append(node.value).append(", "); // Добавляем текущий узел в строку
            inOrderTraversal(node.right, sb);  // Рекурсивно обходим правое поддерево (большие ключи)
        }
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size; // Просто возвращаем значение счетчика size
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0; // Дерево пусто, если размер равен нулю
    }

    // Проверяет, содержится ли указанный ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null; // Если get возвращает не null, ключ существует
    }

    // Проверяет, содержится ли указанное значение в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value); // Запускаем рекурсивный поиск значения по всему дереву
    }

    // Рекурсивный поиск значения в дереве
    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false; // Достигли конца ветки, значение не найдено
        }
        if (Objects.equals(value, node.value)) {
            return true; // Нашли узел с искомым значением
        }
        // Рекурсивно ищем в левом и/или правом поддереве
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Возвращает значение, связанное с указанным ключом
    @Override
    public String get(Object key) {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node found = searchKey((Integer) key, root); // Ищем узел с заданным ключом
        if (found != null) {
            root = splay(root, found.key); // Поднимаем найденный узел к корню (splay операция)
            return found.value; // Возвращаем значение найденного узла
        }
        return null; // Ключ не найден, возвращаем null
    }

    // Рекурсивный поиск узла по ключу в дереве
    private Node searchKey(Integer key, Node node) {
        if (node == null)
            return null; // Если дошли до null, ключ не найден
        int comparison = key.compareTo(node.key); // Сравниваем искомый ключ с ключом текущего узла
        if (comparison == 0)
            return node; // Ключи равны - нашли нужный узел

        // Рекурсивно ищем в левом или правом поддереве в зависимости от результата сравнения
        return searchKey(key, comparison < 0 ? node.left : node.right);
    }

    // Добавляет пару ключ-значение в дерево или обновляет значение существующего ключа
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value); // Если дерево пустое, создаем корень
            size++; // Увеличиваем счетчик элементов
            return null; // Возвращаем null (старого значения не было)
        }

        root = splay(root, key); // Поднимаем узел с близким ключом к корню
        int cmp = key.compareTo(root.key); // Сравниваем ключ с ключом корня

        if (cmp == 0) {
            // Ключ уже существует в корне
            String oldValue = root.value; // Сохраняем старое значение
            root.value = value; // Обновляем значение
            return oldValue; // Возвращаем старое значение
        } else if (cmp < 0) {
            // Ключ меньше корня - вставляем слева от корня
            Node newNode = new Node(key, value); // Создаем новый узел
            newNode.left = root.left; // Левое поддерево нового узла = левое поддерево корня
            newNode.right = root;     // Правый потомок нового узла = текущий корень
            if (root.left != null) root.left.parent = newNode; // Обновляем родителя у левого поддерева
            root.parent = newNode; // Обновляем родителя у корня
            root.left = null; // Обнуляем левое поддерево корня
            root = newNode; // Новый узел становится корнем
            size++; // Увеличиваем счетчик элементов
        } else {
            // Ключ больше корня - вставляем справа от корня
            Node newNode = new Node(key, value); // Создаем новый узел
            newNode.right = root.right; // Правое поддерево нового узла = правое поддерево корня
            newNode.left = root;        // Левый потомок нового узла = текущий корень
            if (root.right != null) root.right.parent = newNode; // Обновляем родителя у правого поддерева
            root.parent = newNode; // Обновляем родителя у корня
            root.right = null; // Обнуляем правое поддерево корня
            root = newNode; // Новый узел становится корнем
            size++; // Увеличиваем счетчик элементов
        }
        return null; // Возвращаем null (старого значения не было)
    }

    // Удаляет узел с заданным ключом из дерева
    @Override
    public String remove(Object key) {
        if (root == null) {
            return null; // Если дерево пустое, возвращаем null
        }

        root = splay(root, (Integer) key); // Поднимаем узел с близким ключом к корню
        int cmp = ((Integer) key).compareTo(root.key); // Сравниваем ключ с ключом корня
        if (cmp != 0) {
            return null; // Ключ не найден, возвращаем null
        }

        String removedValue = root.value; // Сохраняем значение удаляемого узла

        if (root.left == null) {
            // Левого поддерева нет - заменяем корень правым поддеревом
            root = root.right; // Правый потомок становится новым корнем
            if (root != null) {
                root.parent = null; // Обнуляем родителя у нового корня
            }
        } else {
            // Есть левое поддерево
            Node newRoot = root.right; // Начинаем с правого поддерева
            if (newRoot != null) {
                newRoot = splay(newRoot, (Integer) key); // Splay для правого поддерева
                newRoot.left = root.left; // Левое поддерево нового корня = левое поддерево старого корня
                newRoot.left.parent = newRoot; // Обновляем родителя у левого поддерева
                root = newRoot; // Новый корень
            } else {
                root = root.left; // Левое поддерево становится корнем
                root.parent = null; // Обнуляем родителя у нового корня
            }
        }

        size--; // Уменьшаем счетчик элементов
        return removedValue; // Возвращаем значение удаленного узла
    }

    // Основная splay операция - поднимает узел с заданным ключом к корню
    private Node splay(Node node, Integer key) {
        if (node == null) {
            return null; // Пустое поддерево
        }

        int cmp = key.compareTo(node.key); // Сравниваем ключ с ключом текущего узла
        if (cmp < 0) {
            // Ключ меньше текущего узла - ищем в левом поддереве
            if (node.left == null) {
                return node; // Левое поддерево пустое - возвращаем текущий узел
            }
            int cmp2 = key.compareTo(node.left.key); // Сравниваем ключ с ключом левого потомка
            if (cmp2 < 0) {
                // Zig-zig случай (левый-левый)
                node.left.left = splay(node.left.left, key); // Рекурсивно splay для левого-левого потомка
                node = rotateRight(node); // Правый поворот вокруг текущего узла
            } else if (cmp2 > 0) {
                // Zig-zag случай (левый-правый)
                node.left.right = splay(node.left.right, key); // Рекурсивно splay для левого-правого потомка
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left); // Левый поворот вокруг левого потомка
                }
            }
            return (node.left == null) ? node : rotateRight(node); // Правый поворот если левый потомок существует
        } else if (cmp > 0) {
            // Ключ больше текущего узла - ищем в правом поддереве
            if (node.right == null) {
                return node; // Правое поддерево пустое - возвращаем текущий узел
            }
            int cmp2 = key.compareTo(node.right.key); // Сравниваем ключ с ключом правого потомка
            if (cmp2 < 0) {
                // Zig-zag случай (правый-левый)
                node.right.left = splay(node.right.left, key); // Рекурсивно splay для правого-левого потомка
                if (node.right.left != null) {
                    node.right = rotateRight(node.right); // Правый поворот вокруг правого потомка
                }
            } else if (cmp2 > 0) {
                // Zig-zig случай (правый-правый)
                node.right.right = splay(node.right.right, key); // Рекурсивно splay для правого-правого потомка
                node = rotateLeft(node); // Левый поворот вокруг текущего узла
            }
            return (node.right == null) ? node : rotateLeft(node); // Левый поворот если правый потомок существует
        } else {
            // Нашли узел с искомым ключом
            return node; // Возвращаем текущий узел
        }
    }

    // Выполняет правый поворот вокруг указанного узла
    private Node rotateRight(Node node) {
        Node leftChild = node.left; // Левый потомок станет новым корнем
        node.left = leftChild.right; // Правое поддерево левого потомка становится левым поддеревом узла
        if (leftChild.right != null) {
            leftChild.right.parent = node; // Обновляем родителя у перемещаемого поддерева
        }
        leftChild.right = node; // Узел становится правым потомком нового корня
        leftChild.parent = node.parent; // Обновляем родителя у нового корня
        node.parent = leftChild; // Обновляем родителя у узла
        return leftChild; // Возвращаем новый корень поддерева
    }

    // Выполняет левый поворот вокруг указанного узла
    private Node rotateLeft(Node node) {
        Node rightChild = node.right; // Правый потомок станет новым корнем
        node.right = rightChild.left; // Левое поддерево правого потомка становится правым поддеревом узла
        if (rightChild.left != null) {
            rightChild.left.parent = node; // Обновляем родителя у перемещаемого поддерева
        }
        rightChild.left = node; // Узел становится левым потомком нового корня
        rightChild.parent = node.parent; // Обновляем родителя у нового корня
        node.parent = rightChild; // Обновляем родителя у узла
        return rightChild; // Возвращаем новый корень поддерева
    }

    // Полностью очищает дерево
    @Override
    public void clear() {
        root = null; // Обнуляем корень (вся структура дерева становится недоступной)
        size = 0;    // Сбрасываем счетчик размера в ноль
    }

    // Возвращает наибольший ключ, строго меньший указанного ключа
    @Override
    public Integer lowerKey(Integer key) {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = lowerKeyNode(key, root, null); // Находим узел с наибольшим ключом меньше заданного
        return (node != null) ? node.key : null; // Возвращаем ключ или null если не найден
    }

    // Рекурсивно находит узел с наибольшим ключом меньше заданного
    private Node lowerKeyNode(Integer key, Node node, Node best) {
        if (node == null) return best; // Конец ветки - возвращаем лучший найденный вариант
        int cmp = key.compareTo(node.key); // Сравниваем ключи
        if (cmp <= 0) {
            // Текущий ключ больше или равен искомому - ищем в левом поддереве
            return lowerKeyNode(key, node.left, best);
        } else {
            // Текущий ключ меньше искомого - обновляем лучший вариант и ищем в правом поддереве
            return lowerKeyNode(key, node.right, node);
        }
    }

    // Возвращает наибольший ключ, меньший или равный указанному ключу
    @Override
    public Integer floorKey(Integer key) {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = floorKeyNode(key, root, null); // Находим узел с наибольшим ключом меньше или равным заданному
        return (node != null) ? node.key : null; // Возвращаем ключ или null если не найден
    }

    // Рекурсивно находит узел с наибольшим ключом меньше или равным заданному
    private Node floorKeyNode(Integer key, Node node, Node best) {
        if (node == null) return best; // Конец ветки - возвращаем лучший найденный вариант
        int cmp = key.compareTo(node.key); // Сравниваем ключи
        if (cmp == 0) {
            return node; // Нашли точное совпадение
        } else if (cmp < 0) {
            // Текущий ключ больше искомого - ищем в левом поддереве
            return floorKeyNode(key, node.left, best);
        } else {
            // Текущий ключ меньше искомого - обновляем лучший вариант и ищем в правом поддереве
            return floorKeyNode(key, node.right, node);
        }
    }

    // Возвращает наименьший ключ, больший или равный указанному ключу
    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = ceilingKeyNode(key, root, null); // Находим узел с наименьшим ключом больше или равным заданному
        return (node != null) ? node.key : null; // Возвращаем ключ или null если не найден
    }

    // Рекурсивно находит узел с наименьшим ключом больше или равным заданному
    private Node ceilingKeyNode(Integer key, Node node, Node best) {
        if (node == null) return best; // Конец ветки - возвращаем лучший найденный вариант
        int cmp = key.compareTo(node.key); // Сравниваем ключи
        if (cmp == 0) {
            return node; // Нашли точное совпадение
        } else if (cmp > 0) {
            // Текущий ключ меньше искомого - ищем в правом поддереве
            return ceilingKeyNode(key, node.right, best);
        } else {
            // Текущий ключ больше искомого - обновляем лучший вариант и ищем в левом поддереве
            return ceilingKeyNode(key, node.left, node);
        }
    }

    // Возвращает наименьший ключ, строго больший указанного ключа
    @Override
    public Integer higherKey(Integer key) {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = higherKeyNode(key, root, null); // Находим узел с наименьшим ключом больше заданного
        return (node != null) ? node.key : null; // Возвращаем ключ или null если не найден
    }

    // Рекурсивно находит узел с наименьшим ключом больше заданного
    private Node higherKeyNode(Integer key, Node node, Node best) {
        if (node == null) return best; // Конец ветки - возвращаем лучший найденный вариант
        int cmp = key.compareTo(node.key); // Сравниваем ключи
        if (cmp >= 0) {
            // Текущий ключ меньше или равен искомому - ищем в правом поддереве
            return higherKeyNode(key, node.right, best);
        } else {
            // Текущий ключ больше искомого - обновляем лучший вариант и ищем в левом поддереве
            return higherKeyNode(key, node.left, node);
        }
    }

    // Возвращает подкарту с ключами меньше указанного ключа
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap(); // Создаем новую карту для результата
        headMap(root, toKey, subMap); // Заполняем ее подходящими ключами
        return subMap; // Возвращаем подкарту
    }

    // Рекурсивно заполняет подкарту ключами меньше toKey
    private void headMap(Node node, Integer toKey, MySplayMap subMap) {
        if (node == null) return; // Конец ветки
        headMap(node.left, toKey, subMap); // Обходим левое поддерево
        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value); // Добавляем подходящий ключ в подкарту
            headMap(node.right, toKey, subMap); // Обходим правое поддерево
        }
    }

    // Возвращает подкарту с ключами больше или равными указанному ключу
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap(); // Создаем новую карту для результата
        tailMap(root, fromKey, subMap); // Заполняем ее подходящими ключами
        return subMap; // Возвращаем подкарту
    }

    // Рекурсивно заполняет подкарту ключами больше или равными fromKey
    private void tailMap(Node node, Integer fromKey, MySplayMap subMap) {
        if (node == null) return; // Конец ветки
        if (node.key.compareTo(fromKey) >= 0) {
            tailMap(node.left, fromKey, subMap); // Обходим левое поддерево
            subMap.put(node.key, node.value); // Добавляем подходящий ключ в подкарту
        }
        tailMap(node.right, fromKey, subMap); // Обходим правое поддерево
    }

    // Возвращает первый (наименьший) ключ в дереве
    @Override
    public Integer firstKey() {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = root;
        while (node.left != null) {
            node = node.left; // Идем влево пока возможно
        }
        return node.key; // Возвращаем ключ самого левого узла
    }

    // Возвращает последний (наибольший) ключ в дереве
    @Override
    public Integer lastKey() {
        if (root == null) return null; // Если дерево пустое, возвращаем null
        Node node = root;
        while (node.right != null) {
            node = node.right; // Идем вправо пока возможно
        }
        return node.key; // Возвращаем ключ самого правого узла
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используется естественный порядок сортировки ключей
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException(); // Выбрасываем исключение при попытке вызова
    }
}