package by.it.group451003.kuzhik.lesson07;

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
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        char[][] ops = new char[n + 1][m + 1]; // таблица для восстановления операций

        // Инициализация базовых случаев
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            if (i > 0) ops[i][0] = '-';
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            if (j > 0) ops[0][j] = '+';
        }

        // Заполнение таблицы DP и операций
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int costReplace = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                int del = dp[i - 1][j] + 1;
                int ins = dp[i][j - 1] + 1;
                int rep = dp[i - 1][j - 1] + costReplace;

                dp[i][j] = Math.min(Math.min(del, ins), rep);

                if (dp[i][j] == rep) {
                    ops[i][j] = (costReplace == 0) ? '#' : '~';
                } else if (dp[i][j] == del) {
                    ops[i][j] = '-';
                } else {
                    ops[i][j] = '+';
                }
            }
        }

        // Восстановление предписания (идем с конца к началу)
        StringBuilder editScript = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            char op = ops[i][j];
            switch (op) {
                case '#':
                    editScript.insert(0, "#,");
                    i--;
                    j--;
                    break;
                case '~':
                    editScript.insert(0, "~" + two.charAt(j - 1) + ",");
                    i--;
                    j--;
                    break;
                case '-':
                    editScript.insert(0, "-" + one.charAt(i - 1) + ",");
                    i--;
                    break;
                case '+':
                    editScript.insert(0, "+" + two.charAt(j - 1) + ",");
                    j--;
                    break;
            }
        }

        return editScript.toString();
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
