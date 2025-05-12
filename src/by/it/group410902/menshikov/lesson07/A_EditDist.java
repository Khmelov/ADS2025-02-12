package by.it.group410902.menshikov.lesson07;

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
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

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

public class A_EditDist {

    private int[][] dp;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length(), n = two.length();
        dp = new int[m + 1][n + 1];

        // Базовые случаи
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return recursiveLevenshtein(one, two, m, n);
    }
    private int recursiveLevenshtein(String one, String two, int i, int j) {
        // Если значение уже вычислено, возвращаем его
        if (dp[i][j] != -1) return dp[i][j];
        // Базовые случаи:
        if (i == 0) {
            dp[i][j] = j;
            return j;
        }
        if (j == 0) {
            dp[i][j] = i;
            return i;
        }
        // Если символы совпадают, переходим к следующим
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = recursiveLevenshtein(one, two, i - 1, j - 1);
            return dp[i][j];
        }
        // Иначе вычисляем минимальную стоимость из трех операций
        int insert = recursiveLevenshtein(one, two, i, j - 1);
        int delete = recursiveLevenshtein(one, two, i - 1, j);
        int replace = recursiveLevenshtein(one, two, i - 1, j - 1);

        dp[i][j] = 1 + Math.min(Math.min(insert, delete), replace);
        return dp[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
