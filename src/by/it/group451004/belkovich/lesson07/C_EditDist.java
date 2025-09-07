package by.it.group451004.belkovich.lesson07;

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

    String getDistanceEditing(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                d[i][j] = Math.min(
                        Math.min(
                                d[i - 1][j] + 1,     // удаление
                                d[i][j - 1] + 1      // вставка
                        ),
                        d[i - 1][j - 1] + cost  // замена или копирование
                );
            }
        }

        StringBuilder result = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && d[i][j] == d[i - 1][j - 1] + ((one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1)) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    result.insert(0, "#,");  // совпадение
                } else {
                    result.insert(0, "~" + two.charAt(j - 1) + ",");  // замена
                }
                i--;
                j--;
            } else if (i > 0 && d[i][j] == d[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");  // удаление
                i--;
            } else if (j > 0 && d[i][j] == d[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");  // вставка
                j--;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEditing(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(),scanner.nextLine()));
    }
}