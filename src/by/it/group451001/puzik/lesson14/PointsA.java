package by.it.group451001.puzik.lesson14;

import java.util.*;

public class PointsA {
    // DSU with union by size and path compression
    static class DSU {
        int[] parent;
        int[] size;
        
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }
        
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;
            
            // union by size
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
        
        int getSize(int x) {
            return size[find(x)];
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double distance = scanner.nextDouble();
        int n = scanner.nextInt();
        
        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }
        
        DSU dsu = new DSU(n);
        
        // Union points that are within distance (NOT inclusive)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = Math.hypot(
                    Math.hypot(points[i][0] - points[j][0], points[i][1] - points[j][1]),
                    points[i][2] - points[j][2]
                );
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }
        
        // Collect cluster sizes - only count each root once
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        Set<Integer> processedRoots = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!processedRoots.contains(root)) {
                processedRoots.add(root);
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

