package by.it.group451004.redko.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {
        private final int[] parent;
        private final int[] size;

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

        int compSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) return;
        int D = in.nextInt();
        int n = in.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        int[] z = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.nextInt();
            y[i] = in.nextInt();
            z[i] = in.nextInt();
        }

        DSU dsu = new DSU(n);
        long d2 = 1L * D * D;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = x[i] - (long) x[j];
                long dy = y[i] - (long) y[j];
                long dz = z[i] - (long) z[j];
                long dist2 = dx * dx + dy * dy + dz * dz;
                if (dist2 < d2) dsu.union(i, j);
            }
        }

        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = dsu.find(i);
            count.put(r, dsu.size[r]);
        }
        List<Integer> sizes = new ArrayList<>(count.values());
        sizes.sort(Comparator.reverseOrder());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) out.append(' ');
            out.append(sizes.get(i));
        }
        System.out.println(out);
    }
}






