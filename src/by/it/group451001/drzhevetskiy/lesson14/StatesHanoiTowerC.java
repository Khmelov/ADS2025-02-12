package by.it.group451001.drzhevetskiy.lesson14;

import java.io.*;
import java.util.*;

public class StatesHanoiTowerC {

    private static int totalDisks;

    private static int step;
    private static int[] level;
    private static int[] usedHeight;

    private static int[] ufParent;
    private static int[] ufSize;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        totalDisks = Integer.parseInt(line.trim());
        if (totalDisks <= 0) return;

        int totalStates = (1 << totalDisks) - 1;
        if (totalStates < 0) throw new IllegalArgumentException();

        initDSU(totalStates);

        level = new int[]{totalDisks, 0, 0};
        usedHeight = new int[totalDisks + 1];
        Arrays.fill(usedHeight, -1);

        step = 0;

        simulate(totalDisks, 0, 1, 2);

        int[] groupCount = new int[totalStates];
        for (int i = 0; i < totalStates; i++) {
            groupCount[findRoot(i)]++;
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int c : groupCount) if (c > 0) result.add(c);

        Collections.sort(result);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(result.get(i));
        }
        System.out.println(sb);
    }

    // --- Simulation of all moves ---
    private static void simulate(int n, int a, int b, int c) {
        if (n == 0) return;

        simulate(n - 1, a, c, b);

        performMove(a, b);

        simulate(n - 1, c, b, a);
    }

    private static void performMove(int from, int to) {
        level[from]--;
        level[to]++;

        int highest = Math.max(level[0], Math.max(level[1], level[2]));

        if (usedHeight[highest] == -1) {
            usedHeight[highest] = step;
        } else {
            merge(step, usedHeight[highest]);
        }

        step++;
    }

    // --- DSU implementation ---
    private static void initDSU(int n) {
        ufParent = new int[n];
        ufSize = new int[n];
        for (int i = 0; i < n; i++) {
            ufParent[i] = i;
            ufSize[i] = 1;
        }
    }

    private static int findRoot(int v) {
        if (ufParent[v] == v) return v;
        ufParent[v] = findRoot(ufParent[v]);
        return ufParent[v];
    }

    private static void merge(int x, int y) {
        int rx = findRoot(x);
        int ry = findRoot(y);
        if (rx == ry) return;

        if (ufSize[rx] < ufSize[ry]) {
            ufParent[rx] = ry;
            ufSize[ry] += ufSize[rx];
        } else {
            ufParent[ry] = rx;
            ufSize[rx] += ufSize[ry];
        }
    }
}
