package by.it.group451004.momotyuk.lesson14;

import java.util.*;

class DSUSites {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    private Map<String, Integer> sizeMap;

    public DSUSites() {
        parent = new HashMap<>();
        rank = new HashMap<>();
        sizeMap = new HashMap<>();
    }

    public void makeSet(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            rank.put(x, 0);
            sizeMap.put(x, 1);
        }
    }

    public String find(String x) {
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    public void union(String x, String y) {
        String rootX = find(x);
        String rootY = find(y);

        if (rootX.equals(rootY)) return;

        if (rank.get(rootX) < rank.get(rootY)) {
            parent.put(rootX, rootY);
            sizeMap.put(rootY, sizeMap.get(rootY) + sizeMap.get(rootX));
        } else if (rank.get(rootX) > rank.get(rootY)) {
            parent.put(rootY, rootX);
            sizeMap.put(rootX, sizeMap.get(rootX) + sizeMap.get(rootY));
        } else {
            parent.put(rootY, rootX);
            rank.put(rootX, rank.get(rootX) + 1);
            sizeMap.put(rootX, sizeMap.get(rootX) + sizeMap.get(rootY));
        }
    }

    public Map<String, Integer> getClusterSizes() {
        Map<String, Integer> result = new HashMap<>();
        for (String site : parent.keySet()) {
            String root = find(site);
            if (!result.containsKey(root)) {
                result.put(root, sizeMap.get(root));
            }
        }
        return result;
    }
}

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSUSites dsu = new DSUSites();

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

        Map<String, Integer> clusterSizes = dsu.getClusterSizes();
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортируем в порядке убывания (как в тесте)
        Collections.sort(sizes, Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }
}