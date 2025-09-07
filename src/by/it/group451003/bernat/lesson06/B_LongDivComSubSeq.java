package by.it.group451003.bernat.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j+1]] делится на A[i[j]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/
public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataB.txt"
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        // Проверяем, что файл существует
        if (stream == null) {
            throw new FileNotFoundException("Input file dataB.txt not found");
        }
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        // Вызываем метод для вычисления длины наибольшей кратной подпоследовательности
        int result = instance.getDivSeqSize(stream);
        // Выводим результат
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
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

            // Реализуем динамическое программирование для поиска наибольшей кратной подпоследовательности
            // Создаем массив dp, где dp[i] — длина наибольшей кратной подпоследовательности, заканчивающейся на m[i]
            int[] dp = new int[n];
            // Инициализируем массив dp: каждый элемент сам по себе является подпоследовательностью длины 1
            for (int i = 0; i < n; i++) {
                dp[i] = 1;
            }

            // Вычисляем длину наибольшей кратной подпоследовательности для каждого элемента
            for (int i = 1; i < n; i++) {
                // Проверяем все предыдущие элементы
                for (int j = 0; j < i; j++) {
                    // Если текущий элемент m[i] делится на m[j]
                    if (m[i] % m[j] == 0) {
                        // Обновляем dp[i], если можно увеличить длину подпоследовательности, добавив m[i]
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
            }

            // Находим максимальную длину подпоследовательности среди всех dp[i]
            int result = 0;
            for (int i = 0; i < n; i++) {
                result = Math.max(result, dp[i]);
            }

            // Возвращаем длину наибольшей кратной подпоследовательности
            return result;
        } finally {
            // Закрываем Scanner, чтобы избежать утечек ресурсов
            scanner.close();
        }
    }
}