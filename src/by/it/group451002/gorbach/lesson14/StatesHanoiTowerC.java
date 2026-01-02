package by.it.group451002.gorbach.lesson14;

import java.util.Scanner;

class StatesHanoiTowerC {
    static class DSU {
        private int[] parent;
        private int[] rank;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                    rank[rootX]++;
                }
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    static class TowerState {
        int[] disks;
        int height;

        public TowerState(int capacity) {
            disks = new int[capacity];
            height = 0;
        }

        public void push(int disk) {
            disks[height++] = disk;
        }

        public int pop() {
            return disks[--height];
        }

        public int top() {
            return height > 0 ? disks[height - 1] : 0;
        }

        public int getPyramidHeight() {
            return height;
        }
    }

    private static void solveHanoi(int n, TowerState from, TowerState to, TowerState aux,
                                   DSU dsu, int[] reps, int[] stepIdx, int maxN) {
        if (n > 0) {
            solveHanoi(n - 1, from, aux, to, dsu, reps, stepIdx, maxN);

            int disk = from.pop();
            to.push(disk);

            int maxh = Math.max(from.height, Math.max(to.height, aux.height));

            int step = stepIdx[0];
            if (reps[maxh] == -1) {
                reps[maxh] = step;
            } else {
                dsu.union(step, reps[maxh]);
            }
            stepIdx[0]++;

            solveHanoi(n - 1, aux, to, from, dsu, reps, stepIdx, maxN);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        if (N == 0) {
            System.out.println("0");
            return;
        }

        int totalMoves = (1 << N) - 1;
        DSU dsu = new DSU(totalMoves);

        int[] reps = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            reps[i] = -1;
        }

        int[] stepIdx = {0};

        TowerState a = new TowerState(N);
        TowerState b = new TowerState(N);
        TowerState c = new TowerState(N);

        for (int i = N; i >= 1; i--) {
            a.push(i);
        }

        solveHanoi(N, a, b, c, dsu, reps, stepIdx, N);

        int clusterCount = 0;
        int[] clusterSizes = new int[N];
        for (int h = 1; h <= N; h++) {
            if (reps[h] != -1) {
                clusterSizes[clusterCount++] = dsu.getSize(reps[h]);
            }
        }

        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = 0; j < clusterCount - i - 1; j++) {
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i]);
            if (i < clusterCount - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}