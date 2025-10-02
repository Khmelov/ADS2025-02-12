package by.it.group451003.burshtyn.lesson06;

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
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        // dp[i] = длина наибольшей "кратной" подпоследовательности, заканчивающейся на A[i]
        int[] dp = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // подпоследовательность из самого элемента
            for (int j = 0; j < i; j++) {
                if (A[i] % A[j] == 0 && dp[j] + 1 > dp[i]) {
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
