package by.it.group410901.papou.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    +e,#,#,-s,#,~i,#,-c,~g,
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Create DP table and operation tracking table
        int[][] dp = new int[m + 1][n + 1];
        char[][] ops = new char[m + 1][n + 1]; // Store operations: I, D, R, M

        // Initialize first row and column
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            ops[i][0] = 'D'; // Deletion
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            ops[0][j] = 'I'; // Insertion
        }

        // Fill DP and ops tables
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    ops[i][j] = 'M'; // Match
                } else {
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    int min = Math.min(insert, Math.min(delete, replace));

                    dp[i][j] = min;
                    if (min == insert) {
                        ops[i][j] = 'I'; // Insertion
                    } else if (min == delete) {
                        ops[i][j] = 'D'; // Deletion
                    } else {
                        ops[i][j] = 'R'; // Replacement
                    }
                }
            }
        }

        // Backtrack to reconstruct operations
        ArrayList<String> result = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            char op = ops[i][j];
            if (op == 'M') {
                result.add("#"); // Match
                i--;
                j--;
            } else if (op == 'I') {
                result.add("+" + two.charAt(j - 1)); // Insert character from two
                j--;
            } else if (op == 'D') {
                result.add("-" + one.charAt(i - 1)); // Delete character from one
                i--;
            } else if (op == 'R') {
                result.add("~" + two.charAt(j - 1)); // Replace with character from two
                i--;
                j--;
            }
        }

        // Reverse and join operations
        StringBuilder sb = new StringBuilder();
        for (int k = result.size() - 1; k >= 0; k--) {
            sb.append(result.get(k)).append(",");
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