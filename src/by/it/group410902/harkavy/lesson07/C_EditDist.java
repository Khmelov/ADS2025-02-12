package by.it.group410902.harkavy.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.*;

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

    /**
     * Возвращает через запятую (с завершающей запятой) редакционное предписание
     * для превращения строки one в строку two.
     * Операции:
     *   "#"     — совпадение (копирование);
     *   "~x"    — замена на символ x;
     *   "-x"    — удаление символа x из one;
     *   "+x"    — вставка символа x из two.
     */
    String getDistanceEdinting(String one, String two) {
        int n = one.length(), m = two.length();
        // dp[i][j] = минимальное число операций для превращения one[0..i) в two[0..j)
        int[][] dp = new int[n+1][m+1];
        for (int i = 1; i <= n; i++) dp[i][0] = i;
        for (int j = 1; j <= m; j++) dp[0][j] = j;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = one.charAt(i-1) == two.charAt(j-1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i-1][j] + 1,      // удаление
                                dp[i][j-1] + 1),     // вставка
                        dp[i-1][j-1] + cost           // замена/совпадение
                );
            }
        }
        // восстановление пути
        List<String> ops = new ArrayList<>();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                int cost = one.charAt(i-1) == two.charAt(j-1) ? 0 : 1;
                if (dp[i][j] == dp[i-1][j-1] + cost) {
                    if (cost == 0) ops.add("#");
                    else           ops.add("~" + two.charAt(j-1));
                    i--; j--;
                    continue;
                }
            }
            if (i > 0 && dp[i][j] == dp[i-1][j] + 1) {
                ops.add("-" + one.charAt(i-1));
                i--;
            } else {
                ops.add("+" + two.charAt(j-1));
                j--;
            }
        }
        Collections.reverse(ops);
        StringBuilder sb = new StringBuilder();
        for (String op : ops) {
            sb.append(op).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class
                .getResourceAsStream("dataABC.txt");
        Scanner scanner = new Scanner(stream);
        C_EditDist instance = new C_EditDist();
        // читаем три пары строк
        for (int t = 0; t < 3; t++) {
            String one = scanner.nextLine();
            String two = scanner.nextLine();
            System.out.println(instance.getDistanceEdinting(one, two));
        }
    }
}