package by.it.group410902.plekhova.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    static int idx;
    static int[] maxHeights;

    // Рекурсивная генерация ходов Ханойских башен;
    // pegs высотой N, heights — текущее количество дисков на каждом столбе.
    static void move(int n, int from, int to, int aux, int[] heights, int[][] pegs) {
        if (n == 0) return;

        move(n - 1, from, aux, to, heights, pegs);

        // делаем ход: снимаем диск с from и кладём на to
        heights[from]--;
        pegs[from][heights[from]] = 0;

        pegs[to][heights[to]] = 1;
        heights[to]++;

        // сохраняем maxHeight этого состояния (после хода)
        int mh = heights[0];
        if (heights[1] > mh) mh = heights[1];
        if (heights[2] > mh) mh = heights[2];

        maxHeights[idx++] = mh;

        move(n - 1, aux, to, from, heights, pegs);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();

        if (N <= 0) {
            System.out.println();
            return;
        }

        int totalStates = (1 << N) - 1; // число ходов (состояний после ходов)
        maxHeights = new int[totalStates];
        idx = 0;

        // представление столбов: pegs[peg][level] (0/1), но нам важно только heights[]
        int[][] pegs = new int[3][N];
        int[] heights = new int[]{N, 0, 0}; // изначально все N на A

        // заполним A единицами
        for (int i = 0; i < N; i++) pegs[0][i] = 1;

        // генерируем все ходы (и заполняем maxHeights)
        move(N, 0, 1, 2, heights, pegs);

        // подсчитываем частоты по maxHeight (диапазон 1..N)
        int[] freq = new int[N + 1]; // индекс 0 не используется
        for (int i = 0; i < totalStates; i++) {
            int mh = maxHeights[i];
            if (mh >= 1 && mh <= N) freq[mh]++;
        }

        // собираем ненулевые частоты в массив результатов
        int countNonZero = 0;
        for (int h = 1; h <= N; h++) if (freq[h] > 0) countNonZero++;

        int[] result = new int[countNonZero];
        int p = 0;
        for (int h = 1; h <= N; h++) {
            if (freq[h] > 0) result[p++] = freq[h];
        }

        // сортируем по возрастанию как требует тест
        Arrays.sort(result);

        // печатаем
        StringBuilder out = new StringBuilder();
        for (int v : result) {
            out.append(v).append(" ");
        }
        // выводим (остаточный пробел допустим для тестера)
        System.out.print(out.toString().trim());
    }
}
