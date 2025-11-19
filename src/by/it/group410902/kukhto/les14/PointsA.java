package by.it.group410902.kukhto.les14;


import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double distance = scanner.nextDouble();
        int n = scanner.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // сборка кластеров
        int[] clusterSizes = new int[n];
        for (int i = 0; i < n; i++) {
            clusterSizes[dsu.find(i)]++;
        }

        // сортировка по убыванию
        List<Integer> result = new ArrayList<>();
        for (int size : clusterSizes) {
            if (size > 0) {
                result.add(size);
            }
        }
        result.sort(Collections.reverseOrder());


        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
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
       //если корни разные
        void union(int x, int y) {
            int rx = find(x);
            int ry = find(y);
            if (rx != ry) {
                if (rank[rx] < rank[ry]) {
                    parent[rx] = ry;
                } else if (rank[rx] > rank[ry]) {
                    parent[ry] = rx;
                } else {
                    parent[ry] = rx;
                    rank[rx]++;
                }
            }
        }
    }
}