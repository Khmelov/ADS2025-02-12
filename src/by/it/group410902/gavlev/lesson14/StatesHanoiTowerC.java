package by.it.group410902.gavlev.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
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
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) { int t = a; a = b; b = t; }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    static int[] A, B, C;
    static int ta, tb, tc;
    static int stepCount;
    static int[] maxAtStep;
    static DSU dsu;
    static int[] rep;

    static void pushA(int x) { A[ta++] = x; }
    static int popA() { return A[--ta]; }
    static void pushB(int x) { B[tb++] = x; }
    static int popB() { return B[--tb]; }
    static void pushC(int x) { C[tc++] = x; }
    static int popC() { return C[--tc]; }

    static void recordStep() {
        int maxH = ta;
        if (tb > maxH) maxH = tb;
        if (tc > maxH) maxH = tc;
        int idx = stepCount++;
        maxAtStep[idx] = maxH;

        if (rep[maxH] == -1) {
            rep[maxH] = idx;
        } else {
            dsu.union(rep[maxH], idx);
        }
    }

    static void move(int n, char from, char to, char aux) {
        if (n == 0) return;
        move(n - 1, from, aux, to);
        if (from == 'A' && to == 'B') { pushB(popA()); }
        else if (from == 'A' && to == 'C') { pushC(popA()); }
        else if (from == 'B' && to == 'A') { pushA(popB()); }
        else if (from == 'B' && to == 'C') { pushC(popB()); }
        else if (from == 'C' && to == 'A') { pushA(popC()); }
        else if (from == 'C' && to == 'B') { pushB(popC()); }
        recordStep();
        move(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int M = (1 << N) - 1;
        A = new int[N];
        B = new int[N];
        C = new int[N];
        ta = tb = tc = 0;

        for (int i = N; i >= 1; i--) pushA(i);

        maxAtStep = new int[M];
        dsu = new DSU(M);
        rep = new int[N + 1];
        for (int i = 0; i <= N; i++) rep[i] = -1;
        stepCount = 0;

        move(N, 'A', 'B', 'C');

        int rootsCount = 0;
        boolean[] seenRoot = new boolean[M];
        for (int i = 0; i < M; i++) {
            int r = dsu.find(i);
            if (!seenRoot[r]) {
                seenRoot[r] = true;
                rootsCount++;
            }
        }

        int[] sizes = new int[rootsCount];
        int pos = 0;
        for (int i = 0; i < M; i++) {
            int r = dsu.find(i);
            if (seenRoot[r]) {
                sizes[pos++] = dsu.size[r];
                seenRoot[r] = false;
            }
        }

        Arrays.sort(sizes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.print(sb.toString());
    }
}
