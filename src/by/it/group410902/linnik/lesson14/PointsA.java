package by.it.group410902.linnik.lesson14;

import java.util.*;

public class PointsA {

    // Класс для представления точки
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Вычисление евклидова расстояния между точками
        double distanceTo(Point other) {
            long dx = x - other.x;
            long dy = y - other.y;
            long dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    // Реализация DSU с эвристикой по размеру
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double D = scanner.nextDouble();
        int N = scanner.nextInt();
        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (dsu.find(i) == i) {
                int root = dsu.find(i);
                int count = 0;
                for (int j = 0; j < N; j++) {
                    if (dsu.find(j) == root) {
                        count++;
                    }
                }
                clusterSizes.add(count);
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
    }
}