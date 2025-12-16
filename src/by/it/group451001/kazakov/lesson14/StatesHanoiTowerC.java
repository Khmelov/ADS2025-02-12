package by.it.group451001.kazakov.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // Рекурсивная функция для решения Ханойской башни
    private static void hanoi(int n, int from, int to, int[] cnt, int[] res) {
        // Вспомогательный стержень
        final int aux = 3 - from - to;

        if (n == 1) {
            // Базовый случай: перемещаем один диск
            cnt[from]--;  // Убираем диск с исходного стержня
            cnt[to]++;    // Кладем диск на целевой стержень
            // Находим максимальное количество дисков на одном стержне
            // и увеличиваем соответствующий счетчик в массиве res
            res[Arrays.stream(cnt).max().getAsInt()]++;
        } else {
            // Рекурсивный случай:
            // 1. Перемещаем n-1 диск на вспомогательный стержень
            hanoi(n - 1, from, aux, cnt, res);

            // 2. Перемещаем самый большой диск на целевой стержень
            cnt[from]--;
            cnt[to]++;
            res[Arrays.stream(cnt).max().getAsInt()]++;

            // 3. Перемещаем n-1 диск с вспомогательного на целевой стержень
            hanoi(n - 1, aux, to, cnt, res);
        }
    }

    // Основная функция решения задачи
    private static int[] solve(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("Аргумент меньше нуля");

        // res[i] - сколько раз встречалось состояние с максимальной высотой i
        final int[] res = new int[N + 1];
        // cnt[i] - сколько дисков на стержне i (всего 3 стержня)
        final int[] cnt = new int[3];

        // Изначально все N дисков на стержне 0
        cnt[0] = N;

        // Запускаем рекурсивное решение
        hanoi(N, 0, 1, cnt, res);

        return res;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Читаем количество дисков
            final int N = sc.nextInt();

            // Получаем массив с результатами
            final int[] res = solve(N);

            // Сортируем массив результатов
            Arrays.parallelSort(res);

            // Пропускаем нулевые значения в начале массива
            int i = 0;
            while (res[i] == 0)
                i++;

            // Выводим ненулевые результаты
            for (; i <= N; i++)
                System.out.print(res[i] + " ");
        } catch (Exception e) {
            // Обработка исключений
            e.printStackTrace();
        }
    }
}