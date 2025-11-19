package by.it.group410902.jalilova.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double maxDistance = scanner.nextDouble();
        int pointsCount = scanner.nextInt();

        int[][] coordinates = new int[pointsCount][3];
        for (int i = 0; i < pointsCount; i++) {
            coordinates[i][0] = scanner.nextInt();
            coordinates[i][1] = scanner.nextInt();
            coordinates[i][2] = scanner.nextInt();
        }

        DisjointSetUnion dsu = new DisjointSetUnion(pointsCount);

        // объединяем точки, находящиеся в пределах заданного расстояния
        for (int first = 0; first < pointsCount; first++) {
            for (int second = first + 1; second < pointsCount; second++) {
                double deltaX = coordinates[first][0] - coordinates[second][0];
                double deltaY = coordinates[first][1] - coordinates[second][1];
                double deltaZ = coordinates[first][2] - coordinates[second][2];
                double actualDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                if (actualDistance < maxDistance) {
                    dsu.unite(first, second);
                }
            }
        }

        // подсчитываем размеры каждого кластера
        int[] clusterSizes = new int[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            clusterSizes[dsu.findRoot(i)]++;
        }

        // формируем список ненулевых размеров кластеров
        List<Integer> clusters = new ArrayList<>();
        for (int size : clusterSizes) {
            if (size > 0) {
                clusters.add(size);
            }
        }
        // сортируем кластеры по убыванию размера
        clusters.sort(Collections.reverseOrder());

        // выводим результат
        for (int i = 0; i < clusters.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusters.get(i));
        }
    }

    // класс для системы непересекающихся множеств
    static class DisjointSetUnion {
        int[] parent;
        int[] rank;

        DisjointSetUnion(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        // находим корневой элемент с path compression
        int findRoot(int x) {
            if (parent[x] != x) {
                parent[x] = findRoot(parent[x]);
            }
            return parent[x];
        }

        // объединяем два множества по рангу
        void unite(int x, int y) {
            int rootX = findRoot(x);
            int rootY = findRoot(y);
            if (rootX != rootY) {
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
}