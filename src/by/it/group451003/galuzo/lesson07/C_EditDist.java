package by.it.group451003.galuzo.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
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
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];
        // инициализация
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        // заполнение таблицы расстояний
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,    // удаление
                                dp[i][j - 1] + 1),   // вставка
                        dp[i - 1][j - 1] + cost       // замена или совпадение
                );
            }
        }
        // восстановление операций
        int i = m, j = n;
        LinkedList<String> ops = new LinkedList<>();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1]
                    && one.charAt(i - 1) == two.charAt(j - 1)) {
                ops.addFirst("#");
                i--; j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.addFirst("~" + two.charAt(j - 1));
                i--; j--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.addFirst("+" + two.charAt(j - 1));
                j--;
            } else { // удаление
                ops.addFirst("-" + one.charAt(i - 1));
                i--;
            }
        }
        // формируем строку с запятыми
        StringBuilder sb = new StringBuilder();
        for (String op : ops) {
            sb.append(op).append(",");
        }
        return sb.toString();
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
