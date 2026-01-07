package by.it.group451002.shandr.lesson12;


import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/*
 Создайте class MyRbMap, который реализует интерфейс SortedMap<Integer, String>
    и работает на основе красно-черного дерева
 */
// Красно-чёрное дерево - это самобалансирующееся бинарное дерево поиска
// Основная идея: использовать цвета узлов (красный/черный) и строгие правила для поддержания баланса
// Это гарантирует, что дерево никогда не выродится в список и операции будут выполняться за O(log n)
public class MyRbMap implements SortedMap<Integer, String> {

    // Красно-черное дерево использует следующие свойства для балансировки:
    // 1. Каждый узел либо красный, либо черный.
    // 2. Корень дерева всегда черный.
    // 3. Все листья (пустые узлы) считаются черными.
    // 4. У любого красного узла оба его дочерних узла черные (нет двух красных узлов подряд).
    // 5. Любой путь от узла до его листьев содержит одинаковое количество черных узлов.
    // Эти правила гарантируют, что дерево остается сбалансированным
    private enum colors {RED, BLACK}

    // Внутренний класс, представляющий узел красно-черного дерева
    // Каждый узел содержит ключ, значение, ссылки на потомков и родителя, а также цвет
    private class Node {
        public int key;          // Ключ узла (используется для упорядочивания)
        public String data;      // Значение, связанное с ключом
        public Node left;        // Левый потомок (все ключи меньше текущего)
        public Node right;       // Правый потомок (все ключи больше текущего)
        public Node parent;      // Родительский узел (нужен для балансировки)
        public colors color;     // Цвет узла - RED или BLACK

        // Конструктор создает новый узел с указанным ключом и значением
        // В отличие от АВЛ-дерева, здесь есть parent - ссылка на родителя, что критически важно для балансировки.
        // Когда добавляется новый узел, он всегда КРАСНЫЙ:
        public Node(int key, String data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.color = colors.RED;  // Новые узлы всегда красные (это уменьшает количество нарушений)
        }

        // Левый поворот - используется для балансировки дерева
        // Вращает узел влево вокруг его правого потомка
        // Используется когда правое поддерево становится слишком высоким
        public void rotateLeft() {
            Node r = this.right;  // Правый потомок станет новым корнем поддерева
            Node p = this.parent; // Сохраняем ссылку на родителя

            // Обновляем ссылку родителя на новый корень
            if (p != null)
                if (p.left == this)
                    p.left = r;
                else
                    p.right = r;

            // Перенаправляем ссылки
            this.right = r.left;
            if (r.left != null)
                r.left.parent = this;
            r.left = this;
            this.parent = r;
            r.parent = p;
        }

        // Правый поворот - используется для балансировки дерева
        // Вращает узел вправо вокруг его левого потомка
        // Используется когда левое поддерево становится слишком высоким
        public void rotateRight() {
            Node l = this.left;   // Левый потомок станет новым корнем поддерева
            Node p = this.parent; // Сохраняем ссылку на родителя

            // Обновляем ссылку родителя на новый корень
            if (p != null)
                if (p.left == this)
                    p.left = l;
                else
                    p.right = l;

            // Перенаправляем ссылки
            this.left = l.right;
            if (l.right != null)
                l.right.parent = this;
            l.right = this;
            this.parent = l;
            l.parent = p;
        }


        // Находит узел с минимальным ключом в текущем поддереве
        // В бинарном дереве поиска минимальный элемент всегда находится в самом левом узле
        public Node findMin() {
            if (this.left == null)
                return this;      // Если левого потомка нет, текущий узел - минимальный
            else
                return this.left.findMin();  // Рекурсивно идем влево
        }
    }

    private Node head = null;  // Корень дерева (головной узел)
    int size = 0;              // Количество элементов в дереве

    // Проверяет, является ли узел красным
    // Красные узлы имеют ограничения - их потомки должны быть черными
    private boolean isRed(Node n) {
        return n != null && n.color == colors.RED;
    }

    // Проверяет, является ли узел черным
    // Черные узлы могут иметь потомков любого цвета (с соблюдением правил)
    private boolean isBlack(Node n) {
        return (n == null || n.color == colors.BLACK);
    }

    // Возвращает "брата" узла - другого потомка того же родителя
    // Братский узел используется при балансировке после удаления
    private Node sibling(Node n) {
        if (n == n.parent.left)
            return n.parent.right;  // Если узел левый, брат - правый
        else
            return n.parent.left;   // Если узел правый, брат - левый
    }

    // Получает деда узла - родителя родителя
    // Используется при балансировке вставки для проверки дяди
    private Node getGrandfather(Node n) {
        if (n != null && n.parent != null)
            return n.parent.parent;
        else
            return null;
    }

    // Получает дядю узла - брата родителя
    // Цвет дяди определяет стратегию балансировки при вставке
    private Node getUncle(Node n) {
        Node g = getGrandfather(n);
        if (g != null)
            if (g.left == n.parent)
                return g.right;  // Дядя - правый потомок деда
            else
                return g.left;   // Дядя - левый потомок деда
        else
            return null;
    }

    // Возвращает единственного потомка узла (если он есть)
    // Используется при удалении узлов с одним потомком
    private Node getChild(Node n) {
        return n.right == null ? n.left : n.right;
    }

    // Основной метод вставки нового узла в дерево
    // Сначала находит правильную позицию для вставки, затем балансирует дерево
    private void insert(Integer key, String data) {
        // Добавляет новый узел в дерево. Узел всегда сначала вставляется как красный
        Node i = new Node(key, data);
        if (head == null)
            head = i;  // Если дерево пустое, новый узел становится корнем
        else {
            // Поиск места для вставки (бинарный поиск)
            Node tmp = head;
            Node tmp_p = head;
            while (tmp != null) {
                tmp_p = tmp;  // Запоминаем родителя для нового узла
                if (key < tmp.key)
                    tmp = tmp.left;   // Идем влево если ключ меньше
                else
                    tmp = tmp.right;  // Идем вправо если ключ больше или равен
            }
            // Вставляем новый узел как потомка найденного родителя
            i.parent = tmp_p;
            if (key < tmp_p.key)
                tmp_p.left = i;   // Вставляем слева если ключ меньше
            else
                tmp_p.right = i;  // Вставляем справа если ключ больше или равен
        }
        // Балансируем дерево после вставки
        balanceInsert(i);
    }

    // Балансировка дерева после вставки нового узла
    // Восстанавливает свойства красно-черного дерева после вставки узла.
    // Использует повороты и перекрашивание.
    private void balanceInsert(Node n) {
        // Если узел - корень, красим в черный (правило 2)
        if (n.parent == null) {
            n.color = colors.BLACK;
            return;
        }


        // Балансируем пока родитель красный (может быть нарушение правила 4)
        while (n.parent != null && n.parent.color == colors.RED) {
            Node g = getGrandfather(n);  // Дед
            Node u = getUncle(n);        // Дядя

            // Случай 1: Родитель является левым потомком деда
            if (g != null && g.left == n.parent) {
                // Подслучай 1.1: Дядя красный
                if (u != null && u.color == colors.RED) {
                    // Перекрашиваем родителя и дядю в черный, деда в красный
                    n.parent.color = colors.BLACK;
                    u.color = colors.BLACK;
                    g.color = colors.RED;
                    n = g;  // Продолжаем балансировку от деда
                } else {
                    // Подслучай 1.2: Дядя черный
                    if (n.parent.right == n) {
                        // Если узел - правый потомок, нужен левый поворот
                        n = n.parent;
                        n.rotateLeft();
                    }
                    // Правый поворот и перекрашивание
                    n.parent.color = colors.BLACK;
                    getGrandfather(n).color = colors.RED;
                    getGrandfather(n).rotateRight();
                }
            }
            // Случай 2: Родитель является правым потомком деда (симметрично случаю 1)
            else if (g != null && g.right == n.parent) {
                // Подслучай 2.1: Дядя красный
                if (u != null && u.color == colors.RED) {
                    n.parent.color = colors.BLACK;
                    u.color = colors.BLACK;
                    g.color = colors.RED;
                    n = g;
                } else {
                    // Подслучай 2.2: Дядя черный
                    if (n.parent.left == n) {
                        n = n.parent;
                        n.rotateRight();
                    }
                    n.parent.color = colors.BLACK;
                    getGrandfather(n).color = colors.RED;
                    getGrandfather(n).rotateLeft();
                }
            }

            // Поднимаемся к корню (корень может измениться после поворотов)
            while (head.parent != null)
                head = head.parent;
        }
        // Корень всегда черный (правило 2)
        head.color = colors.BLACK;
    }

    // Удаляет узел с одним потомком (упрощенное удаление)
    // Перенаправляет ссылку родителя на единственного потомка удаляемого узла
    private void removeOneChild(Node n) {
        if (n.right == null)
            if (n.parent.left == n)
                n.parent.left = n.left;   // Заменяем левого потомка
            else
                n.parent.right = n.left;  // Заменяем правого потомка
        else if (n.parent.left == n)
            n.parent.left = n.right;      // Заменяем левого потомка
        else
            n.parent.right = n.right;     // Заменяем правого потомка
    }

    // Основной метод удаления узла по ключу
    // Удаляет узел по ключу. Если у узла два дочерних узла, заменяет его на
    // минимальный узел в правом поддереве. Затем вызывает балансировку
    private void delete(Integer key) {
        Node deleted = recGet(head, key);  // Находим узел для удаления

        // Случай 1: Узел - лист (нет потомков)
        if (deleted.right == null && deleted.left == null) {
            if (deleted == head)
                head = null;  // Если это корень, дерево становится пустым
            else {
                // Просто удаляем ссылку родителя
                if (deleted.parent.left == deleted)
                    deleted.parent.left = null;
                else
                    deleted.parent.right = null;
            }
            return;
        }


        // Находим узел для замены
        Node swapped = null;
        if (deleted.right != null && deleted.left != null) {
            // Если два потомка, находим минимальный в правом поддереве
            swapped = deleted.right.findMin();
        }
        else {
            // Если один потомок, берем его
            swapped = getChild(deleted);
        }

        // Копируем данные из заменяющего узла в удаляемый
        deleted.data = swapped.data;
        deleted.key = swapped.key;

        // Удаляем заменяющий узел
        removeOneChild(swapped);

        // Балансируем если удалили черный узел (это может нарушить баланс черных высот)
        if (isBlack(swapped) || isBlack(deleted))
            balanceDelete(getChild(swapped));

        // Корень всегда черный
        head.color = colors.BLACK;
    }

    // Балансировка дерева после удаления узла
    // Восстанавливает свойства красно-черного дерева после удаления узла
    private void balanceDelete(Node ch) {
        // Балансируем пока не дойдем до корня
        while (ch != head && ch != null) {
            if (ch.parent.left == ch) {
                // Случай: узел является левым потомком
                Node b = sibling(ch);  // Брат (правый потомок родителя)

                // Случай 1: Брат красный
                if (isRed(b)) {
                    b.color = colors.BLACK;
                    ch.parent.color = colors.RED;
                    ch.parent.rotateLeft();
                }

                // Случай 2: Оба потомка брата черные
                if (isBlack(b.right) && isBlack(b.left)) {
                    b.color = colors.RED;
                    ch = ch.parent;  // Переходим к родителю для продолжения балансировки
                } else {
                    // Случай 3: Правый потомок брата черный
                    if (isBlack(b.right)) {
                        b.left.color = colors.BLACK;
                        b.color = colors.RED;
                        b.rotateRight();
                    }
                    // Случай 4: Общий случай
                    b.color = ch.parent.color;
                    ch.parent.color = colors.BLACK;
                    b.right.color = colors.BLACK;
                    ch.parent.rotateLeft();
                    ch = head;  // Завершаем балансировку
                }
            } else {
                // Симметричный случай: узел является правым потомком
                Node b = sibling(ch);

                if (isRed(b)) {
                    b.color = colors.BLACK;
                    ch.parent.color = colors.RED;
                    ch.parent.rotateLeft();
                }

                if (isBlack(b.right) && isBlack(b.left)) {
                    b.color = colors.RED;
                    ch = ch.parent;
                } else {
                    if (isBlack(b.left)) {
                        b.right.color = colors.BLACK;
                        b.color = colors.RED;
                        b.rotateLeft();
                    }
                    b.color = ch.parent.color;
                    ch.parent.color = colors.BLACK;
                    b.left.color = colors.BLACK;
                    ch.parent.rotateRight();
                    ch = head;
                }
            }
        }

        // Убеждаемся что head указывает на корень (после поворотов корень мог измениться)
        while (head.parent != null)
            head = head.parent;

        // Корень всегда черный
        head.color = colors.BLACK;
    }

    // Рекурсивный метод для построения строкового представления дерева
    // Использует inorder обход (левый-корень-правый) для вывода элементов в отсортированном порядке
    private void toStr(Node n, StringBuilder res) {
        if (n == null)
            return;
        toStr(n.left, res);  // Сначала левое поддерево (меньшие ключи)
        res.append(n.key).append("=").append(n.data).append(", ");  // Текущий узел
        toStr(n.right, res); // Затем правое поддерево (большие ключи)
    }


    @Override
    // Возвращает строковое представление дерева в формате {key1=value1, key2=value2, ...}
    // Элементы выводятся в порядке возрастания ключей благодаря inorder обходу
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        toStr(head, res);
        if (head != null)
            res = new StringBuilder(res.substring(0, res.length() - 2));  // Убираем последнюю запятую и пробел
        res.append("}");
        return String.valueOf(res);
    }

    @Override
    // Возвращает компаратор для упорядочивания ключей (не реализован - использует естественный порядок)
    public Comparator<? super Integer> comparator() {
        return null;  // Используется естественный порядок Integer
    }

    @Override
    // Возвращает представление части дерева между fromKey (включительно) и toKey (исключительно)
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;  // Не реализовано в данной версии
    }

    // Рекурсивно заполняет текущее дерево узлами с ключами меньше заданного
    // Используется для реализации headMap()
    public void fillLower(Node n, Integer key) {
        if (n == null)
            return;
        fillLower(n.left, key);   // Сначала обрабатываем левое поддерево (меньшие значения)
        if (n.key < key)
            this.put(n.key, n.data);  // Добавляем узел если ключ меньше заданного
        fillLower(n.right, key);  // Затем обрабатываем правое поддерево
    }

    // Рекурсивно заполняет текущее дерево узлами с ключами больше или равными заданному
    // Используется для реализации tailMap()
    public void fillUpper(Node n, Integer key) {
        if (n == null)
            return;
        fillUpper(n.left, key);    // Сначала обрабатываем левое поддерево
        if (n.key >= key)
            this.put(n.key, n.data);  // Добавляем узел если ключ больше или равен заданному
        fillUpper(n.right, key);   // Затем обрабатываем правое поддерево
    }

    // Возвращают поддерево с ключами меньше/больше заданного.
    @Override
    // Возвращает представление части дерева с ключами строго меньше toKey
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap tmp = new MyRbMap();
        tmp.fillLower(this.head, toKey);  // Рекурсивно копируем подходящие узлы в новое дерево
        return tmp;
    }

    @Override
    // Возвращает представление части дерева с ключами больше или равными fromKey
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tmp = new MyRbMap();
        tmp.fillUpper(this.head, fromKey);  // Рекурсивно копируем подходящие узлы в новое дерево
        return tmp;
    }

    //Возвращают минимальный/максимальный ключ в дереве
    @Override
    // Находит и возвращает наименьший ключ в дереве
    // В бинарном дереве поиска минимальный ключ всегда в самом левом узле
    public Integer firstKey() {
        if (head == null) throw new NoSuchElementException("Map is empty");
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;      // Запоминаем текущий узел
            tmp = tmp.left;   // Двигаемся влево (к меньшим ключам)
        }
        return tmp_p.key;
    }

    @Override
    // Находит и возвращает наибольший ключ в дереве
    // В бинарном дереве поиска максимальный ключ всегда в самом правом узле
    public Integer lastKey() {
        if (head == null) throw new NoSuchElementException("Map is empty");
        Node tmp = head;
        Node tmp_p = null;
        while (tmp != null) {
            tmp_p = tmp;      // Запоминаем текущий узел
            tmp = tmp.right;  // Двигаемся вправо (к большим ключам)
        }
        return tmp_p.key;
    }

    @Override
    // Возвращает количество элементов в дереве
    public int size() {
        return size;
    }

    @Override
    // Проверяет, пусто ли дерево
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    // Проверяет, существует ли узел с данным ключом
    public boolean containsKey(Object key) {
        Node res = recGet(head, (Integer) key);
        return res != null;
    }

    private Boolean ContainsValueF;  // Флаг для поиска по значению

    // Рекурсивно ищет узел с заданным значением (полный обход дерева)
    // Внимание: поиск по значению требует O(n) времени в худшем случае
    private void recGetByValue(Node n, Object data) {
        if (n == null)
            return;
        recGetByValue(n.left, data);   // Ищем в левом поддереве
        if (n.data.equals(data))       // Проверяем текущий узел
            ContainsValueF = true;
        recGetByValue(n.right, data);  // Ищем в правом поддереве
    }

    @Override
    // Проверяет, существует ли узел с данным значением, рекурсивно обходя дерево
    // В отличие от containsKey, этот метод требует полного обхода дерева
    public boolean containsValue(Object value) {
        ContainsValueF = false;
        recGetByValue(head, value);
        return ContainsValueF;
    }

    // Рекурсивно ищет узел по ключу (бинарный поиск)
    // Работает за O(log n) благодаря свойствам бинарного дерева поиска
    private Node recGet(Node n, Integer key) {
        if (n == null)
            return null;                    // Ключ не найден
        else if (key < n.key)
            return recGet(n.left, key);     // Ищем в левом поддереве
        else if (key > n.key)
            return recGet(n.right, key);    // Ищем в правом поддереве
        else
            return n;                       // Ключ найден
    }

    @Override
    // Возвращает значение, связанное с ключом, или null если ключ не найден
    public String get(Object key) {
        Node tmp = recGet(head, (Integer) key);
        if (tmp == null)
            return null;
        return tmp.data;
    }

    @Override
    // Добавляет ключ-значение, выполняя вставку и при необходимости балансировку.
    // Если ключ уже существует, обновляет значение и возвращает старое
    // Если ключа нет, вставляет новый узел и возвращает null
    public String put(Integer key, String value) {
        Node retVal = recGet(head, key);
        if (retVal == null) {
            size++;
            insert(key, value);    // Вставляем новый узел
            return null;
        }
        String pr = retVal.data;   // Сохраняем старое значение
        retVal.data = value;       // Обновляем значение
        return pr;                 // Возвращаем старое значение
    }

    @Override
    // Удаляет элемент по ключу, восстанавливает баланс
    // Возвращает значение удаленного элемента или null если ключ не найден
    public String remove(Object key) {
        String res = get(key);      // Получаем значение перед удалением
        if (res != null) {
            size--;
            delete((Integer) key);  // Удаляем узел и балансируем дерево
        }
        return res;
    }

    @Override
    // Добавляет все элементы из переданной карты (не реализовано)
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // Реализация требует последовательного вызова put() для всех элементов m
    }

    @Override
    // Удаляет все элементы из дерева
    public void clear() {
        head = null;   // Удаляем ссылку на корень
        size = 0;      // Сбрасываем счетчик
    }

    @Override
    // Возвращает множество ключей (не реализовано)
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    // Возвращает коллекцию значений (не реализовано)
    public Collection<String> values() {
        return null;
    }

    @Override
    // Возвращает множество пар ключ-значение (не реализовано)
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}





