package by.it.group410902.dziatko.lesson14;

import java.util.*;

public class PointsA {
    private static class DSU {
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

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    private static class Point {
        int x, y, z;
        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    private static double dist(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist(points[i], points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        List<Integer> result = new ArrayList<>(clusterSizes.values());
        Collections.sort(result, Collections.reverseOrder());

        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
