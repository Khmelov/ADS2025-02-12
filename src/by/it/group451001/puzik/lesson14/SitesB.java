package by.it.group451001.puzik.lesson14;

import java.util.*;

public class SitesB {
    // DSU with union by size and path compression
    static class DSU {
        Map<String, String> parent;
        Map<String, Integer> size;
        
        DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }
        
        void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
        }
        
        String find(String x) {
            makeSet(x);
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // path compression
            }
            return parent.get(x);
        }
        
        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (rootX.equals(rootY)) return;
            
            // union by size
            int sizeX = size.get(rootX);
            int sizeY = size.get(rootY);
            
            if (sizeX < sizeY) {
                parent.put(rootX, rootY);
                size.put(rootY, sizeX + sizeY);
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, sizeX + sizeY);
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
            
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();
                dsu.union(site1, site2);
            }
        }
        
        // Collect cluster sizes
        Map<String, Integer> clusterSizes = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            if (!visited.contains(root)) {
                visited.add(root);
                clusterSizes.put(root, dsu.getSize(root));
            }
        }
        
        // Extract sizes and sort in descending order (to match test)
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, (a, b) -> b - a);
        
        // Output sizes in descending order
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}

