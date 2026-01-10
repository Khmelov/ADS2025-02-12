package by.it.group451003.chveikonstantcin.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] counts;
    static int stepIndex;
    static DSU dsu;
    static int[] heightTracker;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        int totalMoves = (1 << N) - 1;

        dsu = new DSU(totalMoves);

        counts = new int[3];
        counts[0] = N;
        counts[1] = 0;
        counts[2] = 0;

        heightTracker = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            heightTracker[i] = -1;
        }

        stepIndex = 0;

        solveHanoi(N, 0, 1, 2);

        int[] sizes = new int[totalMoves];
        int countSizes = 0;

        for (int i = 0; i < totalMoves; i++) {
            if (dsu.parent[i] == i) {
                sizes[countSizes++] = dsu.size[i];
            }
        }

        for (int i = 0; i < countSizes - 1; i++) {
            for (int j = 0; j < countSizes - i - 1; j++) {
                if (sizes[j] > sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countSizes; i++) {
            sb.append(sizes[i]);
            if (i < countSizes - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }

    private static void solveHanoi(int n, int from, int to, int aux) {
        if (n == 0) return;

        solveHanoi(n - 1, from, aux, to);

        counts[from]--;
        counts[to]++;

        int maxH = counts[0];
        if (counts[1] > maxH) maxH = counts[1];
        if (counts[2] > maxH) maxH = counts[2];

        if (heightTracker[maxH] != -1) {
            dsu.union(stepIndex, heightTracker[maxH]);
        }
        heightTracker[maxH] = stepIndex;

        stepIndex++;

        solveHanoi(n - 1, aux, to, from);
    }

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
            if (parent[x] == x) {
                return x;
            }
            return parent[x] = find(parent[x]);
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }
    }
}