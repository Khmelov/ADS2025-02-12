package by.it.group410901.papou.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.println(result);
        // Индексы должны быть выведены в методе getNotUpSeqSize или отдельно, в зависимости от теста
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей невозрастающей подпоследовательности, заканчивающейся на индексе i
        int[] dp = new int[n];
        // prev[i] - предыдущий индекс в подпоследовательности для индекса i
        int[] prev = new int[n];
        // Инициализация
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1; // -1 означает, что нет предыдущего элемента
        }

        // Динамическое программирование
        int maxLen = 1; // Длина наибольшей подпоследовательности
        int maxIndex = 0; // Индекс, где заканчивается наибольшая подпоследовательность
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIndex = i;
            }
        }

        // Восстановление индексов подпоследовательности
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = maxIndex; i != -1; i = prev[i]) {
            indices.add(i + 1); // Индексы начинаются с 1
        }

        // Вывод индексов в порядке возрастания
        for (int i = indices.size() - 1; i >= 0; i--) {
            System.out.print(indices.get(i) + " ");
        }
        System.out.println(); // Пустая строка после индексов

        return maxLen; // Возвращаем длину подпоследовательности
    }
}