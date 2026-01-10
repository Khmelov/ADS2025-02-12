package by.it.group451001.suprunovich.lesson14;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StatesHanoiTowerC {

    static class DSU {
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
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }
        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    static int N;
    static int moveIndex;
    static DSU dsu;
    static int[] firstIndexByMaxHeight;
    static int[][] pegs;
    static int[] pegTop;
    static int totalMoves;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null || s.trim().isEmpty()) return;
        N = Integer.parseInt(s.trim());

        if (N <= 0) {
            System.out.println();
            return;
        }

        totalMoves = (1 << N) - 1;
        dsu = new DSU(totalMoves);

        firstIndexByMaxHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) firstIndexByMaxHeight[i] = -1;


        pegs = new int[3][N];
        pegTop = new int[3];


        for (int i = 0; i < N; i++) {
            pegs[0][i] = N - i;
        }
        pegTop[0] = N;
        pegTop[1] = 0;
        pegTop[2] = 0;

        moveIndex = 0;


        moveRec(N, 0, 1, 2);


        int[] compCount = new int[totalMoves];
        for (int i = 0; i < totalMoves; i++) compCount[i] = 0;
        for (int i = 0; i < totalMoves; i++) {
            int r = dsu.find(i);
            compCount[r]++;
        }


        int[] groups = new int[N];
        int g = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (compCount[i] > 0) {
                groups[g++] = compCount[i];
            }
        }


        if (g > 0) {
            int[] out = new int[g];
            for (int i = 0; i < g; i++) out[i] = groups[i];
            Arrays.sort(out);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < g; i++) {
                if (i > 0) sb.append(' ');
                sb.append(out[i]);
            }
            System.out.println(sb.toString());
        } else {
            System.out.println();
        }
    }


    static void moveRec(int k, int src, int dst, int aux) {
        if (k == 0) return;

        moveRec(k - 1, src, aux, dst);


        int disk = pop(src);
        push(dst, disk);

        int h0 = pegTop[0];
        int h1 = pegTop[1];
        int h2 = pegTop[2];
        int maxH = h0;
        if (h1 > maxH) maxH = h1;
        if (h2 > maxH) maxH = h2;


        int curIndex = moveIndex;
        if (maxH >= 1 && maxH <= N) {
            if (firstIndexByMaxHeight[maxH] == -1) {
                firstIndexByMaxHeight[maxH] = curIndex;
            } else {
                dsu.union(curIndex, firstIndexByMaxHeight[maxH]);
            }
        }
        moveIndex++;

        moveRec(k - 1, aux, dst, src);
    }

    static int pop(int peg) {
        if (pegTop[peg] == 0) return -1;
        pegTop[peg]--;
        return pegs[peg][pegTop[peg]];
    }

    static void push(int peg, int disk) {
        pegs[peg][pegTop[peg]] = disk;
        pegTop[peg]++;
    }
}
