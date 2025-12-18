package by.it.group410902.linnik.lesson14;
import java.util.*;
public class PointsA {
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceSquared(Point other) {
            int dx = this.x - other.x;
            int dy = this.y - other.y;
            int dz = this.z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    static class DSU {
        int[] parent;
        int[] rank;    // высота дерева
        int[] size;    // размер компоненты

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
                parent[x] = find(parent[x]);  // сжатие пути. чтобы все элементы указывали на корень
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // объединяем по рпнгу: меньший ранг присоединяем к большему
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

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(n);

        double maxDistanceSquared = maxDistance * maxDistance;
        for (int i = 0; i < n; i++) { //если расстояние между точками i j < то добавляем одну к другой
            for (int j = i + 1; j < n; j++) { //берем по очереди 0 1, 0 2...
                double distanceSquared = points[i].distanceSquared(points[j]);

                if (distanceSquared < maxDistanceSquared) {
                    dsu.union(i, j); //создаем кластер
                }
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i); //находим корень для текущей точки
            if (i == root) {
                clusterSizes.put(root, dsu.getSize(root)); //если точка - корень то добавляем в мап размер кластера
            }
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes);

        for (int i = sizes.size() - 1; i >= 0; i--) {
            if (i < sizes.size() - 1) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}
