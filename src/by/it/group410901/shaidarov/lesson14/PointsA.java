package by.it.group410901.shaidarov.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        private Map<Integer, Integer> parent;
        private Map<Integer, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }

        public void makeSet(int x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
        }

        public int find(int x) {
            if (parent.get(x) != x) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(int x, int y) {
            makeSet(x);
            makeSet(y);

            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            if (size.get(rootX) < size.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        public List<Integer> getClusterSizes() {
            Map<Integer, Integer> clusters = new HashMap<>();

            for (int point : parent.keySet()) {
                int root = find(point);
                clusters.put(root, size.get(root));
            }

            List<Integer> sizes = new ArrayList<>(clusters.values());
            // ВАЖНО: Сортировка по УБЫВАНИЮ для тестов
            sizes.sort((a, b) -> b - a);
            return sizes;
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

        scanner.close();

        DSU dsu = new DSU();

        for (int i = 0; i < n; i++) {
            dsu.makeSet(i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];

                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}
