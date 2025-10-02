package by.it.group451001.serganovskij.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
*/
public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса
        A_LIS instance = new A_LIS();
        // Вычисляем и выводим результат
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // Читаем общую длину последовательности
        int n = scanner.nextInt();
        // Создаем массив для хранения элементов последовательности
        int[] sequence = new int[n];

        // Читаем всю последовательность из входных данных
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Инициализируем переменную для хранения результата
        int maxLength = 1;

        // Создаем массив dp, где dp[i] будет хранить длину наибольшей возрастающей
        // подпоследовательности, заканчивающейся в элементе sequence[i]
        int[] dp = new int[n];

        // Базовый случай: для каждого отдельного элемента длина LIS равна 1
        dp[0] = 1;

        // Заполняем массив dp
        for (int i = 1; i < n; i++) {
            // Изначально предполагаем, что LIS состоит только из текущего элемента
            dp[i] = 1;

            // Проверяем все предыдущие элементы
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше предыдущего, то можем расширить подпоследовательность
                if (sequence[j] < sequence[i]) {
                    // Выбираем максимальную длину между текущим значением dp[i] и dp[j] + 1
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }

            // Обновляем максимальную длину подпоследовательности
            maxLength = Math.max(maxLength, dp[i]);
        }

        // Возвращаем максимальную длину возрастающей подпоследовательности
        return maxLength;
    }
}