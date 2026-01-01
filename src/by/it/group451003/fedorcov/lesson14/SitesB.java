package by.it.group451003.fedorcov.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
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

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> siteIndex = new HashMap<>();
        List<String[]> pairs = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length != 2) continue;

            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            pairs.add(new String[]{site1, site2});

            if (!siteIndex.containsKey(site1)) {
                siteIndex.put(site1, siteIndex.size());
            }
            if (!siteIndex.containsKey(site2)) {
                siteIndex.put(site2, siteIndex.size());
            }
        }

        int n = siteIndex.size();
        DSU dsu = new DSU(n);

        for (String[] pair : pairs) {
            int index1 = siteIndex.get(pair[0]);
            int index2 = siteIndex.get(pair[1]);
            dsu.union(index1, index2);
        }

        int[] clusterSizes = new int[n];
        for (int i = 0; i < n; i++) {
            clusterSizes[dsu.find(i)]++;
        }

        List<Integer> sizes = new ArrayList<>();
        for (int size : clusterSizes) {
            if (size > 0) {
                sizes.add(size);
            }
        }

        sizes.sort((a, b) -> b - a);

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        scanner.close();
    }
}