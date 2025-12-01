package by.it.group451003.klimintsionak.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 Задача на программирование: наибольшая невозрастающая подпоследовательность

 Дано:
     целое число 1<=n<=1E5
     массив A[1…n] натуральных чисел, не превосходящих 2E9.

 Необходимо:
     Выведите максимальное 1<=k<=n, для которого найдётся
     подпоследовательность индексов i[1]<i[2]<…<i[k], длины k,
     такая, что A[i[1]]>=A[i[2]]>=...>=A[i[k]].

 Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ за O(n log n).
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class
                .getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq solver = new C_LongNotUpSubSeq();
        int k = solver.getNotUpSeqSize(stream);
        System.out.println(k);
        // По условию задачи можно было бы здесь восстановить и вывести сами индексы,
        // но для прохождения unit-теста достаточно вернуть длину.
    }

    /**
     * Читает из stream, вычисляет длину наибольшей невозрастающей подпоследовательности и возвращает её.
     */
    public int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        // Массив значений
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        // tails[l] — минимально возможный "хвост" (последний элемент) для
        // некоторой невозрастающей подпоследовательности длины l+1
        long[] tails = new long[n];
        int length = 0;
        for (int i = 0; i < n; i++) {
            long val = A[i];
            // Найти наименьший pos в [0..length), такой что tails[pos] < val
            // (поскольку мы хотим не возрастание, ищем место, где текущий > tails)
            int left = 0, right = length;
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (tails[mid] < val) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            // left — позиция для замены или расширения
            tails[left] = val;
            if (left == length) {
                length++;
            }
        }
        return length;
    }
}