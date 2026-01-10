package by.it.group451001.russu.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int totalSteps;
    private static int[] parent;
    private static int[] size;
    private static int[][] states;
    private static int currentStep = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        totalSteps = (1 << N) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        states = new int[totalSteps][3];

        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        simulateAllStates(N);
        groupByMaxHeightOptimized();
        printGroupSizes();
    }

    private static void simulateAllStates(int N) {
        int[][][] allStates = new int[totalSteps + 1][3][N];


        for (int i = 0; i < N; i++) {
            allStates[0][0][i] = N - i;
        }

        currentStep = 0;
        generateStates(N, 0, 1, 2, allStates, N);
    }

    private static void generateStates(int n, int from, int to, int aux, int[][][] allStates, int N) {
        if (n == 1) {
            moveDiskInStates(from, to, allStates, N);
            return;
        }

        generateStates(n - 1, from, aux, to, allStates, N);
        moveDiskInStates(from, to, allStates, N);
        generateStates(n - 1, aux, to, from, allStates, N);
    }

    private static void moveDiskInStates(int from, int to, int[][][] allStates, int N) {
        currentStep++;


        for (int i = 0; i < 3; i++) {
            System.arraycopy(allStates[currentStep - 1][i], 0, allStates[currentStep][i], 0, N);
        }


        int disk = 0;
        for (int i = N - 1; i >= 0; i--) {
            if (allStates[currentStep][from][i] != 0) {
                disk = allStates[currentStep][from][i];
                allStates[currentStep][from][i] = 0;
                break;
            }
        }

        for (int i = 0; i < N; i++) {
            if (allStates[currentStep][to][i] == 0) {
                allStates[currentStep][to][i] = disk;
                break;
            }
        }


        states[currentStep - 1][0] = getTowerHeight(allStates[currentStep][0]);
        states[currentStep - 1][1] = getTowerHeight(allStates[currentStep][1]);
        states[currentStep - 1][2] = getTowerHeight(allStates[currentStep][2]);
    }

    private static int getTowerHeight(int[] tower) {
        int height = 0;
        for (int value : tower) {
            if (value != 0) height++;
        }
        return height;
    }

    private static void groupByMaxHeightOptimized() {
        int maxPossibleHeight = 21;
        int[] firstOccurrence = new int[maxPossibleHeight + 2]; // +2 для запаса
        for (int i = 0; i < firstOccurrence.length; i++) {
            firstOccurrence[i] = -1;
        }


        for (int i = 0; i < totalSteps; i++) {
            int maxHeight = Math.max(states[i][0], Math.max(states[i][1], states[i][2]));

            if (firstOccurrence[maxHeight] == -1) {
                firstOccurrence[maxHeight] = i;
            } else {
                union(firstOccurrence[maxHeight], i);
            }
        }
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private static void union(int x, int y) {
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

    private static void printGroupSizes() {
        int[] tempSizes = new int[totalSteps];
        int count = 0;

        for (int i = 0; i < totalSteps; i++) {
            if (parent[i] == i) {
                tempSizes[count++] = size[i];
            }
        }


        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (tempSizes[j] > tempSizes[j + 1]) {
                    int temp = tempSizes[j];
                    tempSizes[j] = tempSizes[j + 1];
                    tempSizes[j + 1] = temp;
                }
            }
        }


        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(tempSizes[i]);
        }
        System.out.println();
    }
}