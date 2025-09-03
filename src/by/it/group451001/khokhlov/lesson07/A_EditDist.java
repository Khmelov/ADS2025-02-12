package by.it.group451001.khokhlov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] memo; // таблица для мемоизации

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];

        // заполняем массив -1 (ещё не вычислено)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        return compute(one, two, n, m);
    }

    // Рекурсивная функция с мемоизацией
    private int compute(String a, String b, int i, int j) {
        if (memo[i][j] != -1)
            return memo[i][j];

        if (i == 0) {
            memo[i][j] = j; // вставить все символы строки b
        } else if (j == 0) {
            memo[i][j] = i; // удалить все символы строки a
        } else {
            int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

            memo[i][j] = Math.min(
                    Math.min(
                            compute(a, b, i - 1, j) + 1,     // удаление
                            compute(a, b, i, j - 1) + 1      // вставка
                    ),
                    compute(a, b, i - 1, j - 1) + cost       // замена или ничего
            );
        }

        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 0
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 3
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 5
    }
}
