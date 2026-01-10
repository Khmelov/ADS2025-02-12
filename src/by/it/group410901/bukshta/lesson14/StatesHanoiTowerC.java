package by.it.group410901.bukshta.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    //объединяет шаги с одинаковыми характеристиками (группы)
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) { // инициализация множеств
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // поиск корня множества
        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        // объединение двух множеств
        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if (size[px] < size[py]) {
                parent[px] = py;
                size[py] += size[px];
            } else {
                parent[py] = px;
                size[px] += size[py];
            }
        }

        // возвращает размер множества
        int getSize(int x) {
            return size[find(x)];
        }
    }

    static int[] heights = new int[3]; // текущие высоты башен
    static int N;                      // диски
    static int totalMoves;             // хлды
    static int[] maxHeight;            // макс высота башен после i-го хода
    static int moveCount;              // счётчик ходов

    // рекурсивный алгоритм Ханойской башни
    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;

        // переносим n-1 дисков на вспомогательный стержень
        hanoi(n - 1, from, aux, to);

        // перемещаем диск n со стержня from на стержень to
        heights[from]--;
        heights[to]++;

        // фиксируем максимальную высоту башен после текущего хода
        if (moveCount < totalMoves) {
            maxHeight[moveCount] = Math.max(heights[0], Math.max(heights[1], heights[2]));
            moveCount++;
        }

        // переносим n-1 дисков со вспомогательного на конечный стержень
        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();

        totalMoves = (1 << N) - 1;
        maxHeight = new int[totalMoves];
        heights[0] = N; // все диски изначально на первом стержне (A)
        heights[1] = 0;
        heights[2] = 0;
        moveCount = 0;

        hanoi(N, 0, 1, 2);

        // создаём DSU для объединения шагов с одинаковыми maxHeight
        DSU dsu = new DSU(totalMoves);

        // разбиваем шаги по группам по значению maxHeight
        int[][] groups = new int[N + 1][totalMoves];
        int[] groupSize = new int[N + 1];
        for (int i = 0; i < totalMoves; i++) {
            int h = maxHeight[i];
            groups[h][groupSize[h]++] = i;
        }

        // объединяем шаги, у которых одинаковая максимальная высота
        for (int h = 1; h <= N; h++) {
            if (groupSize[h] > 1) {
                for (int j = 1; j < groupSize[h]; j++) {
                    dsu.union(groups[h][0], groups[h][j]);
                }
            }
        }

        // вычисляем размеры кластеров
        int[] clusterSizes = new int[totalMoves];
        int count = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (dsu.find(i) == i) { // если элемент — корень
                clusterSizes[count++] = dsu.getSize(i);
            }
        }

        // сортировка размеров по возрастанию
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (clusterSizes[i] > clusterSizes[j]) {
                    int t = clusterSizes[i];
                    clusterSizes[i] = clusterSizes[j];
                    clusterSizes[j] = t;
                }
            }
        }

        // вывод размеров кластеров через пробел
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();
    }
}
