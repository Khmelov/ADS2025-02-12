package by.it.group451003.alekseyuk.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StatesHanoiTowerC {
    static class DSU {
        int[] parent, size;

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
            int rx = find(x), ry = find(y);
            if (rx != ry) {
                if (size[rx] < size[ry]) {
                    int temp = rx;
                    rx = ry;
                    ry = temp;
                }
                parent[ry] = rx;
                size[rx] += size[ry];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = (1 << N) - 1;
        int base = M;
        DSU dsu = new DSU(base + N + 1);
        int[][] towers = new int[3][N];
        int[] heights = new int[3];
        heights[0] = N;
        for (int i = 0; i < N; i++) {
            towers[0][i] = N - i;
        }
        for (int m = 1; m <= M; m++) {
            int disk = Integer.numberOfTrailingZeros(m) + 1;
            int from = (m & (m - 1)) % 3;
            int to = ((m | (m - 1)) + 1) % 3;
            if ((N % 2) == 0) {
                from = from == 1 ? 2 : from == 2 ? 1 : from;
                to = to == 1 ? 2 : to == 2 ? 1 : to;
            }
            towers[to][heights[to]++] = towers[from][--heights[from]];
            int max_h = 0;
            for (int h : heights) {
                max_h = Math.max(max_h, h);
            }
            int step_id = m - 1;
            int group_id = base + max_h;
            dsu.union(step_id, group_id);
        }
        int[] gs = new int[N + 1];
        for (int h = 1; h <= N; h++) {
            int gid = base + h;
            int r = dsu.find(gid);
            gs[h] = dsu.size[r] - 1;
        }
        List<Integer> ress = new ArrayList<>();
        for (int s : gs) {
            if (s > 0) ress.add(s);
        }
        Collections.sort(ress);
        StringBuilder sb = new StringBuilder();
        for (int s : ress) {
            sb.append(s).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}