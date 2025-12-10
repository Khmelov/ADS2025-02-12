package by.it.group451001.romeyko.lesson14;

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

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // объединяем по размеру
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }

    static class Point {
        int x, y, z;
        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(N);

        double D2 = D * D;

        // проверяем расстояния между всеми парами
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                long dx = points[i].x - points[j].x;
                long dy = points[i].y - points[j].y;
                long dz = points[i].z - points[j].z;

                double dist2 = dx * dx + dy * dy + dz * dz;

                if (dist2 < D2) {
                    dsu.union(i, j);
                }
            }
        }

        // считаем размеры компонент
        Map<Integer, Integer> comp = new HashMap<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            comp.put(root, comp.getOrDefault(root, 0) + 1);
        }

        // сортируем размеры
        List<Integer> sizes = new ArrayList<>(comp.values());
        Collections.sort(sizes, Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i + 1 < sizes.size()) System.out.print(" ");
        }
    }
}
