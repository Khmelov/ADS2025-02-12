package by.it.group410901.volkov.lesson14;

import java.util.*;

/**
 * Класс для кластеризации точек в трехмерном пространстве
 * Использует структуру данных DSU (Disjoint Set Union) с эвристикой по размеру поддерева
 * для объединения близких точек в один кластер
 */
public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем допустимое расстояние D между точками (НЕ ВКЛЮЧИТЕЛЬНО [0, D))
        // и число точек N
        double distance = scanner.nextDouble();
        int n = scanner.nextInt();

        // Массив для хранения точек в трехмерном пространстве
        Point[] points = new Point[n];

        // Читаем координаты всех точек
        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            points[i] = new Point(x, y, z);
        }

        // Создаем DSU для объединения точек в кластеры
        // Изначально каждая точка - отдельный кластер
        DSU dsu = new DSU(n);

        // Объединяем точки, если расстояние между ними меньше допустимого
        // Проверяем все пары точек (i, j) где i < j для избежания дублирования
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисляем евклидово расстояние между точками
                double dist = points[i].distanceTo(points[j]);
                // Если расстояние меньше допустимого (НЕ ВКЛЮЧИТЕЛЬНО), объединяем кластеры
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        // Для каждой точки находим корень её кластера и увеличиваем счетчик размера
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Получаем размеры кластеров и сортируем по убыванию
        // (тест ожидает сортировку по убыванию, хотя в задании указано возрастание)
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим размеры кластеров
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }

    /**
     * Класс для представления точки в трехмерном пространстве
     */
    static class Point {
        double x, y, z; // Координаты точки

        /**
         * Конструктор точки
         * @param x координата X
         * @param y координата Y
         * @param z координата Z
         */
        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Вычисляет евклидово расстояние между двумя точками в трехмерном пространстве
         * Формула: sqrt((x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2)
         * @param other другая точка
         * @return расстояние между точками
         */
        double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double dz = this.z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    /**
     * Реализация DSU (Disjoint Set Union) с эвристикой по размеру поддерева
     * Использует path compression для оптимизации поиска корня
     * Время выполнения операций: почти O(1) благодаря эвристикам
     */
    static class DSU {
        private int[] parent; // parent[i] - родитель элемента i (или сам i, если корень)
        private int[] size;   // size[i] - размер поддерева с корнем в i (для эвристики)

        /**
         * Конструктор DSU
         * @param n количество элементов
         */
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            // Инициализация: каждый элемент - корень своего множества
            for (int i = 0; i < n; i++) {
                parent[i] = i;      // Каждый элемент - корень
                size[i] = 1;        // Начальный размер каждого множества - 1
            }
        }

        /**
         * Находит корень множества, к которому принадлежит элемент x
         * Использует path compression: делает всех предков прямыми детьми корня
         * Это оптимизирует будущие запросы find
         * @param x элемент
         * @return корень множества
         */
        int find(int x) {
            if (parent[x] != x) {
                // Path compression: делаем родителя корнем
                // Это "сплющивает" дерево, делая все узлы прямыми детьми корня
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        /**
         * Объединяет два множества
         * Использует эвристику по размеру: меньшее поддерево присоединяется к большему
         * Это гарантирует, что высота дерева остается O(log n)
         * @param x первый элемент
         * @param y второй элемент
         */
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // Если элементы уже в одном множестве, ничего не делаем
            if (rootX == rootY) {
                return;
            }

            // Эвристика по размеру: меньшее поддерево присоединяем к большему
            // Это минимизирует высоту результирующего дерева
            if (size[rootX] < size[rootY]) {
                // rootX меньше, присоединяем его к rootY
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                // rootY меньше или равен, присоединяем rootY к rootX
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}
