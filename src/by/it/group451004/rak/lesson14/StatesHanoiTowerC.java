package by.it.group451004.rak.lesson14;

import java.util.Scanner;

class DSU {
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
        if (parent[x] != x)
            parent[x] = find(parent[x]);
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

class HanoiTower {
    private int[] stack;
    private int[] height = new int[3];
    private int n;

    HanoiTower(int n) {
        this.n = n;
        stack = new int[3 * Math.max(1, n)];
        for (int d = n; d >= 1; d--)
            push(0, d);
    }

    private void push(int peg, int disk) {
        stack[peg * stack.length / 3 + height[peg]++] = disk;
    }

    private int pop(int peg) {
        return stack[peg * stack.length / 3 + (--height[peg])];
    }

    private int top(int peg) {
        if (height[peg] == 0) return Integer.MAX_VALUE;
        return stack[peg * stack.length / 3 + height[peg] - 1];
    }

    int[] simulate(DSU dsu) {
        int moves = (1 << n) - 1;
        int[] rep = new int[n + 1];
        for (int i = 0; i <= n; i++) rep[i] = -1;

        int smallPeg = 0;
        int dir = (n % 2 == 1) ? 1 : 2;

        for (int step = 1; step <= moves; step++) {
            if ((step & 1) == 1) { //верхни
                int nextPeg = (smallPeg + dir) % 3;
                pop(smallPeg);
                push(nextPeg, 1);
                smallPeg = nextPeg;
            } else { // другой диск
                int p1 = (smallPeg + 1) % 3;
                int p2 = (smallPeg + 2) % 3;
                int t1 = top(p1);
                int t2 = top(p2);
                if (t1 < t2) {
                    pop(p1);
                    push(p2, t1);
                } else {
                    pop(p2);
                    push(p1, t2);
                }
            }

            int maxH = Math.max(height[0], Math.max(height[1], height[2]));
            int idx = step - 1;
            if (rep[maxH] == -1)
                rep[maxH] = idx;
            else
                dsu.union(idx, rep[maxH]); //группировка
        }
        return null;
    }


}

public class StatesHanoiTowerC {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if (!in.hasNextInt()) return;
        int n = in.nextInt();
        int moves = (1 << n) - 1;
        DSU dsu = new DSU(Math.max(1, moves));

        HanoiTower hanoi = new HanoiTower(n);
        hanoi.simulate(dsu);

        int[] used = new int[moves];
        int groups = 0; //кол-во множеств
        for (int i = 0; i < moves; i++) {
            int r = dsu.find(i);
            if (used[r] == 0) {
                used[r] = 1;
                groups++;
            }
        }
        int[] sizes = new int[groups]; //размеры множеств
        int k = 0;
        for (int i = 0; i < moves; i++) {
            int r = dsu.find(i);
            if (used[r] == 1) {
                sizes[k++] = dsu.size[r];
                used[r] = 2;
            }
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
