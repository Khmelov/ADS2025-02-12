package by.it.group451002.gorbach.lesson14;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class SitesB {
    static class DSU {
        private int[] parent;
        private int[] rank;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
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

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();
        Map<Integer, String> indexToSite = new HashMap<>();
        int indexCounter = 0;

        java.util.List<String> connections = new java.util.ArrayList<>();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            connections.add(line);

            String[] sites = line.split("\\+");
            for (String site : sites) {
                if (!siteToIndex.containsKey(site)) {
                    siteToIndex.put(site, indexCounter);
                    indexToSite.put(indexCounter, site);
                    indexCounter++;
                }
            }
        }

        DSU dsu = new DSU(indexCounter);

        for (String connection : connections) {
            String[] sites = connection.split("\\+");
            int index1 = siteToIndex.get(sites[0]);
            int index2 = siteToIndex.get(sites[1]);
            dsu.union(index1, index2);
        }

        boolean[] counted = new boolean[indexCounter];
        int[] clusterSizes = new int[indexCounter];
        int count = 0;

        for (int i = 0; i < indexCounter; i++) {
            int root = dsu.find(i);
            if (!counted[root]) {
                clusterSizes[count++] = dsu.getSize(root);
                counted[root] = true;
            }
        }

        // Сортировка по убыванию
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (clusterSizes[j] < clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.print(clusterSizes[i]);
            if (i < count - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        scanner.close();
    }
}