package by.it.group410902.andala.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    // Метод для вычисления редакционного расстояния и восстановления последовательности операций
    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // dp[i][j] — минимальное количество операций, чтобы превратить первые i символов one в j символов two
        int[][] dp = new int[m + 1][n + 1];

        // Заполнение первой строки (превращение непустой строки в пустую — удаления)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Заполнение первого столбца (превращение пустой строки в непустую — вставки)
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполнение основной части таблицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, операция не требуется
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Минимум из: замены, вставки, удаления
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1], // замена
                            Math.min(
                                    dp[i][j - 1], // вставка
                                    dp[i - 1][j]  // удаление
                            )
                    );
                }
            }
        }

        // Восстановление пути операций
        StringBuilder result = new StringBuilder();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            // Совпадение символов — операция не требуется
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            // Удаление символа из one
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Вставка символа из two
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
            // Замена символа one на символ из two
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
        }

        // Возвращаем итоговую строку операций
        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);

        // Вывод результата для каждой пары строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
