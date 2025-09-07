package by.it.group451002.mitskevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на наибольшую возрастающую подпоследовательность (LIS)
https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
Решение — метод динамического программирования.
*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result); // вывод длины LIS
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // считываем размер массива
        int n = scanner.nextInt();
        int[] m = new int[n];

        // считываем сам массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // массив dp[i] будет хранить длину LIS, заканчивающейся в i-том элементе
        int[] dp = new int[n];

        // изначально каждый элемент сам по себе — это подпоследовательность длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // основное динамическое обновление
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // если текущий элемент больше предыдущего — можно расширить LIS
                if (m[j] < m[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // ищем максимальное значение в dp — это и есть длина LIS
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
