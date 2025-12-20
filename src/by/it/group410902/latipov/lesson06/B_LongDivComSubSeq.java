package by.it.group410902.latipov.lesson06;

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
    где каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j]] < A[i[j+1]] и A[i[j+1]] делится на A[i[j]].

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] sequence = new int[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей кратной подпоследовательности, оканчивающейся на элементе i
        int[] dp = new int[n];

        // Инициализация: каждый элемент сам по себе является подпоследовательностью длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Заполняем массив dp
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем условие: sequence[i] должен делиться на sequence[j] без остатка
                // И sequence[i] должен быть больше sequence[j] (по условию возрастания)
                if (sequence[i] % sequence[j] == 0 && sequence[i] > sequence[j]) {
                    if (dp[i] < dp[j] + 1) {
                        dp[i] = dp[j] + 1;
                    }
                }
            }
        }

        // Находим максимальную длину
        int maxLength = 0;
        for (int length : dp) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        return maxLength;
    }
}