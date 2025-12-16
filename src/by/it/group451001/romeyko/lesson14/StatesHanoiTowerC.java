package by.it.group451001.romeyko.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    static class DSU {
        int[] parent;
        int[] size;
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int a, int b) {
            a = find(a); b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) { int t = a; a = b; b = t; }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    static int[][] pegs;
    static int[] top;
    static int[] labels;
    static int moveCounter;

    static void push(int peg, int disk) {
        pegs[peg][top[peg]++] = disk;
    }
    static int pop(int peg) {
        return pegs[peg][--top[peg]];
    }

    static void recordMove() {
        int hA = top[0], hB = top[1], hC = top[2];
        int mx = hA;
        if (hB > mx) mx = hB;
        if (hC > mx) mx = hC;
        labels[moveCounter++] = mx;
    }


    static void hanoiSim(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoiSim(n - 1, from, aux, to);
        int d = pop(from);
        push(to, d);
        recordMove();
        hanoiSim(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) { sc.close(); return; }
        int N = sc.nextInt();
        sc.close();
        if (N <= 0) {
            System.out.println();
            return;
        }

        int totalMoves = (1 << N) - 1;
        labels = new int[totalMoves];

        pegs = new int[3][N];
        top = new int[3];

        for (int disk = N; disk >= 1; disk--) push(0, disk);
        moveCounter = 0;
        hanoiSim(N, 0, 1, 2);

        DSU dsu = new DSU(totalMoves);
        int[] firstIndex = new int[N + 1];
        for (int i = 0; i <= N; i++) firstIndex[i] = -1;

        for (int i = 0; i < totalMoves; i++) {
            int h = labels[i];
            if (firstIndex[h] == -1) firstIndex[h] = i;
            else dsu.union(firstIndex[h], i);
        }

        int[] compCount = new int[totalMoves];
        for (int i = 0; i < totalMoves; i++) {
            int r = dsu.find(i);
            compCount[r]++;
        }

        int[] result = new int[totalMoves];
        int rs = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (compCount[i] > 0) { result[rs++] = compCount[i]; }
        }

        Arrays.sort(result, 0, rs);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rs; i++) {
            if (i > 0) sb.append(' ');
            sb.append(result[i]);
        }
        System.out.println(sb.toString());
    }
}
