package by.it.group451002.mitskevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 (ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ!)
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k],
    соблюдающая A[i[1]]>=A[i[2]]>= ... >=A[i[n]]..

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
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Считываем последовательность
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] — длина наибольшей невозрастающей подпоследовательности, заканчивающейся на i
        int[] dp = new int[n];
        // prev[i] — индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];

        int maxLen = 0;  // длина максимальной подпоследовательности
        int lastIndex = 0;  // последний индекс в этой подпоследовательности

        for (int i = 0; i < n; i++) {
            dp[i] = 1;  // каждый элемент сам по себе подпоследовательность длины 1
            prev[i] = -1; // по умолчанию нет предыдущего

            // сравниваем с предыдущими элементами
            for (int j = 0; j < i; j++) {
                // если текущее значение не больше предыдущего и можем увеличить длину
                if (a[i] <= a[j] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }

            // обновляем максимум
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIndex = i;
            }
        }

        // Восстановим путь по массиву prev
        ArrayList<Integer> path = new ArrayList<>();
        while (lastIndex != -1) {
            path.add(lastIndex + 1); // +1, потому что индексация с 1
            lastIndex = prev[lastIndex];
        }
        Collections.reverse(path); // разворачиваем путь, т.к. мы шли от конца к началу

        // Вывод результата
        System.out.println(maxLen);
        for (int index : path) {
            System.out.print(index + " ");
        }

        return maxLen;
    }
}
