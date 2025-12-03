package by.it.group451001.apanovich.lesson14;

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
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) return;
        double D = sc.nextDouble();
        int N = sc.nextInt();
        double[][] pts = new double[N][3];
        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextDouble();
            pts[i][1] = sc.nextDouble();
            pts[i][2] = sc.nextDouble();
        }

        DSU dsu = new DSU(N);
        double D2 = D * D;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double dz = pts[i][2] - pts[j][2];
                if (dx * dx + dy * dy + dz * dz < D2) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int r = dsu.find(i);
            clusters.put(r, clusters.getOrDefault(r, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusters.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}
