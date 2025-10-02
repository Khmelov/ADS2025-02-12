package by.it.group451001.demidovich.lesson01.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность
Дано:
    целое число 1<=n<=1E5
    массив A[1…n] натуральных чисел, не превосходящих 2E9.
Необходимо:
    Выведите максимальное 1<=k<=n, для которого найдётся подпоследовательность индексов i[1]<i[2]<…<i[k],
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].
    Sample Input:
    5
    5 3 4 4 2
    Sample Output:
    4
    1 3 4 5
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] — длина наибольшей невозрастающей подпоследовательности, заканчивающейся на a[i]
        int[] dp = new int[n];
        // prev[i] — индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (a[j] >= a[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Найдём индекс конца максимальной подпоследовательности
        int maxLength = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановим путь
        List<Integer> indices = new ArrayList<>();
        while (lastIndex != -1) {
            indices.add(0, lastIndex + 1); // +1, так как индексация с 1
            lastIndex = prev[lastIndex];
        }

        // Вывод результата
        System.out.println(maxLength);
        for (int idx : indices) {
            System.out.print(idx + " ");
        }

        return maxLength;
    }
}