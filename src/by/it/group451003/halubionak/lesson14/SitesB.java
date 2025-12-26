package by.it.group451003.halubionak.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Map для хранения соответствия имени сайта и его индекса
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> indexToSite = new ArrayList<>();

        // Считываем пары сайтов
        List<String[]> pairs = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            // Разделяем пару сайтов
            String[] sites = line.split("\\+");
            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            // Добавляем сайты в map, если их еще нет
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, indexToSite.size());
                indexToSite.add(site1);
            }
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, indexToSite.size());
                indexToSite.add(site2);
            }

            pairs.add(new String[]{site1, site2});
        }

        int n = indexToSite.size(); // общее количество уникальных сайтов

        // Создаем DSU
        DSU dsu = new DSU(n);

        // Объединяем сайты из пар
        for (String[] pair : pairs) {
            int index1 = siteToIndex.get(pair[0]);
            int index2 = siteToIndex.get(pair[1]);
            dsu.union(index1, index2);
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортируем размеры кластеров по УБЫВАНИЮ
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    // Класс DSU с эвристикой по рангу и сжатием путей
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
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }

        // Union с union by rank
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // union by rank
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