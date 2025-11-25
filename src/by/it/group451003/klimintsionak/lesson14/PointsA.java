package by.it.group451003.klimintsionak.lesson14;

import java.util.*;

public class PointsA {
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static class DSU {
        int[] parent, size;

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
            int rx = find(x), ry = find(y);
            if (rx != ry) {
                if (size[rx] < size[ry]) {
                    int temp = rx;
                    rx = ry;
                    ry = temp;
                }
                parent[ry] = rx;
                size[rx] += size[ry];
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
                double dist = Math.hypot(Math.hypot(points[i].x - points[j].x, points[i].y - points[j].y), points[i].z - points[j].z);
                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.size[i]);
            }
        }
        sizes.sort(Collections.reverseOrder());
        StringBuilder sb = new StringBuilder();
        for (int s : sizes) {
            sb.append(s).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}