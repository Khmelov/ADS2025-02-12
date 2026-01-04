package by.it.group451004.rak.lesson14;

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
            if (parent[x] != x)
                parent[x] = find(parent[x]); //сжатие пут
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            if (size[a] < size[b]) {
                parent[a] = b;
                size[b] += size[a];
            } else {
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    static boolean closeEnough(int[] A, int[] B, int D2) {
        int dx = A[0] - B[0];
        int dy = A[1] - B[1];
        int dz = A[2] - B[2];
        return dx * dx + dy * dy + dz * dz < D2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int D = sc.nextInt();
        int N = sc.nextInt();
        int D2 = D * D;

        int[][] pts = new int[N][3];
        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextInt();
            pts[i][1] = sc.nextInt();
            pts[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                if (closeEnough(pts[i], pts[j], D2))
                    dsu.union(i, j); //кластрзация

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            cnt.put(root, cnt.getOrDefault(root, 0) + 1);
        }

        List<Integer> res = new ArrayList<>(cnt.values());
        res.sort(Collections.reverseOrder());

        for (int x : res)
            System.out.print(x + " ");
    }
}