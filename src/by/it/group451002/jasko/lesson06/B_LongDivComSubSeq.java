package by.it.group451002.jasko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        // Получаем входной поток данных из файла
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        // Создаем экземпляр класса для решения задачи
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        // Получаем результат и выводим его
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    // Метод для нахождения длины наибольшей кратной подпоследовательности
    int getDivSeqSize(InputStream stream) {
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
        // dp[i] - длина наибольшей кратной подпоследовательности,
        // оканчивающейся на элементе sequence[i]
        int[] dp = new int[n];

        // Инициализация массива dp:
        // Каждый элемент сам по себе является подпоследовательностью длины 1
        Arrays.fill(dp, 1);

        // Заполнение массива dp:
        // Для каждого элемента sequence[i] проверяем все предыдущие элементы sequence[j]
        // (где j < i), и если sequence[i] делится на sequence[j], то возможно увеличить dp[i]
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем два условия:
                // 1. Текущий элемент делится на предыдущий без остатка
                // 2. Текущая длина подпоследовательности может быть увеличена
                if (sequence[i] % sequence[j] == 0 && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1; // Обновляем длину подпоследовательности
                }
            }
        }

        // Нахождение максимального значения в массиве dp:
        // Это и будет длина наибольшей кратной подпоследовательности
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