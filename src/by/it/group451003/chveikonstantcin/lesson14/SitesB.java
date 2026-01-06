package by.it.group451003.chveikonstantcin.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        List<String> sites = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("end".equals(line)) {
                break;
            }
            String[] parts = line.split("\\+");
            String site1 = parts[0];
            String site2 = parts[1];

            if (!siteToId.containsKey(site1)) {
                siteToId.put(site1, sites.size());
                sites.add(site1);
            }
            if (!siteToId.containsKey(site2)) {
                siteToId.put(site2, sites.size());
                sites.add(site2);
            }

            int id1 = siteToId.get(site1);
            int id2 = siteToId.get(site2);
            edges.add(new int[]{id1, id2});
        }

        int n = sites.size();
        DSU dsu = new DSU(n);

        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        int[] sizes = new int[n];
        for (int i = 0; i < n; i++) {
            sizes[dsu.find(i)]++;
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (sizes[i] > 0) {
                result.add(sizes[i]);
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
                rank[i] = 0;
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