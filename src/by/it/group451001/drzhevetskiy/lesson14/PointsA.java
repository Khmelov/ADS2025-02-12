package by.it.group451001.drzhevetskiy.lesson14;

import java.util.*;

public class PointsA {

    // Структура для объединения компонентов
    static class UnionFind {
        private final int[] link;
        private final int[] weight;

        UnionFind(int n) {
            link = new int[n];
            weight = new int[n];
            for (int i = 0; i < n; i++) {
                link[i] = i;
                weight[i] = 1;
            }
        }

        int root(int v) {
            while (v != link[v]) {
                link[v] = link[link[v]];
                v = link[v];
            }
            return v;
        }

        void attach(int x, int y) {
            int rx = root(x);
            int ry = root(y);
            if (rx == ry) return;

            if (weight[rx] < weight[ry]) {
                link[rx] = ry;
                weight[ry] += weight[rx];
            } else {
                link[ry] = rx;
                weight[rx] += weight[ry];
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if (!in.hasNext()) return;

        double limit = in.nextDouble();
        int count = in.nextInt();

        double[][] coords = new double[count][3];
        for (int i = 0; i < count; i++) {
            coords[i][0] = in.nextDouble();
            coords[i][1] = in.nextDouble();
            coords[i][2] = in.nextDouble();
        }

        UnionFind uf = new UnionFind(count);
        double sq = limit * limit;

        for (int a = 0; a < count; a++) {
            for (int b = a + 1; b < count; b++) {
                double dx = coords[a][0] - coords[b][0];
                double dy = coords[a][1] - coords[b][1];
                double dz = coords[a][2] - coords[b][2];
                if (dx * dx + dy * dy + dz * dz < sq) {
                    uf.attach(a, b);
                }
            }
        }

        Map<Integer, Integer> stat = new HashMap<>();
        for (int i = 0; i < count; i++) {
            int r = uf.root(i);
            stat.put(r, stat.getOrDefault(r, 0) + 1);
        }

        ArrayList<Integer> result = new ArrayList<>(stat.values());
        result.sort((x, y) -> Integer.compare(y, x));

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}
