package by.it.group410901.garkusha.lesson14;

import java.util.*;

public class SitesB {

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
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> siteToIndex = new HashMap<>(); // Отображение имени сайта в числовой индекс
        List<String> indexToSite = new ArrayList<>(); // Обратное отображение индекса в имя сайта
        List<int[]> edges = new ArrayList<>(); // Список ребер (связей между сайтами)

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length != 2) continue;

            String site1 = sites[0];
            String site2 = sites[1];

            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, indexToSite.size());
                indexToSite.add(site1);
            }
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, indexToSite.size());
                indexToSite.add(site2);
            }

            int idx1 = siteToIndex.get(site1);
            int idx2 = siteToIndex.get(site2);
            edges.add(new int[]{idx1, idx2});
        }

        int n = indexToSite.size();
        DSU dsu = new DSU(n);

        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}