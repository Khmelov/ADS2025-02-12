package by.it.group451003.klimintsionak.lesson06;

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
        if (stream == null) {
            throw new FileNotFoundException("Input file dataB.txt not found");
        }
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        try {
            //проверка наличия ввода
            if (!scanner.hasNextInt()) {
                throw new IllegalArgumentException("No input for array size");
            }
            int n = scanner.nextInt();
            if (n < 1 || n > 1000) {
                throw new IllegalArgumentException("Invalid array size: " + n);
            }

            //чтение массива
            int[] m = new int[n];
            for (int i = 0; i < n; i++) {
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Insufficient input for array elements");
                }
                m[i] = scanner.nextInt();
                if (m[i] > 2_000_000_000) {
                    throw new IllegalArgumentException("Array element exceeds 2E9: " + m[i]);
                }
            }

            //динамическое программирование
            int[] dp = new int[n];
            for (int i = 0; i < n; i++) {
                dp[i] = 1; //каждый элемент - подпоследовательность длины 1
            }

            //вычисляем длину наибольшей кратной подпоследовательности
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (m[i] % m[j] == 0) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
            }

            //находим максимальную длину
            int result = 0;
            for (int i = 0; i < n; i++) {
                result = Math.max(result, dp[i]);
            }

            return result;
        } finally {
            scanner.close();
        }
    }
}