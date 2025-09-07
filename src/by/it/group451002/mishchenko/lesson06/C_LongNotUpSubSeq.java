package by.it.group451002.mishchenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
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
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        // Для решения задачи вводится массив dp, где dp[i] хранит длину
        // наибольшей невозрастающей подпоследовательности, заканчивающейся в позиции i.
        // Массив prev помогает восстановить саму последовательность, сохраняя индекс предыдущего элемента.
        int[] dp = new int[n];
        int[] prev = new int[n];

        // Изначально каждый элемент сам по себе представляет невозрастающую последовательность длины 1.
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1; // -1 означает отсутствие предыдущего элемента
            // Перебираем все предыдущие элементы, чтобы проверить возможность расширения последовательности.
            for (int j = 0; j < i; j++) {
                // Если m[j] больше или равно m[i], то m[i] может следовать за m[j] в последовательности.
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим позицию, где достигнута максимальная длина подпоследовательности.
        int maxLength = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем последовательность, двигаясь назад по индексу предшественников.
        ArrayList<Integer> indices = new ArrayList<>();
        int curIndex = lastIndex;
        while (curIndex != -1) {
            indices.add(curIndex + 1); // прибавляем 1, чтобы перевести в 1-индексацию
            curIndex = prev[curIndex];
        }
        // Переворачиваем список, чтобы получить последовательность в правильном порядке.
        Collections.reverse(indices);

        // Вывод результата: первая строка - длина последовательности, вторая - индексы элементов.
        System.out.println(maxLength);
        for (int index : indices) {
            System.out.print(index + " ");
        }
        System.out.println();
        int result = maxLength;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
