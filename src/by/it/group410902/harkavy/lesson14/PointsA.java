package by.it.group410902.harkavy.lesson14;

import java.util.*;

public class PointsA {

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
            if (parent[x] != x)
                parent[x] = find(parent[x]); // сжатие пути
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // union by size
            if (size[a] < size[b]) {
                int t = a; a = b; b = t;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }

    static class Point {
        double x, y, z;
        Point(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    // расстояние между двумя точками
    static double dist(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double dz = a.z - b.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] pts = new Point[N];
        for (int i = 0; i < N; i++) {
            pts[i] = new Point(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
        }

        DSU dsu = new DSU(N);

        // проверяем ВСЕ пары точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist(pts[i], pts[j]) < D) {   // строго [0, D)
                    dsu.union(i, j);
                }
            }
        }

        // считаем размеры кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }

        // собираем размеры и сортируем по возрастанию
        List<Integer> sizes = new ArrayList<>(clusters.values());
        sizes.sort(Collections.reverseOrder());

// вывод
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i + 1 < sizes.size()) System.out.print(" ");
        }
    }
}
