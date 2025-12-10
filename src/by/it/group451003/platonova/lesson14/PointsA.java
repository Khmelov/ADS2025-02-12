package by.it.group451003.platonova.lesson14;

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
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // union by size
            if (size[a] < size[b]) {
                parent[a] = b;
                size[b] += size[a];
            } else {
                parent[b] = a;
                size[a] += size[b];
            }
        }

        int clusterSize(int x) {
            return size[find(x)];
        }
    }

    static double dist(double[] a, double[] b) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        double[][] p = new double[N][3];
        for (int i = 0; i < N; i++) {
            p[i][0] = sc.nextDouble();
            p[i][1] = sc.nextDouble();
            p[i][2] = sc.nextDouble();
        }

        DSU dsu = new DSU(N);

        // попарное сравнение точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist(p[i], p[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // подсчет размеров кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusters.put(root, dsu.size[root]);
        }

        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort(Collections.reverseOrder());


        // вывод
        for (int x : result) {
            System.out.print(x + " ");
        }
    }
}

