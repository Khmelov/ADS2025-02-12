package lesson14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import java.util.*;
import java.util.stream.Collectors;

public class PointsA {
    static class DSU {
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
            if (rootX == rootY) return;

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int D = scanner.nextInt();
        int N = scanner.nextInt();
        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(N);
        int squaredD = D * D;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                int dz = points[i][2] - points[j][2];
                int distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < squaredD) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        List<Integer> result = clusterSizes.values().stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}