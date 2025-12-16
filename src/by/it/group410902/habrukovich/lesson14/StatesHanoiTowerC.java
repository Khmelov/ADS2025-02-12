package by.it.group410902.habrukovich.lesson14;

import java.util.Scanner;

import java.util.Arrays;

public class StatesHanoiTowerC {

    // DSU с union-by-size
    static class DSU {
        int[] parent;
        int[] size;
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int a, int b) {
            a = find(a); b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) { parent[a] = b; size[b] += size[a]; }
            else { parent[b] = a; size[a] += size[b]; }
        }
    }


    static int N;
    static int totalSteps;
    static int[] A, B, C;      // стержни как стеки
    static int topA, topB, topC;
    static int stepIndex;      // текущий индекс шага (0..totalSteps-1)

    // Массив максимальной высоты на каждом шаге (заполняется по ходу)
    static int[] maxAtStep;

    // Рекурсивный генератор ходов
    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to); //Перенести n-1 дисков на вспомогательный стержень.
        moveOne(from, to); // Перенести один диск на целевой стержень
        hanoi(n - 1, aux, to, from); //Перенести n-1 дисков на целевой стержень.
    }

    static void moveOne(int from, int to) {
        int disk;
        if (from == 0) disk = A[--topA];
        else if (from == 1) disk = B[--topB]; // Извлекаем диск со стержня from и помещаем на стержень to.
        else disk = C[--topC];

        if (to == 0) A[topA++] = disk;
        else if (to == 1) B[topB++] = disk; // Обновляем to
        else C[topC++] = disk;

        // записываем max высоты после этого хода
        int m = topA;
        if (topB > m) m = topB;
        if (topC > m) m = topC;
        maxAtStep[stepIndex++] = m;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();

        // totalSteps = 2^N - 1 (безопасно для N <= ~30; тесты используют N up to 21)
        totalSteps = (int)((1L << N) - 1L);

        // инициализация стержней
        A = new int[N];
        B = new int[N];
        C = new int[N];
        for (int i = 0; i < N; i++) A[i] = N - i; // большие внизу
        topA = N; topB = 0; topC = 0;

        maxAtStep = new int[totalSteps];
        stepIndex = 0;

        // Генерация последовательности ходов (заполнит maxAtStep)
        hanoi(N, 0, 1, 2);

        // DSU над шагами; будем объединять шаги с одинаковым maxHeight
        DSU dsu = new DSU(totalSteps);

        // массив представителей по maxHeight: rep[h] = index шага первого встретившегося max=h
        int[] rep = new int[N + 1];
        for (int i = 0; i <= N; i++) rep[i] = -1;

        for (int i = 0; i < totalSteps; i++) {
            int h = maxAtStep[i];
            if (rep[h] == -1) rep[h] = i;
            else dsu.union(i, rep[h]);
        }

        // подсчёт размеров компонент (по корню)
        int[] count = new int[totalSteps];
        int nonzero = 0;
        for (int i = 0; i < totalSteps; i++) {
            int r = dsu.find(i);
            if (count[r] == 0) nonzero++;
            count[r]++;
        }

        // собираем ненулевые размеры в массив
        int[] sizes = new int[nonzero];
        int idx = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (count[i] > 0) sizes[idx++] = count[i];
        }

        // сортировка по возрастанию
        Arrays.sort(sizes);

        // печать через пробел
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
        System.out.println();
    }
}
