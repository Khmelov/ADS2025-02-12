package by.it.group451001.russu.lesson14;


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
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    static double dist(int[] p1, int[] p2) {
        long dx = p1[0] - p2[0];
        long dy = p1[1] - p2[1];
        long dz = p1[2] - p2[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();

        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
            points[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(N);


        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (dist(points[i], points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }


        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i + 1 < sizes.size()) System.out.print(" ");
        }


    }
}
