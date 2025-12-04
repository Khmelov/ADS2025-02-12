package by.it.group451001.suprunovich.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> indexToSite = new ArrayList<>();

        List<String[]> connections = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                connections.add(sites);

                if (!siteToIndex.containsKey(sites[0])) {
                    siteToIndex.put(sites[0], indexToSite.size());
                    indexToSite.add(sites[0]);
                }
                if (!siteToIndex.containsKey(sites[1])) {
                    siteToIndex.put(sites[1], indexToSite.size());
                    indexToSite.add(sites[1]);
                }
            }
        }

        int n = indexToSite.size();

        DSU dsu = new DSU(n);

        for (String[] connection : connections) {
            int index1 = siteToIndex.get(connection[0]);
            int index2 = siteToIndex.get(connection[1]);
            dsu.union(index1, index2);
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    // Класс DSU с двумя эвристиками: union by rank и path compression
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

        // Find с path compression
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        // Union by rank
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // Union by rank
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