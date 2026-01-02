package by.it.group410902.linnik.lesson14;

import java.util.*;

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

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // сжатие пути. чтобы все элементы указывали на корень
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
            if (!parent.containsKey(y)) {
                parent.put(y, y);
                rank.put(y, 0);
                size.put(y, 1);
            }

            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            if (rank.get(rootX) < rank.get(rootY)) { // объединяем по рпнгу: меньший ранг присоединяем к большему
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

        public List<Integer> getClusterSizes() {
            Map<String, Integer> clusterSizes = new HashMap<>();

            for (String site : parent.keySet()) {
                String root = find(site);
                clusterSizes.put(root, size.get(root)); //находимм корень и добавляем в кластер его и размер
            }

            List<Integer> sizes = new ArrayList<>(clusterSizes.values()); //преобразуем в список и сортируем
            Collections.sort(sizes);
            return sizes;
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
            if (sites.length == 2) {
                dsu.union(sites[0], sites[1]); //если в одном кластере ничего
            } //если в разных объединяются. если нет добавляются
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        for (int i = clusterSizes.size()-1; i >=0; i--) {
            if (i <clusterSizes.size()-1) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }

        scanner.close();
    }
}
