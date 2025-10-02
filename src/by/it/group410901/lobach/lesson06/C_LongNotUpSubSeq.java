package by.it.group410901.lobach.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Чтение входных данных
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Для хранения длин подпоследовательностей
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Каждый элемент сам по себе подпоследовательность длины 1

        // Для хранения последних элементов подпоследовательностей разной длины
        List<Integer> tails = new ArrayList<>();
        tails.add(m[0]);

        for (int i = 1; i < n; i++) {
            int x = m[i];

            // Бинарный поиск первого элемента в tails, который меньше x
            int l = 0, r = tails.size();
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (tails.get(mid) < x) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }

            if (l == tails.size()) {
                tails.add(x);
            } else {
                tails.set(l, x);
            }

            dp[i] = l + 1; // Обновляем длину для текущего элемента
        }

        // Находим максимальную длину
        int maxLength = 0;
        for (int len : dp) {
            if (len > maxLength) {
                maxLength = len;
            }
        }

        return maxLength;
    }
}