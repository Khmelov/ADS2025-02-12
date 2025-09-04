package by.it.group410901.papou.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5
*/

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Create DP table
        int[][] dp = new int[m + 1][n + 1];

        // Initialize first row and column
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Cost of deleting characters from 'one'
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Cost of inserting characters from 'two'
        }

        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Characters match, no edit needed
                } else {
                    // Minimum of insertion, deletion, substitution
                    dp[i][j] = Math.min(dp[i][j - 1] + 1, // Insertion
                            Math.min(dp[i - 1][j] + 1, // Deletion
                                    dp[i - 1][j - 1] + 1)); // Substitution
                }
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}