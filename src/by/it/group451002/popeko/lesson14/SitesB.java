package by.it.group451002.popeko.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent;
        Map<String, Integer> rank;

        DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                return x;
            }

            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // path compression
            }
            return parent.get(x);
        }

        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            // union by rank
            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) break;

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                dsu.union(sites[0], sites[1]);
            }
        }

        // Собираем размеры кластеров
        Map<String, Integer> clusterSizes = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортируем размеры кластеров по УБЫВАНИЮ
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результата
        for (int size : sizes) {
            System.out.print(size + " ");
        }
    }
}