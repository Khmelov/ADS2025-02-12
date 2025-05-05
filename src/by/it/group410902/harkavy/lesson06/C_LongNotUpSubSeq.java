package by.it.group410902.harkavy.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k],
    для которой A[i[1]]>=A[i[2]]>=…>=A[i[k]].

    В первой строке выведите её длину k,
    во второй — её индексы (1-based) в порядке возрастания индексов.

Решение за O(n log n) с восстановлением пути.
*/
public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class
                .getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream);
    }

    /**
     * Читает из stream, находит и печатает:
     * 1) длину НВП,
     * 2) 1-based индексы одной из самых длинных невозрастающих подпоследовательностей.
     * Возвращает длину (если нужно).
     */
    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // parent[i] — предыдущий индекс в подпоследовательности, кончающейся в i
        int[] parent = new int[n];
        // dIdx[len] = индекс элемента, которым завершается лучшая ННВП длины len в массиве b[]
        int[] dIdx = new int[n + 1];
        // dVal[len] = значение b[dIdx[len]], где b[i] = –a[i]; ищем ННВП в b[]
        long[] dVal = new long[n + 1];

        int length = 0;
        for (int i = 0; i < n; i++) {
            long b = - (long) a[i];
            // находим pos = первая позиция, где dVal[pos] > b  (upper_bound)
            int lo = 1, hi = length, pos = length + 1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (dVal[mid] > b) {
                    pos = mid;
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }
            // связываем i с концом подпоследовательности длины pos-1
            parent[i] = (pos > 1 ? dIdx[pos - 1] : -1);
            dVal[pos] = b;
            dIdx[pos] = i;
            if (pos > length) {
                length = pos;
            }
        }

        // восстанавливаем индексы НВП
        int[] seq = new int[length];
        int cur = dIdx[length];
        for (int p = length - 1; p >= 0; p--) {
            seq[p] = cur + 1;  // переводим в 1-based
            cur = parent[cur];
        }

        // вывод
        System.out.println(length);
        for (int idx : seq) {
            System.out.print(idx + " ");
        }
        return length;
    }
}