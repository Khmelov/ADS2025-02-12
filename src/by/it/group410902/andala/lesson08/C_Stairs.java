package by.it.group410902.andala.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();       // количество ступеней
        int[] stairs = new int[n];       // "очки" на каждой ступеньке

        // считываем значения для каждой ступеньки
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        // если ступенька всего одна — просто возвращаем её значение
        if (n == 1) return stairs[0];

        // dp[i] — максимальная сумма, которую можно набрать,
        // стоя на ступеньке i (учитывая все предыдущие шаги)
        int[] dp = new int[n];

        // база: на первую ступень можно просто встать
        dp[0] = stairs[0];

        // на вторую ступень можно попасть:
        // либо с первой (dp[0] + stairs[1]),
        // либо сразу на неё (stairs[1]) — берём максимум
        dp[1] = Math.max(stairs[1], stairs[0] + stairs[1]);

        // считаем для каждой следующей ступени
        for (int i = 2; i < n; i++) {
            // можно прийти либо с предыдущей, либо через одну:
            // выбираем путь, который даёт большую сумму
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        // итоговый ответ — максимум очков на последней ступеньке
        return dp[n - 1];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}
