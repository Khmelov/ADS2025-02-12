package by.it.group451003.avrusevich.lesson14;
import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int distanceRequired = scanner.nextInt();
        int count = scanner.nextInt();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points.add(new Point(x, y, z));
        }

        DSU dsu = new DSU(count);

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (points.get(i).distanceTo(points.get(j)) < distanceRequired) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> componentSizes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            int root = dsu.find(i);
            componentSizes.put(root, componentSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(componentSizes.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }

    static class Point {
        final int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
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
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}