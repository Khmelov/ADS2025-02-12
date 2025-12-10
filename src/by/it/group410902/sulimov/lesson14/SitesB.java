package by.it.group410902.sulimov.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }

        public void Set(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Сжатие пути
            }
            return parent.get(x);
        }

        public void merge(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return;
            }

            int rankX = rank.get(rootX);
            int rankY = rank.get(rootY);

            if (rankX < rankY) {
                parent.put(rootX, rootY);
            } else if (rankX > rankY) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rankX + 1);
            }
        }

        public List<Integer> getClusterSizes() {
            Map<String, Integer> clusterSizes = new HashMap<>();

            for (String site : parent.keySet()) {
                String root = find(site);
                clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
            }

            List<Integer> sizes = new ArrayList<>(clusterSizes.values());
            sizes.sort(Collections.reverseOrder());
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
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();

                dsu.Set(site1);
                dsu.Set(site2);

                dsu.merge(site1, site2);
            }
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        StringJoiner output = new StringJoiner(" ");
        for (Integer size : clusterSizes) {
            output.add(String.valueOf(size));
        }

        System.out.println(output.toString());
        scanner.close();
    }
}