package by.it.group451002.kureichuk.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        private int[] parent;
        private int[] rank;
        private int[] size;

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
        int x, y, z;
        int index;

        public Point(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }

        public double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение расстояния и количества точек
        int distance = scanner.nextInt();
        int n = scanner.nextInt();

        // Чтение точек
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z, i);
        }

        // Создание DSU
        DSU dsu = new DSU(n);

        // Объединение точек, находящихся на допустимом расстоянии
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].distanceTo(points[j]) < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Получение списка размеров кластеров
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортировка в порядке убывания (как в примере вывода: 3 5)
        sizes.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}