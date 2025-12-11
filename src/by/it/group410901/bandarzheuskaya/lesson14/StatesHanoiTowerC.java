package by.it.group410901.bandarzheuskaya.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;   //корень группы
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
            while (parent[x] != x) {    //пока не корень
                parent[x] = parent[parent[x]];  // path halving
                x = parent[x];
            }
            return x;
        }

        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if (size[px] < size[py]) { //Меньшее дерево приклеиваем к большему
                parent[px] = py;
                size[py] += size[px];
            } else {
                parent[py] = px;
                size[px] += size[py];
            }
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    static int[] heights = new int[3];  // текущие высоты A, B, C
    static int N;
    static int totalMoves;
    static int[] maxHeight;  // maxHeight[i] = max высота после i-го хода

    static void hanoi(int n, int from, int to, int aux) {   //сколько дисков переместилось, откуда, куда и вспомогательный
        if (n == 0) return;

        hanoi(n - 1, from, aux, to);

        // перемещаем диск n с from на to
        heights[from]--;
        heights[to]++;

        // сохраняем max высоту после хода
        // используем глобальный счётчик
        if (moveCount < totalMoves) {
            maxHeight[moveCount] = Math.max(heights[0], Math.max(heights[1], heights[2]));
            moveCount++;
        }

        hanoi(n - 1, aux, to, from);
    }

    static int moveCount;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();

        totalMoves = (1 << N) - 1; //2^N - 1
        maxHeight = new int[totalMoves];    //хранит максимальную высоту башни после каждого хода
        heights[0] = N;
        heights[1] = 0;
        heights[2] = 0;
        moveCount = 0;

        hanoi(N, 0, 1, 2);

        // группируем шаги с одинаковым maxHeight
        DSU dsu = new DSU(totalMoves);

        // Создаём карту: maxH -> список индексов ходов
        int[][] groups = new int[N + 1][totalMoves];
        int[] groupSize = new int[N + 1];
        for (int i = 0; i < totalMoves; i++) {
            int h = maxHeight[i];
            groups[h][groupSize[h]++] = i;
        }

        // Объединяем в DSU все шаги с одинаковым maxH
        for (int h = 1; h <= N; h++) {
            if (groupSize[h] > 1) {
                for (int j = 1; j < groupSize[h]; j++) {
                    dsu.union(groups[h][0], groups[h][j]);
                }
            }
        }

        // Собираем размеры кластеров
        int[] clusterSizes = new int[totalMoves];
        int count = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (dsu.find(i) == i) {  // корень
                clusterSizes[count++] = dsu.getSize(i);
            }
        }

        // Сортировка по возрастанию
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (clusterSizes[i] > clusterSizes[j]) {
                    int t = clusterSizes[i];
                    clusterSizes[i] = clusterSizes[j];
                    clusterSizes[j] = t;
                }
            }
        }

        // Вывод
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();
    }
}