package by.it.group451004.kahnovich.lesson01.lesson06;

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
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        // Создаем сканер для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);

        // Читаем длину последовательности
        int n = scanner.nextInt();

        // Создаем массив для хранения последовательности
        int[] m = new int[n];

        // Считываем все элементы последовательности
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Переменная для хранения длины максимальной возрастающей подпоследовательности
        int result = 1;

        // Массив dp, где dp[i] — длина максимальной возрастающей подпоследовательности, заканчивающейся на элементе i
        int[] dp = new int[n];

        // Инициализируем первый элемент dp: подпоследовательность из одного элемента
        dp[0] = 1;

        // Для каждого элемента массива, начиная со второго
        for (int i = 1; i < n; i++) {
            dp[i] = 1; // минимум длина подпоследовательности — 1 (сам элемент)

            // Проверяем все элементы перед i-ым
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше, чем предыдущий — можно продолжить возрастающую последовательность
                if (m[j] < m[i]) {
                    // Обновляем dp[i], если найдена более длинная последовательность через dp[j]
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // Обновляем глобальный максимум длины подпоследовательности
            result = Math.max(result, dp[i]);
        }

        // Возвращаем длину самой длинной возрастающей подпоследовательности
        return result;
    }
}

