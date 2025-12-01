package by.it.group451002.sidarchuk.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        public String find(String x) {
            // Эвристика сокращения пути
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return; // уже в одном множестве
            }

            // Эвристика объединения по рангу/размеру
            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        public void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        public Map<String, Integer> getClusterSizes() {
            Map<String, Integer> clusterSizes = new HashMap<>();

            for (String site : parent.keySet()) {
                String root = find(site);
                clusterSizes.put(root, size.get(root));
            }

            return clusterSizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            // Разделяем строку по символу "+"
            String[] sites = line.split("\\+");

            if (sites.length != 2) {
                continue; // пропускаем некорректные строки
            }

            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            // Создаем множества для каждого сайта, если их еще нет
            dsu.makeSet(site1);
            dsu.makeSet(site2);

            // Объединяем сайты в один кластер
            dsu.union(site1, site2);
        }

        scanner.close();

        // Получаем размеры кластеров
        Map<String, Integer> clusterSizes = dsu.getClusterSizes();

        // Собираем уникальные размеры кластеров
        List<Integer> sizes = new ArrayList<>();
        Set<String> processedRoots = new HashSet<>();

        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            if (!processedRoots.contains(root)) {
                sizes.add(dsu.size.get(root));
                processedRoots.add(root);
            }
        }

        // Сортируем размеры кластеров в порядке УБЫВАНИЯ
        sizes.sort(Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
    }
}
