package by.it.group451001.volynets.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    static class Point {
        long x, y, z;
        Point(long x, long y, long z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String first = br.readLine();
        if (first == null || first.trim().isEmpty()) return;
        StringTokenizer st = new StringTokenizer(first);
        double D = Double.parseDouble(st.nextToken());
        int N = Integer.parseInt(st.nextToken());

        Point[] pts = new Point[N];
        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = br.readLine();
            }
            if (line == null) break;
            StringTokenizer s = new StringTokenizer(line);
            long x = Long.parseLong(s.nextToken());
            long y = Long.parseLong(s.nextToken());
            long z = Long.parseLong(s.nextToken());
            pts[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(N);
        double d2 = D * D;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                long dx = pts[i].x - pts[j].x;
                long dy = pts[i].y - pts[j].y;
                long dz = pts[i].z - pts[j].z;
                double dist2 = (double) dx * dx + (double) dy * dy + (double) dz * dz;
                if (dist2 < d2) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int r = dsu.find(i);
            compSize.put(r, compSize.getOrDefault(r, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(compSize.values());
        Collections.sort(sizes);
        Collections.reverse(sizes);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) out.append(' ');
            out.append(sizes.get(i));
        }
        System.out.println(out.toString());
    }
}