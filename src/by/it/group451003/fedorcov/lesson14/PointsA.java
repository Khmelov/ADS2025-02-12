package by.it.group451003.fedorcov.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
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

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    static class Point {
        int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distanceTo(Point other) {
            long dx = this.x - other.x;
            long dy = this.y - other.y;
            long dz = this.z - other.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int D = scanner.nextInt();
        int N = scanner.nextInt();

        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            points[i] = new Point(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[N];
        for (int i = 0; i < N; i++) {
            clusterSizes[dsu.find(i)]++;
        }

        List<Integer> sizes = new ArrayList<>();
        for (int size : clusterSizes) {
            if (size > 0) {
                sizes.add(size);
            }
        }

        sizes.sort((a, b) -> b - a);

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        scanner.close();
    }
}