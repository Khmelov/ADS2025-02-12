package by.it.group410901.shaidarov.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }

        public void makeSet(String site) {
            if (!parent.containsKey(site)) {
                parent.put(site, site);
                size.put(site, 1);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            makeSet(x);
            makeSet(y);

            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return;
            }

            if (size.get(rootX) < size.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        public List<Integer> getClusterSizes() {
            Map<String, Integer> clusters = new HashMap<>();

            for (String site : parent.keySet()) {
                String root = find(site);
                clusters.put(root, size.get(root));
            }

            List<Integer> sizes = new ArrayList<>(clusters.values());
            // ВАЖНО: Сортировка по УБЫВАНИЮ для тестов
            sizes.sort((a, b) -> b - a);
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            if (line.contains("+")) {
                String[] sites = line.split("\\+");
                if (sites.length == 2) {
                    String site1 = sites[0].trim();
                    String site2 = sites[1].trim();

                    dsu.union(site1, site2);
                }
            }
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}