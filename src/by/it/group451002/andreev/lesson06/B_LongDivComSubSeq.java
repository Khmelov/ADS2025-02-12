package by.it.group451002.andreev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс B_LongDivComSubSeq решает задачу поиска наибольшей кратной подпоследовательности (LDCSS).
 * Используется динамическое программирование (DP), чтобы найти самую длинную подпоследовательность,
 * где каждый следующий элемент делится на предыдущий.
 */
public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // йЗагружаем входные данные из фала "dataB.txt"
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();

        // Вычисляем длину наибольшей кратной подпоследовательности
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    /**
     * Метод getDivSeqSize читает массив из входного потока и определяет длину LDCSS.
     * @param stream входной поток данных
     * @return длина наибольшей кратной подпоследовательности
     * @throws FileNotFoundException если файл не найден
     */
    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Заполняем массив входными данными
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив dp, где dp[i] хранит длину наибольшей кратной подпоследовательности,
        // заканчивающейся на индексе i
        int[] dp = new int[n];

        // Инициализация dp: каждый элемент сам по себе является подпоследовательностью длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Заполняем массив dp динамическим программированием
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем, делится ли m[i] на m[j] без остатка
                if (m[i] % m[j] == 0) {
                    // Если можем расширить подпоследовательность, обновляем dp[i]
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                    }
                }
            }
        }

        // Нахождение максимального значения в dp (длина наибольшей кратной подпоследовательности)
        int result = 0;
        for (int num : dp) {
            if (num > result) {
                result = num;
            }
        }

        return result;
    }
}
