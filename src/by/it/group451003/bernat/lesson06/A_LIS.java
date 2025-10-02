package by.it.group451003.bernat.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/
public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        // Проверяем, что файл существует
        if (stream == null) {
            throw new FileNotFoundException("Input file dataA.txt not found");
        }
        A_LIS instance = new A_LIS();
        // Вызываем метод для вычисления длины наибольшей возрастающей подпоследовательности
        int result = instance.getSeqSize(stream);
        // Выводим результат
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        try {
            // Проверяем, есть ли данные для размера массива
            if (!scanner.hasNextInt()) {
                throw new IllegalArgumentException("No input for array size");
            }
            // Считываем размер массива (n)
            int n = scanner.nextInt();
            // Проверяем, что размер массива находится в допустимом диапазоне
            if (n < 1 || n > 1000) {
                throw new IllegalArgumentException("Invalid array size: " + n);
            }

            // Создаем массив для хранения входных чисел
            int[] m = new int[n];
            // Читаем n чисел из входных данных
            for (int i = 0; i < n; i++) {
                // Проверяем, достаточно ли данных для чтения
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Insufficient input for array elements");
                }
                m[i] = scanner.nextInt();
                // Проверяем, что число не превышает 2E9
                if (m[i] > 2_000_000_000) {
                    throw new IllegalArgumentException("Array element exceeds 2E9: " + m[i]);
                }
            }

            // Реализуем динамическое программирование для поиска наибольшей возрастающей подпоследовательности (LIS)
            // Создаем массив dp, где dp[i] — длина LIS, заканчивающейся на элементе m[i]
            int[] dp = new int[n];
            // Инициализируем массив dp: каждый элемент сам по себе является LIS длины 1
            for (int i = 0; i < n; i++) {
                dp[i] = 1;
            }

            // Вычисляем длину LIS для каждого элемента
            for (int i = 1; i < n; i++) {
                // Проверяем все предыдущие элементы
                for (int j = 0; j < i; j++) {
                    // Если текущий элемент m[i] больше предыдущего m[j]
                    if (m[j] < m[i]) {
                        // Обновляем dp[i], если можно увеличить длину LIS, добавив m[i]
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
            }

            // Находим максимальную длину LIS среди всех dp[i]
            int result = 0;
            for (int i = 0; i < n; i++) {
                result = Math.max(result, dp[i]);
            }

            // Возвращаем длину наибольшей возрастающей подпоследовательности
            return result;
        } finally {
            // Закрываем Scanner, чтобы избежать утечек ресурсов
            scanner.close();
        }
    }
}