package by.it.group410902.menshikov.lesson06;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        // Массив для хранения длин НВП для каждой позиции
        int[] dp = new int[n];
        int maxLength = 0;

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            // Проверяем все предыдущие элементы
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i] && dp[j] + 1 > dp[i]) dp[i] = dp[j] + 1;
            }
            // Обновляем максимальную длину
            if (dp[i] > maxLength) maxLength = dp[i];
        }
        int result = maxLength;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;

//        Итерация i=0: [5]
//        dp = [1, 1, 1, 1, 1]
//        Итерация i=1: [5, 1]
//        dp = [1, 1, 1, 1, 1] (нет элементов < 1)
//        Итерация i=2: [5, 1, 3]
//        dp = [1, 1, 2, 1, 1] (добавили 3 к подпоследовательности [1])
//        Итерация i=3: [5, 1, 3, 4]
//        dp = [1, 1, 2, 3, 1] (добавили 4 к подпоследовательности [1,3])
//        Итерация i=4: [5, 1, 3, 4, 2]
//        dp = [1, 1, 2, 3, 2] (добавили 2 к подпоследовательности [1])

    }
}
