package by.it.group451004.romanovskaya.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Path compression
            }
            return parent.get(x);
        }

        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (!rootX.equals(rootY)) {
                if (size.get(rootX) < size.get(rootY)) { // Union by size
                    String temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            String[] sites = line.split("\\+");
            dsu.union(sites[0], sites[1]);
        }

        Map<String, Integer> groups = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            groups.put(root, groups.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(groups.values());
        result.sort(Collections.reverseOrder());
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
