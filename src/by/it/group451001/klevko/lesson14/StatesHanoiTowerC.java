package by.it.group451001.klevko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DisjointSetUnion {
        int[] parentNode;
        int[] componentSize;

        DisjointSetUnion(int capacity) {
            parentNode = new int[capacity + 1];
            componentSize = new int[capacity + 1];
            for (int i = 0; i <= capacity; i++) {
                parentNode[i] = i;
                componentSize[i] = 0;
            }
        }

        int getRoot(int element) {
            if (parentNode[element] != element)
                parentNode[element] = getRoot(parentNode[element]);
            return parentNode[element];
        }

        void incrementSet(int element) {
            componentSize[element] += 1;
        }
    }

    static DisjointSetUnion unionFind;
    static int diskCount;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        diskCount = n;
        unionFind = new DisjointSetUnion(n);

        solveHanoi(n, 'A', 'B', 'C', new int[]{n, 0, 0});

        int[] sizesByCluster = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int rootElement = unionFind.getRoot(i);
            sizesByCluster[rootElement] = unionFind.componentSize[rootElement];
        }

        int[] resultSizes = new int[n + 1];
        int resultCount = 0;
        for (int i = 1; i <= n; i++) {
            if (sizesByCluster[i] > 0) {
                resultSizes[resultCount++] = sizesByCluster[i];
            }
        }

        sortAscending(resultSizes, resultCount);

        for (int i = 0; i < resultCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(resultSizes[i]);
        }
        System.out.println();
    }

    static void solveHanoi(int count, char source, char target, char auxiliary, int[] towerHeights) {
        if (count == 0) return;

        solveHanoi(count - 1, source, auxiliary, target, towerHeights);

        transferDisk(count, source, target, towerHeights);

        solveHanoi(count - 1, auxiliary, target, source, towerHeights);
    }

    static void transferDisk(int diskNumber, char sourceRod, char targetRod, int[] towerHeights) {
        if (sourceRod == 'A') towerHeights[0]--;
        if (sourceRod == 'B') towerHeights[1]--;
        if (sourceRod == 'C') towerHeights[2]--;

        if (targetRod == 'A') towerHeights[0]++;
        if (targetRod == 'B') towerHeights[1]++;
        if (targetRod == 'C') towerHeights[2]++;

        int maxTowerHeight = towerHeights[0];
        if (towerHeights[1] > maxTowerHeight) maxTowerHeight = towerHeights[1];
        if (towerHeights[2] > maxTowerHeight) maxTowerHeight = towerHeights[2];

        unionFind.incrementSet(maxTowerHeight);
    }

    static void sortAscending(int[] array, int length) {
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                if (array[i] > array[j]) {
                    int temporary = array[i];
                    array[i] = array[j];
                    array[j] = temporary;
                }
            }
        }
    }
}
