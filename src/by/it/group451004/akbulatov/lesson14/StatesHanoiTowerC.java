package by.it.group451004.akbulatov.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class StatesHanoiTowerC
{
    static class DSU
    {
        int[] parent;
        int[] size;

        DSU(int n)
        {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++)
            {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x)
        {
            if (parent[x] != x)
                parent[x] = find(parent[x]);

            return parent[x];
        }

        void union(int x, int y)
        {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (size[rootX] < size[rootY])
            {
                int temp = rootX;
                rootX = rootY;
                rootY = temp;
            }

            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }

        int getSize(int x)
        {
            return size[find(x)];
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int totalSteps = (1 << N) - 1;

        int[] maxHeights = new int[totalSteps];

        int[] stepIndex = {0};
        int[] heights = {N, 0, 0};
        solveHanoi(N, 0, 1, 2, heights, maxHeights, stepIndex);

        DSU dsu = new DSU(totalSteps);

        int[] lastSeenIndices = new int[N + 1];
        Arrays.fill(lastSeenIndices, -1);

        for (int i = 0; i < totalSteps; i++) {
            int currentHeight = maxHeights[i];

            if (lastSeenIndices[currentHeight] != -1)
                dsu.union(i, lastSeenIndices[currentHeight]);
            else
                lastSeenIndices[currentHeight] = i;
        }

        int[] sizeCounts = new int[totalSteps + 1];

        for (int i = 0; i < totalSteps; i++)
        {
            if (dsu.parent[i] == i)
                sizeCounts[dsu.size[i]]++;
        }

        StringBuilder output = new StringBuilder();
        boolean first = true;

        for (int size = 1; size <= totalSteps; size++)
        {
            int count = sizeCounts[size];
            for (int k = 0; k < count; k++)
            {
                if (!first) output.append(" ");
                output.append(size);
                first = false;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int s = 1; s <= totalSteps; s++)
            for (int k = 0; k < sizeCounts[s]; k++)
                sb.append(s).append(" ");

        System.out.println(sb.toString().trim());
    }

    private static void solveHanoi(int n, int from, int to, int aux, int[] heights, int[] maxHeights, int[] stepIndex)
    {
        if (n == 0)
            return;

        solveHanoi(n - 1, from, aux, to, heights, maxHeights, stepIndex);

        heights[from]--;
        heights[to]++;

        int maxHeight = heights[0];
        if (heights[1] > maxHeight) maxHeight = heights[1];
        if (heights[2] > maxHeight) maxHeight = heights[2];

        maxHeights[stepIndex[0]] = maxHeight;
        stepIndex[0]++;

        solveHanoi(n - 1, aux, to, from, heights, maxHeights, stepIndex);
    }
}