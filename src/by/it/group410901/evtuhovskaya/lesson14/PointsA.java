package by.it.group410901.evtuhovskaya.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent, size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i; size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);

            if (a == b) return;

            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        long D2compare = (long)Math.floor(D * D + 1e-9);

        int[][] pts = new int[N][3];
        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextInt();
            pts[i][1] = sc.nextInt();
            pts[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(N);
        //сравнение по квадрату расстояния: dx^2+dy^2+dz^2 < D^2
        double D2 = D * D;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                long dx = pts[i][0] - pts[j][0];
                long dy = pts[i][1] - pts[j][1];
                long dz = pts[i][2] - pts[j][2];
                double dist2 = (double)dx*dx + (double)dy*dy + (double)dz*dz;
                if (dist2 < D2) dsu.union(i, j);
            }
        }

        //подсчёт размеров кластеров
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int r = dsu.find(i);
            cnt.put(r, cnt.getOrDefault(r, 0) + 1);
        }

        int m = cnt.size();
        int[] sizes = new int[m];
        int idx = 0;
        for (int v : cnt.values()) sizes[idx++] = v;
        Arrays.sort(sizes);

        for (int i = m - 1; i >= 0; i--) {
            System.out.print(sizes[i]);
            if (i > 0) System.out.print(" ");
        }
    }
}