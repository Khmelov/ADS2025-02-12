package by.it.group410902.shahov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 0;
            }
        }

        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        void addToSet(int x) {
            size[x] += 1;
        }
    }

    static DSU dsu;
    static int maxN;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        maxN = N;
        dsu = new DSU(N);

        // Запускаем алгоритм Ханойской башни
        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // Собираем информацию о размерах кластеров
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            clusterSize[root] = dsu.size[root];
        }

        // Формируем результат - ненулевые размеры кластеров
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортируем результат по возрастанию (пузырьковая сортировка)
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int t = result[i];
                    result[i] = result[j];
                    result[j] = t;
                }
            }
        }

        // Вывод результата
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    /**
     * Рекурсивный алгоритм Ханойской башни
     * @param n количество дисков для перемещения
     * @param from начальный стержень
     * @param to целевой стержень
     * @param aux вспомогательный стержень
     * @param heights массив высот на каждом стержне [A, B, C]
     */
    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return;

        // Рекурсивно перемещаем n-1 дисков на вспомогательный стержень
        hanoi(n - 1, from, aux, to, heights);

        // Перемещаем самый большой диск на целевой стержень
        moveDisk(n, from, to, heights);

        // Рекурсивно перемещаем n-1 дисков с вспомогательного на целевой стержень
        hanoi(n - 1, aux, to, from, heights);
    }

    /**
     * Обрабатывает перемещение одного диска и обновляет состояния
     * @param disk номер диска (размер)
     * @param from откуда перемещаем
     * @param to куда перемещаем
     * @param heights текущие высоты стержней
     */
    static void moveDisk(int disk, char from, char to, int[] heights) {
        // Обновляем высоты стержней после перемещения
        if (from == 'A') heights[0]--;
        if (from == 'B') heights[1]--;
        if (from == 'C') heights[2]--;

        if (to == 'A') heights[0]++;
        if (to == 'B') heights[1]++;
        if (to == 'C') heights[2]++;

        // Находим максимальную высоту среди всех стержней
        int maxHeight = heights[0];
        if (heights[1] > maxHeight) maxHeight = heights[1];
        if (heights[2] > maxHeight) maxHeight = heights[2];

        // Добавляем текущее состояние в соответствующую группу
        dsu.addToSet(maxHeight);
    }
}
