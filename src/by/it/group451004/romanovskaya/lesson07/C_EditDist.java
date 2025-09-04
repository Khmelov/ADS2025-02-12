package by.it.group451004.romanovskaya.lesson07;

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

            int[][] dp = new int[m+1][n+1];
            String[][] operations = new String[m+1][n+1];

            // Инициализация базовых случаев
            for (int i = 0; i <= m; i++) {
                dp[i][0] = i;
                if (i > 0) {
                    operations[i][0] = operations[i-1][0] + "-" + one.charAt(i-1) + ",";
                } else {
                    operations[i][0] = "";
                }
            }
            for (int j = 0; j <= n; j++) {
                dp[0][j] = j;
                if (j > 0) {
                    operations[0][j] = operations[0][j-1] + "+" + two.charAt(j-1) + ",";
                } else {
                    operations[0][j] = "";
                }
            }

            // Заполнение матрицы
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (one.charAt(i-1) == two.charAt(j-1)) {
                        dp[i][j] = dp[i-1][j-1];
                        operations[i][j] = operations[i-1][j-1] + "#,";
                    } else {
                        int delete = dp[i-1][j];
                        int insert = dp[i][j-1];
                        int replace = dp[i-1][j-1];

                        if (delete <= insert && delete <= replace) {
                            dp[i][j] = delete + 1;
                            operations[i][j] = operations[i-1][j] + "-" + one.charAt(i-1) + ",";
                        } else if (insert <= delete && insert <= replace) {
                            dp[i][j] = insert + 1;
                            operations[i][j] = operations[i][j-1] + "+" + two.charAt(j-1) + ",";
                        } else {
                            dp[i][j] = replace + 1;
                            operations[i][j] = operations[i-1][j-1] + "~" + two.charAt(j-1) + ",";
                        }
                    }
                }
            }

            return operations[m][n];
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