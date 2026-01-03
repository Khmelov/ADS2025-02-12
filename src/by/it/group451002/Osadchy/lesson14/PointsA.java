package by.it.group451002.Osadchy.lesson14;

import java.util.*;
import java.util.stream.Collectors;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем D и N
        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        // Считываем точки
        double[][] points = new double[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        // Инициализируем DSU
        DSU dsu = new DSU(N);

        // Объединяем точки, находящиеся на расстоянии меньше D
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < D * D) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортируем размеры кластеров по возрастанию
        List<Integer> sizes = clusterSizes.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        // Выводим результат в порядке возрастания
        for (int i = sizes.size()-1; i > -1; i--) {
            if (i < sizes.size()-1) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
    }

    // Класс DSU с эвристикой сжатия путей и объединением по рангу
    static class DSU {
        private int[] parent;
        private int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot) return;

            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else if (rank[xRoot] > rank[yRoot]) {
                parent[yRoot] = xRoot;
            } else {
                parent[yRoot] = xRoot;
                rank[xRoot]++;
            }
        }
    }
}