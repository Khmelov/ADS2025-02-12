package by.it.group410902.sivtsov.lesson07;

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
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];

        // Заполнение таблицы расстояний
        for (int i = 0; i <= m; i++)
        {
            dp[i][0] = i; // Если вторая строка пустая, удаляем все символы из первой строки
        }
        for (int j = 0; j <= n; j++)
        {
            dp[0][j] = j; // Если первая строка пустая, добавляем все символы второй строки
        }

        for (int i = 1; i <= m; i++)
        {
            for (int j = 1; j <= n; j++)
            {
                // Определяем стоимость замены (0 — если символы совпадают, 1 — если отличаются)
                int replaceCost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                // Выбираем минимальное количество операций:
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // удаление
                                dp[i][j - 1] + 1), // вставка
                        dp[i - 1][j - 1] + replaceCost); // замена
            }
        }

        // Восстановление редакционного предписания
        StringBuilder result = new StringBuilder();
        int i = m, j = n;
        // Проходим по таблице dp в обратном порядке, формируя список операций редактирования
        while (i > 0 || j > 0) {
            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                // Удаление символа из первой строки
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                // Вставка символа из второй строки
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            } else {
                // Совпадение или замена символа
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    result.insert(0, "#,");// Символы совпадают → копирование
                } else {
                    result.insert(0, "~" + two.charAt(j - 1) + ",");// Символы отличаются → замена
                }
                i--;
                j--;
            }
        }

        return result.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

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