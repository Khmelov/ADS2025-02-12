package by.it.group451003.burshtyn.lesson06;

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
*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        // dp[i] = длина НВП, заканчивающегося в позиции i
        int[] dp = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1;  // минимальная длина — сам элемент
            for (int j = 0; j < i; j++) {
                if (A[j] < A[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        return result;
    }
}
