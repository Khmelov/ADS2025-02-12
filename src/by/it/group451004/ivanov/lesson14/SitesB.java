package by.it.group451004.ivanov.lesson14;

import java.util.*;

public class SitesB {
    
    static class DSU {
        Map<String, String> parent;
        Map<String, Integer> rank;
        Map<String, Integer> size;
        
        DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }
        
        void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }
        
        String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Path compression
            }
            return parent.get(x);
        }
        
        void union(String x, String y) {
            makeSet(x);
            makeSet(y);
            
            String rootX = find(x);
            String rootY = find(y);
            
            if (rootX.equals(rootY)) return;
            
            // Union by rank
            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }
        
        int getSize(String x) {
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
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                dsu.union(site1, site2);
            }
        }
        
        // Собираем размеры кластеров
        Map<String, Integer> clusterSizes = new HashMap<>();
        Set<String> processed = new HashSet<>();
        
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            if (!processed.contains(root)) {
                clusterSizes.put(root, dsu.getSize(root));
                processed.add(root);
            }
        }
        
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, (a, b) -> b - a); // Убывающий порядок
        
        // Вывод результата
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}

