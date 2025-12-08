package by.it.group410901.borisdubinin.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int[] rootForHeight; // корень множества для каждой высоты

    static int hA, hB, hC;
    static int idx = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int total = (1 << N) - 1;

        parent = new int[total];
        size = new int[total];
        rootForHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) rootForHeight[i] = -1;

        hA = N;
        hB = 0;
        hC = 0;

        idx = 0;
        hanoi(N, 'A', 'B', 'C');

        // собираем размеры в отсортированном порядке
        int[] answer = new int[N + 1];
        int cnt = 0;

        for (int h = 1; h <= N; h++) {
            if (rootForHeight[h] != -1) {
                int root = leader(rootForHeight[h]);
                int sz = size[root];
                answer[cnt++] = sz;
            }
        }

        // пузырёк
        for (int i = 0; i < cnt; i++) {
            for (int j = i + 1; j < cnt; j++) {
                if (answer[i] > answer[j]) {
                    int t = answer[i];
                    answer[i] = answer[j];
                    answer[j] = t;
                }
            }
        }

        for (int i = 0; i < cnt; i++) {
            System.out.print(answer[i]);
            if (i + 1 < cnt) System.out.print(" ");
        }
    }

    static int leader(int x) {
        if (parent[x] != x) parent[x] = leader(parent[x]);
        return parent[x];
    }

    static void union(int a, int b) {
        a = leader(a);
        b = leader(b);
        if (a == b) return;
        if (size[a] < size[b]) {
            int t = a; a = b; b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    static void addState() {
        int maxH = hA;
        if (hB > maxH) maxH = hB;
        if (hC > maxH) maxH = hC;

        parent[idx] = idx;
        size[idx] = 1;

        if (rootForHeight[maxH] == -1) {
            rootForHeight[maxH] = idx;
        } else {
            union(rootForHeight[maxH], idx);
        }

        idx++;
    }

    static void moveDisk(char from, char to) {
        if (from == 'A') hA--;
        if (from == 'B') hB--;
        if (from == 'C') hC--;

        if (to == 'A') hA++;
        if (to == 'B') hB++;
        if (to == 'C') hC++;

        addState();
    }

    static void hanoi(int n, char from, char to, char auxiliary) {
        if (n == 0) return;
        hanoi(n - 1, from, auxiliary, to);
        moveDisk(from, to);
        hanoi(n - 1, auxiliary, to, from);
    }
}

