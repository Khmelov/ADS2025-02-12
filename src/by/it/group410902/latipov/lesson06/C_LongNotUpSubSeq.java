package by.it.group410902.latipov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

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

        return findLongestNonIncreasingSubsequence(m);
    }

    private int findLongestNonIncreasingSubsequence(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0;

        // tails[i] хранит наименьший последний элемент подпоследовательности длины i+1
        int[] tails = new int[n];
        // prev хранит индексы предыдущих элементов для восстановления подпоследовательности
        int[] prev = new int[n];
        // pos хранит позиции элементов в массиве tails
        int[] pos = new int[n];

        Arrays.fill(prev, -1);

        int len = 0; // текущая длина наибольшей подпоследовательности

        for (int i = 0; i < n; i++) {
            int left = 0;
            int right = len;

            // Бинарный поиск позиции для вставки текущего элемента
            // Ищем первый элемент, который МЕНЬШЕ текущего (для невозрастающей)
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (arr[tails[mid]] >= arr[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Обновляем prev для восстановления подпоследовательности
            if (left > 0) {
                prev[i] = tails[left - 1];
            }

            tails[left] = i;
            pos[i] = left;

            if (left == len) {
                len++;
            }
        }

        // Восстанавливаем подпоследовательность
        int[] resultSequence = new int[len];
        int current = tails[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultSequence[i] = current + 1; // +1 потому что индексы с 1
            current = prev[current];
        }

        // Выводим результат согласно формату
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(resultSequence[i] + " ");
        }
        System.out.println();

        return len;
    }
}