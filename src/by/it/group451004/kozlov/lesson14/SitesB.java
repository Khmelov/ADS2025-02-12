package by.it.group451004.kozlov.lesson14;

import java.util.*;
import java.util.stream.Collectors;

public class SitesB {

    private static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        public void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                // Эвристика сжатия пути
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return;
            }

            // Эвристика объединения по рангу
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

        public int getSize(String x) {
            return size.get(find(x));
        }

        public Collection<String> getRoots() {
            Set<String> roots = new HashSet<>();
            for (String site : parent.keySet()) {
                roots.add(find(site));
            }
            return roots;
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

            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                continue;
            }

            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            dsu.makeSet(site1);
            dsu.makeSet(site2);
            dsu.union(site1, site2);
        }

        scanner.close();

        // Получаем размеры всех кластеров
        List<Integer> clusterSizes = dsu.getRoots().stream()
                .map(dsu::getSize)
                .collect(Collectors.toList());

        // Сортируем в порядке убывания (как в тесте)
        clusterSizes.sort(Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}