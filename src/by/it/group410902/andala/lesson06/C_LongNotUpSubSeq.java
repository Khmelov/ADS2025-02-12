package by.it.group410902.andala.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача:
Найти наибольшую подпоследовательность, где элементы не возрастают (A[i1] >= A[i2] >= ...),
и индексы i1 < i2 < ... < ik.
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt(); // Считываем размер массива
        int[] A = new int[n]; // Исходный массив

        // Заполняем массив
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        int[] dp = new int[n];    // dp[i] — длина наибольшей невозрастающей подпоследовательности, заканчивающейся в A[i]
        int[] prev = new int[n];  // prev[i] — индекс предыдущего элемента в подпоследовательности

        for (int i = 0; i < n; i++) {
            dp[i] = 1;       // Минимальная длина подпоследовательности — 1 (сам элемент)
            prev[i] = -1;    // Пока не знаем, откуда пришли

            // Перебираем предыдущие элементы
            for (int j = 0; j < i; j++) {
                // Условие невозрастания: A[j] >= A[i]
                // И если можно улучшить длину dp[i]
                if (A[j] >= A[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j; // Запоминаем, откуда пришли
                }
            }
        }

        // Ищем индекс с максимальной длиной подпоследовательности
        int maxLen = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем последовательность индексов
        int[] seq = new int[maxLen];
        int k = maxLen - 1;
        while (lastIndex != -1) {
            seq[k--] = lastIndex + 1; // +1, так как индексация с 1 по условию
            lastIndex = prev[lastIndex]; // Переходим к предыдущему элементу
        }

        // Выводим результат
        System.out.println(maxLen);
        for (int i = 0; i < maxLen; i++) {
            System.out.print(seq[i] + " ");
        }
        System.out.println();

        return maxLen;
    }
}
