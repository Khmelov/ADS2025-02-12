package by.it.group451001.zabelich.lesson06;

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
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
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
        // dp[i] будет содержать длину наибольшей кратной подпоследовательности, заканчивающейся в элементе m[i]
        int[] dp = new int[n];

        // Заполняем массив dp начальными значениями (минимальная длина = 1)
        for (int i = 0; i < n; i++) {
            dp[i] = 1;  // Каждый элемент сам по себе является подпоследовательностью длины 1
        }

        // Заполнение массива dp по принципу динамического программирования
        for (int i = 1; i < n; i++) {
            // Проверяем все предыдущие элементы
            for (int j = 0; j < i; j++) {
                // Если текущий элемент делится на предыдущий без остатка и длина подпоследовательности может быть увеличена
                if (m[i] % m[j] == 0 && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;  // Обновляем длину подпоследовательности для текущего элемента
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