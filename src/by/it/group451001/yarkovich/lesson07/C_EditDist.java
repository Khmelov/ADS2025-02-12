package by.it.group451001.yarkovich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        String[][] op = new String[n + 1][m + 1]; // Таблица с операциями

        // Инициализация границ
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = "-";
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = "+";
        }

        op[0][0] = "#"; // совпадение на пустых строках

        // Основной цикл
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // возможные действия
                int del = dp[i - 1][j] + 1;
                int ins = dp[i][j - 1] + 1;
                int rep = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(Math.min(del, ins), rep);

                if (dp[i][j] == rep) {
                    op[i][j] = (cost == 0) ? "#" : "~";
                } else if (dp[i][j] == del) {
                    op[i][j] = "-";
                } else {
                    op[i][j] = "+";
                }
            }
        }

        // Восстановление ответа
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            String operation = op[i][j];
            switch (operation) {
                case "#":
                    sb.insert(0, "#,");
                    i--;
                    j--;
                    break;
                case "~":
                    sb.insert(0, "~" + two.charAt(j - 1) + ",");
                    i--;
                    j--;
                    break;
                case "-":
                    sb.insert(0, "-" + one.charAt(i - 1) + ",");
                    i--;
                    break;
                case "+":
                    sb.insert(0, "+" + two.charAt(j - 1) + ",");
                    j--;
                    break;
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // ab ab
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // short ports
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // distance editing
    }
}
