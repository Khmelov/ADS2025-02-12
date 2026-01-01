package by.it.group451003.yepanchuntsau.lesson14;
import java.util.*;

public class PointsA {

    private static class DSU {
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
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (size[ra] < size[rb]) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) return;

        double D = sc.nextDouble();
        int N = sc.nextInt();

        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];

        for (int i = 0; i < N; i++) {
            x[i] = sc.nextDouble();
            y[i] = sc.nextDouble();
            z[i] = sc.nextDouble();
        }

        DSU dsu = new DSU(N);
        double D2 = D * D;


        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dz = z[i] - z[j];
                double dist2 = dx * dx + dy * dy + dz * dz;

                if (dist2 < D2) {
                    dsu.union(i, j);
                }
            }
        }


        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            compSize.put(root, compSize.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(compSize.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}
