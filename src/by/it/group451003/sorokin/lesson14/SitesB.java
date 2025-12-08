package by.it.group451003.sorokin.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        List<String> sites = new ArrayList<>();
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            String[] parts = line.split("\\+");
            String site1 = parts[0];
            String site2 = parts[1];

            if (!siteToId.containsKey(site1)) {
                siteToId.put(site1, sites.size());
                sites.add(site1);
                dsu.add();
            }
            if (!siteToId.containsKey(site2)) {
                siteToId.put(site2, sites.size());
                sites.add(site2);
                dsu.add();
            }

            int id1 = siteToId.get(site1);
            int id2 = siteToId.get(site2);
            dsu.union(id1, id2);
        }

        int[] clusterSizes = new int[sites.size()];
        for (int i = 0; i < sites.size(); i++) {
            int root = dsu.find(i);
            clusterSizes[root]++;
        }

        int count = 0;
        for (int i = 0; i < sites.size(); i++) {
            if (clusterSizes[i] > 0) {
                count++;
            }
        }

        int[] sizes = new int[count];
        int index = 0;
        for (int i = 0; i < sites.size(); i++) {
            if (clusterSizes[i] > 0) {
                sizes[index++] = clusterSizes[i];
            }
        }

        // Сортировка по убыванию
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sizes[j] < sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
    }

    static class DSU {
        private List<Integer> parent;
        private List<Integer> rank;

        DSU() {
            parent = new ArrayList<>();
            rank = new ArrayList<>();
        }

        void add() {
            parent.add(parent.size());
            rank.add(0);
        }

        int find(int x) {
            if (parent.get(x) != x) {
                parent.set(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (rank.get(rootX) < rank.get(rootY)) {
                parent.set(rootX, rootY);
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.set(rootY, rootX);
            } else {
                parent.set(rootY, rootX);
                rank.set(rootX, rank.get(rootX) + 1);
            }
        }
    }
}
