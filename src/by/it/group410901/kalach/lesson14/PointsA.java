package by.it.group410901.kalach.lesson14;

import java.util.*;

public class PointsA {

    // Класс для представления точки в трехмерном пространстве
    static class Point {
        double x, y, z; // Координаты точки

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Метод для вычисления расстояния между двумя точками
        double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double dz = this.z - other.z;
            // Евклидово расстояние в 3D-пространстве
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    // Класс для системы непересекающихся множеств (Disjoint Set Union)
    static class DSU {
        int[] parent; // Массив для хранения родительских элементов
        int[] rank;   // Массив для хранения рангов (глубины) деревьев

        // Конструктор инициализирует DSU для n элементов
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждый элемент - корень своего множества
                rank[i] = 0;   // Изначальный ранг = 0
            }
        }

        // Метод для нахождения корневого элемента с path compression
        int find(int x) {
            if (parent[x] != x) {
                // Рекурсивно находим корень и сжимаем путь
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Метод для объединения двух множеств
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // Если элементы уже в одном множестве, ничего не делаем
            if (rootX != rootY) {
                // Объединение по рангу для поддержания сбалансированности деревьев
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY; // Присоединяем меньшее дерево к большему
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    // Если ранги равны, выбираем произвольно и увеличиваем ранг
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }

        // Метод для получения размеров всех кластеров (компонент связности)
        Map<Integer, Integer> getClusterSizes(int n) {
            Map<Integer, Integer> sizes = new HashMap<>();
            // Для каждого элемента находим корень и увеличиваем счетчик размера кластера
            for (int i = 0; i < n; i++) {
                int root = find(i);
                sizes.put(root, sizes.getOrDefault(root, 0) + 1);
            }
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Чтение входных данных
        double D = sc.nextDouble(); // Максимальное расстояние для объединения точек
        int N = sc.nextInt();       // Количество точек

        // Создание массива точек
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            double z = sc.nextDouble();
            points[i] = new Point(x, y, z);
        }

        // Инициализация DSU для N элементов
        DSU dsu = new DSU(N);

        // Объединение точек в кластеры, если расстояние между ними меньше D
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Если точки находятся достаточно близко, объединяем их множества
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Получение размеров всех кластеров
        Map<Integer, Integer> clusterSizes = dsu.getClusterSizes(N);

        // Создание списка размеров кластеров и сортировка по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результатов - размеры кластеров в порядке убывания
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" "); // Разделитель между числами
            System.out.print(sizes.get(i));
        }
        System.out.println();

        sc.close(); // Закрытие сканера
    }
}