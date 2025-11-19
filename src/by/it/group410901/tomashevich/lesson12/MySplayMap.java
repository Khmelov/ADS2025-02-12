package by.it.group410901.tomashevich.lesson12;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key; // Хранит ключ узла
        String info; // Хранит значение, связанное с ключом
        Node left, right, parent; // Ссылки на левого и правого потомка, а также на родителя

        Node(Integer key, String info) { // Конструктор класса Node
            this.key = key; // Инициализирует ключ узла
            this.info = info; // Инициализирует значение узла
        }
    }

    private Node root; // Корневой узел сплей-дерева
    private int currentSize = 0; // Хранит текущее количество узлов в дереве

    @Override
    public Comparator<? super Integer> comparator() { // Возвращает компаратор, используемый для упорядочивания ключей
        return null; // Возвращает null, что указывает на использование естественного порядка для ключей типа Integer
    }

    @Override
    public Set<Integer> keySet() { // Возвращает множество всех ключей в дереве
        Set<Integer> keys = new TreeSet<>(); // Создаёт новое множество для хранения ключей
        inOrderTraversal(root, keys); // Выполняет обход дерева в порядке возрастания для сбора ключей
        return keys; // Возвращает множество ключей
    }

    @Override
    public Collection<String> values() { // Возвращает коллекцию всех значений в дереве
        Collection<String> arr = new ArrayList<>(); // Создаёт новую коллекцию для хранения значений
        inOrderTraversalValues(root, arr); // Выполняет обход дерева для сбора значений
        return arr; // Возвращает коллекцию значений
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) { // Добавляет все пары ключ-значение из переданной карты
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) { // Перебирает все записи в переданной карте
            put(entry.getKey(), entry.getValue()); // Добавляет каждую пару ключ-значение в дерево
        }
    }

    private void inOrderTraversal(Node node, Set<Integer> keys) { // Выполняет симметричный обход дерева для сбора ключей
        if (node != null) { // Проверяет, существует ли узел
            inOrderTraversal(node.left, keys); // Рекурсивно обходит левое поддерево
            keys.add(node.key); // Добавляет ключ текущего узла в множество
            inOrderTraversal(node.right, keys); // Рекурсивно обходит правое поддерево
        }
    }

    private void inOrderTraversalValues(Node node, Collection<String> values) { // Выполняет симметричный обход дерева для сбора значений
        if (node != null) { // Проверяет, существует ли узел
            inOrderTraversalValues(node.left, values); // Рекурсивно обходит левое поддерево
            values.add(node.info); // Добавляет значение текущего узла в коллекцию
            inOrderTraversalValues(node.right, values); // Рекурсивно обходит правое поддерево
        }
    }

    @Override
    public boolean containsValue(Object obj) { // Проверяет, содержит ли дерево указанное значение
        return containsValue(root, obj); // Вызывает рекурсивную функцию поиска значения, начиная с корня
    }

    private boolean containsValue(Node node, Object obj) { // Рекурсивно ищет значение в дереве
        if (node == null) { // Если узел пустой, возвращает false
            return false; // Значение не найдено
        }
        if (obj.equals(node.info)) { // Сравнивает переданное значение с значением текущего узла
            return true; // Возвращает true, если значение найдено
        }
        return containsValue(node.left, obj) || containsValue(node.right, obj); // Рекурсивно проверяет левое и правое поддерево
    }

    private void splay(Node node) { // Выполняет сплей-операцию для перемещения узла к корню
        while (node.parent != null) { // Продолжает, пока узел не станет корнем
            Node parent = node.parent; // Получает родителя текущего узла
            Node grandparent = parent.parent; // Получает деда текущего узла

            if (grandparent == null) { // Если деда нет, выполняется одиночное вращение
                if (node == parent.left) { // Если узел — левый потомок родителя
                    rotateRight(parent); // Выполняет правое вращение над родителем
                } else { // Если узел — правый потомок родителя
                    rotateLeft(parent); // Выполняет левое вращение над родителем
                }
            } else { // Если дед существует, выполняется двойное вращение
                if (node == parent.left && parent == grandparent.left) { // Зиг-зиг случай (левый-левый)
                    rotateRight(grandparent); // Правое вращение над дедом
                    rotateRight(parent); // Правое вращение над родителем
                } else if (node == parent.right && parent == grandparent.right) { // Зиг-зиг случай (правый-правый)
                    rotateLeft(grandparent); // Левое вращение над дедом
                    rotateLeft(parent); // Левое вращение над родителем
                } else if (node == parent.right && parent == grandparent.left) { // Зиг-заг случай (правый-левый)
                    rotateLeft(parent); // Левое вращение над родителем
                    rotateRight(grandparent); // Правое вращение над дедом
                } else { // Зиг-заг случай (левый-правый)
                    rotateRight(parent); // Правое вращение над родителем
                    rotateLeft(grandparent); // Левое вращение над дедом
                }
            }
        }
        root = node; // Устанавливает текущий узел как корень
    }

    private void rotateLeft(Node node) { // Выполняет левое вращение над указанным узлом
        Node rightChild = node.right; // Получает правого потомка узла
        node.right = rightChild.left; // Устанавливает левый потомок правого узла как правый потомок текущего узла
        if (rightChild.left != null) { // Если у правого потомка есть левый потомок
            rightChild.left.parent = node; // Устанавливает текущий узел как родителя для левого потомка правого узла
        }
        rightChild.parent = node.parent; // Устанавливает родителя правого потомка как родителя текущего узла
        if (node.parent == null) { // Если текущий узел — корень
            root = rightChild; // Устанавливает правого потомка как новый корень
        } else if (node == node.parent.left) { // Если текущий узел — левый потомок своего родителя
            node.parent.left = rightChild; // Устанавливает правого потомка как левого потомка родителя
        } else { // Если текущий узел — правый потомок своего родителя
            node.parent.right = rightChild; // Устанавливает правого потомка как правого потомка родителя
        }
        rightChild.left = node; // Устанавливает текущий узел как левого потомка правого узла
        node.parent = rightChild; // Устанавливает правого потомка как родителя текущего узла
    }

    private void rotateRight(Node node) { // Выполняет правое вращение над указанным узлом
        Node leftChild = node.left; // Получает левого потомка узла
        node.left = leftChild.right; // Устанавливает правый потомок левого узла как левый потомок текущего узла
        if (leftChild.right != null) { // Если у левого потомка есть правый потомок
            leftChild.right.parent = node; // Устанавливает текущий узел как родителя для правого потомка левого узла
        }
        leftChild.parent = node.parent; // Устанавливает родителя левого потомка как родителя текущего узла
        if (node.parent == null) { // Если текущий узел — корень
            root = leftChild; // Устанавливает левого потомка как новый корень
        } else if (node == node.parent.right) { // Если текущий узел — правый потомок своего родителя
            node.parent.right = leftChild; // Устанавливает левого потомка как правого потомка родителя
        } else { // Если текущий узел — левый потомок своего родителя
            node.parent.left = leftChild; // Устанавливает левого потомка как левого потомка родителя
        }
        leftChild.right = node; // Устанавливает текущий узел как правого потомка левого узла
        node.parent = leftChild; // Устанавливает левого потомка как родителя текущего узла
    }

    @Override
    public String put(Integer key, String info) { // Добавляет или обновляет пару ключ-значение в дереве
        if (root == null) { // Если дерево пустое
            root = new Node(key, info); // Создаёт новый корневой узел с указанным ключом и значением
            currentSize++; // Увеличивает счётчик узлов
            return null; // Возвращает null, так как предыдущего значения не было
        }

        Node node = root; // Начинает поиск с корня
        Node parent = null; // Хранит родителя текущего узла
        while (node != null) { // Ищет место для вставки нового узла
            parent = node; // Обновляет родителя
            if (key.compareTo(node.key) < 0) { // Если ключ меньше ключа текущего узла
                node = node.left; // Переходит к левому поддереву
            } else if (key.compareTo(node.key) > 0) { // Если ключ больше ключа текущего узла
                node = node.right; // Переходит к правому поддереву
            } else { // Если ключ уже существует
                String oldValue = node.info; // Сохраняет старое значение
                node.info = info; // Обновляет значение узла
                splay(node); // Выполняет сплей-операцию для перемещения узла к корню
                return oldValue; // Возвращает старое значение
            }
        }

        Node newNode = new Node(key, info); // Создаёт новый узел с указанным ключом и значением
        newNode.parent = parent; // Устанавливает родителя нового узла
        if (key.compareTo(parent.key) < 0) { // Если новый ключ меньше ключа родителя
            parent.left = newNode; // Устанавливает новый узел как левого потомка
        } else { // Если новый ключ больше ключа родителя
            parent.right = newNode; // Устанавливает новый узел как правого потомка
        }
        splay(newNode); // Выполняет сплей-операцию для перемещения нового узла к корню
        currentSize++; // Увеличивает счётчик узлов
        return null; // Возвращает null, так как нового узла не существовало
    }

    @Override
    public String remove(Object obj) { // Удаляет узел с указанным ключом
        Node node = findNode((Integer) obj); // Ищет узел с указанным ключом
        if (node == null) { // Если узел не найден
            return null; // Возвращает null
        }

        splay(node); // Выполняет сплей-операцию для перемещения узла к корню

        if (node.left == null) { // Если у узла нет левого поддерева
            root = node.right; // Устанавливает правое поддерево как новый корень
            if (root != null) { // Если правое поддерево существует
                root.parent = null; // Устанавливает null как родителя нового корня
            }
        } else { // Если у узла есть левое поддерево
            Node rightSubtree = node.right; // Сохраняет правое поддерево
            root = node.left; // Устанавливает левое поддерево как новый корень
            root.parent = null; // Устанавливает null как родителя нового корня
            Node maxLeft = findMax(root); // Находит максимальный узел в левом поддереве
            splay(maxLeft); // Выполняет сплей-операцию для перемещения максимального узла к корню
            maxLeft.right = rightSubtree; // Присоединяет правое поддерево к максимальному узлу
            if (rightSubtree != null) { // Если правое поддерево существует
                rightSubtree.parent = maxLeft; // Устанавливает максимальный узел как родителя правого поддерева
            }
        }

        currentSize--; // Уменьшает счётчик узлов
        return node.info; // Возвращает значение удалённого узла
    }

    private Node findNode(Integer key) { // Ищет узел с указанным ключом
        Node current = root; // Начинает поиск с корня
        while (current != null) { // Продолжает, пока узел существует
            if (key.compareTo(current.key) < 0) { // Если ключ меньше текущего
                current = current.left; // Переходит к левому поддереву
            } else if (key.compareTo(current.key) > 0) { // Если ключ больше текущего
                current = current.right; // Переходит к правому поддереву
            } else { // Если ключ совпадает
                return current; // Возвращает найденный узел
            }
        }
        return null; // Возвращает null, если узел не найден
    }

    private Node findMax(Node node) { // Находит узел с максимальным ключом в поддереве
        while (node.right != null) { // Продолжает, пока есть правый потомок
            node = node.right; // Переходит к правому потомку
        }
        return node; // Возвращает узел с максимальным ключом
    }

    @Override
    public String get(Object obj) { // Возвращает значение, связанное с указанным ключом
        Node node = findNode((Integer) obj); // Ищет узел с указанным ключом
        if (node != null) { // Если узел найден
            splay(node); // Выполняет сплей-операцию для перемещения узла к корню
            return node.info; // Возвращает значение узла
        }
        return null; // Возвращает null, если узел не найден
    }

    @Override
    public boolean containsKey(Object obj) { // Проверяет, содержит ли дерево указанный ключ
        return findNode((Integer) obj) != null; // Возвращает true, если узел с ключом найден
    }

    @Override
    public int size() { // Возвращает количество узлов в дереве
        return currentSize; // Возвращает текущее значение счётчика узлов
    }

    @Override
    public void clear() { // Очищает дерево
        root = null; // Устанавливает корень в null
        currentSize = 0; // Сбрасывает счётчик узлов
    }

    @Override
    public boolean isEmpty() { // Проверяет, пустое ли дерево
        return currentSize == 0; // Возвращает true, если счётчик узлов равен 0
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) { // Возвращает подкарту с ключами меньше указанного
        return headMap(toKey, false); // Вызывает метод с включением конечного ключа по умолчанию
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { // Возвращает подкарту с ключами меньше или равными (если inclusive) указанного
        MySplayMap result = new MySplayMap(); // Создаёт новое сплей-дерево для результата
        headMap(root, toKey, inclusive, result); // Заполняет подкарту рекурсивно
        return result; // Возвращает подкарту
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) { // Рекурсивно собирает подкарту
        if (node == null) { // Если узел пустой, завершает выполнение
            return;
        }
        if (node.key.compareTo(toKey) < 0 || (inclusive && node.key.equals(toKey))) { // Если ключ узла меньше или равен (при inclusive) toKey
            result.put(node.key, node.info); // Добавляет пару ключ-значение в результирующую карту
            headMap(node.right, toKey, inclusive, result); // Рекурсивно обходит правое поддерево
        }
        headMap(node.left, toKey, inclusive, result); // Рекурсивно обходит левое поддерево
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) { // Возвращает подкарту с ключами больше или равными указанного
        return tailMap(fromKey, true); // Вызывает метод с включением начального ключа по умолчанию
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { // Возвращает подкарту с ключами больше или равными (если inclusive) указанного
        MySplayMap result = new MySplayMap(); // Создаёт новое сплей-дерево для результата
        tailMap(root, fromKey, inclusive, result); // Заполняет подкарту рекурсивно
        return result; // Возвращает подкарту
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) { // Рекурсивно собирает подкарту
        if (node == null) { // Если узел пустой, завершает выполнение
            return;
        }
        if (node.key.compareTo(fromKey) > 0 || (inclusive && node.key.equals(fromKey))) { // Если ключ узла больше или равен (при inclusive) fromKey
            result.put(node.key, node.info); // Добавляет пару ключ-значение в результирующую карту
            tailMap(node.left, fromKey, inclusive, result); // Рекурсивно обходит левое поддерево
        }
        tailMap(node.right, fromKey, inclusive, result); // Рекурсивно обходит правое поддерево
    }

    @Override
    public Integer firstKey() { // Возвращает наименьший ключ в дереве
        if (root == null) { // Если дерево пустое
            throw new NoSuchElementException(); // Выбрасывает исключение
        }
        Node node = findMin(root); // Находит узел с минимальным ключом
        splay(node); // Выполняет сплей-операцию для перемещения узла к корню
        return node.key; // Возвращает минимальный ключ
    }

    @Override
    public Integer lastKey() { // Возвращает наибольший ключ в дереве
        if (root == null) { // Если дерево пустое
            throw new NoSuchElementException(); // Выбрасывает исключение
        }
        Node node = findMax(root); // Находит узел с максимальным ключом
        splay(node); // Выполняет сплей-операцию для перемещения узла к корню
        return node.key; // Возвращает максимальный ключ
    }

    private Node findMin(Node node) { // Находит узел с минимальным ключом в поддереве
        while (node.left != null) { // Продолжает, пока есть левый потомок
            node = node.left; // Переходит к левому потомку
        }
        return node; // Возвращает узел с минимальным ключом
    }

    @Override
    public Integer lowerKey(Integer key) { // Возвращает наибольший ключ, строго меньший указанного
        Node node = lowerNode(root, key); // Ищет подходящий узел
        if (node != null) { // Если узел найден
            splay(node); // Выполняет сплей-операцию для перемещения узла к корню
            return node.key; // Возвращает ключ узла
        }
        return null; // Возвращает null, если подходящий ключ не найден
    }

    private Node lowerNode(Node node, Integer key) { // Рекурсивно ищет узел с наибольшим ключом, строго меньшим указанного
        if (node == null) { // Если узел пустой
            return null; // Возвращает null
        }
        if (node.key.compareTo(key) >= 0) { // Если ключ узла больше или равен искомому
            return lowerNode(node.left, key); // Рекурсивно ищет в левом поддереве
        } else { // Если ключ узла меньше искомого
            Node right = lowerNode(node.right, key); // Рекурсивно ищет в правом поддереве
            return right != null ? right : node; // Возвращает найденный узел или текущий
        }
    }

    @Override
    public Integer floorKey(Integer key) { // Возвращает наибольший ключ, меньший или равный указанного
        Node node = floorNode(root, key); // Ищет подходящий узел
        if (node != null) { // Если узел найден
            splay(node); // Выполняет сплей-операцию для перемещения узла к корню
            return node.key; // Возвращает ключ узла
        }
        return null; // Возвращает null, если подходящий ключ не найден
    }

    private Node floorNode(Node node, Integer key) { // Рекурсивно ищет узел с наибольшим ключом, меньшим или равным указанного
        if (node == null) { // Если узел пустой
            return null; // Возвращает null
        }
        if (node.key.compareTo(key) == 0) { // Если ключ узла равен искомому
            return node; // Возвращает текущий узел
        }
        if (node.key.compareTo(key) > 0) { // Если ключ узла больше искомого
            return floorNode(node.left, key); // Рекурсивно ищет в левом поддереве
        } else { // Если ключ узла меньше искомого
            Node right = floorNode(node.right, key); // Рекурсивно ищет в правом поддереве
            return right != null ? right : node; // Возвращает найденный узел или текущий
        }
    }

    @Override
    public Integer ceilingKey(Integer key) { // Возвращает наименьший ключ, больший или равный указанного
        Node node = ceilingNode(root, key); // Ищет подходящий узел
        if (node != null) { // Если узел найден
            splay(node); // Выполняет сплей-операцию для перемещения узла к корню
            return node.key; // Возвращает ключ узла
        }
        return null; // Возвращает null, если подходящий ключ не найден
    }

    private Node ceilingNode(Node node, Integer key) { // Рекурсивно ищет узел с наименьшим ключом, большим или равным указанного
        if (node == null) { // Если узел пустой
            return null; // Возвращает null
        }
        if (node.key.compareTo(key) == 0) { // Если ключ узла равен искомому
            return node; // Возвращает текущий узел
        }
        if (node.key.compareTo(key) < 0) { // Если ключ узла меньше искомого
            return ceilingNode(node.right, key); // Рекурсивно ищет в правом поддереве
        } else { // Если ключ узла больше искомого
            Node left = ceilingNode(node.left, key); // Рекурсивно ищет в левом поддереве
            return left != null ? left : node; // Возвращает найденный узел или текущий
        }
    }

    @Override
    public Integer higherKey(Integer key) { // Возвращает наименьший ключ, строго больший указанного
        Node node = higherNode(root, key); // Ищет подходящий узел
        if (node != null) { // Если узел найден
            splay(node); // Выполняет сплей-операцию для перемещения узла к корню
            return node.key; // Возвращает ключ узла
        }
        return null; // Возвращает null, если подходящий ключ не найден
    }

    private Node higherNode(Node node, Integer key) { // Рекурсивно ищет узел с наименьшим ключом, строго большим указанного
        if (node == null) { // Если узел пустой
            return null; // Возвращает null
        }
        if (node.key.compareTo(key) <= 0) { // Если ключ узла меньше или равен искомому
            return higherNode(node.right, key); // Рекурсивно ищет в правом поддереве
        } else { // Если ключ узла больше искомого
            Node left = higherNode(node.left, key); // Рекурсивно ищет в левом поддереве
            return left != null ? left : node; // Возвращает найденный узел или текущий
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) { // Возвращает подкарту с ключами в диапазоне [fromKey, toKey)
        return subMap(fromKey, true, toKey, false); // Вызывает метод с включением начального ключа и исключением конечного
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { // Возвращает подкарту с ключами в диапазоне
        MySplayMap result = new MySplayMap(); // Создаёт новое сплей-дерево для результата
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result); // Заполняет подкарту рекурсивно
        return result; // Возвращает подкарту
    }

    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) { // Рекурсивно собирает подкарту
        if (node == null) { // Если узел пустой, завершает выполнение
            return;
        }
        if (node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) { // Если ключ узла больше или равен (при fromInclusive) fromKey
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, result); // Рекурсивно обходит левое поддерево
        }
        if ((node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) &&
                (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey)))) { // Если ключ узла в диапазоне
            result.put(node.key, node.info); // Добавляет пару ключ-значение в результирующую карту
        }
        if (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey))) { // Если ключ узла меньше или равен (при toInclusive) toKey
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, result); // Рекурсивно обходит правое поддерево
        }
    }

    @Override
    public String toString() { // Возвращает строковое представление дерева
        StringBuilder resultStr = new StringBuilder(); // Создаёт новый StringBuilder для построения строки
        resultStr.append("{"); // Добавляет открывающую скобку
        toString(root, resultStr); // Рекурсивно формирует строку из содержимого дерева
        if (resultStr.length() > 1) { // Если строка содержит больше одной скобки (не пустое дерево)
            resultStr.setLength(resultStr.length() - 2); // Удаляет последнюю запятую и пробел
        }
        resultStr.append("}"); // Добавляет закрывающую скобку
        return resultStr.toString(); // Возвращает итоговую строку
    }

    private void toString(Node node, StringBuilder resultStr) { // Рекурсивно формирует строковое представление дерева
        if (node != null) { // Если узел существует
            toString(node.left, resultStr); // Рекурсивно обходит левое поддерево
            resultStr.append(node.key).append("=").append(node.info).append(", "); // Добавляет ключ=значение и запятую
            toString(node.right, resultStr); // Рекурсивно обходит правое поддерево
        }
    }

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) { // Не реализован: возвращает запись с наибольшим ключом, строго меньшим указанного
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) { // Не реализован: возвращает запись с наибольшим ключом, меньшим или равным указанного
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) { // Не реализован: возвращает запись с наименьшим ключом, большим или равным указанного
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) { // Не реализован: возвращает запись с наименьшим ключом, строго большим указанного
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() { // Не реализован: возвращает запись с наименьшим ключом
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() { // Не реализован: возвращает запись с наибольшим ключом
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() { // Не реализован: удаляет и возвращает запись с наименьшим ключом
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() { // Не реализован: удаляет и возвращает запись с наибольшим ключом
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() { // Не реализован: возвращает карту с обратным порядком ключей
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() { // Не реализован: возвращает навигируемое множество ключей
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() { // Не реализован: возвращает навигируемое множество ключей в обратном порядке
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { // Не реализован: возвращает множество всех записей
        throw new UnsupportedOperationException(); // Выбрасывает исключение, так как метод не реализован
    }
}