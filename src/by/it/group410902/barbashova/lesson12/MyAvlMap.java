package by.it.group410902.barbashova.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    class AVLNode {
        int key;
        String value;
        int Height;
        AVLNode Left, Right;


        AVLNode(int key, String value) {
            this.key = key;
            this.value = value;
            this.Height = 1; // Новая вершина всегда имеет высоту 1
        }
    }

    AVLNode Root;
    StringBuilder result;

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size(Root);
    }

    // Рекурсивный метод для вычисления размера поддерева
    private int size(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.Left) + size(node.Right);
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    // Возвращает строковое представление дерева в формате {key=value, ...}
    @Override
    public String toString() {
        if (Root == null)
            return "{}";
        StringBuilder sb = new StringBuilder().append('{');
        InOrderTraversal(Root, sb); // Обход в порядке возрастания ключей
        sb.replace(sb.length() - 2, sb.length(), "}");
        return sb.toString();
    }

    // Симметричный обход дерева (левый-корень-правый)
    private void InOrderTraversal(AVLNode node, StringBuilder sb) {
        if (node != null) {
            InOrderTraversal(node.Left, sb);
            sb.append(node.key + "=" + node.value + ", ");
            InOrderTraversal(node.Right, sb);
        }
    }

    // Проверяет, содержится ли ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        return Search((Integer) key, Root) != null;
    }

    // Рекурсивный поиск узла по ключу
    AVLNode Search(Integer key, AVLNode node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        // Рекурсивный поиск в левом или правом поддереве
        return Search(key, comparison < 0 ? node.Left : node.Right);
    }

    // Возвращает значение по ключу
    @Override
    public String get(Object key) {
        AVLNode result = Search((Integer) key, Root);
        return result == null ? null : result.value;
    }

    // Добавляет или обновляет пару ключ-значение
    @Override
    public String put(Integer key, String value) {
        result = new StringBuilder();
        Root = put(Root, key, value);
        return result.isEmpty() ? null : result.toString();
    }

    // Рекурсивный метод вставки узла
    AVLNode put(AVLNode node, Integer key, String value) {
        if (node == null) {
            return new AVLNode(key, value); // Создание нового узла
        }

        int comparison = key.compareTo(node.key);
        if (comparison < 0)
            node.Left = put(node.Left, key, value); // Вставка в левое поддерево
        else if (comparison > 0)
            node.Right = put(node.Right, key, value); // Вставка в правое поддерево
        else {
            // Ключ уже существует - обновление значения
            if (!node.value.equalsIgnoreCase(value)) {
                node.value = value;
                result.append("generate" + key); // Фиксируем изменение
            }
        }
        return Balance(node); // Балансировка дерева после вставки
    }

    // Возвращает высоту узла (для null возвращает 0)
    int Height(AVLNode node) {
        return node == null ? 0 : node.Height;
    }

    // Вычисляет коэффициент балансировки узла
    // Положительный - перевес влево, отрицательный - перевес вправо
    int BalanceFactor(AVLNode node) {
        return node == null ? 0 : Height(node.Left) - Height(node.Right);
    }

    // Правый поворот для балансировки
    AVLNode RotateRight(AVLNode node) {
        AVLNode newRoot = node.Left;
        node.Left = newRoot.Right;
        newRoot.Right = node;

        // Обновление высот повернутых узлов
        node.Height = 1 + Math.max(Height(node.Left), Height(node.Right));
        newRoot.Height = 1 + Math.max(Height(newRoot.Left), Height(newRoot.Right));

        return newRoot;
    }

    // Левый поворот для балансировки
    AVLNode RotateLeft(AVLNode node) {
        AVLNode newRoot = node.Right;
        node.Right = newRoot.Left;
        newRoot.Left = node;

        // Обновление высот повернутых узлов
        node.Height = 1 + Math.max(Height(node.Left), Height(node.Right));
        newRoot.Height = 1 + Math.max(Height(newRoot.Left), Height(newRoot.Right));

        return newRoot;
    }

    // Балансировка узла после операций вставки/удаления
    AVLNode Balance(AVLNode node) {
        if (node == null)
            return node;

        // Обновление высоты текущего узла
        node.Height = 1 + Math.max(Height(node.Left), Height(node.Right));
        int balanceFactor = BalanceFactor(node);

        // Случай Left-Left или Left-Right
        if (balanceFactor > 1) {
            if (BalanceFactor(node.Left) < 0) // Случай Left-Right
                node.Left = RotateLeft(node.Left);
            return RotateRight(node); // Для Left-Left и после Left-Right
        }

        // Случай Right-Right или Right-Left
        if (balanceFactor < -1) {
            if (BalanceFactor(node.Right) > 0) // Случай Right-Left
                node.Right = RotateRight(node.Right);
            return RotateLeft(node); // Для Right-Right и после Right-Left
        }

        return node; // Узел сбалансирован
    }

    // Удаление узла по ключу
    @Override
    public String remove(Object key) {
        result = new StringBuilder();
        Root = remove(Root, (Integer) key);
        return result.isEmpty() ? null : result.toString();
    }

    // Рекурсивный метод удаления узла
    AVLNode remove(AVLNode node, Integer key) {
        if (node == null)
            return node;

        int comparison = key.compareTo(node.key);
        if (comparison < 0)
            node.Left = remove(node.Left, key); // Поиск в левом поддереве
        else if (comparison > 0)
            node.Right = remove(node.Right, key); // Поиск в правом поддереве
        else {
            // Узел найден - удаление
            result.append("generate" + key); // Фиксируем удаление

            // Узел с одним или без потомков
            if (node.Left == null)
                return node.Right;
            if (node.Right == null)
                return node.Left;

            // Узел с двумя потомками - находим минимальный в правом поддереве
            AVLNode minNode = minValueNode(node.Right);
            node.value = minNode.value; // Заменяем значение
            node.Right = RemoveMinNode(node.Right); // Удаляем минимальный узел
        }

        return Balance(node); // Балансировка после удаления
    }

    // Удаление узла с минимальным ключом из поддерева
    AVLNode RemoveMinNode(AVLNode node) {
        if (node.Left == null)
            return node.Right;

        node.Left = RemoveMinNode(node.Left);
        return Balance(node); // Балансировка после удаления
    }

    // Поиск узла с минимальным ключом в поддереве
    AVLNode minValueNode(AVLNode node) {
        return node.Left == null ? node : minValueNode(node.Left);
    }

    // Очистка дерева
    @Override
    public void clear() {
        Root = clear(Root);
    }

    // Рекурсивная очистка поддерева
    AVLNode clear(AVLNode node) {
        if (node == null)
            return null;
        node.Left = clear(node.Left);   // Рекурсивная очистка левого поддерева
        node.Right = clear(node.Right); // Рекурсивная очистка правого поддерева
        return null; // Возврат null для удаления ссылки на узел
    }

    //////////////////////////////////////////////////////
    // Нереализованные методы интерфейса Map
    //////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}