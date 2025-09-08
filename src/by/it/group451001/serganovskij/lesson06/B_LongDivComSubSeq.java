package by.it.group451001.serganovskij.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        // Вычисляем и выводим результат
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем длину последовательности
        int n = scanner.nextInt();
        int[] sequence = new int[n];

        // Читаем саму последовательность
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Массив dp будет хранить длины наибольших кратных подпоследовательностей,
        // заканчивающихся на каждом элементе
        int[] dp = new int[n];
        int maxLength = 1; // Минимальная длина подпоследовательности - 1

        // Инициализация: для каждого элемента минимальная длина подпоследовательности - 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Заполняем массив dp
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем два условия:
                // 1. Элемент sequence[j] должен быть меньше sequence[i] (чтобы сохранить порядок)
                // 2. sequence[i] должен делиться на sequence[j] без остатка
                if (sequence[j] < sequence[i] && sequence[i] % sequence[j] == 0) {
                    // Если условия выполнены, обновляем dp[i]
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // Обновляем максимальную длину
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}