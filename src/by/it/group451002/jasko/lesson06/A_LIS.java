package by.it.group451002.jasko.lesson06;

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
        // Получаем входной поток данных из файла
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса для решения задачи
        A_LIS instance = new A_LIS();
        // Получаем результат и выводим его
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    // Метод для нахождения длины наибольшей возрастающей подпоследовательности
    int getSeqSize(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение длины последовательности
        int n = scanner.nextInt();
        // Создание массива для хранения последовательности чисел
        int[] sequence = new int[n];

        // Чтение последовательности чисел
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Массив для динамического программирования:
        // dp[i] - длина наибольшей возрастающей подпоследовательности,
        // оканчивающейся на элементе sequence[i]
        int[] dp = new int[n];

        // Инициализация массива dp:
        // Каждый элемент сам по себе является подпоследовательностью длины 1
        Arrays.fill(dp, 1);

        // Заполнение массива dp:
        // Для каждого элемента sequence[i] проверяем все предыдущие элементы sequence[j]
        // (где j < i), и если sequence[i] > sequence[j], то возможно увеличить dp[i]
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше предыдущего и может увеличить длину подпоследовательности
                if (sequence[i] > sequence[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1; // Обновляем длину подпоследовательности
                }
            }
        }

        // Нахождение максимального значения в массиве dp:
        // Это и будет длина наибольшей возрастающей подпоследовательности
        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLength;
    }
}