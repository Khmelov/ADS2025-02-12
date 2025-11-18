package by.it.group451004.romanovskaya.lesson14;

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
            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    int temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    static void hanoi(int n, char from, char to, char aux, DSU dsu, int[] heights, int step, List<Integer> maxHeights) {
        if (n == 1) {
            heights[from - 'A']--;
            heights[to - 'A']++;
            int maxHeight = Math.max(Math.max(heights[0], heights[1]), heights[2]);
            maxHeights.add(maxHeight);
            return;
        }
        hanoi(n - 1, from, aux, to, dsu, heights, step, maxHeights);
        heights[from - 'A']--;
        heights[to - 'A']++;
        int maxHeight = Math.max(Math.max(heights[0], heights[1]), heights[2]);
        maxHeights.add(maxHeight);
        hanoi(n - 1, aux, to, from, dsu, heights, step + 1, maxHeights);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int totalSteps = (int) Math.pow(2, N) - 1;
        DSU dsu = new DSU(totalSteps + 1);
        int[] heights = new int[3];
        heights[0] = N;
        List<Integer> maxHeights = new ArrayList<>();
        hanoi(N, 'A', 'B', 'C', dsu, heights, 1, maxHeights);

        Map<Integer, Integer> groupSizes = new HashMap<>();
        for (int i = 0; i < maxHeights.size(); i++) {
            int maxHeight = maxHeights.get(i);
            groupSizes.put(maxHeight, groupSizes.getOrDefault(maxHeight, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(groupSizes.values());
        Collections.sort(result);
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
