package by.it.group451002.mishchenko.lesson14;

import java.util.Scanner;

public class PointsA {

    // Система непересекающихся множеств (Disjoint Set Union)
    static class DSU {
        private int[] parent; // Родительские элементы
        private int[] rank;   // Ранги для балансировки
        private int[] size;   // Размеры множеств

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];

            // Инициализация: каждый элемент - корень своего множества
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Найти корень множества
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Рекурсивное сжатие пути
            }
            return parent[x];
        }

        // Объединение двух множеств
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return; // Уже в одном множестве

            // Объединение по рангу для балансировки
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                rank[rootX]++;
            }
        }

        // Получить размер множества, содержащего элемент x
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс для представления точки в 3D-пространстве
    static class Point {
        double x, y, z;

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Вычисление квадрата расстояния до другой точки
        public double distSquaredTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double dz = this.z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение входных данных
        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        double maxDistSquared = maxDistance * maxDistance; // Квадрат расстояния для сравнения

        // Чтение координат точек
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            points[i] = new Point(x, y, z);
        }

        // Создание системы непересекающихся множеств
        DSU dsu = new DSU(n);

        // Объединение точек, находящихся достаточно близко друг к другу
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].distSquaredTo(points[j]) < maxDistSquared) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор размеров кластеров
        int[] clusterSizes = new int[n];
        int groupCount = 0;
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes[groupCount++] = dsu.getSize(root);
            }
        }

        // Сортировка пузырьком по убыванию
        for (int i = 0; i < groupCount - 1; i++) {
            for (int j = 0; j < groupCount - i - 1; j++) {
                if (clusterSizes[j] < clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        // Вывод результатов
        for (int i = 0; i < groupCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();

        scanner.close();
    }
}