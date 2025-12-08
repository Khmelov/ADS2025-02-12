package by.it.group451004.volynets.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (n == 0) return 0;

        int[] dp = new int[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        // Находим индекс последнего элемента максимальной подпоследовательности
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] == result) {
                lastIndex = i;
                break;
            }
        }

        // Восстанавливаем подпоследовательность
        ArrayList<Integer> indices = new ArrayList<>();
        while (lastIndex != -1) {
            indices.add(lastIndex + 1); // +1 для индексации с 1
            lastIndex = prev[lastIndex];
        }
        Collections.reverse(indices);

        System.out.println(result);
        for (int index : indices) {
            System.out.print(index + " ");
        }

        return result;
    }

}