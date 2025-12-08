package by.it.group410902.barbashova.lesson14;

import java.util.*;
//Этот код решает задачу кластеризации веб-сайтов по их связям.
//Мы группируем сайты в "кластеры" (компоненты связности) на основе прямых ссылок между ними.
//Если сайт A ссылается на сайт B (или наоборот), они попадают в один кластер.

public class SitesB {
    static class DSU {
        int[] parent;  // хранит родителя для каждого элемента
        int[] rank;    // "ранг" или высота дерева для оптимизации

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {       // найти корень множества для элемента x
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {  // объединить множества элементов x и y
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                // Объединяем по рангу: к более высокому дереву подвешиваем более низкое
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();  // соответствие: имя сайта → индекс
        List<String> sites = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine();
            if ("end".equals(line)) break;

            String[] parts = line.split("\\+");
            String site1 = parts[0];
            String site2 = parts[1];

            // Добавляем сайты в словарь, если их еще нет
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, sites.size());
                sites.add(site1);
            }
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, sites.size());
                sites.add(site2);
            }

            // Сохраняем связь между сайтами как пару индексов
            edges.add(new int[]{siteToIndex.get(site1), siteToIndex.get(site2)});
        }

        int n = sites.size();
        DSU dsu = new DSU(n);

        // Объединяем сайты, связанные друг с другом
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        // Подсчет размеров кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }


        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort(Collections.reverseOrder());


        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}