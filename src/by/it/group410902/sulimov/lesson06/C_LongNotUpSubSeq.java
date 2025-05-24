package by.it.group410902.sulimov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        int[] tailInds = new int[n]; // Индексы элементов в массиве m
        int[] prevs = new int[n];    // Индекс предыдущего элемента в подпоследовательности
        Arrays.fill(prevs, -1);

        int len = 0; // Текущая длина подпоследовательности

        for (int i = 0; i < n; i++) {
            // Бинарный поиск первого элемента в tailInds, который МЕНЬШЕ m[i]
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (m[tailInds[mid]] < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            // Обновляем tailInds
            tailInds[left] = i;

            // Запоминаем предыдущий элемент
            if (left > 0) {
                prevs[i] = tailInds[left - 1];
            }

            // Увеличиваем длину, если left == len
            if (left == len) {
                len++;
            }
        }

        // Восстановление последовательности
        int[] sequence = new int[len];
        int current = tailInds[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            sequence[i] = current + 1; // +1, так как индексация с 1
            current = prevs[current];
        }

        // Вывод результата
        System.out.println(len);
        for (int idx : sequence) {
            System.out.print(idx + " ");
        }

        return len;
    }

}
