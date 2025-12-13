package by.it.group451003.sorokin.lesson14;

import java.util.Scanner;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double d = scanner.nextDouble();
        int n = scanner.nextInt();
        double[][] points = new double[n][3];

        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < d) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes[root]++;
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            if (clusterSizes[i] > 0) {
                count++;
            }
        }

        int[] sizes = new int[count];
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (clusterSizes[i] > 0) {
                sizes[index++] = clusterSizes[i];
            }
        }

        // Сортировка по убыванию
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sizes[j] < sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
    }

    private static double distance(double[] a, double[] b) {
        return Math.sqrt(
                (a[0] - b[0]) * (a[0] - b[0]) +
                        (a[1] - b[1]) * (a[1] - b[1]) +
                        (a[2] - b[2]) * (a[2] - b[2])
        );
    }

    static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
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

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
