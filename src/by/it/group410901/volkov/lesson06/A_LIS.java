package by.it.group410901.volkov.lesson06;

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
        // Создаем поток для чтения данных из файла
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса
        A_LIS instance = new A_LIS();
        // Получаем результат - длину наибольшей возрастающей подпоследовательности
        int result = instance.getSeqSize(stream);
        // Выводим результат
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        // Подготовка сканера для чтения данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение длины последовательности
        int n = scanner.nextInt();
        // Создание массива для хранения последовательности
        int[] m = new int[n];

        // Чтение всех элементов последовательности
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Инициализация массива для динамического программирования
        // dp[i] будет содержать длину НВП, заканчивающейся в элементе m[i]
        int[] dp = new int[n];

        // Заполняем массив dp начальными значениями (минимальная длина = 1)
        for (int i = 0; i < n; i++) {
            dp[i] = 1;  // Каждый элемент сам по себе является подпоследовательностью длины 1
        }

        // Заполнение массива dp по принципу динамического программирования
        for (int i = 1; i < n; i++) {
            // Проверяем все предыдущие элементы
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше предыдущего и длина НВП может быть увеличена
                if (m[i] > m[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;  // Обновляем длину НВП для текущего элемента
                }
            }
        }

        // Находим максимальное значение в массиве dp - это и будет ответом
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
