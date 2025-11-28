package by.it.group451002.sidarchuk.lesson14;

import java.util.*;
import java.io.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

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

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    static class Point {
        double x, y, z;
        int index;

        public Point(int index, double x, double y, double z) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение допустимого расстояния и количества точек
        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        // Чтение точек
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            points[i] = new Point(i, x, y, z);
        }

        // Инициализация DSU
        DSU dsu = new DSU(n);

        // Объединение точек, находящихся на допустимом расстоянии
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = calculateDistance(points[i], points[j]);
                if (distance < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор размеров кластеров
        Set<Integer> roots = new HashSet<>();
        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (roots.add(root)) {
                clusterSizes.add(dsu.getSize(root));
            }
        }

        // Сортировка размеров кластеров по УБЫВАНИЮ
        Collections.sort(clusterSizes, Collections.reverseOrder());

        // Вывод результата
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }

    // Метод для вычисления евклидова расстояния между двумя точками
    private static double calculateDistance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dz = p1.z - p2.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
