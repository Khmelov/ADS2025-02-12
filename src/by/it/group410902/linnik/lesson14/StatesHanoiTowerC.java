package by.it.group410902.linnik.lesson14;

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
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    static int[] heightCount;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        DSU dsu = new DSU(N + 1);
        heightCount = new int[N + 1];

        hanoi(N, 0, 1, 2, new int[]{N, 0, 0}, dsu);

        int[] result = new int[N + 1];
        int count = 0;

        for (int i = 1; i <= N; i++) {
            if (heightCount[i] > 0) {
                result[count++] = heightCount[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    static void hanoi(int n, int from, int to, int aux, int[] heights, DSU dsu) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to, heights, dsu);

        heights[from]--;
        heights[to]++;
        int maxHeight = Math.max(Math.max(heights[0], heights[1]), heights[2]);

        heightCount[maxHeight]++;
        hanoi(n - 1, aux, to, from, heights, dsu);
    }
}