package by.it.group451003.plyushchevich.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;
    static int[] heights = new int[3];
    static int[] firstIndex;
    static int stepIndex = 0;

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    static void move(int n, int from, int to, int aux) {
        if (n == 0) return;
        move(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        int maxH = heights[0];
        if (heights[1] > maxH) maxH = heights[1];
        if (heights[2] > maxH) maxH = heights[2];

        int idx = stepIndex++;
        if (firstIndex[maxH] == -1) {
            firstIndex[maxH] = idx;
        } else {
            union(idx, firstIndex[maxH]);
        }

        move(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();
        if (n <= 0) return;

        int steps = (1 << n) - 1;
        parent = new int[steps];
        size = new int[steps];
        for (int i = 0; i < steps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        firstIndex = new int[n + 1];
        Arrays.fill(firstIndex, -1);

        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
        stepIndex = 0;

        move(n, 0, 1, 2);

        int[] clusterSizes = new int[n + 1];
        int countClusters = 0;

        for (int h = 1; h <= n; h++) {
            int idx = firstIndex[h];
            if (idx != -1) {
                int root = find(idx);
                int sz = size[root];
                clusterSizes[countClusters++] = sz;
            }
        }

        Arrays.sort(clusterSizes, 0, countClusters);

        for (int i = 0; i < countClusters; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
    }
}