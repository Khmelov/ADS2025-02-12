package lesson14;

import java.util.*;
import java.util.stream.Collectors;

import java.util.*;
import java.util.stream.Collectors;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        List<String[]> pairs = new ArrayList<>();

        // Чтение входных данных
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if ("end".equals(line)) break;
            String[] sites = line.split("\\+");
            pairs.add(sites);
            siteToId.putIfAbsent(sites[0], siteToId.size());
            siteToId.putIfAbsent(sites[1], siteToId.size());
        }

        // Инициализация DSU
        DSU dsu = new DSU(siteToId.size());

        // Объединение пар сайтов
        for (String[] pair : pairs) {
            int id1 = siteToId.get(pair[0]);
            int id2 = siteToId.get(pair[1]);
            dsu.union(id1, id2);
        }

        // Подсчет размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < siteToId.size(); i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортировка и вывод результатов
        List<Integer> sortedSizes = clusterSizes.values().stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        sortedSizes.forEach(size -> System.out.print(size + " "));
    }

    static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Сжатие пути
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // Объединение по рангу
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