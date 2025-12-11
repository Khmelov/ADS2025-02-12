package by.it.group410902.derzhavskaya_e.lesson14;

import java.util.*;

public class PointsA {

    // DSU (Union-Find)
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
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            if (size[a] < size[b]) {
                int t = a; a = b; b = t;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }

    // Точка
    static class Point {
        int x, y, z;
        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    // Евклидово расстояние в квадрате
    static long dist2(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        return dx*dx + dy*dy + dz*dz;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] pts = new Point[N];
        for (int i = 0; i < N; i++) {
            pts[i] = new Point(sc.nextInt(), sc.nextInt(), sc.nextInt());
        }

        DSU dsu = new DSU(N);
        double D2 = D * D;  // сравниваем квадрат расстояния

        // Попарное сравнение точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist2(pts[i], pts[j]) < D2) { // расстояние строго меньше D
                    dsu.union(i, j);
                }
            }
        }

        // Считаем размеры кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusters.put(root, dsu.size[root]);
        }

        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort(Collections.reverseOrder());


        // Печать
        for (int v : result) {
            System.out.print(v + " ");
        }
    }
}
