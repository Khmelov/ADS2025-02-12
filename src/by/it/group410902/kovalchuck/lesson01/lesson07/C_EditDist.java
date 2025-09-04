package by.it.group410902.kovalchuck.lesson01.lesson07;

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
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];
        String[][] editOps = new String[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                    editOps[i][j] = j > 0 ? "+" + two.charAt(j - 1) + "," : "";
                } else if (j == 0) {
                    dp[i][j] = i;
                    editOps[i][j] = i > 0 ? "-" + one.charAt(i - 1) + "," : "";
                } else {
                    int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int replace = dp[i - 1][j - 1] + cost;

                    dp[i][j] = Math.min(delete, Math.min(insert, replace));

                    if (dp[i][j] == replace) {
                        editOps[i][j] = editOps[i - 1][j - 1] + (cost == 0 ? "#," : "~" + two.charAt(j - 1) + ",");
                    } else if (dp[i][j] == insert) {
                        editOps[i][j] = editOps[i][j - 1] + "+" + two.charAt(j - 1) + ",";
                    } else {
                        editOps[i][j] = editOps[i - 1][j] + "-" + one.charAt(i - 1) + ",";
                    }
                }
            }
        }

        result = editOps[m][n];
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