package by.it.group451004.ivanov.lesson14;

import java.util.*;

public class PointsA {
    
    static class Point {
        int x, y, z;
        
        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        double distance(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2));
        }
    }
    
    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;
        
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return;
            
            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
                size[rootX] += size[rootY];
            }
        }
        
        int getSize(int x) {
            return size[find(x)];
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int n = scanner.nextInt();
        
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }
        
        DSU dsu = new DSU(n);
        
        // Объединяем точки, если расстояние между ними меньше заданного
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].distance(points[j]) < distance) {
                    dsu.union(i, j);
                }
            }
        }
        
        // Собираем размеры кластеров
        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < n; i++) {
            roots.add(dsu.find(i));
        }
        
        List<Integer> clusterSizes = new ArrayList<>();
        for (int root : roots) {
            clusterSizes.add(dsu.getSize(root));
        }
        
        Collections.sort(clusterSizes, (a, b) -> b - a); // Убывающий порядок (как в тесте)
        
        // Вывод результата
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}

