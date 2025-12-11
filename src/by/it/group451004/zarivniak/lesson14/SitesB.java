package by.it.group451004.zarivniak.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
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

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String[]> connections = new ArrayList<>();
        Set<String> uniqueSites = new HashSet<>();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            String[] parts = line.split("\\+");
            String site1 = parts[0];
            String site2 = parts[1];

            connections.add(new String[]{site1, site2});
            uniqueSites.add(site1);
            uniqueSites.add(site2);
        }

        List<String> sites = new ArrayList<>(uniqueSites);
        Map<String, Integer> siteToIndex = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            siteToIndex.put(sites.get(i), i);
        }

        DSU dsu = new DSU(sites.size());

        for (String[] connection : connections) {
            int index1 = siteToIndex.get(connection[0]);
            int index2 = siteToIndex.get(connection[1]);
            dsu.union(index1, index2);
        }

        Map<Integer, Integer> componentSizes = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            int root = dsu.find(i);
            componentSizes.put(root, componentSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(componentSizes.values());
        sizes.sort(Collections.reverseOrder());

        if (!sizes.isEmpty()) {
            System.out.print(sizes.get(0));
            for (int i = 1; i < sizes.size(); i++) {
                System.out.print(" " + sizes.get(i));
            }
        }
    }
}