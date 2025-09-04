package by.it.group451004.belkovich.lesson06;

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        int l = 0;
        int[] m = new int[n + 1];

        int[] predInd = new int[n]; //predInd[k] - хранит индекс предшественника arr[k] в наибольшей
        //возрастающей подпоследовательности оканчивающейся в arr[k]
        //predInd[m[l]] - позволяет рекурсивно собрать последовательность
        //arr[m[l]], arr[predInd[m[l]]], arr[predInd[predInd[m[l]]]], arr[predInd[predInd[predInd[m[l]]]]], ...
        for (int i = 0; i < n; i++) {
            int lo = 1;
            int hi = l;

            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                if (arr[m[mid]] >= arr[i]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            int newL = lo;
            predInd[i] = m[newL - 1];
            m[newL] = i;
            if (newL > l) {
                l = newL;
            }
        }
        printIndexes(m[l], predInd);
        System.out.println();
        return l;
    }

    void printIndexes(int lastInd, int[] predInd){
        if (predInd[lastInd] > 0) {
            printIndexes(predInd[lastInd], predInd);
        }
        System.out.print(lastInd + 1 + " ");
    }

    private class ValInd {
        int value;
        int index;

        ValInd(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

}