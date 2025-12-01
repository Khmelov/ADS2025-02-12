package by.it.group410902.sulimov.lesson14;
import java.util.*;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void incrementSet(int element) {
            size[element] += 1;
        }
    }

    static DSU unionFind;
    static int diskCount;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        diskCount = n;
        unionFind = new DSU(n);

        solveHanoi(n, 'A', 'B', 'C', new int[]{n, 0, 0});

        int[] sizesByCluster = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int rootElement = unionFind.find(i);
            sizesByCluster[rootElement] = unionFind.size[rootElement];
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

    static void solveHanoi(int n, char from, char to, char auxiliary, int[] heights) {
        if (n == 0) return;

        solveHanoi(n - 1, from, auxiliary, to, heights);

        transferDisk(from, to, heights);

        solveHanoi(n - 1, auxiliary, to, from, heights);
    }

    static void transferDisk( char from, char to, int[] heights) {
        if (from == 'A') heights[0]--;
        if (from == 'B') heights[1]--;
        if (from == 'C') heights[2]--;

        if (to == 'A') heights[0]++;
        if (to == 'B') heights[1]++;
        if (to == 'C') heights[2]++;

        int maxTowerHeight = heights[0];
        if (heights[1] > maxTowerHeight) maxTowerHeight = heights[1];
        if (heights[2] > maxTowerHeight) maxTowerHeight = heights[2];

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