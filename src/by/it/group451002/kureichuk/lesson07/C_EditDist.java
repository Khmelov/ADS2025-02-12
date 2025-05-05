package by.it.group451002.kureichuk.lesson07;

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
        int[][] dp = new int[one.length() + 1][two.length() + 1];
        String[][] dpStr = new String[one.length() + 1][two.length() + 1];
        dpStr[0][0] = "";
        for (int i = 1; i <= one.length(); i++) {
            dp[i][0] = i;
            dpStr[i][0] = "-" + one.charAt(i - 1) + ',';
        }
        for (int i = 1; i <= two.length(); i++) {
            dp[0][i] = i;
            dpStr[0][i] = "+" + two.charAt(i - 1) + ',';
        }

        for (int i = 1; i <= one.length(); i++) {
            for (int j = 1; j <= two.length(); j++) {
                int eq = (two.charAt(j - 1) == one.charAt(i - 1) ? 0 : 1);

                int delete = dp[i - 1][j] + 1;
                int insert = dp[i][j - 1] + 1;
                int equal =  dp[i - 1][j - 1] + eq;
                dp[i][j] = Math.min(Math.min(delete, insert),equal);

                if (eq == 0 && dp[i][j] == equal) {
                    dpStr[i][j] = dpStr[i - 1][j - 1] + "#" + ',';
                } else if (eq != 0 && dp[i][j] == equal) {
                    dpStr[i][j] = dpStr[i - 1][j - 1] + "~" + two.charAt(j - 1) + ',';
                } else if (dp[i][j] == insert) {
                    dpStr[i][j] = dpStr[i][j - 1] + "+" + two.charAt(j - 1) + ',';
                } else {
                    dpStr[i][j] = dpStr[i - 1][j] + "-" + one.charAt(j - 1) + ',';
                }
            }
        }
        return dpStr[one.length()][two.length()];
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