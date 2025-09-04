package by.it.group451002.vishnevskiy.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k],
    где A[i[1]] >= A[i[2]] >= ... >= A[i[k]].

    В первой строке: длина k
    Во второй строке: сами индексы i[1], ..., i[k] (индексация с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n]; // исходный массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Массив для хранения конца каждой длины подпоследовательности
        int[] tailIndices = new int[n];
        // Массив для восстановления пути
        int[] prev = new int[n];

        int length = 1; // длина найденной подпоследовательности
        Arrays.fill(prev, -1); // изначально нет предыдущих

        tailIndices[0] = 0; // начнем с первого элемента

        for (int i = 1; i < n; i++) {
            if (a[i] <= a[tailIndices[length - 1]]) {
                // Если a[i] может быть продолжением текущей максимальной подпоследовательности
                prev[i] = tailIndices[length - 1];
                tailIndices[length++] = i;
            } else {
                // Используем двоичный поиск, чтобы найти первое место, где a[i] < a[tailIndices[pos]]
                int left = 0, right = length - 1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (a[tailIndices[mid]] >= a[i]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                // Обновим
                tailIndices[left] = i;
                if (left > 0) {
                    prev[i] = tailIndices[left - 1];
                }
            }
        }

        // Восстановим последовательность индексов
        int[] resultIndices = new int[length];
        int idx = tailIndices[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = idx + 1; // переводим в индексацию с 1
            idx = prev[idx];
        }

        // Вывод результата
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(resultIndices[i] + " ");
        }

        return length;
    }
}
