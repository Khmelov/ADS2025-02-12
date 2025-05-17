package by.it.group410902.gribach.lesson06;

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
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
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
        // Инициализация переменной результата и массива динамики
        int result = 0;

        // dp[i] будет хранить длину максимальной подпоследовательности, заканчивающейся в элементе i,
        // где каждый последующий элемент делится на предыдущий без остатка
        int[] dp = new int[n];

        // По умолчанию каждый элемент может быть подпоследовательностью длины 1
        Arrays.fill(dp, 1);

        // Основной цикл динамического программирования
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Если m[i] делится на m[j], то можно продлить подпоследовательность
                if (m[i] % m[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Поиск максимального значения в массиве dp — это и есть ответ
        for (int length : dp) {
            if (length > result) {
                result = length;
            }
        }

        // Возвращаем результат
        return result;
    }

}