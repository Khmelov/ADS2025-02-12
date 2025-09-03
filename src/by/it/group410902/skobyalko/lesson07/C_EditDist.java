package by.it.group410902.skobyalko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        ///!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];

        // Заполнение базовых случаев: пустые префиксы
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // Заполняем dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // совпадение (копирование)
                } else {
                    int insert = dp[i][j - 1] + 1;    // вставка
                    int delete = dp[i - 1][j] + 1;    // удаление
                    int replace = dp[i - 1][j - 1] + 1; // замена
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }

        // Восстановление пути редакционных предписаний
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                // копирование
                sb.insert(0, "#,");
                i--;
                j--;
            } else if (j > 0 && (i == 0 || dp[i][j] == dp[i][j - 1] + 1)) {
                // вставка
                sb.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            } else if (i > 0 && (j == 0 || dp[i][j] == dp[i - 1][j] + 1)) {
                // удаление
                sb.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else {
                // замена
                sb.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
        }

        String result = sb.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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

