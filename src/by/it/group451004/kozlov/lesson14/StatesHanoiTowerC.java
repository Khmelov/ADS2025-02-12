package by.it.group451004.kozlov.lesson14;


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
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
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

    // Rod stacks implemented with arrays and tops
    static int[] A, B, C;
    static int topA, topB, topC;
    static int[] maxHeight; // max of heights(A,B,C) per step
    static int stepCount;

    static void move(int n, char from, char to, char aux) {
        if (n == 0) return;
        move(n - 1, from, aux, to);
        transfer(from, to);
        move(n - 1, aux, to, from);
    }

    static void transfer(char from, char to) {
        int disk = 0;
        if (from == 'A') disk = A[--topA];
        else if (from == 'B') disk = B[--topB];
        else disk = C[--topC];

        if (to == 'A') A[topA++] = disk;
        else if (to == 'B') B[topB++] = disk;
        else C[topC++] = disk;

        int hA = topA, hB = topB, hC = topC;
        int max = hA;
        if (hB > max) max = hB;
        if (hC > max) max = hC;

        maxHeight[stepCount++] = max; // record after each move
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int totalSteps = (1 << N) - 1; // 2^N - 1
        A = new int[N];
        B = new int[N];
        C = new int[N];
        topA = N; topB = 0; topC = 0;


        for (int i = 0; i < N; i++) {
            A[i] = N - i;
        }

        maxHeight = new int[totalSteps];
        stepCount = 0;

        // Solve: move from A to B using C
        move(N, 'A', 'B', 'C');

        // DSU over steps
        DSU dsu = new DSU(totalSteps);

        // Bucket steps by their max height without collections
        int[] countByH = new int[N + 1];
        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeight[i];
            countByH[h]++;
        }
        int[][] groups = new int[N + 1][];
        for (int h = 0; h <= N; h++) {
            groups[h] = new int[countByH[h]];
        }
        int[] writePos = new int[N + 1];
        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeight[i];
            groups[h][writePos[h]++] = i;
        }

        // Union steps within each same-height group
        for (int h = 0; h <= N; h++) {
            int len = groups[h].length;
            if (len > 1) {
                int first = groups[h][0];
                for (int k = 1; k < len; k++) {
                    dsu.union(first, groups[h][k]);
                }
            }
        }

        // Collect sizes of clusters (roots)
        int[] clusterSizes = new int[totalSteps];
        int clusterCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.find(i) == i) {
                clusterSizes[clusterCount++] = dsu.size[i];
            }
        }

        // Sort ascending (simple selection sort to avoid collections)
        for (int i = 0; i < clusterCount - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < clusterCount; j++) {
                if (clusterSizes[j] < clusterSizes[minIdx]) {
                    minIdx = j;
                }
            }
            int tmp = clusterSizes[i];
            clusterSizes[i] = clusterSizes[minIdx];
            clusterSizes[minIdx] = tmp;
        }


        // Output
        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i]);
            if (i < clusterCount - 1) System.out.print(" ");
        }
    }
}
