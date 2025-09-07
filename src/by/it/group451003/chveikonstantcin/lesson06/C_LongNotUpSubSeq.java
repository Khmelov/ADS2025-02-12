package by.it.group451003.chveikonstantcin.lesson06;

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
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream  stream) throws FileNotFoundException {
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
            if (tail.isEmpty() || num <= m[tail.get(tail.size() - 1)]) {
                if (!tail.isEmpty()) {
                    prev[i] = tail.get(tail.size() - 1);
                } else {
                    prev[i] = -1;
                }
                tail.add(i);
            } else {
                int l = 0, r = tail.size() - 1;
                while (l < r) {
                    int mid = (l + r) / 2;
                    if (m[tail.get(mid)] >= num) {
                        l = mid + 1;
                    } else {
                        r = mid;
                    }
                }
                if (l > 0) {
                    prev[i] = tail.get(l - 1);
                } else {
                    prev[i] = -1;
                }
                tail.set(l, i);
            }
            indices[i] = tail.size() - 1;
        }

        int length = tail.size();
        System.out.println(length);

        List<Integer> sequence = new ArrayList<>();
        int current = tail.get(length - 1);
        while (current != -1) {
            sequence.add(current + 1); // +1 для индексации с 1
            current = prev[current];
        }
        Collections.reverse(sequence);

        for (int idx : sequence) {
            System.out.print(idx + " ");
        }

        return length;
    }

}