package by.it.group410901.zdanovich.lesson14;

import java.util.*;

public class SitesB {

    // DSU с эвристикой по размеру и сжатием путей
    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;  // каждый элемент - корень
                size[i] = 1;    // начальный размер
            }
        }

        // Поиск корня со сжатием путей
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // сжатие пути
            }
            return parent[x];
        }

        // Объединение с эвристикой по размеру
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // Объединяем меньшее дерево с большим
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

        // Map для связи имени сайта с индексом
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> indexToSite = new ArrayList<>();
        List<String[]> connections = new ArrayList<>();

        // Чтение входных данных до строки "end"
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }

            // Разделение строки на два сайта
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                connections.add(new String[]{site1, site2});

                // Добавление сайтов в mapping
                if (!siteToIndex.containsKey(site1)) {
                    siteToIndex.put(site1, indexToSite.size());
                    indexToSite.add(site1);
                }
                if (!siteToIndex.containsKey(site2)) {
                    siteToIndex.put(site2, indexToSite.size());
                    indexToSite.add(site2);
                }
            }
        }

        int totalSites = indexToSite.size();

        // Инициализация DSU
        DSU dsu = new DSU(totalSites);

        // Обработка всех соединений
        for (String[] connection : connections) {
            String site1 = connection[0];
            String site2 = connection[1];

            int index1 = siteToIndex.get(site1);
            int index2 = siteToIndex.get(site2);

            // Объединение сайтов в один кластер
            dsu.union(index1, index2);
        }

        // Сбор размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < totalSites; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Извлечение и сортировка размеров по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}
