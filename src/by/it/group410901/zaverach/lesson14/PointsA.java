package by.it.group410901.zaverach.lesson14;

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

        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (size[a] < size[b]) {
                    int t = a; a = b; b = t;
                }
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    static class Point {
        int x, y, z;
        Point(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] pts = new Point[N];
        for (int i = 0; i < N; i++) {
            pts[i] = new Point(sc.nextInt(), sc.nextInt(), sc.nextInt());
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {

                double dist = Math.hypot(
                        Math.hypot(pts[i].x - pts[j].x, pts[i].y - pts[j].y),
                        pts[i].z - pts[j].z
                );

                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int r = dsu.find(i);
            count.put(r, count.getOrDefault(r, 0) + 1);
        }

        List<Integer> res = new ArrayList<>(count.values());
        res.sort((a, b) -> b - a);

        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i));
            if (i + 1 < res.size()) System.out.print(" ");
        }
    }
}
