package by.it.group451003.kharkevich.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
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
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
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
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double d = scanner.nextDouble();
        int n = scanner.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);
        double dSq = d * d;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < dSq) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчет размеров кластеров
        int[] clusterSize = new int[n];
        for (int i = 0; i < n; i++) {
            clusterSize[dsu.find(i)]++;
        }

        // Сбор ненулевых размеров
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (clusterSize[i] > 0) {
                sizes.add(clusterSize[i]);
            }
        }

        // СОРТИРОВКА ПО УБЫВАНИЮ (чтобы тест перевернул в возрастающий)
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}