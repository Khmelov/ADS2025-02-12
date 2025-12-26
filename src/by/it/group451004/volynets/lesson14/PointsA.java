package by.it.group451004.volynets.lesson14;

import java.util.*;

class Point3D {
    int x, y, z;

    Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double distanceSquared(Point3D other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        int dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    double distance(Point3D other) {
        return Math.sqrt(distanceSquared(other));
    }
}

class DSU {
    private int[] parent;
    private int[] rank;
    private int[] size;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
            rank[rootX]++;
        }
    }

    public int getSize(int x) {
        return size[find(x)];
    }

    public List<Integer> getComponentSizes() {
        List<Integer> sizes = new ArrayList<>();
        Set<Integer> roots = new HashSet<>();

        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            if (!roots.contains(root)) {
                roots.add(root);
                sizes.add(size[root]);
            }
        }

        // Сортируем в порядке убывания
        Collections.sort(sizes, Collections.reverseOrder());
        return sizes;
    }
}

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        Point3D[] points = new Point3D[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point3D(x, y, z);
        }

        DSU dsu = new DSU(n);

        // Используем квадрат расстояния для оптимизации
        double maxDistanceSquared = maxDistance * maxDistance;

        // Объединяем точки, если расстояние между ними меньше maxDistance
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distSquared = points[i].distanceSquared(points[j]);

                // Строго меньше maxDistance (интервал [0, D))
                if (distSquared < maxDistanceSquared) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = dsu.getComponentSizes();

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }

        scanner.close();
    }
}