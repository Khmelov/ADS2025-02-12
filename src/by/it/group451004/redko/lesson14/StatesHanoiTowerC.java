package by.it.group451004.redko.lesson14;

import java.util.Scanner;

public class
StatesHanoiTowerC {

    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    private static void push(int[] stack, int[] h, int peg, int disk) {
        stack[peg * stack.length / 3 + h[peg]++] = disk;
    }

    private static int pop(int[] stack, int[] h, int peg) {
        return stack[peg * stack.length / 3 + (--h[peg])];
    }

    private static int top(int[] stack, int[] h, int peg) {
        if (h[peg] == 0) return Integer.MAX_VALUE;
        return stack[peg * stack.length / 3 + h[peg] - 1];
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) return;
        int n = in.nextInt();
        int moves = (1 << n) - 1;

        int[] stack = new int[3 * Math.max(1, n)];
        int[] h = new int[3];
        for (int d = n; d >= 1; d--) push(stack, h, 0, d);

        DSU dsu = new DSU(Math.max(1, moves));
        int[] rep = new int[n + 1];
        for (int i = 0; i <= n; i++) rep[i] = -1;

        int smallPeg = 0;
        int dir = (n % 2 == 1) ? 1 : 2;

        for (int step = 1; step <= moves; step++) {
            if ((step & 1) == 1) {
                int nextPeg = (smallPeg + dir) % 3;
                pop(stack, h, smallPeg);
                push(stack, h, nextPeg, 1);
                smallPeg = nextPeg;
            } else {
                int p1 = (smallPeg + 1) % 3;
                int p2 = (smallPeg + 2) % 3;
                int t1 = top(stack, h, p1);
                int t2 = top(stack, h, p2);
                if (t1 < t2) {
                    pop(stack, h, p1);
                    push(stack, h, p2, t1);
                } else {
                    pop(stack, h, p2);
                    push(stack, h, p1, t2);
                }
            }

            int maxH = h[0];
            if (h[1] > maxH) maxH = h[1];
            if (h[2] > maxH) maxH = h[2];

            int idx = step - 1;
            if (rep[maxH] == -1) rep[maxH] = idx;
            else dsu.union(idx, rep[maxH]);
        }

        int[] used = new int[moves];
        int groups = 0;
        for (int i = 0; i < moves; i++) {
            int r = dsu.find(i);
            if (used[r] == 0) { used[r] = 1; groups++; }
        }
        int[] sizes = new int[groups];
        int k = 0;
        for (int i = 0; i < moves; i++) {
            int r = dsu.find(i);
            if (used[r] == 1) { sizes[k++] = dsu.size[r]; used[r] = 2; }
        }

        for (int i = 1; i < sizes.length; i++) {
            int v = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > v) { sizes[j + 1] = sizes[j]; j--; }
            sizes[j + 1] = v;
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) out.append(' ');
            out.append(sizes[i]);
        }
        System.out.println(out);
    }
}






