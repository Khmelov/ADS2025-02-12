package by.it.group451004.bortsevich.lesson14;

import java.util.*;

class DSUPoints {
    private int[] parent;
    private int[] size;

    public DSUPoints(int n) {
        parent = new int[n];
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

        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    public int getSize(int x) {
        return size[find(x)];
    }
}

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        double[][] points = new double[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSUPoints dsu = new DSUPoints(N);

        // Объединение точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (distance < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        boolean[] visited = new boolean[N];
        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes.add(dsu.getSize(root));
            }
        }

        // Сортируем в порядке убывания (как в тесте)
        clusterSizes.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }

        scanner.close();
    }
}