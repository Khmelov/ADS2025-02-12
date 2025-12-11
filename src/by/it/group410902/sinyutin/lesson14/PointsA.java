package by.it.group410902.sinyutin.lesson14;

import java.util.*;

public class PointsA {

    // dsu
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
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else if (rank[rb] < rank[ra]) {
                parent[rb] = ra;
                size[ra] += size[rb];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
                rank[ra]++;
            }
        }
    }

    // расстояние между точками
    static double dist3D(int[] a, int[] b) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    // main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();

        int[][] pts = new int[N][3];

        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextInt();
            pts[i][1] = sc.nextInt();
            pts[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(N);

        // объединяем точки по расстоянию
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist3D(pts[i], pts[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // собираем размеры кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusters.put(root, dsu.size[root]);
        }

        // сортируем размеры по убыванию
        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort((a, b) -> b - a);

        // вывод
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}
