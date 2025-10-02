package by.it.group410902.andala.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Считываем количество элементов
        int n = scanner.nextInt();
        int[] m = new int[n]; // Массив чисел
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt(); // Заполнение массива
        }

        int[] dp = new int[n]; // dp[i] — длина наибольшей подпоследовательности, заканчивающейся на m[i]

        // Инициализация
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимум — сам элемент

            // Перебираем все предыдущие элементы
            for (int j = 0; j < i; j++) {
                // Если m[i] делится на m[j] (т.е. m[j] является делителем m[i])
                if (m[i] % m[j] == 0) {
                    // Обновляем dp[i], если найдено большее значение
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Находим максимум в массиве dp — это и будет ответ
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
