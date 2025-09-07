package by.it.group410901.sadouski.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m+1][n+1];
        String[][] operations = new String[m+1][n+1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            operations[i][0] = repeat("-", i);
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            operations[0][j] = repeat("+", j);
        }

        // Заполнение матрицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i-1) == two.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                    operations[i][j] = operations[i-1][j-1] + "#,";
                } else {
                    int del = dp[i-1][j];
                    int ins = dp[i][j-1];
                    int rep = dp[i-1][j-1];

                    if (del <= ins && del <= rep) {
                        dp[i][j] = del + 1;
                        operations[i][j] = operations[i-1][j] + "-" + one.charAt(i-1) + ",";
                    } else if (ins <= del && ins <= rep) {
                        dp[i][j] = ins + 1;
                        operations[i][j] = operations[i][j-1] + "+" + two.charAt(j-1) + ",";
                    } else {
                        dp[i][j] = rep + 1;
                        operations[i][j] = operations[i-1][j-1] + "~" + two.charAt(j-1) + ",";
                    }
                }
            }
        }

        return operations[m][n];
    }

    private String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
            if (s.equals("-")) {
                sb.append((char)('a' + i % 26)); // Просто пример, нужно адаптировать
            } else if (s.equals("+")) {
                sb.append((char)('a' + i % 26)); // Просто пример, нужно адаптировать
            }
            sb.append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}