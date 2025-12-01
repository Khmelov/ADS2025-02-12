package by.it.group410901.getmanchuk.lesson14;

import java.util.*;

// Симуляция Ханойских башен с подсчётом шагов по максимальной высоте

public class StatesHanoiTowerC {

    // Количество колец
    private static int N;
    // Массив стержней, каждый хранит кольца
    private static int[][] pegs;
    // Высота (количество колец) на каждом стержне
    private static int[] tops;
    // Счетчик шагов по каждой высоте
    private static long[] counts;

    public static void main(String[] args) {

        // Чтение числа колец
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        N = scanner.nextInt();

        // Инициализация стержней и счетчиков
        pegs = new int[3][N];
        tops = new int[3];
        counts = new long[N + 1];

        // Заполнение исходного стержня A
        for (int i = 0; i < N; i++) pegs[0][i] = N - i;
        tops[0] = N;
        tops[1] = 0;
        tops[2] = 0;

        // Рекурсивный перенос всех колец с A на B через C
        move(N, 0, 1, 2);

        // Подсчет непустых высот
        int nonZero = 0;
        for (int h = 1; h <= N; h++)
            if (counts[h] > 0)
                nonZero++;

        // Формирование массива размеров поддеревьев
        long[] sizes = new long[nonZero];
        int idx = 0;
        for (int h = 1; h <= N; h++)
            if (counts[h] > 0)
                sizes[idx++] = counts[h];

        // Сортировка по возрастанию
        Arrays.sort(sizes);

        // Формирование строки вывода
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) out.append(' ');
            out.append(sizes[i]);
        }

        // Вывод результата
        System.out.print(out);
    }

    // Рекурсивная функция переноса n колец с src на dst через aux
    private static void move(int n, int src, int dst, int aux) {
        if (n == 0) return;

        // Перенос n-1 колец на вспомогательный стержень
        move(n - 1, src, aux, dst);

        // Перемещение верхнего кольца
        int disk = pop(src);
        push(dst, disk);

        // Определение текущей максимальной высоты
        int maxh = Math.max(tops[0], Math.max(tops[1], tops[2]));
        counts[maxh]++;

        // Перенос n-1 колец со вспомогательного стержня на целевой
        move(n - 1, aux, dst, src);
    }

    // Снятие верхнего кольца с указанного стержня
    private static int pop(int peg) {
        if (tops[peg] == 0) return -1;
        int val = pegs[peg][tops[peg] - 1];
        tops[peg]--;
        return val;
    }

    // Добавление кольца на вершину стержня
    private static void push(int peg, int disk) {
        pegs[peg][tops[peg]] = disk;
        tops[peg]++;
    }
}