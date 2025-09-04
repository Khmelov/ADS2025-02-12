package by.it.group451002.kravtsov.lesson07;

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

        // Создаём таблицу dp для хранения расстояний и массив операций
        int[][] dp = new int[n + 1][m + 1];
        String[][] operations = new String[n + 1][m + 1];

        // Заполняем базовые случаи
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            operations[i][0] = "-"+one.substring(0, i); // Удаление всех символов
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            operations[0][j] = "+"+two.substring(0, j); // Вставка всех символов
            // Показываем какие именно операции нужно выполнить, чтобы превратить одну строку в другую.
        }

        // Заполняем таблицу динамическим программированием
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    operations[i][j] = "#";
                } else {
                    int delete = dp[i - 1][j] + 1; // Удаление
                    int insert = dp[i][j - 1] + 1; // Вставка
                    int replace = dp[i - 1][j - 1] + 1; // Замена

                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete;
                        operations[i][j] = "-" + one.charAt(i - 1);
                    } else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert;
                        operations[i][j] = "+" + two.charAt(j - 1);
                    } else {
                        dp[i][j] = replace;
                        operations[i][j] = "~" + two.charAt(j - 1);
                    }
                }
            }
        }

        // Восстанавливаем редакционное предписание
        StringBuilder result = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            result.insert(0, operations[i][j] + ",");
            if (operations[i][j].startsWith("-")) {
                i--; // Удаление символа
            } else if (operations[i][j].startsWith("+")) {
                j--; // Вставка символа
            } else {
                i--; j--; // Замена или совпадение
            }
        }

        return result.toString();
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
