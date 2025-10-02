package by.it.group451001.romeyko.lesson07;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        String result = "";
        int n = one.length(), m = two.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= m; i++) {
            dp[0][i] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[j][0] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diff = 1;
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    diff = 0;
                }
                dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + diff));
            }
        }
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result = "+" + two.charAt(j - 1) + "," + result;
                --j;
                continue;
            }
            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result = "-" + one.charAt(i - 1) + "," + result;
                --i;
                continue;
            }
            if (j > 0 && i > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                String temp = "~" + two.charAt(j - 1) + ",";
                result = temp + result;
                --i;
                --j;
            }
            if (j > 0 && i > 0 && dp[i][j] == dp[i - 1][j - 1]) {
                --i;
                --j;
                String temp = "#,";
                result = temp + result;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}