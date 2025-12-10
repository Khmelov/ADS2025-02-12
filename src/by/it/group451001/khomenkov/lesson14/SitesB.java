package by.it.group451001.khomenkov.lesson14;

import java.util.*;
import java.util.stream.Collectors;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<int[]> edges = new ArrayList<>();
        int indexCounter = 0;

        // Чтение входных данных
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if ("end".equals(line)) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                continue;
            }

            String site1 = sites[0];
            String site2 = sites[1];

            // Добавляем сайты в маппинг, если их еще нет
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, indexCounter++);
            }
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, indexCounter++);
            }

            int idx1 = siteToIndex.get(site1);
            int idx2 = siteToIndex.get(site2);
            edges.add(new int[]{idx1, idx2});
        }

        int n = siteToIndex.size();
        DSU dsu = new DSU(n);

        // Объединение связанных сайтов
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        // Подсчет размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортировка размеров кластеров по убыванию
        List<Integer> sizes = clusterSizes.values()
                .stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        // Вывод результата
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
    }

    // Класс DSU с эвристикой сжатия путей и объединением по рангу
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
                parent[x] = find(parent[x]); // Сжатие пути
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot) return;

            // Объединение по рангу
            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else if (rank[xRoot] > rank[yRoot]) {
                parent[yRoot] = xRoot;
            } else {
                parent[yRoot] = xRoot;
                rank[xRoot]++;
            }
        }
    }
}