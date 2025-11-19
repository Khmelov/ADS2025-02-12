package by.it.group410901.zubchonak.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> sites = new ArrayList<>();
        List<String[]> pairs = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) continue; // защита от пустых строк
            String[] pair = line.split("\\+");
            if (pair.length < 2) continue; // на случай косяков ввода
            pairs.add(new String[]{pair[0].trim(), pair[1].trim()});
            for (String site : new String[]{pair[0].trim(), pair[1].trim()}) {
                siteToIndex.putIfAbsent(site, sites.size());
                if (!siteToIndex.containsKey(site)) {
                    sites.add(site);
                }
            }
        }

        sites.clear();
        siteToIndex.clear();
        for (String[] pair : pairs) {
            for (String site : pair) {
                if (!siteToIndex.containsKey(site)) {
                    siteToIndex.put(site, sites.size());
                    sites.add(site);
                }
            }
        }

        DSU dsu = new DSU(sites.size());
        for (String[] pair : pairs) {
            int idx1 = siteToIndex.get(pair[0]);
            int idx2 = siteToIndex.get(pair[1]);
            dsu.union(idx1, idx2);
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder()); // по убыванию — как в примере

        // Вывод без лишнего пробела
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }

    static class DSU {
        private final int[] parent;
        private final int[] size;

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
                parent[x] = find(parent[x]); // сжатие путей
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;

            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                size[ry] += size[rx];
            } else {
                parent[ry] = rx;
                size[rx] += size[ry];
            }
        }
    }
}