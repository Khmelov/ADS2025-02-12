package by.it.group451004.volynets.lesson14;

import java.util.*;

class DSU2 {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    private Map<String, Integer> size;

    public DSU2() {
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

    public List<Integer> getComponentSizes() {
        List<Integer> sizes = new ArrayList<>();
        Set<String> roots = new HashSet<>();

        for (String site : parent.keySet()) {
            String root = find(site);
            if (!roots.contains(root)) {
                roots.add(root);
                sizes.add(size.get(root));
            }
        }

        // Сортируем в порядке убывания
        Collections.sort(sizes, Collections.reverseOrder());
        return sizes;
    }
}

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU2 dsu = new DSU2();

        while (true) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                continue;
            }

            String site1 = sites[0].trim(); // создаём множество
            String site2 = sites[1].trim();

            dsu.makeSet(site1);
            dsu.makeSet(site2);

            dsu.union(site1, site2); // объединяем сайты в один кластер
        }

        scanner.close();

        List<Integer> clusterSizes = dsu.getComponentSizes();

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }
}