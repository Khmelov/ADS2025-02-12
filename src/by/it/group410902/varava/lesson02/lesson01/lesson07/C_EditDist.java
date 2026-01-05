package by.it.group410902.varava.lesson02.lesson01.lesson07;

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
    Sample Output 3:
    +e,#,#,-s,#,~i,#,-c,~g,
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        int[][] dp = new int[n + 1][m + 1];
        int[][] prev = new int[n + 1][m + 1]; // Храним предыдущее действие

        // Заполнение базовых случаев
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            prev[i][0] = 1; // Удаление
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            prev[0][j] = 2; // Вставка
        }

        // Заполнение основной таблицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                int del = dp[i - 1][j] + 1;
                int ins = dp[i][j - 1] + 1;
                int rep = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(del, Math.min(ins, rep));

                // Запоминаем, какое действие привело к текущей ячейке
                if (dp[i][j] == del) prev[i][j] = 1; // Удаление
                else if (dp[i][j] == ins) prev[i][j] = 2; // Вставка
                else prev[i][j] = (cost == 0) ? 4 : 3; // 4 - копирование, 3 - замена
            }
        }

        // Восстановление редакционных предписаний
        StringBuilder result = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (prev[i][j] == 1) {
                result.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else if (prev[i][j] == 2) {
                result.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            } else if (prev[i][j] == 3) {
                result.append("~").append(two.charAt(j - 1)).append(",");
                i--;
                j--;
            } else {
                result.append(",#");
                i--;
                j--;
            }
        }

        return result.reverse().toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        if (stream == null) {
            throw new FileNotFoundException("Файл dataABC.txt не найден");
        }

        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
