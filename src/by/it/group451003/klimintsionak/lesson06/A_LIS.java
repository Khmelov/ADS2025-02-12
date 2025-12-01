package by.it.group451003.klimintsionak.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/
public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        if (stream == null) {
            throw new FileNotFoundException("Input file dataA.txt not found");
        }
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
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
                dp[i] = 1; //каждый элемент - LIS длины 1
            }

            //вычисляем длину LIS
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (m[j] < m[i]) {
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