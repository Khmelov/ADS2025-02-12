package by.it.group451002.shandr.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Реализация интерфейса Map на основе АВЛ-дерева (самобалансирующегося бинарного дерева поиска)
 *
 * Особенности:
 * - Автоматическая балансировка при добавлении и удалении элементов
 * - Гарантированная сложность O(log n) для основных операций
 * - Хранение пар ключ-значение в отсортированном порядке по ключам
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Внутренний класс, представляющий узел АВЛ-дерева
     * Каждый узел хранит ключ, значение, высоту поддерева и ссылки на потомков
     */
    private class Node {
        public int key;           // Ключ узла
        public int height;        // Высота поддерева с корнем в этом узле
        public String data;       // Значение, связанное с ключом
        public Node left, right;  // Левый и правый потомки

        // Конструктор узла
        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            height = 1;  // Новая вершина всегда имеет высоту 1
        }

        // Возвращает высоту узла (0 для null-узлов)
        public int height(Node n) {
            return n != null ? n.height : 0;
        }

         // Вычисляет баланс-фактор узла (разница высот правого и левого поддеревьев)
         // Баланс-фактор должен быть в диапазоне [-1, 0, 1] для сбалансированного дерева
        public int getBFactor(Node n) {
            if (n == null)
                return 0;
            return height(n.right) - height(n.left);
        }

        // Обновляет высоту текущего узла на основе высот потомков
        public void fixHeight() {
            int hl = height(this.left);
            int hr = height(this.right);
            this.height = Math.max(hl, hr) + 1;
        }

        //Выполняет правый поворот для балансировки дерева
        // Используется когда левое поддерево слишком высокое
        public Node rotateRight() {
            Node tmpLeft = this.left;
            this.left = tmpLeft.right;
            tmpLeft.right = this;
            this.fixHeight();
            tmpLeft.fixHeight();
            return tmpLeft;
        }


         //Выполняет левый поворот для балансировки дерева
         // Используется когда правое поддерево слишком высокое
        public Node rotateLeft() {
            Node tmpRight = this.right;
            this.right = tmpRight.left;
            tmpRight.left = this;
            this.fixHeight();
            tmpRight.fixHeight();
            return tmpRight;
        }

        // Балансирует узел, выполняя необходимые повороты
        public Node balance() {
            fixHeight();
            int bf = getBFactor(this);

            // Случай 1: Правое поддерево слишком высокое
            if (bf == 2) {
                // Если правое поддерево лево-тяжелое, нужен двойной поворот
                if (getBFactor(this.right) < 0)
                    this.right = this.right.rotateRight();
                return rotateLeft();
            }

            // Случай 2: Левое поддерево слишком высокое
            if (bf == -2) {
                // Если левое поддерево право-тяжелое, нужен двойной поворот
                if (getBFactor(this.left) > 0)
                    this.left = this.left.rotateLeft();
                return rotateRight();
            }

            return this;  // Узел уже сбалансирован
        }

        // Находит узел с минимальным ключом в текущем поддереве
        public Node findMin() {
            if (this.left == null)
                return this;
            else
                return this.left.findMin();
        }

        // Удаляет узел с минимальным ключом из текущего поддерева
        public Node removeMin() {
            if (this.left == null)
                return this.right;
            this.left = this.left.removeMin();
            return this.balance();
        }
    }

    // Корень АВЛ-дерева и счетчик элементов
    private Node tree = null;
    private int size = 0;

    // Рекурсивный поиск узла по ключу
    private Node recGet(Node n, Integer key) {
        if (n == null)
            return null;
        else if (key < n.key)
            return recGet(n.left, key);    // Ищем в левом поддереве
        else if (key > n.key)
            return recGet(n.right, key);   // Ищем в правом поддереве
        else
            return n;                      // Ключ найден
    }

    // Рекурсивная вставка нового узла с балансировкой
    private Node recInsert(Node n, Integer key, String data) {
        if (n == null)
            return new Node(key, data);  // Создаем новый узел

        if (key < n.key)
            n.left = recInsert(n.left, key, data);   // Вставляем в левое поддерево
        else
            n.right = recInsert(n.right, key, data); // Вставляем в правое поддерево

        return n.balance();  // Балансируем дерево после вставки
    }

    // Рекурсивное удаление узла по ключу с балансировкой
    private Node recRemove(Node n, Object key) {
        if (n == null)
            return null;  // Ключ не найден

        // Поиск удаляемого узла
        if ((Integer) key < n.key)
            n.left = recRemove(n.left, key);    // Ищем в левом поддереве
        else if ((Integer) key > n.key)
            n.right = recRemove(n.right, key);  // Ищем в правом поддереве
        else {
            // Узел найден - выполняем удаление
            Node l = n.left;
            Node r = n.right;
            n = null;

            if (r == null)
                return l;  // Нет правого потомка - возвращаем левый

            // Находим минимальный узел в правом поддереве (преемник)
            Node min = r.findMin();
            min.right = r.removeMin();  // Удаляем преемника из правого поддерева
            min.left = l;               // Присоединяем левое поддерево
            return min.balance();       // Балансируем и возвращаем
        }

        return n.balance();  // Балансируем дерево после удаления
    }

    // Рекурсивное построение строкового представления дерева (инфиксный обход)
    private void toStr(Node n, StringBuilder res) {
        if (n == null)
            return;

        toStr(n.left, res);                         // Обходим левое поддерево
        res.append(n.key).append("=").append(n.data).append(", ");  // Добавляем текущий узел
        toStr(n.right, res);                        // Обходим правое поддерево
    }

    /**
     * Возвращает строковое представление карты в формате {key1=value1, key2=value2, ...}
     * Элементы выводятся в отсортированном порядке по ключам
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(tree, res);
        if (tree != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));  // Убираем последнюю запятую
        res.append("}");
        return String.valueOf(res);
    }

    // ========== РЕАЛИЗАЦИЯ МЕТОДОВ ИНТЕРФЕЙСА MAP ==========

    /**
     * Возвращает количество элементов в карте
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли карта
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет наличие ключа в карте
     * @param key ключ для проверки
     * @return true если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        Node res = recGet(tree, (Integer) key);
        return res != null;
    }

    /**
     * Проверяет наличие значения в карте (не реализовано)
     */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    /**
     * Возвращает значение по ключу
     * @param key ключ для поиска
     * @return значение или null если ключ не найден
     */
    @Override
    public String get(Object key) {
        Node tmp = recGet(tree, (Integer) key);
        if (tmp == null)
            return null;
        return tmp.data;
    }

    /**
     * Добавляет пару ключ-значение в карту
     * @param key ключ для добавления
     * @param value значение для добавления
     * @return предыдущее значение или null если ключа не было
     */
    @Override
    public String put(Integer key, String value) {
        Node retVal = recGet(tree, key);
        if (retVal == null) {
            // Новый ключ - добавляем и увеличиваем размер
            size++;
            tree = recInsert(tree, key, value);
            return null;
        }
        // Ключ уже существует - обновляем значение
        String pr = retVal.data;
        retVal.data = value;
        return pr;
    }

    /**
     * Удаляет пару ключ-значение из карты
     * @param key ключ для удаления
     * @return удаленное значение или null если ключ не найден
     */
    @Override
    public String remove(Object key) {
        String res = get(key);
        if (res != null) {
            // Ключ найден - удаляем и уменьшаем размер
            size--;
            tree = recRemove(tree, key);
        }
        return res;
    }

    /**
     * Очищает карту (удаляет все элементы)
     */
    @Override
    public void clear() {
        tree = null;
        size = 0;
    }

    // ========== НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ==========

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // Не реализовано
    }

    @Override
    public Set<Integer> keySet() {
        return null;  // Не реализовано
    }

    @Override
    public Collection<String> values() {
        return null;  // Не реализовано
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;  // Не реализовано
    }
}