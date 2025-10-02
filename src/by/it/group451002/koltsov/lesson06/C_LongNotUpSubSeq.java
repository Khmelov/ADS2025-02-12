package by.it.group451002.koltsov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
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

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // длина текущей LIS
        int maxLISLength = 0;

        int tempLength = 0;

        // массив хранящий последний элемент IS длины соответствующей индексу
        int[] lastLISNumInds = new int[n];

        // массив хранящий предыдущий для каждого элемента (соотв-ему индексу) элемент в LIS
        int[] lastLISNumsPrevElemsInds = new int[n + 1];

        // у нулевого элемента нет предыдущего
        lastLISNumInds[0] = -1;

        // переменные для бинарного поиска
        int left = 0;
        int right = 0;
        int mid = 0;

        // для каждого элемента из исходного массива
        // ищем последний элемент >= искомого
        for (int i = 0; i < m.length; i++) {
            left  = 1;
            right = maxLISLength + 1;
            while (left < right) {
                mid = (left + right) / 2;
                if (m[lastLISNumInds[mid]] < m[i])
                    right = mid;
                else
                    left = mid + 1;
            }

            // записываем найденный элемент как предшествующий анализируемому
            lastLISNumsPrevElemsInds[i] = lastLISNumInds[left - 1];
            // и ещё чё-то, сложно сформулировать
            lastLISNumInds[left] = i;

            // перезаписываем текущую max длину если необходимо
            if (left > maxLISLength)
                maxLISLength = left;
        }

        // восстанавливаем массив LIS
        int i = maxLISLength;
        int[] resultArr = new int[i];
        int j = i - 1;
        while (i >= 0) {
            resultArr[j] = m[i];
            j--;
            i = lastLISNumsPrevElemsInds[i];
        }

        // Выводим результаты
        System.out.println(resultArr.length);
        for (int k = 0; k < resultArr.length; k++) {
            System.out.print(resultArr[k] + " ");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return resultArr.length;
    }

}