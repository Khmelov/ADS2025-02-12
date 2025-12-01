package by.it.group451004.redko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        List<Integer> tail = new ArrayList<>();
        int[] prev = new int[n];
        int[] indices = new int[n];

        for (int i = 0; i < n; i++) {
            int num = m[i];
            int pos = binarySearch(tail, num, m);

            if (pos == tail.size()) {
                tail.add(i);
            } else {
                tail.set(pos, i);
            }

            indices[pos] = i;
            prev[i] = pos > 0 ? indices[pos - 1] : -1;
        }

        int length = tail.size();
        List<Integer> sequence = new ArrayList<>();
        int current = tail.get(length - 1);
        while (current != -1) {
            sequence.add(current + 1); // +1 для индексации с 1
            current = prev[current];
        }
        Collections.reverse(sequence);

        System.out.println(length);
        for (int idx : sequence) {
            System.out.print(idx + " ");
        }

        return length;
    }

    private int binarySearch(List<Integer> tail, int num, int[] m) {
        int left = 0;
        int right = tail.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (m[tail.get(mid)] < num) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}