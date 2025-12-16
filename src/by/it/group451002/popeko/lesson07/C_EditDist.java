package by.it.group451002.popeko.lesson07;

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
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        int[][] dp = new int[m + 1][n + 1];
        String[][] ops = new String[m + 1][n + 1]; // Массив для хранения операций

        // Заполнение базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            ops[i][0] = "-"+ (i > 0 ? one.charAt(i - 1) : "") + ",";
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            ops[0][j] = "+"+ (j > 0 ? two.charAt(j - 1) : "") + ",";
        }

        // Заполнение таблицы расстояний Левенштейна
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    ops[i][j] = "#,";
                } else {
                    int replace = dp[i - 1][j - 1] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;

                    dp[i][j] = Math.min(replace, Math.min(insert, delete));

                    if (dp[i][j] == replace) {
                        ops[i][j] = "~" + two.charAt(j - 1) + ",";
                    } else if (dp[i][j] == insert) {
                        ops[i][j] = "+" + two.charAt(j - 1) + ",";
                    } else {
                        ops[i][j] = "-" + one.charAt(i - 1) + ",";
                    }
                }
            }
        }

        // Восстановление редакционного предписания
        StringBuilder result = new StringBuilder();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            result.insert(0, ops[i][j]);
            if (ops[i][j].startsWith("#") || ops[i][j].startsWith("~")) {
                i--; j--;
            } else if (ops[i][j].startsWith("+")) {
                j--;
            } else {
                i--;
            }
        }

        return result.toString();
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
