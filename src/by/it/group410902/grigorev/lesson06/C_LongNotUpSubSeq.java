package by.it.group410902.grigorev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class C_LongNotUpSubSeq {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataC.txt"
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");

        // Создаем экземпляр класса
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();

        // Определяем длину и индексы элементов наибольшей невозрастающей последовательности
        int[] result = instance.getNotUpSeqSizeAndIndices(stream);

        // Выводим длину последовательности
        System.out.println(result[0]);

        // Выводим индексы элементов последовательности
        for (int i = 1; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
    }

    int[] getNotUpSeqSizeAndIndices(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для считывания входных данных
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Заполняем массив входными значениями
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массивы для хранения данных
        int[] dp = new int[n];

        int[] prev = new int[n];

        // Переменные для хранения длины и последнего элемента наибольшей последовательности
        int maxLength = 0;
        int lastIndex = -1;

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            dp[i] = 1;       // Начальная длина подпоследовательности всегда 1 (сам элемент)
            prev[i] = -1;    // -1 означает отсутствие предыдущего элемента

            // Проверяем все элементы перед текущим
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j; // Запоминаем индекс предыдущего элемента
                }
            }

            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем последовательность по массиву prev
        List<Integer> indices = new ArrayList<>();
        while (lastIndex != -1) {
            indices.add(lastIndex + 1); // +1 для 1-based индексации
            lastIndex = prev[lastIndex];
        }

        // Разворачиваем список, чтобы получить последовательность в правильном порядке
        Collections.reverse(indices);

        // Формируем результат
        int[] result = new int[indices.size() + 1];
        result[0] = maxLength; // Первый элемент — длина последовательности
        for (int i = 0; i < indices.size(); i++) {
            result[i + 1] = indices.get(i); // Остальные — индексы элементов
        }

        return result;
    }

    // Для совместимости с оригинальной сигнатурой
    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        return getNotUpSeqSizeAndIndices(stream)[0];
    }
}
