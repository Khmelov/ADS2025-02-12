package by.it.group410902.gribach.lesson14;

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
                parent[x] = find(parent[x]); // сокращение пути
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return;

            // эвристика по размеру
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
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
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        boolean[] counted = new boolean[N];
        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            if (!counted[root]) {
                clusterSizes.add(dsu.getSize(root));
                counted[root] = true;
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}