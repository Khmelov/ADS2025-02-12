package by.it.group451003.burshtyn.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
        }

        String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (rootX.equals(rootY)) return;

            if (size.get(rootX) < size.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        int getComponentSize(String x) {
            return size.get(find(x));
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

            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                continue;
            }

            String site1 = sites[0];
            String site2 = sites[1];

            dsu.makeSet(site1);
            dsu.makeSet(site2);
            dsu.union(site1, site2);
        }

        Map<String, Integer> componentSizes = new HashMap<>();
        Map<String, Boolean> rootVisited = new HashMap<>();

        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            if (!rootVisited.containsKey(root)) {
                rootVisited.put(root, true);
                componentSizes.put(root, dsu.getComponentSize(root));
            }
        }

        int[] sizes = new int[componentSizes.size()];
        int index = 0;
        for (int size : componentSizes.values()) {
            sizes[index++] = size;
        }

        for (int i = 0; i < sizes.length - 1; i++) {
            for (int j = 0; j < sizes.length - i - 1; j++) {
                if (sizes[j] < sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < sizes.length; i++) {
            System.out.print(sizes[i]);
            if (i < sizes.length - 1) {
                System.out.print(" ");
            }
        }
    }
}