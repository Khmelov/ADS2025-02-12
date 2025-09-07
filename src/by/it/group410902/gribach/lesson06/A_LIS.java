package by.it.group410902.gribach.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        // Чтение длины исходной последовательности
        int n = scanner.nextInt();

        // Создаем массив для хранения последовательности
        int[] m = new int[n];

        // Считываем саму последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Инициализируем переменную для хранения результата — длины LIS
        int result = 0;

        // Массив dp будет хранить длины LIS, заканчивающейся на каждом индексе
        int[] dp = new int[n];

        // Изначально любая отдельная точка — это LIS длины 1
        Arrays.fill(dp, 1);

        // Основной цикл для вычисления dp-массива
        for (int i = 1; i < n; i++) {
            // Проверяем все предыдущие элементы перед i
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше предыдущего,
                // то проверяем, можно ли увеличить длину подпоследовательности
                if (m[i] > m[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Находим максимальное значение среди всех dp[i] — это и будет длина LIS
        for (int length : dp) {
            if (length > result) {
                result = length;
            }
        }

        // Возвращаем результат
        return result;
    }
}
