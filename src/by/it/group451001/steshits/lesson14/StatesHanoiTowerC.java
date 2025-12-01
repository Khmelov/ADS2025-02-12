package by.it.group451001.steshits.lesson14;

import java.io.*;
import java.util.*;

public class StatesHanoiTowerC {
    static int N;
    static int moveIndex;
    static int[] heights;
    static int[] lastForHeight;
    static int[] parent;
    static int[] sz;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null || s.trim().isEmpty()) return;
        N = Integer.parseInt(s.trim());
        if (N <= 0) return;

        long mm = 1L;
        for (int i = 0; i < N; i++) mm <<= 1;
        mm -= 1;
        if (mm > Integer.MAX_VALUE) throw new IllegalArgumentException("N too large");
        int m = (int) mm;

        parent = new int[m];
        sz = new int[m];
        for (int i = 0; i < m; i++) {
            parent[i] = i;
            sz[i] = 1;
        }

        heights = new int[3];
        heights[0] = N;
        heights[1] = 0;
        heights[2] = 0;

        lastForHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) lastForHeight[i] = -1;

        moveIndex = 0;
        generate(N, 0, 1, 2);

        int[] countByRoot = new int[m];
        for (int i = 0; i < m; i++) {
            int r = find(i);
            countByRoot[r]++;
        }

        int nonZero = 0;
        for (int i = 0; i < m; i++) if (countByRoot[i] > 0) nonZero++;

        int[] res = new int[nonZero];
        int p = 0;
        for (int i = 0; i < m; i++) if (countByRoot[i] > 0) res[p++] = countByRoot[i];

        Arrays.sort(res);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i > 0) out.append(' ');
            out.append(res[i]);
        }
        System.out.println(out.toString());
    }

    static void generate(int n, int from, int to, int aux) {
        if (n == 0) return;
        generate(n - 1, from, aux, to);
        heights[from]--;
        heights[to]++;
        int maxh = heights[0];
        if (heights[1] > maxh) maxh = heights[1];
        if (heights[2] > maxh) maxh = heights[2];
        int last = lastForHeight[maxh];
        if (last == -1) lastForHeight[maxh] = moveIndex;
        else union(moveIndex, last);
        moveIndex++;
        generate(n - 1, aux, to, from);
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (sz[ra] < sz[rb]) {
            parent[ra] = rb;
            sz[rb] += sz[ra];
        } else {
            parent[rb] = ra;
            sz[ra] += sz[rb];
        }
    }
}