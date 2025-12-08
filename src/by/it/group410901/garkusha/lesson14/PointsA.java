package by.it.group410901.garkusha.lesson14;

import java.util.*;

public class PointsA {

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
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int d = scanner.nextInt();
        int n = scanner.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);

        // Объединяем точки, если расстояние между ними < d
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < d) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        // Сортируем размеры кластеров в порядке убывания (как в тестах)
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }

    static double distance(int[] p1, int[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}