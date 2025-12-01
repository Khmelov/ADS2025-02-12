package by.it.group451001.volynets.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
            }
        }

        int find(int v) {
            if (parent[v] != v) parent[v] = find(parent[v]);
            return parent[v];
        }

        void add(int x) {
            size[x] += 1;
        }
    }

    static DSU dsu;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        if (N <= 0) {
            System.out.println();
            return;
        }

        dsu = new DSU(N);
        int[] h = {N, 0, 0}; // высоты A,B,C

        solveHanoi(N, 'A', 'B', 'C', h);

        int[] groups = new int[N + 1];
        int cnt = 0;
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            if (dsu.size[root] > 0 && groups[root] == 0) {
                groups[root] = dsu.size[root];
                cnt++;
            }
        }

        int[] res = new int[cnt];
        int k = 0;
        for (int i = 1; i <= N; i++) {
            if (groups[i] > 0) res[k++] = groups[i];
        }

        sort(res, 0, res.length - 1);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i > 0) out.append(' ');
            out.append(res[i]);
        }
        System.out.println(out.toString());
    }

    static void solveHanoi(int n, char from, char to, char aux, int[] h) {
        if (n == 0) return;
        solveHanoi(n - 1, from, aux, to, h);
        move(from, to, h);
        solveHanoi(n - 1, aux, to, from, h);
    }

    static void move(char from, char to, int[] h) {
        if (from == 'A') h[0]--; else if (from == 'B') h[1]--; else h[2]--;
        if (to == 'A') h[0]++; else if (to == 'B') h[1]++; else h[2]++;

        int maxH = h[0];
        if (h[1] > maxH) maxH = h[1];
        if (h[2] > maxH) maxH = h[2];

        dsu.add(maxH);
    }

    static void sort(int[] a, int l, int r) {
        if (l >= r) return;
        int i = l, j = r, p = a[l + (r - l) / 2];
        while (i <= j) {
            while (a[i] < p) i++;
            while (a[j] > p) j--;
            if (i <= j) {
                int t = a[i]; a[i] = a[j]; a[j] = t;
                i++; j--;
            }
        }
        if (l < j) sort(a, l, j);
        if (i < r) sort(a, i, r);
    }
}