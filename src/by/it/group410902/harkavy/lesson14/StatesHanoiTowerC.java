package by.it.group410902.harkavy.lesson14;

import java.util.Scanner;

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
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // сжатие пути
            }
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            // эвристика по размеру поддерева
            if (size[a] < size[b]) {
                int t = a; a = b; b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    int[][] pegs;     // [стержень][позиция]
    int[] sizes;      // текущая высота каждой пирамиды A,B,C
    int idx;          // номер шага (0..total-1)
    DSU dsu;
    int[] repForHeight; // representative step index for each max height
    int total;
    int N;

    void move(int n, int from, int to, int aux) {
        if (n == 0) return;

        move(n - 1, from, aux, to);

        // перенос диска
        int disk = pegs[from][--sizes[from]];
        pegs[to][sizes[to]++] = disk;

        // считаем наибольшую высоту H среди A,B,C
        int h0 = sizes[0];
        int h1 = sizes[1];
        int h2 = sizes[2];
        int H = h0;
        if (h1 > H) H = h1;
        if (h2 > H) H = h2;

        int stepIndex = idx++;

        // объединяем все шаги с одинаковым H через DSU
        if (repForHeight[H] == -1) {
            repForHeight[H] = stepIndex;
        } else {
            dsu.union(stepIndex, repForHeight[H]);
        }

        move(n - 1, aux, to, from);
    }

    void solve(int n) {
        this.N = n;
        total = (1 << N) - 1; // количество шагов

        pegs = new int[3][N];
        sizes = new int[3];
        idx = 0;

        // заполняем стержень A (0) дисками N..1
        for (int i = 0; i < N; i++) {
            pegs[0][i] = N - i;
        }
        sizes[0] = N;

        dsu = new DSU(total);

        // высоты H от 1 до N (0 не будет использоваться)
        repForHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            repForHeight[i] = -1;
        }

        // стандартное рекурсивное решение Ханойских башен
        // переносим с A (0) на B (1), используя C (2)
        move(N, 0, 1, 2);

        // собираем размеры компонент
        boolean[] usedRoot = new boolean[total];
        int[] out = new int[total];
        int k = 0;

        for (int i = 0; i < total; i++) {
            int r = dsu.find(i);
            if (!usedRoot[r]) {
                usedRoot[r] = true;
                out[k++] = dsu.size[r];
            }
        }

        // сортировка размеров по возрастанию (простая, без коллекций)
        for (int i = 0; i < k; i++) {
            for (int j = i + 1; j < k; j++) {
                if (out[i] > out[j]) {
                    int t = out[i];
                    out[i] = out[j];
                    out[j] = t;
                }
            }
        }

        // вывод
        for (int i = 0; i < k; i++) {
            System.out.print(out[i]);
            if (i + 1 < k) System.out.print(" ");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        new StatesHanoiTowerC().solve(n);
    }
}
