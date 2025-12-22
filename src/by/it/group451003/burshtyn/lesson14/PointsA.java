package by.it.group451003.burshtyn.lesson14;

import java.util.*;

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

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double distance = scanner.nextDouble();
        int n = scanner.nextInt();

        int[] xCoords = new int[n];
        int[] yCoords = new int[n];
        int[] zCoords = new int[n];

        for (int i = 0; i < n; i++) {
            xCoords[i] = scanner.nextInt();
            yCoords[i] = scanner.nextInt();
            zCoords[i] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = xCoords[i] - xCoords[j];
                double dy = yCoords[i] - yCoords[j];
                double dz = zCoords[i] - zCoords[j];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < n; i++) {
            roots.add(dsu.find(i));
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int root : roots) {
            clusterSizes.add(dsu.getSize(root));
        }

        clusterSizes.sort((a, b) -> b - a);

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }

        scanner.close();
    }
}