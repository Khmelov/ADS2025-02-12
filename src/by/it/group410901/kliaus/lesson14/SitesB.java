package by.it.group410901.kliaus.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent;
        Map<String, Integer> rank;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }

        public void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                // Path compression - эвристика сжатия пути
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (!rootX.equals(rootY)) {
                // Union by rank - эвристика по рангу
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

                dsu.makeSet(site1);
                dsu.makeSet(site2);
                dsu.union(site1, site2);
            }
        }

        // Count cluster sizes
        Map<String, Integer> clusterSizes = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Sort cluster sizes in descending order
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Output
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}