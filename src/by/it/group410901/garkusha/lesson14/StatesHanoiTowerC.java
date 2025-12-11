package by.it.group410901.garkusha.lesson14;

import java.util.*;

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
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            x = find(x);
            y = find(y);
            if (x == y) return;
            if (size[x] < size[y]) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            parent[y] = x;
            size[x] += size[y];
        }

        int componentSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int totalMoves = (1 << N) - 1; // 2^N - 1 общее колчество шагов
        if (totalMoves == 0) {
            return;
        }

        DSU dsu = new DSU(totalMoves);
        int[] maxHeight = new int[totalMoves]; // Максимальная высота башни после move-го хода
        int[] firstIndex = new int[N + 1]; // Индекс первого хода, где достигнута высота h
        Arrays.fill(firstIndex, -1);

        int[] pegs = {N, 0, 0}; // Текущее количество дисков на каждом стержне
        int[] moveCounter = {0};

        // Запуск алгоритма Ханойских башен
        hanoi(N, 0, 1, 2, pegs, maxHeight, firstIndex, dsu, moveCounter);

        // Сбор и сортировка размеров компонент
        int[] sizes = new int[N + 1];
        int count = 0;
        for (int h = 1; h <= N; h++) {
            if (firstIndex[h] != -1) {
                sizes[count++] = dsu.componentSize(firstIndex[h]);
            }
        }

        Arrays.sort(sizes, 0, count);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString());
    }

    private static void hanoi(int n, int src, int dst, int aux,
                              int[] pegs,
                              int[] maxHeight,
                              int[] firstIndex,
                              DSU dsu,
                              int[] moveCounter) {
        if (n == 0) return;

        // Рекурсивно перемещаем n-1 диск с исходного на вспомогательный стержень
        hanoi(n - 1, src, aux, dst, pegs, maxHeight, firstIndex, dsu, moveCounter);

        // Перемещаем самый большой диск с исходного на целевой стержень
        pegs[src]--;
        pegs[dst]++;

        // Обработка текущего состояния
        int idx = moveCounter[0];
        int h = Math.max(pegs[0], Math.max(pegs[1], pegs[2])); // Максимальная высота башни
        maxHeight[idx] = h;

        // Объединяем в компоненты ходы с одинаковой максимальной высотой
        if (firstIndex[h] == -1) {
            firstIndex[h] = idx;
        } else {
            dsu.union(idx, firstIndex[h]);
        }

        moveCounter[0]++;

        // Рекурсивно перемещаем n-1 диск с вспомогательного на целевой стержень
        hanoi(n - 1, aux, dst, src, pegs, maxHeight, firstIndex, dsu, moveCounter);
    }
}