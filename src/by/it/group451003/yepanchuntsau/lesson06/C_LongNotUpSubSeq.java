package by.it.group451003.yepanchuntsau.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n — длину подпоследовательности,
    в которой каждый элемент не больше предыдущего.

Решить задачу за O(n log n).
*/
public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class
                .getResourceAsStream("dataC.txt");
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

        int[] tail = new int[n];
        int len = 0;

        for (int i = 0; i < n; i++) {
            int x = -m[i];
            int l = 0, r = len;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (tail[mid] <= x) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            tail[l] = x;
            if (l == len) {
                len++;
            }
        }

        return len;
    }
}
