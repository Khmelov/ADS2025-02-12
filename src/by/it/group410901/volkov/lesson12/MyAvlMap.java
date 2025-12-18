package by.it.group410901.volkov.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

/**
 * Реализация Map на основе АВЛ-дерева
 * АВЛ-дерево - это самобалансирующееся бинарное дерево поиска,
 * где высота левого и правого поддеревьев отличается не более чем на 1
 */
public class MyAvlMap implements Map<Integer, String> {
    
    /**
     * Узел АВЛ-дерева
     * АВЛ-дерево поддерживает баланс: разница высот левого и правого поддеревьев <= 1
     */
    private static class Node {
        Integer key;        // Ключ узла (используется для сравнения и поиска)
        String value;       // Значение, связанное с ключом
        Node left;          // Левый потомок (все ключи меньше текущего)
        Node right;         // Правый потомок (все ключи больше текущего)
        int height;         // Высота поддерева с корнем в этом узле (для балансировки)
        
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;  // Новый узел имеет высоту 1
        }
    }
    
    private Node root;  // Корень дерева
    private int size;   // Количество элементов
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }
    
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }
    
    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }
    
    /**
     * Получение значения по ключу
     * Время выполнения: O(log n)
     */
    @Override
    public String get(Object key) {
        Node node = findNode(root, (Integer) key);
        return node != null ? node.value : null;
    }
    
    /**
     * Поиск узла по ключу в дереве
     * Время выполнения: O(log n)
     */
    private Node findNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return findNode(node.left, key);
        if (cmp > 0) return findNode(node.right, key);
        return node;
    }
    
    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = putRecursive(root, key, value);
        if (oldValue == null) size++;
        return oldValue;
    }
    
    private Node putRecursive(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
        
        updateHeight(node);
        return balance(node);
    }
    
    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        root = removeRecursive(root, (Integer) key);
        if (oldValue != null) size--;
        return oldValue;
    }
    
    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = removeRecursive(node.right, minNode.key);
        }
        
        updateHeight(node);
        return balance(node);
    }
    
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * Получение высоты узла
     * null узлы имеют высоту 0
     */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }
    
    /**
     * Обновление высоты узла на основе высот потомков
     */
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
    
    /**
     * Вычисляет коэффициент баланса узла
     * Коэффициент баланса = высота левого поддерева - высота правого поддерева
     * Для АВЛ-дерева этот коэффициент должен быть в диапазоне [-1, 1]
     * @param node узел для проверки
     * @return положительное значение если левое поддерево выше,
     *         отрицательное если правое выше, 0 если равны
     */
    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }
    
    /**
     * Балансирует узел, выполняя необходимые повороты
     * Поддерживает инвариант АВЛ-дерева: |balanceFactor| <= 1
     * @param node узел для балансировки
     * @return корень сбалансированного поддерева
     */
    private Node balance(Node node) {
        int balanceFactor = balanceFactor(node);
        
        // Левое поддерево слишком высокое (лево-лево или лево-право случай)
        if (balanceFactor > 1) {
            // Лево-право случай: сначала поворачиваем левого потомка влево
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            // Затем поворачиваем текущий узел вправо
            return rotateRight(node);
        }
        
        // Правое поддерево слишком высокое (право-право или право-лево случай)
        if (balanceFactor < -1) {
            // Право-лево случай: сначала поворачиваем правого потомка вправо
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            // Затем поворачиваем текущий узел влево
            return rotateLeft(node);
        }
        
        // Узел уже сбалансирован
        return node;
    }
    
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        root = null;
        size = 0;
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRecursive(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void toStringRecursive(Node node, StringBuilder sb) {
        if (node != null) {
            toStringRecursive(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringRecursive(node.right, sb);
        }
    }
}
