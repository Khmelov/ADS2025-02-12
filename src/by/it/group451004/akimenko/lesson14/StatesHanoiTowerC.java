package lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;
    private static int maxStates;
    private static int[] maxHeights;
    private static int stateIndex;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        maxStates = (1 << n) - 1;
        maxHeights = new int[maxStates];
        stateIndex = 0;

        int[] heights = {n, 0, 0};
        solveHanoi(n, heights, 0, 1, 2);

        initializeDSU(maxStates);
        int[] firstOccurrence = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstOccurrence[i] = -1;
        }

        for (int i = 0; i < maxStates; i++) {
            int h = maxHeights[i];
            if (firstOccurrence[h] == -1) {
                firstOccurrence[h] = i;
            } else {
                union(i, firstOccurrence[h]);
            }
        }

        int[] resultSizes = new int[n + 1];
        int count = 0;
        for (int i = 0; i <= n; i++) {
            if (firstOccurrence[i] != -1) {
                int root = find(firstOccurrence[i]);
                resultSizes[count++] = size[root];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int temp = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.print(resultSizes[i]);
            if (i < count - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private static void solveHanoi(int n, int[] heights, int from, int to, int aux) {
        if (n == 0) return;
        solveHanoi(n - 1, heights, from, aux, to);
        heights[from]--;
        heights[to]++;
        maxHeights[stateIndex++] = Math.max(heights[0], Math.max(heights[1], heights[2]));
        solveHanoi(n - 1, heights, aux, to, from);
    }

    private static void initializeDSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
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
