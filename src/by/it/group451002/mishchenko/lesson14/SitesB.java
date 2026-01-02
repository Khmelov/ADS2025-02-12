package by.it.group451002.mishchenko.lesson14;

import java.util.*;

public class SitesB {

    // Система непересекающихся множеств
    static class DSU {
        private int[] parent; // Родительские элементы
        private int[] rank;   // Ранги для балансировки
        private int[] size;   // Размеры множеств

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];

            // Инициализация каждого элемента как корня
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Эвристика сжатия пути (path compression)
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Рекурсивное сжатие пути
            }
            return parent[x];
        }

        // Эвристика объединения по рангу (union by rank)
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return; // Уже в одном множестве

            // Объединение по рангу для минимизации высоты дерева
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                rank[rootX]++;
            }
        }

        // Получить размер множества
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Собираем все уникальные сайты
        Set<String> allSites = new HashSet<>();
        List<String[]> connections = new ArrayList<>();

        // Чтение связей между сайтами до ключевого слова "end"
        while (true) {
            String line = scanner.nextLine();
            if ("end".equals(line)) {
                break;
            }

            // Разделяем строку по символу '+'
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                connections.add(sites);
                allSites.add(sites[0]);
                allSites.add(sites[1]);
            }
        }

        // Создаем mapping: имя сайта -> индекс
        List<String> siteList = new ArrayList<>(allSites);
        Map<String, Integer> siteToIndex = new HashMap<>();
        for (int i = 0; i < siteList.size(); i++) {
            siteToIndex.put(siteList.get(i), i);
        }

        int n = siteList.size();
        DSU dsu = new DSU(n);

        // Объединяем связанные сайты в системе непересекающихся множеств
        for (String[] connection : connections) {
            int index1 = siteToIndex.get(connection[0]);
            int index2 = siteToIndex.get(connection[1]);
            dsu.union(index1, index2);
        }

        // Собираем размеры кластеров (компонент связности)
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Получаем список размеров и сортируем в порядке убывания
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}