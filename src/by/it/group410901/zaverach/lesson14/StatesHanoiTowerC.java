package by.it.group410901.zaverach.lesson14;


import java.util.Scanner;
import java.util.Arrays;

public class StatesHanoiTowerC {
    private static Tower towerA, towerB, towerC;
    private static int[] maxHeights;
    private static int stepCounter;

    static class Tower {
        private int[] disks;
        private int top;

        public Tower(int capacity) {
            disks = new int[capacity];
            top = -1;
        }

        public void push(int disk) {
            disks[++top] = disk;
        }

        public int pop() {
            return disks[top--];
        }

        public int size() {
            return top + 1;
        }
    }

    static class DSU {
        private int[] parent;
        private int[] sz;

        public DSU(int n) {
            parent = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                sz[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }
        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (sz[rootI] < sz[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                sz[rootI] += sz[rootJ];
            }
        }
    }

    public static void solveHanoi(int n, Tower source, Tower destination, Tower auxiliary) {

        if (n == 0) {
            return;
        }
        solveHanoi(n - 1, source, auxiliary, destination);

        destination.push(source.pop());

        int hA = towerA.size();
        int hB = towerB.size();
        int hC = towerC.size();
        maxHeights[stepCounter] = Math.max(hA, Math.max(hB, hC));
        stepCounter++;

        solveHanoi(n - 1, auxiliary, destination, source);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        int totalMoves = (1 << n) - 1;

        towerA = new Tower(n);
        towerB = new Tower(n);
        towerC = new Tower(n);

        for (int i = n; i > 0; i--) {
            towerA.push(i);
        }

        maxHeights = new int[totalMoves];
        stepCounter = 0;

        solveHanoi(n, towerA, towerB, towerC);

        DSU dsu = new DSU(totalMoves);

        int[] representatives = new int[n + 1];
        Arrays.fill(representatives, -1);

        for (int i = 0; i < totalMoves; i++) {
            int height = maxHeights[i];
            if (representatives[height] == -1) {
                representatives[height] = i;
            } else {
                dsu.union(representatives[height], i);
            }
        }

        int[] tempSizes = new int[totalMoves];
        int uniqueSetsCount = 0;
        boolean[] visitedRoots = new boolean[totalMoves];

        for (int i = 0; i < totalMoves; i++) {
            int root = dsu.find(i);
            if (!visitedRoots[root]) {
                tempSizes[uniqueSetsCount++] = dsu.sz[root];
                visitedRoots[root] = true;
            }
        }

        int[] finalSizes = new int[uniqueSetsCount];
        for (int i = 0; i < uniqueSetsCount; i++) {
            finalSizes[i] = tempSizes[i];
        }

        Arrays.sort(finalSizes);
        for (int i = 0; i < finalSizes.length; i++) {
            System.out.print(finalSizes[i] + (i == finalSizes.length - 1 ? "" : " "));
        }
        System.out.println();
    }
}