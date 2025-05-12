package by.it.group410902.menshikov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

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
        int[] dp = new int[n]; // Длины подпоследовательностей
        int[] prev = new int[n]; // Для восстановления последовательности
        int[] tails = new int[n]; // Хвосты подпоследовательностей
        int len = 0; // Текущая длина наибольшей подпоследовательности

        for (int i = 0; i < n; i++) {
            int left = 0;
            int right = len;
            // Бинарный поиск места для текущего элемента
            while (left < right) {
                int mid = (left + right) / 2;
                if (m[tails[mid]] >= m[i]) left = mid + 1;
                else right = mid;
            }

            dp[i] = left + 1;
            if (left < len) tails[left] = i;
            else tails[len++] = i;
            prev[i] = left > 0 ? tails[left - 1] : -1;
        }

        // Восстановление последовательности
        int[] sequence = new int[len];
        int curr = tails[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            sequence[i] = curr + 1;
            curr = prev[curr];
        }

        // Вывод результатов
        for (int i = 0; i < len; i++) {
            System.out.print(sequence[i] + (i < len - 1 ? " " : "\n"));
        }
        int result = len;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}