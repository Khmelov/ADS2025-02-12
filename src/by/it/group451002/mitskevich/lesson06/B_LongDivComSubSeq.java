package by.it.group451002.mitskevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j+1]] делится на A[i[j]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // массив dp хранит длину наибольшей кратной подпоследовательности, заканчивающейся на i-м элементе
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // каждый элемент сам по себе может быть началом подпоследовательности
        }

        // двойной цикл — сравниваем каждый i с предыдущими j
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // если текущий элемент делится на предыдущий
                if (m[i] % m[j] == 0) {
                    // обновляем dp[i], если найдено большее значение
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // находим максимум в массиве dp — это и есть ответ
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
